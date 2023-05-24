package soot.util;

import soot.Body;
import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/util/LocalBitSetPacker.class */
public class LocalBitSetPacker {
    private final Body body;
    private Local[] locals;
    private int[] oldNumbers;

    public LocalBitSetPacker(Body body) {
        this.body = body;
    }

    public void pack() {
        int n = this.body.getLocalCount();
        this.locals = new Local[n];
        this.oldNumbers = new int[n];
        int n2 = 0;
        for (Local local : this.body.getLocals()) {
            this.locals[n2] = local;
            this.oldNumbers[n2] = local.getNumber();
            int i = n2;
            n2++;
            local.setNumber(i);
        }
    }

    public void unpack() {
        for (int i = 0; i < this.locals.length; i++) {
            this.locals[i].setNumber(this.oldNumbers[i]);
        }
        this.locals = null;
        this.oldNumbers = null;
    }

    public int getLocalCount() {
        if (this.locals == null) {
            return 0;
        }
        return this.locals.length;
    }
}
