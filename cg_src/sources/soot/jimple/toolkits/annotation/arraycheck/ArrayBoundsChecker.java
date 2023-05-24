package soot.jimple.toolkits.annotation.arraycheck;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.tags.ArrayCheckTag;
import soot.options.ABCOptions;
import soot.options.Options;
import soot.tagkit.ColorTag;
import soot.tagkit.KeyTag;
import soot.tagkit.Tag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/ArrayBoundsChecker.class */
public class ArrayBoundsChecker extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ArrayBoundsChecker.class);
    protected boolean takeClassField = false;
    protected boolean takeFieldRef = false;
    protected boolean takeArrayRef = false;
    protected boolean takeCSE = false;
    protected boolean takeRectArray = false;
    protected boolean addColorTags = false;

    public ArrayBoundsChecker(Singletons.Global g) {
    }

    public static ArrayBoundsChecker v() {
        return G.v().soot_jimple_toolkits_annotation_arraycheck_ArrayBoundsChecker();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map opts) {
        ABCOptions options = new ABCOptions(opts);
        if (options.with_all()) {
            this.takeClassField = true;
            this.takeFieldRef = true;
            this.takeArrayRef = true;
            this.takeCSE = true;
            this.takeRectArray = true;
        } else {
            this.takeClassField = options.with_classfield();
            this.takeFieldRef = options.with_fieldref();
            this.takeArrayRef = options.with_arrayref();
            this.takeCSE = options.with_cse();
            this.takeRectArray = options.with_rectarray();
        }
        this.addColorTags = options.add_color_tags();
        SootMethod m = body.getMethod();
        Date start = new Date();
        if (Options.v().verbose()) {
            logger.debug("[abc] Analyzing array bounds information for " + m.getName());
            logger.debug("[abc] Started on " + start);
        }
        ArrayBoundsCheckerAnalysis analysis = null;
        if (hasArrayLocals(body)) {
            analysis = new ArrayBoundsCheckerAnalysis(body, this.takeClassField, this.takeFieldRef, this.takeArrayRef, this.takeCSE, this.takeRectArray);
        }
        SootMethod increase = null;
        if (options.profiling()) {
            SootClass counterClass = Scene.v().loadClassAndSupport("MultiCounter");
            increase = counterClass.getMethod("void increase(int)");
        }
        Chain units = body.getUnits();
        IntContainer zero = new IntContainer(0);
        Iterator unitIt = units.snapshotIterator();
        while (unitIt.hasNext()) {
            Stmt stmt = (Stmt) unitIt.next();
            if (stmt.containsArrayRef()) {
                ArrayRef aref = stmt.getArrayRef();
                WeightedDirectedSparseGraph vgraph = (WeightedDirectedSparseGraph) analysis.getFlowBefore(stmt);
                int res = interpretGraph(vgraph, aref, stmt, zero);
                boolean lowercheck = true;
                boolean uppercheck = true;
                if (res == 0) {
                    lowercheck = true;
                    uppercheck = true;
                } else if (res == 1) {
                    lowercheck = true;
                    uppercheck = false;
                } else if (res == 2) {
                    lowercheck = false;
                    uppercheck = true;
                } else if (res == 3) {
                    lowercheck = false;
                    uppercheck = false;
                }
                if (this.addColorTags) {
                    if (res == 0) {
                        aref.getIndexBox().addTag(new ColorTag(255, 0, 0, false, ArrayCheckTag.NAME));
                    } else if (res == 1) {
                        aref.getIndexBox().addTag(new ColorTag(255, 248, 35, false, ArrayCheckTag.NAME));
                    } else if (res == 2) {
                        aref.getIndexBox().addTag(new ColorTag(255, 163, 0, false, ArrayCheckTag.NAME));
                    } else if (res == 3) {
                        aref.getIndexBox().addTag(new ColorTag(45, 255, 84, false, ArrayCheckTag.NAME));
                    }
                    SootClass bodyClass = body.getMethod().getDeclaringClass();
                    boolean keysAdded = false;
                    for (Object next : bodyClass.getTags()) {
                        if ((next instanceof KeyTag) && ((KeyTag) next).analysisType().equals(ArrayCheckTag.NAME)) {
                            keysAdded = true;
                        }
                    }
                    if (!keysAdded) {
                        bodyClass.addTag(new KeyTag(255, 0, 0, "ArrayBounds: Unsafe Lower and Unsafe Upper", ArrayCheckTag.NAME));
                        bodyClass.addTag(new KeyTag(255, 248, 35, "ArrayBounds: Unsafe Lower and Safe Upper", ArrayCheckTag.NAME));
                        bodyClass.addTag(new KeyTag(255, 163, 0, "ArrayBounds: Safe Lower and Unsafe Upper", ArrayCheckTag.NAME));
                        bodyClass.addTag(new KeyTag(45, 255, 84, "ArrayBounds: Safe Lower and Safe Upper", ArrayCheckTag.NAME));
                    }
                }
                if (options.profiling()) {
                    int lowercounter = 0;
                    if (!lowercheck) {
                        lowercounter = 1;
                    }
                    units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(increase.makeRef(), IntConstant.v(lowercounter))), (InvokeStmt) stmt);
                    int uppercounter = 2;
                    if (!uppercheck) {
                        uppercounter = 3;
                    }
                    units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(increase.makeRef(), IntConstant.v(uppercounter))), (InvokeStmt) stmt);
                } else {
                    Tag checkTag = new ArrayCheckTag(lowercheck, uppercheck);
                    stmt.addTag(checkTag);
                }
            }
        }
        if (this.addColorTags && this.takeRectArray) {
            RectangularArrayFinder raf = RectangularArrayFinder.v();
            for (ValueBox vb : body.getUseAndDefBoxes()) {
                Value v = vb.getValue();
                if (v instanceof Local) {
                    Type t = v.getType();
                    if (t instanceof ArrayType) {
                        ArrayType at = (ArrayType) t;
                        if (at.numDimensions > 1) {
                            vb.addTag(new ColorTag(raf.isRectangular(new MethodLocal(m, (Local) v)) ? 1 : 0));
                        }
                    }
                }
            }
        }
        Date finish = new Date();
        if (Options.v().verbose()) {
            long runtime = finish.getTime() - start.getTime();
            logger.debug("[abc] ended on " + finish + ". It took " + (runtime / 60000) + " min. " + ((runtime % 60000) / 1000) + " sec.");
        }
    }

    private boolean hasArrayLocals(Body body) {
        for (Local local : body.getLocals()) {
            if (local.getType() instanceof ArrayType) {
                return true;
            }
        }
        return false;
    }

    protected int interpretGraph(WeightedDirectedSparseGraph vgraph, ArrayRef aref, Stmt stmt, IntContainer zero) {
        boolean lowercheck = true;
        boolean uppercheck = true;
        if (Options.v().debug() && !vgraph.makeShortestPathGraph()) {
            logger.debug(stmt + " :");
            logger.debug(new StringBuilder().append(vgraph).toString());
        }
        Value base = aref.getBase();
        Value index = aref.getIndex();
        if (index instanceof IntConstant) {
            int indexv = ((IntConstant) index).value;
            if (vgraph.hasEdge(base, zero)) {
                int alength = vgraph.edgeWeight(base, zero);
                if ((-alength) > indexv) {
                    uppercheck = false;
                }
            }
            if (indexv >= 0) {
                lowercheck = false;
            }
        } else {
            if (vgraph.hasEdge(base, index)) {
                int upperdistance = vgraph.edgeWeight(base, index);
                if (upperdistance < 0) {
                    uppercheck = false;
                }
            }
            if (vgraph.hasEdge(index, zero)) {
                int lowerdistance = vgraph.edgeWeight(index, zero);
                if (lowerdistance <= 0) {
                    lowercheck = false;
                }
            }
        }
        if (lowercheck && uppercheck) {
            return 0;
        }
        if (lowercheck && !uppercheck) {
            return 1;
        }
        if (!lowercheck && uppercheck) {
            return 2;
        }
        return 3;
    }
}
