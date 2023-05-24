package soot.jimple.toolkits.typing.integer;

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
import soot.BooleanType;
import soot.ByteType;
import soot.IntegerType;
import soot.Local;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.infoflow.rifl.RIFLConstants;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/TypeResolver.class */
public class TypeResolver {
    private static final Logger logger = LoggerFactory.getLogger(TypeResolver.class);
    private static final boolean DEBUG = false;
    private static final boolean IMPERFORMANT_TYPE_CHECK = false;
    private final List<TypeVariable> typeVariableList = new ArrayList();
    private final Map<Object, TypeVariable> typeVariableMap = new HashMap();
    final TypeVariable BOOLEAN = typeVariable(ClassHierarchy.v().BOOLEAN);
    final TypeVariable BYTE = typeVariable(ClassHierarchy.v().BYTE);
    final TypeVariable SHORT = typeVariable(ClassHierarchy.v().SHORT);
    final TypeVariable CHAR = typeVariable(ClassHierarchy.v().CHAR);
    final TypeVariable INT = typeVariable(ClassHierarchy.v().INT);
    final TypeVariable TOP = typeVariable(ClassHierarchy.v().TOP);
    final TypeVariable R0_1 = typeVariable(ClassHierarchy.v().R0_1);
    final TypeVariable R0_127 = typeVariable(ClassHierarchy.v().R0_127);
    final TypeVariable R0_32767 = typeVariable(ClassHierarchy.v().R0_32767);
    private final JimpleBody stmtBody;
    private Collection<TypeVariable> unsolved;
    private Collection<TypeVariable> solved;

    private TypeResolver(JimpleBody stmtBody) {
        this.stmtBody = stmtBody;
    }

    public static void resolve(JimpleBody stmtBody) {
        try {
            TypeResolver resolver = new TypeResolver(stmtBody);
            resolver.resolve_step_1();
        } catch (TypeException e) {
            try {
                TypeResolver resolver2 = new TypeResolver(stmtBody);
                resolver2.resolve_step_2();
            } catch (TypeException e2) {
                StringWriter st = new StringWriter();
                PrintWriter pw = new PrintWriter(st);
                logger.error(e2.getMessage(), (Throwable) e2);
                pw.close();
                throw new RuntimeException(st.toString());
            }
        }
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

    public TypeVariable typeVariable(Type type) {
        return typeVariable(ClassHierarchy.v().typeNode(type));
    }

    public TypeVariable typeVariable() {
        int id = this.typeVariableList.size();
        this.typeVariableList.add(null);
        TypeVariable result = new TypeVariable(id, this);
        this.typeVariableList.set(id, result);
        return result;
    }

    private void debug_vars(String message) {
    }

    private void resolve_step_1() throws TypeException {
        collect_constraints_1();
        debug_vars("constraints");
        compute_approximate_types();
        merge_connected_components();
        debug_vars("components");
        merge_single_constraints();
        debug_vars("single");
        assign_types_1();
        debug_vars(RIFLConstants.ASSIGN_TAG);
        check_and_fix_constraints();
    }

    private void resolve_step_2() throws TypeException {
        collect_constraints_2();
        compute_approximate_types();
        assign_types_2();
        check_and_fix_constraints();
    }

    private void collect_constraints_1() {
        ConstraintCollector collector = new ConstraintCollector(this, true);
        Iterator<Unit> it = this.stmtBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            collector.collect(stmt, this.stmtBody);
        }
    }

