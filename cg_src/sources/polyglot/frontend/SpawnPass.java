package polyglot.frontend;

import polyglot.frontend.Pass;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/SpawnPass.class */
public class SpawnPass extends AbstractPass {
    Job job;
    Pass.ID begin;
    Pass.ID end;

    public SpawnPass(Pass.ID id, Job job, Pass.ID begin, Pass.ID end) {
        super(id);
        this.job = job;
        this.begin = begin;
        this.end = end;
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        if (this.job.ast() == null) {
            throw new InternalCompilerError("Null AST.");
        }
        Job j = this.job.spawn(this.job.context(), this.job.ast(), this.begin, this.end);
        return j.status();
    }
}
