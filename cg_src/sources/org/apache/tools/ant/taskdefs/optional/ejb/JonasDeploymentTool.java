package org.apache.tools.ant.taskdefs.optional.ejb;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import javax.xml.parsers.SAXParser;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.taskdefs.optional.ejb.EjbJar;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/JonasDeploymentTool.class */
public class JonasDeploymentTool extends GenericDeploymentTool {
    protected static final String EJB_JAR_1_1_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
    protected static final String EJB_JAR_2_0_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN";
    protected static final String JONAS_EJB_JAR_2_4_PUBLIC_ID = "-//ObjectWeb//DTD JOnAS 2.4//EN";
    protected static final String JONAS_EJB_JAR_2_5_PUBLIC_ID = "-//ObjectWeb//DTD JOnAS 2.5//EN";
    protected static final String RMI_ORB = "RMI";
    protected static final String JEREMIE_ORB = "JEREMIE";
    protected static final String DAVID_ORB = "DAVID";
    protected static final String EJB_JAR_1_1_DTD = "ejb-jar_1_1.dtd";
    protected static final String EJB_JAR_2_0_DTD = "ejb-jar_2_0.dtd";
    protected static final String JONAS_EJB_JAR_2_4_DTD = "jonas-ejb-jar_2_4.dtd";
    protected static final String JONAS_EJB_JAR_2_5_DTD = "jonas-ejb-jar_2_5.dtd";
    protected static final String JONAS_DD = "jonas-ejb-jar.xml";
    protected static final String GENIC_CLASS = "org.objectweb.jonas_ejb.genic.GenIC";
    protected static final String OLD_GENIC_CLASS_1 = "org.objectweb.jonas_ejb.tools.GenWholeIC";
    protected static final String OLD_GENIC_CLASS_2 = "org.objectweb.jonas_ejb.tools.GenIC";
    private String descriptorName;
    private String jonasDescriptorName;
    private File outputdir;
    private String javac;
    private String javacopts;
    private String rmicopts;
    private String additionalargs;
    private File jonasroot;
    private String orb;
    private boolean keepgenerated = false;
    private boolean nocompil = false;
    private boolean novalidation = false;
    private boolean secpropag = false;
    private boolean verbose = false;
    private boolean keepgeneric = false;
    private String suffix = ".jar";
    private boolean nogenic = false;

    public void setKeepgenerated(boolean aBoolean) {
        this.keepgenerated = aBoolean;
    }

    public void setAdditionalargs(String aString) {
        this.additionalargs = aString;
    }

    public void setNocompil(boolean aBoolean) {
        this.nocompil = aBoolean;
    }

    public void setNovalidation(boolean aBoolean) {
        this.novalidation = aBoolean;
    }

    public void setJavac(String aString) {
        this.javac = aString;
    }

    public void setJavacopts(String aString) {
        this.javacopts = aString;
    }

    public void setRmicopts(String aString) {
        this.rmicopts = aString;
    }

    public void setSecpropag(boolean aBoolean) {
        this.secpropag = aBoolean;
    }

    public void setVerbose(boolean aBoolean) {
        this.verbose = aBoolean;
    }

    public void setJonasroot(File aFile) {
        this.jonasroot = aFile;
    }

    public void setKeepgeneric(boolean aBoolean) {
        this.keepgeneric = aBoolean;
    }

    public void setJarsuffix(String aString) {
        this.suffix = aString;
    }

    public void setOrb(String aString) {
        this.orb = aString;
    }

