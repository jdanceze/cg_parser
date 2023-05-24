package ppg.code;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/code/InitCode.class */
public class InitCode extends Code {
    public InitCode(String initCode) {
        this.value = initCode;
    }

    @Override // ppg.code.Code
    public Object clone() {
        return new InitCode(this.value.toString());
    }

    @Override // ppg.code.Code
    public String toString() {
        return new StringBuffer().append("init code {:\n").append(this.value).append("\n:}\n").toString();
    }
}
