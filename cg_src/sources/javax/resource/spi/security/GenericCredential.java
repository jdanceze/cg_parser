package javax.resource.spi.security;

import javax.resource.spi.SecurityException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/security/GenericCredential.class */
public interface GenericCredential {
    String getName();

    String getMechType();

    byte[] getCredentialData() throws SecurityException;

    boolean equals(Object obj);

    int hashCode();
}
