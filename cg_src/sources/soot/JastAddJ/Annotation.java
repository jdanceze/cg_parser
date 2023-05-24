package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import soot.JastAddJ.ASTNode;
import soot.tagkit.AnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Annotation.class */
public class Annotation extends Modifier implements Cloneable {
    protected boolean decl_computed = false;
    protected TypeDecl decl_value;

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.decl_computed = false;
        this.decl_value = null;
    }

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode, beaver.Symbol
    public Annotation clone() throws CloneNotSupportedException {
        Annotation node = (Annotation) super.clone();
        node.decl_computed = false;
        node.decl_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            Annotation node = clone();
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
    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
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
    public void checkModifiers() {
        AnnotationDecl T;
        Annotation m;
        super.checkModifiers();
        if ((decl() instanceof AnnotationDecl) && (m = (T = (AnnotationDecl) decl()).annotation(lookupType("java.lang.annotation", "Target"))) != null && m.getNumElementValuePair() == 1 && m.getElementValuePair(0).getName().equals("value")) {
            ElementValue v = m.getElementValuePair(0).getElementValue();
            if (!v.validTarget(this)) {
                error("annotation type " + T.typeName() + " is not applicable to this kind of declaration");
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        if (!decl().isAnnotationDecl()) {
            if (!decl().isUnknown()) {
                error(String.valueOf(decl().typeName()) + " is not an annotation type");
            }
        } else {
            TypeDecl typeDecl = decl();
            if (lookupAnnotation(typeDecl) != this) {
                error("duplicate annotation " + typeDecl.typeName());
            }
            for (int i = 0; i < typeDecl.getNumBodyDecl(); i++) {
                if (typeDecl.getBodyDecl(i) instanceof MethodDecl) {
                    MethodDecl decl = (MethodDecl) typeDecl.getBodyDecl(i);
                    if (elementValueFor(decl.name()) == null && (!(decl instanceof AnnotationMethodDecl) || !((AnnotationMethodDecl) decl).hasDefaultValue())) {
                        error("missing value for " + decl.name());
                    }
                }
            }
            for (int i2 = 0; i2 < getNumElementValuePair(); i2++) {
                ElementValuePair pair = getElementValuePair(i2);
                if (typeDecl.memberMethods(pair.getName()).isEmpty()) {
                    error("can not find element named " + pair.getName() + " in " + typeDecl.typeName());
                }
            }
        }
        checkOverride();
    }

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append("@");
        getAccess().toString(s);
        s.append("(");
        for (int i = 0; i < getNumElementValuePair(); i++) {
            if (i != 0) {
                s.append(", ");
            }
            getElementValuePair(i).toString(s);
        }
        s.append(")");
    }

    public void appendAsAttributeTo(Collection list) {
        AnnotationTag tag = new AnnotationTag(decl().typeDescriptor(), getNumElementValuePair());
        ArrayList elements = new ArrayList(getNumElementValuePair());
        for (int i = 0; i < getNumElementValuePair(); i++) {
            String name = getElementValuePair(i).getName();
            ElementValue value = getElementValuePair(i).getElementValue();
            value.appendAsAttributeTo(elements, name);
        }
        tag.setElems(elements);
        list.add(tag);
    }

    public Annotation() {
    }

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 1);
    }

    public Annotation(String p0, Access p1, List<ElementValuePair> p2) {
        setID(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    public Annotation(Symbol p0, Access p1, List<ElementValuePair> p2) {
        setID(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.Modifier
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.Modifier
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.Modifier
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
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

    public void setElementValuePairList(List<ElementValuePair> list) {
        setChild(list, 1);
    }

    public int getNumElementValuePair() {
        return getElementValuePairList().getNumChild();
    }

    public int getNumElementValuePairNoTransform() {
        return getElementValuePairListNoTransform().getNumChildNoTransform();
    }

    public ElementValuePair getElementValuePair(int i) {
        return getElementValuePairList().getChild(i);
    }

    public void addElementValuePair(ElementValuePair node) {
        List<ElementValuePair> list = (this.parent == null || state == null) ? getElementValuePairListNoTransform() : getElementValuePairList();
        list.addChild(node);
    }

    public void addElementValuePairNoTransform(ElementValuePair node) {
        List<ElementValuePair> list = getElementValuePairListNoTransform();
        list.addChild(node);
    }

    public void setElementValuePair(ElementValuePair node, int i) {
        List<ElementValuePair> list = getElementValuePairList();
        list.setChild(node, i);
    }

    public List<ElementValuePair> getElementValuePairs() {
        return getElementValuePairList();
    }

    public List<ElementValuePair> getElementValuePairsNoTransform() {
        return getElementValuePairListNoTransform();
    }

    public List<ElementValuePair> getElementValuePairList() {
        List<ElementValuePair> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<ElementValuePair> getElementValuePairListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void checkOverride() {
        if (decl().fullName().equals("java.lang.Override") && (enclosingBodyDecl() instanceof MethodDecl)) {
            MethodDecl method = (MethodDecl) enclosingBodyDecl();
            TypeDecl host = method.hostType();
            SimpleSet ancestors = host.ancestorMethods(method.signature());
            boolean found = false;
            Iterator iter = ancestors.iterator();
            while (true) {
                if (!iter.hasNext()) {
                    break;
                }
                MethodDecl decl = (MethodDecl) iter.next();
                if (method.overrides(decl)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TypeDecl typeObject = lookupType("java.lang", "Object");
                SimpleSet overrides = typeObject.localMethodsSignature(method.signature());
                if (overrides.isEmpty() || !((MethodDecl) overrides.iterator().next()).isPublic()) {
                    error("method does not override a method from a supertype");
                }
            }
        }
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
        return getAccess().type();
    }

    public ElementValue elementValueFor(String name) {
        state();
        for (int i = 0; i < getNumElementValuePair(); i++) {
            ElementValuePair pair = getElementValuePair(i);
            if (pair.getName().equals(name)) {
                return pair.getElementValue();
            }
        }
        return null;
    }

    public TypeDecl type() {
        state();
        return getAccess().type();
    }

    public boolean isMetaAnnotation() {
        state();
        return hostType().isAnnotationDecl();
    }

    @Override // soot.JastAddJ.Modifier
    public boolean isRuntimeVisible() {
        state();
        Annotation a = decl().annotation(lookupType("java.lang.annotation", "Retention"));
        if (a == null) {
            return false;
        }
        ElementConstantValue value = (ElementConstantValue) a.getElementValuePair(0).getElementValue();
        Variable v = value.getExpr().varDecl();
        return v != null && v.name().equals("RUNTIME");
    }

    @Override // soot.JastAddJ.Modifier
    public boolean isRuntimeInvisible() {
        state();
        Annotation a = decl().annotation(lookupType("java.lang.annotation", "Retention"));
        if (a == null) {
            return true;
        }
        ElementConstantValue value = (ElementConstantValue) a.getElementValuePair(0).getElementValue();
        Variable v = value.getExpr().varDecl();
        return v != null && v.name().equals("CLASS");
    }

    public TypeDecl lookupType(String packageName, String typeName) {
        state();
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    public boolean mayUseAnnotationTarget(String name) {
        state();
        boolean mayUseAnnotationTarget_String_value = getParent().Define_boolean_mayUseAnnotationTarget(this, null, name);
        return mayUseAnnotationTarget_String_value;
    }

    public BodyDecl enclosingBodyDecl() {
        state();
        BodyDecl enclosingBodyDecl_value = getParent().Define_BodyDecl_enclosingBodyDecl(this, null);
        return enclosingBodyDecl_value;
    }

    public Annotation lookupAnnotation(TypeDecl typeDecl) {
        state();
        Annotation lookupAnnotation_TypeDecl_value = getParent().Define_Annotation_lookupAnnotation(this, null, typeDecl);
        return lookupAnnotation_TypeDecl_value;
    }

    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
        if (caller == getElementValuePairListNoTransform()) {
            caller.getIndexOfChild(child);
            return decl();
        }
        return getParent().Define_TypeDecl_enclosingAnnotationDecl(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.Modifier, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
