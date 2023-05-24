package soot;
/* loaded from: gencallgraphv3.jar:soot/ClassMember.class */
public interface ClassMember {
    SootClass getDeclaringClass();

    boolean isDeclared();

    boolean isPhantom();

    void setPhantom(boolean z);

    boolean isProtected();

    boolean isPrivate();

    boolean isPublic();

    boolean isStatic();

    void setModifiers(int i);

    int getModifiers();
}
