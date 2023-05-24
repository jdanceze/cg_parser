package soot.validation;

import java.util.List;
import soot.Body;
/* loaded from: gencallgraphv3.jar:soot/validation/BodyValidator.class */
public interface BodyValidator extends Validator<Body> {
    /* JADX WARN: Can't rename method to resolve collision */
    void validate(Body body, List<ValidationException> list);

    @Override // soot.validation.Validator
    boolean isBasicValidator();

    @Override // soot.validation.Validator
    /* bridge */ /* synthetic */ default void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }
}
