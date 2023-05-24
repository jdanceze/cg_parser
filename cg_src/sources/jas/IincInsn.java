package jas;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/IincInsn.class */
public class IincInsn extends Insn implements RuntimeConstants {
    public IincInsn(int vindex, int increment) {
        this.opc = 132;
        this.operand = new IincOperand(vindex, increment);
    }
}
