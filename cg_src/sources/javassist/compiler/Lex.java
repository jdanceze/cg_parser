package javassist.compiler;

import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/compiler/Lex.class */
public class Lex implements TokenId {
    private String input;
    private int maxlen;
    private static final int[] equalOps = {TokenId.NEQ, 0, 0, 0, TokenId.MOD_E, TokenId.AND_E, 0, 0, 0, TokenId.MUL_E, TokenId.PLUS_E, 0, TokenId.MINUS_E, 0, TokenId.DIV_E, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, TokenId.LE, TokenId.EQ, TokenId.GE, 0};
    private static final KeywordTable ktable = new KeywordTable();
    private int lastChar = -1;
    private StringBuffer textBuffer = new StringBuffer();
    private Token currentToken = new Token();
    private Token lookAheadTokens = null;
    private int position = 0;
    private int lineNumber = 0;

    public Lex(String s) {
        this.input = s;
        this.maxlen = s.length();
    }

    public int get() {
        if (this.lookAheadTokens == null) {
            return get(this.currentToken);
        }
        Token t = this.lookAheadTokens;
        this.currentToken = t;
        this.lookAheadTokens = this.lookAheadTokens.next;
        return t.tokenId;
    }

    public int lookAhead() {
        return lookAhead(0);
    }

    public int lookAhead(int i) {
        Token tk = this.lookAheadTokens;
        if (tk == null) {
            Token token = this.currentToken;
            tk = token;
            this.lookAheadTokens = token;
            tk.next = null;
            get(tk);
        }
        while (true) {
            int i2 = i;
            i--;
            if (i2 > 0) {
                if (tk.next == null) {
                    Token tk2 = new Token();
                    tk.next = tk2;
                    get(tk2);
                }
                tk = tk.next;
            } else {
                this.currentToken = tk;
                return tk.tokenId;
            }
        }
    }

    public String getString() {
        return this.currentToken.textValue;
    }

    public long getLong() {
        return this.currentToken.longValue;
    }

    public double getDouble() {
        return this.currentToken.doubleValue;
    }

    private int get(Token token) {
        int t;
        do {
            t = readLine(token);
        } while (t == 10);
        token.tokenId = t;
        return t;
    }

    private int readLine(Token token) {
        int c = getNextNonWhiteChar();
        if (c < 0) {
            return c;
        }
        if (c == 10) {
            this.lineNumber++;
            return 10;
        } else if (c == 39) {
            return readCharConst(token);
        } else {
            if (c == 34) {
                return readStringL(token);
            }
            if (48 <= c && c <= 57) {
                return readNumber(c, token);
            }
            if (c == 46) {
                int c2 = getc();
                if (48 <= c2 && c2 <= 57) {
                    StringBuffer tbuf = this.textBuffer;
                    tbuf.setLength(0);
                    tbuf.append('.');
                    return readDouble(tbuf, c2, token);
                }
                ungetc(c2);
                return readSeparator(46);
            } else if (Character.isJavaIdentifierStart((char) c)) {
                return readIdentifier(c, token);
            } else {
                return readSeparator(c);
            }
        }
    }

    private int getNextNonWhiteChar() {
        int c;
        do {
            c = getc();
            if (c == 47) {
                int c2 = getc();
                if (c2 == 47) {
                    do {
                        c = getc();
                        if (c == 10 || c == 13) {
                            break;
                        }
                    } while (c != -1);
                } else if (c2 == 42) {
                    while (true) {
                        c = getc();
                        if (c == -1) {
                            break;
                        } else if (c == 42) {
                            int c3 = getc();
                            if (c3 == 47) {
                                c = 32;
                                break;
                            }
                            ungetc(c3);
                        }
                    }
                } else {
                    ungetc(c2);
                    c = 47;
                }
            }
        } while (isBlank(c));
        return c;
    }

    private int readCharConst(Token token) {
        int i = 0;
        while (true) {
            int value = i;
            int c = getc();
            if (c != 39) {
                if (c == 92) {
                    i = readEscapeChar();
                } else if (c < 32) {
                    if (c == 10) {
                        this.lineNumber++;
                        return 500;
                    }
                    return 500;
                } else {
                    i = c;
                }
            } else {
                token.longValue = value;
                return 401;
            }
        }
    }

    private int readEscapeChar() {
        int c = getc();
        if (c == 110) {
            c = 10;
        } else if (c == 116) {
            c = 9;
        } else if (c == 114) {
            c = 13;
        } else if (c == 102) {
            c = 12;
        } else if (c == 10) {
            this.lineNumber++;
        }
        return c;
    }

