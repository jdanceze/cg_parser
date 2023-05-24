package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/EjbJar.class */
public class EjbJar extends MatchingTask {
    private File destDir;
    private Config config = new Config();
    private String genericJarSuffix = "-generic.jar";
    private String cmpVersion = "1.0";
    private List<EJBDeploymentTool> deploymentTools = new ArrayList();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/EjbJar$DTDLocation.class */
    public static class DTDLocation extends org.apache.tools.ant.types.DTDLocation {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/EjbJar$Config.class */
    public static class Config {
        public File srcDir;
        public File descriptorDir;
        public String baseJarName;
        public Path classpath;
        public NamingScheme namingScheme;
        public File manifest;
        public String analyzer;
        public String baseNameTerminator = HelpFormatter.DEFAULT_OPT_PREFIX;
        public boolean flatDestDir = false;
        public List<FileSet> supportFileSets = new ArrayList();
        public ArrayList<DTDLocation> dtdLocations = new ArrayList<>();

        Config() {
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/EjbJar$NamingScheme.class */
    public static class NamingScheme extends EnumeratedAttribute {
        public static final String EJB_NAME = "ejb-name";
        public static final String DIRECTORY = "directory";
        public static final String DESCRIPTOR = "descriptor";
        public static final String BASEJARNAME = "basejarname";

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{EJB_NAME, "directory", DESCRIPTOR, BASEJARNAME};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/EjbJar$CMPVersion.class */
    public static class CMPVersion extends EnumeratedAttribute {
        public static final String CMP1_0 = "1.0";
        public static final String CMP2_0 = "2.0";

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"1.0", CMP2_0};
        }
    }

    protected void addDeploymentTool(EJBDeploymentTool deploymentTool) {
        deploymentTool.setTask(this);
        this.deploymentTools.add(deploymentTool);
    }

    public OrionDeploymentTool createOrion() {
        OrionDeploymentTool tool = new OrionDeploymentTool();
        addDeploymentTool(tool);
        return tool;
    }

    public WeblogicDeploymentTool createWeblogic() {
        WeblogicDeploymentTool tool = new WeblogicDeploymentTool();
        addDeploymentTool(tool);
        return tool;
    }

    public WebsphereDeploymentTool createWebsphere() {
        WebsphereDeploymentTool tool = new WebsphereDeploymentTool();
        addDeploymentTool(tool);
        return tool;
    }

    public BorlandDeploymentTool createBorland() {
        log("Borland deployment tools", 3);
        BorlandDeploymentTool tool = new BorlandDeploymentTool();
        tool.setTask(this);
        this.deploymentTools.add(tool);
        return tool;
    }

    public IPlanetDeploymentTool createIplanet() {
        log("iPlanet Application Server deployment tools", 3);
        IPlanetDeploymentTool tool = new IPlanetDeploymentTool();
        addDeploymentTool(tool);
        return tool;
    }

    public JbossDeploymentTool createJboss() {
        JbossDeploymentTool tool = new JbossDeploymentTool();
        addDeploymentTool(tool);
        return tool;
    }

    public JonasDeploymentTool createJonas() {
        log("JOnAS deployment tools", 3);
        JonasDeploymentTool tool = new JonasDeploymentTool();
        addDeploymentTool(tool);
        return tool;
    }

    public WeblogicTOPLinkDeploymentTool createWeblogictoplink() {
        log("The <weblogictoplink> element is no longer required. Please use the <weblogic> element and set newCMP=\"true\"", 2);
        WeblogicTOPLinkDeploymentTool tool = new WeblogicTOPLinkDeploymentTool();
        addDeploymentTool(tool);
        return tool;
    }

    public Path createClasspath() {
        if (this.config.classpath == null) {
            this.config.classpath = new Path(getProject());
        }
        return this.config.classpath.createPath();
    }

    public DTDLocation createDTD() {
        DTDLocation dtdLocation = new DTDLocation();
        this.config.dtdLocations.add(dtdLocation);
        return dtdLocation;
    }

