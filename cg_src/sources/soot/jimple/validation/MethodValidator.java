package soot.jimple.validation;

import java.util.List;
import soot.Body;
import soot.SootMethod;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/MethodValidator.class */
public enum MethodValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static MethodValidator[] valuesCustom() {
        MethodValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        MethodValidator[] methodValidatorArr = new MethodValidator[length];
        System.arraycopy(valuesCustom, 0, methodValidatorArr, 0, length);
        return methodValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static MethodValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        SootMethod method = body.getMethod();
        if (!method.isAbstract() && method.isStaticInitializer() && !method.isStatic()) {
            exceptions.add(new ValidationException(method, "<clinit> should be static! Static initializer without 'static'('0x8') modifier will cause problem when running on android platform: \"<clinit> is not flagged correctly wrt/ static\"!"));
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
