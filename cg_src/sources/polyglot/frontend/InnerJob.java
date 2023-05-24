package polyglot.frontend;

import java.util.List;
import polyglot.ast.Node;
import polyglot.frontend.Pass;
import polyglot.types.Context;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/InnerJob.class */
public class InnerJob extends Job {
    protected Job outer;
    protected Context context;
    protected Pass.ID begin;
    protected Pass.ID end;

    public InnerJob(ExtensionInfo lang, JobExt ext, Node ast, Context context, Job outer, Pass.ID begin, Pass.ID end) {
        super(lang, ext, ast);
        this.context = context;
        this.outer = outer;
        this.begin = begin;
        this.end = end;
        if (ast == null) {
            throw new InternalCompilerError("Null ast");
        }
        if (outer == null) {
            throw new InternalCompilerError("Null outer job");
        }
    }

    public String toString() {
        String name = new StringBuffer().append("inner-job[").append(this.begin).append("..").append(this.end).append("](code=").append(this.context.currentCode()).append(" class=").append(this.context.currentClass()).append(") [").append(status()).append("]").toString();
        return new StringBuffer().append(name).append(" (").append(isRunning() ? "running " : "before ").append(nextPass()).append(")").append(" <<< passes = ").append(this.passes).append(" >>>").toString();
    }

    @Override // polyglot.frontend.Job
    public List getPasses() {
        List l = this.lang.passes(this, this.begin, this.end);
        for (int i = 0; i < l.size(); i++) {
            Pass pass = (Pass) l.get(i);
            if (pass.id() == this.begin) {
                this.nextPass = i;
            }
            if (i == l.size() - 1 && pass.id() != this.end) {
                throw new InternalCompilerError(new StringBuffer().append("ExtensionInfo.passes returned incorrect list: ").append(l).toString());
            }
        }
        return l;
    }

    @Override // polyglot.frontend.Job
    public Context context() {
        return this.context;
    }

    @Override // polyglot.frontend.Job
    public SourceJob sourceJob() {
        return this.outer.sourceJob();
    }
}
