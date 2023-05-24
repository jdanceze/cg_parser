package soot.jimple.infoflow.resources.controls;

import android.content.ContentResolver;
import java.util.Collections;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/resources/controls/SwingTextField.class */
public class SwingTextField extends JavaSwingLayoutControl {
    protected static final ISourceSinkDefinition UI_SOURCE_DEF = new MethodSourceSinkDefinition(null, null, Collections.singleton(AccessPathTuple.fromPathElements("javax.swing.JTextField", Collections.singletonList(ContentResolver.SCHEME_CONTENT), Collections.singletonList("java.lang.String"), SourceSinkType.Source)), MethodSourceSinkDefinition.CallType.MethodCall);

    static {
        UI_SOURCE_DEF.setCategory(new ISourceSinkCategory() { // from class: soot.jimple.infoflow.resources.controls.SwingTextField.1
            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
            public String getHumanReadableDescription() {
                return "Text Input";
            }

            public String toString() {
                return "Text Input";
            }

            @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
            public String getID() {
                return "TEXTINPUT";
            }
        });
    }

    @Override // soot.jimple.infoflow.resources.controls.LayoutControl
    public ISourceSinkDefinition getSourceDefinition() {
        return UI_SOURCE_DEF;
    }
}
