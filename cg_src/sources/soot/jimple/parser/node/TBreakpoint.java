package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TBreakpoint.class */
public final class TBreakpoint extends Token {
    public TBreakpoint() {
        super.setText(Jimple.BREAKPOINT);
    }

    public TBreakpoint(int line, int pos) {
        super.setText(Jimple.BREAKPOINT);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TBreakpoint(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTBreakpoint(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TBreakpoint text.");
    }
}
