package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TInstanceof.class */
public final class TInstanceof extends Token {
    public TInstanceof() {
        super.setText(Jimple.INSTANCEOF);
    }

    public TInstanceof(int line, int pos) {
        super.setText(Jimple.INSTANCEOF);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TInstanceof(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTInstanceof(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TInstanceof text.");
    }
}
