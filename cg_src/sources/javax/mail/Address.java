package javax.mail;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Address.class */
public abstract class Address implements Serializable {
    public abstract String getType();

    public abstract String toString();

    public abstract boolean equals(Object obj);
}
