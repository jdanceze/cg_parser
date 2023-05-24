package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import javax.xml.parsers.SAXParser;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.ejb.EjbJar;
import org.apache.tools.ant.taskdefs.optional.ejb.IPlanetEjbc;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/IPlanetDeploymentTool.class */
public class IPlanetDeploymentTool extends GenericDeploymentTool {
    private File iashome;
    private String jarSuffix = ".jar";
    private boolean keepgenerated = false;
    private boolean debug = false;
    private String descriptorName;
    private String iasDescriptorName;
    private String displayName;
    private static final String IAS_DD = "ias-ejb-jar.xml";

    public void setIashome(File iashome) {
        this.iashome = iashome;
    }

    public void setKeepgenerated(boolean keepgenerated) {
        this.keepgenerated = keepgenerated;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setSuffix(String jarSuffix) {
        this.jarSuffix = jarSuffix;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public void setGenericJarSuffix(String inString) {
        log("Since a generic JAR file is not created during processing, the iPlanet Deployment Tool does not support the \"genericjarsuffix\" attribute.  It will be ignored.", 1);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool, org.apache.tools.ant.taskdefs.optional.ejb.EJBDeploymentTool
    public void processDescriptor(String descriptorName, SAXParser saxParser) {
        this.descriptorName = descriptorName;
        this.iasDescriptorName = null;
        log("iPlanet Deployment Tool processing: " + descriptorName + " (and " + getIasDescriptorName() + ")", 3);
        super.processDescriptor(descriptorName, saxParser);
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void checkConfiguration(String descriptorFileName, SAXParser saxParser) throws BuildException {
        int startOfName = descriptorFileName.lastIndexOf(File.separatorChar) + 1;
        String stdXml = descriptorFileName.substring(startOfName);
        if (stdXml.equals("ejb-jar.xml") && getConfig().baseJarName == null) {
            throw new BuildException("No name specified for the completed JAR file.  The EJB descriptor should be prepended with the JAR name or it should be specified using the attribute \"basejarname\" in the \"ejbjar\" task.", getLocation());
        }
        File iasDescriptor = new File(getConfig().descriptorDir, getIasDescriptorName());
        if (!iasDescriptor.exists() || !iasDescriptor.isFile()) {
            throw new BuildException("The iAS-specific EJB descriptor (" + iasDescriptor + ") was not found.", getLocation());
        }
        if (this.iashome != null && !this.iashome.isDirectory()) {
            throw new BuildException("If \"iashome\" is specified, it must be a valid directory (it was set to " + this.iashome + ").", getLocation());
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected Hashtable<String, File> parseEjbFiles(String descriptorFileName, SAXParser saxParser) throws IOException, SAXException {
        IPlanetEjbc ejbc = new IPlanetEjbc(new File(getConfig().descriptorDir, descriptorFileName), new File(getConfig().descriptorDir, getIasDescriptorName()), getConfig().srcDir, getCombinedClasspath().toString(), saxParser);
        ejbc.setRetainSource(this.keepgenerated);
        ejbc.setDebugOutput(this.debug);
        if (this.iashome != null) {
            ejbc.setIasHomeDir(this.iashome);
        }
        if (getConfig().dtdLocations != null) {
            Iterator<EjbJar.DTDLocation> it = getConfig().dtdLocations.iterator();
            while (it.hasNext()) {
                EjbJar.DTDLocation dtdLocation = it.next();
                ejbc.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation());
            }
        }
        try {
            ejbc.execute();
            this.displayName = ejbc.getDisplayName();
            Hashtable<String, File> files = ejbc.getEjbFiles();
            String[] cmpDescriptors = ejbc.getCmpDescriptors();
            if (cmpDescriptors.length > 0) {
                File baseDir = getConfig().descriptorDir;
                int endOfPath = descriptorFileName.lastIndexOf(File.separator);
                String relativePath = descriptorFileName.substring(0, endOfPath + 1);
                for (String descriptor : cmpDescriptors) {
                    int endOfCmp = descriptor.lastIndexOf(47);
                    String cmpDescriptor = descriptor.substring(endOfCmp + 1);
                    File cmpFile = new File(baseDir, relativePath + cmpDescriptor);
                    if (!cmpFile.exists()) {
                        throw new BuildException("The CMP descriptor file (" + cmpFile + ") could not be found.", getLocation());
                    }
                    files.put(descriptor, cmpFile);
                }
            }
            return files;
        } catch (IPlanetEjbc.EjbcException e) {
            throw new BuildException("An error has occurred while trying to execute the iAS ejbc utility", e, getLocation());
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
        ejbFiles.put("META-INF/ias-ejb-jar.xml", new File(getConfig().descriptorDir, getIasDescriptorName()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    public File getVendorOutputJarFile(String baseName) {
        File jarFile = new File(getDestDir(), baseName + this.jarSuffix);
        log("JAR file name: " + jarFile.toString(), 3);
        return jarFile;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool
    protected String getPublicId() {
        return null;
    }

    private String getIasDescriptorName() {
        String basename;
        String remainder;
        if (this.iasDescriptorName != null) {
            return this.iasDescriptorName;
        }
        String path = "";
        int startOfFileName = this.descriptorName.lastIndexOf(File.separatorChar);
        if (startOfFileName != -1) {
            path = this.descriptorName.substring(0, startOfFileName + 1);
        }
        if (this.descriptorName.substring(startOfFileName + 1).equals("ejb-jar.xml")) {
            basename = "";
            remainder = "ejb-jar.xml";
        } else {
            int endOfBaseName = this.descriptorName.indexOf(getConfig().baseNameTerminator, startOfFileName);
            if (endOfBaseName < 0) {
                endOfBaseName = this.descriptorName.lastIndexOf(46) - 1;
                if (endOfBaseName < 0) {
                    endOfBaseName = this.descriptorName.length() - 1;
                }
            }
            basename = this.descriptorName.substring(startOfFileName + 1, endOfBaseName + 1);
            remainder = this.descriptorName.substring(endOfBaseName + 1);
        }
        this.iasDescriptorName = path + basename + "ias-" + remainder;
        return this.iasDescriptorName;
    }
}
