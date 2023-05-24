package jas;

import java.io.DataOutputStream;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/InvokeDynamicCP.class */
public class InvokeDynamicCP extends CP implements RuntimeConstants {
    MethodHandleCP bsm;
    NameTypeCP method;
    int bsmTableIndex;

    public InvokeDynamicCP(String bsmClassName, String bsmName, String bsmSig, String methodName, String methodSig, int bsmTableIndex) {
        this.bsmTableIndex = bsmTableIndex;
        this.uniq = (bsmClassName + "fv0¤" + bsmName + "&%$91&" + bsmSig + "*(012$" + methodName + "dfg8932" + methodSig).intern();
        this.bsm = new MethodHandleCP(6, bsmClassName, bsmName, bsmSig);
        this.method = new NameTypeCP(methodName, methodSig);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void resolve(ClassEnv e) {
        e.addCPItem(this.bsm);
        e.addCPItem(this.method);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // jas.CP
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeByte(18);
        out.writeShort(this.bsmTableIndex);
        out.writeShort(e.getCPIndex(this.method));
    }
}
