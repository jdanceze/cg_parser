package soot.jimple.toolkits.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Ref;
import soot.jimple.toolkits.infoflow.CallLocalityContext;
import soot.jimple.toolkits.infoflow.ClassInfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.ClassLocalObjectsAnalysis;
import soot.jimple.toolkits.infoflow.InfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.LocalObjectsAnalysis;
import soot.jimple.toolkits.infoflow.SmartMethodInfoFlowAnalysis;
import soot.jimple.toolkits.infoflow.SmartMethodLocalObjectsAnalysis;
import soot.jimple.toolkits.infoflow.UseFinder;
import soot.jimple.toolkits.thread.mhp.MhpTester;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/ThreadLocalObjectsAnalysis.class */
public class ThreadLocalObjectsAnalysis extends LocalObjectsAnalysis implements IThreadLocalObjectsAnalysis {
    MhpTester mhp;
    List<AbstractRuntimeThread> threads;
    InfoFlowAnalysis primitiveDfa;
    Map valueCache;
    Map fieldCache;
    Map invokeCache;
    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalObjectsAnalysis.class);
    static boolean printDebug = false;

    public ThreadLocalObjectsAnalysis(MhpTester mhp) {
        super(new InfoFlowAnalysis(false, true, printDebug));
        this.mhp = mhp;
        this.threads = mhp.getThreads();
        this.primitiveDfa = new InfoFlowAnalysis(true, true, printDebug);
        this.valueCache = new HashMap();
        this.fieldCache = new HashMap();
        this.invokeCache = new HashMap();
    }

    public void precompute() {
        for (AbstractRuntimeThread thread : this.threads) {
            for (Object item : thread.getRunMethods()) {
                SootMethod runMethod = (SootMethod) item;
                if (runMethod.getDeclaringClass().isApplicationClass()) {
                    getClassLocalObjectsAnalysis(runMethod.getDeclaringClass());
                }
            }
        }
    }

    @Override // soot.jimple.toolkits.infoflow.LocalObjectsAnalysis
    protected ClassLocalObjectsAnalysis newClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, UseFinder uf, SootClass sc) {
        List<SootMethod> runMethods = new ArrayList<>();
        for (AbstractRuntimeThread thread : this.threads) {
            Iterator<Object> runMethodsIt = thread.getRunMethods().iterator();
            while (runMethodsIt.hasNext()) {
                SootMethod runMethod = (SootMethod) runMethodsIt.next();
                if (runMethod.getDeclaringClass() == sc) {
                    runMethods.add(runMethod);
                }
            }
        }
        return new ClassLocalObjectsAnalysis(loa, dfa, this.primitiveDfa, uf, sc, runMethods);
    }

    @Override // soot.jimple.toolkits.thread.IThreadLocalObjectsAnalysis
    public boolean isObjectThreadLocal(Value localOrRef, SootMethod sm) {
        if (this.threads.size() <= 1) {
            return true;
        }
        if (printDebug) {
            logger.debug("- " + localOrRef + " in " + sm + " is...");
        }
        Collection<AbstractRuntimeThread> mhpThreads = this.mhp.getThreads();
        if (mhpThreads != null) {
            for (AbstractRuntimeThread thread : mhpThreads) {
                for (Object meth : thread.getRunMethods()) {
                    SootMethod runMethod = (SootMethod) meth;
                    if (runMethod.getDeclaringClass().isApplicationClass() && !isObjectLocalToContext(localOrRef, sm, runMethod)) {
                        if (printDebug) {
                            logger.debug("  THREAD-SHARED (simpledfa " + ClassInfoFlowAnalysis.methodCount + " smartdfa " + SmartMethodInfoFlowAnalysis.counter + " smartloa " + SmartMethodLocalObjectsAnalysis.counter + ")");
                            return false;
                        }
                        return false;
                    }
                }
            }
        }
        if (printDebug) {
            logger.debug("  THREAD-LOCAL (simpledfa " + ClassInfoFlowAnalysis.methodCount + " smartdfa " + SmartMethodInfoFlowAnalysis.counter + " smartloa " + SmartMethodLocalObjectsAnalysis.counter + ")");
            return true;
        }
        return true;
    }

    public boolean hasNonThreadLocalEffects(SootMethod containingMethod, InvokeExpr ie) {
        if (this.threads.size() <= 1) {
            return true;
        }
        return true;
    }

    public List escapesThrough(Value sharedValue, SootMethod containingMethod) {
        EquivalentValue sharedValueEqVal;
        List ret = new ArrayList();
        for (AbstractRuntimeThread thread : this.mhp.getThreads()) {
            for (Object meth : thread.getRunMethods()) {
                SootMethod runMethod = (SootMethod) meth;
                if (runMethod.getDeclaringClass().isApplicationClass() && !isObjectLocalToContext(sharedValue, containingMethod, runMethod)) {
                    ClassLocalObjectsAnalysis cloa = getClassLocalObjectsAnalysis(containingMethod.getDeclaringClass());
                    CallLocalityContext clc = cloa.getMergedContext(containingMethod);
                    SmartMethodInfoFlowAnalysis smifa = this.dfa.getMethodInfoFlowAnalysis(containingMethod);
                    if (sharedValue instanceof InstanceFieldRef) {
                        sharedValueEqVal = InfoFlowAnalysis.getNodeForFieldRef(containingMethod, ((FieldRef) sharedValue).getField());
                    } else {
                        sharedValueEqVal = new EquivalentValue(sharedValue);
                    }
                    List<EquivalentValue> sources = smifa.sourcesOf(sharedValueEqVal);
                    for (EquivalentValue source : sources) {
                        if ((source.getValue() instanceof Ref) && clc != null && !clc.isFieldLocal(source)) {
                            ret.add(source);
                        }
                    }
                }
            }
        }
        return ret;
    }
}
