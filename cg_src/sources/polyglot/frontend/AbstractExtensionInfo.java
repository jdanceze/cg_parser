package polyglot.frontend;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Pass;
import polyglot.main.Options;
import polyglot.main.Report;
import polyglot.types.Context;
import polyglot.types.TypeSystem;
import polyglot.types.reflect.ClassFile;
import polyglot.util.CodeWriter;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.visit.DumpAst;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/AbstractExtensionInfo.class */
public abstract class AbstractExtensionInfo implements ExtensionInfo {
    protected Compiler compiler;
    private Options options;
    protected TypeSystem ts = null;
    protected NodeFactory nf = null;
    protected SourceLoader source_loader = null;
    protected TargetFactory target_factory = null;
    protected Stats stats;
    protected LinkedList worklist;
    protected Map jobs;
    protected static final Object COMPLETED_JOB = "COMPLETED JOB";
    protected Job currentJob;

    protected abstract void initTypeSystem();

    protected abstract TypeSystem createTypeSystem();

    protected abstract NodeFactory createNodeFactory();

    @Override // polyglot.frontend.ExtensionInfo
    public abstract Parser parser(Reader reader, FileSource fileSource, ErrorQueue errorQueue);

    @Override // polyglot.frontend.ExtensionInfo
    public abstract List passes(Job job);

    @Override // polyglot.frontend.ExtensionInfo
    public Options getOptions() {
        if (this.options == null) {
            this.options = createOptions();
        }
        return this.options;
    }

    protected Options createOptions() {
        return new Options(this);
    }

