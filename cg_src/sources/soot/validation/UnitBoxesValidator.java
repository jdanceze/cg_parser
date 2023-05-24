package soot.validation;

import java.util.List;
import soot.Body;
import soot.UnitBox;
/* loaded from: gencallgraphv3.jar:soot/validation/UnitBoxesValidator.class */
public enum UnitBoxesValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static UnitBoxesValidator[] valuesCustom() {
        UnitBoxesValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        UnitBoxesValidator[] unitBoxesValidatorArr = new UnitBoxesValidator[length];
        System.arraycopy(valuesCustom, 0, unitBoxesValidatorArr, 0, length);
        return unitBoxesValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static UnitBoxesValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        for (UnitBox ub : body.getAllUnitBoxes()) {
            if (!body.getUnits().contains(ub.getUnit())) {
                exception.add(new ValidationException(ub, "UnitBox points outside unitChain! to unit: " + ub.getUnit() + " in " + body.getMethod()));
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
