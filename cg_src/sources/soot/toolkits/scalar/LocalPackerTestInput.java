package soot.toolkits.scalar;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalPackerTestInput.class */
public class LocalPackerTestInput {
    public void prefixVariableNames() {
        int a = getInt();
        int b = getInt();
        System.out.println(a + b);
        String a2 = getString();
        String b2 = getString();
        System.out.println(String.valueOf(a2) + b2);
        double a3 = getDouble();
        double b3 = getDouble();
        System.out.println(a3 + b3);
        long a4 = getLong();
        long b4 = getLong();
        System.out.println(a4 + b4);
    }

    private static int getInt() {
        return 0;
    }

    private static String getString() {
        return "";
    }

    private static double getDouble() {
        return Const.default_value_double;
    }

    private static long getLong() {
        return 1L;
    }
}
