package org.jvnet.staxex;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.activation.DataHandler;
import javax.activation.DataSource;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/StreamingDataHandler.class */
public abstract class StreamingDataHandler extends DataHandler implements Closeable {
    private String hrefCid;

    public abstract InputStream readOnce() throws IOException;

    public abstract void moveTo(File file) throws IOException;

    public abstract void close() throws IOException;

    public StreamingDataHandler(Object o, String s) {
        super(o, s);
    }

    public StreamingDataHandler(URL url) {
        super(url);
    }

    public StreamingDataHandler(DataSource dataSource) {
        super(dataSource);
    }

    public String getHrefCid() {
        return this.hrefCid;
    }

    public void setHrefCid(String cid) {
        this.hrefCid = cid;
    }
}
