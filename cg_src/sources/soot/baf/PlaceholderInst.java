package soot.baf;

import soot.Unit;
import soot.baf.internal.AbstractInst;
/* loaded from: gencallgraphv3.jar:soot/baf/PlaceholderInst.class */
public class PlaceholderInst extends AbstractInst {
    private final Unit source;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PlaceholderInst(Unit source) {
        this.source = source;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new PlaceholderInst(getSource());
    }

    public Unit getSource() {
        return this.source;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "<placeholder>";
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return "<placeholder: " + this.source.toString() + ">";
    }
}
