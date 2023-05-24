package soot.jimple.spark.fieldrw;

import soot.G;
import soot.Singletons;
import soot.tagkit.ImportantTagAggregator;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/fieldrw/FieldWriteTagAggregator.class */
public class FieldWriteTagAggregator extends ImportantTagAggregator {
    public FieldWriteTagAggregator(Singletons.Global g) {
    }

    public static FieldWriteTagAggregator v() {
        return G.v().soot_jimple_spark_fieldrw_FieldWriteTagAggregator();
    }

    @Override // soot.tagkit.TagAggregator
    public boolean wantTag(Tag t) {
        return t instanceof FieldWriteTag;
    }

    @Override // soot.tagkit.TagAggregator
    public String aggregatedName() {
        return "FieldWrite";
    }
}
