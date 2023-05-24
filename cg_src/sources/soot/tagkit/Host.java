package soot.tagkit;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/tagkit/Host.class */
public interface Host {
    List<Tag> getTags();

    Tag getTag(String str);

    void addTag(Tag tag);

    void removeTag(String str);

    boolean hasTag(String str);

    void removeAllTags();

    void addAllTagsOf(Host host);

    int getJavaSourceStartLineNumber();

    int getJavaSourceStartColumnNumber();
}
