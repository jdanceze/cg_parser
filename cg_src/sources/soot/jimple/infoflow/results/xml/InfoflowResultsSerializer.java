package soot.jimple.infoflow.results.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.data.AccessPathFragment;
import soot.jimple.infoflow.results.InfoflowPerformanceData;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.results.xml.XmlConstants;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/InfoflowResultsSerializer.class */
public class InfoflowResultsSerializer {
    public static final int FILE_FORMAT_VERSION = 102;
    protected boolean serializeTaintPath;
    protected IInfoflowCFG icfg;
    protected InfoflowConfiguration config;
    protected long startTime;

    public InfoflowResultsSerializer(InfoflowConfiguration config) {
        this(null, config);
    }

    public InfoflowResultsSerializer(IInfoflowCFG cfg, InfoflowConfiguration config) {
        this.serializeTaintPath = true;
        this.startTime = 0L;
        this.icfg = cfg;
        this.config = config;
    }

    public void serialize(InfoflowResults results, String fileName) throws XMLStreamException, IOException {
        this.startTime = System.currentTimeMillis();
        Throwable th = null;
        try {
            OutputStream out = new FileOutputStream(fileName);
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(out, "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement(XmlConstants.Tags.root);
            writer.writeAttribute(XmlConstants.Attributes.fileFormatVersion, "102");
            writer.writeAttribute(XmlConstants.Attributes.terminationState, terminationStateToString(results.getTerminationState()));
            if (results != null && !results.isEmpty()) {
                writer.writeStartElement(XmlConstants.Tags.results);
                writeDataFlows(results, writer);
                writer.writeEndElement();
            }
            InfoflowPerformanceData performanceData = results.getPerformanceData();
            if (performanceData != null && !performanceData.isEmpty()) {
                writer.writeStartElement(XmlConstants.Tags.performanceData);
                writePerformanceData(performanceData, writer);
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.close();
            if (out != null) {
                out.close();
            }
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private String terminationStateToString(int terminationState) {
        switch (terminationState) {
            case 0:
                return "Success";
            case 1:
                return "DataFlowTimeout";
            case 2:
                return "DataFlowOutOfMemory";
            case 3:
            case 5:
            case 6:
            case 7:
            default:
                return "Unknown";
            case 4:
                return "PathReconstructionTimeout";
            case 8:
                return "PathReconstructionOfMemory";
        }
    }

    private void writePerformanceData(InfoflowPerformanceData performanceData, XMLStreamWriter writer) throws XMLStreamException {
        writePerformanceEntry(XmlConstants.Values.PERF_CALLGRAPH_SECONDS, performanceData.getCallgraphConstructionSeconds(), writer);
        writePerformanceEntry(XmlConstants.Values.PERF_TAINT_PROPAGATION_SECONDS, performanceData.getTaintPropagationSeconds(), writer);
        writePerformanceEntry(XmlConstants.Values.PERF_PATH_RECONSTRUCTION_SECONDS, performanceData.getPathReconstructionSeconds(), writer);
        writePerformanceEntry(XmlConstants.Values.PERF_TOTAL_RUNTIME_SECONDS, performanceData.getTotalRuntimeSeconds(), writer);
        writePerformanceEntry(XmlConstants.Values.PERF_MAX_MEMORY_CONSUMPTION, performanceData.getMaxMemoryConsumption(), writer);
        writePerformanceEntry(XmlConstants.Values.PERF_SOURCE_COUNT, performanceData.getSourceCount(), writer);
        writePerformanceEntry(XmlConstants.Values.PERF_SINK_COUNT, performanceData.getSinkCount(), writer);
    }

    private void writePerformanceEntry(String entryName, int entryValue, XMLStreamWriter writer) throws XMLStreamException {
        if (entryValue > 0) {
            writer.writeStartElement(XmlConstants.Tags.performanceEntry);
            writer.writeAttribute("Name", entryName);
            writer.writeAttribute(XmlConstants.Attributes.value, new StringBuilder(String.valueOf(entryValue)).toString());
            writer.writeEndElement();
        }
    }

    protected void writeDataFlows(InfoflowResults results, XMLStreamWriter writer) throws XMLStreamException {
        for (ResultSinkInfo sink : results.getResults().keySet()) {
            writer.writeStartElement(XmlConstants.Tags.result);
            writeSinkInfo(sink, writer);
            writer.writeStartElement(XmlConstants.Tags.sources);
            for (ResultSourceInfo src : results.getResults().get(sink)) {
                writeSourceInfo(src, writer);
            }
            writer.writeEndElement();
            writer.writeEndElement();
        }
    }

    private void writeSourceInfo(ResultSourceInfo source, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(XmlConstants.Tags.source);
        writer.writeAttribute(XmlConstants.Attributes.statement, source.getStmt().toString());
        if (this.config.getEnableLineNumbers()) {
            writer.writeAttribute(XmlConstants.Attributes.linenumber, String.valueOf(source.getStmt().getJavaSourceStartLineNumber()));
        }
        if (source.getDefinition().getCategory() != null) {
            writer.writeAttribute(XmlConstants.Attributes.category, source.getDefinition().getCategory().getHumanReadableDescription());
        }
        if (this.icfg != null) {
            writer.writeAttribute(XmlConstants.Attributes.method, this.icfg.getMethodOf(source.getStmt()).getSignature());
        }
        ISourceSinkDefinition def = source.getDefinition();
        if (def instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition ms = (MethodSourceSinkDefinition) def;
            writer.writeAttribute(XmlConstants.Attributes.methodSourceSinkDefinition, ms.getMethod().getSignature());
        }
        writeAdditionalSourceInfo(source, writer);
        writeAccessPath(source.getAccessPath(), writer);
        if (this.serializeTaintPath && source.getPath() != null) {
            writer.writeStartElement(XmlConstants.Tags.taintPath);
            for (int i = 0; i < source.getPath().length; i++) {
                writer.writeStartElement(XmlConstants.Tags.pathElement);
                Stmt curStmt = source.getPath()[i];
                writer.writeAttribute(XmlConstants.Attributes.statement, curStmt.toString());
                if (this.icfg != null) {
                    writer.writeAttribute(XmlConstants.Attributes.method, this.icfg.getMethodOf(curStmt).getSignature());
                }
                AccessPath curAP = source.getPathAccessPaths()[i];
                writeAccessPath(curAP, writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAdditionalSourceInfo(ResultSourceInfo source, XMLStreamWriter writer) throws XMLStreamException {
    }

    private void writeSinkInfo(ResultSinkInfo sink, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(XmlConstants.Tags.sink);
        writer.writeAttribute(XmlConstants.Attributes.statement, sink.getStmt().toString());
        if (this.config.getEnableLineNumbers()) {
            writer.writeAttribute(XmlConstants.Attributes.linenumber, String.valueOf(sink.getStmt().getJavaSourceStartLineNumber()));
        }
        if (sink.getDefinition().getCategory() != null) {
            writer.writeAttribute(XmlConstants.Attributes.category, sink.getDefinition().getCategory().getHumanReadableDescription());
        }
        if (this.icfg != null) {
            writer.writeAttribute(XmlConstants.Attributes.method, this.icfg.getMethodOf(sink.getStmt()).getSignature());
        }
        ISourceSinkDefinition def = sink.getDefinition();
        if (def instanceof MethodSourceSinkDefinition) {
            MethodSourceSinkDefinition ms = (MethodSourceSinkDefinition) def;
            writer.writeAttribute(XmlConstants.Attributes.methodSourceSinkDefinition, ms.getMethod().getSignature());
        }
        writeAdditionalSinkInfo(sink, writer);
        writeAccessPath(sink.getAccessPath(), writer);
        writer.writeEndElement();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAdditionalSinkInfo(ResultSinkInfo sink, XMLStreamWriter writer) throws XMLStreamException {
    }

    protected void writeAccessPath(AccessPath accessPath, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(XmlConstants.Tags.accessPath);
        if (accessPath.getPlainValue() != null) {
            writer.writeAttribute(XmlConstants.Attributes.value, accessPath.getPlainValue().toString());
        }
        if (accessPath.getBaseType() != null) {
            writer.writeAttribute(XmlConstants.Attributes.type, accessPath.getBaseType().toString());
        }
        writer.writeAttribute(XmlConstants.Attributes.taintSubFields, accessPath.getTaintSubFields() ? "true" : "false");
        if (accessPath.getFragmentCount() > 0) {
            writer.writeStartElement(XmlConstants.Tags.fields);
            for (int i = 0; i < accessPath.getFragmentCount(); i++) {
                writer.writeStartElement(XmlConstants.Tags.field);
                AccessPathFragment fragment = accessPath.getFragments()[i];
                writer.writeAttribute(XmlConstants.Attributes.value, fragment.getField().toString());
                writer.writeAttribute(XmlConstants.Attributes.type, fragment.getFieldType().toString());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public void setSerializeTaintPath(boolean serialize) {
        this.serializeTaintPath = serialize;
    }
}
