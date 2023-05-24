package polyglot.types;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/Package.class */
public interface Package extends Qualifier, Named {
    Package prefix();

    String translate(Resolver resolver);
}
