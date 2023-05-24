package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.BEncoderStream;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.QDecoderStream;
import com.sun.mail.util.QEncoderStream;
import com.sun.mail.util.QPDecoderStream;
import com.sun.mail.util.QPEncoderStream;
import com.sun.mail.util.UUDecoderStream;
import com.sun.mail.util.UUEncoderStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import net.bytebuddy.description.type.TypeDescription;
import org.apache.commons.cli.HelpFormatter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MimeUtility.class */
public class MimeUtility {
    public static final int ALL = -1;
    private static boolean decodeStrict;
    private static boolean encodeEolStrict;
    private static boolean foldEncodedWords;
    private static boolean foldText;
    private static String defaultJavaCharset;
    private static String defaultMIMECharset;
    private static Hashtable mime2java;
    private static Hashtable java2mime;
    static final int ALL_ASCII = 1;
    static final int MOSTLY_ASCII = 2;
    static final int MOSTLY_NONASCII = 3;
    static Class class$javax$mail$internet$MimeUtility;

    private MimeUtility() {
    }

    static {
        Class cls;
        decodeStrict = true;
        encodeEolStrict = false;
        foldEncodedWords = false;
        foldText = true;
        try {
            String s = System.getProperty("mail.mime.decodetext.strict");
            decodeStrict = s == null || !s.equalsIgnoreCase("false");
            String s2 = System.getProperty("mail.mime.encodeeol.strict");
            encodeEolStrict = s2 != null && s2.equalsIgnoreCase("true");
            String s3 = System.getProperty("mail.mime.foldencodedwords");
            foldEncodedWords = s3 != null && s3.equalsIgnoreCase("true");
            String s4 = System.getProperty("mail.mime.foldtext");
            foldText = s4 == null || !s4.equalsIgnoreCase("false");
        } catch (SecurityException e) {
        }
        java2mime = new Hashtable(40);
        mime2java = new Hashtable(10);
        try {
            if (class$javax$mail$internet$MimeUtility == null) {
                cls = class$("javax.mail.internet.MimeUtility");
                class$javax$mail$internet$MimeUtility = cls;
            } else {
                cls = class$javax$mail$internet$MimeUtility;
            }
            InputStream is = cls.getResourceAsStream("/META-INF/javamail.charset.map");
            if (is != null) {
                LineInputStream lineInputStream = new LineInputStream(is);
                loadMappings(lineInputStream, java2mime);
                loadMappings(lineInputStream, mime2java);
            }
        } catch (Exception e2) {
        }
        if (java2mime.isEmpty()) {
            java2mime.put("8859_1", "ISO-8859-1");
            java2mime.put("iso8859_1", "ISO-8859-1");
            java2mime.put("ISO8859-1", "ISO-8859-1");
            java2mime.put("8859_2", "ISO-8859-2");
            java2mime.put("iso8859_2", "ISO-8859-2");
            java2mime.put("ISO8859-2", "ISO-8859-2");
            java2mime.put("8859_3", "ISO-8859-3");
            java2mime.put("iso8859_3", "ISO-8859-3");
            java2mime.put("ISO8859-3", "ISO-8859-3");
            java2mime.put("8859_4", "ISO-8859-4");
            java2mime.put("iso8859_4", "ISO-8859-4");
            java2mime.put("ISO8859-4", "ISO-8859-4");
            java2mime.put("8859_5", "ISO-8859-5");
            java2mime.put("iso8859_5", "ISO-8859-5");
            java2mime.put("ISO8859-5", "ISO-8859-5");
            java2mime.put("8859_6", "ISO-8859-6");
            java2mime.put("iso8859_6", "ISO-8859-6");
            java2mime.put("ISO8859-6", "ISO-8859-6");
            java2mime.put("8859_7", "ISO-8859-7");
            java2mime.put("iso8859_7", "ISO-8859-7");
            java2mime.put("ISO8859-7", "ISO-8859-7");
            java2mime.put("8859_8", "ISO-8859-8");
            java2mime.put("iso8859_8", "ISO-8859-8");
            java2mime.put("ISO8859-8", "ISO-8859-8");
            java2mime.put("8859_9", "ISO-8859-9");
            java2mime.put("iso8859_9", "ISO-8859-9");
            java2mime.put("ISO8859-9", "ISO-8859-9");
            java2mime.put("SJIS", "Shift_JIS");
            java2mime.put("MS932", "Shift_JIS");
            java2mime.put("JIS", "ISO-2022-JP");
            java2mime.put("ISO2022JP", "ISO-2022-JP");
            java2mime.put("EUC_JP", "euc-jp");
            java2mime.put("KOI8_R", "koi8-r");
            java2mime.put("EUC_CN", "euc-cn");
            java2mime.put("EUC_TW", "euc-tw");
            java2mime.put("EUC_KR", "euc-kr");
        }
        if (mime2java.isEmpty()) {
            mime2java.put("iso-2022-cn", "ISO2022CN");
            mime2java.put("iso-2022-kr", "ISO2022KR");
            mime2java.put("utf-8", "UTF8");
            mime2java.put("utf8", "UTF8");
            mime2java.put("ja_jp.iso2022-7", "ISO2022JP");
            mime2java.put("ja_jp.eucjp", "EUCJIS");
            mime2java.put("euc-kr", "KSC5601");
            mime2java.put("euckr", "KSC5601");
            mime2java.put("us-ascii", "ISO-8859-1");
            mime2java.put("x-us-ascii", "ISO-8859-1");
        }
    }

