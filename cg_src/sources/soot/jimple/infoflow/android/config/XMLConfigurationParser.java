package soot.jimple.infoflow.android.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.data.CategoryDefinition;
import soot.jimple.infoflow.android.source.parsers.xml.ResourceUtils;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/config/XMLConfigurationParser.class */
public class XMLConfigurationParser {
    private static final String XSD_FILE_PATH = "schema/FlowDroidConfiguration.xsd";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private final InputStream xmlStream;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/config/XMLConfigurationParser$XMLSection.class */
    private enum XMLSection {
        CONFIGURATION,
        INPUT_FILES,
        SOURCES,
        SINKS,
        ANDROID_CONFIGURATION,
        ICC_CONFIGURATION,
        DATA_FLOW_CONFIGURATION,
        DUMMY;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static XMLSection[] valuesCustom() {
            XMLSection[] valuesCustom = values();
            int length = valuesCustom.length;
            XMLSection[] xMLSectionArr = new XMLSection[length];
            System.arraycopy(valuesCustom, 0, xMLSectionArr, 0, length);
            return xMLSectionArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/config/XMLConfigurationParser$SAXHandler.class */
    private class SAXHandler extends DefaultHandler {
        private final InfoflowAndroidConfiguration config;
        private String currentElement = "";
        private Stack<XMLSection> parseStack = new Stack<>();
        private boolean enableIccTracking = false;

        public SAXHandler(InfoflowAndroidConfiguration config) {
            this.config = config;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            XMLSection stackElement = this.parseStack.isEmpty() ? null : this.parseStack.peek();
            if (this.parseStack.isEmpty()) {
                if (qName.equals(XMLConstants.TAG_ROOT_ELEMENT)) {
                    this.parseStack.push(XMLSection.CONFIGURATION);
                }
            } else if (stackElement == XMLSection.CONFIGURATION) {
                if (qName.equals(XMLConstants.TAG_INPUT_FILES)) {
                    this.parseStack.push(XMLSection.INPUT_FILES);
                } else if (qName.equals(XMLConstants.TAG_SOURCE_SPEC)) {
                    this.parseStack.push(XMLSection.SOURCES);
                    String strDefaultMode = attributes.getValue(XMLConstants.ATTR_DEFAULT_MODE);
                    if (strDefaultMode != null && !strDefaultMode.isEmpty()) {
                        this.config.getSourceSinkConfig().setSourceFilterMode(InfoflowConfiguration.SourceSinkFilterMode.valueOf(strDefaultMode));
                    }
                } else if (qName.equals(XMLConstants.TAG_SINK_SPEC)) {
                    this.parseStack.push(XMLSection.SINKS);
                    String strDefaultMode2 = attributes.getValue(XMLConstants.ATTR_DEFAULT_MODE);
                    if (strDefaultMode2 != null && !strDefaultMode2.isEmpty()) {
                        this.config.getSourceSinkConfig().setSinkFilterMode(InfoflowConfiguration.SourceSinkFilterMode.valueOf(strDefaultMode2));
                    }
                } else if (qName.equals(XMLConstants.TAG_ANDROID_CONFIGURATION)) {
                    this.parseStack.push(XMLSection.ANDROID_CONFIGURATION);
                } else if (qName.equals(XMLConstants.TAG_ICC_CONFIGURATION)) {
                    this.parseStack.push(XMLSection.ICC_CONFIGURATION);
                } else if (qName.equals(XMLConstants.TAG_DATA_FLOW_CONFIGURATION)) {
                    this.parseStack.push(XMLSection.DATA_FLOW_CONFIGURATION);
                }
            } else if (stackElement == XMLSection.SOURCES) {
                if (qName.equals("category")) {
                    String strId = attributes.getValue("id");
                    String strCustomId = attributes.getValue("customId");
                    String strMode = attributes.getValue("mode");
                    CategoryDefinition catDef = new CategoryDefinition(CategoryDefinition.CATEGORY.valueOf(strId), strCustomId);
                    this.config.getSourceSinkConfig().addSourceCategory(catDef, InfoflowConfiguration.CategoryMode.valueOf(strMode));
                }
            } else if (stackElement == XMLSection.SINKS && qName.equals("category")) {
                String strId2 = attributes.getValue("id");
                String strCustomId2 = attributes.getValue("customId");
                String strMode2 = attributes.getValue("mode");
                CategoryDefinition catDef2 = new CategoryDefinition(CategoryDefinition.CATEGORY.valueOf(strId2), strCustomId2);
                this.config.getSourceSinkConfig().addSinkCategory(catDef2, InfoflowConfiguration.CategoryMode.valueOf(strMode2));
            }
            if (stackElement == XMLSection.INPUT_FILES || stackElement == XMLSection.ANDROID_CONFIGURATION || stackElement == XMLSection.ICC_CONFIGURATION || stackElement == XMLSection.DATA_FLOW_CONFIGURATION || stackElement == XMLSection.SINKS || stackElement == XMLSection.SOURCES) {
                this.currentElement = qName;
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (this.currentElement == null || this.currentElement.isEmpty()) {
                this.parseStack.pop();
            }
            this.currentElement = "";
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String data = new String(ch, start, length);
            if (!this.parseStack.isEmpty()) {
                if (this.parseStack.peek() == XMLSection.INPUT_FILES) {
                    InfoflowAndroidConfiguration.AnalysisFileConfiguration fileConfig = this.config.getAnalysisFileConfig();
                    if (this.currentElement.equals(XMLConstants.TAG_TARGET_APK_FILE)) {
                        fileConfig.setTargetAPKFile(data);
                    } else if (this.currentElement.equals(XMLConstants.TAG_SOURCE_SINK_FILE)) {
                        fileConfig.setSourceSinkFile(data);
                    } else if (this.currentElement.equals(XMLConstants.TAG_ANDROID_PLATFORM_DIR)) {
                        fileConfig.setAndroidPlatformDir(data);
                    } else if (this.currentElement.equals(XMLConstants.TAG_OUTPUT_FILE)) {
                        fileConfig.setOutputFile(data);
                    }
                } else if (this.parseStack.peek() == XMLSection.ANDROID_CONFIGURATION) {
                    InfoflowAndroidConfiguration.CallbackConfiguration callbackConfig = this.config.getCallbackConfig();
                    InfoflowAndroidConfiguration.SourceSinkConfiguration sourceSinkConfig = this.config.getSourceSinkConfig();
                    if (this.currentElement.equals(XMLConstants.TAG_ENABLE_CALLBACKS)) {
                        callbackConfig.setEnableCallbacks(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_FILTER_THREAD_CALLBACKS)) {
                        callbackConfig.setFilterThreadCallbacks(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_MAX_CALLBACKS_PER_COMPONENT)) {
                        callbackConfig.setMaxCallbacksPerComponent(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_MAX_CALLBACK_DEPTH)) {
                        callbackConfig.setMaxAnalysisCallbackDepth(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_LAYOUT_MATCHING_MODE)) {
                        sourceSinkConfig.setLayoutMatchingMode(InfoflowConfiguration.LayoutMatchingMode.valueOf(data));
                    } else if (this.currentElement.equals(XMLConstants.TAG_MERGE_DEX_FILES)) {
                        this.config.setMergeDexFiles(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_CALLBACK_SOURCE_MODE)) {
                        sourceSinkConfig.setCallbackSourceMode(InfoflowConfiguration.CallbackSourceMode.valueOf(data));
                    } else if (this.currentElement.equals(XMLConstants.TAG_CALLBACK_ANALYSIS_TIMEOUT)) {
                        callbackConfig.setCallbackAnalysisTimeout(Integer.valueOf(data).intValue());
                    }
                } else if (this.parseStack.peek() == XMLSection.ICC_CONFIGURATION) {
                    InfoflowAndroidConfiguration.IccConfiguration iccConfig = this.config.getIccConfig();
                    if (this.currentElement.equals(XMLConstants.TAG_ENABLE_ICC_TRACKING)) {
                        this.enableIccTracking = Boolean.valueOf(data).booleanValue();
                    } else if (this.currentElement.equals(XMLConstants.TAG_MODEL_FILE)) {
                        iccConfig.setIccModel(data);
                    } else if (this.currentElement.equals(XMLConstants.TAG_PURIFY_RESULTS)) {
                        iccConfig.setIccResultsPurify(Boolean.valueOf(data).booleanValue());
                    }
                } else if (this.parseStack.peek() == XMLSection.DATA_FLOW_CONFIGURATION) {
                    InfoflowConfiguration.PathConfiguration pathConfig = this.config.getPathConfiguration();
                    InfoflowConfiguration.SolverConfiguration solverConfig = this.config.getSolverConfiguration();
                    if (this.currentElement.equals(XMLConstants.TAG_MAX_JOIN_POINT_ABSTRACTIONS)) {
                        solverConfig.setMaxJoinPointAbstractions(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_MAX_CALLEES_PER_CALL_SITE)) {
                        solverConfig.setMaxCalleesPerCallSite(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_IMPLICIT_FLOW_MODE)) {
                        this.config.setImplicitFlowMode(InfoflowConfiguration.ImplicitFlowMode.valueOf(data));
                    } else if (this.currentElement.equals(XMLConstants.TAG_STATIC_FIELD_TRACKING_MODE)) {
                        this.config.setStaticFieldTrackingMode(InfoflowConfiguration.StaticFieldTrackingMode.valueOf(data));
                    } else if (this.currentElement.equals(XMLConstants.TAG_ENABLE_EXCEPTIONS)) {
                        this.config.setEnableExceptionTracking(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_ENABLE_ARRAYS)) {
                        this.config.setEnableArrayTracking(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_ENABLE_REFLECTION)) {
                        this.config.setEnableReflection(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_ENABLE_LINENUMBERS)) {
                        this.config.setEnableLineNumbers(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_ENABLE_ORIGINALNAMES)) {
                        this.config.setEnableOriginalNames(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_FLOW_SENSITIVE_ALIASING)) {
                        this.config.setFlowSensitiveAliasing(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_LOG_SOURCES_AND_SINKS)) {
                        this.config.setLogSourcesAndSinks(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_ENABLE_ARRAY_SIZE_TAINTING)) {
                        this.config.setEnableArraySizeTainting(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_PATH_RECONSTRUCTION_MODE)) {
                        pathConfig.setPathReconstructionMode(InfoflowConfiguration.PathReconstructionMode.valueOf(data));
                    } else if (this.currentElement.equals(XMLConstants.TAG_PATH_AGNOSTIC_RESULTS)) {
                        this.config.setPathAgnosticResults(Boolean.valueOf(data).booleanValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_MAX_CALLSTACK_SIZE)) {
                        pathConfig.setMaxCallStackSize(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_MAX_PATH_LENGTH)) {
                        pathConfig.setMaxPathLength(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_MAX_PATHS_PER_ABSTRACTION)) {
                        pathConfig.setMaxPathsPerAbstraction(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_DATA_FLOW_TIMEOUT)) {
                        this.config.setDataFlowTimeout(Long.valueOf(data).longValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_PATH_RECONSTRUCTION_TIMEOUT)) {
                        pathConfig.setPathReconstructionTimeout(Long.valueOf(data).longValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_PATH_RECONSTRUCTION_TIMEOUT)) {
                        pathConfig.setPathReconstructionTimeout(Long.valueOf(data).longValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_PATH_RECONSTRUCTION_BATCH_SIZE)) {
                        pathConfig.setPathReconstructionBatchSize(Integer.valueOf(data).intValue());
                    } else if (this.currentElement.equals(XMLConstants.TAG_WRITE_OUTPUT_FILES)) {
                        this.config.setWriteOutputFiles(Boolean.valueOf(data).booleanValue());
                    }
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
            super.endDocument();
            if (!this.enableIccTracking) {
                this.config.getIccConfig().setIccModel(null);
            }
        }
    }

    public static XMLConfigurationParser fromFile(String fileName) throws IOException {
        if (!verifyXML(fileName)) {
            throw new RuntimeException("The XML-File isn't valid");
        }
        FileInputStream inputStream = new FileInputStream(fileName);
        return fromStream(inputStream);
    }

    public static XMLConfigurationParser fromStream(InputStream inputStream) throws IOException {
        XMLConfigurationParser pmp = new XMLConfigurationParser(inputStream);
        return pmp;
    }

    private XMLConfigurationParser(InputStream stream) {
        this.xmlStream = stream;
    }

    private static boolean verifyXML(String fileName) throws IOException {
        SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        StreamSource xsdFile = new StreamSource(ResourceUtils.getResourceStream(XSD_FILE_PATH));
        StreamSource xmlFile = new StreamSource(new File(fileName));
        boolean validXML = false;
        try {
            try {
                Schema schema = sf.newSchema(xsdFile);
                Validator validator = schema.newValidator();
                try {
                    validator.validate(xmlFile);
                    validXML = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!validXML) {
                    new IOException("File isn't  valid against the xsd");
                }
                xsdFile.getInputStream().close();
                if (xmlFile.getInputStream() != null) {
                    xmlFile.getInputStream().close();
                }
            } catch (SAXException e2) {
                e2.printStackTrace();
                xsdFile.getInputStream().close();
                if (xmlFile.getInputStream() != null) {
                    xmlFile.getInputStream().close();
                }
            }
            return validXML;
        } catch (Throwable th) {
            xsdFile.getInputStream().close();
            if (xmlFile.getInputStream() != null) {
                xmlFile.getInputStream().close();
            }
            throw th;
        }
    }

    public void parse(InfoflowAndroidConfiguration config) {
        SAXParserFactory pf = SAXParserFactory.newInstance();
        try {
            SAXParser parser = pf.newSAXParser();
            parser.parse(this.xmlStream, new SAXHandler(config));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e2) {
            e2.printStackTrace();
        } catch (SAXException e3) {
            e3.printStackTrace();
        }
    }

    public void close() throws IOException {
        this.xmlStream.close();
    }
}
