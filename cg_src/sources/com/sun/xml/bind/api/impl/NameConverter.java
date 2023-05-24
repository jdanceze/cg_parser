package com.sun.xml.bind.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.lang.model.SourceVersion;
import org.apache.http.HttpHost;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/impl/NameConverter.class */
public interface NameConverter {
    public static final NameConverter standard = new Standard();
    public static final NameConverter jaxrpcCompatible = new Standard() { // from class: com.sun.xml.bind.api.impl.NameConverter.1
        @Override // com.sun.xml.bind.api.impl.NameUtil
        protected boolean isPunct(char c) {
            return c == '.' || c == '-' || c == ';' || c == 183 || c == 903 || c == 1757 || c == 1758;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.bind.api.impl.NameUtil
        public boolean isLetter(char c) {
            return super.isLetter(c) || c == '_';
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.bind.api.impl.NameUtil
        public int classify(char c0) {
            if (c0 == '_') {
                return 2;
            }
            return super.classify(c0);
        }
    };
    public static final NameConverter smart = new Standard() { // from class: com.sun.xml.bind.api.impl.NameConverter.2
        @Override // com.sun.xml.bind.api.impl.NameConverter.Standard, com.sun.xml.bind.api.impl.NameUtil, com.sun.xml.bind.api.impl.NameConverter
        public String toConstantName(String token) {
            String name = super.toConstantName(token);
            if (!SourceVersion.isKeyword(name)) {
                return name;
            }
            return '_' + name;
        }
    };

    String toClassName(String str);

    String toInterfaceName(String str);

    String toPropertyName(String str);

    String toConstantName(String str);

    String toVariableName(String str);

    String toPackageName(String str);

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/api/impl/NameConverter$Standard.class */
    public static class Standard extends NameUtil implements NameConverter {
        @Override // com.sun.xml.bind.api.impl.NameUtil
        public /* bridge */ /* synthetic */ String toConstantName(List list) {
            return super.toConstantName(list);
        }

        @Override // com.sun.xml.bind.api.impl.NameUtil
        public /* bridge */ /* synthetic */ List toWordList(String str) {
            return super.toWordList(str);
        }

        @Override // com.sun.xml.bind.api.impl.NameUtil
        public /* bridge */ /* synthetic */ String capitalize(String str) {
            return super.capitalize(str);
        }

        @Override // com.sun.xml.bind.api.impl.NameConverter
        public String toClassName(String s) {
            return toMixedCaseName(toWordList(s), true);
        }

        @Override // com.sun.xml.bind.api.impl.NameConverter
        public String toVariableName(String s) {
            return toMixedCaseName(toWordList(s), false);
        }

        @Override // com.sun.xml.bind.api.impl.NameConverter
        public String toInterfaceName(String token) {
            return toClassName(token);
        }

        @Override // com.sun.xml.bind.api.impl.NameConverter
        public String toPropertyName(String s) {
            String prop = toClassName(s);
            if (prop.equals("Class")) {
                prop = "Clazz";
            }
            return prop;
        }

        @Override // com.sun.xml.bind.api.impl.NameUtil, com.sun.xml.bind.api.impl.NameConverter
        public String toConstantName(String token) {
            return super.toConstantName(token);
        }

        @Override // com.sun.xml.bind.api.impl.NameConverter
        public String toPackageName(String nsUri) {
            String lastToken;
            int idx;
            int idx2 = nsUri.indexOf(58);
            String scheme = "";
            if (idx2 >= 0) {
                scheme = nsUri.substring(0, idx2);
                if (scheme.equalsIgnoreCase(HttpHost.DEFAULT_SCHEME_NAME) || scheme.equalsIgnoreCase("urn")) {
                    nsUri = nsUri.substring(idx2 + 1);
                }
            }
            ArrayList<String> tokens = tokenize(nsUri, "/: ");
            if (tokens.size() == 0) {
                return null;
            }
            if (tokens.size() > 1 && (idx = (lastToken = tokens.get(tokens.size() - 1)).lastIndexOf(46)) > 0) {
                tokens.set(tokens.size() - 1, lastToken.substring(0, idx));
            }
            String domain = tokens.get(0);
            int idx3 = domain.indexOf(58);
            if (idx3 >= 0) {
                domain = domain.substring(0, idx3);
            }
            ArrayList<String> r = reverse(tokenize(domain, scheme.equals("urn") ? ".-" : "."));
            if (r.get(r.size() - 1).equalsIgnoreCase("www")) {
                r.remove(r.size() - 1);
            }
            tokens.addAll(1, r);
            tokens.remove(0);
            for (int i = 0; i < tokens.size(); i++) {
                String token = removeIllegalIdentifierChars(tokens.get(i));
                if (SourceVersion.isKeyword(token.toLowerCase())) {
                    token = '_' + token;
                }
                tokens.set(i, token.toLowerCase());
            }
            return combine(tokens, '.');
        }

        private static String removeIllegalIdentifierChars(String token) {
            StringBuilder newToken = new StringBuilder(token.length() + 1);
            for (int i = 0; i < token.length(); i++) {
                char c = token.charAt(i);
                if (i == 0 && !Character.isJavaIdentifierStart(c)) {
                    newToken.append('_');
                }
                if (!Character.isJavaIdentifierPart(c)) {
                    newToken.append('_');
                } else {
                    newToken.append(c);
                }
            }
            return newToken.toString();
        }

        private static ArrayList<String> tokenize(String str, String sep) {
            StringTokenizer tokens = new StringTokenizer(str, sep);
            ArrayList<String> r = new ArrayList<>();
            while (tokens.hasMoreTokens()) {
                r.add(tokens.nextToken());
            }
            return r;
        }

        private static <T> ArrayList<T> reverse(List<T> a) {
            ArrayList<T> r = new ArrayList<>();
            for (int i = a.size() - 1; i >= 0; i--) {
                r.add(a.get(i));
            }
            return r;
        }

        private static String combine(List r, char sep) {
            StringBuilder buf = new StringBuilder(r.get(0).toString());
            for (int i = 1; i < r.size(); i++) {
                buf.append(sep);
                buf.append(r.get(i));
            }
            return buf.toString();
        }
    }
}
