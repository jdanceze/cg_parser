package soot.jimple.validation;

import java.util.List;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.util.Chain;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/IdentityStatementsValidator.class */
public enum IdentityStatementsValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static IdentityStatementsValidator[] valuesCustom() {
        IdentityStatementsValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        IdentityStatementsValidator[] identityStatementsValidatorArr = new IdentityStatementsValidator[length];
        System.arraycopy(valuesCustom, 0, identityStatementsValidatorArr, 0, length);
        return identityStatementsValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static IdentityStatementsValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        SootMethod method = body.getMethod();
        if (method.isAbstract()) {
            return;
        }
        Chain<Unit> units = body.getUnits().getNonPatchingChain();
        boolean foundNonThisOrParamIdentityStatement = false;
        boolean firstStatement = true;
        for (Unit unit : units) {
            if (unit instanceof IdentityStmt) {
                IdentityStmt identityStmt = (IdentityStmt) unit;
                if (identityStmt.getRightOp() instanceof ThisRef) {
                    if (method.isStatic()) {
                        exceptions.add(new ValidationException(identityStmt, "@this-assignment in a static method!"));
                    }
                    if (!firstStatement) {
                        exceptions.add(new ValidationException(identityStmt, "@this-assignment statement should precede all other statements\n method: " + method));
                    }
                } else if (identityStmt.getRightOp() instanceof ParameterRef) {
                    if (foundNonThisOrParamIdentityStatement) {
                        exceptions.add(new ValidationException(identityStmt, "@param-assignment statements should precede all non-identity statements\n method: " + method));
                    }
                } else {
                    foundNonThisOrParamIdentityStatement = true;
                }
            } else {
                foundNonThisOrParamIdentityStatement = true;
            }
            firstStatement = false;
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
