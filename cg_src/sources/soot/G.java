package soot;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.coffi.Utf8_Enumeration;
import soot.dava.internal.SET.SETBasicBlock;
import soot.dava.internal.SET.SETNode;
import soot.dexpler.DalvikThrowAnalysis;
import soot.jimple.spark.pag.MethodPAG;
import soot.jimple.spark.pag.Parm;
import soot.jimple.spark.sets.P2SetFactory;
import soot.jimple.toolkits.annotation.arraycheck.Array2ndDimensionSymbol;
import soot.jimple.toolkits.pointer.UnionFactory;
import soot.jimple.toolkits.pointer.util.NativeHelper;
import soot.jimple.toolkits.typing.ClassHierarchy;
import soot.toolkits.astmetrics.ClassData;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/G.class */
public class G extends Singletons {
    private static GlobalObjectGetter objectGetter = new GlobalObjectGetter() { // from class: soot.G.1
        private G instance = new G();

        @Override // soot.G.GlobalObjectGetter
        public G getG() {
            return this.instance;
        }

        @Override // soot.G.GlobalObjectGetter
        public void reset() {
            this.instance = new G();
        }
    };
    public boolean ASTAnalysis_modified;
    public P2SetFactory newSetFactory;
    public P2SetFactory oldSetFactory;
    public boolean Timer_isGarbageCollecting;
    public int Timer_count;
    public boolean ASTTransformations_modified;
    public boolean ASTIfElseFlipped;
    public boolean SootMethodAddedByDava;
    @Deprecated
    public PrintStream out = System.out;
    public long coffi_BasicBlock_ids = 0;
    public Utf8_Enumeration coffi_CONSTANT_Utf8_info_e1 = new Utf8_Enumeration();
    public Utf8_Enumeration coffi_CONSTANT_Utf8_info_e2 = new Utf8_Enumeration();
    public int SETNodeLabel_uniqueId = 0;
    public HashMap<SETNode, SETBasicBlock> SETBasicBlock_binding = new HashMap<>();
    public NativeHelper NativeHelper_helper = null;
    public Map<Pair<SootMethod, Integer>, Parm> Parm_pairToElement = new HashMap();
    public int SparkNativeHelper_tempVar = 0;
    public int PaddleNativeHelper_tempVar = 0;
    public boolean PointsToSetInternal_warnedAlready = false;
    public HashMap<SootMethod, MethodPAG> MethodPAG_methodToPag = new HashMap<>();
    public Set MethodRWSet_allGlobals = new HashSet();
    public Set MethodRWSet_allFields = new HashSet();
    public int GeneralConstObject_counter = 0;
    public UnionFactory Union_factory = null;
    public HashMap<Object, Array2ndDimensionSymbol> Array2ndDimensionSymbol_pool = new HashMap<>();
    public List<Timer> Timer_outstandingTimers = new ArrayList();
    public Timer Timer_forcedGarbageCollectionTimer = new Timer("gc");
    public final Map<Scene, ClassHierarchy> ClassHierarchy_classHierarchyMap = new HashMap();
    public final Map<MethodContext, MethodContext> MethodContext_map = new HashMap();
    public DalvikThrowAnalysis interproceduralDalvikThrowAnalysis = null;
    public ArrayList<SootClass> SootClassNeedsDavaSuperHandlerClass = new ArrayList<>();
    public ArrayList<SootMethod> SootMethodsAdded = new ArrayList<>();
    public ArrayList<ClassData> ASTMetricsData = new ArrayList<>();

    /* loaded from: gencallgraphv3.jar:soot/G$GlobalObjectGetter.class */
    public interface GlobalObjectGetter {
        G getG();

        void reset();
    }

    public static G v() {
        return objectGetter.getG();
    }

    public static void reset() {
        objectGetter.reset();
    }

    public static void setGlobalObjectGetter(GlobalObjectGetter newGetter) {
        objectGetter = newGetter;
    }

    /* loaded from: gencallgraphv3.jar:soot/G$Global.class */
    public class Global {
        public Global() {
        }
    }

    public DalvikThrowAnalysis interproceduralDalvikThrowAnalysis() {
        if (this.interproceduralDalvikThrowAnalysis == null) {
            this.interproceduralDalvikThrowAnalysis = new DalvikThrowAnalysis(this.g, true);
        }
        return this.interproceduralDalvikThrowAnalysis;
    }

    public void resetSpark() {
        Method[] declaredMethods;
        for (Method m : getClass().getSuperclass().getDeclaredMethods()) {
            if (m.getName().startsWith("release_soot_jimple_spark_")) {
                try {
                    m.invoke(this, new Object[0]);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e2) {
                    throw new RuntimeException(e2);
                } catch (InvocationTargetException e3) {
                    throw new RuntimeException(e3);
                }
            }
        }
        this.MethodPAG_methodToPag.clear();
        this.MethodRWSet_allFields.clear();
        this.MethodRWSet_allGlobals.clear();
        this.newSetFactory = null;
        this.oldSetFactory = null;
        this.Parm_pairToElement.clear();
        release_soot_jimple_toolkits_callgraph_VirtualCalls();
    }
}
