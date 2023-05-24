package javax.mail.search;

import java.util.Date;
import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/SentDateTerm.class */
public final class SentDateTerm extends DateTerm {
    public SentDateTerm(int comparison, Date date) {
        super(comparison, date);
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            Date d = msg.getSentDate();
            if (d == null) {
                return false;
            }
            return super.match(d);
        } catch (Exception e) {
            return false;
        }
    }

    @Override // javax.mail.search.DateTerm, javax.mail.search.ComparisonTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof SentDateTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}
