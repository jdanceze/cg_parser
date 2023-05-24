package soot.util;

import jasmin.Main;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:soot/util/JasminOutputStream.class */
public class JasminOutputStream extends ByteArrayOutputStream {
    private final OutputStream out;

    public JasminOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() {
        ByteArrayInputStream bais = new ByteArrayInputStream(toByteArray());
        Main.assemble((InputStream) bais, this.out, false);
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
        super.close();
    }
}
