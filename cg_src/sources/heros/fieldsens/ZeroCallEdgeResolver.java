package heros.fieldsens;

import heros.fieldsens.FlowFunction;
import heros.fieldsens.structs.WrappedFactAtStatement;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/ZeroCallEdgeResolver.class */
public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {
    private ZeroHandler<Field> zeroHandler;

    @Override // heros.fieldsens.CallEdgeResolver
    public /* bridge */ /* synthetic */ boolean hasIncomingEdges() {
        return super.hasIncomingEdges();
    }

    @Override // heros.fieldsens.CallEdgeResolver
    public /* bridge */ /* synthetic */ void applySummaries(WrappedFactAtStatement wrappedFactAtStatement) {
        super.applySummaries(wrappedFactAtStatement);
    }

    public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler, Debugger<Field, Fact, Stmt, Method> debugger) {
        super(analyzer, debugger);
        this.zeroHandler = zeroHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZeroCallEdgeResolver<Field, Fact, Stmt, Method> copyWithAnalyzer(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
        return new ZeroCallEdgeResolver<>(analyzer, this.zeroHandler, this.debugger);
    }

    @Override // heros.fieldsens.ResolverTemplate, heros.fieldsens.Resolver
    public void resolve(FlowFunction.Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {
        if (this.zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath<>()))) {
            callback.interest(this.analyzer, this);
        }
    }

    @Override // heros.fieldsens.Resolver
    public void interest(Resolver<Field, Fact, Stmt, Method> resolver) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // heros.fieldsens.ResolverTemplate
    public ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
        return this;
    }

    @Override // heros.fieldsens.CallEdgeResolver
    public String toString() {
        return "[0-Resolver" + super.toString() + "]";
    }

    public int hashCode() {
        int result = (31 * 1) + (this.zeroHandler == null ? 0 : this.zeroHandler.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZeroCallEdgeResolver other = (ZeroCallEdgeResolver) obj;
        if (this.zeroHandler == null) {
            if (other.zeroHandler != null) {
                return false;
            }
            return true;
        } else if (!this.zeroHandler.equals(other.zeroHandler)) {
            return false;
        } else {
            return true;
        }
    }
}
