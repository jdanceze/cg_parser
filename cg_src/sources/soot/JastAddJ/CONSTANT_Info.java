package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_Info.class */
public class CONSTANT_Info {
    protected BytecodeParser p;

    public CONSTANT_Info(BytecodeParser parser) {
        this.p = parser;
    }

    public Expr expr() {
        throw new Error("CONSTANT_info.expr() should not be computed for " + getClass().getName());
    }

    public Expr exprAsBoolean() {
        return expr();
    }
}
