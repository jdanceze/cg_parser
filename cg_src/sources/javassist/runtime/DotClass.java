package javassist.runtime;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/runtime/DotClass.class */
public class DotClass {
    public static NoClassDefFoundError fail(ClassNotFoundException e) {
        return new NoClassDefFoundError(e.getMessage());
    }
}
