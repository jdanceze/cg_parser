package soot.asm;

import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:soot/asm/LocalNaming.class */
public class LocalNaming {
    public void localNaming(String alpha, Integer beta, byte[] gamma, StringBuilder delta) {
        gamma[0] = 23;
        delta.append(alpha);
        delta.append(beta);
        long iota = 90 * 2;
        Long eta = Long.valueOf(iota);
        long theta = eta.longValue();
        delta.append(90L);
        delta.append(theta);
        PrintStream omega = System.out;
        omega.println(delta);
    }

    /* loaded from: gencallgraphv3.jar:soot/asm/LocalNaming$Config.class */
    static class Config {
        Config() {
        }

        static int getD() {
            return 0;
        }

        static int getF() {
            return 1;
        }
    }

    public void test() {
        int d = Config.getD();
        int f = Config.getF();
        int[] arr = {d, f};
        System.out.println(arr);
    }
}
