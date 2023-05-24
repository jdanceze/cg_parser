package soot.util.queue;
/* loaded from: gencallgraphv3.jar:soot/util/queue/ChunkedQueue.class */
public class ChunkedQueue<E> {
    protected static final Object NULL_CONST = new Object();
    protected static final Object DELETED_CONST = new Object();
    protected static final int LENGTH = 60;
    protected Object[] q = new Object[60];
    protected int index = 0;

    /* JADX WARN: Multi-variable type inference failed */
    public void add(E o) {
        if (o == null) {
            o = NULL_CONST;
        }
        if (this.index == 59) {
            Object[] temp = new Object[60];
            this.q[this.index] = temp;
            this.q = temp;
            this.index = 0;
        }
        Object[] objArr = this.q;
        int i = this.index;
        this.index = i + 1;
        objArr[i] = o;
    }

    public QueueReader<E> reader() {
        return new QueueReader<>(this.q, this.index);
    }

    public String toString() {
        Object curObj;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean isFirst = true;
        int idx = this.index;
        Object[] curArray = this.q;
        while (idx < curArray.length && (curObj = curArray[idx]) != null) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            if (curObj instanceof Object[]) {
                curArray = (Object[]) curObj;
                idx = 0;
            } else {
                sb.append(curObj.toString());
                idx++;
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
