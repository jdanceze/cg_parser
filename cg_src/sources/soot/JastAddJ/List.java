package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/List.class */
public class List<T extends ASTNode> extends ASTNode<T> implements Cloneable {
    private boolean list$touched = true;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public List<T> clone() throws CloneNotSupportedException {
        List node = (List) super.mo287clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    public List<T> copy() {
        try {
            List node = clone();
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
    public List<T> fullCopy() {
        List tree = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    tree.setChild(child.fullCopy(), i);
                }
            }
        }
        return tree;
    }

    /* JADX WARN: Type inference failed for: r3v11, types: [soot.JastAddJ.Modifiers] */
    /* JADX WARN: Type inference failed for: r3v8, types: [soot.JastAddJ.Modifiers] */
    public List substitute(Parameterization parTypeDecl) {
        List list = new List();
        for (int i = 0; i < getNumChild(); i++) {
            ASTNode node = getChild(i);
            if (node instanceof Access) {
                Access a = (Access) node;
                list.add(a.type().substitute(parTypeDecl));
            } else if (node instanceof VariableArityParameterDeclaration) {
                VariableArityParameterDeclaration p = (VariableArityParameterDeclaration) node;
                list.add(new VariableArityParameterDeclarationSubstituted((Modifiers) p.getModifiers().fullCopy2(), p.getTypeAccess().type().substituteParameterType(parTypeDecl), p.getID(), p));
            } else if (node instanceof ParameterDeclaration) {
                ParameterDeclaration p2 = (ParameterDeclaration) node;
                list.add(new ParameterDeclarationSubstituted((Modifiers) p2.getModifiers().fullCopy2(), p2.type().substituteParameterType(parTypeDecl), p2.getID(), p2));
            } else {
                throw new Error("Can only substitute lists of access nodes but node number " + i + " is of type " + node.getClass().getName());
            }
        }
        return list;
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public List<T> add(T node) {
        addChild(node);
        return this;
    }

    @Override // soot.JastAddJ.ASTNode
    public void insertChild(ASTNode node, int i) {
        this.list$touched = true;
        super.insertChild(node, i);
    }

    @Override // soot.JastAddJ.ASTNode
    public void addChild(T node) {
        this.list$touched = true;
        super.addChild(node);
    }

    @Override // soot.JastAddJ.ASTNode
    public void removeChild(int i) {
        this.list$touched = true;
        super.removeChild(i);
    }

    @Override // soot.JastAddJ.ASTNode
    public int getNumChild() {
        if (this.list$touched) {
            for (int i = 0; i < getNumChildNoTransform(); i++) {
                getChild(i);
            }
            this.list$touched = false;
        }
        return getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    public boolean requiresDefaultConstructor() {
        state();
        if (getParent() instanceof ClassDecl) {
            ClassDecl c = (ClassDecl) getParent();
            return c.noConstructor() && c.getBodyDeclListNoTransform() == this && !(c instanceof AnonymousDecl);
        }
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean definesLabel() {
        state();
        return getParent().definesLabel();
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (this.list$touched) {
            for (int i = 0; i < getNumChildNoTransform(); i++) {
                getChild(i);
            }
            this.list$touched = false;
            return this;
        } else if (requiresDefaultConstructor()) {
            state().duringImplicitConstructor++;
            ASTNode result = rewriteRule0();
            state().duringImplicitConstructor--;
            return result;
        } else {
            return super.rewriteTo();
        }
    }

    private List rewriteRule0() {
        ClassDecl c = (ClassDecl) getParent();
        Modifiers m = new Modifiers();
        if (c.isPublic()) {
            m.addModifier(new Modifier(Jimple.PUBLIC));
        } else if (c.isProtected()) {
            m.addModifier(new Modifier(Jimple.PROTECTED));
        } else if (c.isPrivate()) {
            m.addModifier(new Modifier(Jimple.PRIVATE));
        }
        ConstructorDecl constructor = new ConstructorDecl(m, c.name(), new List(), new List(), new Opt(), new Block());
        constructor.setDefaultConstructor();
        c.addBodyDecl(constructor);
        return this;
    }
}
