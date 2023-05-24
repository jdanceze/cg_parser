package soot.validation;

import java.util.List;
import soot.Body;
import soot.Local;
import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/validation/LocalsValidator.class */
public enum LocalsValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static LocalsValidator[] valuesCustom() {
        LocalsValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        LocalsValidator[] localsValidatorArr = new LocalsValidator[length];
        System.arraycopy(valuesCustom, 0, localsValidatorArr, 0, length);
        return localsValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static LocalsValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        for (ValueBox vb : body.getUseBoxes()) {
            validateLocal(body, vb, exception);
        }
        for (ValueBox vb2 : body.getDefBoxes()) {
            validateLocal(body, vb2, exception);
        }
    }

    private void validateLocal(Body body, ValueBox vb, List<ValidationException> exception) {
        Value value = vb.getValue();
        if ((value instanceof Local) && !body.getLocals().contains((Local) value)) {
            exception.add(new ValidationException(value, "Local not in chain : " + value + " in " + body.getMethod()));
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
