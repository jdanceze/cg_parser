package soot.jimple.infoflow.cfg;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/cfg/FlowDroidEssentialMethodTag.class */
public class FlowDroidEssentialMethodTag implements Tag {
    public static final String TAG_NAME = "fd_essential_method";

    @Override // soot.tagkit.Tag
    public String getName() {
        return TAG_NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() throws AttributeValueException {
        return null;
    }
}
