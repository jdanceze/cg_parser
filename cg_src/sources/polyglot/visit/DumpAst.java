package polyglot.visit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DumpAst.class */
public class DumpAst extends NodeVisitor {
    protected Writer fw;
    protected CodeWriter w;

    public DumpAst(String name, int width) throws IOException {
        this.fw = new FileWriter(name);
        this.w = new CodeWriter(this.fw, width);
    }

    public DumpAst(CodeWriter w) {
        this.w = w;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node n) {
        this.w.write("(");
        n.dump(this.w);
        this.w.allowBreak(4, Instruction.argsep);
        this.w.begin(0);
        return this;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        this.w.end();
        this.w.write(")");
        this.w.allowBreak(0, Instruction.argsep);
        return n;
    }

    @Override // polyglot.visit.NodeVisitor
    public void finish() {
        try {
            this.w.flush();
            if (this.fw != null) {
                this.fw.flush();
                this.fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
