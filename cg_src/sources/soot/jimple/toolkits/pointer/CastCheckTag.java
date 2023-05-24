package soot.jimple.toolkits.pointer;

import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/CastCheckTag.class */
public class CastCheckTag implements Tag {
    public static final String NAME = "CastCheckTag";
    private final boolean eliminateCheck;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CastCheckTag(boolean eliminateCheck) {
        this.eliminateCheck = eliminateCheck;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte[] bArr = new byte[1];
        bArr[0] = (byte) (this.eliminateCheck ? 1 : 0);
        return bArr;
    }

    public String toString() {
        if (this.eliminateCheck) {
            return "This cast check can be eliminated.";
        }
        return "This cast check should NOT be eliminated.";
    }

    public boolean canEliminateCheck() {
        return this.eliminateCheck;
    }
}
