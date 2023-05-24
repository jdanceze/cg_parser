package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/DigestAlgorithm.class */
public class DigestAlgorithm implements Algorithm {
    private static final int BYTE_MASK = 255;
    private static final int BUFFER_SIZE = 8192;
    private String algorithm = "MD5";
    private String provider = null;
    private MessageDigest messageDigest = null;
    private int readBufferSize = 8192;

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm != null ? algorithm.toUpperCase(Locale.ENGLISH) : null;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void initMessageDigest() {
        if (this.messageDigest != null) {
            return;
        }
        if (this.provider != null && !this.provider.isEmpty() && !Jimple.NULL.equals(this.provider)) {
            try {
                this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
                return;
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                throw new BuildException(e);
            }
        }
        try {
            this.messageDigest = MessageDigest.getInstance(this.algorithm);
        } catch (NoSuchAlgorithmException noalgo) {
            throw new BuildException(noalgo);
        }
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public boolean isValid() {
        return "SHA".equals(this.algorithm) || "MD5".equals(this.algorithm);
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Algorithm
    public String getValue(File file) {
        byte[] digest;
        if (!file.canRead()) {
            return null;
        }
        initMessageDigest();
        byte[] buf = new byte[this.readBufferSize];
        this.messageDigest.reset();
        try {
            DigestInputStream dis = new DigestInputStream(Files.newInputStream(file.toPath(), new OpenOption[0]), this.messageDigest);
            while (dis.read(buf, 0, this.readBufferSize) != -1) {
            }
            StringBuilder checksumSb = new StringBuilder();
            for (byte digestByte : this.messageDigest.digest()) {
                checksumSb.append(String.format("%02x", Integer.valueOf(255 & digestByte)));
            }
            String sb = checksumSb.toString();
            dis.close();
            return sb;
        } catch (IOException e) {
            return null;
        }
    }

    public String toString() {
        return String.format("<DigestAlgorithm:algorithm=%s;provider=%s>", this.algorithm, this.provider);
    }
}
