package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.ClassMember;
import soot.Hierarchy;
import soot.Local;
import soot.LocalGenerator;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/invoke/AccessManager.class */
public class AccessManager {
    public static boolean isAccessLegal(SootMethod container, ClassMember target) {
        SootClass targetClass = target.getDeclaringClass();
        if (!isAccessLegal(container, targetClass)) {
            return false;
        }
        SootClass containerClass = container.getDeclaringClass();
        if (target.isPrivate() && !targetClass.getName().equals(containerClass.getName())) {
            return false;
        }
        if (!target.isPrivate() && !target.isProtected() && !target.isPublic() && !targetClass.getPackageName().equals(containerClass.getPackageName())) {
            return false;
        }
        if (target.isProtected() && !targetClass.getPackageName().equals(containerClass.getPackageName())) {
            Hierarchy h = Scene.v().getActiveHierarchy();
            return h.isClassSuperclassOfIncluding(targetClass, containerClass);
        }
        return true;
    }

    public static boolean isAccessLegal(SootMethod container, SootClass target) {
        return target.isPublic() || container.getDeclaringClass().getPackageName().equals(target.getPackageName());
    }

    public static boolean isAccessLegal(SootMethod container, Stmt stmt) {
        if (stmt.containsInvokeExpr()) {
            return isAccessLegal(container, stmt.getInvokeExpr().getMethod());
        }
        if (stmt instanceof AssignStmt) {
            AssignStmt as = (AssignStmt) stmt;
            Value rightOp = as.getRightOp();
            if (rightOp instanceof FieldRef) {
                return isAccessLegal(container, ((FieldRef) rightOp).getField());
            }
            Value leftOp = as.getLeftOp();
            if (leftOp instanceof FieldRef) {
                return isAccessLegal(container, ((FieldRef) leftOp).getField());
            }
            return true;
        }
        return true;
    }

    public static void createAccessorMethods(Body body, Stmt before, Stmt after) {
        Chain<Unit> units = body.getUnits();
        if ((before != null && !units.contains(before)) || (after != null && !units.contains(after))) {
            throw new RuntimeException();
        }
        boolean bInside = before == null;
        Iterator it = new ArrayList(units).iterator();
        while (it.hasNext()) {
            Unit unit = (Unit) it.next();
            Stmt s = (Stmt) unit;
            if (bInside) {
                if (s == after) {
                    return;
                }
                SootMethod m = body.getMethod();
                if (!isAccessLegal(m, s)) {
                    createAccessorMethod(m, s);
                }
            } else if (s == before) {
                bInside = true;
            }
        }
    }

    public static String createAccessorName(ClassMember member, boolean setter) {
        StringBuilder name = new StringBuilder("access$");
        if (member instanceof SootField) {
            name.append(setter ? "set$" : "get$");
            SootField f = (SootField) member;
            name.append(f.getName());
        } else {
            SootMethod m = (SootMethod) member;
            name.append(m.getName()).append('$');
            for (Type type : m.getParameterTypes()) {
                name.append(type.toString().replaceAll("\\.", "\\$\\$")).append('$');
            }
        }
        return name.toString();
    }

