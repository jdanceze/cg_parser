package soot.jimple.spark.ondemand.genericutil;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/Predicate.class */
public abstract class Predicate<T> {
    public static final Predicate FALSE = new Predicate() { // from class: soot.jimple.spark.ondemand.genericutil.Predicate.1
        @Override // soot.jimple.spark.ondemand.genericutil.Predicate
        public boolean test(Object obj_) {
            return false;
        }
    };
    public static final Predicate TRUE = FALSE.not();

    public abstract boolean test(T t);

    public static <T> Predicate<T> truePred() {
        return TRUE;
    }

    public static <T> Predicate<T> falsePred() {
        return FALSE;
    }

    public Predicate<T> not() {
        return new Predicate<T>() { // from class: soot.jimple.spark.ondemand.genericutil.Predicate.2
            @Override // soot.jimple.spark.ondemand.genericutil.Predicate
            public boolean test(T obj_) {
                return !this.test(obj_);
            }
        };
    }

    public Predicate<T> and(final Predicate<T> conjunct_) {
        return new Predicate<T>() { // from class: soot.jimple.spark.ondemand.genericutil.Predicate.3
            @Override // soot.jimple.spark.ondemand.genericutil.Predicate
            public boolean test(T obj_) {
                return this.test(obj_) && conjunct_.test(obj_);
            }
        };
    }

    public Predicate<T> or(final Predicate<T> disjunct_) {
        return new Predicate<T>() { // from class: soot.jimple.spark.ondemand.genericutil.Predicate.4
            @Override // soot.jimple.spark.ondemand.genericutil.Predicate
            public boolean test(T obj_) {
                return this.test(obj_) || disjunct_.test(obj_);
            }
        };
    }
}
