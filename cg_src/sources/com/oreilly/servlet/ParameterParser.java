package com.oreilly.servlet;

import android.hardware.Camera;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.servlet.ServletRequest;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/ParameterParser.class */
public class ParameterParser {
    private ServletRequest req;
    private String encoding;

    public ParameterParser(ServletRequest req) {
        this.req = req;
    }

    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        new String("".getBytes("8859_1"), encoding);
        this.encoding = encoding;
    }

    public String getStringParameter(String name) throws ParameterNotFoundException {
        String[] values = this.req.getParameterValues(name);
        if (values == null) {
            throw new ParameterNotFoundException(new StringBuffer().append(name).append(" not found").toString());
        }
        if (values[0].length() == 0) {
            throw new ParameterNotFoundException(new StringBuffer().append(name).append(" was empty").toString());
        }
        if (this.encoding == null) {
            return values[0];
        }
        try {
            return new String(values[0].getBytes("8859_1"), this.encoding);
        } catch (UnsupportedEncodingException e) {
            return values[0];
        }
    }

    public String getStringParameter(String name, String def) {
        try {
            return getStringParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean getBooleanParameter(String name) throws ParameterNotFoundException, NumberFormatException {
        String value = getStringParameter(name).toLowerCase();
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_ON) || value.equalsIgnoreCase("yes")) {
            return true;
        }
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("off") || value.equalsIgnoreCase("no")) {
            return false;
        }
        throw new NumberFormatException(new StringBuffer().append("Parameter ").append(name).append(" value ").append(value).append(" is not a boolean").toString());
    }

    public boolean getBooleanParameter(String name, boolean def) {
        try {
            return getBooleanParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public byte getByteParameter(String name) throws ParameterNotFoundException, NumberFormatException {
        return Byte.parseByte(getStringParameter(name));
    }

    public byte getByteParameter(String name, byte def) {
        try {
            return getByteParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public char getCharParameter(String name) throws ParameterNotFoundException {
        String param = getStringParameter(name);
        if (param.length() == 0) {
            throw new ParameterNotFoundException(new StringBuffer().append(name).append(" is empty string").toString());
        }
        return param.charAt(0);
    }

    public char getCharParameter(String name, char def) {
        try {
            return getCharParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public double getDoubleParameter(String name) throws ParameterNotFoundException, NumberFormatException {
        return new Double(getStringParameter(name)).doubleValue();
    }

    public double getDoubleParameter(String name, double def) {
        try {
            return getDoubleParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public float getFloatParameter(String name) throws ParameterNotFoundException, NumberFormatException {
        return new Float(getStringParameter(name)).floatValue();
    }

    public float getFloatParameter(String name, float def) {
        try {
            return getFloatParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public int getIntParameter(String name) throws ParameterNotFoundException, NumberFormatException {
        return Integer.parseInt(getStringParameter(name));
    }

    public int getIntParameter(String name, int def) {
        try {
            return getIntParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public long getLongParameter(String name) throws ParameterNotFoundException, NumberFormatException {
        return Long.parseLong(getStringParameter(name));
    }

    public long getLongParameter(String name, long def) {
        try {
            return getLongParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public short getShortParameter(String name) throws ParameterNotFoundException, NumberFormatException {
        return Short.parseShort(getStringParameter(name));
    }

    public short getShortParameter(String name, short def) {
        try {
            return getShortParameter(name);
        } catch (Exception e) {
            return def;
        }
    }

    public String[] getMissingParameters(String[] required) {
        Vector missing = new Vector();
        for (int i = 0; i < required.length; i++) {
            String val = getStringParameter(required[i], null);
            if (val == null) {
                missing.addElement(required[i]);
            }
        }
        if (missing.size() == 0) {
            return null;
        }
        String[] ret = new String[missing.size()];
        missing.copyInto(ret);
        return ret;
    }
}
