package javax.mail.internet;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Session;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/InternetAddress.class */
public class InternetAddress extends Address implements Cloneable {
    protected String address;
    protected String personal;
    protected String encodedPersonal;
    private static final String rfc822phrase = HeaderTokenizer.RFC822.replace(' ', (char) 0).replace('\t', (char) 0);
    private static final String specialsNoDotNoAt = "()<>,;:\\\"[]";
    private static final String specialsNoDot = "()<>,;:\\\"[]@";

    public InternetAddress() {
    }

    public InternetAddress(String address) throws AddressException {
        InternetAddress[] a = parse(address, true);
        if (a.length != 1) {
            throw new AddressException("Illegal address", address);
        }
        this.address = a[0].address;
        this.personal = a[0].personal;
        this.encodedPersonal = a[0].encodedPersonal;
    }

    public InternetAddress(String address, boolean strict) throws AddressException {
        this(address);
        if (strict) {
            checkAddress(this.address, true, true);
        }
    }

    public InternetAddress(String address, String personal) throws UnsupportedEncodingException {
        this(address, personal, null);
    }

    public InternetAddress(String address, String personal, String charset) throws UnsupportedEncodingException {
        this.address = address;
        setPersonal(personal, charset);
    }

    public Object clone() {
        InternetAddress a = null;
        try {
            a = (InternetAddress) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return a;
    }

    @Override // javax.mail.Address
    public String getType() {
        return "rfc822";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPersonal(String name, String charset) throws UnsupportedEncodingException {
        this.personal = name;
        if (name != null) {
            this.encodedPersonal = MimeUtility.encodeWord(name, charset, null);
        } else {
            this.encodedPersonal = null;
        }
    }

    public void setPersonal(String name) throws UnsupportedEncodingException {
        this.personal = name;
        if (name != null) {
            this.encodedPersonal = MimeUtility.encodeWord(name);
        } else {
            this.encodedPersonal = null;
        }
    }

    public String getAddress() {
        return this.address;
    }

    public String getPersonal() {
        if (this.personal != null) {
            return this.personal;
        }
        if (this.encodedPersonal != null) {
            try {
                this.personal = MimeUtility.decodeText(this.encodedPersonal);
                return this.personal;
            } catch (Exception e) {
                return this.encodedPersonal;
            }
        }
        return null;
    }

    @Override // javax.mail.Address
    public String toString() {
        if (this.encodedPersonal == null && this.personal != null) {
            try {
                this.encodedPersonal = MimeUtility.encodeWord(this.personal);
            } catch (UnsupportedEncodingException e) {
            }
        }
        if (this.encodedPersonal != null) {
            return new StringBuffer().append(quotePhrase(this.encodedPersonal)).append(" <").append(this.address).append(">").toString();
        }
        if (isGroup() || isSimple()) {
            return this.address;
        }
        return new StringBuffer().append("<").append(this.address).append(">").toString();
    }

    public String toUnicodeString() {
        if (getPersonal() != null) {
            return new StringBuffer().append(quotePhrase(this.personal)).append(" <").append(this.address).append(">").toString();
        }
        if (isGroup() || isSimple()) {
            return this.address;
        }
        return new StringBuffer().append("<").append(this.address).append(">").toString();
    }

    private static String quotePhrase(String phrase) {
        int len = phrase.length();
        boolean needQuoting = false;
        for (int i = 0; i < len; i++) {
            char c = phrase.charAt(i);
            if (c == '\"' || c == '\\') {
                StringBuffer sb = new StringBuffer(len + 3);
                sb.append('\"');
                for (int j = 0; j < len; j++) {
                    char cc = phrase.charAt(j);
                    if (cc == '\"' || cc == '\\') {
                        sb.append('\\');
                    }
                    sb.append(cc);
                }
                sb.append('\"');
                return sb.toString();
            }
            if ((c < ' ' && c != '\r' && c != '\n' && c != '\t') || c >= 127 || rfc822phrase.indexOf(c) >= 0) {
                needQuoting = true;
            }
        }
        if (needQuoting) {
            StringBuffer sb2 = new StringBuffer(len + 2);
            sb2.append('\"').append(phrase).append('\"');
            return sb2.toString();
        }
        return phrase;
    }

    private static String unquote(String s) {
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
            if (s.indexOf(92) >= 0) {
                StringBuffer sb = new StringBuffer(s.length());
                int i = 0;
                while (i < s.length()) {
                    char c = s.charAt(i);
                    if (c == '\\' && i < s.length() - 1) {
                        i++;
                        c = s.charAt(i);
                    }
                    sb.append(c);
                    i++;
                }
                s = sb.toString();
            }
        }
        return s;
    }

    @Override // javax.mail.Address
    public boolean equals(Object a) {
        if (!(a instanceof InternetAddress)) {
            return false;
        }
        String s = ((InternetAddress) a).getAddress();
        if (s == this.address) {
            return true;
        }
        if (this.address != null && this.address.equalsIgnoreCase(s)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.address == null) {
            return 0;
        }
        return this.address.toLowerCase().hashCode();
    }

    public static String toString(Address[] addresses) {
        return toString(addresses, 0);
    }

    public static String toString(Address[] addresses, int used) {
        if (addresses == null || addresses.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < addresses.length; i++) {
            if (i != 0) {
                sb.append(", ");
                used += 2;
            }
            String s = addresses[i].toString();
            int len = lengthOfFirstSegment(s);
            if (used + len > 76) {
                sb.append("\r\n\t");
                used = 8;
            }
            sb.append(s);
            used = lengthOfLastSegment(s, used);
        }
        return sb.toString();
    }

    private static int lengthOfFirstSegment(String s) {
        int pos = s.indexOf("\r\n");
        if (pos != -1) {
            return pos;
        }
        return s.length();
    }

    private static int lengthOfLastSegment(String s, int used) {
        int pos = s.lastIndexOf("\r\n");
        if (pos != -1) {
            return (s.length() - pos) - 2;
        }
        return s.length() + used;
    }

    public static InternetAddress getLocalAddress(Session session) {
        InetAddress me;
        String user = null;
        String host = null;
        String address = null;
        try {
            if (session == null) {
                user = System.getProperty("user.name");
                host = InetAddress.getLocalHost().getHostName();
            } else {
                address = session.getProperty("mail.from");
                if (address == null) {
                    user = session.getProperty("mail.user");
                    if (user == null || user.length() == 0) {
                        user = session.getProperty("user.name");
                    }
                    if (user == null || user.length() == 0) {
                        user = System.getProperty("user.name");
                    }
                    host = session.getProperty("mail.host");
                    if ((host == null || host.length() == 0) && (me = InetAddress.getLocalHost()) != null) {
                        host = me.getHostName();
                    }
                }
            }
            if (address == null && user != null && user.length() != 0 && host != null && host.length() != 0) {
                address = new StringBuffer().append(user).append("@").append(host).toString();
            }
            if (address != null) {
                return new InternetAddress(address);
            }
            return null;
        } catch (SecurityException e) {
            return null;
        } catch (UnknownHostException e2) {
            return null;
        } catch (AddressException e3) {
            return null;
        }
    }

    public static InternetAddress[] parse(String addresslist) throws AddressException {
        return parse(addresslist, true);
    }

    public static InternetAddress[] parse(String addresslist, boolean strict) throws AddressException {
        return parse(addresslist, strict, false);
    }

    public static InternetAddress[] parseHeader(String addresslist, boolean strict) throws AddressException {
        return parse(addresslist, strict, true);
    }

    private static InternetAddress[] parse(String s, boolean strict, boolean parseHdr) throws AddressException {
        int start_personal = -1;
        int end_personal = -1;
        int length = s.length();
        boolean in_group = false;
        boolean route_addr = false;
        boolean rfc822 = false;
        Vector v = new Vector();
        int end = -1;
        int start = -1;
        int index = 0;
        while (index < length) {
            char c = s.charAt(index);
            switch (c) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    break;
                case '\"':
                    rfc822 = true;
                    if (start == -1) {
                        start = index;
                    }
                    while (true) {
                        index++;
                        if (index < length) {
                            char c2 = s.charAt(index);
                            switch (c2) {
                                case '\"':
                                    break;
                                case '\\':
                                    index++;
                            }
                        }
                    }
                    if (index < length) {
                        break;
                    } else {
                        throw new AddressException("Missing '\"'", s, index);
                    }
                    break;
                case '(':
                    rfc822 = true;
                    if (start >= 0 && end == -1) {
                        end = index;
                    }
                    if (start_personal == -1) {
                        start_personal = index + 1;
                    }
                    int index2 = index + 1;
                    int nesting = 1;
                    while (index2 < length && nesting > 0) {
                        char c3 = s.charAt(index2);
                        switch (c3) {
                            case '(':
                                nesting++;
                                break;
                            case ')':
                                nesting--;
                                break;
                            case '\\':
                                index2++;
                                break;
                        }
                        index2++;
                    }
                    if (nesting > 0) {
                        throw new AddressException("Missing ')'", s, index2);
                    }
                    index = index2 - 1;
                    if (end_personal != -1) {
                        break;
                    } else {
                        end_personal = index;
                        break;
                    }
                case ')':
                    throw new AddressException("Missing '('", s, index);
                case ',':
                    if (start == -1) {
                        route_addr = false;
                        rfc822 = false;
                        end = -1;
                        start = -1;
                        break;
                    } else if (in_group) {
                        route_addr = false;
                        break;
                    } else {
                        if (end == -1) {
                            end = index;
                        }
                        String addr = s.substring(start, end).trim();
                        if (rfc822 || strict || parseHdr) {
                            if (strict || !parseHdr) {
                                checkAddress(addr, route_addr, false);
                            }
                            InternetAddress ma = new InternetAddress();
                            ma.setAddress(addr);
                            if (start_personal >= 0) {
                                ma.encodedPersonal = unquote(s.substring(start_personal, end_personal).trim());
                                end_personal = -1;
                                start_personal = -1;
                            }
                            v.addElement(ma);
                        } else {
                            StringTokenizer st = new StringTokenizer(addr);
                            while (st.hasMoreTokens()) {
                                String a = st.nextToken();
                                checkAddress(a, false, false);
                                InternetAddress ma2 = new InternetAddress();
                                ma2.setAddress(a);
                                v.addElement(ma2);
                            }
                        }
                        route_addr = false;
                        rfc822 = false;
                        end = -1;
                        start = -1;
                        break;
                    }
                case ':':
                    rfc822 = true;
                    if (in_group) {
                        throw new AddressException("Nested group", s, index);
                    }
                    in_group = true;
                    if (start != -1) {
                        break;
                    } else {
                        start = index;
                        break;
                    }
                case ';':
                    if (start == -1) {
                        start = index;
                    }
                    if (!in_group) {
                        throw new AddressException("Illegal semicolon, not in group", s, index);
                    }
                    in_group = false;
                    if (start == -1) {
                        start = index;
                    }
                    InternetAddress ma3 = new InternetAddress();
                    int end2 = index + 1;
                    ma3.setAddress(s.substring(start, end2).trim());
                    v.addElement(ma3);
                    route_addr = false;
                    end = -1;
                    start = -1;
                    break;
                case '<':
                    rfc822 = true;
                    if (route_addr) {
                        throw new AddressException("Extra route-addr", s, index);
                    }
                    if (!in_group) {
                        start_personal = start;
                        if (start_personal >= 0) {
                            end_personal = index;
                        }
                        start = index + 1;
                    }
                    boolean inquote = false;
                    while (true) {
                        index++;
                        if (index < length) {
                            char c4 = s.charAt(index);
                            switch (c4) {
                                case '\"':
                                    inquote = !inquote;
                                case '>':
                                    if (!inquote) {
                                        break;
                                    }
                                case '\\':
                                    index++;
                            }
                        }
                    }
                    if (index >= length) {
                        if (inquote) {
                            throw new AddressException("Missing '\"'", s, index);
                        }
                        throw new AddressException("Missing '>'", s, index);
                    }
                    route_addr = true;
                    end = index;
                    break;
                    break;
                case '>':
                    throw new AddressException("Missing '<'", s, index);
                case '[':
                    rfc822 = true;
                    while (true) {
                        index++;
                        if (index < length) {
                            char c5 = s.charAt(index);
                            switch (c5) {
                                case '\\':
                                    index++;
                                case ']':
                                    break;
                            }
                        }
                    }
                    if (index < length) {
                        break;
                    } else {
                        throw new AddressException("Missing ']'", s, index);
                    }
                default:
                    if (start != -1) {
                        break;
                    } else {
                        start = index;
                        break;
                    }
            }
            index++;
        }
        if (start >= 0) {
            if (end == -1) {
                end = index;
            }
            String addr2 = s.substring(start, end).trim();
            if (rfc822 || strict || parseHdr) {
                if (strict || !parseHdr) {
                    checkAddress(addr2, route_addr, false);
                }
                InternetAddress ma4 = new InternetAddress();
                ma4.setAddress(addr2);
                if (start_personal >= 0) {
                    ma4.encodedPersonal = unquote(s.substring(start_personal, end_personal).trim());
                }
                v.addElement(ma4);
            } else {
                StringTokenizer st2 = new StringTokenizer(addr2);
                while (st2.hasMoreTokens()) {
                    String a2 = st2.nextToken();
                    checkAddress(a2, false, false);
                    InternetAddress ma5 = new InternetAddress();
                    ma5.setAddress(a2);
                    v.addElement(ma5);
                }
            }
        }
        InternetAddress[] a3 = new InternetAddress[v.size()];
        v.copyInto(a3);
        return a3;
    }

