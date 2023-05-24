package org.apache.tools.ant.util;

import java.util.Objects;
import java.util.stream.Stream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ChainedMapper.class */
public class ChainedMapper extends ContainerMapper {
    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        String[] result = (String[]) getMappers().stream().filter((v0) -> {
            return Objects.nonNull(v0);
        }).reduce(new String[]{sourceFileName}, i, m -> {
            Stream of = Stream.of((Object[]) i);
            Objects.requireNonNull(m);
            return (String[]) of.map(this::mapFileName).filter((v0) -> {
                return Objects.nonNull(v0);
            }).flatMap((v0) -> {
                return Stream.of(v0);
            }).toArray(x$0 -> {
                return new String[x$0];
            });
        }, i2, o -> {
            return o;
        });
        if (result == null || result.length == 0) {
            return null;
        }
        return result;
    }
}
