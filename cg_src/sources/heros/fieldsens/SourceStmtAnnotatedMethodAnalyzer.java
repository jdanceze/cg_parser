package heros.fieldsens;

import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/SourceStmtAnnotatedMethodAnalyzer.class */
public class SourceStmtAnnotatedMethodAnalyzer<Field, Fact, Stmt, Method> implements MethodAnalyzer<Field, Fact, Stmt, Method> {
    private Method method;
    private DefaultValueMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = new DefaultValueMap<Key<Fact, Stmt>, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() { // from class: heros.fieldsens.SourceStmtAnnotatedMethodAnalyzer.1
        @Override // heros.utilities.DefaultValueMap
        protected /* bridge */ /* synthetic */ Object createItem(Object obj) {
            return createItem((Key) ((Key) obj));
        }

        protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Key<Fact, Stmt> key) {
            return new PerAccessPathMethodAnalyzer<>(SourceStmtAnnotatedMethodAnalyzer.this.method, ((Key) key).fact, SourceStmtAnnotatedMethodAnalyzer.this.context, SourceStmtAnnotatedMethodAnalyzer.this.debugger);
        }
    };
    private Context<Field, Fact, Stmt, Method> context;
    private Synchronizer<Stmt> synchronizer;
    private Debugger<Field, Fact, Stmt, Method> debugger;

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/SourceStmtAnnotatedMethodAnalyzer$Synchronizer.class */
    public interface Synchronizer<Stmt> {
        void synchronizeOnStmt(Stmt stmt, Runnable runnable);
    }

    public SourceStmtAnnotatedMethodAnalyzer(Method method, Context<Field, Fact, Stmt, Method> context, Synchronizer<Stmt> synchronizer, Debugger<Field, Fact, Stmt, Method> debugger) {
        this.method = method;
        this.context = context;
        this.synchronizer = synchronizer;
        this.debugger = debugger;
    }

    @Override // heros.fieldsens.MethodAnalyzer
    public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {
        WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
        Key<Fact, Stmt> key = new Key<>(calleeSourceFact.getFact(), null);
        PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = this.perSourceAnalyzer.getOrCreate(key);
        analyzer.addIncomingEdge(incEdge);
    }

    @Override // heros.fieldsens.MethodAnalyzer
    public void addInitialSeed(Stmt startPoint, Fact val) {
        Key<Fact, Stmt> key = new Key<>(val, startPoint);
        this.perSourceAnalyzer.getOrCreate(key).addInitialSeed(startPoint);
    }

    @Override // heros.fieldsens.MethodAnalyzer
    public void addUnbalancedReturnFlow(final WrappedFactAtStatement<Field, Fact, Stmt, Method> target, final Stmt callSite) {
        this.synchronizer.synchronizeOnStmt(callSite, new Runnable() { // from class: heros.fieldsens.SourceStmtAnnotatedMethodAnalyzer.2
            @Override // java.lang.Runnable
            public void run() {
                Key<Fact, Stmt> key = new Key<>(SourceStmtAnnotatedMethodAnalyzer.this.context.zeroValue, callSite);
                ((PerAccessPathMethodAnalyzer) SourceStmtAnnotatedMethodAnalyzer.this.perSourceAnalyzer.getOrCreate(key)).scheduleUnbalancedReturnEdgeTo(target);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/SourceStmtAnnotatedMethodAnalyzer$Key.class */
    public static class Key<Fact, Stmt> {
        private Fact fact;
        private Stmt stmt;

        private Key(Fact fact, Stmt stmt) {
            this.fact = fact;
            this.stmt = stmt;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.fact == null ? 0 : this.fact.hashCode());
            return (31 * result) + (this.stmt == null ? 0 : this.stmt.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Key other = (Key) obj;
            if (this.fact == null) {
                if (other.fact != null) {
                    return false;
                }
            } else if (!this.fact.equals(other.fact)) {
                return false;
            }
            if (this.stmt == null) {
                if (other.stmt != null) {
                    return false;
                }
                return true;
            } else if (!this.stmt.equals(other.stmt)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
