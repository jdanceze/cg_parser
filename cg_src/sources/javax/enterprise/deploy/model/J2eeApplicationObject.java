package javax.enterprise.deploy.model;

import javax.enterprise.deploy.shared.ModuleType;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/model/J2eeApplicationObject.class */
public interface J2eeApplicationObject extends DeployableObject {
    DeployableObject getDeployableObject(String str);

    DeployableObject[] getDeployableObjects(ModuleType moduleType);

    DeployableObject[] getDeployableObjects();

    String[] getModuleUris(ModuleType moduleType);

    String[] getModuleUris();

    DDBean[] getChildBean(ModuleType moduleType, String str);

    String[] getText(ModuleType moduleType, String str);

    void addXpathListener(ModuleType moduleType, String str, XpathListener xpathListener);

    void removeXpathListener(ModuleType moduleType, String str, XpathListener xpathListener);
}
