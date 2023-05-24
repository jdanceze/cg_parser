package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ResourceCount.class */
public class ResourceCount extends Task implements Condition {
    private static final String ONE_NESTED_MESSAGE = "ResourceCount can count resources from exactly one nested ResourceCollection.";
    private static final String COUNT_REQUIRED = "Use of the ResourceCount condition requires that the count attribute be set.";
    private ResourceCollection rc;
    private Comparison when = Comparison.EQUAL;
    private Integer count;
    private String property;

    public void add(ResourceCollection r) {
        if (this.rc != null) {
            throw new BuildException(ONE_NESTED_MESSAGE);
        }
        this.rc = r;
    }

    public void setRefid(Reference r) {
        Object o = r.getReferencedObject();
        if (!(o instanceof ResourceCollection)) {
            throw new BuildException("%s doesn't denote a ResourceCollection", r.getRefId());
        }
        add((ResourceCollection) o);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        if (this.rc == null) {
            throw new BuildException(ONE_NESTED_MESSAGE);
        }
        if (this.property == null) {
            log("resource count = " + this.rc.size());
        } else {
            getProject().setNewProperty(this.property, Integer.toString(this.rc.size()));
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() {
        if (this.rc == null) {
            throw new BuildException(ONE_NESTED_MESSAGE);
        }
        if (this.count == null) {
            throw new BuildException(COUNT_REQUIRED);
        }
        return this.when.evaluate(Integer.valueOf(this.rc.size()).compareTo(this.count));
    }

    public void setCount(int c) {
        this.count = Integer.valueOf(c);
    }

    public void setWhen(Comparison c) {
        this.when = c;
    }

    public void setProperty(String p) {
        this.property = p;
    }
}