    public void setNogenic(boolean aBoolean) {
        this.nogenic = aBoolean;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool, org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void processDescriptor(String aDescriptorName, SAXParser saxParser) {
        this.descriptorName = aDescriptorName;
        log("JOnAS Deployment Tool processing: " + this.descriptorName, 3);
        super.processDescriptor(this.descriptorName, saxParser);
        if (this.outputdir != null) {
            log("Deleting temp output directory '" + this.outputdir + "'.", 3);
            deleteAllFiles(this.outputdir);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public void writeJar(String baseName, File jarfile, Hashtable<String, File> ejbFiles, String publicId) throws BuildException {
        File genericJarFile = super.getVendorOutputJarFile(baseName);
        super.writeJar(baseName, genericJarFile, ejbFiles, publicId);
        addGenICGeneratedFiles(genericJarFile, ejbFiles);
        super.writeJar(baseName, getVendorOutputJarFile(baseName), ejbFiles, publicId);
        if (!this.keepgeneric) {
            log("Deleting generic JAR " + genericJarFile.toString(), 3);
            genericJarFile.delete();
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
        this.jonasDescriptorName = getJonasDescriptorName();
        File jonasDD = new File(getConfig().descriptorDir, this.jonasDescriptorName);
        if (jonasDD.exists()) {
            ejbFiles.put("META-INF/jonas-ejb-jar.xml", jonasDD);
        } else {
            log("Unable to locate the JOnAS deployment descriptor. It was expected to be in: " + jonasDD.getPath() + ".", 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public File getVendorOutputJarFile(String baseName) {
        return new File(getDestDir(), baseName + this.suffix);
    }

    private String getJonasDescriptorName() {
        String path;
        String fileName;
        String jonasDN;
        boolean jonasConvention = false;
        int startOfFileName = this.descriptorName.lastIndexOf(File.separatorChar);
        if (startOfFileName != -1) {
            path = this.descriptorName.substring(0, startOfFileName + 1);
            fileName = this.descriptorName.substring(startOfFileName + 1);
        } else {
            path = "";
            fileName = this.descriptorName;
        }
        if (fileName.startsWith("ejb-jar.xml")) {
            return path + JONAS_DD;
        }
        int endOfBaseName = this.descriptorName.indexOf(getConfig().baseNameTerminator, startOfFileName);
        if (endOfBaseName < 0) {
            endOfBaseName = this.descriptorName.lastIndexOf(46) - 1;
            if (endOfBaseName < 0) {
                endOfBaseName = this.descriptorName.length() - 1;
            }
            jonasConvention = true;
        }
        String baseName = this.descriptorName.substring(startOfFileName + 1, endOfBaseName + 1);
        String remainder = this.descriptorName.substring(endOfBaseName + 1);
        if (jonasConvention) {
            jonasDN = path + "jonas-" + baseName + ".xml";
        } else {
            jonasDN = path + baseName + "jonas-" + remainder;
        }
        log("Standard EJB descriptor name: " + this.descriptorName, 3);
        log("JOnAS-specific descriptor name: " + jonasDN, 3);
        return jonasDN;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public String getJarBaseName(String descriptorFileName) {
        int endOfBaseName;
        String baseName = null;
        if (getConfig().namingScheme.getValue().equals(EjbJar.NamingScheme.DESCRIPTOR) && !descriptorFileName.contains(getConfig().baseNameTerminator)) {
            String aCanonicalDescriptor = descriptorFileName.replace('\\', '/');
            int lastSeparatorIndex = aCanonicalDescriptor.lastIndexOf(47);
            if (lastSeparatorIndex != -1) {
                endOfBaseName = descriptorFileName.indexOf(".xml", lastSeparatorIndex);
            } else {
                endOfBaseName = descriptorFileName.indexOf(".xml");
            }
            if (endOfBaseName != -1) {
                baseName = descriptorFileName.substring(0, endOfBaseName);
            }
        }
        if (baseName == null) {
            baseName = super.getJarBaseName(descriptorFileName);
        }
        log("JAR base name: " + baseName, 3);
        return baseName;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void registerKnownDTDs(DescriptorHandler handler) {
        handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", this.jonasroot + File.separator + EncodingConstants.XML_NAMESPACE_PREFIX + File.separator + EJB_JAR_1_1_DTD);
        handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", this.jonasroot + File.separator + EncodingConstants.XML_NAMESPACE_PREFIX + File.separator + EJB_JAR_2_0_DTD);
        handler.registerDTD(JONAS_EJB_JAR_2_4_PUBLIC_ID, this.jonasroot + File.separator + EncodingConstants.XML_NAMESPACE_PREFIX + File.separator + JONAS_EJB_JAR_2_4_DTD);
        handler.registerDTD(JONAS_EJB_JAR_2_5_PUBLIC_ID, this.jonasroot + File.separator + EncodingConstants.XML_NAMESPACE_PREFIX + File.separator + JONAS_EJB_JAR_2_5_DTD);
    }

    private void addGenICGeneratedFiles(File genericJarFile, Hashtable<String, File> ejbFiles) {
        if (this.nogenic) {
            return;
        }
        Java genicTask = new Java(getTask());
        genicTask.setTaskName("genic");
        genicTask.setFork(true);
        genicTask.createJvmarg().setValue("-Dinstall.root=" + this.jonasroot);
        String jonasConfigDir = this.jonasroot + File.separator + "config";
        File javaPolicyFile = new File(jonasConfigDir, "java.policy");
        if (javaPolicyFile.exists()) {
            genicTask.createJvmarg().setValue("-Djava.security.policy=" + javaPolicyFile.toString());
        }
        try {
            this.outputdir = createTempDir();
            log("Using temporary output directory: " + this.outputdir, 3);
            genicTask.createArg().setValue("-d");
            genicTask.createArg().setFile(this.outputdir);
            for (String key : ejbFiles.keySet()) {
                File f = new File(this.outputdir + File.separator + key);
                f.getParentFile().mkdirs();
            }
            log("Worked around a bug of GenIC 2.5.", 3);
            Path classpath = getCombinedClasspath();
            if (classpath == null) {
                classpath = new Path(getTask().getProject());
            }
            classpath.append(new Path(classpath.getProject(), jonasConfigDir));
            classpath.append(new Path(classpath.getProject(), this.outputdir.toString()));
            if (this.orb != null) {
                String orbJar = this.jonasroot + File.separator + Launcher.ANT_PRIVATELIB + File.separator + this.orb + "_jonas.jar";
                classpath.append(new Path(classpath.getProject(), orbJar));
            }
            log("Using classpath: " + classpath.toString(), 3);
            genicTask.setClasspath(classpath);
            String genicClass = getGenicClassName(classpath);
            if (genicClass == null) {
                log("Cannot find GenIC class in classpath.", 0);
                throw new BuildException("GenIC class not found, please check the classpath.");
            }
            log("Using '" + genicClass + "' GenIC class.", 3);
            genicTask.setClassname(genicClass);
            if (this.keepgenerated) {
                genicTask.createArg().setValue("-keepgenerated");
            }
            if (this.nocompil) {
                genicTask.createArg().setValue("-nocompil");
            }
            if (this.novalidation) {
                genicTask.createArg().setValue("-novalidation");
            }
            if (this.javac != null) {
                genicTask.createArg().setValue("-javac");
                genicTask.createArg().setLine(this.javac);
            }
            if (this.javacopts != null && !this.javacopts.isEmpty()) {
                genicTask.createArg().setValue("-javacopts");
                genicTask.createArg().setLine(this.javacopts);
            }
            if (this.rmicopts != null && !this.rmicopts.isEmpty()) {
                genicTask.createArg().setValue("-rmicopts");
                genicTask.createArg().setLine(this.rmicopts);
            }
            if (this.secpropag) {
                genicTask.createArg().setValue("-secpropag");
            }
            if (this.verbose) {
                genicTask.createArg().setValue(SOSCmd.FLAG_VERBOSE);
            }
            if (this.additionalargs != null) {
                genicTask.createArg().setValue(this.additionalargs);
            }
            genicTask.createArg().setValue("-noaddinjar");
            genicTask.createArg().setValue(genericJarFile.getPath());
            log("Calling " + genicClass + " for " + getConfig().descriptorDir + File.separator + this.descriptorName + ".", 3);
            if (genicTask.executeJava() != 0) {
                log("Deleting temp output directory '" + this.outputdir + "'.", 3);
                deleteAllFiles(this.outputdir);
                if (!this.keepgeneric) {
                    log("Deleting generic JAR " + genericJarFile.toString(), 3);
                    genericJarFile.delete();
                }
                throw new BuildException("GenIC reported an error.");
            }
            addAllFiles(this.outputdir, "", ejbFiles);
        } catch (IOException aIOException) {
            String msg = "Cannot create temp dir: " + aIOException.getMessage();
            throw new BuildException(msg, aIOException);
        }
    }

    String getGenicClassName(Path classpath) {
        log("Looking for GenIC class in classpath: " + classpath.toString(), 3);
        AntClassLoader cl = classpath.getProject().createClassLoader(classpath);
        try {
            try {
                cl.loadClass(GENIC_CLASS);
                log("Found GenIC class 'org.objectweb.jonas_ejb.genic.GenIC' in classpath.", 3);
                if (cl != null) {
                    cl.close();
                }
                return GENIC_CLASS;
            } catch (ClassNotFoundException e) {
                log("GenIC class 'org.objectweb.jonas_ejb.genic.GenIC' not found in classpath.", 3);
                try {
                    cl.loadClass(OLD_GENIC_CLASS_1);
                    log("Found GenIC class 'org.objectweb.jonas_ejb.tools.GenWholeIC' in classpath.", 3);
                    if (cl != null) {
                        cl.close();
                    }
                    return OLD_GENIC_CLASS_1;
                } catch (ClassNotFoundException e2) {
                    log("GenIC class 'org.objectweb.jonas_ejb.tools.GenWholeIC' not found in classpath.", 3);
                    try {
                        cl.loadClass(OLD_GENIC_CLASS_2);
                        log("Found GenIC class 'org.objectweb.jonas_ejb.tools.GenIC' in classpath.", 3);
                        if (cl != null) {
                            cl.close();
                        }
                        return OLD_GENIC_CLASS_2;
                    } catch (ClassNotFoundException e3) {
                        log("GenIC class 'org.objectweb.jonas_ejb.tools.GenIC' not found in classpath.", 3);
                        if (cl != null) {
                            cl.close();
                            return null;
                        }
                        return null;
                    }
                }
            }
        } catch (Throwable th) {
            if (cl != null) {
                try {
                    cl.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void checkConfiguration(String descriptorFileName, SAXParser saxParser) throws BuildException {
        if (this.jonasroot == null) {
            throw new BuildException("The jonasroot attribute is not set.");
        }
        if (!this.jonasroot.isDirectory()) {
            throw new BuildException("The jonasroot attribute '%s' is not a valid directory.", this.jonasroot);
        }
        List<String> validOrbs = Arrays.asList(RMI_ORB, JEREMIE_ORB, DAVID_ORB);
        if (this.orb != null && !validOrbs.contains(this.orb)) {
            throw new BuildException("The orb attribute '%s' is not valid (must be one of %s.", this.orb, validOrbs);
        }
        if (this.additionalargs != null && this.additionalargs.isEmpty()) {
            throw new BuildException("Empty additionalargs attribute.");
        }
        if (this.javac != null && this.javac.isEmpty()) {
            throw new BuildException("Empty javac attribute.");
        }
    }

    private File createTempDir() throws IOException {
        return Files.createTempDirectory("genic", new FileAttribute[0]).toFile();
    }

    private void deleteAllFiles(File aFile) {
        File[] listFiles;
        if (aFile.isDirectory()) {
            for (File child : aFile.listFiles()) {
                deleteAllFiles(child);
            }
        }
        aFile.delete();
    }

    private void addAllFiles(File file, String rootDir, Hashtable<String, File> hashtable) {
        File[] listFiles;
        String str;
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (rootDir.isEmpty()) {
                    str = child.getName();
                } else {
                    str = rootDir + File.separator + child.getName();
                }
                String newRootDir = str;
                addAllFiles(child, newRootDir, hashtable);
            }
            return;
        }
        hashtable.put(rootDir, file);
    }
}
