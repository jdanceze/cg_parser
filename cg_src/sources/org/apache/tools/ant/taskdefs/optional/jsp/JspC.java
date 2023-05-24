package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;
import java.time.Instant;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapter;
import org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapterFactory;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/JspC.class */
public class JspC extends MatchingTask {
    private Path classpath;
    private Path compilerClasspath;
    private Path src;
    private File destDir;
    private String packageName;
    private String iepluginid;
    private boolean mapped;
    private File uriroot;
    private File webinc;
    private File webxml;
    protected WebAppParameter webApp;
    private static final String FAIL_MSG = "Compile failed, messages should have been provided.";
    private String compilerName = "jasper";
    private int verbose = 0;
    protected Vector<String> compileList = new Vector<>();
    Vector<File> javaFiles = new Vector<>();
    protected boolean failOnError = true;

    public void setSrcDir(Path srcDir) {
        if (this.src == null) {
            this.src = srcDir;
        } else {
            this.src.append(srcDir);
        }
    }

    public Path getSrcDir() {
        return this.src;
    }

    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    public File getDestdir() {
        return this.destDir;
    }

    public void setPackage(String pkg) {
        this.packageName = pkg;
    }

    public String getPackage() {
        return this.packageName;
    }

    public void setVerbose(int i) {
        this.verbose = i;
    }

    public int getVerbose() {
        return this.verbose;
    }

    public void setFailonerror(boolean fail) {
        this.failOnError = fail;
    }

    public boolean getFailonerror() {
        return this.failOnError;
    }

    public String getIeplugin() {
        return this.iepluginid;
    }

    public void setIeplugin(String iepluginid) {
        this.iepluginid = iepluginid;
    }

    public boolean isMapped() {
        return this.mapped;
    }

    public void setMapped(boolean mapped) {
        this.mapped = mapped;
    }

    public void setUribase(File uribase) {
        log("Uribase is currently an unused parameter", 1);
    }

    public File getUribase() {
        return this.uriroot;
    }

    public void setUriroot(File uriroot) {
        this.uriroot = uriroot;
    }

    public File getUriroot() {
        return this.uriroot;
    }

