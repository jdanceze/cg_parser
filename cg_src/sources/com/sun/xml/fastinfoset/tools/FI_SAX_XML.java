package com.sun.xml.fastinfoset.tools;

import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.jvnet.fastinfoset.FastInfosetSource;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/FI_SAX_XML.class */
public class FI_SAX_XML extends TransformInputOutput {
    @Override // com.sun.xml.fastinfoset.tools.TransformInputOutput
    public void parse(InputStream finf, OutputStream xml) throws Exception {
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new FastInfosetSource(finf), new StreamResult(xml));
    }

    public static void main(String[] args) throws Exception {
        FI_SAX_XML p = new FI_SAX_XML();
        p.parse(args);
    }
}
