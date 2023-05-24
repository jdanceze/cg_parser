package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.LongType;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/validation/CheckTypesValidator.class */
public enum CheckTypesValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static CheckTypesValidator[] valuesCustom() {
        CheckTypesValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        CheckTypesValidator[] checkTypesValidatorArr = new CheckTypesValidator[length];
        System.arraycopy(valuesCustom, 0, checkTypesValidatorArr, 0, length);
        return checkTypesValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static CheckTypesValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        String methodSuffix = " in " + body.getMethod();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            String errorSuffix = " at " + u + methodSuffix;
            if (u instanceof DefinitionStmt) {
                DefinitionStmt astmt = (DefinitionStmt) u;
                if (!(astmt.getRightOp() instanceof CaughtExceptionRef)) {
                    Type leftType = Type.toMachineType(astmt.getLeftOp().getType());
                    Type rightType = Type.toMachineType(astmt.getRightOp().getType());
                    checkCopy(astmt, exception, leftType, rightType, errorSuffix);
                }
            }
            if (u instanceof Stmt) {
                Stmt stmt = (Stmt) u;
                if (stmt.containsInvokeExpr()) {
                    InvokeExpr iexpr = stmt.getInvokeExpr();
                    SootMethodRef called = iexpr.getMethodRef();
                    if (iexpr instanceof InstanceInvokeExpr) {
                        InstanceInvokeExpr iiexpr = (InstanceInvokeExpr) iexpr;
                        checkCopy(stmt, exception, called.getDeclaringClass().getType(), iiexpr.getBase().getType(), " in receiver of call" + errorSuffix);
                    }
                    int argCount = iexpr.getArgCount();
                    if (called.getParameterTypes().size() != argCount) {
                        exception.add(new ValidationException(stmt, "Argument count does not match the signature of the called function", "Warning: Argument count doesn't match up with signature in call" + errorSuffix));
                    } else {
                        for (int i = 0; i < argCount; i++) {
                            checkCopy(stmt, exception, Type.toMachineType(called.getParameterType(i)), Type.toMachineType(iexpr.getArg(i).getType()), " in argument " + i + " of call" + errorSuffix + " (Note: Parameters are zero-indexed)");
                        }
                    }
                }
            }
        }
    }

    private void checkCopy(Unit stmt, List<ValidationException> exception, Type leftType, Type rightType, String errorSuffix) {
        if ((leftType instanceof PrimType) || (rightType instanceof PrimType)) {
            if (!(leftType instanceof IntType) || !(rightType instanceof IntType)) {
                if ((leftType instanceof LongType) && (rightType instanceof LongType)) {
                    return;
                }
                if ((leftType instanceof FloatType) && (rightType instanceof FloatType)) {
                    return;
                }
                if ((leftType instanceof DoubleType) && (rightType instanceof DoubleType)) {
                    return;
                }
                if (Options.v().src_prec() == 7) {
                    if (!(leftType instanceof RefType) || !((RefType) leftType).getClassName().equals(DotnetBasicTypes.SYSTEM_INTPTR)) {
                        if ((rightType instanceof RefType) && ((RefType) rightType).getClassName().equals(DotnetBasicTypes.SYSTEM_INTPTR)) {
                            return;
                        }
                        if (leftType instanceof RefType) {
                            FastHierarchy fastHierarchy = Scene.v().getFastHierarchy();
                            if (fastHierarchy.canStoreClass(((RefType) leftType).getSootClass(), Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_VALUETYPE)) || ((RefType) leftType).getSootClass().getName().equals(DotnetBasicTypes.SYSTEM_OBJECT) || leftType.equals(RefType.v(DotnetBasicTypes.SYSTEM_ICOMPARABLE)) || leftType.equals(RefType.v(DotnetBasicTypes.SYSTEM_ICOMPARABLE_1)) || leftType.equals(RefType.v(DotnetBasicTypes.SYSTEM_ICONVERTIBLE)) || leftType.equals(RefType.v(DotnetBasicTypes.SYSTEM_IEQUATABLE_1)) || leftType.equals(RefType.v(DotnetBasicTypes.SYSTEM_IFORMATTABLE))) {
                                return;
                            }
                        }
                        if (rightType instanceof RefType) {
                            FastHierarchy fastHierarchy2 = Scene.v().getFastHierarchy();
                            if (fastHierarchy2.canStoreClass(((RefType) rightType).getSootClass(), Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_VALUETYPE))) {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
                exception.add(new ValidationException(stmt, "Warning: Bad use of primitive type" + errorSuffix + " - LeftType is " + leftType.getClass().getName() + " and RightType is " + rightType.getClass().getName()));
            }
        } else if (!(rightType instanceof NullType)) {
            if ((leftType instanceof RefType) && Scene.v().getObjectType().toString().equals(((RefType) leftType).getClassName())) {
                return;
            }
            if ((leftType instanceof ArrayType) || (rightType instanceof ArrayType)) {
                if ((leftType instanceof ArrayType) && (rightType instanceof ArrayType)) {
                    return;
                }
                if ((rightType instanceof ArrayType) && (leftType.equals(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE)) || leftType.equals(RefType.v("java.lang.Cloneable")) || leftType.equals(Scene.v().getObjectType()) || leftType.equals(RefType.v(DotnetBasicTypes.SYSTEM_ARRAY)))) {
                    return;
                }
                exception.add(new ValidationException(stmt, "Warning: Bad use of array type" + errorSuffix));
            } else if ((leftType instanceof RefType) && (rightType instanceof RefType)) {
                SootClass leftClass = ((RefType) leftType).getSootClass();
                SootClass rightClass = ((RefType) rightType).getSootClass();
                if (leftClass.isPhantom() || rightClass.isPhantom()) {
                    return;
                }
                if (leftClass.isInterface()) {
                    if (rightClass.isInterface() && !leftClass.getName().equals(rightClass.getName()) && !Scene.v().getActiveHierarchy().isInterfaceSubinterfaceOf(rightClass, leftClass)) {
                        exception.add(new ValidationException(stmt, "Warning: Bad use of interface type" + errorSuffix));
                    }
                } else if (rightClass.isInterface()) {
                    exception.add(new ValidationException(stmt, "Warning: trying to use interface type where non-Object class expected" + errorSuffix));
                } else if (Options.v().src_prec() == 7) {
                    FastHierarchy fastHierarchy3 = Scene.v().getFastHierarchy();
                    boolean lTypeIsChild = fastHierarchy3.canStoreClass(((RefType) leftType).getSootClass(), Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_VALUETYPE));
                    boolean rTypeIsChild = fastHierarchy3.canStoreClass(((RefType) rightType).getSootClass(), Scene.v().getSootClass(DotnetBasicTypes.SYSTEM_VALUETYPE));
                    if (!lTypeIsChild || rTypeIsChild) {
                    }
                } else if (!Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(rightClass, leftClass)) {
                    exception.add(new ValidationException(stmt, "Warning: Bad use of class type" + errorSuffix));
                }
            } else {
                exception.add(new ValidationException(stmt, "Warning: Bad types" + errorSuffix));
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return false;
    }
}
