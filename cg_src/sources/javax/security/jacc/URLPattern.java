package javax.security.jacc;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/URLPattern.class */
class URLPattern implements Comparable {
    private static String DEFAULT_PATTERN = "/";
    private int patternType;
    private final String pattern;
    public static final int PT_DEFAULT = 0;
    public static final int PT_EXTENSION = 1;
    public static final int PT_PREFIX = 2;
    public static final int PT_EXACT = 3;

    public URLPattern() {
        this.patternType = -1;
        this.pattern = DEFAULT_PATTERN;
        this.patternType = 0;
    }

    public URLPattern(String p) {
        this.patternType = -1;
        if (p == null || p.length() == 0) {
            this.pattern = DEFAULT_PATTERN;
            this.patternType = 0;
            return;
        }
        this.pattern = p;
    }

    public int patternType() {
        if (this.patternType < 0) {
            if (this.pattern.startsWith("*.")) {
                this.patternType = 1;
            } else if (this.pattern.startsWith("/") && this.pattern.endsWith("/*")) {
                this.patternType = 2;
            } else if (this.pattern.equals("/")) {
                this.patternType = 0;
            } else {
                this.patternType = 3;
            }
        }
        return this.patternType;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        if (!(o instanceof URLPattern)) {
            throw new ClassCastException("argument must be URLPattern");
        }
        URLPattern p = (URLPattern) o;
        if (p == null) {
            p = new URLPattern(null);
        }
        int refPatternType = patternType();
        int result = refPatternType - p.patternType();
        if (result == 0) {
            if (refPatternType == 2 || refPatternType == 3) {
                result = getPatternDepth() - p.getPatternDepth();
                if (result == 0) {
                    result = this.pattern.compareTo(p.pattern);
                }
            } else {
                result = this.pattern.compareTo(p.pattern);
            }
        }
        if (result > 0) {
            return 1;
        }
        return result < 0 ? -1 : 0;
    }

    public boolean implies(URLPattern p) {
        if (p == null) {
            p = new URLPattern(null);
        }
        String path = p.pattern;
        String pattern = this.pattern;
        if (pattern.equals(path)) {
            return true;
        }
        if (pattern.startsWith("/") && pattern.endsWith("/*")) {
            String pattern2 = pattern.substring(0, pattern.length() - 2);
            int length = pattern2.length();
            if (length == 0) {
                return true;
            }
            return path.startsWith(pattern2) && (path.length() == length || path.substring(length).startsWith("/"));
        } else if (pattern.startsWith("*.")) {
            int slash = path.lastIndexOf(47);
            int period = path.lastIndexOf(46);
            if (slash >= 0 && period > slash && path.endsWith(pattern.substring(1))) {
                return true;
            }
            return false;
        } else if (pattern.equals("/")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof URLPattern) {
            return this.pattern.equals(((URLPattern) obj).pattern);
        }
        return false;
    }

    public String toString() {
        return this.pattern;
    }

    public int getPatternDepth() {
        int i = 0;
        while (i >= 0) {
            i = this.pattern.indexOf("/", i);
            if (i >= 0) {
                if (i == 0 && 1 != 1) {
                    throw new IllegalArgumentException("// in pattern");
                }
                i++;
            }
        }
        return 1;
    }
}
