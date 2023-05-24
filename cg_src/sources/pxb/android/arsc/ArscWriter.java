package pxb.android.arsc;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import pxb.android.ResConst;
import pxb.android.StringItem;
import pxb.android.StringItems;
import pxb.android.axml.Util;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/ArscWriter.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/ArscWriter.class */
public class ArscWriter implements ResConst {
    private List<Pkg> pkgs;
    private List<PkgCtx> ctxs = new ArrayList(5);
    private Map<String, StringItem> strTable = new TreeMap();
    private StringItems strTable0 = new StringItems();

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/ArscWriter$PkgCtx.class
     */
    /* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/ArscWriter$PkgCtx.class */
    public static class PkgCtx {
        Map<String, StringItem> keyNames;
        StringItems keyNames0;
        public int keyStringOff;
        int offset;
        Pkg pkg;
        int pkgSize;
        List<StringItem> typeNames;
        StringItems typeNames0;
        int typeStringOff;

        private PkgCtx() {
            this.keyNames = new HashMap();
            this.keyNames0 = new StringItems();
            this.typeNames = new ArrayList();
            this.typeNames0 = new StringItems();
        }

        public void addKeyName(String name) {
            if (this.keyNames.containsKey(name)) {
                return;
            }
            StringItem stringItem = new StringItem(name);
            this.keyNames.put(name, stringItem);
            this.keyNames0.add(stringItem);
        }

        public void addTypeName(int id, String name) {
            while (this.typeNames.size() <= id) {
                this.typeNames.add(null);
            }
            StringItem item = this.typeNames.get(id);
            if (item == null) {
                this.typeNames.set(id, new StringItem(name));
                return;
            }
            throw new RuntimeException();
        }
    }

    private static void D(String fmt, Object... args) {
    }

    public ArscWriter(List<Pkg> pkgs) {
        this.pkgs = pkgs;
    }

    public static void main(String... args) throws IOException {
        if (args.length < 2) {
            System.err.println("asrc-write-test in.arsc out.arsc");
            return;
        }
        byte[] data = Util.readFile(new File(args[0]));
        List<Pkg> pkgs = new ArscParser(data).parse();
        byte[] data2 = new ArscWriter(pkgs).toByteArray();
        Util.writeFile(data2, new File(args[1]));
    }

    private void addString(String str) {
        if (this.strTable.containsKey(str)) {
            return;
        }
        StringItem stringItem = new StringItem(str);
        this.strTable.put(str, stringItem);
        this.strTable0.add(stringItem);
    }

    private int count() {
        int size = 0 + 12;
        int stringSize = this.strTable0.getSize();
        if (stringSize % 4 != 0) {
            stringSize += 4 - (stringSize % 4);
        }
        int size2 = size + 8 + stringSize;
        for (PkgCtx ctx : this.ctxs) {
            ctx.offset = size2;
            int pkgSize = 0 + 268 + 16;
            ctx.typeStringOff = pkgSize;
            int stringSize2 = ctx.typeNames0.getSize();
            if (stringSize2 % 4 != 0) {
                stringSize2 += 4 - (stringSize2 % 4);
            }
            int pkgSize2 = pkgSize + 8 + stringSize2;
            ctx.keyStringOff = pkgSize2;
            int stringSize3 = ctx.keyNames0.getSize();
            if (stringSize3 % 4 != 0) {
                stringSize3 += 4 - (stringSize3 % 4);
            }
            int pkgSize3 = pkgSize2 + 8 + stringSize3;
            for (Type type : ctx.pkg.types.values()) {
                type.wPosition = size2 + pkgSize3;
                pkgSize3 += 16 + (4 * type.specs.length);
                for (Config config : type.configs) {
                    config.wPosition = pkgSize3 + size2;
                    int configBasePostion = pkgSize3;
                    int pkgSize4 = pkgSize3 + 20;
                    int size0 = config.id.length;
                    if (size0 % 4 != 0) {
                        size0 += 4 - (size0 % 4);
                    }
                    if ((pkgSize4 + size0) - configBasePostion > 56) {
                        throw new RuntimeException("config id  too big");
                    }
                    pkgSize3 = configBasePostion + 56 + (4 * config.entryCount);
                    config.wEntryStart = pkgSize3 - configBasePostion;
                    for (ResEntry e : config.resources.values()) {
                        e.wOffset = pkgSize3 - pkgSize3;
                        int pkgSize5 = pkgSize3 + 8;
                        if (e.value instanceof BagValue) {
                            BagValue big = (BagValue) e.value;
                            pkgSize3 = pkgSize5 + 8 + (big.map.size() * 12);
                        } else {
                            pkgSize3 = pkgSize5 + 8;
                        }
                    }
                    config.wChunkSize = pkgSize3 - configBasePostion;
                }
            }
            ctx.pkgSize = pkgSize3;
            size2 += pkgSize3;
        }
        return size2;
    }

    private List<PkgCtx> prepare() throws IOException {
        ResSpec[] resSpecArr;
        for (Pkg pkg : this.pkgs) {
            PkgCtx ctx = new PkgCtx();
            ctx.pkg = pkg;
            this.ctxs.add(ctx);
            for (Type type : pkg.types.values()) {
                ctx.addTypeName(type.id - 1, type.name);
                for (ResSpec spec : type.specs) {
                    ctx.addKeyName(spec.name);
                }
                for (Config config : type.configs) {
                    for (ResEntry e : config.resources.values()) {
                        Object object = e.value;
                        if (object instanceof BagValue) {
                            travelBagValue((BagValue) object);
                        } else {
                            travelValue((Value) object);
                        }
                    }
                }
            }
            ctx.keyNames0.prepare();
            ctx.typeNames0.addAll(ctx.typeNames);
            ctx.typeNames0.prepare();
        }
        this.strTable0.prepare();
        return this.ctxs;
    }

