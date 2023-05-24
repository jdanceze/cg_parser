package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.XmlConstants;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/SchemaValidate.class */
public class SchemaValidate extends XMLValidateTask {
    public static final String ERROR_SAX_1 = "SAX1 parsers are not supported";
    public static final String ERROR_NO_XSD_SUPPORT = "Parser does not support Xerces or JAXP schema features";
    public static final String ERROR_TOO_MANY_DEFAULT_SCHEMAS = "Only one of defaultSchemaFile and defaultSchemaURL allowed";
    public static final String ERROR_PARSER_CREATION_FAILURE = "Could not create parser";
    public static final String MESSAGE_ADDING_SCHEMA = "Adding schema ";
    public static final String ERROR_DUPLICATE_SCHEMA = "Duplicate declaration of schema ";
    private Map<String, SchemaLocation> schemaLocations = new HashMap();
    private boolean fullChecking = true;
    private boolean disableDTD = false;
    private SchemaLocation anonymousSchema;

    @Override // org.apache.tools.ant.taskdefs.optional.XMLValidateTask, org.apache.tools.ant.Task
    public void init() throws BuildException {
        super.init();
        setLenient(false);
    }

    public boolean enableXercesSchemaValidation() {
        try {
            setFeature(XmlConstants.FEATURE_XSD, true);
            setNoNamespaceSchemaProperty(XmlConstants.PROPERTY_NO_NAMESPACE_SCHEMA_LOCATION);
            return true;
        } catch (BuildException e) {
            log(e.toString(), 3);
            return false;
        }
    }

    private void setNoNamespaceSchemaProperty(String property) {
        String anonSchema = getNoNamespaceSchemaURL();
        if (anonSchema != null) {
            setProperty(property, anonSchema);
        }
    }

    public boolean enableJAXP12SchemaValidation() {
        try {
            setProperty(XmlConstants.FEATURE_JAXP12_SCHEMA_LANGUAGE, "http://www.w3.org/2001/XMLSchema");
            setNoNamespaceSchemaProperty(XmlConstants.FEATURE_JAXP12_SCHEMA_SOURCE);
            return true;
        } catch (BuildException e) {
            log(e.toString(), 3);
            return false;
        }
    }

    public void addConfiguredSchema(SchemaLocation location) {
        log("adding schema " + location, 4);
        location.validateNamespace();
        SchemaLocation old = this.schemaLocations.get(location.getNamespace());
        if (old != null && !old.equals(location)) {
            throw new BuildException(ERROR_DUPLICATE_SCHEMA + location);
        }
        this.schemaLocations.put(location.getNamespace(), location);
    }

    public void setFullChecking(boolean fullChecking) {
        this.fullChecking = fullChecking;
    }

    protected void createAnonymousSchema() {
        if (this.anonymousSchema == null) {
            this.anonymousSchema = new SchemaLocation();
        }
        this.anonymousSchema.setNamespace("(no namespace)");
    }

    public void setNoNamespaceURL(String defaultSchemaURL) {
        createAnonymousSchema();
        this.anonymousSchema.setUrl(defaultSchemaURL);
    }

    public void setNoNamespaceFile(File defaultSchemaFile) {
        createAnonymousSchema();
        this.anonymousSchema.setFile(defaultSchemaFile);
    }

    public void setDisableDTD(boolean disableDTD) {
        this.disableDTD = disableDTD;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.optional.XMLValidateTask
    public void initValidator() {
        super.initValidator();
        if (isSax1Parser()) {
            throw new BuildException(ERROR_SAX_1);
        }
        setFeature("http://xml.org/sax/features/namespaces", true);
        if (!enableXercesSchemaValidation() && !enableJAXP12SchemaValidation()) {
            throw new BuildException(ERROR_NO_XSD_SUPPORT);
        }
        setFeature(XmlConstants.FEATURE_XSD_FULL_VALIDATION, this.fullChecking);
        setFeatureIfSupported(XmlConstants.FEATURE_DISALLOW_DTD, this.disableDTD);
        addSchemaLocations();
    }

    @Override // org.apache.tools.ant.taskdefs.optional.XMLValidateTask
    protected XMLReader createDefaultReader() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        try {
            SAXParser saxParser = factory.newSAXParser();
            XMLReader reader = saxParser.getXMLReader();
            return reader;
        } catch (ParserConfigurationException | SAXException e) {
            throw new BuildException(ERROR_PARSER_CREATION_FAILURE, e);
        }
    }

