package ppg.cmds;

import java.util.Vector;
import ppg.atoms.Nonterminal;
import ppg.atoms.Production;
import ppg.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/cmds/TransferCmd.class */
public class TransferCmd implements Command {
    private Nonterminal nonterminal;
    private Vector transferList;

    public TransferCmd(String nt, Vector tlist) {
        this.nonterminal = new Nonterminal(nt);
        this.transferList = tlist;
    }

    public Nonterminal getSource() {
        return this.nonterminal;
    }

    public Vector getTransferList() {
        return this.transferList;
    }

    @Override // ppg.parse.Unparse
    public void unparse(CodeWriter cw) {
        cw.write("TransferCmd");
        cw.allowBreak(2);
        cw.write(new StringBuffer().append(this.nonterminal).append(" to ").toString());
        for (int i = 0; i < this.transferList.size(); i++) {
            Production prod = (Production) this.transferList.elementAt(i);
            prod.unparse(cw);
        }
    }
}
