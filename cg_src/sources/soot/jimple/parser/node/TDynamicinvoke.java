package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TDynamicinvoke.class */
public final class TDynamicinvoke extends Token {
    public TDynamicinvoke() {
        super.setText(Jimple.DYNAMICINVOKE);
    }

    public TDynamicinvoke(int line, int pos) {
        super.setText(Jimple.DYNAMICINVOKE);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TDynamicinvoke(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTDynamicinvoke(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TDynamicinvoke text.");
    }
}