    public void setClasspath(Path cp) {
        if (this.classpath == null) {
            this.classpath = cp;
        } else {
            this.classpath.append(cp);
        }
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public Path getClasspath() {
        return this.classpath;
    }

    public void setCompilerclasspath(Path cp) {
        if (this.compilerClasspath == null) {
            this.compilerClasspath = cp;
        } else {
            this.compilerClasspath.append(cp);
        }
    }

    public Path getCompilerclasspath() {
        return this.compilerClasspath;
    }

    public Path createCompilerclasspath() {
        if (this.compilerClasspath == null) {
            this.compilerClasspath = new Path(getProject());
        }
        return this.compilerClasspath.createPath();
    }

    public void setWebxml(File webxml) {
        this.webxml = webxml;
    }

    public File getWebxml() {
        return this.webxml;
    }

    public void setWebinc(File webinc) {
        this.webinc = webinc;
    }

    public File getWebinc() {
        return this.webinc;
    }

    public void addWebApp(WebAppParameter webappParam) throws BuildException {
        if (this.webApp == null) {
            this.webApp = webappParam;
            return;
        }
        throw new BuildException("Only one webapp can be specified");
    }

    public WebAppParameter getWebApp() {
        return this.webApp;
    }

    public void setCompiler(String compiler) {
        this.compilerName = compiler;
    }

    public Vector<String> getCompileList() {
        return this.compileList;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.destDir == null) {
            throw new BuildException("destdir attribute must be set!", getLocation());
        }
        if (!this.destDir.isDirectory()) {
            throw new BuildException("destination directory \"" + this.destDir + "\" does not exist or is not a directory", getLocation());
        }
        File dest = getActualDestDir();
        AntClassLoader al = getProject().createClassLoader(this.compilerClasspath);
        try {
            JspCompilerAdapter compiler = JspCompilerAdapterFactory.getCompiler(this.compilerName, this, al);
            if (this.webApp != null) {
                doCompilation(compiler);
                if (al != null) {
                    al.close();
                }
            } else if (this.src == null) {
                throw new BuildException("srcdir attribute must be set!", getLocation());
            } else {
                String[] list = this.src.list();
                if (list.length == 0) {
                    throw new BuildException("srcdir attribute must be set!", getLocation());
                }
                if (compiler.implementsOwnDependencyChecking()) {
                    doCompilation(compiler);
                    if (al != null) {
                        al.close();
                        return;
                    }
                    return;
                }
                JspMangler mangler = compiler.createMangler();
                resetFileLists();
                int filecount = 0;
                for (String fileName : list) {
                    File srcDir = getProject().resolveFile(fileName);
                    if (!srcDir.exists()) {
                        throw new BuildException("srcdir \"" + srcDir.getPath() + "\" does not exist!", getLocation());
                    }
                    DirectoryScanner ds = getDirectoryScanner(srcDir);
                    String[] files = ds.getIncludedFiles();
                    filecount = files.length;
                    scanDir(srcDir, dest, mangler, files);
                }
                log("compiling " + this.compileList.size() + " files", 3);
                if (!this.compileList.isEmpty()) {
                    log("Compiling " + this.compileList.size() + " source file" + (this.compileList.size() == 1 ? "" : "s") + " to " + dest);
                    doCompilation(compiler);
                } else if (filecount == 0) {
                    log("there were no files to compile", 2);
                } else {
                    log("all files are up to date", 3);
                }
                if (al != null) {
                    al.close();
                }
            }
        } catch (Throwable th) {
            if (al != null) {
                try {
                    al.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private File getActualDestDir() {
        if (this.packageName == null) {
            return this.destDir;
        }
        return new File(this.destDir.getPath() + File.separatorChar + this.packageName.replace('.', File.separatorChar));
    }

    private void doCompilation(JspCompilerAdapter compiler) throws BuildException {
        compiler.setJspc(this);
        if (!compiler.execute()) {
            if (this.failOnError) {
                throw new BuildException(FAIL_MSG, getLocation());
            }
            log(FAIL_MSG, 0);
        }
    }

    protected void resetFileLists() {
        this.compileList.removeAllElements();
    }

    protected void scanDir(File srcDir, File dest, JspMangler mangler, String[] files) {
        long now = Instant.now().toEpochMilli();
        for (String filename : files) {
            File srcFile = new File(srcDir, filename);
            File javaFile = mapToJavaFile(mangler, srcFile, srcDir, dest);
            if (javaFile != null) {
                if (srcFile.lastModified() > now) {
                    log("Warning: file modified in the future: " + filename, 1);
                }
                if (isCompileNeeded(srcFile, javaFile)) {
                    this.compileList.addElement(srcFile.getAbsolutePath());
                    this.javaFiles.addElement(javaFile);
                }
            }
        }
    }

    private boolean isCompileNeeded(File srcFile, File javaFile) {
        boolean shouldCompile = false;
        if (!javaFile.exists()) {
            shouldCompile = true;
            log("Compiling " + srcFile.getPath() + " because java file " + javaFile.getPath() + " does not exist", 3);
        } else if (srcFile.lastModified() > javaFile.lastModified()) {
            shouldCompile = true;
            log("Compiling " + srcFile.getPath() + " because it is out of date with respect to " + javaFile.getPath(), 3);
        } else if (javaFile.length() == 0) {
            shouldCompile = true;
            log("Compiling " + srcFile.getPath() + " because java file " + javaFile.getPath() + " is empty", 3);
        }
        return shouldCompile;
    }

    protected File mapToJavaFile(JspMangler mangler, File srcFile, File srcDir, File dest) {
        if (!srcFile.getName().endsWith(".jsp")) {
            return null;
        }
        String javaFileName = mangler.mapJspToJavaName(srcFile);
        return new File(dest, javaFileName);
    }

    public void deleteEmptyJavaFiles() {
        if (this.javaFiles != null) {
            Iterator<File> it = this.javaFiles.iterator();
            while (it.hasNext()) {
                File file = it.next();
                if (file.exists() && file.length() == 0) {
                    log("deleting empty output file " + file);
                    file.delete();
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/JspC$WebAppParameter.class */
    public static class WebAppParameter {
        private File directory;

        public File getDirectory() {
            return this.directory;
        }

        public void setBaseDir(File directory) {
            this.directory = directory;
        }
    }
}
