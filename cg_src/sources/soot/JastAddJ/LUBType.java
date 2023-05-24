package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/LUBType.class */
public class LUBType extends ReferenceType implements Cloneable {
    protected TypeDecl lub_value;
    protected Map subtype_TypeDecl_values;
    protected SootClass getSootClassDecl_value;
    protected boolean lub_computed = false;
    protected boolean getSootClassDecl_computed = false;

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.lub_computed = false;
        this.lub_value = null;
        this.subtype_TypeDecl_values = null;
        this.getSootClassDecl_computed = false;
        this.getSootClassDecl_value = null;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public LUBType clone() throws CloneNotSupportedException {
        LUBType node = (LUBType) super.clone();
        node.lub_computed = false;
        node.lub_value = null;
        node.subtype_TypeDecl_values = null;
        node.getSootClassDecl_computed = false;
        node.getSootClassDecl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            LUBType node = clone();
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

    public static HashSet EC(ArrayList list) {
        HashSet result = new HashSet();
        boolean first = true;
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            TypeDecl U = (TypeDecl) iter.next();
            HashSet EST = EST(U);
            if (first) {
                result.addAll(EST);
                first = false;
            } else {
                result.retainAll(EST);
            }
        }
        return result;
    }

    public static HashSet MEC(ArrayList list) {
        HashSet EC = EC(list);
        if (EC.size() == 1) {
            return EC;
        }
        HashSet MEC = new HashSet();
        Iterator iter = EC.iterator();
        while (iter.hasNext()) {
            TypeDecl V = (TypeDecl) iter.next();
            boolean keep = true;
            Iterator i2 = EC.iterator();
            while (i2.hasNext()) {
                TypeDecl W = (TypeDecl) i2.next();
                if (!(V instanceof TypeVariable) && V != W && W.instanceOf(V)) {
                    keep = false;
                }
            }
            if (keep) {
                MEC.add(V);
            }
        }
        return MEC;
    }

    public static HashSet Inv(TypeDecl G, ArrayList Us) {
        HashSet result = new HashSet();
        Iterator iter = Us.iterator();
        while (iter.hasNext()) {
            TypeDecl U = (TypeDecl) iter.next();
            Iterator i2 = ST(U).iterator();
            while (i2.hasNext()) {
                TypeDecl V = (TypeDecl) i2.next();
                if ((V instanceof ParTypeDecl) && !V.isRawType() && ((ParTypeDecl) V).genericDecl() == G) {
                    result.add(V);
                }
            }
        }
        return result;
    }

