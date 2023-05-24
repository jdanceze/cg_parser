package ppg.util;
/* compiled from: CodeWriter.java */
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/util/Overrun.class */
class Overrun extends Exception {
    int amount;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Overrun(int amount_) {
        this.amount = amount_;
    }
}
