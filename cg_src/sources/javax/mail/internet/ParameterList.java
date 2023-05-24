package javax.mail.internet;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.mail.internet.HeaderTokenizer;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/ParameterList.class */
public class ParameterList {
    private Hashtable list = new Hashtable();

    public ParameterList() {
    }

    public ParameterList(String s) throws ParseException {
        HeaderTokenizer h = new HeaderTokenizer(s, HeaderTokenizer.MIME);
        while (true) {
            int type = h.next().getType();
            if (type == -4) {
                return;
            }
            if (((char) type) == ';') {
                HeaderTokenizer.Token tk = h.next();
                if (tk.getType() == -4) {
                    return;
                }
                if (tk.getType() != -1) {
                    throw new ParseException();
                }
                String name = tk.getValue().toLowerCase();
                if (((char) h.next().getType()) != '=') {
                    throw new ParseException();
                }
                HeaderTokenizer.Token tk2 = h.next();
                int type2 = tk2.getType();
                if (type2 != -1 && type2 != -2) {
                    throw new ParseException();
                }
                this.list.put(name, tk2.getValue());
            } else {
                throw new ParseException();
            }
        }
    }

    public int size() {
        return this.list.size();
    }

    public String get(String name) {
        return (String) this.list.get(name.trim().toLowerCase());
    }

    public void set(String name, String value) {
        this.list.put(name.trim().toLowerCase(), value);
    }

    public void remove(String name) {
        this.list.remove(name.trim().toLowerCase());
    }

    public Enumeration getNames() {
        return this.list.keys();
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int used) {
        StringBuffer sb = new StringBuffer();
        Enumeration e = this.list.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = quote((String) this.list.get(name));
            sb.append("; ");
            int used2 = used + 2;
            int len = name.length() + value.length() + 1;
            if (used2 + len > 76) {
                sb.append("\r\n\t");
                used2 = 8;
            }
            sb.append(name).append('=');
            int used3 = used2 + name.length() + 1;
            if (used3 + value.length() > 76) {
                String s = MimeUtility.fold(used3, value);
                sb.append(s);
                int lastlf = s.lastIndexOf(10);
                if (lastlf >= 0) {
                    used = used3 + ((s.length() - lastlf) - 1);
                } else {
                    used = used3 + s.length();
                }
            } else {
                sb.append(value);
                used = used3 + value.length();
            }
        }
        return sb.toString();
    }

    private String quote(String value) {
        return MimeUtility.quote(value, HeaderTokenizer.MIME);
    }
}
