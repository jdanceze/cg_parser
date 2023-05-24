package jasmin;

import jas.jasError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java_cup.runtime.Symbol;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jasmin/Scanner.class */
public class Scanner implements java_cup.runtime.Scanner {
    InputStreamReader inp;
    int next_char;
    static final String WHITESPACE = " \n\t\r";
    static final String SEPARATORS = " \n\t\r:=";
    public int token_line_num;
    static final int BIGNUM = 65000;
    public Hashtable<String, Object> dict = new Hashtable<>();
    public int line_num = 1;
    public int char_num = 0;
    public StringBuffer line = new StringBuffer();
    char[] chars = new char[BIGNUM];
    char[] secondChars = new char[BIGNUM];
    char[] unicodeBuffer = new char[4];
    boolean is_first_sep = true;

    protected static boolean whitespace(int c) {
        return WHITESPACE.indexOf(c) != -1;
    }

    protected static boolean separator(int c) {
        return SEPARATORS.indexOf(c) != -1;
    }

    protected void advance() throws IOException {
        this.next_char = this.inp.read();
        if (this.next_char == 10) {
            this.line_num++;
            this.char_num = 0;
            this.line.setLength(0);
            return;
        }
        this.line.append((char) this.next_char);
        this.char_num++;
    }

    public Scanner(InputStream i) throws IOException {
        this.inp = new InputStreamReader(i);
        advance();
    }

    int readOctal(int firstChar) throws IOException {
        advance();
        int d2 = this.next_char;
        advance();
        int d3 = this.next_char;
        return (((firstChar - 48) & 7) * 64) + (((d2 - 48) & 7) * 8) + ((d3 - 48) & 7);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0139, code lost:
        if (r9.next_char != 59) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x013c, code lost:
        advance();
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0146, code lost:
        if (r9.next_char != 10) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0150, code lost:
        if (r9.is_first_sep == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0157, code lost:
        return next_token();
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0158, code lost:
        r9.token_line_num = r9.line_num;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0169, code lost:
        return new java_cup.runtime.Symbol(74);
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0125, code lost:
        advance();
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0130, code lost:
        if (whitespace(r9.next_char) != false) goto L27;
     */
    @Override // java_cup.runtime.Scanner
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java_cup.runtime.Symbol next_token() throws java.io.IOException, jas.jasError {
        /*
            Method dump skipped, instructions count: 1108
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: jasmin.Scanner.next_token():java_cup.runtime.Symbol");
    }

    private Symbol tryParseAsNumber(String str) throws jasError {
        if (str.isEmpty()) {
            return null;
        }
        if (str.equals("+DoubleInfinity")) {
            return new Symbol(287, new Double(Double.POSITIVE_INFINITY));
        }
        if (str.equals("+DoubleNaN")) {
            return new Symbol(287, new Double(Double.NaN));
        }
        if (str.equals("+FloatNaN")) {
            return new Symbol(287, new Float(Double.NaN));
        }
        if (str.equals("-DoubleInfinity")) {
            return new Symbol(287, new Double(Double.NEGATIVE_INFINITY));
        }
        if (str.equals("+FloatInfinity")) {
            return new Symbol(287, new Float(Float.POSITIVE_INFINITY));
        }
        if (str.equals("-FloatInfinity")) {
            return new Symbol(287, new Float(Float.NEGATIVE_INFINITY));
        }
        Symbol tok = ReservedWords.get(str);
        if (tok != null) {
            return tok;
        }
        int idxOpen = str.indexOf("(");
        int idxClose = str.indexOf(")");
        if (idxOpen > 0 && idxClose > idxOpen) {
            return null;
        }
        char c = str.charAt(0);
        if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '-' || c == '+' || c == '.') {
            try {
                Number num = ScannerUtils.convertNumber(str);
                if (num instanceof Integer) {
                    return new Symbol(286, new Integer(num.intValue()));
                }
                return new Symbol(287, num);
            } catch (NumberFormatException e) {
                if (this.chars[0] == '.') {
                    throw new jasError("Unknown directive or badly formed number.");
                }
                throw new jasError("Badly formatted number: " + str);
            }
        }
        return null;
    }

    private int translateUnicodeCharacters(int pos) {
        int secondPos = 0;
        int i = 0;
        while (i < pos) {
            if (this.chars[i] == '\\' && i + 5 < pos && this.chars[i + 1] == 'u') {
                int intValue = Integer.parseInt(new String(this.chars, i + 2, 4), 16);
                try {
                    this.secondChars[secondPos] = (char) intValue;
                } catch (ArrayIndexOutOfBoundsException e) {
                    char[] tmparray = new char[this.secondChars.length * 2];
                    System.arraycopy(this.secondChars, 0, tmparray, 0, this.secondChars.length);
                    this.secondChars = tmparray;
                    this.secondChars[secondPos] = (char) intValue;
                }
                secondPos++;
                i += 5;
            } else {
                try {
                    this.secondChars[secondPos] = this.chars[i];
                } catch (ArrayIndexOutOfBoundsException e2) {
                    char[] tmparray2 = new char[this.secondChars.length * 2];
                    System.arraycopy(this.secondChars, 0, tmparray2, 0, this.secondChars.length);
                    this.secondChars = tmparray2;
                    this.secondChars[secondPos] = this.chars[i];
                }
                secondPos++;
            }
            i++;
        }
        return secondPos;
    }
}
