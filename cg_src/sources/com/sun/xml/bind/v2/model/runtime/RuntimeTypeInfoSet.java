package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ArrayInfo;
import com.sun.xml.bind.v2.model.core.BuiltinLeafInfo;
import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.model.core.ElementInfo;
import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.TypeInfoSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/runtime/RuntimeTypeInfoSet.class */
public interface RuntimeTypeInfoSet extends TypeInfoSet<Type, Class, Field, Method> {
    @Override // 
    Map<? extends Type, ? extends ArrayInfo<Type, Class>> arrays();

    @Override // 
    Map<Class, ? extends ClassInfo<Type, Class>> beans();

    @Override // 
    Map<Type, ? extends BuiltinLeafInfo<Type, Class>> builtins();

    @Override // 
    Map<Class, ? extends EnumLeafInfo<Type, Class>> enums();

    RuntimeNonElement getTypeInfo(Type type);

    @Override // 
    NonElement<Type, Class> getAnyTypeInfo();

    RuntimeNonElement getClassInfo(Class cls);

    RuntimeElementInfo getElementInfo(Class cls, QName qName);

    Map<QName, ? extends RuntimeElementInfo> getElementMappings(Class cls);

    @Override // 
    Iterable<? extends ElementInfo<Type, Class>> getAllElements();
}
