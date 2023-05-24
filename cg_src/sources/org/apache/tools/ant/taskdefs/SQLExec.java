package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Appendable;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.KeepAliveOutputStream;
import org.apache.tools.ant.util.StringUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SQLExec.class */
public class SQLExec extends JDBCTask {
    private Union resources;
    private boolean rawBlobs;
    private int goodSql = 0;
    private int totalSql = 0;
    private Connection conn = null;
    private Statement statement = null;
    private File srcFile = null;
    private String sqlCommand = "";
    private List<Transaction> transactions = new Vector();
    private String delimiter = ";";
    private String delimiterType = DelimiterType.NORMAL;
    private boolean print = false;
    private boolean showheaders = true;
    private boolean showtrailers = true;
    private Resource output = null;
    private String outputEncoding = null;
    private String onError = "abort";
    private String encoding = null;
    private boolean append = false;
    private boolean keepformat = false;
    private boolean escapeProcessing = true;
    private boolean expandProperties = true;
    private boolean strictDelimiterMatching = true;
    private boolean showWarnings = false;
    private String csvColumnSep = ",";
    private String csvQuoteChar = null;
    private boolean treatWarningsAsErrors = false;
    private String errorProperty = null;
    private String warningProperty = null;
    private String rowCountProperty = null;
    private boolean forceCsvQuoteChar = false;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SQLExec$DelimiterType.class */
    public static class DelimiterType extends EnumeratedAttribute {
        public static final String NORMAL = "normal";
        public static final String ROW = "row";

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{NORMAL, ROW};
        }
    }

    public void setSrc(File srcFile) {
        this.srcFile = srcFile;
    }

    public void setExpandProperties(boolean expandProperties) {
        this.expandProperties = expandProperties;
    }

    public boolean getExpandProperties() {
        return this.expandProperties;
    }

    public void addText(String sql) {
        this.sqlCommand += sql;
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void add(ResourceCollection rc) {
        if (rc == null) {
            throw new BuildException("Cannot add null ResourceCollection");
        }
        synchronized (this) {
            if (this.resources == null) {
                this.resources = new Union();
            }
        }
        this.resources.add(rc);
    }

    public Transaction createTransaction() {
        Transaction t = new Transaction();
        this.transactions.add(t);
        return t;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setDelimiterType(DelimiterType delimiterType) {
        this.delimiterType = delimiterType.getValue();
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public void setShowheaders(boolean showheaders) {
        this.showheaders = showheaders;
    }

    public void setShowtrailers(boolean showtrailers) {
        this.showtrailers = showtrailers;
    }

    public void setOutput(File output) {
        setOutput(new FileResource(getProject(), output));
    }

    public void setOutput(Resource output) {
        this.output = output;
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void setOnerror(OnError action) {
        this.onError = action.getValue();
    }

    public void setKeepformat(boolean keepformat) {
        this.keepformat = keepformat;
    }

    public void setEscapeProcessing(boolean enable) {
        this.escapeProcessing = enable;
    }

    public void setRawBlobs(boolean rawBlobs) {
        this.rawBlobs = rawBlobs;
    }

    public void setStrictDelimiterMatching(boolean b) {
        this.strictDelimiterMatching = b;
    }

    public void setShowWarnings(boolean b) {
        this.showWarnings = b;
    }

    public void setTreatWarningsAsErrors(boolean b) {
        this.treatWarningsAsErrors = b;
    }

    public void setCsvColumnSeparator(String s) {
        this.csvColumnSep = s;
    }

    public void setCsvQuoteCharacter(String s) {
        if (s != null && s.length() > 1) {
            throw new BuildException("The quote character must be a single character.");
        }
        this.csvQuoteChar = s;
    }

    public void setErrorProperty(String errorProperty) {
        this.errorProperty = errorProperty;
    }

    public void setWarningProperty(String warningProperty) {
        this.warningProperty = warningProperty;
    }

    public void setRowCountProperty(String rowCountProperty) {
        this.rowCountProperty = rowCountProperty;
    }

    public void setForceCsvQuoteChar(boolean forceCsvQuoteChar) {
        this.forceCsvQuoteChar = forceCsvQuoteChar;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Appendable a;
        List<Transaction> savedTransaction = new Vector<>(this.transactions);
        String savedSqlCommand = this.sqlCommand;
        this.sqlCommand = this.sqlCommand.trim();
        try {
            if (this.srcFile == null && this.sqlCommand.isEmpty() && this.resources == null && this.transactions.isEmpty()) {
                throw new BuildException("Source file or resource collection, transactions or sql statement must be set!", getLocation());
            }
            if (this.srcFile != null && !this.srcFile.isFile()) {
                throw new BuildException("Source file " + this.srcFile + " is not a file!", getLocation());
            }
            if (this.resources != null) {
                Iterator<Resource> it = this.resources.iterator();
                while (it.hasNext()) {
                    Resource r = it.next();
                    createTransaction().setSrcResource(r);
                }
            }
            Transaction t = createTransaction();
            t.setSrc(this.srcFile);
            t.addText(this.sqlCommand);
            if (getConnection() == null) {
                return;
            }
            try {
                PrintStream out = KeepAliveOutputStream.wrapSystemOut();
                try {
                    if (this.output != null) {
                        log("Opening PrintStream to output Resource " + this.output, 3);
                        OutputStream os = null;
                        FileProvider fp = (FileProvider) this.output.as(FileProvider.class);
                        if (fp != null) {
                            os = FileUtils.newOutputStream(fp.getFile().toPath(), this.append);
                        } else {
                            if (this.append && (a = (Appendable) this.output.as(Appendable.class)) != null) {
                                os = a.getAppendOutputStream();
                            }
                            if (os == null) {
                                os = this.output.getOutputStream();
                                if (this.append) {
                                    log("Ignoring append=true for non-appendable resource " + this.output, 1);
                                }
                            }
                        }
                        if (this.outputEncoding != null) {
                            out = new PrintStream(new BufferedOutputStream(os), false, this.outputEncoding);
                        } else {
                            out = new PrintStream(new BufferedOutputStream(os));
                        }
                    }
                    for (Transaction txn : this.transactions) {
                        txn.runTransaction(out);
                        if (!isAutocommit()) {
                            log("Committing transaction", 3);
                            getConnection().commit();
                        }
                    }
                    FileUtils.close((OutputStream) out);
                    try {
                        FileUtils.close(getStatement());
                    } catch (SQLException e) {
                    }
                    FileUtils.close(getConnection());
                } catch (Throwable th) {
                    FileUtils.close((OutputStream) out);
                    throw th;
                }
            } catch (IOException | SQLException e2) {
                closeQuietly();
                setErrorProperty();
                if ("abort".equals(this.onError)) {
                    throw new BuildException(e2, getLocation());
                }
                try {
                    FileUtils.close(getStatement());
                } catch (SQLException e3) {
                }
                FileUtils.close(getConnection());
            }
            log(this.goodSql + " of " + this.totalSql + " SQL statements executed successfully");
            this.transactions = savedTransaction;
            this.sqlCommand = savedSqlCommand;
        } finally {
            this.transactions = savedTransaction;
            this.sqlCommand = savedSqlCommand;
        }
    }

    protected void runStatements(Reader reader, PrintStream out) throws SQLException, IOException {
        StringBuffer sql = new StringBuffer();
        BufferedReader in = new BufferedReader(reader);
        while (true) {
            String readLine = in.readLine();
            String line = readLine;
            if (readLine == null) {
                break;
            }
            if (!this.keepformat) {
                line = line.trim();
            }
            if (this.expandProperties) {
                line = getProject().replaceProperties(line);
            }
            if (!this.keepformat) {
                if (!line.startsWith("//") && !line.startsWith(HelpFormatter.DEFAULT_LONG_OPT_PREFIX)) {
                    StringTokenizer st = new StringTokenizer(line);
                    if (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        if ("REM".equalsIgnoreCase(token)) {
                        }
                    }
                }
            }
            sql.append(this.keepformat ? "\n" : Instruction.argsep).append(line);
            if (!this.keepformat && line.contains(HelpFormatter.DEFAULT_LONG_OPT_PREFIX)) {
                sql.append("\n");
            }
            int lastDelimPos = lastDelimiterPosition(sql, line);
            if (lastDelimPos > -1) {
                execSQL(sql.substring(0, lastDelimPos), out);
                sql.replace(0, sql.length(), "");
            }
        }
        if (sql.length() > 0) {
            execSQL(sql.toString(), out);
        }
    }

    protected void execSQL(String sql, PrintStream out) throws SQLException {
        if (sql.trim().isEmpty()) {
            return;
        }
        ResultSet resultSet = null;
        try {
            try {
                this.totalSql++;
                log("SQL: " + sql, 3);
                int updateCountTotal = 0;
                boolean ret = getStatement().execute(sql);
                int updateCount = getStatement().getUpdateCount();
                while (true) {
                    if (updateCount != -1) {
                        updateCountTotal += updateCount;
                    }
                    if (ret) {
                        resultSet = getStatement().getResultSet();
                        printWarnings(resultSet.getWarnings(), false);
                        resultSet.clearWarnings();
                        if (this.print) {
                            printResults(resultSet, out);
                        }
                    }
                    ret = getStatement().getMoreResults();
                    updateCount = getStatement().getUpdateCount();
                    if (!ret && updateCount == -1) {
                        break;
                    }
                }
                printWarnings(getStatement().getWarnings(), false);
                getStatement().clearWarnings();
                log(updateCountTotal + " rows affected", 3);
                if (updateCountTotal != -1) {
                    setRowCountProperty(updateCountTotal);
                }
                if (this.print && this.showtrailers) {
                    out.println(updateCountTotal + " rows affected");
                }
                SQLWarning warning = getConnection().getWarnings();
                printWarnings(warning, true);
                getConnection().clearWarnings();
                this.goodSql++;
                FileUtils.close(resultSet);
            } catch (SQLException e) {
                log("Failed to execute: " + sql, 0);
                setErrorProperty();
                if (!"abort".equals(this.onError)) {
                    log(e.toString(), 0);
                }
                if (!"continue".equals(this.onError)) {
                    throw e;
                }
                FileUtils.close(resultSet);
            }
        } catch (Throwable th) {
            FileUtils.close(resultSet);
            throw th;
        }
    }

    @Deprecated
    protected void printResults(PrintStream out) throws SQLException {
        ResultSet rs = getStatement().getResultSet();
        try {
            printResults(rs, out);
            if (rs != null) {
                rs.close();
            }
        } catch (Throwable th) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    protected void printResults(ResultSet rs, PrintStream out) throws SQLException {
        if (rs != null) {
            log("Processing new result set.", 3);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            if (columnCount > 0) {
                if (this.showheaders) {
                    out.print(maybeQuote(md.getColumnName(1)));
                    for (int col = 2; col <= columnCount; col++) {
                        out.print(this.csvColumnSep);
                        out.print(maybeQuote(md.getColumnName(col)));
                    }
                    out.println();
                }
                while (rs.next()) {
                    printValue(rs, 1, out);
                    for (int col2 = 2; col2 <= columnCount; col2++) {
                        out.print(this.csvColumnSep);
                        printValue(rs, col2, out);
                    }
                    out.println();
                    printWarnings(rs.getWarnings(), false);
                }
            }
        }
        out.println();
    }

    private void printValue(ResultSet rs, int col, PrintStream out) throws SQLException {
        if (this.rawBlobs && rs.getMetaData().getColumnType(col) == 2004) {
            Blob blob = rs.getBlob(col);
            if (blob != null) {
                new StreamPumper(rs.getBlob(col).getBinaryStream(), out).run();
                return;
            }
            return;
        }
        out.print(maybeQuote(rs.getString(col)));
    }

    private String maybeQuote(String s) {
        char[] charArray;
        if (this.csvQuoteChar == null || s == null || (!this.forceCsvQuoteChar && !s.contains(this.csvColumnSep) && !s.contains(this.csvQuoteChar))) {
            return s;
        }
        StringBuilder sb = new StringBuilder(this.csvQuoteChar);
        char q = this.csvQuoteChar.charAt(0);
        for (char c : s.toCharArray()) {
            if (c == q) {
                sb.append(q);
            }
            sb.append(c);
        }
        return sb.append(this.csvQuoteChar).toString();
    }

    private void closeQuietly() {
        if (!isAutocommit() && getConnection() != null && "abort".equals(this.onError)) {
            try {
                getConnection().rollback();
            } catch (SQLException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.JDBCTask
    public Connection getConnection() {
        if (this.conn == null) {
            this.conn = super.getConnection();
            if (!isValidRdbms(this.conn)) {
                this.conn = null;
            }
        }
        return this.conn;
    }

    protected Statement getStatement() throws SQLException {
        if (this.statement == null) {
            this.statement = getConnection().createStatement();
            this.statement.setEscapeProcessing(this.escapeProcessing);
        }
        return this.statement;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SQLExec$OnError.class */
    public static class OnError extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"continue", "stop", "abort"};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/SQLExec$Transaction.class */
    public class Transaction {
        private Resource tSrcResource = null;
        private String tSqlCommand = "";

        public Transaction() {
        }

        public void setSrc(File src) {
            if (src != null) {
                setSrcResource(new FileResource(src));
            }
        }

        public void setSrcResource(Resource src) {
            if (this.tSrcResource != null) {
                throw new BuildException("only one resource per transaction");
            }
            this.tSrcResource = src;
        }

        public void addText(String sql) {
            if (sql != null) {
                this.tSqlCommand += sql;
            }
        }

        public void addConfigured(ResourceCollection a) {
            if (a.size() != 1) {
                throw new BuildException("only single argument resource collections are supported.");
            }
            setSrcResource(a.iterator().next());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void runTransaction(PrintStream out) throws IOException, SQLException {
            if (!this.tSqlCommand.isEmpty()) {
                SQLExec.this.log("Executing commands", 2);
                SQLExec.this.runStatements(new StringReader(this.tSqlCommand), out);
            }
            if (this.tSrcResource != null) {
                SQLExec.this.log("Executing resource: " + this.tSrcResource.toString(), 2);
                Charset charset = SQLExec.this.encoding == null ? Charset.defaultCharset() : Charset.forName(SQLExec.this.encoding);
                Reader reader = new InputStreamReader(this.tSrcResource.getInputStream(), charset);
                try {
                    SQLExec.this.runStatements(reader, out);
                    reader.close();
                } catch (Throwable th) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        }
    }

    public int lastDelimiterPosition(StringBuffer buf, String currentLine) {
        if (this.strictDelimiterMatching) {
            if ((this.delimiterType.equals(DelimiterType.NORMAL) && StringUtils.endsWith(buf, this.delimiter)) || (this.delimiterType.equals(DelimiterType.ROW) && currentLine.equals(this.delimiter))) {
                return buf.length() - this.delimiter.length();
            }
            return -1;
        }
        String d = this.delimiter.trim().toLowerCase(Locale.ENGLISH);
        if (DelimiterType.NORMAL.equals(this.delimiterType)) {
            int endIndex = this.delimiter.length() - 1;
            int bufferIndex = buf.length() - 1;
            while (bufferIndex >= 0 && Character.isWhitespace(buf.charAt(bufferIndex))) {
                bufferIndex--;
            }
            if (bufferIndex < endIndex) {
                return -1;
            }
            while (endIndex >= 0) {
                if (buf.substring(bufferIndex, bufferIndex + 1).toLowerCase(Locale.ENGLISH).charAt(0) != d.charAt(endIndex)) {
                    return -1;
                }
                bufferIndex--;
                endIndex--;
            }
            return bufferIndex + 1;
        } else if (currentLine.trim().toLowerCase(Locale.ENGLISH).equals(d)) {
            return buf.length() - currentLine.length();
        } else {
            return -1;
        }
    }

    private void printWarnings(SQLWarning warning, boolean force) throws SQLException {
        if (this.showWarnings || force) {
            while (warning != null) {
                log(warning + " sql warning", this.showWarnings ? 1 : 3);
                warning = warning.getNextWarning();
            }
        }
        if (warning != null) {
            setWarningProperty();
        }
        if (this.treatWarningsAsErrors && warning != null) {
            throw warning;
        }
    }

    protected final void setErrorProperty() {
        setProperty(this.errorProperty, "true");
    }

    protected final void setWarningProperty() {
        setProperty(this.warningProperty, "true");
    }

    protected final void setRowCountProperty(int rowCount) {
        setProperty(this.rowCountProperty, Integer.toString(rowCount));
    }

    private void setProperty(String name, String value) {
        if (name != null) {
            getProject().setNewProperty(name, value);
        }
    }
}
