package soot.jimple.infoflow.data;

import java.util.Objects;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/ContextDefinition.class */
public class ContextDefinition {
    private final String name;
    private final String value;

    public ContextDefinition(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return String.format("%s=%s", this.name, this.value);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ContextDefinition other = (ContextDefinition) obj;
        return Objects.equals(this.name, other.name) && Objects.equals(this.value, other.value);
    }
}
