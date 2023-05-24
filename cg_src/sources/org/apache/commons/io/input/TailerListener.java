package org.apache.commons.io.input;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/TailerListener.class */
public interface TailerListener {
    void init(Tailer tailer);

    void fileNotFound();

    void fileRotated();

    void handle(String str);

    void handle(Exception exc);
}
