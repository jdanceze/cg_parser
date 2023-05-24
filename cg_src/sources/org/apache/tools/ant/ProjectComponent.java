package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ProjectComponent.class */
public abstract class ProjectComponent implements Cloneable {
    @Deprecated
    protected Project project;
    @Deprecated
    protected Location location = Location.UNKNOWN_LOCATION;
    @Deprecated
    protected String description;

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return this.project;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }

    public void log(String msg) {
        log(msg, 2);
    }

    public void log(String msg, int msgLevel) {
        if (getProject() != null) {
            getProject().log(msg, msgLevel);
        } else if (msgLevel <= 2) {
            System.err.println(msg);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        ProjectComponent pc = (ProjectComponent) super.clone();
        pc.setLocation(getLocation());
        pc.setProject(getProject());
        return pc;
    }
}
