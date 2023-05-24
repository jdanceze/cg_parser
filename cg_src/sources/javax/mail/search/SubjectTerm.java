package javax.mail.search;

import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/SubjectTerm.class */
public final class SubjectTerm extends StringTerm {
    public SubjectTerm(String pattern) {
        super(pattern);
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            String subj = msg.getSubject();
            if (subj == null) {
                return false;
            }
            return super.match(subj);
        } catch (Exception e) {
            return false;
        }
    }

    @Override // javax.mail.search.StringTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof SubjectTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}
