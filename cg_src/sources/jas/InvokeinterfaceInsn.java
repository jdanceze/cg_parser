package jas;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/InvokeinterfaceInsn.class */
public class InvokeinterfaceInsn extends Insn implements RuntimeConstants {
    public InvokeinterfaceInsn(CP cpe, int nargs) {
        this.opc = 185;
        this.operand = new InvokeinterfaceOperand(cpe, nargs);
    }
}
