package org.apache.tools.ant.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.resource.spi.work.WorkManager;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/StreamUtils.class */
public class StreamUtils {
    public static <T> Stream<T> enumerationAsStream(final Enumeration<T> e) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<T>(WorkManager.INDEFINITE, 16) { // from class: org.apache.tools.ant.util.StreamUtils.1
            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super T> action) {
                if (e.hasMoreElements()) {
                    action.accept(e.nextElement());
                    return true;
                }
                return false;
            }

            @Override // java.util.Spliterator
            public void forEachRemaining(Consumer<? super T> action) {
                while (e.hasMoreElements()) {
                    action.accept(e.nextElement());
                }
            }
        }, false);
    }

    public static <T> Stream<T> iteratorAsStream(Iterator<T> i) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(i, 16), false);
    }
}
