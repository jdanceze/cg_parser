package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Expr;
import polyglot.ast.Ext;
import polyglot.ast.JL;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Node_c.class */
public abstract class Node_c implements Node {
    protected Position position;
    protected JL del;
    protected Ext ext;

    public Node_c(Position pos) {
        this.position = pos;
    }

    @Override // polyglot.ast.JL
    public void init(Node node) {
        if (node != this) {
            throw new InternalCompilerError("Cannot use a Node as a delegate or extension.");
        }
    }

    @Override // polyglot.ast.JL
    public Node node() {
        return this;
    }

    @Override // polyglot.ast.Node
    public JL del() {
        return this.del != null ? this.del : this;
    }

    @Override // polyglot.ast.Node
    public Node del(JL del) {
        if (this.del == del) {
            return this;
        }
        JL old = this.del;
        this.del = null;
        Node_c n = (Node_c) copy();
        n.del = del != this ? del : null;
        if (n.del != null) {
            n.del.init(n);
        }
        this.del = old;
        return n;
    }

    @Override // polyglot.ast.Node
    public Ext ext(int n) {
        if (n < 1) {
            throw new InternalCompilerError("n must be >= 1");
        }
        return n == 1 ? ext() : ext(n - 1).ext();
    }

    @Override // polyglot.ast.Node
    public Node ext(int n, Ext ext) {
        if (n < 1) {
            throw new InternalCompilerError("n must be >= 1");
        }
        if (n == 1) {
            return ext(ext);
        }
        Ext prev = ext(n - 1);
        if (prev == null) {
            throw new InternalCompilerError("cannot set the nth extension if there is no (n-1)st extension");
        }
        return ext(n - 1, prev.ext(ext));
    }

    @Override // polyglot.ast.Node
    public Ext ext() {
        return this.ext;
    }

    @Override // polyglot.ast.Node
    public Node ext(Ext ext) {
        if (this.ext == ext) {
            return this;
        }
        Ext old = this.ext;
        this.ext = null;
        Node_c n = (Node_c) copy();
        n.ext = ext;
        if (n.ext != null) {
            n.ext.init(n);
        }
        this.ext = old;
        return n;
    }

    @Override // polyglot.util.Copy
    public Object copy() {
        try {
            Node_c n = (Node_c) super.clone();
            if (this.del != null) {
                n.del = (JL) this.del.copy();
                n.del.init(n);
            }
            if (this.ext != null) {
                n.ext = (Ext) this.ext.copy();
                n.ext.init(n);
            }
            return n;
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone() weirdness.");
        }
    }

    @Override // polyglot.ast.Node
    public Position position() {
        return this.position;
    }

    @Override // polyglot.ast.Node
    public Node position(Position position) {
        Node_c n = (Node_c) copy();
        n.position = position;
        return n;
    }

    @Override // polyglot.ast.Node
    public Node visitChild(Node n, NodeVisitor v) {
        if (n == null) {
            return null;
        }
        return v.visitEdge(this, n);
    }

    @Override // polyglot.ast.Node
    public Node visit(NodeVisitor v) {
        return v.visitEdge(null, this);
    }

    @Override // polyglot.ast.Node
    public Node visitEdge(Node parent, NodeVisitor v) {
        Node n = v.override(parent, this);
        if (n == null) {
            NodeVisitor v_ = v.enter(parent, this);
            if (v_ == null) {
                throw new InternalCompilerError("NodeVisitor.enter() returned null.");
            }
            Node n2 = del().visitChildren(v_);
            if (n2 == null) {
                throw new InternalCompilerError("Node_c.visitChildren() returned null.");
            }
            n = v.leave(parent, this, n2, v_);
            if (n == null) {
                throw new InternalCompilerError("NodeVisitor.leave() returned null.");
            }
        }
        return n;
    }

    @Override // polyglot.ast.Node
    public List visitList(List l, NodeVisitor v) {
        if (l == null) {
            return null;
        }
        List result = l;
        List arrayList = new ArrayList(l.size());
        Iterator i = l.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            Node m = visitChild(n, v);
            if (n != m) {
                result = arrayList;
            }
            arrayList.add(m);
        }
        return result;
    }

    @Override // polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        return this;
    }

    @Override // polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c;
    }

    @Override // polyglot.ast.NodeOps
    public Context enterScope(Node child, Context c) {
        return child.del().enterScope(c);
    }

    @Override // polyglot.ast.NodeOps
    public void addDecls(Context c) {
    }

    public Node buildTypesOverride(TypeBuilder tb) throws SemanticException {
        return null;
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        return tb;
    }

    @Override // polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        return this;
    }

    public Node disambiguateOverride(AmbiguityRemover ar) throws SemanticException {
        return null;
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        return ar;
    }

    @Override // polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        return this;
    }

    public Node addMembersOverride(AddMemberVisitor am) throws SemanticException {
        return null;
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor addMembersEnter(AddMemberVisitor am) throws SemanticException {
        return am;
    }

    @Override // polyglot.ast.NodeOps
    public Node addMembers(AddMemberVisitor am) throws SemanticException {
        return this;
    }

    public Node typeCheckOverride(TypeChecker tc) throws SemanticException {
        return null;
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
        return tc;
    }

    @Override // polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        return this;
    }

    @Override // polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        return child.type();
    }

    public Node exceptionCheckOverride(ExceptionChecker ec) throws SemanticException {
        return null;
    }

    @Override // polyglot.ast.NodeOps
    public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
        return ec.push();
    }

    @Override // polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        List<Type> l = del().throwTypes(ec.typeSystem());
        for (Type type : l) {
            ec.throwsException(type, position());
        }
        return this;
    }

    @Override // polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        return Collections.EMPTY_LIST;
    }

    @Override // polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter pp) {
    }

    public void printBlock(Node n, CodeWriter w, PrettyPrinter pp) {
        w.begin(0);
        print(n, w, pp);
        w.end();
    }

    public void printSubStmt(Stmt stmt, CodeWriter w, PrettyPrinter pp) {
        if (stmt instanceof Block) {
            w.write(Instruction.argsep);
            print(stmt, w, pp);
            return;
        }
        w.allowBreak(4, Instruction.argsep);
        printBlock(stmt, w, pp);
    }

    public void print(Node child, CodeWriter w, PrettyPrinter pp) {
        pp.print(this, child, w);
    }

    @Override // polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        del().prettyPrint(w, tr);
    }

    @Override // polyglot.ast.Node
    public void dump(CodeWriter w) {
        w.write(StringUtil.getShortNameComponent(getClass().getName()));
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(del ").append(del()).append(")").toString());
        w.end();
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write("(ext ");
        if (ext() == null) {
            w.write(Jimple.NULL);
        } else {
            ext().dump(w);
        }
        w.write(")");
        w.end();
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(position ").append(this.position != null ? this.position.toString() : "UNKNOWN").append(")").toString());
        w.end();
    }

    public String toString() {
        return getClass().getName();
    }
}
