package soot.jimple.infoflow.rifl;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import soot.jimple.infoflow.rifl.RIFLDocument;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLWriter.class */
public class RIFLWriter {
    private final RIFLDocument document;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLDocument$SourceSinkType;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLDocument$SourceSinkType() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLDocument$SourceSinkType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[RIFLDocument.SourceSinkType.valuesCustom().length];
        try {
            iArr2[RIFLDocument.SourceSinkType.Category.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[RIFLDocument.SourceSinkType.Sink.ordinal()] = 3;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[RIFLDocument.SourceSinkType.Source.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLDocument$SourceSinkType = iArr2;
        return iArr2;
    }

    public RIFLWriter(RIFLDocument document) {
        this.document = document;
    }

    public String write() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(RIFLConstants.RIFL_SPEC_TAG);
            document.appendChild(rootElement);
            writeInterfaceSpec(document, rootElement);
            writeDomains(document, rootElement);
            writeDomainAssignment(document, rootElement);
            writeFlowPolicy(document, rootElement);
            StringWriter stringWriter = new StringWriter();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);
            return stringWriter.toString();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (TransformerConfigurationException ex2) {
            throw new RuntimeException(ex2);
        } catch (TransformerException ex3) {
            throw new RuntimeException(ex3);
        }
    }

    private void writeInterfaceSpec(Document document, Element rootElement) {
        Element attackerIO = document.createElement(RIFLConstants.INTERFACE_SPEC_TAG);
        rootElement.appendChild(attackerIO);
        for (RIFLDocument.Assignable assign : this.document.getInterfaceSpec().getSourcesSinks()) {
            writeAssignable(assign, document, attackerIO);
        }
    }

    private void writeAssignable(RIFLDocument.Assignable assign, Document document, Element rootElement) {
        Element attackerIO = document.createElement(RIFLConstants.ASSIGNABLE_TAG);
        rootElement.appendChild(attackerIO);
        attackerIO.setAttribute(RIFLConstants.HANDLE_ATTRIBUTE, assign.getHandle());
        writeSourceSinkSpec(assign.getElement(), document, attackerIO);
    }

    private void writeSourceSinkSpec(RIFLDocument.SourceSinkSpec spec, Document document, Element parentElement) {
        Element containerElement;
        switch ($SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLDocument$SourceSinkType()[spec.getType().ordinal()]) {
            case 1:
                containerElement = document.createElement("category");
                break;
            case 2:
                containerElement = document.createElement(RIFLConstants.SOURCE_TAG);
                break;
            case 3:
                containerElement = document.createElement(RIFLConstants.SINK_TAG);
                break;
            default:
                throw new RuntimeException("Invalid source/sink type");
        }
        parentElement.appendChild(containerElement);
        if (spec instanceof RIFLDocument.JavaParameterSpec) {
            writeJavaParameterSpec((RIFLDocument.JavaParameterSpec) spec, document, containerElement);
        } else if (spec instanceof RIFLDocument.JavaFieldSpec) {
            writeJavaFieldSpec((RIFLDocument.JavaFieldSpec) spec, document, containerElement);
        } else if (spec instanceof RIFLDocument.JavaReturnValueSpec) {
            writeJavaReturnValueSpec((RIFLDocument.JavaReturnValueSpec) spec, document, containerElement);
        } else if (spec instanceof RIFLDocument.Category) {
            writeCategory((RIFLDocument.Category) spec, document, containerElement);
        } else {
            throw new RuntimeException("Unsupported source or sink specification type");
        }
    }

    private void writeJavaParameterSpec(RIFLDocument.JavaParameterSpec spec, Document document, Element parentElement) {
        Element parameter = document.createElement("parameter");
        parentElement.appendChild(parameter);
        parameter.setAttribute("class", spec.getClassName());
        parameter.setAttribute("method", spec.getHalfSignature());
        parameter.setAttribute("parameter", Integer.toString(spec.getParamIdx()));
    }

    private void writeJavaFieldSpec(RIFLDocument.JavaFieldSpec spec, Document document, Element parentElement) {
        Element parameter = document.createElement("field");
        parentElement.appendChild(parameter);
        parameter.setAttribute("class", spec.getClassName());
        parameter.setAttribute("field", spec.getFieldName());
    }

    private void writeJavaReturnValueSpec(RIFLDocument.JavaReturnValueSpec spec, Document document, Element parentElement) {
        Element parameter = document.createElement(RIFLConstants.RETURN_VALUE_TAG);
        parentElement.appendChild(parameter);
        parameter.setAttribute("class", spec.getClassName());
        parameter.setAttribute("method", spec.getHalfSignature());
    }

    private void writeCategory(RIFLDocument.Category category, Document document, Element parentElement) {
        Element categoryElement = document.createElement("category");
        parentElement.appendChild(categoryElement);
        categoryElement.setAttribute("name", category.getName());
        for (RIFLDocument.SourceSinkSpec element : category.getElements()) {
            writeSourceSinkSpec(element, document, categoryElement);
        }
    }

    private void writeDomains(Document document, Element rootElement) {
        Element domains = document.createElement(RIFLConstants.DOMAINS_TAG);
        rootElement.appendChild(domains);
        for (RIFLDocument.DomainSpec spec : this.document.getDomains()) {
            writeDomainSpec(spec, document, domains);
        }
    }

    private void writeDomainSpec(RIFLDocument.DomainSpec spec, Document document, Element parentElement) {
        Element categoryDomain = document.createElement("domain");
        parentElement.appendChild(categoryDomain);
        categoryDomain.setAttribute("name", spec.getName());
    }

    private void writeDomainAssignment(Document document, Element rootElement) {
        Element domainAssignment = document.createElement(RIFLConstants.DOMAIN_ASSIGNMENT_TAG);
        rootElement.appendChild(domainAssignment);
        for (RIFLDocument.DomainAssignment spec : this.document.getDomainAssignment()) {
            writeDomainAssignment(spec, document, domainAssignment);
        }
    }

    private void writeDomainAssignment(RIFLDocument.DomainAssignment pair, Document document, Element rootElement) {
        Element pairElement = document.createElement(RIFLConstants.ASSIGN_TAG);
        rootElement.appendChild(pairElement);
        pairElement.setAttribute(RIFLConstants.HANDLE_ATTRIBUTE, pair.getSourceOrSink().getHandle());
        pairElement.setAttribute("domain", pair.getDomain().getName());
    }

    private void writeFlowPolicy(Document document, Element rootElement) {
        Element flowPolicy = document.createElement(RIFLConstants.FLOW_RELATION_TAG);
        rootElement.appendChild(flowPolicy);
        for (RIFLDocument.FlowPair pair : this.document.getFlowPolicy()) {
            writeFlowPair(pair, document, flowPolicy);
        }
    }

    private void writeFlowPair(RIFLDocument.FlowPair pair, Document document, Element parentElement) {
        Element flowPair = document.createElement(RIFLConstants.FLOW_TAG);
        parentElement.appendChild(flowPair);
        flowPair.setAttribute("from", pair.getFirstDomain().getName());
        flowPair.setAttribute("to", pair.getSecondDomain().getName());
    }

    public RIFLDocument getDocument() {
        return this.document;
    }
}
