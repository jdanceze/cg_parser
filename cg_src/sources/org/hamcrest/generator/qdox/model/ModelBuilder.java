package org.hamcrest.generator.qdox.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.hamcrest.generator.qdox.JavaClassContext;
import org.hamcrest.generator.qdox.model.annotation.AnnotationFieldRef;
import org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor;
import org.hamcrest.generator.qdox.model.annotation.RecursiveAnnotationVisitor;
import org.hamcrest.generator.qdox.parser.Builder;
import org.hamcrest.generator.qdox.parser.structs.ClassDef;
import org.hamcrest.generator.qdox.parser.structs.FieldDef;
import org.hamcrest.generator.qdox.parser.structs.MethodDef;
import org.hamcrest.generator.qdox.parser.structs.PackageDef;
import org.hamcrest.generator.qdox.parser.structs.TagDef;
import org.hamcrest.generator.qdox.parser.structs.TypeDef;
import org.hamcrest.generator.qdox.parser.structs.TypeVariableDef;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/ModelBuilder.class */
public class ModelBuilder implements Builder {
    private final JavaClassContext context;
    private final JavaSource source;
    private JavaClassParent currentParent;
    private JavaClass currentClass;
    private JavaMethod currentMethod;
    private List currentAnnoDefs;
    private String lastComment;
    private List lastTagSet;
    private DocletTagFactory docletTagFactory;

    public ModelBuilder() {
        this(new JavaClassContext(new ClassLibrary()), new DefaultDocletTagFactory(), new HashMap());
    }

