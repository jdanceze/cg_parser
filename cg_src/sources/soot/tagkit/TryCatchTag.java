package soot.tagkit;

import java.util.HashMap;
import java.util.Map;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/TryCatchTag.class */
public class TryCatchTag implements Tag {
    public static final String NAME = "TryCatchTag";
    protected Map<Unit, Unit> handlerUnitToFallThroughUnit = new HashMap();

    public void register(Unit handler, Unit fallThrough) {
        this.handlerUnitToFallThroughUnit.put(handler, fallThrough);
    }

    public Unit getFallThroughUnitOf(Unit handlerUnit) {
        return this.handlerUnitToFallThroughUnit.get(handlerUnit);
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() throws AttributeValueException {
        throw new UnsupportedOperationException();
    }
}
