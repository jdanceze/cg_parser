package soot.validation;

import java.util.List;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/validation/ClassValidator.class */
public interface ClassValidator extends Validator<SootClass> {
    /* JADX WARN: Can't rename method to resolve collision */
    void validate(SootClass sootClass, List<ValidationException> list);

    @Override // soot.validation.Validator
    boolean isBasicValidator();

    @Override // soot.validation.Validator
    /* bridge */ /* synthetic */ default void validate(SootClass sootClass, List list) {
        validate(sootClass, (List<ValidationException>) list);
    }
}
