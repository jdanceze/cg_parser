package javassist;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/Translator.class */
public interface Translator {
    void start(ClassPool classPool) throws NotFoundException, CannotCompileException;

    void onLoad(ClassPool classPool, String str) throws NotFoundException, CannotCompileException;
}
