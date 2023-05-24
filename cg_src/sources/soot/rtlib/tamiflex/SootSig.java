package soot.rtlib.tamiflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/rtlib/tamiflex/SootSig.class */
public class SootSig {
    private static final Logger logger = LoggerFactory.getLogger(SootSig.class);
    private static Map<Constructor<?>, String> constrCache = new ConcurrentHashMap();
    private static Map<Method, String> methodCache = new ConcurrentHashMap();

    public static String sootSignature(Constructor<?> c) {
        String res = constrCache.get(c);
        if (res == null) {
            String[] paramTypes = classesToTypeNames(c.getParameterTypes());
            res = sootSignature(c.getDeclaringClass().getName(), Jimple.VOID, "<init>", paramTypes);
            constrCache.put(c, res);
        }
        return res;
    }

    public static String sootSignature(Object receiver, Method m) {
        Class<?> receiverClass = Modifier.isStatic(m.getModifiers()) ? m.getDeclaringClass() : receiver.getClass();
        Method resolved = null;
        Class<?> c = receiverClass;
        do {
            try {
                try {
                    resolved = c.getDeclaredMethod(m.getName(), m.getParameterTypes());
                } catch (NoSuchMethodException e) {
                    c = c.getSuperclass();
                }
                if (resolved != null) {
                    break;
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        } while (c != null);
        if (resolved == null) {
            Error error = new Error("Method not found : " + m + " in class " + receiverClass + " and super classes.");
            logger.error(error.getMessage(), (Throwable) error);
        }
        String res = methodCache.get(resolved);
        if (res == null) {
            String[] paramTypes = classesToTypeNames(resolved.getParameterTypes());
            res = sootSignature(resolved.getDeclaringClass().getName(), getTypeName(resolved.getReturnType()), resolved.getName(), paramTypes);
            methodCache.put(resolved, res);
        }
        return res;
    }

    private static String[] classesToTypeNames(Class<?>[] clsArr) {
        String[] paramTypes = new String[clsArr.length];
        int i = 0;
        for (Class<?> type : clsArr) {
            paramTypes[i] = getTypeName(type);
            i++;
        }
        return paramTypes;
    }

    private static String getTypeName(Class<?> type) {
        if (type.isArray()) {
            Class<?> cl = type;
            int dimensions = 0;
            while (cl.isArray()) {
                try {
                    dimensions++;
                    cl = cl.getComponentType();
                } catch (Throwable th) {
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(cl.getName());
            for (int i = 0; i < dimensions; i++) {
                sb.append("[]");
            }
            return sb.toString();
        }
        return type.getName();
    }

    private static String sootSignature(String declaringClass, String returnType, String name, String... paramTypes) {
        StringBuilder b = new StringBuilder();
        b.append('<');
        b.append(declaringClass);
        b.append(": ");
        b.append(returnType);
        b.append(' ');
        b.append(name);
        b.append('(');
        int i = 0;
        for (String type : paramTypes) {
            i++;
            b.append(type);
            if (i < paramTypes.length) {
                b.append(',');
            }
        }
        b.append(")>");
        return b.toString();
    }

    public static String sootSignature(Field f) {
        return '<' + getTypeName(f.getDeclaringClass()) + ": " + getTypeName(f.getType()) + ' ' + f.getName() + '>';
    }
}
