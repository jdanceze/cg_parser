package soot.javaToJimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.resource.spi.work.WorkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.ast.Block;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassLit;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.Initializer;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.ast.SourceFile;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.util.IdentityKey;
import soot.BooleanType;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.VoidType;
import soot.options.Options;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.QualifyingTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.SyntheticTag;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/ClassResolver.class */
public class ClassResolver {
    private static final Logger logger = LoggerFactory.getLogger(ClassResolver.class);
    private ArrayList<FieldDecl> staticFieldInits;
    private ArrayList<FieldDecl> fieldInits;
    private ArrayList<Block> initializerBlocks;
    private ArrayList<Block> staticInitializerBlocks;
    private final SootClass sootClass;
    private final Collection<Type> references;

    /* JADX INFO: Access modifiers changed from: protected */
    public void addSourceFileTag(SootClass sc) {
        SourceFileTag tag = (SourceFileTag) sc.getTag(SourceFileTag.NAME);
        if (tag == null) {
            tag = new SourceFileTag();
            sc.addTag(tag);
        }
        String name = Util.getSourceFileOfClass(sc);
        if (InitialResolver.v().classToSourceMap() != null && InitialResolver.v().classToSourceMap().containsKey(name)) {
            name = InitialResolver.v().classToSourceMap().get(name);
        }
        int slashIndex = name.lastIndexOf(47);
        if (slashIndex != -1) {
            name = name.substring(slashIndex + 1);
        }
        tag.setSourceFile(name);
    }

    private void createClassDecl(ClassDecl cDecl) {
        SootMethod clinitMethod;
        if (!cDecl.type().isTopLevel()) {
            SootClass outerClass = ((RefType) Util.getSootType(cDecl.type().outer())).getSootClass();
            if (InitialResolver.v().getInnerClassInfoMap() == null) {
                InitialResolver.v().setInnerClassInfoMap(new HashMap<>());
            }
            InitialResolver.v().getInnerClassInfoMap().put(this.sootClass, new InnerClassInfo(outerClass, cDecl.name(), 0));
            this.sootClass.setOuterClass(outerClass);
        }
        Flags flags = cDecl.flags();
        addModifiers(flags, cDecl);
        if (cDecl.superClass() == null) {
            SootClass superClass = Scene.v().getSootClass(Scene.v().getObjectType().toString());
            this.sootClass.setSuperclass(superClass);
        } else {
            this.sootClass.setSuperclass(((RefType) Util.getSootType(cDecl.superClass().type())).getSootClass());
            if (((ClassType) cDecl.superClass().type()).isNested()) {
                ClassType superType = (ClassType) cDecl.superClass().type();
                Util.addInnerClassTag(this.sootClass, this.sootClass.getName(), ((RefType) Util.getSootType(superType.outer())).toString(), superType.name(), Util.getModifier(superType.flags()));
            }
        }
        for (TypeNode next : cDecl.interfaces()) {
            this.sootClass.addInterface(((RefType) Util.getSootType(next.type())).getSootClass());
        }
        findReferences(cDecl);
        createClassBody(cDecl.body());
        handleFieldInits();
        if (this.staticFieldInits != null || this.staticInitializerBlocks != null) {
            if (!this.sootClass.declaresMethod("<clinit>", new ArrayList(), VoidType.v())) {
                clinitMethod = Scene.v().makeSootMethod("<clinit>", new ArrayList(), VoidType.v(), 8, new ArrayList());
                this.sootClass.addMethod(clinitMethod);
                PolyglotMethodSource mSource = new PolyglotMethodSource();
                mSource.setJBB(InitialResolver.v().getJBBFactory().createJimpleBodyBuilder());
                clinitMethod.setSource(mSource);
            } else {
                clinitMethod = this.sootClass.getMethod("<clinit>", new ArrayList(), VoidType.v());
            }
            ((PolyglotMethodSource) clinitMethod.getSource()).setStaticFieldInits(this.staticFieldInits);
            ((PolyglotMethodSource) clinitMethod.getSource()).setStaticInitializerBlocks(this.staticInitializerBlocks);
        }
        if (cDecl.type().isLocal()) {
            AnonLocalClassInfo info = InitialResolver.v().finalLocalInfo().get(new IdentityKey(cDecl.type()));
            ArrayList<SootField> finalsList = addFinalLocals(cDecl.body(), info.finalLocalsAvail(), cDecl.type(), info);
            for (SootMethod meth : this.sootClass.getMethods()) {
                if (meth.getName().equals("<init>")) {
                    ((PolyglotMethodSource) meth.getSource()).setFinalsList(finalsList);
                }
            }
            if (!info.inStaticMethod()) {
                ClassType outerType = cDecl.type().outer();
                addOuterClassThisRefToInit(outerType);
                addOuterClassThisRefField(outerType);
            }
        } else if (cDecl.type().isNested() && !cDecl.flags().isStatic()) {
            ClassType outerType2 = cDecl.type().outer();
            addOuterClassThisRefToInit(outerType2);
            addOuterClassThisRefField(outerType2);
        }
        Util.addLnPosTags(this.sootClass, cDecl.position());
    }

