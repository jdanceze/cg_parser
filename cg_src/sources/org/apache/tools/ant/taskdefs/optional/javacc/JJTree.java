package org.apache.tools.ant.taskdefs.optional.javacc;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/javacc/JJTree.class */
public class JJTree extends Task {
    private static final String OUTPUT_FILE = "OUTPUT_FILE";
    private static final String BUILD_NODE_FILES = "BUILD_NODE_FILES";
    private static final String MULTI = "MULTI";
    private static final String NODE_DEFAULT_VOID = "NODE_DEFAULT_VOID";
    private static final String NODE_FACTORY = "NODE_FACTORY";
    private static final String NODE_SCOPE_HOOK = "NODE_SCOPE_HOOK";
    private static final String NODE_USES_PARSER = "NODE_USES_PARSER";
    private static final String STATIC = "STATIC";
    private static final String VISITOR = "VISITOR";
    private static final String NODE_PACKAGE = "NODE_PACKAGE";
    private static final String VISITOR_EXCEPTION = "VISITOR_EXCEPTION";
    private static final String NODE_PREFIX = "NODE_PREFIX";
    private static final String DEFAULT_SUFFIX = ".jj";
    private final Map<String, Object> optionalAttrs = new Hashtable();
    private String outputFile = null;
    private File outputDirectory = null;
    private File targetFile = null;
    private File javaccHome = null;
    private CommandlineJava cmdl = new CommandlineJava();
    private String maxMemory = null;

    public void setBuildnodefiles(boolean buildNodeFiles) {
        this.optionalAttrs.put(BUILD_NODE_FILES, Boolean.valueOf(buildNodeFiles));
    }

    public void setMulti(boolean multi) {
        this.optionalAttrs.put(MULTI, Boolean.valueOf(multi));
    }

    public void setNodedefaultvoid(boolean nodeDefaultVoid) {
        this.optionalAttrs.put(NODE_DEFAULT_VOID, Boolean.valueOf(nodeDefaultVoid));
    }

    public void setNodefactory(boolean nodeFactory) {
        this.optionalAttrs.put(NODE_FACTORY, Boolean.valueOf(nodeFactory));
    }

    public void setNodescopehook(boolean nodeScopeHook) {
        this.optionalAttrs.put(NODE_SCOPE_HOOK, Boolean.valueOf(nodeScopeHook));
    }

    public void setNodeusesparser(boolean nodeUsesParser) {
        this.optionalAttrs.put(NODE_USES_PARSER, Boolean.valueOf(nodeUsesParser));
    }

    public void setStatic(boolean staticParser) {
        this.optionalAttrs.put(STATIC, Boolean.valueOf(staticParser));
    }

    public void setVisitor(boolean visitor) {
        this.optionalAttrs.put(VISITOR, Boolean.valueOf(visitor));
    }

    public void setNodepackage(String nodePackage) {
        this.optionalAttrs.put(NODE_PACKAGE, nodePackage);
    }

    public void setVisitorException(String visitorException) {
        this.optionalAttrs.put(VISITOR_EXCEPTION, visitorException);
    }

    public void setNodeprefix(String nodePrefix) {
        this.optionalAttrs.put(NODE_PREFIX, nodePrefix);
    }

