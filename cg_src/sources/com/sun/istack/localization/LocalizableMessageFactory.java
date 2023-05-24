package com.sun.istack.localization;

import java.util.Locale;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/localization/LocalizableMessageFactory.class */
public class LocalizableMessageFactory {
    private final String _bundlename;
    private final ResourceBundleSupplier _rbSupplier;

    /* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/localization/LocalizableMessageFactory$ResourceBundleSupplier.class */
    public interface ResourceBundleSupplier {
        ResourceBundle getResourceBundle(Locale locale);
    }

    @Deprecated
    public LocalizableMessageFactory(String bundlename) {
        this._bundlename = bundlename;
        this._rbSupplier = null;
    }

    public LocalizableMessageFactory(String bundlename, ResourceBundleSupplier rbSupplier) {
        this._bundlename = bundlename;
        this._rbSupplier = rbSupplier;
    }

    public Localizable getMessage(String key, Object... args) {
        return new LocalizableMessage(this._bundlename, this._rbSupplier, key, args);
    }
}
