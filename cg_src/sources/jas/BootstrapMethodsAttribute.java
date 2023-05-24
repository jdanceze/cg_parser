package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/BootstrapMethodsAttribute.class */
public class BootstrapMethodsAttribute {
    static CP attr = new AsciiCP("BootstrapMethods");
    short attr_length = 0;
    short num = 0;
    ArrayList<Pair<MethodHandleCP, CP[]>> list = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public int addEntry(MethodHandleCP bsm, CP[] argCPs) {
        int i = 0;
        Iterator<Pair<MethodHandleCP, CP[]>> it = this.list.iterator();
        while (it.hasNext()) {
            Pair<MethodHandleCP, CP[]> pair = it.next();
            MethodHandleCP mh = pair.getO1();
            CP[] args = pair.getO2();
            if (mh.uniq.equals(bsm.uniq)) {
                boolean equal = true;
                int j = 0;
                while (true) {
                    if (j >= args.length) {
                        break;
                    }
                    CP arg = args[j];
                    CP otherArg = argCPs[j];
                    if (arg.uniq.equals(otherArg.uniq)) {
                        j++;
                    } else {
                        equal = false;
                        break;
                    }
                }
                if (equal) {
                    return i;
                }
            }
            i++;
        }
        this.list.add(new Pair<>(bsm, argCPs));
        return this.list.size() - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resolve(ClassEnv e) {
        e.addCPItem(attr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
        out.writeShort(e.getCPIndex(attr));
        out.writeInt(size());
        out.writeShort(this.list.size());
        Iterator<Pair<MethodHandleCP, CP[]>> it = this.list.iterator();
        while (it.hasNext()) {
            Pair<MethodHandleCP, CP[]> pair = it.next();
            out.writeShort(e.getCPIndex(pair.getO1()));
            CP[] cps = pair.getO2();
            out.writeShort(cps.length);
            for (CP cp : cps) {
                out.writeShort(e.getCPIndex(cp));
            }
        }
    }

    int size() {
        int size = 2;
        Iterator<Pair<MethodHandleCP, CP[]>> it = this.list.iterator();
        while (it.hasNext()) {
            Pair<MethodHandleCP, CP[]> pair = it.next();
            size = size + 4 + (pair.getO2().length * 2);
        }
        return size;
    }
}
