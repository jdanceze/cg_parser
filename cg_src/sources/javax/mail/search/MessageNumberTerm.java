package javax.mail.search;

import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/MessageNumberTerm.class */
public final class MessageNumberTerm extends IntegerComparisonTerm {
    public MessageNumberTerm(int number) {
        super(3, number);
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            int msgno = msg.getMessageNumber();
            return super.match(msgno);
        } catch (Exception e) {
            return false;
        }
    }

    @Override // javax.mail.search.IntegerComparisonTerm, javax.mail.search.ComparisonTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof MessageNumberTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}
