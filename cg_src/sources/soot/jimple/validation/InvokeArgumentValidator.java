package soot.jimple.validation;

import java.util.List;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.baf.BafBody;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/InvokeArgumentValidator.class */
public enum InvokeArgumentValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static InvokeArgumentValidator[] valuesCustom() {
        InvokeArgumentValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        InvokeArgumentValidator[] invokeArgumentValidatorArr = new InvokeArgumentValidator[length];
        System.arraycopy(valuesCustom, 0, invokeArgumentValidatorArr, 0, length);
        return invokeArgumentValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static InvokeArgumentValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        InvokeExpr iinvExpr;
        SootMethod callee;
        if (!(body instanceof BafBody)) {
            for (Unit unit : body.getUnits().getNonPatchingChain()) {
                Stmt s = (Stmt) unit;
                if (s.containsInvokeExpr() && (callee = (iinvExpr = s.getInvokeExpr()).getMethod()) != null && iinvExpr.getArgCount() != callee.getParameterCount()) {
                    exceptions.add(new ValidationException(s, "Invalid number of arguments"));
                }
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
