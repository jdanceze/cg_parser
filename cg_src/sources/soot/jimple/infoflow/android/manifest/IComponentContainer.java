package soot.jimple.infoflow.android.manifest;

import java.util.List;
import soot.jimple.infoflow.android.manifest.IAndroidComponent;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/IComponentContainer.class */
public interface IComponentContainer<E extends IAndroidComponent> extends Iterable<E> {
    List<E> asList();

    E getComponentByName(String str);

    boolean isEmpty();

    default boolean hasComponentByName(String name) {
        return (name == null || getComponentByName(name) == null) ? false : true;
    }
}