    private void findReferences(Node node) {
        TypeListBuilder typeListBuilder = new TypeListBuilder();
        node.visit(typeListBuilder);
        Iterator<polyglot.types.Type> it = typeListBuilder.getList().iterator();
        while (it.hasNext()) {
            polyglot.types.Type type = it.next();
            if (!type.isPrimitive() && type.isClass()) {
                ClassType classType = (ClassType) type;
                Type sootClassType = Util.getSootType(classType);
                this.references.add(sootClassType);
            }
        }
    }

    private void createClassBody(ClassBody classBody) {
        this.staticFieldInits = null;
        this.fieldInits = null;
        this.initializerBlocks = null;
        this.staticInitializerBlocks = null;
        for (Object next : classBody.members()) {
            if (next instanceof MethodDecl) {
                createMethodDecl((MethodDecl) next);
            } else if (next instanceof FieldDecl) {
                createFieldDecl((FieldDecl) next);
            } else if (next instanceof ConstructorDecl) {
                createConstructorDecl((ConstructorDecl) next);
            } else if (next instanceof ClassDecl) {
                Util.addInnerClassTag(this.sootClass, Util.getSootType(((ClassDecl) next).type()).toString(), this.sootClass.getName(), ((ClassDecl) next).name().toString(), Util.getModifier(((ClassDecl) next).flags()));
            } else if (next instanceof Initializer) {
                createInitializer((Initializer) next);
            } else if (Options.v().verbose()) {
                logger.debug("Class Body Member not implemented for type " + next.getClass().getName());
            }
        }
        handleInnerClassTags(classBody);
        handleClassLiteral(classBody);
        handleAssert(classBody);
    }

    private void addOuterClassThisRefField(polyglot.types.Type outerType) {
        Type outerSootType = Util.getSootType(outerType);
        SootField field = Scene.v().makeSootField("this$0", outerSootType, 18);
        this.sootClass.addField(field);
        field.addTag(new SyntheticTag());
    }

    private void addOuterClassThisRefToInit(polyglot.types.Type outerType) {
        Type outerSootType = Util.getSootType(outerType);
        for (SootMethod meth : this.sootClass.getMethods()) {
            if (meth.getName().equals("<init>")) {
                List<Type> newParams = new ArrayList<>();
                newParams.add(outerSootType);
                newParams.addAll(meth.getParameterTypes());
                meth.setParameterTypes(newParams);
                meth.addTag(new EnclosingTag());
                if (InitialResolver.v().getHasOuterRefInInit() == null) {
                    InitialResolver.v().setHasOuterRefInInit(new ArrayList());
                }
                InitialResolver.v().getHasOuterRefInInit().add(meth.getDeclaringClass().getType());
            }
        }
    }

    private void addFinals(LocalInstance li, ArrayList<SootField> finalFields) {
        for (SootMethod meth : this.sootClass.getMethods()) {
            if (meth.getName().equals("<init>")) {
                List<Type> newParams = new ArrayList<>();
                newParams.addAll(meth.getParameterTypes());
                newParams.add(Util.getSootType(li.type()));
                meth.setParameterTypes(newParams);
            }
        }
        SootField sf = Scene.v().makeSootField("val$" + li.name(), Util.getSootType(li.type()), 18);
        this.sootClass.addField(sf);
        finalFields.add(sf);
        sf.addTag(new SyntheticTag());
    }

