package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TInterfaceinvoke.class */
public final class TInterfaceinvoke extends Token {
    public TInterfaceinvoke() {
        super.setText(Jimple.INTERFACEINVOKE);
    }

    public TInterfaceinvoke(int line, int pos) {
        super.setText(Jimple.INTERFACEINVOKE);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TInterfaceinvoke(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTInterfaceinvoke(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TInterfaceinvoke text.");
    }
}
