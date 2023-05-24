package soot.jimple.toolkits.typing;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import soot.LongType;
import soot.NullType;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.Unit;
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
import soot.util.BitSetIterator;
import soot.util.BitVector;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/TypeResolverBV.class */
public class TypeResolverBV {
    private static final Logger logger = LoggerFactory.getLogger(TypeResolverBV.class);
    private final ClassHierarchy hierarchy;
    private final List<TypeVariableBV> typeVariableList = new ArrayList();
    private final BitVector invalidIds = new BitVector();
    private final Map<Object, TypeVariableBV> typeVariableMap = new HashMap();
    private final JimpleBody stmtBody;
    final TypeNode NULL;
    private final TypeNode OBJECT;
    private static final boolean DEBUG = false;
    private BitVector unsolved;
    private BitVector solved;
    private BitVector single_soft_parent;
    private BitVector single_hard_parent;
    private BitVector multiple_parents;
    private BitVector single_child_not_null;
    private BitVector single_null_child;
    private BitVector multiple_children;

    public ClassHierarchy hierarchy() {
        return this.hierarchy;
    }

    public TypeNode typeNode(Type type) {
        return this.hierarchy.typeNode(type);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TypeVariableBV typeVariable(Local local) {
        TypeVariableBV result = this.typeVariableMap.get(local);
        if (result == null) {
            int id = this.typeVariableList.size();
            this.typeVariableList.add(null);
            result = new TypeVariableBV(id, this);
            this.typeVariableList.set(id, result);
            this.typeVariableMap.put(local, result);
        }
        return result;
    }

    public TypeVariableBV typeVariable(TypeNode typeNode) {
        TypeVariableBV result = this.typeVariableMap.get(typeNode);
        if (result == null) {
            int id = this.typeVariableList.size();
            this.typeVariableList.add(null);
            result = new TypeVariableBV(id, this, typeNode);
            this.typeVariableList.set(id, result);
            this.typeVariableMap.put(typeNode, result);
        }
        return result;
    }

    public TypeVariableBV typeVariable(SootClass sootClass) {
        return typeVariable(this.hierarchy.typeNode(sootClass.getType()));
    }

    public TypeVariableBV typeVariable(Type type) {
        return typeVariable(this.hierarchy.typeNode(type));
    }

    public TypeVariableBV typeVariable() {
        int id = this.typeVariableList.size();
        this.typeVariableList.add(null);
        TypeVariableBV result = new TypeVariableBV(id, this);
        this.typeVariableList.set(id, result);
        return result;
    }

    private TypeResolverBV(JimpleBody stmtBody, Scene scene) {
        this.stmtBody = stmtBody;
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
            TypeResolverBV resolver = new TypeResolverBV(stmtBody, scene);
            resolver.resolve_step_1();
        } catch (TypeException e) {
            try {
                TypeResolverBV resolver2 = new TypeResolverBV(stmtBody, scene);
                resolver2.resolve_step_2();
            } catch (TypeException e2) {
                try {
                    TypeResolverBV resolver3 = new TypeResolverBV(stmtBody, scene);
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
        ConstraintCollectorBV collector = new ConstraintCollectorBV(this, true);
        Iterator<Unit> stmtIt = this.stmtBody.getUnits().iterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            collector.collect(stmt, this.stmtBody);
        }
    }

    private void collect_constraints_3() {
        ConstraintCollectorBV collector = new ConstraintCollectorBV(this, false);
        Iterator<Unit> stmtIt = this.stmtBody.getUnits().iterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            collector.collect(stmt, this.stmtBody);
        }
    }

    private void compute_array_depth() throws TypeException {
        compute_approximate_types();
        TypeVariableBV[] vars = (TypeVariableBV[]) this.typeVariableList.toArray(new TypeVariableBV[this.typeVariableList.size()]);
        for (TypeVariableBV element : vars) {
            element.fixDepth();
        }
    }

    private void propagate_array_constraints() {
        int max = 0;
        for (TypeVariableBV var : this.typeVariableList) {
            int depth = var.depth();
            if (depth > max) {
                max = depth;
            }
        }
        if (max > 1 && !Options.v().j2me() && Options.v().src_prec() != 7) {
            typeVariable(ArrayType.v(RefType.v("java.lang.Cloneable"), max - 1));
            typeVariable(ArrayType.v(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE), max - 1));
        }
        LinkedList[] lists = new LinkedList[max + 1];
        for (int i = 0; i <= max; i++) {
            lists[i] = new LinkedList();
        }
        for (TypeVariableBV var2 : this.typeVariableList) {
            lists[var2.depth()].add(var2);
        }
        for (int i2 = max; i2 >= 0; i2--) {
            for (TypeVariableBV var3 : this.typeVariableList) {
                var3.propagate();
            }
        }
    }