    private ArrayList<SootField> addFinalLocals(ClassBody cBody, ArrayList<IdentityKey> finalLocalsAvail, ClassType nodeKeyType, AnonLocalClassInfo info) {
        ArrayList<SootField> finalFields = new ArrayList<>();
        LocalUsesChecker luc = new LocalUsesChecker();
        cBody.visit(luc);
        ArrayList<IdentityKey> localsUsed = new ArrayList<>();
        Iterator<IdentityKey> it = finalLocalsAvail.iterator();
        while (it.hasNext()) {
            IdentityKey next = it.next();
            LocalInstance li = (LocalInstance) next.object();
            if (!luc.getLocalDecls().contains(new IdentityKey(li))) {
                localsUsed.add(new IdentityKey(li));
                addFinals(li, finalFields);
            }
        }
        Iterator<Node> it2 = luc.getNews().iterator();
        while (it2.hasNext()) {
            Node next2 = it2.next();
            New tempNew = (New) next2;
            ClassType tempNewType = (ClassType) tempNew.objectType().type();
            if (InitialResolver.v().finalLocalInfo().containsKey(new IdentityKey(tempNewType))) {
                AnonLocalClassInfo lInfo = InitialResolver.v().finalLocalInfo().get(new IdentityKey(tempNewType));
                Iterator<IdentityKey> it3 = lInfo.finalLocalsAvail().iterator();
                while (it3.hasNext()) {
                    LocalInstance li2 = (LocalInstance) it3.next().object();
                    if (!this.sootClass.declaresField("val$" + li2.name(), Util.getSootType(li2.type())) && !luc.getLocalDecls().contains(new IdentityKey(li2))) {
                        addFinals(li2, finalFields);
                        localsUsed.add(new IdentityKey(li2));
                    }
                }
            }
        }
        polyglot.types.Type superType = nodeKeyType.superType();
        while (true) {
            ClassType superType2 = (ClassType) superType;
            if (!Util.getSootType(superType2).equals(Scene.v().getSootClass(Scene.v().getObjectType().toString()).getType())) {
                if (InitialResolver.v().finalLocalInfo().containsKey(new IdentityKey(superType2))) {
                    AnonLocalClassInfo lInfo2 = InitialResolver.v().finalLocalInfo().get(new IdentityKey(superType2));
                    Iterator<IdentityKey> it4 = lInfo2.finalLocalsAvail().iterator();
                    while (it4.hasNext()) {
                        IdentityKey next3 = it4.next();
                        LocalInstance li22 = (LocalInstance) next3.object();
                        if (!this.sootClass.declaresField("val$" + li22.name(), Util.getSootType(li22.type())) && !luc.getLocalDecls().contains(new IdentityKey(li22))) {
                            addFinals(li22, finalFields);
                            localsUsed.add(new IdentityKey(li22));
                        }
                    }
                }
                superType = superType2.superType();
            } else {
                info.finalLocalsUsed(localsUsed);
                InitialResolver.v().finalLocalInfo().put(new IdentityKey(nodeKeyType), info);
                return finalFields;
            }
        }
    }

