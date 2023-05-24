package javax.mail.internet;

import javax.mail.Session;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/UniqueValue.class */
class UniqueValue {
    private static int part = 0;

    UniqueValue() {
    }

    public static String getUniqueBoundaryValue() {
        StringBuffer s = new StringBuffer();
        StringBuffer append = s.append("----=_Part_");
        int i = part;
        part = i + 1;
        append.append(i).append("_").append(s.hashCode()).append('.').append(System.currentTimeMillis());
        return s.toString();
    }

    public static String getUniqueMessageIDValue(Session ssn) {
        String suffix;
        InternetAddress addr = InternetAddress.getLocalAddress(ssn);
        if (addr != null) {
            suffix = addr.getAddress();
        } else {
            suffix = "javamailuser@localhost";
        }
        StringBuffer s = new StringBuffer();
        s.append(s.hashCode()).append('.').append(System.currentTimeMillis()).append('.').append("JavaMail.").append(suffix);
        return s.toString();
    }
}
