package org.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/JSONTokener.class */
public class JSONTokener {
    private int index;
    private Reader reader;
    private char lastChar;
    private boolean useLastChar;

    public JSONTokener(Reader reader) {
        this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
        this.useLastChar = false;
        this.index = 0;
    }

    public JSONTokener(String s) {
        this(new StringReader(s));
    }

    public void back() throws JSONException {
        if (this.useLastChar || this.index <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.index--;
        this.useLastChar = true;
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'W';
        }
        return -1;
    }

    public boolean more() throws JSONException {
        char nextChar = next();
        if (nextChar == 0) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        if (this.useLastChar) {
            this.useLastChar = false;
            if (this.lastChar != 0) {
                this.index++;
            }
            return this.lastChar;
        }
        try {
            int c = this.reader.read();
            if (c <= 0) {
                this.lastChar = (char) 0;
                return (char) 0;
            }
            this.index++;
            this.lastChar = (char) c;
            return this.lastChar;
        } catch (IOException exc) {
            throw new JSONException(exc);
        }
    }

    public char next(char c) throws JSONException {
        char n = next();
        if (n != c) {
            throw syntaxError(new StringBuffer().append("Expected '").append(c).append("' and instead saw '").append(n).append("'").toString());
        }
        return n;
    }

    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        char[] buffer = new char[n];
        int pos = 0;
        if (this.useLastChar) {
            this.useLastChar = false;
            buffer[0] = this.lastChar;
            pos = 1;
        }
        while (pos < n) {
            try {
                int len = this.reader.read(buffer, pos, n - pos);
                if (len == -1) {
                    break;
                }
                pos += len;
            } catch (IOException exc) {
                throw new JSONException(exc);
            }
        }
        this.index += pos;
        if (pos < n) {
            throw syntaxError("Substring bounds error");
        }
        this.lastChar = buffer[n - 1];
        return new String(buffer);
    }

    public char nextClean() throws JSONException {
        char c;
        char c2;
        while (true) {
            c = next();
            if (c == '/') {
                switch (next()) {
                    case '*':
                        while (true) {
                            char c3 = next();
                            if (c3 == 0) {
                                throw syntaxError("Unclosed comment");
                            }
                            if (c3 == '*') {
                                if (next() == '/') {
                                    continue;
                                } else {
                                    back();
                                }
                            }
                        }
                    case '/':
                        while (true) {
                            char c4 = next();
                            if (c4 == '\n') {
                                continue;
                            } else if (c4 != '\r') {
                                if (c4 == 0) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        break;
                    default:
                        back();
                        return '/';
                }
            } else if (c == '#') {
                do {
                    c2 = next();
                    if (c2 != '\n' && c2 != '\r') {
                    }
                } while (c2 != 0);
            } else if (c == 0 || c > ' ') {
            }
        }
        return c;
    }

    public String nextString(char quote) throws JSONException {
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = next();
            switch (c) {
                case 0:
                case '\n':
                case '\r':
                    throw syntaxError("Unterminated string");
                case '\\':
                    char c2 = next();
                    switch (c2) {
                        case 'b':
                            sb.append('\b');
                            continue;
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'g':
                        case 'h':
                        case 'i':
                        case 'j':
                        case 'k':
                        case 'l':
                        case 'm':
                        case 'o':
                        case 'p':
                        case 'q':
                        case 's':
                        case 'v':
                        case 'w':
                        default:
                            sb.append(c2);
                            continue;
                        case 'f':
                            sb.append('\f');
                            continue;
                        case 'n':
                            sb.append('\n');
                            continue;
                        case 'r':
                            sb.append('\r');
                            continue;
                        case 't':
                            sb.append('\t');
                            continue;
                        case 'u':
                            sb.append((char) Integer.parseInt(next(4), 16));
                            continue;
                        case 'x':
                            sb.append((char) Integer.parseInt(next(2), 16));
                            continue;
                    }
                default:
                    if (c == quote) {
                        return sb.toString();
                    }
                    sb.append(c);
                    break;
            }
        }
    }

    public String nextTo(char d) throws JSONException {
        char c;
        StringBuffer sb = new StringBuffer();
        while (true) {
            c = next();
            if (c == d || c == 0 || c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if (c != 0) {
            back();
        }
        return sb.toString().trim();
    }

    public String nextTo(String delimiters) throws JSONException {
        char c;
        StringBuffer sb = new StringBuffer();
        while (true) {
            c = next();
            if (delimiters.indexOf(c) >= 0 || c == 0 || c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if (c != 0) {
            back();
        }
        return sb.toString().trim();
    }

    public Object nextValue() throws JSONException {
        char c = nextClean();
        switch (c) {
            case '\"':
            case '\'':
                return nextString(c);
            case '(':
            case '[':
                back();
                return new JSONArray(this);
            case '{':
                back();
                return new JSONObject(this);
            default:
                StringBuffer sb = new StringBuffer();
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = next();
                }
                back();
                String s = sb.toString().trim();
                if (s.equals("")) {
                    throw syntaxError("Missing value");
                }
                if (s.equalsIgnoreCase("true")) {
                    return Boolean.TRUE;
                }
                if (s.equalsIgnoreCase("false")) {
                    return Boolean.FALSE;
                }
                if (s.equalsIgnoreCase(Jimple.NULL)) {
                    return JSONObject.NULL;
                }
                if ((c >= '0' && c <= '9') || c == '.' || c == '-' || c == '+') {
                    if (c == '0') {
                        if (s.length() > 2 && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
                            try {
                                return new Integer(Integer.parseInt(s.substring(2), 16));
                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                return new Integer(Integer.parseInt(s, 8));
                            } catch (Exception e2) {
                            }
                        }
                    }
                    try {
                        return new Integer(s);
                    } catch (Exception e3) {
                        try {
                            return new Long(s);
                        } catch (Exception e4) {
                            try {
                                return new Double(s);
                            } catch (Exception e5) {
                                return s;
                            }
                        }
                    }
                }
                return s;
        }
    }

    public char skipTo(char to) throws JSONException {
        char c;
        try {
            int startIndex = this.index;
            this.reader.mark(Integer.MAX_VALUE);
            do {
                c = next();
                if (c == 0) {
                    this.reader.reset();
                    this.index = startIndex;
                    return c;
                }
            } while (c != to);
            back();
            return c;
        } catch (IOException exc) {
            throw new JSONException(exc);
        }
    }

    public JSONException syntaxError(String message) {
        return new JSONException(new StringBuffer().append(message).append(toString()).toString());
    }

    public String toString() {
        return new StringBuffer().append(" at character ").append(this.index).toString();
    }
}
