package soot;

import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityRef;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
/* loaded from: gencallgraphv3.jar:soot/BriefUnitPrinter.class */
public class BriefUnitPrinter extends LabeledUnitPrinter {
    private boolean baf;
    private boolean eatSpace;

    public BriefUnitPrinter(Body body) {
        super(body);
        this.eatSpace = false;
    }

    @Override // soot.AbstractUnitPrinter, soot.UnitPrinter
    public void startUnit(Unit u) {
        super.startUnit(u);
        this.baf = !(u instanceof Stmt);
    }

    @Override // soot.UnitPrinter
    public void methodRef(SootMethodRef m) {
        handleIndent();
        if (!this.baf && m.resolve().isStatic()) {
            this.output.append(m.getDeclaringClass().getName());
            literal(".");
        }
        this.output.append(m.name());
    }

    @Override // soot.UnitPrinter
    public void fieldRef(SootFieldRef f) {
        handleIndent();
        if (this.baf || f.resolve().isStatic()) {
            this.output.append(f.declaringClass().getName());
            literal(".");
        }
        this.output.append(f.name());
    }

    @Override // soot.UnitPrinter
    public void identityRef(IdentityRef r) {
        handleIndent();
        if (r instanceof ThisRef) {
            literal("@this");
        } else if (r instanceof ParameterRef) {
            ParameterRef pr = (ParameterRef) r;
            literal("@parameter" + pr.getIndex());
        } else if (r instanceof CaughtExceptionRef) {
            literal("@caughtexception");
        } else {
            throw new RuntimeException();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0056, code lost:
        if (r4.equals(soot.jimple.Jimple.INTERFACEINVOKE) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0062, code lost:
        if (r4.equals(soot.jimple.Jimple.VIRTUALINVOKE) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006e, code lost:
        if (r4.equals(soot.jimple.Jimple.STATICINVOKE) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0074, code lost:
        r3.eatSpace = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0079, code lost:
        return;
     */
    @Override // soot.UnitPrinter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void literal(java.lang.String r4) {
        /*
            r3 = this;
            r0 = r3
            r0.handleIndent()
            r0 = r3
            boolean r0 = r0.eatSpace
            if (r0 == 0) goto L1a
            java.lang.String r0 = " "
            r1 = r4
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L1a
            r0 = r3
            r1 = 0
            r0.eatSpace = r1
            return
        L1a:
            r0 = r3
            r1 = 0
            r0.eatSpace = r1
            r0 = r3
            boolean r0 = r0.baf
            if (r0 != 0) goto L7a
            r0 = r4
            r1 = r0
            r5 = r1
            int r0 = r0.hashCode()
            switch(r0) {
                case -1708842255: goto L50;
                case 1099738947: goto L5c;
                case 1964705894: goto L68;
                default: goto L7a;
            }
        L50:
            r0 = r5
            java.lang.String r1 = "interfaceinvoke"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L74
            goto L7a
        L5c:
            r0 = r5
            java.lang.String r1 = "virtualinvoke"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L74
            goto L7a
        L68:
            r0 = r5
            java.lang.String r1 = "staticinvoke"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L74
            goto L7a
        L74:
            r0 = r3
            r1 = 1
            r0.eatSpace = r1
            return
        L7a:
            r0 = r3
            java.lang.StringBuffer r0 = r0.output
            r1 = r4
            java.lang.StringBuffer r0 = r0.append(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.BriefUnitPrinter.literal(java.lang.String):void");
    }

    @Override // soot.UnitPrinter
    public void type(Type t) {
        handleIndent();
        this.output.append(t.toString());
    }
}
