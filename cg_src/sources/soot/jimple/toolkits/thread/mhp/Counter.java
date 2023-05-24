package soot.jimple.toolkits.thread.mhp;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/Counter.class */
public class Counter {
    private static int tagNo = 0;
    private static int objNo = 0;
    private static int threadNo = 0;

    Counter() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getTagNo() {
        int i = tagNo;
        tagNo = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getObjNo() {
        int i = objNo;
        objNo = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int getThreadNo() {
        int i = threadNo;
        threadNo = i + 1;
        return i;
    }
}
