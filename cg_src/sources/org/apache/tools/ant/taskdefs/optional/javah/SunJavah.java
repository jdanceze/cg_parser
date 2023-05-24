package org.apache.tools.ant.taskdefs.optional.javah;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.ExecuteJava;
import org.apache.tools.ant.taskdefs.optional.Javah;
import org.apache.tools.ant.taskdefs.optional.clearcase.CCRmtype;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/javah/SunJavah.class */
public class SunJavah implements JavahAdapter {
    public static final String IMPLEMENTATION_NAME = "sun";

    @Override // org.apache.tools.ant.taskdefs.optional.javah.JavahAdapter
    public boolean compile(Javah javah) throws BuildException {
        Class<?> c;
        Commandline cmd = setupJavahCommand(javah);
        ExecuteJava ej = new ExecuteJava();
        try {
            try {
                c = Class.forName("com.sun.tools.javah.oldjavah.Main");
            } catch (ClassNotFoundException e) {
                c = Class.forName("com.sun.tools.javah.Main");
            }
            cmd.setExecutable(c.getName());
            ej.setJavaCommand(cmd);
            File f = Locator.getClassSource(c);
            if (f != null) {
                ej.setClasspath(new Path(javah.getProject(), f.getPath()));
            }
            return ej.fork(javah) == 0;
        } catch (ClassNotFoundException ex) {
            throw new BuildException("Can't load javah", ex, javah.getLocation());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Commandline setupJavahCommand(Javah javah) {
        Commandline cmd = new Commandline();
        if (javah.getDestdir() != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(javah.getDestdir());
        }
        if (javah.getOutputfile() != null) {
            cmd.createArgument().setValue("-o");
            cmd.createArgument().setFile(javah.getOutputfile());
        }
        if (javah.getClasspath() != null) {
            cmd.createArgument().setValue("-classpath");
            cmd.createArgument().setPath(javah.getClasspath());
        }
        if (javah.getVerbose()) {
            cmd.createArgument().setValue(SOSCmd.FLAG_VERBOSE);
        }
        if (javah.getOld()) {
            cmd.createArgument().setValue("-old");
        }
        if (javah.getForce()) {
            cmd.createArgument().setValue(CCRmtype.FLAG_FORCE);
        }
        if (javah.getStubs() && !javah.getOld()) {
            throw new BuildException("stubs only available in old mode.", javah.getLocation());
        }
        if (javah.getStubs()) {
            cmd.createArgument().setValue("-stubs");
        }
        Path bcp = new Path(javah.getProject());
        if (javah.getBootclasspath() != null) {
            bcp.append(javah.getBootclasspath());
        }
        Path bcp2 = bcp.concatSystemBootClasspath(Definer.OnError.POLICY_IGNORE);
        if (bcp2.size() > 0) {
            cmd.createArgument().setValue("-bootclasspath");
            cmd.createArgument().setPath(bcp2);
        }
        cmd.addArguments(javah.getCurrentArgs());
        javah.logAndAddFiles(cmd);
        return cmd;
    }
}
