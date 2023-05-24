package org.slf4j.spi;

import java.util.Deque;
import java.util.Map;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:slf4j-api-1.7.5.jar:org/slf4j/spi/MDCAdapter.class
 */
/* loaded from: gencallgraphv3.jar:slf4j-api-2.0.3.jar:org/slf4j/spi/MDCAdapter.class */
public interface MDCAdapter {
    void put(String str, String str2);

    String get(String str);

    void remove(String str);

    void clear();

    Map<String, String> getCopyOfContextMap();

    void setContextMap(Map<String, String> map);

    void pushByKey(String str, String str2);

    String popByKey(String str);

    Deque<String> getCopyOfDequeByKey(String str);

    void clearDequeByKey(String str);
}
