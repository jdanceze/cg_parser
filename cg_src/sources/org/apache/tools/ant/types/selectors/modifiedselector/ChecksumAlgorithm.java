package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/ChecksumAlgorithm.class */
public class ChecksumAlgorithm implements Algorithm {
    private String algorithm = "CRC";
    private Checksum checksum = null;

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm != null ? algorithm.toUpperCase(Locale.ENGLISH) : null;
    }

    public void initChecksum() {
        if (this.checksum != null) {
            return;
        }
        if ("CRC".equals(this.algorithm)) {
            this.checksum = new CRC32();
        } else if ("ADLER".equals(this.algorithm)) {
            this.checksum = new Adler32();
        } else {
            throw new BuildException(new NoSuchAlgorithmException());
        }
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public boolean isValid() {
        return "CRC".equals(this.algorithm) || "ADLER".equals(this.algorithm);
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public String getValue(File file) {
        initChecksum();
        if (file.canRead()) {
            this.checksum.reset();
            try {
                CheckedInputStream check = new CheckedInputStream(new BufferedInputStream(Files.newInputStream(file.toPath(), new OpenOption[0])), this.checksum);
                while (check.read() != -1) {
                }
                String l = Long.toString(check.getChecksum().getValue());
                check.close();
                return l;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public String toString() {
        return String.format("<ChecksumAlgorithm:algorithm=%s>", this.algorithm);
    }
}
