package org.json;

import android.content.ContentResolver;
import java.util.Iterator;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/XML.class */
public class XML {
    public static final Character AMP = new Character('&');
    public static final Character APOS = new Character('\'');
    public static final Character BANG = new Character('!');
    public static final Character EQ = new Character('=');
    public static final Character GT = new Character('>');
    public static final Character LT = new Character('<');
    public static final Character QUEST = new Character('?');
    public static final Character QUOT = new Character('\"');
    public static final Character SLASH = new Character('/');

    public static String escape(String string) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    private static boolean parse(XMLTokener x, JSONObject context, String name) throws JSONException {
        Object t = x.nextToken();
        if (t == BANG) {
            char c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                if (x.nextToken().equals("CDATA") && x.next() == '[') {
                    String s = x.nextCDATA();
                    if (s.length() > 0) {
                        context.accumulate(ContentResolver.SCHEME_CONTENT, s);
                        return false;
                    }
                    return false;
                }
                throw x.syntaxError("Expected 'CDATA['");
            }
            int i = 1;
            do {
                Object t2 = x.nextMeta();
                if (t2 == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                }
                if (t2 == LT) {
                    i++;
                } else if (t2 == GT) {
                    i--;
                }
            } while (i > 0);
            return false;
        } else if (t == QUEST) {
            x.skipPast("?>");
            return false;
        } else if (t == SLASH) {
            Object t3 = x.nextToken();
            if (name == null) {
                throw x.syntaxError(new StringBuffer().append("Mismatched close tag").append(t3).toString());
            }
            if (!t3.equals(name)) {
                throw x.syntaxError(new StringBuffer().append("Mismatched ").append(name).append(" and ").append(t3).toString());
            }
            if (x.nextToken() != GT) {
                throw x.syntaxError("Misshaped close tag");
            }
            return true;
        } else if (t instanceof Character) {
            throw x.syntaxError("Misshaped tag");
        } else {
            String n = (String) t;
            Object t4 = null;
            JSONObject o = new JSONObject();
            while (true) {
                Object obj = t4;
                Object t5 = t4;
                if (obj == null) {
                    t5 = x.nextToken();
                }
                if (t5 instanceof String) {
                    String s2 = (String) t5;
                    t4 = x.nextToken();
                    if (t4 == EQ) {
                        Object t6 = x.nextToken();
                        if (!(t6 instanceof String)) {
                            throw x.syntaxError("Missing value");
                        }
                        o.accumulate(s2, t6);
                        t4 = null;
                    } else {
                        o.accumulate(s2, "");
                    }
                } else if (t5 == SLASH) {
                    if (x.nextToken() != GT) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    context.accumulate(n, o);
                    return false;
                } else if (t5 != GT) {
                    throw x.syntaxError("Misshaped tag");
                } else {
                    while (true) {
                        Object t7 = x.nextContent();
                        if (t7 == null) {
                            if (n != null) {
                                throw x.syntaxError(new StringBuffer().append("Unclosed tag ").append(n).toString());
                            }
                            return false;
                        } else if (t7 instanceof String) {
                            String s3 = (String) t7;
                            if (s3.length() > 0) {
                                o.accumulate(ContentResolver.SCHEME_CONTENT, s3);
                            }
                        } else if (t7 == LT && parse(x, o, n)) {
                            if (o.length() == 0) {
                                context.accumulate(n, "");
                                return false;
                            } else if (o.length() == 1 && o.opt(ContentResolver.SCHEME_CONTENT) != null) {
                                context.accumulate(n, o.opt(ContentResolver.SCHEME_CONTENT));
                                return false;
                            } else {
                                context.accumulate(n, o);
                                return false;
                            }
                        }
                    }
                }
            }
        }
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject o = new JSONObject();
        XMLTokener x = new XMLTokener(string);
        while (x.more() && x.skipPast("<")) {
            parse(x, o, null);
        }
        return o;
    }

    public static String toString(Object o) throws JSONException {
        return toString(o, null);
    }

    public static String toString(Object o, String tagName) throws JSONException {
        StringBuffer b = new StringBuffer();
        if (o instanceof JSONObject) {
            if (tagName != null) {
                b.append('<');
                b.append(tagName);
                b.append('>');
            }
            JSONObject jo = (JSONObject) o;
            Iterator keys = jo.keys();
            while (keys.hasNext()) {
                String k = keys.next().toString();
                Object v = jo.get(k);
                if (v instanceof String) {
                    String str = (String) v;
                }
                if (k.equals(ContentResolver.SCHEME_CONTENT)) {
                    if (v instanceof JSONArray) {
                        JSONArray ja = (JSONArray) v;
                        int len = ja.length();
                        for (int i = 0; i < len; i++) {
                            if (i > 0) {
                                b.append('\n');
                            }
                            b.append(escape(ja.get(i).toString()));
                        }
                    } else {
                        b.append(escape(v.toString()));
                    }
                } else if (v instanceof JSONArray) {
                    JSONArray ja2 = (JSONArray) v;
                    int len2 = ja2.length();
                    for (int i2 = 0; i2 < len2; i2++) {
                        b.append(toString(ja2.get(i2), k));
                    }
                } else if (v.equals("")) {
                    b.append('<');
                    b.append(k);
                    b.append("/>");
                } else {
                    b.append(toString(v, k));
                }
            }
            if (tagName != null) {
                b.append("</");
                b.append(tagName);
                b.append('>');
            }
            return b.toString();
        } else if (o instanceof JSONArray) {
            JSONArray ja3 = (JSONArray) o;
            int len3 = ja3.length();
            for (int i3 = 0; i3 < len3; i3++) {
                b.append(toString(ja3.opt(i3), tagName == null ? "array" : tagName));
            }
            return b.toString();
        } else {
            String s = o == null ? Jimple.NULL : escape(o.toString());
            return tagName == null ? new StringBuffer().append("\"").append(s).append("\"").toString() : s.length() == 0 ? new StringBuffer().append("<").append(tagName).append("/>").toString() : new StringBuffer().append("<").append(tagName).append(">").append(s).append("</").append(tagName).append(">").toString();
        }
    }
}
