package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Unit;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/InvokeValidator.class */
public enum InvokeValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static InvokeValidator[] valuesCustom() {
        InvokeValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        InvokeValidator[] invokeValidatorArr = new InvokeValidator[length];
        System.arraycopy(valuesCustom, 0, invokeValidatorArr, 0, length);
        return invokeValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static InvokeValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        SootClass objClass = Scene.v().getObjectType().getSootClass();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit instanceof Stmt) {
                Stmt statement = (Stmt) unit;
                if (statement.containsInvokeExpr()) {
                    InvokeExpr ie = statement.getInvokeExpr();
                    SootMethodRef methodRef = ie.getMethodRef();
                    try {
                        SootMethod method = methodRef.resolve();
                        if (!method.isPhantom()) {
                            if (method.isStaticInitializer()) {
                                exceptions.add(new ValidationException(unit, "Calling <clinit> methods is not allowed."));
                            } else if (method.isStatic()) {
                                if (!(ie instanceof StaticInvokeExpr)) {
                                    exceptions.add(new ValidationException(unit, "Should use staticinvoke for static methods."));
                                }
                            } else {
                                SootClass clazzDeclaring = method.getDeclaringClass();
                                if (clazzDeclaring.isInterface()) {
                                    if (!(ie instanceof InterfaceInvokeExpr) && !(ie instanceof VirtualInvokeExpr) && !(ie instanceof SpecialInvokeExpr)) {
                                        exceptions.add(new ValidationException(unit, "Should use interface/virtual/specialinvoke for interface methods."));
                                    }
                                } else if (method.isPrivate() || method.isConstructor()) {
                                    if (!(ie instanceof SpecialInvokeExpr)) {
                                        String type = method.isPrivate() ? "private methods" : "constructors";
                                        exceptions.add(new ValidationException(unit, "Should use specialinvoke for " + type + "."));
                                    }
                                } else if (methodRef.getDeclaringClass().isInterface() && objClass.equals(clazzDeclaring)) {
                                    if (!(ie instanceof InterfaceInvokeExpr) && !(ie instanceof VirtualInvokeExpr) && !(ie instanceof SpecialInvokeExpr)) {
                                        exceptions.add(new ValidationException(unit, "Should use interface/virtual/specialinvoke for java.lang.Object methods."));
                                    }
                                } else if (!(ie instanceof VirtualInvokeExpr) && !(ie instanceof SpecialInvokeExpr)) {
                                    exceptions.add(new ValidationException(unit, "Should use virtualinvoke or specialinvoke."));
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return false;
    }
}
