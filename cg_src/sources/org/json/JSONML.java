package org.json;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/JSONML.class */
public class JSONML {
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0073, code lost:
        throw r5.syntaxError("Expected 'CDATA['");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static org.json.JSONArray parse(org.json.XMLTokener r5, org.json.JSONArray r6) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 555
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.JSONML.parse(org.json.XMLTokener, org.json.JSONArray):org.json.JSONArray");
    }

    public static JSONArray toJSONArray(String string) throws JSONException {
        return toJSONArray(new XMLTokener(string));
    }

    public static JSONArray toJSONArray(XMLTokener x) throws JSONException {
        return parse(x, null);
    }

    private static void stringify(JSONArray ja, StringBuffer b) throws JSONException {
        int i;
        b.append('<');
        b.append(ja.get(0));
        Object o = ja.opt(1);
        if (o instanceof JSONObject) {
            JSONObject jo = (JSONObject) o;
            Iterator keys = jo.keys();
            while (keys.hasNext()) {
                String k = keys.next().toString();
                Object v = jo.get(k).toString();
                b.append(' ');
                b.append(k);
                b.append("=\"");
                b.append(XML.escape((String) v));
                b.append('\"');
            }
            i = 2;
        } else {
            i = 1;
        }
        int len = ja.length();
        if (i >= len) {
            b.append("/>");
            return;
        }
        b.append('>');
        while (i < len) {
            Object v2 = ja.get(i);
            if (v2 instanceof JSONArray) {
                stringify((JSONArray) v2, b);
            } else {
                b.append(XML.escape(v2.toString()));
            }
            i++;
        }
        b.append("</");
        b.append(ja.get(0));
        b.append('>');
    }

    public static String toString(JSONArray ja) throws JSONException {
        StringBuffer b = new StringBuffer();
        stringify(ja, b);
        return b.toString();
    }
}
