package javax.mail.search;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/IntegerComparisonTerm.class */
public abstract class IntegerComparisonTerm extends ComparisonTerm {
    protected int number;

    /* JADX INFO: Access modifiers changed from: protected */
    public IntegerComparisonTerm(int comparison, int number) {
        this.comparison = comparison;
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public int getComparison() {
        return this.comparison;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean match(int i) {
        switch (this.comparison) {
            case 1:
                return i <= this.number;
            case 2:
                return i < this.number;
            case 3:
                return i == this.number;
            case 4:
                return i != this.number;
            case 5:
                return i > this.number;
            case 6:
                return i >= this.number;
            default:
                return false;
        }
    }

    @Override // javax.mail.search.ComparisonTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerComparisonTerm)) {
            return false;
        }
        IntegerComparisonTerm ict = (IntegerComparisonTerm) obj;
        return ict.number == this.number && super.equals(obj);
    }

    @Override // javax.mail.search.ComparisonTerm
    public int hashCode() {
        return this.number + super.hashCode();
    }
}
