package javax.mail.search;

import javax.mail.Address;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/AddressTerm.class */
public abstract class AddressTerm extends SearchTerm {
    protected Address address;

    /* JADX INFO: Access modifiers changed from: protected */
    public AddressTerm(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean match(Address a) {
        return a.equals(this.address);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AddressTerm)) {
            return false;
        }
        AddressTerm at = (AddressTerm) obj;
        return at.address.equals(this.address);
    }

    public int hashCode() {
        return this.address.hashCode();
    }
}
