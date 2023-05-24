package com.sun.xml.fastinfoset;

import java.util.Locale;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/CommonResourceBundle.class */
public class CommonResourceBundle extends AbstractResourceBundle {
    public static final String BASE_NAME = "com.sun.xml.fastinfoset.resources.ResourceBundle";
    private static volatile CommonResourceBundle instance = null;
    private static Locale locale = null;
    private ResourceBundle bundle;

    protected CommonResourceBundle() {
        this.bundle = null;
        this.bundle = ResourceBundle.getBundle(BASE_NAME);
    }

    protected CommonResourceBundle(Locale locale2) {
        this.bundle = null;
        this.bundle = ResourceBundle.getBundle(BASE_NAME, locale2);
    }

    public static CommonResourceBundle getInstance() {
        if (instance == null) {
            synchronized (CommonResourceBundle.class) {
                instance = new CommonResourceBundle();
                locale = parseLocale(null);
            }
        }
        return instance;
    }

    public static CommonResourceBundle getInstance(Locale locale2) {
        if (instance == null) {
            synchronized (CommonResourceBundle.class) {
                instance = new CommonResourceBundle(locale2);
            }
        } else {
            synchronized (CommonResourceBundle.class) {
                if (locale != locale2) {
                    instance = new CommonResourceBundle(locale2);
                }
            }
        }
        return instance;
    }

    @Override // com.sun.xml.fastinfoset.AbstractResourceBundle
    public ResourceBundle getBundle() {
        return this.bundle;
    }

    public ResourceBundle getBundle(Locale locale2) {
        return ResourceBundle.getBundle(BASE_NAME, locale2);
    }
}
