package org.powermock.core.transformers.javassist.testclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import org.powermock.core.IndicateReloadClass;
import org.powermock.core.testlisteners.GlobalNotificationBuildSupport;
import org.powermock.core.transformers.ClassWrapper;
import org.powermock.core.transformers.MethodSignatureWriter;
import org.powermock.core.transformers.TestClassTransformer;
import org.powermock.core.transformers.javassist.support.Primitives;
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/testclass/JavaAssistTestClassTransformer.class */
public abstract class JavaAssistTestClassTransformer extends TestClassTransformer<CtClass, CtMethod> {
    protected abstract boolean mustHaveTestAnnotationRemoved(CtMethod ctMethod) throws Exception;

    /* JADX INFO: Access modifiers changed from: package-private */
    public JavaAssistTestClassTransformer(Class<?> testClass, Class<? extends Annotation> testMethodAnnotationType, MethodSignatureWriter<CtMethod> signatureWriter) {
        super(testClass, testMethodAnnotationType, signatureWriter);
    }

    @Override // org.powermock.core.transformers.MockTransformer
    public ClassWrapper<CtClass> transform(ClassWrapper<CtClass> clazz) throws Exception {
        transform(clazz.unwrap());
        return clazz;
    }

    private void transform(CtClass clazz) throws Exception {
        if (clazz.isFrozen()) {
            clazz.defrost();
        }
        if (isTestClass(clazz)) {
            removeTestAnnotationsForTestMethodsThatRunOnOtherClassLoader(clazz);
            addLifeCycleNotifications(clazz);
            makeDeferConstructorNonPublic(clazz);
            restoreOriginalConstructorsAccesses(clazz);
        } else if (isNestedWithinTestClass(clazz)) {
            makeDeferConstructorNonPublic(clazz);
            restoreOriginalConstructorsAccesses(clazz);
        }
    }

    private boolean isTestClass(CtClass clazz) {
        try {
            return Class.forName(clazz.getName(), false, getTestClass().getClassLoader()).isAssignableFrom(getTestClass());
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean isNestedWithinTestClass(CtClass clazz) {
        String clazzName = clazz.getName();
        return clazzName.startsWith(getTestClass().getName()) && '$' == clazzName.charAt(getTestClass().getName().length());
    }

    private Class<?> asOriginalClass(CtClass type) throws Exception {
        try {
            if (type.isArray()) {
                return Array.newInstance(asOriginalClass(type.getComponentType()), 0).getClass();
            }
            if (type.isPrimitive()) {
                return Primitives.getClassFor((CtPrimitiveType) type);
            }
            return Class.forName(type.getName(), true, getTestClass().getClassLoader());
        } catch (Exception ex) {
            throw new RuntimeException("Cannot resolve type: " + type, ex);
        }
    }

    private Class<?>[] asOriginalClassParams(CtClass[] parameterTypes) throws Exception {
        Class<?>[] classParams = new Class[parameterTypes.length];
        for (int i = 0; i < classParams.length; i++) {
            classParams[i] = asOriginalClass(parameterTypes[i]);
        }
        return classParams;
    }

    private void removeTestMethodAnnotationFrom(CtMethod m) {
        javassist.bytecode.annotation.Annotation[] annotations;
        AnnotationsAttribute attr = (AnnotationsAttribute) m.getMethodInfo().getAttribute("RuntimeVisibleAnnotations");
        javassist.bytecode.annotation.Annotation[] newAnnotations = new javassist.bytecode.annotation.Annotation[attr.numAnnotations() - 1];
        int i = -1;
        for (javassist.bytecode.annotation.Annotation a : attr.getAnnotations()) {
            if (!a.getTypeName().equals(getTestMethodAnnotationType().getName())) {
                i++;
                newAnnotations[i] = a;
            }
        }
        attr.setAnnotations(newAnnotations);
    }

    private void removeTestAnnotationsForTestMethodsThatRunOnOtherClassLoader(CtClass clazz) throws Exception {
        CtMethod[] declaredMethods;
        for (CtMethod m : clazz.getDeclaredMethods()) {
            if (m.hasAnnotation(getTestMethodAnnotationType()) && mustHaveTestAnnotationRemoved(m)) {
                removeTestMethodAnnotationFrom(m);
            }
        }
    }

    private void addLifeCycleNotifications(CtClass clazz) {
        try {
            addClassInitializerNotification(clazz);
            addConstructorNotification(clazz);
        } catch (CannotCompileException ex) {
            throw new Error("Powermock error: " + ex.getMessage(), ex);
        }
    }

    private void addClassInitializerNotification(CtClass clazz) throws CannotCompileException {
        if (null == clazz.getClassInitializer()) {
            clazz.makeClassInitializer();
        }
        clazz.getClassInitializer().insertBefore(GlobalNotificationBuildSupport.class.getName() + ".testClassInitiated(" + clazz.getName() + ".class);");
    }

    private static boolean hasSuperClass(CtClass clazz) {
        try {
            CtClass superClazz = clazz.getSuperclass();
            if (null != superClazz) {
                if (!JavaBasicTypes.JAVA_LANG_OBJECT.equals(superClazz.getName())) {
                    return true;
                }
            }
            return false;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private void addConstructorNotification(CtClass clazz) throws CannotCompileException {
        CtConstructor[] declaredConstructors;
        String notificationCode = GlobalNotificationBuildSupport.class.getName() + ".testInstanceCreated(this);";
        boolean asFinally = !hasSuperClass(clazz);
        for (CtConstructor constr : clazz.getDeclaredConstructors()) {
            constr.insertAfter(notificationCode, asFinally);
        }
    }

    private void restoreOriginalConstructorsAccesses(CtClass clazz) throws Exception {
        Class<?> cls;
        CtConstructor[] constructors;
        if (getTestClass().getName().equals(clazz.getName())) {
            cls = getTestClass();
        } else {
            cls = Class.forName(clazz.getName(), true, getTestClass().getClassLoader());
        }
        Class<?> originalClass = cls;
        for (CtConstructor ctConstr : clazz.getConstructors()) {
            int ctModifiers = ctConstr.getModifiers();
            if (Modifier.isPublic(ctModifiers)) {
                int desiredAccessModifiers = originalClass.getDeclaredConstructor(asOriginalClassParams(ctConstr.getParameterTypes())).getModifiers();
                if (Modifier.isPrivate(desiredAccessModifiers)) {
                    ctConstr.setModifiers(Modifier.setPrivate(ctModifiers));
                } else if (Modifier.isProtected(desiredAccessModifiers)) {
                    ctConstr.setModifiers(Modifier.setProtected(ctModifiers));
                } else if (!Modifier.isPublic(desiredAccessModifiers)) {
                    ctConstr.setModifiers(Modifier.setPackage(ctModifiers));
                }
            }
        }
    }

    private void makeDeferConstructorNonPublic(CtClass clazz) {
        CtConstructor[] constructors;
        for (CtConstructor constr : clazz.getConstructors()) {
            try {
                CtClass[] parameterTypes = constr.getParameterTypes();
                int length = parameterTypes.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    CtClass paramType = parameterTypes[i];
                    if (!IndicateReloadClass.class.getName().equals(paramType.getName())) {
                        i++;
                    } else {
                        int modifiers = constr.getModifiers();
                        if (Modifier.isPublic(modifiers)) {
                            constr.setModifiers(Modifier.setProtected(modifiers));
                        }
                    }
                }
            } catch (NotFoundException e) {
            }
        }
    }
}
