package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapter;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapterFactory;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Rmic.class */
public class Rmic extends MatchingTask {
    public static final String ERROR_RMIC_FAILED = "Rmic failed; see the compiler error output for details.";
    public static final String ERROR_UNABLE_TO_VERIFY_CLASS = "Unable to verify class ";
    public static final String ERROR_NOT_FOUND = ". It could not be found.";
    public static final String ERROR_NOT_DEFINED = ". It is not defined.";
    public static final String ERROR_LOADING_CAUSED_EXCEPTION = ". Loading caused Exception: ";
    public static final String ERROR_NO_BASE_EXISTS = "base or destdir does not exist: ";
    public static final String ERROR_NOT_A_DIR = "base or destdir is not a directory:";
    public static final String ERROR_BASE_NOT_SET = "base or destdir attribute must be set!";
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private File baseDir;
    private File destDir;
    private String classname;
    private File sourceBase;
    private String stubVersion;
    private Path compileClasspath;
    private Path extDirs;
    private String iiopOpts;
    private String idlOpts;
    private boolean verify = false;
    private boolean filtering = false;
    private boolean iiop = false;
    private boolean idl = false;
    private boolean debug = false;
    private boolean includeAntRuntime = true;
    private boolean includeJavaRuntime = false;
    private Vector<String> compileList = new Vector<>();
    private AntClassLoader loader = null;
    private String executable = null;
    private boolean listFiles = false;
    private RmicAdapter nestedAdapter = null;
    private FacadeTaskHelper facade = new FacadeTaskHelper("default");

    public void setBase(File base) {
        this.baseDir = base;
    }

    public void setDestdir(File destdir) {
        this.destDir = destdir;
    }

    public File getDestdir() {
        return this.destDir;
    }

    public File getOutputDir() {
        if (getDestdir() != null) {
            return getDestdir();
        }
        return getBase();
    }

