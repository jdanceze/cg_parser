package soot.validation;

import java.util.List;
import soot.Body;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/validation/TrapsValidator.class */
public enum TrapsValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static TrapsValidator[] valuesCustom() {
        TrapsValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        TrapsValidator[] trapsValidatorArr = new TrapsValidator[length];
        System.arraycopy(valuesCustom, 0, trapsValidatorArr, 0, length);
        return trapsValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static TrapsValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        PatchingChain<Unit> units = body.getUnits();
        for (Trap t : body.getTraps()) {
            if (!units.contains(t.getBeginUnit())) {
                exception.add(new ValidationException(t.getBeginUnit(), "begin not in chain in " + body.getMethod()));
            }
            if (!units.contains(t.getEndUnit())) {
                exception.add(new ValidationException(t.getEndUnit(), "end not in chain in " + body.getMethod()));
            }
            if (!units.contains(t.getHandlerUnit())) {
                exception.add(new ValidationException(t.getHandlerUnit(), "handler not in chain in " + body.getMethod()));
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
