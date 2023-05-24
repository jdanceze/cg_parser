package soot.util;

import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:soot/util/FastStack.class */
public class FastStack<T> extends ArrayList<T> {
    private static final long serialVersionUID = 1;

    public FastStack() {
    }

    public FastStack(int initialSize) {
        super(initialSize);
    }

    public T peek() {
        return get(size() - 1);
    }

    public void push(T t) {
        add(t);
    }

    public T pop() {
        return remove(size() - 1);
    }

    public boolean empty() {
        return size() == 0;
    }
}
