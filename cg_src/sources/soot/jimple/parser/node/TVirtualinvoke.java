package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TVirtualinvoke.class */
public final class TVirtualinvoke extends Token {
    public TVirtualinvoke() {
        super.setText(Jimple.VIRTUALINVOKE);
    }

    public TVirtualinvoke(int line, int pos) {
        super.setText(Jimple.VIRTUALINVOKE);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TVirtualinvoke(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTVirtualinvoke(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TVirtualinvoke text.");
    }
}
