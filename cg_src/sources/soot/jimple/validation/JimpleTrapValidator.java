package soot.jimple.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/JimpleTrapValidator.class */
public enum JimpleTrapValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static JimpleTrapValidator[] valuesCustom() {
        JimpleTrapValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        JimpleTrapValidator[] jimpleTrapValidatorArr = new JimpleTrapValidator[length];
        System.arraycopy(valuesCustom, 0, jimpleTrapValidatorArr, 0, length);
        return jimpleTrapValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static JimpleTrapValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        Set<Unit> caughtUnits = new HashSet<>();
        for (Trap trap : body.getTraps()) {
            caughtUnits.add(trap.getHandlerUnit());
            if (!(trap.getHandlerUnit() instanceof IdentityStmt)) {
                exceptions.add(new ValidationException(trap, "Trap handler does not start with caught exception reference"));
            } else {
                IdentityStmt is = (IdentityStmt) trap.getHandlerUnit();
                if (!(is.getRightOp() instanceof CaughtExceptionRef)) {
                    exceptions.add(new ValidationException(trap, "Trap handler does not start with caught exception reference"));
                }
            }
        }
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof IdentityStmt) {
                IdentityStmt id = (IdentityStmt) u;
                if ((id.getRightOp() instanceof CaughtExceptionRef) && !caughtUnits.contains(id)) {
                    exceptions.add(new ValidationException(id, "Could not find a corresponding trap using this statement as handler", "Body of method " + body.getMethod().getSignature() + " contains a caught exception reference,but not a corresponding trap using this statement as handler"));
                }
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
