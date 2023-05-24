package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.util.Hashtable;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.ejb.EjbJar;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/JbossDeploymentTool.class */
public class JbossDeploymentTool extends GenericDeploymentTool {
    protected static final String JBOSS_DD = "jboss.xml";
    protected static final String JBOSS_CMP10D = "jaws.xml";
    protected static final String JBOSS_CMP20D = "jbosscmp-jdbc.xml";
    private String jarSuffix = ".jar";

    public void setSuffix(String inString) {
        this.jarSuffix = inString;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
        File jbossDD = new File(getConfig().descriptorDir, ddPrefix + JBOSS_DD);
        if (jbossDD.exists()) {
            ejbFiles.put("META-INF/jboss.xml", jbossDD);
            String descriptorFileName = JBOSS_CMP10D;
            if (EjbJar.CMPVersion.CMP2_0.equals(getParent().getCmpversion())) {
                descriptorFileName = JBOSS_CMP20D;
            }
            File jbossCMPD = new File(getConfig().descriptorDir, ddPrefix + descriptorFileName);
            if (jbossCMPD.exists()) {
                ejbFiles.put("META-INF/" + descriptorFileName, jbossCMPD);
                return;
            } else {
                log("Unable to locate jboss cmp descriptor. It was expected to be in " + jbossCMPD.getPath(), 3);
                return;
            }
        }
        log("Unable to locate jboss deployment descriptor. It was expected to be in " + jbossDD.getPath(), 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public File getVendorOutputJarFile(String baseName) {
        if (getDestDir() == null && getParent().getDestdir() == null) {
            throw new BuildException("DestDir not specified");
        }
        if (getDestDir() == null) {
            return new File(getParent().getDestdir(), baseName + this.jarSuffix);
        }
        return new File(getDestDir(), baseName + this.jarSuffix);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool, org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void validateConfigured() throws BuildException {
    }

    private EjbJar getParent() {
        return (EjbJar) getTask();
    }
}
