package javax.mail.search;

import java.io.Serializable;
import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/SearchTerm.class */
public abstract class SearchTerm implements Serializable {
    public abstract boolean match(Message message);
}
