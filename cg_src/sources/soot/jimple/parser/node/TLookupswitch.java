package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TLookupswitch.class */
public final class TLookupswitch extends Token {
    public TLookupswitch() {
        super.setText(Jimple.LOOKUPSWITCH);
    }

    public TLookupswitch(int line, int pos) {
        super.setText(Jimple.LOOKUPSWITCH);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TLookupswitch(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTLookupswitch(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TLookupswitch text.");
    }
}
