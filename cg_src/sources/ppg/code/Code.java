package ppg.code;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/code/Code.class */
public abstract class Code {
    protected String value;

    public abstract Object clone();

    public abstract String toString();

    public void append(String s) {
        this.value = new StringBuffer().append(this.value).append("\n").append(s).toString();
    }

    public void prepend(String s) {
        this.value = new StringBuffer().append(s).append("\n").append(this.value).toString();
    }
}
