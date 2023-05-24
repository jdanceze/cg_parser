package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AnnotationDecl.class */
public class AnnotationDecl extends InterfaceDecl implements Cloneable {
    protected boolean getSuperInterfaceIdList_computed = false;
    protected List getSuperInterfaceIdList_value;
    protected Map containsElementOf_TypeDecl_values;

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.getSuperInterfaceIdList_computed = false;
        this.getSuperInterfaceIdList_value = null;
        this.containsElementOf_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public AnnotationDecl clone() throws CloneNotSupportedException {
        AnnotationDecl node = (AnnotationDecl) super.clone();
        node.getSuperInterfaceIdList_computed = false;
        node.getSuperInterfaceIdList_value = null;
        node.containsElementOf_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            AnnotationDecl node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>, soot.JastAddJ.AnnotationDecl] */
    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
                    case 3:
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

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void typeCheck() {
        super.typeCheck();
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof MethodDecl) {
                MethodDecl m = (MethodDecl) getBodyDecl(i);
                if (!m.type().isValidAnnotationMethodReturnType()) {
                    m.error("invalid type for annotation member");
                }
                if (m.annotationMethodOverride()) {
                    m.error("annotation method overrides " + m.signature());
                }
            }
        }
        if (containsElementOf(this)) {
            error("cyclic annotation element type");
        }
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getModifiers().toString(s);
        s.append("@interface " + name());
        s.append(" {");
        for (int i = 0; i < getNumBodyDecl(); i++) {
            getBodyDecl(i).toString(s);
        }
        s.append(String.valueOf(indent()) + "}");
    }

    public AnnotationDecl() {
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
        setChild(new List(), 2);
    }

    public AnnotationDecl(Modifiers p0, String p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    public AnnotationDecl(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void setSuperInterfaceIdList(List<Access> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public int getNumSuperInterfaceId() {
        return getSuperInterfaceIdList().getNumChild();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public int getNumSuperInterfaceIdNoTransform() {
        return getSuperInterfaceIdListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public Access getSuperInterfaceId(int i) {
        return (Access) getSuperInterfaceIdList().getChild(i);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void addSuperInterfaceId(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getSuperInterfaceIdListNoTransform() : getSuperInterfaceIdList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void addSuperInterfaceIdNoTransform(Access node) {
        List<Access> list = getSuperInterfaceIdListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public void setSuperInterfaceId(Access node, int i) {
        List<Access> list = getSuperInterfaceIdList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIds() {
        return getSuperInterfaceIdList();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIdsNoTransform() {
        return getSuperInterfaceIdListNoTransform();
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIdListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    protected int getSuperInterfaceIdListChildPosition() {
        return 2;
    }

    @Override // soot.JastAddJ.InterfaceDecl
    public List getSuperInterfaceIdList() {
        if (this.getSuperInterfaceIdList_computed) {
            return (List) getChild(getSuperInterfaceIdListChildPosition());
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getSuperInterfaceIdList_value = getSuperInterfaceIdList_compute();
        setSuperInterfaceIdList(this.getSuperInterfaceIdList_value);
        if (isFinal && num == state().boundariesCrossed) {
            this.getSuperInterfaceIdList_computed = true;
        }
        return (List) getChild(getSuperInterfaceIdListChildPosition());
    }

    private List getSuperInterfaceIdList_compute() {
        return new List().add(new TypeAccess("java.lang.annotation", "Annotation"));
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public boolean isValidAnnotationMethodReturnType() {
        state();
        return true;
    }

    public boolean containsElementOf(TypeDecl typeDecl) {
        ASTNode.State.CircularValue _value;
        boolean new_containsElementOf_TypeDecl_value;
        if (this.containsElementOf_TypeDecl_values == null) {
            this.containsElementOf_TypeDecl_values = new HashMap(4);
        }
        if (this.containsElementOf_TypeDecl_values.containsKey(typeDecl)) {
            Object _o = this.containsElementOf_TypeDecl_values.get(typeDecl);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.containsElementOf_TypeDecl_values.put(typeDecl, _value);
            _value.value = false;
        }
        ASTNode.State state = state();
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
                state.CHANGE = false;
                new_containsElementOf_TypeDecl_value = containsElementOf_compute(typeDecl);
                if (new_containsElementOf_TypeDecl_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_containsElementOf_TypeDecl_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.containsElementOf_TypeDecl_values.remove(typeDecl);
                state.RESET_CYCLE = true;
                containsElementOf_compute(typeDecl);
                state.RESET_CYCLE = false;
            } else {
                this.containsElementOf_TypeDecl_values.put(typeDecl, Boolean.valueOf(new_containsElementOf_TypeDecl_value));
            }
            state.IN_CIRCLE = false;
            return new_containsElementOf_TypeDecl_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_containsElementOf_TypeDecl_value2 = containsElementOf_compute(typeDecl);
            if (state.RESET_CYCLE) {
                this.containsElementOf_TypeDecl_values.remove(typeDecl);
            } else if (new_containsElementOf_TypeDecl_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_containsElementOf_TypeDecl_value2);
            }
            return new_containsElementOf_TypeDecl_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean containsElementOf_compute(TypeDecl typeDecl) {
        for (int i = 0; i < getNumBodyDecl(); i++) {
            if (getBodyDecl(i) instanceof MethodDecl) {
                MethodDecl m = (MethodDecl) getBodyDecl(i);
                if (m.type() == typeDecl) {
                    return true;
                }
                if ((m.type() instanceof AnnotationDecl) && ((AnnotationDecl) m.type()).containsElementOf(typeDecl)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isAnnotationDecl() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public int sootTypeModifiers() {
        state();
        return super.sootTypeModifiers() | 8192;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getModifiersNoTransform()) {
            return name.equals("ANNOTATION_TYPE") || name.equals("TYPE");
        }
        return super.Define_boolean_mayUseAnnotationTarget(caller, child, name);
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
