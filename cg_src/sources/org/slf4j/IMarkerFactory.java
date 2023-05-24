package org.slf4j;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:slf4j-api-1.7.5.jar:org/slf4j/IMarkerFactory.class
 */
/* loaded from: gencallgraphv3.jar:slf4j-api-2.0.3.jar:org/slf4j/IMarkerFactory.class */
public interface IMarkerFactory {
    Marker getMarker(String str);

    boolean exists(String str);

    boolean detachMarker(String str);

    Marker getDetachedMarker(String str);
}
