package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/DOMElementWriter.class */
public class DOMElementWriter {
    private static final int HEX = 16;
    private static final String[] WS_ENTITIES = new String[5];
    private static final String NS = "ns";
    private boolean xmlDeclaration;
    private XmlNamespacePolicy namespacePolicy;
    private Map<String, String> nsPrefixMap;
    private int nextPrefix;
    private Map<Element, List<String>> nsURIByElement;
    protected String[] knownEntities;

    static {
        for (int i = 9; i < 14; i++) {
            WS_ENTITIES[i - 9] = "&#x" + Integer.toHexString(i) + ";";
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/DOMElementWriter$XmlNamespacePolicy.class */
    public static class XmlNamespacePolicy {
        private boolean qualifyElements;
        private boolean qualifyAttributes;
        public static final XmlNamespacePolicy IGNORE = new XmlNamespacePolicy(false, false);
        public static final XmlNamespacePolicy ONLY_QUALIFY_ELEMENTS = new XmlNamespacePolicy(true, false);
        public static final XmlNamespacePolicy QUALIFY_ALL = new XmlNamespacePolicy(true, true);

        public XmlNamespacePolicy(boolean qualifyElements, boolean qualifyAttributes) {
            this.qualifyElements = qualifyElements;
            this.qualifyAttributes = qualifyAttributes;
        }
    }

    public DOMElementWriter() {
        this.xmlDeclaration = true;
        this.namespacePolicy = XmlNamespacePolicy.IGNORE;
        this.nsPrefixMap = new HashMap();
        this.nextPrefix = 0;
        this.nsURIByElement = new HashMap();
        this.knownEntities = new String[]{"gt", "amp", "lt", "apos", "quot"};
    }

    public DOMElementWriter(boolean xmlDeclaration) {
        this(xmlDeclaration, XmlNamespacePolicy.IGNORE);
    }

    public DOMElementWriter(boolean xmlDeclaration, XmlNamespacePolicy namespacePolicy) {
        this.xmlDeclaration = true;
        this.namespacePolicy = XmlNamespacePolicy.IGNORE;
        this.nsPrefixMap = new HashMap();
        this.nextPrefix = 0;
        this.nsURIByElement = new HashMap();
        this.knownEntities = new String[]{"gt", "amp", "lt", "apos", "quot"};
        this.xmlDeclaration = xmlDeclaration;
        this.namespacePolicy = namespacePolicy;
    }

    public void write(Element root, OutputStream out) throws IOException {
        Writer wri = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        writeXMLDeclaration(wri);
        write(root, wri, 0, "  ");
        wri.flush();
    }

    public void writeXMLDeclaration(Writer wri) throws IOException {
        if (this.xmlDeclaration) {
            wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        }
    }

    public void write(Element element, Writer out, int indent, String indentWith) throws IOException {
        NodeList children = element.getChildNodes();
        boolean hasChildren = children.getLength() > 0;
        boolean hasChildElements = false;
        openElement(element, out, indent, indentWith, hasChildren);
        if (hasChildren) {
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                switch (child.getNodeType()) {
                    case 1:
                        hasChildElements = true;
                        if (i == 0) {
                            out.write(System.lineSeparator());
                        }
                        write((Element) child, out, indent + 1, indentWith);
                        break;
                    case 3:
                        out.write(encode(child.getNodeValue()));
                        break;
                    case 4:
                        out.write("<![CDATA[");
                        encodedata(out, ((Text) child).getData());
                        out.write("]]>");
                        break;
                    case 5:
                        out.write(38);
                        out.write(child.getNodeName());
                        out.write(59);
                        break;
                    case 7:
                        out.write("<?");
                        out.write(child.getNodeName());
                        String data = child.getNodeValue();
                        if (data != null && !data.isEmpty()) {
                            out.write(32);
                            out.write(data);
                        }
                        out.write("?>");
                        break;
                    case 8:
                        out.write("<!--");
                        out.write(encode(child.getNodeValue()));
                        out.write("-->");
                        break;
                }
            }
            closeElement(element, out, indent, indentWith, hasChildElements);
        }
    }

    public void openElement(Element element, Writer out, int indent, String indentWith) throws IOException {
        openElement(element, out, indent, indentWith, true);
    }

    public void openElement(Element element, Writer out, int indent, String indentWith, boolean hasChildren) throws IOException {
        for (int i = 0; i < indent; i++) {
            out.write(indentWith);
        }
        out.write("<");
        if (this.namespacePolicy.qualifyElements) {
            String uri = getNamespaceURI(element);
            String prefix = this.nsPrefixMap.get(uri);
            if (prefix == null) {
                if (this.nsPrefixMap.isEmpty()) {
                    prefix = "";
                } else {
                    StringBuilder append = new StringBuilder().append(NS);
                    int i2 = this.nextPrefix;
                    this.nextPrefix = i2 + 1;
                    prefix = append.append(i2).toString();
                }
                this.nsPrefixMap.put(uri, prefix);
                addNSDefinition(element, uri);
            }
            if (!prefix.isEmpty()) {
                out.write(prefix);
                out.write(":");
            }
        }
        out.write(element.getTagName());
        NamedNodeMap attrs = element.getAttributes();
        for (int i3 = 0; i3 < attrs.getLength(); i3++) {
            Attr attr = (Attr) attrs.item(i3);
            out.write(Instruction.argsep);
            if (this.namespacePolicy.qualifyAttributes) {
                String uri2 = getNamespaceURI(attr);
                String prefix2 = this.nsPrefixMap.get(uri2);
                if (prefix2 == null) {
                    StringBuilder append2 = new StringBuilder().append(NS);
                    int i4 = this.nextPrefix;
                    this.nextPrefix = i4 + 1;
                    prefix2 = append2.append(i4).toString();
                    this.nsPrefixMap.put(uri2, prefix2);
                    addNSDefinition(element, uri2);
                }
                out.write(prefix2);
                out.write(":");
            }
            out.write(attr.getName());
            out.write("=\"");
            out.write(encodeAttributeValue(attr.getValue()));
            out.write("\"");
        }
        List<String> uris = this.nsURIByElement.get(element);
        if (uris != null) {
            for (String uri3 : uris) {
                String prefix3 = this.nsPrefixMap.get(uri3);
                out.write(" xmlns");
                if (!prefix3.isEmpty()) {
                    out.write(":");
                    out.write(prefix3);
                }
                out.write("=\"");
                out.write(uri3);
                out.write("\"");
            }
        }
        if (hasChildren) {
            out.write(">");
            return;
        }
        removeNSDefinitions(element);
        out.write(String.format(" />%n", new Object[0]));
        out.flush();
    }

    public void closeElement(Element element, Writer out, int indent, String indentWith, boolean hasChildren) throws IOException {
        if (hasChildren) {
            for (int i = 0; i < indent; i++) {
                out.write(indentWith);
            }
        }
        out.write("</");
        if (this.namespacePolicy.qualifyElements) {
            String uri = getNamespaceURI(element);
            String prefix = this.nsPrefixMap.get(uri);
            if (prefix != null && !prefix.isEmpty()) {
                out.write(prefix);
                out.write(":");
            }
            removeNSDefinitions(element);
        }
        out.write(element.getTagName());
        out.write(String.format(">%n", new Object[0]));
        out.flush();
    }

    public String encode(String value) {
        return encode(value, false);
    }

    public String encodeAttributeValue(String value) {
        return encode(value, true);
    }

    private String encode(String value, boolean encodeWhitespace) {
        char[] charArray;
        StringBuilder sb = new StringBuilder(value.length());
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\t':
                case '\n':
                case '\r':
                    if (encodeWhitespace) {
                        sb.append(WS_ENTITIES[c - '\t']);
                        break;
                    } else {
                        sb.append(c);
                        break;
                    }
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    if (isLegalCharacter(c)) {
                        sb.append(c);
                        break;
                    } else {
                        break;
                    }
            }
        }
        return sb.substring(0);
    }

    public String encodedata(String value) {
        StringWriter out = new StringWriter();
        try {
            encodedata(out, value);
            return out.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void encodedata(Writer out, String value) throws IOException {
        int len = value.length();
        int prevEnd = 0;
        int cdataEndPos = value.indexOf("]]>");
        while (prevEnd < len) {
            int end = cdataEndPos < 0 ? len : cdataEndPos;
            int i = prevEnd;
            while (true) {
                int prevLegalCharPos = i;
                if (prevLegalCharPos >= end) {
                    break;
                }
                int illegalCharPos = prevLegalCharPos;
                while (illegalCharPos < end && isLegalCharacter(value.charAt(illegalCharPos))) {
                    illegalCharPos++;
                }
                out.write(value, prevLegalCharPos, illegalCharPos - prevLegalCharPos);
                i = illegalCharPos + 1;
            }
            if (cdataEndPos >= 0) {
                out.write("]]]]><![CDATA[>");
                prevEnd = cdataEndPos + 3;
                cdataEndPos = value.indexOf("]]>", prevEnd);
            } else {
                prevEnd = end;
            }
        }
    }

    public boolean isReference(String ent) {
        String[] strArr;
        if (ent.charAt(0) != '&' || !ent.endsWith(";")) {
            return false;
        }
        if (ent.charAt(1) == '#') {
            if (ent.charAt(2) == 'x') {
                try {
                    Integer.parseInt(ent.substring(3, ent.length() - 1), 16);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            try {
                Integer.parseInt(ent.substring(2, ent.length() - 1));
                return true;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
        String name = ent.substring(1, ent.length() - 1);
        for (String knownEntity : this.knownEntities) {
            if (name.equals(knownEntity)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLegalCharacter(char c) {
        return isLegalXmlCharacter(c);
    }

    public static boolean isLegalXmlCharacter(char c) {
        if (c == '\t' || c == '\n' || c == '\r') {
            return true;
        }
        if (c < ' ') {
            return false;
        }
        if (c <= 55295) {
            return true;
        }
        return c >= 57344 && c <= 65533;
    }

    private void removeNSDefinitions(Element element) {
        List<String> uris = this.nsURIByElement.get(element);
        if (uris != null) {
            Map<String, String> map = this.nsPrefixMap;
            Objects.requireNonNull(map);
            uris.forEach((v1) -> {
                r1.remove(v1);
            });
            this.nsURIByElement.remove(element);
        }
    }

    private void addNSDefinition(Element element, String uri) {
        this.nsURIByElement.computeIfAbsent(element, e -> {
            return new ArrayList();
        }).add(uri);
    }

    private static String getNamespaceURI(Node n) {
        String uri = n.getNamespaceURI();
        return uri == null ? "" : uri;
    }
}
