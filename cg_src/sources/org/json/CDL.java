package org.json;
/* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/CDL.class */
public class CDL {
    private static String getValue(JSONTokener x) throws JSONException {
        char c;
        do {
            c = x.next();
            if (c > ' ') {
                break;
            }
        } while (c != 0);
        switch (c) {
            case 0:
                return null;
            case '\"':
            case '\'':
                return x.nextString(c);
            case ',':
                x.back();
                return "";
            default:
                x.back();
                return x.nextTo(',');
        }
    }

    public static JSONArray rowToJSONArray(JSONTokener x) throws JSONException {
        JSONArray ja = new JSONArray();
        while (true) {
            String value = getValue(x);
            if (value == null) {
                return null;
            }
            ja.put(value);
            while (true) {
                char c = x.next();
                if (c == ',') {
                    break;
                } else if (c != ' ') {
                    if (c == '\n' || c == '\r' || c == 0) {
                        return ja;
                    }
                    throw x.syntaxError(new StringBuffer().append("Bad character '").append(c).append("' (").append((int) c).append(").").toString());
                }
            }
        }
    }

    public static JSONObject rowToJSONObject(JSONArray names, JSONTokener x) throws JSONException {
        JSONArray ja = rowToJSONArray(x);
        if (ja != null) {
            return ja.toJSONObject(names);
        }
        return null;
    }

    public static JSONArray toJSONArray(String string) throws JSONException {
        return toJSONArray(new JSONTokener(string));
    }

    public static JSONArray toJSONArray(JSONTokener x) throws JSONException {
        return toJSONArray(rowToJSONArray(x), x);
    }

    public static JSONArray toJSONArray(JSONArray names, String string) throws JSONException {
        return toJSONArray(names, new JSONTokener(string));
    }

    public static JSONArray toJSONArray(JSONArray names, JSONTokener x) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        while (true) {
            JSONObject jo = rowToJSONObject(names, x);
            if (jo == null) {
                break;
            }
            ja.put(jo);
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }

    public static String rowToString(JSONArray ja) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ja.length(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            Object o = ja.opt(i);
            if (o != null) {
                String s = o.toString();
                if (s.indexOf(44) >= 0) {
                    if (s.indexOf(34) >= 0) {
                        sb.append('\'');
                        sb.append(s);
                        sb.append('\'');
                    } else {
                        sb.append('\"');
                        sb.append(s);
                        sb.append('\"');
                    }
                } else {
                    sb.append(s);
                }
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    public static String toString(JSONArray ja) throws JSONException {
        JSONArray names;
        JSONObject jo = ja.optJSONObject(0);
        if (jo != null && (names = jo.names()) != null) {
            return new StringBuffer().append(rowToString(names)).append(toString(names, ja)).toString();
        }
        return null;
    }

    public static String toString(JSONArray names, JSONArray ja) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.optJSONObject(i);
            if (jo != null) {
                sb.append(rowToString(jo.toJSONArray(names)));
            }
        }
        return sb.toString();
    }
}
