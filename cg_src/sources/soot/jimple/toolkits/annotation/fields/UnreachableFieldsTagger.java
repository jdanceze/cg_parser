package soot.jimple.toolkits.annotation.fields;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.tagkit.ColorTag;
import soot.tagkit.StringTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/fields/UnreachableFieldsTagger.class */
public class UnreachableFieldsTagger extends SceneTransformer {
    public UnreachableFieldsTagger(Singletons.Global g) {
    }

    public static UnreachableFieldsTagger v() {
        return G.v().soot_jimple_toolkits_annotation_fields_UnreachableFieldsTagger();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map options) {
        ArrayList<SootField> fieldList = new ArrayList<>();
        for (SootClass appClass : Scene.v().getApplicationClasses()) {
            for (SootField field : appClass.getFields()) {
                fieldList.add(field);
            }
        }
        for (SootClass appClass2 : Scene.v().getApplicationClasses()) {
            for (SootMethod sm : appClass2.getMethods()) {
                if (sm.hasActiveBody() && Scene.v().getReachableMethods().contains(sm)) {
                    Body b = sm.getActiveBody();
                    for (ValueBox vBox : b.getUseBoxes()) {
                        Value v = vBox.getValue();
                        if (v instanceof FieldRef) {
                            FieldRef fieldRef = (FieldRef) v;
                            SootField f = fieldRef.getField();
                            if (fieldList.contains(f)) {
                                int index = fieldList.indexOf(f);
                                fieldList.remove(index);
                            }
                        }
                    }
                }
            }
        }
        Iterator<SootField> unusedIt = fieldList.iterator();
        while (unusedIt.hasNext()) {
            SootField unusedField = unusedIt.next();
            unusedField.addTag(new StringTag("Field " + unusedField.getName() + " is not used!", "Unreachable Fields"));
            unusedField.addTag(new ColorTag(0, true, "Unreachable Fields"));
        }
    }
}