    private void merge_primitive_types() throws TypeException {
        boolean finished;
        compute_solved();
        BitSetIterator varIt = this.solved.iterator();
        while (varIt.hasNext()) {
            TypeVariableBV var = typeVariableForId(varIt.next());
            if ((var.type().type() instanceof IntType) || (var.type().type() instanceof LongType) || (var.type().type() instanceof FloatType) || (var.type().type() instanceof DoubleType)) {
                do {
                    finished = true;
                    BitVector parents = var.parents();
                    if (parents.length() != 0) {
                        finished = false;
                        BitSetIterator j = parents.iterator();
                        while (j.hasNext()) {
                            TypeVariableBV parent = typeVariableForId(j.next());
                            var = var.union(parent);
                        }
                    }
                    BitVector children = var.children();
                    if (children.length() != 0) {
                        finished = false;
                        BitSetIterator j2 = children.iterator();
                        while (j2.hasNext()) {
                            TypeVariableBV child = typeVariableForId(j2.next());
                            var = var.union(child);
                        }
                    }
                } while (!finished);
            }
        }
    }

    private void merge_connected_components() throws TypeException {
        refresh_solved();
        BitVector list = new BitVector();
        list.or(this.solved);
        list.or(this.unsolved);
        new StronglyConnectedComponentsBV(list, this);
    }

    private void remove_transitive_constraints() throws TypeException {
        refresh_solved();
        BitVector list = new BitVector();
        list.or(this.solved);
        list.or(this.unsolved);
        BitSetIterator varIt = list.iterator();
        while (varIt.hasNext()) {
            TypeVariableBV var = typeVariableForId(varIt.next());
            var.removeIndirectRelations();
        }
    }

