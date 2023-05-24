package polyglot.types;

import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/NoClassException.class */
public class NoClassException extends SemanticException {
    private String className;

    public NoClassException(String className) {
        super(new StringBuffer().append("Class \"").append(className).append("\" not found.").toString());
        this.className = className;
    }

    public NoClassException(String className, Named scope) {
        super(new StringBuffer().append("Class \"").append(className).append("\" not found").append(scope != null ? new StringBuffer().append(" in scope of ").append(scope.toString()).toString() : ".").toString());
        this.className = className;
    }

    public NoClassException(String className, Position position) {
        super(new StringBuffer().append("Class \"").append(className).append("\" not found.").toString(), position);
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}
