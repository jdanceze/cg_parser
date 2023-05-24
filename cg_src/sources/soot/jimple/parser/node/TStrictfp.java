package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TStrictfp.class */
public final class TStrictfp extends Token {
    public TStrictfp() {
        super.setText(Jimple.STRICTFP);
    }

    public TStrictfp(int line, int pos) {
        super.setText(Jimple.STRICTFP);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TStrictfp(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTStrictfp(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TStrictfp text.");
    }
}
