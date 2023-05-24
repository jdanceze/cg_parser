package javax.mail.internet;
/* compiled from: MailDateFormat.java */
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MailDateParser.class */
class MailDateParser {
    int index = 0;
    char[] orig;

    public MailDateParser(char[] orig) {
        this.orig = null;
        this.orig = orig;
    }

    public void skipUntilNumber() throws java.text.ParseException {
        while (true) {
            try {
                switch (this.orig[this.index]) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        return;
                    default:
                        this.index++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new java.text.ParseException("No Number Found", this.index);
            }
        }
    }

    public void skipWhiteSpace() {
        int len = this.orig.length;
        while (this.index < len) {
            switch (this.orig[this.index]) {
                case '\t':
                case '\n':
                case '\r':
                case ' ':
                    this.index++;
                default:
                    return;
            }
        }
    }

    public int peekChar() throws java.text.ParseException {
        if (this.index < this.orig.length) {
            return this.orig[this.index];
        }
        throw new java.text.ParseException("No more characters", this.index);
    }

    public void skipChar(char c) throws java.text.ParseException {
        if (this.index < this.orig.length) {
            if (this.orig[this.index] == c) {
                this.index++;
                return;
            }
            throw new java.text.ParseException("Wrong char", this.index);
        }
        throw new java.text.ParseException("No more characters", this.index);
    }

    public boolean skipIfChar(char c) throws java.text.ParseException {
        if (this.index < this.orig.length) {
            if (this.orig[this.index] == c) {
                this.index++;
                return true;
            }
            return false;
        }
        throw new java.text.ParseException("No more characters", this.index);
    }

    public int parseNumber() throws java.text.ParseException {
        int i;
        int length = this.orig.length;
        boolean gotNum = false;
        int result = 0;
        while (this.index < length) {
            switch (this.orig[this.index]) {
                case '0':
                    i = result * 10;
                    break;
                case '1':
                    i = (result * 10) + 1;
                    break;
                case '2':
                    i = (result * 10) + 2;
                    break;
                case '3':
                    i = (result * 10) + 3;
                    break;
                case '4':
                    i = (result * 10) + 4;
                    break;
                case '5':
                    i = (result * 10) + 5;
                    break;
                case '6':
                    i = (result * 10) + 6;
                    break;
                case '7':
                    i = (result * 10) + 7;
                    break;
                case '8':
                    i = (result * 10) + 8;
                    break;
                case '9':
                    i = (result * 10) + 9;
                    break;
                default:
                    if (gotNum) {
                        return result;
                    }
                    throw new java.text.ParseException("No Number found", this.index);
            }
            result = i;
            gotNum = true;
            this.index++;
        }
        if (gotNum) {
            return result;
        }
        throw new java.text.ParseException("No Number found", this.index);
    }

