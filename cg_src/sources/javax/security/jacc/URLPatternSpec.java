package javax.security.jacc;

import java.util.Arrays;
import java.util.StringTokenizer;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/URLPatternSpec.class */
class URLPatternSpec extends URLPattern {
    private transient int hashCodeValue;
    private String canonicalSpec;
    private final String urlPatternList;
    private URLPattern[] urlPatternArray;

    public URLPatternSpec(String urlPatternSpec) {
        super(getFirstPattern(urlPatternSpec));
        this.hashCodeValue = 0;
        this.canonicalSpec = null;
        this.urlPatternArray = null;
        int colon = urlPatternSpec.indexOf(":");
        if (colon > 0) {
            this.urlPatternList = urlPatternSpec.substring(colon + 1);
            setURLPatternArray();
            return;
        }
        this.urlPatternList = null;
    }

    public String getURLPattern() {
        return super.toString();
    }

    @Override // javax.security.jacc.URLPattern
    public boolean equals(Object o) {
        if (o == null || !(o instanceof URLPatternSpec)) {
            return false;
        }
        URLPatternSpec that = (URLPatternSpec) o;
        return toString().equals(that.toString());
    }

    public int hashCode() {
        if (this.hashCodeValue == 0) {
            this.hashCodeValue = toString().hashCode();
        }
        return this.hashCodeValue;
    }

    public boolean implies(URLPatternSpec that) {
        if (that != null && super.implies((URLPattern) that)) {
            for (int i = 0; this.urlPatternArray != null && i < this.urlPatternArray.length; i++) {
                if (this.urlPatternArray[i] != null && this.urlPatternArray[i].implies(that)) {
                    return false;
                }
            }
            if (this.urlPatternArray != null && that.implies((URLPattern) this)) {
                if (that.urlPatternArray == null) {
                    return false;
                }
                boolean[] flags = new boolean[this.urlPatternArray.length];
                for (int i2 = 0; i2 < flags.length; i2++) {
                    flags[i2] = false;
                }
                int count = 0;
                for (int j = 0; j < that.urlPatternArray.length; j++) {
                    for (int i3 = 0; i3 < flags.length; i3++) {
                        if (!flags[i3] && (this.urlPatternArray[i3] == null || (that.urlPatternArray[j] != null && that.urlPatternArray[j].implies(this.urlPatternArray[i3])))) {
                            count++;
                            flags[i3] = true;
                            if (count == flags.length) {
                                return true;
                            }
                        }
                    }
                }
                return count == flags.length;
            }
            return true;
        }
        return false;
    }

    @Override // javax.security.jacc.URLPattern
    public String toString() {
        if (this.canonicalSpec == null) {
            if (this.urlPatternList == null) {
                this.canonicalSpec = new String(super.toString());
            } else {
                StringBuffer s = null;
                for (int i = 0; i < this.urlPatternArray.length; i++) {
                    if (this.urlPatternArray[i] != null) {
                        if (s == null) {
                            s = new StringBuffer(this.urlPatternArray[i].toString());
                        } else {
                            s.append(new StringBuffer().append(":").append(this.urlPatternArray[i].toString()).toString());
                        }
                    }
                }
                if (s == null) {
                    this.canonicalSpec = new String(super.toString());
                } else {
                    this.canonicalSpec = new String(new StringBuffer().append(super.toString()).append(":").append(s.toString()).toString());
                }
            }
        }
        return this.canonicalSpec;
    }

    private static String getFirstPattern(String urlPatternSpec) {
        if (urlPatternSpec == null) {
            throw new IllegalArgumentException("Invalid URLPatternSpec");
        }
        int colon = urlPatternSpec.indexOf(":");
        if (colon < 0) {
            return urlPatternSpec;
        }
        if (colon > 0) {
            return urlPatternSpec.substring(0, colon);
        }
        throw new IllegalArgumentException("Invalid URLPatternSpec");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void setURLPatternArray() {
        if (this.urlPatternArray == null && this.urlPatternList != null) {
            StringTokenizer tokenizer = new StringTokenizer(this.urlPatternList, ":");
            int count = tokenizer.countTokens();
            if (count == 0) {
                throw new IllegalArgumentException("colon followed by empty URLPatternList");
            }
            this.urlPatternArray = new URLPattern[count];
            int firstType = patternType();
            for (int i = 0; i < count; i++) {
                this.urlPatternArray[i] = new URLPattern(tokenizer.nextToken());
                if (this.urlPatternArray[i].implies(this)) {
                    throw new IllegalArgumentException("pattern in URLPatternList implies first pattern");
                }
                switch (firstType) {
                    case 0:
                        break;
                    case 1:
                    case 2:
                        switch (this.urlPatternArray[i].patternType()) {
                            case 2:
                                if (firstType == 2 && (super.equals(this.urlPatternArray[i]) || !super.implies(this.urlPatternArray[i]))) {
                                    throw new IllegalArgumentException("Invalid prefix pattern in URLPatternList");
                                }
                                break;
                            case 3:
                                if (!super.implies(this.urlPatternArray[i])) {
                                    throw new IllegalArgumentException("Invalid exact pattern in URLPatternList");
                                }
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid pattern type in URLPatternList");
                        }
                    case 3:
                        throw new IllegalArgumentException("invalid URLPatternSpec");
                    default:
                        throw new IllegalArgumentException("Invalid pattern type in URLPatternList");
                }
                if (super.equals(this.urlPatternArray[i])) {
                    throw new IllegalArgumentException("Invalid default pattern in URLPatternList");
                }
            }
            Arrays.sort(this.urlPatternArray);
            for (int i2 = 0; i2 < this.urlPatternArray.length; i2++) {
                if (this.urlPatternArray[i2] != null) {
                    switch (this.urlPatternArray[i2].patternType()) {
                        case 2:
                            for (int j = i2 + 1; j < this.urlPatternArray.length; j++) {
                                if (this.urlPatternArray[i2].implies(this.urlPatternArray[j])) {
                                    this.urlPatternArray[j] = null;
                                }
                            }
                            continue;
                    }
                }
            }
        }
    }
}
