package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/Qualifier.class */
public interface Qualifier extends TypeObject {
    boolean isPackage();

    Package toPackage();

    boolean isType();

    Type toType();
}
