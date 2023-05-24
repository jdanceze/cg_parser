package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.util.Hashtable;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/WeblogicTOPLinkDeploymentTool.class */
public class WeblogicTOPLinkDeploymentTool extends WeblogicDeploymentTool {
    private static final String TL_DTD_LOC = "http://www.objectpeople.com/tlwl/dtd/toplink-cmp_2_5_1.dtd";
    private String toplinkDescriptor;
    private String toplinkDTD;

    public void setToplinkdescriptor(String inString) {
        this.toplinkDescriptor = inString;
    }

    public void setToplinkdtd(String inString) {
        this.toplinkDTD = inString;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public DescriptorHandler getDescriptorHandler(File srcDir) {
        DescriptorHandler handler = super.getDescriptorHandler(srcDir);
        if (this.toplinkDTD != null) {
            handler.registerDTD("-//The Object People, Inc.//DTD TOPLink for WebLogic CMP 2.5.1//EN", this.toplinkDTD);
        } else {
            handler.registerDTD("-//The Object People, Inc.//DTD TOPLink for WebLogic CMP 2.5.1//EN", TL_DTD_LOC);
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.WeblogicDeploymentTool, org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
        super.addVendorFiles(ejbFiles, ddPrefix);
        File toplinkDD = new File(getConfig().descriptorDir, ddPrefix + this.toplinkDescriptor);
        if (toplinkDD.exists()) {
            ejbFiles.put("META-INF/" + this.toplinkDescriptor, toplinkDD);
        } else {
            log("Unable to locate toplink deployment descriptor. It was expected to be in " + toplinkDD.getPath(), 1);
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.WeblogicDeploymentTool, org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool, org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void validateConfigured() throws BuildException {
        super.validateConfigured();
        if (this.toplinkDescriptor == null) {
            throw new BuildException("The toplinkdescriptor attribute must be specified");
        }
    }
}
