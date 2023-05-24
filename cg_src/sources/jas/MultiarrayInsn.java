package jas;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/MultiarrayInsn.class */
public class MultiarrayInsn extends Insn implements RuntimeConstants {
    public MultiarrayInsn(CP cpe, int sz) {
        this.opc = 197;
        this.operand = new MultiarrayOperand(cpe, sz);
    }
}
