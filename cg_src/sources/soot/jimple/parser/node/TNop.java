package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TNop.class */
public final class TNop extends Token {
    public TNop() {
        super.setText(Jimple.NOP);
    }

    public TNop(int line, int pos) {
        super.setText(Jimple.NOP);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TNop(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTNop(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TNop text.");
    }
}
