package soot.jimple.infoflow.android.callbacks.filters;

import soot.SootClass;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/filters/UnreachableConstructorFilter.class */
public class UnreachableConstructorFilter extends AbstractCallbackFilter {
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0063, code lost:
        if (soot.Scene.v().getOrMakeFastHierarchy().canStoreClass(r5, r6) == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x006a, code lost:
        if (r5.isConcrete() == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x006d, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x006f, code lost:
        r9 = false;
        r0 = r6.getMethods().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0080, code lost:
        r0 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0091, code lost:
        if (r0.isConstructor() == false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009d, code lost:
        if (r4.reachableMethods.contains(r0) == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a0, code lost:
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ad, code lost:
        if (r0.hasNext() != false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b2, code lost:
        return r9;
     */
    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean accepts(soot.SootClass r5, soot.SootClass r6) {
        /*
            r4 = this;
            r0 = r4
            soot.jimple.infoflow.android.callbacks.ComponentReachableMethods r0 = r0.reachableMethods
            if (r0 != 0) goto L9
            r0 = 1
            return r0
        L9:
            r0 = r5
            r1 = r6
            if (r0 != r1) goto L10
            r0 = 1
            return r0
        L10:
            java.lang.String r0 = "android.app.Fragment"
            soot.RefType r0 = soot.RefType.v(r0)
            r7 = r0
            soot.Scene r0 = soot.Scene.v()
            soot.FastHierarchy r0 = r0.getOrMakeFastHierarchy()
            r1 = r6
            soot.RefType r1 = r1.getType()
            r2 = r7
            boolean r0 = r0.canStoreType(r1, r2)
            r8 = r0
            r0 = r8
            if (r0 == 0) goto L2d
            r0 = 1
            return r0
        L2d:
            r0 = r6
            r9 = r0
            goto L50
        L33:
            r0 = r9
            soot.SootClass r0 = r0.getOuterClass()
            r10 = r0
            r0 = r5
            r1 = r10
            if (r0 != r1) goto L42
            r0 = 1
            return r0
        L42:
            r0 = r9
            r1 = r10
            if (r0 != r1) goto L4c
            goto L58
        L4c:
            r0 = r10
            r9 = r0
        L50:
            r0 = r9
            boolean r0 = r0.isInnerClass()
            if (r0 != 0) goto L33
        L58:
            soot.Scene r0 = soot.Scene.v()
            soot.FastHierarchy r0 = r0.getOrMakeFastHierarchy()
            r1 = r5
            r2 = r6
            boolean r0 = r0.canStoreClass(r1, r2)
            if (r0 == 0) goto L6f
            r0 = r5
            boolean r0 = r0.isConcrete()
            if (r0 == 0) goto L6f
            r0 = 1
            return r0
        L6f:
            r0 = 0
            r9 = r0
            r0 = r6
            java.util.List r0 = r0.getMethods()
            java.util.Iterator r0 = r0.iterator()
            r11 = r0
            goto La6
        L80:
            r0 = r11
            java.lang.Object r0 = r0.next()
            soot.SootMethod r0 = (soot.SootMethod) r0
            r10 = r0
            r0 = r10
            boolean r0 = r0.isConstructor()
            if (r0 == 0) goto La6
            r0 = r4
            soot.jimple.infoflow.android.callbacks.ComponentReachableMethods r0 = r0.reachableMethods
            r1 = r10
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto La6
            r0 = 1
            r9 = r0
            goto Lb0
        La6:
            r0 = r11
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L80
        Lb0:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.android.callbacks.filters.UnreachableConstructorFilter.accepts(soot.SootClass, soot.SootClass):boolean");
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public boolean accepts(SootClass component, SootMethod callback) {
        return true;
    }

    @Override // soot.jimple.infoflow.android.callbacks.filters.ICallbackFilter
    public void reset() {
    }
}
