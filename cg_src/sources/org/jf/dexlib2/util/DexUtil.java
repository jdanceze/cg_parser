package org.jf.dexlib2.util;

import com.google.common.io.ByteStreams;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import org.jf.dexlib2.dexbacked.raw.CdexHeaderItem;
import org.jf.dexlib2.dexbacked.raw.HeaderItem;
import org.jf.dexlib2.dexbacked.raw.OdexHeaderItem;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/DexUtil.class */
public class DexUtil {
    public static int verifyDexHeader(@Nonnull InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            throw new IllegalArgumentException("InputStream must support mark");
        }
        inputStream.mark(44);
        byte[] partialHeader = new byte[44];
        try {
            try {
                ByteStreams.readFully(inputStream, partialHeader);
                inputStream.reset();
                return verifyDexHeader(partialHeader, 0);
            } catch (EOFException e) {
                throw new DexBackedDexFile.NotADexFile("File is too short");
            }
        } catch (Throwable th) {
            inputStream.reset();
            throw th;
        }
    }

    public static int verifyDexHeader(@Nonnull byte[] buf, int offset) {
        int dexVersion = HeaderItem.getVersion(buf, offset);
        if (dexVersion == -1) {
            StringBuilder sb = new StringBuilder("Not a valid dex magic value:");
            for (int i = 0; i < 8; i++) {
                sb.append(String.format(" %02x", Byte.valueOf(buf[i])));
            }
            throw new DexBackedDexFile.NotADexFile(sb.toString());
        } else if (!HeaderItem.isSupportedDexVersion(dexVersion)) {
            throw new UnsupportedFile(String.format("Dex version %03d is not supported", Integer.valueOf(dexVersion)));
        } else {
            int endian = HeaderItem.getEndian(buf, offset);
            if (endian == 2018915346) {
                throw new UnsupportedFile("Big endian dex files are not supported");
            }
            if (endian != 305419896) {
                throw new InvalidFile(String.format("Invalid endian tag: 0x%x", Integer.valueOf(endian)));
            }
            return dexVersion;
        }
    }

    public static int verifyCdexHeader(@Nonnull byte[] buf, int offset) {
        int cdexVersion = CdexHeaderItem.getVersion(buf, offset);
        if (cdexVersion == -1) {
            StringBuilder sb = new StringBuilder("Not a valid cdex magic value:");
            for (int i = 0; i < 8; i++) {
                sb.append(String.format(" %02x", Byte.valueOf(buf[offset + i])));
            }
            throw new DexBackedDexFile.NotADexFile(sb.toString());
        } else if (!CdexHeaderItem.isSupportedCdexVersion(cdexVersion)) {
            throw new UnsupportedFile(String.format("Dex version %03d is not supported", Integer.valueOf(cdexVersion)));
        } else {
            int endian = HeaderItem.getEndian(buf, offset);
            if (endian == 2018915346) {
                throw new UnsupportedFile("Big endian dex files are not supported");
            }
            if (endian != 305419896) {
                throw new InvalidFile(String.format("Invalid endian tag: 0x%x", Integer.valueOf(endian)));
            }
            return cdexVersion;
        }
    }

    public static void verifyOdexHeader(@Nonnull InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            throw new IllegalArgumentException("InputStream must support mark");
        }
        inputStream.mark(8);
        byte[] partialHeader = new byte[8];
        try {
            try {
                ByteStreams.readFully(inputStream, partialHeader);
                inputStream.reset();
                verifyOdexHeader(partialHeader, 0);
            } catch (EOFException e) {
                throw new DexBackedOdexFile.NotAnOdexFile("File is too short");
            }
        } catch (Throwable th) {
            inputStream.reset();
            throw th;
        }
    }

    public static void verifyOdexHeader(@Nonnull byte[] buf, int offset) {
        int odexVersion = OdexHeaderItem.getVersion(buf, offset);
        if (odexVersion == -1) {
            StringBuilder sb = new StringBuilder("Not a valid odex magic value:");
            for (int i = 0; i < 8; i++) {
                sb.append(String.format(" %02x", Byte.valueOf(buf[i])));
            }
            throw new DexBackedOdexFile.NotAnOdexFile(sb.toString());
        } else if (!OdexHeaderItem.isSupportedOdexVersion(odexVersion)) {
            throw new UnsupportedFile(String.format("Odex version %03d is not supported", Integer.valueOf(odexVersion)));
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/DexUtil$InvalidFile.class */
    public static class InvalidFile extends RuntimeException {
        public InvalidFile() {
        }

        public InvalidFile(String message) {
            super(message);
        }

        public InvalidFile(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidFile(Throwable cause) {
            super(cause);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/DexUtil$UnsupportedFile.class */
    public static class UnsupportedFile extends RuntimeException {
        public UnsupportedFile() {
        }

        public UnsupportedFile(String message) {
            super(message);
        }

        public UnsupportedFile(String message, Throwable cause) {
            super(message, cause);
        }

        public UnsupportedFile(Throwable cause) {
            super(cause);
        }
    }
}
