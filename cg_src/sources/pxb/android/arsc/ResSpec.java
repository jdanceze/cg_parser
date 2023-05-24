package pxb.android.arsc;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/ResSpec.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/ResSpec.class */
public class ResSpec {
    public int flags;
    public final int id;
    public String name;

    public ResSpec(int id) {
        this.id = id;
    }

    public void updateName(String s) {
        String name = this.name;
        if (name == null) {
            this.name = s;
        } else if (!s.equals(name)) {
            throw new RuntimeException();
        }
    }
}
