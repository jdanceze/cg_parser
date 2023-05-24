package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/RawInterfaceDecl.class */
public class RawInterfaceDecl extends ParInterfaceDecl implements Cloneable {
    protected boolean getArgumentList_computed = false;
    protected List getArgumentList_value;
    protected Map subtype_TypeDecl_values;
    protected Map instanceOf_TypeDecl_values;

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.getArgumentList_computed = false;
        this.getArgumentList_value = null;
        this.subtype_TypeDecl_values = null;
        this.instanceOf_TypeDecl_values = null;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public RawInterfaceDecl clone() throws CloneNotSupportedException {
        RawInterfaceDecl node = (RawInterfaceDecl) super.clone();
        node.getArgumentList_computed = false;
        node.getArgumentList_value = null;
        node.subtype_TypeDecl_values = null;
        node.instanceOf_TypeDecl_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            RawInterfaceDecl node = clone();
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
    /* JADX WARN: Type inference failed for: r0v1, types: [soot.JastAddJ.RawInterfaceDecl, soot.JastAddJ.ASTNode<soot.JastAddJ.ASTNode>] */
    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ?? copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                switch (i) {
                    case 2:
                    case 3:
                    case 4:
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

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.TypeDecl
    public Access substitute(Parameterization parTypeDecl) {
        return createBoundAccess();
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substituteReturnType(Parameterization parTypeDecl) {
        return createBoundAccess();
    }

    @Override // soot.JastAddJ.TypeDecl
    public Access substituteParameterType(Parameterization parTypeDecl) {
        return createBoundAccess();
    }

    public RawInterfaceDecl() {
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[4];
        setChild(new List(), 1);
        setChild(new List(), 2);
        setChild(new List(), 3);
    }

    public RawInterfaceDecl(Modifiers p0, String p1) {
        setChild(p0, 0);
        setID(p1);
    }

    public RawInterfaceDecl(Modifiers p0, Symbol p1) {
        setChild(p0, 0);
        setID(p1);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public void setSuperInterfaceIdList(List<Access> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public int getNumSuperInterfaceId() {
        return getSuperInterfaceIdList().getNumChild();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public int getNumSuperInterfaceIdNoTransform() {
        return getSuperInterfaceIdListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public Access getSuperInterfaceId(int i) {
        return (Access) getSuperInterfaceIdList().getChild(i);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public void addSuperInterfaceId(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getSuperInterfaceIdListNoTransform() : getSuperInterfaceIdList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public void addSuperInterfaceIdNoTransform(Access node) {
        List<Access> list = getSuperInterfaceIdListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public void setSuperInterfaceId(Access node, int i) {
        List<Access> list = getSuperInterfaceIdList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIds() {
        return getSuperInterfaceIdList();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIdsNoTransform() {
        return getSuperInterfaceIdListNoTransform();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl
    public List<Access> getSuperInterfaceIdListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    protected int getSuperInterfaceIdListChildPosition() {
        return 1;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 2);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return (BodyDecl) getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    protected int getBodyDeclListChildPosition() {
        return 2;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public void setArgumentList(List<Access> list) {
        setChild(list, 3);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.ParTypeDecl
    public int getNumArgument() {
        return getArgumentList().getNumChild();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public int getNumArgumentNoTransform() {
        return getArgumentListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.ParTypeDecl
    public Access getArgument(int i) {
        return (Access) getArgumentList().getChild(i);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public void addArgument(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getArgumentListNoTransform() : getArgumentList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public void addArgumentNoTransform(Access node) {
        List<Access> list = getArgumentListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public void setArgument(Access node, int i) {
        List<Access> list = getArgumentList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public List<Access> getArguments() {
        return getArgumentList();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public List<Access> getArgumentsNoTransform() {
        return getArgumentListNoTransform();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public List<Access> getArgumentListNoTransform() {
        return (List) getChildNoTransform(3);
    }

    protected int getArgumentListChildPosition() {
        return 3;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.TypeDecl
    public TypeDecl hostType() {
        state();
        return original();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl, soot.JastAddJ.Parameterization
    public boolean isRawType() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.TypeDecl, soot.JastAddJ.ParTypeDecl
    public boolean sameSignature(Access a) {
        state();
        return (a instanceof TypeAccess) && a.type() == this;
    }

    @Override // soot.JastAddJ.ParInterfaceDecl
    public List getArgumentList() {
        if (this.getArgumentList_computed) {
            return (List) getChild(getArgumentListChildPosition());
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.getArgumentList_value = getArgumentList_compute();
        setArgumentList(this.getArgumentList_value);
        this.getArgumentList_computed = true;
        return (List) getChild(getArgumentListChildPosition());
    }

    private List getArgumentList_compute() {
        return ((GenericInterfaceDecl) genericDecl()).createArgumentList(new ArrayList());
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.ParTypeDecl
    public String nameWithArgs() {
        state();
        return name();
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean supertypeGenericInterfaceDecl(GenericInterfaceDecl type) {
        state();
        return type.subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean subtype(TypeDecl type) {
        ASTNode.State.CircularValue _value;
        boolean new_subtype_TypeDecl_value;
        if (this.subtype_TypeDecl_values == null) {
            this.subtype_TypeDecl_values = new HashMap(4);
        }
        if (this.subtype_TypeDecl_values.containsKey(type)) {
            Object _o = this.subtype_TypeDecl_values.get(type);
            if (!(_o instanceof ASTNode.State.CircularValue)) {
                return ((Boolean) _o).booleanValue();
            }
            _value = (ASTNode.State.CircularValue) _o;
        } else {
            _value = new ASTNode.State.CircularValue();
            this.subtype_TypeDecl_values.put(type, _value);
            _value.value = true;
        }
        ASTNode.State state = state();
        if (!state.IN_CIRCLE) {
            state.IN_CIRCLE = true;
            int num = state.boundariesCrossed;
            boolean isFinal = is$Final();
            do {
                _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
                state.CHANGE = false;
                new_subtype_TypeDecl_value = subtype_compute(type);
                if (new_subtype_TypeDecl_value != ((Boolean) _value.value).booleanValue()) {
                    state.CHANGE = true;
                    _value.value = Boolean.valueOf(new_subtype_TypeDecl_value);
                }
                state.CIRCLE_INDEX++;
            } while (state.CHANGE);
            if (!isFinal || num != state().boundariesCrossed) {
                this.subtype_TypeDecl_values.remove(type);
                state.RESET_CYCLE = true;
                subtype_compute(type);
                state.RESET_CYCLE = false;
            } else {
                this.subtype_TypeDecl_values.put(type, Boolean.valueOf(new_subtype_TypeDecl_value));
            }
            state.IN_CIRCLE = false;
            return new_subtype_TypeDecl_value;
        } else if (!new Integer(state.CIRCLE_INDEX).equals(Integer.valueOf(_value.visited))) {
            _value.visited = new Integer(state.CIRCLE_INDEX).intValue();
            boolean new_subtype_TypeDecl_value2 = subtype_compute(type);
            if (state.RESET_CYCLE) {
                this.subtype_TypeDecl_values.remove(type);
            } else if (new_subtype_TypeDecl_value2 != ((Boolean) _value.value).booleanValue()) {
                state.CHANGE = true;
                _value.value = Boolean.valueOf(new_subtype_TypeDecl_value2);
            }
            return new_subtype_TypeDecl_value2;
        } else {
            return ((Boolean) _value.value).booleanValue();
        }
    }

    private boolean subtype_compute(TypeDecl type) {
        return type.supertypeRawInterfaceDecl(this);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        return type.subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        return type.subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
        state();
        return type.genericDecl().original().subtype(genericDecl().original());
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.TypeDecl
    public boolean instanceOf(TypeDecl type) {
        if (this.instanceOf_TypeDecl_values == null) {
            this.instanceOf_TypeDecl_values = new HashMap(4);
        }
        if (this.instanceOf_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.instanceOf_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean instanceOf_TypeDecl_value = instanceOf_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.instanceOf_TypeDecl_values.put(type, Boolean.valueOf(instanceOf_TypeDecl_value));
        }
        return instanceOf_TypeDecl_value;
    }

    private boolean instanceOf_compute(TypeDecl type) {
        return subtype(type);
    }

    @Override // soot.JastAddJ.ParInterfaceDecl, soot.JastAddJ.InterfaceDecl, soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
