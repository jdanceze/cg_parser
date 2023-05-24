package soot.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/validation/OuterClassValidator.class */
public enum OuterClassValidator implements ClassValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static OuterClassValidator[] valuesCustom() {
        OuterClassValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        OuterClassValidator[] outerClassValidatorArr = new OuterClassValidator[length];
        System.arraycopy(valuesCustom, 0, outerClassValidatorArr, 0, length);
        return outerClassValidatorArr;
    }

    @Override // soot.validation.ClassValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(SootClass sootClass, List list) {
        validate(sootClass, (List<ValidationException>) list);
    }

    public static OuterClassValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.ClassValidator
    public void validate(SootClass sc, List<ValidationException> exceptions) {
        Set<SootClass> outerClasses = new HashSet<>();
        SootClass sootClass = sc;
        while (true) {
            SootClass curClass = sootClass;
            if (curClass != null) {
                if (!outerClasses.add(curClass)) {
                    exceptions.add(new ValidationException(curClass, "Circular outer class chain"));
                    return;
                }
                sootClass = curClass.hasOuterClass() ? curClass.getOuterClass() : null;
            } else {
                return;
            }
        }
    }

    @Override // soot.validation.ClassValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
