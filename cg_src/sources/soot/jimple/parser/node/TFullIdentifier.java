package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TFullIdentifier.class */
public final class TFullIdentifier extends Token {
    public TFullIdentifier(String text) {
        setText(text);
    }

    public TFullIdentifier(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TFullIdentifier(getText(), getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTFullIdentifier(this);
    }
}
