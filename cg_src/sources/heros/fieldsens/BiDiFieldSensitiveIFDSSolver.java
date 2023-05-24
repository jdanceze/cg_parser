package heros.fieldsens;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Sets;
import heros.InterproceduralCFG;
import heros.fieldsens.SourceStmtAnnotatedMethodAnalyzer;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/BiDiFieldSensitiveIFDSSolver.class */
public class BiDiFieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I extends InterproceduralCFG<Stmt, Method>> {
    private FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I> forwardSolver;
    private FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I> backwardSolver;
    private Scheduler scheduler;
    private SynchronizerImpl<Stmt> forwardSynchronizer = new SynchronizerImpl<>();
    private SynchronizerImpl<Stmt> backwardSynchronizer = new SynchronizerImpl<>();

    public BiDiFieldSensitiveIFDSSolver(IFDSTabulationProblem<Stmt, Field, Fact, Method, I> forwardProblem, IFDSTabulationProblem<Stmt, Field, Fact, Method, I> backwardProblem, FactMergeHandler<Fact> factHandler, Debugger<Field, Fact, Stmt, Method> debugger, Scheduler scheduler) {
        this.scheduler = scheduler;
        ((SynchronizerImpl) this.forwardSynchronizer).otherSynchronizer = this.backwardSynchronizer;
        ((SynchronizerImpl) this.backwardSynchronizer).otherSynchronizer = this.forwardSynchronizer;
        this.forwardSolver = createSolver(forwardProblem, factHandler, debugger, this.forwardSynchronizer);
        this.backwardSolver = createSolver(backwardProblem, factHandler, debugger, this.backwardSynchronizer);
    }

    private FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I> createSolver(IFDSTabulationProblem<Stmt, Field, Fact, Method, I> problem, FactMergeHandler<Fact> factHandler, Debugger<Field, Fact, Stmt, Method> debugger, final SynchronizerImpl<Stmt> synchronizer) {
        return (FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I>) new FieldSensitiveIFDSSolver<Field, Fact, Stmt, Method, I>(problem, factHandler, debugger, this.scheduler) { // from class: heros.fieldsens.BiDiFieldSensitiveIFDSSolver.1
            @Override // heros.fieldsens.FieldSensitiveIFDSSolver
            protected MethodAnalyzer<Field, Fact, Stmt, Method> createMethodAnalyzer(Method method) {
                return new SourceStmtAnnotatedMethodAnalyzer(method, this.context, synchronizer, this.debugger);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/BiDiFieldSensitiveIFDSSolver$SynchronizerImpl.class */
    public static class SynchronizerImpl<Stmt> implements SourceStmtAnnotatedMethodAnalyzer.Synchronizer<Stmt> {
        private SynchronizerImpl<Stmt> otherSynchronizer;
        private Set<Stmt> leakedSources;
        private HashMultimap<Stmt, Runnable> pausedJobs;

        private SynchronizerImpl() {
            this.leakedSources = Sets.newHashSet();
            this.pausedJobs = HashMultimap.create();
        }

        @Override // heros.fieldsens.SourceStmtAnnotatedMethodAnalyzer.Synchronizer
        public void synchronizeOnStmt(Stmt stmt, Runnable job) {
            this.leakedSources.add(stmt);
            if (this.otherSynchronizer.leakedSources.contains(stmt)) {
                job.run();
                for (Runnable runnable : this.otherSynchronizer.pausedJobs.get((Object) stmt)) {
                    runnable.run();
                }
                this.otherSynchronizer.pausedJobs.removeAll((Object) stmt);
                return;
            }
            this.pausedJobs.put(stmt, job);
        }
    }
}
