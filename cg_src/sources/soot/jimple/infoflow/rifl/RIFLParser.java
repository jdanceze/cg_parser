package soot.jimple.infoflow.rifl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import soot.jimple.infoflow.rifl.RIFLDocument;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLParser.class */
public class RIFLParser {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLParser$RIFLState.class */
    public enum RIFLState {
        RiflSpec,
        InterfaceSpec,
        Assignable,
        Source,
        Sink,
        ReturnValue,
        Parameter,
        Field,
        Category,
        Domains,
        Domain,
        FlowRelation,
        Flow,
        DomainAssignments,
        Assign;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static RIFLState[] valuesCustom() {
            RIFLState[] valuesCustom = values();
            int length = valuesCustom.length;
            RIFLState[] rIFLStateArr = new RIFLState[length];
            System.arraycopy(valuesCustom, 0, rIFLStateArr, 0, length);
            return rIFLStateArr;
        }
    }

    public RIFLDocument parseRIFL(File riflFile) throws SAXException, IOException {
        if (!riflFile.exists()) {
            throw new FileNotFoundException("RIFL file " + riflFile + " not found");
        }
        final RIFLDocument doc = new RIFLDocument();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(riflFile, new DefaultHandler() { // from class: soot.jimple.infoflow.rifl.RIFLParser.1
                private Stack<RIFLState> stateStack = new Stack<>();
                private String assignableHandle = "";
                private RIFLDocument.Assignable assignable = null;
                private RIFLDocument.SourceSinkSpec sourceSinkSpec = null;
                private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLParser$RIFLState;

                static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLParser$RIFLState() {
                    int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLParser$RIFLState;
                    if (iArr != null) {
                        return iArr;
                    }
                    int[] iArr2 = new int[RIFLState.valuesCustom().length];
                    try {
                        iArr2[RIFLState.Assign.ordinal()] = 15;
                    } catch (NoSuchFieldError unused) {
                    }
                    try {
                        iArr2[RIFLState.Assignable.ordinal()] = 3;
                    } catch (NoSuchFieldError unused2) {
                    }
                    try {
                        iArr2[RIFLState.Category.ordinal()] = 9;
                    } catch (NoSuchFieldError unused3) {
                    }
                    try {
                        iArr2[RIFLState.Domain.ordinal()] = 11;
                    } catch (NoSuchFieldError unused4) {
                    }
                    try {
                        iArr2[RIFLState.DomainAssignments.ordinal()] = 14;
                    } catch (NoSuchFieldError unused5) {
                    }
                    try {
                        iArr2[RIFLState.Domains.ordinal()] = 10;
                    } catch (NoSuchFieldError unused6) {
                    }
                    try {
                        iArr2[RIFLState.Field.ordinal()] = 8;
                    } catch (NoSuchFieldError unused7) {
                    }
                    try {
                        iArr2[RIFLState.Flow.ordinal()] = 13;
                    } catch (NoSuchFieldError unused8) {
                    }
                    try {
                        iArr2[RIFLState.FlowRelation.ordinal()] = 12;
                    } catch (NoSuchFieldError unused9) {
                    }
                    try {
                        iArr2[RIFLState.InterfaceSpec.ordinal()] = 2;
                    } catch (NoSuchFieldError unused10) {
                    }
                    try {
                        iArr2[RIFLState.Parameter.ordinal()] = 7;
                    } catch (NoSuchFieldError unused11) {
                    }
                    try {
                        iArr2[RIFLState.ReturnValue.ordinal()] = 6;
                    } catch (NoSuchFieldError unused12) {
                    }
                    try {
                        iArr2[RIFLState.RiflSpec.ordinal()] = 1;
                    } catch (NoSuchFieldError unused13) {
                    }
                    try {
                        iArr2[RIFLState.Sink.ordinal()] = 5;
                    } catch (NoSuchFieldError unused14) {
                    }
                    try {
                        iArr2[RIFLState.Source.ordinal()] = 4;
                    } catch (NoSuchFieldError unused15) {
                    }
                    $SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLParser$RIFLState = iArr2;
                    return iArr2;
                }

                @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
                public void startDocument() throws SAXException {
                    super.startDocument();
                    this.stateStack.push(RIFLState.RiflSpec);
                }

                @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    super.startElement(uri, localName, qName, attributes);
                    RIFLState curState = this.stateStack.peek();
                    if (curState == RIFLState.RiflSpec && (qName.equalsIgnoreCase(RIFLConstants.INTERFACE_SPEC_TAG) || localName.equalsIgnoreCase(RIFLConstants.INTERFACE_SPEC_TAG))) {
                        this.stateStack.push(RIFLState.InterfaceSpec);
                    } else if (curState == RIFLState.InterfaceSpec && (qName.equalsIgnoreCase(RIFLConstants.ASSIGNABLE_TAG) || localName.equalsIgnoreCase(RIFLConstants.ASSIGNABLE_TAG))) {
                        this.stateStack.push(RIFLState.Assignable);
                        this.assignableHandle = attributes.getValue(RIFLConstants.HANDLE_ATTRIBUTE);
                    } else if ((curState == RIFLState.Source || curState == RIFLState.Sink) && (qName.equalsIgnoreCase("parameter") || localName.equalsIgnoreCase("parameter"))) {
                        this.stateStack.push(RIFLState.Parameter);
                        int parameterIdx = Integer.parseInt(attributes.getValue("parameter"));
                        String className = attributes.getValue("class");
                        String methodSig = attributes.getValue("method");
                        RIFLDocument.SourceSinkType type = getSourceSinkTypeFromState(curState);
                        RIFLDocument rIFLDocument = doc;
                        rIFLDocument.getClass();
                        RIFLDocument.SourceSinkSpec newSpec = new RIFLDocument.JavaParameterSpec(type, className, methodSig, parameterIdx);
                        if (this.sourceSinkSpec == null) {
                            this.sourceSinkSpec = newSpec;
                        } else if (this.sourceSinkSpec instanceof RIFLDocument.Category) {
                            ((RIFLDocument.Category) this.sourceSinkSpec).getElements().add(newSpec);
                        }
                    } else if ((curState == RIFLState.Source || curState == RIFLState.Sink) && (qName.equalsIgnoreCase(RIFLConstants.RETURN_VALUE_TAG) || localName.equalsIgnoreCase(RIFLConstants.RETURN_VALUE_TAG))) {
                        this.stateStack.push(RIFLState.Field);
                        String className2 = attributes.getValue("class");
                        String methodSig2 = attributes.getValue("method");
                        RIFLDocument.SourceSinkType type2 = getSourceSinkTypeFromState(curState);
                        RIFLDocument rIFLDocument2 = doc;
                        rIFLDocument2.getClass();
                        RIFLDocument.SourceSinkSpec newSpec2 = new RIFLDocument.JavaReturnValueSpec(type2, className2, methodSig2);
                        if (this.sourceSinkSpec == null) {
                            this.sourceSinkSpec = newSpec2;
                        } else if (this.sourceSinkSpec instanceof RIFLDocument.Category) {
                            ((RIFLDocument.Category) this.sourceSinkSpec).getElements().add(newSpec2);
                        }
                    } else if (curState == RIFLState.Assignable && (qName.equalsIgnoreCase("category") || localName.equalsIgnoreCase("category"))) {
                        this.stateStack.push(RIFLState.Category);
                    } else if (curState == RIFLState.Category && (qName.equalsIgnoreCase("category") || localName.equalsIgnoreCase("category"))) {
                        this.stateStack.push(RIFLState.Category);
                        RIFLDocument rIFLDocument3 = doc;
                        rIFLDocument3.getClass();
                        this.sourceSinkSpec = new RIFLDocument.Category(attributes.getValue("name"));
                    } else if (curState == RIFLState.Category && (qName.equalsIgnoreCase(RIFLConstants.SOURCE_TAG) || localName.equalsIgnoreCase(RIFLConstants.SOURCE_TAG))) {
                        this.stateStack.push(RIFLState.Source);
                    } else if (curState == RIFLState.Category && (qName.equalsIgnoreCase(RIFLConstants.SINK_TAG) || localName.equalsIgnoreCase(RIFLConstants.SINK_TAG))) {
                        this.stateStack.push(RIFLState.Sink);
                    } else if (curState == RIFLState.RiflSpec && (qName.equalsIgnoreCase(RIFLConstants.DOMAINS_TAG) || localName.equalsIgnoreCase(RIFLConstants.DOMAINS_TAG))) {
                        this.stateStack.push(RIFLState.Domains);
                    } else if (curState == RIFLState.Domains && (qName.equalsIgnoreCase("domain") || localName.equalsIgnoreCase("domain"))) {
                        this.stateStack.push(RIFLState.Domain);
                        List<RIFLDocument.DomainSpec> domains = doc.getDomains();
                        RIFLDocument rIFLDocument4 = doc;
                        rIFLDocument4.getClass();
                        domains.add(new RIFLDocument.DomainSpec(attributes.getValue("name")));
                    } else if (curState == RIFLState.RiflSpec && (qName.equalsIgnoreCase(RIFLConstants.FLOW_RELATION_TAG) || localName.equalsIgnoreCase(RIFLConstants.FLOW_RELATION_TAG))) {
                        this.stateStack.push(RIFLState.FlowRelation);
                    } else if (curState == RIFLState.FlowRelation && (qName.equalsIgnoreCase(RIFLConstants.FLOW_TAG) || localName.equalsIgnoreCase(RIFLConstants.FLOW_TAG))) {
                        this.stateStack.push(RIFLState.Flow);
                        String fromDomain = attributes.getValue("from");
                        String toDomain = attributes.getValue("to");
                        List<RIFLDocument.FlowPair> flowPolicy = doc.getFlowPolicy();
                        RIFLDocument rIFLDocument5 = doc;
                        rIFLDocument5.getClass();
                        flowPolicy.add(new RIFLDocument.FlowPair(doc.getDomainByName(fromDomain), doc.getDomainByName(toDomain)));
                    } else if (curState == RIFLState.RiflSpec && (qName.equalsIgnoreCase(RIFLConstants.DOMAIN_ASSIGNMENT_TAG) || localName.equalsIgnoreCase(RIFLConstants.DOMAIN_ASSIGNMENT_TAG))) {
                        this.stateStack.push(RIFLState.DomainAssignments);
                    } else if (curState == RIFLState.DomainAssignments) {
                        if (qName.equalsIgnoreCase(RIFLConstants.ASSIGN_TAG) || localName.equalsIgnoreCase(RIFLConstants.ASSIGN_TAG)) {
                            this.stateStack.push(RIFLState.Assign);
                            String assignableName = attributes.getValue(RIFLConstants.HANDLE_ATTRIBUTE);
                            String domainName = attributes.getValue("domain");
                            List<RIFLDocument.DomainAssignment> domainAssignment = doc.getDomainAssignment();
                            RIFLDocument rIFLDocument6 = doc;
                            rIFLDocument6.getClass();
                            domainAssignment.add(new RIFLDocument.DomainAssignment(doc.getInterfaceSpec().getElementByHandle(assignableName), doc.getDomainByName(domainName)));
                        }
                    }
                }

                private RIFLDocument.SourceSinkType getSourceSinkTypeFromState(RIFLState curState) {
                    switch ($SWITCH_TABLE$soot$jimple$infoflow$rifl$RIFLParser$RIFLState()[curState.ordinal()]) {
                        case 4:
                            return RIFLDocument.SourceSinkType.Source;
                        case 5:
                            return RIFLDocument.SourceSinkType.Sink;
                        default:
                            throw new RuntimeException("Invalid source/sink type: " + curState);
                    }
                }

                @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    super.endElement(uri, localName, qName);
                    RIFLState curState = this.stateStack.pop();
                    if (curState == RIFLState.Assignable) {
                        RIFLDocument rIFLDocument = doc;
                        rIFLDocument.getClass();
                        this.assignable = new RIFLDocument.Assignable(this.assignableHandle, this.sourceSinkSpec);
                        doc.getInterfaceSpec().getSourcesSinks().add(this.assignable);
                        this.sourceSinkSpec = null;
                    }
                }
            });
            return doc;
        } catch (ParserConfigurationException e) {
            return null;
        }
    }
}
