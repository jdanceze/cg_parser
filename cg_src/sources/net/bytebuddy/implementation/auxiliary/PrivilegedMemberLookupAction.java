package net.bytebuddy.implementation.auxiliary;

import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodAccessorFactory;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.CompoundList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/PrivilegedMemberLookupAction.class */
public enum PrivilegedMemberLookupAction implements AuxiliaryType {
    FOR_PUBLIC_METHOD("getMethod", "name", String.class, "parameters", Class[].class),
    FOR_DECLARED_METHOD("getDeclaredMethod", "name", String.class, "parameters", Class[].class),
    FOR_PUBLIC_CONSTRUCTOR("getConstructor", "parameters", Class[].class),
    FOR_DECLARED_CONSTRUCTOR(TypeProxy.SilentConstruction.Appender.GET_DECLARED_CONSTRUCTOR_METHOD_NAME, "parameters", Class[].class);
    
    private static final String TYPE_FIELD = "type";
    private static final MethodDescription.InDefinedShape DEFAULT_CONSTRUCTOR = (MethodDescription.InDefinedShape) TypeDescription.OBJECT.getDeclaredMethods().filter(ElementMatchers.isConstructor()).getOnly();
    private final MethodDescription.InDefinedShape methodDescription;
    private final Map<String, Class<?>> fields;

    PrivilegedMemberLookupAction(String name, String field, Class cls) {
        try {
            this.methodDescription = new MethodDescription.ForLoadedMethod(Class.class.getMethod(name, cls));
            this.fields = Collections.singletonMap(field, cls);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Could not locate method: " + name, exception);
        }
    }

    PrivilegedMemberLookupAction(String name, String firstField, Class cls, String secondField, Class cls2) {
        try {
            this.methodDescription = new MethodDescription.ForLoadedMethod(Class.class.getMethod(name, cls, cls2));
            this.fields = new LinkedHashMap();
            this.fields.put(firstField, cls);
            this.fields.put(secondField, cls2);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Could not locate method: " + name, exception);
        }
    }

    public static AuxiliaryType of(MethodDescription methodDescription) {
        if (methodDescription.isConstructor()) {
            return methodDescription.isPublic() ? FOR_PUBLIC_CONSTRUCTOR : FOR_DECLARED_CONSTRUCTOR;
        } else if (methodDescription.isMethod()) {
            return methodDescription.isPublic() ? FOR_PUBLIC_METHOD : FOR_DECLARED_METHOD;
        } else {
            throw new IllegalStateException("Cannot load constant for type initializer: " + methodDescription);
        }
    }

    @Override // net.bytebuddy.implementation.auxiliary.AuxiliaryType
    public DynamicType make(String auxiliaryTypeName, ClassFileVersion classFileVersion, MethodAccessorFactory methodAccessorFactory) {
        Implementation.Composable constructor = MethodCall.invoke(DEFAULT_CONSTRUCTOR).andThen(FieldAccessor.ofField("type").setsArgumentAt(0));
        int index = 1;
        for (String field : this.fields.keySet()) {
            int i = index;
            index++;
            constructor = constructor.andThen(FieldAccessor.ofField(field).setsArgumentAt(i));
        }
        DynamicType.Builder<?> builder = new ByteBuddy(classFileVersion).with(TypeValidation.DISABLED).subclass(PrivilegedExceptionAction.class, (ConstructorStrategy) ConstructorStrategy.Default.NO_CONSTRUCTORS).name(auxiliaryTypeName).modifiers(DEFAULT_TYPE_MODIFIER).defineConstructor(Visibility.PUBLIC).withParameters(CompoundList.of(Class.class, new ArrayList(this.fields.values()))).intercept(constructor).method(ElementMatchers.named("run")).intercept(MethodCall.invoke(this.methodDescription).onField("type").withField((String[]) this.fields.keySet().toArray(new String[0]))).defineField("type", Class.class, Visibility.PRIVATE);
        for (Map.Entry<String, Class<?>> entry : this.fields.entrySet()) {
            builder = builder.defineField(entry.getKey(), entry.getValue(), Visibility.PRIVATE);
        }
        return builder.make();
    }
}