    public static String getEncoding(DataSource ds) {
        String encoding;
        try {
            ContentType cType = new ContentType(ds.getContentType());
            InputStream is = ds.getInputStream();
            boolean isText = cType.match("text/*");
            int i = checkAscii(is, -1, !isText);
            switch (i) {
                case 1:
                    encoding = "7bit";
                    break;
                case 2:
                    encoding = "quoted-printable";
                    break;
                default:
                    encoding = "base64";
                    break;
            }
            try {
                is.close();
            } catch (IOException e) {
            }
            return encoding;
        } catch (Exception e2) {
            return "base64";
        }
    }

    public static String getEncoding(DataHandler dh) {
        String encoding;
        if (dh.getName() != null) {
            return getEncoding(dh.getDataSource());
        }
        try {
            ContentType cType = new ContentType(dh.getContentType());
            if (cType.match("text/*")) {
                AsciiOutputStream aos = new AsciiOutputStream(false, false);
                try {
                    dh.writeTo(aos);
                } catch (IOException e) {
                }
                switch (aos.getAscii()) {
                    case 1:
                        encoding = "7bit";
                        break;
                    case 2:
                        encoding = "quoted-printable";
                        break;
                    default:
                        encoding = "base64";
                        break;
                }
            } else {
                AsciiOutputStream aos2 = new AsciiOutputStream(true, encodeEolStrict);
                try {
                    dh.writeTo(aos2);
                } catch (IOException e2) {
                }
                if (aos2.getAscii() == 1) {
                    encoding = "7bit";
                } else {
                    encoding = "base64";
                }
            }
            return encoding;
        } catch (Exception e3) {
            return "base64";
        }
    }

