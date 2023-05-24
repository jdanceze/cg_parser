package soot.jimple.infoflow.rifl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.xml.sax.SAXException;
import soot.coffi.Instruction;
import soot.jimple.infoflow.data.SootMethodAndClass;
import soot.jimple.infoflow.rifl.RIFLDocument;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.util.SootMethodRepresentationParser;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLSourceSinkDefinitionProvider.class */
public class RIFLSourceSinkDefinitionProvider implements ISourceSinkDefinitionProvider {
    private final Set<ISourceSinkDefinition> sources = new HashSet();
    private final Set<ISourceSinkDefinition> sinks = new HashSet();
    private Set<ISourceSinkDefinition> allMethods = null;
    private String lastCategory = null;

    public RIFLSourceSinkDefinitionProvider(String file) throws SAXException, IOException {
        RIFLParser parser = new RIFLParser();
        RIFLDocument doc = parser.parseRIFL(new File(file));
        for (RIFLDocument.Assignable assign : doc.getInterfaceSpec().getSourcesSinks()) {
            parseRawDefinition(assign.getElement());
        }
    }

    private void parseRawDefinition(RIFLDocument.SourceSinkSpec element) {
        if (element.getType() == RIFLDocument.SourceSinkType.Source) {
            ISourceSinkDefinition sourceSinkDefinition = parseDefinition(element, RIFLDocument.SourceSinkType.Source);
            final String permanentCategory = this.lastCategory;
            sourceSinkDefinition.setCategory(new ISourceSinkCategory() { // from class: soot.jimple.infoflow.rifl.RIFLSourceSinkDefinitionProvider.1
                @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
                public String getHumanReadableDescription() {
                    return permanentCategory;
                }

                @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
                public String getID() {
                    return permanentCategory;
                }
            });
            this.sources.add(sourceSinkDefinition);
        } else if (element.getType() == RIFLDocument.SourceSinkType.Sink) {
            ISourceSinkDefinition sourceSinkDefinition2 = parseDefinition(element, RIFLDocument.SourceSinkType.Sink);
            final String permanentCategory2 = this.lastCategory;
            sourceSinkDefinition2.setCategory(new ISourceSinkCategory() { // from class: soot.jimple.infoflow.rifl.RIFLSourceSinkDefinitionProvider.2
                @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
                public String getHumanReadableDescription() {
                    return permanentCategory2;
                }

                @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
                public String getID() {
                    return permanentCategory2.toUpperCase(Locale.US);
                }
            });
            this.sinks.add(sourceSinkDefinition2);
        } else if (element.getType() == RIFLDocument.SourceSinkType.Category) {
            RIFLDocument.Category cat = (RIFLDocument.Category) element;
            this.lastCategory = cat.getName();
            String[] s = this.lastCategory.split("_");
            this.lastCategory = "";
            for (int i = 0; i < s.length - 1; i++) {
                if (i != 0) {
                    this.lastCategory = String.valueOf(this.lastCategory) + Instruction.argsep;
                }
                this.lastCategory = String.valueOf(this.lastCategory) + s[i];
            }
            for (RIFLDocument.SourceSinkSpec spec : cat.getElements()) {
                parseRawDefinition(spec);
            }
        } else {
            throw new RuntimeException("Invalid element type");
        }
    }

    private ISourceSinkDefinition parseDefinition(RIFLDocument.SourceSinkSpec element, RIFLDocument.SourceSinkType sourceSinkType) {
        if (element instanceof RIFLDocument.JavaMethodSourceSinkSpec) {
            RIFLDocument.JavaMethodSourceSinkSpec javaElement = (RIFLDocument.JavaMethodSourceSinkSpec) element;
            String methodName = SootMethodRepresentationParser.v().getMethodNameFromSubSignature(javaElement.getHalfSignature());
            String[] parameters = SootMethodRepresentationParser.v().getParameterTypesFromSubSignature(javaElement.getHalfSignature());
            List<String> parameterTypes = new ArrayList<>();
            if (parameters != null) {
                for (String p : parameters) {
                    parameterTypes.add(p);
                }
            }
            if (element instanceof RIFLDocument.JavaParameterSpec) {
                RIFLDocument.JavaParameterSpec javaParameterSpec = (RIFLDocument.JavaParameterSpec) element;
                Set<AccessPathTuple> returnValue = new HashSet<>();
                SootMethodAndClass am = new SootMethodAndClass(methodName, javaElement.getClassName(), "", parameterTypes);
                return new MethodSourceSinkDefinition(am, null, null, returnValue, MethodSourceSinkDefinition.CallType.MethodCall);
            } else if (element instanceof RIFLDocument.JavaReturnValueSpec) {
                AccessPathTuple.fromPathElements((String[]) null, (String[]) null, sourceSinkType == RIFLDocument.SourceSinkType.Source, sourceSinkType == RIFLDocument.SourceSinkType.Sink);
                SootMethodAndClass am2 = new SootMethodAndClass(methodName, javaElement.getClassName(), "", parameterTypes);
                return new MethodSourceSinkDefinition(am2, null, null, null, MethodSourceSinkDefinition.CallType.MethodCall);
            }
        } else {
            boolean z = element instanceof RIFLDocument.JavaFieldSpec;
        }
        throw new RuntimeException("Invalid source/sink specification element");
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSources() {
        return this.sources;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getSinks() {
        return this.sinks;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinitionProvider
    public Set<ISourceSinkDefinition> getAllMethods() {
        if (this.allMethods == null) {
            this.allMethods = new HashSet(this.sources.size() + this.sinks.size());
            this.allMethods.addAll(this.sources);
            this.allMethods.addAll(this.sinks);
        }
        return this.allMethods;
    }
}
