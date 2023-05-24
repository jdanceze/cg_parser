package soot.jimple.infoflow.android.axml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import pxb.android.axml.AxmlWriter;
import pxb.android.axml.NodeVisitor;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.jimple.infoflow.android.axml.parsers.AXML20Parser;
import soot.jimple.infoflow.android.axml.parsers.IBinaryXMLFileParser;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlHandler.class */
public class AXmlHandler {
    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    protected byte[] xml;
    protected final IBinaryXMLFileParser parser;
    private static final int resId_maxSdkVersion = 16843377;
    private static final int resId_minSdkVersion = 16843276;
    private static final int resId_name = 16842755;
    private static final int resId_onClick = 16843375;

    public AXmlHandler(InputStream aXmlIs) throws IOException {
        this(aXmlIs, new AXML20Parser());
    }

    public AXmlHandler(InputStream aXmlIs, IBinaryXMLFileParser parser) throws IOException {
        int bytesRead;
        byte[] nextChunk;
        int chunkSize;
        if (aXmlIs == null) {
            throw new RuntimeException("NULL input stream for AXmlHandler");
        }
        BufferedInputStream buffer = new BufferedInputStream(aXmlIs);
        List<byte[]> chunks = new ArrayList<>();
        int i = 0;
        while (true) {
            bytesRead = i;
            if (aXmlIs.available() > 0 && (chunkSize = buffer.read((nextChunk = new byte[aXmlIs.available()]))) >= 0) {
                chunks.add(nextChunk);
                i = bytesRead + chunkSize;
            }
        }
        this.xml = new byte[bytesRead];
        int bytesCopied = 0;
        for (byte[] chunk : chunks) {
            int toCopy = Math.min(chunk.length, bytesRead - bytesCopied);
            System.arraycopy(chunk, 0, this.xml, bytesCopied, toCopy);
            bytesCopied += toCopy;
        }
        parser.parseFile(this.xml);
        this.parser = parser;
    }

    public AXmlDocument getDocument() {
        return this.parser.getDocument();
    }

    public List<AXmlNode> getNodesWithTag(String tag) {
        return this.parser.getNodesWithTag(tag);
    }

    public byte[] toByteArray() {
        try {
            AxmlWriter aw = new AxmlWriter();
            for (AXmlNamespace ns : getDocument().getNamespaces()) {
                aw.ns(ns.getPrefix(), ns.getUri(), ns.getLine());
            }
            writeNode(aw, getDocument().getRootNode());
            return aw.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getAttributeResourceId(String name) {
        if (name.equals("name")) {
            return 16842755;
        }
        if (name.equals("maxSdkVersion")) {
            return 16843377;
        }
        if (name.equals("minSdkVersion")) {
            return 16843276;
        }
        if (name.equals("onClick")) {
            return 16843375;
        }
        SootClass rClass = Scene.v().forceResolve("android.R$attr", 3);
        if (!rClass.declaresFieldByName(name)) {
            return -1;
        }
        SootField idField = rClass.getFieldByName(name);
        for (Tag t : idField.getTags()) {
            if (t instanceof IntegerConstantValueTag) {
                IntegerConstantValueTag cvt = (IntegerConstantValueTag) t;
                return cvt.getIntValue();
            }
        }
        return -1;
    }

    private void writeNode(NodeVisitor parentNodeVisitor, AXmlNode node) {
        NodeVisitor childNodeVisitor = parentNodeVisitor.child(node.getNamespace(), node.getTag());
        if (!node.isIncluded()) {
            return;
        }
        for (AXmlAttribute<?> attr : node.getAttributes().values()) {
            String namespace = attr.getNamespace();
            if (namespace != null && namespace.isEmpty()) {
                namespace = null;
            }
            int resourceId = attr.getResourceId();
            if (resourceId < 0 && !node.getTag().equals("manifest")) {
                resourceId = getAttributeResourceId(attr.getName());
            }
            int attrType = attr.getAttributeType();
            if (attrType < 0) {
                attrType = attr.getType();
            }
            childNodeVisitor.attr(namespace, attr.getName(), resourceId, attrType, attr.getValue());
        }
        for (AXmlNode child : node.getChildren()) {
            writeNode(childNodeVisitor, child);
        }
        childNodeVisitor.end();
    }

    public String toString() {
        return toString(getDocument().getRootNode(), 0);
    }

    protected String toString(AXmlNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        StringBuilder padding = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            padding.append("\t");
        }
        sb.append((CharSequence) padding).append(node.getTag());
        for (AXmlAttribute<?> attr : node.getAttributes().values()) {
            sb.append("\n").append((CharSequence) padding).append("- ").append(attr.getName()).append(": ").append(attr.getValue());
        }
        for (AXmlNode n : node.getChildren()) {
            sb.append("\n").append(toString(n, depth + 1));
        }
        return sb.toString();
    }
}
