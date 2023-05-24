package soot.validation;

import java.io.PrintStream;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.Body;
import soot.Unit;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/validation/ValueBoxesValidator.class */
public enum ValueBoxesValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static ValueBoxesValidator[] valuesCustom() {
        ValueBoxesValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        ValueBoxesValidator[] valueBoxesValidatorArr = new ValueBoxesValidator[length];
        System.arraycopy(valuesCustom, 0, valueBoxesValidatorArr, 0, length);
        return valueBoxesValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static ValueBoxesValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        boolean foundProblem = false;
        Set<ValueBox> set = Collections.newSetFromMap(new IdentityHashMap());
        for (ValueBox vb : body.getUseAndDefBoxes()) {
            if (!set.add(vb)) {
                foundProblem = true;
                exception.add(new ValidationException(vb, "Aliased value box : " + vb + " in " + body.getMethod()));
            }
        }
        if (foundProblem) {
            PrintStream err = System.err;
            err.println("Found a ValueBox used more than once in body of " + body.getMethod() + ":");
            Iterator<Unit> it = body.getUnits().iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                err.println("\t" + u);
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return false;
    }
}
