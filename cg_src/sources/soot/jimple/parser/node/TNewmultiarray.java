package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TNewmultiarray.class */
public final class TNewmultiarray extends Token {
    public TNewmultiarray() {
        super.setText(Jimple.NEWMULTIARRAY);
    }

    public TNewmultiarray(int line, int pos) {
        super.setText(Jimple.NEWMULTIARRAY);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TNewmultiarray(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTNewmultiarray(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TNewmultiarray text.");
    }
}
