package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SourceLnNamePosTag.class */
public class SourceLnNamePosTag extends SourceLnPosTag {
    protected final String fileName;

    public SourceLnNamePosTag(String fileName, int sline, int eline, int spos, int epos) {
        super(sline, eline, spos, epos);
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    @Override // soot.tagkit.SourceLnPosTag
    public String toString() {
        return String.valueOf(super.toString()) + " file: " + this.fileName;
    }
}
