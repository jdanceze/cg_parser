package polyglot.frontend;

import polyglot.frontend.Pass;
import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/BarrierPass.class */
public class BarrierPass extends AbstractPass {
    Job job;

    public BarrierPass(Pass.ID id, Job job) {
        super(id);
        this.job = job;
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        if (Report.should_report(Report.frontend, 1)) {
            Report.report(1, new StringBuffer().append(this.job).append(" at barrier ").append(this.id).toString());
        }
        if (Report.should_report(Report.frontend, 2)) {
            Report.report(2, new StringBuffer().append("dependencies of ").append(this.job.sourceJob()).append(" = ").append(this.job.sourceJob().dependencies()).toString());
            return true;
        }
        return true;
    }
}