    public void setOutputdirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setOutputfile(String outputFile) {
        this.outputFile = outputFile;
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

    public JJTree() {
        this.cmdl.setVm(JavaEnvUtils.getJreExecutable("java"));
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        File javaFile;
        this.optionalAttrs.forEach(name, value -> {
            this.cmdl.createArgument().setValue(HelpFormatter.DEFAULT_OPT_PREFIX + name + ":" + value.toString());
        });
        if (this.targetFile == null || !this.targetFile.isFile()) {
            throw new BuildException("Invalid target: %s", this.targetFile);
        }
        if (this.outputDirectory == null) {
            this.cmdl.createArgument().setValue("-OUTPUT_DIRECTORY:" + getDefaultOutputDirectory());
            javaFile = new File(createOutputFileName(this.targetFile, this.outputFile, null));
        } else if (!this.outputDirectory.isDirectory()) {
            throw new BuildException("'outputdirectory' " + this.outputDirectory + " is not a directory.");
        } else {
            this.cmdl.createArgument().setValue("-OUTPUT_DIRECTORY:" + this.outputDirectory.getAbsolutePath().replace('\\', '/'));
            javaFile = new File(createOutputFileName(this.targetFile, this.outputFile, this.outputDirectory.getPath()));
        }
        if (javaFile.exists() && this.targetFile.lastModified() < javaFile.lastModified()) {
            log("Target is already built - skipping (" + this.targetFile + ")", 3);
            return;
        }
        if (this.outputFile != null) {
            this.cmdl.createArgument().setValue("-OUTPUT_FILE:" + this.outputFile.replace('\\', '/'));
        }
        this.cmdl.createArgument().setValue(this.targetFile.getAbsolutePath());
        Path classpath = this.cmdl.createClasspath(getProject());
        File javaccJar = JavaCC.getArchiveFile(this.javaccHome);
        classpath.createPathElement().setPath(javaccJar.getAbsolutePath());
        classpath.addJavaRuntime();
        this.cmdl.setClassname(JavaCC.getMainClass(classpath, 2));
        this.cmdl.setMaxmemory(this.maxMemory);
        Commandline.Argument arg = this.cmdl.createVmArgument();
        arg.setValue("-Dinstall.root=" + this.javaccHome.getAbsolutePath());
        Execute process = new Execute(new LogStreamHandler((Task) this, 2, 2), null);
        log(this.cmdl.describeCommand(), 3);
        process.setCommandline(this.cmdl.getCommandline());
        try {
            if (process.execute() != 0) {
                throw new BuildException("JJTree failed.");
            }
        } catch (IOException e) {
            throw new BuildException("Failed to launch JJTree", e);
        }
    }

    private String createOutputFileName(File destFile, String optionalOutputFile, String outputDir) {
        String optionalOutputFile2 = validateOutputFile(optionalOutputFile, outputDir);
        String jjtreeFile = destFile.getAbsolutePath().replace('\\', '/');
        if (optionalOutputFile2 == null || optionalOutputFile2.isEmpty()) {
            int filePos = jjtreeFile.lastIndexOf(47);
            if (filePos >= 0) {
                jjtreeFile = jjtreeFile.substring(filePos + 1);
            }
            int suffixPos = jjtreeFile.lastIndexOf(46);
            if (suffixPos == -1) {
                optionalOutputFile2 = jjtreeFile + DEFAULT_SUFFIX;
            } else {
                String currentSuffix = jjtreeFile.substring(suffixPos);
                if (currentSuffix.equals(DEFAULT_SUFFIX)) {
                    optionalOutputFile2 = jjtreeFile + DEFAULT_SUFFIX;
                } else {
                    optionalOutputFile2 = jjtreeFile.substring(0, suffixPos) + DEFAULT_SUFFIX;
                }
            }
        }
        if (outputDir == null || outputDir.isEmpty()) {
            outputDir = getDefaultOutputDirectory();
        }
        return (outputDir + "/" + optionalOutputFile2).replace('\\', '/');
    }

    private String validateOutputFile(String destFile, String outputDir) throws BuildException {
        if (destFile == null) {
            return null;
        }
        if (outputDir == null && (destFile.startsWith("/") || destFile.startsWith("\\"))) {
            String relativeOutputFile = makeOutputFileRelative(destFile);
            setOutputfile(relativeOutputFile);
            return relativeOutputFile;
        }
        String root = getRoot(new File(destFile)).getAbsolutePath();
        if (root.length() > 1 && destFile.startsWith(root.substring(0, root.length() - 1))) {
            throw new BuildException("Drive letter in 'outputfile' not supported: %s", destFile);
        }
        return destFile;
    }

    private String makeOutputFileRelative(String destFile) {
        StringBuilder relativePath = new StringBuilder();
        String defaultOutputDirectory = getDefaultOutputDirectory();
        int indexOf = defaultOutputDirectory.indexOf(47) + 1;
        while (true) {
            int startPos = indexOf;
            if (startPos <= -1 || startPos >= defaultOutputDirectory.length()) {
                break;
            }
            relativePath.append("/..");
            int nextPos = defaultOutputDirectory.indexOf(47, startPos);
            if (nextPos == -1) {
                indexOf = nextPos;
            } else {
                indexOf = nextPos + 1;
            }
        }
        return relativePath.append(destFile).toString();
    }

    private String getDefaultOutputDirectory() {
        return getProject().getBaseDir().getAbsolutePath().replace('\\', '/');
    }

    private File getRoot(File file) {
        File absoluteFile = file.getAbsoluteFile();
        while (true) {
            File root = absoluteFile;
            if (root.getParent() != null) {
                absoluteFile = root.getParentFile();
            } else {
                return root;
            }
        }
    }
}
