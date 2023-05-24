package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.util.Hashtable;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/OrionDeploymentTool.class */
public class OrionDeploymentTool extends GenericDeploymentTool {
    protected static final String ORION_DD = "orion-ejb-jar.xml";
    private String jarSuffix = ".jar";

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void addVendorFiles(Hashtable<String, File> ejbFiles, String baseName) {
        String ddPrefix = usingBaseJarName() ? "" : baseName;
        File orionDD = new File(getConfig().descriptorDir, ddPrefix + ORION_DD);
        if (orionDD.exists()) {
            ejbFiles.put("META-INF/orion-ejb-jar.xml", orionDD);
        } else {
            log("Unable to locate Orion deployment descriptor. It was expected to be in " + orionDD.getPath(), 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public File getVendorOutputJarFile(String baseName) {
        return new File(getDestDir(), baseName + this.jarSuffix);
    }
}
