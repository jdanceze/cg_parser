package polyglot.util;

import java.io.Serializable;
import org.apache.commons.cli.HelpFormatter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/Position.class */
public class Position implements Serializable {
    static final long serialVersionUID = -4588386982624074261L;
    private String file;
    private int line;
    private int column;
    private int endLine;
    private int endColumn;
    public static final int UNKNOWN = -1;
    public static final int END_UNUSED = -2;
    public static final Position COMPILER_GENERATED = new Position("Compiler Generated");

    public static Position compilerGenerated(int depth) {
        new Position();
        StackTraceElement[] stack = new Exception().getStackTrace();
        if (depth < stack.length) {
            return new Position(new StringBuffer().append(stack[depth].getFileName()).append(" (compiler generated)").toString(), stack[depth].getLineNumber());
        }
        return COMPILER_GENERATED;
    }

    public static Position compilerGenerated() {
        return compilerGenerated(2);
    }

    protected Position() {
    }

    public Position(String file) {
        this(file, -1, -1);
    }

    public Position(String file, int line) {
        this(file, line, -1);
    }

    public Position(String file, int line, int column) {
        this(file, line, column, -2, -2);
    }

    public Position(String file, int line, int column, int endLine, int endColumn) {
        this.file = file;
        this.line = line;
        this.column = column;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public Position(Position start, Position end) {
        this(start.file(), start.line, start.column, end.endLine, end.endColumn);
    }

    public int line() {
        return this.line;
    }

    public int column() {
        return this.column;
    }

    public int endLine() {
        if (this.endLine == -1 || (this.line != -1 && this.endLine < this.line)) {
            return this.line;
        }
        return this.endLine;
    }

    public int endColumn() {
        if (this.endColumn == -1 || (this.column != -1 && endLine() == line() && this.endColumn < this.column)) {
            return this.column;
        }
        return this.endColumn;
    }

    public String file() {
        return this.file;
    }

    public String nameAndLineString() {
        String s = this.file;
        if (this.line != -1) {
            s = new StringBuffer().append(s).append(":").append(this.line).toString();
            if (this.endLine != this.line && this.endLine != -1 && this.endLine != -2) {
                s = new StringBuffer().append(s).append(HelpFormatter.DEFAULT_OPT_PREFIX).append(this.endLine).toString();
            }
        }
        return s;
    }

    public String toString() {
        String s = this.file;
        if (this.line != -1) {
            s = new StringBuffer().append(s).append(":").append(this.line).toString();
            if (this.column != -1) {
                s = new StringBuffer().append(s).append(",").append(this.column).toString();
                if (this.line == this.endLine && this.endColumn != -1 && this.endColumn != -2) {
                    s = new StringBuffer().append(s).append(HelpFormatter.DEFAULT_OPT_PREFIX).append(this.endColumn).toString();
                }
                if (this.line != this.endLine && this.endColumn != -1 && this.endColumn != -2) {
                    s = new StringBuffer().append(s).append(HelpFormatter.DEFAULT_OPT_PREFIX).append(this.endLine).append(":").append(this.endColumn).toString();
                }
            }
        }
        return s;
    }
}
