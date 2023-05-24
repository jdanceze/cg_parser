package polyglot.util;
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Overrun.class */
class Overrun extends Exception {
    int amount;
    int type;
    static final int POS = 0;
    static final int WIDTH = 1;
    static final int FIN = 2;
    private static final Overrun overrun = new Overrun();

    private Overrun() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Overrun overrun(int amount, int type) {
        overrun.amount = amount;
        overrun.type = type;
        return overrun;
    }
}
