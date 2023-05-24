package org.apache.commons.io.output;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;
import javax.xml.transform.OutputKeys;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/output/FileWriterWithEncoding.class */
public class FileWriterWithEncoding extends Writer {
    private final Writer out;

    public FileWriterWithEncoding(String fileName, String charsetName) throws IOException {
        this(new File(fileName), charsetName, false);
    }

    public FileWriterWithEncoding(String fileName, String charsetName, boolean append) throws IOException {
        this(new File(fileName), charsetName, append);
    }

    public FileWriterWithEncoding(String fileName, Charset charset) throws IOException {
        this(new File(fileName), charset, false);
    }

    public FileWriterWithEncoding(String fileName, Charset charset, boolean append) throws IOException {
        this(new File(fileName), charset, append);
    }

    public FileWriterWithEncoding(String fileName, CharsetEncoder encoding) throws IOException {
        this(new File(fileName), encoding, false);
    }

    public FileWriterWithEncoding(String fileName, CharsetEncoder charsetEncoder, boolean append) throws IOException {
        this(new File(fileName), charsetEncoder, append);
    }

    public FileWriterWithEncoding(File file, String charsetName) throws IOException {
        this(file, charsetName, false);
    }

    public FileWriterWithEncoding(File file, String charsetName, boolean append) throws IOException {
        this.out = initWriter(file, charsetName, append);
    }

    public FileWriterWithEncoding(File file, Charset charset) throws IOException {
        this(file, charset, false);
    }

    public FileWriterWithEncoding(File file, Charset encoding, boolean append) throws IOException {
        this.out = initWriter(file, encoding, append);
    }

    public FileWriterWithEncoding(File file, CharsetEncoder charsetEncoder) throws IOException {
        this(file, charsetEncoder, false);
    }

    public FileWriterWithEncoding(File file, CharsetEncoder charsetEncoder, boolean append) throws IOException {
        this.out = initWriter(file, charsetEncoder, append);
    }

    private static Writer initWriter(File file, Object encoding, boolean append) throws IOException {
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(encoding, OutputKeys.ENCODING);
        boolean fileExistedAlready = file.exists();
        try {
            OutputStream stream = new FileOutputStream(file, append);
            if (encoding instanceof Charset) {
                return new OutputStreamWriter(stream, (Charset) encoding);
            }
            if (encoding instanceof CharsetEncoder) {
                return new OutputStreamWriter(stream, (CharsetEncoder) encoding);
            }
            return new OutputStreamWriter(stream, (String) encoding);
        } catch (IOException | RuntimeException ex) {
            try {
                IOUtils.close((Closeable) null);
            } catch (IOException e) {
                ex.addSuppressed(e);
            }
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
    }

    @Override // java.io.Writer
    public void write(int idx) throws IOException {
        this.out.write(idx);
    }

    @Override // java.io.Writer
    public void write(char[] chr) throws IOException {
        this.out.write(chr);
    }

    @Override // java.io.Writer
    public void write(char[] chr, int st, int end) throws IOException {
        this.out.write(chr, st, end);
    }

    @Override // java.io.Writer
    public void write(String str) throws IOException {
        this.out.write(str);
    }

    @Override // java.io.Writer
    public void write(String str, int st, int end) throws IOException {
        this.out.write(str, st, end);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.out.close();
    }
}