    private int readStringL(Token token) {
        int c;
        StringBuffer tbuf = this.textBuffer;
        tbuf.setLength(0);
        while (true) {
            int cVar = getc();
            int c2 = cVar;
            if (cVar != 34) {
                if (c2 == 92) {
                    c2 = readEscapeChar();
                } else if (c2 == 10 || c2 < 0) {
                    break;
                }
                tbuf.append((char) c2);
            } else {
                while (true) {
                    c = getc();
                    if (c == 10) {
                        this.lineNumber++;
                    } else if (!isBlank(c)) {
                        break;
                    }
                }
                if (c != 34) {
                    ungetc(c);
                    token.textValue = tbuf.toString();
                    return 406;
                }
            }
        }
        this.lineNumber++;
        return 500;
    }

    private int readNumber(int c, Token token) {
        int c2;
        long value;
        int c3;
        long value2 = 0;
        int c22 = getc();
        if (c == 48) {
            if (c22 == 88 || c22 == 120) {
                while (true) {
                    c2 = getc();
                    if (48 <= c2 && c2 <= 57) {
                        value2 = (value2 * 16) + (c2 - 48);
                    } else if (65 <= c2 && c2 <= 70) {
                        value2 = (value2 * 16) + (c2 - 65) + 10;
                    } else if (97 > c2 || c2 > 102) {
                        break;
                    } else {
                        value2 = (value2 * 16) + (c2 - 97) + 10;
                    }
                }
                token.longValue = value2;
                if (c2 == 76 || c2 == 108) {
                    return 403;
                }
                ungetc(c2);
                return 402;
            } else if (48 <= c22 && c22 <= 55) {
                long j = c22 - 48;
                while (true) {
                    value = j;
                    c3 = getc();
                    if (48 > c3 || c3 > 55) {
                        break;
                    }
                    j = (value * 8) + (c3 - 48);
                }
                token.longValue = value;
                if (c3 == 76 || c3 == 108) {
                    return 403;
                }
                ungetc(c3);
                return 402;
            }
        }
        long value3 = c - 48;
        while (48 <= c22 && c22 <= 57) {
            value3 = ((value3 * 10) + c22) - 48;
            c22 = getc();
        }
        token.longValue = value3;
        if (c22 == 70 || c22 == 102) {
            token.doubleValue = value3;
            return 404;
        } else if (c22 == 69 || c22 == 101 || c22 == 68 || c22 == 100 || c22 == 46) {
            StringBuffer tbuf = this.textBuffer;
            tbuf.setLength(0);
            tbuf.append(value3);
            return readDouble(tbuf, c22, token);
        } else if (c22 == 76 || c22 == 108) {
            return 403;
        } else {
            ungetc(c22);
            return 402;
        }
    }

    private int readDouble(StringBuffer sbuf, int c, Token token) {
        if (c != 69 && c != 101 && c != 68 && c != 100) {
            sbuf.append((char) c);
            while (true) {
                c = getc();
                if (48 > c || c > 57) {
                    break;
                }
                sbuf.append((char) c);
            }
        }
        if (c == 69 || c == 101) {
            sbuf.append((char) c);
            c = getc();
            if (c == 43 || c == 45) {
                sbuf.append((char) c);
                c = getc();
            }
            while (48 <= c && c <= 57) {
                sbuf.append((char) c);
                c = getc();
            }
        }
        try {
            token.doubleValue = Double.parseDouble(sbuf.toString());
            if (c == 70 || c == 102) {
                return 404;
            }
            if (c != 68 && c != 100) {
                ungetc(c);
                return 405;
            }
            return 405;
        } catch (NumberFormatException e) {
            return 500;
        }
    }

