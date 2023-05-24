package org.junit.experimental.theories;

import soot.jimple.Jimple;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/theories/PotentialAssignment.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/theories/PotentialAssignment.class */
public abstract class PotentialAssignment {
    public abstract Object getValue() throws CouldNotGenerateValueException;

    public abstract String getDescription() throws CouldNotGenerateValueException;

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/theories/PotentialAssignment$CouldNotGenerateValueException.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/theories/PotentialAssignment$CouldNotGenerateValueException.class */
    public static class CouldNotGenerateValueException extends Exception {
        private static final long serialVersionUID = 1;

        public CouldNotGenerateValueException() {
        }

        public CouldNotGenerateValueException(Throwable e) {
            super(e);
        }
    }

    public static PotentialAssignment forValue(final String name, final Object value) {
        return new PotentialAssignment() { // from class: org.junit.experimental.theories.PotentialAssignment.1
            @Override // org.junit.experimental.theories.PotentialAssignment
            public Object getValue() {
                return value;
            }

            public String toString() {
                return String.format("[%s]", value);
            }

            @Override // org.junit.experimental.theories.PotentialAssignment
            public String getDescription() {
                String valueString;
                if (value == null) {
                    valueString = Jimple.NULL;
                } else {
                    try {
                        valueString = String.format("\"%s\"", value);
                    } catch (Throwable e) {
                        valueString = String.format("[toString() threw %s: %s]", e.getClass().getSimpleName(), e.getMessage());
                    }
                }
                return String.format("%s <from %s>", valueString, name);
            }
        };
    }
}
