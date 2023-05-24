package soot.tagkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AbstractHost.class */
public class AbstractHost implements Host {
    protected int line;
    protected int col;
    protected List<Tag> mTagList = null;

    @Override // soot.tagkit.Host
    public List<Tag> getTags() {
        return this.mTagList == null ? Collections.emptyList() : this.mTagList;
    }

    @Override // soot.tagkit.Host
    public void removeTag(String aName) {
        int tagIndex = searchForTag(aName);
        if (tagIndex != -1) {
            this.mTagList.remove(tagIndex);
        }
    }

    private int searchForTag(String aName) {
        if (this.mTagList != null) {
            int i = 0;
            for (Tag tag : this.mTagList) {
                if (tag != null && tag.getName().equals(aName)) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        return -1;
    }

    @Override // soot.tagkit.Host
    public Tag getTag(String aName) {
        int tagIndex = searchForTag(aName);
        if (tagIndex == -1) {
            return null;
        }
        return this.mTagList.get(tagIndex);
    }

    @Override // soot.tagkit.Host
    public boolean hasTag(String aName) {
        return searchForTag(aName) != -1;
    }

    @Override // soot.tagkit.Host
    public void addTag(Tag t) {
        if (this.mTagList == null) {
            this.mTagList = new ArrayList(1);
        }
        this.mTagList.add(t);
    }

    @Override // soot.tagkit.Host
    public void removeAllTags() {
        this.mTagList = null;
    }

    @Override // soot.tagkit.Host
    public void addAllTagsOf(Host h) {
        List<Tag> tags = h.getTags();
        if (!tags.isEmpty()) {
            if (this.mTagList == null) {
                this.mTagList = new ArrayList(tags.size());
            }
            this.mTagList.addAll(tags);
        }
    }

    @Override // soot.tagkit.Host
    public int getJavaSourceStartLineNumber() {
        if (this.line <= 0) {
            SourceLnPosTag tag = (SourceLnPosTag) getTag(SourceLnPosTag.NAME);
            if (tag != null) {
                this.line = tag.startLn();
            } else {
                LineNumberTag tag2 = (LineNumberTag) getTag(LineNumberTag.NAME);
                this.line = tag2 == null ? -1 : tag2.getLineNumber();
            }
        }
        return this.line;
    }

    @Override // soot.tagkit.Host
    public int getJavaSourceStartColumnNumber() {
        if (this.col <= 0) {
            SourceLnPosTag tag = (SourceLnPosTag) getTag(SourceLnPosTag.NAME);
            this.col = tag == null ? -1 : tag.startPos();
        }
        return this.col;
    }
}