    public void validate() throws AddressException {
        checkAddress(getAddress(), true, true);
    }

    private static void checkAddress(String addr, boolean routeAddr, boolean validate) throws AddressException {
        String local;
        String domain;
        int start = 0;
        if (addr.indexOf(34) >= 0) {
            return;
        }
        if (routeAddr) {
            int i = 0;
            while (true) {
                start = i;
                int indexOfAny = indexOfAny(addr, ",:", start);
                if (indexOfAny < 0) {
                    break;
                } else if (addr.charAt(start) != '@') {
                    throw new AddressException("Illegal route-addr", addr);
                } else {
                    if (addr.charAt(indexOfAny) != ':') {
                        i = indexOfAny + 1;
                    } else {
                        start = indexOfAny + 1;
                        break;
                    }
                }
            }
        }
        int i2 = addr.indexOf(64, start);
        if (i2 >= 0) {
            if (i2 == start) {
                throw new AddressException("Missing local name", addr);
            }
            if (i2 == addr.length() - 1) {
                throw new AddressException("Missing domain", addr);
            }
            local = addr.substring(start, i2);
            domain = addr.substring(i2 + 1);
        } else if (validate) {
            throw new AddressException("Missing final '@domain'", addr);
        } else {
            local = addr;
            domain = null;
        }
        if (indexOfAny(addr, " \t\n\r") >= 0) {
            throw new AddressException("Illegal whitespace in address", addr);
        }
        if (indexOfAny(local, specialsNoDot) >= 0) {
            throw new AddressException("Illegal character in local name", addr);
        }
        if (domain != null && domain.indexOf(91) < 0 && indexOfAny(domain, specialsNoDot) >= 0) {
            throw new AddressException("Illegal character in domain", addr);
        }
    }

    private boolean isSimple() {
        return this.address == null || indexOfAny(this.address, specialsNoDotNoAt) < 0;
    }

    public boolean isGroup() {
        return this.address != null && this.address.endsWith(";") && this.address.indexOf(58) > 0;
    }

    public InternetAddress[] getGroup(boolean strict) throws AddressException {
        int ix;
        String addr = getAddress();
        if (!addr.endsWith(";") || (ix = addr.indexOf(58)) < 0) {
            return null;
        }
        String list = addr.substring(ix + 1, addr.length() - 1);
        return parseHeader(list, strict);
    }

    private static int indexOfAny(String s, String any) {
        return indexOfAny(s, any, 0);
    }

    private static int indexOfAny(String s, String any, int start) {
        try {
            int len = s.length();
            for (int i = start; i < len; i++) {
                if (any.indexOf(s.charAt(i)) >= 0) {
                    return i;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException e) {
            return -1;
        }
    }
}
