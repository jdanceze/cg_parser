package net.bytebuddy.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/CompoundList.class */
public class CompoundList {
    private CompoundList() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    public static <S> List<S> of(S left, List<? extends S> right) {
        if (right.isEmpty()) {
            return Collections.singletonList(left);
        }
        List<S> list = new ArrayList<>(1 + right.size());
        list.add(left);
        list.addAll(right);
        return list;
    }

    public static <S> List<S> of(List<? extends S> left, S right) {
        if (left.isEmpty()) {
            return Collections.singletonList(right);
        }
        List<S> list = new ArrayList<>(left.size() + 1);
        list.addAll(left);
        list.add(right);
        return list;
    }

    public static <S> List<S> of(List<? extends S> left, List<? extends S> right) {
        List<S> list = new ArrayList<>(left.size() + right.size());
        list.addAll(left);
        list.addAll(right);
        return list;
    }

    public static <S> List<S> of(List<? extends S> left, List<? extends S> middle, List<? extends S> right) {
        List<S> list = new ArrayList<>(left.size() + middle.size() + right.size());
        list.addAll(left);
        list.addAll(middle);
        list.addAll(right);
        return list;
    }
}
