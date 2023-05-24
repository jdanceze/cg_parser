package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicAttribute;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.property.LocalProperties;
import org.apache.tools.ant.taskdefs.MacroDef;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroInstance.class */
public class MacroInstance extends Task implements DynamicAttribute, TaskContainer {
    private MacroDef macroDef;
    private Map<String, UnknownElement> presentElements;
    private Map<String, String> localAttributes;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_EXPECT_BRACKET = 1;
    private static final int STATE_EXPECT_NAME = 2;
    private Map<String, String> map = new HashMap();
    private Map<String, MacroDef.TemplateElement> nsElements = null;
    private String text = null;
    private String implicitTag = null;
    private List<Task> unknownElements = new ArrayList();

    public void setMacroDef(MacroDef macroDef) {
        this.macroDef = macroDef;
    }

    public MacroDef getMacroDef() {
        return this.macroDef;
    }

    @Override // org.apache.tools.ant.DynamicAttribute
    public void setDynamicAttribute(String name, String value) {
        this.map.put(name.toLowerCase(Locale.ENGLISH), value);
    }

    @Deprecated
    public Object createDynamicElement(String name) throws BuildException {
        throw new BuildException("Not implemented any more");
    }

    private Map<String, MacroDef.TemplateElement> getNsElements() {
        if (this.nsElements == null) {
            this.nsElements = new HashMap();
            for (Map.Entry<String, MacroDef.TemplateElement> entry : this.macroDef.getElements().entrySet()) {
                this.nsElements.put(entry.getKey(), entry.getValue());
                MacroDef.TemplateElement te = entry.getValue();
                if (te.isImplicit()) {
                    this.implicitTag = te.getName();
                }
            }
        }
        return this.nsElements;
    }

    @Override // org.apache.tools.ant.TaskContainer
    public void addTask(Task nestedTask) {
        this.unknownElements.add(nestedTask);
    }

