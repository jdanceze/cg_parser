package javax.mail.search;

import java.util.Date;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/DateTerm.class */
public abstract class DateTerm extends ComparisonTerm {
    protected Date date;

    /* JADX INFO: Access modifiers changed from: protected */
    public DateTerm(int comparison, Date date) {
        this.comparison = comparison;
        this.date = date;
    }

    public Date getDate() {
        return new Date(this.date.getTime());
    }

    public int getComparison() {
        return this.comparison;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean match(Date d) {
        switch (this.comparison) {
            case 1:
                return d.before(this.date) || d.equals(this.date);
            case 2:
                return d.before(this.date);
            case 3:
                return d.equals(this.date);
            case 4:
                return !d.equals(this.date);
            case 5:
                return d.after(this.date);
            case 6:
                return d.after(this.date) || d.equals(this.date);
            default:
                return false;
        }
    }

    @Override // javax.mail.search.ComparisonTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof DateTerm)) {
            return false;
        }
        DateTerm dt = (DateTerm) obj;
        return dt.date.equals(this.date) && super.equals(obj);
    }

    @Override // javax.mail.search.ComparisonTerm
    public int hashCode() {
        return this.date.hashCode() + super.hashCode();
    }
}
