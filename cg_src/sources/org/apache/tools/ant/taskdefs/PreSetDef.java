package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PreSetDef.class */
public class PreSetDef extends AntlibDefinition implements TaskContainer {
    private UnknownElement nestedTask;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override // org.apache.tools.ant.TaskContainer
    public void addTask(Task nestedTask) {
        if (this.nestedTask != null) {
            throw new BuildException("Only one nested element allowed");
        }
        if (!(nestedTask instanceof UnknownElement)) {
            throw new BuildException("addTask called with a task that is not an unknown element");
        }
        this.nestedTask = (UnknownElement) nestedTask;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.nestedTask == null) {
            throw new BuildException("Missing nested element");
        }
        if (this.name == null) {
            throw new BuildException("Name not specified");
        }
        this.name = ProjectHelper.genComponentName(getURI(), this.name);
        ComponentHelper helper = ComponentHelper.getComponentHelper(getProject());
        String componentName = ProjectHelper.genComponentName(this.nestedTask.getNamespace(), this.nestedTask.getTag());
        AntTypeDefinition def = helper.getDefinition(componentName);
        if (def == null) {
            throw new BuildException("Unable to find typedef %s", componentName);
        }
        PreSetDefinition newDef = new PreSetDefinition(def, this.nestedTask);
        newDef.setName(this.name);
        helper.addDataTypeDefinition(newDef);
        log("defining preset " + this.name, 3);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PreSetDef$PreSetDefinition.class */
    public static class PreSetDefinition extends AntTypeDefinition {
        private AntTypeDefinition parent;
        private UnknownElement element;

        public PreSetDefinition(AntTypeDefinition parent, UnknownElement el) {
            if (parent instanceof PreSetDefinition) {
                PreSetDefinition p = (PreSetDefinition) parent;
                el.applyPreSet(p.element);
                parent = p.parent;
            }
            this.parent = parent;
            this.element = el;
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public void setClass(Class<?> clazz) {
            throw new BuildException("Not supported");
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public void setClassName(String className) {
            throw new BuildException("Not supported");
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public String getClassName() {
            return this.parent.getClassName();
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public void setAdapterClass(Class<?> adapterClass) {
            throw new BuildException("Not supported");
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public void setAdaptToClass(Class<?> adaptToClass) {
            throw new BuildException("Not supported");
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public void setClassLoader(ClassLoader classLoader) {
            throw new BuildException("Not supported");
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public ClassLoader getClassLoader() {
            return this.parent.getClassLoader();
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public Class<?> getExposedClass(Project project) {
            return this.parent.getExposedClass(project);
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public Class<?> getTypeClass(Project project) {
            return this.parent.getTypeClass(project);
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public void checkClass(Project project) {
            this.parent.checkClass(project);
        }

        public Object createObject(Project project) {
            return this.parent.create(project);
        }

        public UnknownElement getPreSets() {
            return this.element;
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public Object create(Project project) {
            return this;
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public boolean sameDefinition(AntTypeDefinition other, Project project) {
            return other != null && other.getClass() == getClass() && this.parent != null && this.parent.sameDefinition(((PreSetDefinition) other).parent, project) && this.element.similar(((PreSetDefinition) other).element);
        }

        @Override // org.apache.tools.ant.AntTypeDefinition
        public boolean similarDefinition(AntTypeDefinition other, Project project) {
            return other != null && other.getClass().getName().equals(getClass().getName()) && this.parent != null && this.parent.similarDefinition(((PreSetDefinition) other).parent, project) && this.element.similar(((PreSetDefinition) other).element);
        }
    }
}
