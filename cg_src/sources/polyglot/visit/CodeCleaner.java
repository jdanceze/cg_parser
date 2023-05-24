package polyglot.visit;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Labeled;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.SwitchBlock;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/CodeCleaner.class */
public class CodeCleaner extends NodeVisitor {
    protected NodeFactory nf;
    protected AlphaRenamer alphaRen;

    public CodeCleaner(NodeFactory nf) {
        this.nf = nf;
        this.alphaRen = new AlphaRenamer(nf);
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        if (!(n instanceof Block) && !(n instanceof Labeled)) {
            return n;
        }
        if (n instanceof Labeled) {
            Labeled l = (Labeled) n;
            if (!(l.statement() instanceof Block)) {
                return n;
            }
            Block b = (Block) l.statement();
            if (b.statements().size() != 1) {
                if (labelRefs(b).contains(l.label())) {
                    return n;
                }
                return this.nf.Block(b.position(), clean(flattenBlock(b)));
            }
            return this.nf.Labeled(l.position(), l.label(), (Stmt) ((Block) b.visit(this.alphaRen)).statements().get(0));
        }
        Block b2 = (Block) n;
        List stmtList = clean(flattenBlock(b2));
        if (b2 instanceof SwitchBlock) {
            return this.nf.SwitchBlock(b2.position(), stmtList);
        }
        return this.nf.Block(b2.position(), stmtList);
    }

    protected List flattenBlock(Block b) {
        List stmtList = new LinkedList();
        for (Stmt stmt : b.statements()) {
            if (stmt instanceof Block) {
                stmtList.addAll(((Block) ((Stmt) stmt.visit(this.alphaRen))).statements());
            } else {
                stmtList.add(stmt);
            }
        }
        return stmtList;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0018  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected java.util.List clean(java.util.List r4) {
        /*
            r3 = this;
            java.util.LinkedList r0 = new java.util.LinkedList
            r1 = r0
            r1.<init>()
            r5 = r0
            r0 = r4
            java.util.Iterator r0 = r0.iterator()
            r6 = r0
        Lf:
            r0 = r6
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L49
            r0 = r6
            java.lang.Object r0 = r0.next()
            polyglot.ast.Stmt r0 = (polyglot.ast.Stmt) r0
            r7 = r0
            r0 = r5
            r1 = r7
            boolean r0 = r0.add(r1)
            r0 = r7
            boolean r0 = r0 instanceof polyglot.ast.Branch
            if (r0 != 0) goto L44
            r0 = r7
            boolean r0 = r0 instanceof polyglot.ast.Return
            if (r0 != 0) goto L44
            r0 = r7
            boolean r0 = r0 instanceof polyglot.ast.Throw
            if (r0 == 0) goto L46
        L44:
            r0 = r5
            return r0
        L46:
            goto Lf
        L49:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.visit.CodeCleaner.clean(java.util.List):java.util.List");
    }

    protected Set labelRefs(Block b) {
        Set result = new HashSet();
        b.visit(new NodeVisitor(this, result) { // from class: polyglot.visit.CodeCleaner.1
            private final Set val$result;
            private final CodeCleaner this$0;

            {
                this.this$0 = this;
                this.val$result = result;
            }

            @Override // polyglot.visit.NodeVisitor
            public Node leave(Node old, Node n, NodeVisitor v) {
                if (n instanceof Branch) {
                    this.val$result.add(((Branch) n).label());
                }
                return n;
            }
        });
        return result;
    }
}
