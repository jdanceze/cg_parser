package soot.jimple.infoflow.android.source.parsers.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.tools.ant.util.XmlConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.android.data.CategoryDefinition;
import soot.jimple.infoflow.data.AbstractMethodAndClass;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/parsers/xml/AbstractXMLSourceSinkParser.class */
public abstract class AbstractXMLSourceSinkParser {
    private static final Logger logger = LoggerFactory.getLogger(AbstractXMLSourceSinkParser.class);
    protected Map<String, ISourceSinkDefinition> sourcesAndSinks;
    protected Set<ISourceSinkDefinition> sources = new HashSet();
    protected Set<ISourceSinkDefinition> sinks = new HashSet();
    protected ICategoryFilter categoryFilter = null;
    protected final Map<ISourceSinkCategory, ISourceSinkCategory> categories = new HashMap();

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/parsers/xml/AbstractXMLSourceSinkParser$ICategoryFilter.class */
    public interface ICategoryFilter {
        boolean acceptsCategory(ISourceSinkCategory iSourceSinkCategory);

        SourceSinkType filter(ISourceSinkCategory iSourceSinkCategory, SourceSinkType sourceSinkType);
    }

    protected abstract void buildSourceSinkLists();

    protected abstract void runParse(SAXParser sAXParser, InputStream inputStream);

    protected abstract ISourceSinkDefinition createFieldSourceSinkDefinition(String str, Set<AccessPathTuple> set, List<Set<AccessPathTuple>> list);

    protected abstract ISourceSinkDefinition createMethodSourceSinkDefinition(AbstractMethodAndClass abstractMethodAndClass, Set<AccessPathTuple> set, Set<AccessPathTuple>[] setArr, Set<AccessPathTuple> set2, MethodSourceSinkDefinition.CallType callType, ISourceSinkCategory iSourceSinkCategory);

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/parsers/xml/AbstractXMLSourceSinkParser$SAXHandler.class */
    protected class SAXHandler extends DefaultHandler {
        protected String methodSignature;
        protected String fieldSignature;
        protected ISourceSinkCategory category;
        protected boolean isSource;
        protected boolean isSink;
        protected List<String> pathElements;
        protected List<String> pathElementTypes;
        protected int paramIndex;
        protected List<String> paramTypes;
        protected MethodSourceSinkDefinition.CallType callType;
        protected String accessPathParentElement;
        protected String description;
        protected Set<AccessPathTuple> baseAPs;
        protected List<Set<AccessPathTuple>> paramAPs;
        protected Set<AccessPathTuple> returnAPs;
        protected Map<String, ISourceSinkDefinition> sourcesAndSinks;
        protected ICategoryFilter categoryFilter;

        public SAXHandler() {
            this.methodSignature = null;
            this.fieldSignature = null;
            this.paramTypes = new ArrayList();
            this.accessPathParentElement = "";
            this.description = "";
            this.baseAPs = new HashSet();
            this.paramAPs = new ArrayList();
            this.returnAPs = new HashSet();
            this.categoryFilter = null;
        }

