package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ClassResolver.class */
public abstract class ClassResolver implements Resolver {
    @Override // polyglot.types.Resolver
    public abstract Named find(String str) throws SemanticException;
}