    public byte[] toByteArray() throws IOException {
        prepare();
        int size = count();
        ByteBuffer out = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);
        write(out, size);
        return out.array();
    }

    private void travelBagValue(BagValue bag) {
        for (Map.Entry<Integer, Value> e : bag.map) {
            travelValue(e.getValue());
        }
    }

    private void travelValue(Value v) {
        if (v.raw != null) {
            addString(v.raw);
        }
    }

    private void write(ByteBuffer out, int size) throws IOException {
        ResSpec[] resSpecArr;
        int flag;
        out.putInt(786434);
        out.putInt(size);
        out.putInt(this.ctxs.size());
        int stringSize = this.strTable0.getSize();
        int padding = 0;
        if (stringSize % 4 != 0) {
            padding = 4 - (stringSize % 4);
        }
        out.putInt(1835009);
        out.putInt(stringSize + padding + 8);
        this.strTable0.write(out);
        out.put(new byte[padding]);
        for (PkgCtx pctx : this.ctxs) {
            if (out.position() != pctx.offset) {
                throw new RuntimeException();
            }
            int basePosition = out.position();
            out.putInt(18612736);
            out.putInt(pctx.pkgSize);
            out.putInt(pctx.pkg.id);
            int p = out.position();
            out.put(pctx.pkg.name.getBytes("UTF-16LE"));
            out.position(p + 256);
            out.putInt(pctx.typeStringOff);
            out.putInt(pctx.typeNames0.size());
            out.putInt(pctx.keyStringOff);
            out.putInt(pctx.keyNames0.size());
            if (out.position() - basePosition != pctx.typeStringOff) {
                throw new RuntimeException();
            }
            int stringSize2 = pctx.typeNames0.getSize();
            int padding2 = 0;
            if (stringSize2 % 4 != 0) {
                padding2 = 4 - (stringSize2 % 4);
            }
            out.putInt(1835009);
            out.putInt(stringSize2 + padding2 + 8);
            pctx.typeNames0.write(out);
            out.put(new byte[padding2]);
            if (out.position() - basePosition != pctx.keyStringOff) {
                throw new RuntimeException();
            }
            int stringSize3 = pctx.keyNames0.getSize();
            int padding3 = 0;
            if (stringSize3 % 4 != 0) {
                padding3 = 4 - (stringSize3 % 4);
            }
            out.putInt(1835009);
            out.putInt(stringSize3 + padding3 + 8);
            pctx.keyNames0.write(out);
            out.put(new byte[padding3]);
            for (Type t : pctx.pkg.types.values()) {
                D("[%08x]write spec", Integer.valueOf(out.position()), t.name);
                if (t.wPosition != out.position()) {
                    throw new RuntimeException();
                }
                out.putInt(1049090);
                out.putInt(16 + (4 * t.specs.length));
                out.putInt(t.id);
                out.putInt(t.specs.length);
                for (ResSpec spec : t.specs) {
                    out.putInt(spec.flags);
                }
                for (Config config : t.configs) {
                    D("[%08x]write config", Integer.valueOf(out.position()));
                    int typeConfigPosition = out.position();
                    if (config.wPosition != typeConfigPosition) {
                        throw new RuntimeException();
                    }
                    out.putInt(3670529);
                    out.putInt(config.wChunkSize);
                    out.putInt(t.id);
                    out.putInt(t.specs.length);
                    out.putInt(config.wEntryStart);
                    D("[%08x]write config ids", Integer.valueOf(out.position()));
                    out.put(config.id);
                    int size0 = config.id.length;
                    int padding4 = 0;
                    if (size0 % 4 != 0) {
                        padding4 = 4 - (size0 % 4);
                    }
                    out.put(new byte[padding4]);
                    out.position(typeConfigPosition + 56);
                    D("[%08x]write config entry offsets", Integer.valueOf(out.position()));
                    for (int i = 0; i < config.entryCount; i++) {
                        ResEntry entry = config.resources.get(Integer.valueOf(i));
                        if (entry == null) {
                            out.putInt(-1);
                        } else {
                            out.putInt(entry.wOffset);
                        }
                    }
                    if (out.position() - typeConfigPosition != config.wEntryStart) {
                        throw new RuntimeException();
                    }
                    D("[%08x]write config entrys", Integer.valueOf(out.position()));
                    for (ResEntry e : config.resources.values()) {
                        D("[%08x]ResTable_entry", Integer.valueOf(out.position()));
                        boolean isBag = e.value instanceof BagValue;
                        out.putShort((short) (isBag ? 16 : 8));
                        int flag2 = e.flag;
                        if (isBag) {
                            flag = flag2 | 1;
                        } else {
                            flag = flag2 & (-2);
                        }
                        out.putShort((short) flag);
                        out.putInt(pctx.keyNames.get(e.spec.name).index);
                        if (isBag) {
                            BagValue bag = (BagValue) e.value;
                            out.putInt(bag.parent);
                            out.putInt(bag.map.size());
                            for (Map.Entry<Integer, Value> entry2 : bag.map) {
                                out.putInt(entry2.getKey().intValue());
                                writeValue(entry2.getValue(), out);
                            }
                        } else {
                            writeValue((Value) e.value, out);
                        }
                    }
                }
            }
        }
    }

    private void writeValue(Value value, ByteBuffer out) {
        out.putShort((short) 8);
        out.put((byte) 0);
        out.put((byte) value.type);
        if (value.type == 3) {
            out.putInt(this.strTable.get(value.raw).index);
        } else {
            out.putInt(value.data);
        }
    }
}
