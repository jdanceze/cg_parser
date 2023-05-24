package heros.fieldsens;

import heros.fieldsens.FlowFunction;
import heros.fieldsens.structs.WrappedFact;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/AccessPathHandler.class */
public class AccessPathHandler<Field, Fact, Stmt, Method> {
    private AccessPath<Field> accessPath;
    private Resolver<Field, Fact, Stmt, Method> resolver;
    private Debugger<Field, Fact, Stmt, Method> debugger;

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/AccessPathHandler$ResultBuilder.class */
    public interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {
        FlowFunction.ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction factabstraction);
    }

    public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver, Debugger<Field, Fact, Stmt, Method> debugger) {
        this.accessPath = accessPath;
        this.resolver = resolver;
        this.debugger = debugger;
    }

    public boolean canRead(Field field) {
        return this.accessPath.canRead(field);
    }

    public boolean mayCanRead(Field field) {
        return this.accessPath.canRead(field) || (this.accessPath.hasEmptyAccessPath() && !this.accessPath.isAccessInExclusions(field));
    }

    public boolean mayBeEmpty() {
        return this.accessPath.hasEmptyAccessPath();
    }

    public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
        return new FlowFunction.ConstrainedFact<>(new WrappedFact(fact, this.accessPath, this.resolver));
    }

    public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
        return new FlowFunction.ConstrainedFact<>(new WrappedFact(fact, new AccessPath(), new ZeroCallEdgeResolver(this.resolver.analyzer, zeroHandler, this.debugger)));
    }

    public ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
        return new ResultBuilder<Field, Fact, Stmt, Method>() { // from class: heros.fieldsens.AccessPathHandler.1
            @Override // heros.fieldsens.AccessPathHandler.ResultBuilder
            public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
                return new FlowFunction.ConstrainedFact<>(new WrappedFact(fact, AccessPathHandler.this.accessPath.prepend(field), AccessPathHandler.this.resolver));
            }
        };
    }

    public ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
        if (mayCanRead(field)) {
            return new ResultBuilder<Field, Fact, Stmt, Method>() { // from class: heros.fieldsens.AccessPathHandler.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // heros.fieldsens.AccessPathHandler.ResultBuilder
                public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
                    if (AccessPathHandler.this.canRead(field)) {
                        return new FlowFunction.ConstrainedFact<>(new WrappedFact(fact, AccessPathHandler.this.accessPath.removeFirst(), AccessPathHandler.this.resolver));
                    }
                    return new FlowFunction.ConstrainedFact<>(new WrappedFact(fact, new AccessPath(), AccessPathHandler.this.resolver), new FlowFunction.ReadFieldConstraint(field));
                }
            };
        }
        throw new IllegalArgumentException("Cannot read field " + field);
    }

    public ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {
        if (mayBeEmpty()) {
            return new ResultBuilder<Field, Fact, Stmt, Method>() { // from class: heros.fieldsens.AccessPathHandler.3
                @Override // heros.fieldsens.AccessPathHandler.ResultBuilder
                public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
                    if (AccessPathHandler.this.accessPath.isAccessInExclusions(field)) {
                        return new FlowFunction.ConstrainedFact<>(new WrappedFact(fact, AccessPathHandler.this.accessPath, AccessPathHandler.this.resolver));
                    }
                    return new FlowFunction.ConstrainedFact<>(new WrappedFact(fact, AccessPathHandler.this.accessPath.appendExcludedFieldReference(field), AccessPathHandler.this.resolver), new FlowFunction.WriteFieldConstraint(field));
                }
            };
        }
        throw new IllegalArgumentException("Cannot write field " + field);
    }
}
