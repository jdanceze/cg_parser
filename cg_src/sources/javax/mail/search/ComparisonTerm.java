package javax.mail.search;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/ComparisonTerm.class */
public abstract class ComparisonTerm extends SearchTerm {
    public static final int LE = 1;
    public static final int LT = 2;
    public static final int EQ = 3;
    public static final int NE = 4;
    public static final int GT = 5;
    public static final int GE = 6;
    protected int comparison;

    public boolean equals(Object obj) {
        if (!(obj instanceof ComparisonTerm)) {
            return false;
        }
        ComparisonTerm ct = (ComparisonTerm) obj;
        return ct.comparison == this.comparison;
    }

    public int hashCode() {
        return this.comparison;
    }
}