    private void collect_constraints_2() {
        ConstraintCollector collector = new ConstraintCollector(this, false);
        Iterator<Unit> it = this.stmtBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            collector.collect(stmt, this.stmtBody);
        }
    }

    private void merge_connected_components() throws TypeException {
        compute_solved();
    }

    private void merge_single_constraints() throws TypeException {
        TypeNode approx;
        TypeNode inv_approx;
        TypeVariable parent;
        TypeNode type;
        TypeVariable child;
        TypeNode type2;
        boolean modified = true;
        while (modified) {
            modified = false;
            refresh_solved();
            for (TypeVariable var : this.unsolved) {
                var.fixChildren();
                TypeNode lca = null;
                List<TypeVariable> children_to_remove = new LinkedList<>();
                for (TypeVariable child2 : var.children()) {
                    TypeNode type3 = child2.type();
                    if (type3 != null) {
                        children_to_remove.add(child2);
                        lca = lca == null ? type3 : lca.lca_1(type3);
                    }
                }
                if (lca != null) {
                    for (TypeVariable child3 : children_to_remove) {
                        var.removeChild(child3);
                    }
                    var.addChild(typeVariable(lca));
                }
                if (var.children().size() == 1 && ((type2 = (child = var.children().get(0)).type()) == null || type2.type() != null)) {
                    var.union(child);
                    modified = true;
                }
            }
            if (!modified) {
                for (TypeVariable var2 : this.unsolved) {
                    var2.fixParents();
                    TypeNode gcd = null;
                    List<TypeVariable> parents_to_remove = new LinkedList<>();
                    for (TypeVariable parent2 : var2.parents()) {
                        TypeNode type4 = parent2.type();
                        if (type4 != null) {
                            parents_to_remove.add(parent2);
                            gcd = gcd == null ? type4 : gcd.gcd_1(type4);
                        }
                    }
                    if (gcd != null) {
                        for (TypeVariable parent3 : parents_to_remove) {
                            var2.removeParent(parent3);
                        }
                        var2.addParent(typeVariable(gcd));
                    }
                    if (var2.parents().size() == 1 && ((type = (parent = var2.parents().get(0)).type()) == null || type.type() != null)) {
                        var2.union(parent);
                        modified = true;
                    }
                }
            }
            if (!modified) {
                for (TypeVariable var3 : this.unsolved) {
                    if (var3.type() == null && (inv_approx = var3.inv_approx()) != null && inv_approx.type() != null) {
                        var3.union(typeVariable(inv_approx));
                        modified = true;
                    }
                }
            }
            if (!modified) {
                for (TypeVariable var4 : this.unsolved) {
                    if (var4.type() == null && (approx = var4.approx()) != null && approx.type() != null) {
                        var4.union(typeVariable(approx));
                        modified = true;
                    }
                }
            }
            if (!modified) {
                for (TypeVariable var5 : this.unsolved) {
                    if (var5.type() == null && var5.approx() == ClassHierarchy.v().R0_32767) {
                        var5.union(this.SHORT);
                        modified = true;
                    }
                }
            }
            if (!modified) {
                for (TypeVariable var6 : this.unsolved) {
                    if (var6.type() == null && var6.approx() == ClassHierarchy.v().R0_127) {
                        var6.union(this.BYTE);
                        modified = true;
                    }
                }
            }
            if (!modified) {
                for (TypeVariable var7 : this.R0_1.parents()) {
                    if (var7.type() == null && var7.approx() == ClassHierarchy.v().R0_1) {
                        var7.union(this.BOOLEAN);
                        modified = true;
                    }
                }
            }
        }
    }

    private void assign_types_1() throws TypeException {
        for (Local local : this.stmtBody.getLocals()) {
            if (local.getType() instanceof IntegerType) {
                TypeVariable var = typeVariable(local);
                TypeNode type = var.type();
                if (type == null || type.type() == null) {
                    TypeVariable.error("Type Error(21):  Variable without type");
                } else {
                    local.setType(type.type());
                }
            }
        }
    }

    private void assign_types_2() throws TypeException {
        for (Local local : this.stmtBody.getLocals()) {
            if (local.getType() instanceof IntegerType) {
                TypeVariable var = typeVariable(local);
                TypeNode inv_approx = var.inv_approx();
                if (inv_approx != null && inv_approx.type() != null) {
                    local.setType(inv_approx.type());
                } else {
                    TypeNode approx = var.approx();
                    if (approx.type() != null) {
                        local.setType(approx.type());
                    } else if (approx == ClassHierarchy.v().R0_1) {
                        local.setType(BooleanType.v());
                    } else if (approx == ClassHierarchy.v().R0_127) {
                        local.setType(ByteType.v());
                    } else {
                        local.setType(ShortType.v());
                    }
                }
            }
        }
    }

    private void check_constraints() throws TypeException {
        ConstraintChecker checker = new ConstraintChecker(this, false);
        Iterator<Unit> it = this.stmtBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit stmt = it.next();
            try {
                checker.check((Stmt) stmt, this.stmtBody);
            } catch (TypeException e) {
                throw e;
            }
        }
    }

    private void check_and_fix_constraints() throws TypeException {
        ConstraintChecker checker = new ConstraintChecker(this, true);
        Iterator<Unit> it = this.stmtBody.getUnits().snapshotIterator();
        while (it.hasNext()) {
            Unit stmt = it.next();
            try {
                checker.check((Stmt) stmt, this.stmtBody);
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
        TreeSet<TypeVariable> workList2 = new TreeSet<>();
        for (TypeVariable var2 : this.typeVariableList) {
            if (var2.type() != null) {
                workList2.add(var2);
            }
        }
        TypeVariable.computeInvApprox(workList2);
        for (TypeVariable var3 : this.typeVariableList) {
            if (var3.approx() == null) {
                var3.union(this.INT);
            }
        }
    }

    private void compute_solved() {
        Set<TypeVariable> unsolved_set = new TreeSet<>();
        Set<TypeVariable> solved_set = new TreeSet<>();
        for (TypeVariable var : this.typeVariableList) {
            if (var.type() == null) {
                unsolved_set.add(var);
            } else {
                solved_set.add(var);
            }
        }
        this.solved = solved_set;
        this.unsolved = unsolved_set;
    }

    private void refresh_solved() throws TypeException {
        Set<TypeVariable> unsolved_set = new TreeSet<>();
        Set<TypeVariable> solved_set = new TreeSet<>(this.solved);
        for (TypeVariable var : this.unsolved) {
            if (var.type() == null) {
                unsolved_set.add(var);
            } else {
                solved_set.add(var);
            }
        }
        this.solved = solved_set;
        this.unsolved = unsolved_set;
    }
}
