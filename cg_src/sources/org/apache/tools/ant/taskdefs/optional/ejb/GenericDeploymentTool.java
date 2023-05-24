package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import javax.xml.parsers.SAXParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.optional.ejb.EjbJar;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.depend.DependencyAnalyzer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/GenericDeploymentTool.class */
public class GenericDeploymentTool implements EJBDeploymentTool {
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final int JAR_COMPRESS_LEVEL = 9;
    protected static final String META_DIR = "META-INF/";
    protected static final String MANIFEST = "META-INF/MANIFEST.MF";
    protected static final String EJB_DD = "ejb-jar.xml";
    public static final String ANALYZER_SUPER = "super";
    public static final String ANALYZER_FULL = "full";
    public static final String ANALYZER_NONE = "none";
    public static final String DEFAULT_ANALYZER = "super";
    public static final String ANALYZER_CLASS_SUPER = "org.apache.tools.ant.util.depend.bcel.AncestorAnalyzer";
    public static final String ANALYZER_CLASS_FULL = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";
    private EjbJar.Config config;
    private File destDir;
    private Path classpath;
    private Task task;
    private Set<String> addedfiles;
    private DescriptorHandler handler;
    private DependencyAnalyzer dependencyAnalyzer;
    private String genericJarSuffix = "-generic.jar";
    private ClassLoader classpathLoader = null;

