package org.apache.tools.ant.taskdefs.optional.j2ee;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/j2ee/JonasHotDeploymentTool.class */
public class JonasHotDeploymentTool extends GenericHotDeploymentTool implements HotDeploymentTool {
    protected static final String DEFAULT_ORB = "RMI";
    private static final String JONAS_DEPLOY_CLASS_NAME = "org.objectweb.jonas.adm.JonasAdmin";
    private static final String[] VALID_ACTIONS = {HotDeploymentTool.ACTION_DELETE, HotDeploymentTool.ACTION_DEPLOY, HotDeploymentTool.ACTION_LIST, HotDeploymentTool.ACTION_UNDEPLOY, "update"};
    private File jonasroot;
    private String orb = null;
    private String davidHost;
    private int davidPort;

    public void setDavidhost(String inValue) {
        this.davidHost = inValue;
    }

    public void setDavidport(int inValue) {
        this.davidPort = inValue;
    }

    public void setJonasroot(File inValue) {
        this.jonasroot = inValue;
    }

    public void setOrb(String inValue) {
        this.orb = inValue;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool
    public Path getClasspath() {
        Path aClassPath = super.getClasspath();
        if (aClassPath == null) {
            aClassPath = new Path(getTask().getProject());
        }
        if (this.orb != null) {
            String aOrbJar = new File(this.jonasroot, "lib/" + this.orb + "_jonas.jar").toString();
            String aConfigDir = new File(this.jonasroot, "config/").toString();
            Path aJOnASOrbPath = new Path(aClassPath.getProject(), aOrbJar + File.pathSeparator + aConfigDir);
            aClassPath.append(aJOnASOrbPath);
        }
        return aClassPath;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.GenericHotDeploymentTool, org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool, org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool
    public void validateAttributes() throws BuildException {
        Java java = getJava();
        String action = getTask().getAction();
        if (action == null) {
            throw new BuildException("The \"action\" attribute must be set");
        }
        if (!isActionValid()) {
            throw new BuildException("Invalid action \"%s\" passed", action);
        }
        if (getClassName() == null) {
            setClassName(JONAS_DEPLOY_CLASS_NAME);
        }
        if (this.jonasroot == null || this.jonasroot.isDirectory()) {
            java.createJvmarg().setValue("-Dinstall.root=" + this.jonasroot);
            java.createJvmarg().setValue("-Djava.security.policy=" + this.jonasroot + "/config/java.policy");
            if ("DAVID".equals(this.orb)) {
                java.createJvmarg().setValue("-Dorg.omg.CORBA.ORBClass=org.objectweb.david.libs.binding.orbs.iiop.IIOPORB");
                java.createJvmarg().setValue("-Dorg.omg.CORBA.ORBSingletonClass=org.objectweb.david.libs.binding.orbs.ORBSingletonClass");
                java.createJvmarg().setValue("-Djavax.rmi.CORBA.StubClass=org.objectweb.david.libs.stub_factories.rmi.StubDelegate");
                java.createJvmarg().setValue("-Djavax.rmi.CORBA.PortableRemoteObjectClass=org.objectweb.david.libs.binding.rmi.ORBPortableRemoteObjectDelegate");
                java.createJvmarg().setValue("-Djavax.rmi.CORBA.UtilClass=org.objectweb.david.libs.helpers.RMIUtilDelegate");
                java.createJvmarg().setValue("-Ddavid.CosNaming.default_method=0");
                java.createJvmarg().setValue("-Ddavid.rmi.ValueHandlerClass=com.sun.corba.se.internal.io.ValueHandlerImpl");
                if (this.davidHost != null) {
                    java.createJvmarg().setValue("-Ddavid.CosNaming.default_host=" + this.davidHost);
                }
                if (this.davidPort != 0) {
                    java.createJvmarg().setValue("-Ddavid.CosNaming.default_port=" + this.davidPort);
                }
            }
        }
        if (getServer() != null) {
            java.createArg().setLine("-n " + getServer());
        }
        if (HotDeploymentTool.ACTION_DEPLOY.equals(action) || "update".equals(action) || "redeploy".equals(action)) {
            java.createArg().setLine("-a " + getTask().getSource());
        } else if (action.equals(HotDeploymentTool.ACTION_DELETE) || action.equals(HotDeploymentTool.ACTION_UNDEPLOY)) {
            java.createArg().setLine("-r " + getTask().getSource());
        } else if (action.equals(HotDeploymentTool.ACTION_LIST)) {
            java.createArg().setValue("-l");
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.j2ee.GenericHotDeploymentTool, org.apache.tools.ant.taskdefs.optional.j2ee.AbstractHotDeploymentTool
    protected boolean isActionValid() {
        String[] strArr;
        String action = getTask().getAction();
        for (String validAction : VALID_ACTIONS) {
            if (action.equals(validAction)) {
                return true;
            }
        }
        return false;
    }
}
