package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/validation/CheckEscapingValidator.class */
public enum CheckEscapingValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static CheckEscapingValidator[] valuesCustom() {
        CheckEscapingValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        CheckEscapingValidator[] checkEscapingValidatorArr = new CheckEscapingValidator[length];
        System.arraycopy(valuesCustom, 0, checkEscapingValidatorArr, 0, length);
        return checkEscapingValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static CheckEscapingValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof Stmt) {
                Stmt stmt = (Stmt) u;
                if (stmt.containsInvokeExpr()) {
                    InvokeExpr iexpr = stmt.getInvokeExpr();
                    SootMethodRef ref = iexpr.getMethodRef();
                    if (ref.name().contains("'") || ref.declaringClass().getName().contains("'")) {
                        exception.add(new ValidationException(stmt, "Escaped name found in signature"));
                    }
                    for (Type paramType : ref.parameterTypes()) {
                        if (paramType.toString().contains("'")) {
                            exception.add(new ValidationException(stmt, "Escaped name found in signature"));
                        }
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