    public void setDestdir(File inDir) {
        this.destDir = inDir;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public File getDestDir() {
        return this.destDir;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void setTask(Task task) {
        this.task = task;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Task getTask() {
        return this.task;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public EjbJar.Config getConfig() {
        return this.config;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean usingBaseJarName() {
        return this.config.baseJarName != null;
    }

    public void setGenericJarSuffix(String inString) {
        this.genericJarSuffix = inString;
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(this.task.getProject());
        }
        return this.classpath.createPath();
    }

    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Path getCombinedClasspath() {
        Path combinedPath = this.classpath;
        if (this.config.classpath != null) {
            if (combinedPath == null) {
                combinedPath = this.config.classpath;
            } else {
                combinedPath.append(this.config.classpath);
            }
        }
        return combinedPath;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void log(String message, int level) {
        getTask().log(message, level);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Location getLocation() {
        return getTask().getLocation();
    }

    private void createAnalyzer() {
        String analyzerClassName;
        String analyzer = this.config.analyzer;
        if (analyzer == null) {
            analyzer = "super";
        }
        if (analyzer.equals("none")) {
            return;
        }
        String str = analyzer;
        boolean z = true;
        switch (str.hashCode()) {
            case 3154575:
                if (str.equals(ANALYZER_FULL)) {
                    z = true;
                    break;
                }
                break;
            case 109801339:
                if (str.equals("super")) {
                    z = false;
                    break;
                }
                break;
        }
        switch (z) {
            case false:
                analyzerClassName = ANALYZER_CLASS_SUPER;
                break;
            case true:
                analyzerClassName = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";
                break;
            default:
                analyzerClassName = analyzer;
                break;
        }
        try {
            this.dependencyAnalyzer = (DependencyAnalyzer) Class.forName(analyzerClassName).asSubclass(DependencyAnalyzer.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            this.dependencyAnalyzer.addClassPath(new Path(this.task.getProject(), this.config.srcDir.getPath()));
            this.dependencyAnalyzer.addClassPath(this.config.classpath);
        } catch (Exception e) {
            this.dependencyAnalyzer = null;
            this.task.log("Unable to load dependency analyzer: " + analyzerClassName + " - exception: " + e.getMessage(), 1);
        } catch (NoClassDefFoundError e2) {
            this.dependencyAnalyzer = null;
            this.task.log("Unable to load dependency analyzer: " + analyzerClassName + " - dependent class not found: " + e2.getMessage(), 1);
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void configure(EjbJar.Config config) {
        this.config = config;
        createAnalyzer();
        this.classpathLoader = null;
    }

    protected void addFileToJar(JarOutputStream jStream, File inputFile, String logicalFilename) throws BuildException {
        if (!this.addedfiles.contains(logicalFilename)) {
            try {
                InputStream iStream = Files.newInputStream(inputFile.toPath(), new OpenOption[0]);
                ZipEntry zipEntry = new ZipEntry(logicalFilename.replace('\\', '/'));
                jStream.putNextEntry(zipEntry);
                byte[] byteBuffer = new byte[2048];
                int count = 0;
                do {
                    jStream.write(byteBuffer, 0, count);
                    count = iStream.read(byteBuffer, 0, byteBuffer.length);
                } while (count != -1);
                this.addedfiles.add(logicalFilename);
                if (iStream != null) {
                    iStream.close();
                }
            } catch (IOException ioe) {
                log("WARNING: IOException while adding entry " + logicalFilename + " to jarfile from " + inputFile.getPath() + Instruction.argsep + ioe.getClass().getName() + HelpFormatter.DEFAULT_OPT_PREFIX + ioe.getMessage(), 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DescriptorHandler getDescriptorHandler(File srcDir) {
        DescriptorHandler h = new DescriptorHandler(getTask(), srcDir);
        registerKnownDTDs(h);
        Iterator<EjbJar.DTDLocation> it = getConfig().dtdLocations.iterator();
        while (it.hasNext()) {
            EjbJar.DTDLocation dtdLocation = it.next();
            h.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation());
        }
        return h;
    }

    protected void registerKnownDTDs(DescriptorHandler handler) {
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void processDescriptor(String descriptorFileName, SAXParser saxParser) {
        checkConfiguration(descriptorFileName, saxParser);
        try {
            this.handler = getDescriptorHandler(this.config.srcDir);
            Hashtable<String, File> ejbFiles = parseEjbFiles(descriptorFileName, saxParser);
            addSupportClasses(ejbFiles);
            String baseName = getJarBaseName(descriptorFileName);
            String ddPrefix = getVendorDDPrefix(baseName, descriptorFileName);
            File manifestFile = getManifestFile(ddPrefix);
            if (manifestFile != null) {
                ejbFiles.put(MANIFEST, manifestFile);
            }
            ejbFiles.put("META-INF/ejb-jar.xml", new File(this.config.descriptorDir, descriptorFileName));
            addVendorFiles(ejbFiles, ddPrefix);
            checkAndAddDependants(ejbFiles);
            if (this.config.flatDestDir && !baseName.isEmpty()) {
                int startName = baseName.lastIndexOf(File.separator);
                if (startName == -1) {
                    startName = 0;
                }
                int endName = baseName.length();
                baseName = baseName.substring(startName, endName);
            }
            File jarFile = getVendorOutputJarFile(baseName);
            if (needToRebuild(ejbFiles, jarFile)) {
                log("building " + jarFile.getName() + " with " + String.valueOf(ejbFiles.size()) + " files", 2);
                String publicId = getPublicId();
                writeJar(baseName, jarFile, ejbFiles, publicId);
            } else {
                log(jarFile.toString() + " is up to date.", 3);
            }
        } catch (IOException ioe) {
            throw new BuildException("IOException while parsing'" + descriptorFileName + "'.  This probably indicates that the descriptor doesn't exist. Details: " + ioe.getMessage(), ioe);
        } catch (SAXException se) {
            throw new BuildException("SAXException while parsing '" + descriptorFileName + "'. This probably indicates badly-formed XML.  Details: " + se.getMessage(), se);
        }
    }

    protected void checkConfiguration(String descriptorFileName, SAXParser saxParser) throws BuildException {
    }

    protected Hashtable<String, File> parseEjbFiles(String descriptorFileName, SAXParser saxParser) throws IOException, SAXException {
        InputStream descriptorStream = Files.newInputStream(new File(this.config.descriptorDir, descriptorFileName).toPath(), new OpenOption[0]);
        try {
            saxParser.parse(new InputSource(descriptorStream), this.handler);
            Hashtable<String, File> files = this.handler.getFiles();
            if (descriptorStream != null) {
                descriptorStream.close();
            }
            return files;
        } catch (Throwable th) {
            if (descriptorStream != null) {
                try {
                    descriptorStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    protected void addSupportClasses(Hashtable<String, File> ejbFiles) {
        String[] includedFiles;
        Project project = this.task.getProject();
        for (FileSet supportFileSet : this.config.supportFileSets) {
            File supportBaseDir = supportFileSet.getDir(project);
            DirectoryScanner supportScanner = supportFileSet.getDirectoryScanner(project);
            for (String supportFile : supportScanner.getIncludedFiles()) {
                ejbFiles.put(supportFile, new File(supportBaseDir, supportFile));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getJarBaseName(String descriptorFileName) {
        int endBaseName;
        String baseName = "";
        if (EjbJar.NamingScheme.BASEJARNAME.equals(this.config.namingScheme.getValue())) {
            String canonicalDescriptor = descriptorFileName.replace('\\', '/');
            int index = canonicalDescriptor.lastIndexOf(47);
            if (index != -1) {
                baseName = descriptorFileName.substring(0, index + 1);
            }
            baseName = baseName + this.config.baseJarName;
        } else if (EjbJar.NamingScheme.DESCRIPTOR.equals(this.config.namingScheme.getValue())) {
            int lastSeparatorIndex = descriptorFileName.lastIndexOf(File.separator);
            if (lastSeparatorIndex != -1) {
                endBaseName = descriptorFileName.indexOf(this.config.baseNameTerminator, lastSeparatorIndex);
            } else {
                endBaseName = descriptorFileName.indexOf(this.config.baseNameTerminator);
            }
            if (endBaseName != -1) {
                baseName = descriptorFileName.substring(0, endBaseName);
            } else {
                throw new BuildException("Unable to determine jar name from descriptor \"%s\"", descriptorFileName);
            }
        } else if ("directory".equals(this.config.namingScheme.getValue())) {
            File descriptorFile = new File(this.config.descriptorDir, descriptorFileName);
            String path = descriptorFile.getAbsolutePath();
            int lastSeparatorIndex2 = path.lastIndexOf(File.separator);
            if (lastSeparatorIndex2 == -1) {
                throw new BuildException("Unable to determine directory name holding descriptor");
            }
            String dirName = path.substring(0, lastSeparatorIndex2);
            int dirSeparatorIndex = dirName.lastIndexOf(File.separator);
            if (dirSeparatorIndex != -1) {
                dirName = dirName.substring(dirSeparatorIndex + 1);
            }
            baseName = dirName;
        } else if (EjbJar.NamingScheme.EJB_NAME.equals(this.config.namingScheme.getValue())) {
            baseName = this.handler.getEjbName();
        }
        return baseName;
    }

    public String getVendorDDPrefix(String baseName, String descriptorFileName) {
        String ddPrefix = null;
        if (this.config.namingScheme.getValue().equals(EjbJar.NamingScheme.DESCRIPTOR)) {
            ddPrefix = baseName + this.config.baseNameTerminator;
        } else if (this.config.namingScheme.getValue().equals(EjbJar.NamingScheme.BASEJARNAME) || this.config.namingScheme.getValue().equals(EjbJar.NamingScheme.EJB_NAME) || this.config.namingScheme.getValue().equals("directory")) {
            String canonicalDescriptor = descriptorFileName.replace('\\', '/');
            int index = canonicalDescriptor.lastIndexOf(47);
            if (index == -1) {
                ddPrefix = "";
            } else {
                ddPrefix = descriptorFileName.substring(0, index + 1);
            }
        }
        return ddPrefix;
    }

    protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public File getVendorOutputJarFile(String baseName) {
        return new File(this.destDir, baseName + this.genericJarSuffix);
    }

    protected boolean needToRebuild(Hashtable<String, File> ejbFiles, File jarFile) {
        if (jarFile.exists()) {
            long lastBuild = jarFile.lastModified();
            for (File currentFile : ejbFiles.values()) {
                if (lastBuild < currentFile.lastModified()) {
                    log("Build needed because " + currentFile.getPath() + " is out of date", 3);
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    protected String getPublicId() {
        return this.handler.getPublicId();
    }

    protected File getManifestFile(String prefix) {
        File manifestFile = new File(getConfig().descriptorDir, prefix + "manifest.mf");
        if (manifestFile.exists()) {
            return manifestFile;
        }
        if (this.config.manifest != null) {
            return this.config.manifest;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeJar(String baseName, File jarfile, Hashtable<String, File> files, String publicId) throws BuildException {
        InputStream in;
        if (this.addedfiles == null) {
            this.addedfiles = new HashSet();
        } else {
            this.addedfiles.clear();
        }
        try {
            if (jarfile.exists()) {
                jarfile.delete();
            }
            jarfile.getParentFile().mkdirs();
            jarfile.createNewFile();
            try {
                File manifestFile = files.get(MANIFEST);
                if (manifestFile != null && manifestFile.exists()) {
                    in = Files.newInputStream(manifestFile.toPath(), new OpenOption[0]);
                } else {
                    in = getClass().getResourceAsStream("/org/apache/tools/ant/defaultManifest.mf");
                    if (in == null) {
                        throw new BuildException("Could not find default manifest: %s", "/org/apache/tools/ant/defaultManifest.mf");
                    }
                }
                Manifest manifest = new Manifest(in);
                if (in != null) {
                    in.close();
                }
                JarOutputStream jarStream = new JarOutputStream(Files.newOutputStream(jarfile.toPath(), new OpenOption[0]), manifest);
                jarStream.setMethod(8);
                for (Map.Entry<String, File> entryFiles : files.entrySet()) {
                    String entryName = entryFiles.getKey();
                    if (!entryName.equals(MANIFEST)) {
                        File entryFile = entryFiles.getValue();
                        log("adding file '" + entryName + "'", 3);
                        addFileToJar(jarStream, entryFile, entryName);
                        InnerClassFilenameFilter flt = new InnerClassFilenameFilter(entryFile.getName());
                        File entryDir = entryFile.getParentFile();
                        String[] innerfiles = entryDir.list(flt);
                        if (innerfiles != null) {
                            for (String innerfile : innerfiles) {
                                int entryIndex = entryName.lastIndexOf(entryFile.getName()) - 1;
                                if (entryIndex < 0) {
                                    entryName = innerfile;
                                } else {
                                    entryName = entryName.substring(0, entryIndex) + File.separatorChar + innerfile;
                                }
                                entryFile = new File(this.config.srcDir, entryName);
                                log("adding innerclass file '" + entryName + "'", 3);
                                addFileToJar(jarStream, entryFile, entryName);
                            }
                        }
                    }
                }
                jarStream.close();
            } catch (IOException e) {
                throw new BuildException("Unable to read manifest", e, getLocation());
            }
        } catch (IOException ioe) {
            String msg = "IOException while processing ejb-jar file '" + jarfile.toString() + "'. Details: " + ioe.getMessage();
            throw new BuildException(msg, ioe);
        }
    }

    protected void checkAndAddDependants(Hashtable<String, File> checkEntries) throws BuildException {
        if (this.dependencyAnalyzer == null) {
            return;
        }
        this.dependencyAnalyzer.reset();
        for (String entryName : checkEntries.keySet()) {
            if (entryName.endsWith(".class")) {
                String className = entryName.substring(0, entryName.length() - ".class".length());
                this.dependencyAnalyzer.addRootClass(className.replace(File.separatorChar, '/').replace('/', '.'));
            }
        }
        Iterator it = Collections.list(this.dependencyAnalyzer.getClassDependencies()).iterator();
        while (it.hasNext()) {
            String classname = (String) it.next();
            String location = classname.replace('.', File.separatorChar) + ".class";
            File classFile = new File(this.config.srcDir, location);
            if (classFile.exists()) {
                checkEntries.put(location, classFile);
                log("dependent class: " + classname + " - " + classFile, 3);
            }
        }
    }

    protected ClassLoader getClassLoaderForBuild() {
        if (this.classpathLoader != null) {
            return this.classpathLoader;
        }
        Path combinedClasspath = getCombinedClasspath();
        if (combinedClasspath == null) {
            this.classpathLoader = getClass().getClassLoader();
        } else {
            this.classpathLoader = getTask().getProject().createClassLoader(combinedClasspath);
        }
        return this.classpathLoader;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void validateConfigured() throws BuildException {
        if (this.destDir == null || !this.destDir.isDirectory()) {
            throw new BuildException("A valid destination directory must be specified using the \"destdir\" attribute.", getLocation());
        }
    }
}
