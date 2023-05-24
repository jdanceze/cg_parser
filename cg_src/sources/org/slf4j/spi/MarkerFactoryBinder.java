package org.slf4j.spi;

import org.slf4j.IMarkerFactory;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:slf4j-api-1.7.5.jar:org/slf4j/spi/MarkerFactoryBinder.class
 */
/* loaded from: gencallgraphv3.jar:slf4j-api-2.0.3.jar:org/slf4j/spi/MarkerFactoryBinder.class */
public interface MarkerFactoryBinder {
    IMarkerFactory getMarkerFactory();

    String getMarkerFactoryClassStr();
}
