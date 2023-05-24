package soot.jimple.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.VoidType;
import soot.baf.GotoInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.baf.ThrowInst;
import soot.jimple.GotoStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ThrowStmt;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/ReturnStatementsValidator.class */
public enum ReturnStatementsValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static ReturnStatementsValidator[] valuesCustom() {
        ReturnStatementsValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        ReturnStatementsValidator[] returnStatementsValidatorArr = new ReturnStatementsValidator[length];
        System.arraycopy(valuesCustom, 0, returnStatementsValidatorArr, 0, length);
        return returnStatementsValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static ReturnStatementsValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        SootMethod method = body.getMethod();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if ((u instanceof ThrowStmt) || (u instanceof ThrowInst)) {
                return;
            }
            if ((u instanceof ReturnStmt) || (u instanceof ReturnInst)) {
                if (!(method.getReturnType() instanceof VoidType)) {
                    return;
                }
            } else if ((u instanceof ReturnVoidStmt) || (u instanceof ReturnVoidInst)) {
                if (method.getReturnType() instanceof VoidType) {
                    return;
                }
            }
        }
        Unit last = body.getUnits().getLast();
        if ((last instanceof GotoStmt) || (last instanceof GotoInst) || (last instanceof ThrowStmt) || (last instanceof ThrowInst)) {
            return;
        }
        exceptions.add(new ValidationException(method, "The method does not contain a return statement, or the return statement is not of the appropriate type", "Body of method " + method.getSignature() + " does not contain a return statement, or the return statement is not of the appropriate type"));
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
