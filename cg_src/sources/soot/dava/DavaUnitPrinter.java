package soot.dava;

import soot.AbstractUnitPrinter;
import soot.ArrayType;
import soot.RefType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.IdentityRef;
import soot.jimple.ThisRef;
/* loaded from: gencallgraphv3.jar:soot/dava/DavaUnitPrinter.class */
public class DavaUnitPrinter extends AbstractUnitPrinter {
    private boolean eatSpace = false;
    DavaBody body;

    public DavaUnitPrinter(DavaBody body) {
        this.body = body;
    }

    @Override // soot.UnitPrinter
    public void methodRef(SootMethodRef m) {
        handleIndent();
        this.output.append(m.getName());
    }

    @Override // soot.UnitPrinter
    public void fieldRef(SootFieldRef f) {
        handleIndent();
        this.output.append(f.name());
    }

    @Override // soot.UnitPrinter
    public void identityRef(IdentityRef r) {
        handleIndent();
        if (r instanceof ThisRef) {
            literal("this");
            return;
        }
        throw new RuntimeException();
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x004e, code lost:
        if (r4.equals(soot.jimple.Jimple.INTERFACEINVOKE) == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x005a, code lost:
        if (r4.equals(soot.jimple.Jimple.VIRTUALINVOKE) == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0066, code lost:
        if (r4.equals(soot.jimple.Jimple.STATICINVOKE) == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006c, code lost:
        r3.eatSpace = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0071, code lost:
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
            r0 = r4
            r1 = r0
            r5 = r1
            int r0 = r0.hashCode()
            switch(r0) {
                case -1708842255: goto L48;
                case 1099738947: goto L54;
                case 1964705894: goto L60;
                default: goto L72;
            }
        L48:
            r0 = r5
            java.lang.String r1 = "interfaceinvoke"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L6c
            goto L72
        L54:
            r0 = r5
            java.lang.String r1 = "virtualinvoke"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L6c
            goto L72
        L60:
            r0 = r5
            java.lang.String r1 = "staticinvoke"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L6c
            goto L72
        L6c:
            r0 = r3
            r1 = 1
            r0.eatSpace = r1
            return
        L72:
            r0 = r3
            java.lang.StringBuffer r0 = r0.output
            r1 = r4
            java.lang.StringBuffer r0 = r0.append(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dava.DavaUnitPrinter.literal(java.lang.String):void");
    }

    @Override // soot.UnitPrinter
    public void type(Type t) {
        handleIndent();
        if (!(t instanceof RefType)) {
            if (t instanceof ArrayType) {
                ((ArrayType) t).toString(this);
                return;
            } else {
                this.output.append(t.toString());
                return;
            }
        }
        SootClass sootClass = ((RefType) t).getSootClass();
        String name = sootClass.getJavaStyleName();
        if (!name.equals(sootClass.toString())) {
            name = RemoveFullyQualifiedName.getReducedName(this.body.getImportList(), sootClass.toString(), t);
        }
        this.output.append(name);
    }

    @Override // soot.UnitPrinter
    public void unitRef(Unit u, boolean branchTarget) {
        throw new RuntimeException("Dava doesn't have unit references!");
    }

    @Override // soot.AbstractUnitPrinter, soot.UnitPrinter
    public void constant(Constant c) {
        if (c instanceof ClassConstant) {
            handleIndent();
            this.output.append(((ClassConstant) c).value.replace('/', '.')).append(".class");
            return;
        }
        super.constant(c);
    }

    public void addNot() {
        this.output.append(" !");
    }

    public void addAggregatedOr() {
        this.output.append(" || ");
    }

    public void addAggregatedAnd() {
        this.output.append(" && ");
    }

    public void addLeftParen() {
        this.output.append(" (");
    }

    public void addRightParen() {
        this.output.append(") ");
    }

    public void printString(String s) {
        this.output.append(s);
    }
}
