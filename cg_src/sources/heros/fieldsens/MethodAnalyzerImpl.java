package heros.fieldsens;

import heros.fieldsens.structs.WrappedFact;
import heros.fieldsens.structs.WrappedFactAtStatement;
import heros.utilities.DefaultValueMap;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/MethodAnalyzerImpl.class */
public class MethodAnalyzerImpl<Field, Fact, Stmt, Method> implements MethodAnalyzer<Field, Fact, Stmt, Method> {
    private Method method;
    private DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>> perSourceAnalyzer = new DefaultValueMap<Fact, PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method>>() { // from class: heros.fieldsens.MethodAnalyzerImpl.1
        @Override // heros.utilities.DefaultValueMap
        protected /* bridge */ /* synthetic */ Object createItem(Object obj) {
            return createItem((AnonymousClass1) obj);
        }

        @Override // heros.utilities.DefaultValueMap
        protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> createItem(Fact key) {
            return new PerAccessPathMethodAnalyzer<>(MethodAnalyzerImpl.this.method, key, MethodAnalyzerImpl.this.context, MethodAnalyzerImpl.this.debugger);
        }
    };
    private Context<Field, Fact, Stmt, Method> context;
    private Debugger<Field, Fact, Stmt, Method> debugger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MethodAnalyzerImpl(Method method, Context<Field, Fact, Stmt, Method> context, Debugger<Field, Fact, Stmt, Method> debugger) {
        this.method = method;
        this.context = context;
        this.debugger = debugger;
    }

    @Override // heros.fieldsens.MethodAnalyzer
    public void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> incEdge) {
        WrappedFact<Field, Fact, Stmt, Method> calleeSourceFact = incEdge.getCalleeSourceFact();
        PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer = this.perSourceAnalyzer.getOrCreate(calleeSourceFact.getFact());
        analyzer.addIncomingEdge(incEdge);
    }

    @Override // heros.fieldsens.MethodAnalyzer
    public void addInitialSeed(Stmt startPoint, Fact val) {
        this.perSourceAnalyzer.getOrCreate(val).addInitialSeed(startPoint);
    }

    @Override // heros.fieldsens.MethodAnalyzer
    public void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> target, Stmt callSite) {
        this.perSourceAnalyzer.getOrCreate(this.context.zeroValue).scheduleUnbalancedReturnEdgeTo(target);
    }
}
