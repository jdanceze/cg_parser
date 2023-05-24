package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TUnknown.class */
public final class TUnknown extends Token {
    public TUnknown() {
        super.setText("unknown");
    }

    public TUnknown(int line, int pos) {
        super.setText("unknown");
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TUnknown(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTUnknown(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TUnknown text.");
    }
}
