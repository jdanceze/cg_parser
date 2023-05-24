package org.apache.tools.ant.types;

import java.util.stream.Stream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/ResourceCollection.class */
public interface ResourceCollection extends Iterable<Resource> {
    int size();

    boolean isFilesystemOnly();

    default Stream<? extends Resource> stream() {
        Stream.Builder<Resource> b = Stream.builder();
        forEach(b);
        return b.build();
    }

    default boolean isEmpty() {
        return size() == 0;
    }
}
