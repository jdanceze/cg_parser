package javax.xml.transform.stream;

import java.io.File;
import java.io.UnsupportedEncodingException;
import org.apache.tools.ant.taskdefs.XSLTLiaison;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/stream/FilePathToURI.class */
class FilePathToURI {
    private static boolean[] gNeedEscaping = new boolean[128];
    private static char[] gAfterEscaping1 = new char[128];
    private static char[] gAfterEscaping2 = new char[128];
    private static char[] gHexChs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    FilePathToURI() {
    }

    public static String filepath2URI(String str) {
        char charAt;
        char upperCase;
        if (str == null) {
            return null;
        }
        String replace = str.replace(File.separatorChar, '/');
        int length = replace.length();
        StringBuffer stringBuffer = new StringBuffer(length * 3);
        stringBuffer.append(XSLTLiaison.FILE_PROTOCOL_PREFIX);
        if (length >= 2 && replace.charAt(1) == ':' && (upperCase = Character.toUpperCase(replace.charAt(0))) >= 'A' && upperCase <= 'Z') {
            stringBuffer.append('/');
        }
        int i = 0;
        while (i < length && (charAt = replace.charAt(i)) < 128) {
            if (gNeedEscaping[charAt]) {
                stringBuffer.append('%');
                stringBuffer.append(gAfterEscaping1[charAt]);
                stringBuffer.append(gAfterEscaping2[charAt]);
            } else {
                stringBuffer.append(charAt);
            }
            i++;
        }
        if (i < length) {
            try {
                byte[] bytes = replace.substring(i).getBytes("UTF-8");
                for (byte b : bytes) {
                    if (b < 0) {
                        int i2 = b + 256;
                        stringBuffer.append('%');
                        stringBuffer.append(gHexChs[i2 >> 4]);
                        stringBuffer.append(gHexChs[i2 & 15]);
                    } else if (gNeedEscaping[b]) {
                        stringBuffer.append('%');
                        stringBuffer.append(gAfterEscaping1[b]);
                        stringBuffer.append(gAfterEscaping2[b]);
                    } else {
                        stringBuffer.append((char) b);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                return replace;
            }
        }
        return stringBuffer.toString();
    }

    static {
        char[] cArr;
        for (int i = 0; i <= 31; i++) {
            gNeedEscaping[i] = true;
            gAfterEscaping1[i] = gHexChs[i >> 4];
            gAfterEscaping2[i] = gHexChs[i & 15];
        }
        gNeedEscaping[127] = true;
        gAfterEscaping1[127] = '7';
        gAfterEscaping2[127] = 'F';
        for (char c : new char[]{' ', '<', '>', '#', '%', '\"', '{', '}', '|', '\\', '^', '~', '[', ']', '`'}) {
            gNeedEscaping[c] = true;
            gAfterEscaping1[c] = gHexChs[c >> 4];
            gAfterEscaping2[c] = gHexChs[c & 15];
        }
    }
}
