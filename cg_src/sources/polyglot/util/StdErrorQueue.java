package polyglot.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.StringTokenizer;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/StdErrorQueue.class */
public class StdErrorQueue extends AbstractErrorQueue {
    private PrintStream err;

    public StdErrorQueue(PrintStream err, int limit, String name) {
        super(limit, name);
        this.err = err;
    }

    @Override // polyglot.util.AbstractErrorQueue
    public void displayError(ErrorInfo e) {
        boolean needNewline;
        int width;
        String message = e.getErrorKind() != 0 ? e.getMessage() : new StringBuffer().append(e.getErrorString()).append(" -- ").append(e.getMessage()).toString();
        Position position = e.getPosition();
        String prefix = position != null ? position.nameAndLineString() : this.name;
        this.err.print(new StringBuffer().append(prefix).append(":").toString());
        int width2 = 0 + prefix.length() + 1;
        StringTokenizer lines = new StringTokenizer(message, "\n", true);
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken();
            if (line.indexOf("\n") < 0) {
                StringTokenizer st = new StringTokenizer(line, Instruction.argsep);
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    if (width2 + s.length() + 1 > 78) {
                        this.err.println();
                        for (int i = 0; i < 4; i++) {
                            this.err.print(Instruction.argsep);
                        }
                        width = 4;
                    } else {
                        this.err.print(Instruction.argsep);
                        width = width2 + 1;
                    }
                    this.err.print(s);
                    width2 = width + s.length();
                }
                needNewline = true;
            } else {
                this.err.println();
                needNewline = false;
            }
            width2 = 4;
            if (lines.hasMoreTokens()) {
                for (int i2 = 0; i2 < 4; i2++) {
                    this.err.print(Instruction.argsep);
                }
            } else if (needNewline) {
                this.err.println();
            }
        }
        if (position != null) {
            showError(position);
        }
    }

    @Override // polyglot.util.AbstractErrorQueue
    protected void tooManyErrors(ErrorInfo lastError) {
        Position position = lastError.getPosition();
        String prefix = position != null ? new StringBuffer().append(position.file()).append(": ").toString() : "";
        this.err.println(new StringBuffer().append(prefix).append("Too many errors.  Aborting compilation.").toString());
    }

    protected Reader reader(Position pos) throws IOException {
        if (pos.file() != null && pos.line() != -1) {
            return new FileReader(pos.file());
        }
        return null;
    }

    private void showError(Position pos) {
        try {
            Reader r = reader(pos);
            if (r == null) {
                return;
            }
            LineNumberReader reader = new LineNumberReader(r);
            String s = null;
            while (reader.getLineNumber() < pos.line()) {
                s = reader.readLine();
            }
            if (s != null) {
                this.err.println(s);
                showErrorIndicator(pos, reader.getLineNumber(), s);
                if (pos.endLine() != pos.line() && pos.endLine() != -1 && pos.endLine() != -2) {
                    if (pos.endLine() - pos.line() > 1) {
                        for (int j = 0; j < s.length() && Character.isWhitespace(s.charAt(j)); j++) {
                            this.err.print(s.charAt(j));
                        }
                        this.err.println("...");
                    }
                    while (reader.getLineNumber() < pos.endLine()) {
                        s = reader.readLine();
                    }
                    if (s != null) {
                        this.err.println(s);
                        showErrorIndicator(pos, reader.getLineNumber(), s);
                    }
                }
            }
            reader.close();
            this.err.println();
        } catch (IOException e) {
        }
    }

    protected void showErrorIndicator(Position pos, int lineNum, String s) {
        if (pos.column() == -1) {
            return;
        }
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            int i2 = i;
            i++;
            this.err.print(s.charAt(i2));
        }
        int startIndAt = i;
        int stopIndAt = s.length() - 1;
        if (pos.line() == lineNum) {
            startIndAt = pos.column();
        }
        if (pos.endLine() == lineNum) {
            stopIndAt = pos.endColumn() - 1;
        }
        if (stopIndAt < startIndAt) {
            stopIndAt = startIndAt;
        }
        if (pos.endColumn() == -1 || pos.endColumn() == -2) {
            stopIndAt = startIndAt;
        }
        while (i <= stopIndAt) {
            char c = '-';
            if (i < startIndAt) {
                c = ' ';
            }
            if (i < s.length() && s.charAt(i) == '\t') {
                c = '\t';
            }
            if (i == startIndAt && pos.line() == lineNum) {
                c = '^';
            }
            if (i == stopIndAt && pos.endLine() == lineNum) {
                c = '^';
            }
            this.err.print(c);
            i++;
        }
        this.err.println();
    }

    @Override // polyglot.util.AbstractErrorQueue, polyglot.util.ErrorQueue
    public void flush() {
        if (!this.flushed && errorCount() > 0) {
            this.err.println(new StringBuffer().append(errorCount()).append(" error").append(errorCount() > 1 ? "s." : ".").toString());
        }
        super.flush();
    }
}
