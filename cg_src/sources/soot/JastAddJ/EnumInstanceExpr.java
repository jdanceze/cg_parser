package soot.JastAddJ;

import java.util.Iterator;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/EnumInstanceExpr.class */
public class EnumInstanceExpr extends ClassInstanceExpr implements Cloneable {
    protected Access getAccess_value;
    protected List<Expr> getArgList_value;
    protected boolean getAccess_computed = false;
    protected boolean getArgList_computed = false;

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.getAccess_computed = false;
        this.getAccess_value = null;
        this.getArgList_computed = false;
        this.getArgList_value = null;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public EnumInstanceExpr clone() throws CloneNotSupportedException {
        EnumInstanceExpr node = (EnumInstanceExpr) super.clone();
        node.getAccess_computed = false;
        node.getAccess_value = null;
        node.getArgList_computed = false;
        node.getArgList_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            EnumInstanceExpr node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>, soot.JastAddJ.EnumInstanceExpr] */
    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
                    case 1:
                        copy.children[i] = null;
                        break;
                    case 2:
                        copy.children[i] = new List();
                        break;
                    default:
                        ASTNode child = this.children[i];
                        if (child != null) {
                            copy.setChild(child.fullCopy(), i);
                            break;
                        } else {
                            break;
                        }
                }
            }
        }
        return copy;
    }

    public EnumInstanceExpr() {
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new Opt(), 0);
        setChild(new List(), 2);
    }

    public EnumInstanceExpr(Opt<TypeDecl> p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setTypeDeclOpt(Opt<TypeDecl> opt) {
        setChild(opt, 0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public boolean hasTypeDecl() {
        return getTypeDeclOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public TypeDecl getTypeDecl() {
        return getTypeDeclOpt().getChild(0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setTypeDecl(TypeDecl node) {
        getTypeDeclOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Opt<TypeDecl> getTypeDeclOpt() {
        return (Opt) getChild(0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Opt<TypeDecl> getTypeDeclOptNoTransform() {
        return (Opt) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setAccess(Access node) {
        setChild(node, 1);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(1);
    }

    protected int getAccessChildPosition() {
        return 1;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setArgList(List<Expr> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public int getNumArg() {
        return getArgList().getNumChild();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgs() {
        return getArgList();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    protected int getArgListChildPosition() {
        return 2;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public Access getAccess() {
        if (this.getAccess_computed) {
            return (Access) getChild(getAccessChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getAccess_value = getAccess_compute();
        setAccess(this.getAccess_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getAccess_computed = true;
        }
        return (Access) getChild(getAccessChildPosition());
    }

    private Access getAccess_compute() {
        return hostType().createQualifiedAccess();
    }

    @Override // soot.JastAddJ.ClassInstanceExpr
    public List<Expr> getArgList() {
        if (this.getArgList_computed) {
            return (List) getChild(getArgListChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getArgList_value = getArgList_compute();
        setArgList(this.getArgList_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getArgList_computed = true;
        }
        return (List) getChild(getArgListChildPosition());
    }

    private List<Expr> getArgList_compute() {
        EnumConstant ec = (EnumConstant) getParent().getParent();
        List<EnumConstant> ecs = (List) ec.getParent();
        int idx = ecs.getIndexOfChild(ec);
        if (idx == -1) {
            throw new Error("internal: cannot determine numeric value of enum constant");
        }
        List<Expr> argList = new List<>();
        argList.add(Literal.buildStringLiteral(ec.name()));
        argList.add(Literal.buildIntegerLiteral(idx));
        Iterator<Expr> it = ec.getArgs().iterator();
        while (it.hasNext()) {
            Expr arg = it.next();
            argList.add((Expr) arg.fullCopy());
        }
        return argList;
    }

    @Override // soot.JastAddJ.ClassInstanceExpr, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
