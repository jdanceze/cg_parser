package org.apache.tools.ant.taskdefs.optional.jsp.compilers;

import java.io.File;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
import org.apache.tools.ant.taskdefs.optional.jsp.JspMangler;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/compilers/JasperC.class */
public class JasperC extends DefaultJspCompilerAdapter {
    JspMangler mangler;

    public JasperC(JspMangler mangler) {
        this.mangler = mangler;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapter
    public boolean execute() throws BuildException {
        String[] arguments;
        getJspc().log("Using jasper compiler", 3);
        CommandlineJava cmd = setupJasperCommand();
        try {
            try {
                Java java = new Java(this.owner);
                Path p = getClasspath();
                if (getJspc().getClasspath() != null) {
                    getProject().log("using user supplied classpath: " + p, 4);
                } else {
                    getProject().log("using system classpath: " + p, 4);
                }
                java.setClasspath(p);
                java.setDir(getProject().getBaseDir());
                java.setClassname("org.apache.jasper.JspC");
                for (String arg : cmd.getJavaCommand().getArguments()) {
                    java.createArg().setValue(arg);
                }
                java.setFailonerror(getJspc().getFailonerror());
                java.setFork(true);
                java.setTaskName("jasperc");
                java.execute();
                getJspc().deleteEmptyJavaFiles();
                return true;
            } catch (Exception ex) {
                if (ex instanceof BuildException) {
                    throw ((BuildException) ex);
                }
                throw new BuildException("Error running jsp compiler: ", ex, getJspc().getLocation());
            }
        } catch (Throwable th) {
            getJspc().deleteEmptyJavaFiles();
            throw th;
        }
    }

    private CommandlineJava setupJasperCommand() {
        CommandlineJava cmd = new CommandlineJava();
        JspC jspc = getJspc();
        addArg(cmd, "-d", jspc.getDestdir());
        addArg(cmd, "-p", jspc.getPackage());
        if (!isTomcat5x()) {
            addArg(cmd, "-v" + jspc.getVerbose());
        } else {
            getProject().log("this task doesn't support Tomcat 5.x properly, please use the Tomcat provided jspc task instead");
        }
        addArg(cmd, "-uriroot", jspc.getUriroot());
        addArg(cmd, "-uribase", jspc.getUribase());
        addArg(cmd, "-ieplugin", jspc.getIeplugin());
        addArg(cmd, "-webinc", jspc.getWebinc());
        addArg(cmd, "-webxml", jspc.getWebxml());
        addArg(cmd, "-die9");
        if (jspc.isMapped()) {
            addArg(cmd, "-mapped");
        }
        if (jspc.getWebApp() != null) {
            File dir = jspc.getWebApp().getDirectory();
            addArg(cmd, "-webapp", dir);
        }
        logAndAddFilesToCompile(getJspc(), getJspc().getCompileList(), cmd);
        return cmd;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapter
    public JspMangler createMangler() {
        return this.mangler;
    }

    private Path getClasspath() {
        Path p = getJspc().getClasspath();
        if (p == null) {
            return new Path(getProject()).concatSystemClasspath("only");
        }
        return p.concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
    }

    private boolean isTomcat5x() {
        try {
            AntClassLoader l = getProject().createClassLoader(getClasspath());
            l.loadClass("org.apache.jasper.tagplugins.jstl.If");
            if (l != null) {
                l.close();
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
