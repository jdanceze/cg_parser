package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_Utf8_Info.class */
public class CONSTANT_Utf8_Info extends CONSTANT_Info {
    public String string;

    public CONSTANT_Utf8_Info(BytecodeParser parser) {
        super(parser);
        this.string = this.p.readUTF();
    }

    public String toString() {
        return "Utf8Info: " + this.string;
    }

    @Override // soot.JastAddJ.CONSTANT_Info
    public Expr expr() {
        return Literal.buildStringLiteral(this.string);
    }

    public String string() {
        return this.string;
    }
}
