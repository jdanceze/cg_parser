package org.apache.tools.ant.taskdefs.rmic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Rmic;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.StringUtils;
import soot.dava.internal.AST.ASTNode;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/DefaultRmicAdapter.class */
public abstract class DefaultRmicAdapter implements RmicAdapter {
    private static final Random RAND = new Random();
    public static final String RMI_STUB_SUFFIX = "_Stub";
    public static final String RMI_SKEL_SUFFIX = "_Skel";
    public static final String RMI_TIE_SUFFIX = "_Tie";
    public static final String STUB_COMPAT = "-vcompat";
    public static final String STUB_1_1 = "-v1.1";
    public static final String STUB_1_2 = "-v1.2";
    public static final String STUB_OPTION_1_1 = "1.1";
    public static final String STUB_OPTION_1_2 = "1.2";
    public static final String STUB_OPTION_COMPAT = "compat";
    private Rmic attributes;
    private FileNameMapper mapper;

    @Override // org.apache.tools.ant.taskdefs.rmic.RmicAdapter
    public void setRmic(Rmic attributes) {
        this.attributes = attributes;
        this.mapper = new RmicFileNameMapper();
    }

    public Rmic getRmic() {
        return this.attributes;
    }

    protected String getStubClassSuffix() {
        return RMI_STUB_SUFFIX;
    }

    protected String getSkelClassSuffix() {
        return RMI_SKEL_SUFFIX;
    }

