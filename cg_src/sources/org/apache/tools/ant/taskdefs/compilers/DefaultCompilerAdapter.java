package org.apache.tools.ant.taskdefs.compilers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.resource.spi.work.WorkException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/compilers/DefaultCompilerAdapter.class */
public abstract class DefaultCompilerAdapter implements CompilerAdapter, CompilerAdapterExtension {
    private static final int COMMAND_LINE_LIMIT;
    private static final FileUtils FILE_UTILS;
    @Deprecated
    protected static final String lSep;
    private static final Pattern JAVAC_ARG_FILE_CHARS_TO_QUOTE;
    protected Path src;
    protected File destDir;
    protected String encoding;
    protected boolean debug = false;
    protected boolean optimize = false;
    protected boolean deprecation = false;
    protected boolean depend = false;
    protected boolean verbose = false;
    protected String target;
    protected String release;
    protected Path bootclasspath;
    protected Path extdirs;
    protected Path compileClasspath;
    protected Path modulepath;
    protected Path upgrademodulepath;
    protected Path compileSourcepath;
    protected Path moduleSourcepath;
    protected Project project;
    protected Location location;
    protected boolean includeAntRuntime;
    protected boolean includeJavaRuntime;
    protected String memoryInitialSize;
    protected String memoryMaximumSize;
    protected File[] compileList;
    protected Javac attributes;

    static {
        if (Os.isFamily(Os.FAMILY_OS2)) {
            COMMAND_LINE_LIMIT = 1000;
        } else {
            COMMAND_LINE_LIMIT = 4096;
        }
        FILE_UTILS = FileUtils.getFileUtils();
        lSep = StringUtils.LINE_SEP;
        JAVAC_ARG_FILE_CHARS_TO_QUOTE = Pattern.compile("[ #]");
    }

    @Override // org.apache.tools.ant.taskdefs.compilers.CompilerAdapter
    public void setJavac(Javac attributes) {
        this.attributes = attributes;
        this.src = attributes.getSrcdir();
        this.destDir = attributes.getDestdir();
        this.encoding = attributes.getEncoding();
        this.debug = attributes.getDebug();
        this.optimize = attributes.getOptimize();
        this.deprecation = attributes.getDeprecation();
        this.depend = attributes.getDepend();
        this.verbose = attributes.getVerbose();
        this.target = attributes.getTarget();
        this.release = attributes.getRelease();
        this.bootclasspath = attributes.getBootclasspath();
        this.extdirs = attributes.getExtdirs();
        this.compileList = attributes.getFileList();
        this.compileClasspath = attributes.getClasspath();
        this.modulepath = attributes.getModulepath();
        this.upgrademodulepath = attributes.getUpgrademodulepath();
        this.compileSourcepath = attributes.getSourcepath();
        this.moduleSourcepath = attributes.getModulesourcepath();
        this.project = attributes.getProject();
        this.location = attributes.getLocation();
        this.includeAntRuntime = attributes.getIncludeantruntime();
        this.includeJavaRuntime = attributes.getIncludejavaruntime();
        this.memoryInitialSize = attributes.getMemoryInitialSize();
        this.memoryMaximumSize = attributes.getMemoryMaximumSize();
        if (this.moduleSourcepath != null && this.src == null && this.compileSourcepath == null) {
            this.compileSourcepath = new Path(getProject());
        }
    }

    public Javac getJavac() {
        return this.attributes;
    }

