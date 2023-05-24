package ppg.atoms;

import ppg.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/atoms/SemanticAction.class */
public class SemanticAction extends GrammarPart {
    private String action;

    public SemanticAction(String actionCode) {
        this.action = actionCode;
    }

    @Override // ppg.atoms.GrammarPart
    public Object clone() {
        return new SemanticAction(this.action.toString());
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.begin(0);
        cw.write("{:");
        cw.allowBreak(-1);
        cw.write(this.action);
        cw.allowBreak(0);
        cw.write(":}");
        cw.end();
    }

    public String toString() {
        return new StringBuffer().append("{:").append(this.action).append(":}\n").toString();
    }
}
