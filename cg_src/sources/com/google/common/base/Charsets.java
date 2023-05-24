package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.nio.charset.Charset;
import org.apache.http.protocol.HTTP;
import org.jvnet.fastinfoset.FastInfosetSerializer;
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/Charsets.class */
public final class Charsets {
    @GwtIncompatible
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    @GwtIncompatible
    public static final Charset UTF_16BE = Charset.forName(FastInfosetSerializer.UTF_16BE);
    @GwtIncompatible
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    @GwtIncompatible
    public static final Charset UTF_16 = Charset.forName(HTTP.UTF_16);

    private Charsets() {
    }
}
