package org.powermock.core.transformers.javassist.support;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import javassist.bytecode.FieldInfo;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import org.powermock.core.IndicateReloadClass;
import org.powermock.core.MockGateway;
import org.powermock.core.transformers.TransformStrategy;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/transformers/javassist/support/PowerMockExpressionEditor.class */
public final class PowerMockExpressionEditor extends ExprEditor {
    private final CtClass clazz;
    private final Class<?> mockGetawayClass;
    private final TransformStrategy strategy;

    public PowerMockExpressionEditor(TransformStrategy strategy, CtClass clazz, Class<?> mockGetawayClass) {
        this.strategy = strategy;
        this.clazz = clazz;
        this.mockGetawayClass = mockGetawayClass;
    }

    @Override // javassist.expr.ExprEditor
    public void edit(NewExpr e) throws CannotCompileException {
        String code = "Object instance =" + MockGateway.class.getName() + ".newInstanceCall($type,$args,$sig);if(instance != " + MockGateway.class.getName() + ".PROCEED) {\tif(instance instanceof java.lang.reflect.Constructor) {\t\t$_ = ($r) sun.reflect.ReflectionFactory.getReflectionFactory().newConstructorForSerialization($type, java.lang.Object.class.getDeclaredConstructor(null)).newInstance(null);\t} else {\t\t$_ = ($r) instance;\t}} else {\t$_ = $proceed($$);}";
        e.replace(code);
    }

    @Override // javassist.expr.ExprEditor
    public void edit(MethodCall m) throws CannotCompileException {
        try {
            CtMethod method = m.getMethod();
            CtClass declaringClass = method.getDeclaringClass();
            if (declaringClass != null && TransformerHelper.shouldTreatAsSystemClassCall(declaringClass)) {
                StringBuilder code = new StringBuilder();
                code.append("{Object classOrInstance = null; if($0!=null){classOrInstance = $0;} else { classOrInstance = $class;}");
                code.append("Object value =  ").append(MockGateway.class.getName()).append(".methodCall(").append("classOrInstance,\"").append(m.getMethodName()).append("\",$args, $sig,\"").append(TransformerHelper.getReturnTypeAsString(method)).append("\");");
                code.append("if(value == ").append(MockGateway.class.getName()).append(".PROCEED) {");
                code.append("\t$_ = $proceed($$);");
                code.append("} else {");
                String correctReturnValueType = TransformerHelper.getCorrectReturnValueType(method.getReturnType());
                if (!"".equals(correctReturnValueType)) {
                    code.append("\t$_ = ").append(correctReturnValueType).append(";");
                }
                code.append("}}");
                m.replace(code.toString());
            }
        } catch (NotFoundException e) {
        }
    }

    @Override // javassist.expr.ExprEditor
    public void edit(ConstructorCall c) throws CannotCompileException {
        if (this.strategy != TransformStrategy.INST_REDEFINE && !c.getClassName().startsWith("java.lang")) {
            try {
                CtClass superclass = this.clazz.getSuperclass();
                addNewDeferConstructor(this.clazz);
                StringBuilder code = new StringBuilder();
                code.append("{Object value =").append(this.mockGetawayClass.getName()).append(".constructorCall($class, $args, $sig);");
                code.append("if (value != ").append(MockGateway.class.getName()).append(".PROCEED){");
                if (superclass.getName().equals(Object.class.getName())) {
                    code.append(" super();");
                } else {
                    code.append(" super((").append(IndicateReloadClass.class.getName()).append(") null);");
                }
                code.append("} else {");
                code.append("   $proceed($$);");
                code.append("}}");
                c.replace(code.toString());
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override // javassist.expr.ExprEditor
    public void edit(FieldAccess f) throws CannotCompileException {
        if (f.isReader()) {
            try {
                CtField field = f.getField();
                CtClass returnTypeAsCtClass = field.getType();
                FieldInfo fieldInfo = field.getFieldInfo2();
                if (TransformerHelper.isNotSyntheticField(fieldInfo)) {
                    String code = "{Object value =  " + MockGateway.class.getName() + ".fieldCall($0,$class,\"" + f.getFieldName() + "\",$type);if(value == " + MockGateway.class.getName() + ".PROCEED) {\t$_ = $proceed($$);} else {\t$_ = " + TransformerHelper.getCorrectReturnValueType(returnTypeAsCtClass) + ";}}";
                    f.replace(code);
                }
            } catch (NotFoundException e) {
            }
        }
    }

    private void addNewDeferConstructor(CtClass clazz) throws CannotCompileException {
        try {
            CtClass superClass = clazz.getSuperclass();
            ClassPool classPool = clazz.getClassPool();
            try {
                CtClass constructorType = classPool.get(IndicateReloadClass.class.getName());
                clazz.defrost();
                if (superClass.getName().equals(Object.class.getName())) {
                    try {
                        clazz.addConstructor(CtNewConstructor.make(new CtClass[]{constructorType}, new CtClass[0], "{super();}", clazz));
                        return;
                    } catch (DuplicateMemberException e) {
                        return;
                    }
                }
                addNewDeferConstructor(superClass);
                try {
                    clazz.addConstructor(CtNewConstructor.make(new CtClass[]{constructorType}, new CtClass[0], "{super($$);}", clazz));
                } catch (DuplicateMemberException e2) {
                }
            } catch (NotFoundException e3) {
                throw new IllegalArgumentException("Internal error: failed to get the " + IndicateReloadClass.class.getName() + " when added defer constructor.");
            }
        } catch (NotFoundException e4) {
            throw new IllegalArgumentException("Internal error: Failed to get superclass for " + clazz.getName() + " when about to create a new default constructor.");
        }
    }
}
