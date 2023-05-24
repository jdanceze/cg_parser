package soot.JastAddJ;

import beaver.Symbol;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.JavaBasicTypes;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ReferenceType.class */
public abstract class ReferenceType extends TypeDecl implements Cloneable {
    protected Map narrowingConversionTo_TypeDecl_values;
    protected TypeDecl unboxed_value;
    protected String jvmName_value;
    protected boolean unboxed_computed = false;
    protected boolean jvmName_computed = false;

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.narrowingConversionTo_TypeDecl_values = null;
        this.unboxed_computed = false;
        this.unboxed_value = null;
        this.jvmName_computed = false;
        this.jvmName_value = null;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public ReferenceType clone() throws CloneNotSupportedException {
        ReferenceType node = (ReferenceType) super.clone();
        node.narrowingConversionTo_TypeDecl_values = null;
        node.unboxed_computed = false;
        node.unboxed_value = null;
        node.jvmName_computed = false;
        node.jvmName_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.TypeDecl
    public Value emitCastTo(Body b, Value v, TypeDecl type, ASTNode location) {
        if (this == type) {
            return v;
        }
        if (type instanceof PrimitiveType) {
            return type.boxed().emitUnboxingOperation(b, emitCastTo(b, v, type.boxed(), location), location);
        }
        return super.emitCastTo(b, v, type, location);
    }

    public ReferenceType() {
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 1);
    }

