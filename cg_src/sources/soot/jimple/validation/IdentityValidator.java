package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.IdentityUnit;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/IdentityValidator.class */
public enum IdentityValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static IdentityValidator[] valuesCustom() {
        IdentityValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        IdentityValidator[] identityValidatorArr = new IdentityValidator[length];
        System.arraycopy(valuesCustom, 0, identityValidatorArr, 0, length);
        return identityValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static IdentityValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        SootMethod method = body.getMethod();
        int paramCount = method.getParameterCount();
        boolean[] parameterRefs = new boolean[paramCount];
        boolean hasThisLocal = false;
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof IdentityUnit) {
                IdentityUnit id = (IdentityUnit) u;
                Value rhs = id.getRightOp();
                if (rhs instanceof ThisRef) {
                    hasThisLocal = true;
                } else if (rhs instanceof ParameterRef) {
                    ParameterRef ref = (ParameterRef) rhs;
                    if (ref.getIndex() < 0 || ref.getIndex() >= paramCount) {
                        if (paramCount == 0) {
                            exceptions.add(new ValidationException(id, "This method has no parameters, so no parameter reference is allowed"));
                            return;
                        } else {
                            exceptions.add(new ValidationException(id, String.format("Parameter reference index must be between 0 and %d (inclusive)", Integer.valueOf(paramCount - 1))));
                            return;
                        }
                    }
                    if (parameterRefs[ref.getIndex()]) {
                        exceptions.add(new ValidationException(id, String.format("Only one local for parameter %d is allowed", Integer.valueOf(ref.getIndex()))));
                    }
                    parameterRefs[ref.getIndex()] = true;
                } else {
                    continue;
                }
            }
        }
        if (!method.isStatic() && !hasThisLocal) {
            exceptions.add(new ValidationException(body, String.format("The method %s is not static, but does not have a this local", method.getSignature())));
        }
        for (int i = 0; i < paramCount; i++) {
            if (!parameterRefs[i]) {
                exceptions.add(new ValidationException(body, String.format("There is no parameter local for parameter number %d", Integer.valueOf(i))));
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
