package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TIntegerConstant.class */
public final class TIntegerConstant extends Token {
    public TIntegerConstant(String text) {
        setText(text);
    }

    public TIntegerConstant(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TIntegerConstant(getText(), getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTIntegerConstant(this);
    }
}