    static {
        ktable.append(Jimple.ABSTRACT, 300);
        ktable.append("boolean", 301);
        ktable.append(Jimple.BREAK, 302);
        ktable.append("byte", 303);
        ktable.append(Jimple.CASE, 304);
        ktable.append(Jimple.CATCH, 305);
        ktable.append("char", 306);
        ktable.append("class", 307);
        ktable.append("const", 308);
        ktable.append("continue", 309);
        ktable.append("default", 310);
        ktable.append("do", 311);
        ktable.append("double", 312);
        ktable.append("else", 313);
        ktable.append(Jimple.EXTENDS, 314);
        ktable.append("false", 411);
        ktable.append(Jimple.FINAL, 315);
        ktable.append("finally", 316);
        ktable.append(Jimple.FLOAT, 317);
        ktable.append("for", 318);
        ktable.append(Jimple.GOTO, 319);
        ktable.append(Jimple.IF, 320);
        ktable.append(Jimple.IMPLEMENTS, 321);
        ktable.append("import", 322);
        ktable.append(Jimple.INSTANCEOF, 323);
        ktable.append("int", 324);
        ktable.append("interface", 325);
        ktable.append("long", 326);
        ktable.append(Jimple.NATIVE, 327);
        ktable.append("new", 328);
        ktable.append(Jimple.NULL, 412);
        ktable.append("package", 329);
        ktable.append(Jimple.PRIVATE, 330);
        ktable.append(Jimple.PROTECTED, 331);
        ktable.append(Jimple.PUBLIC, 332);
        ktable.append("return", 333);
        ktable.append("short", 334);
        ktable.append(Jimple.STATIC, 335);
        ktable.append(Jimple.STRICTFP, TokenId.STRICT);
        ktable.append("super", 336);
        ktable.append("switch", 337);
        ktable.append(Jimple.SYNCHRONIZED, 338);
        ktable.append("this", 339);
        ktable.append(Jimple.THROW, TokenId.THROW);
        ktable.append(Jimple.THROWS, TokenId.THROWS);
        ktable.append(Jimple.TRANSIENT, TokenId.TRANSIENT);
        ktable.append("true", 410);
        ktable.append("try", TokenId.TRY);
        ktable.append(Jimple.VOID, TokenId.VOID);
        ktable.append(Jimple.VOLATILE, TokenId.VOLATILE);
        ktable.append("while", TokenId.WHILE);
    }

    private int readSeparator(int c) {
        int c2;
        if (33 <= c && c <= 63) {
            int t = equalOps[c - 33];
            if (t == 0) {
                return c;
            }
            c2 = getc();
            if (c == c2) {
                switch (c) {
                    case 38:
                        return TokenId.ANDAND;
                    case 43:
                        return TokenId.PLUSPLUS;
                    case 45:
                        return TokenId.MINUSMINUS;
                    case 60:
                        int c3 = getc();
                        if (c3 == 61) {
                            return TokenId.LSHIFT_E;
                        }
                        ungetc(c3);
                        return TokenId.LSHIFT;
                    case 61:
                        return TokenId.EQ;
                    case 62:
                        int c32 = getc();
                        if (c32 == 61) {
                            return TokenId.RSHIFT_E;
                        }
                        if (c32 == 62) {
                            int c33 = getc();
                            if (c33 == 61) {
                                return TokenId.ARSHIFT_E;
                            }
                            ungetc(c33);
                            return TokenId.ARSHIFT;
                        }
                        ungetc(c32);
                        return TokenId.RSHIFT;
                }
            } else if (c2 == 61) {
                return t;
            }
        } else if (c == 94) {
            c2 = getc();
            if (c2 == 61) {
                return TokenId.EXOR_E;
            }
        } else if (c == 124) {
            c2 = getc();
            if (c2 == 61) {
                return TokenId.OR_E;
            }
            if (c2 == 124) {
                return TokenId.OROR;
            }
        } else {
            return c;
        }
        ungetc(c2);
        return c;
    }

    private int readIdentifier(int c, Token token) {
        StringBuffer tbuf = this.textBuffer;
        tbuf.setLength(0);
        do {
            tbuf.append((char) c);
            c = getc();
        } while (Character.isJavaIdentifierPart((char) c));
        ungetc(c);
        String name = tbuf.toString();
        int t = ktable.lookup(name);
        if (t >= 0) {
            return t;
        }
        token.textValue = name;
        return 400;
    }

    private static boolean isBlank(int c) {
        return c == 32 || c == 9 || c == 12 || c == 13 || c == 10;
    }

    private static boolean isDigit(int c) {
        return 48 <= c && c <= 57;
    }

    private void ungetc(int c) {
        this.lastChar = c;
    }

    public String getTextAround() {
        int begin = this.position - 10;
        if (begin < 0) {
            begin = 0;
        }
        int end = this.position + 10;
        if (end > this.maxlen) {
            end = this.maxlen;
        }
        return this.input.substring(begin, end);
    }

    private int getc() {
        if (this.lastChar < 0) {
            if (this.position < this.maxlen) {
                String str = this.input;
                int i = this.position;
                this.position = i + 1;
                return str.charAt(i);
            }
            return -1;
        }
        int c = this.lastChar;
        this.lastChar = -1;
        return c;
    }
}
