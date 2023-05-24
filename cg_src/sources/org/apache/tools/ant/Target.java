package org.apache.tools.ant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.tools.ant.property.LocalProperties;
import org.apache.tools.ant.taskdefs.condition.And;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.Or;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Target.class */
public class Target implements TaskContainer {
    private String name;
    private String ifString;
    private String unlessString;
    private Condition ifCondition;
    private Condition unlessCondition;
    private List<String> dependencies;
    private List<Object> children;
    private Location location;
    private Project project;
    private String description;

    public Target() {
        this.ifString = "";
        this.unlessString = "";
        this.dependencies = null;
        this.children = new ArrayList();
        this.location = Location.UNKNOWN_LOCATION;
        this.description = null;
    }

    public Target(Target other) {
        this.ifString = "";
        this.unlessString = "";
        this.dependencies = null;
        this.children = new ArrayList();
        this.location = Location.UNKNOWN_LOCATION;
        this.description = null;
        this.name = other.name;
        this.ifString = other.ifString;
        this.unlessString = other.unlessString;
        this.ifCondition = other.ifCondition;
        this.unlessCondition = other.unlessCondition;
        this.dependencies = other.dependencies;
        this.location = other.location;
        this.project = other.project;
        this.description = other.description;
        this.children = other.children;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return this.project;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setDepends(String depS) {
        for (String dep : parseDepends(depS, getName(), "depends")) {
            addDependency(dep);
        }
    }

    public static List<String> parseDepends(String depends, String targetName, String attributeName) {
        if (depends.isEmpty()) {
            return new ArrayList();
        }
        List<String> list = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(depends, ",", true);
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken().trim();
            if (token.isEmpty() || ",".equals(token)) {
                throw new BuildException("Syntax Error: " + attributeName + " attribute of target \"" + targetName + "\" contains an empty string.");
            }
            list.add(token);
            if (tok.hasMoreTokens()) {
                String token2 = tok.nextToken();
                if (!tok.hasMoreTokens() || !",".equals(token2)) {
                    throw new BuildException("Syntax Error: " + attributeName + " attribute for target \"" + targetName + "\" ends with a \",\" character");
                }
            }
        }
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override // org.apache.tools.ant.TaskContainer
    public void addTask(Task task) {
        this.children.add(task);
    }

    public void addDataType(RuntimeConfigurable r) {
        this.children.add(r);
    }

    public Task[] getTasks() {
        List<Task> tasks = new ArrayList<>(this.children.size());
        for (Object o : this.children) {
            if (o instanceof Task) {
                tasks.add((Task) o);
            }
        }
        return (Task[]) tasks.toArray(new Task[tasks.size()]);
    }

    public void addDependency(String dependency) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList(2);
        }
        this.dependencies.add(dependency);
    }

    public Enumeration<String> getDependencies() {
        return this.dependencies == null ? Collections.emptyEnumeration() : Collections.enumeration(this.dependencies);
    }

    public boolean dependsOn(String other) {
        Project p = getProject();
        Hashtable<String, Target> t = p == null ? null : p.getTargets();
        return p != null && p.topoSort(getName(), t, false).contains(t.get(other));
    }

    public void setIf(String property) {
        this.ifString = property == null ? "" : property;
        setIf(() -> {
            PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
            Object o = propertyHelper.parseProperties(this.ifString);
            return propertyHelper.testIfCondition(o);
        });
    }

    public String getIf() {
        if (this.ifString.isEmpty()) {
            return null;
        }
        return this.ifString;
    }

    public void setIf(Condition condition) {
        if (this.ifCondition == null) {
            this.ifCondition = condition;
            return;
        }
        And andCondition = new And();
        andCondition.setProject(getProject());
        andCondition.setLocation(getLocation());
        andCondition.add(this.ifCondition);
        andCondition.add(condition);
        this.ifCondition = andCondition;
    }

    public void setUnless(String property) {
        this.unlessString = property == null ? "" : property;
        setUnless(() -> {
            PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
            Object o = propertyHelper.parseProperties(this.unlessString);
            return !propertyHelper.testUnlessCondition(o);
        });
    }

    public String getUnless() {
        if (this.unlessString.isEmpty()) {
            return null;
        }
        return this.unlessString;
    }

    public void setUnless(Condition condition) {
        if (this.unlessCondition == null) {
            this.unlessCondition = condition;
            return;
        }
        Or orCondition = new Or();
        orCondition.setProject(getProject());
        orCondition.setLocation(getLocation());
        orCondition.add(this.unlessCondition);
        orCondition.add(condition);
        this.unlessCondition = orCondition;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return this.name;
    }

    public void execute() throws BuildException {
        if (this.ifCondition != null && !this.ifCondition.eval()) {
            this.project.log(this, "Skipped because property '" + this.project.replaceProperties(this.ifString) + "' not set.", 3);
        } else if (this.unlessCondition != null && this.unlessCondition.eval()) {
            this.project.log(this, "Skipped because property '" + this.project.replaceProperties(this.unlessString) + "' set.", 3);
        } else {
            LocalProperties localProperties = LocalProperties.get(getProject());
            localProperties.enterScope();
            for (int i = 0; i < this.children.size(); i++) {
                try {
                    Object o = this.children.get(i);
                    if (o instanceof Task) {
                        Task task = (Task) o;
                        task.perform();
                    } else {
                        ((RuntimeConfigurable) o).maybeConfigure(this.project);
                    }
                } finally {
                    localProperties.exitScope();
                }
            }
        }
    }

    public final void performTasks() {
        RuntimeException thrown = null;
        this.project.fireTargetStarted(this);
        try {
            try {
                execute();
                this.project.fireTargetFinished(this, null);
            } catch (RuntimeException exc) {
                thrown = exc;
                throw exc;
            }
        } catch (Throwable th) {
            this.project.fireTargetFinished(this, thrown);
            throw th;
        }
    }

    void replaceChild(Task el, RuntimeConfigurable o) {
        while (true) {
            int index = this.children.indexOf(el);
            if (index >= 0) {
                this.children.set(index, o);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void replaceChild(Task el, Task o) {
        while (true) {
            int index = this.children.indexOf(el);
            if (index >= 0) {
                this.children.set(index, o);
            } else {
                return;
            }
        }
    }
}
