package soot.jimple.parser.node;

import soot.jimple.Jimple;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TAnnotation.class */
public final class TAnnotation extends Token {
    public TAnnotation() {
        super.setText(Jimple.ANNOTATION);
    }

    public TAnnotation(int line, int pos) {
        super.setText(Jimple.ANNOTATION);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TAnnotation(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTAnnotation(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TAnnotation text.");
    }
}
