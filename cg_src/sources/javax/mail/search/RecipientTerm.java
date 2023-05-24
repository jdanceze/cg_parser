package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/RecipientTerm.class */
public final class RecipientTerm extends AddressTerm {
    protected Message.RecipientType type;

    public RecipientTerm(Message.RecipientType type, Address address) {
        super(address);
        this.type = type;
    }

    public Message.RecipientType getRecipientType() {
        return this.type;
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            Address[] recipients = msg.getRecipients(this.type);
            if (recipients == null) {
                return false;
            }
            for (Address address : recipients) {
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
        if (!(obj instanceof RecipientTerm)) {
            return false;
        }
        RecipientTerm rt = (RecipientTerm) obj;
        return rt.type.equals(this.type) && super.equals(obj);
    }

    @Override // javax.mail.search.AddressTerm
    public int hashCode() {
        return this.type.hashCode() + super.hashCode();
    }
}
