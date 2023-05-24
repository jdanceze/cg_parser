package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TTableswitch.class */
public final class TTableswitch extends Token {
    public TTableswitch() {
        super.setText(Jimple.TABLESWITCH);
    }

    public TTableswitch(int line, int pos) {
        super.setText(Jimple.TABLESWITCH);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TTableswitch(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTTableswitch(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TTableswitch text.");
    }
}