    private void merge_single_constraints() throws TypeException {
        boolean finished = false;
        boolean modified = false;
        while (true) {
            categorize();
            if (this.single_child_not_null.length() != 0) {
                finished = false;
                modified = true;
                BitSetIterator i = this.single_child_not_null.iterator();
                while (i.hasNext()) {
                    TypeVariableBV var = typeVariableForId(i.next());
                    if (this.single_child_not_null.get(var.id())) {
                        TypeVariableBV child = typeVariableForId(var.children().iterator().next());
                        var.union(child);
                    }
                }
            }
            if (finished) {
                if (this.single_soft_parent.length() != 0) {
                    finished = false;
                    modified = true;
                    BitSetIterator i2 = this.single_soft_parent.iterator();
                    while (i2.hasNext()) {
                        TypeVariableBV var2 = typeVariableForId(i2.next());
                        if (this.single_soft_parent.get(var2.id())) {
                            var2.union(typeVariableForId(var2.parents().iterator().next()));
                        }
                    }
                }
                if (this.single_hard_parent.length() != 0) {
                    finished = false;
                    modified = true;
                    BitSetIterator i3 = this.single_hard_parent.iterator();
                    while (i3.hasNext()) {
                        TypeVariableBV var3 = typeVariableForId(i3.next());
                        if (this.single_hard_parent.get(var3.id())) {
                            TypeVariableBV parent = typeVariableForId(var3.parents().iterator().next());
                            debug_vars("union single parent\n " + var3 + "\n " + parent);
                            var3.union(parent);
                        }
                    }
                }
                if (this.single_null_child.length() != 0) {
                    finished = false;
                    modified = true;
                    BitSetIterator i4 = this.single_null_child.iterator();
                    while (i4.hasNext()) {
                        TypeVariableBV var4 = typeVariableForId(i4.next());
                        if (this.single_null_child.get(var4.id())) {
                            TypeVariableBV child2 = typeVariableForId(var4.children().iterator().next());
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
                BitSetIterator varIt = this.multiple_children.iterator();
                while (varIt.hasNext()) {
                    TypeVariableBV var5 = typeVariableForId(varIt.next());
                    TypeNode lca = null;
                    BitVector children_to_remove = new BitVector();
                    BitSetIterator childIt = var5.children().iterator();
                    while (true) {
                        if (childIt.hasNext()) {
                            TypeVariableBV child3 = typeVariableForId(childIt.next());
                            TypeNode type = child3.type();
                            if (type != null && type.isNull()) {
                                var5.removeChild(child3);
                            } else if (type != null && type.isClass()) {
                                children_to_remove.set(child3.id());
                                if (lca == null) {
                                    lca = type;
                                } else {
                                    lca = lca.lcaIfUnique(type);
                                    if (lca == null) {
                                        break;
                                    }
                                }
                            }
                        } else if (lca != null) {
                            BitSetIterator childIt2 = children_to_remove.iterator();
                            while (childIt2.hasNext()) {
                                TypeVariableBV child4 = typeVariableForId(childIt2.next());
                                var5.removeChild(child4);
                            }
                            var5.addChild(typeVariable(lca));
                        }
                    }
                }
                BitSetIterator varIt2 = this.multiple_parents.iterator();
                while (varIt2.hasNext()) {
                    TypeVariableBV var6 = typeVariableForId(varIt2.next());
                    LinkedList<TypeVariableBV> hp = new LinkedList<>();
                    BitSetIterator parentIt = var6.parents().iterator();
                    while (parentIt.hasNext()) {
                        TypeVariableBV parent2 = typeVariableForId(parentIt.next());
                        TypeNode type2 = parent2.type();
                        if (type2 != null) {
                            Iterator<TypeVariableBV> k = hp.iterator();
                            while (true) {
                                if (!k.hasNext()) {
                                    break;
                                }
                                TypeVariableBV otherparent = k.next();
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
            TypeVariableBV var = typeVariable(local);
            if (var == null) {
                local.setType(Scene.v().getObjectType());
            } else if (var.depth() == 0) {
                if (var.type() == null) {
                    TypeVariableBV.error("Type Error(5):  Variable without type");
                } else {
                    local.setType(var.type().type());
                }
            } else {
                TypeVariableBV element = var.element();
                for (int j = 1; j < var.depth(); j++) {
                    element = element.element();
                }
                if (element.type() == null) {
                    TypeVariableBV.error("Type Error(6):  Array variable without base type");
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
            TypeVariableBV var = typeVariable(local);
            if (var == null || var.approx() == null || var.approx().type() == null) {
                local.setType(Scene.v().getObjectType());
            } else {
                local.setType(var.approx().type());
            }
        }
    }

    private void check_constraints() throws TypeException {
        ConstraintCheckerBV checker = new ConstraintCheckerBV(this, false);
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
        ConstraintCheckerBV checker = new ConstraintCheckerBV(this, true);
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
        TreeSet<TypeVariableBV> workList = new TreeSet<>();
        for (TypeVariableBV var : this.typeVariableList) {
            if (var.type() != null) {
                workList.add(var);
            }
        }
        TypeVariableBV.computeApprox(workList);
        for (TypeVariableBV var2 : this.typeVariableList) {
            if (var2.approx() == this.NULL) {
                var2.union(typeVariable(this.NULL));
            } else if (var2.approx() == null) {
                var2.union(typeVariable(this.NULL));
            }
        }
    }

    private void compute_solved() {
        this.unsolved = new BitVector();
        this.solved = new BitVector();
        for (TypeVariableBV var : this.typeVariableList) {
            if (var.depth() == 0) {
                if (var.type() == null) {
                    this.unsolved.set(var.id());
                } else {
                    this.solved.set(var.id());
                }
            }
        }
    }

    private void refresh_solved() throws TypeException {
        this.unsolved = new BitVector();
        BitSetIterator varIt = this.unsolved.iterator();
        while (varIt.hasNext()) {
            TypeVariableBV var = typeVariableForId(varIt.next());
            if (var.depth() == 0) {
                if (var.type() == null) {
                    this.unsolved.set(var.id());
                } else {
                    this.solved.set(var.id());
                }
            }
        }
    }

    private void categorize() throws TypeException {
        refresh_solved();
        this.single_soft_parent = new BitVector();
        this.single_hard_parent = new BitVector();
        this.multiple_parents = new BitVector();
        this.single_child_not_null = new BitVector();
        this.single_null_child = new BitVector();
        this.multiple_children = new BitVector();
        BitSetIterator i = this.unsolved.iterator();
        while (i.hasNext()) {
            TypeVariableBV var = typeVariableForId(i.next());
            BitVector parents = var.parents();
            int size = parents.length();
            if (size == 0) {
                var.addParent(typeVariable(this.OBJECT));
                this.single_soft_parent.set(var.id());
            } else if (size == 1) {
                TypeVariableBV parent = typeVariableForId(parents.iterator().next());
                if (parent.type() == null) {
                    this.single_soft_parent.set(var.id());
                } else {
                    this.single_hard_parent.set(var.id());
                }
            } else {
                this.multiple_parents.set(var.id());
            }
            BitVector children = var.children();
            int size2 = children.size();
            if (size2 == 0) {
                var.addChild(typeVariable(this.NULL));
                this.single_null_child.set(var.id());
            } else if (size2 == 1) {
                TypeVariableBV child = typeVariableForId(children.iterator().next());
                if (child.type() == this.NULL) {
                    this.single_null_child.set(var.id());
                } else {
                    this.single_child_not_null.set(var.id());
                }
            } else {
                this.multiple_children.set(var.id());
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
                    if (special.getMethodRef().name().equals("<init>")) {
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
                                    Local newlocal = Jimple.v().newLocal("tmp", null);
                                    this.stmtBody.getLocals().add(newlocal);
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

    public TypeVariableBV typeVariableForId(int idx) {
        return this.typeVariableList.get(idx);
    }

    public BitVector invalidIds() {
        return this.invalidIds;
    }

    public void invalidateId(int id) {
        this.invalidIds.set(id);
    }
}
