package soot.tagkit;

import java.nio.charset.StandardCharsets;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SourceFileTag.class */
public class SourceFileTag implements Tag {
    public static final String NAME = "SourceFileTag";
    private String sourceFile;
    private String absolutePath;

    public SourceFileTag(String sourceFile) {
        this(sourceFile, null);
    }

    public SourceFileTag(String sourceFile, String path) {
        this.sourceFile = sourceFile.intern();
        this.absolutePath = path;
    }

    public SourceFileTag() {
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return this.sourceFile.getBytes(StandardCharsets.UTF_8);
    }

    public void setSourceFile(String srcFile) {
        this.sourceFile = srcFile.intern();
    }

    public String getSourceFile() {
        return this.sourceFile;
    }

    public void setAbsolutePath(String path) {
        this.absolutePath = path;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    public String toString() {
        return this.sourceFile;
    }
}
