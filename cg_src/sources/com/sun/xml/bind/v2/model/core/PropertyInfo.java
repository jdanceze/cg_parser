package com.sun.xml.bind.v2.model.core;

import com.sun.istack.Nullable;
import com.sun.xml.bind.v2.model.annotation.AnnotationSource;
import java.util.Collection;
import javax.activation.MimeType;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/core/PropertyInfo.class */
public interface PropertyInfo<T, C> extends AnnotationSource {
    TypeInfo<T, C> parent();

    String getName();

    String displayName();

    boolean isCollection();

    Collection<? extends TypeInfo<T, C>> ref();

    PropertyKind kind();

    Adapter<T, C> getAdapter();

    ID id();

    MimeType getExpectedMimeType();

    boolean inlineBinaryData();

    @Nullable
    QName getSchemaType();
}
