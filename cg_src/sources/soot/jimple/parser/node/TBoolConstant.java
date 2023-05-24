package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TBoolConstant.class */
public final class TBoolConstant extends Token {
    public TBoolConstant(String text) {
        setText(text);
    }

    public TBoolConstant(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TBoolConstant(getText(), getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTBoolConstant(this);
    }
}
