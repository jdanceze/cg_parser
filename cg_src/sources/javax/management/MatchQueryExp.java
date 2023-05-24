package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MatchQueryExp.class */
class MatchQueryExp extends QueryEval implements QueryExp {
    private static final long serialVersionUID = -7156603696948215014L;
    private AttributeValueExp exp;
    private String pattern;

    public MatchQueryExp() {
    }

    public MatchQueryExp(AttributeValueExp attributeValueExp, StringValueExp stringValueExp) {
        this.exp = attributeValueExp;
        this.pattern = stringValueExp.getValue();
    }

    public AttributeValueExp getAttribute() {
        return this.exp;
    }

    public String getPattern() {
        return this.pattern;
    }

    @Override // javax.management.QueryExp
    public boolean apply(ObjectName objectName) throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException {
        ValueExp apply = this.exp.apply(objectName);
        if (!(apply instanceof StringValueExp)) {
            return false;
        }
        return wildmatch(((StringValueExp) apply).getValue(), this.pattern);
    }

    public String toString() {
        return new StringBuffer().append(this.exp).append(" like ").append(new StringValueExp(likeTranslate(this.pattern))).toString();
    }

    private static String likeTranslate(String str) {
        return str.replace('?', '_').replace('*', '%');
    }

    private static boolean wildmatch(String str, String str2) {
        char charAt;
        int i = 0;
        int i2 = 0;
        int length = str.length();
        int length2 = str2.length();
        while (i2 < length2) {
            int i3 = i2;
            i2++;
            char charAt2 = str2.charAt(i3);
            if (charAt2 == '?') {
                i++;
                if (i > length) {
                    return false;
                }
            } else if (charAt2 == '[') {
                boolean z = true;
                boolean z2 = false;
                if (str2.charAt(i2) == '!') {
                    z = false;
                    i2++;
                }
                while (true) {
                    i2++;
                    if (i2 >= length2 || (charAt = str2.charAt(i2)) == ']') {
                        break;
                    } else if (str2.charAt(i2) == '-' && i2 + 1 < length2) {
                        if (str.charAt(i) >= charAt && str.charAt(i) <= str2.charAt(i2 + 1)) {
                            z2 = true;
                        }
                        i2++;
                    } else if (charAt == str.charAt(i)) {
                        z2 = true;
                    }
                }
                if (i2 >= length2 || z != z2) {
                    return false;
                }
                i2++;
                i++;
            } else if (charAt2 == '*') {
                if (i2 >= length2) {
                    return true;
                }
                while (!wildmatch(str.substring(i), str2.substring(i2))) {
                    i++;
                    if (i >= length) {
                        return false;
                    }
                }
                return true;
            } else if (charAt2 == '\\') {
                if (i2 >= length2) {
                    return false;
                }
                i2++;
                int i4 = i;
                i++;
                if (str2.charAt(i2) != str.charAt(i4)) {
                    return false;
                }
            } else if (i >= length) {
                return false;
            } else {
                int i5 = i;
                i++;
                if (charAt2 != str.charAt(i5)) {
                    return false;
                }
            }
        }
        return i == length;
    }
}
