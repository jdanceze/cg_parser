package soot.jimple.infoflow.android.manifest.containers;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.jimple.infoflow.android.manifest.IAndroidComponent;
import soot.jimple.infoflow.android.manifest.IComponentContainer;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/containers/EmptyComponentContainer.class */
public class EmptyComponentContainer<E extends IAndroidComponent> implements IComponentContainer<E> {
    private static final EmptyComponentContainer<?> INSTANCE = new EmptyComponentContainer<>();

    private EmptyComponentContainer() {
    }

    public static <E extends IAndroidComponent> EmptyComponentContainer<E> get() {
        return (EmptyComponentContainer<E>) INSTANCE;
    }

    @Override // soot.jimple.infoflow.android.manifest.IComponentContainer
    public List<E> asList() {
        return Collections.emptyList();
    }

    @Override // java.lang.Iterable
    public Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    @Override // soot.jimple.infoflow.android.manifest.IComponentContainer
    public E getComponentByName(String name) {
        return null;
    }

    @Override // soot.jimple.infoflow.android.manifest.IComponentContainer
    public boolean isEmpty() {
        return true;
    }
}
