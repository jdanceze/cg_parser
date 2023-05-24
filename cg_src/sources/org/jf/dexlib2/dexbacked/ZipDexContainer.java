package org.jf.dexlib2.dexbacked;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.jf.dexlib2.util.DexUtil;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/ZipDexContainer.class */
public class ZipDexContainer implements MultiDexContainer<DexBackedDexFile> {
    private final File zipFilePath;
    @Nullable
    private final Opcodes opcodes;

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/ZipDexContainer$NotAZipFileException.class */
    public static class NotAZipFileException extends RuntimeException {
    }

    public ZipDexContainer(@Nonnull File zipFilePath, @Nullable Opcodes opcodes) {
        this.zipFilePath = zipFilePath;
        this.opcodes = opcodes;
    }

    @Override // org.jf.dexlib2.iface.MultiDexContainer
    @Nonnull
    public List<String> getDexEntryNames() throws IOException {
        List<String> entryNames = Lists.newArrayList();
        ZipFile zipFile = getZipFile();
        try {
            Enumeration<? extends ZipEntry> entriesEnumeration = zipFile.entries();
            while (entriesEnumeration.hasMoreElements()) {
                ZipEntry entry = entriesEnumeration.nextElement();
                if (isDex(zipFile, entry)) {
                    entryNames.add(entry.getName());
                }
            }
            return entryNames;
        } finally {
            zipFile.close();
        }
    }

    @Override // org.jf.dexlib2.iface.MultiDexContainer
    @Nullable
    public MultiDexContainer.DexEntry<DexBackedDexFile> getEntry(@Nonnull String entryName) throws IOException {
        ZipFile zipFile = getZipFile();
        try {
            ZipEntry entry = zipFile.getEntry(entryName);
            if (entry == null) {
                return null;
            }
            MultiDexContainer.DexEntry<DexBackedDexFile> loadEntry = loadEntry(zipFile, entry);
            zipFile.close();
            return loadEntry;
        } finally {
            zipFile.close();
        }
    }

    public boolean isZipFile() {
        ZipFile zipFile = null;
        try {
            zipFile = getZipFile();
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                }
            }
            return true;
        } catch (IOException e2) {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e3) {
                }
            }
            return false;
        } catch (NotAZipFileException e4) {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e5) {
                }
            }
            return false;
        } catch (Throwable th) {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    protected boolean isDex(@Nonnull ZipFile zipFile, @Nonnull ZipEntry zipEntry) throws IOException {
        InputStream inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
        try {
            DexUtil.verifyDexHeader(inputStream);
            inputStream.close();
            return true;
        } catch (DexBackedDexFile.NotADexFile e) {
            inputStream.close();
            return false;
        } catch (DexUtil.InvalidFile e2) {
            inputStream.close();
            return false;
        } catch (DexUtil.UnsupportedFile e3) {
            inputStream.close();
            return false;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    protected ZipFile getZipFile() throws IOException {
        try {
            return new ZipFile(this.zipFilePath);
        } catch (IOException e) {
            throw new NotAZipFileException();
        }
    }

    @Nonnull
    protected MultiDexContainer.DexEntry loadEntry(@Nonnull ZipFile zipFile, @Nonnull final ZipEntry zipEntry) throws IOException {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        try {
            final byte[] buf = ByteStreams.toByteArray(inputStream);
            MultiDexContainer.DexEntry dexEntry = new MultiDexContainer.DexEntry() { // from class: org.jf.dexlib2.dexbacked.ZipDexContainer.1
                @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
                @Nonnull
                public String getEntryName() {
                    return zipEntry.getName();
                }

                @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
                @Nonnull
                public DexFile getDexFile() {
                    return new DexBackedDexFile(ZipDexContainer.this.opcodes, buf);
                }

                @Override // org.jf.dexlib2.iface.MultiDexContainer.DexEntry
                @Nonnull
                public MultiDexContainer getContainer() {
                    return ZipDexContainer.this;
                }
            };
            inputStream.close();
            return dexEntry;
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }
}
