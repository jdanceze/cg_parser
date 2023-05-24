package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.taskdefs.compilers.CompilerAdapter;
import org.apache.tools.ant.taskdefs.compilers.CompilerAdapterExtension;
import org.apache.tools.ant.taskdefs.compilers.CompilerAdapterFactory;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javac.class */
public class Javac extends MatchingTask {
    private static final String FAIL_MSG = "Compile failed; see the compiler error output for details.";
    private static final String JAVAC10_PLUS = "javac10+";
    private static final String JAVAC9 = "javac9";
    private static final String JAVAC9_ALIAS = "javac1.9";
    private static final String JAVAC1_8 = "javac1.8";
    private static final String JAVAC1_7 = "javac1.7";
    private static final String JAVAC1_6 = "javac1.6";
    private static final String JAVAC1_5 = "javac1.5";
    private static final String JAVAC1_4 = "javac1.4";
    private static final String JAVAC1_3 = "javac1.3";
    private static final String JAVAC1_2 = "javac1.2";
    private static final String JAVAC1_1 = "javac1.1";
    private static final String MODERN = "modern";
    private static final String CLASSIC = "classic";
    private static final String EXTJAVAC = "extJavac";
    private static final char GROUP_START_MARK = '{';
    private static final char GROUP_END_MARK = '}';
    private static final char GROUP_SEP_MARK = ',';
    private static final String MODULE_MARKER = "*";
    private static final FileUtils FILE_UTILS;
    private Path src;
    private File destDir;
    private File nativeHeaderDir;
    private Path compileClasspath;
    private Path modulepath;
    private Path upgrademodulepath;
    private Path compileSourcepath;
    private Path moduleSourcepath;
    private String encoding;
    private String targetAttribute;
    private String release;
    private Path bootclasspath;
    private Path extdirs;
    private Boolean includeAntRuntime;
    private String memoryInitialSize;
    private String memoryMaximumSize;
    private FacadeTaskHelper facade;
    private String source;
    private String debugLevel;
    private File tmpDir;
    private String updatedProperty;
    private String errorProperty;
    private static final byte[] PACKAGE_INFO_CLASS_HEADER;
    private static final byte[] PACKAGE_INFO_CLASS_FOOTER;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean debug = false;
    private boolean optimize = false;
    private boolean deprecation = false;
    private boolean depend = false;
    private boolean verbose = false;
    private boolean includeJavaRuntime = false;
    private boolean fork = false;
    private String forkedExecutable = null;
    private boolean nowarn = false;
    protected boolean failOnError = true;
    protected boolean listFiles = false;
    protected File[] compileList = new File[0];
    private Map<String, Long> packageInfos = new HashMap();
    private boolean taskSuccess = true;
    private boolean includeDestClasses = true;
    private CompilerAdapter nestedAdapter = null;
    private boolean createMissingPackageInfoClass = true;

    static {
        $assertionsDisabled = !Javac.class.desiredAssertionStatus();
        FILE_UTILS = FileUtils.getFileUtils();
        PACKAGE_INFO_CLASS_HEADER = new byte[]{-54, -2, -70, -66, 0, 0, 0, 49, 0, 7, 7, 0, 5, 7, 0, 6, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 17, 112, 97, 99, 107, 97, 103, 101, 45, 105, 110, 102, 111, 46, 106, 97, 118, 97, 1};
        PACKAGE_INFO_CLASS_FOOTER = new byte[]{47, 112, 97, 99, 107, 97, 103, 101, 45, 105, 110, 102, 111, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 2, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0, 2, 0, 4};
    }

    public Javac() {
        this.facade = null;
        this.facade = new FacadeTaskHelper(assumedJavaVersion());
    }

