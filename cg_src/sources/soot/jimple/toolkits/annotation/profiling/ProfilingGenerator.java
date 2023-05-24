package soot.jimple.toolkits.annotation.profiling;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.JavaMethods;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.dotnet.members.DotnetMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.options.ProfilingOptions;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/profiling/ProfilingGenerator.class */
public class ProfilingGenerator extends BodyTransformer {
    public String mainSignature;

    public ProfilingGenerator(Singletons.Global g) {
        this.mainSignature = Options.v().src_prec() != 7 ? JavaMethods.SIG_MAIN : DotnetMethod.MAIN_METHOD_SIGNATURE;
    }

    public static ProfilingGenerator v() {
        return G.v().soot_jimple_toolkits_annotation_profiling_ProfilingGenerator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map opts) {
        ProfilingOptions options = new ProfilingOptions(opts);
        if (options.notmainentry()) {
            this.mainSignature = "long runBenchmark(java.lang.String[])";
        }
        SootMethod m = body.getMethod();
        SootClass counterClass = Scene.v().loadClassAndSupport("MultiCounter");
        SootMethod reset = counterClass.getMethod("void reset()");
        SootMethod report = counterClass.getMethod("void report()");
        boolean isMainMethod = m.getSubSignature().equals(this.mainSignature);
        Chain units = body.getUnits();
        if (isMainMethod) {
            units.addFirst(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(reset.makeRef())));
        }
        Iterator stmtIt = body.getUnits().snapshotIterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            if (stmt instanceof InvokeStmt) {
                InvokeExpr iexpr = ((InvokeStmt) stmt).getInvokeExpr();
                if (iexpr instanceof StaticInvokeExpr) {
                    SootMethod tempm = ((StaticInvokeExpr) iexpr).getMethod();
                    if (tempm.getSignature().equals("<java.lang.System: void exit(int)>")) {
                        units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(report.makeRef())), (InvokeStmt) stmt);
                    }
                }
            } else if (isMainMethod && ((stmt instanceof ReturnStmt) || (stmt instanceof ReturnVoidStmt))) {
                units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(report.makeRef())), (InvokeStmt) stmt);
            }
        }
    }
}
