package pxb.android.axml;

import java.io.IOException;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/AxmlReader.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/AxmlReader.class */
public class AxmlReader {
    private static final Logger logger = LoggerFactory.getLogger(AxmlReader.class);
    public static final NodeVisitor EMPTY_VISITOR = new NodeVisitor() { // from class: pxb.android.axml.AxmlReader.1
        @Override // pxb.android.axml.NodeVisitor
        public NodeVisitor child(String ns, String name) {
            return this;
        }
    };
    final AxmlParser parser;

    public AxmlReader(byte[] data) {
        this.parser = new AxmlParser(data);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void accept(AxmlVisitor av) throws IOException {
        Stack<NodeVisitor> nvs = new Stack<>();
        NodeVisitor tos = av;
        while (true) {
            int type = this.parser.next();
            switch (type) {
                case 1:
                case 5:
                    break;
                case 2:
                    nvs.push(tos);
                    tos = tos.child(this.parser.getNamespaceUri(), this.parser.getName());
                    if (tos != null) {
                        if (tos != EMPTY_VISITOR) {
                            tos.line(this.parser.getLineNumber());
                            for (int i = 0; i < this.parser.getAttrCount(); i++) {
                                tos.attr(this.parser.getAttrNs(i), this.parser.getAttrName(i), this.parser.getAttrResId(i), this.parser.getAttrType(i), this.parser.getAttrValue(i));
                            }
                            break;
                        } else {
                            break;
                        }
                    } else {
                        tos = EMPTY_VISITOR;
                        break;
                    }
                case 3:
                    tos.end();
                    tos = nvs.pop();
                    break;
                case 4:
                    av.ns(this.parser.getNamespacePrefix(), this.parser.getNamespaceUri(), this.parser.getLineNumber());
                    break;
                case 6:
                    tos.text(this.parser.getLineNumber(), this.parser.getText());
                    break;
                case 7:
                    return;
                default:
                    logger.warn("Unsupported tag: {}", Integer.valueOf(type));
                    break;
            }
        }
    }
}
