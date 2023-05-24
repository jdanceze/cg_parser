package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Reference.class */
public class Reference {
    private String refid;
    private Project project;

    @Deprecated
    public Reference() {
    }

    @Deprecated
    public Reference(String id) {
        setRefId(id);
    }

    public Reference(Project p, String id) {
        setRefId(id);
        setProject(p);
    }

    public void setRefId(String id) {
        this.refid = id;
    }

    public String getRefId() {
        return this.refid;
    }

    public void setProject(Project p) {
        this.project = p;
    }

    public Project getProject() {
        return this.project;
    }

    public <T> T getReferencedObject(Project fallback) throws BuildException {
        if (this.refid == null) {
            throw new BuildException("No reference specified");
        }
        T o = (T) (this.project == null ? fallback.getReference(this.refid) : this.project.getReference(this.refid));
        if (o == null) {
            throw new BuildException("Reference " + this.refid + " not found.");
        }
        return o;
    }

    public <T> T getReferencedObject() throws BuildException {
        if (this.project == null) {
            throw new BuildException("No project set on reference to " + this.refid);
        }
        return (T) getReferencedObject(this.project);
    }
}
