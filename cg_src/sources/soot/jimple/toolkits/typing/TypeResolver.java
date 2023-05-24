package soot.jimple.toolkits.typing;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.NullType;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.rifl.RIFLConstants;
import soot.options.Options;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/TypeResolver.class */
public class TypeResolver {
    private static final Logger logger = LoggerFactory.getLogger(TypeResolver.class);
    private final ClassHierarchy hierarchy;
    private final List<TypeVariable> typeVariableList = new ArrayList();
    private final Map<Object, TypeVariable> typeVariableMap = new HashMap();
    private final JimpleBody stmtBody;
    private final LocalGenerator localGenerator;
    final TypeNode NULL;
    private final TypeNode OBJECT;
    private static final boolean DEBUG = false;
    private static final boolean IMPERFORMANT_TYPE_CHECK = false;
    private Collection<TypeVariable> unsolved;
    private Collection<TypeVariable> solved;
    private List<TypeVariable> single_soft_parent;
    private List<TypeVariable> single_hard_parent;
    private List<TypeVariable> multiple_parents;
    private List<TypeVariable> single_child_not_null;
    private List<TypeVariable> single_null_child;
    private List<TypeVariable> multiple_children;

    public ClassHierarchy hierarchy() {
        return this.hierarchy;
    }

    public TypeNode typeNode(Type type) {
        return this.hierarchy.typeNode(type);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TypeVariable typeVariable(Local local) {
        TypeVariable result = this.typeVariableMap.get(local);
        if (result == null) {
            int id = this.typeVariableList.size();
            this.typeVariableList.add(null);
            result = new TypeVariable(id, this);
            this.typeVariableList.set(id, result);
            this.typeVariableMap.put(local, result);
        }
        return result;
    }

    public TypeVariable typeVariable(TypeNode typeNode) {
        TypeVariable result = this.typeVariableMap.get(typeNode);
        if (result == null) {
            int id = this.typeVariableList.size();
            this.typeVariableList.add(null);
            result = new TypeVariable(id, this, typeNode);
            this.typeVariableList.set(id, result);
            this.typeVariableMap.put(typeNode, result);
        }
        return result;
    }

    public TypeVariable typeVariable(SootClass sootClass) {
        return typeVariable(this.hierarchy.typeNode(sootClass.getType()));
    }

    public TypeVariable typeVariable(Type type) {
        return typeVariable(this.hierarchy.typeNode(type));
    }

    public TypeVariable typeVariable() {
        int id = this.typeVariableList.size();
        this.typeVariableList.add(null);
        TypeVariable result = new TypeVariable(id, this);
        this.typeVariableList.set(id, result);
        return result;
    }

    private TypeResolver(JimpleBody stmtBody, Scene scene) {
        this.stmtBody = stmtBody;
        this.localGenerator = Scene.v().createLocalGenerator(stmtBody);
        this.hierarchy = ClassHierarchy.classHierarchy(scene);
        this.OBJECT = this.hierarchy.OBJECT;
        this.NULL = this.hierarchy.NULL;
        typeVariable(this.OBJECT);
        typeVariable(this.NULL);
        if (!Options.v().j2me()) {
            typeVariable(this.hierarchy.CLONEABLE);
            typeVariable(this.hierarchy.SERIALIZABLE);
        }
    }

    public static void resolve(JimpleBody stmtBody, Scene scene) {
        try {
            TypeResolver resolver = new TypeResolver(stmtBody, scene);
            resolver.resolve_step_1();
        } catch (TypeException e) {
            try {
                TypeResolver resolver2 = new TypeResolver(stmtBody, scene);
                resolver2.resolve_step_2();
            } catch (TypeException e2) {
                try {
                    TypeResolver resolver3 = new TypeResolver(stmtBody, scene);
                    resolver3.resolve_step_3();
                } catch (TypeException e3) {
                    StringWriter st = new StringWriter();
                    PrintWriter pw = new PrintWriter(st);
                    logger.error(e3.getMessage(), (Throwable) e3);
                    pw.close();
                    throw new RuntimeException(st.toString());
                }
            }
        }
        soot.jimple.toolkits.typing.integer.TypeResolver.resolve(stmtBody);
    }

    private void debug_vars(String message) {
    }

    private void debug_body() {
    }

    private void resolve_step_1() throws TypeException {
        collect_constraints_1_2();
        debug_vars("constraints");
        compute_array_depth();
        propagate_array_constraints();
        debug_vars("arrays");
        merge_primitive_types();
        debug_vars("primitive");
        merge_connected_components();
        debug_vars("components");
        remove_transitive_constraints();
        debug_vars("transitive");
        merge_single_constraints();
        debug_vars("single");
        assign_types_1_2();
        debug_vars(RIFLConstants.ASSIGN_TAG);
        check_constraints();
    }

    private void resolve_step_2() throws TypeException {
        debug_body();
        split_new();
        debug_body();
        collect_constraints_1_2();
        debug_vars("constraints");
        compute_array_depth();
        propagate_array_constraints();
        debug_vars("arrays");
        merge_primitive_types();
        debug_vars("primitive");
        merge_connected_components();
        debug_vars("components");
        remove_transitive_constraints();
        debug_vars("transitive");
        merge_single_constraints();
        debug_vars("single");
        assign_types_1_2();
        debug_vars(RIFLConstants.ASSIGN_TAG);
        check_constraints();
    }

    private void resolve_step_3() throws TypeException {
        collect_constraints_3();
        compute_approximate_types();
        assign_types_3();
        check_and_fix_constraints();
    }

    private void collect_constraints_1_2() {
        ConstraintCollector collector = new ConstraintCollector(this, true);
        Iterator<Unit> stmtIt = this.stmtBody.getUnits().iterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            collector.collect(stmt, this.stmtBody);
        }
    }