    public TypeDecl lci(HashSet set, TypeDecl G) {
        ArrayList list = new ArrayList();
        boolean first = true;
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            ParTypeDecl decl = (ParTypeDecl) iter.next();
            if (first) {
                first = false;
                for (int i = 0; i < decl.getNumArgument(); i++) {
                    list.add(decl.getArgument(i).type());
                }
            } else {
                for (int i2 = 0; i2 < decl.getNumArgument(); i2++) {
                    list.set(i2, lcta((TypeDecl) list.get(i2), decl.getArgument(i2).type()));
                }
            }
        }
        return ((GenericTypeDecl) G).lookupParTypeDecl(list);
    }

    public TypeDecl lcta(TypeDecl X, TypeDecl Y) {
        if (!X.isWildcard() && !Y.isWildcard()) {
            return X == Y ? X : lub(X, Y).asWildcardExtends();
        } else if (!X.isWildcard() && (Y instanceof WildcardExtendsType)) {
            TypeDecl V = ((WildcardExtendsType) Y).getAccess().type();
            return lub(X, V).asWildcardExtends();
        } else if (!X.isWildcard() && (Y instanceof WildcardSuperType)) {
            TypeDecl V2 = ((WildcardSuperType) Y).getAccess().type();
            ArrayList bounds = new ArrayList();
            bounds.add(X);
            bounds.add(V2);
            return GLBTypeFactory.glb(bounds).asWildcardSuper();
        } else if ((X instanceof WildcardExtendsType) && (Y instanceof WildcardExtendsType)) {
            TypeDecl U = ((WildcardExtendsType) X).getAccess().type();
            TypeDecl V3 = ((WildcardExtendsType) Y).getAccess().type();
            return lub(U, V3).asWildcardExtends();
        } else if ((X instanceof WildcardExtendsType) && (Y instanceof WildcardSuperType)) {
            TypeDecl U2 = ((WildcardExtendsType) X).getAccess().type();
            TypeDecl V4 = ((WildcardSuperType) Y).getAccess().type();
            return U2 == V4 ? U2 : U2.typeWildcard();
        } else if ((X instanceof WildcardSuperType) && (Y instanceof WildcardSuperType)) {
            TypeDecl U3 = ((WildcardSuperType) X).getAccess().type();
            TypeDecl V5 = ((WildcardSuperType) Y).getAccess().type();
            ArrayList bounds2 = new ArrayList();
            bounds2.add(U3);
            bounds2.add(V5);
            return GLBTypeFactory.glb(bounds2).asWildcardSuper();
        } else {
            throw new Error("lcta not defined for (" + X.getClass().getName() + ", " + Y.getClass().getName());
        }
    }

    public TypeDecl lub(TypeDecl X, TypeDecl Y) {
        ArrayList list = new ArrayList(2);
        list.add(X);
        list.add(Y);
        return lub(list);
    }

    public TypeDecl lub(ArrayList list) {
        return lookupLUBType(list);
    }

    public static HashSet EST(TypeDecl t) {
        HashSet result = new HashSet();
        Iterator iter = ST(t).iterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            if (typeDecl instanceof TypeVariable) {
                result.add(typeDecl);
            } else {
                result.add(typeDecl.erasure());
            }
        }
        return result;
    }

    public static HashSet ST(TypeDecl t) {
        HashSet result = new HashSet();
        addSupertypes(result, t);
        return result;
    }

    public static void addSupertypes(HashSet set, TypeDecl t) {
        set.add(t);
        if (t instanceof ClassDecl) {
            ClassDecl type = (ClassDecl) t;
            if (type.hasSuperclass()) {
                addSupertypes(set, type.superclass());
            }
            for (int i = 0; i < type.getNumImplements(); i++) {
                addSupertypes(set, type.getImplements(i).type());
            }
        } else if (t instanceof InterfaceDecl) {
            InterfaceDecl type2 = (InterfaceDecl) t;
            for (int i2 = 0; i2 < type2.getNumSuperInterfaceId(); i2++) {
                addSupertypes(set, type2.getSuperInterfaceId(i2).type());
            }
            if (type2.getNumSuperInterfaceId() == 0) {
                set.add(type2.typeObject());
            }
        } else if (t instanceof TypeVariable) {
            TypeVariable type3 = (TypeVariable) t;
            for (int i3 = 0; i3 < type3.getNumTypeBound(); i3++) {
                addSupertypes(set, type3.getTypeBound(i3).type());
            }
            if (type3.getNumTypeBound() == 0) {
                set.add(type3.typeObject());
            }
        } else if (t instanceof LUBType) {
            LUBType type4 = (LUBType) t;
            for (int i4 = 0; i4 < type4.getNumTypeBound(); i4++) {
                addSupertypes(set, type4.getTypeBound(i4).type());
            }
            if (type4.getNumTypeBound() == 0) {
                set.add(type4.typeObject());
            }
        } else {
            throw new Error("Operation not supported for " + t.fullName() + ", " + t.getClass().getName());
        }
    }

    @Override // soot.JastAddJ.TypeDecl
    public HashSet implementedInterfaces() {
        HashSet ret = new HashSet();
        for (int i = 0; i < getNumTypeBound(); i++) {
            ret.addAll(getTypeBound(i).type().implementedInterfaces());
        }
        return ret;
    }

    public LUBType() {
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 1);
        setChild(new List(), 2);
    }

    public LUBType(Modifiers p0, String p1, List<BodyDecl> p2, List<Access> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public LUBType(Modifiers p0, Symbol p1, List<BodyDecl> p2, List<Access> p3) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void setTypeBoundList(List<Access> list) {
        setChild(list, 2);
    }

    public int getNumTypeBound() {
        return getTypeBoundList().getNumChild();
    }

    public int getNumTypeBoundNoTransform() {
        return getTypeBoundListNoTransform().getNumChildNoTransform();
    }

    public Access getTypeBound(int i) {
        return getTypeBoundList().getChild(i);
    }

    public void addTypeBound(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeBoundListNoTransform() : getTypeBoundList();
        list.addChild(node);
    }

    public void addTypeBoundNoTransform(Access node) {
        List<Access> list = getTypeBoundListNoTransform();
        list.addChild(node);
    }

    public void setTypeBound(Access node, int i) {
        List<Access> list = getTypeBoundList();
        list.setChild(node, i);
    }

    public List<Access> getTypeBounds() {
        return getTypeBoundList();
    }

    public List<Access> getTypeBoundsNoTransform() {
        return getTypeBoundListNoTransform();
    }

    public List<Access> getTypeBoundList() {
        List<Access> list = (List) getChild(2);
        list.getNumChild();
        return list;
    }

    public List<Access> getTypeBoundListNoTransform() {
        return (List) getChildNoTransform(2);
    }

    public TypeDecl lub() {
        if (this.lub_computed) {
            return this.lub_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.lub_value = lub_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.lub_computed = true;
        }
        return this.lub_value;
    }

    private TypeDecl lub_compute() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < getNumTypeBound(); i++) {
            list.add(getTypeBound(i).type());
        }
        ArrayList bounds = new ArrayList();
        Iterator iter = MEC(list).iterator();
        while (iter.hasNext()) {
            TypeDecl W = (TypeDecl) iter.next();
            TypeDecl C = W instanceof GenericTypeDecl ? lci(Inv(W, list), W) : W;
            bounds.add(C);
        }
        if (bounds.size() == 1) {
            return (TypeDecl) bounds.iterator().next();
        }
        return lookupLUBType(bounds);
    }

    @Override // soot.JastAddJ.TypeDecl
    public String typeName() {
        state();
        if (getNumTypeBound() == 0) {
            return "<NOTYPE>";
        }
        StringBuffer s = new StringBuffer();
        s.append(getTypeBound(0).type().typeName());
        for (int i = 1; i < getNumTypeBound(); i++) {
            s.append(" & " + getTypeBound(i).type().typeName());
        }
        return s.toString();
    }

    @Override // soot.JastAddJ.TypeDecl
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
        return type.supertypeLUBType(this);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeClassDecl(ClassDecl type) {
        state();
        return type.subtype(lub());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeInterfaceDecl(InterfaceDecl type) {
        state();
        return type.subtype(lub());
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeGLBType(GLBType type) {
        state();
        ArrayList bounds = new ArrayList(getNumTypeBound());
        for (int i = 0; i < getNumTypeBound(); i++) {
            bounds.add(getTypeBound(i));
        }
        return type == lookupGLBType(bounds);
    }

    @Override // soot.JastAddJ.TypeDecl
    public SootClass getSootClassDecl() {
        if (this.getSootClassDecl_computed) {
            return this.getSootClassDecl_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.getSootClassDecl_value = getSootClassDecl_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.getSootClassDecl_computed = true;
        }
        return this.getSootClassDecl_value;
    }

    private SootClass getSootClassDecl_compute() {
        return typeObject().getSootClassDecl();
    }

    @Override // soot.JastAddJ.ReferenceType, soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
