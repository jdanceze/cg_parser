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
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/javacc/JJDoc.class */
public class JJDoc extends Task {
    private static final String OUTPUT_FILE = "OUTPUT_FILE";
    private static final String TEXT = "TEXT";
    private static final String ONE_TABLE = "ONE_TABLE";
    private static final String DEFAULT_SUFFIX_HTML = ".html";
    private static final String DEFAULT_SUFFIX_TEXT = ".txt";
    private final Map<String, Object> optionalAttrs = new Hashtable();
    private String outputFile = null;
    private boolean plainText = false;
    private File targetFile = null;
    private File javaccHome = null;
    private CommandlineJava cmdl = new CommandlineJava();
    private String maxMemory = null;

    public void setText(boolean plainText) {
        this.optionalAttrs.put(TEXT, Boolean.valueOf(plainText));
        this.plainText = plainText;
    }

    public void setOnetable(boolean oneTable) {
        this.optionalAttrs.put(ONE_TABLE, Boolean.valueOf(oneTable));
    }

    public void setOutputfile(String outputFile) {
        this.outputFile = outputFile;
    }

    public void setTarget(File target) {
        this.targetFile = target;
    }

    public void setJavacchome(File javaccHome) {
        this.javaccHome = javaccHome;
    }

    public void setMaxmemory(String max) {
        this.maxMemory = max;
    }

    public JJDoc() {
        this.cmdl.setVm(JavaEnvUtils.getJreExecutable("java"));
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        this.optionalAttrs.forEach(name, value -> {
            this.cmdl.createArgument().setValue(HelpFormatter.DEFAULT_OPT_PREFIX + name + ":" + value.toString());
        });
        if (this.targetFile == null || !this.targetFile.isFile()) {
            throw new BuildException("Invalid target: %s", this.targetFile);
        }
        if (this.outputFile != null) {
            this.cmdl.createArgument().setValue("-OUTPUT_FILE:" + this.outputFile.replace('\\', '/'));
        }
        File javaFile = new File(createOutputFileName(this.targetFile, this.outputFile, this.plainText));
        if (javaFile.exists() && this.targetFile.lastModified() < javaFile.lastModified()) {
            log("Target is already built - skipping (" + this.targetFile + ")", 3);
            return;
        }
        this.cmdl.createArgument().setValue(this.targetFile.getAbsolutePath());
        Path classpath = this.cmdl.createClasspath(getProject());
        File javaccJar = JavaCC.getArchiveFile(this.javaccHome);
        classpath.createPathElement().setPath(javaccJar.getAbsolutePath());
        classpath.addJavaRuntime();
        this.cmdl.setClassname(JavaCC.getMainClass(classpath, 3));
        this.cmdl.setMaxmemory(this.maxMemory);
        Commandline.Argument arg = this.cmdl.createVmArgument();
        arg.setValue("-Dinstall.root=" + this.javaccHome.getAbsolutePath());
        Execute process = new Execute(new LogStreamHandler((Task) this, 2, 2), null);
        log(this.cmdl.describeCommand(), 3);
        process.setCommandline(this.cmdl.getCommandline());
        try {
            if (process.execute() != 0) {
                throw new BuildException("JJDoc failed.");
            }
        } catch (IOException e) {
            throw new BuildException("Failed to launch JJDoc", e);
        }
    }

    private String createOutputFileName(File destFile, String optionalOutputFile, boolean plain) {
        String optionalOutputFile2;
        String suffix = DEFAULT_SUFFIX_HTML;
        String javaccFile = destFile.getAbsolutePath().replace('\\', '/');
        if (plain) {
            suffix = DEFAULT_SUFFIX_TEXT;
        }
        if (optionalOutputFile == null || optionalOutputFile.isEmpty()) {
            int filePos = javaccFile.lastIndexOf(47);
            if (filePos >= 0) {
                javaccFile = javaccFile.substring(filePos + 1);
            }
            int suffixPos = javaccFile.lastIndexOf(46);
            if (suffixPos == -1) {
                optionalOutputFile2 = javaccFile + suffix;
            } else {
                String currentSuffix = javaccFile.substring(suffixPos);
                if (currentSuffix.equals(suffix)) {
                    optionalOutputFile2 = javaccFile + suffix;
                } else {
                    optionalOutputFile2 = javaccFile.substring(0, suffixPos) + suffix;
                }
            }
        } else {
            optionalOutputFile2 = optionalOutputFile.replace('\\', '/');
        }
        return (getProject().getBaseDir() + "/" + optionalOutputFile2).replace('\\', '/');
    }
}
