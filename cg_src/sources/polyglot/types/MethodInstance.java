package polyglot.types;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/MethodInstance.class */
public interface MethodInstance extends ProcedureInstance {
    Type returnType();

    MethodInstance returnType(Type type);

    String name();

    MethodInstance name(String str);

    MethodInstance flags(Flags flags);

    MethodInstance formalTypes(List list);

    MethodInstance throwTypes(List list);

    MethodInstance container(ReferenceType referenceType);

    List overrides();

    boolean canOverride(MethodInstance methodInstance);

    void checkOverride(MethodInstance methodInstance) throws SemanticException;

    List implemented();

    boolean isSameMethod(MethodInstance methodInstance);

    boolean methodCallValid(String str, List list);

    List overridesImpl();

    boolean canOverrideImpl(MethodInstance methodInstance, boolean z) throws SemanticException;

    List implementedImpl(ReferenceType referenceType);

    boolean isSameMethodImpl(MethodInstance methodInstance);

    boolean methodCallValidImpl(String str, List list);
}
