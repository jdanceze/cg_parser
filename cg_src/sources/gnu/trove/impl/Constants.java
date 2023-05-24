package gnu.trove.impl;

import com.sun.istack.localization.Localizable;
import javax.resource.spi.work.WorkException;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/Constants.class */
public class Constants {
    private static final boolean VERBOSE;
    public static final int DEFAULT_CAPACITY = 10;
    public static final float DEFAULT_LOAD_FACTOR = 0.5f;
    public static final byte DEFAULT_BYTE_NO_ENTRY_VALUE;
    public static final short DEFAULT_SHORT_NO_ENTRY_VALUE;
    public static final char DEFAULT_CHAR_NO_ENTRY_VALUE;
    public static final int DEFAULT_INT_NO_ENTRY_VALUE;
    public static final long DEFAULT_LONG_NO_ENTRY_VALUE;
    public static final float DEFAULT_FLOAT_NO_ENTRY_VALUE;
    public static final double DEFAULT_DOUBLE_NO_ENTRY_VALUE;

    static {
        byte value;
        short value2;
        char value3;
        int value4;
        long value5;
        float value6;
        double value7;
        VERBOSE = System.getProperty("gnu.trove.verbose", null) != null;
        String property = System.getProperty("gnu.trove.no_entry.byte", WorkException.UNDEFINED);
        if ("MAX_VALUE".equalsIgnoreCase(property)) {
            value = Byte.MAX_VALUE;
        } else {
            value = "MIN_VALUE".equalsIgnoreCase(property) ? Byte.MIN_VALUE : Byte.valueOf(property).byteValue();
        }
        if (value > Byte.MAX_VALUE) {
            value = Byte.MAX_VALUE;
        } else if (value < Byte.MIN_VALUE) {
            value = Byte.MIN_VALUE;
        }
        DEFAULT_BYTE_NO_ENTRY_VALUE = value;
        if (VERBOSE) {
            System.out.println("DEFAULT_BYTE_NO_ENTRY_VALUE: " + ((int) DEFAULT_BYTE_NO_ENTRY_VALUE));
        }
        String property2 = System.getProperty("gnu.trove.no_entry.short", WorkException.UNDEFINED);
        if ("MAX_VALUE".equalsIgnoreCase(property2)) {
            value2 = Short.MAX_VALUE;
        } else {
            value2 = "MIN_VALUE".equalsIgnoreCase(property2) ? Short.MIN_VALUE : Short.valueOf(property2).shortValue();
        }
        if (value2 > Short.MAX_VALUE) {
            value2 = Short.MAX_VALUE;
        } else if (value2 < Short.MIN_VALUE) {
            value2 = Short.MIN_VALUE;
        }
        DEFAULT_SHORT_NO_ENTRY_VALUE = value2;
        if (VERBOSE) {
            System.out.println("DEFAULT_SHORT_NO_ENTRY_VALUE: " + ((int) DEFAULT_SHORT_NO_ENTRY_VALUE));
        }
        String property3 = System.getProperty("gnu.trove.no_entry.char", Localizable.NOT_LOCALIZABLE);
        if ("MAX_VALUE".equalsIgnoreCase(property3)) {
            value3 = 65535;
        } else {
            value3 = "MIN_VALUE".equalsIgnoreCase(property3) ? (char) 0 : property3.toCharArray()[0];
        }
        if (value3 > 65535) {
            value3 = 65535;
        } else if (value3 < 0) {
            value3 = 0;
        }
        DEFAULT_CHAR_NO_ENTRY_VALUE = value3;
        if (VERBOSE) {
            System.out.println("DEFAULT_CHAR_NO_ENTRY_VALUE: " + Integer.valueOf(value3));
        }
        String property4 = System.getProperty("gnu.trove.no_entry.int", WorkException.UNDEFINED);
        if ("MAX_VALUE".equalsIgnoreCase(property4)) {
            value4 = Integer.MAX_VALUE;
        } else {
            value4 = "MIN_VALUE".equalsIgnoreCase(property4) ? Integer.MIN_VALUE : Integer.valueOf(property4).intValue();
        }
        DEFAULT_INT_NO_ENTRY_VALUE = value4;
        if (VERBOSE) {
            System.out.println("DEFAULT_INT_NO_ENTRY_VALUE: " + DEFAULT_INT_NO_ENTRY_VALUE);
        }
        String property5 = System.getProperty("gnu.trove.no_entry.long", WorkException.UNDEFINED);
        if ("MAX_VALUE".equalsIgnoreCase(property5)) {
            value5 = Long.MAX_VALUE;
        } else {
            value5 = "MIN_VALUE".equalsIgnoreCase(property5) ? Long.MIN_VALUE : Long.valueOf(property5).longValue();
        }
        DEFAULT_LONG_NO_ENTRY_VALUE = value5;
        if (VERBOSE) {
            System.out.println("DEFAULT_LONG_NO_ENTRY_VALUE: " + DEFAULT_LONG_NO_ENTRY_VALUE);
        }
        String property6 = System.getProperty("gnu.trove.no_entry.float", WorkException.UNDEFINED);
        if ("MAX_VALUE".equalsIgnoreCase(property6)) {
            value6 = Float.MAX_VALUE;
        } else if ("MIN_VALUE".equalsIgnoreCase(property6)) {
            value6 = Float.MIN_VALUE;
        } else if ("MIN_NORMAL".equalsIgnoreCase(property6)) {
            value6 = Float.MIN_NORMAL;
        } else if ("NEGATIVE_INFINITY".equalsIgnoreCase(property6)) {
            value6 = Float.NEGATIVE_INFINITY;
        } else {
            value6 = "POSITIVE_INFINITY".equalsIgnoreCase(property6) ? Float.POSITIVE_INFINITY : Float.valueOf(property6).floatValue();
        }
        DEFAULT_FLOAT_NO_ENTRY_VALUE = value6;
        if (VERBOSE) {
            System.out.println("DEFAULT_FLOAT_NO_ENTRY_VALUE: " + DEFAULT_FLOAT_NO_ENTRY_VALUE);
        }
        String property7 = System.getProperty("gnu.trove.no_entry.double", WorkException.UNDEFINED);
        if ("MAX_VALUE".equalsIgnoreCase(property7)) {
            value7 = Double.MAX_VALUE;
        } else if ("MIN_VALUE".equalsIgnoreCase(property7)) {
            value7 = Double.MIN_VALUE;
        } else if ("MIN_NORMAL".equalsIgnoreCase(property7)) {
            value7 = Double.MIN_NORMAL;
        } else if ("NEGATIVE_INFINITY".equalsIgnoreCase(property7)) {
            value7 = Double.NEGATIVE_INFINITY;
        } else {
            value7 = "POSITIVE_INFINITY".equalsIgnoreCase(property7) ? Double.POSITIVE_INFINITY : Double.valueOf(property7).doubleValue();
        }
        DEFAULT_DOUBLE_NO_ENTRY_VALUE = value7;
        if (VERBOSE) {
            System.out.println("DEFAULT_DOUBLE_NO_ENTRY_VALUE: " + DEFAULT_DOUBLE_NO_ENTRY_VALUE);
        }
    }
}
