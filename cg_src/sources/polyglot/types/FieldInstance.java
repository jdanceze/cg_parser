package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/FieldInstance.class */
public interface FieldInstance extends VarInstance, MemberInstance {
    FieldInstance flags(Flags flags);

    FieldInstance name(String str);

    FieldInstance type(Type type);

    FieldInstance container(ReferenceType referenceType);

    FieldInstance constantValue(Object obj);

    void setConstantValue(Object obj);
}
