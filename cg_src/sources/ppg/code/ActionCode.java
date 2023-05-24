package ppg.code;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/code/ActionCode.class */
public class ActionCode extends Code {
    public ActionCode(String actionCode) {
        this.value = actionCode;
    }

    @Override // ppg.code.Code
    public Object clone() {
        return new ActionCode(this.value.toString());
    }

    @Override // ppg.code.Code
    public String toString() {
        return new StringBuffer().append("action code {:\n").append(this.value).append("\n:}\n").toString();
    }
}
