package soot.validation;

import java.util.List;
import soot.Body;
import soot.Local;
import soot.VoidType;
/* loaded from: gencallgraphv3.jar:soot/validation/CheckVoidLocalesValidator.class */
public enum CheckVoidLocalesValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static CheckVoidLocalesValidator[] valuesCustom() {
        CheckVoidLocalesValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        CheckVoidLocalesValidator[] checkVoidLocalesValidatorArr = new CheckVoidLocalesValidator[length];
        System.arraycopy(valuesCustom, 0, checkVoidLocalesValidatorArr, 0, length);
        return checkVoidLocalesValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static CheckVoidLocalesValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        for (Local l : body.getLocals()) {
            if (l.getType() instanceof VoidType) {
                exception.add(new ValidationException(l, "Local " + l + " in " + body.getMethod() + " defined with void type"));
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return false;
    }
}
