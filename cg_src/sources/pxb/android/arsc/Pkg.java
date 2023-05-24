package pxb.android.arsc;

import java.util.TreeMap;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/Pkg.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/Pkg.class */
public class Pkg {
    public final int id;
    public String name;
    public TreeMap<Integer, Type> types = new TreeMap<>();

    public Pkg(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Type getType(int tid, String name, int entrySize) {
        Type type = this.types.get(Integer.valueOf(tid));
        if (type != null) {
            if (name != null) {
                if (type.name == null) {
                    type.name = name;
                } else if (!name.endsWith(type.name)) {
                    throw new RuntimeException();
                }
                if (type.specs.length != entrySize) {
                    throw new RuntimeException();
                }
            }
        } else {
            type = new Type();
            type.id = tid;
            type.name = name;
            type.specs = new ResSpec[entrySize];
            this.types.put(Integer.valueOf(tid), type);
        }
        return type;
    }
}
