package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/WebsphereDeploymentTool.class */
public class WebsphereDeploymentTool extends GenericDeploymentTool {
    public static final String PUBLICID_EJB11 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
    public static final String PUBLICID_EJB20 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN";
    protected static final String SCHEMA_DIR = "Schema/";
    protected static final String WAS_EXT = "ibm-ejb-jar-ext.xmi";
    protected static final String WAS_BND = "ibm-ejb-jar-bnd.xmi";
    protected static final String WAS_CMP_MAP = "Map.mapxmi";
    protected static final String WAS_CMP_SCHEMA = "Schema.dbxmi";
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private String ejb11DTD;
    private String dbVendor;
    private String dbName;
    private String dbSchema;
    private boolean codegen;
    private boolean novalidate;
    private boolean nowarn;
    private boolean noinform;
    private boolean trace;
    private String rmicOptions;
    private boolean use35MappingRules;
    private File websphereHome;
    private String jarSuffix = ".jar";
    private boolean keepGeneric = false;
    private boolean alwaysRebuild = true;
    private boolean ejbdeploy = true;
    private boolean newCMP = false;
    private Path wasClasspath = null;
    private boolean quiet = true;
    private String tempdir = "_ejbdeploy_temp";

    public Path createWASClasspath() {
        if (this.wasClasspath == null) {
            this.wasClasspath = new Path(getTask().getProject());
        }
        return this.wasClasspath.createPath();
    }

    public void setWASClasspath(Path wasClasspath) {
        this.wasClasspath = wasClasspath;
    }

    public void setDbvendor(String dbvendor) {
        this.dbVendor = dbvendor;
    }

    public void setDbname(String dbName) {
        this.dbName = dbName;
    }

