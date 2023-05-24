package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import soot.JastAddJ.ASTNode;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/TypeAccess.class */
public class TypeAccess extends Access implements Cloneable {
    protected String tokenString_Package;
    public int Packagestart;
    public int Packageend;
    protected String tokenString_ID;
    public int IDstart;
    public int IDend;
    protected boolean decls_computed;
    protected SimpleSet decls_value;
    protected boolean decl_computed;
    protected TypeDecl decl_value;
    protected boolean type_computed;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decls_computed = false;
        this.decls_value = null;
        this.decl_computed = false;
        this.decl_value = null;
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public TypeAccess clone() throws CloneNotSupportedException {
        TypeAccess node = (TypeAccess) super.clone();
        node.decls_computed = false;
        node.decls_value = null;
        node.decl_computed = false;
        node.decl_value = null;
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            TypeAccess node = clone();
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
    @Override // soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.ASTNode
    public void accessControl() {
        super.accessControl();
        TypeDecl hostType = hostType();
        if (hostType != null && !hostType.isUnknown() && !type().accessibleFrom(hostType)) {
            error(this + " in " + hostType().fullName() + " can not access type " + type().fullName());
        } else if ((hostType == null || hostType.isUnknown()) && !type().accessibleFromPackage(hostPackage())) {
            error(this + " can not access type " + type().fullName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        if (isQualified() && !qualifier().isTypeAccess() && !qualifier().isPackageAccess()) {
            error("can not access the type named " + decl().typeName() + " in this context");
        }
        if (decls().isEmpty()) {
            error("no visible type named " + typeName());
        }
        if (decls().size() > 1) {
            StringBuffer s = new StringBuffer();
            s.append("several types named " + name() + ":");
            for (TypeDecl t : decls()) {
                s.append(Instruction.argsep + t.typeName());
            }
            error(s.toString());
        }
    }

    public TypeAccess(String name, int start, int end) {
        this(name);
        this.IDstart = start;
        this.start = start;
        this.IDend = end;
        this.end = end;
    }

    public TypeAccess(String typeName) {
        this("", typeName);
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        if (decl().isReferenceType()) {
            s.append(nameWithPackage());
        } else {
            s.append(decl().name());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void checkModifiers() {
        if (decl().isDeprecated() && !withinDeprecatedAnnotation()) {
            if ((hostType() == null || hostType().topLevelType() != decl().topLevelType()) && !withinSuppressWarnings("deprecation")) {
                warning(String.valueOf(decl().typeName()) + " has been deprecated");
            }
        }
    }

    public boolean isRaw() {
        ASTNode parent;
        ASTNode parent2 = getParent();
        while (true) {
            parent = parent2;
            if (!(parent instanceof AbstractDot)) {
                break;
            }
            parent2 = parent.getParent();
        }
        if ((parent instanceof ParTypeAccess) || (parent instanceof ImportDecl)) {
            return false;
        }
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        TypeDecl type = type();
        if (type.isRawType() && type.isNestedType() && type.enclosingType().isParameterizedType() && !type.enclosingType().isRawType()) {
            error("Can not access a member type of a paramterized type as a raw type");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        super.transformation();
        if (type().elementType().isNestedType() && hostType() != null) {
            hostType().addUsedNestedType(type().elementType());
        }
    }

    public TypeAccess() {
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    public TypeAccess(String p0, String p1) {
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
        setPackage(p0);
        setID(p1);
    }

    public TypeAccess(Symbol p0, Symbol p1) {
        this.decls_computed = false;
        this.decl_computed = false;
        this.type_computed = false;
        setPackage(p0);
        setID(p1);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setPackage(String value) {
        this.tokenString_Package = value;
    }

    public void setPackage(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setPackage is only valid for String lexemes");
        }
        this.tokenString_Package = (String) symbol.value;
        this.Packagestart = symbol.getStart();
        this.Packageend = symbol.getEnd();
    }

    public String getPackage() {
        return this.tokenString_Package != null ? this.tokenString_Package : "";
    }

    public void setID(String value) {
        this.tokenString_ID = value;
    }

    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    private TypeDecl refined_TypeScopePropagation_TypeAccess_decl() {
        SimpleSet decls = decls();
        if (decls.size() == 1) {
            return (TypeDecl) decls.iterator().next();
        }
        return unknownType();
    }

    public SimpleSet decls() {
        if (this.decls_computed) {
            return this.decls_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.decls_value = decls_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.decls_computed = true;
        }
        return this.decls_value;
    }

    private SimpleSet decls_compute() {
        if (packageName().equals("")) {
            return lookupType(name());
        }
        TypeDecl typeDecl = lookupType(packageName(), name());
        if (typeDecl != null) {
            return SimpleSet.emptySet.add(typeDecl);
        }
        return SimpleSet.emptySet;
    }

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
        TypeDecl decl = refined_TypeScopePropagation_TypeAccess_decl();
        if ((decl instanceof GenericTypeDecl) && isRaw()) {
            return ((GenericTypeDecl) decl).lookupParTypeDecl(new ArrayList());
        }
        return decl;
    }

    @Override // soot.JastAddJ.Expr
    public SimpleSet qualifiedLookupVariable(String name) {
        state();
        if (type().accessibleFrom(hostType())) {
            SimpleSet c = keepAccessibleFields(type().memberFields(name));
            if (type().isClassDecl() && c.size() == 1) {
                c = removeInstanceVariables(c);
            }
            return c;
        }
        return SimpleSet.emptySet;
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getPackage() + ", " + getID() + "]";
    }

    public String name() {
        state();
        return getID();
    }

    @Override // soot.JastAddJ.Expr
    public String packageName() {
        state();
        return getPackage();
    }

    public String nameWithPackage() {
        state();
        return getPackage().equals("") ? name() : String.valueOf(getPackage()) + "." + name();
    }

    @Override // soot.JastAddJ.Expr
    public String typeName() {
        state();
        return isQualified() ? String.valueOf(qualifier().typeName()) + "." + name() : nameWithPackage();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isTypeAccess() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Access
    public NameType predNameType() {
        state();
        return NameType.PACKAGE_OR_TYPE_NAME;
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
        return decl();
    }

    @Override // soot.JastAddJ.Expr
    public boolean staticContextQualifier() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean usesTypeVariable() {
        state();
        return decl().usesTypeVariable() || super.usesTypeVariable();
    }

    @Override // soot.JastAddJ.Access
    public Access substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
        state();
        TypeDecl decl = decl();
        int i = 0;
        for (TypeVariable typeVar : original) {
            if (typeVar == decl) {
                return new TypeAccess(substitution.getChild(i).getID());
            }
            i++;
        }
        return super.substituted(original, substitution);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
