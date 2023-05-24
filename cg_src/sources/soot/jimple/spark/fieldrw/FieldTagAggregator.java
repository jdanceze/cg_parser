package soot.jimple.spark.fieldrw;

import java.util.LinkedList;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.tagkit.Tag;
import soot.tagkit.TagAggregator;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/fieldrw/FieldTagAggregator.class */
public class FieldTagAggregator extends TagAggregator {
    public FieldTagAggregator(Singletons.Global g) {
    }

    public static FieldTagAggregator v() {
        return G.v().soot_jimple_spark_fieldrw_FieldTagAggregator();
    }

    @Override // soot.tagkit.TagAggregator, soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map options) {
        FieldReadTagAggregator.v().transform(b, phaseName, options);
        FieldWriteTagAggregator.v().transform(b, phaseName, options);
    }

    @Override // soot.tagkit.TagAggregator
    public boolean wantTag(Tag t) {
        throw new RuntimeException();
    }

    @Override // soot.tagkit.TagAggregator
    public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
        throw new RuntimeException();
    }

    @Override // soot.tagkit.TagAggregator
    public String aggregatedName() {
        throw new RuntimeException();
    }
}
