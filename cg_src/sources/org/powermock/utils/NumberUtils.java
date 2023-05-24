package org.powermock.utils;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/utils/NumberUtils.class */
public class NumberUtils {
    public static float toFloat(String str, float defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
