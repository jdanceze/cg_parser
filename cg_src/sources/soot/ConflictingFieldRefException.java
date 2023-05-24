package soot;
/* loaded from: gencallgraphv3.jar:soot/ConflictingFieldRefException.class */
public class ConflictingFieldRefException extends RuntimeException {
    private static final long serialVersionUID = -2351763146637880592L;
    private final SootField existingField;
    private final Type requestedType;

    public ConflictingFieldRefException(SootField existingField, Type requestedType) {
        this.existingField = existingField;
        this.requestedType = requestedType;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return String.format("Existing field %s does not match expected field type %s", this.existingField.toString(), this.requestedType.toString());
    }
}