    public ReferenceType(Modifiers p0, String p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    public ReferenceType(Modifiers p0, Symbol p1, List<BodyDecl> p2) {
        setChild(p0, 0);
        setID(p1);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public void setModifiers(Modifiers node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.TypeDecl
    public Modifiers getModifiers() {
        return (Modifiers) getChild(0);
    }

    @Override // soot.JastAddJ.TypeDecl
    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.TypeDecl
    public void setID(String value) {
        this.tokenString_ID = value;
    }

    @Override // soot.JastAddJ.TypeDecl
    public void setID(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setID is only valid for String lexemes");
        }
        this.tokenString_ID = (String) symbol.value;
        this.IDstart = symbol.getStart();
        this.IDend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.TypeDecl
    public String getID() {
        return this.tokenString_ID != null ? this.tokenString_ID : "";
    }

    @Override // soot.JastAddJ.TypeDecl
    public void setBodyDeclList(List<BodyDecl> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.TypeDecl
    public int getNumBodyDecl() {
        return getBodyDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.TypeDecl
    public int getNumBodyDeclNoTransform() {
        return getBodyDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.TypeDecl
    public BodyDecl getBodyDecl(int i) {
        return getBodyDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.TypeDecl
    public void addBodyDecl(BodyDecl node) {
        List<BodyDecl> list = (this.parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.TypeDecl
    public void addBodyDeclNoTransform(BodyDecl node) {
        List<BodyDecl> list = getBodyDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.TypeDecl
    public void setBodyDecl(BodyDecl node, int i) {
        List<BodyDecl> list = getBodyDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDecls() {
        return getBodyDeclList();
    }

    @Override // soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclsNoTransform() {
        return getBodyDeclListNoTransform();
    }

    @Override // soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclList() {
        List<BodyDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.TypeDecl
    public List<BodyDecl> getBodyDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean wideningConversionTo(TypeDecl type) {
        state();
        return instanceOf(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean narrowingConversionTo(TypeDecl type) {
        if (this.narrowingConversionTo_TypeDecl_values == null) {
            this.narrowingConversionTo_TypeDecl_values = new HashMap(4);
        }
        if (this.narrowingConversionTo_TypeDecl_values.containsKey(type)) {
            return ((Boolean) this.narrowingConversionTo_TypeDecl_values.get(type)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean narrowingConversionTo_TypeDecl_value = narrowingConversionTo_compute(type);
        if (isFinal && num == state().boundariesCrossed) {
            this.narrowingConversionTo_TypeDecl_values.put(type, Boolean.valueOf(narrowingConversionTo_TypeDecl_value));
        }
        return narrowingConversionTo_TypeDecl_value;
    }

    private boolean narrowingConversionTo_compute(TypeDecl type) {
        if (type.instanceOf(this)) {
            return true;
        }
        if (isClassDecl() && !getModifiers().isFinal() && type.isInterfaceDecl()) {
            return true;
        }
        if (isInterfaceDecl() && type.isClassDecl() && !type.getModifiers().isFinal()) {
            return true;
        }
        if (isInterfaceDecl() && type.instanceOf(this)) {
            return true;
        }
        if (fullName().equals(JavaBasicTypes.JAVA_LANG_OBJECT) && type.isInterfaceDecl()) {
            return true;
        }
        if (isArrayDecl() && type.isArrayDecl() && elementType().instanceOf(type.elementType())) {
            return true;
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isReferenceType() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isSupertypeOfNullType(NullType type) {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isValidAnnotationMethodReturnType() {
        state();
        if (isString() || fullName().equals("java.lang.Class") || erasure().fullName().equals("java.lang.Class")) {
            return true;
        }
        return false;
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean unboxingConversionTo(TypeDecl typeDecl) {
        state();
        return unboxed() == typeDecl;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl unboxed() {
        if (this.unboxed_computed) {
            return this.unboxed_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unboxed_value = unboxed_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.unboxed_computed = true;
        }
        return this.unboxed_value;
    }

    private TypeDecl unboxed_compute() {
        if (packageName().equals("java.lang") && isTopLevelType()) {
            String n = name();
            if (n.equals("Boolean")) {
                return typeBoolean();
            }
            if (n.equals("Byte")) {
                return typeByte();
            }
            if (n.equals("Character")) {
                return typeChar();
            }
            if (n.equals("Short")) {
                return typeShort();
            }
            if (n.equals("Integer")) {
                return typeInt();
            }
            if (n.equals("Long")) {
                return typeLong();
            }
            if (n.equals("Float")) {
                return typeFloat();
            }
            if (n.equals("Double")) {
                return typeDouble();
            }
        }
        return unknownType();
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl unaryNumericPromotion() {
        state();
        return (!isNumericType() || isUnknown()) ? this : unboxed().unaryNumericPromotion();
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl binaryNumericPromotion(TypeDecl type) {
        state();
        return unboxed().binaryNumericPromotion(type);
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isNumericType() {
        state();
        return !unboxed().isUnknown() && unboxed().isNumericType();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isIntegralType() {
        state();
        return !unboxed().isUnknown() && unboxed().isIntegralType();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isPrimitive() {
        state();
        return !unboxed().isUnknown() && unboxed().isPrimitive();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean isBoolean() {
        state();
        return fullName().equals(JavaBasicTypes.JAVA_LANG_BOOLEAN) && unboxed().isBoolean();
    }

    @Override // soot.JastAddJ.TypeDecl
    public boolean supertypeNullType(NullType type) {
        state();
        return true;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl stringPromotion() {
        state();
        return typeObject();
    }

    @Override // soot.JastAddJ.TypeDecl
    public String jvmName() {
        if (this.jvmName_computed) {
            return this.jvmName_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.jvmName_value = jvmName_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.jvmName_computed = true;
        }
        return this.jvmName_value;
    }

    private String jvmName_compute() {
        if (!isNestedType()) {
            return fullName();
        }
        if (isAnonymous() || isLocalClass()) {
            return String.valueOf(enclosingType().jvmName()) + "$" + uniqueIndex() + name();
        }
        return String.valueOf(enclosingType().jvmName()) + "$" + name();
    }

    @Override // soot.JastAddJ.TypeDecl
    public String referenceClassFieldName() {
        state();
        return "class$" + jvmName().replace('[', '$').replace('.', '$').replace(';', ' ').trim();
    }

    public TypeDecl typeBoolean() {
        state();
        TypeDecl typeBoolean_value = getParent().Define_TypeDecl_typeBoolean(this, null);
        return typeBoolean_value;
    }

    public TypeDecl typeByte() {
        state();
        TypeDecl typeByte_value = getParent().Define_TypeDecl_typeByte(this, null);
        return typeByte_value;
    }

    public TypeDecl typeChar() {
        state();
        TypeDecl typeChar_value = getParent().Define_TypeDecl_typeChar(this, null);
        return typeChar_value;
    }

    public TypeDecl typeShort() {
        state();
        TypeDecl typeShort_value = getParent().Define_TypeDecl_typeShort(this, null);
        return typeShort_value;
    }

    @Override // soot.JastAddJ.TypeDecl
    public TypeDecl typeInt() {
        state();
        TypeDecl typeInt_value = getParent().Define_TypeDecl_typeInt(this, null);
        return typeInt_value;
    }

    public TypeDecl typeLong() {
        state();
        TypeDecl typeLong_value = getParent().Define_TypeDecl_typeLong(this, null);
        return typeLong_value;
    }

    public TypeDecl typeFloat() {
        state();
        TypeDecl typeFloat_value = getParent().Define_TypeDecl_typeFloat(this, null);
        return typeFloat_value;
    }

    public TypeDecl typeDouble() {
        state();
        TypeDecl typeDouble_value = getParent().Define_TypeDecl_typeDouble(this, null);
        return typeDouble_value;
    }

    @Override // soot.JastAddJ.TypeDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
