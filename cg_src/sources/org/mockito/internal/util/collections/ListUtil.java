package org.mockito.internal.util.collections;

import java.util.Collection;
import java.util.LinkedList;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/ListUtil.class */
public class ListUtil {

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/ListUtil$Converter.class */
    public interface Converter<From, To> {
        To convert(From from);
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/ListUtil$Filter.class */
    public interface Filter<T> {
        boolean isOut(T t);
    }

    public static <T> LinkedList<T> filter(Collection<T> collection, Filter<T> filter) {
        LinkedList<T> filtered = new LinkedList<>();
        for (T t : collection) {
            if (!filter.isOut(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public static <From, To> LinkedList<To> convert(Collection<From> collection, Converter<From, To> converter) {
        LinkedList<To> converted = new LinkedList<>();
        for (From f : collection) {
            converted.add(converter.convert(f));
        }
        return converted;
    }
}
