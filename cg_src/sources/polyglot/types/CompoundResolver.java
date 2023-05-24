package polyglot.types;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/CompoundResolver.class */
public class CompoundResolver implements TopLevelResolver {
    TopLevelResolver head;
    TopLevelResolver tail;

    public CompoundResolver(TopLevelResolver head, TopLevelResolver tail) {
        this.head = head;
        this.tail = tail;
    }

    public String toString() {
        return new StringBuffer().append("(compound ").append(this.head).append(Instruction.argsep).append(this.tail).append(")").toString();
    }

    @Override // polyglot.types.TopLevelResolver
    public boolean packageExists(String name) {
        return this.head.packageExists(name) || this.tail.packageExists(name);
    }

    @Override // polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        try {
            return this.head.find(name);
        } catch (NoClassException e) {
            return this.tail.find(name);
        }
    }
}
