package org.json;

import org.apache.http.cookie.ClientCookie;
/* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/Cookie.class */
public class Cookie {
    public static String escape(String string) {
        String s = string.trim();
        StringBuffer sb = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c < ' ' || c == '+' || c == '%' || c == '=' || c == ';') {
                sb.append('%');
                sb.append(Character.forDigit((char) ((c >>> 4) & 15), 16));
                sb.append(Character.forDigit((char) (c & 15), 16));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        Object v;
        JSONObject o = new JSONObject();
        JSONTokener x = new JSONTokener(string);
        o.put("name", x.nextTo('='));
        x.next('=');
        o.put("value", x.nextTo(';'));
        x.next();
        while (x.more()) {
            String n = unescape(x.nextTo("=;"));
            if (x.next() != '=') {
                if (n.equals(ClientCookie.SECURE_ATTR)) {
                    v = Boolean.TRUE;
                } else {
                    throw x.syntaxError("Missing '=' in cookie parameter.");
                }
            } else {
                v = unescape(x.nextTo(';'));
                x.next();
            }
            o.put(n, v);
        }
        return o;
    }

    public static String toString(JSONObject o) throws JSONException {
        StringBuffer sb = new StringBuffer();
        sb.append(escape(o.getString("name")));
        sb.append("=");
        sb.append(escape(o.getString("value")));
        if (o.has(ClientCookie.EXPIRES_ATTR)) {
            sb.append(";expires=");
            sb.append(o.getString(ClientCookie.EXPIRES_ATTR));
        }
        if (o.has("domain")) {
            sb.append(";domain=");
            sb.append(escape(o.getString("domain")));
        }
        if (o.has(ClientCookie.PATH_ATTR)) {
            sb.append(";path=");
            sb.append(escape(o.getString(ClientCookie.PATH_ATTR)));
        }
        if (o.optBoolean(ClientCookie.SECURE_ATTR)) {
            sb.append(";secure");
        }
        return sb.toString();
    }

    public static String unescape(String s) {
        int len = s.length();
        StringBuffer b = new StringBuffer();
        int i = 0;
        while (i < len) {
            char c = s.charAt(i);
            if (c == '+') {
                c = ' ';
            } else if (c == '%' && i + 2 < len) {
                int d = JSONTokener.dehexchar(s.charAt(i + 1));
                int e = JSONTokener.dehexchar(s.charAt(i + 2));
                if (d >= 0 && e >= 0) {
                    c = (char) ((d * 16) + e);
                    i += 2;
                }
            }
            b.append(c);
            i++;
        }
        return b.toString();
    }
}
