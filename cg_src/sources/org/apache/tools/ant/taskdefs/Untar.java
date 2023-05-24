package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.zip.GZIPInputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Untar.class */
public class Untar extends Expand {
    private UntarCompressionMethod compression;

    public Untar() {
        super(null);
        this.compression = new UntarCompressionMethod();
    }

    public void setCompression(UntarCompressionMethod method) {
        this.compression = method;
    }

    @Override // org.apache.tools.ant.taskdefs.Expand
    public void setScanForUnicodeExtraFields(boolean b) {
        throw new BuildException("The " + getTaskName() + " task doesn't support the encoding attribute", getLocation());
    }

    @Override // org.apache.tools.ant.taskdefs.Expand
    protected void expandFile(FileUtils fileUtils, File srcF, File dir) {
        if (!srcF.exists()) {
            throw new BuildException("Unable to untar " + srcF + " as the file does not exist", getLocation());
        }
        try {
            InputStream fis = Files.newInputStream(srcF.toPath(), new OpenOption[0]);
            expandStream(srcF.getPath(), fis, dir);
            if (fis != null) {
                fis.close();
            }
        } catch (IOException ioe) {
            throw new BuildException("Error while expanding " + srcF.getPath() + "\n" + ioe.toString(), ioe, getLocation());
        }
    }

    @Override // org.apache.tools.ant.taskdefs.Expand
    protected void expandResource(Resource srcR, File dir) {
        if (!srcR.isExists()) {
            throw new BuildException("Unable to untar " + srcR.getName() + " as the it does not exist", getLocation());
        }
        try {
            InputStream i = srcR.getInputStream();
            expandStream(srcR.getName(), i, dir);
            if (i != null) {
                i.close();
            }
        } catch (IOException ioe) {
            throw new BuildException("Error while expanding " + srcR.getName(), ioe, getLocation());
        }
    }

    private void expandStream(String name, InputStream stream, File dir) throws IOException {
        TarInputStream tis = new TarInputStream(this.compression.decompress(name, new BufferedInputStream(stream)), getEncoding());
        try {
            log("Expanding: " + name + " into " + dir, 2);
            boolean empty = true;
            FileNameMapper mapper = getMapper();
            while (true) {
                TarEntry te = tis.getNextEntry();
                if (te == null) {
                    break;
                }
                empty = false;
                extractFile(FileUtils.getFileUtils(), null, dir, tis, te.getName(), te.getModTime(), te.isDirectory(), mapper);
            }
            if (empty && getFailOnEmptyArchive()) {
                throw new BuildException("archive '%s' is empty", name);
            }
            log("expand complete", 3);
            tis.close();
        } catch (Throwable th) {
            try {
                tis.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Untar$UntarCompressionMethod.class */
    public static final class UntarCompressionMethod extends EnumeratedAttribute {
        private static final String NONE = "none";
        private static final String GZIP = "gzip";
        private static final String BZIP2 = "bzip2";
        private static final String XZ = "xz";

        public UntarCompressionMethod() {
            setValue("none");
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"none", GZIP, BZIP2, XZ};
        }

        public InputStream decompress(String name, InputStream istream) throws IOException, BuildException {
            String v = getValue();
            if (GZIP.equals(v)) {
                return new GZIPInputStream(istream);
            }
            if (XZ.equals(v)) {
                return newXZInputStream(istream);
            }
            if (BZIP2.equals(v)) {
                char[] magic = {'B', 'Z'};
                for (char c : magic) {
                    if (istream.read() != c) {
                        throw new BuildException("Invalid bz2 file." + name);
                    }
                }
                return new CBZip2InputStream(istream);
            }
            return istream;
        }

        private static InputStream newXZInputStream(InputStream istream) throws BuildException {
            try {
                return (InputStream) Class.forName("org.tukaani.xz.XZInputStream").asSubclass(InputStream.class).getConstructor(InputStream.class).newInstance(istream);
            } catch (ClassNotFoundException ex) {
                throw new BuildException("xz decompression requires the XZ for Java library", ex);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex2) {
                throw new BuildException("failed to create XZInputStream", ex2);
            }
        }
    }
}
