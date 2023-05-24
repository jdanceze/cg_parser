package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TLengthof.class */
public final class TLengthof extends Token {
    public TLengthof() {
        super.setText(Jimple.LENGTHOF);
    }

    public TLengthof(int line, int pos) {
        super.setText(Jimple.LENGTHOF);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TLengthof(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTLengthof(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TLengthof text.");
    }
}
