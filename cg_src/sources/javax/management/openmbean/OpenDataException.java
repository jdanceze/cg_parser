package javax.management.openmbean;

import java.io.Serializable;
import javax.management.JMException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/OpenDataException.class */
public class OpenDataException extends JMException implements Serializable {
    private static final long serialVersionUID = 8346311255433349870L;

    public OpenDataException() {
    }

    public OpenDataException(String str) {
        super(str);
    }
}