    public ModelBuilder(JavaClassContext context, DocletTagFactory docletTagFactory, Map allPackages) {
        this.context = context;
        this.docletTagFactory = docletTagFactory;
        this.source = new JavaSource(context);
        this.currentParent = this.source;
        this.currentAnnoDefs = new ArrayList();
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void addPackage(PackageDef packageDef) {
        JavaPackage jPackage = this.context.getPackageByName(packageDef.name);
        if (jPackage == null) {
            jPackage = new JavaPackage(packageDef.name);
            this.context.add(jPackage);
        }
        jPackage.setLineNumber(packageDef.lineNumber);
        setAnnotations(jPackage);
        this.source.setPackage(jPackage);
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void addImport(String importName) {
        this.source.addImport(importName);
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void addJavaDoc(String text) {
        this.lastComment = text;
        this.lastTagSet = new LinkedList();
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void addJavaDocTag(TagDef tagDef) {
        this.lastTagSet.add(tagDef);
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void beginClass(ClassDef def) {
        this.currentClass = new JavaClass();
        this.currentClass.setLineNumber(def.lineNumber);
        this.currentClass.setName(def.name);
        this.currentClass.setInterface("interface".equals(def.type));
        this.currentClass.setEnum("enum".equals(def.type));
        this.currentClass.setAnnotation(ClassDef.ANNOTATION_TYPE.equals(def.type));
        if (this.currentClass.isInterface()) {
            this.currentClass.setSuperClass(null);
        } else if (!this.currentClass.isEnum()) {
            this.currentClass.setSuperClass(def.extendz.size() > 0 ? createType((TypeDef) def.extendz.toArray()[0], 0) : null);
        }
        Set implementSet = this.currentClass.isInterface() ? def.extendz : def.implementz;
        Iterator implementIt = implementSet.iterator();
        Type[] implementz = new Type[implementSet.size()];
        for (int i = 0; i < implementz.length && implementIt.hasNext(); i++) {
            implementz[i] = createType((TypeDef) implementIt.next(), 0);
        }
        this.currentClass.setImplementz(implementz);
        String[] modifiers = new String[def.modifiers.size()];
        def.modifiers.toArray(modifiers);
        this.currentClass.setModifiers(modifiers);
        if (def.typeParams != null) {
            TypeVariable[] typeParams = new TypeVariable[def.typeParams.size()];
            int index = 0;
            for (TypeVariableDef typeVariableDef : def.typeParams) {
                int i2 = index;
                index++;
                typeParams[i2] = createTypeVariable(typeVariableDef);
            }
            this.currentClass.setTypeParameters(typeParams);
        }
        addJavaDoc(this.currentClass);
        setAnnotations(this.currentClass);
        this.currentParent.addClass(this.currentClass);
        this.currentParent = this.currentClass;
        this.context.add(this.currentClass.getFullyQualifiedName());
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void endClass() {
        this.currentParent = this.currentClass.getParent();
        if (this.currentParent instanceof JavaClass) {
            this.currentClass = (JavaClass) this.currentParent;
        } else {
            this.currentClass = null;
        }
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public Type createType(String typeName, int dimensions) {
        if (typeName == null || typeName.equals("")) {
            return null;
        }
        return createType(new TypeDef(typeName), dimensions);
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public Type createType(TypeDef typeDef) {
        return createType(typeDef, 0);
    }

    public Type createType(TypeDef typeDef, int dimensions) {
        if (typeDef == null) {
            return null;
        }
        return Type.createUnresolved(typeDef, dimensions, this.currentClass == null ? this.currentParent : this.currentClass);
    }

    private void addJavaDoc(AbstractJavaEntity entity) {
        if (this.lastComment == null) {
            return;
        }
        entity.setComment(this.lastComment);
        List tagList = new ArrayList();
        for (TagDef tagDef : this.lastTagSet) {
            tagList.add(this.docletTagFactory.createDocletTag(tagDef.name, tagDef.text, entity, tagDef.lineNumber));
        }
        entity.setTags(tagList);
        this.lastComment = null;
    }

    public void addMethod(MethodDef def) {
        beginMethod();
        endMethod(def);
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void beginMethod() {
        this.currentMethod = new JavaMethod();
        setAnnotations(this.currentMethod);
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void endMethod(MethodDef def) {
        this.currentMethod.setParentClass(this.currentClass);
        this.currentMethod.setLineNumber(def.lineNumber);
        this.currentMethod.setName(def.name);
        this.currentMethod.setReturns(createType(def.returnType, def.dimensions));
        this.currentMethod.setConstructor(def.constructor);
        if (def.typeParams != null) {
            TypeVariable[] typeParams = new TypeVariable[def.typeParams.size()];
            int index = 0;
            for (TypeVariableDef typeVariableDef : def.typeParams) {
                int i = index;
                index++;
                typeParams[i] = createTypeVariable(typeVariableDef);
            }
            this.currentMethod.setTypeParameters(typeParams);
        }
        Type[] exceptions = new Type[def.exceptions.size()];
        int index2 = 0;
        for (String str : def.exceptions) {
            int i2 = index2;
            index2++;
            exceptions[i2] = createType(str, 0);
        }
        this.currentMethod.setExceptions(exceptions);
        String[] modifiers = new String[def.modifiers.size()];
        def.modifiers.toArray(modifiers);
        this.currentMethod.setModifiers(modifiers);
        this.currentMethod.setSourceCode(def.body);
        addJavaDoc(this.currentMethod);
        this.currentClass.addMethod(this.currentMethod);
    }

    public TypeVariable createTypeVariable(TypeVariableDef typeVariableDef) {
        if (typeVariableDef == null) {
            return null;
        }
        return TypeVariable.createUnresolved(typeVariableDef, this.currentClass == null ? this.currentParent : this.currentClass);
    }

    public TypeVariable createTypeVariable(String name, List typeParams) {
        if (name == null || name.equals("")) {
            return null;
        }
        return createTypeVariable(new TypeVariableDef(name, typeParams));
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void addField(FieldDef def) {
        JavaField currentField = new JavaField();
        currentField.setParentClass(this.currentClass);
        currentField.setLineNumber(def.lineNumber);
        currentField.setName(def.name);
        currentField.setType(createType(def.type, def.dimensions));
        String[] modifiers = new String[def.modifiers.size()];
        def.modifiers.toArray(modifiers);
        currentField.setModifiers(modifiers);
        currentField.setInitializationExpression(def.body);
        addJavaDoc(currentField);
        setAnnotations(currentField);
        this.currentClass.addField(currentField);
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void addParameter(FieldDef fieldDef) {
        JavaParameter jParam = new JavaParameter(createType(fieldDef.type, fieldDef.dimensions), fieldDef.name, fieldDef.isVarArgs);
        setAnnotations(jParam);
        this.currentMethod.addParameter(jParam);
    }

    private void setAnnotations(AbstractBaseJavaEntity entity) {
        if (!this.currentAnnoDefs.isEmpty()) {
            AnnotationVisitor visitor = new RecursiveAnnotationVisitor(this, entity) { // from class: org.hamcrest.generator.qdox.model.ModelBuilder.1
                private final AbstractBaseJavaEntity val$entity;
                private final ModelBuilder this$0;

                {
                    this.this$0 = this;
                    this.val$entity = entity;
                }

                @Override // org.hamcrest.generator.qdox.model.annotation.RecursiveAnnotationVisitor, org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
                public Object visitAnnotation(Annotation annotation) {
                    annotation.setContext(this.val$entity);
                    return super.visitAnnotation(annotation);
                }

                @Override // org.hamcrest.generator.qdox.model.annotation.RecursiveAnnotationVisitor, org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
                public Object visitAnnotationFieldRef(AnnotationFieldRef fieldRef) {
                    fieldRef.setContext(this.val$entity);
                    return super.visitAnnotationFieldRef(fieldRef);
                }
            };
            Annotation[] annotations = new Annotation[this.currentAnnoDefs.size()];
            ListIterator iter = this.currentAnnoDefs.listIterator();
            while (iter.hasNext()) {
                Annotation annotation = (Annotation) iter.next();
                annotation.accept(visitor);
                annotations[iter.previousIndex()] = annotation;
            }
            entity.setAnnotations(annotations);
            this.currentAnnoDefs.clear();
        }
    }

    @Override // org.hamcrest.generator.qdox.parser.Builder
    public void addAnnotation(Annotation annotation) {
        this.currentAnnoDefs.add(annotation);
    }

    public JavaSource getSource() {
        return this.source;
    }
}
