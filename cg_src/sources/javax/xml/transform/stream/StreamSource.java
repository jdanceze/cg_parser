package javax.xml.transform.stream;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.transform.Source;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/stream/StreamSource.class */
public class StreamSource implements Source {
    public static final String FEATURE = "http://javax.xml.transform.stream.StreamSource/feature";
    private String publicId;
    private String systemId;
    private InputStream inputStream;
    private Reader reader;

    public StreamSource() {
    }

    public StreamSource(InputStream inputStream) {
        setInputStream(inputStream);
    }

    public StreamSource(InputStream inputStream, String str) {
        setInputStream(inputStream);
        setSystemId(str);
    }

    public StreamSource(Reader reader) {
        setReader(reader);
    }

    public StreamSource(Reader reader, String str) {
        setReader(reader);
        setSystemId(str);
    }

    public StreamSource(String str) {
        this.systemId = str;
    }

    public StreamSource(File file) {
        setSystemId(file);
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader getReader() {
        return this.reader;
    }

    public void setPublicId(String str) {
        this.publicId = str;
    }

    public String getPublicId() {
        return this.publicId;
    }

    @Override // javax.xml.transform.Source
    public void setSystemId(String str) {
        this.systemId = str;
    }

    @Override // javax.xml.transform.Source
    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(File file) {
        this.systemId = FilePathToURI.filepath2URI(file.getAbsolutePath());
    }
}
