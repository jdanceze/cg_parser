package soot.dexpler;

import java.util.ArrayList;
import java.util.List;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodSource;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.Type;
import soot.jimple.Jimple;
import soot.jimple.toolkits.typing.TypeAssigner;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexMethod.class */
public class DexMethod {
    private static final Logger logger = LoggerFactory.getLogger(DexMethod.class);
    protected final MultiDexContainer.DexEntry<? extends DexFile> dexEntry;
    protected final SootClass declaringClass;

    public DexMethod(MultiDexContainer.DexEntry<? extends DexFile> dexFile, SootClass declaringClass) {
        this.dexEntry = dexFile;
        this.declaringClass = declaringClass;
    }

    public SootMethod makeSootMethod(Method method) {
        int accessFlags = method.getAccessFlags();
        String name = method.getName();
        List<SootClass> thrownExceptions = getThrownExceptions(method);
        List<Type> parameterTypes = getParameterTypes(method);
        Type returnType = DexType.toSoot(method.getReturnType());
        SootMethod sm = this.declaringClass.getMethodUnsafe(name, parameterTypes, returnType);
        if (sm == null) {
            sm = Scene.v().makeSootMethod(name, parameterTypes, returnType, accessFlags, thrownExceptions);
        }
        int flags = method.getAccessFlags();
        if (Modifier.isAbstract(flags) || Modifier.isNative(flags) || (Options.v().oaat() && this.declaringClass.resolvingLevel() <= 2)) {
            return sm;
        }
        sm.setSource(createMethodSource(method));
        return sm;
    }

    protected MethodSource createMethodSource(final Method method) {
        return new MethodSource() { // from class: soot.dexpler.DexMethod.1
            @Override // soot.MethodSource
            public Body getBody(SootMethod m, String phaseName) {
                Body b = Jimple.v().newBody(m);
                try {
                    DexBody dexBody = new DexBody(DexMethod.this.dexEntry, method, DexMethod.this.declaringClass.getType());
                    dexBody.jimplify(b, m);
                } catch (InvalidDalvikBytecodeException e) {
                    String msg = "Warning: Invalid bytecode in method " + m + ": " + e;
                    DexMethod.logger.debug(msg);
                    Util.emptyBody(b);
                    Util.addExceptionAfterUnit(b, "java.lang.RuntimeException", b.getUnits().getLast(), "Soot has detected that this method contains invalid Dalvik bytecode, which would have throw an exception at runtime. [" + msg + "]");
                    TypeAssigner.v().transform(b);
                }
                m.setActiveBody(b);
                return m.getActiveBody();
            }
        };
    }

    protected List<Type> getParameterTypes(Method method) {
        List<Type> parameterTypes = new ArrayList<>();
        if (method.getParameters() != null) {
            List<? extends CharSequence> parameters = method.getParameterTypes();
            for (CharSequence t : parameters) {
                Type type = DexType.toSoot(t.toString());
                parameterTypes.add(type);
            }
        }
        return parameterTypes;
    }

    protected List<SootClass> getThrownExceptions(Method method) {
        List<SootClass> thrownExceptions = new ArrayList<>();
        for (Annotation a : method.getAnnotations()) {
            Type atype = DexType.toSoot(a.getType());
            String atypes = atype.toString();
            if (atypes.equals(DexAnnotation.DALVIK_ANNOTATION_THROWS)) {
                for (AnnotationElement ae : a.getElements()) {
                    EncodedValue ev = ae.getValue();
                    if (ev instanceof ArrayEncodedValue) {
                        for (EncodedValue evSub : ((ArrayEncodedValue) ev).getValue()) {
                            if (evSub instanceof TypeEncodedValue) {
                                TypeEncodedValue valueType = (TypeEncodedValue) evSub;
                                String exceptionName = valueType.getValue();
                                String dottedName = Util.dottedClassName(exceptionName);
                                thrownExceptions.add(SootResolver.v().makeClassRef(dottedName));
                            }
                        }
                    }
                }
            }
        }
        return thrownExceptions;
    }
}
