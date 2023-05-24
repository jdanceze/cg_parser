package javax.mail.search;

import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/MessageIDTerm.class */
public final class MessageIDTerm extends StringTerm {
    public MessageIDTerm(String msgid) {
        super(msgid);
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            String[] s = msg.getHeader("Message-ID");
            if (s == null) {
                return false;
            }
            for (String str : s) {
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
        if (!(obj instanceof MessageIDTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}
