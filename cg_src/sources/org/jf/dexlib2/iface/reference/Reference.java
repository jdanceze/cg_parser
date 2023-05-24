package org.jf.dexlib2.iface.reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/reference/Reference.class */
public interface Reference {
    void validateReference() throws InvalidReferenceException;

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/reference/Reference$InvalidReferenceException.class */
    public static class InvalidReferenceException extends Exception {
        private final String invalidReferenceRepresentation;

        public InvalidReferenceException(String invalidReferenceRepresentation) {
            super("Invalid reference");
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public InvalidReferenceException(String invalidReferenceRepresentation, String msg) {
            super(msg);
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public InvalidReferenceException(String invalidReferenceRepresentation, String s, Throwable throwable) {
            super(s, throwable);
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public InvalidReferenceException(String invalidReferenceRepresentation, Throwable throwable) {
            super(throwable);
            this.invalidReferenceRepresentation = invalidReferenceRepresentation;
        }

        public String getInvalidReferenceRepresentation() {
            return this.invalidReferenceRepresentation;
        }
    }
}
