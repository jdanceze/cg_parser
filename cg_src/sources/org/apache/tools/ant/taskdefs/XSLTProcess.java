package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.taskdefs.optional.TraXLiaison;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess.class */
public class XSLTProcess extends MatchingTask implements XSLTLogger {
    public static final String PROCESSOR_TRAX = "trax";
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private String processor;
    private XSLTLiaison liaison;
    private XPathFactory xpathFactory;
    private XPath xpath;
    private TraceConfiguration traceConfiguration;
    private File destDir = null;
    private File baseDir = null;
    private String xslFile = null;
    private Resource xslResource = null;
    private String targetExtension = ".html";
    private String fileNameParameter = null;
    private String fileDirParameter = null;
    private final List<Param> params = new ArrayList();
    private File inFile = null;
    private File outFile = null;
    private Path classpath = null;
    private boolean stylesheetLoaded = false;
    private boolean force = false;
    private final List<OutputProperty> outputProperties = new Vector();
    private final XMLCatalog xmlCatalog = new XMLCatalog();
    private boolean performDirectoryScan = true;
    private Factory factory = null;
    private boolean reuseLoadedStylesheet = true;
    private AntClassLoader loader = null;
    private Mapper mapperElement = null;
    private final Union resources = new Union();
    private boolean useImplicitFileset = true;
    private boolean suppressWarnings = false;
    private boolean failOnTransformationError = true;
    private boolean failOnError = true;
    private boolean failOnNoResources = true;
    private final CommandlineJava.SysProperties sysProperties = new CommandlineJava.SysProperties();

    public void setScanIncludedDirectories(boolean b) {
        this.performDirectoryScan = b;
    }

    public void setReloadStylesheet(boolean b) {
        this.reuseLoadedStylesheet = !b;
    }

    public void addMapper(Mapper mapper) {
        if (this.mapperElement != null) {
            handleError(Expand.ERROR_MULTIPLE_MAPPERS);
        } else {
            this.mapperElement = mapper;
        }
    }

    public void add(ResourceCollection rc) {
        this.resources.add(rc);
    }

    public void addConfiguredStyle(Resources rc) {
        if (rc.size() != 1) {
            handleError("The style element must be specified with exactly one nested resource.");
        } else {
            setXslResource(rc.iterator().next());
        }
    }

    public void setXslResource(Resource xslResource) {
        this.xslResource = xslResource;
    }

