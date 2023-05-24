package ppg.atoms;

import java.util.Vector;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/Precedence.class */
public class Precedence {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int NONASSOC = 2;
    private int type;
    private Vector symbols;

    public Precedence(int type, Vector syms) {
        this.type = type;
        this.symbols = syms;
    }

    public Object clone() {
        Vector newSyms = new Vector();
        for (int i = 0; i < this.symbols.size(); i++) {
            newSyms.addElement(((GrammarSymbol) this.symbols.elementAt(i)).clone());
        }
        return new Precedence(this.type, newSyms);
    }

    public String toString() {
        String result = "precedence ";
        switch (this.type) {
            case 0:
                result = new StringBuffer().append(result).append("left ").toString();
                break;
            case 1:
                result = new StringBuffer().append(result).append("right ").toString();
                break;
            case 2:
                result = new StringBuffer().append(result).append("nonassoc ").toString();
                break;
        }
        for (int i = 0; i < this.symbols.size(); i++) {
            result = new StringBuffer().append(result).append(this.symbols.elementAt(i)).toString();
            if (i < this.symbols.size() - 1) {
                result = new StringBuffer().append(result).append(", ").toString();
            }
        }
        return new StringBuffer().append(result).append(";").toString();
    }
}
