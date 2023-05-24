package soot.jimple.infoflow.results.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import soot.jimple.infoflow.results.InfoflowPerformanceData;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/InfoflowResultsReader.class */
public class InfoflowResultsReader {

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/InfoflowResultsReader$State.class */
    private enum State {
        init,
        dataFlowResults,
        results,
        result,
        fields,
        field,
        sources,
        source,
        sink,
        taintPath,
        pathElement,
        accessPath,
        performanceData,
        performanceEntry;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static State[] valuesCustom() {
            State[] valuesCustom = values();
            int length = valuesCustom.length;
            State[] stateArr = new State[length];
            System.arraycopy(valuesCustom, 0, stateArr, 0, length);
            return stateArr;
        }
    }

    public SerializedInfoflowResults readResults(String fileName) throws XMLStreamException, IOException {
        SerializedInfoflowResults results = new SerializedInfoflowResults();
        InfoflowPerformanceData perfData = null;
        XMLStreamReader reader = null;
        try {
            InputStream in = new FileInputStream(fileName);
            try {
                XMLInputFactory factory = XMLInputFactory.newInstance();
                factory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
                factory.setProperty("javax.xml.stream.supportDTD", false);
                reader = factory.createXMLStreamReader(in);
                String statement = null;
                String method = null;
                String methodSourceSinkDefinition = null;
                String apValue = null;
                String apValueType = null;
                boolean apTaintSubFields = false;
                List<String> apFields = new ArrayList<>();
                List<String> apTypes = new ArrayList<>();
                SerializedAccessPath ap = null;
                SerializedSinkInfo sink = null;
                List<SerializedPathElement> pathElements = new ArrayList<>();
                Stack<State> stateStack = new Stack<>();
                stateStack.push(State.init);
                while (reader.hasNext()) {
                    reader.next();
                    if (reader.hasName()) {
                        if (reader.getLocalName().equals(XmlConstants.Tags.root) && reader.isStartElement() && stateStack.peek() == State.init) {
                            stateStack.push(State.dataFlowResults);
                            results.setFileFormatVersion(int2Str(getAttributeByName(reader, XmlConstants.Attributes.fileFormatVersion)));
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.results) && reader.isStartElement() && stateStack.peek() == State.dataFlowResults) {
                            stateStack.push(State.results);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.result) && reader.isStartElement() && stateStack.peek() == State.results) {
                            stateStack.push(State.result);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.sink) && reader.isStartElement() && stateStack.peek() == State.result) {
                            stateStack.push(State.sink);
                            statement = getAttributeByName(reader, XmlConstants.Attributes.statement);
                            method = getAttributeByName(reader, XmlConstants.Attributes.method);
                            methodSourceSinkDefinition = getAttributeByName(reader, XmlConstants.Attributes.methodSourceSinkDefinition);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.accessPath) && reader.isStartElement()) {
                            stateStack.push(State.accessPath);
                            apValue = getAttributeByName(reader, XmlConstants.Attributes.value);
                            apValueType = getAttributeByName(reader, XmlConstants.Attributes.type);
                            apTaintSubFields = getAttributeByName(reader, XmlConstants.Attributes.taintSubFields).equals("true");
                            apFields.clear();
                            apTypes.clear();
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.fields) && reader.isStartElement() && stateStack.peek() == State.accessPath) {
                            stateStack.push(State.fields);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.field) && reader.isStartElement() && stateStack.peek() == State.fields) {
                            stateStack.push(State.field);
                            String value = getAttributeByName(reader, XmlConstants.Attributes.value);
                            String type = getAttributeByName(reader, XmlConstants.Attributes.type);
                            if (value != null && !value.isEmpty() && type != null && !type.isEmpty()) {
                                apFields.add(value);
                                apTypes.add(value);
                            }
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.sources) && reader.isStartElement() && stateStack.peek() == State.result) {
                            stateStack.push(State.sources);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.source) && reader.isStartElement() && stateStack.peek() == State.sources) {
                            stateStack.push(State.source);
                            statement = getAttributeByName(reader, XmlConstants.Attributes.statement);
                            method = getAttributeByName(reader, XmlConstants.Attributes.method);
                            methodSourceSinkDefinition = getAttributeByName(reader, XmlConstants.Attributes.methodSourceSinkDefinition);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.taintPath) && reader.isStartElement() && stateStack.peek() == State.source) {
                            stateStack.push(State.taintPath);
                            pathElements.clear();
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.pathElement) && reader.isStartElement() && stateStack.peek() == State.source) {
                            stateStack.push(State.taintPath);
                            statement = getAttributeByName(reader, XmlConstants.Attributes.statement);
                            method = getAttributeByName(reader, XmlConstants.Attributes.method);
                            methodSourceSinkDefinition = getAttributeByName(reader, XmlConstants.Attributes.methodSourceSinkDefinition);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.performanceData) && reader.isStartElement() && stateStack.peek() == State.dataFlowResults) {
                            stateStack.push(State.performanceData);
                        } else if (reader.getLocalName().equals(XmlConstants.Tags.performanceEntry) && reader.isStartElement() && stateStack.peek() == State.performanceData) {
                            stateStack.push(State.performanceEntry);
                            if (perfData == null) {
                                perfData = results.getOrCreatePerformanceData();
                            }
                            String perfName = getAttributeByName(reader, "Name");
                            String perfValue = getAttributeByName(reader, XmlConstants.Attributes.value);
                            switch (perfName.hashCode()) {
                                case -1865765643:
                                    if (!perfName.equals(XmlConstants.Values.PERF_TAINT_PROPAGATION_SECONDS)) {
                                        break;
                                    } else {
                                        perfData.setTaintPropagationSeconds(Integer.parseInt(perfValue));
                                        break;
                                    }
                                case -308709429:
                                    if (!perfName.equals(XmlConstants.Values.PERF_TOTAL_RUNTIME_SECONDS)) {
                                        break;
                                    } else {
                                        perfData.setTotalRuntimeSeconds(Integer.parseInt(perfValue));
                                        break;
                                    }
                                case -191487978:
                                    if (!perfName.equals(XmlConstants.Values.PERF_PATH_RECONSTRUCTION_SECONDS)) {
                                        break;
                                    } else {
                                        perfData.setPathReconstructionSeconds(Integer.parseInt(perfValue));
                                        break;
                                    }
                                case 293470398:
                                    if (!perfName.equals(XmlConstants.Values.PERF_CALLGRAPH_SECONDS)) {
                                        break;
                                    } else {
                                        perfData.setCallgraphConstructionSeconds(Integer.parseInt(perfValue));
                                        break;
                                    }
                                case 586401308:
                                    if (!perfName.equals(XmlConstants.Values.PERF_SINK_COUNT)) {
                                        break;
                                    } else {
                                        perfData.setSinkCount(Integer.parseInt(perfValue));
                                        break;
                                    }
                                case 990193398:
                                    if (!perfName.equals(XmlConstants.Values.PERF_MAX_MEMORY_CONSUMPTION)) {
                                        break;
                                    } else {
                                        perfData.setMaxMemoryConsumption(Integer.parseInt(perfValue));
                                        break;
                                    }
                                case 1557232020:
                                    if (!perfName.equals(XmlConstants.Values.PERF_SOURCE_COUNT)) {
                                        break;
                                    } else {
                                        perfData.setSourceCount(Integer.parseInt(perfValue));
                                        break;
                                    }
                            }
                        } else if (reader.isEndElement()) {
                            stateStack.pop();
                            if (reader.getLocalName().equals(XmlConstants.Tags.accessPath)) {
                                ap = new SerializedAccessPath(apValue, apValueType, apTaintSubFields, (String[]) apFields.toArray(new String[apFields.size()]), (String[]) apTypes.toArray(new String[apTypes.size()]));
                            } else if (reader.getLocalName().equals(XmlConstants.Tags.sink)) {
                                sink = new SerializedSinkInfo(ap, statement, method, methodSourceSinkDefinition);
                            } else if (reader.getLocalName().equals(XmlConstants.Tags.source)) {
                                SerializedSourceInfo source = new SerializedSourceInfo(ap, statement, method, pathElements, methodSourceSinkDefinition);
                                results.addResult(source, sink);
                            } else if (reader.getLocalName().equals(XmlConstants.Tags.pathElement)) {
                                pathElements.add(new SerializedPathElement(ap, statement, method));
                            }
                        }
                    }
                }
                return results;
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private int int2Str(String value) {
        if (value == null || value.isEmpty()) {
            return -1;
        }
        return Integer.valueOf(value).intValue();
    }

    private String getAttributeByName(XMLStreamReader reader, String id) {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if (reader.getAttributeLocalName(i).equals(id)) {
                return reader.getAttributeValue(i);
            }
        }
        return "";
    }
}
