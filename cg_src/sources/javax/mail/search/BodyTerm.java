package javax.mail.search;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/BodyTerm.class */
public final class BodyTerm extends StringTerm {
    public BodyTerm(String pattern) {
        super(pattern);
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        return matchPart(msg);
    }

    private boolean matchPart(Part p) {
        try {
            if (p.isMimeType("text/*")) {
                String s = (String) p.getContent();
                if (s == null) {
                    return false;
                }
                return super.match(s);
            } else if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    if (matchPart(mp.getBodyPart(i))) {
                        return true;
                    }
                }
                return false;
            } else if (p.isMimeType("message/rfc822")) {
                return matchPart((Part) p.getContent());
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override // javax.mail.search.StringTerm
    public boolean equals(Object obj) {
        if (!(obj instanceof BodyTerm)) {
            return false;
        }
        return super.equals(obj);
    }
}
