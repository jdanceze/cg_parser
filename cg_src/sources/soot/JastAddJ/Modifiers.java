package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.PhaseOptions;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
import soot.options.JBOptions;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Modifiers.class */
public class Modifiers extends ASTNode<ASTNode> implements Cloneable {
    public static final int ACC_ANNOTATION = 8192;
    public static final int ACC_ENUM = 16384;
    public static final int ACC_BRIDGE = 64;
    public static final int ACC_VARARGS = 128;
    protected boolean isPublic_value;
    protected boolean isPrivate_value;
    protected boolean isProtected_value;
    protected boolean isStatic_value;
    protected boolean isFinal_value;
    protected boolean isAbstract_value;
    protected boolean isVolatile_value;
    protected boolean isTransient_value;
    protected boolean isStrictfp_value;
    protected boolean isSynchronized_value;
    protected boolean isNative_value;
    protected boolean isSynthetic_value;
    protected Map numModifier_String_values;
    protected boolean isPublic_computed = false;
    protected boolean isPrivate_computed = false;
    protected boolean isProtected_computed = false;
    protected boolean isStatic_computed = false;
    protected boolean isFinal_computed = false;
    protected boolean isAbstract_computed = false;
    protected boolean isVolatile_computed = false;
    protected boolean isTransient_computed = false;
    protected boolean isStrictfp_computed = false;
    protected boolean isSynchronized_computed = false;
    protected boolean isNative_computed = false;
    protected boolean isSynthetic_computed = false;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isPublic_computed = false;
        this.isPrivate_computed = false;
        this.isProtected_computed = false;
        this.isStatic_computed = false;
        this.isFinal_computed = false;
        this.isAbstract_computed = false;
        this.isVolatile_computed = false;
        this.isTransient_computed = false;
        this.isStrictfp_computed = false;
        this.isSynchronized_computed = false;
        this.isNative_computed = false;
        this.isSynthetic_computed = false;
        this.numModifier_String_values = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public Modifiers clone() throws CloneNotSupportedException {
        Modifiers node = (Modifiers) super.mo287clone();
        node.isPublic_computed = false;
        node.isPrivate_computed = false;
        node.isProtected_computed = false;
        node.isStatic_computed = false;
        node.isFinal_computed = false;
        node.isAbstract_computed = false;
        node.isVolatile_computed = false;
        node.isTransient_computed = false;
        node.isStrictfp_computed = false;
        node.isSynchronized_computed = false;
        node.isNative_computed = false;
        node.isSynthetic_computed = false;
        node.numModifier_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            Modifiers node = clone();
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
    public void checkModifiers() {
        super.checkModifiers();
        if (numProtectionModifiers() > 1) {
            error("only one public, protected, private allowed");
        }
        if (numModifier(Jimple.STATIC) > 1) {
            error("only one static allowed");
        }
        if (numCompletenessModifiers() > 1) {
            error("only one of final, abstract, volatile allowed");
        }
        if (numModifier(Jimple.SYNCHRONIZED) > 1) {
            error("only one synchronized allowed");
        }
        if (numModifier(Jimple.TRANSIENT) > 1) {
            error("only one transient allowed");
        }
        if (numModifier(Jimple.NATIVE) > 1) {
            error("only one native allowed");
        }
        if (numModifier(Jimple.STRICTFP) > 1) {
            error("only one strictfp allowed");
        }
        if (isPublic() && !mayBePublic()) {
            error("modifier public not allowed in this context");
        }
        if (isPrivate() && !mayBePrivate()) {
            error("modifier private not allowed in this context");
        }
        if (isProtected() && !mayBeProtected()) {
            error("modifier protected not allowed in this context");
        }
        if (isStatic() && !mayBeStatic()) {
            error("modifier static not allowed in this context");
        }
        if (isFinal() && !mayBeFinal()) {
            error("modifier final not allowed in this context");
        }
        if (isAbstract() && !mayBeAbstract()) {
            error("modifier abstract not allowed in this context");
        }
        if (isVolatile() && !mayBeVolatile()) {
            error("modifier volatile not allowed in this context");
        }
        if (isTransient() && !mayBeTransient()) {
            error("modifier transient not allowed in this context");
        }
        if (isStrictfp() && !mayBeStrictfp()) {
            error("modifier strictfp not allowed in this context");
        }
        if (isSynchronized() && !mayBeSynchronized()) {
            error("modifier synchronized not allowed in this context");
        }
        if (isNative() && !mayBeNative()) {
            error("modifier native not allowed in this context");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        for (int i = 0; i < getNumModifier(); i++) {
            getModifier(i).toString(s);
            s.append(Instruction.argsep);
        }
    }

    public void addSourceOnlyAnnotations(Collection c) {
        if (new JBOptions(PhaseOptions.v().getPhaseOptions("jb")).preserve_source_annotations()) {
            for (int i = 0; i < getNumModifier(); i++) {
                if (getModifier(i) instanceof Annotation) {
                    Annotation a = (Annotation) getModifier(i);
                    if (!a.isRuntimeVisible() && !a.isRuntimeInvisible()) {
                        VisibilityAnnotationTag tag = new VisibilityAnnotationTag(2);
                        ArrayList elements = new ArrayList(1);
                        a.appendAsAttributeTo(elements);
                        tag.addAnnotation((AnnotationTag) elements.get(0));
                        c.add(tag);
                    }
                }
            }
        }
    }

    public void addAllAnnotations(Collection c) {
        for (int i = 0; i < getNumModifier(); i++) {
            if (getModifier(i) instanceof Annotation) {
                Annotation a = (Annotation) getModifier(i);
                a.appendAsAttributeTo(c);
            }
        }
    }

    public void addRuntimeVisibleAnnotationsAttribute(Collection c) {
        Collection<Annotation> annotations = runtimeVisibleAnnotations();
        if (!annotations.isEmpty()) {
            VisibilityAnnotationTag tag = new VisibilityAnnotationTag(0);
            for (Annotation annotation : annotations) {
                ArrayList elements = new ArrayList(1);
                annotation.appendAsAttributeTo(elements);
                tag.addAnnotation((AnnotationTag) elements.get(0));
            }
            c.add(tag);
        }
    }

    public void addRuntimeInvisibleAnnotationsAttribute(Collection c) {
        Collection<Annotation> annotations = runtimeInvisibleAnnotations();
        if (!annotations.isEmpty()) {
            VisibilityAnnotationTag tag = new VisibilityAnnotationTag(1);
            for (Annotation annotation : annotations) {
                ArrayList elements = new ArrayList(1);
                annotation.appendAsAttributeTo(elements);
                tag.addAnnotation((AnnotationTag) elements.get(0));
            }
            c.add(tag);
        }
    }

    public Collection runtimeVisibleAnnotations() {
        Collection annotations = new ArrayList();
        for (int i = 0; i < getNumModifier(); i++) {
            if (getModifier(i).isRuntimeVisible()) {
                annotations.add(getModifier(i));
            }
        }
        return annotations;
    }

    public Collection runtimeInvisibleAnnotations() {
        Collection annotations = new ArrayList();
        for (int i = 0; i < getNumModifier(); i++) {
            if (getModifier(i).isRuntimeInvisible()) {
                annotations.add(getModifier(i));
            }
        }
        return annotations;
    }

    public Modifiers() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public Modifiers(List<Modifier> p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setModifierList(List<Modifier> list) {
        setChild(list, 0);
    }

    public int getNumModifier() {
        return getModifierList().getNumChild();
    }

    public int getNumModifierNoTransform() {
        return getModifierListNoTransform().getNumChildNoTransform();
    }

    public Modifier getModifier(int i) {
        return getModifierList().getChild(i);
    }

    public void addModifier(Modifier node) {
        List<Modifier> list = (this.parent == null || state == null) ? getModifierListNoTransform() : getModifierList();
        list.addChild(node);
    }

    public void addModifierNoTransform(Modifier node) {
        List<Modifier> list = getModifierListNoTransform();
        list.addChild(node);
    }

    public void setModifier(Modifier node, int i) {
        List<Modifier> list = getModifierList();
        list.setChild(node, i);
    }

    public List<Modifier> getModifiers() {
        return getModifierList();
    }

    public List<Modifier> getModifiersNoTransform() {
        return getModifierListNoTransform();
    }

    public List<Modifier> getModifierList() {
        List<Modifier> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<Modifier> getModifierListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    public boolean isPublic() {
        if (this.isPublic_computed) {
            return this.isPublic_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isPublic_value = isPublic_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isPublic_computed = true;
        }
        return this.isPublic_value;
    }

    private boolean isPublic_compute() {
        return numModifier(Jimple.PUBLIC) != 0;
    }

    public boolean isPrivate() {
        if (this.isPrivate_computed) {
            return this.isPrivate_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isPrivate_value = isPrivate_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isPrivate_computed = true;
        }
        return this.isPrivate_value;
    }

    private boolean isPrivate_compute() {
        return numModifier(Jimple.PRIVATE) != 0;
    }

    public boolean isProtected() {
        if (this.isProtected_computed) {
            return this.isProtected_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isProtected_value = isProtected_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isProtected_computed = true;
        }
        return this.isProtected_value;
    }

    private boolean isProtected_compute() {
        return numModifier(Jimple.PROTECTED) != 0;
    }

    public boolean isStatic() {
        if (this.isStatic_computed) {
            return this.isStatic_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isStatic_value = isStatic_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isStatic_computed = true;
        }
        return this.isStatic_value;
    }

    private boolean isStatic_compute() {
        return numModifier(Jimple.STATIC) != 0;
    }

    public boolean isFinal() {
        if (this.isFinal_computed) {
            return this.isFinal_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isFinal_value = isFinal_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isFinal_computed = true;
        }
        return this.isFinal_value;
    }

    private boolean isFinal_compute() {
        return numModifier(Jimple.FINAL) != 0;
    }

    public boolean isAbstract() {
        if (this.isAbstract_computed) {
            return this.isAbstract_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isAbstract_value = isAbstract_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isAbstract_computed = true;
        }
        return this.isAbstract_value;
    }

    private boolean isAbstract_compute() {
        return numModifier(Jimple.ABSTRACT) != 0;
    }

    public boolean isVolatile() {
        if (this.isVolatile_computed) {
            return this.isVolatile_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isVolatile_value = isVolatile_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isVolatile_computed = true;
        }
        return this.isVolatile_value;
    }

    private boolean isVolatile_compute() {
        return numModifier(Jimple.VOLATILE) != 0;
    }

    public boolean isTransient() {
        if (this.isTransient_computed) {
            return this.isTransient_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isTransient_value = isTransient_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isTransient_computed = true;
        }
        return this.isTransient_value;
    }

    private boolean isTransient_compute() {
        return numModifier(Jimple.TRANSIENT) != 0;
    }

    public boolean isStrictfp() {
        if (this.isStrictfp_computed) {
            return this.isStrictfp_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isStrictfp_value = isStrictfp_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isStrictfp_computed = true;
        }
        return this.isStrictfp_value;
    }

    private boolean isStrictfp_compute() {
        return numModifier(Jimple.STRICTFP) != 0;
    }

    public boolean isSynchronized() {
        if (this.isSynchronized_computed) {
            return this.isSynchronized_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isSynchronized_value = isSynchronized_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isSynchronized_computed = true;
        }
        return this.isSynchronized_value;
    }

    private boolean isSynchronized_compute() {
        return numModifier(Jimple.SYNCHRONIZED) != 0;
    }

    public boolean isNative() {
        if (this.isNative_computed) {
            return this.isNative_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isNative_value = isNative_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isNative_computed = true;
        }
        return this.isNative_value;
    }

    private boolean isNative_compute() {
        return numModifier(Jimple.NATIVE) != 0;
    }

    public boolean isSynthetic() {
        if (this.isSynthetic_computed) {
            return this.isSynthetic_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isSynthetic_value = isSynthetic_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isSynthetic_computed = true;
        }
        return this.isSynthetic_value;
    }

    private boolean isSynthetic_compute() {
        return numModifier("synthetic") != 0;
    }

    public int numProtectionModifiers() {
        state();
        return numModifier(Jimple.PUBLIC) + numModifier(Jimple.PROTECTED) + numModifier(Jimple.PRIVATE);
    }

    public int numCompletenessModifiers() {
        state();
        return numModifier(Jimple.ABSTRACT) + numModifier(Jimple.FINAL) + numModifier(Jimple.VOLATILE);
    }

    public int numModifier(String name) {
        if (this.numModifier_String_values == null) {
            this.numModifier_String_values = new HashMap(4);
        }
        if (this.numModifier_String_values.containsKey(name)) {
            return ((Integer) this.numModifier_String_values.get(name)).intValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        int numModifier_String_value = numModifier_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.numModifier_String_values.put(name, Integer.valueOf(numModifier_String_value));
        }
        return numModifier_String_value;
    }

    private int numModifier_compute(String name) {
        int n = 0;
        for (int i = 0; i < getNumModifier(); i++) {
            String s = getModifier(i).getID();
            if (s.equals(name)) {
                n++;
            }
        }
        return n;
    }

    public Annotation annotation(TypeDecl typeDecl) {
        state();
        for (int i = 0; i < getNumModifier(); i++) {
            if (getModifier(i) instanceof Annotation) {
                Annotation a = (Annotation) getModifier(i);
                if (a.type() == typeDecl) {
                    return a;
                }
            }
        }
        return null;
    }

    public boolean hasAnnotationSuppressWarnings(String s) {
        state();
        Annotation a = annotation(lookupType("java.lang", "SuppressWarnings"));
        if (a != null && a.getNumElementValuePair() == 1 && a.getElementValuePair(0).getName().equals("value")) {
            return a.getElementValuePair(0).getElementValue().hasValue(s);
        }
        return false;
    }

    public boolean hasDeprecatedAnnotation() {
        state();
        return annotation(lookupType("java.lang", "Deprecated")) != null;
    }

    public boolean hasAnnotationSafeVarargs() {
        state();
        return annotation(lookupType("java.lang", "SafeVarargs")) != null;
    }

    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    public boolean mayBePublic() {
        state();
        boolean mayBePublic_value = getParent().Define_boolean_mayBePublic(this, null);
        return mayBePublic_value;
    }

    public boolean mayBePrivate() {
        state();
        boolean mayBePrivate_value = getParent().Define_boolean_mayBePrivate(this, null);
        return mayBePrivate_value;
    }

    public boolean mayBeProtected() {
        state();
        boolean mayBeProtected_value = getParent().Define_boolean_mayBeProtected(this, null);
        return mayBeProtected_value;
    }

    public boolean mayBeStatic() {
        state();
        boolean mayBeStatic_value = getParent().Define_boolean_mayBeStatic(this, null);
        return mayBeStatic_value;
    }

    public boolean mayBeFinal() {
        state();
        boolean mayBeFinal_value = getParent().Define_boolean_mayBeFinal(this, null);
        return mayBeFinal_value;
    }

    public boolean mayBeAbstract() {
        state();
        boolean mayBeAbstract_value = getParent().Define_boolean_mayBeAbstract(this, null);
        return mayBeAbstract_value;
    }

    public boolean mayBeVolatile() {
        state();
        boolean mayBeVolatile_value = getParent().Define_boolean_mayBeVolatile(this, null);
        return mayBeVolatile_value;
    }

    public boolean mayBeTransient() {
        state();
        boolean mayBeTransient_value = getParent().Define_boolean_mayBeTransient(this, null);
        return mayBeTransient_value;
    }

    public boolean mayBeStrictfp() {
        state();
        boolean mayBeStrictfp_value = getParent().Define_boolean_mayBeStrictfp(this, null);
        return mayBeStrictfp_value;
    }

    public boolean mayBeSynchronized() {
        state();
        boolean mayBeSynchronized_value = getParent().Define_boolean_mayBeSynchronized(this, null);
        return mayBeSynchronized_value;
    }

    public boolean mayBeNative() {
        state();
        boolean mayBeNative_value = getParent().Define_boolean_mayBeNative(this, null);
        return mayBeNative_value;
    }

    public TypeDecl lookupType(String packageName, String typeName) {
        state();
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        if (caller == getModifierListNoTransform()) {
            caller.getIndexOfChild(child);
            return annotation(typeDecl);
        }
        return getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
