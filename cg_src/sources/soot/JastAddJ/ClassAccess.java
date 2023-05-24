package soot.JastAddJ;

import java.util.ArrayList;
import soot.JastAddJ.ASTNode;
import soot.Local;
import soot.Value;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ClassAccess.class */
public class ClassAccess extends Access implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ClassAccess clone() throws CloneNotSupportedException {
        ClassAccess node = (ClassAccess) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ClassAccess node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: fullCopy */
    public ASTNode<ASTNode> fullCopy2() {
        ASTNode<ASTNode> copy2 = copy2();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy2.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy2;
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (isQualified() && !qualifier().isTypeAccess()) {
            error("class literal may only contain type names");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append("class");
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        super.transformation();
        if (isQualified() && qualifier().type().isReferenceType()) {
            hostType().topLevelType().createStaticClassMethod();
            hostType().topLevelType().createStaticClassField(prevExpr().type().referenceClassFieldName());
        }
    }

    @Override // soot.JastAddJ.Expr
    public Value eval(Body b) {
        if (prevExpr().type().isPrimitiveType() || prevExpr().type().isVoid()) {
            TypeDecl typeDecl = lookupType("java.lang", prevExpr().type().primitiveClassName());
            SimpleSet c = typeDecl.memberFields("TYPE");
            return b.newStaticFieldRef(((FieldDeclaration) c.iterator().next()).sootRef(), this);
        }
        FieldDeclaration f = hostType().topLevelType().createStaticClassField(prevExpr().type().referenceClassFieldName());
        MethodDecl m = hostType().topLevelType().createStaticClassMethod();
        soot.jimple.Stmt next_label = b.newLabel();
        soot.jimple.Stmt end_label = b.newLabel();
        Local result = b.newTemp(type().getSootType());
        Local ref = asLocal(b, b.newStaticFieldRef(f.sootRef(), this));
        b.setLine(this);
        b.add(b.newIfStmt(b.newNeExpr(ref, NullConstant.v(), this), next_label, this));
        ArrayList list = new ArrayList();
        list.add(new StringLiteral(prevExpr().type().jvmName()).eval(b));
        Local l = asLocal(b, b.newStaticInvokeExpr(m.sootRef(), list, this));
        b.setLine(this);
        b.add(b.newAssignStmt(b.newStaticFieldRef(f.sootRef(), this), l, this));
        b.setLine(this);
        b.add(b.newAssignStmt(result, l, this));
        b.add(b.newGotoStmt(end_label, this));
        b.addLabel(next_label);
        b.add(b.newAssignStmt(result, b.newStaticFieldRef(f.sootRef(), this), this));
        b.addLabel(end_label);
        return result;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    private TypeDecl refined_TypeAnalysis_ClassAccess_type() {
        return lookupType("java.lang", "Class");
    }

    @Override // soot.JastAddJ.Expr
    public boolean isClassAccess() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.TYPE_NAME;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr
    public TypeDecl type() {
        if (this.type_computed) {
            return this.type_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.type_value = type_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.type_computed = true;
        }
        return this.type_value;
    }

    private TypeDecl type_compute() {
        GenericClassDecl d = (GenericClassDecl) refined_TypeAnalysis_ClassAccess_type();
        TypeDecl type = qualifier().type();
        if (type.isPrimitiveType()) {
            type = type.boxed();
        }
        ArrayList list = new ArrayList();
        list.add(type);
        return d.lookupParTypeDecl(list);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