        public SAXHandler(ICategoryFilter categoryFilter) {
            this.methodSignature = null;
            this.fieldSignature = null;
            this.paramTypes = new ArrayList();
            this.accessPathParentElement = "";
            this.description = "";
            this.baseAPs = new HashSet();
            this.paramAPs = new ArrayList();
            this.returnAPs = new HashSet();
            this.categoryFilter = null;
            this.categoryFilter = categoryFilter;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            String qNameLower = qName.toLowerCase();
            switch (qNameLower.hashCode()) {
                case -1287538889:
                    if (qNameLower.equals(XMLConstants.PATHELEMENT_TAG)) {
                        handleStarttagPathelement(attributes);
                        return;
                    }
                    return;
                case -1141192823:
                    if (qNameLower.equals(XMLConstants.ACCESSPATH_TAG)) {
                        handleStarttagAccesspath(attributes);
                        return;
                    }
                    return;
                case -1077554975:
                    if (qNameLower.equals("method")) {
                        handleStarttagMeethod(attributes);
                        return;
                    }
                    return;
                case -934396624:
                    if (qNameLower.equals("return")) {
                        this.accessPathParentElement = qNameLower;
                        this.description = attributes.getValue("description");
                        return;
                    }
                    return;
                case 3016401:
                    if (qNameLower.equals(XMLConstants.BASE_TAG)) {
                        this.accessPathParentElement = qNameLower;
                        this.description = attributes.getValue("description");
                        return;
                    }
                    return;
                case 50511102:
                    if (qNameLower.equals("category")) {
                        handleStarttagCategory(attributes);
                        return;
                    }
                    return;
                case 97427706:
                    if (qNameLower.equals("field")) {
                        handleStarttagField(attributes);
                        return;
                    }
                    return;
                case 106436749:
                    if (qNameLower.equals("param")) {
                        handleStarttagParam(attributes, qNameLower);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        protected void handleStarttagCategory(Attributes attributes) {
            String strSysCategory = attributes.getValue("id").trim();
            String strCustomCategory = attributes.getValue("customId");
            if (strCustomCategory != null && !strCustomCategory.isEmpty()) {
                strCustomCategory = strCustomCategory.trim();
            }
            String strCustomDescription = attributes.getValue("description");
            if (strCustomDescription != null && !strCustomDescription.isEmpty()) {
                strCustomDescription = strCustomDescription.trim();
            }
            this.category = AbstractXMLSourceSinkParser.this.getOrMakeCategory(CategoryDefinition.CATEGORY.valueOf(strSysCategory), strCustomCategory, strCustomDescription);
            if (this.categoryFilter != null && !this.categoryFilter.acceptsCategory(this.category)) {
                this.category = null;
            }
        }

        protected void handleStarttagField(Attributes attributes) {
            if (this.category != null && attributes != null) {
                this.fieldSignature = parseSignature(attributes);
                this.accessPathParentElement = XMLConstants.BASE_TAG;
            }
        }

        protected void handleStarttagMeethod(Attributes attributes) {
            if (this.category != null && attributes != null) {
                this.methodSignature = parseSignature(attributes);
                this.callType = MethodSourceSinkDefinition.CallType.MethodCall;
                String strCallType = attributes.getValue(XMLConstants.CALL_TYPE);
                if (strCallType != null && !strCallType.isEmpty()) {
                    String strCallType2 = strCallType.trim();
                    if (strCallType2.equalsIgnoreCase("MethodCall")) {
                        this.callType = MethodSourceSinkDefinition.CallType.MethodCall;
                    } else if (strCallType2.equalsIgnoreCase("Callback")) {
                        this.callType = MethodSourceSinkDefinition.CallType.Callback;
                    }
                }
            }
        }

        protected void handleStarttagAccesspath(Attributes attributes) {
            if ((this.methodSignature != null && !this.methodSignature.isEmpty()) || (this.fieldSignature != null && !this.fieldSignature.isEmpty())) {
                this.pathElements = new ArrayList();
                this.pathElementTypes = new ArrayList();
                if (attributes != null) {
                    String tempStr = attributes.getValue(XMLConstants.IS_SOURCE_ATTRIBUTE);
                    if (tempStr != null && !tempStr.isEmpty()) {
                        this.isSource = tempStr.equalsIgnoreCase("true");
                    }
                    String tempStr2 = attributes.getValue(XMLConstants.IS_SINK_ATTRIBUTE);
                    if (tempStr2 != null && !tempStr2.isEmpty()) {
                        this.isSink = tempStr2.equalsIgnoreCase("true");
                    }
                    String newDesc = attributes.getValue("description");
                    if (newDesc != null && !newDesc.isEmpty()) {
                        this.description = newDesc;
                    }
                }
            }
        }

        protected void handleStarttagParam(Attributes attributes, String qNameLower) {
            if ((this.methodSignature != null || this.fieldSignature != null) && attributes != null) {
                String tempStr = attributes.getValue(XMLConstants.INDEX_ATTRIBUTE);
                if (tempStr != null && !tempStr.isEmpty()) {
                    this.paramIndex = Integer.parseInt(tempStr);
                }
                String tempStr2 = attributes.getValue("type");
                if (tempStr2 != null && !tempStr2.isEmpty()) {
                    this.paramTypes.add(tempStr2.trim());
                }
            }
            this.accessPathParentElement = qNameLower;
            this.description = attributes.getValue("description");
        }

        protected void handleStarttagPathelement(Attributes attributes) {
            if (this.methodSignature != null && attributes != null) {
                String tempStr = attributes.getValue("field");
                if (tempStr != null && !tempStr.isEmpty()) {
                    this.pathElements.add(tempStr);
                }
                String tempStr2 = attributes.getValue("type");
                if (tempStr2 != null && !tempStr2.isEmpty()) {
                    this.pathElementTypes.add(tempStr2);
                }
            }
        }

        private String parseSignature(Attributes attributes) {
            String signature = attributes.getValue(XMLConstants.SIGNATURE_ATTRIBUTE).trim();
            if (signature != null && !signature.isEmpty() && !signature.startsWith("<")) {
                signature = "<" + signature + ">";
            }
            return signature;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String qNameLower = qName.toLowerCase();
            switch (qNameLower.hashCode()) {
                case -1287538889:
                    if (!qNameLower.equals(XMLConstants.PATHELEMENT_TAG)) {
                    }
                    return;
                case -1141192823:
                    if (qNameLower.equals(XMLConstants.ACCESSPATH_TAG)) {
                        handleEndtagAccesspath();
                        return;
                    }
                    return;
                case -1077554975:
                    if (qNameLower.equals("method")) {
                        handleEndtagMethod();
                        return;
                    }
                    return;
                case -934396624:
                    if (qNameLower.equals("return")) {
                        this.accessPathParentElement = "";
                        return;
                    }
                    return;
                case 3016401:
                    if (qNameLower.equals(XMLConstants.BASE_TAG)) {
                        this.accessPathParentElement = "";
                        return;
                    }
                    return;
                case 50511102:
                    if (qNameLower.equals("category")) {
                        this.category = null;
                        return;
                    }
                    return;
                case 97427706:
                    if (qNameLower.equals("field")) {
                        handleEndtagField();
                        return;
                    }
                    return;
                case 106436749:
                    if (qNameLower.equals("param")) {
                        this.accessPathParentElement = "";
                        this.paramIndex = -1;
                        this.paramTypes.clear();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        protected void handleEndtagMethod() {
            if (this.methodSignature == null) {
                return;
            }
            if (!this.baseAPs.isEmpty() || !this.paramAPs.isEmpty() || !this.returnAPs.isEmpty()) {
                AndroidMethod tempMeth = AndroidMethod.createFromSignature(this.methodSignature);
                ISourceSinkDefinition ssd = AbstractXMLSourceSinkParser.this.createMethodSourceSinkDefinition(tempMeth, this.baseAPs, (Set[]) this.paramAPs.toArray(new Set[this.paramAPs.size()]), this.returnAPs, this.callType, this.category);
                AbstractXMLSourceSinkParser.this.addSourceSinkDefinition(this.methodSignature, ssd);
            }
            this.methodSignature = null;
            this.fieldSignature = null;
            this.baseAPs = new HashSet();
            this.paramAPs = new ArrayList();
            this.returnAPs = new HashSet();
            this.description = null;
        }

        protected void handleEndtagField() {
            if (!this.baseAPs.isEmpty()) {
                ISourceSinkDefinition ssd = AbstractXMLSourceSinkParser.this.createFieldSourceSinkDefinition(this.fieldSignature, this.baseAPs, this.paramAPs);
                ssd.setCategory(this.category);
                AbstractXMLSourceSinkParser.this.addSourceSinkDefinition(this.fieldSignature, ssd);
            }
            this.methodSignature = null;
            this.fieldSignature = null;
            this.baseAPs = new HashSet();
            this.paramAPs = new ArrayList();
            this.returnAPs = new HashSet();
            this.description = null;
        }

        protected void handleEndtagAccesspath() {
            if (this.isSource || this.isSink) {
                if (this.pathElements != null && this.pathElements.isEmpty() && this.pathElementTypes != null && this.pathElementTypes.isEmpty()) {
                    this.pathElements = null;
                    this.pathElementTypes = null;
                }
                if (this.pathElements != null && this.pathElementTypes != null && this.pathElements.size() != this.pathElementTypes.size()) {
                    throw new RuntimeException(String.format("Length mismatch between path elements (%d) and their types (%d)", Integer.valueOf(this.pathElements.size()), Integer.valueOf(this.pathElementTypes.size())));
                }
                if ((this.pathElements == null || this.pathElements.isEmpty()) && this.pathElementTypes != null && !this.pathElementTypes.isEmpty()) {
                    throw new RuntimeException("Got types for path elements, but no elements (i.e., fields)");
                }
                SourceSinkType sstype = SourceSinkType.fromFlags(this.isSink, this.isSource);
                if (this.categoryFilter != null) {
                    sstype = this.categoryFilter.filter(this.category, sstype);
                }
                if (sstype != SourceSinkType.Neither) {
                    AccessPathTuple apt = AccessPathTuple.fromPathElements(this.pathElements, this.pathElementTypes, sstype);
                    if (this.description != null && !this.description.isEmpty()) {
                        apt.setDescription(this.description);
                    }
                    AccessPathTuple apt2 = apt.simplify();
                    String str = this.accessPathParentElement;
                    switch (str.hashCode()) {
                        case -934396624:
                            if (str.equals("return")) {
                                this.returnAPs.add(apt2);
                                break;
                            }
                            break;
                        case 3016401:
                            if (str.equals(XMLConstants.BASE_TAG)) {
                                this.baseAPs.add(apt2);
                                break;
                            }
                            break;
                        case 106436749:
                            if (str.equals("param")) {
                                while (this.paramAPs.size() <= this.paramIndex) {
                                    this.paramAPs.add(new HashSet());
                                }
                                this.paramAPs.get(this.paramIndex).add(apt2);
                                break;
                            }
                            break;
                    }
                }
            }
            this.pathElements = null;
            this.pathElementTypes = null;
            this.isSource = false;
            this.isSink = false;
            this.pathElements = null;
            this.pathElementTypes = null;
            this.description = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ISourceSinkCategory getOrMakeCategory(CategoryDefinition.CATEGORY systemCategory, String customCategory, String customDescription) {
        ISourceSinkCategory keyDef = new CategoryDefinition(systemCategory, customCategory);
        return this.categories.computeIfAbsent(keyDef, d -> {
            return new CategoryDefinition(systemCategory, customCategory, customDescription);
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void parseInputStream(InputStream stream) {
        SAXParserFactory pf = SAXParserFactory.newInstance();
        try {
            pf.setFeature(XmlConstants.FEATURE_DISALLOW_DTD, true);
            pf.setFeature(XmlConstants.FEATURE_EXTERNAL_ENTITIES, false);
            pf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            SAXParser parser = pf.newSAXParser();
            runParse(parser, stream);
        } catch (ParserConfigurationException e) {
            logger.error("Could not parse sources/sinks from stream", (Throwable) e);
        } catch (SAXException e2) {
            logger.error("Could not parse sources/sinks from stream", (Throwable) e2);
        }
        buildSourceSinkLists();
    }

    protected void addSourceSinkDefinition(String signature, IAccessPathBasedSourceSinkDefinition ssd) {
        if (this.sourcesAndSinks.containsKey(signature)) {
            this.sourcesAndSinks.get(signature).merge(ssd);
        } else {
            this.sourcesAndSinks.put(signature, ssd);
        }
    }

    public Set<ISourceSinkDefinition> getSources() {
        return this.sources;
    }

    public Set<ISourceSinkDefinition> getSinks() {
        return this.sinks;
    }

    protected static InputStream getStream(String fileName) throws IOException {
        File f = new File(fileName);
        if (f.exists()) {
            return new FileInputStream(f);
        }
        return ResourceUtils.getResourceStream(fileName);
    }

    public Set<ISourceSinkDefinition> getAllMethods() {
        Set<ISourceSinkDefinition> sourcesSinks = new HashSet<>(this.sources.size() + this.sinks.size());
        sourcesSinks.addAll(this.sources);
        sourcesSinks.addAll(this.sinks);
        return sourcesSinks;
    }

    protected void addSourceSinkDefinition(String signature, ISourceSinkDefinition ssd) {
        if (this.sourcesAndSinks.containsKey(signature)) {
            this.sourcesAndSinks.get(signature).merge(ssd);
        } else {
            this.sourcesAndSinks.put(signature, ssd);
        }
    }

    protected String parseSignature(Attributes attributes) {
        String signature = attributes.getValue(XMLConstants.SIGNATURE_ATTRIBUTE).trim();
        if (signature != null && !signature.isEmpty() && !signature.startsWith("<")) {
            signature = "<" + signature + ">";
        }
        return signature;
    }
}
