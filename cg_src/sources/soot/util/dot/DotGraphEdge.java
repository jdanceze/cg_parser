package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import org.apache.commons.cli.HelpFormatter;
/* loaded from: gencallgraphv3.jar:soot/util/dot/DotGraphEdge.class */
public class DotGraphEdge extends AbstractDotGraphElement implements Renderable {
    private final DotGraphNode start;
    private final DotGraphNode end;
    private final boolean isDirected;

    public DotGraphEdge(DotGraphNode src, DotGraphNode dst) {
        this.start = src;
        this.end = dst;
        this.isDirected = true;
    }

    public DotGraphEdge(DotGraphNode src, DotGraphNode dst, boolean directed) {
        this.start = src;
        this.end = dst;
        this.isDirected = directed;
    }

    public void setStyle(String style) {
        setAttribute("style", style);
    }

    @Override // soot.util.dot.Renderable
    public void render(OutputStream out, int indent) throws IOException {
        StringBuilder line = new StringBuilder();
        line.append(this.start.getName());
        line.append(this.isDirected ? "->" : HelpFormatter.DEFAULT_LONG_OPT_PREFIX);
        line.append(this.end.getName());
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
