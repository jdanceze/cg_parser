package soot.jimple.infoflow.android.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
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
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.data.CategoryDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/config/XMLConfigurationWriter.class */
public class XMLConfigurationWriter {
    private final InfoflowAndroidConfiguration config;

    public XMLConfigurationWriter(InfoflowAndroidConfiguration config) {
        this.config = config;
    }

    public String write() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(XMLConstants.TAG_ROOT_ELEMENT);
            document.appendChild(rootElement);
            writeAnalysisFileConfig(document, rootElement);
            writeSourceSinkConfig(document, rootElement);
            writeAndroidConfig(document, rootElement);
            writeIccConfig(document, rootElement);
            writeDataFlowConfig(document, rootElement);
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

    private void writeDataFlowConfig(Document document, Element parentElement) {
        Element dataFlowConfigTag = document.createElement(XMLConstants.TAG_DATA_FLOW_CONFIGURATION);
        parentElement.appendChild(dataFlowConfigTag);
        InfoflowConfiguration.PathConfiguration pathConfig = this.config.getPathConfiguration();
        InfoflowConfiguration.SolverConfiguration solverConfig = this.config.getSolverConfiguration();
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_MAX_JOIN_POINT_ABSTRACTIONS, Integer.toString(solverConfig.getMaxJoinPointAbstractions()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_MAX_CALLEES_PER_CALL_SITE, Integer.toString(solverConfig.getMaxCalleesPerCallSite()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_IMPLICIT_FLOW_MODE, this.config.getImplicitFlowMode().toString());
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_STATIC_FIELD_TRACKING_MODE, this.config.getStaticFieldTrackingMode().toString());
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_ENABLE_EXCEPTIONS, Boolean.toString(this.config.getEnableExceptionTracking()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_ENABLE_ARRAYS, Boolean.toString(this.config.getEnableArrayTracking()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_ENABLE_REFLECTION, Boolean.toString(this.config.getEnableReflection()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_FLOW_SENSITIVE_ALIASING, Boolean.toString(this.config.getFlowSensitiveAliasing()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_LOG_SOURCES_AND_SINKS, Boolean.toString(this.config.getLogSourcesAndSinks()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_ENABLE_ARRAY_SIZE_TAINTING, Boolean.toString(this.config.getEnableArraySizeTainting()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_PATH_RECONSTRUCTION_MODE, pathConfig.getPathReconstructionMode().toString());
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_PATH_AGNOSTIC_RESULTS, Boolean.toString(this.config.getPathAgnosticResults()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_MAX_CALLSTACK_SIZE, Integer.toString(pathConfig.getMaxCallStackSize()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_MAX_PATH_LENGTH, Integer.toString(pathConfig.getMaxPathLength()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_MAX_PATHS_PER_ABSTRACTION, Integer.toString(pathConfig.getMaxPathsPerAbstraction()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_DATA_FLOW_TIMEOUT, Long.toString(this.config.getDataFlowTimeout()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_PATH_RECONSTRUCTION_TIMEOUT, Long.toString(pathConfig.getPathReconstructionTimeout()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_PATH_RECONSTRUCTION_BATCH_SIZE, Integer.toString(pathConfig.getPathReconstructionBatchSize()));
        appendSimpleTag(document, dataFlowConfigTag, XMLConstants.TAG_WRITE_OUTPUT_FILES, Boolean.toString(this.config.getWriteOutputFiles()));
    }

    private void writeIccConfig(Document document, Element parentElement) {
        Element iccConfigTag = document.createElement(XMLConstants.TAG_ICC_CONFIGURATION);
        parentElement.appendChild(iccConfigTag);
        InfoflowAndroidConfiguration.IccConfiguration iccConfig = this.config.getIccConfig();
        appendSimpleTag(document, iccConfigTag, XMLConstants.TAG_ENABLE_ICC_TRACKING, Boolean.toString(iccConfig.isIccEnabled()));
        appendSimpleTag(document, iccConfigTag, XMLConstants.TAG_MODEL_FILE, iccConfig.getIccModel());
        appendSimpleTag(document, iccConfigTag, XMLConstants.TAG_PURIFY_RESULTS, Boolean.toString(iccConfig.isIccResultsPurifyEnabled()));
    }

    private void writeAndroidConfig(Document document, Element parentElement) {
        Element androidConfigTag = document.createElement(XMLConstants.TAG_ANDROID_CONFIGURATION);
        parentElement.appendChild(androidConfigTag);
        InfoflowAndroidConfiguration.CallbackConfiguration callbackConfig = this.config.getCallbackConfig();
        InfoflowAndroidConfiguration.SourceSinkConfiguration sourceSinkConfig = this.config.getSourceSinkConfig();
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_ENABLE_CALLBACKS, Boolean.toString(callbackConfig.getEnableCallbacks()));
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_FILTER_THREAD_CALLBACKS, Boolean.toString(callbackConfig.getFilterThreadCallbacks()));
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_MAX_CALLBACKS_PER_COMPONENT, Integer.toString(callbackConfig.getMaxCallbacksPerComponent()));
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_MAX_CALLBACK_DEPTH, Integer.toString(callbackConfig.getMaxAnalysisCallbackDepth()));
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_LAYOUT_MATCHING_MODE, sourceSinkConfig.getLayoutMatchingMode().toString());
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_MERGE_DEX_FILES, Boolean.toString(this.config.getMergeDexFiles()));
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_CALLBACK_SOURCE_MODE, sourceSinkConfig.getCallbackSourceMode().toString());
        appendSimpleTag(document, androidConfigTag, XMLConstants.TAG_CALLBACK_ANALYSIS_TIMEOUT, Integer.toString(callbackConfig.getCallbackAnalysisTimeout()));
    }

    private void writeSourceSinkConfig(Document document, Element parentElement) {
        Element sourceSpecTag = document.createElement(XMLConstants.TAG_SOURCE_SPEC);
        parentElement.appendChild(sourceSpecTag);
        sourceSpecTag.setAttribute(XMLConstants.ATTR_DEFAULT_MODE, this.config.getSourceSinkConfig().getSourceFilterMode().toString());
        writeCategoryConfig(document, sourceSpecTag, this.config.getSourceSinkConfig().getSourceCategoriesAndModes());
        Element sinkSpecTag = document.createElement(XMLConstants.TAG_SINK_SPEC);
        parentElement.appendChild(sinkSpecTag);
        sinkSpecTag.setAttribute(XMLConstants.ATTR_DEFAULT_MODE, this.config.getSourceSinkConfig().getSinkFilterMode().toString());
        writeCategoryConfig(document, sinkSpecTag, this.config.getSourceSinkConfig().getSinkCategoriesAndModes());
    }

    private void writeCategoryConfig(Document document, Element parentElement, Map<CategoryDefinition, InfoflowConfiguration.CategoryMode> categorySpec) {
        for (CategoryDefinition def : categorySpec.keySet()) {
            if (this.config.getSourceSinkConfig().getSourceFilterMode() != InfoflowConfiguration.SourceSinkFilterMode.UseAllButExcluded || categorySpec.get(def) != InfoflowConfiguration.CategoryMode.Include) {
                if (this.config.getSourceSinkConfig().getSourceFilterMode() != InfoflowConfiguration.SourceSinkFilterMode.UseOnlyIncluded || categorySpec.get(def) != InfoflowConfiguration.CategoryMode.Exclude) {
                    Element categoryTag = document.createElement("category");
                    parentElement.appendChild(categoryTag);
                    categoryTag.setAttribute("id", def.getSystemCategory().toString());
                    if (def.getCustomCategory() != null && !def.getCustomCategory().isEmpty()) {
                        categoryTag.setAttribute("customId", def.getCustomCategory());
                    }
                    categoryTag.setAttribute("mode", categorySpec.get(def).toString());
                }
            }
        }
    }

    private void writeAnalysisFileConfig(Document document, Element parentElement) {
        Element inputFileTag = document.createElement(XMLConstants.TAG_INPUT_FILES);
        parentElement.appendChild(inputFileTag);
        appendSimpleTag(document, inputFileTag, XMLConstants.TAG_TARGET_APK_FILE, this.config.getAnalysisFileConfig().getTargetAPKFile());
        appendSimpleTag(document, inputFileTag, XMLConstants.TAG_SOURCE_SINK_FILE, this.config.getAnalysisFileConfig().getSourceSinkFile());
        appendSimpleTag(document, inputFileTag, XMLConstants.TAG_ANDROID_PLATFORM_DIR, this.config.getAnalysisFileConfig().getAndroidPlatformDir());
        appendSimpleTag(document, inputFileTag, XMLConstants.TAG_OUTPUT_FILE, this.config.getAnalysisFileConfig().getOutputFile());
    }

    private void appendSimpleTag(Document document, Element parentElement, String tagName, String contents) {
        Element newElement = document.createElement(tagName);
        parentElement.appendChild(newElement);
        newElement.setTextContent(contents);
    }

    public void write(String fileName) throws IOException {
        String xmlData = write();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(fileName);
            pw.write(xmlData);
            if (pw != null) {
                pw.close();
            }
        } catch (Throwable th) {
            if (pw != null) {
                pw.close();
            }
            throw th;
        }
    }
}
