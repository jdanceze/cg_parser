package pxb.android.arsc;

import java.util.ArrayList;
import java.util.List;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/Type.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/Type.class */
public class Type {
    public List<Config> configs = new ArrayList();
    public int id;
    public String name;
    public ResSpec[] specs;
    int wPosition;

    public void addConfig(Config config) {
        if (config.entryCount != this.specs.length) {
            throw new RuntimeException();
        }
        this.configs.add(config);
    }

    public ResSpec getSpec(int resId) {
        ResSpec res = this.specs[resId];
        if (res == null) {
            res = new ResSpec(resId);
            this.specs[resId] = res;
        }
        return res;
    }
}
