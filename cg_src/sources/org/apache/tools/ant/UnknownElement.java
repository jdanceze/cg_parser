package org.apache.tools.ant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.taskdefs.PreSetDef;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/UnknownElement.class */
public class UnknownElement extends Task {
    private final String elementName;
    private String qname;
    private Object realThing;
    private String namespace = "";
    private List<UnknownElement> children = null;
    private boolean presetDefed = false;

    public UnknownElement(String elementName) {
        this.elementName = elementName;
    }

    public List<UnknownElement> getChildren() {
        return this.children;
    }

    public String getTag() {
        return this.elementName;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        if (namespace.equals(ProjectHelper.ANT_CURRENT_URI)) {
            ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
            namespace = helper.getCurrentAntlibUri();
        }
        this.namespace = namespace == null ? "" : namespace;
    }

    public String getQName() {
        return this.qname;
    }

    public void setQName(String qname) {
        this.qname = qname;
    }

    @Override // org.apache.tools.ant.Task
    public RuntimeConfigurable getWrapper() {
        return super.getWrapper();
    }

    @Override // org.apache.tools.ant.Task
    public void maybeConfigure() throws BuildException {
        Object copy = this.realThing;
        if (copy != null) {
            return;
        }
        configure(makeObject(this, getWrapper()));
    }

