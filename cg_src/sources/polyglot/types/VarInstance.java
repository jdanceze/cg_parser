package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/VarInstance.class */
public interface VarInstance extends TypeObject {
    Flags flags();

    String name();

    Type type();

    Object constantValue();

    boolean isConstant();

    void setType(Type type);

    void setFlags(Flags flags);
}