    protected String getTieClassSuffix() {
        return RMI_TIE_SUFFIX;
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.RmicAdapter
    public FileNameMapper getMapper() {
        return this.mapper;
    }

    @Override // org.apache.tools.ant.taskdefs.rmic.RmicAdapter
    public Path getClasspath() {
        return getCompileClasspath();
    }

    protected Path getCompileClasspath() {
        Path classpath = new Path(this.attributes.getProject());
        classpath.setLocation(this.attributes.getBase());
        Path cp = this.attributes.getClasspath();
        if (cp == null) {
            cp = new Path(this.attributes.getProject());
        }
        if (this.attributes.getIncludeantruntime()) {
            classpath.addExisting(cp.concatSystemClasspath("last"));
        } else {
            classpath.addExisting(cp.concatSystemClasspath(Definer.OnError.POLICY_IGNORE));
        }
        if (this.attributes.getIncludejavaruntime()) {
            classpath.addJavaRuntime();
        }
        return classpath;
    }

    protected boolean areIiopAndIdlSupported() {
        return !JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_11);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Commandline setupRmicCommand() {
        return setupRmicCommand(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Commandline setupRmicCommand(String[] options) {
        Commandline cmd = new Commandline();
        if (options != null) {
            for (String option : options) {
                cmd.createArgument().setValue(option);
            }
        }
        Path classpath = getCompileClasspath();
        cmd.createArgument().setValue("-d");
        cmd.createArgument().setFile(this.attributes.getOutputDir());
        if (this.attributes.getExtdirs() != null) {
            cmd.createArgument().setValue("-extdirs");
            cmd.createArgument().setPath(this.attributes.getExtdirs());
        }
        cmd.createArgument().setValue("-classpath");
        cmd.createArgument().setPath(classpath);
        String stubOption = addStubVersionOptions();
        if (stubOption != null) {
            cmd.createArgument().setValue(stubOption);
        }
        if (null != this.attributes.getSourceBase()) {
            cmd.createArgument().setValue("-keepgenerated");
        }
        if (this.attributes.getIiop()) {
            if (!areIiopAndIdlSupported()) {
                throw new BuildException("this rmic implementation doesn't support the -iiop switch");
            }
            this.attributes.log("IIOP has been turned on.", 2);
            cmd.createArgument().setValue("-iiop");
            if (this.attributes.getIiopopts() != null) {
                this.attributes.log("IIOP Options: " + this.attributes.getIiopopts(), 2);
                cmd.createArgument().setValue(this.attributes.getIiopopts());
            }
        }
        if (this.attributes.getIdl()) {
            if (!areIiopAndIdlSupported()) {
                throw new BuildException("this rmic implementation doesn't support the -idl switch");
            }
            cmd.createArgument().setValue("-idl");
            this.attributes.log("IDL has been turned on.", 2);
            if (this.attributes.getIdlopts() != null) {
                cmd.createArgument().setValue(this.attributes.getIdlopts());
                this.attributes.log("IDL Options: " + this.attributes.getIdlopts(), 2);
            }
        }
        if (this.attributes.getDebug()) {
            cmd.createArgument().setValue("-g");
        }
        String[] compilerArgs = this.attributes.getCurrentCompilerArgs();
        cmd.addArguments(preprocessCompilerArgs(compilerArgs));
        verifyArguments(cmd);
        logAndAddFilesToCompile(cmd);
        return cmd;
    }

    protected String addStubVersionOptions() {
        String stubVersion = this.attributes.getStubVersion();
        String stubOption = null;
        if (null != stubVersion) {
            if ("1.1".equals(stubVersion)) {
                stubOption = STUB_1_1;
            } else if ("1.2".equals(stubVersion)) {
                stubOption = STUB_1_2;
            } else if (STUB_OPTION_COMPAT.equals(stubVersion)) {
                stubOption = STUB_COMPAT;
            } else {
                this.attributes.log("Unknown stub option " + stubVersion);
            }
        }
        if (stubOption == null && !this.attributes.getIiop() && !this.attributes.getIdl()) {
            stubOption = STUB_COMPAT;
        }
        return stubOption;
    }

    protected String[] preprocessCompilerArgs(String[] compilerArgs) {
        return compilerArgs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String[] filterJvmCompilerArgs(String[] compilerArgs) {
        int len = compilerArgs.length;
        List<String> args = new ArrayList<>(len);
        for (String arg : compilerArgs) {
            if (arg.startsWith("-J")) {
                this.attributes.log("Dropping " + arg + " from compiler arguments");
            } else {
                args.add(arg);
            }
        }
        return (String[]) args.toArray(new String[args.size()]);
    }

    protected void logAndAddFilesToCompile(Commandline cmd) {
        Vector<String> compileList = this.attributes.getCompileList();
        this.attributes.log("Compilation " + cmd.describeArguments(), 3);
        String niceSourceList = (compileList.size() == 1 ? "File" : "Files") + " to be compiled:" + ((String) compileList.stream().peek(arg -> {
            cmd.createArgument().setValue(arg);
        }).collect(Collectors.joining(ASTNode.TAB)));
        this.attributes.log(niceSourceList, 3);
    }

    private void verifyArguments(Commandline cmd) {
        String[] arguments;
        if (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_9)) {
            for (String arg : cmd.getArguments()) {
                if ("-Xnew".equals(arg)) {
                    throw new BuildException("JDK9 has removed support for -Xnew");
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/DefaultRmicAdapter$RmicFileNameMapper.class */
    private class RmicFileNameMapper implements FileNameMapper {
        private RmicFileNameMapper() {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setFrom(String s) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setTo(String s) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public String[] mapFileName(String name) {
            int index;
            String dirname;
            int iIndex;
            String iDir;
            if (name == null || !name.endsWith(".class") || name.endsWith(DefaultRmicAdapter.this.getStubClassSuffix() + ".class") || name.endsWith(DefaultRmicAdapter.this.getSkelClassSuffix() + ".class") || name.endsWith(DefaultRmicAdapter.this.getTieClassSuffix() + ".class")) {
                return null;
            }
            String base = StringUtils.removeSuffix(name, ".class");
            String classname = base.replace(File.separatorChar, '.');
            if (!DefaultRmicAdapter.this.attributes.getVerify() || DefaultRmicAdapter.this.attributes.isValidRmiRemote(classname)) {
                String[] target = {name + ".tmp." + DefaultRmicAdapter.RAND.nextLong()};
                if (DefaultRmicAdapter.this.attributes.getIiop() || DefaultRmicAdapter.this.attributes.getIdl()) {
                    if (!DefaultRmicAdapter.this.attributes.getIdl()) {
                        int lastSlash = base.lastIndexOf(File.separatorChar);
                        if (lastSlash == -1) {
                            index = 0;
                            dirname = "";
                        } else {
                            index = lastSlash + 1;
                            dirname = base.substring(0, index);
                        }
                        String filename = base.substring(index);
                        try {
                            Class<?> c = DefaultRmicAdapter.this.attributes.getLoader().loadClass(classname);
                            if (c.isInterface()) {
                                target = new String[]{dirname + "_" + filename + DefaultRmicAdapter.this.getStubClassSuffix() + ".class"};
                            } else {
                                Class<?> interf = DefaultRmicAdapter.this.attributes.getRemoteInterface(c);
                                String iName = interf.getName();
                                int lastDot = iName.lastIndexOf(46);
                                if (lastDot == -1) {
                                    iIndex = 0;
                                    iDir = "";
                                } else {
                                    iIndex = lastDot + 1;
                                    String iDir2 = iName.substring(0, iIndex);
                                    iDir = iDir2.replace('.', File.separatorChar);
                                }
                                target = new String[]{dirname + "_" + filename + DefaultRmicAdapter.this.getTieClassSuffix() + ".class", iDir + "_" + iName.substring(iIndex) + DefaultRmicAdapter.this.getStubClassSuffix() + ".class"};
                            }
                        } catch (ClassNotFoundException e) {
                            DefaultRmicAdapter.this.attributes.log(Rmic.ERROR_UNABLE_TO_VERIFY_CLASS + classname + Rmic.ERROR_NOT_FOUND, 1);
                        } catch (NoClassDefFoundError e2) {
                            DefaultRmicAdapter.this.attributes.log(Rmic.ERROR_UNABLE_TO_VERIFY_CLASS + classname + Rmic.ERROR_NOT_DEFINED, 1);
                        } catch (Throwable t) {
                            DefaultRmicAdapter.this.attributes.log(Rmic.ERROR_UNABLE_TO_VERIFY_CLASS + classname + Rmic.ERROR_LOADING_CAUSED_EXCEPTION + t.getMessage(), 1);
                        }
                    }
                } else {
                    target = "1.2".equals(DefaultRmicAdapter.this.attributes.getStubVersion()) ? new String[]{base + DefaultRmicAdapter.this.getStubClassSuffix() + ".class"} : new String[]{base + DefaultRmicAdapter.this.getStubClassSuffix() + ".class", base + DefaultRmicAdapter.this.getSkelClassSuffix() + ".class"};
                }
                return target;
            }
            return null;
        }
    }
}
