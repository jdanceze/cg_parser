package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TAbstract.class */
public final class TAbstract extends Token {
    public TAbstract() {
        super.setText(Jimple.ABSTRACT);
    }

    public TAbstract(int line, int pos) {
        super.setText(Jimple.ABSTRACT);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TAbstract(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTAbstract(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TAbstract text.");
    }
}
