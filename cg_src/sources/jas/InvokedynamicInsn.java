package jas;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/InvokedynamicInsn.class */
public class InvokedynamicInsn extends Insn implements RuntimeConstants {
    public InvokedynamicInsn(CP cpe, int nargs) {
        this.opc = 185;
        this.operand = new InvokeinterfaceOperand(cpe, nargs);
    }
}
