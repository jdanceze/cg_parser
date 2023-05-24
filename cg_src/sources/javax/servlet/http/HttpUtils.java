package javax.servlet.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.servlet.ServletInputStream;
import org.apache.http.HttpHost;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/http/HttpUtils.class */
public class HttpUtils {
    private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
    private static ResourceBundle lStrings = ResourceBundle.getBundle(LSTRING_FILE);

    public static Hashtable parseQueryString(String s) {
        String[] valArray;
        if (s == null) {
            throw new IllegalArgumentException();
        }
        Hashtable ht = new Hashtable();
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(s, "&");
        while (st.hasMoreTokens()) {
            String pair = st.nextToken();
            int pos = pair.indexOf(61);
            if (pos == -1) {
                throw new IllegalArgumentException();
            }
            String key = parseName(pair.substring(0, pos), sb);
            String val = parseName(pair.substring(pos + 1, pair.length()), sb);
            if (ht.containsKey(key)) {
                String[] oldVals = (String[]) ht.get(key);
                valArray = new String[oldVals.length + 1];
                for (int i = 0; i < oldVals.length; i++) {
                    valArray[i] = oldVals[i];
                }
                valArray[oldVals.length] = val;
            } else {
                valArray = new String[]{val};
            }
            ht.put(key, valArray);
        }
        return ht;
    }

    public static Hashtable parsePostData(int len, ServletInputStream in) {
        if (len <= 0) {
            return new Hashtable();
        }
        if (in == null) {
            throw new IllegalArgumentException();
        }
        byte[] postedBytes = new byte[len];
        int offset = 0;
        do {
            try {
                int inputLen = in.read(postedBytes, offset, len - offset);
                if (inputLen <= 0) {
                    String msg = lStrings.getString("err.io.short_read");
                    throw new IllegalArgumentException(msg);
                }
                offset += inputLen;
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        } while (len - offset > 0);
        try {
            String postedBody = new String(postedBytes, 0, len, "8859_1");
            return parseQueryString(postedBody);
        } catch (UnsupportedEncodingException e2) {
            throw new IllegalArgumentException(e2.getMessage());
        }
    }

    private static String parseName(String s, StringBuffer sb) {
        sb.setLength(0);
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            switch (c) {
                case '%':
                    try {
                        sb.append((char) Integer.parseInt(s.substring(i + 1, i + 3), 16));
                        i += 2;
                        break;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    } catch (StringIndexOutOfBoundsException e2) {
                        String rest = s.substring(i);
                        sb.append(rest);
                        if (rest.length() != 2) {
                            break;
                        } else {
                            i++;
                            break;
                        }
                    }
                case '+':
                    sb.append(' ');
                    break;
                default:
                    sb.append(c);
                    break;
            }
            i++;
        }
        return sb.toString();
    }

    public static StringBuffer getRequestURL(HttpServletRequest req) {
        StringBuffer url = new StringBuffer();
        String scheme = req.getScheme();
        int port = req.getServerPort();
        String urlPath = req.getRequestURI();
        url.append(scheme);
        url.append("://");
        url.append(req.getServerName());
        if ((scheme.equals(HttpHost.DEFAULT_SCHEME_NAME) && port != 80) || (scheme.equals("https") && port != 443)) {
            url.append(':');
            url.append(req.getServerPort());
        }
        url.append(urlPath);
        return url;
    }
}
