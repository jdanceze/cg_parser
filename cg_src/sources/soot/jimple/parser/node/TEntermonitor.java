package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TEntermonitor.class */
public final class TEntermonitor extends Token {
    public TEntermonitor() {
        super.setText(Jimple.ENTERMONITOR);
    }

    public TEntermonitor(int line, int pos) {
        super.setText(Jimple.ENTERMONITOR);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TEntermonitor(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTEntermonitor(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TEntermonitor text.");
    }
}
