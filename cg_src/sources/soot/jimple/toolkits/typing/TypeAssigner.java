package soot.jimple.toolkits.typing;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.ByteType;
import soot.CharType;
import soot.G;
import soot.Local;
import soot.NullType;
import soot.PhaseOptions;
import soot.Scene;
import soot.ShortType;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.typing.fast.AugHierarchy;
import soot.options.JBTROptions;
import soot.options.Options;
import soot.toolkits.scalar.UnusedLocalEliminator;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/TypeAssigner.class */
public class TypeAssigner extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(TypeAssigner.class);

    public TypeAssigner(Singletons.Global g) {
    }

    public static TypeAssigner v() {
        return G.v().soot_jimple_toolkits_typing_TypeAssigner();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Date start;
        if (b == null) {
            throw new NullPointerException();
        }
        if (Options.v().verbose()) {
            start = new Date();
            logger.debug("[TypeAssigner] typing system started on " + start);
        } else {
            start = null;
        }
        JBTROptions opt = new JBTROptions(options);
        JimpleBody jb = (JimpleBody) b;
        if (opt.compare_type_assigners()) {
            compareTypeAssigners(jb, opt.use_older_type_assigner());
        } else if (opt.use_older_type_assigner()) {
            TypeResolver.resolve(jb, Scene.v());
        } else {
            new soot.jimple.toolkits.typing.fast.TypeResolver(jb).inferTypes();
        }
        if (Options.v().verbose()) {
            Date finish = new Date();
            long runtime = finish.getTime() - start.getTime();
            long mins = runtime / 60000;
            long secs = (runtime % 60000) / 1000;
            logger.debug("[TypeAssigner] typing system ended. It took " + mins + " mins and " + secs + " secs.");
        }
        if (!opt.ignore_nullpointer_dereferences()) {
            replaceNullType(jb);
        }
        if (typingFailed(jb)) {
            throw new RuntimeException("type inference failed!");
        }
    }

    protected static void replaceNullType(Body b) {
        boolean hasNullType = false;
        Iterator<Local> it = b.getLocals().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Local l = it.next();
            if (l.getType() instanceof NullType) {
                hasNullType = true;
                break;
            }
        }
        if (!hasNullType) {
            return;
        }
        Map<String, String> opts = PhaseOptions.v().getPhaseOptions("jop.cpf");
        if (!opts.containsKey("enabled") || !"true".equals(opts.get("enabled"))) {
            logger.warn("Cannot run TypeAssigner.replaceNullType(Body). Try to enable jop.cfg.");
            return;
        }
        ConstantPropagatorAndFolder.v().transform(b);
        List<Unit> unitToReplaceByException = new ArrayList<>();
        Iterator<Unit> it2 = b.getUnits().iterator();
        while (it2.hasNext()) {
            Unit u = it2.next();
            Stmt s = (Stmt) u;
            for (ValueBox vb : u.getUseBoxes()) {
                Value value = vb.getValue();
                if ((value instanceof Local) && (value.getType() instanceof NullType)) {
                    boolean replace = false;
                    if (s.containsArrayRef()) {
                        if (s.getArrayRef().getBase() == value) {
                            replace = true;
                        }
                    } else if (s.containsFieldRef()) {
                        FieldRef r = s.getFieldRef();
                        if (r instanceof InstanceFieldRef) {
                            InstanceFieldRef ir = (InstanceFieldRef) r;
                            if (ir.getBase() == value) {
                                replace = true;
                            }
                        }
                    } else if (s.containsInvokeExpr()) {
                        InvokeExpr ie = s.getInvokeExpr();
                        if (ie instanceof InstanceInvokeExpr) {
                            InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                            if (iie.getBase() == value) {
                                replace = true;
                            }
                        }
                    }
                    if (replace) {
                        unitToReplaceByException.add(u);
                    }
                }
            }
        }
        for (Unit u2 : unitToReplaceByException) {
            soot.dexpler.Util.addExceptionAfterUnit(b, "java.lang.NullPointerException", u2, "This statement would have triggered an Exception: " + u2);
            b.getUnits().remove(u2);
        }
        DeadAssignmentEliminator.v().transform(b);
        UnusedLocalEliminator.v().transform(b);
    }

    private void compareTypeAssigners(JimpleBody jb, boolean useOlderTypeAssigner) {
        JimpleBody oldJb;
        long oldTime;
        long newTime;
        JimpleBody newJb;
        int cmp;
        int size = jb.getUnits().size();
        if (useOlderTypeAssigner) {
            newJb = (JimpleBody) jb.clone();
            long newTime2 = System.currentTimeMillis();
            new soot.jimple.toolkits.typing.fast.TypeResolver(newJb).inferTypes();
            newTime = System.currentTimeMillis() - newTime2;
            long oldTime2 = System.currentTimeMillis();
            TypeResolver.resolve(jb, Scene.v());
            oldTime = System.currentTimeMillis() - oldTime2;
            oldJb = jb;
        } else {
            oldJb = (JimpleBody) jb.clone();
            long oldTime3 = System.currentTimeMillis();
            TypeResolver.resolve(oldJb, Scene.v());
            oldTime = System.currentTimeMillis() - oldTime3;
            long newTime3 = System.currentTimeMillis();
            new soot.jimple.toolkits.typing.fast.TypeResolver(jb).inferTypes();
            newTime = System.currentTimeMillis() - newTime3;
            newJb = jb;
        }
        if (newJb.getLocals().size() < oldJb.getLocals().size()) {
            cmp = 2;
        } else if (newJb.getLocals().size() > oldJb.getLocals().size()) {
            cmp = -2;
        } else {
            cmp = compareTypings(oldJb, newJb);
        }
        logger.debug("cmp;" + jb.getMethod() + ";" + size + ";" + oldTime + ";" + newTime + ";" + cmp);
    }

    /* JADX WARN: Removed duplicated region for block: B:3:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean typingFailed(soot.jimple.JimpleBody r4) {
        /*
            r3 = this;
            soot.UnknownType r0 = soot.UnknownType.v()
            r5 = r0
            soot.ErroneousType r0 = soot.ErroneousType.v()
            r6 = r0
            r0 = r4
            soot.util.Chain r0 = r0.getLocals()
            java.util.Iterator r0 = r0.iterator()
            r8 = r0
            goto L3f
        L16:
            r0 = r8
            java.lang.Object r0 = r0.next()
            soot.Local r0 = (soot.Local) r0
            r7 = r0
            r0 = r7
            soot.Type r0 = r0.getType()
            r9 = r0
            r0 = r5
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L3d
            r0 = r6
            r1 = r9
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3f
        L3d:
            r0 = 1
            return r0
        L3f:
            r0 = r8
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L16
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.typing.TypeAssigner.typingFailed(soot.jimple.JimpleBody):boolean");
    }

    private static int compareTypings(JimpleBody a, JimpleBody b) {
        int r = 0;
        Iterator<Local> ib = b.getLocals().iterator();
        for (Local v : a.getLocals()) {
            Type ta = v.getType();
            Type tb = ib.next().getType();
            if (!soot.jimple.toolkits.typing.fast.TypeResolver.typesEqual(ta, tb) && (!(ta instanceof CharType) || (!(tb instanceof ByteType) && !(tb instanceof ShortType)))) {
                if (!(tb instanceof CharType) || (!(ta instanceof ByteType) && !(ta instanceof ShortType))) {
                    if (AugHierarchy.ancestor_(ta, tb)) {
                        if (r == -1) {
                            return 3;
                        }
                        r = 1;
                    } else if (!AugHierarchy.ancestor_(tb, ta) || r == 1) {
                        return 3;
                    } else {
                        r = -1;
                    }
                }
            }
        }
        return r;
    }
}
