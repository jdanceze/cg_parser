package ppg.atoms;

import java.util.Vector;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/TerminalList.class */
public class TerminalList {
    private String type;
    private Vector symbols;

    public TerminalList(String type, Vector syms) {
        this.type = type;
        this.symbols = syms;
    }

    public String toString() {
        String result = "TERMINAL ";
        if (this.type != null) {
            result = new StringBuffer().append(result).append(this.type).toString();
        }
        for (int i = 0; i < this.symbols.size(); i++) {
            result = new StringBuffer().append(result).append((String) this.symbols.elementAt(i)).toString();
            if (i < this.symbols.size() - 1) {
                result = new StringBuffer().append(result).append(", ").toString();
            }
        }
        return new StringBuffer().append(result).append(";").toString();
    }
}