    public int parseMonth() throws java.text.ParseException {
        try {
            char[] cArr = this.orig;
            int i = this.index;
            this.index = i + 1;
            switch (cArr[i]) {
                case 'A':
                case 'a':
                    char[] cArr2 = this.orig;
                    int i2 = this.index;
                    this.index = i2 + 1;
                    char curr = cArr2[i2];
                    if (curr == 'P' || curr == 'p') {
                        char[] cArr3 = this.orig;
                        int i3 = this.index;
                        this.index = i3 + 1;
                        char curr2 = cArr3[i3];
                        if (curr2 == 'R' || curr2 == 'r') {
                            return 3;
                        }
                    } else if (curr == 'U' || curr == 'u') {
                        char[] cArr4 = this.orig;
                        int i4 = this.index;
                        this.index = i4 + 1;
                        char curr3 = cArr4[i4];
                        if (curr3 == 'G' || curr3 == 'g') {
                            return 7;
                        }
                    }
                    break;
                case 'D':
                case 'd':
                    char[] cArr5 = this.orig;
                    int i5 = this.index;
                    this.index = i5 + 1;
                    char curr4 = cArr5[i5];
                    if (curr4 == 'E' || curr4 == 'e') {
                        char[] cArr6 = this.orig;
                        int i6 = this.index;
                        this.index = i6 + 1;
                        char curr5 = cArr6[i6];
                        if (curr5 == 'C' || curr5 == 'c') {
                            return 11;
                        }
                    }
                    break;
                case 'F':
                case 'f':
                    char[] cArr7 = this.orig;
                    int i7 = this.index;
                    this.index = i7 + 1;
                    char curr6 = cArr7[i7];
                    if (curr6 == 'E' || curr6 == 'e') {
                        char[] cArr8 = this.orig;
                        int i8 = this.index;
                        this.index = i8 + 1;
                        char curr7 = cArr8[i8];
                        if (curr7 == 'B' || curr7 == 'b') {
                            return 1;
                        }
                    }
                    break;
                case 'J':
                case 'j':
                    char[] cArr9 = this.orig;
                    int i9 = this.index;
                    this.index = i9 + 1;
                    switch (cArr9[i9]) {
                        case 'A':
                        case 'a':
                            char[] cArr10 = this.orig;
                            int i10 = this.index;
                            this.index = i10 + 1;
                            char curr8 = cArr10[i10];
                            if (curr8 == 'N' || curr8 == 'n') {
                                return 0;
                            }
                            break;
                        case 'U':
                        case 'u':
                            char[] cArr11 = this.orig;
                            int i11 = this.index;
                            this.index = i11 + 1;
                            char curr9 = cArr11[i11];
                            if (curr9 == 'N' || curr9 == 'n') {
                                return 5;
                            }
                            if (curr9 == 'L' || curr9 == 'l') {
                                return 6;
                            }
                            break;
                    }
                    break;
                case 'M':
                case 'm':
                    char[] cArr12 = this.orig;
                    int i12 = this.index;
                    this.index = i12 + 1;
                    char curr10 = cArr12[i12];
                    if (curr10 == 'A' || curr10 == 'a') {
                        char[] cArr13 = this.orig;
                        int i13 = this.index;
                        this.index = i13 + 1;
                        char curr11 = cArr13[i13];
                        if (curr11 == 'R' || curr11 == 'r') {
                            return 2;
                        }
                        if (curr11 == 'Y' || curr11 == 'y') {
                            return 4;
                        }
                    }
                    break;
                case 'N':
                case 'n':
                    char[] cArr14 = this.orig;
                    int i14 = this.index;
                    this.index = i14 + 1;
                    char curr12 = cArr14[i14];
                    if (curr12 == 'O' || curr12 == 'o') {
                        char[] cArr15 = this.orig;
                        int i15 = this.index;
                        this.index = i15 + 1;
                        char curr13 = cArr15[i15];
                        if (curr13 == 'V' || curr13 == 'v') {
                            return 10;
                        }
                    }
                    break;
                case 'O':
                case 'o':
                    char[] cArr16 = this.orig;
                    int i16 = this.index;
                    this.index = i16 + 1;
                    char curr14 = cArr16[i16];
                    if (curr14 == 'C' || curr14 == 'c') {
                        char[] cArr17 = this.orig;
                        int i17 = this.index;
                        this.index = i17 + 1;
                        char curr15 = cArr17[i17];
                        if (curr15 == 'T' || curr15 == 't') {
                            return 9;
                        }
                    }
                    break;
                case 'S':
                case 's':
                    char[] cArr18 = this.orig;
                    int i18 = this.index;
                    this.index = i18 + 1;
                    char curr16 = cArr18[i18];
                    if (curr16 == 'E' || curr16 == 'e') {
                        char[] cArr19 = this.orig;
                        int i19 = this.index;
                        this.index = i19 + 1;
                        char curr17 = cArr19[i19];
                        if (curr17 == 'P' || curr17 == 'p') {
                            return 8;
                        }
                    }
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        throw new java.text.ParseException("Bad Month", this.index);
    }

    public int parseTimeZone() throws java.text.ParseException {
        if (this.index >= this.orig.length) {
            throw new java.text.ParseException("No more characters", this.index);
        }
        char test = this.orig[this.index];
        if (test == '+' || test == '-') {
            return parseNumericTimeZone();
        }
        return parseAlphaTimeZone();
    }

    public int parseNumericTimeZone() throws java.text.ParseException {
        boolean switchSign = false;
        char[] cArr = this.orig;
        int i = this.index;
        this.index = i + 1;
        char first = cArr[i];
        if (first == '+') {
            switchSign = true;
        } else if (first != '-') {
            throw new java.text.ParseException("Bad Numeric TimeZone", this.index);
        }
        int tz = parseNumber();
        int offset = ((tz / 100) * 60) + (tz % 100);
        if (switchSign) {
            return -offset;
        }
        return offset;
    }

    public int parseAlphaTimeZone() throws java.text.ParseException {
        int result;
        boolean foundCommon = false;
        try {
            char[] cArr = this.orig;
            int i = this.index;
            this.index = i + 1;
            switch (cArr[i]) {
                case 'C':
                case 'c':
                    result = 360;
                    foundCommon = true;
                    break;
                case 'E':
                case 'e':
                    result = 300;
                    foundCommon = true;
                    break;
                case 'G':
                case 'g':
                    char[] cArr2 = this.orig;
                    int i2 = this.index;
                    this.index = i2 + 1;
                    char curr = cArr2[i2];
                    if (curr == 'M' || curr == 'm') {
                        char[] cArr3 = this.orig;
                        int i3 = this.index;
                        this.index = i3 + 1;
                        char curr2 = cArr3[i3];
                        if (curr2 == 'T' || curr2 == 't') {
                            result = 0;
                            break;
                        }
                    }
                    throw new java.text.ParseException("Bad Alpha TimeZone", this.index);
                case 'M':
                case 'm':
                    result = 420;
                    foundCommon = true;
                    break;
                case 'P':
                case 'p':
                    result = 480;
                    foundCommon = true;
                    break;
                case 'U':
                case 'u':
                    char[] cArr4 = this.orig;
                    int i4 = this.index;
                    this.index = i4 + 1;
                    char curr3 = cArr4[i4];
                    if (curr3 == 'T' || curr3 == 't') {
                        result = 0;
                        break;
                    } else {
                        throw new java.text.ParseException("Bad Alpha TimeZone", this.index);
                    }
                    break;
                default:
                    throw new java.text.ParseException("Bad Alpha TimeZone", this.index);
            }
            if (foundCommon) {
                char[] cArr5 = this.orig;
                int i5 = this.index;
                this.index = i5 + 1;
                char curr4 = cArr5[i5];
                if (curr4 == 'S' || curr4 == 's') {
                    char[] cArr6 = this.orig;
                    int i6 = this.index;
                    this.index = i6 + 1;
                    char curr5 = cArr6[i6];
                    if (curr5 != 'T' && curr5 != 't') {
                        throw new java.text.ParseException("Bad Alpha TimeZone", this.index);
                    }
                } else if (curr4 == 'D' || curr4 == 'd') {
                    char[] cArr7 = this.orig;
                    int i7 = this.index;
                    this.index = i7 + 1;
                    char curr6 = cArr7[i7];
                    if (curr6 == 'T' || curr6 != 't') {
                        result -= 60;
                    } else {
                        throw new java.text.ParseException("Bad Alpha TimeZone", this.index);
                    }
                }
            }
            return result;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new java.text.ParseException("Bad Alpha TimeZone", this.index);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getIndex() {
        return this.index;
    }
}