    public void configure(Object realObject) {
        if (realObject == null) {
            return;
        }
        this.realThing = realObject;
        getWrapper().setProxy(realObject);
        Task task = null;
        if (realObject instanceof Task) {
            task = (Task) realObject;
            task.setRuntimeConfigurableWrapper(getWrapper());
            if (getWrapper().getId() != null) {
                getOwningTarget().replaceChild(this, (Task) realObject);
            }
        }
        if (task != null) {
            task.maybeConfigure();
        } else {
            getWrapper().maybeConfigure(getProject());
        }
        handleChildren(realObject, getWrapper());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleOutput(String output) {
        Object copy = this.realThing;
        if (copy instanceof Task) {
            ((Task) copy).handleOutput(output);
        } else {
            super.handleOutput(output);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public int handleInput(byte[] buffer, int offset, int length) throws IOException {
        Object copy = this.realThing;
        if (copy instanceof Task) {
            return ((Task) copy).handleInput(buffer, offset, length);
        }
        return super.handleInput(buffer, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleFlush(String output) {
        Object copy = this.realThing;
        if (copy instanceof Task) {
            ((Task) copy).handleFlush(output);
        } else {
            super.handleFlush(output);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleErrorOutput(String output) {
        Object copy = this.realThing;
        if (copy instanceof Task) {
            ((Task) copy).handleErrorOutput(output);
        } else {
            super.handleErrorOutput(output);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleErrorFlush(String output) {
        Object copy = this.realThing;
        if (copy instanceof Task) {
            ((Task) copy).handleErrorFlush(output);
        } else {
            super.handleErrorFlush(output);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        Object copy = this.realThing;
        if (copy == null) {
            return;
        }
        try {
            if (copy instanceof Task) {
                ((Task) copy).execute();
            }
        } finally {
            if (getWrapper().getId() == null) {
                this.realThing = null;
                getWrapper().setProxy(null);
            }
        }
    }

    public void addChild(UnknownElement child) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(child);
    }

    protected void handleChildren(Object parent, RuntimeConfigurable parentWrapper) throws BuildException {
        if (parent instanceof TypeAdapter) {
            parent = ((TypeAdapter) parent).getProxy();
        }
        String parentUri = getNamespace();
        Class<?> parentClass = parent.getClass();
        IntrospectionHelper ih = IntrospectionHelper.getHelper(getProject(), parentClass);
        if (this.children != null) {
            int i = 0;
            for (UnknownElement child : this.children) {
                RuntimeConfigurable childWrapper = parentWrapper.getChild(i);
                try {
                    if (childWrapper.isEnabled(child) || !ih.supportsNestedElement(parentUri, ProjectHelper.genComponentName(child.getNamespace(), child.getTag()))) {
                        if (!handleChild(parentUri, ih, parent, child, childWrapper)) {
                            if (!(parent instanceof TaskContainer)) {
                                ih.throwNotSupported(getProject(), parent, child.getTag());
                            } else {
                                TaskContainer container = (TaskContainer) parent;
                                container.addTask(child);
                            }
                        }
                    }
                    i++;
                } catch (UnsupportedElementException ex) {
                    throw new BuildException(parentWrapper.getElementTag() + " doesn't support the nested \"" + ex.getElement() + "\" element.", ex);
                }
            }
        }
    }

    protected String getComponentName() {
        return ProjectHelper.genComponentName(getNamespace(), getTag());
    }

    public void applyPreSet(UnknownElement u) {
        if (this.presetDefed) {
            return;
        }
        getWrapper().applyPreSet(u.getWrapper());
        if (u.children != null) {
            List<UnknownElement> newChildren = new ArrayList<>(u.children);
            if (this.children != null) {
                newChildren.addAll(this.children);
            }
            this.children = newChildren;
        }
        this.presetDefed = true;
    }

    protected Object makeObject(UnknownElement ue, RuntimeConfigurable w) {
        if (!w.isEnabled(ue)) {
            return null;
        }
        ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
        String name = ue.getComponentName();
        Object o = helper.createComponent(ue, ue.getNamespace(), name);
        if (o == null) {
            throw getNotFoundException("task or type", name);
        }
        if (o instanceof PreSetDef.PreSetDefinition) {
            PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition) o;
            o = def.createObject(ue.getProject());
            if (o == null) {
                throw getNotFoundException("preset " + name, def.getPreSets().getComponentName());
            }
            ue.applyPreSet(def.getPreSets());
            if (o instanceof Task) {
                Task task = (Task) o;
                task.setTaskType(ue.getTaskType());
                task.setTaskName(ue.getTaskName());
                task.init();
            }
        }
        if (o instanceof UnknownElement) {
            o = ((UnknownElement) o).makeObject((UnknownElement) o, w);
        }
        if (o instanceof Task) {
            ((Task) o).setOwningTarget(getOwningTarget());
        }
        if (o instanceof ProjectComponent) {
            ((ProjectComponent) o).setLocation(getLocation());
        }
        return o;
    }

    protected Task makeTask(UnknownElement ue, RuntimeConfigurable w) {
        Task task = getProject().createTask(ue.getTag());
        if (task != null) {
            task.setLocation(getLocation());
            task.setOwningTarget(getOwningTarget());
            task.init();
        }
        return task;
    }

    protected BuildException getNotFoundException(String what, String name) {
        ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
        String msg = helper.diagnoseCreationFailure(name, what);
        return new BuildException(msg, getLocation());
    }

    @Override // org.apache.tools.ant.Task
    public String getTaskName() {
        Object copy = this.realThing;
        return !(copy instanceof Task) ? super.getTaskName() : ((Task) copy).getTaskName();
    }

    public Task getTask() {
        Object copy = this.realThing;
        if (copy instanceof Task) {
            return (Task) copy;
        }
        return null;
    }

    public Object getRealThing() {
        return this.realThing;
    }

    public void setRealThing(Object realThing) {
        this.realThing = realThing;
    }

    private boolean handleChild(String parentUri, IntrospectionHelper ih, Object parent, UnknownElement child, RuntimeConfigurable childWrapper) {
        String childName = ProjectHelper.genComponentName(child.getNamespace(), child.getTag());
        if (ih.supportsNestedElement(parentUri, childName, getProject(), parent)) {
            try {
                IntrospectionHelper.Creator creator = ih.getElementCreator(getProject(), parentUri, parent, childName, child);
                creator.setPolyType(childWrapper.getPolyType());
                Object realChild = creator.create();
                if (realChild instanceof PreSetDef.PreSetDefinition) {
                    PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition) realChild;
                    realChild = creator.getRealObject();
                    child.applyPreSet(def.getPreSets());
                }
                childWrapper.setCreator(creator);
                childWrapper.setProxy(realChild);
                if (realChild instanceof Task) {
                    Task childTask = (Task) realChild;
                    childTask.setRuntimeConfigurableWrapper(childWrapper);
                    childTask.setTaskName(childName);
                    childTask.setTaskType(childName);
                }
                if (realChild instanceof ProjectComponent) {
                    ((ProjectComponent) realChild).setLocation(child.getLocation());
                }
                childWrapper.maybeConfigure(getProject());
                child.handleChildren(realChild, childWrapper);
                creator.store();
                return true;
            } catch (UnsupportedElementException use) {
                if (!ih.isDynamic()) {
                    throw use;
                }
                return false;
            }
        }
        return false;
    }

    public boolean similar(Object obj) {
        if (obj == null || !getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }
        UnknownElement other = (UnknownElement) obj;
        if (!Objects.equals(this.elementName, other.elementName) || !this.namespace.equals(other.namespace) || !this.qname.equals(other.qname) || !getWrapper().getAttributeMap().equals(other.getWrapper().getAttributeMap()) || !getWrapper().getText().toString().equals(other.getWrapper().getText().toString())) {
            return false;
        }
        int childrenSize = this.children == null ? 0 : this.children.size();
        if (childrenSize == 0) {
            return other.children == null || other.children.isEmpty();
        } else if (other.children == null || childrenSize != other.children.size()) {
            return false;
        } else {
            for (int i = 0; i < childrenSize; i++) {
                UnknownElement child = this.children.get(i);
                if (!child.similar(other.children.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public UnknownElement copy(Project newProject) {
        UnknownElement ret = new UnknownElement(getTag());
        ret.setNamespace(getNamespace());
        ret.setProject(newProject);
        ret.setQName(getQName());
        ret.setTaskType(getTaskType());
        ret.setTaskName(getTaskName());
        ret.setLocation(getLocation());
        if (getOwningTarget() == null) {
            Target t = new Target();
            t.setProject(getProject());
            ret.setOwningTarget(t);
        } else {
            ret.setOwningTarget(getOwningTarget());
        }
        RuntimeConfigurable copyRC = new RuntimeConfigurable(ret, getTaskName());
        copyRC.setPolyType(getWrapper().getPolyType());
        Map<String, Object> m = getWrapper().getAttributeMap();
        for (Map.Entry<String, Object> entry : m.entrySet()) {
            copyRC.setAttribute(entry.getKey(), (String) entry.getValue());
        }
        copyRC.addText(getWrapper().getText().toString());
        Iterator it = Collections.list(getWrapper().getChildren()).iterator();
        while (it.hasNext()) {
            RuntimeConfigurable r = (RuntimeConfigurable) it.next();
            UnknownElement ueChild = (UnknownElement) r.getProxy();
            UnknownElement copyChild = ueChild.copy(newProject);
            copyRC.addChild(copyChild.getWrapper());
            ret.addChild(copyChild);
        }
        return ret;
    }
}
