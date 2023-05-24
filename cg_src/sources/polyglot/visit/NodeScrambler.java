package polyglot.visit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Expr;
import polyglot.ast.Formal;
import polyglot.ast.Import;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.ast.TypeNode;
import polyglot.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/NodeScrambler.class */
public class NodeScrambler extends NodeVisitor {
    protected long seed;
    protected Random ran;
    static Class class$polyglot$ast$Node;
    static Class class$polyglot$ast$Import;
    static Class class$polyglot$ast$TypeNode;
    static Class class$polyglot$ast$ClassDecl;
    static Class class$polyglot$ast$ClassMember;
    static Class class$polyglot$ast$Formal;
    static Class class$polyglot$ast$Expr;
    static Class class$polyglot$ast$Block;
    static Class class$polyglot$ast$Catch;
    static Class class$polyglot$ast$LocalDecl;
    static Class class$polyglot$ast$Stmt;
    protected boolean scrambled = false;
    public FirstPass fp = new FirstPass(this);
    protected HashMap pairs = new HashMap();
    protected LinkedList nodes = new LinkedList();
    protected LinkedList currentParents = new LinkedList();
    protected CodeWriter cw = new CodeWriter(System.err, 72);

    public NodeScrambler() {
        Random ran = new Random();
        this.seed = ran.nextLong();
        System.err.println(new StringBuffer().append("Using seed: ").append(this.seed).toString());
        this.ran = new Random(this.seed);
    }

    public NodeScrambler(long seed) {
        this.seed = seed;
        this.ran = new Random(seed);
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/NodeScrambler$FirstPass.class */
    public class FirstPass extends NodeVisitor {
        private final NodeScrambler this$0;

        public FirstPass(NodeScrambler this$0) {
            this.this$0 = this$0;
        }

        @Override // polyglot.visit.NodeVisitor
        public NodeVisitor enter(Node n) {
            this.this$0.pairs.put(n, this.this$0.currentParents.clone());
            this.this$0.nodes.add(n);
            this.this$0.currentParents.add(n);
            return this;
        }

        @Override // polyglot.visit.NodeVisitor
        public Node leave(Node old, Node n, NodeVisitor v) {
            this.this$0.currentParents.remove(n);
            return n;
        }
    }

    public long getSeed() {
        return this.seed;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node override(Node n) {
        Node m;
        if (!coinFlip() || (m = potentialScramble(n)) == null) {
            return null;
        }
        this.scrambled = true;
        try {
            System.err.println("Replacing:");
            n.dump(this.cw);
            this.cw.newline();
            this.cw.flush();
            System.err.println("With:");
            m.dump(this.cw);
            this.cw.newline();
            this.cw.flush();
            return m;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected boolean coinFlip() {
        if (!this.scrambled && this.ran.nextDouble() > 0.9d) {
            return true;
        }
        return false;
    }

    protected Node potentialScramble(Node n) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        Class cls10;
        Class cls11;
        if (class$polyglot$ast$Node == null) {
            cls = class$("polyglot.ast.Node");
            class$polyglot$ast$Node = cls;
        } else {
            cls = class$polyglot$ast$Node;
        }
        Class required = cls;
        if (n instanceof SourceFile) {
            return null;
        }
        if (n instanceof Import) {
            if (class$polyglot$ast$Import == null) {
                cls11 = class$("polyglot.ast.Import");
                class$polyglot$ast$Import = cls11;
            } else {
                cls11 = class$polyglot$ast$Import;
            }
            required = cls11;
        } else if (n instanceof TypeNode) {
            if (class$polyglot$ast$TypeNode == null) {
                cls10 = class$("polyglot.ast.TypeNode");
                class$polyglot$ast$TypeNode = cls10;
            } else {
                cls10 = class$polyglot$ast$TypeNode;
            }
            required = cls10;
        } else if (n instanceof ClassDecl) {
            if (class$polyglot$ast$ClassDecl == null) {
                cls9 = class$("polyglot.ast.ClassDecl");
                class$polyglot$ast$ClassDecl = cls9;
            } else {
                cls9 = class$polyglot$ast$ClassDecl;
            }
            required = cls9;
        } else if (n instanceof ClassMember) {
            if (class$polyglot$ast$ClassMember == null) {
                cls8 = class$("polyglot.ast.ClassMember");
                class$polyglot$ast$ClassMember = cls8;
            } else {
                cls8 = class$polyglot$ast$ClassMember;
            }
            required = cls8;
        } else if (n instanceof Formal) {
            if (class$polyglot$ast$Formal == null) {
                cls7 = class$("polyglot.ast.Formal");
                class$polyglot$ast$Formal = cls7;
            } else {
                cls7 = class$polyglot$ast$Formal;
            }
            required = cls7;
        } else if (n instanceof Expr) {
            if (class$polyglot$ast$Expr == null) {
                cls6 = class$("polyglot.ast.Expr");
                class$polyglot$ast$Expr = cls6;
            } else {
                cls6 = class$polyglot$ast$Expr;
            }
            required = cls6;
        } else if (n instanceof Block) {
            if (class$polyglot$ast$Block == null) {
                cls5 = class$("polyglot.ast.Block");
                class$polyglot$ast$Block = cls5;
            } else {
                cls5 = class$polyglot$ast$Block;
            }
            required = cls5;
        } else if (n instanceof Catch) {
            if (class$polyglot$ast$Catch == null) {
                cls4 = class$("polyglot.ast.Catch");
                class$polyglot$ast$Catch = cls4;
            } else {
                cls4 = class$polyglot$ast$Catch;
            }
            required = cls4;
        } else if (n instanceof LocalDecl) {
            if (class$polyglot$ast$LocalDecl == null) {
                cls3 = class$("polyglot.ast.LocalDecl");
                class$polyglot$ast$LocalDecl = cls3;
            } else {
                cls3 = class$polyglot$ast$LocalDecl;
            }
            required = cls3;
        } else if (n instanceof Stmt) {
            if (class$polyglot$ast$Stmt == null) {
                cls2 = class$("polyglot.ast.Stmt");
                class$polyglot$ast$Stmt = cls2;
            } else {
                cls2 = class$polyglot$ast$Stmt;
            }
            required = cls2;
        }
        LinkedList parents = (LinkedList) this.pairs.get(n);
        Iterator iter1 = this.nodes.iterator();
        while (iter1.hasNext()) {
            Node m = (Node) iter1.next();
            if (required.isAssignableFrom(m.getClass())) {
                boolean isParent = false;
                Iterator iter2 = parents.iterator();
                while (iter2.hasNext()) {
                    if (m == iter2.next()) {
                        isParent = true;
                    }
                }
                if (!isParent && m != n) {
                    return m;
                }
            }
        }
        return null;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
