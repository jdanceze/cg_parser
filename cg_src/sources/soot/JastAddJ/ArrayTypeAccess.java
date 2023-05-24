package soot.JastAddJ;

import java.util.ArrayList;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ArrayTypeAccess.class */
public class ArrayTypeAccess extends TypeAccess implements Cloneable {
    protected String tokenString_Package;
    protected String tokenString_ID;
    protected String getPackage_value;
    protected String getID_value;
    protected TypeDecl decl_value;
    protected boolean getPackage_computed = false;
    protected boolean getID_computed = false;
    protected boolean decl_computed = false;

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.getPackage_computed = false;
        this.getPackage_value = null;
        this.getID_computed = false;
        this.getID_value = null;
        this.decl_computed = false;
        this.decl_value = null;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ArrayTypeAccess clone() throws CloneNotSupportedException {
        ArrayTypeAccess node = (ArrayTypeAccess) super.clone();
        node.getPackage_computed = false;
        node.getPackage_value = null;
        node.getID_computed = false;
        node.getID_value = null;
        node.decl_computed = false;
        node.decl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ArrayTypeAccess node = clone();
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
    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (decl().elementType().isUnknown()) {
            error("no type named " + decl().elementType().typeName());
        }
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getAccess().toString(s);
        s.append("[]");
    }

    @Override // soot.JastAddJ.Access
    public void addArraySize(Body b, ArrayList list) {
        getAccess().addArraySize(b, list);
    }

    public ArrayTypeAccess() {
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ArrayTypeAccess(Access p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setAccess(Access node) {
        setChild(node, 0);
    }

    public Access getAccess() {
        return (Access) getChild(0);
    }

    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setPackage(String value) {
        this.tokenString_Package = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getPackage() {
        if (this.getPackage_computed) {
            return this.getPackage_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getPackage_value = getPackage_compute();
        setPackage(this.getPackage_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getPackage_computed = true;
        }
        return this.getPackage_value;
    }

    private String getPackage_compute() {
        return getAccess().type().packageName();
    }

    @Override // soot.JastAddJ.TypeAccess
    public String getID() {
        if (this.getID_computed) {
            return this.getID_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getID_value = getID_compute();
        setID(this.getID_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getID_computed = true;
        }
        return this.getID_value;
    }

    private String getID_compute() {
        return getAccess().type().name();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDAafter(Variable v) {
        state();
        return getAccess().isDAafter(v);
    }

    @Override // soot.JastAddJ.Expr
    public boolean isDUafter(Variable v) {
        state();
        return getAccess().isDUafter(v);
    }

    @Override // soot.JastAddJ.TypeAccess
    public TypeDecl decl() {
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

    private TypeDecl decl_compute() {
        return getAccess().type().arrayType();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return getClass().getName();
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.AMBIGUOUS_NAME;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Expr
    public boolean staticContextQualifier() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeAccess, soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
