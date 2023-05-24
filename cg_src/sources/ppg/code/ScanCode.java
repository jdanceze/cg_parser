package ppg.code;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/code/ScanCode.class */
public class ScanCode extends Code {
    public ScanCode(String scanCode) {
        this.value = scanCode;
    }

    @Override // ppg.code.Code
    public Object clone() {
        return new ScanCode(this.value.toString());
    }

    @Override // ppg.code.Code
    public String toString() {
        return new StringBuffer().append("scan with {:").append(this.value).append(":};").toString();
    }
}
