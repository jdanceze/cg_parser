package org.apache.tools.ant.taskdefs.optional.javacc;

import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/javacc/JavaCC.class */
public class JavaCC extends Task {
    private static final String LOOKAHEAD = "LOOKAHEAD";
    private static final String CHOICE_AMBIGUITY_CHECK = "CHOICE_AMBIGUITY_CHECK";
    private static final String OTHER_AMBIGUITY_CHECK = "OTHER_AMBIGUITY_CHECK";
    private static final String STATIC = "STATIC";
    private static final String DEBUG_PARSER = "DEBUG_PARSER";
    private static final String DEBUG_LOOKAHEAD = "DEBUG_LOOKAHEAD";
    private static final String DEBUG_TOKEN_MANAGER = "DEBUG_TOKEN_MANAGER";
    private static final String OPTIMIZE_TOKEN_MANAGER = "OPTIMIZE_TOKEN_MANAGER";
    private static final String ERROR_REPORTING = "ERROR_REPORTING";
    private static final String JAVA_UNICODE_ESCAPE = "JAVA_UNICODE_ESCAPE";
    private static final String UNICODE_INPUT = "UNICODE_INPUT";
    private static final String IGNORE_CASE = "IGNORE_CASE";
    private static final String COMMON_TOKEN_ACTION = "COMMON_TOKEN_ACTION";
    private static final String USER_TOKEN_MANAGER = "USER_TOKEN_MANAGER";
    private static final String USER_CHAR_STREAM = "USER_CHAR_STREAM";
    private static final String BUILD_PARSER = "BUILD_PARSER";
    private static final String BUILD_TOKEN_MANAGER = "BUILD_TOKEN_MANAGER";
    private static final String SANITY_CHECK = "SANITY_CHECK";
    private static final String FORCE_LA_CHECK = "FORCE_LA_CHECK";
    private static final String CACHE_TOKENS = "CACHE_TOKENS";
    private static final String KEEP_LINE_COLUMN = "KEEP_LINE_COLUMN";
    private static final String JDK_VERSION = "JDK_VERSION";
    protected static final int TASKDEF_TYPE_JAVACC = 1;
    protected static final int TASKDEF_TYPE_JJTREE = 2;
    protected static final int TASKDEF_TYPE_JJDOC = 3;
    protected static final String[] ARCHIVE_LOCATIONS = {"JavaCC.zip", "bin/lib/JavaCC.zip", "bin/lib/javacc.jar", "javacc.jar"};
    protected static final int[] ARCHIVE_LOCATIONS_VS_MAJOR_VERSION = {1, 2, 3, 3};
    protected static final String COM_PACKAGE = "COM.sun.labs.";
    protected static final String COM_JAVACC_CLASS = "javacc.Main";
    protected static final String COM_JJTREE_CLASS = "jjtree.Main";
    protected static final String COM_JJDOC_CLASS = "jjdoc.JJDocMain";
    protected static final String ORG_PACKAGE_3_0 = "org.netbeans.javacc.";
    protected static final String ORG_PACKAGE_3_1 = "org.javacc.";
    protected static final String ORG_JAVACC_CLASS = "parser.Main";
    protected static final String ORG_JJTREE_CLASS = "jjtree.Main";
    protected static final String ORG_JJDOC_CLASS = "jjdoc.JJDocMain";
    private final Map<String, Object> optionalAttrs = new Hashtable();
    private File outputDirectory = null;
    private File targetFile = null;
    private File javaccHome = null;
    private CommandlineJava cmdl = new CommandlineJava();
    private String maxMemory = null;

    public void setLookahead(int lookahead) {
        this.optionalAttrs.put(LOOKAHEAD, Integer.valueOf(lookahead));
    }

    public void setChoiceambiguitycheck(int choiceAmbiguityCheck) {
        this.optionalAttrs.put(CHOICE_AMBIGUITY_CHECK, Integer.valueOf(choiceAmbiguityCheck));
    }

    public void setOtherambiguityCheck(int otherAmbiguityCheck) {
        this.optionalAttrs.put(OTHER_AMBIGUITY_CHECK, Integer.valueOf(otherAmbiguityCheck));
    }

