package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/FromTerm.class */
public final class FromTerm extends AddressTerm {
    public FromTerm(Address address) {
        super(address);
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            Address[] from = msg.getFrom();
            if (from == null) {
                return false;
            }
            for (Address address : from) {
                if (super.match(address)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // javax.mail.search.AddressTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof FromTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}
