package soot.jimple.toolkits.infoflow;

import android.os.Environment;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.EquivalentValue;
import soot.RefLikeType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Value;
import soot.coffi.Instruction;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.MutableDirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/ClassLocalObjectsAnalysis.class */
public class ClassLocalObjectsAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(ClassLocalObjectsAnalysis.class);
    boolean printdfgs;
    LocalObjectsAnalysis loa;
    InfoFlowAnalysis dfa;
    InfoFlowAnalysis primitiveDfa;
    UseFinder uf;
    SootClass sootClass;
    Map<SootMethod, SmartMethodLocalObjectsAnalysis> methodToMethodLocalObjectsAnalysis;
    Map<SootMethod, CallLocalityContext> methodToContext;
    List<SootMethod> allMethods;
    List<SootMethod> externalMethods;
    List<SootMethod> internalMethods;
    List<SootMethod> entryMethods;
    List<SootField> allFields;
    List<SootField> externalFields;
    List<SootField> internalFields;
    ArrayList<SootField> localFields;
    ArrayList<SootField> sharedFields;
    ArrayList<SootField> localInnerFields;
    ArrayList<SootField> sharedInnerFields;

    public ClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, UseFinder uf, SootClass sootClass) {
        this(loa, dfa, null, uf, sootClass, null);
    }

    public ClassLocalObjectsAnalysis(LocalObjectsAnalysis loa, InfoFlowAnalysis dfa, InfoFlowAnalysis primitiveDfa, UseFinder uf, SootClass sootClass, List<SootMethod> entryMethods) {
        this.printdfgs = dfa.printDebug();
        this.loa = loa;
        this.dfa = dfa;
        this.primitiveDfa = primitiveDfa;
        this.uf = uf;
        this.sootClass = sootClass;
        this.methodToMethodLocalObjectsAnalysis = new HashMap();
        this.methodToContext = null;
        this.allMethods = null;
        this.externalMethods = null;
        this.internalMethods = null;
        this.entryMethods = entryMethods;
        this.allFields = null;
        this.externalFields = null;
        this.internalFields = null;
        this.localFields = null;
        this.sharedFields = null;
        this.localInnerFields = null;
        this.sharedInnerFields = null;
        logger.debug("[local-objects] Analyzing local objects for " + sootClass);
        logger.debug("[local-objects]   preparing class             " + new Date());
        prepare();
        logger.debug("[local-objects]   analyzing class             " + new Date());
        doAnalysis();
        logger.debug("[local-objects]   propagating over call graph " + new Date());
        propagate();
        logger.debug("[local-objects]   finished at                 " + new Date());
        logger.debug("[local-objects]   (#analyzed/#encountered): " + SmartMethodInfoFlowAnalysis.counter + "/" + ClassInfoFlowAnalysis.methodCount);
    }

    private void prepare() {
        this.allMethods = getAllReachableMethods(this.sootClass);
        this.externalMethods = this.uf.getExtMethods(this.sootClass);
        SootClass superclass = this.sootClass;
        if (superclass.hasSuperclass()) {
            superclass = superclass.getSuperclass();
        }
        while (superclass.hasSuperclass()) {
            if (superclass.isApplicationClass()) {
                this.externalMethods.addAll(this.uf.getExtMethods(superclass));
            }
            superclass = superclass.getSuperclass();
        }
        this.internalMethods = new ArrayList();
        for (SootMethod method : this.allMethods) {
            if (!this.externalMethods.contains(method)) {
                this.internalMethods.add(method);
            }
        }
        this.allFields = getAllFields(this.sootClass);
        this.externalFields = this.uf.getExtFields(this.sootClass);
        SootClass superclass2 = this.sootClass;
        if (superclass2.hasSuperclass()) {
            superclass2 = superclass2.getSuperclass();
        }
        while (superclass2.hasSuperclass()) {
            if (superclass2.isApplicationClass()) {
                this.externalFields.addAll(this.uf.getExtFields(superclass2));
            }
            superclass2 = superclass2.getSuperclass();
        }
        this.internalFields = new ArrayList();
        for (SootField field : this.allFields) {
            if (!this.externalFields.contains(field)) {
                this.internalFields.add(field);
            }
        }
    }

    public static List<SootMethod> getAllReachableMethods(SootClass sc) {
        ReachableMethods rm = Scene.v().getReachableMethods();
        List<SootMethod> allMethods = new ArrayList<>();
        Iterator methodsIt = sc.methodIterator();
        while (methodsIt.hasNext()) {
            SootMethod method = methodsIt.next();
            if (rm.contains(method)) {
                allMethods.add(method);
            }
        }
        SootClass superclass = sc;
        if (superclass.hasSuperclass()) {
            superclass = superclass.getSuperclass();
        }
        while (superclass.hasSuperclass()) {
            Iterator scMethodsIt = superclass.methodIterator();
            while (scMethodsIt.hasNext()) {
                SootMethod scMethod = scMethodsIt.next();
                if (rm.contains(scMethod)) {
                    allMethods.add(scMethod);
                }
            }
            superclass = superclass.getSuperclass();
        }
        return allMethods;
    }

    public static List<SootField> getAllFields(SootClass sc) {
        List<SootField> allFields = new ArrayList<>();
        for (SootField field : sc.getFields()) {
            allFields.add(field);
        }
        SootClass superclass = sc;
        if (superclass.hasSuperclass()) {
            superclass = superclass.getSuperclass();
        }
        while (superclass.hasSuperclass()) {
            for (SootField scField : superclass.getFields()) {
                allFields.add(scField);
            }
            superclass = superclass.getSuperclass();
        }
        return allFields;
    }

    private void doAnalysis() {
        MutableDirectedGraph dataFlowSummary;
        boolean fieldBecomesShared;
        MutableDirectedGraph dataFlowSummary2;
        boolean fieldBecomesShared2;
        MutableDirectedGraph dataFlowSummary3;
        this.localFields = new ArrayList<>();
        this.sharedFields = new ArrayList<>();
        for (SootField field : this.allFields) {
            if (fieldIsInitiallyLocal(field)) {
                this.localFields.add(field);
            } else {
                this.sharedFields.add(field);
            }
        }
        this.localInnerFields = new ArrayList<>();
        this.sharedInnerFields = new ArrayList<>();
        for (SootMethod method : this.allMethods) {
            if (this.primitiveDfa != null) {
                dataFlowSummary3 = this.primitiveDfa.getMethodInfoFlowSummary(method);
                if (this.printdfgs && method.getDeclaringClass().isApplicationClass()) {
                    logger.debug("Attempting to print graphs (will succeed only if ./dfg/ is a valid path)");
                    DirectedGraph primitiveGraph = this.primitiveDfa.getMethodInfoFlowAnalysis(method).getMethodAbbreviatedInfoFlowGraph();
                    InfoFlowAnalysis.printGraphToDotFile("dfg/" + method.getDeclaringClass().getShortName() + "_" + method.getName() + "_primitive", primitiveGraph, String.valueOf(method.getName()) + "_primitive", false);
                    DirectedGraph nonPrimitiveGraph = this.dfa.getMethodInfoFlowAnalysis(method).getMethodAbbreviatedInfoFlowGraph();
                    InfoFlowAnalysis.printGraphToDotFile("dfg/" + method.getDeclaringClass().getShortName() + "_" + method.getName(), nonPrimitiveGraph, method.getName(), false);
                }
            } else {
                dataFlowSummary3 = this.dfa.getMethodInfoFlowSummary(method);
                if (this.printdfgs && method.getDeclaringClass().isApplicationClass()) {
                    logger.debug("Attempting to print graph (will succeed only if ./dfg/ is a valid path)");
                    DirectedGraph nonPrimitiveGraph2 = this.dfa.getMethodInfoFlowAnalysis(method).getMethodAbbreviatedInfoFlowGraph();
                    InfoFlowAnalysis.printGraphToDotFile("dfg/" + method.getDeclaringClass().getShortName() + "_" + method.getName(), nonPrimitiveGraph2, method.getName(), false);
                }
            }
            for (EquivalentValue node : dataFlowSummary3.getNodes()) {
                if (node.getValue() instanceof InstanceFieldRef) {
                    InstanceFieldRef ifr = (InstanceFieldRef) node.getValue();
                    if (!this.localFields.contains(ifr.getField()) && !this.sharedFields.contains(ifr.getField()) && !this.localInnerFields.contains(ifr.getField())) {
                        this.localInnerFields.add(ifr.getField());
                    }
                }
            }
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (SootMethod method2 : this.allMethods) {
                if (!method2.isStatic() && method2.isConcrete()) {
                    ListIterator<SootField> localFieldsIt = this.localFields.listIterator();
                    while (localFieldsIt.hasNext()) {
                        SootField localField = localFieldsIt.next();
                        List sourcesAndSinks = new ArrayList();
                        if (this.primitiveDfa != null) {
                            dataFlowSummary2 = this.primitiveDfa.getMethodInfoFlowSummary(method2);
                        } else {
                            dataFlowSummary2 = this.dfa.getMethodInfoFlowSummary(method2);
                        }
                        EquivalentValue node2 = InfoFlowAnalysis.getNodeForFieldRef(method2, localField);
                        if (dataFlowSummary2.containsNode(node2)) {
                            sourcesAndSinks.addAll(dataFlowSummary2.getSuccsOf(node2));
                            sourcesAndSinks.addAll(dataFlowSummary2.getPredsOf(node2));
                        }
                        Iterator sourcesAndSinksIt = sourcesAndSinks.iterator();
                        if (localField.getDeclaringClass().isApplicationClass()) {
                            sourcesAndSinksIt.hasNext();
                        }
                        while (true) {
                            if (!sourcesAndSinksIt.hasNext()) {
                                break;
                            }
                            EquivalentValue sourceOrSink = (EquivalentValue) sourcesAndSinksIt.next();
                            Ref sourceOrSinkRef = (Ref) sourceOrSink.getValue();
                            if (sourceOrSinkRef instanceof ParameterRef) {
                                fieldBecomesShared2 = !parameterIsLocal(method2, sourceOrSink, true);
                            } else if (sourceOrSinkRef instanceof ThisRef) {
                                fieldBecomesShared2 = !thisIsLocal(method2, sourceOrSink);
                            } else if (sourceOrSinkRef instanceof InstanceFieldRef) {
                                fieldBecomesShared2 = this.sharedFields.contains(((FieldRef) sourceOrSinkRef).getField()) || this.sharedInnerFields.contains(((FieldRef) sourceOrSinkRef).getField());
                            } else if (sourceOrSinkRef instanceof StaticFieldRef) {
                                fieldBecomesShared2 = true;
                            } else {
                                throw new RuntimeException("Unknown type of Ref in Data Flow Graph:");
                            }
                            if (fieldBecomesShared2) {
                                localFieldsIt.remove();
                                this.sharedFields.add(localField);
                                changed = true;
                                break;
                            }
                        }
                    }
                    ListIterator<SootField> localInnerFieldsIt = this.localInnerFields.listIterator();
                    while (!changed && localInnerFieldsIt.hasNext()) {
                        SootField localInnerField = localInnerFieldsIt.next();
                        List sourcesAndSinks2 = new ArrayList();
                        if (this.primitiveDfa != null) {
                            dataFlowSummary = this.primitiveDfa.getMethodInfoFlowSummary(method2);
                        } else {
                            dataFlowSummary = this.dfa.getMethodInfoFlowSummary(method2);
                        }
                        EquivalentValue node3 = InfoFlowAnalysis.getNodeForFieldRef(method2, localInnerField);
                        if (dataFlowSummary.containsNode(node3)) {
                            sourcesAndSinks2.addAll(dataFlowSummary.getSuccsOf(node3));
                            sourcesAndSinks2.addAll(dataFlowSummary.getPredsOf(node3));
                        }
                        Iterator sourcesAndSinksIt2 = sourcesAndSinks2.iterator();
                        if (localInnerField.getDeclaringClass().isApplicationClass()) {
                            sourcesAndSinksIt2.hasNext();
                        }
                        while (true) {
                            if (!sourcesAndSinksIt2.hasNext()) {
                                break;
                            }
                            EquivalentValue sourceOrSink2 = (EquivalentValue) sourcesAndSinksIt2.next();
                            Ref sourceOrSinkRef2 = (Ref) sourceOrSink2.getValue();
                            if (sourceOrSinkRef2 instanceof ParameterRef) {
                                fieldBecomesShared = !parameterIsLocal(method2, sourceOrSink2, true);
                            } else if (sourceOrSinkRef2 instanceof ThisRef) {
                                fieldBecomesShared = !thisIsLocal(method2, sourceOrSink2);
                            } else if (sourceOrSinkRef2 instanceof InstanceFieldRef) {
                                fieldBecomesShared = this.sharedFields.contains(((FieldRef) sourceOrSinkRef2).getField()) || this.sharedInnerFields.contains(((FieldRef) sourceOrSinkRef2).getField());
                            } else if (sourceOrSinkRef2 instanceof StaticFieldRef) {
                                fieldBecomesShared = true;
                            } else {
                                throw new RuntimeException("Unknown type of Ref in Data Flow Graph:");
                            }
                            if (fieldBecomesShared) {
                                localInnerFieldsIt.remove();
                                this.sharedInnerFields.add(localInnerField);
                                changed = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (this.dfa.printDebug()) {
            logger.debug("        Found local/shared fields for " + this.sootClass.toString());
            logger.debug("          Local fields: ");
            Iterator<SootField> localsToPrintIt = this.localFields.iterator();
            while (localsToPrintIt.hasNext()) {
                SootField localToPrint = localsToPrintIt.next();
                if (localToPrint.getDeclaringClass().isApplicationClass()) {
                    logger.debug("                  " + localToPrint);
                }
            }
            logger.debug("          Shared fields: ");
            Iterator<SootField> sharedsToPrintIt = this.sharedFields.iterator();
            while (sharedsToPrintIt.hasNext()) {
                SootField sharedToPrint = sharedsToPrintIt.next();
                if (sharedToPrint.getDeclaringClass().isApplicationClass()) {
                    logger.debug("                  " + sharedToPrint);
                }
            }
            logger.debug("          Local inner fields: ");
            Iterator<SootField> localsToPrintIt2 = this.localInnerFields.iterator();
            while (localsToPrintIt2.hasNext()) {
                SootField localToPrint2 = localsToPrintIt2.next();
                if (localToPrint2.getDeclaringClass().isApplicationClass()) {
                    logger.debug("                  " + localToPrint2);
                }
            }
            logger.debug("          Shared inner fields: ");
            Iterator<SootField> sharedsToPrintIt2 = this.sharedInnerFields.iterator();
            while (sharedsToPrintIt2.hasNext()) {
                SootField sharedToPrint2 = sharedsToPrintIt2.next();
                if (sharedToPrint2.getDeclaringClass().isApplicationClass()) {
                    logger.debug("                  " + sharedToPrint2);
                }
            }
        }
    }

    private void propagate() {
        CallLocalityContext invokeContext;
        ArrayList<SootMethod> worklist = new ArrayList<>();
        worklist.addAll(this.entryMethods);
        this.methodToContext = new HashMap();
        Iterator<SootMethod> it = worklist.iterator();
        while (it.hasNext()) {
            SootMethod method = it.next();
            this.methodToContext.put(method, getContextFor(method));
        }
        Date start = new Date();
        if (this.dfa.printDebug()) {
            logger.debug("CLOA: Starting Propagation at " + start);
        }
        while (worklist.size() > 0) {
            ArrayList<SootMethod> newWorklist = new ArrayList<>();
            Iterator<SootMethod> it2 = worklist.iterator();
            while (it2.hasNext()) {
                SootMethod containingMethod = it2.next();
                CallLocalityContext containingContext = this.methodToContext.get(containingMethod);
                if (this.dfa.printDebug()) {
                    logger.debug("      " + containingMethod.getName() + Instruction.argsep + containingContext.toShortString());
                }
                Map<Stmt, CallLocalityContext> invokeToContext = new HashMap<>();
                Iterator edgesIt = Scene.v().getCallGraph().edgesOutOf(containingMethod);
                while (edgesIt.hasNext()) {
                    Edge e = edgesIt.next();
                    if (e.src().getDeclaringClass().isApplicationClass() && e.srcStmt() != null) {
                        if (!invokeToContext.containsKey(e.srcStmt())) {
                            invokeContext = getContextFor(e, containingMethod, containingContext);
                            invokeToContext.put(e.srcStmt(), invokeContext);
                        } else {
                            invokeContext = invokeToContext.get(e.srcStmt());
                        }
                        if (!this.methodToContext.containsKey(e.tgt())) {
                            this.methodToContext.put(e.tgt(), invokeContext);
                            newWorklist.add(e.tgt());
                        } else {
                            boolean causedChange = this.methodToContext.get(e.tgt()).merge(invokeContext);
                            if (causedChange) {
                                newWorklist.add(e.tgt());
                            }
                        }
                    }
                }
            }
            worklist = newWorklist;
        }
        long longTime = (new Date().getTime() - start.getTime()) / 100;
        float time = ((float) longTime) / 10.0f;
        if (this.dfa.printDebug()) {
            logger.debug("CLOA: Ending Propagation after " + time + "s");
        }
    }

    public CallLocalityContext getMergedContext(SootMethod method) {
        if (this.methodToContext.containsKey(method)) {
            return this.methodToContext.get(method);
        }
        return null;
    }

    private CallLocalityContext getContextFor(Edge e, SootMethod containingMethod, CallLocalityContext containingContext) {
        InvokeExpr ie;
        if (e.srcStmt().containsInvokeExpr()) {
            ie = e.srcStmt().getInvokeExpr();
        } else {
            ie = null;
        }
        SootMethod callingMethod = e.tgt();
        CallLocalityContext callingContext = new CallLocalityContext(this.dfa.getMethodInfoFlowSummary(callingMethod).getNodes());
        if (callingMethod.isConcrete()) {
            Body b = containingMethod.retrieveActiveBody();
            if (ie != null && (ie instanceof InstanceInvokeExpr)) {
                InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                if (!containingMethod.isStatic() && iie.getBase().equivTo(b.getThisLocal())) {
                    Iterator<Object> localRefsIt = containingContext.getLocalRefs().iterator();
                    while (localRefsIt.hasNext()) {
                        EquivalentValue rEqVal = (EquivalentValue) localRefsIt.next();
                        Ref r = (Ref) rEqVal.getValue();
                        if (r instanceof InstanceFieldRef) {
                            EquivalentValue newRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(callingMethod, ((FieldRef) r).getFieldRef().resolve());
                            if (callingContext.containsField(newRefEqVal)) {
                                callingContext.setFieldLocal(newRefEqVal);
                            }
                        } else if (r instanceof ThisRef) {
                            callingContext.setThisLocal();
                        }
                    }
                } else if (SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, containingMethod, containingContext, iie.getBase())) {
                    callingContext.setAllFieldsLocal();
                    callingContext.setThisLocal();
                } else {
                    callingContext.setAllFieldsShared();
                    callingContext.setThisShared();
                }
            } else {
                callingContext.setAllFieldsShared();
                callingContext.setThisShared();
            }
            if (ie == null) {
                callingContext.setAllParamsShared();
            } else {
                for (int param = 0; param < ie.getArgCount(); param++) {
                    if (SmartMethodLocalObjectsAnalysis.isObjectLocal(this.dfa, containingMethod, containingContext, ie.getArg(param))) {
                        callingContext.setParamLocal(param);
                    } else {
                        callingContext.setParamShared(param);
                    }
                }
            }
        } else {
            callingContext.setAllFieldsShared();
            callingContext.setThisShared();
            callingContext.setAllParamsShared();
        }
        return callingContext;
    }

    public CallLocalityContext getContextFor(SootMethod sm) {
        return getContextFor(sm, false);
    }

    private CallLocalityContext getContextFor(SootMethod sm, boolean includePrimitiveDataFlowIfAvailable) {
        CallLocalityContext context;
        if (includePrimitiveDataFlowIfAvailable) {
            context = new CallLocalityContext(this.primitiveDfa.getMethodInfoFlowSummary(sm).getNodes());
        } else {
            context = new CallLocalityContext(this.dfa.getMethodInfoFlowSummary(sm).getNodes());
        }
        for (int i = 0; i < sm.getParameterCount(); i++) {
            EquivalentValue paramEqVal = InfoFlowAnalysis.getNodeForParameterRef(sm, i);
            if (parameterIsLocal(sm, paramEqVal, includePrimitiveDataFlowIfAvailable)) {
                context.setParamLocal(i);
            } else {
                context.setParamShared(i);
            }
        }
        for (SootField sf : getLocalFields()) {
            EquivalentValue fieldRefEqVal = InfoFlowAnalysis.getNodeForFieldRef(sm, sf);
            context.setFieldLocal(fieldRefEqVal);
        }
        for (SootField sf2 : getSharedFields()) {
            EquivalentValue fieldRefEqVal2 = InfoFlowAnalysis.getNodeForFieldRef(sm, sf2);
            context.setFieldShared(fieldRefEqVal2);
        }
        return context;
    }

    public boolean isObjectLocal(Value localOrRef, SootMethod sm) {
        return isObjectLocal(localOrRef, sm, false);
    }

    private boolean isObjectLocal(Value localOrRef, SootMethod sm, boolean includePrimitiveDataFlowIfAvailable) {
        if (localOrRef instanceof StaticFieldRef) {
            return false;
        }
        if (this.dfa.printDebug()) {
            logger.debug("      CLOA testing if " + localOrRef + " is local in " + sm);
        }
        SmartMethodLocalObjectsAnalysis smloa = getMethodLocalObjectsAnalysis(sm, includePrimitiveDataFlowIfAvailable);
        if (localOrRef instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef) localOrRef;
            if (ifr.getBase().equivTo(smloa.getThisLocal())) {
                return isFieldLocal(ifr.getFieldRef().resolve());
            }
            if (isObjectLocal(ifr.getBase(), sm, includePrimitiveDataFlowIfAvailable)) {
                boolean retval = this.loa.isFieldLocalToParent(ifr.getFieldRef().resolve());
                if (this.dfa.printDebug()) {
                    logger.debug("      " + (retval ? "local" : Environment.MEDIA_SHARED));
                }
                return retval;
            } else if (this.dfa.printDebug()) {
                logger.debug("      shared");
                return false;
            } else {
                return false;
            }
        }
        CallLocalityContext context = getContextFor(sm);
        boolean retval2 = smloa.isObjectLocal(localOrRef, context);
        if (this.dfa.printDebug()) {
            logger.debug("      " + (retval2 ? "local" : Environment.MEDIA_SHARED));
        }
        return retval2;
    }

    public SmartMethodLocalObjectsAnalysis getMethodLocalObjectsAnalysis(SootMethod sm) {
        return getMethodLocalObjectsAnalysis(sm, false);
    }

    private SmartMethodLocalObjectsAnalysis getMethodLocalObjectsAnalysis(SootMethod sm, boolean includePrimitiveDataFlowIfAvailable) {
        if (includePrimitiveDataFlowIfAvailable && this.primitiveDfa != null) {
            Body b = sm.retrieveActiveBody();
            UnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b);
            return new SmartMethodLocalObjectsAnalysis(g, this.primitiveDfa);
        }
        if (!this.methodToMethodLocalObjectsAnalysis.containsKey(sm)) {
            Body b2 = sm.retrieveActiveBody();
            UnitGraph g2 = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b2);
            SmartMethodLocalObjectsAnalysis smloa = new SmartMethodLocalObjectsAnalysis(g2, this.dfa);
            this.methodToMethodLocalObjectsAnalysis.put(sm, smloa);
        }
        return this.methodToMethodLocalObjectsAnalysis.get(sm);
    }

    private boolean fieldIsInitiallyLocal(SootField field) {
        if (field.isStatic()) {
            return false;
        }
        return field.isPrivate() || !this.externalFields.contains(field);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<SootField> getSharedFields() {
        return (List) this.sharedFields.clone();
    }

    protected List<SootField> getLocalFields() {
        return (List) this.localFields.clone();
    }

    public List<SootField> getInnerSharedFields() {
        return this.sharedInnerFields;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isFieldLocal(SootField field) {
        return this.localFields.contains(field);
    }

    protected boolean isFieldLocal(EquivalentValue fieldRef) {
        return this.localFields.contains(((SootFieldRef) fieldRef.getValue()).resolve());
    }

    public boolean parameterIsLocal(SootMethod method, EquivalentValue parameterRef) {
        return parameterIsLocal(method, parameterRef, false);
    }

    protected boolean parameterIsLocal(SootMethod method, EquivalentValue parameterRef, boolean includePrimitiveDataFlowIfAvailable) {
        if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
            logger.debug("        Checking PARAM " + parameterRef + " for " + method);
        }
        ParameterRef param = (ParameterRef) parameterRef.getValue();
        if (!(param.getType() instanceof RefLikeType) && (!this.dfa.includesPrimitiveInfoFlow() || method.getName().equals("<init>"))) {
            if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
                logger.debug("          PARAM is local (primitive)");
                return true;
            }
            return true;
        }
        List<Pair> extClassCalls = this.uf.getExtCalls(this.sootClass);
        for (Pair extCall : extClassCalls) {
            if (((Stmt) extCall.getO2()).getInvokeExpr().getMethodRef().resolve() == method) {
                if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
                    logger.debug("          PARAM is shared (external access)");
                    return false;
                }
                return false;
            }
        }
        List<Pair> intClassCalls = this.uf.getIntCalls(this.sootClass);
        for (Pair intCall : intClassCalls) {
            SootMethod containingMethod = (SootMethod) intCall.getO1();
            Stmt s = (Stmt) intCall.getO2();
            InvokeExpr ie = s.getInvokeExpr();
            if (ie.getMethodRef().resolve() == method) {
                if (((ParameterRef) parameterRef.getValue()).getIndex() >= 0) {
                    if (!isObjectLocal(ie.getArg(((ParameterRef) parameterRef.getValue()).getIndex()), containingMethod, includePrimitiveDataFlowIfAvailable)) {
                        if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
                            logger.debug("          PARAM is shared (internal propagation)");
                            return false;
                        }
                        return false;
                    }
                } else if (s instanceof DefinitionStmt) {
                    Value obj = ((DefinitionStmt) s).getLeftOp();
                    if (!isObjectLocal(obj, containingMethod, includePrimitiveDataFlowIfAvailable)) {
                        if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
                            logger.debug("          PARAM is shared (internal propagation)");
                            return false;
                        }
                        return false;
                    }
                } else {
                    continue;
                }
            }
        }
        if (this.dfa.printDebug() && method.getDeclaringClass().isApplicationClass()) {
            logger.debug("          PARAM is local SO FAR (internal propagation)");
            return true;
        }
        return true;
    }

    protected boolean thisIsLocal(SootMethod method, EquivalentValue thisRef) {
        return true;
    }
}
