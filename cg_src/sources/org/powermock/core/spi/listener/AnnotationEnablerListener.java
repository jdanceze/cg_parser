package org.powermock.core.spi.listener;

import java.lang.annotation.Annotation;
import org.powermock.core.spi.PowerMockTestListener;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/listener/AnnotationEnablerListener.class */
public interface AnnotationEnablerListener extends PowerMockTestListener {
    Class<? extends Annotation>[] getMockAnnotations();
}