    protected void addSchemaLocations() {
        if (!this.schemaLocations.isEmpty()) {
            String joinedValue = (String) this.schemaLocations.values().stream().map((v0) -> {
                return v0.getURIandLocation();
            }).peek(tuple -> {
                log(MESSAGE_ADDING_SCHEMA + tuple, 3);
            }).collect(Collectors.joining(Instruction.argsep));
            setProperty(XmlConstants.PROPERTY_SCHEMA_LOCATION, joinedValue);
        }
    }

    protected String getNoNamespaceSchemaURL() {
        if (this.anonymousSchema == null) {
            return null;
        }
        return this.anonymousSchema.getSchemaLocationURL();
    }

    protected void setFeatureIfSupported(String feature, boolean value) {
        try {
            getXmlReader().setFeature(feature, value);
        } catch (SAXNotRecognizedException e) {
            log("Not recognized: " + feature, 3);
        } catch (SAXNotSupportedException e2) {
            log("Not supported: " + feature, 3);
        }
    }

    @Override // org.apache.tools.ant.taskdefs.optional.XMLValidateTask
    protected void onSuccessfulValidation(int fileProcessed) {
        log(fileProcessed + XMLValidateTask.MESSAGE_FILES_VALIDATED, 3);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/SchemaValidate$SchemaLocation.class */
    public static class SchemaLocation {
        private String namespace;
        private File file;
        private String url;
        public static final String ERROR_NO_URI = "No namespace URI";
        public static final String ERROR_TWO_LOCATIONS = "Both URL and File were given for schema ";
        public static final String ERROR_NO_FILE = "File not found: ";
        public static final String ERROR_NO_URL_REPRESENTATION = "Cannot make a URL of ";
        public static final String ERROR_NO_LOCATION = "No file or URL supplied for the schema ";

        public String getNamespace() {
            return this.namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public File getFile() {
            return this.file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSchemaLocationURL() {
            boolean hasFile = this.file != null;
            boolean hasURL = isSet(this.url);
            if (!hasFile && !hasURL) {
                throw new BuildException(ERROR_NO_LOCATION + this.namespace);
            }
            if (hasFile && hasURL) {
                throw new BuildException(ERROR_TWO_LOCATIONS + this.namespace);
            }
            String schema = this.url;
            if (hasFile) {
                if (!this.file.exists()) {
                    throw new BuildException(ERROR_NO_FILE + this.file);
                }
                try {
                    schema = FileUtils.getFileUtils().getFileURL(this.file).toString();
                } catch (MalformedURLException e) {
                    throw new BuildException(ERROR_NO_URL_REPRESENTATION + this.file, e);
                }
            }
            return schema;
        }

        public String getURIandLocation() throws BuildException {
            validateNamespace();
            return this.namespace + ' ' + getSchemaLocationURL();
        }

        public void validateNamespace() {
            if (!isSet(getNamespace())) {
                throw new BuildException(ERROR_NO_URI);
            }
        }

        private boolean isSet(String property) {
            return (property == null || property.isEmpty()) ? false : true;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SchemaLocation)) {
                return false;
            }
            SchemaLocation schemaLocation = (SchemaLocation) o;
            if (this.file != null ? this.file.equals(schemaLocation.file) : schemaLocation.file == null) {
                if (this.namespace != null ? this.namespace.equals(schemaLocation.namespace) : schemaLocation.namespace == null) {
                    if (this.url != null ? this.url.equals(schemaLocation.url) : schemaLocation.url == null) {
                        return true;
                    }
                }
            }
            return false;
        }

        public int hashCode() {
            int result = this.namespace == null ? 0 : this.namespace.hashCode();
            return (29 * ((29 * result) + (this.file == null ? 0 : this.file.hashCode()))) + (this.url == null ? 0 : this.url.hashCode());
        }

        public String toString() {
            return (this.namespace == null ? "(anonymous)" : this.namespace) + (this.url == null ? "" : Instruction.argsep + this.url) + (this.file == null ? "" : Instruction.argsep + this.file.getAbsolutePath());
        }
    }
}
