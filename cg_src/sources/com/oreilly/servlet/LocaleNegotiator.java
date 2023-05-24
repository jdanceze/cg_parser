package com.oreilly.servlet;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/LocaleNegotiator.class */
public class LocaleNegotiator {
    private ResourceBundle chosenBundle;
    private Locale chosenLocale;
    private String chosenCharset;

    public LocaleNegotiator(String bundleName, String languages, String charsets) {
        String charset;
        Locale defaultLocale = new Locale("en", "US");
        ResourceBundle defaultBundle = null;
        try {
            defaultBundle = ResourceBundle.getBundle(bundleName, defaultLocale);
        } catch (MissingResourceException e) {
        }
        if (languages == null) {
            this.chosenLocale = defaultLocale;
            this.chosenCharset = "ISO-8859-1";
            this.chosenBundle = defaultBundle;
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(languages, ",");
        while (tokenizer.hasMoreTokens()) {
            String lang = tokenizer.nextToken();
            Locale loc = getLocaleForLanguage(lang);
            ResourceBundle bundle = getBundleNoFallback(bundleName, loc);
            if (bundle != null && (charset = getCharsetForLocale(loc, charsets)) != null) {
                this.chosenLocale = loc;
                this.chosenBundle = bundle;
                this.chosenCharset = charset;
                return;
            }
        }
        this.chosenLocale = defaultLocale;
        this.chosenCharset = "ISO-8859-1";
        this.chosenBundle = defaultBundle;
    }

    public ResourceBundle getBundle() {
        return this.chosenBundle;
    }

    public Locale getLocale() {
        return this.chosenLocale;
    }

    public String getCharset() {
        return this.chosenCharset;
    }

    private Locale getLocaleForLanguage(String lang) {
        Locale loc;
        int semi = lang.indexOf(59);
        if (semi != -1) {
            lang = lang.substring(0, semi);
        }
        String lang2 = lang.trim();
        int dash = lang2.indexOf(45);
        if (dash == -1) {
            loc = new Locale(lang2, "");
        } else {
            loc = new Locale(lang2.substring(0, dash), lang2.substring(dash + 1));
        }
        return loc;
    }

    private ResourceBundle getBundleNoFallback(String bundleName, Locale loc) {
        ResourceBundle fallback = null;
        try {
            fallback = ResourceBundle.getBundle(bundleName, new Locale("bogus", ""));
        } catch (MissingResourceException e) {
        }
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, loc);
            if (bundle != fallback) {
                return bundle;
            }
            if (bundle == fallback) {
                if (loc.getLanguage().equals(Locale.getDefault().getLanguage())) {
                    return bundle;
                }
            }
            return null;
        } catch (MissingResourceException e2) {
            return null;
        }
    }

    protected String getCharsetForLocale(Locale loc, String charsets) {
        return LocaleToCharsetMap.getCharset(loc);
    }
}
