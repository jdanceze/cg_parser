package soot.jimple.spark.pag;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Context;
import soot.FastHierarchy;
import soot.Kind;
import soot.Local;
import soot.PhaseOptions;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.builder.GlobalNodeFactory;
import soot.jimple.spark.builder.MethodNodeFactory;
import soot.jimple.spark.internal.ClientAccessibilityOracle;
import soot.jimple.spark.internal.SparkLibraryHelper;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.sets.BitPointsToSet;
import soot.jimple.spark.sets.DoublePointsToSet;
import soot.jimple.spark.sets.EmptyPointsToSet;
import soot.jimple.spark.sets.HashPointsToSet;
import soot.jimple.spark.sets.HybridPointsToSet;
import soot.jimple.spark.sets.P2SetFactory;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.spark.sets.SharedHybridSet;
import soot.jimple.spark.sets.SharedListSet;
import soot.jimple.spark.sets.SortedArraySet;
import soot.jimple.spark.solver.OnFlyCallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.pointer.util.NativeMethodDriver;
import soot.options.CGOptions;
import soot.options.SparkOptions;
import soot.tagkit.LinkTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
import soot.toolkits.scalar.Pair;
import soot.util.ArrayNumberer;
import soot.util.HashMultiMap;
import soot.util.LargeNumberedMap;
import soot.util.MultiMap;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/PAG.class */
public class PAG implements PointsToAnalysis {
    private static final Logger logger;
    protected static final Node[] EMPTY_NODE_ARRAY;
    protected P2SetFactory setFactory;
    protected SparkOptions opts;
    private boolean runGeomPTA;
    private OnFlyCallGraph ofcg;
    protected TypeManager typeManager;
    private Map<Node, Tag> nodeToTag;
    public NativeMethodDriver nativeMethodDriver;
    public HashMultiMap<InvokeExpr, Pair<Node, Node>> callAssigns;
    public Map<InvokeExpr, SootMethod> callToMethod;
    public Map<InvokeExpr, Node> virtualCallsToReceivers;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected boolean somethingMerged = false;
    ChunkedQueue<AllocNode> newAllocNodes = new ChunkedQueue<>();
    protected ChunkedQueue<Node> edgeQueue = new ChunkedQueue<>();
    protected final ArrayNumberer<AllocNode> allocNodeNumberer = new ArrayNumberer<>();
    private final ArrayNumberer<VarNode> varNodeNumberer = new ArrayNumberer<>();
    private final ArrayNumberer<FieldRefNode> fieldRefNodeNumberer = new ArrayNumberer<>();
    private final ArrayNumberer<AllocDotField> allocDotFieldNodeNumberer = new ArrayNumberer<>();
    protected ClientAccessibilityOracle accessibilityOracle = Scene.v().getClientAccessibilityOracle();
    protected Map<VarNode, Object> simple = new HashMap();
    protected Map<FieldRefNode, Object> load = new HashMap();
    protected Map<VarNode, Object> store = new HashMap();
    protected Map<AllocNode, Object> alloc = new HashMap();
    protected Map<VarNode, Object> newInstance = new HashMap();
    protected Map<NewInstanceNode, Object> assignInstance = new HashMap();
    protected Map<VarNode, Object> simpleInv = new HashMap();
    protected Map<VarNode, Object> loadInv = new HashMap();
    protected Map<FieldRefNode, Object> storeInv = new HashMap();
    protected Map<VarNode, Object> allocInv = new HashMap();
    protected Map<NewInstanceNode, Object> newInstanceInv = new HashMap();
    protected Map<VarNode, Object> assignInstanceInv = new HashMap();
    protected MultiMap<Pair<Node, Node>, Edge> assign2edges = new HashMultiMap();
    private final Map<Object, LocalVarNode> valToLocalVarNode = new HashMap(1000);
    private final Map<Object, GlobalVarNode> valToGlobalVarNode = new HashMap(1000);
    private final Map<Object, AllocNode> valToAllocNode = new HashMap(1000);
    private final Table<Object, Type, AllocNode> valToReflAllocNode = HashBasedTable.create();
    private final ArrayList<VarNode> dereferences = new ArrayList<>();
    private final LargeNumberedMap<Local, LocalVarNode> localToNodeMap = new LargeNumberedMap<>(Scene.v().getLocalNumberer());
    private final Map<Value, NewInstanceNode> newInstToNodeMap = new HashMap();
    public int maxFinishNumber = 0;
    private final GlobalNodeFactory nodeFactory = new GlobalNodeFactory(this);
    protected CGOptions cgOpts = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));

    static {
        $assertionsDisabled = !PAG.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(PAG.class);
        EMPTY_NODE_ARRAY = new Node[0];
    }

    public PAG(SparkOptions opts) {
        P2SetFactory oldF;
        P2SetFactory newF;
        this.runGeomPTA = false;
        this.opts = opts;
        if (opts.add_tags()) {
            this.nodeToTag = new HashMap();
        }
        if (opts.rta() && opts.on_fly_cg()) {
            throw new RuntimeException("Incompatible options rta:true and on-fly-cg:true for cg.spark. Use -p cg-.spark on-fly-cg:false when using RTA.");
        }
        this.typeManager = new TypeManager(this);
        if (!opts.ignore_types()) {
            this.typeManager.setFastHierarchy(() -> {
                return Scene.v().getOrMakeFastHierarchy();
            });
        }
        if (opts.cs_demand()) {
            this.virtualCallsToReceivers = new HashMap();
            this.callToMethod = new HashMap();
            this.callAssigns = new HashMultiMap<>();
        }
        switch (opts.set_impl()) {
            case 1:
                this.setFactory = HashPointsToSet.getFactory();
                break;
            case 2:
                this.setFactory = BitPointsToSet.getFactory();
                break;
            case 3:
                this.setFactory = HybridPointsToSet.getFactory();
                break;
            case 4:
                this.setFactory = SortedArraySet.getFactory();
                break;
            case 5:
                this.setFactory = SharedHybridSet.getFactory();
                break;
            case 6:
                this.setFactory = SharedListSet.getFactory();
                break;
            case 7:
                switch (opts.double_set_old()) {
                    case 1:
                        oldF = HashPointsToSet.getFactory();
                        break;
                    case 2:
                        oldF = BitPointsToSet.getFactory();
                        break;
                    case 3:
                        oldF = HybridPointsToSet.getFactory();
                        break;
                    case 4:
                        oldF = SortedArraySet.getFactory();
                        break;
                    case 5:
                        oldF = SharedHybridSet.getFactory();
                        break;
                    case 6:
                        oldF = SharedListSet.getFactory();
                        break;
                    default:
                        throw new RuntimeException();
                }
                switch (opts.double_set_new()) {
                    case 1:
                        newF = HashPointsToSet.getFactory();
                        break;
                    case 2:
                        newF = BitPointsToSet.getFactory();
                        break;
                    case 3:
                        newF = HybridPointsToSet.getFactory();
                        break;
                    case 4:
                        newF = SortedArraySet.getFactory();
                        break;
                    case 5:
                        newF = SharedHybridSet.getFactory();
                        break;
                    case 6:
                        newF = SharedListSet.getFactory();
                        break;
                    default:
                        throw new RuntimeException();
                }
                this.setFactory = DoublePointsToSet.getFactory(newF, oldF);
                break;
            default:
                throw new RuntimeException();
        }
        this.runGeomPTA = opts.geom_pta();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l) {
        VarNode n = findLocalVarNode(l);
        if (n == null) {
            return EmptyPointsToSet.v();
        }
        return n.getP2Set();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l) {
        VarNode n = findContextVarNode(l, c);
        if (n == null) {
            return EmptyPointsToSet.v();
        }
        return n.getP2Set();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(SootField f) {
        if (!f.isStatic()) {
            throw new RuntimeException("The parameter f must be a *static* field.");
        }
        VarNode n = findGlobalVarNode(f);
        if (n == null) {
            return EmptyPointsToSet.v();
        }
        return n.getP2Set();
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(PointsToSet s, SootField f) {
        if (f.isStatic()) {
            throw new RuntimeException("The parameter f must be an *instance* field.");
        }
        return reachingObjectsInternal(s, f);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
        return reachingObjectsInternal(s, ArrayElement.v());
    }

    private PointsToSet reachingObjectsInternal(PointsToSet s, final SparkField f) {
        if (getOpts().field_based() || getOpts().vta()) {
            VarNode n = findGlobalVarNode(f);
            if (n == null) {
                return EmptyPointsToSet.v();
            }
            return n.getP2Set();
        } else if (getOpts().propagator() == 5) {
            throw new RuntimeException("The alias edge propagator does not compute points-to information for instance fields!Use a different propagator.");
        } else {
            PointsToSetInternal bases = (PointsToSetInternal) s;
            final PointsToSetInternal ret = this.setFactory.newSet(f instanceof SootField ? ((SootField) f).getType() : null, this);
            bases.forall(new P2SetVisitor() { // from class: soot.jimple.spark.pag.PAG.1
                @Override // soot.jimple.spark.sets.P2SetVisitor
                public final void visit(Node n2) {
                    Node nDotF = ((AllocNode) n2).dot(f);
                    if (nDotF != null) {
                        ret.addAll(nDotF.getP2Set(), null);
                    }
                }
            });
            return ret;
        }
    }

    public P2SetFactory getSetFactory() {
        return this.setFactory;
    }

    private <K extends Node> void lookupInMap(Map<K, Object> map) {
        for (K object : map.keySet()) {
            lookup(map, object);
        }
    }

    public void cleanUpMerges() {
        if (this.opts.verbose()) {
            logger.debug("Cleaning up graph for merged nodes");
        }
        lookupInMap(this.simple);
        lookupInMap(this.alloc);
        lookupInMap(this.store);
        lookupInMap(this.load);
        lookupInMap(this.simpleInv);
        lookupInMap(this.allocInv);
        lookupInMap(this.storeInv);
        lookupInMap(this.loadInv);
        this.somethingMerged = false;
        if (this.opts.verbose()) {
            logger.debug("Done cleaning up graph for merged nodes");
        }
    }

    public boolean doAddSimpleEdge(VarNode from, VarNode to) {
        return addToMap(this.simple, from, to) | addToMap(this.simpleInv, to, from);
    }

    public boolean doAddStoreEdge(VarNode from, FieldRefNode to) {
        return addToMap(this.store, from, to) | addToMap(this.storeInv, to, from);
    }

    public boolean doAddLoadEdge(FieldRefNode from, VarNode to) {
        return addToMap(this.load, from, to) | addToMap(this.loadInv, to, from);
    }

    public boolean doAddAllocEdge(AllocNode from, VarNode to) {
        return addToMap(this.alloc, from, to) | addToMap(this.allocInv, to, from);
    }

    public boolean doAddNewInstanceEdge(VarNode from, NewInstanceNode to) {
        return addToMap(this.newInstance, from, to) | addToMap(this.newInstanceInv, to, from);
    }

    public boolean doAddAssignInstanceEdge(NewInstanceNode from, VarNode to) {
        return addToMap(this.assignInstance, from, to) | addToMap(this.assignInstanceInv, to, from);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mergedWith(Node n1, Node n2) {
        if (n1.equals(n2)) {
            throw new RuntimeException("oops");
        }
        this.somethingMerged = true;
        if (ofcg() != null) {
            ofcg().mergedWith(n1, n2);
        }
        Map<Node, Object>[] maps = {this.simple, this.alloc, this.store, this.load, this.simpleInv, this.allocInv, this.storeInv, this.loadInv};
        for (Map<Node, Object> m : maps) {
            if (m.keySet().contains(n2)) {
                Object[] os = {m.get(n1), m.get(n2)};
                int size1 = getSize(os[0]);
                int size2 = getSize(os[1]);
                if (size1 == 0) {
                    if (os[1] != null) {
                        m.put(n1, os[1]);
                    }
                } else if (size2 != 0) {
                    if (os[0] instanceof HashSet) {
                        if (os[1] instanceof HashSet) {
                            ((HashSet) os[0]).addAll((HashSet) os[1]);
                        } else {
                            Node[] ar = (Node[]) os[1];
                            for (Node element0 : ar) {
                                ((HashSet) os[0]).add(element0);
                            }
                        }
                    } else if (os[1] instanceof HashSet) {
                        Node[] ar2 = (Node[]) os[0];
                        for (Node element02 : ar2) {
                            ((HashSet) os[1]).add(element02);
                        }
                        m.put(n1, os[1]);
                    } else if (size1 * size2 < 1000) {
                        Node[] a1 = (Node[]) os[0];
                        Node[] a2 = (Node[]) os[1];
                        Node[] ret = new Node[size1 + size2];
                        System.arraycopy(a1, 0, ret, 0, a1.length);
                        int j = a1.length;
                        for (Node rep : a2) {
                            int k = 0;
                            while (true) {
                                if (k < j) {
                                    if (rep == ret[k]) {
                                        break;
                                    }
                                    k++;
                                } else {
                                    int i = j;
                                    j++;
                                    ret[i] = rep;
                                    break;
                                }
                            }
                        }
                        Node[] newArray = new Node[j];
                        System.arraycopy(ret, 0, newArray, 0, j);
                        m.put(n1, newArray);
                    } else {
                        HashSet<Node> s = new HashSet<>(size1 + size2);
                        for (Object o : os) {
                            if (o != null) {
                                if (o instanceof Set) {
                                    s.addAll((Set) o);
                                } else {
                                    Node[] ar3 = (Node[]) o;
                                    for (Node element1 : ar3) {
                                        s.add(element1);
                                    }
                                }
                            }
                        }
                        m.put(n1, s);
                    }
                }
                m.remove(n2);
            }
        }
    }

    protected <K extends Node> Node[] lookup(Map<K, Object> m, K key) {
        Object valueList = m.get(key);
        if (valueList == null) {
            return EMPTY_NODE_ARRAY;
        }
        if (valueList instanceof Set) {
            try {
                Object array = ((Set) valueList).toArray(EMPTY_NODE_ARRAY);
                valueList = array;
                m.put(key, array);
            } catch (Exception e) {
                for (Object obj : (Set) valueList) {
                    logger.debug(new StringBuilder().append(obj).toString());
                }
                throw new RuntimeException(new StringBuilder().append(valueList).append(e).toString());
            }
        }
        Node[] ret = (Node[]) valueList;
        if (this.somethingMerged) {
            int i = 0;
            while (i < ret.length) {
                Node reti = ret[i];
                Node rep = reti.getReplacement();
                if (rep == reti && rep != key) {
                    i++;
                } else if (ret.length <= 75) {
                    int j = i;
                    while (i < ret.length) {
                        Node rep2 = ret[i].getReplacement();
                        if (rep2 != key) {
                            int k = 0;
                            while (true) {
                                if (k < j) {
                                    if (rep2 == ret[k]) {
                                        break;
                                    }
                                    k++;
                                } else {
                                    int i2 = j;
                                    j++;
                                    ret[i2] = rep2;
                                    break;
                                }
                            }
                        }
                        i++;
                    }
                    Node[] newArray = new Node[j];
                    System.arraycopy(ret, 0, newArray, 0, j);
                    ret = newArray;
                    m.put(key, newArray);
                } else {
                    Set<Node> s = new HashSet<>(ret.length * 2);
                    for (int j2 = 0; j2 < i; j2++) {
                        s.add(ret[j2]);
                    }
                    for (int j3 = i; j3 < ret.length; j3++) {
                        Node rep3 = ret[j3].getReplacement();
                        if (rep3 != key) {
                            s.add(rep3);
                        }
                    }
                    Node[] nodeArr = (Node[]) s.toArray(EMPTY_NODE_ARRAY);
                    ret = nodeArr;
                    m.put(key, nodeArr);
                }
            }
        }
        return ret;
    }

    public Node[] simpleLookup(VarNode key) {
        return lookup(this.simple, key);
    }

    public Node[] simpleInvLookup(VarNode key) {
        return lookup(this.simpleInv, key);
    }

    public Node[] loadLookup(FieldRefNode key) {
        return lookup(this.load, key);
    }

    public Node[] loadInvLookup(VarNode key) {
        return lookup(this.loadInv, key);
    }

    public Node[] storeLookup(VarNode key) {
        return lookup(this.store, key);
    }

    public Node[] newInstanceLookup(VarNode key) {
        return lookup(this.newInstance, key);
    }

    public Node[] assignInstanceLookup(NewInstanceNode key) {
        return lookup(this.assignInstance, key);
    }

    public Node[] storeInvLookup(FieldRefNode key) {
        return lookup(this.storeInv, key);
    }

    public Node[] allocLookup(AllocNode key) {
        return lookup(this.alloc, key);
    }

    public Node[] allocInvLookup(VarNode key) {
        return lookup(this.allocInv, key);
    }

    public Set<VarNode> simpleSources() {
        return this.simple.keySet();
    }

    public Set<AllocNode> allocSources() {
        return this.alloc.keySet();
    }

    public Set<VarNode> storeSources() {
        return this.store.keySet();
    }

    public Set<FieldRefNode> loadSources() {
        return this.load.keySet();
    }

    public Set<VarNode> newInstanceSources() {
        return this.newInstance.keySet();
    }

    public Set<NewInstanceNode> assignInstanceSources() {
        return this.assignInstance.keySet();
    }

    public Set<VarNode> simpleInvSources() {
        return this.simpleInv.keySet();
    }

    public Set<VarNode> allocInvSources() {
        return this.allocInv.keySet();
    }

    public Set<FieldRefNode> storeInvSources() {
        return this.storeInv.keySet();
    }

    public Set<VarNode> loadInvSources() {
        return this.loadInv.keySet();
    }

    public Iterator<VarNode> simpleSourcesIterator() {
        return this.simple.keySet().iterator();
    }

    public Iterator<AllocNode> allocSourcesIterator() {
        return this.alloc.keySet().iterator();
    }

    public Iterator<VarNode> storeSourcesIterator() {
        return this.store.keySet().iterator();
    }

    public Iterator<FieldRefNode> loadSourcesIterator() {
        return this.load.keySet().iterator();
    }

    public Iterator<VarNode> simpleInvSourcesIterator() {
        return this.simpleInv.keySet().iterator();
    }

    public Iterator<VarNode> allocInvSourcesIterator() {
        return this.allocInv.keySet().iterator();
    }

    public Iterator<FieldRefNode> storeInvSourcesIterator() {
        return this.storeInv.keySet().iterator();
    }

    public Iterator<VarNode> loadInvSourcesIterator() {
        return this.loadInv.keySet().iterator();
    }

    private static int getSize(Object set) {
        if (set instanceof Set) {
            return ((Set) set).size();
        }
        if (set == null) {
            return 0;
        }
        return ((Object[]) set).length;
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Local l, SootField f) {
        return reachingObjects(reachingObjects(l), f);
    }

    @Override // soot.PointsToAnalysis
    public PointsToSet reachingObjects(Context c, Local l, SootField f) {
        return reachingObjects(reachingObjects(c, l), f);
    }

    private void addNodeTag(Node node, SootMethod m) {
        Tag tag;
        if (this.nodeToTag != null) {
            if (m == null) {
                tag = new StringTag(node.toString());
            } else {
                tag = new LinkTag(node.toString(), m, m.getDeclaringClass().getName());
            }
            this.nodeToTag.put(node, tag);
        }
    }

    public AllocNode makeAllocNode(Object newExpr, Type type, SootMethod m) {
        if (this.opts.types_for_sites() || this.opts.vta()) {
            newExpr = type;
        }
        AllocNode ret = this.valToAllocNode.get(newExpr);
        if (newExpr instanceof NewExpr) {
            if (ret == null) {
                AllocNode allocNode = new AllocNode(this, newExpr, type, m);
                ret = allocNode;
                this.valToAllocNode.put(newExpr, allocNode);
                this.newAllocNodes.add(ret);
                addNodeTag(ret, m);
            } else if (!ret.getType().equals(type)) {
                throw new RuntimeException("NewExpr " + newExpr + " of type " + type + " previously had type " + ret.getType());
            }
        } else {
            ret = this.valToReflAllocNode.get(newExpr, type);
            if (ret == null) {
                AllocNode allocNode2 = new AllocNode(this, newExpr, type, m);
                ret = allocNode2;
                this.valToReflAllocNode.put(newExpr, type, allocNode2);
                this.newAllocNodes.add(ret);
                addNodeTag(ret, m);
            }
        }
        return ret;
    }

    public AllocNode makeStringConstantNode(String s) {
        if (this.opts.types_for_sites() || this.opts.vta()) {
            return makeAllocNode(RefType.v("java.lang.String"), RefType.v("java.lang.String"), null);
        }
        StringConstantNode ret = (StringConstantNode) this.valToAllocNode.get(s);
        if (ret == null) {
            Map<Object, AllocNode> map = this.valToAllocNode;
            StringConstantNode stringConstantNode = new StringConstantNode(this, s);
            ret = stringConstantNode;
            map.put(s, stringConstantNode);
            this.newAllocNodes.add(ret);
            addNodeTag(ret, null);
        }
        return ret;
    }

    public AllocNode makeClassConstantNode(ClassConstant cc) {
        if (this.opts.types_for_sites() || this.opts.vta()) {
            return makeAllocNode(RefType.v("java.lang.Class"), RefType.v("java.lang.Class"), null);
        }
        ClassConstantNode ret = (ClassConstantNode) this.valToAllocNode.get(cc);
        if (ret == null) {
            Map<Object, AllocNode> map = this.valToAllocNode;
            ClassConstantNode classConstantNode = new ClassConstantNode(this, cc);
            ret = classConstantNode;
            map.put(cc, classConstantNode);
            this.newAllocNodes.add(ret);
            addNodeTag(ret, null);
        }
        return ret;
    }

    public QueueReader<AllocNode> allocNodeListener() {
        return this.newAllocNodes.reader();
    }

    public GlobalVarNode findGlobalVarNode(Object value) {
        if (this.opts.rta()) {
            value = null;
        }
        return this.valToGlobalVarNode.get(value);
    }

    public LocalVarNode findLocalVarNode(Object value) {
        if (this.opts.rta()) {
            value = null;
        } else if (value instanceof Local) {
            return this.localToNodeMap.get((Local) value);
        }
        return this.valToLocalVarNode.get(value);
    }

    public GlobalVarNode makeGlobalVarNode(Object value, Type type) {
        if (this.opts.rta()) {
            value = null;
            type = Scene.v().getObjectType();
        }
        GlobalVarNode ret = this.valToGlobalVarNode.get(value);
        if (ret == null) {
            GlobalVarNode globalVarNode = new GlobalVarNode(this, value, type);
            ret = globalVarNode;
            this.valToGlobalVarNode.put(value, globalVarNode);
            if (this.cgOpts.library() != 1 && (value instanceof SootField)) {
                SootField sf = (SootField) value;
                if (this.accessibilityOracle.isAccessible(sf)) {
                    type.apply(new SparkLibraryHelper(this, ret, null));
                }
            }
            addNodeTag(ret, null);
        } else if (!ret.getType().equals(type)) {
            throw new RuntimeException("Value " + value + " of type " + type + " previously had type " + ret.getType());
        }
        return ret;
    }

    public LocalVarNode makeLocalVarNode(Object value, Type type, SootMethod method) {
        if (this.opts.rta()) {
            value = null;
            type = Scene.v().getObjectType();
            method = null;
        } else if (value instanceof Local) {
            Local val = (Local) value;
            if (val.getNumber() == 0) {
                Scene.v().getLocalNumberer().add(val);
            }
            LocalVarNode ret = this.localToNodeMap.get(val);
            if (ret == null) {
                LocalVarNode localVarNode = new LocalVarNode(this, value, type, method);
                ret = localVarNode;
                this.localToNodeMap.put((Local) value, localVarNode);
                addNodeTag(ret, method);
            } else if (!ret.getType().equals(type)) {
                throw new RuntimeException("Value " + value + " of type " + type + " previously had type " + ret.getType());
            }
            return ret;
        }
        LocalVarNode ret2 = this.valToLocalVarNode.get(value);
        if (ret2 == null) {
            LocalVarNode localVarNode2 = new LocalVarNode(this, value, type, method);
            ret2 = localVarNode2;
            this.valToLocalVarNode.put(value, localVarNode2);
            addNodeTag(ret2, method);
        } else if (!ret2.getType().equals(type)) {
            throw new RuntimeException("Value " + value + " of type " + type + " previously had type " + ret2.getType());
        }
        return ret2;
    }

    public NewInstanceNode makeNewInstanceNode(Value value, Type type, SootMethod method) {
        NewInstanceNode node = this.newInstToNodeMap.get(value);
        if (node == null) {
            node = new NewInstanceNode(this, value, type);
            this.newInstToNodeMap.put(value, node);
            addNodeTag(node, method);
        }
        return node;
    }

    public ContextVarNode findContextVarNode(Object baseValue, Context context) {
        LocalVarNode base = findLocalVarNode(baseValue);
        if (base == null) {
            return null;
        }
        return base.context(context);
    }

    public ContextVarNode makeContextVarNode(Object baseValue, Type baseType, Context context, SootMethod method) {
        LocalVarNode base = makeLocalVarNode(baseValue, baseType, method);
        return makeContextVarNode(base, context);
    }

    public ContextVarNode makeContextVarNode(LocalVarNode base, Context context) {
        ContextVarNode ret = base.context(context);
        if (ret == null) {
            ret = new ContextVarNode(this, base, context);
            addNodeTag(ret, base.getMethod());
        }
        return ret;
    }

    public FieldRefNode findLocalFieldRefNode(Object baseValue, SparkField field) {
        VarNode base = findLocalVarNode(baseValue);
        if (base == null) {
            return null;
        }
        return base.dot(field);
    }

    public FieldRefNode findGlobalFieldRefNode(Object baseValue, SparkField field) {
        VarNode base = findGlobalVarNode(baseValue);
        if (base == null) {
            return null;
        }
        return base.dot(field);
    }

    public FieldRefNode makeLocalFieldRefNode(Object baseValue, Type baseType, SparkField field, SootMethod method) {
        VarNode base = makeLocalVarNode(baseValue, baseType, method);
        FieldRefNode ret = makeFieldRefNode(base, field);
        if (this.cgOpts.library() != 1 && (field instanceof SootField)) {
            SootField sf = (SootField) field;
            Type type = sf.getType();
            if (this.accessibilityOracle.isAccessible(sf)) {
                type.apply(new SparkLibraryHelper(this, ret, method));
            }
        }
        return ret;
    }

    public FieldRefNode makeGlobalFieldRefNode(Object baseValue, Type baseType, SparkField field) {
        VarNode base = makeGlobalVarNode(baseValue, baseType);
        return makeFieldRefNode(base, field);
    }

    public FieldRefNode makeFieldRefNode(VarNode base, SparkField field) {
        FieldRefNode ret = base.dot(field);
        if (ret == null) {
            ret = new FieldRefNode(this, base, field);
            if (base instanceof LocalVarNode) {
                addNodeTag(ret, ((LocalVarNode) base).getMethod());
            } else {
                addNodeTag(ret, null);
            }
        }
        return ret;
    }

    public AllocDotField findAllocDotField(AllocNode an, SparkField field) {
        return an.dot(field);
    }

    public AllocDotField makeAllocDotField(AllocNode an, SparkField field) {
        AllocDotField ret = an.dot(field);
        if (ret == null) {
            ret = new AllocDotField(this, an, field);
        }
        return ret;
    }

    public boolean addSimpleEdge(VarNode from, VarNode to) {
        boolean ret = false;
        if (doAddSimpleEdge(from, to)) {
            this.edgeQueue.add(from);
            this.edgeQueue.add(to);
            ret = true;
        }
        if (this.opts.simple_edges_bidirectional() && doAddSimpleEdge(to, from)) {
            this.edgeQueue.add(to);
            this.edgeQueue.add(from);
            ret = true;
        }
        return ret;
    }

    public boolean addStoreEdge(VarNode from, FieldRefNode to) {
        if (!this.opts.rta() && doAddStoreEdge(from, to)) {
            this.edgeQueue.add(from);
            this.edgeQueue.add(to);
            return true;
        }
        return false;
    }

    public boolean addLoadEdge(FieldRefNode from, VarNode to) {
        if (!this.opts.rta() && doAddLoadEdge(from, to)) {
            this.edgeQueue.add(from);
            this.edgeQueue.add(to);
            return true;
        }
        return false;
    }

    public boolean addAllocEdge(AllocNode from, VarNode to) {
        FastHierarchy fh = this.typeManager.getFastHierarchy();
        if ((fh == null || to.getType() == null || fh.canStoreType(from.getType(), to.getType())) && doAddAllocEdge(from, to)) {
            this.edgeQueue.add(from);
            this.edgeQueue.add(to);
            return true;
        }
        return false;
    }

    public boolean addNewInstanceEdge(VarNode from, NewInstanceNode to) {
        if (!this.opts.rta() && doAddNewInstanceEdge(from, to)) {
            this.edgeQueue.add(from);
            this.edgeQueue.add(to);
            return true;
        }
        return false;
    }

    public boolean addAssignInstanceEdge(NewInstanceNode from, VarNode to) {
        if (!this.opts.rta() && doAddAssignInstanceEdge(from, to)) {
            this.edgeQueue.add(from);
            this.edgeQueue.add(to);
            return true;
        }
        return false;
    }

    public boolean addEdge(Node from, Node to) {
        Node from2 = from.getReplacement();
        Node to2 = to.getReplacement();
        if (from2 instanceof VarNode) {
            if (to2 instanceof VarNode) {
                return addSimpleEdge((VarNode) from2, (VarNode) to2);
            }
            if (to2 instanceof FieldRefNode) {
                return addStoreEdge((VarNode) from2, (FieldRefNode) to2);
            }
            if (to2 instanceof NewInstanceNode) {
                return addNewInstanceEdge((VarNode) from2, (NewInstanceNode) to2);
            }
            throw new RuntimeException("Invalid node type");
        } else if (from2 instanceof FieldRefNode) {
            return addLoadEdge((FieldRefNode) from2, (VarNode) to2);
        } else {
            if (from2 instanceof NewInstanceNode) {
                return addAssignInstanceEdge((NewInstanceNode) from2, (VarNode) to2);
            }
            return addAllocEdge((AllocNode) from2, (VarNode) to2);
        }
    }

    public QueueReader<Node> edgeReader() {
        return this.edgeQueue.reader();
    }

    public int getNumAllocNodes() {
        return this.allocNodeNumberer.size();
    }

    public TypeManager getTypeManager() {
        return this.typeManager;
    }

    public void setOnFlyCallGraph(OnFlyCallGraph ofcg) {
        this.ofcg = ofcg;
    }

    public OnFlyCallGraph getOnFlyCallGraph() {
        return this.ofcg;
    }

    public OnFlyCallGraph ofcg() {
        return this.ofcg;
    }

    public void addDereference(VarNode base) {
        this.dereferences.add(base);
    }

    public List<VarNode> getDereferences() {
        return this.dereferences;
    }

    public Map<Node, Tag> getNodeTags() {
        return this.nodeToTag;
    }

    public ArrayNumberer<AllocNode> getAllocNodeNumberer() {
        return this.allocNodeNumberer;
    }

    public ArrayNumberer<VarNode> getVarNodeNumberer() {
        return this.varNodeNumberer;
    }

    public ArrayNumberer<FieldRefNode> getFieldRefNodeNumberer() {
        return this.fieldRefNodeNumberer;
    }

    public ArrayNumberer<AllocDotField> getAllocDotFieldNodeNumberer() {
        return this.allocDotFieldNodeNumberer;
    }

    public SparkOptions getOpts() {
        return this.opts;
    }

    public CGOptions getCGOpts() {
        return this.cgOpts;
    }

    public Pair<Node, Node> addInterproceduralAssignment(Node from, Node to, Edge e) {
        Pair<Node, Node> val = new Pair<>(from, to);
        if (this.runGeomPTA) {
            this.assign2edges.put(val, e);
        }
        return val;
    }

    public Set<Edge> lookupEdgesForAssignment(Pair<Node, Node> val) {
        return this.assign2edges.get(val);
    }

    public void addCallTarget(Edge e) {
        if (!e.passesParameters()) {
            return;
        }
        MethodPAG srcmpag = MethodPAG.v(this, e.src());
        MethodPAG tgtmpag = MethodPAG.v(this, e.tgt());
        if (e.isExplicit() || e.kind() == Kind.THREAD) {
            addCallTarget(srcmpag, tgtmpag, (Stmt) e.srcUnit(), e.srcCtxt(), e.tgtCtxt(), e);
        } else if (e.kind() == Kind.ASYNCTASK) {
            addCallTarget(srcmpag, tgtmpag, (Stmt) e.srcUnit(), e.srcCtxt(), e.tgtCtxt(), e, false);
        } else if (e.kind() == Kind.EXECUTOR) {
            InvokeExpr ie = e.srcStmt().getInvokeExpr();
            Node parm = srcmpag.parameterize(srcmpag.nodeFactory().getNode(ie.getArg(0)), e.srcCtxt()).getReplacement();
            Node thiz = tgtmpag.parameterize(tgtmpag.nodeFactory().caseThis(), e.tgtCtxt()).getReplacement();
            addEdge(parm, thiz);
            Pair<Node, Node> pval = addInterproceduralAssignment(parm, thiz, e);
            if (this.callAssigns != null) {
                this.callToMethod.put(ie, srcmpag.getMethod());
                boolean virtualCall = !this.callAssigns.put(ie, pval);
                if (virtualCall) {
                    this.virtualCallsToReceivers.putIfAbsent(ie, parm);
                }
            }
        } else if (e.kind() == Kind.HANDLER) {
            InvokeExpr ie2 = e.srcStmt().getInvokeExpr();
            Node base = srcmpag.parameterize(srcmpag.nodeFactory().getNode(((VirtualInvokeExpr) ie2).getBase()), e.srcCtxt()).getReplacement();
            Node thiz2 = tgtmpag.parameterize(tgtmpag.nodeFactory().caseThis(), e.tgtCtxt()).getReplacement();
            addEdge(base, thiz2);
            Pair<Node, Node> pval2 = addInterproceduralAssignment(base, thiz2, e);
            if (this.callAssigns != null) {
                boolean virtualCall2 = !this.callAssigns.put(ie2, pval2);
                if (!$assertionsDisabled && !virtualCall2) {
                    throw new AssertionError();
                }
                this.callToMethod.put(ie2, srcmpag.getMethod());
                this.virtualCallsToReceivers.put(ie2, base);
            }
        } else if (e.kind() == Kind.PRIVILEGED) {
            InvokeExpr ie3 = e.srcStmt().getInvokeExpr();
            Node parm2 = srcmpag.parameterize(srcmpag.nodeFactory().getNode(ie3.getArg(0)), e.srcCtxt()).getReplacement();
            Node thiz3 = tgtmpag.parameterize(tgtmpag.nodeFactory().caseThis(), e.tgtCtxt()).getReplacement();
            addEdge(parm2, thiz3);
            Pair<Node, Node> pval3 = addInterproceduralAssignment(parm2, thiz3, e);
            if (this.callAssigns != null) {
                this.callAssigns.put(ie3, pval3);
                this.callToMethod.put(ie3, srcmpag.getMethod());
            }
            if (e.srcUnit() instanceof AssignStmt) {
                AssignStmt as = (AssignStmt) e.srcUnit();
                Node ret = tgtmpag.parameterize(tgtmpag.nodeFactory().caseRet(), e.tgtCtxt()).getReplacement();
                Node lhs = srcmpag.parameterize(srcmpag.nodeFactory().getNode(as.getLeftOp()), e.srcCtxt()).getReplacement();
                addEdge(ret, lhs);
                Pair<Node, Node> pval4 = addInterproceduralAssignment(ret, lhs, e);
                if (this.callAssigns != null) {
                    this.callAssigns.put(ie3, pval4);
                    this.callToMethod.put(ie3, srcmpag.getMethod());
                }
            }
        } else if (e.kind() == Kind.FINALIZE) {
            Node srcThis = srcmpag.parameterize(srcmpag.nodeFactory().caseThis(), e.srcCtxt()).getReplacement();
            Node tgtThis = tgtmpag.parameterize(tgtmpag.nodeFactory().caseThis(), e.tgtCtxt()).getReplacement();
            addEdge(srcThis, tgtThis);
            addInterproceduralAssignment(srcThis, tgtThis, e);
        } else if (e.kind() == Kind.NEWINSTANCE) {
            Stmt s = (Stmt) e.srcUnit();
            Node newObject = this.nodeFactory.caseNewInstance((VarNode) srcmpag.parameterize(srcmpag.nodeFactory().getNode(((InstanceInvokeExpr) s.getInvokeExpr()).getBase()), e.srcCtxt()).getReplacement());
            Node initThis = tgtmpag.parameterize(tgtmpag.nodeFactory().caseThis(), e.tgtCtxt()).getReplacement();
            addEdge(newObject, initThis);
            if (s instanceof AssignStmt) {
                AssignStmt as2 = (AssignStmt) s;
                Node asLHS = srcmpag.nodeFactory().getNode(as2.getLeftOp());
                addEdge(newObject, srcmpag.parameterize(asLHS, e.srcCtxt()).getReplacement());
            }
            Pair<Node, Node> pval5 = addInterproceduralAssignment(newObject, initThis, e);
            if (this.callAssigns != null) {
                this.callAssigns.put(s.getInvokeExpr(), pval5);
                this.callToMethod.put(s.getInvokeExpr(), srcmpag.getMethod());
            }
        } else if (e.kind() == Kind.REFL_INVOKE) {
            InvokeExpr ie4 = e.srcStmt().getInvokeExpr();
            Value arg0 = ie4.getArg(0);
            if (arg0 != NullConstant.v()) {
                Node parm0 = srcmpag.nodeFactory().getNode(arg0);
                Node parm02 = srcmpag.parameterize(parm0, e.srcCtxt()).getReplacement();
                Node thiz4 = tgtmpag.parameterize(tgtmpag.nodeFactory().caseThis(), e.tgtCtxt()).getReplacement();
                addEdge(parm02, thiz4);
                Pair<Node, Node> pval6 = addInterproceduralAssignment(parm02, thiz4, e);
                if (this.callAssigns != null) {
                    this.callAssigns.put(ie4, pval6);
                    this.callToMethod.put(ie4, srcmpag.getMethod());
                }
            }
            Value arg1 = ie4.getArg(1);
            SootMethod tgt = e.getTgt().method();
            if (arg1 != NullConstant.v() && tgt.getParameterCount() > 0) {
                Node parm1 = srcmpag.nodeFactory().getNode(arg1);
                FieldRefNode parm1contents = makeFieldRefNode((VarNode) srcmpag.parameterize(parm1, e.srcCtxt()).getReplacement(), ArrayElement.v());
                for (int i = 0; i < tgt.getParameterCount(); i++) {
                    if (tgt.getParameterType(i) instanceof RefLikeType) {
                        Node tgtParmI = tgtmpag.parameterize(tgtmpag.nodeFactory().caseParm(i), e.tgtCtxt()).getReplacement();
                        addEdge(parm1contents, tgtParmI);
                        Pair<Node, Node> pval7 = addInterproceduralAssignment(parm1contents, tgtParmI, e);
                        if (this.callAssigns != null) {
                            this.callAssigns.put(ie4, pval7);
                        }
                    }
                }
            }
            if ((e.srcUnit() instanceof AssignStmt) && (tgt.getReturnType() instanceof RefLikeType)) {
                AssignStmt as3 = (AssignStmt) e.srcUnit();
                Node ret2 = tgtmpag.parameterize(tgtmpag.nodeFactory().caseRet(), e.tgtCtxt()).getReplacement();
                Node lhs2 = srcmpag.parameterize(srcmpag.nodeFactory().getNode(as3.getLeftOp()), e.srcCtxt()).getReplacement();
                addEdge(ret2, lhs2);
                Pair<Node, Node> pval8 = addInterproceduralAssignment(ret2, lhs2, e);
                if (this.callAssigns != null) {
                    this.callAssigns.put(ie4, pval8);
                }
            }
        } else if (e.kind() == Kind.REFL_CLASS_NEWINSTANCE || e.kind() == Kind.REFL_CONSTR_NEWINSTANCE) {
            Stmt s2 = (Stmt) e.srcUnit();
            InstanceInvokeExpr iie = (InstanceInvokeExpr) s2.getInvokeExpr();
            Node cls = srcmpag.parameterize(srcmpag.nodeFactory().getNode(iie.getBase()), e.srcCtxt()).getReplacement();
            if (cls instanceof ContextVarNode) {
                cls = findLocalVarNode(((VarNode) cls).getVariable());
            }
            VarNode newObject2 = makeGlobalVarNode(cls, Scene.v().getObjectType());
            SootClass tgtClass = e.getTgt().method().getDeclaringClass();
            RefType tgtType = tgtClass.getType();
            AllocNode site = makeAllocNode(new Pair(cls, tgtClass), tgtType, null);
            addEdge(site, newObject2);
            Node initThis2 = tgtmpag.parameterize(tgtmpag.nodeFactory().caseThis(), e.tgtCtxt()).getReplacement();
            addEdge(newObject2, initThis2);
            addInterproceduralAssignment(newObject2, initThis2, e);
            if (e.kind() == Kind.REFL_CONSTR_NEWINSTANCE) {
                Value arg = iie.getArg(0);
                SootMethod tgt2 = e.getTgt().method();
                if (arg != NullConstant.v() && tgt2.getParameterCount() > 0) {
                    Node parm03 = srcmpag.nodeFactory().getNode(arg);
                    FieldRefNode parm1contents2 = makeFieldRefNode((VarNode) srcmpag.parameterize(parm03, e.srcCtxt()).getReplacement(), ArrayElement.v());
                    for (int i2 = 0; i2 < tgt2.getParameterCount(); i2++) {
                        if (tgt2.getParameterType(i2) instanceof RefLikeType) {
                            Node tgtParmI2 = tgtmpag.parameterize(tgtmpag.nodeFactory().caseParm(i2), e.tgtCtxt()).getReplacement();
                            addEdge(parm1contents2, tgtParmI2);
                            Pair<Node, Node> pval9 = addInterproceduralAssignment(parm1contents2, tgtParmI2, e);
                            if (this.callAssigns != null) {
                                this.callAssigns.put(iie, pval9);
                            }
                        }
                    }
                }
            }
            if (s2 instanceof AssignStmt) {
                AssignStmt as4 = (AssignStmt) s2;
                Node asLHS2 = srcmpag.nodeFactory().getNode(as4.getLeftOp());
                addEdge(newObject2, srcmpag.parameterize(asLHS2, e.srcCtxt()).getReplacement());
            }
            Pair<Node, Node> pval10 = addInterproceduralAssignment(newObject2, initThis2, e);
            if (this.callAssigns != null) {
                this.callAssigns.put(s2.getInvokeExpr(), pval10);
                this.callToMethod.put(s2.getInvokeExpr(), srcmpag.getMethod());
            }
        } else {
            throw new RuntimeException("Unhandled edge " + e);
        }
    }

    public void addCallTarget(MethodPAG srcmpag, MethodPAG tgtmpag, Stmt s, Context srcContext, Context tgtContext, Edge e) {
        addCallTarget(srcmpag, tgtmpag, s, srcContext, tgtContext, e, true);
    }

    public void addCallTarget(MethodPAG srcmpag, MethodPAG tgtmpag, Stmt s, Context srcContext, Context tgtContext, Edge e, boolean propagateReturn) {
        MethodNodeFactory srcnf = srcmpag.nodeFactory();
        MethodNodeFactory tgtnf = tgtmpag.nodeFactory();
        InvokeExpr ie = s.getInvokeExpr();
        int numArgs = ie.getArgCount();
        for (int i = 0; i < numArgs; i++) {
            Value arg = ie.getArg(i);
            if ((arg.getType() instanceof RefLikeType) && !(arg instanceof NullConstant)) {
                Node argNode = srcmpag.parameterize(srcnf.getNode(arg), srcContext).getReplacement();
                Node parm = tgtnf.caseParm(i);
                if (parm != null) {
                    Node parm2 = tgtmpag.parameterize(parm, tgtContext).getReplacement();
                    addEdge(argNode, parm2);
                    Pair<Node, Node> pval = addInterproceduralAssignment(argNode, parm2, e);
                    if (this.callAssigns != null) {
                        this.callAssigns.put(ie, pval);
                        this.callToMethod.put(ie, srcmpag.getMethod());
                    }
                }
            }
        }
        if (ie instanceof InstanceInvokeExpr) {
            InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
            Node baseNode = srcmpag.parameterize(srcnf.getNode(iie.getBase()), srcContext).getReplacement();
            Node thisRef = tgtmpag.parameterize(tgtnf.caseThis(), tgtContext).getReplacement();
            addEdge(baseNode, thisRef);
            Pair<Node, Node> pval2 = addInterproceduralAssignment(baseNode, thisRef, e);
            if (this.callAssigns != null) {
                boolean virtualCall = !this.callAssigns.put(ie, pval2);
                this.callToMethod.put(ie, srcmpag.getMethod());
                if (virtualCall) {
                    this.virtualCallsToReceivers.putIfAbsent(ie, baseNode);
                }
            }
        }
        if (propagateReturn && (s instanceof AssignStmt)) {
            Value dest = ((AssignStmt) s).getLeftOp();
            if ((dest.getType() instanceof RefLikeType) && !(dest instanceof NullConstant)) {
                Node destNode = srcmpag.parameterize(srcnf.getNode(dest), srcContext).getReplacement();
                Node retNode = tgtmpag.parameterize(tgtnf.caseRet(), tgtContext).getReplacement();
                addEdge(retNode, destNode);
                Pair<Node, Node> pval3 = addInterproceduralAssignment(retNode, destNode, e);
                if (this.callAssigns != null) {
                    this.callAssigns.put(ie, pval3);
                    this.callToMethod.put(ie, srcmpag.getMethod());
                }
            }
        }
    }

    public void cleanPAG() {
        this.simple.clear();
        this.load.clear();
        this.store.clear();
        this.alloc.clear();
        this.simpleInv.clear();
        this.loadInv.clear();
        this.storeInv.clear();
        this.allocInv.clear();
    }

    protected <K extends Node> boolean addToMap(Map<K, Object> m, K key, Node value) {
        Object valueList;
        Object valueList2 = m.get(key);
        if (valueList2 == null) {
            Object hashSet = new HashSet(4);
            valueList = hashSet;
            m.put(key, hashSet);
        } else {
            boolean z = valueList2 instanceof Set;
            valueList = valueList2;
            if (!z) {
                Node[] ar = (Node[]) valueList2;
                HashSet<Node> vl = new HashSet<>(ar.length + 4);
                m.put(key, vl);
                for (Node element : ar) {
                    vl.add(element);
                }
                return vl.add(value);
            }
        }
        return ((Set) valueList).add(value);
    }

    public GlobalNodeFactory nodeFactory() {
        return this.nodeFactory;
    }
}
