package javax.activation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/MimeTypeParameterList.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/MimeTypeParameterList.class */
public class MimeTypeParameterList {
    private Hashtable parameters = new Hashtable();
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";

    public MimeTypeParameterList() {
    }

    public MimeTypeParameterList(String parameterList) throws MimeTypeParseException {
        parse(parameterList);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x007c, code lost:
        throw new javax.activation.MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0172, code lost:
        if (r8 >= r0) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x017e, code lost:
        throw new javax.activation.MimeTypeParseException("More characters encountered in input than expected.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x017f, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void parse(java.lang.String r6) throws javax.activation.MimeTypeParseException {
        /*
            Method dump skipped, instructions count: 384
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.activation.MimeTypeParameterList.parse(java.lang.String):void");
    }

    public int size() {
        return this.parameters.size();
    }

    public boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public String get(String name) {
        return (String) this.parameters.get(name.trim().toLowerCase(Locale.ENGLISH));
    }

    public void set(String name, String value) {
        this.parameters.put(name.trim().toLowerCase(Locale.ENGLISH), value);
    }

    public void remove(String name) {
        this.parameters.remove(name.trim().toLowerCase(Locale.ENGLISH));
    }

    public Enumeration getNames() {
        return this.parameters.keys();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(this.parameters.size() * 16);
        Enumeration keys = this.parameters.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            buffer.append("; ");
            buffer.append(key);
            buffer.append('=');
            buffer.append(quote((String) this.parameters.get(key)));
        }
        return buffer.toString();
    }

    private static boolean isTokenChar(char c) {
        return c > ' ' && c < 127 && "()<>@,;:/[]?=\\\"".indexOf(c) < 0;
    }

    private static int skipWhiteSpace(String rawdata, int i) {
        int length = rawdata.length();
        while (i < length && Character.isWhitespace(rawdata.charAt(i))) {
            i++;
        }
        return i;
    }

    private static String quote(String value) {
        boolean needsQuotes = false;
        int length = value.length();
        for (int i = 0; i < length && !needsQuotes; i++) {
            needsQuotes = !isTokenChar(value.charAt(i));
        }
        if (needsQuotes) {
            StringBuffer buffer = new StringBuffer();
            buffer.ensureCapacity((int) (length * 1.5d));
            buffer.append('\"');
            for (int i2 = 0; i2 < length; i2++) {
                char c = value.charAt(i2);
                if (c == '\\' || c == '\"') {
                    buffer.append('\\');
                }
                buffer.append(c);
            }
            buffer.append('\"');
            return buffer.toString();
        }
        return value;
    }

    private static String unquote(String value) {
        int valueLength = value.length();
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(valueLength);
        boolean escaped = false;
        for (int i = 0; i < valueLength; i++) {
            char currentChar = value.charAt(i);
            if (!escaped && currentChar != '\\') {
                buffer.append(currentChar);
            } else if (escaped) {
                buffer.append(currentChar);
                escaped = false;
            } else {
                escaped = true;
            }
        }
        return buffer.toString();
    }
}
