package soot.jimple.infoflow.android.axml.parsers;

import java.io.IOException;
import java.util.List;
import soot.jimple.infoflow.android.axml.AXmlDocument;
import soot.jimple.infoflow.android.axml.AXmlNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/parsers/IBinaryXMLFileParser.class */
public interface IBinaryXMLFileParser {
    void parseFile(byte[] bArr) throws IOException;

    AXmlDocument getDocument();

    List<AXmlNode> getNodesWithTag(String str);
}
