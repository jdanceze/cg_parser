package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_Double_Info.class */
public class CONSTANT_Double_Info extends CONSTANT_Info {
    public double value;

    public CONSTANT_Double_Info(BytecodeParser parser) {
        super(parser);
        this.value = this.p.readDouble();
    }

    public String toString() {
        return "DoubleInfo: " + Double.toString(this.value);
    }

    @Override // soot.JastAddJ.CONSTANT_Info
    public Expr expr() {
        return Literal.buildDoubleLiteral(this.value);
    }
}
