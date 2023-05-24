package ppg.atoms;

import ppg.util.CodeWriter;
import ppg.util.Equatable;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/PrecedenceModifier.class */
public class PrecedenceModifier extends GrammarPart implements Equatable {
    protected String terminalName;

    public String getTerminalName() {
        return this.terminalName;
    }

    public PrecedenceModifier(String terminalName) {
        this.terminalName = terminalName;
    }

    @Override // ppg.atoms.GrammarPart
    public Object clone() {
        return new PrecedenceModifier(getTerminalName());
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.begin(0);
        cw.write("%prec ");
        cw.write(getTerminalName());
        cw.end();
    }

    public String toString() {
        return new StringBuffer().append("%prec ").append(getTerminalName()).toString();
    }
}
