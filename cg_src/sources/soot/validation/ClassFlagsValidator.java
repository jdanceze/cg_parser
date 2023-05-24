package soot.validation;

import java.util.List;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/validation/ClassFlagsValidator.class */
public enum ClassFlagsValidator implements ClassValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static ClassFlagsValidator[] valuesCustom() {
        ClassFlagsValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        ClassFlagsValidator[] classFlagsValidatorArr = new ClassFlagsValidator[length];
        System.arraycopy(valuesCustom, 0, classFlagsValidatorArr, 0, length);
        return classFlagsValidatorArr;
    }

    @Override // soot.validation.ClassValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(SootClass sootClass, List list) {
        validate(sootClass, (List<ValidationException>) list);
    }

    public static ClassFlagsValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.ClassValidator
    public void validate(SootClass sc, List<ValidationException> exceptions) {
        if (sc.isInterface() && sc.isEnum()) {
            exceptions.add(new ValidationException(sc, "Class is both an interface and an enum"));
        }
        if (sc.isSynchronized()) {
            exceptions.add(new ValidationException(sc, "Classes cannot be synchronized"));
        }
    }

    @Override // soot.validation.ClassValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
