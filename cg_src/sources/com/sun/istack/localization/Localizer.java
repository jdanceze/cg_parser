package com.sun.istack.localization;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/localization/Localizer.class */
public class Localizer {
    private final Locale _locale;
    private final HashMap<String, ResourceBundle> _resourceBundles;

    public Localizer() {
        this(Locale.getDefault());
    }

    public Localizer(Locale l) {
        this._locale = l;
        this._resourceBundles = new HashMap<>();
    }

    public Locale getLocale() {
        return this._locale;
    }

    public String localize(Localizable l) {
        String msg;
        String key = l.getKey();
        if (key == Localizable.NOT_LOCALIZABLE) {
            return (String) l.getArguments()[0];
        }
        String bundlename = l.getResourceBundleName();
        try {
            ResourceBundle bundle = this._resourceBundles.get(bundlename);
            if (bundle == null) {
                bundle = l.getResourceBundle(this._locale);
                if (bundle != null) {
                    this._resourceBundles.put(bundlename, bundle);
                }
            }
            if (bundle == null) {
                try {
                    bundle = ResourceBundle.getBundle(bundlename, this._locale);
                } catch (MissingResourceException e) {
                    int i = bundlename.lastIndexOf(46);
                    if (i != -1) {
                        String alternateBundleName = bundlename.substring(i + 1);
                        try {
                            bundle = ResourceBundle.getBundle(alternateBundleName, this._locale);
                        } catch (MissingResourceException e2) {
                            try {
                                bundle = ResourceBundle.getBundle(bundlename, this._locale, Thread.currentThread().getContextClassLoader());
                            } catch (MissingResourceException e3) {
                                return getDefaultMessage(l);
                            }
                        }
                    }
                }
                this._resourceBundles.put(bundlename, bundle);
            }
            if (bundle == null) {
                return getDefaultMessage(l);
            }
            if (key == null) {
                key = "undefined";
            }
            try {
                msg = bundle.getString(key);
            } catch (MissingResourceException e4) {
                msg = bundle.getString("undefined");
            }
            Object[] args = l.getArguments();
            for (int i2 = 0; i2 < args.length; i2++) {
                if (args[i2] instanceof Localizable) {
                    args[i2] = localize((Localizable) args[i2]);
                }
            }
            String message = MessageFormat.format(msg, args);
            return message;
        } catch (MissingResourceException e5) {
            return getDefaultMessage(l);
        }
    }

    private String getDefaultMessage(Localizable l) {
        String key = l.getKey();
        Object[] args = l.getArguments();
        StringBuilder sb = new StringBuilder();
        sb.append("[failed to localize] ");
        sb.append(key);
        if (args != null) {
            sb.append('(');
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(String.valueOf(args[i]));
            }
            sb.append(')');
        }
        return sb.toString();
    }
}
