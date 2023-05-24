package soot.jimple.parser.node;

import org.apache.commons.cli.HelpFormatter;
import soot.jimple.parser.analysis.Analysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/TMinus.class */
public final class TMinus extends Token {
    public TMinus() {
        super.setText(HelpFormatter.DEFAULT_OPT_PREFIX);
    }

    public TMinus(int line, int pos) {
        super.setText(HelpFormatter.DEFAULT_OPT_PREFIX);
        setLine(line);
        setPos(pos);
    }

    @Override // soot.jimple.parser.node.Node
    public Object clone() {
        return new TMinus(getLine(), getPos());
    }

    @Override // soot.jimple.parser.node.Switchable
    public void apply(Switch sw) {
        ((Analysis) sw).caseTMinus(this);
    }

    @Override // soot.jimple.parser.node.Token
    public void setText(String text) {
        throw new RuntimeException("Cannot change TMinus text.");
    }
}
