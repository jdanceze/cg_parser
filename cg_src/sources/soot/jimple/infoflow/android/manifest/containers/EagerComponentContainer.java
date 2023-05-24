package soot.jimple.infoflow.android.manifest.containers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.jimple.infoflow.android.manifest.IAndroidComponent;
import soot.jimple.infoflow.android.manifest.IComponentContainer;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/containers/EagerComponentContainer.class */
public class EagerComponentContainer<E extends IAndroidComponent> implements IComponentContainer<E> {
    private final Map<String, E> innerCollection;

    public EagerComponentContainer(Collection<E> innerCollection) {
        this.innerCollection = new HashMap(innerCollection.size());
        for (E e : innerCollection) {
            this.innerCollection.put(e.getNameString(), e);
        }
    }

    @Override // soot.jimple.infoflow.android.manifest.IComponentContainer
    public List<E> asList() {
        return new ArrayList(this.innerCollection.values());
    }

    @Override // java.lang.Iterable
    public Iterator<E> iterator() {
        return this.innerCollection.values().iterator();
    }

    @Override // soot.jimple.infoflow.android.manifest.IComponentContainer
    public E getComponentByName(String name) {
        return this.innerCollection.get(name);
    }

    @Override // soot.jimple.infoflow.android.manifest.IComponentContainer
    public boolean isEmpty() {
        return this.innerCollection.isEmpty();
    }
}
