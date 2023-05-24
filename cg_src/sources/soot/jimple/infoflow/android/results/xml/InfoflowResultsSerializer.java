package soot.jimple.infoflow.android.results.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.data.AndroidMethod;
import soot.jimple.infoflow.android.data.CategoryDefinition;
import soot.jimple.infoflow.android.results.xml.XmlConstants;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/results/xml/InfoflowResultsSerializer.class */
public class InfoflowResultsSerializer extends soot.jimple.infoflow.results.xml.InfoflowResultsSerializer {
    public InfoflowResultsSerializer(InfoflowConfiguration config) {
        super(config);
    }

    public InfoflowResultsSerializer(IInfoflowCFG cfg, InfoflowConfiguration config) {
        super(cfg, config);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.results.xml.InfoflowResultsSerializer
    public void writeAdditionalSourceInfo(ResultSourceInfo source, XMLStreamWriter writer) throws XMLStreamException {
        super.writeAdditionalSourceInfo(source, writer);
        if (source.getDefinition() != null && (source.getDefinition() instanceof MethodSourceSinkDefinition)) {
            MethodSourceSinkDefinition mssd = (MethodSourceSinkDefinition) source.getDefinition();
            if ((mssd.getMethod() instanceof AndroidMethod) && mssd.getCategory() != null) {
                writer.writeAttribute(XmlConstants.Attributes.systemCategory, mssd.getCategory().toString());
                if (mssd.getCategory() instanceof CategoryDefinition) {
                    CategoryDefinition catDef = (CategoryDefinition) mssd.getCategory();
                    String customCat = catDef.getCustomCategory();
                    if (customCat != null && !customCat.isEmpty()) {
                        writer.writeAttribute(XmlConstants.Attributes.userCategory, customCat);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.results.xml.InfoflowResultsSerializer
    public void writeAdditionalSinkInfo(ResultSinkInfo sink, XMLStreamWriter writer) throws XMLStreamException {
        super.writeAdditionalSinkInfo(sink, writer);
        if (sink.getDefinition() != null && (sink.getDefinition() instanceof MethodSourceSinkDefinition)) {
            MethodSourceSinkDefinition mssd = (MethodSourceSinkDefinition) sink.getDefinition();
            if ((mssd.getMethod() instanceof AndroidMethod) && mssd.getCategory() != null) {
                writer.writeAttribute(XmlConstants.Attributes.systemCategory, mssd.getCategory().toString());
                if (mssd.getCategory() instanceof CategoryDefinition) {
                    CategoryDefinition catDef = (CategoryDefinition) mssd.getCategory();
                    String customCat = catDef.getCustomCategory();
                    if (customCat != null && !customCat.isEmpty()) {
                        writer.writeAttribute(XmlConstants.Attributes.userCategory, customCat);
                    }
                }
            }
        }
    }
}
