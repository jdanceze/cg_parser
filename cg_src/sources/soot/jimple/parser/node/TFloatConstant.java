package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TFloatConstant.class */
public final class TFloatConstant extends Token {
    public TFloatConstant(String text) {
        setText(text);
    }

    public TFloatConstant(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TFloatConstant(getText(), getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTFloatConstant(this);
    }
}
