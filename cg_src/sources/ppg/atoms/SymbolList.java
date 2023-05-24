package ppg.atoms;

import java.util.Vector;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/SymbolList.class */
public class SymbolList {
    public static final int TERMINAL = 0;
    public static final int NONTERMINAL = 1;
    private int variety;
    private String type;
    private Vector symbols;

    public SymbolList(int which, String type, Vector syms) {
        this.variety = which;
        this.type = type;
        this.symbols = syms;
    }

    public boolean dropSymbol(String gs) {
        for (int i = 0; i < this.symbols.size(); i++) {
            if (gs.equals(this.symbols.elementAt(i))) {
                this.symbols.removeElementAt(i);
                return true;
            }
        }
        return false;
    }

    public Object clone() {
        String newType = this.type == null ? null : this.type.toString();
        Vector newSyms = new Vector();
        for (int i = 0; i < this.symbols.size(); i++) {
            newSyms.addElement(((String) this.symbols.elementAt(i)).toString());
        }
        return new SymbolList(this.variety, newType, newSyms);
    }

    public String toString() {
        String result = "";
        if (this.symbols.size() > 0) {
            switch (this.variety) {
                case 0:
                    result = "terminal ";
                    break;
                case 1:
                    result = "non terminal ";
                    break;
            }
            if (this.type != null) {
                result = new StringBuffer().append(result).append(this.type).append(Instruction.argsep).toString();
            }
            int size = this.symbols.size();
            for (int i = 0; i < size; i++) {
                result = new StringBuffer().append(result).append((String) this.symbols.elementAt(i)).toString();
                if (i < size - 1) {
                    result = new StringBuffer().append(result).append(", ").toString();
                }
            }
            result = new StringBuffer().append(result).append(";").toString();
        }
        return result;
    }
}
