package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TVolatile.class */
public final class TVolatile extends Token {
    public TVolatile() {
        super.setText(Jimple.VOLATILE);
    }

    public TVolatile(int line, int pos) {
        super.setText(Jimple.VOLATILE);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TVolatile(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTVolatile(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TVolatile text.");
    }
}
