package javassist;
/* compiled from: ClassPoolTail.java */
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/ClassPathList.class */
final class ClassPathList {
    ClassPathList next;
    ClassPath path;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ClassPathList(ClassPath p, ClassPathList n) {
        this.next = n;
        this.path = p;
    }
}
