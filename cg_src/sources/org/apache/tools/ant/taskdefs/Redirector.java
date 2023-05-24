package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.util.ConcatFileInputStream;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.KeepAliveOutputStream;
import org.apache.tools.ant.util.LazyFileOutputStream;
import org.apache.tools.ant.util.LeadPipeInputStream;
import org.apache.tools.ant.util.LineOrientedOutputStreamRedirector;
import org.apache.tools.ant.util.NullOutputStream;
import org.apache.tools.ant.util.OutputStreamFunneler;
import org.apache.tools.ant.util.ReaderInputStream;
import org.apache.tools.ant.util.TeeOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Redirector.class */
public class Redirector {
    private static final int STREAMPUMPER_WAIT_INTERVAL = 1000;
    private static final String DEFAULT_ENCODING = System.getProperty("file.encoding");
    private File[] input;
    private File[] out;
    private File[] error;
    private boolean logError;
    private PropertyOutputStream baos;
    private PropertyOutputStream errorBaos;
    private String outputProperty;
    private String errorProperty;
    private String inputString;
    private boolean appendOut;
    private boolean appendErr;
    private boolean alwaysLogOut;
    private boolean alwaysLogErr;
    private boolean createEmptyFilesOut;
    private boolean createEmptyFilesErr;
    private final ProjectComponent managingTask;
    private OutputStream outputStream;
    private OutputStream errorStream;
    private InputStream inputStream;
    private PrintStream outPrintStream;
    private PrintStream errorPrintStream;
    private Vector<FilterChain> outputFilterChains;
    private Vector<FilterChain> errorFilterChains;
    private Vector<FilterChain> inputFilterChains;
    private String outputEncoding;
    private String errorEncoding;
    private String inputEncoding;
    private boolean appendProperties;
    private final ThreadGroup threadGroup;
    private boolean logInputString;
    private final Object inMutex;
    private final Object outMutex;
    private final Object errMutex;
    private boolean outputIsBinary;
    private boolean discardOut;
    private boolean discardErr;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Redirector$PropertyOutputStream.class */
    public class PropertyOutputStream extends ByteArrayOutputStream {
        private final String property;
        private boolean closed = false;

        PropertyOutputStream(String property) {
            this.property = property;
        }

