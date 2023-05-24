package org.apache.tools.ant.util;

import java.util.Objects;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/FirstMatchMapper.class */
public class FirstMatchMapper extends ContainerMapper {
    @Override // org.apache.tools.ant.util.FileNameMapper
    public String[] mapFileName(String sourceFileName) {
        return (String[]) getMappers().stream().filter((v0) -> {
            return Objects.nonNull(v0);
        }).map(m -> {
            return m.mapFileName(sourceFileName);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).findFirst().orElse(null);
    }
}
