package javax.mail.search;

import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/HeaderTerm.class */
public final class HeaderTerm extends StringTerm {
    protected String headerName;

    public HeaderTerm(String headerName, String pattern) {
        super(pattern);
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            String[] headers = msg.getHeader(this.headerName);
            if (headers == null) {
                return false;
            }
            for (String str : headers) {
                if (super.match(str)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // javax.mail.search.StringTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof HeaderTerm)) {
            return false;
        }
        HeaderTerm ht = (HeaderTerm) obj;
        return ht.headerName.equalsIgnoreCase(this.headerName) && super.equals(ht);
    }

    @Override // javax.mail.search.StringTerm
    public int hashCode() {
        return this.headerName.toLowerCase().hashCode() + super.hashCode();
    }
}
