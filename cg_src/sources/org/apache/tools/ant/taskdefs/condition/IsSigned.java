package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ManifestTask;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.util.StreamUtils;
import org.apache.tools.zip.ZipFile;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsSigned.class */
public class IsSigned extends DataType implements Condition {
    private static final String SIG_START = "META-INF/";
    private static final String SIG_END = ".SF";
    private static final int SHORT_SIG_LIMIT = 8;
    private String name;
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isSigned(File zipFile, String name) throws IOException {
        ZipFile jarFile = new ZipFile(zipFile);
        try {
            if (null == name) {
                boolean anyMatch = StreamUtils.enumerationAsStream(jarFile.getEntries()).anyMatch(e -> {
                    return e.getName().startsWith(SIG_START) && e.getName().endsWith(SIG_END);
                });
                jarFile.close();
                return anyMatch;
            }
            String name2 = replaceInvalidChars(name);
            boolean shortSig = jarFile.getEntry(new StringBuilder().append(SIG_START).append(name2.toUpperCase()).append(SIG_END).toString()) != null;
            boolean longSig = false;
            if (name2.length() > 8) {
                longSig = jarFile.getEntry(new StringBuilder().append(SIG_START).append(name2.substring(0, 8).toUpperCase()).append(SIG_END).toString()) != null;
            }
            boolean z = shortSig || longSig;
            jarFile.close();
            return z;
        } catch (Throwable th) {
            try {
                jarFile.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() {
        if (this.file == null) {
            throw new BuildException("The file attribute must be set.");
        }
        if (!this.file.exists()) {
            log("The file \"" + this.file.getAbsolutePath() + "\" does not exist.", 3);
            return false;
        }
        boolean r = false;
        try {
            r = isSigned(this.file, this.name);
        } catch (IOException e) {
            log("Got IOException reading file \"" + this.file.getAbsolutePath() + "\"" + e, 1);
        }
        if (r) {
            log("File \"" + this.file.getAbsolutePath() + "\" is signed.", 3);
        }
        return r;
    }

    private static String replaceInvalidChars(String name) {
        char[] charArray;
        StringBuilder sb = new StringBuilder();
        boolean changes = false;
        for (char ch : name.toCharArray()) {
            if (ManifestTask.VALID_ATTRIBUTE_CHARS.indexOf(ch) < 0) {
                sb.append("_");
                changes = true;
            } else {
                sb.append(ch);
            }
        }
        return changes ? sb.toString() : name;
    }
}