    public void setDbschema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public void setCodegen(boolean codegen) {
        this.codegen = codegen;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public void setNovalidate(boolean novalidate) {
        this.novalidate = novalidate;
    }

    public void setNowarn(boolean nowarn) {
        this.nowarn = nowarn;
    }

    public void setNoinform(boolean noinform) {
        this.noinform = noinform;
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public void setRmicoptions(String options) {
        this.rmicOptions = options;
    }

    public void setUse35(boolean attr) {
        this.use35MappingRules = attr;
    }

    public void setRebuild(boolean rebuild) {
        this.alwaysRebuild = rebuild;
    }

    public void setSuffix(String inString) {
        this.jarSuffix = inString;
    }

    public void setKeepgeneric(boolean inValue) {
        this.keepGeneric = inValue;
    }

    public void setEjbdeploy(boolean ejbdeploy) {
        this.ejbdeploy = ejbdeploy;
    }

    public void setEJBdtd(String inString) {
        this.ejb11DTD = inString;
    }

    public void setOldCMP(boolean oldCMP) {
        this.newCMP = !oldCMP;
    }

    public void setNewCMP(boolean newCMP) {
        this.newCMP = newCMP;
    }

    public void setTempdir(String tempdir) {
        this.tempdir = tempdir;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public DescriptorHandler getDescriptorHandler(File srcDir) {
        DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir);
        handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", this.ejb11DTD);
        getConfig().dtdLocations.forEach(l -> {
            handler.registerDTD(l.getPublicId(), l.getLocation());
        });
        return handler;
    }

    protected DescriptorHandler getWebsphereDescriptorHandler(File srcDir) {
        DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir) { // from class: org.apache.tools.ant.taskdefs.optional.ejb.WebsphereDeploymentTool.1
            @Override // org.apache.tools.ant.taskdefs.optional.ejb.DescriptorHandler
            protected void processElement() {
            }
        };
        getConfig().dtdLocations.forEach(l -> {
            handler.registerDTD(l.getPublicId(), l.getLocation());
        });
        return handler;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void addVendorFiles(Hashtable<String, File> ejbFiles, String baseName) {
        String ddPrefix = usingBaseJarName() ? "" : baseName;
        String dbPrefix = this.dbVendor == null ? "" : this.dbVendor + HelpFormatter.DEFAULT_OPT_PREFIX;
        File websphereEXT = new File(getConfig().descriptorDir, ddPrefix + WAS_EXT);
        if (websphereEXT.exists()) {
            ejbFiles.put("META-INF/ibm-ejb-jar-ext.xmi", websphereEXT);
        } else {
            log("Unable to locate websphere extensions. It was expected to be in " + websphereEXT.getPath(), 3);
        }
        File websphereBND = new File(getConfig().descriptorDir, ddPrefix + WAS_BND);
        if (websphereBND.exists()) {
            ejbFiles.put("META-INF/ibm-ejb-jar-bnd.xmi", websphereBND);
        } else {
            log("Unable to locate websphere bindings. It was expected to be in " + websphereBND.getPath(), 3);
        }
        if (!this.newCMP) {
            log("The old method for locating CMP files has been DEPRECATED.", 3);
            log("Please adjust your websphere descriptor and set newCMP=\"true\" to use the new CMP descriptor inclusion mechanism. ", 3);
            return;
        }
        try {
            File websphereMAP = new File(getConfig().descriptorDir, ddPrefix + dbPrefix + WAS_CMP_MAP);
            if (websphereMAP.exists()) {
                ejbFiles.put("META-INF/Map.mapxmi", websphereMAP);
            } else {
                log("Unable to locate the websphere Map: " + websphereMAP.getPath(), 3);
            }
            File websphereSchema = new File(getConfig().descriptorDir, ddPrefix + dbPrefix + WAS_CMP_SCHEMA);
            if (websphereSchema.exists()) {
                ejbFiles.put("META-INF/Schema/Schema.dbxmi", websphereSchema);
            } else {
                log("Unable to locate the websphere Schema: " + websphereSchema.getPath(), 3);
            }
        } catch (Exception e) {
            throw new BuildException("Exception while adding Vendor specific files: " + e.toString(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public File getVendorOutputJarFile(String baseName) {
        return new File(getDestDir(), baseName + this.jarSuffix);
    }

    protected String getOptions() {
        StringBuilder options = new StringBuilder();
        if (this.dbVendor != null) {
            options.append(" -dbvendor ").append(this.dbVendor);
        }
        if (this.dbName != null) {
            options.append(" -dbname \"").append(this.dbName).append("\"");
        }
        if (this.dbSchema != null) {
            options.append(" -dbschema \"").append(this.dbSchema).append("\"");
        }
        if (this.codegen) {
            options.append(" -codegen");
        }
        if (this.quiet) {
            options.append(" -quiet");
        }
        if (this.novalidate) {
            options.append(" -novalidate");
        }
        if (this.nowarn) {
            options.append(" -nowarn");
        }
        if (this.noinform) {
            options.append(" -noinform");
        }
        if (this.trace) {
            options.append(" -trace");
        }
        if (this.use35MappingRules) {
            options.append(" -35");
        }
        if (this.rmicOptions != null) {
            options.append(" -rmic \"").append(this.rmicOptions).append("\"");
        }
        return options.toString();
    }

    private void buildWebsphereJar(File sourceJar, File destJar) {
        try {
            if (this.ejbdeploy) {
                Java javaTask = new Java(getTask());
                javaTask.createJvmarg().setValue("-Xms64m");
                javaTask.createJvmarg().setValue("-Xmx128m");
                Environment.Variable var = new Environment.Variable();
                var.setKey("websphere.lib.dir");
                File libdir = new File(this.websphereHome, Launcher.ANT_PRIVATELIB);
                var.setValue(libdir.getAbsolutePath());
                javaTask.addSysproperty(var);
                javaTask.setDir(this.websphereHome);
                javaTask.setTaskName("ejbdeploy");
                javaTask.setClassname("com.ibm.etools.ejbdeploy.EJBDeploy");
                javaTask.createArg().setValue(sourceJar.getPath());
                javaTask.createArg().setValue(this.tempdir);
                javaTask.createArg().setValue(destJar.getPath());
                javaTask.createArg().setLine(getOptions());
                if (getCombinedClasspath() != null && !getCombinedClasspath().toString().isEmpty()) {
                    javaTask.createArg().setValue("-cp");
                    javaTask.createArg().setValue(getCombinedClasspath().toString());
                }
                Path classpath = this.wasClasspath;
                if (classpath == null) {
                    classpath = getCombinedClasspath();
                }
                javaTask.setFork(true);
                if (classpath != null) {
                    javaTask.setClasspath(classpath);
                }
                log("Calling websphere.ejbdeploy for " + sourceJar.toString(), 3);
                javaTask.execute();
            }
        } catch (Exception e) {
            throw new BuildException("Exception while calling ejbdeploy. Details: " + e.toString(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public void writeJar(String baseName, File jarFile, Hashtable<String, File> files, String publicId) throws BuildException {
        if (this.ejbdeploy) {
            File genericJarFile = super.getVendorOutputJarFile(baseName);
            super.writeJar(baseName, genericJarFile, files, publicId);
            if (this.alwaysRebuild || isRebuildRequired(genericJarFile, jarFile)) {
                buildWebsphereJar(genericJarFile, jarFile);
            }
            if (!this.keepGeneric) {
                log("deleting generic jar " + genericJarFile.toString(), 3);
                genericJarFile.delete();
                return;
            }
            return;
        }
        super.writeJar(baseName, jarFile, files, publicId);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool, org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void validateConfigured() throws BuildException {
        super.validateConfigured();
        if (this.ejbdeploy) {
            String home = getTask().getProject().getProperty("websphere.home");
            if (home == null) {
                throw new BuildException("The 'websphere.home' property must be set when 'ejbdeploy=true'");
            }
            this.websphereHome = getTask().getProject().resolveFile(home);
        }
    }

    protected boolean isRebuildRequired(File genericJarFile, File websphereJarFile) {
        ClassLoader genericLoader;
        InputStream is;
        boolean rebuild = false;
        JarFile genericJar = null;
        JarFile wasJar = null;
        File newwasJarFile = null;
        JarOutputStream newJarStream = null;
        ClassLoader genericLoader2 = null;
        try {
            try {
                log("Checking if websphere Jar needs to be rebuilt for jar " + websphereJarFile.getName(), 3);
                if (genericJarFile.exists() && genericJarFile.isFile() && websphereJarFile.exists() && websphereJarFile.isFile()) {
                    genericJar = new JarFile(genericJarFile);
                    wasJar = new JarFile(websphereJarFile);
                    Map<String, JarEntry> genericEntries = (Map) genericJar.stream().collect(Collectors.toMap(je -> {
                        return je.getName().replace('\\', '/');
                    }, je2 -> {
                        return je2;
                    }, a, b -> {
                        return b;
                    }));
                    Map<String, JarEntry> wasEntries = (Map) wasJar.stream().collect(Collectors.toMap((v0) -> {
                        return v0.getName();
                    }, je3 -> {
                        return je3;
                    }, a2, b2 -> {
                        return b2;
                    }));
                    ClassLoader genericLoader3 = getClassLoaderFromJar(genericJarFile);
                    Map<String, JarEntry> replaceEntries = new HashMap<>();
                    Iterator<String> it = genericEntries.keySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        String filepath = it.next();
                        if (!wasEntries.containsKey(filepath)) {
                            log("File " + filepath + " not present in websphere jar", 3);
                            rebuild = true;
                            break;
                        }
                        JarEntry genericEntry = genericEntries.get(filepath);
                        JarEntry wasEntry = wasEntries.get(filepath);
                        if (genericEntry.getCrc() != wasEntry.getCrc() || genericEntry.getSize() != wasEntry.getSize()) {
                            if (genericEntry.getName().endsWith(".class")) {
                                String classname = genericEntry.getName().replace(File.separatorChar, '.');
                                Class<?> genclass = genericLoader3.loadClass(classname.substring(0, classname.lastIndexOf(".class")));
                                if (genclass.isInterface()) {
                                    log("Interface " + genclass.getName() + " has changed", 3);
                                    rebuild = true;
                                    break;
                                }
                                replaceEntries.put(filepath, genericEntry);
                            } else if (!genericEntry.getName().equals("META-INF/MANIFEST.MF")) {
                                log("Non class file " + genericEntry.getName() + " has changed", 3);
                                rebuild = true;
                            }
                        }
                    }
                    if (!rebuild) {
                        log("No rebuild needed - updating jar", 3);
                        newwasJarFile = new File(websphereJarFile.getAbsolutePath() + ".temp");
                        if (newwasJarFile.exists()) {
                            newwasJarFile.delete();
                        }
                        newJarStream = new JarOutputStream(Files.newOutputStream(newwasJarFile.toPath(), new OpenOption[0]));
                        newJarStream.setLevel(0);
                        for (JarEntry je4 : wasEntries.values()) {
                            if (je4.getCompressedSize() == -1 || je4.getCompressedSize() == je4.getSize()) {
                                newJarStream.setLevel(0);
                            } else {
                                newJarStream.setLevel(9);
                            }
                            if (replaceEntries.containsKey(je4.getName())) {
                                log("Updating Bean class from generic Jar " + je4.getName(), 3);
                                je4 = replaceEntries.get(je4.getName());
                                is = genericJar.getInputStream(je4);
                            } else {
                                is = wasJar.getInputStream(je4);
                            }
                            newJarStream.putNextEntry(new JarEntry(je4.getName()));
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int bytesRead = is.read(buffer);
                                if (bytesRead != -1) {
                                    newJarStream.write(buffer, 0, bytesRead);
                                }
                            }
                            is.close();
                        }
                    } else {
                        log("websphere Jar rebuild needed due to changed interface or XML", 3);
                    }
                    genericLoader = genericLoader3;
                } else {
                    rebuild = true;
                    genericLoader = genericLoader2;
                }
                FileUtils.close(genericJar);
                FileUtils.close(wasJar);
                FileUtils.close((OutputStream) newJarStream);
                if (newJarStream != null) {
                    try {
                        FILE_UTILS.rename(newwasJarFile, websphereJarFile);
                    } catch (IOException renameException) {
                        log(renameException.getMessage(), 1);
                        rebuild = true;
                    }
                }
                if (genericLoader instanceof AntClassLoader) {
                    AntClassLoader loader = (AntClassLoader) genericLoader;
                    loader.cleanup();
                }
                return rebuild;
            } catch (IOException ioe) {
                throw new BuildException("IOException while processing ejb-jar file . Details: " + ioe.getMessage(), ioe);
            } catch (ClassNotFoundException cnfe) {
                throw new BuildException("ClassNotFoundException while processing ejb-jar file. Details: " + cnfe.getMessage(), cnfe);
            }
        } catch (Throwable th) {
            FileUtils.close((AutoCloseable) null);
            FileUtils.close((AutoCloseable) null);
            FileUtils.close((OutputStream) null);
            if (0 != 0) {
                try {
                    FILE_UTILS.rename(null, websphereJarFile);
                } catch (IOException renameException2) {
                    log(renameException2.getMessage(), 1);
                }
            }
            if (genericLoader2 instanceof AntClassLoader) {
                AntClassLoader loader2 = null;
                loader2.cleanup();
            }
            throw th;
        }
    }

    protected ClassLoader getClassLoaderFromJar(File classjar) throws IOException {
        Path lookupPath = new Path(getTask().getProject());
        lookupPath.setLocation(classjar);
        Path classpath = getCombinedClasspath();
        if (classpath != null) {
            lookupPath.append(classpath);
        }
        return getTask().getProject().createClassLoader(lookupPath);
    }
}
