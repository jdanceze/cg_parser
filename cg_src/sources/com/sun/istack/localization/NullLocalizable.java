package com.sun.istack.localization;

import java.util.Locale;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/localization/NullLocalizable.class */
public final class NullLocalizable implements Localizable {
    private final String msg;

    public NullLocalizable(String msg) {
        if (msg == null) {
            throw new IllegalArgumentException();
        }
        this.msg = msg;
    }

    @Override // com.sun.istack.localization.Localizable
    public String getKey() {
        return Localizable.NOT_LOCALIZABLE;
    }

    @Override // com.sun.istack.localization.Localizable
    public Object[] getArguments() {
        return new Object[]{this.msg};
    }

    @Override // com.sun.istack.localization.Localizable
    public String getResourceBundleName() {
        return "";
    }

    @Override // com.sun.istack.localization.Localizable
    public ResourceBundle getResourceBundle(Locale locale) {
        return null;
    }
}
