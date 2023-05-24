package soot.validation;

import java.util.List;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
/* loaded from: gencallgraphv3.jar:soot/validation/MethodDeclarationValidator.class */
public enum MethodDeclarationValidator implements ClassValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static MethodDeclarationValidator[] valuesCustom() {
        MethodDeclarationValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        MethodDeclarationValidator[] methodDeclarationValidatorArr = new MethodDeclarationValidator[length];
        System.arraycopy(valuesCustom, 0, methodDeclarationValidatorArr, 0, length);
        return methodDeclarationValidatorArr;
    }

    @Override // soot.validation.ClassValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(SootClass sootClass, List list) {
        validate(sootClass, (List<ValidationException>) list);
    }

    public static MethodDeclarationValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.ClassValidator
    public void validate(SootClass sc, List<ValidationException> exceptions) {
        if (sc.isConcrete()) {
            for (SootMethod sm : sc.getMethods()) {
                for (Type tp : sm.getParameterTypes()) {
                    if (tp == null) {
                        exceptions.add(new ValidationException(sm, "Null parameter types are invalid"));
                    } else {
                        if (tp instanceof VoidType) {
                            exceptions.add(new ValidationException(sm, "Void parameter types are invalid"));
                        }
                        if (!tp.isAllowedInFinalCode()) {
                            exceptions.add(new ValidationException(sm, "Parameter type not allowed in final code"));
                        }
                    }
                }
            }
        }
    }

    @Override // soot.validation.ClassValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