    public File getBase() {
        return this.baseDir;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClassname() {
        return this.classname;
    }

    public void setSourceBase(File sourceBase) {
        this.sourceBase = sourceBase;
    }

    public File getSourceBase() {
        return this.sourceBase;
    }

    public void setStubVersion(String stubVersion) {
        this.stubVersion = stubVersion;
    }

    public String getStubVersion() {
        return this.stubVersion;
    }

    public void setFiltering(boolean filter) {
        this.filtering = filter;
    }

    public boolean getFiltering() {
        return this.filtering;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public synchronized void setClasspath(Path classpath) {
        if (this.compileClasspath == null) {
            this.compileClasspath = classpath;
        } else {
            this.compileClasspath.append(classpath);
        }
    }

    public synchronized Path createClasspath() {
        if (this.compileClasspath == null) {
            this.compileClasspath = new Path(getProject());
        }
        return this.compileClasspath.createPath();
    }

    public void setClasspathRef(Reference pathRef) {
        createClasspath().setRefid(pathRef);
    }

    public Path getClasspath() {
        return this.compileClasspath;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public boolean getVerify() {
        return this.verify;
    }

    public void setIiop(boolean iiop) {
        this.iiop = iiop;
    }

    public boolean getIiop() {
        return this.iiop;
    }

    public void setIiopopts(String iiopOpts) {
        this.iiopOpts = iiopOpts;
    }

    public String getIiopopts() {
        return this.iiopOpts;
    }

    public void setIdl(boolean idl) {
        this.idl = idl;
    }

    public boolean getIdl() {
        return this.idl;
    }

    public void setIdlopts(String idlOpts) {
        this.idlOpts = idlOpts;
    }

    public String getIdlopts() {
        return this.idlOpts;
    }

    public Vector<String> getFileList() {
        return this.compileList;
    }

    public void setIncludeantruntime(boolean include) {
        this.includeAntRuntime = include;
    }

    public boolean getIncludeantruntime() {
        return this.includeAntRuntime;
    }

    public void setIncludejavaruntime(boolean include) {
        this.includeJavaRuntime = include;
    }

    public boolean getIncludejavaruntime() {
        return this.includeJavaRuntime;
    }

    public synchronized void setExtdirs(Path extDirs) {
        if (this.extDirs == null) {
            this.extDirs = extDirs;
        } else {
            this.extDirs.append(extDirs);
        }
    }

    public synchronized Path createExtdirs() {
        if (this.extDirs == null) {
            this.extDirs = new Path(getProject());
        }
        return this.extDirs.createPath();
    }

    public Path getExtdirs() {
        return this.extDirs;
    }

    public Vector<String> getCompileList() {
        return this.compileList;
    }

    public void setCompiler(String compiler) {
        if (!compiler.isEmpty()) {
            this.facade.setImplementation(compiler);
        }
    }

    public String getCompiler() {
        this.facade.setMagicValue(getProject().getProperty("build.rmic"));
        return this.facade.getImplementation();
    }

    public ImplementationSpecificArgument createCompilerArg() {
        ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
        this.facade.addImplementationArgument(arg);
        return arg;
    }

    public String[] getCurrentCompilerArgs() {
        getCompiler();
        return this.facade.getArgs();
    }

    public void setExecutable(String ex) {
        this.executable = ex;
    }

    public String getExecutable() {
        return this.executable;
    }

    public Path createCompilerClasspath() {
        return this.facade.getImplementationClasspath(getProject());
    }

    public void setListfiles(boolean list) {
        this.listFiles = list;
    }

    public void add(RmicAdapter adapter) {
        if (this.nestedAdapter != null) {
            throw new BuildException("Can't have more than one rmic adapter");
        }
        this.nestedAdapter = adapter;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        try {
            this.compileList.clear();
            File outputDir = getOutputDir();
            if (outputDir == null) {
                throw new BuildException(ERROR_BASE_NOT_SET, getLocation());
            }
            if (!outputDir.exists()) {
                throw new BuildException(ERROR_NO_BASE_EXISTS + outputDir, getLocation());
            }
            if (!outputDir.isDirectory()) {
                throw new BuildException(ERROR_NOT_A_DIR + outputDir, getLocation());
            }
            if (this.verify) {
                log("Verify has been turned on.", 3);
            }
            RmicAdapter adapter = this.nestedAdapter != null ? this.nestedAdapter : RmicAdapterFactory.getRmic(getCompiler(), this, createCompilerClasspath());
            adapter.setRmic(this);
            Path classpath = adapter.getClasspath();
            this.loader = getProject().createClassLoader(classpath);
            if (this.classname == null) {
                DirectoryScanner ds = getDirectoryScanner(this.baseDir);
                scanDir(this.baseDir, ds.getIncludedFiles(), adapter.getMapper());
            } else {
                String path = this.classname.replace('.', File.separatorChar) + ".class";
                File f = new File(this.baseDir, path);
                if (f.isFile()) {
                    scanDir(this.baseDir, new String[]{path}, adapter.getMapper());
                } else {
                    this.compileList.add(this.classname);
                }
            }
            int fileCount = this.compileList.size();
            if (fileCount > 0) {
                log("RMI Compiling " + fileCount + " class" + (fileCount > 1 ? "es" : "") + " to " + outputDir, 2);
                if (this.listFiles) {
                    this.compileList.forEach(this::log);
                }
                if (!adapter.execute()) {
                    throw new BuildException(ERROR_RMIC_FAILED, getLocation());
                }
            }
            if (null != this.sourceBase && !outputDir.equals(this.sourceBase) && fileCount > 0) {
                if (this.idl) {
                    log("Cannot determine sourcefiles in idl mode, ", 1);
                    log("sourcebase attribute will be ignored.", 1);
                } else {
                    this.compileList.forEach(f2 -> {
                        moveGeneratedFile(outputDir, this.sourceBase, adapter, outputDir);
                    });
                }
            }
        } finally {
            cleanup();
        }
    }

    protected void cleanup() {
        if (this.loader != null) {
            this.loader.cleanup();
            this.loader = null;
        }
    }

    private void moveGeneratedFile(File baseDir, File sourceBaseFile, String classname, RmicAdapter adapter) throws BuildException {
        String classFileName = classname.replace('.', File.separatorChar) + ".class";
        String[] generatedFiles = adapter.getMapper().mapFileName(classFileName);
        if (generatedFiles == null) {
            return;
        }
        for (String generatedFile : generatedFiles) {
            if (generatedFile.endsWith(".class")) {
                String sourceFileName = StringUtils.removeSuffix(generatedFile, ".class") + ".java";
                File oldFile = new File(baseDir, sourceFileName);
                if (oldFile.exists()) {
                    File newFile = new File(sourceBaseFile, sourceFileName);
                    try {
                        if (this.filtering) {
                            FILE_UTILS.copyFile(oldFile, newFile, new FilterSetCollection(getProject().getGlobalFilterSet()));
                        } else {
                            FILE_UTILS.copyFile(oldFile, newFile);
                        }
                        oldFile.delete();
                    } catch (IOException ioe) {
                        throw new BuildException("Failed to copy " + oldFile + " to " + newFile + " due to " + ioe.getMessage(), ioe, getLocation());
                    }
                } else {
                    continue;
                }
            }
        }
    }

    protected void scanDir(File baseDir, String[] files, FileNameMapper mapper) {
        String[] newFiles = files;
        if (this.idl) {
            log("will leave uptodate test to rmic implementation in idl mode.", 3);
        } else if (this.iiop && this.iiopOpts != null && this.iiopOpts.contains("-always")) {
            log("no uptodate test as -always option has been specified", 3);
        } else {
            SourceFileScanner sfs = new SourceFileScanner(this);
            newFiles = sfs.restrict(files, baseDir, getOutputDir(), mapper);
        }
        Stream map = Stream.of((Object[]) newFiles).map(s -> {
            return s.replace(File.separatorChar, '.');
        }).map(s2 -> {
            return s2.substring(0, s2.lastIndexOf(".class"));
        });
        Vector<String> vector = this.compileList;
        Objects.requireNonNull(vector);
        map.forEach((v1) -> {
            r1.add(v1);
        });
    }

    public boolean isValidRmiRemote(String classname) {
        try {
            Class<?> testClass = this.loader.loadClass(classname);
            if (!testClass.isInterface() || this.iiop || this.idl) {
                if (isValidRmiRemote(testClass)) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e) {
            log(ERROR_UNABLE_TO_VERIFY_CLASS + classname + ERROR_NOT_FOUND, 1);
            return false;
        } catch (NoClassDefFoundError e2) {
            log(ERROR_UNABLE_TO_VERIFY_CLASS + classname + ERROR_NOT_DEFINED, 1);
            return false;
        } catch (Throwable t) {
            log(ERROR_UNABLE_TO_VERIFY_CLASS + classname + ERROR_LOADING_CAUSED_EXCEPTION + t.getMessage(), 1);
            return false;
        }
    }

    public Class<?> getRemoteInterface(Class<?> testClass) {
        Stream of = Stream.of((Object[]) testClass.getInterfaces());
        Objects.requireNonNull(Remote.class);
        return (Class) of.filter(this::isAssignableFrom).findFirst().orElse(null);
    }

    private boolean isValidRmiRemote(Class<?> testClass) {
        return Remote.class.isAssignableFrom(testClass);
    }

    public ClassLoader getLoader() {
        return this.loader;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Rmic$ImplementationSpecificArgument.class */
    public class ImplementationSpecificArgument extends org.apache.tools.ant.util.facade.ImplementationSpecificArgument {
        public ImplementationSpecificArgument() {
        }

        public void setCompiler(String impl) {
            super.setImplementation(impl);
        }
    }
}
