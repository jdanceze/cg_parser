package org.apache.tools.ant.taskdefs.condition;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/ResourceContains.class */
public class ResourceContains implements Condition {
    private Project project;
    private String substring;
    private Resource resource;
    private String refid;
    private boolean casesensitive = true;

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return this.project;
    }

    public void setResource(String r) {
        this.resource = new FileResource(new File(r));
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    private void resolveRefid() {
        try {
            if (getProject() == null) {
                throw new BuildException("Cannot retrieve refid; project unset");
            }
            Object o = getProject().getReference(this.refid);
            if (!(o instanceof Resource)) {
                if (o instanceof ResourceCollection) {
                    ResourceCollection rc = (ResourceCollection) o;
                    if (rc.size() == 1) {
                        o = rc.iterator().next();
                    }
                } else {
                    throw new BuildException("Illegal value at '%s': %s", this.refid, o);
                }
            }
            this.resource = (Resource) o;
        } finally {
            this.refid = null;
        }
    }

    public void setSubstring(String substring) {
        this.substring = substring;
    }

    public void setCasesensitive(boolean casesensitive) {
        this.casesensitive = casesensitive;
    }

    private void validate() {
        if (this.resource != null && this.refid != null) {
            throw new BuildException("Cannot set both resource and refid");
        }
        if (this.resource == null && this.refid != null) {
            resolveRefid();
        }
        if (this.resource == null || this.substring == null) {
            throw new BuildException("both resource and substring are required in <resourcecontains>");
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public synchronized boolean eval() throws BuildException {
        validate();
        if (this.substring.isEmpty()) {
            if (getProject() != null) {
                getProject().log("Substring is empty; returning true", 3);
                return true;
            }
            return true;
        } else if (this.resource.getSize() == 0) {
            return false;
        } else {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.resource.getInputStream()));
                String contents = FileUtils.safeReadFully(reader);
                String sub = this.substring;
                if (!this.casesensitive) {
                    contents = contents.toLowerCase();
                    sub = sub.toLowerCase();
                }
                boolean contains = contents.contains(sub);
                reader.close();
                return contains;
            } catch (IOException e) {
                throw new BuildException("There was a problem accessing resource : " + this.resource);
            }
        }
    }
}
