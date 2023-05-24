package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TSynchronized.class */
public final class TSynchronized extends Token {
    public TSynchronized() {
        super.setText(Jimple.SYNCHRONIZED);
    }

    public TSynchronized(int line, int pos) {
        super.setText(Jimple.SYNCHRONIZED);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TSynchronized(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTSynchronized(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TSynchronized text.");
    }
}
