package org.powermock.tests.utils.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedList;
import java.util.List;
import org.powermock.core.MockRepository;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/StaticConstructorSuppressExtractorImpl.class */
public class StaticConstructorSuppressExtractorImpl extends AbstractTestClassExtractor {
    @Override // org.powermock.tests.utils.impl.AbstractTestClassExtractor
    public String[] getClassesToModify(AnnotatedElement element) {
        List<String> all = new LinkedList<>();
        SuppressStaticInitializationFor suppressAnnotation = (SuppressStaticInitializationFor) element.getAnnotation(SuppressStaticInitializationFor.class);
        if (suppressAnnotation == null) {
            return null;
        }
        String[] value = suppressAnnotation.value();
        for (String classToSuppress : value) {
            if (!"".equals(classToSuppress)) {
                all.add(classToSuppress);
                MockRepository.addSuppressStaticInitializer(classToSuppress);
            }
        }
        return (String[]) all.toArray(new String[all.size()]);
    }
}
