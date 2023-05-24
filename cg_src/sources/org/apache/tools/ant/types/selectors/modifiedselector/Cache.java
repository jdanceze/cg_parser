package org.apache.tools.ant.types.selectors.modifiedselector;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/Cache.class */
public interface Cache {
    boolean isValid();

    void delete();

    void load();

    void save();

    Object get(Object obj);

    void put(Object obj, Object obj2);

    Iterator<String> iterator();
}
