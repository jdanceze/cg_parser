package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.resource.spi.work.WorkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.JimpleBody;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JNopStmt;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.pointer.Union;
import soot.jimple.toolkits.pointer.UnionFactory;
import soot.jimple.toolkits.thread.ThreadLocalObjectsAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.Pair;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/SynchronizedRegionFinder.class */
public class SynchronizedRegionFinder extends ForwardFlowAnalysis<Unit, FlowSet<SynchronizedRegionFlowPair>> {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizedRegionFinder.class);
    FlowSet<SynchronizedRegionFlowPair> emptySet;
    Map unitToGenerateSet;
    Body body;
    Chain<Unit> units;
    SootMethod method;
    ExceptionalUnitGraph egraph;
    LocalUses slu;
    CriticalSectionAwareSideEffectAnalysis tasea;
    List<Object> prepUnits;
    CriticalSection methodTn;
    public boolean optionPrintDebug;
    public boolean optionOpenNesting;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SynchronizedRegionFinder(UnitGraph graph, Body b, boolean optionPrintDebug, boolean optionOpenNesting, ThreadLocalObjectsAnalysis tlo) {
        super(graph);
        this.emptySet = new ArraySparseSet();
        this.optionPrintDebug = false;
        this.optionOpenNesting = true;
        this.optionPrintDebug = optionPrintDebug;
        this.optionOpenNesting = optionOpenNesting;
        this.body = b;
        this.units = b.getUnits();
        this.method = this.body.getMethod();
        if (graph instanceof ExceptionalUnitGraph) {
            this.egraph = (ExceptionalUnitGraph) graph;
        } else {
            this.egraph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
        }
        this.slu = LocalUses.Factory.newLocalUses(this.egraph);
        if (G.v().Union_factory == null) {
            G.v().Union_factory = new UnionFactory() { // from class: soot.jimple.toolkits.thread.synchronization.SynchronizedRegionFinder.1
                @Override // soot.jimple.toolkits.pointer.UnionFactory
                public Union newUnion() {
                    return FullObjectSet.v();
                }
            };
        }
        this.tasea = new CriticalSectionAwareSideEffectAnalysis(Scene.v().getPointsToAnalysis(), Scene.v().getCallGraph(), null, tlo);
        this.prepUnits = new ArrayList();
        this.methodTn = null;
        if (this.method.isSynchronized()) {
            this.methodTn = new CriticalSection(true, this.method, 1);
            this.methodTn.beginning = ((JimpleBody) this.body).getFirstNonIdentityStmt();
        }
        doAnalysis();
        if (this.method.isSynchronized() && this.methodTn != null) {
            Iterator<Unit> tailIt = graph.getTails().iterator();
            while (tailIt.hasNext()) {
                Stmt tail = (Stmt) tailIt.next();
                this.methodTn.earlyEnds.add(new Pair<>(tail, null));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public FlowSet<SynchronizedRegionFlowPair> newInitialFlow() {
        FlowSet<SynchronizedRegionFlowPair> ret = this.emptySet.mo2534clone();
        if (this.method.isSynchronized() && this.methodTn != null) {
            ret.add(new SynchronizedRegionFlowPair(this.methodTn, true));
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.FlowAnalysis
    public void flowThrough(FlowSet<SynchronizedRegionFlowPair> in, Unit unit, FlowSet<SynchronizedRegionFlowPair> out) {
        Stmt stmt = (Stmt) unit;
        copy(in, out);
        if (unit instanceof AssignStmt) {
            boolean isPrep = true;
            Iterator<UnitValueBoxPair> uses = this.slu.getUsesOf(unit).iterator();
            if (!uses.hasNext()) {
                isPrep = false;
            }
            while (true) {
                if (!uses.hasNext()) {
                    break;
                }
                UnitValueBoxPair use = uses.next();
                Unit useStmt = use.getUnit();
                if (!(useStmt instanceof EnterMonitorStmt) && !(useStmt instanceof ExitMonitorStmt)) {
                    isPrep = false;
                    break;
                }
            }
            if (isPrep) {
                this.prepUnits.add(unit);
                if (this.optionPrintDebug) {
                    logger.debug("prep: " + unit.toString());
                    return;
                }
                return;
            }
        }
        boolean addSelf = unit instanceof EnterMonitorStmt;
        int nestLevel = 0;
        for (SynchronizedRegionFlowPair srfp : out) {
            if (srfp.tn.nestLevel > nestLevel && srfp.inside) {
                nestLevel = srfp.tn.nestLevel;
            }
        }
        RWSet stmtRead = null;
        RWSet stmtWrite = null;
        boolean printed = false;
        for (SynchronizedRegionFlowPair srfp2 : out) {
            CriticalSection tn = srfp2.tn;
            if (tn.entermonitor == stmt) {
                srfp2.inside = true;
                addSelf = false;
            }
            if (srfp2.inside && (tn.nestLevel == nestLevel || !this.optionOpenNesting)) {
                printed = true;
                if (!tn.units.contains(unit)) {
                    tn.units.add(unit);
                }
                if (stmt.containsInvokeExpr()) {
                    String InvokeSig = stmt.getInvokeExpr().getMethod().getSubSignature();
                    if ((InvokeSig.equals("void notify()") || InvokeSig.equals("void notifyAll()")) && tn.nestLevel == nestLevel) {
                        if (!tn.notifys.contains(unit)) {
                            tn.notifys.add(unit);
                        }
                        if (this.optionPrintDebug) {
                            logger.debug("{x,x} ");
                        }
                    } else if ((InvokeSig.equals("void wait()") || InvokeSig.equals("void wait(long)") || InvokeSig.equals("void wait(long,int)")) && tn.nestLevel == nestLevel) {
                        if (!tn.waits.contains(unit)) {
                            tn.waits.add(unit);
                        }
                        if (this.optionPrintDebug) {
                            logger.debug("{x,x} ");
                        }
                    }
                    if (!tn.invokes.contains(unit)) {
                        tn.invokes.add(unit);
                        if (this.optionPrintDebug) {
                            stmtRead = this.tasea.readSet(tn.method, stmt, tn, new HashSet());
                            stmtWrite = this.tasea.writeSet(tn.method, stmt, tn, new HashSet());
                            logger.debug("{");
                            if (stmtRead != null) {
                                logger.debug(new StringBuilder().append((stmtRead.getGlobals() != null ? stmtRead.getGlobals().size() : 0) + (stmtRead.getFields() != null ? stmtRead.getFields().size() : 0)).toString());
                            } else {
                                logger.debug(WorkException.UNDEFINED);
                            }
                            logger.debug(",");
                            if (stmtWrite != null) {
                                logger.debug(new StringBuilder().append((stmtWrite.getGlobals() != null ? stmtWrite.getGlobals().size() : 0) + (stmtWrite.getFields() != null ? stmtWrite.getFields().size() : 0)).toString());
                            } else {
                                logger.debug(WorkException.UNDEFINED);
                            }
                            logger.debug("} ");
                        }
                    }
                } else if ((unit instanceof ExitMonitorStmt) && tn.nestLevel == nestLevel) {
                    srfp2.inside = false;
                    Stmt nextUnit = stmt;
                    do {
                        nextUnit = (Stmt) this.units.getSuccOf(nextUnit);
                    } while (nextUnit instanceof JNopStmt);
                    if ((nextUnit instanceof ReturnStmt) || (nextUnit instanceof ReturnVoidStmt) || (nextUnit instanceof ExitMonitorStmt)) {
                        tn.earlyEnds.add(new Pair<>(nextUnit, stmt));
                    } else if (nextUnit instanceof GotoStmt) {
                        tn.end = new Pair<>(nextUnit, stmt);
                        tn.after = (Stmt) ((GotoStmt) nextUnit).getTarget();
                    } else if (nextUnit instanceof ThrowStmt) {
                        tn.exceptionalEnd = new Pair<>(nextUnit, stmt);
                    } else {
                        throw new RuntimeException("Unknown bytecode pattern: exitmonitor not followed by return, exitmonitor, goto, or throw");
                    }
                    if (this.optionPrintDebug) {
                        logger.debug("[0,0] ");
                    }
                } else {
                    HashSet uses2 = new HashSet();
                    stmtRead = this.tasea.readSet(this.method, stmt, tn, uses2);
                    stmtWrite = this.tasea.writeSet(this.method, stmt, tn, uses2);
                    tn.read.union(stmtRead);
                    tn.write.union(stmtWrite);
                    if (this.optionPrintDebug) {
                        logger.debug("[");
                        if (stmtRead != null) {
                            logger.debug(new StringBuilder().append((stmtRead.getGlobals() != null ? stmtRead.getGlobals().size() : 0) + (stmtRead.getFields() != null ? stmtRead.getFields().size() : 0)).toString());
                        } else {
                            logger.debug(WorkException.UNDEFINED);
                        }
                        logger.debug(",");
                        if (stmtWrite != null) {
                            logger.debug(new StringBuilder().append((stmtWrite.getGlobals() != null ? stmtWrite.getGlobals().size() : 0) + (stmtWrite.getFields() != null ? stmtWrite.getFields().size() : 0)).toString());
                        } else {
                            logger.debug(WorkException.UNDEFINED);
                        }
                        logger.debug("] ");
                    }
                }
            }
        }
        if (this.optionPrintDebug) {
            if (!printed) {
                logger.debug("[0,0] ");
            }
            logger.debug(unit.toString());
            if (stmt.containsInvokeExpr() && stmt.getInvokeExpr().getMethod().getDeclaringClass().toString().startsWith("java.") && stmtRead != null && stmtWrite != null && stmtRead.size() < 25 && stmtWrite.size() < 25) {
                logger.debug("        Read/Write Set for LibInvoke:");
                logger.debug("Read Set:(" + stmtRead.size() + ")" + stmtRead.toString().replaceAll("\n", "\n        "));
                logger.debug("Write Set:(" + stmtWrite.size() + ")" + stmtWrite.toString().replaceAll("\n", "\n        "));
            }
        }
        if (addSelf) {
            CriticalSection newTn = new CriticalSection(false, this.method, nestLevel + 1);
            newTn.entermonitor = stmt;
            newTn.beginning = (Stmt) this.units.getSuccOf(stmt);
            if (stmt instanceof EnterMonitorStmt) {
                newTn.origLock = ((EnterMonitorStmt) stmt).getOp();
            }
            if (this.optionPrintDebug) {
                logger.debug("Transaction found in method: " + newTn.method.toString());
            }
            out.add(new SynchronizedRegionFlowPair(newTn, true));
            Iterator<Object> prepUnitsIt = this.prepUnits.iterator();
            while (prepUnitsIt.hasNext()) {
                Unit prepUnit = (Unit) prepUnitsIt.next();
                for (UnitValueBoxPair use2 : this.slu.getUsesOf(prepUnit)) {
                    if (use2.getUnit() == unit) {
                        newTn.prepStmt = (Stmt) prepUnit;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void merge(FlowSet<SynchronizedRegionFlowPair> inSet1, FlowSet<SynchronizedRegionFlowPair> inSet2, FlowSet<SynchronizedRegionFlowPair> outSet) {
        inSet1.union(inSet2, outSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(FlowSet<SynchronizedRegionFlowPair> sourceSet, FlowSet<SynchronizedRegionFlowPair> destSet) {
        destSet.clear();
        for (SynchronizedRegionFlowPair tfp : sourceSet) {
            destSet.add(tfp.m2965clone());
        }
    }
}
