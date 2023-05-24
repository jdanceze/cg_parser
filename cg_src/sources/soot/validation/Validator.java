package soot.validation;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/validation/Validator.class */
public interface Validator<E> {
    void validate(E e, List<ValidationException> list);

    boolean isBasicValidator();
}