    public void add(FileNameMapper fileNameMapper) throws BuildException {
        Mapper mapper = new Mapper(getProject());
        mapper.add(fileNameMapper);
        addMapper(mapper);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Resource styleResource;
        String[] includedFiles;
        String[] includedDirectories;
        String[] list;
        if ("style".equals(getTaskType())) {
            log("Warning: the task name <style> is deprecated. Use <xslt> instead.", 1);
        }
        File savedBaseDir = this.baseDir;
        if (this.xslResource == null && this.xslFile == null) {
            handleError("specify the stylesheet either as a filename in style attribute or as a nested resource");
        } else if (this.xslResource != null && this.xslFile != null) {
            handleError("specify the stylesheet either as a filename in style attribute or as a nested resource but not as both");
        } else if (this.inFile == null || this.inFile.exists()) {
            try {
                setupLoader();
                if (this.sysProperties.size() > 0) {
                    this.sysProperties.setSystem();
                }
                if (this.baseDir == null) {
                    this.baseDir = getProject().getBaseDir();
                }
                this.liaison = getLiaison();
                if (this.liaison instanceof XSLTLoggerAware) {
                    ((XSLTLoggerAware) this.liaison).setLogger(this);
                }
                log("Using " + this.liaison.getClass().toString(), 3);
                if (this.xslFile != null) {
                    File stylesheet = getProject().resolveFile(this.xslFile);
                    if (!stylesheet.exists()) {
                        File alternative = FILE_UTILS.resolveFile(this.baseDir, this.xslFile);
                        if (alternative.exists()) {
                            log("DEPRECATED - the 'style' attribute should be relative to the project's");
                            log("             basedir, not the tasks's basedir.");
                            stylesheet = alternative;
                        }
                    }
                    FileResource fr = new FileResource();
                    fr.setProject(getProject());
                    fr.setFile(stylesheet);
                    styleResource = fr;
                } else {
                    styleResource = this.xslResource;
                }
                if (!styleResource.isExists()) {
                    handleError("stylesheet " + styleResource + " doesn't exist.");
                    if (this.loader != null) {
                        this.loader.resetThreadContextLoader();
                        this.loader.cleanup();
                        this.loader = null;
                    }
                    if (this.sysProperties.size() > 0) {
                        this.sysProperties.restoreSystem();
                    }
                    this.liaison = null;
                    this.stylesheetLoaded = false;
                    this.baseDir = savedBaseDir;
                } else if (this.inFile == null || this.outFile == null) {
                    checkDest();
                    if (this.useImplicitFileset) {
                        DirectoryScanner scanner = getDirectoryScanner(this.baseDir);
                        log("Transforming into " + this.destDir, 2);
                        for (String element : scanner.getIncludedFiles()) {
                            process(this.baseDir, element, this.destDir, styleResource);
                        }
                        if (this.performDirectoryScan) {
                            for (String dir : scanner.getIncludedDirectories()) {
                                for (String element2 : new File(this.baseDir, dir).list()) {
                                    process(this.baseDir, dir + File.separator + element2, this.destDir, styleResource);
                                }
                            }
                        }
                    } else if (this.resources.isEmpty()) {
                        if (this.failOnNoResources) {
                            handleError("no resources specified");
                        }
                        return;
                    }
                    processResources(styleResource);
                    if (this.loader != null) {
                        this.loader.resetThreadContextLoader();
                        this.loader.cleanup();
                        this.loader = null;
                    }
                    if (this.sysProperties.size() > 0) {
                        this.sysProperties.restoreSystem();
                    }
                    this.liaison = null;
                    this.stylesheetLoaded = false;
                    this.baseDir = savedBaseDir;
                } else {
                    process(this.inFile, this.outFile, styleResource);
                    if (this.loader != null) {
                        this.loader.resetThreadContextLoader();
                        this.loader.cleanup();
                        this.loader = null;
                    }
                    if (this.sysProperties.size() > 0) {
                        this.sysProperties.restoreSystem();
                    }
                    this.liaison = null;
                    this.stylesheetLoaded = false;
                    this.baseDir = savedBaseDir;
                }
            } finally {
                if (this.loader != null) {
                    this.loader.resetThreadContextLoader();
                    this.loader.cleanup();
                    this.loader = null;
                }
                if (this.sysProperties.size() > 0) {
                    this.sysProperties.restoreSystem();
                }
                this.liaison = null;
                this.stylesheetLoaded = false;
                this.baseDir = savedBaseDir;
            }
        } else {
            handleError("input file " + this.inFile + " does not exist");
        }
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public void setBasedir(File dir) {
        this.baseDir = dir;
    }

    public void setDestdir(File dir) {
        this.destDir = dir;
    }

    public void setExtension(String name) {
        this.targetExtension = name;
    }

    public void setStyle(String xslFile) {
        this.xslFile = xslFile;
    }

    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
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

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public void setUseImplicitFileset(boolean useimplicitfileset) {
        this.useImplicitFileset = useimplicitfileset;
    }

    public void addConfiguredXMLCatalog(XMLCatalog xmlCatalog) {
        this.xmlCatalog.addConfiguredXMLCatalog(xmlCatalog);
    }

    public void setFileNameParameter(String fileNameParameter) {
        this.fileNameParameter = fileNameParameter;
    }

    public void setFileDirParameter(String fileDirParameter) {
        this.fileDirParameter = fileDirParameter;
    }

    public void setSuppressWarnings(boolean b) {
        this.suppressWarnings = b;
    }

    public boolean getSuppressWarnings() {
        return this.suppressWarnings;
    }

    public void setFailOnTransformationError(boolean b) {
        this.failOnTransformationError = b;
    }

    public void setFailOnError(boolean b) {
        this.failOnError = b;
    }

    public void setFailOnNoResources(boolean b) {
        this.failOnNoResources = b;
    }

    public void addSysproperty(Environment.Variable sysp) {
        this.sysProperties.addVariable(sysp);
    }

    public void addSyspropertyset(PropertySet sysp) {
        this.sysProperties.addSyspropertyset(sysp);
    }

    public TraceConfiguration createTrace() {
        if (this.traceConfiguration != null) {
            throw new BuildException("can't have more than one trace configuration");
        }
        this.traceConfiguration = new TraceConfiguration();
        return this.traceConfiguration;
    }

    public TraceConfiguration getTraceConfiguration() {
        return this.traceConfiguration;
    }

    private void resolveProcessor(String proc) throws Exception {
        if (PROCESSOR_TRAX.equals(proc)) {
            this.liaison = new TraXLiaison();
        } else {
            this.liaison = (XSLTLiaison) loadClass(proc).asSubclass(XSLTLiaison.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
    }

    private Class<?> loadClass(String classname) throws ClassNotFoundException {
        setupLoader();
        if (this.loader == null) {
            return Class.forName(classname);
        }
        return Class.forName(classname, true, this.loader);
    }

    private void setupLoader() {
        if (this.classpath != null && this.loader == null) {
            this.loader = getProject().createClassLoader(this.classpath);
            this.loader.setThreadContextLoader();
        }
    }

    public void setOut(File outFile) {
        this.outFile = outFile;
    }

    public void setIn(File inFile) {
        this.inFile = inFile;
    }

    private void checkDest() {
        if (this.destDir == null) {
            handleError("destdir attributes must be set!");
        }
    }

    private void processResources(Resource stylesheet) {
        Iterator<Resource> it = this.resources.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            if (r.isExists()) {
                File base = this.baseDir;
                String name = r.getName();
                FileProvider fp = (FileProvider) r.as(FileProvider.class);
                if (fp != null) {
                    FileResource f = ResourceUtils.asFileResource(fp);
                    base = f.getBaseDir();
                    if (base == null) {
                        name = f.getFile().getAbsolutePath();
                    }
                }
                process(base, name, this.destDir, stylesheet);
            }
        }
    }

    private void process(File baseDir, String xmlFile, File destDir, Resource stylesheet) throws BuildException {
        File outF = null;
        try {
            long styleSheetLastModified = stylesheet.getLastModified();
            File inF = new File(baseDir, xmlFile);
            if (inF.isDirectory()) {
                log("Skipping " + inF + " it is a directory.", 3);
                return;
            }
            FileNameMapper mapper = this.mapperElement == null ? new StyleMapper() : this.mapperElement.getImplementation();
            String[] outFileName = mapper.mapFileName(xmlFile);
            if (outFileName == null || outFileName.length == 0) {
                log("Skipping " + this.inFile + " it cannot get mapped to output.", 3);
            } else if (outFileName.length > 1) {
                log("Skipping " + this.inFile + " its mapping is ambiguous.", 3);
            } else {
                File outF2 = new File(destDir, outFileName[0]);
                if (this.force || inF.lastModified() > outF2.lastModified() || styleSheetLastModified > outF2.lastModified()) {
                    ensureDirectoryFor(outF2);
                    log("Processing " + inF + " to " + outF2);
                    configureLiaison(stylesheet);
                    setLiaisonDynamicFileParameters(this.liaison, inF);
                    this.liaison.transform(inF, outF2);
                }
            }
        } catch (Exception ex) {
            log("Failed to process " + this.inFile, 2);
            if (0 != 0) {
                outF.delete();
            }
            handleTransformationError(ex);
        }
    }

    private void process(File inFile, File outFile, Resource stylesheet) throws BuildException {
        try {
            long styleSheetLastModified = stylesheet.getLastModified();
            log("In file " + inFile + " time: " + inFile.lastModified(), 4);
            log("Out file " + outFile + " time: " + outFile.lastModified(), 4);
            log("Style file " + this.xslFile + " time: " + styleSheetLastModified, 4);
            if (this.force || inFile.lastModified() >= outFile.lastModified() || styleSheetLastModified >= outFile.lastModified()) {
                ensureDirectoryFor(outFile);
                log("Processing " + inFile + " to " + outFile, 2);
                configureLiaison(stylesheet);
                setLiaisonDynamicFileParameters(this.liaison, inFile);
                this.liaison.transform(inFile, outFile);
            } else {
                log("Skipping input file " + inFile + " because it is older than output file " + outFile + " and so is the stylesheet " + stylesheet, 4);
            }
        } catch (Exception ex) {
            log("Failed to process " + inFile, 2);
            if (outFile != null) {
                outFile.delete();
            }
            handleTransformationError(ex);
        }
    }

    private void ensureDirectoryFor(File targetFile) throws BuildException {
        File directory = targetFile.getParentFile();
        if (!directory.exists() && !directory.mkdirs() && !directory.isDirectory()) {
            handleError("Unable to create directory: " + directory.getAbsolutePath());
        }
    }

    public Factory getFactory() {
        return this.factory;
    }

    public XMLCatalog getXMLCatalog() {
        this.xmlCatalog.setProject(getProject());
        return this.xmlCatalog;
    }

    public Enumeration<OutputProperty> getOutputProperties() {
        return Collections.enumeration(this.outputProperties);
    }

    protected XSLTLiaison getLiaison() {
        if (this.liaison == null) {
            if (this.processor != null) {
                try {
                    resolveProcessor(this.processor);
                } catch (Exception e) {
                    handleError(e);
                }
            } else {
                try {
                    resolveProcessor(PROCESSOR_TRAX);
                } catch (Throwable e1) {
                    log(StringUtils.getStackTrace(e1), 0);
                    handleError(e1);
                }
            }
        }
        return this.liaison;
    }

    public Param createParam() {
        Param p = new Param();
        this.params.add(p);
        return p;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$Param.class */
    public static class Param {
        private String name = null;
        private String expression = null;
        private String type;
        private Object ifCond;
        private Object unlessCond;
        private Project project;

        public void setProject(Project project) {
            this.project = project;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() throws BuildException {
            if (this.name == null) {
                throw new BuildException("Name attribute is missing.");
            }
            return this.name;
        }

        public String getExpression() throws BuildException {
            if (this.expression == null) {
                throw new BuildException("Expression attribute is missing.");
            }
            return this.expression;
        }

        public String getType() {
            return this.type;
        }

        public void setIf(Object ifCond) {
            this.ifCond = ifCond;
        }

        public void setIf(String ifProperty) {
            setIf((Object) ifProperty);
        }

        public void setUnless(Object unlessCond) {
            this.unlessCond = unlessCond;
        }

        public void setUnless(String unlessProperty) {
            setUnless((Object) unlessProperty);
        }

        public boolean shouldUse() {
            PropertyHelper ph = PropertyHelper.getPropertyHelper(this.project);
            return ph.testIfCondition(this.ifCond) && ph.testUnlessCondition(this.unlessCond);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$ParamType.class */
    public enum ParamType {
        STRING,
        BOOLEAN,
        INT,
        LONG,
        DOUBLE,
        XPATH_STRING,
        XPATH_BOOLEAN,
        XPATH_NUMBER,
        XPATH_NODE,
        XPATH_NODESET;
        
        public static final Map<ParamType, QName> XPATH_TYPES;

        static {
            Map<ParamType, QName> m = new EnumMap<>(ParamType.class);
            m.put(XPATH_STRING, XPathConstants.STRING);
            m.put(XPATH_BOOLEAN, XPathConstants.BOOLEAN);
            m.put(XPATH_NUMBER, XPathConstants.NUMBER);
            m.put(XPATH_NODE, XPathConstants.NODE);
            m.put(XPATH_NODESET, XPathConstants.NODESET);
            XPATH_TYPES = Collections.unmodifiableMap(m);
        }
    }

    public OutputProperty createOutputProperty() {
        OutputProperty p = new OutputProperty();
        this.outputProperties.add(p);
        return p;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$OutputProperty.class */
    public static class OutputProperty {
        private String name;
        private String value;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @Override // org.apache.tools.ant.Task
    public void init() throws BuildException {
        super.init();
        this.xmlCatalog.setProject(getProject());
        this.xpathFactory = XPathFactory.newInstance();
        this.xpath = this.xpathFactory.newXPath();
        this.xpath.setXPathVariableResolver(variableName -> {
            return getProject().getProperty(variableName.toString());
        });
    }

    @Deprecated
    protected void configureLiaison(File stylesheet) throws BuildException {
        FileResource fr = new FileResource();
        fr.setProject(getProject());
        fr.setFile(stylesheet);
        configureLiaison(fr);
    }

    protected void configureLiaison(Resource stylesheet) throws BuildException {
        if (this.stylesheetLoaded && this.reuseLoadedStylesheet) {
            return;
        }
        this.stylesheetLoaded = true;
        try {
            log("Loading stylesheet " + stylesheet, 2);
            if (this.liaison instanceof XSLTLiaison2) {
                ((XSLTLiaison2) this.liaison).configure(this);
            }
            if (this.liaison instanceof XSLTLiaison3) {
                ((XSLTLiaison3) this.liaison).setStylesheet(stylesheet);
            } else {
                FileProvider fp = (FileProvider) stylesheet.as(FileProvider.class);
                if (fp != null) {
                    this.liaison.setStylesheet(fp.getFile());
                } else {
                    handleError(this.liaison.getClass().toString() + " accepts the stylesheet only as a file");
                    return;
                }
            }
            for (Param p : this.params) {
                if (p.shouldUse()) {
                    Object evaluatedParam = evaluateParam(p);
                    if (this.liaison instanceof XSLTLiaison4) {
                        ((XSLTLiaison4) this.liaison).addParam(p.getName(), evaluatedParam);
                    } else if (evaluatedParam == null || (evaluatedParam instanceof String)) {
                        this.liaison.addParam(p.getName(), (String) evaluatedParam);
                    } else {
                        log("XSLTLiaison '" + this.liaison.getClass().getName() + "' supports only String parameters. Converting parameter '" + p.getName() + "' to its String value '" + evaluatedParam, 1);
                        this.liaison.addParam(p.getName(), String.valueOf(evaluatedParam));
                    }
                }
            }
        } catch (Exception ex) {
            log("Failed to transform using stylesheet " + stylesheet, 2);
            handleTransformationError(ex);
        }
    }

    private Object evaluateParam(Param param) throws XPathExpressionException {
        ParamType type;
        String typeName = param.getType();
        String expression = param.getExpression();
        if (typeName == null || typeName.isEmpty()) {
            type = ParamType.STRING;
        } else {
            try {
                type = ParamType.valueOf(typeName);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid XSLT parameter type: " + typeName, e);
            }
        }
        switch (type) {
            case STRING:
                return expression;
            case BOOLEAN:
                return Boolean.valueOf(Boolean.parseBoolean(expression));
            case DOUBLE:
                return Double.valueOf(Double.parseDouble(expression));
            case INT:
                return Integer.valueOf(Integer.parseInt(expression));
            case LONG:
                return Long.valueOf(Long.parseLong(expression));
            default:
                QName xpathType = ParamType.XPATH_TYPES.get(type);
                if (xpathType == null) {
                    throw new IllegalArgumentException("Invalid XSLT parameter type: " + typeName);
                }
                XPathExpression xpe = this.xpath.compile(expression);
                return xpe.evaluate((Object) null, xpathType);
        }
    }

    private void setLiaisonDynamicFileParameters(XSLTLiaison liaison, File inFile) throws Exception {
        if (this.fileNameParameter != null) {
            liaison.addParam(this.fileNameParameter, inFile.getName());
        }
        if (this.fileDirParameter != null) {
            String fileName = FileUtils.getRelativePath(this.baseDir, inFile);
            File file = new File(fileName);
            liaison.addParam(this.fileDirParameter, file.getParent() != null ? file.getParent().replace('\\', '/') : ".");
        }
    }

    public Factory createFactory() throws BuildException {
        if (this.factory != null) {
            handleError("'factory' element must be unique");
        } else {
            this.factory = new Factory();
        }
        return this.factory;
    }

    protected void handleError(String msg) {
        if (this.failOnError) {
            throw new BuildException(msg, getLocation());
        }
        log(msg, 1);
    }

    protected void handleError(Throwable ex) {
        if (this.failOnError) {
            throw new BuildException(ex);
        }
        log("Caught an exception: " + ex, 1);
    }

    protected void handleTransformationError(Exception ex) {
        if (this.failOnError && this.failOnTransformationError) {
            throw new BuildException(ex);
        }
        log("Caught an error during transformation: " + ex, 1);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$Factory.class */
    public static class Factory {
        private String name;
        private final List<Attribute> attributes = new ArrayList();
        private final List<Feature> features = new ArrayList();

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void addAttribute(Attribute attr) {
            this.attributes.add(attr);
        }

        public Enumeration<Attribute> getAttributes() {
            return Collections.enumeration(this.attributes);
        }

        public void addFeature(Feature feature) {
            this.features.add(feature);
        }

        public Iterable<Feature> getFeatures() {
            return this.features;
        }

        /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$Factory$Attribute.class */
        public static class Attribute extends ProjectComponent implements DynamicConfigurator {
            private String name;
            private Object value;

            public String getName() {
                return this.name;
            }

            public Object getValue() {
                return this.value;
            }

            @Override // org.apache.tools.ant.DynamicElement
            public Object createDynamicElement(String name) throws BuildException {
                return null;
            }

            @Override // org.apache.tools.ant.DynamicAttribute
            public void setDynamicAttribute(String name, String value) throws BuildException {
                if ("name".equalsIgnoreCase(name)) {
                    this.name = value;
                } else if ("value".equalsIgnoreCase(name)) {
                    if ("true".equalsIgnoreCase(value)) {
                        this.value = Boolean.TRUE;
                    } else if ("false".equalsIgnoreCase(value)) {
                        this.value = Boolean.FALSE;
                    } else {
                        try {
                            this.value = Integer.valueOf(value);
                        } catch (NumberFormatException e) {
                            this.value = value;
                        }
                    }
                } else if ("valueref".equalsIgnoreCase(name)) {
                    this.value = getProject().getReference(value);
                } else if ("classloaderforpath".equalsIgnoreCase(name)) {
                    this.value = ClasspathUtils.getClassLoaderForPath(getProject(), new Reference(getProject(), value));
                } else {
                    throw new BuildException("Unsupported attribute: %s", name);
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$Factory$Feature.class */
        public static class Feature {
            private String name;
            private boolean value;

            public Feature() {
            }

            public Feature(String name, boolean value) {
                this.name = name;
                this.value = value;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setValue(boolean value) {
                this.value = value;
            }

            public String getName() {
                return this.name;
            }

            public boolean getValue() {
                return this.value;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$StyleMapper.class */
    public class StyleMapper implements FileNameMapper {
        private StyleMapper() {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setFrom(String from) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setTo(String to) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public String[] mapFileName(String xmlFile) {
            int dotPos = xmlFile.lastIndexOf(46);
            if (dotPos > 0) {
                xmlFile = xmlFile.substring(0, dotPos);
            }
            return new String[]{xmlFile + XSLTProcess.this.targetExtension};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XSLTProcess$TraceConfiguration.class */
    public final class TraceConfiguration {
        private boolean elements;
        private boolean extension;
        private boolean generation;
        private boolean selection;
        private boolean templates;

        public TraceConfiguration() {
        }

        public void setElements(boolean b) {
            this.elements = b;
        }

        public boolean getElements() {
            return this.elements;
        }

        public void setExtension(boolean b) {
            this.extension = b;
        }

        public boolean getExtension() {
            return this.extension;
        }

        public void setGeneration(boolean b) {
            this.generation = b;
        }

        public boolean getGeneration() {
            return this.generation;
        }

        public void setSelection(boolean b) {
            this.selection = b;
        }

        public boolean getSelection() {
            return this.selection;
        }

        public void setTemplates(boolean b) {
            this.templates = b;
        }

        public boolean getTemplates() {
            return this.templates;
        }

        public OutputStream getOutputStream() {
            return new LogOutputStream(XSLTProcess.this);
        }
    }
}
