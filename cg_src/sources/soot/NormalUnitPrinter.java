package soot;

import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityRef;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
/* loaded from: gencallgraphv3.jar:soot/NormalUnitPrinter.class */
public class NormalUnitPrinter extends LabeledUnitPrinter {
    public NormalUnitPrinter(Body body) {
        super(body);
    }

    @Override // soot.UnitPrinter
    public void type(Type t) {
        handleIndent();
        this.output.append(t == null ? "<null>" : t.toQuotedString());
    }

    @Override // soot.UnitPrinter
    public void methodRef(SootMethodRef m) {
        handleIndent();
        this.output.append(m.getSignature());
    }

    @Override // soot.UnitPrinter
    public void fieldRef(SootFieldRef f) {
        handleIndent();
        this.output.append(f.getSignature());
    }

    @Override // soot.UnitPrinter
    public void identityRef(IdentityRef r) {
        handleIndent();
        if (r instanceof ThisRef) {
            literal("@this: ");
            type(r.getType());
        } else if (r instanceof ParameterRef) {
            ParameterRef pr = (ParameterRef) r;
            literal("@parameter" + pr.getIndex() + ": ");
            type(r.getType());
        } else if (r instanceof CaughtExceptionRef) {
            literal("@caughtexception");
        } else {
            throw new RuntimeException();
        }
    }

    @Override // soot.UnitPrinter
    public void literal(String s) {
        handleIndent();
        this.output.append(s);
    }
}
