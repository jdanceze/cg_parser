package soot.jimple.infoflow.android.source.parsers.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser;
import soot.jimple.infoflow.data.AbstractMethodAndClass;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.FieldSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/source/parsers/xml/XMLSourceSinkParser.class */
public class XMLSourceSinkParser extends AbstractXMLSourceSinkParser implements ISourceSinkDefinitionProvider {
    private static final Logger logger = LoggerFactory.getLogger(XMLSourceSinkParser.class);
    protected static final String XSD_FILE_PATH = "schema/SourcesAndSinks.xsd";
    protected static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    @Override // soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser
    protected /* bridge */ /* synthetic */ ISourceSinkDefinition createFieldSourceSinkDefinition(String str, Set set, List list) {
        return createFieldSourceSinkDefinition(str, (Set<AccessPathTuple>) set, (List<Set<AccessPathTuple>>) list);
    }

    public static XMLSourceSinkParser fromFile(String fileName) throws IOException {
        return fromFile(fileName, null);
    }

    public static XMLSourceSinkParser fromFile(String fileName, AbstractXMLSourceSinkParser.ICategoryFilter categoryFilter) throws IOException {
        Throwable th;
        logger.info(String.format("Loading sources and sinks from %s...", fileName));
        Throwable th2 = null;
        try {
            InputStream is = getStream(fileName);
            verifyXML(is);
            if (is != null) {
                is.close();
            }
            th2 = null;
            try {
                InputStream inputStream = getStream(fileName);
                XMLSourceSinkParser fromStream = fromStream(inputStream, categoryFilter);
                if (inputStream != null) {
                    inputStream.close();
                }
                return fromStream;
            } finally {
            }
        } finally {
        }
    }

    protected static InputStream getStream(String fileName) throws IOException {
        File f = new File(fileName);
        if (f.exists()) {
            return new FileInputStream(f);
        }
        return ResourceUtils.getResourceStream(fileName);
    }

    public static XMLSourceSinkParser fromStream(InputStream inputStream) throws IOException {
        return fromStream(inputStream, null);
    }

    public static XMLSourceSinkParser fromStream(InputStream inputStream, AbstractXMLSourceSinkParser.ICategoryFilter categoryFilter) throws IOException {
        XMLSourceSinkParser pmp = new XMLSourceSinkParser(categoryFilter);
        pmp.parseInputStream(inputStream);
        return pmp;
    }

    protected XMLSourceSinkParser(AbstractXMLSourceSinkParser.ICategoryFilter categoryFilter) {
        this.sourcesAndSinks = new HashMap();
        this.categoryFilter = categoryFilter;
    }

    @Override // soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser
    protected void buildSourceSinkLists() {
        for (ISourceSinkDefinition def : this.sourcesAndSinks.values()) {
            ISourceSinkDefinition sourceDef = def.getSourceOnlyDefinition();
            if (sourceDef != null && !sourceDef.isEmpty()) {
                if (sourceDef instanceof MethodSourceSinkDefinition) {
                    MethodSourceSinkDefinition methodSrc = (MethodSourceSinkDefinition) sourceDef;
                    if (methodSrc.getMethod() instanceof AndroidMethod) {
                        AndroidMethod am = (AndroidMethod) methodSrc.getMethod();
                        am.setSourceSinkType(am.getSourceSinkType().addType(SourceSinkType.Source));
                    }
                }
                this.sources.add(sourceDef);
            }
            ISourceSinkDefinition sinkDef = def.getSinkOnlyDefinition();
            if (sinkDef != null && !sinkDef.isEmpty()) {
                if (sourceDef instanceof MethodSourceSinkDefinition) {
                    MethodSourceSinkDefinition methodSink = (MethodSourceSinkDefinition) sourceDef;
                    if (methodSink.getMethod() instanceof AndroidMethod) {
                        AndroidMethod am2 = (AndroidMethod) methodSink.getMethod();
                        am2.setSourceSinkType(am2.getSourceSinkType().addType(SourceSinkType.Sink));
                    }
                }
                this.sinks.add(sinkDef);
            }
        }
        logger.info(String.format("Loaded %d sources and %d sinks from the XML file", Integer.valueOf(this.sources.size()), Integer.valueOf(this.sinks.size())));
    }

    private static void verifyXML(InputStream inp) throws IOException {
        SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        StreamSource xsdFile = new StreamSource(ResourceUtils.getResourceStream(String.valueOf(InfoflowConfiguration.getBaseDirectory()) + XSD_FILE_PATH));
        StreamSource xmlFile = new StreamSource(inp);
        try {
            try {
                Schema schema = sf.newSchema(xsdFile);
                Validator validator = schema.newValidator();
                try {
                    validator.validate(xmlFile);
                } catch (IOException e) {
                    throw new IOException("File isn't  valid against the xsd", e);
                }
            } catch (SAXException e2) {
                throw new IOException("File isn't  valid against the xsd", e2);
            }
        } finally {
            xsdFile.getInputStream().close();
            xmlFile.getInputStream().close();
        }
    }

    @Override // soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser
    protected ISourceSinkDefinition createMethodSourceSinkDefinition(AbstractMethodAndClass method, Set<AccessPathTuple> baseAPs, Set<AccessPathTuple>[] setArr, Set<AccessPathTuple> returnAPs, MethodSourceSinkDefinition.CallType callType, ISourceSinkCategory category) {
        if (method instanceof AndroidMethod) {
            AndroidMethod amethod = (AndroidMethod) method;
            return new MethodSourceSinkDefinition(amethod, baseAPs, setArr, returnAPs, callType, category);
        }
        return null;
    }

    @Override // soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser
    protected IAccessPathBasedSourceSinkDefinition createFieldSourceSinkDefinition(String signature, Set<AccessPathTuple> baseAPs, List<Set<AccessPathTuple>> paramAPs) {
        return new FieldSourceSinkDefinition(signature, baseAPs);
    }

    @Override // soot.jimple.infoflow.android.source.parsers.xml.AbstractXMLSourceSinkParser
    protected void runParse(SAXParser parser, InputStream stream) {
        try {
            parser.parse(stream, new AbstractXMLSourceSinkParser.SAXHandler(this.categoryFilter));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
