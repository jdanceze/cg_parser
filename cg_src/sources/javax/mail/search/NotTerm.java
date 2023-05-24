package javax.mail.search;

import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/NotTerm.class */
public final class NotTerm extends SearchTerm {
    protected SearchTerm term;

    public NotTerm(SearchTerm t) {
        this.term = t;
    }

    public SearchTerm getTerm() {
        return this.term;
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        return !this.term.match(msg);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NotTerm)) {
            return false;
        }
        NotTerm nt = (NotTerm) obj;
        return nt.term.equals(this.term);
    }

    public int hashCode() {
        return this.term.hashCode() << 1;
    }
}
