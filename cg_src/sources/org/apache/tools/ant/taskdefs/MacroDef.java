package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroDef.class */
public class MacroDef extends AntlibDefinition {
    private NestedSequential nestedSequential;
    private String name;
    private boolean backTrace = true;
    private List<Attribute> attributes = new ArrayList();
    private Map<String, TemplateElement> elements = new HashMap();
    private String textName = null;
    private Text text = null;
    private boolean hasImplicitElement = false;

    public void setName(String name) {
        this.name = name;
    }

    public void addConfiguredText(Text text) {
        if (this.text != null) {
            throw new BuildException("Only one nested text element allowed");
        }
        if (text.getName() == null) {
            throw new BuildException("the text nested element needed a \"name\" attribute");
        }
        for (Attribute attribute : this.attributes) {
            if (text.getName().equals(attribute.getName())) {
                throw new BuildException("the name \"%s\" is already used as an attribute", text.getName());
            }
        }
        this.text = text;
        this.textName = text.getName();
    }

    public Text getText() {
        return this.text;
    }

    public void setBackTrace(boolean backTrace) {
        this.backTrace = backTrace;
    }

    public boolean getBackTrace() {
        return this.backTrace;
    }

    public NestedSequential createSequential() {
        if (this.nestedSequential != null) {
            throw new BuildException("Only one sequential allowed");
        }
        this.nestedSequential = new NestedSequential();
        return this.nestedSequential;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroDef$NestedSequential.class */
    public static class NestedSequential implements TaskContainer {
        private List<Task> nested = new ArrayList();

        @Override // org.apache.tools.ant.TaskContainer
        public void addTask(Task task) {
            this.nested.add(task);
        }

        public List<Task> getNested() {
            return this.nested;
        }

        public boolean similar(NestedSequential other) {
            int size = this.nested.size();
            if (size != other.nested.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                UnknownElement me = (UnknownElement) this.nested.get(i);
                UnknownElement o = (UnknownElement) other.nested.get(i);
                if (!me.similar(o)) {
                    return false;
                }
            }
            return true;
        }
    }

    public UnknownElement getNestedTask() {
        UnknownElement ret = new UnknownElement("sequential");
        ret.setTaskName("sequential");
        ret.setNamespace("");
        ret.setQName("sequential");
        new RuntimeConfigurable(ret, "sequential");
        int size = this.nestedSequential.getNested().size();
        for (int i = 0; i < size; i++) {
            UnknownElement e = (UnknownElement) this.nestedSequential.getNested().get(i);
            ret.addChild(e);
            ret.getWrapper().addChild(e.getWrapper());
        }
        return ret;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public Map<String, TemplateElement> getElements() {
        return this.elements;
    }

    public static boolean isValidNameCharacter(char c) {
        return Character.isLetterOrDigit(c) || c == '.' || c == '-';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (!isValidNameCharacter(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void addConfiguredAttribute(Attribute attribute) {
        if (attribute.getName() == null) {
            throw new BuildException("the attribute nested element needed a \"name\" attribute");
        }
        if (attribute.getName().equals(this.textName)) {
            throw new BuildException("the name \"%s\" has already been used by the text element", attribute.getName());
        }
        for (Attribute att : this.attributes) {
            if (att.getName().equals(attribute.getName())) {
                throw new BuildException("the name \"%s\" has already been used in another attribute element", attribute.getName());
            }
        }
        this.attributes.add(attribute);
    }

    public void addConfiguredElement(TemplateElement element) {
        if (element.getName() == null) {
            throw new BuildException("the element nested element needed a \"name\" attribute");
        }
        if (this.elements.get(element.getName()) != null) {
            throw new BuildException("the element %s has already been specified", element.getName());
        }
        if (this.hasImplicitElement || (element.isImplicit() && !this.elements.isEmpty())) {
            throw new BuildException("Only one element allowed when using implicit elements");
        }
        this.hasImplicitElement = element.isImplicit();
        this.elements.put(element.getName(), element);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.nestedSequential == null) {
            throw new BuildException("Missing sequential element");
        }
        if (this.name == null) {
            throw new BuildException("Name not specified");
        }
        this.name = ProjectHelper.genComponentName(getURI(), this.name);
        MyAntTypeDefinition def = new MyAntTypeDefinition(this);
        def.setName(this.name);
        def.setClass(MacroInstance.class);
        ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
        helper.addDataTypeDefinition(def);
        log("creating macro  " + this.name, 3);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroDef$Attribute.class */
    public static class Attribute {
        private String name;
        private String defaultValue;
        private String description;
        private boolean doubleExpanding = true;

        public void setName(String name) {
            if (!MacroDef.isValidName(name)) {
                throw new BuildException("Illegal name [%s] for attribute", name);
            }
            this.name = name.toLowerCase(Locale.ENGLISH);
        }

        public String getName() {
            return this.name;
        }

        public void setDefault(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getDefault() {
            return this.defaultValue;
        }

        public void setDescription(String desc) {
            this.description = desc;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDoubleExpanding(boolean doubleExpanding) {
            this.doubleExpanding = doubleExpanding;
        }

        public boolean isDoubleExpanding() {
            return this.doubleExpanding;
        }

        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Attribute other = (Attribute) obj;
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!this.name.equals(other.name)) {
                return false;
            }
            if (this.defaultValue == null) {
                return other.defaultValue == null;
            }
            return this.defaultValue.equals(other.defaultValue);
        }

        public int hashCode() {
            return Objects.hashCode(this.defaultValue) + Objects.hashCode(this.name);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroDef$Text.class */
    public static class Text {
        private String name;
        private boolean optional;
        private boolean trim;
        private String description;
        private String defaultString;

        public void setName(String name) {
            if (!MacroDef.isValidName(name)) {
                throw new BuildException("Illegal name [%s] for element", name);
            }
            this.name = name.toLowerCase(Locale.ENGLISH);
        }

        public String getName() {
            return this.name;
        }

        public void setOptional(boolean optional) {
            this.optional = optional;
        }

        public boolean getOptional() {
            return this.optional;
        }

        public void setTrim(boolean trim) {
            this.trim = trim;
        }

        public boolean getTrim() {
            return this.trim;
        }

        public void setDescription(String desc) {
            this.description = desc;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDefault(String defaultString) {
            this.defaultString = defaultString;
        }

        public String getDefault() {
            return this.defaultString;
        }

        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Text other = (Text) obj;
            return Objects.equals(this.name, other.name) && this.optional == other.optional && this.trim == other.trim && Objects.equals(this.defaultString, other.defaultString);
        }

        public int hashCode() {
            return Objects.hashCode(this.name);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroDef$TemplateElement.class */
    public static class TemplateElement {
        private String name;
        private String description;
        private boolean optional = false;
        private boolean implicit = false;

        public void setName(String name) {
            if (!MacroDef.isValidName(name)) {
                throw new BuildException("Illegal name [%s] for macro element", name);
            }
            this.name = name.toLowerCase(Locale.ENGLISH);
        }

        public String getName() {
            return this.name;
        }

        public void setDescription(String desc) {
            this.description = desc;
        }

        public String getDescription() {
            return this.description;
        }

        public void setOptional(boolean optional) {
            this.optional = optional;
        }

        public boolean isOptional() {
            return this.optional;
        }

        public void setImplicit(boolean implicit) {
            this.implicit = implicit;
        }

        public boolean isImplicit() {
            return this.implicit;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !obj.getClass().equals(getClass())) {
                return false;
            }
            TemplateElement t = (TemplateElement) obj;
            if (this.name != null ? this.name.equals(t.name) : t.name == null) {
                if (this.optional == t.optional && this.implicit == t.implicit) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.name) + (this.optional ? 1 : 0) + (this.implicit ? 1 : 0);
        }
    }

    private boolean sameOrSimilar(Object obj, boolean same) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MacroDef other = (MacroDef) obj;
        if (this.name == null) {
            return other.name == null;
        } else if (!this.name.equals(other.name)) {
            return false;
        } else {
            if (other.getLocation() != null && other.getLocation().equals(getLocation()) && !same) {
                return true;
            }
            if (this.text == null) {
                if (other.text != null) {
                    return false;
                }
            } else if (!this.text.equals(other.text)) {
                return false;
            }
            if (getURI() == null || getURI().isEmpty() || getURI().equals(ProjectHelper.ANT_CORE_URI)) {
                if (other.getURI() != null && !other.getURI().isEmpty() && !other.getURI().equals(ProjectHelper.ANT_CORE_URI)) {
                    return false;
                }
            } else if (!getURI().equals(other.getURI())) {
                return false;
            }
            return this.nestedSequential.similar(other.nestedSequential) && this.attributes.equals(other.attributes) && this.elements.equals(other.elements);
        }
    }

    public boolean similar(Object obj) {
        return sameOrSimilar(obj, false);
    }

    public boolean sameDefinition(Object obj) {
        return sameOrSimilar(obj, true);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroDef$MyAntTypeDefinition.class */
    private static class MyAntTypeDefinition extends AntTypeDefinition {
        private MacroDef macroDef;

        public MyAntTypeDefinition(MacroDef macroDef) {
            this.macroDef = macroDef;
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public Object create(Project project) {
            Object o = super.create(project);
            if (o == null) {
                return null;
            }
            ((MacroInstance) o).setMacroDef(this.macroDef);
            return o;
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public boolean sameDefinition(AntTypeDefinition other, Project project) {
            if (!super.sameDefinition(other, project)) {
                return false;
            }
            MyAntTypeDefinition otherDef = (MyAntTypeDefinition) other;
            return this.macroDef.sameDefinition(otherDef.macroDef);
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public boolean similarDefinition(AntTypeDefinition other, Project project) {
            if (!super.similarDefinition(other, project)) {
                return false;
            }
            MyAntTypeDefinition otherDef = (MyAntTypeDefinition) other;
            return this.macroDef.similar(otherDef.macroDef);
        }
    }
}