    private void createAnonClassDecl(New aNew) {
        SootMethod method;
        SootClass outerClass = ((RefType) Util.getSootType(aNew.anonType().outer())).getSootClass();
        if (InitialResolver.v().getInnerClassInfoMap() == null) {
            InitialResolver.v().setInnerClassInfoMap(new HashMap<>());
        }
        InitialResolver.v().getInnerClassInfoMap().put(this.sootClass, new InnerClassInfo(outerClass, WorkException.UNDEFINED, 3));
        this.sootClass.setOuterClass(outerClass);
        SootClass typeClass = ((RefType) Util.getSootType(aNew.objectType().type())).getSootClass();
        if (((ClassType) aNew.objectType().type()).flags().isInterface()) {
            this.sootClass.addInterface(typeClass);
            this.sootClass.setSuperclass(Scene.v().getSootClass(Scene.v().getObjectType().toString()));
        } else {
            this.sootClass.setSuperclass(typeClass);
            if (((ClassType) aNew.objectType().type()).isNested()) {
                ClassType superType = (ClassType) aNew.objectType().type();
                Util.addInnerClassTag(this.sootClass, typeClass.getName(), ((RefType) Util.getSootType(superType.outer())).toString(), superType.name(), Util.getModifier(superType.flags()));
            }
        }
        ArrayList params = new ArrayList();
        if (((ClassType) aNew.objectType().type()).flags().isInterface()) {
            method = Scene.v().makeSootMethod("<init>", params, VoidType.v());
        } else {
            if (!aNew.arguments().isEmpty()) {
                ConstructorInstance ci = InitialResolver.v().getConstructorForAnon(aNew);
                for (polyglot.types.Type pType : ci.formalTypes()) {
                    params.add(Util.getSootType(pType));
                }
            }
            method = Scene.v().makeSootMethod("<init>", params, VoidType.v());
        }
        AnonClassInitMethodSource src = new AnonClassInitMethodSource();
        method.setSource(src);
        this.sootClass.addMethod(method);
        AnonLocalClassInfo info = InitialResolver.v().finalLocalInfo().get(new IdentityKey(aNew.anonType()));
        if (aNew.qualifier() != null) {
            addQualifierRefToInit(aNew.qualifier().type());
            src.hasQualifier(true);
        }
        if (info != null) {
            src.inStaticMethod(info.inStaticMethod());
            if (!info.inStaticMethod() && !InitialResolver.v().isAnonInCCall(aNew.anonType())) {
                addOuterClassThisRefToInit(aNew.anonType().outer());
                addOuterClassThisRefField(aNew.anonType().outer());
                src.thisOuterType(Util.getSootType(aNew.anonType().outer()));
                src.hasOuterRef(true);
            }
        }
        src.polyglotType((ClassType) aNew.anonType().superType());
        src.anonType(aNew.anonType());
        if (info != null) {
            src.setFinalsList(addFinalLocals(aNew.body(), info.finalLocalsAvail(), aNew.anonType(), info));
        }
        src.outerClassType(Util.getSootType(aNew.anonType().outer()));
        if (((ClassType) aNew.objectType().type()).isNested()) {
            src.superOuterType(Util.getSootType(((ClassType) aNew.objectType().type()).outer()));
            src.isSubType(Util.isSubType(aNew.anonType().outer(), ((ClassType) aNew.objectType().type()).outer()));
        }
        Util.addLnPosTags(this.sootClass, aNew.position().line(), aNew.body().position().endLine(), aNew.position().column(), aNew.body().position().endColumn());
    }

    public int getModifiers(Flags flags) {
        return Util.getModifier(flags);
    }

    private void addModifiers(Flags flags, ClassDecl cDecl) {
        int modifiers = 0;
        if (cDecl.type().isNested()) {
            if (flags.isPublic() || flags.isProtected() || flags.isPrivate()) {
                modifiers = 1;
            }
            if (flags.isInterface()) {
                modifiers |= 512;
            }
            if (flags.isAbstract()) {
                modifiers |= 1024;
            }
            if (cDecl.type().outer().flags().isInterface()) {
                modifiers |= 1;
            }
        } else {
            modifiers = getModifiers(flags);
        }
        this.sootClass.setModifiers(modifiers);
    }

