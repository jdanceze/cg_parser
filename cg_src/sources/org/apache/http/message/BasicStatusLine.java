package org.apache.http.message;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.util.CharArrayBuffer;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/message/BasicStatusLine.class */
public class BasicStatusLine implements StatusLine, Cloneable {
    private final ProtocolVersion protoVersion;
    private final int statusCode;
    private final String reasonPhrase;

    public BasicStatusLine(ProtocolVersion version, int statusCode, String reasonPhrase) {
        if (version == null) {
            throw new IllegalArgumentException("Protocol version may not be null.");
        }
        if (statusCode < 0) {
            throw new IllegalArgumentException("Status code may not be negative.");
        }
        this.protoVersion = version;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    @Override // org.apache.http.StatusLine
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override // org.apache.http.StatusLine
    public ProtocolVersion getProtocolVersion() {
        return this.protoVersion;
    }

    @Override // org.apache.http.StatusLine
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String toString() {
        return BasicLineFormatter.DEFAULT.formatStatusLine((CharArrayBuffer) null, this).toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
