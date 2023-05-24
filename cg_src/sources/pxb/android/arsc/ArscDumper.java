package pxb.android.arsc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pxb.android.axml.Util;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/ArscDumper.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/ArscDumper.class */
public class ArscDumper {
    public static void dump(List<Pkg> pkgs) {
        for (int x = 0; x < pkgs.size(); x++) {
            Pkg pkg = pkgs.get(x);
            System.out.println(String.format("  Package %d id=%d name=%s typeCount=%d", Integer.valueOf(x), Integer.valueOf(pkg.id), pkg.name, Integer.valueOf(pkg.types.size())));
            for (Type type : pkg.types.values()) {
                System.out.println(String.format("    type %d %s", Integer.valueOf(type.id - 1), type.name));
                int resPrefix = (pkg.id << 24) | (type.id << 16);
                for (int i = 0; i < type.specs.length; i++) {
                    ResSpec spec = type.getSpec(i);
                    System.out.println(String.format("      spec 0x%08x 0x%08x %s", Integer.valueOf(resPrefix | spec.id), Integer.valueOf(spec.flags), spec.name));
                }
                for (int i2 = 0; i2 < type.configs.size(); i2++) {
                    Config config = type.configs.get(i2);
                    System.out.println("      config");
                    List<ResEntry> entries = new ArrayList<>(config.resources.values());
                    for (int j = 0; j < entries.size(); j++) {
                        ResEntry entry = entries.get(j);
                        System.out.println(String.format("        resource 0x%08x %-20s: %s", Integer.valueOf(resPrefix | entry.spec.id), entry.spec.name, entry.value));
                    }
                }
            }
        }
    }

    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            System.err.println("asrc-dump file.arsc");
            return;
        }
        byte[] data = Util.readFile(new File(args[0]));
        List<Pkg> pkgs = new ArscParser(data).parse();
        dump(pkgs);
    }
}
