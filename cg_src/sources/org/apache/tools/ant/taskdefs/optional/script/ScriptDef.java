package org.apache.tools.ant.taskdefs.optional.script;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.DefBase;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.ScriptRunnerBase;
import org.apache.tools.ant.util.ScriptRunnerHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/script/ScriptDef.class */
public class ScriptDef extends DefBase {
    private String name;
    private Set<String> attributeSet;
    private Map<String, NestedElement> nestedElementMap;
    private ScriptRunnerHelper helper = new ScriptRunnerHelper();
    private List<Attribute> attributes = new ArrayList();
    private List<NestedElement> nestedElements = new ArrayList();

    @Override // org.apache.tools.ant.ProjectComponent
    public void setProject(Project project) {
        super.setProject(project);
        this.helper.setProjectComponent(this);
        this.helper.setSetBeans(false);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttributeSupported(String attributeName) {
        return this.attributeSet.contains(attributeName);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/script/ScriptDef$Attribute.class */
    public static class Attribute {
        private String name;

        public void setName(String name) {
            this.name = name.toLowerCase(Locale.ENGLISH);
        }
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/script/ScriptDef$NestedElement.class */
    public static class NestedElement {
        private String name;
        private String type;
        private String className;

        public void setName(String name) {
            this.name = name.toLowerCase(Locale.ENGLISH);
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    public void addElement(NestedElement nestedElement) {
        this.nestedElements.add(nestedElement);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.name == null) {
            throw new BuildException("scriptdef requires a name attribute to name the script");
        }
        if (this.helper.getLanguage() == null) {
            throw new BuildException("scriptdef requires a language attribute to specify the script language");
        }
        if (this.helper.getSrc() == null && this.helper.getEncoding() != null) {
            throw new BuildException("scriptdef requires a src attribute if the encoding is set");
        }
        if (getAntlibClassLoader() != null || hasCpDelegate()) {
            this.helper.setClassLoader(createLoader());
        }
        this.attributeSet = new HashSet();
        for (Attribute attribute : this.attributes) {
            if (attribute.name != null) {
                if (!this.attributeSet.contains(attribute.name)) {
                    this.attributeSet.add(attribute.name);
                } else {
                    throw new BuildException("scriptdef <%s> declares the %s attribute more than once", this.name, attribute.name);
                }
            } else {
                throw new BuildException("scriptdef <attribute> elements must specify an attribute name");
            }
        }
        this.nestedElementMap = new HashMap();
        for (NestedElement nestedElement : this.nestedElements) {
            if (nestedElement.name != null) {
                if (!this.nestedElementMap.containsKey(nestedElement.name)) {
                    if (nestedElement.className != null || nestedElement.type != null) {
                        if (nestedElement.className == null || nestedElement.type == null) {
                            this.nestedElementMap.put(nestedElement.name, nestedElement);
                        } else {
                            throw new BuildException("scriptdef <element> elements must specify only one of the classname and type attributes");
                        }
                    } else {
                        throw new BuildException("scriptdef <element> elements must specify either a classname or type attribute");
                    }
                } else {
                    throw new BuildException("scriptdef <%s> declares the %s nested element more than once", this.name, nestedElement.name);
                }
            } else {
                throw new BuildException("scriptdef <element> elements must specify an element name");
            }
        }
        Map<String, ScriptDef> scriptRepository = lookupScriptRepository();
        this.name = ProjectHelper.genComponentName(getURI(), this.name);
        scriptRepository.put(this.name, this);
        AntTypeDefinition def = new AntTypeDefinition();
        def.setName(this.name);
        def.setClass(ScriptDefBase.class);
        ComponentHelper.getComponentHelper(getProject()).addDataTypeDefinition(def);
    }

    private Map<String, ScriptDef> lookupScriptRepository() {
        Map<String, ScriptDef> scriptRepository;
        Project p = getProject();
        synchronized (p) {
            scriptRepository = (Map) p.getReference(MagicNames.SCRIPT_REPOSITORY);
            if (scriptRepository == null) {
                scriptRepository = new HashMap<>();
                p.addReference(MagicNames.SCRIPT_REPOSITORY, scriptRepository);
            }
        }
        return scriptRepository;
    }

    public Object createNestedElement(String elementName) {
        Object instance;
        NestedElement definition = this.nestedElementMap.get(elementName);
        if (definition != null) {
            String classname = definition.className;
            if (classname == null) {
                instance = getProject().createTask(definition.type);
                if (instance == null) {
                    instance = getProject().createDataType(definition.type);
                }
            } else {
                ClassLoader loader = createLoader();
                try {
                    instance = ClasspathUtils.newInstance(classname, loader);
                } catch (BuildException e) {
                    instance = ClasspathUtils.newInstance(classname, ScriptDef.class.getClassLoader());
                }
                getProject().setProjectReference(instance);
            }
            if (instance == null) {
                throw new BuildException("<%s> is unable to create the <%s> nested element", this.name, elementName);
            }
            return instance;
        }
        throw new BuildException("<%s> does not support the <%s> nested element", this.name, elementName);
    }

    @Deprecated
    public void executeScript(Map<String, String> attributes, Map<String, List<Object>> elements) {
        executeScript(attributes, elements, null);
    }

    public void executeScript(Map<String, String> attributes, Map<String, List<Object>> elements, ScriptDefBase instance) {
        ScriptRunnerBase runner = this.helper.getScriptRunner();
        runner.addBean("attributes", attributes);
        runner.addBean("elements", elements);
        runner.addBean("project", getProject());
        if (instance != null) {
            runner.addBean("self", instance);
        }
        runner.executeScript("scriptdef_" + this.name);
    }

    public void setManager(String manager) {
        this.helper.setManager(manager);
    }

    public void setLanguage(String language) {
        this.helper.setLanguage(language);
    }

    public void setCompiled(boolean compiled) {
        this.helper.setCompiled(compiled);
    }

    public void setSrc(File file) {
        this.helper.setSrc(file);
    }

    public void setEncoding(String encoding) {
        this.helper.setEncoding(encoding);
    }

    public void addText(String text) {
        this.helper.addText(text);
    }

    public void add(ResourceCollection resource) {
        this.helper.add(resource);
    }
}
