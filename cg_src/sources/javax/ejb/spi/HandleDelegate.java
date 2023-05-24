package javax.ejb.spi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/spi/HandleDelegate.class */
public interface HandleDelegate {
    void writeEJBObject(EJBObject eJBObject, ObjectOutputStream objectOutputStream) throws IOException;

    EJBObject readEJBObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException;

    void writeEJBHome(EJBHome eJBHome, ObjectOutputStream objectOutputStream) throws IOException;

    EJBHome readEJBHome(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException;
}
