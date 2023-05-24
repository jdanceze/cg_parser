package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TAtIdentifier.class */
public final class TAtIdentifier extends Token {
    public TAtIdentifier(String text) {
        setText(text);
    }

    public TAtIdentifier(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TAtIdentifier(getText(), getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTAtIdentifier(this);
    }
}
