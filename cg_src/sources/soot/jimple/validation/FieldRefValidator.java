package soot.jimple.validation;

import java.util.List;
import soot.Body;
import soot.ResolutionFailedException;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.validation.BodyValidator;
import soot.validation.UnitValidationException;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/validation/FieldRefValidator.class */
public enum FieldRefValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static FieldRefValidator[] valuesCustom() {
        FieldRefValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        FieldRefValidator[] fieldRefValidatorArr = new FieldRefValidator[length];
        System.arraycopy(valuesCustom, 0, fieldRefValidatorArr, 0, length);
        return fieldRefValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static FieldRefValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exceptions) {
        SootMethod method = body.getMethod();
        if (method.isAbstract()) {
            return;
        }
        for (Unit unit : body.getUnits().getNonPatchingChain()) {
            Stmt s = (Stmt) unit;
            if (s.containsFieldRef()) {
                FieldRef fr = s.getFieldRef();
                if (fr instanceof StaticFieldRef) {
                    StaticFieldRef v = (StaticFieldRef) fr;
                    try {
                        SootField field = v.getField();
                        if (field == null) {
                            exceptions.add(new UnitValidationException(unit, body, "Resolved field is null: " + fr.toString()));
                        } else if (!field.isStatic() && !field.isPhantom()) {
                            exceptions.add(new UnitValidationException(unit, body, "Trying to get a static field which is non-static: " + v));
                        }
                    } catch (ResolutionFailedException e) {
                        exceptions.add(new UnitValidationException(unit, body, "Trying to get a static field which is non-static: " + v));
                    }
                } else if (fr instanceof InstanceFieldRef) {
                    InstanceFieldRef v2 = (InstanceFieldRef) fr;
                    try {
                        SootField field2 = v2.getField();
                        if (field2 == null) {
                            exceptions.add(new UnitValidationException(unit, body, "Resolved field is null: " + fr.toString()));
                        } else if (field2.isStatic() && !field2.isPhantom()) {
                            exceptions.add(new UnitValidationException(unit, body, "Trying to get an instance field which is static: " + v2));
                        }
                    } catch (ResolutionFailedException e2) {
                        exceptions.add(new UnitValidationException(unit, body, "Trying to get an instance field which is static: " + v2));
                    }
                } else {
                    throw new AssertionError("unknown field ref: " + fr);
                }
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return true;
    }
}
