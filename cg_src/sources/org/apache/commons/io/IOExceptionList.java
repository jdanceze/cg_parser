package org.apache.commons.io;

import java.io.IOException;
import java.util.List;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/IOExceptionList.class */
public class IOExceptionList extends IOException {
    private static final long serialVersionUID = 1;
    private final List<? extends Throwable> causeList;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public IOExceptionList(java.util.List<? extends java.lang.Throwable> r8) {
        /*
            r7 = this;
            r0 = r7
            java.lang.String r1 = "%,d exceptions: %s"
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = r2
            r4 = 0
            r5 = r8
            if (r5 != 0) goto L11
            r5 = 0
            goto L17
        L11:
            r5 = r8
            int r5 = r5.size()
        L17:
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r3[r4] = r5
            r3 = r2
            r4 = 1
            r5 = r8
            r3[r4] = r5
            java.lang.String r1 = java.lang.String.format(r1, r2)
            r2 = r8
            if (r2 != 0) goto L2a
            r2 = 0
            goto L34
        L2a:
            r2 = r8
            r3 = 0
            java.lang.Object r2 = r2.get(r3)
            java.lang.Throwable r2 = (java.lang.Throwable) r2
        L34:
            r0.<init>(r1, r2)
            r0 = r7
            r1 = r8
            if (r1 != 0) goto L42
            java.util.List r1 = java.util.Collections.emptyList()
            goto L43
        L42:
            r1 = r8
        L43:
            r0.causeList = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.IOExceptionList.<init>(java.util.List):void");
    }

    public <T extends Throwable> List<T> getCauseList() {
        return (List<T>) this.causeList;
    }

    public <T extends Throwable> T getCause(int index) {
        return (T) this.causeList.get(index);
    }

    public <T extends Throwable> T getCause(int index, Class<T> clazz) {
        return (T) this.causeList.get(index);
    }

    public <T extends Throwable> List<T> getCauseList(Class<T> clazz) {
        return (List<T>) this.causeList;
    }
}
