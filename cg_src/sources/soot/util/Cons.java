package soot.util;
/* loaded from: gencallgraphv3.jar:soot/util/Cons.class */
public final class Cons<U, V> {
    private final U car;
    private final V cdr;

    public Cons(U car, V cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.car == null ? 0 : this.car.hashCode());
        return (31 * result) + (this.cdr == null ? 0 : this.cdr.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cons<U, V> other = (Cons) obj;
        if (this.car == null) {
            if (other.car != null) {
                return false;
            }
        } else if (!this.car.equals(other.car)) {
            return false;
        }
        if (this.cdr == null) {
            if (other.cdr != null) {
                return false;
            }
            return true;
        } else if (!this.cdr.equals(other.cdr)) {
            return false;
        } else {
            return true;
        }
    }

    public U car() {
        return this.car;
    }

    public V cdr() {
        return this.cdr;
    }

    public String toString() {
        return String.valueOf(this.car.toString()) + "," + this.cdr.toString();
    }
}
