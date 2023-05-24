package net.bytebuddy.utility;

import android.view.InputDevice;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import soot.PolymorphicMethodRef;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/JavaType.class */
public enum JavaType {
    CONSTABLE("java.lang.constant.Constable", 1537, TypeDescription.UNDEFINED, new TypeDefinition[0]),
    TYPE_DESCRIPTOR("java.lang.invoke.TypeDescriptor", 1537, TypeDescription.UNDEFINED, new TypeDefinition[0]),
    TYPE_DESCRIPTOR_OF_FIELD("java.lang.invoke.TypeDescriptor$OfField", 1537, TypeDescription.UNDEFINED, TYPE_DESCRIPTOR.getTypeStub()),
    TYPE_DESCRIPTOR_OF_METHOD("java.lang.invoke.TypeDescriptor$OfMethod", 1537, TypeDescription.UNDEFINED, TYPE_DESCRIPTOR.getTypeStub()),
    CONSTANT_DESCRIPTION("java.lang.constant.ConstantDesc", 1537, TypeDescription.UNDEFINED, new TypeDefinition[0]),
    DYNAMIC_CONSTANT_DESCRIPTION("java.lang.constant.DynamicConstantDesc", (int) InputDevice.SOURCE_GAMEPAD, TypeDescription.OBJECT, CONSTANT_DESCRIPTION.getTypeStub()),
    CLASS_DESCRIPTION("java.lang.constant.ClassDesc", 1537, TypeDescription.UNDEFINED, CONSTANT_DESCRIPTION.getTypeStub(), TYPE_DESCRIPTOR_OF_FIELD.getTypeStub()),
    METHOD_TYPE_DESCRIPTION("java.lang.constant.MethodTypeDesc", 1537, TypeDescription.UNDEFINED, CONSTANT_DESCRIPTION.getTypeStub(), TYPE_DESCRIPTOR_OF_METHOD.getTypeStub()),
    METHOD_HANDLE_DESCRIPTION("java.lang.constant.MethodHandleDesc", 1537, TypeDescription.UNDEFINED, CONSTANT_DESCRIPTION.getTypeStub()),
    DIRECT_METHOD_HANDLE_DESCRIPTION("java.lang.constant.DirectMethodHandleDesc", 1537, TypeDescription.UNDEFINED, METHOD_HANDLE_DESCRIPTION.getTypeStub()),
    METHOD_HANDLE(PolymorphicMethodRef.METHODHANDLE_SIGNATURE, (int) InputDevice.SOURCE_GAMEPAD, TypeDescription.OBJECT, CONSTABLE.getTypeStub()),
    METHOD_HANDLES("java.lang.invoke.MethodHandles", 1, Object.class, new Type[0]),
    METHOD_TYPE("java.lang.invoke.MethodType", 17, TypeDescription.OBJECT, CONSTABLE.getTypeStub(), TYPE_DESCRIPTOR_OF_METHOD.getTypeStub(), TypeDescription.ForLoadedType.of(Serializable.class)),
    METHOD_HANDLES_LOOKUP("java.lang.invoke.MethodHandles$Lookup", 25, Object.class, new Type[0]),
    CALL_SITE("java.lang.invoke.CallSite", (int) InputDevice.SOURCE_GAMEPAD, Object.class, new Type[0]),
    VAR_HANDLE(PolymorphicMethodRef.VARHANDLE_SIGNATURE, (int) InputDevice.SOURCE_GAMEPAD, TypeDescription.Generic.OBJECT, CONSTABLE.getTypeStub()),
    PARAMETER("java.lang.reflect.Parameter", 17, Object.class, AnnotatedElement.class),
    EXECUTABLE("java.lang.reflect.Executable", (int) InputDevice.SOURCE_GAMEPAD, AccessibleObject.class, Member.class, GenericDeclaration.class),
    MODULE("java.lang.Module", 17, Object.class, AnnotatedElement.class),
    RECORD("java.lang.Record", (int) InputDevice.SOURCE_GAMEPAD, Object.class, new Type[0]),
    OBJECT_METHODS("java.lang.runtime.ObjectMethods", 1, Object.class, new Type[0]);
    
    private final TypeDescription typeDescription;
    private transient /* synthetic */ Class load_iBAk0M1j;
    private transient /* synthetic */ boolean isAvailable_2WZLFA7h;

    JavaType(String typeName, int modifiers, Type superClass, Type... anInterface) {
        this(typeName, modifiers, superClass == null ? TypeDescription.Generic.UNDEFINED : TypeDefinition.Sort.describe(superClass), new TypeList.Generic.ForLoadedTypes(anInterface));
    }

    JavaType(String typeName, int modifiers, TypeDefinition superClass, TypeDefinition... anInterface) {
        this(typeName, modifiers, superClass == null ? TypeDescription.Generic.UNDEFINED : superClass.asGenericType(), new TypeList.Generic.Explicit(anInterface));
    }

    JavaType(String typeName, int modifiers, TypeDescription.Generic superClass, TypeList.Generic interfaces) {
        this.typeDescription = new TypeDescription.Latent(typeName, modifiers, superClass, interfaces);
    }

    public TypeDescription getTypeStub() {
        return this.typeDescription;
    }

    @CachedReturnPlugin.Enhance
    public Class<?> load() throws ClassNotFoundException {
        Class<?> cls = this.load_iBAk0M1j != null ? null : Class.forName(this.typeDescription.getName(), false, ClassLoadingStrategy.BOOTSTRAP_LOADER);
        if (cls == null) {
            cls = this.load_iBAk0M1j;
        } else {
            this.load_iBAk0M1j = cls;
        }
        return cls;
    }

    public TypeDescription loadAsDescription() throws ClassNotFoundException {
        return TypeDescription.ForLoadedType.of(load());
    }

    /* JADX WARN: Type inference failed for: r0v15 */
    /* JADX WARN: Type inference failed for: r0v3, types: [net.bytebuddy.utility.JavaType] */
    /* JADX WARN: Type inference failed for: r0v4 */
    @CachedReturnPlugin.Enhance
    public boolean isAvailable() {
        boolean z;
        if (this.isAvailable_2WZLFA7h) {
            z = false;
        } else {
            ?? r0 = this;
            try {
                r0.load();
                z = true;
                r0 = 1;
            } catch (ClassNotFoundException e) {
                z = r0;
            }
        }
        boolean z2 = z;
        if (z2) {
            this.isAvailable_2WZLFA7h = true;
        } else {
            z2 = true;
        }
        return z2;
    }

    public boolean isInstance(Object instance) {
        if (!isAvailable()) {
            return false;
        }
        try {
            return load().isInstance(instance);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
