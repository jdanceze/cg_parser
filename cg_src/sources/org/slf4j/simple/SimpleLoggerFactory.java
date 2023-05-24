package org.slf4j.simple;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
/* loaded from: gencallgraphv3.jar:slf4j-simple-2.0.3.jar:org/slf4j/simple/SimpleLoggerFactory.class */
public class SimpleLoggerFactory implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap();

    public SimpleLoggerFactory() {
        SimpleLogger.lazyInit();
    }

    @Override // org.slf4j.ILoggerFactory
    public Logger getLogger(String name) {
        Logger simpleLogger = this.loggerMap.get(name);
        if (simpleLogger != null) {
            return simpleLogger;
        }
        Logger newInstance = new SimpleLogger(name);
        Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
        return oldInstance == null ? newInstance : oldInstance;
    }

    void reset() {
        this.loggerMap.clear();
    }
}
