package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:soot/util/dot/DotGraphCommand.class */
public class DotGraphCommand implements Renderable {
    private final String command;

    public DotGraphCommand(String cmd) {
        this.command = cmd;
    }

    @Override // soot.util.dot.Renderable
    public void render(OutputStream out, int indent) throws IOException {
        DotGraphUtility.renderLine(out, this.command, indent);
    }
}
