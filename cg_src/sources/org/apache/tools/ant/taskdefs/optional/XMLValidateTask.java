package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DTDLocation;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.apache.tools.ant.util.XmlConstants;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.ParserAdapter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/XMLValidateTask.class */
public class XMLValidateTask extends Task {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    protected static final String INIT_FAILED_MSG = "Could not start xml validation: ";
    protected Path classpath;
    public static final String MESSAGE_FILES_VALIDATED = " file(s) have been successfully validated.";
    protected boolean failOnError = true;
    protected boolean warn = true;
    protected boolean lenient = false;
    protected String readerClassName = null;
    protected File file = null;
    protected Vector<FileSet> filesets = new Vector<>();
    protected XMLReader xmlReader = null;
    protected ValidatorErrorHandler errorHandler = new ValidatorErrorHandler();
    private Vector<Attribute> attributeList = new Vector<>();
    private final Vector<Property> propertyList = new Vector<>();
    private XMLCatalog xmlCatalog = new XMLCatalog();
    private AntClassLoader readerLoader = null;

    public void setFailOnError(boolean fail) {
        this.failOnError = fail;
    }

    public void setWarn(boolean bool) {
        this.warn = bool;
    }

    public void setLenient(boolean bool) {
        this.lenient = bool;
    }

    public void setClassName(String className) {
        this.readerClassName = className;
    }

    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
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

    public void setFile(File file) {
        this.file = file;
    }

    public void addConfiguredXMLCatalog(XMLCatalog catalog) {
        this.xmlCatalog.addConfiguredXMLCatalog(catalog);
    }

    public void addFileset(FileSet set) {
        this.filesets.addElement(set);
    }

    public Attribute createAttribute() {
        Attribute feature = new Attribute();
        this.attributeList.addElement(feature);
        return feature;
    }

    public Property createProperty() {
        Property prop = new Property();
        this.propertyList.addElement(prop);
        return prop;
    }

    @Override // org.apache.tools.ant.Task
    public void init() throws BuildException {
        super.init();
        this.xmlCatalog.setProject(getProject());
    }

    public DTDLocation createDTD() {
        DTDLocation dtdLocation = new DTDLocation();
        this.xmlCatalog.addDTD(dtdLocation);
        return dtdLocation;
    }

