package org.jf.dexlib2;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import org.jf.dexlib2.dexbacked.OatFile;
import org.jf.dexlib2.dexbacked.ZipDexContainer;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.dexlib2.writer.pool.DexPool;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory.class */
public final class DexFileFactory {
    @Nonnull
    public static DexBackedDexFile loadDexFile(@Nonnull String path, @Nullable Opcodes opcodes) throws IOException {
        return loadDexFile(new File(path), opcodes);
    }

    @Nonnull
    public static DexBackedDexFile loadDexFile(@Nonnull File file, @Nullable Opcodes opcodes) throws IOException {
        if (file.exists()) {
            try {
                ZipDexContainer container = new ZipDexContainer(file, opcodes);
                return new DexEntryFinder(file.getPath(), container).findEntry("classes.dex", true).getDexFile();
            } catch (ZipDexContainer.NotAZipFileException e) {
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                try {
                    try {
                        DexBackedDexFile fromInputStream = DexBackedDexFile.fromInputStream(opcodes, inputStream);
                        inputStream.close();
                        return fromInputStream;
                    } catch (DexBackedDexFile.NotADexFile e2) {
                        try {
                            DexBackedOdexFile fromInputStream2 = DexBackedOdexFile.fromInputStream(opcodes, inputStream);
                            inputStream.close();
                            return fromInputStream2;
                        } catch (DexBackedOdexFile.NotAnOdexFile e3) {
                            OatFile oatFile = null;
                            try {
                                oatFile = OatFile.fromInputStream(inputStream, new FilenameVdexProvider(file));
                            } catch (OatFile.NotAnOatFileException e4) {
                            }
                            if (oatFile == null) {
                                inputStream.close();
                                throw new UnsupportedFileTypeException("%s is not an apk, dex, odex or oat file.", file.getPath());
                            } else if (oatFile.isSupportedVersion() == 0) {
                                throw new UnsupportedOatVersionException(oatFile);
                            } else {
                                List<DexBackedDexFile> oatDexFiles = oatFile.getDexFiles();
                                if (oatDexFiles.size() == 0) {
                                    throw new DexFileNotFoundException("Oat file %s contains no dex files", file.getName());
                                }
                                DexBackedDexFile dexBackedDexFile = oatDexFiles.get(0);
                                inputStream.close();
                                return dexBackedDexFile;
                            }
                        }
                    }
                } catch (Throwable th) {
                    inputStream.close();
                    throw th;
                }
            }
        }
        throw new DexFileNotFoundException("%s does not exist", file.getName());
    }

    public static MultiDexContainer.DexEntry<? extends DexBackedDexFile> loadDexEntry(@Nonnull File file, @Nonnull String dexEntry, boolean exactMatch, @Nullable Opcodes opcodes) throws IOException {
        if (!file.exists()) {
            throw new DexFileNotFoundException("Container file %s does not exist", file.getName());
        }
        try {
            ZipDexContainer container = new ZipDexContainer(file, opcodes);
            return new DexEntryFinder(file.getPath(), container).findEntry(dexEntry, exactMatch);
        } catch (ZipDexContainer.NotAZipFileException e) {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            OatFile oatFile = null;
            try {
                try {
                    oatFile = OatFile.fromInputStream(inputStream, new FilenameVdexProvider(file));
                } catch (OatFile.NotAnOatFileException e2) {
                }
                if (oatFile != null) {
                    if (oatFile.isSupportedVersion() == 0) {
                        throw new UnsupportedOatVersionException(oatFile);
                    }
                    List<? extends DexFile> oatDexFiles = oatFile.getDexFiles();
                    if (oatDexFiles.size() == 0) {
                        throw new DexFileNotFoundException("Oat file %s contains no dex files", file.getName());
                    }
                    MultiDexContainer.DexEntry<? extends DexBackedDexFile> findEntry = new DexEntryFinder(file.getPath(), oatFile).findEntry(dexEntry, exactMatch);
                    inputStream.close();
                    return findEntry;
                }
                inputStream.close();
                throw new UnsupportedFileTypeException("%s is not an apk or oat file.", file.getPath());
            } catch (Throwable th) {
                inputStream.close();
                throw th;
            }
        }
    }

