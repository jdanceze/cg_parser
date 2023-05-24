package polyglot.types;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/BadSerializationException.class */
public class BadSerializationException extends SemanticException {
    private String className;

    private static String message(String className) {
        String className2 = className.replace('/', '.');
        return new StringBuffer().append("Could not decode Polyglot type information for \"").append(className2).append("\". The most likely cause is ").append("that the compiler has ").append("been modified since the class file was created.  ").append("Please delete ").append("the class file for \"").append(className2).append("\", and recompile from source.").toString();
    }

    public BadSerializationException(String className) {
        super(message(className));
        this.className = className;
    }

    public BadSerializationException(String className, Position position) {
        super(message(className), position);
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}
