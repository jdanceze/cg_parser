package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TExitmonitor.class */
public final class TExitmonitor extends Token {
    public TExitmonitor() {
        super.setText(Jimple.EXITMONITOR);
    }

    public TExitmonitor(int line, int pos) {
        super.setText(Jimple.EXITMONITOR);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TExitmonitor(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTExitmonitor(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TExitmonitor text.");
    }
}
