package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/LazyClassInitializer.class */
public interface LazyClassInitializer {
    boolean fromClassFile();

    void initConstructors(ParsedClassType parsedClassType);

    void initMethods(ParsedClassType parsedClassType);

    void initFields(ParsedClassType parsedClassType);

    void initMemberClasses(ParsedClassType parsedClassType);

    void initInterfaces(ParsedClassType parsedClassType);
}