    private String assumedJavaVersion() {
        if (JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_8)) {
            return JAVAC1_8;
        }
        if (JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_9)) {
            return JAVAC9;
        }
        if (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_10)) {
            return JAVAC10_PLUS;
        }
        return MODERN;
    }

    public String getDebugLevel() {
        return this.debugLevel;
    }

    public void setDebugLevel(String v) {
        this.debugLevel = v;
    }

    public String getSource() {
        return this.source != null ? this.source : getProject().getProperty(MagicNames.BUILD_JAVAC_SOURCE);
    }

    public void setSource(String v) {
        this.source = v;
    }

    public Path createSrc() {
        if (this.src == null) {
            this.src = new Path(getProject());
        }
        return this.src.createPath();
    }

    protected Path recreateSrc() {
        this.src = null;
        return createSrc();
    }

    public void setSrcdir(Path srcDir) {
        if (this.src == null) {
            this.src = srcDir;
        } else {
            this.src.append(srcDir);
        }
    }

    public Path getSrcdir() {
        return this.src;
    }

    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    public File getDestdir() {
        return this.destDir;
    }

    public void setNativeHeaderDir(File nhDir) {
        this.nativeHeaderDir = nhDir;
    }

    public File getNativeHeaderDir() {
        return this.nativeHeaderDir;
    }

    public void setSourcepath(Path sourcepath) {
        if (this.compileSourcepath == null) {
            this.compileSourcepath = sourcepath;
        } else {
            this.compileSourcepath.append(sourcepath);
        }
    }

    public Path getSourcepath() {
        return this.compileSourcepath;
    }

    public Path createSourcepath() {
        if (this.compileSourcepath == null) {
            this.compileSourcepath = new Path(getProject());
        }
        return this.compileSourcepath.createPath();
    }

    public void setSourcepathRef(Reference r) {
        createSourcepath().setRefid(r);
    }

    public void setModulesourcepath(Path msp) {
        if (this.moduleSourcepath == null) {
            this.moduleSourcepath = msp;
        } else {
            this.moduleSourcepath.append(msp);
        }
    }

    public Path getModulesourcepath() {
        return this.moduleSourcepath;
    }

    public Path createModulesourcepath() {
        if (this.moduleSourcepath == null) {
            this.moduleSourcepath = new Path(getProject());
        }
        return this.moduleSourcepath.createPath();
    }

    public void setModulesourcepathRef(Reference r) {
        createModulesourcepath().setRefid(r);
    }

    public void setClasspath(Path classpath) {
        if (this.compileClasspath == null) {
            this.compileClasspath = classpath;
        } else {
            this.compileClasspath.append(classpath);
        }
    }

    public Path getClasspath() {
        return this.compileClasspath;
    }

    public Path createClasspath() {
        if (this.compileClasspath == null) {
            this.compileClasspath = new Path(getProject());
        }
        return this.compileClasspath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setModulepath(Path mp) {
        if (this.modulepath == null) {
            this.modulepath = mp;
        } else {
            this.modulepath.append(mp);
        }
    }

    public Path getModulepath() {
        return this.modulepath;
    }

    public Path createModulepath() {
        if (this.modulepath == null) {
            this.modulepath = new Path(getProject());
        }
        return this.modulepath.createPath();
    }

    public void setModulepathRef(Reference r) {
        createModulepath().setRefid(r);
    }

    public void setUpgrademodulepath(Path ump) {
        if (this.upgrademodulepath == null) {
            this.upgrademodulepath = ump;
        } else {
            this.upgrademodulepath.append(ump);
        }
    }

    public Path getUpgrademodulepath() {
        return this.upgrademodulepath;
    }

    public Path createUpgrademodulepath() {
        if (this.upgrademodulepath == null) {
            this.upgrademodulepath = new Path(getProject());
        }
        return this.upgrademodulepath.createPath();
    }

    public void setUpgrademodulepathRef(Reference r) {
        createUpgrademodulepath().setRefid(r);
    }

    public void setBootclasspath(Path bootclasspath) {
        if (this.bootclasspath == null) {
            this.bootclasspath = bootclasspath;
        } else {
            this.bootclasspath.append(bootclasspath);
        }
    }

    public Path getBootclasspath() {
        return this.bootclasspath;
    }

    public Path createBootclasspath() {
        if (this.bootclasspath == null) {
            this.bootclasspath = new Path(getProject());
        }
        return this.bootclasspath.createPath();
    }

    public void setBootClasspathRef(Reference r) {
        createBootclasspath().setRefid(r);
    }

    public void setExtdirs(Path extdirs) {
        if (this.extdirs == null) {
            this.extdirs = extdirs;
        } else {
            this.extdirs.append(extdirs);
        }
    }

    public Path getExtdirs() {
        return this.extdirs;
    }

    public Path createExtdirs() {
        if (this.extdirs == null) {
            this.extdirs = new Path(getProject());
        }
        return this.extdirs.createPath();
    }

    public void setListfiles(boolean list) {
        this.listFiles = list;
    }

    public boolean getListfiles() {
        return this.listFiles;
    }

    public void setFailonerror(boolean fail) {
        this.failOnError = fail;
    }

    public void setProceed(boolean proceed) {
        this.failOnError = !proceed;
    }

    public boolean getFailonerror() {
        return this.failOnError;
    }

    public void setDeprecation(boolean deprecation) {
        this.deprecation = deprecation;
    }

    public boolean getDeprecation() {
        return this.deprecation;
    }

    public void setMemoryInitialSize(String memoryInitialSize) {
        this.memoryInitialSize = memoryInitialSize;
    }

    public String getMemoryInitialSize() {
        return this.memoryInitialSize;
    }

    public void setMemoryMaximumSize(String memoryMaximumSize) {
        this.memoryMaximumSize = memoryMaximumSize;
    }

    public String getMemoryMaximumSize() {
        return this.memoryMaximumSize;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }

    public boolean getOptimize() {
        return this.optimize;
    }

    public void setDepend(boolean depend) {
        this.depend = depend;
    }

    public boolean getDepend() {
        return this.depend;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean getVerbose() {
        return this.verbose;
    }

    public void setTarget(String target) {
        this.targetAttribute = target;
    }

    public String getTarget() {
        if (this.targetAttribute != null) {
            return this.targetAttribute;
        }
        return getProject().getProperty(MagicNames.BUILD_JAVAC_TARGET);
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRelease() {
        return this.release;
    }

    public void setIncludeantruntime(boolean include) {
        this.includeAntRuntime = Boolean.valueOf(include);
    }

    public boolean getIncludeantruntime() {
        return this.includeAntRuntime == null || this.includeAntRuntime.booleanValue();
    }

    public void setIncludejavaruntime(boolean include) {
        this.includeJavaRuntime = include;
    }

    public boolean getIncludejavaruntime() {
        return this.includeJavaRuntime;
    }

    public void setFork(boolean f) {
        this.fork = f;
    }

    public void setExecutable(String forkExec) {
        this.forkedExecutable = forkExec;
    }

    public String getExecutable() {
        return this.forkedExecutable;
    }

    public boolean isForkedJavac() {
        return this.fork || EXTJAVAC.equalsIgnoreCase(getCompiler());
    }

    public String getJavacExecutable() {
        if (this.forkedExecutable == null && isForkedJavac()) {
            this.forkedExecutable = getSystemJavac();
        } else if (this.forkedExecutable != null && !isForkedJavac()) {
            this.forkedExecutable = null;
        }
        return this.forkedExecutable;
    }

    public void setNowarn(boolean flag) {
        this.nowarn = flag;
    }

    public boolean getNowarn() {
        return this.nowarn;
    }

    public ImplementationSpecificArgument createCompilerArg() {
        ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
        this.facade.addImplementationArgument(arg);
        return arg;
    }

    public String[] getCurrentCompilerArgs() {
        String chosen = this.facade.getExplicitChoice();
        try {
            String appliedCompiler = getCompiler();
            this.facade.setImplementation(appliedCompiler);
            String[] result = this.facade.getArgs();
            String altCompilerName = getAltCompilerName(this.facade.getImplementation());
            if (result.length == 0 && altCompilerName != null) {
                this.facade.setImplementation(altCompilerName);
                result = this.facade.getArgs();
            }
            return result;
        } finally {
            this.facade.setImplementation(chosen);
        }
    }

    private String getAltCompilerName(String anImplementation) {
        if (JAVAC10_PLUS.equalsIgnoreCase(anImplementation) || JAVAC9.equalsIgnoreCase(anImplementation) || JAVAC9_ALIAS.equalsIgnoreCase(anImplementation) || JAVAC1_8.equalsIgnoreCase(anImplementation) || JAVAC1_7.equalsIgnoreCase(anImplementation) || JAVAC1_6.equalsIgnoreCase(anImplementation) || JAVAC1_5.equalsIgnoreCase(anImplementation) || JAVAC1_4.equalsIgnoreCase(anImplementation) || JAVAC1_3.equalsIgnoreCase(anImplementation)) {
            return MODERN;
        }
        if (JAVAC1_2.equalsIgnoreCase(anImplementation) || JAVAC1_1.equalsIgnoreCase(anImplementation)) {
            return CLASSIC;
        }
        if (MODERN.equalsIgnoreCase(anImplementation)) {
            String nextSelected = assumedJavaVersion();
            if (JAVAC10_PLUS.equalsIgnoreCase(anImplementation) || JAVAC9.equalsIgnoreCase(nextSelected) || JAVAC1_8.equalsIgnoreCase(nextSelected)) {
                return nextSelected;
            }
        }
        if (CLASSIC.equalsIgnoreCase(anImplementation)) {
            return assumedJavaVersion();
        }
        if (EXTJAVAC.equalsIgnoreCase(anImplementation)) {
            return assumedJavaVersion();
        }
        return null;
    }

    public void setTempdir(File tmpDir) {
        this.tmpDir = tmpDir;
    }

    public File getTempdir() {
        return this.tmpDir;
    }

    public void setUpdatedProperty(String updatedProperty) {
        this.updatedProperty = updatedProperty;
    }

    public void setErrorProperty(String errorProperty) {
        this.errorProperty = errorProperty;
    }

    public void setIncludeDestClasses(boolean includeDestClasses) {
        this.includeDestClasses = includeDestClasses;
    }

    public boolean isIncludeDestClasses() {
        return this.includeDestClasses;
    }

    public boolean getTaskSuccess() {
        return this.taskSuccess;
    }

    public Path createCompilerClasspath() {
        return this.facade.getImplementationClasspath(getProject());
    }

    public void add(CompilerAdapter adapter) {
        if (this.nestedAdapter != null) {
            throw new BuildException("Can't have more than one compiler adapter");
        }
        this.nestedAdapter = adapter;
    }

    public void setCreateMissingPackageInfoClass(boolean b) {
        this.createMissingPackageInfoClass = b;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        checkParameters();
        resetFileLists();
        if (hasPath(this.src)) {
            collectFileListFromSourcePath();
        } else if (!$assertionsDisabled && !hasPath(this.moduleSourcepath)) {
            throw new AssertionError("Either srcDir or moduleSourcepath must be given");
        } else {
            collectFileListFromModulePath();
        }
        compile();
        if (this.updatedProperty != null && this.taskSuccess && this.compileList.length != 0) {
            getProject().setNewProperty(this.updatedProperty, "true");
        }
    }

    protected void resetFileLists() {
        this.compileList = new File[0];
        this.packageInfos = new HashMap();
    }

    protected void scanDir(File srcDir, File destDir, String[] files) {
        String[] findSupportedFileExtensions;
        GlobPatternMapper m = new GlobPatternMapper();
        for (String extension : findSupportedFileExtensions()) {
            m.setFrom(extension);
            m.setTo("*.class");
            SourceFileScanner sfs = new SourceFileScanner(this);
            File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);
            if (newFiles.length > 0) {
                lookForPackageInfos(srcDir, newFiles);
                File[] newCompileList = new File[this.compileList.length + newFiles.length];
                System.arraycopy(this.compileList, 0, newCompileList, 0, this.compileList.length);
                System.arraycopy(newFiles, 0, newCompileList, this.compileList.length, newFiles.length);
                this.compileList = newCompileList;
            }
        }
    }

    private void collectFileListFromSourcePath() {
        String[] list;
        for (String filename : this.src.list()) {
            File srcDir = getProject().resolveFile(filename);
            if (!srcDir.exists()) {
                throw new BuildException("srcdir \"" + srcDir.getPath() + "\" does not exist!", getLocation());
            }
            DirectoryScanner ds = getDirectoryScanner(srcDir);
            scanDir(srcDir, this.destDir != null ? this.destDir : srcDir, ds.getIncludedFiles());
        }
    }

    private void collectFileListFromModulePath() {
        String[] list;
        FileUtils fu = FileUtils.getFileUtils();
        for (String pathElement : this.moduleSourcepath.list()) {
            boolean valid = false;
            for (Map.Entry<String, Collection<File>> modules : resolveModuleSourcePathElement(getProject().getBaseDir(), pathElement).entrySet()) {
                String moduleName = modules.getKey();
                for (File srcDir : modules.getValue()) {
                    if (srcDir.exists()) {
                        valid = true;
                        DirectoryScanner ds = getDirectoryScanner(srcDir);
                        String[] files = ds.getIncludedFiles();
                        scanDir(srcDir, fu.resolveFile(this.destDir, moduleName), files);
                    }
                }
            }
            if (!valid) {
                throw new BuildException("modulesourcepath \"" + pathElement + "\" does not exist!", getLocation());
            }
        }
    }

    private String[] findSupportedFileExtensions() {
        String compilerImpl = getCompiler();
        CompilerAdapter adapter = this.nestedAdapter != null ? this.nestedAdapter : CompilerAdapterFactory.getCompiler(compilerImpl, this, createCompilerClasspath());
        String[] extensions = null;
        if (adapter instanceof CompilerAdapterExtension) {
            extensions = ((CompilerAdapterExtension) adapter).getSupportedFileExtensions();
        }
        if (extensions == null) {
            extensions = new String[]{"java"};
        }
        for (int i = 0; i < extensions.length; i++) {
            if (!extensions[i].startsWith("*.")) {
                extensions[i] = "*." + extensions[i];
            }
        }
        return extensions;
    }

    public File[] getFileList() {
        return this.compileList;
    }

    protected boolean isJdkCompiler(String compilerImpl) {
        return MODERN.equals(compilerImpl) || CLASSIC.equals(compilerImpl) || JAVAC10_PLUS.equals(compilerImpl) || JAVAC9.equals(compilerImpl) || JAVAC1_8.equals(compilerImpl) || JAVAC1_7.equals(compilerImpl) || JAVAC1_6.equals(compilerImpl) || JAVAC1_5.equals(compilerImpl) || JAVAC1_4.equals(compilerImpl) || JAVAC1_3.equals(compilerImpl) || JAVAC1_2.equals(compilerImpl) || JAVAC1_1.equals(compilerImpl);
    }

    protected String getSystemJavac() {
        return JavaEnvUtils.getJdkExecutable("javac");
    }

    public void setCompiler(String compiler) {
        this.facade.setImplementation(compiler);
    }

    public String getCompiler() {
        String compilerImpl = getCompilerVersion();
        if (this.fork) {
            if (isJdkCompiler(compilerImpl)) {
                compilerImpl = EXTJAVAC;
            } else {
                log("Since compiler setting isn't classic or modern, ignoring fork setting.", 1);
            }
        }
        return compilerImpl;
    }

    public String getCompilerVersion() {
        this.facade.setMagicValue(getProject().getProperty("build.compiler"));
        return this.facade.getImplementation();
    }

    protected void checkParameters() throws BuildException {
        if (hasPath(this.src)) {
            if (hasPath(this.moduleSourcepath)) {
                throw new BuildException("modulesourcepath cannot be combined with srcdir attribute!", getLocation());
            }
        } else if (hasPath(this.moduleSourcepath)) {
            if (hasPath(this.src) || hasPath(this.compileSourcepath)) {
                throw new BuildException("modulesourcepath cannot be combined with srcdir or sourcepath !", getLocation());
            }
            if (this.destDir == null) {
                throw new BuildException("modulesourcepath requires destdir attribute to be set!", getLocation());
            }
        } else {
            throw new BuildException("either srcdir or modulesourcepath attribute must be set!", getLocation());
        }
        if (this.destDir != null && !this.destDir.isDirectory()) {
            throw new BuildException("destination directory \"" + this.destDir + "\" does not exist or is not a directory", getLocation());
        }
        if (this.includeAntRuntime == null && getProject().getProperty(MagicNames.BUILD_SYSCLASSPATH) == null) {
            log(getLocation() + "warning: 'includeantruntime' was not set, defaulting to " + MagicNames.BUILD_SYSCLASSPATH + "=last; set to false for repeatable builds", 1);
        }
    }

    protected void compile() {
        File resolveFile;
        File[] fileArr;
        String compilerImpl = getCompiler();
        if (this.compileList.length > 0) {
            log("Compiling " + this.compileList.length + " source file" + (this.compileList.length == 1 ? "" : "s") + (this.destDir != null ? " to " + this.destDir : ""));
            if (this.listFiles) {
                for (File element : this.compileList) {
                    log(element.getAbsolutePath());
                }
            }
            CompilerAdapter adapter = this.nestedAdapter != null ? this.nestedAdapter : CompilerAdapterFactory.getCompiler(compilerImpl, this, createCompilerClasspath());
            adapter.setJavac(this);
            if (adapter.execute()) {
                if (this.createMissingPackageInfoClass) {
                    try {
                        if (this.destDir != null) {
                            resolveFile = this.destDir;
                        } else {
                            resolveFile = getProject().resolveFile(this.src.list()[0]);
                        }
                        generateMissingPackageInfoClasses(resolveFile);
                        return;
                    } catch (IOException x) {
                        throw new BuildException(x, getLocation());
                    }
                }
                return;
            }
            this.taskSuccess = false;
            if (this.errorProperty != null) {
                getProject().setNewProperty(this.errorProperty, "true");
            }
            if (this.failOnError) {
                throw new BuildException(FAIL_MSG, getLocation());
            }
            log(FAIL_MSG, 0);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javac$ImplementationSpecificArgument.class */
    public class ImplementationSpecificArgument extends org.apache.tools.ant.util.facade.ImplementationSpecificArgument {
        public ImplementationSpecificArgument() {
        }

        public void setCompiler(String impl) {
            super.setImplementation(impl);
        }
    }

    private void lookForPackageInfos(File srcDir, File[] newFiles) {
        for (File f : newFiles) {
            if ("package-info.java".equals(f.getName())) {
                String path = FILE_UTILS.removeLeadingPath(srcDir, f).replace(File.separatorChar, '/');
                if (!path.endsWith("/package-info.java")) {
                    log("anomalous package-info.java path: " + path, 1);
                } else {
                    String pkg = path.substring(0, path.length() - "/package-info.java".length());
                    this.packageInfos.put(pkg, Long.valueOf(f.lastModified()));
                }
            }
        }
    }

    private void generateMissingPackageInfoClasses(File dest) throws IOException {
        for (Map.Entry<String, Long> entry : this.packageInfos.entrySet()) {
            String pkg = entry.getKey();
            Long sourceLastMod = entry.getValue();
            File pkgBinDir = new File(dest, pkg.replace('/', File.separatorChar));
            pkgBinDir.mkdirs();
            File pkgInfoClass = new File(pkgBinDir, "package-info.class");
            if (!pkgInfoClass.isFile() || pkgInfoClass.lastModified() < sourceLastMod.longValue()) {
                log("Creating empty " + pkgInfoClass);
                OutputStream os = Files.newOutputStream(pkgInfoClass.toPath(), new OpenOption[0]);
                try {
                    os.write(PACKAGE_INFO_CLASS_HEADER);
                    byte[] name = pkg.getBytes(StandardCharsets.UTF_8);
                    int length = name.length + 13;
                    os.write(((byte) length) / 256);
                    os.write(((byte) length) % 256);
                    os.write(name);
                    os.write(PACKAGE_INFO_CLASS_FOOTER);
                    if (os != null) {
                        os.close();
                    }
                } catch (Throwable th) {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
        }
    }

    private static boolean hasPath(Path path) {
        return (path == null || path.isEmpty()) ? false : true;
    }

    private static Map<String, Collection<File>> resolveModuleSourcePathElement(File projectDir, String element) {
        Map<String, Collection<File>> result = new TreeMap<>();
        for (CharSequence resolvedElement : expandGroups(element)) {
            findModules(projectDir, resolvedElement.toString(), result);
        }
        return result;
    }

    private static Collection<? extends CharSequence> expandGroups(CharSequence element) {
        List<StringBuilder> result = new ArrayList<>();
        result.add(new StringBuilder());
        StringBuilder resolved = new StringBuilder();
        int i = 0;
        while (i < element.length()) {
            char c = element.charAt(i);
            switch (c) {
                case '{':
                    int end = getGroupEndIndex(element, i);
                    if (end < 0) {
                        throw new BuildException(String.format("Unclosed group %s, starting at: %d", element, Integer.valueOf(i)));
                    }
                    Collection<? extends CharSequence> parts = resolveGroup(element.subSequence(i + 1, end));
                    switch (parts.size()) {
                        case 0:
                            break;
                        case 1:
                            resolved.append(parts.iterator().next());
                            break;
                        default:
                            List<StringBuilder> oldRes = result;
                            result = new ArrayList<>(oldRes.size() * parts.size());
                            for (CharSequence part : parts) {
                                for (StringBuilder prefix : oldRes) {
                                    result.add(new StringBuilder(prefix).append((CharSequence) resolved).append(part));
                                }
                            }
                            resolved = new StringBuilder();
                            break;
                    }
                    i = end;
                    break;
                default:
                    resolved.append(c);
                    break;
            }
            i++;
        }
        for (StringBuilder prefix2 : result) {
            prefix2.append((CharSequence) resolved);
        }
        return result;
    }

    private static Collection<? extends CharSequence> resolveGroup(CharSequence group) {
        Collection<CharSequence> result = new ArrayList<>();
        int start = 0;
        int depth = 0;
        for (int i = 0; i < group.length(); i++) {
            char c = group.charAt(i);
            switch (c) {
                case ',':
                    if (depth == 0) {
                        result.addAll(expandGroups(group.subSequence(start, i)));
                        start = i + 1;
                        break;
                    } else {
                        break;
                    }
                case '{':
                    depth++;
                    break;
                case '}':
                    depth--;
                    break;
            }
        }
        result.addAll(expandGroups(group.subSequence(start, group.length())));
        return result;
    }

    private static int getGroupEndIndex(CharSequence element, int start) {
        int depth = 0;
        for (int i = start; i < element.length(); i++) {
            char c = element.charAt(i);
            switch (c) {
                case '{':
                    depth++;
                    break;
                case '}':
                    depth--;
                    if (depth != 0) {
                        break;
                    } else {
                        return i;
                    }
            }
        }
        return -1;
    }

    private static void findModules(File root, String pattern, Map<String, Collection<File>> collector) {
        String pattern2 = pattern.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        int startIndex = pattern2.indexOf("*");
        if (startIndex == -1) {
            findModules(root, pattern2, null, collector);
        } else if (startIndex == 0) {
            throw new BuildException("The modulesourcepath entry must be a folder.");
        } else {
            int endIndex = startIndex + "*".length();
            if (pattern2.charAt(startIndex - 1) != File.separatorChar) {
                throw new BuildException("The module mark must be preceded by separator");
            }
            if (endIndex < pattern2.length() && pattern2.charAt(endIndex) != File.separatorChar) {
                throw new BuildException("The module mark must be followed by separator");
            }
            if (pattern2.indexOf("*", endIndex) != -1) {
                throw new BuildException("The modulesourcepath entry must contain at most one module mark");
            }
            String pathToModule = pattern2.substring(0, startIndex);
            String pathInModule = endIndex == pattern2.length() ? null : pattern2.substring(endIndex + 1);
            findModules(root, pathToModule, pathInModule, collector);
        }
    }

    private static void findModules(File root, String pathToModule, String pathInModule, Map<String, Collection<File>> collector) {
        File[] listFiles;
        File f = FileUtils.getFileUtils().resolveFile(root, pathToModule);
        if (!f.isDirectory()) {
            return;
        }
        for (File module : f.listFiles((v0) -> {
            return v0.isDirectory();
        })) {
            String moduleName = module.getName();
            File moduleSourceRoot = pathInModule == null ? module : new File(module, pathInModule);
            Collection<File> moduleRoots = collector.computeIfAbsent(moduleName, k -> {
                return new ArrayList();
            });
            moduleRoots.add(moduleSourceRoot);
        }
    }
}
