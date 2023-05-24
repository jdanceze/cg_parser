package soot.jimple.spark.fieldrw;

import soot.G;
import soot.Singletons;
import soot.tagkit.ImportantTagAggregator;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/fieldrw/FieldReadTagAggregator.class */
public class FieldReadTagAggregator extends ImportantTagAggregator {
    public FieldReadTagAggregator(Singletons.Global g) {
    }

    public static FieldReadTagAggregator v() {
        return G.v().soot_jimple_spark_fieldrw_FieldReadTagAggregator();
    }

    @Override // soot.tagkit.TagAggregator
    public boolean wantTag(Tag t) {
        return t instanceof FieldReadTag;
    }

    @Override // soot.tagkit.TagAggregator
    public String aggregatedName() {
        return "FieldRead";
    }
}
