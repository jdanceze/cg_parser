package javax.mail.search;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/StringTerm.class */
public abstract class StringTerm extends SearchTerm {
    protected String pattern;
    protected boolean ignoreCase;

    /* JADX INFO: Access modifiers changed from: protected */
    public StringTerm(String pattern) {
        this.pattern = pattern;
        this.ignoreCase = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public StringTerm(String pattern, boolean ignoreCase) {
        this.pattern = pattern;
        this.ignoreCase = ignoreCase;
    }

    public String getPattern() {
        return this.pattern;
    }

    public boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean match(String s) {
        int len = s.length() - this.pattern.length();
        for (int i = 0; i <= len; i++) {
            if (s.regionMatches(this.ignoreCase, i, this.pattern, 0, this.pattern.length())) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof StringTerm)) {
            return false;
        }
        StringTerm st = (StringTerm) obj;
        return this.ignoreCase ? st.pattern.equalsIgnoreCase(this.pattern) && st.ignoreCase == this.ignoreCase : st.pattern.equals(this.pattern) && st.ignoreCase == this.ignoreCase;
    }

    public int hashCode() {
        return this.ignoreCase ? this.pattern.hashCode() : this.pattern.hashCode() ^ (-1);
    }
}
