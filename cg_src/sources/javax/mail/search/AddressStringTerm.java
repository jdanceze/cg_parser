package javax.mail.search;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/AddressStringTerm.class */
public abstract class AddressStringTerm extends StringTerm {
    /* JADX INFO: Access modifiers changed from: protected */
    public AddressStringTerm(String pattern) {
        super(pattern, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean match(Address a) {
        if (a instanceof InternetAddress) {
            InternetAddress ia = (InternetAddress) a;
            return super.match(ia.toUnicodeString());
        }
        return super.match(a.toString());
    }

    @Override // javax.mail.search.StringTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof AddressStringTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}
