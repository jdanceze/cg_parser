package jas;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/TableswitchInsn.class */
public class TableswitchInsn extends Insn implements RuntimeConstants {
    public TableswitchInsn(int min, int max, Label def, Label[] j) {
        this.opc = 170;
        this.operand = new TableswitchOperand(this, min, max, def, j);
    }
}
