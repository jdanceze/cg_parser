package ppg.code;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/code/ParserCode.class */
public class ParserCode extends Code {
    String classname;
    String extendsimpls;

    public ParserCode(String classname, String extendsimpls, String parserCode) {
        this.classname = classname;
        this.extendsimpls = extendsimpls;
        this.value = parserCode;
    }

    @Override // ppg.code.Code
    public Object clone() {
        return new ParserCode(this.classname, this.extendsimpls, this.value);
    }

    @Override // ppg.code.Code
    public String toString() {
        if (this.classname == null) {
            this.classname = "code";
        }
        return new StringBuffer().append("parser ").append(this.classname).append(this.extendsimpls).append(" {:\n").append(this.value).append("\n:}\n").toString();
    }
}