    private void processTasks() {
        if (this.implicitTag != null) {
            return;
        }
        for (Task task : this.unknownElements) {
            UnknownElement ue = (UnknownElement) task;
            String name = ProjectHelper.extractNameFromComponentName(ue.getTag()).toLowerCase(Locale.ENGLISH);
            if (getNsElements().get(name) == null) {
                throw new BuildException("unsupported element %s", name);
            }
            if (this.presentElements.get(name) != null) {
                throw new BuildException("Element %s already present", name);
            }
            this.presentElements.put(name, ue);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/MacroInstance$Element.class */
    public static class Element implements TaskContainer {
        private List<Task> unknownElements = new ArrayList();

        @Override // org.apache.tools.ant.TaskContainer
        public void addTask(Task nestedTask) {
            this.unknownElements.add(nestedTask);
        }

        public List<Task> getUnknownElements() {
            return this.unknownElements;
        }
    }

    private String macroSubs(String s, Map<String, String> macroMapping) {
        char[] charArray;
        if (s == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        StringBuilder macroName = null;
        int state = 0;
        for (char ch : s.toCharArray()) {
            switch (state) {
                case 0:
                    if (ch == '@') {
                        state = 1;
                        break;
                    } else {
                        ret.append(ch);
                        break;
                    }
                case 1:
                    if (ch == '{') {
                        state = 2;
                        macroName = new StringBuilder();
                        break;
                    } else if (ch == '@') {
                        state = 0;
                        ret.append('@');
                        break;
                    } else {
                        state = 0;
                        ret.append('@');
                        ret.append(ch);
                        break;
                    }
                case 2:
                    if (ch == '}') {
                        state = 0;
                        String name = macroName.toString().toLowerCase(Locale.ENGLISH);
                        String value = macroMapping.get(name);
                        if (value == null) {
                            ret.append("@{");
                            ret.append(name);
                            ret.append("}");
                        } else {
                            ret.append(value);
                        }
                        macroName = null;
                        break;
                    } else {
                        macroName.append(ch);
                        break;
                    }
            }
        }
        switch (state) {
            case 1:
                ret.append('@');
                break;
            case 2:
                ret.append("@{");
                ret.append(macroName.toString());
                break;
        }
        return ret.toString();
    }

    public void addText(String text) {
        this.text = text;
    }

    private UnknownElement copy(UnknownElement ue, boolean nested) {
        UnknownElement ret = new UnknownElement(ue.getTag());
        ret.setNamespace(ue.getNamespace());
        ret.setProject(getProject());
        ret.setQName(ue.getQName());
        ret.setTaskType(ue.getTaskType());
        ret.setTaskName(ue.getTaskName());
        ret.setLocation(this.macroDef.getBackTrace() ? ue.getLocation() : getLocation());
        if (getOwningTarget() == null) {
            Target t = new Target();
            t.setProject(getProject());
            ret.setOwningTarget(t);
        } else {
            ret.setOwningTarget(getOwningTarget());
        }
        RuntimeConfigurable rc = new RuntimeConfigurable(ret, ue.getTaskName());
        rc.setPolyType(ue.getWrapper().getPolyType());
        Map<String, Object> m = ue.getWrapper().getAttributeMap();
        for (Map.Entry<String, Object> entry : m.entrySet()) {
            rc.setAttribute(entry.getKey(), macroSubs((String) entry.getValue(), this.localAttributes));
        }
        rc.addText(macroSubs(ue.getWrapper().getText().toString(), this.localAttributes));
        Iterator it = Collections.list(ue.getWrapper().getChildren()).iterator();
        while (it.hasNext()) {
            RuntimeConfigurable r = (RuntimeConfigurable) it.next();
            UnknownElement unknownElement = (UnknownElement) r.getProxy();
            String tag = unknownElement.getTaskType();
            if (tag != null) {
                tag = tag.toLowerCase(Locale.ENGLISH);
            }
            MacroDef.TemplateElement templateElement = getNsElements().get(tag);
            if (templateElement == null || nested) {
                UnknownElement child = copy(unknownElement, nested);
                rc.addChild(child.getWrapper());
                ret.addChild(child);
            } else if (templateElement.isImplicit()) {
                if (this.unknownElements.isEmpty() && !templateElement.isOptional()) {
                    throw new BuildException("Missing nested elements for implicit element %s", templateElement.getName());
                }
                for (Task task : this.unknownElements) {
                    UnknownElement child2 = copy((UnknownElement) task, true);
                    rc.addChild(child2.getWrapper());
                    ret.addChild(child2);
                }
            } else {
                UnknownElement presentElement = this.presentElements.get(tag);
                if (presentElement == null) {
                    if (!templateElement.isOptional()) {
                        throw new BuildException("Required nested element %s missing", templateElement.getName());
                    }
                } else {
                    String presentText = presentElement.getWrapper().getText().toString();
                    if (!presentText.isEmpty()) {
                        rc.addText(macroSubs(presentText, this.localAttributes));
                    }
                    List<UnknownElement> list = presentElement.getChildren();
                    if (list != null) {
                        for (UnknownElement unknownElement2 : list) {
                            UnknownElement child3 = copy(unknownElement2, true);
                            rc.addChild(child3.getWrapper());
                            ret.addChild(child3);
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        this.presentElements = new HashMap();
        getNsElements();
        processTasks();
        this.localAttributes = new Hashtable();
        Set<String> copyKeys = new HashSet<>(this.map.keySet());
        for (MacroDef.Attribute attribute : this.macroDef.getAttributes()) {
            String value = this.map.get(attribute.getName());
            if (value == null && "description".equals(attribute.getName())) {
                value = getDescription();
            }
            if (value == null) {
                value = macroSubs(attribute.getDefault(), this.localAttributes);
            }
            if (value == null) {
                throw new BuildException("required attribute %s not set", attribute.getName());
            }
            this.localAttributes.put(attribute.getName(), value);
            copyKeys.remove(attribute.getName());
        }
        copyKeys.remove("id");
        if (this.macroDef.getText() != null) {
            if (this.text == null) {
                String defaultText = this.macroDef.getText().getDefault();
                if (!this.macroDef.getText().getOptional() && defaultText == null) {
                    throw new BuildException("required text missing");
                }
                this.text = defaultText == null ? "" : defaultText;
            }
            if (this.macroDef.getText().getTrim()) {
                this.text = this.text.trim();
            }
            this.localAttributes.put(this.macroDef.getText().getName(), this.text);
        } else if (this.text != null && !this.text.trim().isEmpty()) {
            throw new BuildException("The \"%s\" macro does not support nested text data.", getTaskName());
        }
        if (!copyKeys.isEmpty()) {
            throw new BuildException("Unknown attribute" + (copyKeys.size() > 1 ? "s " : Instruction.argsep) + copyKeys);
        }
        UnknownElement c = copy(this.macroDef.getNestedTask(), false);
        c.init();
        LocalProperties localProperties = LocalProperties.get(getProject());
        localProperties.enterScope();
        try {
            try {
                c.perform();
                this.presentElements = null;
                this.localAttributes = null;
                localProperties.exitScope();
            } catch (BuildException ex) {
                if (this.macroDef.getBackTrace()) {
                    throw ProjectHelper.addLocationToBuildException(ex, getLocation());
                }
                ex.setLocation(getLocation());
                throw ex;
            }
        } catch (Throwable th) {
            this.presentElements = null;
            this.localAttributes = null;
            localProperties.exitScope();
            throw th;
        }
    }
}
