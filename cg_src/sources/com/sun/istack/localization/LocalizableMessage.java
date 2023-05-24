package com.sun.istack.localization;

import com.sun.istack.localization.LocalizableMessageFactory;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/localization/LocalizableMessage.class */
public final class LocalizableMessage implements Localizable {
    private final String _bundlename;
    private final LocalizableMessageFactory.ResourceBundleSupplier _rbSupplier;
    private final String _key;
    private final Object[] _args;

    @Deprecated
    public LocalizableMessage(String bundlename, String key, Object... args) {
        this(bundlename, null, key, args);
    }

    public LocalizableMessage(String bundlename, LocalizableMessageFactory.ResourceBundleSupplier rbSupplier, String key, Object... args) {
        this._bundlename = bundlename;
        this._rbSupplier = rbSupplier;
        this._key = key;
        this._args = args == null ? new Object[0] : args;
    }

    @Override // com.sun.istack.localization.Localizable
    public String getKey() {
        return this._key;
    }

    @Override // com.sun.istack.localization.Localizable
    public Object[] getArguments() {
        return Arrays.copyOf(this._args, this._args.length);
    }

    @Override // com.sun.istack.localization.Localizable
    public String getResourceBundleName() {
        return this._bundlename;
    }

    @Override // com.sun.istack.localization.Localizable
    public ResourceBundle getResourceBundle(Locale locale) {
        if (this._rbSupplier == null) {
            return null;
        }
        return this._rbSupplier.getResourceBundle(locale);
    }
}
