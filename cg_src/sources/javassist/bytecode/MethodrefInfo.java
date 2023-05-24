package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConstPool.java */
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/bytecode/MethodrefInfo.class */
public class MethodrefInfo extends MemberrefInfo {
    static final int tag = 10;

    public MethodrefInfo(int cindex, int ntindex, int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }

    public MethodrefInfo(DataInputStream in, int thisIndex) throws IOException {
        super(in, thisIndex);
    }

    @Override // javassist.bytecode.ConstInfo
    public int getTag() {
        return 10;
    }

    @Override // javassist.bytecode.MemberrefInfo
    public String getTagName() {
        return XmlConstants.Attributes.method;
    }

    @Override // javassist.bytecode.MemberrefInfo
    protected int copy2(ConstPool dest, int cindex, int ntindex) {
        return dest.addMethodrefInfo(cindex, ntindex);
    }
}
