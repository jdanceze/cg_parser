package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:soot/util/dot/DotGraphNode.class */
public class DotGraphNode extends AbstractDotGraphElement implements Renderable {
    private final String name;

    public DotGraphNode(String name) {
        this.name = String.valueOf('\"') + DotGraphUtility.replaceQuotes(name) + '\"';
    }

    public DotGraphNode(String name, boolean dontQuoteName) {
        this.name = dontQuoteName ? DotGraphUtility.replaceQuotes(name) : String.valueOf('\"') + DotGraphUtility.replaceQuotes(name) + '\"';
    }

    public String getName() {
        return this.name;
    }

    public void setHTMLLabel(String label) {
        setAttribute("label", DotGraphUtility.replaceReturns(label));
    }

    public void setShape(String shape) {
        setAttribute("shape", shape);
    }

    public void setStyle(String style) {
        setAttribute("style", style);
    }

    @Override // soot.util.dot.Renderable
    public void render(OutputStream out, int indent) throws IOException {
        StringBuilder line = new StringBuilder();
        line.append(getName());
        Collection<DotGraphAttribute> attrs = getAttributes();
        if (!attrs.isEmpty()) {
            line.append(" [");
            for (DotGraphAttribute attr : attrs) {
                line.append(attr.toString()).append(',');
            }
            line.append(']');
        }
        line.append(';');
        DotGraphUtility.renderLine(out, line.toString(), indent);
    }
}
