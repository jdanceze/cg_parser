package soot.jimple.spark.fieldrw;

import java.util.Set;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/fieldrw/FieldWriteTag.class */
public class FieldWriteTag extends FieldRWTag {
    public static final String NAME = "FieldWriteTag";

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldWriteTag(Set<SootField> fields) {
        super(fields);
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }
}
