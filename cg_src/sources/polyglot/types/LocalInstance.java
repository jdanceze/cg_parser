package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/LocalInstance.class */
public interface LocalInstance extends VarInstance {
    LocalInstance flags(Flags flags);

    LocalInstance name(String str);

    LocalInstance type(Type type);

    LocalInstance constantValue(Object obj);

    void setConstantValue(Object obj);
}
