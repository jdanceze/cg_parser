package jasmin;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jasmin/ScannerUtils.class */
abstract class ScannerUtils {
    ScannerUtils() {
    }

    public static Number convertInt(String str, int radix) throws NumberFormatException {
        boolean forceLong = false;
        if (str.endsWith("L")) {
            forceLong = true;
            str = str.substring(0, str.length() - 1);
        }
        long x = Long.parseLong(str, radix);
        if (x <= 2147483647L && x >= -2147483648L && !forceLong) {
            return new Integer((int) x);
        }
        return new Long(x);
    }

    public static Number convertNumber(String str) throws NumberFormatException {
        if (str.startsWith("0x")) {
            return convertInt(str.substring(2), 16);
        }
        if (str.indexOf(46) != -1) {
            boolean isFloat = false;
            if (str.endsWith("F")) {
                isFloat = true;
                str = str.substring(0, str.length() - 1);
            }
            double x = new Double(str).doubleValue();
            if (isFloat) {
                return new Float((float) x);
            }
            return new Double(x);
        }
        return convertInt(str, 10);
    }

    public static String convertDots(String orig_name) {
        return convertChars(orig_name, ".", '/');
    }

    public static String convertChars(String orig_name, String chars, char toChar) {
        StringBuffer tmp = new StringBuffer(orig_name);
        for (int i = 0; i < tmp.length(); i++) {
            if (chars.indexOf(tmp.charAt(i)) != -1) {
                tmp.setCharAt(i, toChar);
            }
        }
        return new String(tmp);
    }

    public static String[] splitClassMethodSignature(String name) {
        String[] result = new String[3];
        int pos = 0;
        int sigpos = 0;
        int i = 0;
        while (true) {
            if (i >= name.length()) {
                break;
            }
            char c = name.charAt(i);
            if (c == '.' || c == '/') {
                pos = i;
            } else if (c == '(') {
                sigpos = i;
                break;
            }
            i++;
        }
        result[0] = convertDots(name.substring(0, pos));
        result[1] = name.substring(pos + 1, sigpos);
        result[2] = convertDots(name.substring(sigpos));
        return result;
    }

    public static String[] splitClassField(String name) {
        String[] result = new String[2];
        int pos = -1;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '.' || c == '/') {
                pos = i;
            }
        }
        if (pos == -1) {
            result[0] = null;
            result[1] = name;
        } else {
            result[0] = convertDots(name.substring(0, pos));
            result[1] = name.substring(pos + 1);
        }
        return result;
    }

    public static String[] splitMethodSignature(String name) {
        String[] result = new String[2];
        int sigpos = 0;
        int i = 0;
        while (true) {
            if (i >= name.length()) {
                break;
            }
            char c = name.charAt(i);
            if (c == '(') {
                sigpos = i;
                break;
            }
            i++;
        }
        result[0] = name.substring(0, sigpos);
        result[1] = convertDots(name.substring(sigpos));
        return result;
    }
}
