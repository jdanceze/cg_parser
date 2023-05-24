package android.net;

import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/UrlQuerySanitizer.class */
public class UrlQuerySanitizer {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/UrlQuerySanitizer$ValueSanitizer.class */
    public interface ValueSanitizer {
        String sanitize(String str);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/UrlQuerySanitizer$ParameterValuePair.class */
    public class ParameterValuePair {
        public String mParameter;
        public String mValue;

        public ParameterValuePair(String parameter, String value) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/UrlQuerySanitizer$IllegalCharacterValueSanitizer.class */
    public static class IllegalCharacterValueSanitizer implements ValueSanitizer {
        public static final int SPACE_OK = 1;
        public static final int OTHER_WHITESPACE_OK = 2;
        public static final int NON_7_BIT_ASCII_OK = 4;
        public static final int DQUOTE_OK = 8;
        public static final int SQUOTE_OK = 16;
        public static final int LT_OK = 32;
        public static final int GT_OK = 64;
        public static final int AMP_OK = 128;
        public static final int PCT_OK = 256;
        public static final int NUL_OK = 512;
        public static final int SCRIPT_URL_OK = 1024;
        public static final int ALL_OK = 2047;
        public static final int ALL_WHITESPACE_OK = 3;
        public static final int ALL_ILLEGAL = 0;
        public static final int ALL_BUT_NUL_LEGAL = 1535;
        public static final int ALL_BUT_WHITESPACE_LEGAL = 1532;
        public static final int URL_LEGAL = 404;
        public static final int URL_AND_SPACE_LEGAL = 405;
        public static final int AMP_LEGAL = 128;
        public static final int AMP_AND_SPACE_LEGAL = 129;
        public static final int SPACE_LEGAL = 1;
        public static final int ALL_BUT_NUL_AND_ANGLE_BRACKETS_LEGAL = 1439;

        public IllegalCharacterValueSanitizer(int flags) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.net.UrlQuerySanitizer.ValueSanitizer
        public String sanitize(String value) {
            throw new RuntimeException("Stub!");
        }
    }

    public UrlQuerySanitizer() {
        throw new RuntimeException("Stub!");
    }

    public UrlQuerySanitizer(String url) {
        throw new RuntimeException("Stub!");
    }

    public ValueSanitizer getUnregisteredParameterValueSanitizer() {
        throw new RuntimeException("Stub!");
    }

    public void setUnregisteredParameterValueSanitizer(ValueSanitizer sanitizer) {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getAllIllegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getAllButNulLegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getAllButWhitespaceLegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getUrlLegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getUrlAndSpaceLegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getAmpLegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getAmpAndSpaceLegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getSpaceLegal() {
        throw new RuntimeException("Stub!");
    }

    public static final ValueSanitizer getAllButNulAndAngleBracketsLegal() {
        throw new RuntimeException("Stub!");
    }

    public void parseUrl(String url) {
        throw new RuntimeException("Stub!");
    }

    public void parseQuery(String query) {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getParameterSet() {
        throw new RuntimeException("Stub!");
    }

    public List<ParameterValuePair> getParameterList() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasParameter(String parameter) {
        throw new RuntimeException("Stub!");
    }

    public String getValue(String parameter) {
        throw new RuntimeException("Stub!");
    }

    public void registerParameter(String parameter, ValueSanitizer valueSanitizer) {
        throw new RuntimeException("Stub!");
    }

    public void registerParameters(String[] parameters, ValueSanitizer valueSanitizer) {
        throw new RuntimeException("Stub!");
    }

    public void setAllowUnregisteredParamaters(boolean allowUnregisteredParamaters) {
        throw new RuntimeException("Stub!");
    }

    public boolean getAllowUnregisteredParamaters() {
        throw new RuntimeException("Stub!");
    }

    public void setPreferFirstRepeatedParameter(boolean preferFirstRepeatedParameter) {
        throw new RuntimeException("Stub!");
    }

    public boolean getPreferFirstRepeatedParameter() {
        throw new RuntimeException("Stub!");
    }

    protected void parseEntry(String parameter, String value) {
        throw new RuntimeException("Stub!");
    }

    protected void addSanitizedEntry(String parameter, String value) {
        throw new RuntimeException("Stub!");
    }

    public ValueSanitizer getValueSanitizer(String parameter) {
        throw new RuntimeException("Stub!");
    }

    public ValueSanitizer getEffectiveValueSanitizer(String parameter) {
        throw new RuntimeException("Stub!");
    }

    public String unescape(String string) {
        throw new RuntimeException("Stub!");
    }

    protected boolean isHexDigit(char c) {
        throw new RuntimeException("Stub!");
    }

    protected int decodeHexDigit(char c) {
        throw new RuntimeException("Stub!");
    }

    protected void clear() {
        throw new RuntimeException("Stub!");
    }
}
