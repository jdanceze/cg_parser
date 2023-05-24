package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.types.PropertySet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/CallTarget.class */
public class CallTarget extends Task {
    private Ant callee;
    private boolean inheritAll = true;
    private boolean inheritRefs = false;
    private boolean targetSet = false;

    public void setInheritAll(boolean inherit) {
        this.inheritAll = inherit;
    }

    public void setInheritRefs(boolean inheritRefs) {
        this.inheritRefs = inheritRefs;
    }

    @Override // org.apache.tools.ant.Task
    public void init() {
        this.callee = new Ant(this);
        this.callee.init();
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.callee == null) {
            init();
        }
        if (!this.targetSet) {
            throw new BuildException("Attribute target or at least one nested target is required.", getLocation());
        }
        this.callee.setAntfile(getProject().getProperty(MagicNames.ANT_FILE));
        this.callee.setInheritAll(this.inheritAll);
        this.callee.setInheritRefs(this.inheritRefs);
        this.callee.execute();
    }

    public Property createParam() {
        if (this.callee == null) {
            init();
        }
        return this.callee.createProperty();
    }

    public void addReference(Ant.Reference r) {
        if (this.callee == null) {
            init();
        }
        this.callee.addReference(r);
    }

    public void addPropertyset(PropertySet ps) {
        if (this.callee == null) {
            init();
        }
        this.callee.addPropertyset(ps);
    }

    public void setTarget(String target) {
        if (this.callee == null) {
            init();
        }
        this.callee.setTarget(target);
        this.targetSet = true;
    }

    public void addConfiguredTarget(Ant.TargetElement t) {
        if (this.callee == null) {
            init();
        }
        this.callee.addConfiguredTarget(t);
        this.targetSet = true;
    }

    @Override // org.apache.tools.ant.Task
    public void handleOutput(String output) {
        if (this.callee != null) {
            this.callee.handleOutput(output);
        } else {
            super.handleOutput(output);
        }
    }

    @Override // org.apache.tools.ant.Task
    public int handleInput(byte[] buffer, int offset, int length) throws IOException {
        if (this.callee != null) {
            return this.callee.handleInput(buffer, offset, length);
        }
        return super.handleInput(buffer, offset, length);
    }

    @Override // org.apache.tools.ant.Task
    public void handleFlush(String output) {
        if (this.callee != null) {
            this.callee.handleFlush(output);
        } else {
            super.handleFlush(output);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void handleErrorOutput(String output) {
        if (this.callee != null) {
            this.callee.handleErrorOutput(output);
        } else {
            super.handleErrorOutput(output);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void handleErrorFlush(String output) {
        if (this.callee != null) {
            this.callee.handleErrorFlush(output);
        } else {
            super.handleErrorFlush(output);
        }
    }
}