    public static MultiDexContainer<? extends DexBackedDexFile> loadDexContainer(@Nonnull File file, @Nullable Opcodes opcodes) throws IOException {
        if (file.exists()) {
            ZipDexContainer zipDexContainer = new ZipDexContainer(file, opcodes);
            if (zipDexContainer.isZipFile()) {
                return zipDexContainer;
            }
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            try {
                try {
                    DexBackedDexFile dexFile = DexBackedDexFile.fromInputStream(opcodes, inputStream);
                    SingletonMultiDexContainer singletonMultiDexContainer = new SingletonMultiDexContainer(file.getPath(), dexFile);
                    inputStream.close();
                    return singletonMultiDexContainer;
                } catch (DexBackedDexFile.NotADexFile e) {
                    try {
                        DexBackedOdexFile odexFile = DexBackedOdexFile.fromInputStream(opcodes, inputStream);
                        SingletonMultiDexContainer singletonMultiDexContainer2 = new SingletonMultiDexContainer(file.getPath(), odexFile);
                        inputStream.close();
                        return singletonMultiDexContainer2;
                    } catch (DexBackedOdexFile.NotAnOdexFile e2) {
                        OatFile oatFile = null;
                        try {
                            oatFile = OatFile.fromInputStream(inputStream, new FilenameVdexProvider(file));
                        } catch (OatFile.NotAnOatFileException e3) {
                        }
                        if (oatFile == null) {
                            inputStream.close();
                            throw new UnsupportedFileTypeException("%s is not an apk, dex, odex or oat file.", file.getPath());
                        } else if (oatFile.isSupportedVersion() == 0) {
                            throw new UnsupportedOatVersionException(oatFile);
                        } else {
                            OatFile oatFile2 = oatFile;
                            inputStream.close();
                            return oatFile2;
                        }
                    }
                }
            } catch (Throwable th) {
                inputStream.close();
                throw th;
            }
        }
        throw new DexFileNotFoundException("%s does not exist", file.getName());
    }

    public static void writeDexFile(@Nonnull String path, @Nonnull DexFile dexFile) throws IOException {
        DexPool.writeTo(path, dexFile);
    }

