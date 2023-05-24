package org.jf.dexlib2.dexbacked;

import android.widget.ExpandableListView;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.io.ByteStreams;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.dexlib2.util.DexUtil;
import org.jf.util.AbstractForwardSequentialList;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile.class */
public class OatFile extends DexBuffer implements MultiDexContainer<DexBackedDexFile> {
    private static final byte[] ELF_MAGIC = {Byte.MAX_VALUE, 69, 76, 70};
    private static final byte[] OAT_MAGIC = {111, 97, 116, 10};
    private static final int MIN_ELF_HEADER_SIZE = 52;
    private static final int MIN_OAT_VERSION = 56;
    private static final int MAX_OAT_VERSION = 178;
    public static final int UNSUPPORTED = 0;
    public static final int SUPPORTED = 1;
    public static final int UNKNOWN = 2;
    private final boolean is64bit;
    @Nonnull
    private final OatHeader oatHeader;
    @Nonnull
    private final Opcodes opcodes;
    @Nullable
    private final VdexProvider vdexProvider;

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$NotAnOatFileException.class */
    public static class NotAnOatFileException extends RuntimeException {
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$VdexProvider.class */
    public interface VdexProvider {
        @Nullable
        byte[] getVdex();
    }

    public OatFile(@Nonnull byte[] buf) {
        this(buf, null);
    }

    public OatFile(@Nonnull byte[] buf, @Nullable VdexProvider vdexProvider) {
        super(buf);
        if (buf.length < 52) {
            throw new NotAnOatFileException();
        }
        verifyMagic(buf);
        if (buf[4] == 1) {
            this.is64bit = false;
        } else if (buf[4] == 2) {
            this.is64bit = true;
        } else {
            throw new InvalidOatFileException(String.format("Invalid word-size value: %x", Byte.valueOf(buf[5])));
        }
        OatHeader oatHeader = null;
        SymbolTable symbolTable = getSymbolTable();
        Iterator<SymbolTable.Symbol> it = symbolTable.getSymbols().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            SymbolTable.Symbol symbol = it.next();
            if (symbol.getName().equals("oatdata")) {
                oatHeader = new OatHeader(symbol.getFileOffset());
                break;
            }
        }
        if (oatHeader == null) {
            throw new InvalidOatFileException("Oat file has no oatdata symbol");
        }
        this.oatHeader = oatHeader;
        if (!oatHeader.isValid()) {
            throw new InvalidOatFileException("Invalid oat magic value");
        }
        this.opcodes = Opcodes.forArtVersion(oatHeader.getVersion());
        this.vdexProvider = vdexProvider;
    }

    private static void verifyMagic(byte[] buf) {
        for (int i = 0; i < ELF_MAGIC.length; i++) {
            if (buf[i] != ELF_MAGIC[i]) {
                throw new NotAnOatFileException();
            }
        }
    }

    public static OatFile fromInputStream(@Nonnull InputStream is) throws IOException {
        return fromInputStream(is, null);
    }

    public static OatFile fromInputStream(@Nonnull InputStream is, @Nullable VdexProvider vdexProvider) throws IOException {
        if (!is.markSupported()) {
            throw new IllegalArgumentException("InputStream must support mark");
        }
        is.mark(4);
        byte[] partialHeader = new byte[4];
        try {
            try {
                ByteStreams.readFully(is, partialHeader);
                is.reset();
                verifyMagic(partialHeader);
                is.reset();
                byte[] buf = ByteStreams.toByteArray(is);
                return new OatFile(buf, vdexProvider);
            } catch (EOFException e) {
                throw new NotAnOatFileException();
            }
        } catch (Throwable th) {
            is.reset();
            throw th;
        }
    }

    public int getOatVersion() {
        return this.oatHeader.getVersion();
    }

    public int isSupportedVersion() {
        int version = getOatVersion();
        if (version < 56) {
            return 0;
        }
        if (version <= 178) {
            return 1;
        }
        return 2;
    }

    @Nonnull
    public List<String> getBootClassPath() {
        if (getOatVersion() < 75) {
            return ImmutableList.of();
        }
        String bcp = this.oatHeader.getKeyValue("bootclasspath");
        if (bcp == null) {
            return ImmutableList.of();
        }
        return Arrays.asList(bcp.split(":"));
    }

