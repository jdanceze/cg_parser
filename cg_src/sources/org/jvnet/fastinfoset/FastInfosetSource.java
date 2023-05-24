package org.jvnet.fastinfoset;

import com.sun.xml.fastinfoset.sax.SAXDocumentParser;
import java.io.InputStream;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/FastInfosetSource.class */
public class FastInfosetSource extends SAXSource {
    public FastInfosetSource(InputStream inputStream) {
        super(new InputSource(inputStream));
    }

    @Override // javax.xml.transform.sax.SAXSource
    public XMLReader getXMLReader() {
        XMLReader reader = super.getXMLReader();
        if (reader == null) {
            reader = new SAXDocumentParser();
            setXMLReader(reader);
        }
        ((SAXDocumentParser) reader).setInputStream(getInputStream());
        return reader;
    }

    public InputStream getInputStream() {
        return getInputSource().getByteStream();
    }

    public void setInputStream(InputStream inputStream) {
        setInputSource(new InputSource(inputStream));
    }
}
