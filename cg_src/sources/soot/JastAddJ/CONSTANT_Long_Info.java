package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_Long_Info.class */
public class CONSTANT_Long_Info extends CONSTANT_Info {
    public long value;

    public CONSTANT_Long_Info(BytecodeParser parser) {
        super(parser);
        this.value = this.p.readLong();
    }

    public String toString() {
        return "LongInfo: " + Long.toString(this.value);
    }

    @Override // soot.JastAddJ.CONSTANT_Info
    public Expr expr() {
        return Literal.buildLongLiteral(this.value);
    }
}
