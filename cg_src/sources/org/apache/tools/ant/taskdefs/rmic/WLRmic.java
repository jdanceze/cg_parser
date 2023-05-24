package org.apache.tools.ant.taskdefs.rmic;

import java.lang.reflect.Method;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/WLRmic.class */
public class WLRmic extends DefaultRmicAdapter {
    public static final String WLRMIC_CLASSNAME = "weblogic.rmic";
    public static final String COMPILER_NAME = "weblogic";
    public static final String ERROR_NO_WLRMIC_ON_CLASSPATH = "Cannot use WebLogic rmic, as it is not available. Add it to Ant's classpath with the -lib option";
    public static final String ERROR_WLRMIC_FAILED = "Error starting WebLogic rmic: ";
    public static final String WL_RMI_STUB_SUFFIX = "_WLStub";
    public static final String WL_RMI_SKEL_SUFFIX = "_WLSkel";
    public static final String UNSUPPORTED_STUB_OPTION = "Unsupported stub option: ";

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    protected boolean areIiopAndIdlSupported() {
        return true;
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.RmicAdapter
    public boolean execute() throws BuildException {
        Class<?> c;
        getRmic().log("Using WebLogic rmic", 3);
        Commandline cmd = setupRmicCommand(new String[]{"-noexit"});
        AntClassLoader loader = null;
        try {
            try {
                if (getRmic().getClasspath() == null) {
                    c = Class.forName(WLRMIC_CLASSNAME);
                } else {
                    loader = getRmic().getProject().createClassLoader(getRmic().getClasspath());
                    c = Class.forName(WLRMIC_CLASSNAME, true, loader);
                }
                Method doRmic = c.getMethod("main", String[].class);
                doRmic.invoke(null, cmd.getArguments());
                if (loader != null) {
                    loader.cleanup();
                }
                return true;
            } catch (ClassNotFoundException e) {
                throw new BuildException(ERROR_NO_WLRMIC_ON_CLASSPATH, getRmic().getLocation());
            } catch (Exception ex) {
                if (ex instanceof BuildException) {
                    throw ((BuildException) ex);
                }
                throw new BuildException(ERROR_WLRMIC_FAILED, ex, getRmic().getLocation());
            }
        } catch (Throwable th) {
            if (loader != null) {
                loader.cleanup();
            }
            throw th;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    public String getStubClassSuffix() {
        return WL_RMI_STUB_SUFFIX;
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    public String getSkelClassSuffix() {
        return WL_RMI_SKEL_SUFFIX;
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    protected String[] preprocessCompilerArgs(String[] compilerArgs) {
        return filterJvmCompilerArgs(compilerArgs);
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    protected String addStubVersionOptions() {
        String stubVersion = getRmic().getStubVersion();
        if (null != stubVersion) {
            getRmic().log(UNSUPPORTED_STUB_OPTION + stubVersion, 1);
            return null;
        }
        return null;
    }
}
