package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Problem.class */
public class Problem implements Comparable {
    protected int line;
    protected int column;
    protected int endLine;
    protected int endColumn;
    protected String fileName;
    protected String message;
    protected Severity severity;
    protected Kind kind;

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        if (o instanceof Problem) {
            Problem other = (Problem) o;
            if (!this.fileName.equals(other.fileName)) {
                return this.fileName.compareTo(other.fileName);
            }
            if (this.line != other.line) {
                return this.line - other.line;
            }
            return this.message.compareTo(other.message);
        }
        return 0;
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Problem$Severity.class */
    public static class Severity {
        public static final Severity ERROR = new Severity();
        public static final Severity WARNING = new Severity();

        private Severity() {
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Problem$Kind.class */
    public static class Kind {
        public static final Kind OTHER = new Kind();
        public static final Kind LEXICAL = new Kind();
        public static final Kind SYNTACTIC = new Kind();
        public static final Kind SEMANTIC = new Kind();

        private Kind() {
        }
    }

    public int line() {
        return this.line;
    }

    public int column() {
        return this.column;
    }

    public int endLine() {
        return this.endLine;
    }

    public int endColumn() {
        return this.endColumn;
    }

    public String fileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String message() {
        return this.message;
    }

    public Severity severity() {
        return this.severity;
    }

    public Kind kind() {
        return this.kind;
    }

    public Problem(String fileName, String message) {
        this.line = -1;
        this.column = -1;
        this.endLine = -1;
        this.endColumn = -1;
        this.severity = Severity.ERROR;
        this.kind = Kind.OTHER;
        this.fileName = fileName;
        this.message = message;
    }

    public Problem(String fileName, String message, int line) {
        this(fileName, message);
        this.line = line;
    }

    public Problem(String fileName, String message, int line, Severity severity) {
        this(fileName, message);
        this.line = line;
        this.severity = severity;
    }

    public Problem(String fileName, String message, int line, int column, Severity severity) {
        this(fileName, message);
        this.line = line;
        this.column = column;
        this.severity = severity;
    }

    public Problem(String fileName, String message, int line, Severity severity, Kind kind) {
        this(fileName, message);
        this.line = line;
        this.kind = kind;
        this.severity = severity;
    }

    public Problem(String fileName, String message, int line, int column, Severity severity, Kind kind) {
        this(fileName, message);
        this.line = line;
        this.column = column;
        this.kind = kind;
        this.severity = severity;
    }

    public Problem(String fileName, String message, int line, int column, int endLine, int endColumn, Severity severity, Kind kind) {
        this(fileName, message);
        this.line = line;
        this.column = column;
        this.endLine = endLine;
        this.endColumn = endColumn;
        this.kind = kind;
        this.severity = severity;
    }

    public String toString() {
        String location = "";
        if (this.line != -1 && this.column != -1) {
            location = String.valueOf(this.line) + "," + this.column + ":";
        } else if (this.line != -1) {
            location = String.valueOf(this.line) + ":";
        }
        String s = "";
        if (this.kind == Kind.LEXICAL) {
            s = "Lexical Error: ";
        } else if (this.kind == Kind.SYNTACTIC) {
            s = "Syntactic Error: ";
        } else if (this.kind == Kind.SEMANTIC) {
            s = "Semantic Error: ";
        }
        return String.valueOf(this.fileName) + ":" + location + "\n  " + s + this.message;
    }
}
