package soot.jimple.infoflow.android.callbacks;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/FastCallbackAnalyzer.class */
public class FastCallbackAnalyzer extends AbstractCallbackAnalyzer {
    public FastCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses) throws IOException {
        super(config, entryPointClasses);
    }

    public FastCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, String callbackFile) throws IOException {
        super(config, entryPointClasses, callbackFile);
    }

    public FastCallbackAnalyzer(InfoflowAndroidConfiguration config, Set<SootClass> entryPointClasses, Set<String> androidCallbacks) throws IOException {
        super(config, entryPointClasses, androidCallbacks);
    }

    @Override // soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer
    public void collectCallbackMethods() {
        super.collectCallbackMethods();
        this.logger.info("Collecting callbacks in FAST mode...");
        findClassLayoutMappings();
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (sc.isConcrete()) {
                for (SootMethod sm : sc.getMethods()) {
                    if (sm.isConcrete()) {
                        analyzeMethodForCallbackRegistrations(null, sm);
                        analyzeMethodForDynamicBroadcastReceiver(sm);
                        analyzeMethodForServiceConnection(sm);
                    }
                }
                analyzeMethodOverrideCallbacks(sc);
            }
        }
    }

    private void findClassLayoutMappings() {
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            if (sc.isConcrete()) {
                for (SootMethod sm : sc.getMethods()) {
                    if (sm.isConcrete()) {
                        Iterator<Unit> it = sm.retrieveActiveBody().getUnits().iterator();
                        while (it.hasNext()) {
                            Unit u = it.next();
                            if (u instanceof Stmt) {
                                Stmt stmt = (Stmt) u;
                                if (stmt.containsInvokeExpr()) {
                                    InvokeExpr inv = stmt.getInvokeExpr();
                                    if (invokesSetContentView(inv)) {
                                        for (Value val : inv.getArgs()) {
                                            Integer intValue = (Integer) this.valueProvider.getValue(sm, stmt, val, Integer.class);
                                            if (intValue != null) {
                                                this.layoutClasses.put(sm.getDeclaringClass(), intValue);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.android.callbacks.AbstractCallbackAnalyzer
    public void excludeEntryPoint(SootClass entryPoint) {
    }
}