    protected EntityResolver getEntityResolver() {
        return this.xmlCatalog;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public XMLReader getXmlReader() {
        return this.xmlReader;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String[] includedFiles;
        try {
            int fileProcessed = 0;
            if (this.file == null && this.filesets.isEmpty()) {
                throw new BuildException("Specify at least one source - a file or a fileset.");
            }
            if (this.file != null) {
                if (this.file.exists() && this.file.canRead() && this.file.isFile()) {
                    doValidate(this.file);
                    fileProcessed = 0 + 1;
                } else {
                    String errorMsg = "File " + this.file + " cannot be read";
                    if (this.failOnError) {
                        throw new BuildException(errorMsg);
                    }
                    log(errorMsg, 0);
                }
            }
            Iterator<FileSet> it = this.filesets.iterator();
            while (it.hasNext()) {
                FileSet fs = it.next();
                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                for (String fileName : ds.getIncludedFiles()) {
                    File srcFile = new File(fs.getDir(getProject()), fileName);
                    doValidate(srcFile);
                    fileProcessed++;
                }
            }
            onSuccessfulValidation(fileProcessed);
            cleanup();
        } catch (Throwable th) {
            cleanup();
            throw th;
        }
    }

    protected void onSuccessfulValidation(int fileProcessed) {
        log(fileProcessed + MESSAGE_FILES_VALIDATED);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initValidator() {
        this.xmlReader = createXmlReader();
        this.xmlReader.setEntityResolver(getEntityResolver());
        this.xmlReader.setErrorHandler(this.errorHandler);
        if (!isSax1Parser()) {
            if (!this.lenient) {
                setFeature(XmlConstants.FEATURE_VALIDATION, true);
            }
            Iterator<Attribute> it = this.attributeList.iterator();
            while (it.hasNext()) {
                Attribute feature = it.next();
                setFeature(feature.getName(), feature.getValue());
            }
            Iterator<Property> it2 = this.propertyList.iterator();
            while (it2.hasNext()) {
                Property prop = it2.next();
                setProperty(prop.getName(), prop.getValue());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isSax1Parser() {
        return this.xmlReader instanceof ParserAdapter;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected XMLReader createXmlReader() {
        Class<?> readerClass;
        Object reader;
        XMLReader newReader;
        if (this.readerClassName == null) {
            reader = createDefaultReaderOrParser();
        } else {
            try {
                if (this.classpath != null) {
                    this.readerLoader = getProject().createClassLoader(this.classpath);
                    readerClass = Class.forName(this.readerClassName, true, this.readerLoader);
                } else {
                    readerClass = Class.forName(this.readerClassName);
                }
                reader = readerClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new BuildException(INIT_FAILED_MSG + this.readerClassName, e);
            }
        }
        if (reader instanceof XMLReader) {
            newReader = (XMLReader) reader;
            log("Using SAX2 reader " + reader.getClass().getName(), 3);
        } else if (reader instanceof Parser) {
            newReader = new ParserAdapter((Parser) reader);
            log("Using SAX1 parser " + reader.getClass().getName(), 3);
        } else {
            throw new BuildException(INIT_FAILED_MSG + reader.getClass().getName() + " implements nor SAX1 Parser nor SAX2 XMLReader.");
        }
        return newReader;
    }

    protected void cleanup() {
        if (this.readerLoader != null) {
            this.readerLoader.cleanup();
            this.readerLoader = null;
        }
    }

    private Object createDefaultReaderOrParser() {
        Object reader;
        try {
            reader = createDefaultReader();
        } catch (BuildException e) {
            reader = JAXPUtils.getParser();
        }
        return reader;
    }

    protected XMLReader createDefaultReader() {
        return JAXPUtils.getXMLReader();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setFeature(String feature, boolean value) throws BuildException {
        log("Setting feature " + feature + "=" + value, 4);
        try {
            this.xmlReader.setFeature(feature, value);
        } catch (SAXNotRecognizedException e) {
            throw new BuildException("Parser " + this.xmlReader.getClass().getName() + " doesn't recognize feature " + feature, e, getLocation());
        } catch (SAXNotSupportedException e2) {
            throw new BuildException("Parser " + this.xmlReader.getClass().getName() + " doesn't support feature " + feature, e2, getLocation());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setProperty(String name, String value) throws BuildException {
        if (name == null || value == null) {
            throw new BuildException("Property name and value must be specified.");
        }
        try {
            this.xmlReader.setProperty(name, value);
        } catch (SAXNotRecognizedException e) {
            throw new BuildException("Parser " + this.xmlReader.getClass().getName() + " doesn't recognize property " + name, e, getLocation());
        } catch (SAXNotSupportedException e2) {
            throw new BuildException("Parser " + this.xmlReader.getClass().getName() + " doesn't support property " + name, e2, getLocation());
        }
    }

    protected boolean doValidate(File afile) {
        initValidator();
        boolean result = true;
        try {
            log("Validating " + afile.getName() + "... ", 3);
            this.errorHandler.init(afile);
            InputSource is = new InputSource(Files.newInputStream(afile.toPath(), new OpenOption[0]));
            String uri = FILE_UTILS.toURI(afile.getAbsolutePath());
            is.setSystemId(uri);
            this.xmlReader.parse(is);
        } catch (IOException ex) {
            throw new BuildException("Could not validate document " + afile, ex);
        } catch (SAXException ex2) {
            log("Caught when validating: " + ex2.toString(), 4);
            if (this.failOnError) {
                throw new BuildException("Could not validate document " + afile);
            }
            log("Could not validate document " + afile + ": " + ex2.toString());
            result = false;
        }
        if (this.errorHandler.getFailure()) {
            if (this.failOnError) {
                throw new BuildException(afile + " is not a valid XML document.");
            }
            result = false;
            log(afile + " is not a valid XML document", 0);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/XMLValidateTask$ValidatorErrorHandler.class */
    public class ValidatorErrorHandler implements ErrorHandler {
        protected File currentFile = null;
        protected String lastErrorMessage = null;
        protected boolean failed = false;

        protected ValidatorErrorHandler() {
        }

        public void init(File file) {
            this.currentFile = file;
            this.failed = false;
        }

        public boolean getFailure() {
            return this.failed;
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException exception) {
            this.failed = true;
            doLog(exception, 0);
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException exception) {
            this.failed = true;
            doLog(exception, 0);
        }

        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException exception) {
            if (XMLValidateTask.this.warn) {
                doLog(exception, 1);
            }
        }

        private void doLog(SAXParseException e, int logLevel) {
            XMLValidateTask.this.log(getMessage(e), logLevel);
        }

        private String getMessage(SAXParseException e) {
            String str;
            String sysID = e.getSystemId();
            if (sysID != null) {
                String name = sysID;
                if (sysID.startsWith("file:")) {
                    try {
                        name = XMLValidateTask.FILE_UTILS.fromURI(sysID);
                    } catch (Exception e2) {
                    }
                }
                int line = e.getLineNumber();
                int col = e.getColumnNumber();
                StringBuilder append = new StringBuilder().append(name);
                if (line == -1) {
                    str = "";
                } else {
                    str = ":" + line + (col == -1 ? "" : ":" + col);
                }
                return append.append(str).append(": ").append(e.getMessage()).toString();
            }
            return e.getMessage();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/XMLValidateTask$Attribute.class */
    public static class Attribute {
        private String attributeName = null;
        private boolean attributeValue;

        public void setName(String name) {
            this.attributeName = name;
        }

        public void setValue(boolean value) {
            this.attributeValue = value;
        }

        public String getName() {
            return this.attributeName;
        }

        public boolean getValue() {
            return this.attributeValue;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/XMLValidateTask$Property.class */
    public static final class Property {
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
}
