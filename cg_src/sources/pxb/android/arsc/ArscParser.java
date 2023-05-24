package pxb.android.arsc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxb.android.ResConst;
import pxb.android.StringItems;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/ArscParser.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/ArscParser.class */
public class ArscParser implements ResConst {
    private static final Logger logger = LoggerFactory.getLogger(ArscParser.class);
    static final int ENGRY_FLAG_PUBLIC = 2;
    static final short ENTRY_FLAG_COMPLEX = 1;
    public static final int TYPE_STRING = 3;
    private ByteBuffer in;
    private String[] keyNamesX;
    private Pkg pkg;
    private String[] strings;
    private String[] typeNamesX;
    private int fileSize = -1;
    private List<Pkg> pkgs = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/ArscParser$Chunk.class
     */
    /* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/ArscParser$Chunk.class */
    public class Chunk {
        public final int headSize;
        public final int location;
        public final int size;
        public final int type;

        public Chunk() {
            this.location = ArscParser.this.in.position();
            this.type = ArscParser.this.in.getShort() & 65535;
            this.headSize = ArscParser.this.in.getShort() & 65535;
            this.size = ArscParser.this.in.getInt();
            ArscParser.D("[%08x]type: %04x, headsize: %04x, size:%08x", Integer.valueOf(this.location), Integer.valueOf(this.type), Integer.valueOf(this.headSize), Integer.valueOf(this.size));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void D(String fmt, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format(fmt, args));
        }
    }

    public ArscParser(byte[] b) {
        this.in = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
    }

    public List<Pkg> parse() throws IOException {
        if (this.fileSize < 0) {
            Chunk head = new Chunk();
            if (head.type != 2) {
                throw new RuntimeException();
            }
            this.fileSize = head.size;
            this.in.getInt();
        }
        while (this.in.hasRemaining()) {
            Chunk chunk = new Chunk();
            switch (chunk.type) {
                case 1:
                    this.strings = StringItems.read(this.in);
                    if (logger.isTraceEnabled()) {
                        for (int i = 0; i < this.strings.length; i++) {
                            D("STR [%08x] %s", Integer.valueOf(i), this.strings[i]);
                        }
                        break;
                    } else {
                        break;
                    }
                case 512:
                    readPackage(this.in);
                    break;
            }
            this.in.position(chunk.location + chunk.size);
        }
        return this.pkgs;
    }

    private void readEntry(Config config, ResSpec spec) {
        D("[%08x]read ResTable_entry", Integer.valueOf(this.in.position()));
        int size = this.in.getShort();
        D("ResTable_entry %d", Integer.valueOf(size));
        int flags = this.in.getShort();
        int keyStr = this.in.getInt();
        spec.updateName(this.keyNamesX[keyStr]);
        ResEntry resEntry = new ResEntry(flags, spec);
        if (0 != (flags & 1)) {
            int parent = this.in.getInt();
            int count = this.in.getInt();
            BagValue bag = new BagValue(parent);
            for (int i = 0; i < count; i++) {
                Map.Entry<Integer, Value> entry = new AbstractMap.SimpleEntry<>(Integer.valueOf(this.in.getInt()), readValue());
                bag.map.add(entry);
            }
            resEntry.value = bag;
        } else {
            resEntry.value = readValue();
        }
        config.resources.put(Integer.valueOf(spec.id), resEntry);
    }

    private void readPackage(ByteBuffer in) throws IOException {
        int s;
        int pid = in.getInt() % 255;
        int nextPisition = in.position() + 256;
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 128 && (s = in.getShort()) != 0; i++) {
            sb.append((char) s);
        }
        String name = sb.toString();
        in.position(nextPisition);
        this.pkg = new Pkg(pid, name);
        this.pkgs.add(this.pkg);
        in.getInt();
        in.getInt();
        in.getInt();
        in.getInt();
        Chunk chunk = new Chunk();
        if (chunk.type != 1) {
            throw new RuntimeException();
        }
        this.typeNamesX = StringItems.read(in);
        in.position(chunk.location + chunk.size);
        Chunk chunk2 = new Chunk();
        if (chunk2.type != 1) {
            throw new RuntimeException();
        }
        this.keyNamesX = StringItems.read(in);
        if (logger.isTraceEnabled()) {
            for (int i2 = 0; i2 < this.keyNamesX.length; i2++) {
                D("STR [%08x] %s", Integer.valueOf(i2), this.keyNamesX[i2]);
            }
        }
        in.position(chunk2.location + chunk2.size);
        while (in.hasRemaining()) {
            Chunk chunk3 = new Chunk();
            switch (chunk3.type) {
                case 513:
                    D("[%08x]read config", Integer.valueOf(in.position() - 8));
                    int tid = in.get() & 255;
                    in.get();
                    in.getShort();
                    int entryCount = in.getInt();
                    Type t = this.pkg.getType(tid, this.typeNamesX[tid - 1], entryCount);
                    int entriesStart = in.getInt();
                    D("[%08x]read config id", Integer.valueOf(in.position()));
                    int p = in.position();
                    int size = in.getInt();
                    byte[] data = new byte[size];
                    in.position(p);
                    in.get(data);
                    Config config = new Config(data, entryCount);
                    in.position(chunk3.location + chunk3.headSize);
                    D("[%08x]read config entry offset", Integer.valueOf(in.position()));
                    int[] entrys = new int[entryCount];
                    for (int i3 = 0; i3 < entryCount; i3++) {
                        entrys[i3] = in.getInt();
                    }
                    D("[%08x]read config entrys", Integer.valueOf(in.position()));
                    for (int i4 = 0; i4 < entrys.length; i4++) {
                        if (entrys[i4] != -1) {
                            in.position(chunk3.location + entriesStart + entrys[i4]);
                            ResSpec spec = t.getSpec(i4);
                            readEntry(config, spec);
                        }
                    }
                    t.addConfig(config);
                    break;
                case 514:
                    D("[%08x]read spec", Integer.valueOf(in.position() - 8));
                    int tid2 = in.get() & 255;
                    in.get();
                    in.getShort();
                    int entryCount2 = in.getInt();
                    Type t2 = this.pkg.getType(tid2, this.typeNamesX[tid2 - 1], entryCount2);
                    for (int i5 = 0; i5 < entryCount2; i5++) {
                        t2.getSpec(i5).flags = in.getInt();
                    }
                    break;
                default:
                    return;
            }
            in.position(chunk3.location + chunk3.size);
        }
    }

    private Object readValue() {
        this.in.getShort();
        this.in.get();
        int type = this.in.get() & 255;
        int data = this.in.getInt();
        String raw = null;
        if (type == 3) {
            raw = this.strings[data];
        }
        return new Value(type, data, raw);
    }
}
