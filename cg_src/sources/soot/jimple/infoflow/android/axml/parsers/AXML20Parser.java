package soot.jimple.infoflow.android.axml.parsers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlVisitor;
import pxb.android.axml.NodeVisitor;
import soot.jimple.infoflow.android.axml.AXmlNamespace;
import soot.jimple.infoflow.android.axml.AXmlNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/parsers/AXML20Parser.class */
public class AXML20Parser extends AbstractBinaryXMLFileParser {
    private final Map<Integer, String> idToNameMap = new HashMap();

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/parsers/AXML20Parser$MyNodeVisitor.class */
    private class MyNodeVisitor extends AxmlVisitor {
        public final AXmlNode node;

        public MyNodeVisitor() {
            this.node = new AXmlNode("dummy", "", null);
        }

        public MyNodeVisitor(AXmlNode node) {
            this.node = node;
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x008a, code lost:
            r0 = (soot.tagkit.IntegerConstantValueTag) r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0097, code lost:
            if (r0.getIntValue() != r13) goto L121;
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x009a, code lost:
            r16 = r0.getName();
            r10.this$0.idToNameMap.put(java.lang.Integer.valueOf(r13), r16);
            r11 = soot.jimple.infoflow.android.axml.AXmlHandler.ANDROID_NAMESPACE;
         */
        @Override // pxb.android.axml.NodeVisitor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void attr(java.lang.String r11, java.lang.String r12, int r13, int r14, java.lang.Object r15) {
            /*
                Method dump skipped, instructions count: 1219
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.android.axml.parsers.AXML20Parser.MyNodeVisitor.attr(java.lang.String, java.lang.String, int, int, java.lang.Object):void");
        }

        @Override // pxb.android.axml.NodeVisitor
        public NodeVisitor child(String ns, String name) {
            AXmlNode childNode = new AXmlNode(name == null ? null : name.trim(), ns == null ? null : ns.trim(), this.node);
            if (name != null) {
                AXML20Parser.this.addPointer(name, childNode);
            }
            return new MyNodeVisitor(childNode);
        }

        @Override // pxb.android.axml.NodeVisitor
        public void end() {
            AXML20Parser.this.document.setRootNode(this.node);
        }

        @Override // pxb.android.axml.AxmlVisitor
        public void ns(String prefix, String uri, int line) {
            AXML20Parser.this.document.addNamespace(new AXmlNamespace(prefix, uri, line));
        }

        @Override // pxb.android.axml.NodeVisitor
        public void text(int lineNumber, String value) {
            this.node.setText(value);
            super.text(lineNumber, value);
        }
    }

    @Override // soot.jimple.infoflow.android.axml.parsers.IBinaryXMLFileParser
    public void parseFile(byte[] buffer) throws IOException {
        AxmlReader rdr = new AxmlReader(buffer);
        rdr.accept(new MyNodeVisitor());
    }
}