    private SootClass getSpecialInterfaceAnonClass(SootClass addToClass) {
        if (InitialResolver.v().specialAnonMap() != null && InitialResolver.v().specialAnonMap().containsKey(addToClass)) {
            return InitialResolver.v().specialAnonMap().get(addToClass);
        }
        String specialClassName = String.valueOf(addToClass.getName()) + "$" + InitialResolver.v().getNextAnonNum();
        SootClass specialClass = new SootClass(specialClassName);
        Scene.v().addClass(specialClass);
        specialClass.setApplicationClass();
        specialClass.addTag(new SyntheticTag());
        specialClass.setSuperclass(Scene.v().getSootClass(Scene.v().getObjectType().toString()));
        Util.addInnerClassTag(addToClass, specialClass.getName(), addToClass.getName(), null, 8);
        Util.addInnerClassTag(specialClass, specialClass.getName(), addToClass.getName(), null, 8);
        InitialResolver.v().addNameToAST(specialClassName);
        this.references.add(RefType.v(specialClassName));
        if (InitialResolver.v().specialAnonMap() == null) {
            InitialResolver.v().setSpecialAnonMap(new HashMap<>());
        }
        InitialResolver.v().specialAnonMap().put(addToClass, specialClass);
        return specialClass;
    }

    private void handleAssert(ClassBody cBody) {
        SootClass addToClass;
        AssertStmtChecker asc = new AssertStmtChecker();
        cBody.visit(asc);
        if (!asc.isHasAssert()) {
            return;
        }
        Type fieldType = BooleanType.v();
        if (!this.sootClass.declaresField("$assertionsDisabled", fieldType)) {
            SootField assertionsDisabledField = Scene.v().makeSootField("$assertionsDisabled", fieldType, 24);
            this.sootClass.addField(assertionsDisabledField);
            assertionsDisabledField.addTag(new SyntheticTag());
        }
        SootClass sootClass = this.sootClass;
        while (true) {
            addToClass = sootClass;
            if (InitialResolver.v().getInnerClassInfoMap() == null || !InitialResolver.v().getInnerClassInfoMap().containsKey(addToClass)) {
                break;
            }
            sootClass = InitialResolver.v().getInnerClassInfoMap().get(addToClass).getOuterClass();
        }
        String fieldName = "class$" + addToClass.getName().replaceAll(".", "$");
        if (InitialResolver.v().getInterfacesList() != null && InitialResolver.v().getInterfacesList().contains(addToClass.getName())) {
            addToClass = getSpecialInterfaceAnonClass(addToClass);
        }
        Type fieldType2 = RefType.v("java.lang.Class");
        if (!addToClass.declaresField(fieldName, fieldType2)) {
            SootField classField = Scene.v().makeSootField(fieldName, fieldType2, 8);
            addToClass.addField(classField);
            classField.addTag(new SyntheticTag());
        }
        Type methodRetType = RefType.v("java.lang.Class");
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(RefType.v("java.lang.String"));
        SootMethod sootMethod = Scene.v().makeSootMethod("class$", paramTypes, methodRetType, 8);
        AssertClassMethodSource assertMSrc = new AssertClassMethodSource();
        sootMethod.setSource(assertMSrc);
        if (!addToClass.declaresMethod("class$", paramTypes, methodRetType)) {
            addToClass.addMethod(sootMethod);
            sootMethod.addTag(new SyntheticTag());
        }
        Type methodRetType2 = VoidType.v();
        ArrayList paramTypes2 = new ArrayList();
        SootMethod sootMethod2 = Scene.v().makeSootMethod("<clinit>", paramTypes2, methodRetType2, 8);
        PolyglotMethodSource mSrc = new PolyglotMethodSource();
        mSrc.setJBB(InitialResolver.v().getJBBFactory().createJimpleBodyBuilder());
        mSrc.hasAssert(true);
        sootMethod2.setSource(mSrc);
        if (!this.sootClass.declaresMethod("<clinit>", paramTypes2, methodRetType2)) {
            this.sootClass.addMethod(sootMethod2);
        } else {
            ((PolyglotMethodSource) this.sootClass.getMethod("<clinit>", paramTypes2, methodRetType2).getSource()).hasAssert(true);
        }
    }

    private void createConstructorDecl(ConstructorDecl constructor) {
        ArrayList parameters = createParameters(constructor);
        ArrayList<SootClass> exceptions = createExceptions(constructor);
        SootMethod sootMethod = createSootConstructor("<init>", constructor.flags(), parameters, exceptions);
        finishProcedure(constructor, sootMethod);
    }