    @Override // polyglot.frontend.ExtensionInfo
    public Stats getStats() {
        if (this.stats == null) {
            this.stats = new Stats(this);
        }
        return this.stats;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public Compiler compiler() {
        return this.compiler;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void initCompiler(Compiler compiler) {
        this.compiler = compiler;
        this.jobs = new HashMap();
        this.worklist = new LinkedList();
        compiler.addExtension(this);
        this.currentJob = null;
        typeSystem();
        nodeFactory();
        initTypeSystem();
    }

    @Override // polyglot.frontend.ExtensionInfo
    public boolean runToCompletion() {
        boolean okay = true;
        while (okay && !this.worklist.isEmpty()) {
            SourceJob job = selectJobFromWorklist();
            if (Report.should_report(Report.frontend, 1)) {
                Report.report(1, new StringBuffer().append("Running job ").append(job).toString());
            }
            okay &= runAllPasses(job);
            if (job.completed()) {
                this.jobs.put(job.source(), COMPLETED_JOB);
                if (Report.should_report(Report.frontend, 1)) {
                    Report.report(1, new StringBuffer().append("Completed job ").append(job).toString());
                }
            } else {
                if (Report.should_report(Report.frontend, 1)) {
                    Report.report(1, new StringBuffer().append("Failed to complete job ").append(job).toString());
                }
                this.worklist.add(job);
            }
        }
        if (Report.should_report(Report.frontend, 1)) {
            Report.report(1, new StringBuffer().append("Finished all passes -- ").append(okay ? "okay" : "failed").toString());
        }
        return okay;
    }

    protected SourceJob selectJobFromWorklist() {
        return (SourceJob) this.worklist.remove(0);
    }

    @Override // polyglot.frontend.ExtensionInfo
    public boolean readSource(FileSource source) {
        Pass.ID barrier;
        SourceJob job = addJob(source);
        if (job == null) {
            return true;
        }
        if (this.currentJob != null) {
            if (this.currentJob.sourceJob().lastBarrier() == null) {
                throw new InternalCompilerError("A Source Job which has not reached a barrier cannot read another source file.");
            }
            barrier = this.currentJob.sourceJob().lastBarrier().id();
        } else {
            barrier = Pass.FIRST_BARRIER;
        }
        return runToPass(job, barrier) && runToPass(job, Pass.FIRST_BARRIER);
    }

    @Override // polyglot.frontend.ExtensionInfo
    public boolean runAllPasses(Job job) {
        List pending = job.pendingPasses();
        if (!pending.isEmpty()) {
            Pass lastPass = (Pass) pending.get(pending.size() - 1);
            return runToPass(job, lastPass);
        }
        return true;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public boolean runToPass(Job job, Pass.ID goal) {
        if (Report.should_report(Report.frontend, 1)) {
            Report.report(1, new StringBuffer().append("Running ").append(job).append(" to pass named ").append(goal).toString());
        }
        if (job.completed(goal)) {
            return true;
        }
        Pass pass = job.passByID(goal);
        return runToPass(job, pass);
    }

    public boolean runToPass(Job job, Pass goal) {
        if (Report.should_report(Report.frontend, 1)) {
            Report.report(1, new StringBuffer().append("Running ").append(job).append(" to pass ").append(goal).toString());
        }
        while (!job.pendingPasses().isEmpty()) {
            Pass pass = (Pass) job.pendingPasses().get(0);
            try {
                runPass(job, pass);
            } catch (CyclicDependencyException e) {
                job.finishPass(pass, false);
            }
            if (pass == goal) {
                break;
            }
        }
        if (job.completed() && Report.should_report(Report.frontend, 1)) {
            Report.report(1, new StringBuffer().append("Job ").append(job).append(" completed").toString());
        }
        return job.status();
    }

    protected void runPass(Job job, Pass pass) throws CyclicDependencyException {
        try {
            enforceInvariants(job, pass);
            if (getOptions().disable_passes.contains(pass.name())) {
                if (Report.should_report(Report.frontend, 1)) {
                    Report.report(1, new StringBuffer().append("Skipping pass ").append(pass).toString());
                }
                job.finishPass(pass, true);
                return;
            }
            if (Report.should_report(Report.frontend, 1)) {
                Report.report(1, new StringBuffer().append("Trying to run pass ").append(pass).append(" in ").append(job).toString());
            }
            if (job.isRunning()) {
                throw new CyclicDependencyException(new StringBuffer().append(job).append(" cannot reach pass ").append(pass).toString());
            }
            pass.resetTimers();
            boolean result = false;
            if (job.status()) {
                Job oldCurrentJob = this.currentJob;
                this.currentJob = job;
                Report.should_report.push(pass.name());
                Pass oldPass = oldCurrentJob != null ? oldCurrentJob.runningPass() : null;
                if (oldPass != null) {
                    oldPass.toggleTimers(true);
                }
                job.setRunningPass(pass);
                pass.toggleTimers(false);
                result = pass.run();
                pass.toggleTimers(false);
                job.setRunningPass(null);
                Report.should_report.pop();
                this.currentJob = oldCurrentJob;
                if (oldPass != null) {
                    oldPass.toggleTimers(true);
                }
                if (getOptions().print_ast.contains(pass.name())) {
                    System.err.println("----------------------------------------------------------------");
                    System.err.println(new StringBuffer().append("Pretty-printing AST for ").append(job).append(" after ").append(pass.name()).toString());
                    PrettyPrinter pp = new PrettyPrinter();
                    pp.printAst(job.ast(), new CodeWriter(System.err, 78));
                }
                if (getOptions().dump_ast.contains(pass.name())) {
                    System.err.println("----------------------------------------------------------------");
                    System.err.println(new StringBuffer().append("Dumping AST for ").append(job).append(" after ").append(pass.name()).toString());
                    NodeVisitor dumper = new DumpAst(new CodeWriter(System.err, 78)).begin();
                    job.ast().visit(dumper);
                    dumper.finish();
                }
            }
            Stats stats = getStats();
            stats.accumPassTimes(pass.id(), pass.inclusiveTime(), pass.exclusiveTime());
            if (Report.should_report("time", 2)) {
                Report.report(2, new StringBuffer().append("Finished ").append(pass).append(" status=").append(str(result)).append(" inclusive_time=").append(pass.inclusiveTime()).append(" exclusive_time=").append(pass.exclusiveTime()).toString());
            } else if (Report.should_report(Report.frontend, 1)) {
                Report.report(1, new StringBuffer().append("Finished ").append(pass).append(" status=").append(str(result)).toString());
            }
            job.finishPass(pass, result);
        } catch (CyclicDependencyException e) {
        }
    }

    protected void enforceInvariants(Job job, Pass pass) throws CyclicDependencyException {
        SourceJob srcJob = job.sourceJob();
        if (srcJob == null) {
            return;
        }
        BarrierPass lastBarrier = srcJob.lastBarrier();
        if (lastBarrier != null) {
            List<Source> allDependentSrcs = new ArrayList(srcJob.dependencies());
            for (Source s : allDependentSrcs) {
                Object o = this.jobs.get(s);
                if (o != COMPLETED_JOB) {
                    if (o == null) {
                        throw new InternalCompilerError(new StringBuffer().append("Unknown source ").append(s).toString());
                    }
                    SourceJob sj = (SourceJob) o;
                    if (sj.pending(lastBarrier.id())) {
                        if (Report.should_report(Report.frontend, 3)) {
                            Report.report(3, new StringBuffer().append("Running ").append(sj).append(" to ").append(lastBarrier.id()).append(" for ").append(srcJob).toString());
                        }
                        runToPass(sj, lastBarrier.id());
                    }
                }
            }
        }
        if (pass instanceof GlobalBarrierPass) {
            LinkedList barrierWorklist = new LinkedList(this.jobs.values());
            while (!barrierWorklist.isEmpty()) {
                Object o2 = barrierWorklist.removeFirst();
                if (o2 != COMPLETED_JOB) {
                    SourceJob sj2 = (SourceJob) o2;
                    if (!sj2.completed(pass.id()) && sj2.nextPass() != sj2.passByID(pass.id())) {
                        Pass beforeGlobal = sj2.getPreviousTo(pass.id());
                        if (Report.should_report(Report.frontend, 3)) {
                            Report.report(3, new StringBuffer().append("Running ").append(sj2).append(" to ").append(beforeGlobal.id()).append(" for ").append(srcJob).toString());
                        }
                        while (!sj2.pendingPasses().isEmpty()) {
                            Pass p = (Pass) sj2.pendingPasses().get(0);
                            runPass(sj2, p);
                            if (p == beforeGlobal) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static String str(boolean okay) {
        if (okay) {
            return "done";
        }
        return "failed";
    }

    @Override // polyglot.frontend.ExtensionInfo
    public String[] fileExtensions() {
        String[] sx = getOptions() == null ? null : getOptions().source_ext;
        if (sx == null) {
            sx = defaultFileExtensions();
        }
        if (sx.length == 0) {
            return defaultFileExtensions();
        }
        return sx;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public String[] defaultFileExtensions() {
        String ext = defaultFileExtension();
        return new String[]{ext};
    }

    @Override // polyglot.frontend.ExtensionInfo
    public SourceLoader sourceLoader() {
        if (this.source_loader == null) {
            this.source_loader = new SourceLoader(this, getOptions().source_path);
        }
        return this.source_loader;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public TargetFactory targetFactory() {
        if (this.target_factory == null) {
            this.target_factory = new TargetFactory(getOptions().output_directory, getOptions().output_ext, getOptions().output_stdout);
        }
        return this.target_factory;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public TypeSystem typeSystem() {
        if (this.ts == null) {
            this.ts = createTypeSystem();
        }
        return this.ts;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public NodeFactory nodeFactory() {
        if (this.nf == null) {
            this.nf = createNodeFactory();
        }
        return this.nf;
    }

    public JobExt jobExt() {
        return null;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void addDependencyToCurrentJob(Source s) {
        if (s == null) {
            return;
        }
        if (this.currentJob != null) {
            Object o = this.jobs.get(s);
            if (o != COMPLETED_JOB) {
                if (Report.should_report(Report.frontend, 2)) {
                    Report.report(2, new StringBuffer().append("Adding dependency from ").append(this.currentJob.source()).append(" to ").append(s).toString());
                }
                this.currentJob.sourceJob().addDependency(s);
                return;
            }
            return;
        }
        throw new InternalCompilerError("No current job!");
    }

    @Override // polyglot.frontend.ExtensionInfo
    public SourceJob addJob(Source source) {
        return addJob(source, null);
    }

    @Override // polyglot.frontend.ExtensionInfo
    public SourceJob addJob(Source source, Node ast) {
        SourceJob job;
        Object o = this.jobs.get(source);
        if (o == COMPLETED_JOB) {
            return null;
        }
        if (o == null) {
            job = createSourceJob(source, ast);
            this.jobs.put(source, job);
            this.worklist.addLast(job);
            if (Report.should_report(Report.frontend, 3)) {
                Report.report(3, new StringBuffer().append("Adding job for ").append(source).append(" at the ").append("request of job ").append(this.currentJob).toString());
            }
        } else {
            job = (SourceJob) o;
        }
        if (this.currentJob instanceof SourceJob) {
            ((SourceJob) this.currentJob).addDependency(source);
        }
        return job;
    }

    protected SourceJob createSourceJob(Source source, Node ast) {
        return new SourceJob(this, jobExt(), source, ast);
    }

    protected Job createJob(Node ast, Context context, Job outer, Pass.ID begin, Pass.ID end) {
        return new InnerJob(this, jobExt(), ast, context, outer, begin, end);
    }

    @Override // polyglot.frontend.ExtensionInfo
    public Job spawnJob(Context c, Node ast, Job outerJob, Pass.ID begin, Pass.ID end) {
        Job j = createJob(ast, c, outerJob, begin, end);
        if (Report.should_report(Report.frontend, 1)) {
            Report.report(1, new StringBuffer().append(this).append(" spawning ").append(j).toString());
        }
        runAllPasses(j);
        return j;
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void replacePass(List passes, Pass.ID id, List newPasses) {
        ListIterator i = passes.listIterator();
        while (i.hasNext()) {
            Pass p = (Pass) i.next();
            if (p.id() == id) {
                if (p instanceof BarrierPass) {
                    throw new InternalCompilerError("Cannot replace a barrier pass.");
                }
                i.remove();
                for (Object obj : newPasses) {
                    i.add(obj);
                }
                return;
            }
        }
        throw new InternalCompilerError(new StringBuffer().append("Pass ").append(id).append(" not found.").toString());
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void removePass(List passes, Pass.ID id) {
        ListIterator i = passes.listIterator();
        while (i.hasNext()) {
            Pass p = (Pass) i.next();
            if (p.id() == id) {
                if (p instanceof BarrierPass) {
                    throw new InternalCompilerError("Cannot remove a barrier pass.");
                }
                i.remove();
                return;
            }
        }
        throw new InternalCompilerError(new StringBuffer().append("Pass ").append(id).append(" not found.").toString());
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void beforePass(List passes, Pass.ID id, List newPasses) {
        ListIterator i = passes.listIterator();
        while (i.hasNext()) {
            Pass p = (Pass) i.next();
            if (p.id() == id) {
                i.previous();
                for (Object obj : newPasses) {
                    i.add(obj);
                }
                return;
            }
        }
        throw new InternalCompilerError(new StringBuffer().append("Pass ").append(id).append(" not found.").toString());
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void afterPass(List passes, Pass.ID id, List newPasses) {
        ListIterator i = passes.listIterator();
        while (i.hasNext()) {
            Pass p = (Pass) i.next();
            if (p.id() == id) {
                for (Object obj : newPasses) {
                    i.add(obj);
                }
                return;
            }
        }
        throw new InternalCompilerError(new StringBuffer().append("Pass ").append(id).append(" not found.").toString());
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void replacePass(List passes, Pass.ID id, Pass pass) {
        replacePass(passes, id, Collections.singletonList(pass));
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void beforePass(List passes, Pass.ID id, Pass pass) {
        beforePass(passes, id, Collections.singletonList(pass));
    }

    @Override // polyglot.frontend.ExtensionInfo
    public void afterPass(List passes, Pass.ID id, Pass pass) {
        afterPass(passes, id, Collections.singletonList(pass));
    }

    @Override // polyglot.frontend.ExtensionInfo
    public List passes(Job job, Pass.ID begin, Pass.ID end) {
        List l = passes(job);
        Pass p = null;
        Iterator i = l.iterator();
        while (i.hasNext()) {
            p = (Pass) i.next();
            if (begin == p.id()) {
                break;
            } else if (!(p instanceof BarrierPass)) {
                i.remove();
            }
        }
        while (p.id() != end && i.hasNext()) {
            p = (Pass) i.next();
        }
        while (i.hasNext()) {
            Pass pass = (Pass) i.next();
            i.remove();
        }
        return l;
    }

    public String toString() {
        return new StringBuffer().append(getClass().getName()).append(" worklist=").append(this.worklist).toString();
    }

    @Override // polyglot.frontend.ExtensionInfo
    public ClassFile createClassFile(File classFileSource, byte[] code) {
        return new ClassFile(classFileSource, code, this);
    }
}
