package soot;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/ScenePack.class */
public class ScenePack extends Pack {
    public ScenePack(String name) {
        super(name);
    }

    @Override // soot.Pack
    protected void internalApply() {
        Iterator<Transform> it = iterator();
        while (it.hasNext()) {
            Transform t = it.next();
            t.apply();
        }
    }
}
