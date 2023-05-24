package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_Integer_Info.class */
public class CONSTANT_Integer_Info extends CONSTANT_Info {
    public int value;

    public CONSTANT_Integer_Info(BytecodeParser parser) {
        super(parser);
        this.value = this.p.readInt();
    }

    public String toString() {
        return "IntegerInfo: " + Integer.toString(this.value);
    }

    @Override // soot.JastAddJ.CONSTANT_Info
    public Expr expr() {
        return Literal.buildIntegerLiteral(this.value);
    }

    @Override // soot.JastAddJ.CONSTANT_Info
    public Expr exprAsBoolean() {
        return Literal.buildBooleanLiteral(this.value == 0);
    }
}
