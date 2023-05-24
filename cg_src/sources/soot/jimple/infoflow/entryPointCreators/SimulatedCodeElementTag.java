package soot.jimple.infoflow.entryPointCreators;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/entryPointCreators/SimulatedCodeElementTag.class */
public class SimulatedCodeElementTag implements Tag {
    public static final String TAG_NAME = "SimulatedCodeElementTag";
    public static SimulatedCodeElementTag TAG = new SimulatedCodeElementTag();

    private SimulatedCodeElementTag() {
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return TAG_NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() throws AttributeValueException {
        return null;
    }
}
