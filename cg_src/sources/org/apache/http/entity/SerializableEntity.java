package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/entity/SerializableEntity.class */
public class SerializableEntity extends AbstractHttpEntity {
    private byte[] objSer;
    private Serializable objRef;

    public SerializableEntity(Serializable ser, boolean bufferize) throws IOException {
        if (ser == null) {
            throw new IllegalArgumentException("Source object may not be null");
        }
        if (bufferize) {
            createBytes(ser);
        } else {
            this.objRef = ser;
        }
    }

    private void createBytes(Serializable ser) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(ser);
        out.flush();
        this.objSer = baos.toByteArray();
    }

    @Override // org.apache.http.HttpEntity
    public InputStream getContent() throws IOException, IllegalStateException {
        if (this.objSer == null) {
            createBytes(this.objRef);
        }
        return new ByteArrayInputStream(this.objSer);
    }

    @Override // org.apache.http.HttpEntity
    public long getContentLength() {
        if (this.objSer == null) {
            return -1L;
        }
        return this.objSer.length;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isRepeatable() {
        return true;
    }

    @Override // org.apache.http.HttpEntity
    public boolean isStreaming() {
        return this.objSer == null;
    }

    @Override // org.apache.http.HttpEntity
    public void writeTo(OutputStream outstream) throws IOException {
        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        if (this.objSer == null) {
            ObjectOutputStream out = new ObjectOutputStream(outstream);
            out.writeObject(this.objRef);
            out.flush();
            return;
        }
        outstream.write(this.objSer);
        outstream.flush();
    }
}
