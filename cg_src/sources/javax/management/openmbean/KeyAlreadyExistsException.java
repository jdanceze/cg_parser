package javax.management.openmbean;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/KeyAlreadyExistsException.class */
public class KeyAlreadyExistsException extends IllegalArgumentException implements Serializable {
    private static final long serialVersionUID = 1845183636745282866L;

    public KeyAlreadyExistsException() {
    }

    public KeyAlreadyExistsException(String str) {
        super(str);
    }
}
