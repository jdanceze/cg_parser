package com.oreilly.servlet.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletInputStream;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/ParamPart.class */
public class ParamPart extends Part {
    private byte[] value;
    private String encoding;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParamPart(String name, ServletInputStream in, String boundary, String encoding) throws IOException {
        super(name);
        this.encoding = encoding;
        PartInputStream pis = new PartInputStream(in, boundary);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        byte[] buf = new byte[128];
        while (true) {
            int read = pis.read(buf);
            if (read != -1) {
                baos.write(buf, 0, read);
            } else {
                pis.close();
                baos.close();
                this.value = baos.toByteArray();
                return;
            }
        }
    }

    public byte[] getValue() {
        return this.value;
    }

    public String getStringValue() throws UnsupportedEncodingException {
        return getStringValue(this.encoding);
    }

    public String getStringValue(String encoding) throws UnsupportedEncodingException {
        return new String(this.value, encoding);
    }

    @Override // com.oreilly.servlet.multipart.Part
    public boolean isParam() {
        return true;
    }
}