    public void setStatic(boolean staticParser) {
        this.optionalAttrs.put(STATIC, staticParser ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setDebugparser(boolean debugParser) {
        this.optionalAttrs.put(DEBUG_PARSER, debugParser ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setDebuglookahead(boolean debugLookahead) {
        this.optionalAttrs.put(DEBUG_LOOKAHEAD, debugLookahead ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setDebugtokenmanager(boolean debugTokenManager) {
        this.optionalAttrs.put(DEBUG_TOKEN_MANAGER, debugTokenManager ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setOptimizetokenmanager(boolean optimizeTokenManager) {
        this.optionalAttrs.put(OPTIMIZE_TOKEN_MANAGER, optimizeTokenManager ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setErrorreporting(boolean errorReporting) {
        this.optionalAttrs.put(ERROR_REPORTING, errorReporting ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setJavaunicodeescape(boolean javaUnicodeEscape) {
        this.optionalAttrs.put(JAVA_UNICODE_ESCAPE, javaUnicodeEscape ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setUnicodeinput(boolean unicodeInput) {
        this.optionalAttrs.put(UNICODE_INPUT, unicodeInput ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setIgnorecase(boolean ignoreCase) {
        this.optionalAttrs.put(IGNORE_CASE, ignoreCase ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setCommontokenaction(boolean commonTokenAction) {
        this.optionalAttrs.put(COMMON_TOKEN_ACTION, commonTokenAction ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setUsertokenmanager(boolean userTokenManager) {
        this.optionalAttrs.put(USER_TOKEN_MANAGER, userTokenManager ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setUsercharstream(boolean userCharStream) {
        this.optionalAttrs.put(USER_CHAR_STREAM, userCharStream ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setBuildparser(boolean buildParser) {
        this.optionalAttrs.put(BUILD_PARSER, buildParser ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setBuildtokenmanager(boolean buildTokenManager) {
        this.optionalAttrs.put(BUILD_TOKEN_MANAGER, buildTokenManager ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setSanitycheck(boolean sanityCheck) {
        this.optionalAttrs.put(SANITY_CHECK, sanityCheck ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setForcelacheck(boolean forceLACheck) {
        this.optionalAttrs.put(FORCE_LA_CHECK, forceLACheck ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setCachetokens(boolean cacheTokens) {
        this.optionalAttrs.put(CACHE_TOKENS, cacheTokens ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setKeeplinecolumn(boolean keepLineColumn) {
        this.optionalAttrs.put(KEEP_LINE_COLUMN, keepLineColumn ? Boolean.TRUE : Boolean.FALSE);
    }

    public void setJDKversion(String jdkVersion) {
        this.optionalAttrs.put(JDK_VERSION, jdkVersion);
    }

    public void setOutputdirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setTarget(File targetFile) {
        this.targetFile = targetFile;
    }

    public void setJavacchome(File javaccHome) {
        this.javaccHome = javaccHome;
    }

    public void setMaxmemory(String max) {
        this.maxMemory = max;
    }

    public JavaCC() {
        this.cmdl.setVm(JavaEnvUtils.getJreExecutable("java"));
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        this.optionalAttrs.forEach(name, value -> {
            this.cmdl.createArgument().setValue(HelpFormatter.DEFAULT_OPT_PREFIX + name + ":" + value);
        });
        if (this.targetFile == null || !this.targetFile.isFile()) {
            throw new BuildException("Invalid target: %s", this.targetFile);
        }
        if (this.outputDirectory == null) {
            this.outputDirectory = new File(this.targetFile.getParent());
        } else if (!this.outputDirectory.isDirectory()) {
            throw new BuildException("Outputdir not a directory.");
        }
        this.cmdl.createArgument().setValue("-OUTPUT_DIRECTORY:" + this.outputDirectory.getAbsolutePath());
        File javaFile = getOutputJavaFile(this.outputDirectory, this.targetFile);
        if (javaFile.exists() && this.targetFile.lastModified() < javaFile.lastModified()) {
            log("Target is already built - skipping (" + this.targetFile + ")", 3);
            return;
        }
        this.cmdl.createArgument().setValue(this.targetFile.getAbsolutePath());
        Path classpath = this.cmdl.createClasspath(getProject());
        File javaccJar = getArchiveFile(this.javaccHome);
        classpath.createPathElement().setPath(javaccJar.getAbsolutePath());
        classpath.addJavaRuntime();
        this.cmdl.setClassname(getMainClass(classpath, 1));
        this.cmdl.setMaxmemory(this.maxMemory);
        Commandline.Argument arg = this.cmdl.createVmArgument();
        arg.setValue("-Dinstall.root=" + this.javaccHome.getAbsolutePath());
        Execute.runCommand(this, this.cmdl.getCommandline());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static File getArchiveFile(File home) throws BuildException {
        return new File(home, ARCHIVE_LOCATIONS[getArchiveLocationIndex(home)]);
    }

    protected static String getMainClass(File home, int type) throws BuildException {
        Path p = new Path(null);
        p.createPathElement().setLocation(getArchiveFile(home));
        p.addJavaRuntime();
        return getMainClass(p, type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String getMainClass(Path path, int type) throws BuildException {
        String packagePrefix = null;
        String mainClass = null;
        AntClassLoader l = AntClassLoader.newAntClassLoader(null, null, path.concatSystemClasspath(Definer.OnError.POLICY_IGNORE), true);
        try {
            if (l.getResourceAsStream("COM.sun.labs.javacc.Main".replace('.', '/') + ".class") != null) {
                packagePrefix = COM_PACKAGE;
                switch (type) {
                    case 1:
                        mainClass = COM_JAVACC_CLASS;
                        break;
                    case 2:
                        mainClass = "jjtree.Main";
                        break;
                    case 3:
                        mainClass = "jjdoc.JJDocMain";
                        break;
                }
            } else {
                InputStream is = l.getResourceAsStream("org.javacc.parser.Main".replace('.', '/') + ".class");
                if (is != null) {
                    packagePrefix = ORG_PACKAGE_3_1;
                } else {
                    is = l.getResourceAsStream("org.netbeans.javacc.parser.Main".replace('.', '/') + ".class");
                    if (is != null) {
                        packagePrefix = ORG_PACKAGE_3_0;
                    }
                }
                if (is != null) {
                    switch (type) {
                        case 1:
                            mainClass = ORG_JAVACC_CLASS;
                            break;
                        case 2:
                            mainClass = "jjtree.Main";
                            break;
                        case 3:
                            mainClass = "jjdoc.JJDocMain";
                            break;
                    }
                }
            }
            if (packagePrefix == null) {
                throw new BuildException("failed to load JavaCC");
            }
            if (mainClass == null) {
                throw new BuildException("unknown task type " + type);
            }
            String str = packagePrefix + mainClass;
            if (l != null) {
                l.close();
            }
            return str;
        } catch (Throwable th) {
            if (l != null) {
                try {
                    l.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static int getArchiveLocationIndex(File home) throws BuildException {
        if (home == null || !home.isDirectory()) {
            throw new BuildException("JavaCC home must be a valid directory.");
        }
        for (int i = 0; i < ARCHIVE_LOCATIONS.length; i++) {
            File f = new File(home, ARCHIVE_LOCATIONS[i]);
            if (f.exists()) {
                return i;
            }
        }
        throw new BuildException("Could not find a path to JavaCC.zip or javacc.jar from '%s'.", home);
    }

    protected static int getMajorVersionNumber(File home) throws BuildException {
        return ARCHIVE_LOCATIONS_VS_MAJOR_VERSION[getArchiveLocationIndex(home)];
    }

    private File getOutputJavaFile(File outputdir, File srcfile) {
        String path;
        String path2 = srcfile.getPath();
        int startBasename = path2.lastIndexOf(File.separator);
        if (startBasename != -1) {
            path2 = path2.substring(startBasename + 1);
        }
        int startExtn = path2.lastIndexOf(46);
        if (startExtn != -1) {
            path = path2.substring(0, startExtn) + ".java";
        } else {
            path = path2 + ".java";
        }
        if (outputdir != null) {
            path = outputdir + File.separator + path;
        }
        return new File(path);
    }
}