    @Nonnull
    public List<DexBackedDexFile> getDexFiles() {
        return new AbstractForwardSequentialList<DexBackedDexFile>() { // from class: org.jf.dexlib2.dexbacked.OatFile.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return Iterators.size(Iterators.filter(new DexEntryIterator(), (v0) -> {
                    return Objects.nonNull(v0);
                }));
            }

            @Override // org.jf.util.AbstractForwardSequentialList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
            @Nonnull
            public Iterator<DexBackedDexFile> iterator() {
                return Iterators.transform(Iterators.filter(new DexEntryIterator(), (v0) -> {
                    return Objects.nonNull(v0);
                }), new Function<OatDexEntry, DexBackedDexFile>() { // from class: org.jf.dexlib2.dexbacked.OatFile.1.1
                    @Override // com.google.common.base.Function
                    @Nullable
                    public DexBackedDexFile apply(OatDexEntry dexEntry) {
                        return dexEntry.getDexFile();
                    }
                });
            }
        };
    }

    @Override // org.jf.dexlib2.iface.MultiDexContainer
    @Nonnull
    public List<String> getDexEntryNames() throws IOException {
        return new AbstractForwardSequentialList<String>() { // from class: org.jf.dexlib2.dexbacked.OatFile.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return Iterators.size(Iterators.filter(new DexEntryIterator(), (v0) -> {
                    return Objects.nonNull(v0);
                }));
            }

            @Override // org.jf.util.AbstractForwardSequentialList, java.util.AbstractSequentialList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
            @Nonnull
            public Iterator<String> iterator() {
                return Iterators.transform(Iterators.filter(new DexEntryIterator(), (v0) -> {
                    return Objects.nonNull(v0);
                }), new Function<OatDexEntry, String>() { // from class: org.jf.dexlib2.dexbacked.OatFile.2.1
                    @Override // com.google.common.base.Function
                    @Nullable
                    public String apply(OatDexEntry dexEntry) {
                        return dexEntry.entryName;
                    }
                });
            }
        };
    }

    @Override // org.jf.dexlib2.iface.MultiDexContainer
    @Nullable
    /* renamed from: getEntry */
    public MultiDexContainer.DexEntry<DexBackedDexFile> getEntry2(@Nonnull String entryName) throws IOException {
        DexEntryIterator iterator = new DexEntryIterator();
        while (iterator.hasNext()) {
            OatDexEntry entry = iterator.next();
            if (entry != null && entry.getEntryName().equals(entryName)) {
                return entry;
            }
        }
        return null;
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$OatDexFile.class */
    public class OatDexFile extends DexBackedDexFile {
        public OatDexFile(@Nonnull byte[] buf, int offset) {
            super(OatFile.this.opcodes, buf, offset);
        }

        @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile
        public boolean supportsOptimizedOpcodes() {
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$OatCDexFile.class */
    public class OatCDexFile extends CDexBackedDexFile {
        public OatCDexFile(byte[] buf, int offset) {
            super(OatFile.this.opcodes, buf, offset);
        }

        @Override // org.jf.dexlib2.dexbacked.DexBackedDexFile
        public boolean supportsOptimizedOpcodes() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$OatHeader.class */
    public class OatHeader {
        private final int headerOffset;
        private final int keyValueStoreOffset;

        public OatHeader(int offset) {
            this.headerOffset = offset;
            if (getVersion() >= 170) {
                this.keyValueStoreOffset = 56;
            } else if (getVersion() >= 166) {
                this.keyValueStoreOffset = 64;
            } else if (getVersion() >= 162) {
                this.keyValueStoreOffset = 68;
            } else if (getVersion() >= 127) {
                this.keyValueStoreOffset = 76;
            } else {
                this.keyValueStoreOffset = 72;
            }
        }

        public boolean isValid() {
            for (int i = 0; i < OatFile.OAT_MAGIC.length; i++) {
                if (OatFile.this.buf[this.headerOffset + i] != OatFile.OAT_MAGIC[i]) {
                    return false;
                }
            }
            for (int i2 = 4; i2 < 7; i2++) {
                if (OatFile.this.buf[this.headerOffset + i2] < 48 || OatFile.this.buf[this.headerOffset + i2] > 57) {
                    return false;
                }
            }
            return OatFile.this.buf[this.headerOffset + 7] == 0;
        }

        public int getVersion() {
            return Integer.valueOf(new String(OatFile.this.buf, this.headerOffset + 4, 3)).intValue();
        }

        public int getDexFileCount() {
            return OatFile.this.readSmallUint(this.headerOffset + 20);
        }

        public int getKeyValueStoreSize() {
            if (getVersion() < 56) {
                throw new IllegalStateException("Unsupported oat version");
            }
            int fieldOffset = this.keyValueStoreOffset - 4;
            return OatFile.this.readSmallUint(this.headerOffset + fieldOffset);
        }

        public int getHeaderSize() {
            if (getVersion() < 56) {
                throw new IllegalStateException("Unsupported oat version");
            }
            return this.keyValueStoreOffset + getKeyValueStoreSize();
        }

        @Nullable
        public String getKeyValue(@Nonnull String key) {
            int size = getKeyValueStoreSize();
            int offset = this.headerOffset + this.keyValueStoreOffset;
            int endOffset = offset + size;
            while (offset < endOffset) {
                int keyStartOffset = offset;
                while (offset < endOffset && OatFile.this.buf[offset] != 0) {
                    offset++;
                }
                if (offset >= endOffset) {
                    throw new InvalidOatFileException("Oat file contains truncated key value store");
                }
                int keyEndOffset = offset;
                String k = new String(OatFile.this.buf, keyStartOffset, keyEndOffset - keyStartOffset);
                if (k.equals(key)) {
                    int offset2 = offset + 1;
                    while (offset2 < endOffset && OatFile.this.buf[offset2] != 0) {
                        offset2++;
                    }
                    if (offset2 >= endOffset) {
                        throw new InvalidOatFileException("Oat file contains truncated key value store");
                    }
                    int valueEndOffset = offset2;
                    return new String(OatFile.this.buf, offset2, valueEndOffset - offset2);
                }
                offset++;
            }
            return null;
        }

        public int getDexListStart() {
            if (getVersion() >= 127) {
                return this.headerOffset + OatFile.this.readSmallUint(this.headerOffset + 24);
            }
            return this.headerOffset + getHeaderSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public List<SectionHeader> getSections() {
        int offset;
        int entrySize;
        int entryCount;
        if (this.is64bit) {
            offset = readLongAsSmallUint(40);
            entrySize = readUshort(58);
            entryCount = readUshort(60);
        } else {
            offset = readSmallUint(32);
            entrySize = readUshort(46);
            entryCount = readUshort(48);
        }
        if (offset + (entrySize * entryCount) > this.buf.length) {
            throw new InvalidOatFileException("The ELF section headers extend past the end of the file");
        }
        final int i = entryCount;
        final int i2 = offset;
        final int i3 = entrySize;
        return new AbstractList<SectionHeader>() { // from class: org.jf.dexlib2.dexbacked.OatFile.3
            @Override // java.util.AbstractList, java.util.List
            public SectionHeader get(int index) {
                if (index >= 0 && index < i) {
                    if (OatFile.this.is64bit) {
                        return new SectionHeader64Bit(i2 + (index * i3));
                    }
                    return new SectionHeader32Bit(i2 + (index * i3));
                }
                throw new IndexOutOfBoundsException();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return i;
            }
        };
    }

    @Nonnull
    private SymbolTable getSymbolTable() {
        for (SectionHeader header : getSections()) {
            if (header.getType() == 11) {
                return new SymbolTable(header);
            }
        }
        throw new InvalidOatFileException("Oat file has no symbol table");
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nonnull
    public StringTable getSectionNameStringTable() {
        int index = readUshort(50);
        if (index == 0) {
            throw new InvalidOatFileException("There is no section name string table");
        }
        try {
            return new StringTable(getSections().get(index));
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidOatFileException("The section index for the section name string table is invalid");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$SectionHeader.class */
    public abstract class SectionHeader {
        protected final int offset;
        public static final int TYPE_DYNAMIC_SYMBOL_TABLE = 11;

        public abstract long getAddress();

        public abstract int getOffset();

        public abstract int getSize();

        public abstract int getLink();

        public abstract int getEntrySize();

        public SectionHeader(int offset) {
            this.offset = offset;
        }

        @Nonnull
        public String getName() {
            return OatFile.this.getSectionNameStringTable().getString(OatFile.this.readSmallUint(this.offset));
        }

        public int getType() {
            return OatFile.this.readInt(this.offset + 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$SectionHeader32Bit.class */
    public class SectionHeader32Bit extends SectionHeader {
        public SectionHeader32Bit(int offset) {
            super(offset);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public long getAddress() {
            return OatFile.this.readInt(this.offset + 12) & ExpandableListView.PACKED_POSITION_VALUE_NULL;
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getOffset() {
            return OatFile.this.readSmallUint(this.offset + 16);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getSize() {
            return OatFile.this.readSmallUint(this.offset + 20);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getLink() {
            return OatFile.this.readSmallUint(this.offset + 24);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getEntrySize() {
            return OatFile.this.readSmallUint(this.offset + 36);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$SectionHeader64Bit.class */
    public class SectionHeader64Bit extends SectionHeader {
        public SectionHeader64Bit(int offset) {
            super(offset);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public long getAddress() {
            return OatFile.this.readLong(this.offset + 16);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getOffset() {
            return OatFile.this.readLongAsSmallUint(this.offset + 24);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getSize() {
            return OatFile.this.readLongAsSmallUint(this.offset + 32);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getLink() {
            return OatFile.this.readSmallUint(this.offset + 40);
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.SectionHeader
        public int getEntrySize() {
            return OatFile.this.readLongAsSmallUint(this.offset + 56);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$SymbolTable.class */
    public class SymbolTable {
        @Nonnull
        private final StringTable stringTable;
        private final int offset;
        private final int entryCount;
        private final int entrySize;

        public SymbolTable(@Nonnull SectionHeader header) {
            try {
                this.stringTable = new StringTable((SectionHeader) OatFile.this.getSections().get(header.getLink()));
                this.offset = header.getOffset();
                this.entrySize = header.getEntrySize();
                this.entryCount = header.getSize() / this.entrySize;
                if (this.offset + (this.entryCount * this.entrySize) > OatFile.this.buf.length) {
                    throw new InvalidOatFileException("Symbol table extends past end of file");
                }
            } catch (IndexOutOfBoundsException e) {
                throw new InvalidOatFileException("String table section index is invalid");
            }
        }

        @Nonnull
        public List<Symbol> getSymbols() {
            return new AbstractList<Symbol>() { // from class: org.jf.dexlib2.dexbacked.OatFile.SymbolTable.1
                @Override // java.util.AbstractList, java.util.List
                public Symbol get(int index) {
                    if (index >= 0 && index < SymbolTable.this.entryCount) {
                        if (OatFile.this.is64bit) {
                            return new Symbol64(SymbolTable.this.offset + (index * SymbolTable.this.entrySize));
                        }
                        return new Symbol32(SymbolTable.this.offset + (index * SymbolTable.this.entrySize));
                    }
                    throw new IndexOutOfBoundsException();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public int size() {
                    return SymbolTable.this.entryCount;
                }
            };
        }

        /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$SymbolTable$Symbol.class */
        public abstract class Symbol {
            protected final int offset;
            static final /* synthetic */ boolean $assertionsDisabled;

            @Nonnull
            public abstract String getName();

            public abstract long getValue();

            public abstract int getSize();

            public abstract int getSectionIndex();

            static {
                $assertionsDisabled = !OatFile.class.desiredAssertionStatus();
            }

            public Symbol(int offset) {
                this.offset = offset;
            }

            public int getFileOffset() {
                try {
                    SectionHeader sectionHeader = (SectionHeader) OatFile.this.getSections().get(getSectionIndex());
                    long sectionAddress = sectionHeader.getAddress();
                    int sectionOffset = sectionHeader.getOffset();
                    int sectionSize = sectionHeader.getSize();
                    long symbolAddress = getValue();
                    if (symbolAddress < sectionAddress || symbolAddress >= sectionAddress + sectionSize) {
                        throw new InvalidOatFileException("symbol address lies outside it's associated section");
                    }
                    long fileOffset = sectionOffset + (getValue() - sectionAddress);
                    if ($assertionsDisabled || fileOffset <= 2147483647L) {
                        return (int) fileOffset;
                    }
                    throw new AssertionError();
                } catch (IndexOutOfBoundsException e) {
                    throw new InvalidOatFileException("Section index for symbol is out of bounds");
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$SymbolTable$Symbol32.class */
        public class Symbol32 extends Symbol {
            public Symbol32(int offset) {
                super(offset);
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            @Nonnull
            public String getName() {
                return SymbolTable.this.stringTable.getString(OatFile.this.readSmallUint(this.offset));
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            public long getValue() {
                return OatFile.this.readSmallUint(this.offset + 4);
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            public int getSize() {
                return OatFile.this.readSmallUint(this.offset + 8);
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            public int getSectionIndex() {
                return OatFile.this.readUshort(this.offset + 14);
            }
        }

        /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$SymbolTable$Symbol64.class */
        public class Symbol64 extends Symbol {
            public Symbol64(int offset) {
                super(offset);
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            @Nonnull
            public String getName() {
                return SymbolTable.this.stringTable.getString(OatFile.this.readSmallUint(this.offset));
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            public long getValue() {
                return OatFile.this.readLong(this.offset + 8);
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            public int getSize() {
                return OatFile.this.readLongAsSmallUint(this.offset + 16);
            }

            @Override // org.jf.dexlib2.dexbacked.OatFile.SymbolTable.Symbol
            public int getSectionIndex() {
                return OatFile.this.readUshort(this.offset + 6);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$StringTable.class */
    public class StringTable {
        private final int offset;
        private final int size;

        public StringTable(@Nonnull SectionHeader header) {
            this.offset = header.getOffset();
            this.size = header.getSize();
            if (this.offset + this.size > OatFile.this.buf.length) {
                throw new InvalidOatFileException("String table extends past end of file");
            }
        }

        @Nonnull
        public String getString(int index) {
            if (index >= this.size) {
                throw new InvalidOatFileException("String index is out of bounds");
            }
            int start = this.offset + index;
            int end = start;
            while (OatFile.this.buf[end] != 0) {
                end++;
                if (end >= this.offset + this.size) {
                    throw new InvalidOatFileException("String extends past end of string table");
                }
            }
            return new String(OatFile.this.buf, start, end - start, Charset.forName("US-ASCII"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$OatDexEntry.class */
    public class OatDexEntry implements MultiDexContainer.DexEntry<DexBackedDexFile> {
        public final String entryName;
        public final byte[] buf;
        public final int dexOffset;

        public OatDexEntry(String entryName, byte[] buf, int dexOffset) {
            this.entryName = entryName;
            this.buf = buf;
            this.dexOffset = dexOffset;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
        public DexBackedDexFile getDexFile() {
            if (CDexBackedDexFile.isCdex(this.buf, this.dexOffset)) {
                return new OatCDexFile(this.buf, this.dexOffset);
            }
            try {
                DexUtil.verifyDexHeader(this.buf, this.dexOffset);
                return new OatDexFile(this.buf, this.dexOffset);
            } catch (DexBackedDexFile.NotADexFile ex) {
                if (OatFile.this.getOatVersion() >= 87) {
                    throw new DexFileFactory.DexFileNotFoundException(ex, "Could not locate the embedded dex file %s. Is the vdex file missing?", this.entryName);
                }
                throw new DexFileFactory.DexFileNotFoundException(ex, "The embedded dex file %s does not appear to be a valid dex file.", this.entryName);
            }
        }

        @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public String getEntryName() {
            return this.entryName;
        }

        @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
        @Nonnull
        public MultiDexContainer<? extends DexBackedDexFile> getContainer() {
            return OatFile.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$DexEntryIterator.class */
    public class DexEntryIterator implements Iterator<OatDexEntry> {
        int index;
        int offset;

        private DexEntryIterator() {
            this.index = 0;
            this.offset = OatFile.this.oatHeader.getDexListStart();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < OatFile.this.oatHeader.getDexFileCount();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Removed duplicated region for block: B:4:0x0007  */
        @Override // java.util.Iterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public org.jf.dexlib2.dexbacked.OatFile.OatDexEntry next() {
            /*
                Method dump skipped, instructions count: 371
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jf.dexlib2.dexbacked.OatFile.DexEntryIterator.next():org.jf.dexlib2.dexbacked.OatFile$OatDexEntry");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/OatFile$InvalidOatFileException.class */
    public static class InvalidOatFileException extends RuntimeException {
        public InvalidOatFileException(String message) {
            super(message);
        }
    }
}
