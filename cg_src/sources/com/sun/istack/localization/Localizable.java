package com.sun.istack.localization;

import java.util.Locale;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:istack-commons-runtime-3.0.7.jar:com/sun/istack/localization/Localizable.class */
public interface Localizable {
    public static final String NOT_LOCALIZABLE = "��";

    String getKey();

    Object[] getArguments();

    String getResourceBundleName();

    ResourceBundle getResourceBundle(Locale locale);
}
