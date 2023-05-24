package soot.jimple.validation;

import java.util.List;
import soot.Body;
import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/TypesValidator.class */
public enum TypesValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static TypesValidator[] valuesCustom() {
        TypesValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        TypesValidator[] typesValidatorArr = new TypesValidator[length];
        System.arraycopy(valuesCustom, 0, typesValidatorArr, 0, length);
        return typesValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static TypesValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        SootMethod method = body.getMethod();
        if (method != null) {
            if (!method.getReturnType().isAllowedInFinalCode()) {
                exceptions.add(new ValidationException(method, "Return type not allowed in final code: " + method.getReturnType(), "return type not allowed in final code:" + method.getReturnType() + "\n method: " + method));
            }
            for (Type t : method.getParameterTypes()) {
                if (!t.isAllowedInFinalCode()) {
                    exceptions.add(new ValidationException(method, "Parameter type not allowed in final code: " + t, "parameter type not allowed in final code:" + t + "\n method: " + method));
                }
            }
        }
        for (Local l : body.getLocals()) {
            Type t2 = l.getType();
            if (!t2.isAllowedInFinalCode()) {
                exceptions.add(new ValidationException(l, "Local type not allowed in final code: " + t2, "(" + method + ") local type not allowed in final code: " + t2 + " local: " + l));
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
