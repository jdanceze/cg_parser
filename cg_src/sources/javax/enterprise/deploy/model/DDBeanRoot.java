package javax.enterprise.deploy.model;

import javax.enterprise.deploy.shared.ModuleType;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/model/DDBeanRoot.class */
public interface DDBeanRoot extends DDBean {
    ModuleType getType();

    DeployableObject getDeployableObject();

    String getModuleDTDVersion();

    String getDDBeanRootVersion();

    @Override // javax.enterprise.deploy.model.DDBean
    String getXpath();

    String getFilename();
}
