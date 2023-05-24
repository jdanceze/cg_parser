package soot.jimple.infoflow.android.axml.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import soot.jimple.infoflow.android.axml.AXmlDocument;
import soot.jimple.infoflow.android.axml.AXmlNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/parsers/AbstractBinaryXMLFileParser.class */
public abstract class AbstractBinaryXMLFileParser implements IBinaryXMLFileParser {
    protected HashMap<String, ArrayList<AXmlNode>> nodesWithTag = new HashMap<>();
    protected AXmlDocument document = new AXmlDocument();

    /* JADX INFO: Access modifiers changed from: protected */
    public void addPointer(String tag, AXmlNode node) {
        if (!this.nodesWithTag.containsKey(tag)) {
            this.nodesWithTag.put(tag, new ArrayList<>());
        }
        this.nodesWithTag.get(tag).add(node);
    }

    @Override // soot.jimple.infoflow.android.axml.parsers.IBinaryXMLFileParser
    public AXmlDocument getDocument() {
        return this.document;
    }

    @Override // soot.jimple.infoflow.android.axml.parsers.IBinaryXMLFileParser
    public List<AXmlNode> getNodesWithTag(String tag) {
        if (this.nodesWithTag.containsKey(tag)) {
            return new ArrayList(this.nodesWithTag.get(tag));
        }
        return Collections.emptyList();
    }
}
