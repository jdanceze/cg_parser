package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/WLJspc.class */
public class WLJspc extends MatchingTask {
    private File destinationDirectory;
    private File sourceDirectory;
    private String destinationPackage;
    private Path compileClasspath;
    private String pathToPackage = "";
    private List<String> filesToDo = new Vector();

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (!this.destinationDirectory.isDirectory()) {
            throw new BuildException("destination directory %s is not valid", this.destinationDirectory.getPath());
        }
        if (!this.sourceDirectory.isDirectory()) {
            throw new BuildException("src directory %s is not valid", this.sourceDirectory.getPath());
        }
        if (this.destinationPackage == null) {
            throw new BuildException("package attribute must be present.", getLocation());
        }
        this.pathToPackage = this.destinationPackage.replace('.', File.separatorChar);
        DirectoryScanner ds = super.getDirectoryScanner(this.sourceDirectory);
        if (this.compileClasspath == null) {
            this.compileClasspath = new Path(getProject());
        }
        this.compileClasspath = this.compileClasspath.concatSystemClasspath();
        Java helperTask = new Java(this);
        helperTask.setFork(true);
        helperTask.setClassname("weblogic.jspc");
        helperTask.setTaskName(getTaskName());
        String[] args = new String[12];
        int j = 0 + 1;
        args[0] = "-d";
        int j2 = j + 1;
        args[j] = this.destinationDirectory.getAbsolutePath().trim();
        int j3 = j2 + 1;
        args[j2] = "-docroot";
        int j4 = j3 + 1;
        args[j3] = this.sourceDirectory.getAbsolutePath().trim();
        int j5 = j4 + 1;
        args[j4] = "-keepgenerated";
        int j6 = j5 + 1;
        args[j5] = "-compilerclass";
        int j7 = j6 + 1;
        args[j6] = "sun.tools.javac.Main";
        int j8 = j7 + 1;
        args[j7] = "-classpath";
        int j9 = j8 + 1;
        args[j8] = this.compileClasspath.toString();
        scanDir(ds.getIncludedFiles());
        log("Compiling " + this.filesToDo.size() + " JSP files");
        for (String filename : this.filesToDo) {
            File jspFile = new File(filename);
            args[j9] = "-package";
            String parents = jspFile.getParent();
            if (parents == null || parents.isEmpty()) {
                args[j9 + 1] = this.destinationPackage;
            } else {
                args[j9 + 1] = this.destinationPackage + "._" + replaceString(parents, File.separator, "_.");
            }
            args[j9 + 2] = this.sourceDirectory + File.separator + filename;
            helperTask.clearArgs();
            for (int x = 0; x < j9 + 3; x++) {
                helperTask.createArg().setValue(args[x]);
            }
            helperTask.setClasspath(this.compileClasspath);
            if (helperTask.executeJava() != 0) {
                log(filename + " failed to compile", 1);
            }
        }
    }

    public void setClasspath(Path classpath) {
        if (this.compileClasspath == null) {
            this.compileClasspath = classpath;
        } else {
            this.compileClasspath.append(classpath);
        }
    }

    public Path createClasspath() {
        if (this.compileClasspath == null) {
            this.compileClasspath = new Path(getProject());
        }
        return this.compileClasspath;
    }

    public void setSrc(File dirName) {
        this.sourceDirectory = dirName;
    }

    public void setDest(File dirName) {
        this.destinationDirectory = dirName;
    }

    public void setPackage(String packageName) {
        this.destinationPackage = packageName;
    }

    protected void scanDir(String[] files) {
        String pack;
        long now = Instant.now().toEpochMilli();
        for (String file : files) {
            File srcFile = new File(this.sourceDirectory, file);
            File jspFile = new File(file);
            String parents = jspFile.getParent();
            if (parents == null || parents.isEmpty()) {
                pack = this.pathToPackage;
            } else {
                pack = this.pathToPackage + File.separator + "_" + replaceString(parents, File.separator, "_/");
            }
            String filePath = pack + File.separator + "_";
            int startingIndex = file.lastIndexOf(File.separator) != -1 ? file.lastIndexOf(File.separator) + 1 : 0;
            int endingIndex = file.indexOf(".jsp");
            if (endingIndex == -1) {
                log("Skipping " + file + ". Not a JSP", 3);
            } else {
                File classFile = new File(this.destinationDirectory, (filePath + file.substring(startingIndex, endingIndex)) + ".class");
                if (srcFile.lastModified() > now) {
                    log("Warning: file modified in the future: " + file, 1);
                }
                if (srcFile.lastModified() > classFile.lastModified()) {
                    this.filesToDo.add(file);
                    log("Recompiling File " + file, 3);
                }
            }
        }
    }

    protected String replaceString(String inpString, String escapeChars, String replaceChars) {
        StringBuilder localString = new StringBuilder();
        StringTokenizer st = new StringTokenizer(inpString, escapeChars, true);
        int numTokens = st.countTokens();
        for (int i = 0; i < numTokens; i++) {
            String test = st.nextToken();
            localString.append(test.equals(escapeChars) ? replaceChars : test);
        }
        return localString.toString();
    }
}
