package org.slf4j.event;

import org.apache.http.client.methods.HttpTrace;
/* loaded from: gencallgraphv3.jar:slf4j-api-2.0.3.jar:org/slf4j/event/Level.class */
public enum Level {
    ERROR(40, "ERROR"),
    WARN(30, "WARN"),
    INFO(20, "INFO"),
    DEBUG(10, "DEBUG"),
    TRACE(0, HttpTrace.METHOD_NAME);
    
    private final int levelInt;
    private final String levelStr;

    Level(int i, String s) {
        this.levelInt = i;
        this.levelStr = s;
    }

    public int toInt() {
        return this.levelInt;
    }

    public static Level intToLevel(int levelInt) {
        switch (levelInt) {
            case 0:
                return TRACE;
            case 10:
                return DEBUG;
            case 20:
                return INFO;
            case 30:
                return WARN;
            case 40:
                return ERROR;
            default:
                throw new IllegalArgumentException("Level integer [" + levelInt + "] not recognized.");
        }
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.levelStr;
    }
}