    private DexFileFactory() {
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory$DexFileNotFoundException.class */
    public static class DexFileNotFoundException extends ExceptionWithContext {
        public DexFileNotFoundException(@Nullable String message, Object... formatArgs) {
            super(message, formatArgs);
        }

        public DexFileNotFoundException(Throwable cause, @Nullable String message, Object... formatArgs) {
            super(cause, message, formatArgs);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory$UnsupportedOatVersionException.class */
    public static class UnsupportedOatVersionException extends ExceptionWithContext {
        @Nonnull
        public final OatFile oatFile;

        public UnsupportedOatVersionException(@Nonnull OatFile oatFile) {
            super("Unsupported oat version: %d", Integer.valueOf(oatFile.getOatVersion()));
            this.oatFile = oatFile;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory$MultipleMatchingDexEntriesException.class */
    public static class MultipleMatchingDexEntriesException extends ExceptionWithContext {
        public MultipleMatchingDexEntriesException(@Nonnull String message, Object... formatArgs) {
            super(String.format(message, formatArgs), new Object[0]);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory$UnsupportedFileTypeException.class */
    public static class UnsupportedFileTypeException extends ExceptionWithContext {
        public UnsupportedFileTypeException(@Nonnull String message, Object... formatArgs) {
            super(String.format(message, formatArgs), new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean fullEntryMatch(@Nonnull String entry, @Nonnull String targetEntry) {
        if (entry.equals(targetEntry)) {
            return true;
        }
        if (entry.charAt(0) == '/') {
            entry = entry.substring(1);
        }
        if (targetEntry.charAt(0) == '/') {
            targetEntry = targetEntry.substring(1);
        }
        return entry.equals(targetEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean partialEntryMatch(String entry, String targetEntry) {
        if (entry.equals(targetEntry)) {
            return true;
        }
        if (!entry.endsWith(targetEntry)) {
            return false;
        }
        char precedingChar = entry.charAt((entry.length() - targetEntry.length()) - 1);
        char firstTargetChar = targetEntry.charAt(0);
        return firstTargetChar == ':' || firstTargetChar == '/' || firstTargetChar == '!' || precedingChar == ':' || precedingChar == '/' || precedingChar == '!';
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory$DexEntryFinder.class */
    public static class DexEntryFinder {
        private final String filename;
        private final MultiDexContainer<? extends DexBackedDexFile> dexContainer;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DexFileFactory.class.desiredAssertionStatus();
        }

        public DexEntryFinder(@Nonnull String filename, @Nonnull MultiDexContainer<? extends DexBackedDexFile> dexContainer) {
            this.filename = filename;
            this.dexContainer = dexContainer;
        }

        @Nonnull
        public MultiDexContainer.DexEntry<? extends DexBackedDexFile> findEntry(@Nonnull String targetEntry, boolean exactMatch) throws IOException {
            if (exactMatch) {
                try {
                    MultiDexContainer.DexEntry<? extends DexBackedDexFile> entry = this.dexContainer.getEntry(targetEntry);
                    if (entry == null) {
                        throw new DexFileNotFoundException("Could not find entry %s in %s.", targetEntry, this.filename);
                    }
                    return entry;
                } catch (DexBackedDexFile.NotADexFile e) {
                    throw new UnsupportedFileTypeException("Entry %s in %s is not a dex file", targetEntry, this.filename);
                }
            }
            List<String> fullMatches = Lists.newArrayList();
            List<MultiDexContainer.DexEntry<? extends DexBackedDexFile>> fullEntries = Lists.newArrayList();
            List<String> partialMatches = Lists.newArrayList();
            List<MultiDexContainer.DexEntry<? extends DexBackedDexFile>> partialEntries = Lists.newArrayList();
            for (String entry2 : this.dexContainer.getDexEntryNames()) {
                if (!DexFileFactory.fullEntryMatch(entry2, targetEntry)) {
                    if (DexFileFactory.partialEntryMatch(entry2, targetEntry)) {
                        partialMatches.add(entry2);
                        MultiDexContainer.DexEntry<? extends DexBackedDexFile> dexEntry = this.dexContainer.getEntry(entry2);
                        if (!$assertionsDisabled && dexEntry == null) {
                            throw new AssertionError();
                        }
                        partialEntries.add(dexEntry);
                    } else {
                        continue;
                    }
                } else {
                    fullMatches.add(entry2);
                    MultiDexContainer.DexEntry<? extends DexBackedDexFile> dexEntry2 = this.dexContainer.getEntry(entry2);
                    if (!$assertionsDisabled && dexEntry2 == null) {
                        throw new AssertionError();
                    }
                    fullEntries.add(dexEntry2);
                }
            }
            if (fullEntries.size() == 1) {
                try {
                    MultiDexContainer.DexEntry<? extends DexBackedDexFile> dexEntry3 = fullEntries.get(0);
                    if ($assertionsDisabled || dexEntry3 != null) {
                        return dexEntry3;
                    }
                    throw new AssertionError();
                } catch (DexBackedDexFile.NotADexFile e2) {
                    throw new UnsupportedFileTypeException("Entry %s in %s is not a dex file", fullMatches.get(0), this.filename);
                }
            } else if (fullEntries.size() > 1) {
                throw new MultipleMatchingDexEntriesException(String.format("Multiple entries in %s match %s: %s", this.filename, targetEntry, Joiner.on(", ").join(fullMatches)), new Object[0]);
            } else {
                if (partialEntries.size() == 0) {
                    throw new DexFileNotFoundException("Could not find a dex entry in %s matching %s", this.filename, targetEntry);
                }
                if (partialEntries.size() > 1) {
                    throw new MultipleMatchingDexEntriesException(String.format("Multiple dex entries in %s match %s: %s", this.filename, targetEntry, Joiner.on(", ").join(partialMatches)), new Object[0]);
                }
                return partialEntries.get(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory$SingletonMultiDexContainer.class */
    public static class SingletonMultiDexContainer implements MultiDexContainer<DexBackedDexFile> {
        private final String entryName;
        private final DexBackedDexFile dexFile;

        public SingletonMultiDexContainer(@Nonnull String entryName, @Nonnull DexBackedDexFile dexFile) {
            this.entryName = entryName;
            this.dexFile = dexFile;
        }

        @Override // org.jf.dexlib2.iface.MultiDexContainer
        @Nonnull
        public List<String> getDexEntryNames() {
            return ImmutableList.of(this.entryName);
        }

        @Override // org.jf.dexlib2.iface.MultiDexContainer
        @Nullable
        public MultiDexContainer.DexEntry<DexBackedDexFile> getEntry(@Nonnull final String entryName) {
            if (entryName.equals(this.entryName)) {
                return new MultiDexContainer.DexEntry<DexBackedDexFile>() { // from class: org.jf.dexlib2.DexFileFactory.SingletonMultiDexContainer.1
                    @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
                    @Nonnull
                    public String getEntryName() {
                        return entryName;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
                    @Nonnull
                    public DexBackedDexFile getDexFile() {
                        return SingletonMultiDexContainer.this.dexFile;
                    }

                    @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
                    @Nonnull
                    public MultiDexContainer<? extends DexBackedDexFile> getContainer() {
                        return SingletonMultiDexContainer.this;
                    }
                };
            }
            return null;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/DexFileFactory$FilenameVdexProvider.class */
    public static class FilenameVdexProvider implements OatFile.VdexProvider {
        private final File vdexFile;
        @Nullable
        private byte[] buf = null;
        private boolean loadedVdex = false;

        public FilenameVdexProvider(File oatFile) {
            File oatParent = oatFile.getAbsoluteFile().getParentFile();
            String baseName = Files.getNameWithoutExtension(oatFile.getAbsolutePath());
            this.vdexFile = new File(oatParent, baseName + ".vdex");
        }

        @Override // org.jf.dexlib2.dexbacked.OatFile.VdexProvider
        @Nullable
        public byte[] getVdex() {
            File parentDirectory;
            if (!this.loadedVdex) {
                File candidateFile = this.vdexFile;
                if (!candidateFile.exists() && (parentDirectory = candidateFile.getParentFile().getParentFile()) != null) {
                    candidateFile = new File(parentDirectory, this.vdexFile.getName());
                }
                if (candidateFile.exists()) {
                    try {
                        this.buf = ByteStreams.toByteArray(new FileInputStream(candidateFile));
                    } catch (FileNotFoundException e) {
                        this.buf = null;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                this.loadedVdex = true;
            }
            return this.buf;
        }
    }
}