        @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (Redirector.this.outMutex) {
                if (!this.closed && (!Redirector.this.appendOut || !Redirector.this.appendProperties)) {
                    Redirector.this.setPropertyFromBAOS(this, this.property);
                    this.closed = true;
                }
            }
        }
    }

    public Redirector(Task managingTask) {
        this((ProjectComponent) managingTask);
    }

    public Redirector(ProjectComponent managingTask) {
        this.logError = false;
        this.baos = null;
        this.errorBaos = null;
        this.appendOut = false;
        this.appendErr = false;
        this.alwaysLogOut = false;
        this.alwaysLogErr = false;
        this.createEmptyFilesOut = true;
        this.createEmptyFilesErr = true;
        this.outputStream = null;
        this.errorStream = null;
        this.inputStream = null;
        this.outPrintStream = null;
        this.errorPrintStream = null;
        this.outputEncoding = DEFAULT_ENCODING;
        this.errorEncoding = DEFAULT_ENCODING;
        this.inputEncoding = DEFAULT_ENCODING;
        this.appendProperties = true;
        this.threadGroup = new ThreadGroup("redirector");
        this.logInputString = true;
        this.inMutex = new Object();
        this.outMutex = new Object();
        this.errMutex = new Object();
        this.outputIsBinary = false;
        this.discardOut = false;
        this.discardErr = false;
        this.managingTask = managingTask;
    }

    public void setInput(File input) {
        setInput(input == null ? null : new File[]{input});
    }

    public void setInput(File[] input) {
        synchronized (this.inMutex) {
            if (input == null) {
                this.input = null;
            } else {
                this.input = (File[]) input.clone();
            }
        }
    }

    public void setInputString(String inputString) {
        synchronized (this.inMutex) {
            this.inputString = inputString;
        }
    }

    public void setLogInputString(boolean logInputString) {
        this.logInputString = logInputString;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInputStream(InputStream inputStream) {
        synchronized (this.inMutex) {
            this.inputStream = inputStream;
        }
    }

    public void setOutput(File out) {
        setOutput(out == null ? null : new File[]{out});
    }

    public void setOutput(File[] out) {
        synchronized (this.outMutex) {
            if (out == null) {
                this.out = null;
            } else {
                this.out = (File[]) out.clone();
            }
        }
    }

    public void setOutputEncoding(String outputEncoding) {
        if (outputEncoding == null) {
            throw new IllegalArgumentException("outputEncoding must not be null");
        }
        synchronized (this.outMutex) {
            this.outputEncoding = outputEncoding;
        }
    }

    public void setErrorEncoding(String errorEncoding) {
        if (errorEncoding == null) {
            throw new IllegalArgumentException("errorEncoding must not be null");
        }
        synchronized (this.errMutex) {
            this.errorEncoding = errorEncoding;
        }
    }

    public void setInputEncoding(String inputEncoding) {
        if (inputEncoding == null) {
            throw new IllegalArgumentException("inputEncoding must not be null");
        }
        synchronized (this.inMutex) {
            this.inputEncoding = inputEncoding;
        }
    }

    public void setLogError(boolean logError) {
        synchronized (this.errMutex) {
            this.logError = logError;
        }
    }

    public void setAppendProperties(boolean appendProperties) {
        synchronized (this.outMutex) {
            this.appendProperties = appendProperties;
        }
    }

    public void setError(File error) {
        setError(error == null ? null : new File[]{error});
    }

    public void setError(File[] error) {
        synchronized (this.errMutex) {
            if (error == null) {
                this.error = null;
            } else {
                this.error = (File[]) error.clone();
            }
        }
    }

    public void setOutputProperty(String outputProperty) {
        if (outputProperty == null || !outputProperty.equals(this.outputProperty)) {
            synchronized (this.outMutex) {
                this.outputProperty = outputProperty;
                this.baos = null;
            }
        }
    }

    public void setAppend(boolean append) {
        synchronized (this.outMutex) {
            this.appendOut = append;
        }
        synchronized (this.errMutex) {
            this.appendErr = append;
        }
    }

    public void setDiscardOutput(boolean discard) {
        synchronized (this.outMutex) {
            this.discardOut = discard;
        }
    }

    public void setDiscardError(boolean discard) {
        synchronized (this.errMutex) {
            this.discardErr = discard;
        }
    }

    public void setAlwaysLog(boolean alwaysLog) {
        synchronized (this.outMutex) {
            this.alwaysLogOut = alwaysLog;
        }
        synchronized (this.errMutex) {
            this.alwaysLogErr = alwaysLog;
        }
    }

    public void setCreateEmptyFiles(boolean createEmptyFiles) {
        synchronized (this.outMutex) {
            this.createEmptyFilesOut = createEmptyFiles;
        }
        synchronized (this.outMutex) {
            this.createEmptyFilesErr = createEmptyFiles;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0013, code lost:
        if (r4.equals(r3.errorProperty) == false) goto L5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setErrorProperty(java.lang.String r4) {
        /*
            r3 = this;
            r0 = r3
            java.lang.Object r0 = r0.errMutex
            r1 = r0
            r5 = r1
            monitor-enter(r0)
            r0 = r4
            if (r0 == 0) goto L16
            r0 = r4
            r1 = r3
            java.lang.String r1 = r1.errorProperty     // Catch: java.lang.Throwable -> L25
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L25
            if (r0 != 0) goto L20
        L16:
            r0 = r3
            r1 = r4
            r0.errorProperty = r1     // Catch: java.lang.Throwable -> L25
            r0 = r3
            r1 = 0
            r0.errorBaos = r1     // Catch: java.lang.Throwable -> L25
        L20:
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L25
            goto L2a
        L25:
            r6 = move-exception
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L25
            r0 = r6
            throw r0
        L2a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.taskdefs.Redirector.setErrorProperty(java.lang.String):void");
    }

    public void setInputFilterChains(Vector<FilterChain> inputFilterChains) {
        synchronized (this.inMutex) {
            this.inputFilterChains = inputFilterChains;
        }
    }

    public void setOutputFilterChains(Vector<FilterChain> outputFilterChains) {
        synchronized (this.outMutex) {
            this.outputFilterChains = outputFilterChains;
        }
    }

    public void setErrorFilterChains(Vector<FilterChain> errorFilterChains) {
        synchronized (this.errMutex) {
            this.errorFilterChains = errorFilterChains;
        }
    }

    public void setBinaryOutput(boolean b) {
        this.outputIsBinary = b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPropertyFromBAOS(ByteArrayOutputStream baos, String propertyName) {
        BufferedReader in = new BufferedReader(new StringReader(Execute.toString(baos)));
        this.managingTask.getProject().setNewProperty(propertyName, (String) in.lines().collect(Collectors.joining(System.lineSeparator())));
    }

    public void createStreams() {
        synchronized (this.outMutex) {
            outStreams();
            if (this.alwaysLogOut || this.outputStream == null) {
                OutputStream outputLog = new LogOutputStream(this.managingTask, 2);
                this.outputStream = this.outputStream == null ? outputLog : new TeeOutputStream(outputLog, this.outputStream);
            }
            if ((this.outputFilterChains != null && this.outputFilterChains.size() > 0) || !this.outputEncoding.equalsIgnoreCase(this.inputEncoding)) {
                try {
                    LeadPipeInputStream snk = new LeadPipeInputStream();
                    snk.setManagingComponent(this.managingTask);
                    Reader reader = new InputStreamReader(snk, this.inputEncoding);
                    if (this.outputFilterChains != null && this.outputFilterChains.size() > 0) {
                        ChainReaderHelper helper = new ChainReaderHelper();
                        helper.setProject(this.managingTask.getProject());
                        helper.setPrimaryReader(reader);
                        helper.setFilterChains(this.outputFilterChains);
                        reader = helper.getAssembledReader();
                    }
                    InputStream outPumpIn = new ReaderInputStream(reader, this.outputEncoding);
                    Thread t = new Thread(this.threadGroup, new StreamPumper(outPumpIn, this.outputStream, true), "output pumper");
                    t.setPriority(10);
                    this.outputStream = new PipedOutputStream(snk);
                    t.start();
                } catch (IOException eyeOhEx) {
                    throw new BuildException("error setting up output stream", eyeOhEx);
                }
            }
        }
        synchronized (this.errMutex) {
            errorStreams();
            if (this.alwaysLogErr || this.errorStream == null) {
                OutputStream errorLog = new LogOutputStream(this.managingTask, 1);
                this.errorStream = this.errorStream == null ? errorLog : new TeeOutputStream(errorLog, this.errorStream);
            }
            if ((this.errorFilterChains != null && this.errorFilterChains.size() > 0) || !this.errorEncoding.equalsIgnoreCase(this.inputEncoding)) {
                try {
                    LeadPipeInputStream snk2 = new LeadPipeInputStream();
                    snk2.setManagingComponent(this.managingTask);
                    Reader reader2 = new InputStreamReader(snk2, this.inputEncoding);
                    if (this.errorFilterChains != null && this.errorFilterChains.size() > 0) {
                        ChainReaderHelper helper2 = new ChainReaderHelper();
                        helper2.setProject(this.managingTask.getProject());
                        helper2.setPrimaryReader(reader2);
                        helper2.setFilterChains(this.errorFilterChains);
                        reader2 = helper2.getAssembledReader();
                    }
                    InputStream errPumpIn = new ReaderInputStream(reader2, this.errorEncoding);
                    Thread t2 = new Thread(this.threadGroup, new StreamPumper(errPumpIn, this.errorStream, true), "error pumper");
                    t2.setPriority(10);
                    this.errorStream = new PipedOutputStream(snk2);
                    t2.start();
                } catch (IOException eyeOhEx2) {
                    throw new BuildException("error setting up error stream", eyeOhEx2);
                }
            }
        }
        synchronized (this.inMutex) {
            if (this.input != null && this.input.length > 0) {
                this.managingTask.log("Redirecting input from file" + (this.input.length == 1 ? "" : "s"), 3);
                try {
                    this.inputStream = new ConcatFileInputStream(this.input);
                    ((ConcatFileInputStream) this.inputStream).setManagingComponent(this.managingTask);
                } catch (IOException eyeOhEx3) {
                    throw new BuildException(eyeOhEx3);
                }
            } else if (this.inputString != null) {
                StringBuffer buf = new StringBuffer("Using input ");
                if (this.logInputString) {
                    buf.append('\"').append(this.inputString).append('\"');
                } else {
                    buf.append("string");
                }
                this.managingTask.log(buf.toString(), 3);
                this.inputStream = new ByteArrayInputStream(this.inputString.getBytes());
            }
            if (this.inputStream != null && this.inputFilterChains != null && this.inputFilterChains.size() > 0) {
                ChainReaderHelper helper3 = new ChainReaderHelper();
                helper3.setProject(this.managingTask.getProject());
                try {
                    helper3.setPrimaryReader(new InputStreamReader(this.inputStream, this.inputEncoding));
                    helper3.setFilterChains(this.inputFilterChains);
                    this.inputStream = new ReaderInputStream(helper3.getAssembledReader(), this.inputEncoding);
                } catch (IOException eyeOhEx4) {
                    throw new BuildException("error setting up input stream", eyeOhEx4);
                }
            }
        }
    }

    private void outStreams() {
        boolean haveOutputFiles = this.out != null && this.out.length > 0;
        if (this.discardOut) {
            if (haveOutputFiles || this.outputProperty != null) {
                throw new BuildException("Cant discard output when output or outputProperty are set");
            }
            this.managingTask.log("Discarding output", 3);
            this.outputStream = NullOutputStream.INSTANCE;
            return;
        }
        if (haveOutputFiles) {
            String logHead = "Output " + (this.appendOut ? "appended" : "redirected") + " to ";
            this.outputStream = foldFiles(this.out, logHead, 3, this.appendOut, this.createEmptyFilesOut);
        }
        if (this.outputProperty != null) {
            if (this.baos == null) {
                this.baos = new PropertyOutputStream(this.outputProperty);
                this.managingTask.log("Output redirected to property: " + this.outputProperty, 3);
            }
            OutputStream keepAliveOutput = new KeepAliveOutputStream(this.baos);
            this.outputStream = this.outputStream == null ? keepAliveOutput : new TeeOutputStream(this.outputStream, keepAliveOutput);
            return;
        }
        this.baos = null;
    }

    private void errorStreams() {
        boolean haveErrorFiles = this.error != null && this.error.length > 0;
        if (this.discardErr) {
            if (haveErrorFiles || this.errorProperty != null || this.logError) {
                throw new BuildException("Cant discard error output when error, errorProperty or logError are set");
            }
            this.managingTask.log("Discarding error output", 3);
            this.errorStream = NullOutputStream.INSTANCE;
            return;
        }
        if (haveErrorFiles) {
            String logHead = "Error " + (this.appendErr ? "appended" : "redirected") + " to ";
            this.errorStream = foldFiles(this.error, logHead, 3, this.appendErr, this.createEmptyFilesErr);
        } else if (!this.logError && this.outputStream != null && this.errorProperty == null) {
            OutputStreamFunneler funneler = new OutputStreamFunneler(this.outputStream, 0L);
            try {
                this.outputStream = funneler.getFunnelInstance();
                this.errorStream = funneler.getFunnelInstance();
                if (!this.outputIsBinary) {
                    this.outputStream = new LineOrientedOutputStreamRedirector(this.outputStream);
                    this.errorStream = new LineOrientedOutputStreamRedirector(this.errorStream);
                }
            } catch (IOException eyeOhEx) {
                throw new BuildException("error splitting output/error streams", eyeOhEx);
            }
        }
        if (this.errorProperty != null) {
            if (this.errorBaos == null) {
                this.errorBaos = new PropertyOutputStream(this.errorProperty);
                this.managingTask.log("Error redirected to property: " + this.errorProperty, 3);
            }
            OutputStream keepAliveError = new KeepAliveOutputStream(this.errorBaos);
            this.errorStream = (this.error == null || this.error.length == 0) ? keepAliveError : new TeeOutputStream(this.errorStream, keepAliveError);
            return;
        }
        this.errorBaos = null;
    }

    public ExecuteStreamHandler createHandler() throws BuildException {
        createStreams();
        boolean nonBlockingRead = this.input == null && this.inputString == null;
        return new PumpStreamHandler(getOutputStream(), getErrorStream(), getInputStream(), nonBlockingRead);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleOutput(String output) {
        synchronized (this.outMutex) {
            if (this.outPrintStream == null) {
                this.outPrintStream = new PrintStream(this.outputStream);
            }
            this.outPrintStream.print(output);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int handleInput(byte[] buffer, int offset, int length) throws IOException {
        synchronized (this.inMutex) {
            if (this.inputStream == null) {
                return this.managingTask.getProject().defaultInput(buffer, offset, length);
            }
            return this.inputStream.read(buffer, offset, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleFlush(String output) {
        synchronized (this.outMutex) {
            if (this.outPrintStream == null) {
                this.outPrintStream = new PrintStream(this.outputStream);
            }
            this.outPrintStream.print(output);
            this.outPrintStream.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleErrorOutput(String output) {
        synchronized (this.errMutex) {
            if (this.errorPrintStream == null) {
                this.errorPrintStream = new PrintStream(this.errorStream);
            }
            this.errorPrintStream.print(output);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleErrorFlush(String output) {
        synchronized (this.errMutex) {
            if (this.errorPrintStream == null) {
                this.errorPrintStream = new PrintStream(this.errorStream);
            }
            this.errorPrintStream.print(output);
            this.errorPrintStream.flush();
        }
    }

    public OutputStream getOutputStream() {
        OutputStream outputStream;
        synchronized (this.outMutex) {
            outputStream = this.outputStream;
        }
        return outputStream;
    }

    public OutputStream getErrorStream() {
        OutputStream outputStream;
        synchronized (this.errMutex) {
            outputStream = this.errorStream;
        }
        return outputStream;
    }

    public InputStream getInputStream() {
        InputStream inputStream;
        synchronized (this.inMutex) {
            inputStream = this.inputStream;
        }
        return inputStream;
    }

    public void complete() throws IOException {
        System.out.flush();
        System.err.flush();
        synchronized (this.inMutex) {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
        }
        synchronized (this.outMutex) {
            this.outputStream.flush();
            this.outputStream.close();
        }
        synchronized (this.errMutex) {
            this.errorStream.flush();
            this.errorStream.close();
        }
        synchronized (this) {
            while (this.threadGroup.activeCount() > 0) {
                try {
                    this.managingTask.log("waiting for " + this.threadGroup.activeCount() + " Threads:", 4);
                    Thread[] thread = new Thread[this.threadGroup.activeCount()];
                    this.threadGroup.enumerate(thread);
                    for (int i = 0; i < thread.length && thread[i] != null; i++) {
                        try {
                            this.managingTask.log(thread[i].toString(), 4);
                        } catch (NullPointerException e) {
                        }
                    }
                    wait(1000L);
                } catch (InterruptedException e2) {
                    Thread[] thread2 = new Thread[this.threadGroup.activeCount()];
                    this.threadGroup.enumerate(thread2);
                    for (int i2 = 0; i2 < thread2.length && thread2[i2] != null; i2++) {
                        thread2[i2].interrupt();
                    }
                }
            }
        }
        setProperties();
        synchronized (this.inMutex) {
            this.inputStream = null;
        }
        synchronized (this.outMutex) {
            this.outputStream = null;
            this.outPrintStream = null;
        }
        synchronized (this.errMutex) {
            this.errorStream = null;
            this.errorPrintStream = null;
        }
    }

    public void setProperties() {
        synchronized (this.outMutex) {
            FileUtils.close((OutputStream) this.baos);
        }
        synchronized (this.errMutex) {
            FileUtils.close((OutputStream) this.errorBaos);
        }
    }

    private OutputStream foldFiles(File[] file, String logHead, int loglevel, boolean append, boolean createEmptyFiles) {
        OutputStream result = new LazyFileOutputStream(file[0], append, createEmptyFiles);
        this.managingTask.log(logHead + file[0], loglevel);
        char[] c = new char[logHead.length()];
        Arrays.fill(c, ' ');
        String indent = new String(c);
        for (int i = 1; i < file.length; i++) {
            this.outputStream = new TeeOutputStream(this.outputStream, new LazyFileOutputStream(file[i], append, createEmptyFiles));
            this.managingTask.log(indent + file[i], loglevel);
        }
        return result;
    }
}