    public static void createAccessorMethod(SootMethod container, Stmt stmt) {
        if (!container.getActiveBody().getUnits().contains(stmt)) {
            throw new RuntimeException();
        }
        if (stmt.containsInvokeExpr()) {
            createInvokeAccessor(container, stmt);
        } else if (stmt instanceof AssignStmt) {
            AssignStmt as = (AssignStmt) stmt;
            Value leftOp = as.getLeftOp();
            if (leftOp instanceof FieldRef) {
                createSetAccessor(container, as, (FieldRef) leftOp);
                return;
            }
            Value rightOp = as.getRightOp();
            if (rightOp instanceof FieldRef) {
                createGetAccessor(container, as, (FieldRef) rightOp);
                return;
            }
            throw new RuntimeException("Expected class member access");
        } else {
            throw new RuntimeException("Expected class member access");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void createGetAccessor(SootMethod container, AssignStmt as, FieldRef ref) {
        List<Type> parameterTypes;
        Jimple jimp = Jimple.v();
        SootClass target = ref.getField().getDeclaringClass();
        String name = createAccessorName(ref.getField(), false);
        SootMethod accessor = target.getMethodByNameUnsafe(name);
        if (accessor == null) {
            JimpleBody accessorBody = jimp.newBody();
            Chain<Unit> accStmts = accessorBody.getUnits();
            LocalGenerator lg = Scene.v().createLocalGenerator(accessorBody);
            Type targetType = target.getType();
            Local thisLocal = lg.generateLocal(targetType);
            if (ref instanceof InstanceFieldRef) {
                accStmts.addFirst(jimp.newIdentityStmt(thisLocal, jimp.newParameterRef(targetType, 0)));
                parameterTypes = Collections.singletonList(targetType);
            } else {
                parameterTypes = Collections.emptyList();
            }
            Type refFieldType = ref.getField().getType();
            Local l = lg.generateLocal(refFieldType);
            accStmts.add(jimp.newAssignStmt(l, ref instanceof InstanceFieldRef ? jimp.newInstanceFieldRef(thisLocal, ref.getFieldRef()) : jimp.newStaticFieldRef(ref.getFieldRef())));
            accStmts.add(jimp.newReturnStmt(l));
            accessor = Scene.v().makeSootMethod(name, parameterTypes, refFieldType, 9, Collections.emptyList());
            accessorBody.setMethod(accessor);
            accessor.setActiveBody(accessorBody);
            target.addMethod(accessor);
        }
        List<Value> args = ref instanceof InstanceFieldRef ? Collections.singletonList(((InstanceFieldRef) ref).getBase()) : Collections.emptyList();
        as.setRightOp(jimp.newStaticInvokeExpr(accessor.makeRef(), args));
    }

    private static void createSetAccessor(SootMethod container, AssignStmt as, FieldRef ref) {
        Jimple jimp = Jimple.v();
        SootClass target = ref.getField().getDeclaringClass();
        String name = createAccessorName(ref.getField(), true);
        SootMethod accessor = target.getMethodByNameUnsafe(name);
        if (accessor == null) {
            JimpleBody accessorBody = jimp.newBody();
            Chain<Unit> accStmts = accessorBody.getUnits();
            LocalGenerator lg = Scene.v().createLocalGenerator(accessorBody);
            Local thisLocal = lg.generateLocal(target.getType());
            List<Type> parameterTypes = new ArrayList<>(2);
            int paramID = 0;
            if (ref instanceof InstanceFieldRef) {
                accStmts.add(jimp.newIdentityStmt(thisLocal, jimp.newParameterRef(target.getType(), 0)));
                parameterTypes.add(target.getType());
                paramID = 0 + 1;
            }
            parameterTypes.add(ref.getField().getType());
            Local l = lg.generateLocal(ref.getField().getType());
            accStmts.add(jimp.newIdentityStmt(l, jimp.newParameterRef(ref.getField().getType(), paramID)));
            int i = paramID + 1;
            if (ref instanceof InstanceFieldRef) {
                accStmts.add(jimp.newAssignStmt(jimp.newInstanceFieldRef(thisLocal, ref.getFieldRef()), l));
            } else {
                accStmts.add(jimp.newAssignStmt(jimp.newStaticFieldRef(ref.getFieldRef()), l));
            }
            accStmts.addLast(jimp.newReturnVoidStmt());
            accessor = Scene.v().makeSootMethod(name, parameterTypes, VoidType.v(), 9, Collections.emptyList());
            accessorBody.setMethod(accessor);
            accessor.setActiveBody(accessorBody);
            target.addMethod(accessor);
        }
        ArrayList<Value> args = new ArrayList<>(2);
        if (ref instanceof InstanceFieldRef) {
            args.add(((InstanceFieldRef) ref).getBase());
        }
        args.add(as.getRightOp());
        Chain<Unit> containerStmts = container.getActiveBody().getUnits();
        containerStmts.insertAfter(jimp.newInvokeStmt(jimp.newStaticInvokeExpr(accessor.makeRef(), args)), as);
        containerStmts.remove(as);
    }

    private static void createInvokeAccessor(SootMethod container, Stmt stmt) {
        InvokeExpr accExpr;
        Jimple jimp = Jimple.v();
        InvokeExpr expr = stmt.getInvokeExpr();
        SootMethod method = expr.getMethod();
        SootClass target = method.getDeclaringClass();
        String name = createAccessorName(method, true);
        SootMethod accessor = target.getMethodByNameUnsafe(name);
        if (accessor == null) {
            JimpleBody accessorBody = jimp.newBody();
            Chain<Unit> accStmts = accessorBody.getUnits();
            LocalGenerator lg = Scene.v().createLocalGenerator(accessorBody);
            List<Type> parameterTypes = new ArrayList<>();
            if (expr instanceof InstanceInvokeExpr) {
                parameterTypes.add(target.getType());
            }
            parameterTypes.addAll(method.getParameterTypes());
            List<Local> arguments = new ArrayList<>();
            int paramID = 0;
            for (Type type : parameterTypes) {
                Local l = lg.generateLocal(type);
                accStmts.add(jimp.newIdentityStmt(l, jimp.newParameterRef(type, paramID)));
                arguments.add(l);
                paramID++;
            }
            if (expr instanceof StaticInvokeExpr) {
                accExpr = jimp.newStaticInvokeExpr(method.makeRef(), arguments);
            } else if (expr instanceof VirtualInvokeExpr) {
                Local thisLocal = arguments.get(0);
                arguments.remove(0);
                accExpr = jimp.newVirtualInvokeExpr(thisLocal, method.makeRef(), arguments);
            } else if (expr instanceof SpecialInvokeExpr) {
                Local thisLocal2 = arguments.get(0);
                arguments.remove(0);
                accExpr = jimp.newSpecialInvokeExpr(thisLocal2, method.makeRef(), arguments);
            } else {
                throw new RuntimeException();
            }
            Type returnType = method.getReturnType();
            if (returnType instanceof VoidType) {
                Stmt s = jimp.newInvokeStmt(accExpr);
                accStmts.add(s);
                accStmts.add(jimp.newReturnVoidStmt());
            } else {
                Local resultLocal = lg.generateLocal(returnType);
                Stmt s2 = jimp.newAssignStmt(resultLocal, accExpr);
                accStmts.add(s2);
                accStmts.add(jimp.newReturnStmt(resultLocal));
            }
            accessor = Scene.v().makeSootMethod(name, parameterTypes, returnType, 9, method.getExceptions());
            accessorBody.setMethod(accessor);
            accessor.setActiveBody(accessorBody);
            target.addMethod(accessor);
        }
        List<Value> args = new ArrayList<>();
        if (expr instanceof InstanceInvokeExpr) {
            args.add(((InstanceInvokeExpr) expr).getBase());
        }
        args.addAll(expr.getArgs());
        stmt.getInvokeExprBox().setValue(jimp.newStaticInvokeExpr(accessor.makeRef(), args));
    }

    public static boolean ensureAccess(SootMethod container, ClassMember target, String options) {
        SootClass targetClass = target.getDeclaringClass();
        if (!ensureAccess(container, targetClass, options)) {
            return false;
        }
        if (isAccessLegal(container, target)) {
            return true;
        }
        if (!targetClass.isApplicationClass()) {
            return false;
        }
        if (options != null) {
            switch (options.hashCode()) {
                case -2115023092:
                    if (options.equals("accessors")) {
                        return true;
                    }
                    break;
                case -840246874:
                    if (options.equals("unsafe")) {
                        target.setModifiers(target.getModifiers() | 1);
                        return true;
                    }
                    break;
                case 3387192:
                    if (options.equals("none")) {
                        return false;
                    }
                    break;
            }
        }
        throw new RuntimeException("Not implemented yet!");
    }

    public static boolean ensureAccess(SootMethod container, SootClass target, String options) {
        if (isAccessLegal(container, target)) {
            return true;
        }
        if (options != null) {
            switch (options.hashCode()) {
                case -2115023092:
                    if (options.equals("accessors")) {
                        return false;
                    }
                    break;
                case -840246874:
                    if (options.equals("unsafe")) {
                        if (target.isApplicationClass()) {
                            target.setModifiers(target.getModifiers() | 1);
                            return true;
                        }
                        return false;
                    }
                    break;
                case 3387192:
                    if (options.equals("none")) {
                        return false;
                    }
                    break;
            }
        }
        throw new RuntimeException("Not implemented yet!");
    }

    private AccessManager() {
    }
}
