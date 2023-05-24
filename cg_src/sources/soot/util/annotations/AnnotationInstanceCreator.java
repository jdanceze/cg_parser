package soot.util.annotations;

import com.google.common.reflect.AbstractInvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.util.annotations.AnnotationElemSwitch;
/* loaded from: gencallgraphv3.jar:soot/util/annotations/AnnotationInstanceCreator.class */
public class AnnotationInstanceCreator {
    public Object create(AnnotationTag tag) {
        ClassLoader cl = getClass().getClassLoader();
        try {
            final Class<?> clazz = ClassLoaderUtils.loadClass(tag.getType().replace('/', '.'));
            final Map<String, Object> map = new HashMap<>();
            for (AnnotationElem elem : tag.getElems()) {
                AnnotationElemSwitch sw = new AnnotationElemSwitch();
                elem.apply(sw);
                AnnotationElemSwitch.AnnotationElemResult<Object> result = sw.getResult();
                map.put(result.getKey(), result.getValue());
            }
            return Proxy.newProxyInstance(cl, new Class[]{clazz}, new AbstractInvocationHandler() { // from class: soot.util.annotations.AnnotationInstanceCreator.1
                @Override // com.google.common.reflect.AbstractInvocationHandler
                protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                    String name = method.getName();
                    Class<?> retType = method.getReturnType();
                    if (name.equals("annotationType")) {
                        return clazz;
                    }
                    Object result2 = map.get(name);
                    if (result2 != null) {
                        if (result2 instanceof Object[]) {
                            Object[] oa = (Object[]) result2;
                            return Arrays.copyOf(oa, oa.length, retType);
                        } else if ((retType.equals(Boolean.TYPE) || retType.equals(Boolean.class)) && (result2 instanceof Integer)) {
                            return ((Integer) result2).intValue() != 0;
                        } else {
                            return result2;
                        }
                    }
                    Object result3 = method.getDefaultValue();
                    if (result3 != null) {
                        return result3;
                    }
                    throw new RuntimeException("No value for " + name + " declared in the annotation " + clazz);
                }
            });
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class: " + tag.getType());
        }
    }
}
