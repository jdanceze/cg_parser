package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_String_Info.class */
public class CONSTANT_String_Info extends CONSTANT_Info {
    public int string_index;

    public CONSTANT_String_Info(BytecodeParser parser) {
        super(parser);
        this.string_index = this.p.u2();
    }

    @Override // soot.JastAddJ.CONSTANT_Info
    public Expr expr() {
        CONSTANT_Utf8_Info i = (CONSTANT_Utf8_Info) this.p.constantPool[this.string_index];
        return Literal.buildStringLiteral(i.string);
    }

    public String toString() {
        return "StringInfo: " + this.p.constantPool[this.string_index];
    }
}