    private void createMethodDecl(MethodDecl method) {
        String name = createName(method);
        ArrayList parameters = createParameters(method);
        ArrayList<SootClass> exceptions = createExceptions(method);
        SootMethod sootMethod = createSootMethod(name, method.flags(), method.returnType().type(), parameters, exceptions);
        finishProcedure(method, sootMethod);
    }

    private void finishProcedure(ProcedureDecl procedure, SootMethod sootMethod) {
        addProcedureToClass(sootMethod);
        if (procedure.position() != null) {
            Util.addLnPosTags(sootMethod, procedure.position());
        }
        PolyglotMethodSource mSrc = new PolyglotMethodSource(procedure.body(), procedure.formals());
        mSrc.setJBB(InitialResolver.v().getJBBFactory().createJimpleBodyBuilder());
        sootMethod.setSource(mSrc);
    }

    private void handleFieldInits() {
        if (this.fieldInits != null || this.initializerBlocks != null) {
            for (SootMethod next : this.sootClass.getMethods()) {
                if (next.getName().equals("<init>")) {
                    PolyglotMethodSource src = (PolyglotMethodSource) next.getSource();
                    src.setInitializerBlocks(this.initializerBlocks);
                    src.setFieldInits(this.fieldInits);
                }
            }
        }
    }

