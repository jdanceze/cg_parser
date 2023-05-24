package soot;

import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/Local.class */
public interface Local extends Value, Numberable, Immediate {
    String getName();

    void setName(String str);

    void setType(Type type);

    boolean isStackLocal();
}
