package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.bind.v2.model.nav.Navigator;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/model/impl/ModelBuilderI.class */
public interface ModelBuilderI<T, C, F, M> {
    Navigator<T, C, F, M> getNavigator();

    AnnotationReader<T, C, F, M> getReader();
}
