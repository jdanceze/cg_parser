package soot.jimple.spark.pag;

import soot.Type;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/NewInstanceNode.class */
public class NewInstanceNode extends Node {
    private final Value value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NewInstanceNode(PAG pag, Value value, Type type) {
        super(pag, type);
        this.value = value;
    }

    public Value getValue() {
        return this.value;
    }
}
