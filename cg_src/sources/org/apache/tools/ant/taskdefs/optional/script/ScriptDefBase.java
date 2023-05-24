package org.apache.tools.ant.taskdefs.optional.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/script/ScriptDefBase.class */
public class ScriptDefBase extends Task implements DynamicConfigurator {
    private Map<String, List<Object>> nestedElementMap = new HashMap();
    private Map<String, String> attributes = new HashMap();
    private String text;

    @Override // org.apache.tools.ant.Task
    public void execute() {
        getScript().executeScript(this.attributes, this.nestedElementMap, this);
    }

    private ScriptDef getScript() {
        String name = getTaskType();
        Map<String, ScriptDef> scriptRepository = (Map) getProject().getReference(MagicNames.SCRIPT_REPOSITORY);
        if (scriptRepository == null) {
            throw new BuildException("Script repository not found for " + name);
        }
        ScriptDef definition = scriptRepository.get(getTaskType());
        if (definition == null) {
            throw new BuildException("Script definition not found for " + name);
        }
        return definition;
    }

    @Override // org.apache.tools.ant.DynamicElement
    public Object createDynamicElement(String name) {
        List<Object> nestedElementList = this.nestedElementMap.computeIfAbsent(name, k -> {
            return new ArrayList();
        });
        Object element = getScript().createNestedElement(name);
        nestedElementList.add(element);
        return element;
    }

    @Override // org.apache.tools.ant.DynamicAttribute
    public void setDynamicAttribute(String name, String value) {
        ScriptDef definition = getScript();
        if (!definition.isAttributeSupported(name)) {
            throw new BuildException("<%s> does not support the \"%s\" attribute", getTaskType(), name);
        }
        this.attributes.put(name, value);
    }

    public void addText(String text) {
        this.text = getProject().replaceProperties(text);
    }

    public String getText() {
        return this.text;
    }

    public void fail(String message) {
        throw new BuildException(message);
    }
}