    private void collect_constraints_3() {
        ConstraintCollector collector = new ConstraintCollector(this, false);
        Iterator<Unit> stmtIt = this.stmtBody.getUnits().iterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            collector.collect(stmt, this.stmtBody);
        }
    }

    private void compute_array_depth() throws TypeException {
        compute_approximate_types();
        TypeVariable[] vars = (TypeVariable[]) this.typeVariableList.toArray(new TypeVariable[this.typeVariableList.size()]);
        for (TypeVariable element : vars) {
            element.fixDepth();
        }
    }

    private void propagate_array_constraints() {
        int max = 0;
        for (TypeVariable var : this.typeVariableList) {
            int depth = var.depth();
            if (depth > max) {
                max = depth;
            }
        }
        if (max > 1 && !Options.v().j2me()) {
            typeVariable(ArrayType.v(RefType.v("java.lang.Cloneable"), max - 1));
            typeVariable(ArrayType.v(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE), max - 1));
        }
        for (int i = max; i >= 0; i--) {
            for (TypeVariable var2 : this.typeVariableList) {
                var2.propagate();
            }
        }
    }

    private void merge_primitive_types() throws TypeException {
        boolean finished;
        compute_solved();
        for (TypeVariable var : this.solved) {
            if ((var.type().type() instanceof IntType) || (var.type().type() instanceof LongType) || (var.type().type() instanceof FloatType) || (var.type().type() instanceof DoubleType)) {
                do {
                    finished = true;
                    List<TypeVariable> parents = var.parents();
                    if (parents.size() != 0) {
                        finished = false;
                        for (TypeVariable parent : parents) {
                            var = var.union(parent);
                        }
                    }
                    List<TypeVariable> children = var.children();
                    if (children.size() != 0) {
                        finished = false;
                        for (TypeVariable child : children) {
                            var = var.union(child);
                        }
                    }
                } while (!finished);
            }
        }
    }

    private void merge_connected_components() throws TypeException {
        refresh_solved();
    }

    private void remove_transitive_constraints() throws TypeException {
        refresh_solved();
        for (TypeVariable var : this.solved) {
            var.removeIndirectRelations();
        }
        for (TypeVariable var2 : this.unsolved) {
            var2.removeIndirectRelations();
        }
    }

    private void merge_single_constraints() throws TypeException {
        boolean finished = false;
        boolean modified = false;
        while (true) {
            categorize();
            if (this.single_child_not_null.size() != 0) {
                finished = false;
                modified = true;
                for (TypeVariable var : this.single_child_not_null) {
                    if (this.single_child_not_null.contains(var)) {
                        TypeVariable child = var.children().get(0);
                        var.union(child);
                    }
                }
            }
            if (finished) {
                if (this.single_soft_parent.size() != 0) {
                    finished = false;
                    modified = true;
                    for (TypeVariable var2 : this.single_soft_parent) {
                        if (this.single_soft_parent.contains(var2)) {
                            var2.union(var2.parents().get(0));
                        }
                    }
                }
                if (this.single_hard_parent.size() != 0) {
                    finished = false;
                    modified = true;
                    for (TypeVariable var3 : this.single_hard_parent) {
                        if (this.single_hard_parent.contains(var3)) {
                            TypeVariable parent = var3.parents().get(0);
                            debug_vars("union single parent\n " + var3 + "\n " + parent);
                            var3.union(parent);
                        }
                    }
                }
                if (this.single_null_child.size() != 0) {
                    finished = false;
                    modified = true;
                    for (TypeVariable var4 : this.single_null_child) {
                        if (this.single_null_child.contains(var4)) {
                            TypeVariable child2 = var4.children().get(0);
                            var4.union(child2);
                        }
                    }
                }
                if (finished) {
                    return;
                }
            } else if (modified) {
                modified = false;
            } else {
                finished = true;
                for (TypeVariable var5 : this.multiple_children) {
                    TypeNode lca = null;
                    List<TypeVariable> children_to_remove = new LinkedList<>();
                    var5.fixChildren();
                    Iterator<TypeVariable> it = var5.children().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            TypeVariable child3 = it.next();
                            TypeNode type = child3.type();
                            if (type == null || !type.isNull()) {
                                if (type != null && type.isClass()) {
                                    children_to_remove.add(child3);
                                    if (lca == null) {
                                        lca = type;
                                    } else {
                                        lca = lca.lcaIfUnique(type);
                                        if (lca == null) {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                var5.removeChild(child3);
                            }
                        } else if (lca != null) {
                            for (TypeVariable child4 : children_to_remove) {
                                var5.removeChild(child4);
                            }
                            var5.addChild(typeVariable(lca));
                        }
                    }
                }
                for (TypeVariable var6 : this.multiple_parents) {
                    List<TypeVariable> hp = new ArrayList<>();
                    var6.fixParents();
                    for (TypeVariable parent2 : var6.parents()) {
                        TypeNode type2 = parent2.type();
                        if (type2 != null) {
                            Iterator<TypeVariable> k = hp.iterator();
                            while (true) {
                                if (!k.hasNext()) {
                                    break;
                                }
                                TypeVariable otherparent = k.next();
                                TypeNode othertype = otherparent.type();
                                if (type2.hasDescendant(othertype)) {
                                    var6.removeParent(parent2);
                                    type2 = null;
                                    break;
                                } else if (type2.hasAncestor(othertype)) {
                                    var6.removeParent(otherparent);
                                    k.remove();
                                }
                            }
                            if (type2 != null) {
                                hp.add(parent2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void assign_types_1_2() throws TypeException {
        for (Local local : this.stmtBody.getLocals()) {
            TypeVariable var = typeVariable(local);
            if (var == null) {
                local.setType(Scene.v().getObjectType());
            } else if (var.depth() == 0) {
                if (var.type() == null) {
                    TypeVariable.error("Type Error(5):  Variable without type");
                } else {
                    local.setType(var.type().type());
                }
            } else {
                TypeVariable element = var.element();
                for (int j = 1; j < var.depth(); j++) {
                    element = element.element();
                }
                if (element.type() == null) {
                    TypeVariable.error("Type Error(6):  Array variable without base type");
                } else if (element.type().type() instanceof NullType) {
                    local.setType(NullType.v());
                } else {
                    Type t = element.type().type();
                    if (t instanceof IntType) {
                        local.setType(var.approx().type());
                    } else {
                        local.setType(ArrayType.v(t, var.depth()));
                    }
                }
            }
        }
    }

    private void assign_types_3() throws TypeException {
        for (Local local : this.stmtBody.getLocals()) {
            TypeVariable var = typeVariable(local);
            if (var == null || var.approx() == null || var.approx().type() == null) {
                local.setType(Scene.v().getObjectType());
            } else {
                local.setType(var.approx().type());
            }
        }
    }

    private void check_constraints() throws TypeException {
        ConstraintChecker checker = new ConstraintChecker(this, false);
        Iterator<Unit> stmtIt = this.stmtBody.getUnits().iterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            try {
                checker.check(stmt, this.stmtBody);
            } catch (TypeException e) {
                throw e;
            }
        }
    }

    private void check_and_fix_constraints() throws TypeException {
        ConstraintChecker checker = new ConstraintChecker(this, true);
        PatchingChain<Unit> units = this.stmtBody.getUnits();
        Stmt[] stmts = new Stmt[units.size()];
        units.toArray(stmts);
        for (Stmt stmt : stmts) {
            try {
                checker.check(stmt, this.stmtBody);
            } catch (TypeException e) {
                throw e;
            }
        }
    }

    private void compute_approximate_types() throws TypeException {
        TreeSet<TypeVariable> workList = new TreeSet<>();
        for (TypeVariable var : this.typeVariableList) {
            if (var.type() != null) {
                workList.add(var);
            }
        }
        TypeVariable.computeApprox(workList);
        for (TypeVariable var2 : this.typeVariableList) {
            if (var2.approx() == this.NULL) {
                var2.union(typeVariable(this.NULL));
            } else if (var2.approx() == null) {
                var2.union(typeVariable(this.NULL));
            }
        }
    }

    private void compute_solved() {
        Set<TypeVariable> unsolved_set = new TreeSet<>();
        Set<TypeVariable> solved_set = new TreeSet<>();
        for (TypeVariable var : this.typeVariableList) {
            if (var.depth() == 0) {
                if (var.type() == null) {
                    unsolved_set.add(var);
                } else {
                    solved_set.add(var);
                }
            }
        }
        this.solved = solved_set;
        this.unsolved = unsolved_set;
    }

    private void refresh_solved() throws TypeException {
        Set<TypeVariable> unsolved_set = new TreeSet<>();
        Set<TypeVariable> solved_set = new TreeSet<>(this.solved);
        for (TypeVariable var : this.unsolved) {
            if (var.depth() == 0) {
                if (var.type() == null) {
                    unsolved_set.add(var);
                } else {
                    solved_set.add(var);
                }
            }
        }
        this.solved = solved_set;
        this.unsolved = unsolved_set;
    }

    private void categorize() throws TypeException {
        refresh_solved();
        this.single_soft_parent = new LinkedList();
        this.single_hard_parent = new LinkedList();
        this.multiple_parents = new LinkedList();
        this.single_child_not_null = new LinkedList();
        this.single_null_child = new LinkedList();
        this.multiple_children = new LinkedList();
        for (TypeVariable var : this.unsolved) {
            List<TypeVariable> parents = var.parents();
            int size = parents.size();
            if (size == 0) {
                var.addParent(typeVariable(this.OBJECT));
                this.single_soft_parent.add(var);
            } else if (size == 1) {
                TypeVariable parent = parents.get(0);
                if (parent.type() == null) {
                    this.single_soft_parent.add(var);
                } else {
                    this.single_hard_parent.add(var);
                }
            } else {
                this.multiple_parents.add(var);
            }
            List<TypeVariable> children = var.children();
            int size2 = children.size();
            if (size2 == 0) {
                var.addChild(typeVariable(this.NULL));
                this.single_null_child.add(var);
            } else if (size2 == 1) {
                TypeVariable child = children.get(0);
                if (child.type() == this.NULL) {
                    this.single_null_child.add(var);
                } else {
                    this.single_child_not_null.add(var);
                }
            } else {
                this.multiple_children.add(var);
            }
        }
    }

    private void split_new() {
        LocalDefs defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(this.stmtBody);
        PatchingChain<Unit> units = this.stmtBody.getUnits();
        Stmt[] stmts = new Stmt[units.size()];
        units.toArray(stmts);
        for (Stmt stmt : stmts) {
            if (stmt instanceof InvokeStmt) {
                InvokeStmt invoke = (InvokeStmt) stmt;
                if (invoke.getInvokeExpr() instanceof SpecialInvokeExpr) {
                    SpecialInvokeExpr special = (SpecialInvokeExpr) invoke.getInvokeExpr();
                    if ("<init>".equals(special.getMethodRef().name())) {
                        List<Unit> defsOfAt = defs.getDefsOfAt((Local) special.getBase(), invoke);
                        while (true) {
                            List<Unit> deflist = defsOfAt;
                            if (deflist.size() != 1) {
                                break;
                            }
                            Stmt stmt2 = (Stmt) deflist.get(0);
                            if (stmt2 instanceof AssignStmt) {
                                AssignStmt assign = (AssignStmt) stmt2;
                                if (assign.getRightOp() instanceof Local) {
                                    defsOfAt = defs.getDefsOfAt((Local) assign.getRightOp(), assign);
                                } else if (assign.getRightOp() instanceof NewExpr) {
                                    Local newlocal = this.localGenerator.generateLocal(UnknownType.v());
                                    special.setBase(newlocal);
                                    units.insertAfter(Jimple.v().newAssignStmt(assign.getLeftOp(), newlocal), (Unit) assign);
                                    assign.setLeftOp(newlocal);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
