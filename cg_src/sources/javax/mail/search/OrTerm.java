package javax.mail.search;

import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/OrTerm.class */
public final class OrTerm extends SearchTerm {
    protected SearchTerm[] terms;

    public OrTerm(SearchTerm t1, SearchTerm t2) {
        this.terms = new SearchTerm[2];
        this.terms[0] = t1;
        this.terms[1] = t2;
    }

    public OrTerm(SearchTerm[] t) {
        this.terms = new SearchTerm[t.length];
        for (int i = 0; i < t.length; i++) {
            this.terms[i] = t[i];
        }
    }

    public SearchTerm[] getTerms() {
        return (SearchTerm[]) this.terms.clone();
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        for (int i = 0; i < this.terms.length; i++) {
            if (this.terms[i].match(msg)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof OrTerm)) {
            return false;
        }
        OrTerm ot = (OrTerm) obj;
        if (ot.terms.length != this.terms.length) {
            return false;
        }
        for (int i = 0; i < this.terms.length; i++) {
            if (!this.terms[i].equals(ot.terms[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.terms.length; i++) {
            hash += this.terms[i].hashCode();
        }
        return hash;
    }
}
