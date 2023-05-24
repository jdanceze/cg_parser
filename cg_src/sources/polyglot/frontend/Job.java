package polyglot.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import polyglot.ast.Node;
import polyglot.frontend.Pass;
import polyglot.types.Context;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/Job.class */
public abstract class Job {
    protected JobExt ext;
    protected ExtensionInfo lang;
    protected Node ast;
    protected ArrayList passes = null;
    protected Map passMap = null;
    protected int nextPass = 0;
    protected Pass runningPass = null;
    protected boolean status = true;
    protected int initialErrorCount = 0;
    protected boolean reportedErrors = false;

    public abstract SourceJob sourceJob();

    protected abstract List getPasses();

    public Job(ExtensionInfo lang, JobExt ext, Node ast) {
        this.lang = lang;
        this.ext = ext;
        this.ast = ast;
    }

    public JobExt ext() {
        return this.ext;
    }

    public BarrierPass lastBarrier() {
        for (int i = this.nextPass - 1; i >= 0; i--) {
            Pass pass = (Pass) this.passes.get(i);
            if (pass instanceof BarrierPass) {
                return (BarrierPass) pass;
            }
        }
        return null;
    }

    public void setRunningPass(Pass pass) {
        if (pass != null) {
            this.initialErrorCount = compiler().errorQueue().errorCount();
        } else {
            int errorCount = compiler().errorQueue().errorCount();
            if (errorCount > this.initialErrorCount) {
                this.reportedErrors = true;
            }
        }
        this.runningPass = pass;
    }

    public boolean isRunning() {
        return this.runningPass != null;
    }

    public Pass runningPass() {
        return this.runningPass;
    }

    public Node ast() {
        return this.ast;
    }

    public void ast(Node ast) {
        this.ast = ast;
    }

    public boolean reportedErrors() {
        return this.reportedErrors;
    }

    public void dump(CodeWriter cw) {
        if (this.ast != null) {
            this.ast.dump(cw);
        }
    }

    public Context context() {
        return null;
    }

    public Source source() {
        return sourceJob().source();
    }

    public boolean userSpecified() {
        return source().userSpecified();
    }

    public final List passes() {
        if (this.passes == null) {
            init();
        }
        return this.passes;
    }

    private Map passMap() {
        if (this.passMap == null) {
            init();
        }
        return this.passMap;
    }

    protected void init() {
        this.passes = new ArrayList(getPasses());
        this.passMap = new HashMap();
        for (int i = 0; i < this.passes.size(); i++) {
            Pass pass = (Pass) this.passes.get(i);
            this.passMap.put(pass.id(), new Integer(i));
        }
    }

    public boolean completed() {
        return pendingPasses().isEmpty();
    }

    public List completedPasses() {
        return passes().subList(0, this.nextPass);
    }

    public List pendingPasses() {
        return passes().subList(this.nextPass, this.passes.size());
    }

    public boolean completed(Pass.ID id) {
        Integer i = (Integer) passMap().get(id);
        return i != null && i.intValue() < this.nextPass;
    }

    public boolean pending(Pass.ID id) {
        Integer i = (Integer) passMap().get(id);
        return i != null && i.intValue() >= this.nextPass;
    }

    public Pass passByID(Pass.ID id) {
        Integer i = (Integer) passMap().get(id);
        if (i != null) {
            return (Pass) passes().get(i.intValue());
        }
        throw new InternalCompilerError(new StringBuffer().append("No pass named \"").append(id).append("\".").toString());
    }

    public Pass getPreviousTo(Pass.ID id) {
        Integer i = (Integer) passMap().get(id);
        if (i != null) {
            if (i.intValue() == 0) {
                return null;
            }
            return (Pass) passes().get(i.intValue() - 1);
        }
        throw new InternalCompilerError(new StringBuffer().append("No pass named \"").append(id).append("\".").toString());
    }

    public Pass nextPass() {
        if (this.nextPass < passes().size()) {
            Pass pass = (Pass) passes().get(this.nextPass);
            return pass;
        }
        return null;
    }

    public boolean status() {
        return this.status;
    }

    public void finishPass(Pass p, boolean okay) {
        List passes = passes();
        this.status &= okay;
        for (int i = this.nextPass; i < passes.size(); i++) {
            Pass pass = (Pass) passes.get(i);
            if (pass == p) {
                this.nextPass = i + 1;
                return;
            }
        }
        throw new InternalCompilerError(new StringBuffer().append("Pass ").append(p).append(" was not a pending ").append("pass.").toString());
    }

    public ExtensionInfo extensionInfo() {
        return this.lang;
    }

    public Compiler compiler() {
        return this.lang.compiler();
    }

    public Job spawn(Context c, Node ast, Pass.ID begin, Pass.ID end) {
        return this.lang.spawnJob(c, ast, this, begin, end);
    }
}
