package soot.jimple.spark.fieldrw;

import java.util.Set;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/fieldrw/FieldReadTag.class */
public class FieldReadTag extends FieldRWTag {
    public static final String NAME = "FieldReadTag";

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldReadTag(Set<SootField> fields) {
        super(fields);
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }
}
