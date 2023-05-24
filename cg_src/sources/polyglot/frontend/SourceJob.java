package polyglot.frontend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import polyglot.ast.Node;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/SourceJob.class */
public class SourceJob extends Job {
    protected Source source;
    protected Set dependencies;
    protected Set dependents;

    public SourceJob(ExtensionInfo lang, JobExt ext, Source source, Node ast) {
        super(lang, ext, ast);
        this.source = source;
        this.dependencies = new HashSet();
        this.dependents = new HashSet();
    }

    public Set dependencies() {
        return this.dependencies;
    }

    public Set dependents() {
        return this.dependents;
    }

    public void addDependent(Source s) {
        if (s != source()) {
            this.dependents.add(s);
        }
    }

    public void addDependency(Source s) {
        if (s != source()) {
            this.dependencies.add(s);
        }
    }

    @Override // polyglot.frontend.Job
    public List getPasses() {
        return this.lang.passes(this);
    }

    @Override // polyglot.frontend.Job
    public Source source() {
        return this.source;
    }

    @Override // polyglot.frontend.Job
    public SourceJob sourceJob() {
        return this;
    }

    public String toString() {
        String stringBuffer;
        StringBuffer append = new StringBuffer().append(this.source.toString()).append(" (");
        if (completed()) {
            stringBuffer = "done";
        } else {
            stringBuffer = new StringBuffer().append(isRunning() ? "running " : "before ").append(nextPass()).toString();
        }
        return append.append(stringBuffer).append(")").toString();
    }
}