    @Override // org.apache.tools.ant.taskdefs.compilers.CompilerAdapterExtension
    public String[] getSupportedFileExtensions() {
        return new String[]{"java"};
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Project getProject() {
        return this.project;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Path getCompileClasspath() {
        Path classpath = new Path(this.project);
        if (this.destDir != null && getJavac().isIncludeDestClasses()) {
            classpath.setLocation(this.destDir);
        }
        Path cp = this.compileClasspath;
        if (cp == null) {
            cp = new Path(this.project);
        }
        if (this.includeAntRuntime) {
            classpath.addExisting(cp.concatSystemClasspath("last"));
        } else {
            classpath.addExisting(cp.concatSystemClasspath(Definer.OnError.POLICY_IGNORE));
        }
        if (this.includeJavaRuntime) {
            classpath.addJavaRuntime();
        }
        return classpath;
    }

    protected Path getModulepath() {
        Path mp = new Path(getProject());
        if (this.modulepath != null) {
            mp.addExisting(this.modulepath);
        }
        return mp;
    }

    protected Path getUpgrademodulepath() {
        Path ump = new Path(getProject());
        if (this.upgrademodulepath != null) {
            ump.addExisting(this.upgrademodulepath);
        }
        return ump;
    }

    protected Path getModulesourcepath() {
        Path msp = new Path(getProject());
        if (this.moduleSourcepath != null) {
            msp.add(this.moduleSourcepath);
        }
        return msp;
    }

    protected Commandline setupJavacCommandlineSwitches(Commandline cmd) {
        return setupJavacCommandlineSwitches(cmd, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Commandline setupJavacCommandlineSwitches(Commandline cmd, boolean useDebugLevel) {
        Path sourcepath;
        Path classpath = getCompileClasspath();
        if (this.compileSourcepath != null) {
            sourcepath = this.compileSourcepath;
        } else {
            sourcepath = this.src;
        }
        String memoryParameterPrefix = assumeJava1_2Plus() ? "-J-X" : "-J-";
        if (this.memoryInitialSize != null) {
            if (!this.attributes.isForkedJavac()) {
                this.attributes.log("Since fork is false, ignoring memoryInitialSize setting.", 1);
            } else {
                cmd.createArgument().setValue(memoryParameterPrefix + "ms" + this.memoryInitialSize);
            }
        }
        if (this.memoryMaximumSize != null) {
            if (!this.attributes.isForkedJavac()) {
                this.attributes.log("Since fork is false, ignoring memoryMaximumSize setting.", 1);
            } else {
                cmd.createArgument().setValue(memoryParameterPrefix + "mx" + this.memoryMaximumSize);
            }
        }
        if (this.attributes.getNowarn()) {
            cmd.createArgument().setValue("-nowarn");
        }
        if (this.deprecation) {
            cmd.createArgument().setValue("-deprecation");
        }
        if (this.destDir != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(this.destDir);
        }
        cmd.createArgument().setValue("-classpath");
        if (!assumeJava1_2Plus()) {
            Path cp = new Path(this.project);
            Optional ofNullable = Optional.ofNullable(getBootClassPath());
            Objects.requireNonNull(cp);
            ofNullable.ifPresent(this::append);
            if (this.extdirs != null) {
                cp.addExtdirs(this.extdirs);
            }
            cp.append(classpath);
            cp.append(sourcepath);
            cmd.createArgument().setPath(cp);
        } else {
            cmd.createArgument().setPath(classpath);
            if (sourcepath.size() > 0) {
                cmd.createArgument().setValue("-sourcepath");
                cmd.createArgument().setPath(sourcepath);
            }
            if (this.release == null || !assumeJava9Plus()) {
                if (this.target != null) {
                    cmd.createArgument().setValue("-target");
                    cmd.createArgument().setValue(this.target);
                }
                Path bp = getBootClassPath();
                if (!bp.isEmpty()) {
                    cmd.createArgument().setValue("-bootclasspath");
                    cmd.createArgument().setPath(bp);
                }
            }
            if (this.extdirs != null && !this.extdirs.isEmpty()) {
                cmd.createArgument().setValue("-extdirs");
                cmd.createArgument().setPath(this.extdirs);
            }
        }
        if (this.encoding != null) {
            cmd.createArgument().setValue("-encoding");
            cmd.createArgument().setValue(this.encoding);
        }
        if (this.debug) {
            if (useDebugLevel && assumeJava1_2Plus()) {
                String debugLevel = this.attributes.getDebugLevel();
                if (debugLevel != null) {
                    cmd.createArgument().setValue("-g:" + debugLevel);
                } else {
                    cmd.createArgument().setValue("-g");
                }
            } else {
                cmd.createArgument().setValue("-g");
            }
        } else if (getNoDebugArgument() != null) {
            cmd.createArgument().setValue(getNoDebugArgument());
        }
        if (this.optimize) {
            cmd.createArgument().setValue(MSVSSConstants.FLAG_OUTPUT);
        }
        if (this.depend) {
            if (assumeJava1_3Plus()) {
                this.attributes.log("depend attribute is not supported by the modern compiler", 1);
            } else if (assumeJava1_2Plus()) {
                cmd.createArgument().setValue("-Xdepend");
            } else {
                cmd.createArgument().setValue("-depend");
            }
        }
        if (this.verbose) {
            cmd.createArgument().setValue(SOSCmd.FLAG_VERBOSE);
        }
        addCurrentCompilerArgs(cmd);
        return cmd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Commandline setupModernJavacCommandlineSwitches(Commandline cmd) {
        setupJavacCommandlineSwitches(cmd, true);
        if (assumeJava1_4Plus()) {
            String t = this.attributes.getTarget();
            String s = this.attributes.getSource();
            if (this.release == null || !assumeJava9Plus()) {
                if (this.release != null) {
                    this.attributes.log("Support for javac --release has been added in Java9 ignoring it");
                }
                if (s != null) {
                    cmd.createArgument().setValue("-source");
                    cmd.createArgument().setValue(adjustSourceValue(s));
                } else if (t != null && mustSetSourceForTarget(t)) {
                    setImplicitSourceSwitch(cmd, t, adjustSourceValue(t));
                }
            } else {
                if (t != null || s != null || getBootClassPath().size() > 0) {
                    this.attributes.log("Ignoring source, target and bootclasspath as release has been set", 1);
                }
                cmd.createArgument().setValue("--release");
                cmd.createArgument().setValue(this.release);
            }
        }
        Path msp = getModulesourcepath();
        if (!msp.isEmpty()) {
            cmd.createArgument().setValue("--module-source-path");
            cmd.createArgument().setPath(msp);
        }
        Path mp = getModulepath();
        if (!mp.isEmpty()) {
            cmd.createArgument().setValue("--module-path");
            cmd.createArgument().setPath(mp);
        }
        Path ump = getUpgrademodulepath();
        if (!ump.isEmpty()) {
            cmd.createArgument().setValue("--upgrade-module-path");
            cmd.createArgument().setPath(ump);
        }
        if (this.attributes.getNativeHeaderDir() != null) {
            if (!assumeJava1_8Plus()) {
                this.attributes.log("Support for javac -h has been added in Java8, ignoring it");
            } else {
                cmd.createArgument().setValue("-h");
                cmd.createArgument().setFile(this.attributes.getNativeHeaderDir());
            }
        }
        return cmd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Commandline setupModernJavacCommand() {
        Commandline cmd = new Commandline();
        setupModernJavacCommandlineSwitches(cmd);
        logAndAddFilesToCompile(cmd);
        return cmd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Commandline setupJavacCommand() {
        return setupJavacCommand(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Commandline setupJavacCommand(boolean debugLevelCheck) {
        Commandline cmd = new Commandline();
        setupJavacCommandlineSwitches(cmd, debugLevelCheck);
        logAndAddFilesToCompile(cmd);
        return cmd;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void logAndAddFilesToCompile(Commandline cmd) {
        this.attributes.log("Compilation " + cmd.describeArguments(), 3);
        Javac javac = this.attributes;
        Object[] objArr = new Object[1];
        objArr[0] = this.compileList.length == 1 ? "File" : "Files";
        javac.log(String.format("%s to be compiled:", objArr), 3);
        this.attributes.log((String) Stream.of((Object[]) this.compileList).map((v0) -> {
            return v0.getAbsolutePath();
        }).peek(arg -> {
            cmd.createArgument().setValue(arg);
        }).map(arg2 -> {
            return String.format("    %s%n", arg2);
        }).collect(Collectors.joining("")), 3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int executeExternalCompile(String[] args, int firstFileName) {
        return executeExternalCompile(args, firstFileName, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int executeExternalCompile(String[] args, int firstFileName, boolean quoteFiles) {
        String[] commandArray;
        File tmpFile = null;
        try {
            if (Commandline.toString(args).length() > COMMAND_LINE_LIMIT && firstFileName >= 0) {
                try {
                    tmpFile = FILE_UTILS.createTempFile(getProject(), "files", "", getJavac().getTempdir(), true, true);
                    BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
                    for (int i = firstFileName; i < args.length; i++) {
                        try {
                            if (quoteFiles && JAVAC_ARG_FILE_CHARS_TO_QUOTE.matcher(args[i]).find()) {
                                args[i] = args[i].replace(File.separatorChar, '/');
                                out.write("\"" + args[i] + "\"");
                            } else {
                                out.write(args[i]);
                            }
                            out.newLine();
                        } catch (Throwable th) {
                            try {
                                out.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    }
                    out.flush();
                    commandArray = new String[firstFileName + 1];
                    System.arraycopy(args, 0, commandArray, 0, firstFileName);
                    commandArray[firstFileName] = "@" + tmpFile;
                    out.close();
                } catch (IOException e) {
                    throw new BuildException("Error creating temporary file", e, this.location);
                }
            } else {
                commandArray = args;
            }
            try {
                Execute exe = new Execute(new LogStreamHandler((Task) this.attributes, 2, 1));
                if (Os.isFamily(Os.FAMILY_VMS)) {
                    exe.setVMLauncher(true);
                }
                exe.setAntRun(this.project);
                exe.setWorkingDirectory(this.project.getBaseDir());
                exe.setCommandline(commandArray);
                exe.execute();
                int exitValue = exe.getExitValue();
                if (tmpFile != null) {
                    tmpFile.delete();
                }
                return exitValue;
            } catch (IOException e2) {
                throw new BuildException("Error running " + args[0] + " compiler", e2, this.location);
            }
        } catch (Throwable th3) {
            if (tmpFile != null) {
                tmpFile.delete();
            }
            throw th3;
        }
    }

    @Deprecated
    protected void addExtdirsToClasspath(Path classpath) {
        classpath.addExtdirs(this.extdirs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addCurrentCompilerArgs(Commandline cmd) {
        cmd.addArguments(getJavac().getCurrentCompilerArgs());
    }

    @Deprecated
    protected boolean assumeJava11() {
        return assumeJava1_1Plus() && !assumeJava1_2Plus();
    }

    protected boolean assumeJava1_1Plus() {
        return "javac1.1".equals(this.attributes.getCompilerVersion()) || assumeJava1_2Plus();
    }

    @Deprecated
    protected boolean assumeJava12() {
        return assumeJava1_2Plus() && !assumeJava1_3Plus();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean assumeJava1_2Plus() {
        return "javac1.2".equals(this.attributes.getCompilerVersion()) || assumeJava1_3Plus();
    }

    @Deprecated
    protected boolean assumeJava13() {
        return assumeJava1_3Plus() && !assumeJava1_4Plus();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean assumeJava1_3Plus() {
        return "javac1.3".equals(this.attributes.getCompilerVersion()) || assumeJava1_4Plus();
    }

    @Deprecated
    protected boolean assumeJava14() {
        return assumeJava1_4Plus() && !assumeJava1_5Plus();
    }

    protected boolean assumeJava1_4Plus() {
        return assumeJavaXY("javac1.4", "1.4") || assumeJava1_5Plus();
    }

    @Deprecated
    protected boolean assumeJava15() {
        return assumeJava1_5Plus() && !assumeJava1_6Plus();
    }

    protected boolean assumeJava1_5Plus() {
        return assumeJavaXY("javac1.5", JavaEnvUtils.JAVA_1_5) || assumeJava1_6Plus();
    }

    @Deprecated
    protected boolean assumeJava16() {
        return assumeJava1_6Plus() && !assumeJava1_7Plus();
    }

    protected boolean assumeJava1_6Plus() {
        return assumeJavaXY("javac1.6", JavaEnvUtils.JAVA_1_6) || assumeJava1_7Plus();
    }

    @Deprecated
    protected boolean assumeJava17() {
        return assumeJava1_7Plus() && !assumeJava1_8Plus();
    }

    protected boolean assumeJava1_7Plus() {
        return assumeJavaXY("javac1.7", JavaEnvUtils.JAVA_1_7) || assumeJava1_8Plus();
    }

    @Deprecated
    protected boolean assumeJava18() {
        return assumeJava1_8Plus() && !assumeJava9Plus();
    }

    protected boolean assumeJava1_8Plus() {
        return assumeJavaXY("javac1.8", JavaEnvUtils.JAVA_1_8) || assumeJava9Plus();
    }

    @Deprecated
    protected boolean assumeJava19() {
        return assumeJava9();
    }

    @Deprecated
    protected boolean assumeJava9() {
        return assumeJava9Plus() && !assumeJava10Plus();
    }

    protected boolean assumeJava9Plus() {
        return assumeJavaXY("javac1.9", JavaEnvUtils.JAVA_9) || assumeJavaXY("javac9", JavaEnvUtils.JAVA_9) || assumeJava10Plus();
    }

    protected boolean assumeJava10Plus() {
        return "javac10+".equals(this.attributes.getCompilerVersion()) || (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_10) && ("classic".equals(this.attributes.getCompilerVersion()) || "modern".equals(this.attributes.getCompilerVersion()) || "extJavac".equals(this.attributes.getCompilerVersion())));
    }

    private boolean assumeJavaXY(String javacXY, String javaEnvVersionXY) {
        String compilerVersion = this.attributes.getCompilerVersion();
        return javacXY.equals(compilerVersion) || (JavaEnvUtils.isJavaVersion(javaEnvVersionXY) && ("classic".equals(compilerVersion) || "modern".equals(compilerVersion) || "extJavac".equals(compilerVersion)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Path getBootClassPath() {
        Path bp = new Path(this.project);
        if (this.bootclasspath != null) {
            bp.append(this.bootclasspath);
        }
        return bp.concatSystemBootClasspath(Definer.OnError.POLICY_IGNORE);
    }

    protected String getNoDebugArgument() {
        if (assumeJava1_2Plus()) {
            return "-g:none";
        }
        return null;
    }

    private void setImplicitSourceSwitch(Commandline cmd, String target, String source) {
        this.attributes.log("", 1);
        this.attributes.log("          WARNING", 1);
        this.attributes.log("", 1);
        this.attributes.log("The -source switch defaults to " + getDefaultSource() + ".", 1);
        this.attributes.log("If you specify -target " + target + " you now must also specify -source " + source + ".", 1);
        this.attributes.log("Ant will implicitly add -source " + source + " for you.  Please change your build file.", 1);
        cmd.createArgument().setValue("-source");
        cmd.createArgument().setValue(source);
    }

    private String getDefaultSource() {
        if (assumeJava9Plus()) {
            return "9 in JDK 9";
        }
        if (assumeJava1_8Plus()) {
            return "1.8 in JDK 1.8";
        }
        if (assumeJava1_7Plus()) {
            return "1.7 in JDK 1.7";
        }
        if (assumeJava1_5Plus()) {
            return "1.5 in JDK 1.5 and 1.6";
        }
        return "";
    }

    private boolean mustSetSourceForTarget(String t) {
        if (!assumeJava1_5Plus()) {
            return false;
        }
        if (t.startsWith("1.")) {
            t = t.substring(2);
        }
        return WorkException.START_TIMED_OUT.equals(t) || WorkException.TX_CONCURRENT_WORK_DISALLOWED.equals(t) || WorkException.TX_RECREATE_FAILED.equals(t) || "4".equals(t) || (("5".equals(t) || "6".equals(t)) && assumeJava1_7Plus()) || (("7".equals(t) && assumeJava1_8Plus()) || (("8".equals(t) && assumeJava9Plus()) || (JavaEnvUtils.JAVA_9.equals(t) && assumeJava10Plus())));
    }

    private String adjustSourceValue(String source) {
        return ("1.1".equals(source) || "1.2".equals(source)) ? "1.3" : source;
    }
}
