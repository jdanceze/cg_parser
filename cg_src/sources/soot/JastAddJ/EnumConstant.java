package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Iterator;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/EnumConstant.class */
public class EnumConstant extends FieldDeclaration implements Cloneable {
    protected boolean getTypeAccess_computed;
    protected Access getTypeAccess_value;
    protected boolean localMethodsSignatureMap_computed;
    protected HashMap localMethodsSignatureMap_value;

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.getTypeAccess_computed = false;
        this.getTypeAccess_value = null;
        this.localMethodsSignatureMap_computed = false;
        this.localMethodsSignatureMap_value = null;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public EnumConstant clone() throws CloneNotSupportedException {
        EnumConstant node = (EnumConstant) super.clone();
        node.getTypeAccess_computed = false;
        node.getTypeAccess_value = null;
        node.localMethodsSignatureMap_computed = false;
        node.localMethodsSignatureMap_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            EnumConstant node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>, soot.JastAddJ.EnumConstant] */
    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
                    case 4:
                        copy.children[i] = null;
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

    public EnumConstant(Modifiers mods, String name, List<Expr> args, List<BodyDecl> bds) {
        this(mods, name, args, new Opt(new EnumInstanceExpr(createOptAnonymousDecl(bds))));
    }

    private static Opt<TypeDecl> createOptAnonymousDecl(List<BodyDecl> bds) {
        if (bds.getNumChildNoTransform() == 0) {
            return new Opt<>();
        }
        return new Opt<>(new AnonymousDecl(new Modifiers(), "Anonymous", bds));
    }

    public int getNumBodyDecl() {
        int cnt = 0;
        ClassInstanceExpr init = (ClassInstanceExpr) getInit();
        if (!init.hasTypeDecl()) {
            return 0;
        }
        Iterator<BodyDecl> it = init.getTypeDecl().getBodyDecls().iterator();
        while (it.hasNext()) {
            BodyDecl bd = it.next();
            if (!(bd instanceof ConstructorDecl)) {
                cnt++;
            }
        }
        return cnt;
    }

    public BodyDecl getBodyDecl(int i) {
        ClassInstanceExpr init = (ClassInstanceExpr) getInit();
        if (init.hasTypeDecl()) {
            Iterator<BodyDecl> it = init.getTypeDecl().getBodyDecls().iterator();
            while (it.hasNext()) {
                BodyDecl bd = it.next();
                if (!(bd instanceof ConstructorDecl)) {
                    int i2 = i;
                    i--;
                    if (i2 == 0) {
                        return bd;
                    }
                }
            }
        }
        throw new ArrayIndexOutOfBoundsException(i);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(indent());
        getModifiers().toString(s);
        s.append(getID());
        s.append("(");
        if (getNumArg() > 0) {
            getArg(0).toString(s);
            for (int i = 1; i < getNumArg(); i++) {
                s.append(", ");
                getArg(i).toString(s);
            }
        }
        s.append(")");
        if (getNumBodyDecl() > 0) {
            s.append(" {");
            for (int i2 = 0; i2 < getNumBodyDecl(); i2++) {
                BodyDecl d = getBodyDecl(i2);
                d.toString(s);
            }
            s.append(String.valueOf(indent()) + "}");
        }
        s.append(",\n");
    }

    public EnumConstant() {
        this.getTypeAccess_computed = false;
        this.localMethodsSignatureMap_computed = false;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 1);
        setChild(new Opt(), 2);
    }

    public EnumConstant(Modifiers p0, String p1, List<Expr> p2, Opt<Expr> p3) {
        this.getTypeAccess_computed = false;
        this.localMethodsSignatureMap_computed = false;
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public EnumConstant(Modifiers p0, Symbol p1, List<Expr> p2, Opt<Expr> p3) {
        this.getTypeAccess_computed = false;
        this.localMethodsSignatureMap_computed = false;
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    public void setArgList(List<Expr> list) {
        setChild(list, 1);
    }

    public int getNumArg() {
        return getArgList().getNumChild();
    }

    public int getNumArgNoTransform() {
        return getArgListNoTransform().getNumChildNoTransform();
    }

    public Expr getArg(int i) {
        return getArgList().getChild(i);
    }

    public void addArg(Expr node) {
        List<Expr> list = (this.parent == null || state == null) ? getArgListNoTransform() : getArgList();
        list.addChild(node);
    }

    public void addArgNoTransform(Expr node) {
        List<Expr> list = getArgListNoTransform();
        list.addChild(node);
    }

    public void setArg(Expr node, int i) {
        List<Expr> list = getArgList();
        list.setChild(node, i);
    }

    public List<Expr> getArgs() {
        return getArgList();
    }

    public List<Expr> getArgsNoTransform() {
        return getArgListNoTransform();
    }

    public List<Expr> getArgList() {
        List<Expr> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<Expr> getArgListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setInitOpt(Opt<Expr> opt) {
        setChild(opt, 2);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public boolean hasInit() {
        return getInitOpt().getNumChild() != 0;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public Expr getInit() {
        return getInitOpt().getChild(0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setInit(Expr node) {
        getInitOpt().setChild(node, 0);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Opt<Expr> getInitOpt() {
        return (Opt) getChild(2);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Opt<Expr> getInitOptNoTransform() {
        return (Opt) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public void setTypeAccess(Access node) {
        setChild(node, 3);
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(3);
    }

    protected int getTypeAccessChildPosition() {
        return 3;
    }

    @Override // soot.JastAddJ.BodyDecl
    public boolean isEnumConstant() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public boolean isPublic() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.Variable
    public boolean isStatic() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.Variable
    public boolean isFinal() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public Access getTypeAccess() {
        if (this.getTypeAccess_computed) {
            return (Access) getChild(getTypeAccessChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getTypeAccess_value = getTypeAccess_compute();
        setTypeAccess(this.getTypeAccess_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getTypeAccess_computed = true;
        }
        return (Access) getChild(getTypeAccessChildPosition());
    }

    private Access getTypeAccess_compute() {
        return hostType().createQualifiedAccess();
    }

    public SimpleSet localMethodsSignature(String signature) {
        state();
        SimpleSet set = (SimpleSet) localMethodsSignatureMap().get(signature);
        return set != null ? set : SimpleSet.emptySet;
    }

    public HashMap localMethodsSignatureMap() {
        if (this.localMethodsSignatureMap_computed) {
            return this.localMethodsSignatureMap_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.localMethodsSignatureMap_value = localMethodsSignatureMap_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.localMethodsSignatureMap_computed = true;
        }
        return this.localMethodsSignatureMap_value;
    }

    private HashMap localMethodsSignatureMap_compute() {
        HashMap map = new HashMap(getNumBodyDecl());
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof MethodDecl) {
                MethodDecl decl = (MethodDecl) getBodyDecl(i);
                map.put(decl.signature(), decl);
            }
        }
        return map;
    }

    public boolean implementsMethod(MethodDecl method) {
        state();
        SimpleSet set = localMethodsSignature(method.signature());
        if (set.size() == 1) {
            MethodDecl n = (MethodDecl) set.iterator().next();
            if (!n.isAbstract()) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // soot.JastAddJ.FieldDeclaration
    public int sootTypeModifiers() {
        state();
        return super.sootTypeModifiers() | 16384;
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getTypeAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return super.Define_NameType_nameType(caller, child);
    }

    @Override // soot.JastAddJ.FieldDeclaration, soot.JastAddJ.MemberDecl, soot.JastAddJ.BodyDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
