package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
@Beta
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/io/Flushables.class */
public final class Flushables {
    private static final Logger logger = Logger.getLogger(Flushables.class.getName());

    private Flushables() {
    }

    public static void flush(Flushable flushable, boolean swallowIOException) throws IOException {
        try {
            flushable.flush();
        } catch (IOException e) {
            if (swallowIOException) {
                logger.log(Level.WARNING, "IOException thrown while flushing Flushable.", (Throwable) e);
                return;
            }
            throw e;
        }
    }

    public static void flushQuietly(Flushable flushable) {
        try {
            flush(flushable, true);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException should not have been thrown.", (Throwable) e);
        }
    }
}
