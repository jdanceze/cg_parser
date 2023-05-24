package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TSpecialinvoke.class */
public final class TSpecialinvoke extends Token {
    public TSpecialinvoke() {
        super.setText(Jimple.SPECIALINVOKE);
    }

    public TSpecialinvoke(int line, int pos) {
        super.setText(Jimple.SPECIALINVOKE);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TSpecialinvoke(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTSpecialinvoke(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TSpecialinvoke text.");
    }
}
