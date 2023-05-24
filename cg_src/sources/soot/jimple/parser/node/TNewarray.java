package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TNewarray.class */
public final class TNewarray extends Token {
    public TNewarray() {
        super.setText(Jimple.NEWARRAY);
    }

    public TNewarray(int line, int pos) {
        super.setText(Jimple.NEWARRAY);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TNewarray(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTNewarray(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TNewarray text.");
    }
}
