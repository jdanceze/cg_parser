package com.oreilly.servlet;

import java.util.Hashtable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/CookieParser.class */
public class CookieParser {
    private HttpServletRequest req;
    private Hashtable cookieJar = new Hashtable();

    public CookieParser(HttpServletRequest req) {
        this.req = req;
        parseCookies();
    }

    void parseCookies() {
        Cookie[] cookies = this.req.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                String name = cookies[i].getName();
                String value = cookies[i].getValue();
                this.cookieJar.put(name, value);
            }
        }
    }

    public String getStringCookie(String name) throws CookieNotFoundException {
        String value = (String) this.cookieJar.get(name);
        if (value == null) {
            throw new CookieNotFoundException(new StringBuffer().append(name).append(" not found").toString());
        }
        return value;
    }

    public String getStringCookie(String name, String def) {
        try {
            return getStringCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean getBooleanCookie(String name) throws CookieNotFoundException {
        return new Boolean(getStringCookie(name)).booleanValue();
    }

    public boolean getBooleanCookie(String name, boolean def) {
        try {
            return getBooleanCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public byte getByteCookie(String name) throws CookieNotFoundException, NumberFormatException {
        return Byte.parseByte(getStringCookie(name));
    }

    public byte getByteCookie(String name, byte def) {
        try {
            return getByteCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public char getCharCookie(String name) throws CookieNotFoundException {
        String param = getStringCookie(name);
        if (param.length() == 0) {
            throw new CookieNotFoundException(new StringBuffer().append(name).append(" is empty string").toString());
        }
        return param.charAt(0);
    }

    public char getCharCookie(String name, char def) {
        try {
            return getCharCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public double getDoubleCookie(String name) throws CookieNotFoundException, NumberFormatException {
        return new Double(getStringCookie(name)).doubleValue();
    }

    public double getDoubleCookie(String name, double def) {
        try {
            return getDoubleCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public float getFloatCookie(String name) throws CookieNotFoundException, NumberFormatException {
        return new Float(getStringCookie(name)).floatValue();
    }

    public float getFloatCookie(String name, float def) {
        try {
            return getFloatCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public int getIntCookie(String name) throws CookieNotFoundException, NumberFormatException {
        return Integer.parseInt(getStringCookie(name));
    }

    public int getIntCookie(String name, int def) {
        try {
            return getIntCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public long getLongCookie(String name) throws CookieNotFoundException, NumberFormatException {
        return Long.parseLong(getStringCookie(name));
    }

    public long getLongCookie(String name, long def) {
        try {
            return getLongCookie(name);
        } catch (Exception e) {
            return def;
        }
    }

    public short getShortCookie(String name) throws CookieNotFoundException, NumberFormatException {
        return Short.parseShort(getStringCookie(name));
    }

    public short getShortCookie(String name, short def) {
        try {
            return getShortCookie(name);
        } catch (Exception e) {
            return def;
        }
    }
}