    public static InputStream decode(InputStream is, String encoding) throws MessagingException {
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64DecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return is;
        }
        throw new MessagingException(new StringBuffer().append("Unknown encoding: ").append(encoding).toString());
    }

    public static OutputStream encode(OutputStream os, String encoding) throws MessagingException {
        if (encoding == null) {
            return os;
        }
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64EncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return os;
        }
        throw new MessagingException(new StringBuffer().append("Unknown encoding: ").append(encoding).toString());
    }

    public static OutputStream encode(OutputStream os, String encoding, String filename) throws MessagingException {
        if (encoding == null) {
            return os;
        }
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64EncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPEncoderStream(os);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUEncoderStream(os, filename);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return os;
        }
        throw new MessagingException(new StringBuffer().append("Unknown encoding: ").append(encoding).toString());
    }

    public static String encodeText(String text) throws UnsupportedEncodingException {
        return encodeText(text, null, null);
    }

    public static String encodeText(String text, String charset, String encoding) throws UnsupportedEncodingException {
        return encodeWord(text, charset, encoding, false);
    }

    public static String decodeText(String etext) throws UnsupportedEncodingException {
        String word;
        if (etext.indexOf("=?") == -1) {
            return etext;
        }
        StringTokenizer st = new StringTokenizer(etext, " \t\n\r", true);
        StringBuffer sb = new StringBuffer();
        StringBuffer wsb = new StringBuffer();
        boolean prevWasEncoded = false;
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            char c = s.charAt(0);
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                wsb.append(c);
            } else {
                try {
                    word = decodeWord(s);
                    if (!prevWasEncoded && wsb.length() > 0) {
                        sb.append(wsb);
                    }
                    prevWasEncoded = true;
                } catch (ParseException e) {
                    word = s;
                    if (!decodeStrict) {
                        word = decodeInnerWords(word);
                    }
                    if (wsb.length() > 0) {
                        sb.append(wsb);
                    }
                    prevWasEncoded = false;
                }
                sb.append(word);
                wsb.setLength(0);
            }
        }
        return sb.toString();
    }

    public static String encodeWord(String word) throws UnsupportedEncodingException {
        return encodeWord(word, null, null);
    }

    public static String encodeWord(String word, String charset, String encoding) throws UnsupportedEncodingException {
        return encodeWord(word, charset, encoding, true);
    }

    private static String encodeWord(String string, String charset, String encoding, boolean encodingWord) throws UnsupportedEncodingException {
        String jcharset;
        boolean b64;
        int ascii = checkAscii(string);
        if (ascii == 1) {
            return string;
        }
        if (charset == null) {
            jcharset = getDefaultJavaCharset();
            charset = getDefaultMIMECharset();
        } else {
            jcharset = javaCharset(charset);
        }
        if (encoding == null) {
            if (ascii != 3) {
                encoding = "Q";
            } else {
                encoding = "B";
            }
        }
        if (encoding.equalsIgnoreCase("B")) {
            b64 = true;
        } else if (encoding.equalsIgnoreCase("Q")) {
            b64 = false;
        } else {
            throw new UnsupportedEncodingException(new StringBuffer().append("Unknown transfer encoding: ").append(encoding).toString());
        }
        StringBuffer outb = new StringBuffer();
        doEncode(string, b64, jcharset, 68 - charset.length(), new StringBuffer().append("=?").append(charset).append(TypeDescription.Generic.OfWildcardType.SYMBOL).append(encoding).append(TypeDescription.Generic.OfWildcardType.SYMBOL).toString(), true, encodingWord, outb);
        return outb.toString();
    }

    private static void doEncode(String string, boolean b64, String jcharset, int avail, String prefix, boolean first, boolean encodingWord, StringBuffer buf) throws UnsupportedEncodingException {
        int len;
        QEncoderStream qEncoderStream;
        int size;
        byte[] bytes = string.getBytes(jcharset);
        if (b64) {
            len = BEncoderStream.encodedLength(bytes);
        } else {
            len = QEncoderStream.encodedLength(bytes, encodingWord);
        }
        if (len > avail && (size = string.length()) > 1) {
            doEncode(string.substring(0, size / 2), b64, jcharset, avail, prefix, first, encodingWord, buf);
            doEncode(string.substring(size / 2, size), b64, jcharset, avail, prefix, false, encodingWord, buf);
            return;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (b64) {
            qEncoderStream = new BEncoderStream(os);
        } else {
            qEncoderStream = new QEncoderStream(os, encodingWord);
        }
        try {
            qEncoderStream.write(bytes);
            qEncoderStream.close();
        } catch (IOException e) {
        }
        byte[] encodedBytes = os.toByteArray();
        if (!first) {
            if (foldEncodedWords) {
                buf.append("\r\n ");
            } else {
                buf.append(Instruction.argsep);
            }
        }
        buf.append(prefix);
        for (byte b : encodedBytes) {
            buf.append((char) b);
        }
        buf.append("?=");
    }

    public static String decodeWord(String eword) throws ParseException, UnsupportedEncodingException {
        QDecoderStream qDecoderStream;
        if (!eword.startsWith("=?")) {
            throw new ParseException();
        }
        int pos = eword.indexOf(63, 2);
        if (pos == -1) {
            throw new ParseException();
        }
        String charset = javaCharset(eword.substring(2, pos));
        int start = pos + 1;
        int pos2 = eword.indexOf(63, start);
        if (pos2 == -1) {
            throw new ParseException();
        }
        String encoding = eword.substring(start, pos2);
        int start2 = pos2 + 1;
        int pos3 = eword.indexOf("?=", start2);
        if (pos3 == -1) {
            throw new ParseException();
        }
        String word = eword.substring(start2, pos3);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(ASCIIUtility.getBytes(word));
            if (encoding.equalsIgnoreCase("B")) {
                qDecoderStream = new BASE64DecoderStream(bis);
            } else if (encoding.equalsIgnoreCase("Q")) {
                qDecoderStream = new QDecoderStream(bis);
            } else {
                throw new UnsupportedEncodingException(new StringBuffer().append("unknown encoding: ").append(encoding).toString());
            }
            int count = bis.available();
            byte[] bytes = new byte[count];
            String s = new String(bytes, 0, qDecoderStream.read(bytes, 0, count), charset);
            if (pos3 + 2 < eword.length()) {
                String rest = eword.substring(pos3 + 2);
                if (!decodeStrict) {
                    rest = decodeInnerWords(rest);
                }
                s = new StringBuffer().append(s).append(rest).toString();
            }
            return s;
        } catch (UnsupportedEncodingException uex) {
            throw uex;
        } catch (IOException e) {
            throw new ParseException();
        } catch (IllegalArgumentException e2) {
            throw new UnsupportedEncodingException();
        }
    }

    private static String decodeInnerWords(String word) throws UnsupportedEncodingException {
        int start = 0;
        StringBuffer buf = new StringBuffer();
        while (true) {
            int indexOf = word.indexOf("=?", start);
            if (indexOf < 0) {
                break;
            }
            buf.append(word.substring(start, indexOf));
            int end = word.indexOf("?=", indexOf);
            if (end < 0) {
                break;
            }
            String s = word.substring(indexOf, end + 2);
            try {
                s = decodeWord(s);
            } catch (ParseException e) {
            }
            buf.append(s);
            start = end + 2;
        }
        if (start == 0) {
            return word;
        }
        if (start < word.length()) {
            buf.append(word.substring(start));
        }
        return buf.toString();
    }

    public static String quote(String word, String specials) {
        int len = word.length();
        boolean needQuoting = false;
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            if (c == '\"' || c == '\\' || c == '\r' || c == '\n') {
                StringBuffer sb = new StringBuffer(len + 3);
                sb.append('\"');
                sb.append(word.substring(0, i));
                int lastc = 0;
                for (int j = i; j < len; j++) {
                    char cc = word.charAt(j);
                    if ((cc == '\"' || cc == '\\' || cc == '\r' || cc == '\n') && (cc != '\n' || lastc != 13)) {
                        sb.append('\\');
                    }
                    sb.append(cc);
                    lastc = cc;
                }
                sb.append('\"');
                return sb.toString();
            }
            if (c < ' ' || c >= 127 || specials.indexOf(c) >= 0) {
                needQuoting = true;
            }
        }
        if (needQuoting) {
            StringBuffer sb2 = new StringBuffer(len + 2);
            sb2.append('\"').append(word).append('\"');
            return sb2.toString();
        }
        return word;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String fold(int used, String s) {
        char c;
        if (!foldText) {
            return s;
        }
        int end = s.length() - 1;
        while (end >= 0 && ((c = s.charAt(end)) == ' ' || c == '\t')) {
            end--;
        }
        if (end != s.length() - 1) {
            s = s.substring(0, end + 1);
        }
        if (used + s.length() <= 76) {
            return s;
        }
        StringBuffer sb = new StringBuffer(s.length() + 4);
        char lastc = 0;
        while (true) {
            if (used + s.length() <= 76) {
                break;
            }
            int lastspace = -1;
            for (int i = 0; i < s.length() && (lastspace == -1 || used + i <= 76); i++) {
                char c2 = s.charAt(i);
                if ((c2 == ' ' || c2 == '\t') && lastc != ' ' && lastc != '\t') {
                    lastspace = i;
                }
                lastc = c2;
            }
            if (lastspace == -1) {
                sb.append(s);
                s = "";
                break;
            }
            sb.append(s.substring(0, lastspace));
            sb.append("\r\n");
            lastc = s.charAt(lastspace);
            sb.append(lastc);
            s = s.substring(lastspace + 1);
            used = 1;
        }
        sb.append(s);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String unfold(String s) {
        char c;
        char c2;
        if (!foldText) {
            return s;
        }
        StringBuffer sb = null;
        while (true) {
            int start = indexOfAny(s, "\r\n");
            if (start < 0) {
                break;
            }
            int l = s.length();
            int i = start + 1;
            if (i < l && s.charAt(i - 1) == '\r' && s.charAt(i) == '\n') {
                i++;
            }
            if (start == 0 || s.charAt(start - 1) != '\\') {
                if (i < l && ((c = s.charAt(i)) == ' ' || c == '\t')) {
                    while (true) {
                        i++;
                        if (i >= l || ((c2 = s.charAt(i)) != ' ' && c2 != '\t')) {
                            break;
                        }
                    }
                    if (sb == null) {
                        sb = new StringBuffer(s.length());
                    }
                    if (start != 0) {
                        sb.append(s.substring(0, start));
                        sb.append(' ');
                    }
                    s = s.substring(i);
                } else {
                    if (sb == null) {
                        sb = new StringBuffer(s.length());
                    }
                    sb.append(s.substring(0, i));
                    s = s.substring(i);
                }
            } else {
                if (sb == null) {
                    sb = new StringBuffer(s.length());
                }
                sb.append(s.substring(0, start - 1));
                sb.append(s.substring(start, i));
                s = s.substring(i);
            }
        }
        if (sb != null) {
            sb.append(s);
            return sb.toString();
        }
        return s;
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

    public static String javaCharset(String charset) {
        if (mime2java == null || charset == null) {
            return charset;
        }
        String alias = (String) mime2java.get(charset.toLowerCase());
        return alias == null ? charset : alias;
    }

    public static String mimeCharset(String charset) {
        if (java2mime == null || charset == null) {
            return charset;
        }
        String alias = (String) java2mime.get(charset.toLowerCase());
        return alias == null ? charset : alias;
    }

    public static String getDefaultJavaCharset() {
        if (defaultJavaCharset == null) {
            String mimecs = null;
            try {
                mimecs = System.getProperty("mail.mime.charset");
            } catch (SecurityException e) {
            }
            if (mimecs != null && mimecs.length() > 0) {
                defaultJavaCharset = javaCharset(mimecs);
                return defaultJavaCharset;
            }
            try {
                defaultJavaCharset = System.getProperty("file.encoding", "8859_1");
            } catch (SecurityException e2) {
                InputStreamReader reader = new InputStreamReader(new InputStream() { // from class: javax.mail.internet.MimeUtility$1$NullInputStream
                    @Override // java.io.InputStream
                    public int read() {
                        return 0;
                    }
                });
                defaultJavaCharset = reader.getEncoding();
                if (defaultJavaCharset == null) {
                    defaultJavaCharset = "8859_1";
                }
            }
        }
        return defaultJavaCharset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getDefaultMIMECharset() {
        if (defaultMIMECharset == null) {
            try {
                defaultMIMECharset = System.getProperty("mail.mime.charset");
            } catch (SecurityException e) {
            }
        }
        if (defaultMIMECharset == null) {
            defaultMIMECharset = mimeCharset(getDefaultJavaCharset());
        }
        return defaultMIMECharset;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private static void loadMappings(LineInputStream is, Hashtable table) {
        while (true) {
            try {
                String currLine = is.readLine();
                if (currLine != null) {
                    if (!currLine.startsWith(HelpFormatter.DEFAULT_LONG_OPT_PREFIX) || !currLine.endsWith(HelpFormatter.DEFAULT_LONG_OPT_PREFIX)) {
                        if (currLine.trim().length() != 0 && !currLine.startsWith("#")) {
                            StringTokenizer tk = new StringTokenizer(currLine, " \t");
                            try {
                                String key = tk.nextToken();
                                String value = tk.nextToken();
                                table.put(key.toLowerCase(), value);
                            } catch (NoSuchElementException e) {
                            }
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } catch (IOException e2) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int checkAscii(String s) {
        int ascii = 0;
        int non_ascii = 0;
        int l = s.length();
        for (int i = 0; i < l; i++) {
            if (nonascii(s.charAt(i))) {
                non_ascii++;
            } else {
                ascii++;
            }
        }
        if (non_ascii == 0) {
            return 1;
        }
        if (ascii > non_ascii) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(byte[] b) {
        int ascii = 0;
        int non_ascii = 0;
        for (byte b2 : b) {
            if (nonascii(b2 & 255)) {
                non_ascii++;
            } else {
                ascii++;
            }
        }
        if (non_ascii == 0) {
            return 1;
        }
        if (ascii > non_ascii) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(InputStream is, int max, boolean breakOnNonAscii) {
        int ascii = 0;
        int non_ascii = 0;
        int block = 4096;
        int linelen = 0;
        boolean longLine = false;
        boolean badEOL = false;
        boolean checkEOL = encodeEolStrict && breakOnNonAscii;
        byte[] buf = null;
        if (max != 0) {
            block = max == -1 ? 4096 : Math.min(max, 4096);
            buf = new byte[block];
        }
        while (max != 0) {
            try {
                int len = is.read(buf, 0, block);
                if (len == -1) {
                    break;
                }
                int lastb = 0;
                for (int i = 0; i < len; i++) {
                    int b = buf[i] & 255;
                    if (checkEOL && ((lastb == 13 && b != 10) || (lastb != 13 && b == 10))) {
                        badEOL = true;
                    }
                    if (b == 13 || b == 10) {
                        linelen = 0;
                    } else {
                        linelen++;
                        if (linelen > 998) {
                            longLine = true;
                        }
                    }
                    if (nonascii(b)) {
                        if (breakOnNonAscii) {
                            return 3;
                        }
                        non_ascii++;
                    } else {
                        ascii++;
                    }
                    lastb = b;
                }
                if (max != -1) {
                    max -= len;
                }
            } catch (IOException e) {
            }
        }
        if (max == 0 && breakOnNonAscii) {
            return 3;
        }
        if (non_ascii == 0) {
            if (badEOL) {
                return 3;
            }
            if (longLine) {
                return 2;
            }
            return 1;
        } else if (ascii > non_ascii) {
            return 2;
        } else {
            return 3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final boolean nonascii(int b) {
        return b >= 127 || !(b >= 32 || b == 13 || b == 10 || b == 9);
    }
}
