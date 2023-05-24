package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TCmple.class */
public final class TCmple extends Token {
    public TCmple() {
        super.setText("<=");
    }

    public TCmple(int line, int pos) {
        super.setText("<=");
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TCmple(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTCmple(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TCmple text.");
    }
}
