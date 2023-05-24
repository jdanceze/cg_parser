package soot.toDex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.writer.io.FileDataStore;
import org.jf.dexlib2.writer.pool.DexPool;
/* loaded from: gencallgraphv3.jar:soot/toDex/MultiDexBuilder.class */
public class MultiDexBuilder {
    protected final Opcodes opcodes;
    protected final List<DexPool> dexPools = new LinkedList();
    protected DexPool curPool;

    public MultiDexBuilder(Opcodes opcodes) {
        this.opcodes = opcodes;
        newDexPool();
    }

    protected void newDexPool() {
        this.curPool = new DexPool(this.opcodes);
        this.dexPools.add(this.curPool);
    }

    public void internClass(ClassDef clz) {
        this.curPool.mark();
        this.curPool.internClass(clz);
        if (hasOverflowed()) {
            this.curPool.reset();
            newDexPool();
            this.curPool.internClass(clz);
            if (this.curPool.hasOverflowed()) {
                throw new RuntimeException("Class is bigger than a single dex file can be");
            }
        }
    }

    protected boolean hasOverflowed() {
        if (!this.curPool.hasOverflowed()) {
            return false;
        }
        if (!this.opcodes.isArt()) {
            throw new RuntimeException("Dex file overflow. Splitting not support for pre Lollipop Android (Api 22).");
        }
        return true;
    }

    public List<File> writeTo(String folder) throws IOException {
        List<File> result = new ArrayList<>(this.dexPools.size());
        for (DexPool dexPool : this.dexPools) {
            int count = result.size();
            File file = new File(folder, "classes" + (count == 0 ? "" : Integer.valueOf(count + 1)) + ".dex");
            result.add(file);
            FileDataStore fds = new FileDataStore(file);
            dexPool.writeTo(fds);
            fds.close();
        }
        return result;
    }
}
