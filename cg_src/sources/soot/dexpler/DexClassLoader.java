package soot.dexpler;

import java.util.Iterator;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MultiDexContainer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootResolver;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexClassLoader.class */
public class DexClassLoader {
    protected void loadMethod(Method method, SootClass declaringClass, DexAnnotation annotations, DexMethod dexMethodFactory) {
        SootMethod sm = dexMethodFactory.makeSootMethod(method);
        if (declaringClass.declaresMethod(sm.getName(), sm.getParameterTypes(), sm.getReturnType())) {
            return;
        }
        declaringClass.addMethod(sm);
        annotations.handleMethodAnnotation(sm, method);
    }

    public IInitialResolver.Dependencies makeSootClass(SootClass sc, ClassDef defItem, MultiDexContainer.DexEntry<? extends DexFile> dexEntry) {
        String superClass = defItem.getSuperclass();
        IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
        String sourceFile = defItem.getSourceFile();
        if (sourceFile != null) {
            sc.addTag(new SourceFileTag(sourceFile));
        }
        if (superClass != null) {
            String superClassName = Util.dottedClassName(superClass);
            SootClass sootSuperClass = SootResolver.v().makeClassRef(superClassName);
            sc.setSuperclass(sootSuperClass);
            deps.typesToHierarchy.add(sootSuperClass.getType());
        }
        int accessFlags = defItem.getAccessFlags();
        sc.setModifiers(accessFlags);
        if (defItem.getInterfaces() != null) {
            for (String interfaceName : defItem.getInterfaces()) {
                String interfaceClassName = Util.dottedClassName(interfaceName);
                if (!sc.implementsInterface(interfaceClassName)) {
                    SootClass interfaceClass = SootResolver.v().makeClassRef(interfaceClassName);
                    interfaceClass.setModifiers(interfaceClass.getModifiers() | 512);
                    sc.addInterface(interfaceClass);
                    deps.typesToHierarchy.add(interfaceClass.getType());
                }
            }
        }
        if (Options.v().oaat() && sc.resolvingLevel() <= 1) {
            return deps;
        }
        DexAnnotation da = createDexAnnotation(sc, deps);
        for (Field sf : defItem.getStaticFields()) {
            loadField(sc, da, sf);
        }
        for (Field f : defItem.getInstanceFields()) {
            loadField(sc, da, f);
        }
        DexMethod dexMethod = createDexMethodFactory(dexEntry, sc);
        for (Method method : defItem.getDirectMethods()) {
            loadMethod(method, sc, da, dexMethod);
        }
        for (Method method2 : defItem.getVirtualMethods()) {
            loadMethod(method2, sc, da, dexMethod);
        }
        da.handleClassAnnotation(defItem);
        InnerClassAttribute ica = (InnerClassAttribute) sc.getTag(InnerClassAttribute.NAME);
        if (ica != null) {
            Iterator<InnerClassTag> innerTagIt = ica.getSpecs().iterator();
            while (innerTagIt.hasNext()) {
                Tag t = innerTagIt.next();
                if (t instanceof InnerClassTag) {
                    InnerClassTag ict = (InnerClassTag) t;
                    String outer = DexInnerClassParser.getOuterClassNameFromTag(ict);
                    if (outer == null || outer.length() == 0) {
                        innerTagIt.remove();
                    } else if (!outer.equals(sc.getName())) {
                        String inner = ict.getInnerClass().replaceAll("/", ".");
                        if (!inner.equals(sc.getName())) {
                            innerTagIt.remove();
                        } else {
                            SootClass osc = SootResolver.v().makeClassRef(outer);
                            if (osc == sc) {
                                if (sc.hasOuterClass()) {
                                    osc = sc.getOuterClass();
                                }
                            } else {
                                deps.typesToHierarchy.add(osc.getType());
                            }
                            InnerClassAttribute icat = (InnerClassAttribute) osc.getTag(InnerClassAttribute.NAME);
                            if (icat == null) {
                                icat = new InnerClassAttribute();
                                osc.addTag(icat);
                            }
                            icat.add(new InnerClassTag(ict.getInnerClass(), ict.getOuterClass(), ict.getShortName(), ict.getAccessFlags()));
                            innerTagIt.remove();
                            if (!sc.hasTag(InnerClassTag.NAME) && ((InnerClassTag) t).getInnerClass().replaceAll("/", ".").equals(sc.toString())) {
                                sc.addTag(t);
                            }
                        }
                    }
                }
            }
            if (ica.getSpecs().isEmpty()) {
                sc.getTags().remove(ica);
            }
        }
        return deps;
    }

    protected DexAnnotation createDexAnnotation(SootClass clazz, IInitialResolver.Dependencies deps) {
        return new DexAnnotation(clazz, deps);
    }

    protected DexMethod createDexMethodFactory(MultiDexContainer.DexEntry<? extends DexFile> dexEntry, SootClass sc) {
        return new DexMethod(dexEntry, sc);
    }

    protected void loadField(SootClass declaringClass, DexAnnotation annotations, Field sf) {
        if (declaringClass.declaresField(sf.getName(), DexType.toSoot(sf.getType()))) {
            return;
        }
        SootField sootField = DexField.makeSootField(sf);
        annotations.handleFieldAnnotation(declaringClass.getOrAddField(sootField), sf);
    }
}
