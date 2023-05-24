package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TStaticinvoke.class */
public final class TStaticinvoke extends Token {
    public TStaticinvoke() {
        super.setText(Jimple.STATICINVOKE);
    }

    public TStaticinvoke(int line, int pos) {
        super.setText(Jimple.STATICINVOKE);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TStaticinvoke(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTStaticinvoke(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TStaticinvoke text.");
    }
}