    private void handleClassLiteral(ClassBody cBody) {
        ClassLiteralChecker classLitChecker = new ClassLiteralChecker();
        cBody.visit(classLitChecker);
        ArrayList<Node> classLitList = classLitChecker.getList();
        if (!classLitList.isEmpty()) {
            SootClass addToClass = this.sootClass;
            if (addToClass.isInterface()) {
                addToClass = getSpecialInterfaceAnonClass(addToClass);
            }
            Type methodRetType = RefType.v("java.lang.Class");
            ArrayList paramTypes = new ArrayList();
            paramTypes.add(RefType.v("java.lang.String"));
            SootMethod sootMethod = Scene.v().makeSootMethod("class$", paramTypes, methodRetType, 8);
            ClassLiteralMethodSource mSrc = new ClassLiteralMethodSource();
            sootMethod.setSource(mSrc);
            if (!addToClass.declaresMethod("class$", paramTypes, methodRetType)) {
                addToClass.addMethod(sootMethod);
                sootMethod.addTag(new SyntheticTag());
            }
            Iterator<Node> classLitIt = classLitList.iterator();
            while (classLitIt.hasNext()) {
                ClassLit classLit = (ClassLit) classLitIt.next();
                String fieldName = Util.getFieldNameForClassLit(classLit.typeNode().type());
                Type fieldType = RefType.v("java.lang.Class");
                SootField sootField = Scene.v().makeSootField(fieldName, fieldType, 8);
                if (!addToClass.declaresField(fieldName, fieldType)) {
                    addToClass.addField(sootField);
                    sootField.addTag(new SyntheticTag());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createSource(SourceFile source) {
        SourceFileTag t = (SourceFileTag) this.sootClass.getTag(SourceFileTag.NAME);
        if (t != null) {
            t.setAbsolutePath(source.source().path());
        } else {
            SourceFileTag t2 = new SourceFileTag();
            t2.setAbsolutePath(source.source().path());
            this.sootClass.addTag(t2);
        }
        String simpleName = this.sootClass.getName();
        boolean found = false;
        for (Object next : source.decls()) {
            if (next instanceof ClassDecl) {
                ClassType nextType = ((ClassDecl) next).type();
                if (Util.getSootType(nextType).equals(this.sootClass.getType())) {
                    createClassDecl((ClassDecl) next);
                    found = true;
                }
            }
        }
        if (!found) {
            NestedClassListBuilder nestedClassBuilder = new NestedClassListBuilder();
            source.visit(nestedClassBuilder);
            Iterator<Node> nestedDeclsIt = nestedClassBuilder.getClassDeclsList().iterator();
            while (nestedDeclsIt.hasNext() && !found) {
                ClassDecl nextDecl = (ClassDecl) nestedDeclsIt.next();
                ClassType type = nextDecl.type();
                if (type.isLocal() && !type.isAnonymous()) {
                    if (InitialResolver.v().getLocalClassMap().containsVal(simpleName)) {
                        createClassDecl(((LocalClassDecl) InitialResolver.v().getLocalClassMap().getKey(simpleName)).decl());
                        found = true;
                    }
                } else if (Util.getSootType(type).equals(this.sootClass.getType())) {
                    createClassDecl(nextDecl);
                    found = true;
                }
            }
            if (!found && InitialResolver.v().getAnonClassMap() != null && InitialResolver.v().getAnonClassMap().containsVal(simpleName)) {
                New aNew = (New) InitialResolver.v().getAnonClassMap().getKey(simpleName);
                if (aNew == null) {
                    throw new RuntimeException("Could resolve class: " + simpleName);
                }
                createAnonClassDecl(aNew);
                findReferences(aNew.body());
                createClassBody(aNew.body());
                handleFieldInits();
            }
        }
    }

    private void handleInnerClassTags(ClassBody classBody) {
        int modifiers;
        if (InitialResolver.v().getInnerClassInfoMap() != null && InitialResolver.v().getInnerClassInfoMap().containsKey(this.sootClass)) {
            InnerClassInfo tag = InitialResolver.v().getInnerClassInfoMap().get(this.sootClass);
            Util.addInnerClassTag(this.sootClass, this.sootClass.getName(), tag.getInnerType() == 3 ? null : tag.getOuterClass().getName(), tag.getInnerType() == 3 ? null : tag.getSimpleName(), Modifier.isInterface(tag.getOuterClass().getModifiers()) ? 9 : this.sootClass.getModifiers());
            SootClass outerClass = tag.getOuterClass();
            while (true) {
                SootClass outerClass2 = outerClass;
                if (InitialResolver.v().getInnerClassInfoMap().containsKey(outerClass2)) {
                    InnerClassInfo tag2 = InitialResolver.v().getInnerClassInfoMap().get(outerClass2);
                    SootClass sootClass = this.sootClass;
                    String name = outerClass2.getName();
                    String name2 = tag2.getInnerType() == 3 ? null : tag2.getOuterClass().getName();
                    String simpleName = tag2.getInnerType() == 3 ? null : tag2.getSimpleName();
                    if (tag2.getInnerType() == 3 && Modifier.isInterface(tag2.getOuterClass().getModifiers())) {
                        modifiers = 9;
                    } else {
                        modifiers = outerClass2.getModifiers();
                    }
                    Util.addInnerClassTag(sootClass, name, name2, simpleName, modifiers);
                    outerClass = tag2.getOuterClass();
                } else {
                    return;
                }
            }
        }
    }

    private void addQualifierRefToInit(polyglot.types.Type type) {
        Type sootType = Util.getSootType(type);
        for (SootMethod meth : this.sootClass.getMethods()) {
            if (meth.getName().equals("<init>")) {
                List<Type> newParams = new ArrayList<>();
                newParams.add(sootType);
                newParams.addAll(meth.getParameterTypes());
                meth.setParameterTypes(newParams);
                meth.addTag(new QualifyingTag());
            }
        }
    }

    private void addProcedureToClass(SootMethod method) {
        this.sootClass.addMethod(method);
    }

    private void addConstValTag(FieldDecl field, SootField sootField) {
        if (field.fieldInstance().constantValue() instanceof Integer) {
            sootField.addTag(new IntegerConstantValueTag(((Integer) field.fieldInstance().constantValue()).intValue()));
        } else if (field.fieldInstance().constantValue() instanceof Character) {
            sootField.addTag(new IntegerConstantValueTag(((Character) field.fieldInstance().constantValue()).charValue()));
        } else if (field.fieldInstance().constantValue() instanceof Short) {
            sootField.addTag(new IntegerConstantValueTag(((Short) field.fieldInstance().constantValue()).shortValue()));
        } else if (field.fieldInstance().constantValue() instanceof Byte) {
            sootField.addTag(new IntegerConstantValueTag(((Byte) field.fieldInstance().constantValue()).byteValue()));
        } else if (field.fieldInstance().constantValue() instanceof Boolean) {
            boolean b = ((Boolean) field.fieldInstance().constantValue()).booleanValue();
            sootField.addTag(new IntegerConstantValueTag(b ? 1 : 0));
        } else if (field.fieldInstance().constantValue() instanceof Long) {
            sootField.addTag(new LongConstantValueTag(((Long) field.fieldInstance().constantValue()).longValue()));
        } else if (field.fieldInstance().constantValue() instanceof Double) {
            sootField.addTag(new DoubleConstantValueTag((long) ((Double) field.fieldInstance().constantValue()).doubleValue()));
            DoubleConstantValueTag doubleConstantValueTag = (DoubleConstantValueTag) sootField.getTag(DoubleConstantValueTag.NAME);
        } else if (field.fieldInstance().constantValue() instanceof Float) {
            sootField.addTag(new FloatConstantValueTag(((Float) field.fieldInstance().constantValue()).floatValue()));
        } else if (field.fieldInstance().constantValue() instanceof String) {
            sootField.addTag(new StringConstantValueTag((String) field.fieldInstance().constantValue()));
        } else {
            throw new RuntimeException("Expecting static final field to have a constant value! For field: " + field + " of type: " + field.fieldInstance().constantValue().getClass());
        }
    }

    private void createFieldDecl(FieldDecl field) {
        int modifiers = Util.getModifier(field.fieldInstance().flags());
        String name = field.fieldInstance().name();
        Type sootType = Util.getSootType(field.fieldInstance().type());
        SootField sootField = Scene.v().makeSootField(name, sootType, modifiers);
        this.sootClass.addField(sootField);
        if (field.fieldInstance().flags().isStatic()) {
            if (field.init() != null) {
                if (field.flags().isFinal() && ((field.type().type().isPrimitive() || field.type().type().toString().equals("java.lang.String")) && field.fieldInstance().isConstant())) {
                    addConstValTag(field, sootField);
                } else {
                    if (this.staticFieldInits == null) {
                        this.staticFieldInits = new ArrayList<>();
                    }
                    this.staticFieldInits.add(field);
                }
            }
        } else if (field.init() != null) {
            if (this.fieldInits == null) {
                this.fieldInits = new ArrayList<>();
            }
            this.fieldInits.add(field);
        }
        Util.addLnPosTags(sootField, field.position());
    }

    public ClassResolver(SootClass sootClass, Set<Type> set) {
        this.sootClass = sootClass;
        this.references = set;
    }

    private String createName(ProcedureDecl procedure) {
        return procedure.name();
    }

    private ArrayList createParameters(ProcedureDecl procedure) {
        ArrayList parameters = new ArrayList();
        for (Formal next : procedure.formals()) {
            parameters.add(Util.getSootType(next.type().type()));
        }
        return parameters;
    }

    private ArrayList<SootClass> createExceptions(ProcedureDecl procedure) {
        ArrayList<SootClass> exceptions = new ArrayList<>();
        for (TypeNode typeNode : procedure.throwTypes()) {
            polyglot.types.Type throwType = typeNode.type();
            exceptions.add(((RefType) Util.getSootType(throwType)).getSootClass());
        }
        return exceptions;
    }

    private SootMethod createSootMethod(String name, Flags flags, polyglot.types.Type returnType, ArrayList parameters, ArrayList<SootClass> exceptions) {
        int modifier = Util.getModifier(flags);
        Type sootReturnType = Util.getSootType(returnType);
        return Scene.v().makeSootMethod(name, parameters, sootReturnType, modifier, exceptions);
    }

    private void createInitializer(Initializer initializer) {
        if (initializer.flags().isStatic()) {
            if (this.staticInitializerBlocks == null) {
                this.staticInitializerBlocks = new ArrayList<>();
            }
            this.staticInitializerBlocks.add(initializer.body());
            return;
        }
        if (this.initializerBlocks == null) {
            this.initializerBlocks = new ArrayList<>();
        }
        this.initializerBlocks.add(initializer.body());
    }

    private SootMethod createSootConstructor(String name, Flags flags, ArrayList parameters, ArrayList<SootClass> exceptions) {
        int modifier = Util.getModifier(flags);
        return Scene.v().makeSootMethod(name, parameters, VoidType.v(), modifier, exceptions);
    }
}
