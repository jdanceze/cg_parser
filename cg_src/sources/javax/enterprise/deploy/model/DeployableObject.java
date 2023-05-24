package javax.enterprise.deploy.model;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.shared.ModuleType;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/model/DeployableObject.class */
public interface DeployableObject {
    ModuleType getType();

    DDBeanRoot getDDBeanRoot();

    DDBean[] getChildBean(String str);

    String[] getText(String str);

    Class getClassFromScope(String str);

    String getModuleDTDVersion();

    DDBeanRoot getDDBeanRoot(String str) throws FileNotFoundException, DDBeanCreateException;

    Enumeration entries();

    InputStream getEntry(String str);
}
