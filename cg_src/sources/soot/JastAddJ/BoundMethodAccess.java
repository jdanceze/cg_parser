package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BoundMethodAccess.class */
public class BoundMethodAccess extends MethodAccess implements Cloneable {
    private MethodDecl methodDecl;
    protected boolean decl_computed;
    protected MethodDecl decl_value;

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decl_computed = false;
        this.decl_value = null;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public BoundMethodAccess clone() throws CloneNotSupportedException {
        BoundMethodAccess node = (BoundMethodAccess) super.clone();
        node.decl_computed = false;
        node.decl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            BoundMethodAccess node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ASTNode<ASTNode> copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy;
    }

    public BoundMethodAccess(String name, List args, MethodDecl methodDecl) {
        this(name, args);
        this.methodDecl = methodDecl;
    }

    public BoundMethodAccess() {
        this.decl_computed = false;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public BoundMethodAccess(String p0, List<Expr> p1) {
        this.decl_computed = false;
        setID(p0);
        setChild(p1, 0);
    }

    public BoundMethodAccess(Symbol p0, List<Expr> p1) {
        this.decl_computed = false;
        setID(p0);
        setChild(p1, 0);
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.MethodAccess
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setArgList(List<Expr> list) {
        setChild(list, 0);
    }

    @Override // soot.JastAddJ.MethodAccess
    public int getNumArg() {
        return getArgList().getNumChild();
    }

    @Override // soot.JastAddJ.MethodAccess
    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.MethodAccess
    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    @Override // soot.JastAddJ.MethodAccess
    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodAccess
    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.MethodAccess
    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgs() {
        return getArgList();
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgList() {
        List<Expr> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.MethodAccess
    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.MethodAccess
    public MethodDecl decl() {
        if (this.decl_computed) {
            return this.decl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decl_value = decl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decl_computed = true;
        }
        return this.decl_value;
    }

    private MethodDecl decl_compute() {
        return this.methodDecl;
    }

    @Override // soot.JastAddJ.MethodAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
