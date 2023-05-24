package soot.jimple.infoflow.android.resources.controls;

import android.content.ContentResolver;
import java.util.Collections;
import java.util.Map;
import soot.JavaBasicTypes;
import soot.SootClass;
import soot.jimple.infoflow.sourcesSinks.definitions.AccessPathTuple;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.MethodSourceSinkDefinition;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/controls/GenericLayoutControl.class */
public class GenericLayoutControl extends AndroidLayoutControl {
    protected static final ISourceSinkDefinition UI_SOURCE_DEF = new MethodSourceSinkDefinition(null, null, Collections.singleton(AccessPathTuple.fromPathElements(Collections.singletonList(ContentResolver.SCHEME_CONTENT), Collections.singletonList(JavaBasicTypes.JAVA_LANG_OBJECT), SourceSinkType.Source)), MethodSourceSinkDefinition.CallType.MethodCall);

    public GenericLayoutControl(int id, SootClass viewClass, Map<String, Object> additionalAttributes) {
        super(id, viewClass, additionalAttributes);
    }

    public GenericLayoutControl(int id, SootClass viewClass) {
        super(id, viewClass);
    }

    public GenericLayoutControl(SootClass viewClass) {
        super(viewClass);
    }

    @Override // soot.jimple.infoflow.resources.controls.LayoutControl
    public ISourceSinkDefinition getSourceDefinition() {
        return UI_SOURCE_DEF;
    }
}
