package jas;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/LookupswitchInsn.class */
public class LookupswitchInsn extends Insn implements RuntimeConstants {
    public LookupswitchInsn(Label def, int[] match, Label[] target) {
        this.opc = 171;
        this.operand = new LookupswitchOperand(this, def, match, target);
    }
}