    public FileSet createSupport() {
        FileSet supportFileSet = new FileSet();
        this.config.supportFileSets.add(supportFileSet);
        return supportFileSet;
    }

    public void setManifest(File manifest) {
        this.config.manifest = manifest;
    }

    public void setSrcdir(File inDir) {
        this.config.srcDir = inDir;
    }

    public void setDescriptordir(File inDir) {
        this.config.descriptorDir = inDir;
    }

    public void setDependency(String analyzer) {
        this.config.analyzer = analyzer;
    }

    public void setBasejarname(String inValue) {
        this.config.baseJarName = inValue;
        if (this.config.namingScheme == null) {
            this.config.namingScheme = new NamingScheme();
            this.config.namingScheme.setValue(NamingScheme.BASEJARNAME);
        } else if (!NamingScheme.BASEJARNAME.equals(this.config.namingScheme.getValue())) {
            throw new BuildException("The basejarname attribute is not compatible with the %s naming scheme", this.config.namingScheme.getValue());
        }
    }

    public void setNaming(NamingScheme namingScheme) {
        this.config.namingScheme = namingScheme;
        if (!NamingScheme.BASEJARNAME.equals(this.config.namingScheme.getValue()) && this.config.baseJarName != null) {
            throw new BuildException("The basejarname attribute is not compatible with the %s naming scheme", this.config.namingScheme.getValue());
        }
    }

    public File getDestdir() {
        return this.destDir;
    }

    public void setDestdir(File inDir) {
        this.destDir = inDir;
    }

    public String getCmpversion() {
        return this.cmpVersion;
    }

    public void setCmpversion(CMPVersion version) {
        this.cmpVersion = version.getValue();
    }

    public void setClasspath(Path classpath) {
        this.config.classpath = classpath;
    }

    public void setFlatdestdir(boolean inValue) {
        this.config.flatDestDir = inValue;
    }

    public void setGenericjarsuffix(String inString) {
        this.genericJarSuffix = inString;
    }

    public void setBasenameterminator(String inValue) {
        this.config.baseNameTerminator = inValue;
    }

    private void validateConfig() throws BuildException {
        if (this.config.srcDir == null) {
            throw new BuildException("The srcDir attribute must be specified");
        }
        if (this.config.descriptorDir == null) {
            this.config.descriptorDir = this.config.srcDir;
        }
        if (this.config.namingScheme == null) {
            this.config.namingScheme = new NamingScheme();
            this.config.namingScheme.setValue(NamingScheme.DESCRIPTOR);
        } else if (NamingScheme.BASEJARNAME.equals(this.config.namingScheme.getValue()) && this.config.baseJarName == null) {
            throw new BuildException("The basejarname attribute must be specified with the basejarname naming scheme");
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        validateConfig();
        if (this.deploymentTools.isEmpty()) {
            GenericDeploymentTool genericTool = new GenericDeploymentTool();
            genericTool.setTask(this);
            genericTool.setDestdir(this.destDir);
            genericTool.setGenericJarSuffix(this.genericJarSuffix);
            this.deploymentTools.add(genericTool);
        }
        for (EJBDeploymentTool tool : this.deploymentTools) {
            tool.configure(this.config);
            tool.validateConfigured();
        }
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setValidating(true);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DirectoryScanner ds = getDirectoryScanner(this.config.descriptorDir);
            ds.scan();
            String[] files = ds.getIncludedFiles();
            log(files.length + " deployment descriptors located.", 3);
            for (String file : files) {
                for (EJBDeploymentTool tool2 : this.deploymentTools) {
                    tool2.processDescriptor(file, saxParser);
                }
            }
        } catch (ParserConfigurationException pce) {
            throw new BuildException("ParserConfigurationException while creating parser. ", pce);
        } catch (SAXException se) {
            throw new BuildException("SAXException while creating parser.", se);
        }
    }
}
