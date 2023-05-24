package com.sun.xml.fastinfoset.tools;

import com.sun.xml.fastinfoset.dom.DOMDocumentSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/XML_DOM_FI.class */
public class XML_DOM_FI extends TransformInputOutput {
    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream finf, String workingDirectory) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        if (workingDirectory != null) {
            db.setEntityResolver(createRelativePathResolver(workingDirectory));
        }
        Document d = db.parse(document);
        DOMDocumentSerializer s = new DOMDocumentSerializer();
        s.setOutputStream(finf);
        s.serialize(d);
    }

    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream document, OutputStream finf) throws Exception {
        parse(document, finf, null);
    }

    public static void main(String[] args) throws Exception {
        XML_DOM_FI p = new XML_DOM_FI();
        p.parse(args);
    }
}
