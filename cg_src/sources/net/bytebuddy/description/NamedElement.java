package net.bytebuddy.description;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/NamedElement.class */
public interface NamedElement {
    public static final String NO_NAME = null;
    public static final String EMPTY_NAME = "";

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/NamedElement$WithDescriptor.class */
    public interface WithDescriptor extends NamedElement {
        public static final String NON_GENERIC_SIGNATURE = null;

        String getDescriptor();

        String getGenericSignature();
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/NamedElement$WithGenericName.class */
    public interface WithGenericName extends WithRuntimeName {
        String toGenericString();
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/NamedElement$WithOptionalName.class */
    public interface WithOptionalName extends NamedElement {
        boolean isNamed();
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/NamedElement$WithRuntimeName.class */
    public interface WithRuntimeName extends NamedElement {
        String getName();

        String getInternalName();
    }

    String getActualName();
}
