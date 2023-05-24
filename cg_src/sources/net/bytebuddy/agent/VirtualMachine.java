package net.bytebuddy.agent;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.http.cookie.ClientCookie;
import soot.coffi.Instruction;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
/* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine.class */
public interface VirtualMachine {
    Properties getSystemProperties() throws IOException;

    Properties getAgentProperties() throws IOException;

    void loadAgent(String str) throws IOException;

    void loadAgent(String str, String str2) throws IOException;

    void loadAgentPath(String str) throws IOException;

    void loadAgentPath(String str, String str2) throws IOException;

    void loadAgentLibrary(String str) throws IOException;

    void loadAgentLibrary(String str, String str2) throws IOException;

    void startManagementAgent(Properties properties) throws IOException;

    String startLocalManagementAgent() throws IOException;

    void detach() throws IOException;

    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$Resolver.class */
    public enum Resolver implements PrivilegedAction<Class<? extends VirtualMachine>> {
        INSTANCE;

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Class<? extends VirtualMachine> run() {
            try {
                Class.forName("com.sun.jna.Platform");
                return System.getProperty("java.vm.name", "").toUpperCase(Locale.US).contains("J9") ? ForOpenJ9.class : ForHotSpot.class;
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Optional JNA dependency is not available", exception);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$AbstractBase.class */
    public static abstract class AbstractBase implements VirtualMachine {
        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgent(String jarFile) throws IOException {
            loadAgent(jarFile, null);
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgentPath(String path) throws IOException {
            loadAgentPath(path, null);
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgentLibrary(String library) throws IOException {
            loadAgentLibrary(library, null);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot.class */
    public static class ForHotSpot extends AbstractBase {
        private static final String PROTOCOL_VERSION = "1";
        private static final String LOAD_COMMAND = "load";
        private static final String INSTRUMENT_COMMAND = "instrument";
        private static final String ARGUMENT_DELIMITER = "=";
        private final Connection connection;

        protected ForHotSpot(Connection connection) {
            this.connection = connection;
        }

        public static VirtualMachine attach(String processId) throws IOException {
            if (Platform.isWindows()) {
                return attach(processId, new Connection.ForJnaWindowsNamedPipe.Factory());
            }
            if (Platform.isSolaris()) {
                return attach(processId, new Connection.ForJnaSolarisDoor.Factory(15, 100L, TimeUnit.MILLISECONDS));
            }
            return attach(processId, Connection.ForJnaPosixSocket.Factory.withDefaultTemporaryFolder(15, 100L, TimeUnit.MILLISECONDS));
        }

        public static VirtualMachine attach(String processId, Connection.Factory connectionFactory) throws IOException {
            return new ForHotSpot(connectionFactory.connect(processId));
        }

        private static void checkHeader(Connection.Response response) throws IOException {
            byte[] buffer = new byte[1];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while (true) {
                int length = response.read(buffer);
                if (length == -1) {
                    break;
                } else if (length > 0) {
                    if (buffer[0] == 10) {
                        break;
                    }
                    outputStream.write(buffer[0]);
                }
            }
            switch (Integer.parseInt(outputStream.toString("UTF-8"))) {
                case 0:
                    return;
                case 101:
                    throw new IOException("Protocol mismatch with target VM");
                default:
                    byte[] buffer2 = new byte[1024];
                    ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
                    while (true) {
                        int length2 = response.read(buffer2);
                        if (length2 != -1) {
                            outputStream2.write(buffer2, 0, length2);
                        } else {
                            throw new IllegalStateException(outputStream2.toString("UTF-8"));
                        }
                    }
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public Properties getSystemProperties() throws IOException {
            return getProperties("properties");
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public Properties getAgentProperties() throws IOException {
            return getProperties("agentProperties");
        }

        private Properties getProperties(String command) throws IOException {
            Connection.Response response = this.connection.execute("1", command, null, null, null);
            try {
                checkHeader(response);
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                while (true) {
                    int length = response.read(buffer);
                    if (length != -1) {
                        outputStream.write(buffer, 0, length);
                    } else {
                        Properties properties = new Properties();
                        properties.load(new ByteArrayInputStream(outputStream.toByteArray()));
                        response.close();
                        return properties;
                    }
                }
            } catch (Throwable th) {
                response.close();
                throw th;
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgent(String jarFile, String argument) throws IOException {
            load(jarFile, false, argument);
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgentPath(String path, String argument) throws IOException {
            load(path, true, argument);
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgentLibrary(String library, String argument) throws IOException {
            load(library, false, argument);
        }

        protected void load(String file, boolean absolute, String argument) throws IOException {
            Connection connection = this.connection;
            String[] strArr = new String[4];
            strArr[0] = LOAD_COMMAND;
            strArr[1] = INSTRUMENT_COMMAND;
            strArr[2] = Boolean.toString(absolute);
            strArr[3] = argument == null ? file : file + ARGUMENT_DELIMITER + argument;
            Connection.Response response = connection.execute("1", strArr);
            try {
                checkHeader(response);
                response.close();
            } catch (Throwable th) {
                response.close();
                throw th;
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void startManagementAgent(Properties properties) throws IOException {
            StringBuilder stringBuilder = new StringBuilder("ManagementAgent.start ");
            boolean first = true;
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                if (!(entry.getKey() instanceof String) || !((String) entry.getKey()).startsWith("com.sun.management.")) {
                    throw new IllegalArgumentException("Illegal property name: " + entry.getKey());
                }
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(((String) entry.getKey()).substring("com.sun.management.".length())).append('=');
                String value = entry.getValue().toString();
                if (value.contains(Instruction.argsep)) {
                    stringBuilder.append('\'').append(value).append('\'');
                } else {
                    stringBuilder.append(value);
                }
            }
            Connection.Response response = this.connection.execute("1", "jcmd", stringBuilder.toString(), null, null);
            try {
                checkHeader(response);
                response.close();
            } catch (Throwable th) {
                response.close();
                throw th;
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public String startLocalManagementAgent() throws IOException {
            Connection.Response response = this.connection.execute("1", "jcmd", "ManagementAgent.start_local", null, null);
            try {
                checkHeader(response);
                return getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
            } finally {
                response.close();
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void detach() throws IOException {
            this.connection.close();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection.class */
        public interface Connection extends Closeable {

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$Response.class */
            public interface Response extends Closeable {
                int read(byte[] bArr) throws IOException;
            }

            Response execute(String str, String... strArr) throws IOException;

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$Factory.class */
            public interface Factory {
                Connection connect(String str) throws IOException;

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$Factory$ForSocketFile.class */
                public static abstract class ForSocketFile implements Factory {
                    private static final String SOCKET_FILE_PREFIX = ".java_pid";
                    private static final String ATTACH_FILE_PREFIX = ".attach_pid";
                    private final String temporaryDirectory;
                    private final int attempts;
                    private final long pause;
                    private final TimeUnit timeUnit;

                    protected abstract void kill(String str, int i);

                    protected abstract Connection doConnect(File file) throws IOException;

                    protected ForSocketFile(String temporaryDirectory, int attempts, long pause, TimeUnit timeUnit) {
                        this.temporaryDirectory = temporaryDirectory;
                        this.attempts = attempts;
                        this.pause = pause;
                        this.timeUnit = timeUnit;
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Factory
                    @SuppressFBWarnings(value = {"DMI_HARDCODED_ABSOLUTE_FILENAME"}, justification = "File name convention is specified.")
                    public Connection connect(String processId) throws IOException {
                        File socket = new File(this.temporaryDirectory, SOCKET_FILE_PREFIX + processId);
                        if (!socket.exists()) {
                            String target = ATTACH_FILE_PREFIX + processId;
                            String path = "/proc/" + processId + "/cwd/" + target;
                            File attachFile = new File(path);
                            try {
                            } catch (IOException e) {
                                attachFile = new File(this.temporaryDirectory, target);
                                if (!attachFile.createNewFile() && !attachFile.isFile()) {
                                    throw new IllegalStateException("Could not create attach file: " + attachFile);
                                }
                            }
                            if (!attachFile.createNewFile() && !attachFile.isFile()) {
                                throw new IllegalStateException("Could not create attach file: " + attachFile);
                            }
                            try {
                                try {
                                    kill(processId, 3);
                                    int attempts = this.attempts;
                                    while (!socket.exists()) {
                                        int i = attempts;
                                        attempts--;
                                        if (i <= 0) {
                                            break;
                                        }
                                        this.timeUnit.sleep(this.pause);
                                    }
                                    if (!socket.exists()) {
                                        throw new IllegalStateException("Target VM did not respond: " + processId);
                                    }
                                } catch (InterruptedException exception) {
                                    Thread.currentThread().interrupt();
                                    throw new IllegalStateException(exception);
                                }
                            } finally {
                                if (!attachFile.delete()) {
                                    attachFile.deleteOnExit();
                                }
                            }
                        }
                        return doConnect(socket);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$OnPersistentByteChannel.class */
            public static abstract class OnPersistentByteChannel<T> implements Connection {
                private static final byte[] BLANK = {0};

                protected abstract T connect() throws IOException;

                protected abstract void close(T t) throws IOException;

                protected abstract void write(T t, byte[] bArr) throws IOException;

                protected abstract int read(T t, byte[] bArr) throws IOException;

                @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection
                public Response execute(String protocol, String... argument) throws IOException {
                    T connection = connect();
                    try {
                        write(connection, protocol.getBytes("UTF-8"));
                        write(connection, BLANK);
                        for (String anArgument : argument) {
                            if (anArgument != null) {
                                write(connection, anArgument.getBytes("UTF-8"));
                            }
                            write(connection, BLANK);
                        }
                        return new Response(connection);
                    } catch (Throwable throwable) {
                        close(connection);
                        if (throwable instanceof RuntimeException) {
                            throw ((RuntimeException) throwable);
                        }
                        if (throwable instanceof IOException) {
                            throw ((IOException) throwable);
                        }
                        throw new IllegalStateException(throwable);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$OnPersistentByteChannel$Response.class */
                private class Response implements Response {
                    private final T connection;

                    private Response(T connection) {
                        this.connection = connection;
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response
                    public int read(byte[] buffer) throws IOException {
                        return OnPersistentByteChannel.this.read(this.connection, buffer);
                    }

                    @Override // java.io.Closeable, java.lang.AutoCloseable
                    public void close() throws IOException {
                        OnPersistentByteChannel.this.close(this.connection);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaPosixSocket.class */
            public static class ForJnaPosixSocket extends OnPersistentByteChannel<Integer> {
                private final PosixLibrary library;
                private final File socket;

                protected ForJnaPosixSocket(PosixLibrary library, File socket) {
                    this.library = library;
                    this.socket = socket;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.OnPersistentByteChannel
                public Integer connect() {
                    int handle = this.library.socket(1, 1, 0);
                    try {
                        PosixLibrary.SocketAddress address = new PosixLibrary.SocketAddress();
                        address.setPath(this.socket.getAbsolutePath());
                        this.library.connect(handle, address, address.size());
                        return Integer.valueOf(handle);
                    } catch (RuntimeException exception) {
                        this.library.close(handle);
                        throw exception;
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.OnPersistentByteChannel
                public int read(Integer handle, byte[] buffer) {
                    int read = this.library.read(handle.intValue(), ByteBuffer.wrap(buffer), buffer.length);
                    if (read == 0) {
                        return -1;
                    }
                    return read;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.OnPersistentByteChannel
                public void write(Integer handle, byte[] buffer) {
                    this.library.write(handle.intValue(), ByteBuffer.wrap(buffer), buffer.length);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.OnPersistentByteChannel
                public void close(Integer handle) {
                    this.library.close(handle.intValue());
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaPosixSocket$PosixLibrary.class */
                public interface PosixLibrary extends Library {
                    int kill(int i, int i2) throws LastErrorException;

                    int socket(int i, int i2, int i3) throws LastErrorException;

                    int connect(int i, SocketAddress socketAddress, int i2) throws LastErrorException;

                    int read(int i, ByteBuffer byteBuffer, int i2) throws LastErrorException;

                    int write(int i, ByteBuffer byteBuffer, int i2) throws LastErrorException;

                    int close(int i) throws LastErrorException;

                    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaPosixSocket$PosixLibrary$SocketAddress.class */
                    public static class SocketAddress extends Structure {
                        @SuppressFBWarnings(value = {"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"}, justification = "Field required by native implementation.")
                        public short family = 1;
                        public byte[] path = new byte[100];

                        protected void setPath(String path) {
                            try {
                                System.arraycopy(path.getBytes("UTF-8"), 0, this.path, 0, path.length());
                                System.arraycopy(new byte[]{0}, 0, this.path, path.length(), 1);
                            } catch (UnsupportedEncodingException exception) {
                                throw new IllegalStateException(exception);
                            }
                        }

                        protected List<String> getFieldOrder() {
                            return Arrays.asList("family", ClientCookie.PATH_ATTR);
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaPosixSocket$Factory.class */
                public static class Factory extends Factory.ForSocketFile {
                    private final PosixLibrary library;

                    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaPosixSocket$Factory$MacLibrary.class */
                    public interface MacLibrary extends Library {
                        public static final int CS_DARWIN_USER_TEMP_DIR = 65537;

                        long confstr(int i, Pointer pointer, long j);
                    }

                    public Factory(String temporaryDirectory, int attempts, long pause, TimeUnit timeUnit) {
                        super(temporaryDirectory, attempts, pause, timeUnit);
                        this.library = (PosixLibrary) Native.loadLibrary("c", PosixLibrary.class);
                    }

                    public static Factory withDefaultTemporaryFolder(int attempts, long pause, TimeUnit timeUnit) {
                        String temporaryDirectory;
                        if (Platform.isMac()) {
                            MacLibrary library = (MacLibrary) Native.loadLibrary("c", MacLibrary.class);
                            Memory memory = new Memory(4096L);
                            try {
                                long length = library.confstr(MacLibrary.CS_DARWIN_USER_TEMP_DIR, memory, memory.size());
                                if (length == 0 || length > 4096) {
                                    temporaryDirectory = "/tmp";
                                } else {
                                    temporaryDirectory = memory.getString(0L);
                                }
                            } finally {
                            }
                        } else {
                            temporaryDirectory = "/tmp";
                        }
                        return new Factory(temporaryDirectory, attempts, pause, timeUnit);
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Factory.ForSocketFile
                    protected void kill(String processId, int signal) {
                        this.library.kill(Integer.parseInt(processId), signal);
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Factory.ForSocketFile
                    public Connection doConnect(File socket) {
                        return new ForJnaPosixSocket(this.library, socket);
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaWindowsNamedPipe.class */
            public static class ForJnaWindowsNamedPipe implements Connection {
                private static final int MEM_RELEASE = 32768;
                private final WindowsLibrary library;
                private final WindowsAttachLibrary attachLibrary;
                private final WinNT.HANDLE process;
                private final WinDef.LPVOID code;
                private final SecureRandom random = new SecureRandom();

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaWindowsNamedPipe$WindowsAttachLibrary.class */
                protected interface WindowsAttachLibrary extends StdCallLibrary {
                    WinDef.LPVOID allocate_remote_code(WinNT.HANDLE handle);

                    WinDef.LPVOID allocate_remote_argument(WinNT.HANDLE handle, String str, String str2, String str3, String str4, String str5);
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaWindowsNamedPipe$WindowsLibrary.class */
                protected interface WindowsLibrary extends StdCallLibrary {
                    Pointer VirtualAllocEx(WinNT.HANDLE handle, Pointer pointer, int i, int i2, int i3);

                    boolean VirtualFreeEx(WinNT.HANDLE handle, Pointer pointer, int i, int i2);

                    WinNT.HANDLE CreateRemoteThread(WinNT.HANDLE handle, WinBase.SECURITY_ATTRIBUTES security_attributes, int i, Pointer pointer, Pointer pointer2, WinDef.DWORD dword, Pointer pointer3);

                    boolean GetExitCodeThread(WinNT.HANDLE handle, IntByReference intByReference);
                }

                protected ForJnaWindowsNamedPipe(WindowsLibrary library, WindowsAttachLibrary attachLibrary, WinNT.HANDLE process, WinDef.LPVOID code) {
                    this.library = library;
                    this.attachLibrary = attachLibrary;
                    this.process = process;
                    this.code = code;
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection
                public Response execute(String protocol, String... argument) {
                    int code;
                    if (!"1".equals(protocol)) {
                        throw new IllegalArgumentException("Unknown protocol version: " + protocol);
                    }
                    if (argument.length > 4) {
                        throw new IllegalArgumentException("Cannot supply more then four arguments to Windows attach mechanism: " + Arrays.asList(argument));
                    }
                    String name = "\\\\.\\pipe\\javatool" + Math.abs(this.random.nextInt() + 1);
                    WinNT.HANDLE pipe = Kernel32.INSTANCE.CreateNamedPipe(name, 1, 0, 1, 4096, 8192, 0, (WinBase.SECURITY_ATTRIBUTES) null);
                    if (pipe == null) {
                        throw new Win32Exception(Native.getLastError());
                    }
                    try {
                        WinDef.LPVOID data = this.attachLibrary.allocate_remote_argument(this.process, name, argument.length < 1 ? null : argument[0], argument.length < 2 ? null : argument[1], argument.length < 3 ? null : argument[2], argument.length < 4 ? null : argument[3]);
                        if (data == null) {
                            throw new Win32Exception(Native.getLastError());
                        }
                        WinNT.HANDLE thread = this.library.CreateRemoteThread(this.process, null, 0, this.code.getPointer(), data.getPointer(), null, null);
                        if (thread == null) {
                            throw new Win32Exception(Native.getLastError());
                        }
                        try {
                            int result = Kernel32.INSTANCE.WaitForSingleObject(thread, -1);
                            if (result != 0) {
                                throw new Win32Exception(result);
                            }
                            IntByReference exitCode = new IntByReference();
                            if (!this.library.GetExitCodeThread(thread, exitCode)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                            if (exitCode.getValue() != 0) {
                                throw new IllegalStateException("Target could not dispatch command successfully");
                            }
                            if (!Kernel32.INSTANCE.ConnectNamedPipe(pipe, (WinBase.OVERLAPPED) null) && (code = Native.getLastError()) != 535) {
                                throw new Win32Exception(code);
                            }
                            NamedPipeResponse namedPipeResponse = new NamedPipeResponse(pipe);
                            if (!Kernel32.INSTANCE.CloseHandle(thread)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                            if (this.library.VirtualFreeEx(this.process, data.getPointer(), 0, 32768)) {
                                return namedPipeResponse;
                            }
                            throw new Win32Exception(Native.getLastError());
                        } catch (Throwable th) {
                            if (!Kernel32.INSTANCE.CloseHandle(thread)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                            throw th;
                        }
                    } catch (Throwable throwable) {
                        if (!Kernel32.INSTANCE.CloseHandle(pipe)) {
                            throw new Win32Exception(Native.getLastError());
                        }
                        if (throwable instanceof RuntimeException) {
                            throw ((RuntimeException) throwable);
                        }
                        throw new IllegalStateException(throwable);
                    }
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                    try {
                        if (!this.library.VirtualFreeEx(this.process, this.code.getPointer(), 0, 32768)) {
                            throw new Win32Exception(Native.getLastError());
                        }
                        if (!Kernel32.INSTANCE.CloseHandle(this.process)) {
                            throw new Win32Exception(Native.getLastError());
                        }
                    } catch (Throwable th) {
                        if (!Kernel32.INSTANCE.CloseHandle(this.process)) {
                            throw new Win32Exception(Native.getLastError());
                        }
                        throw th;
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaWindowsNamedPipe$NamedPipeResponse.class */
                protected static class NamedPipeResponse implements Response {
                    private final WinNT.HANDLE pipe;

                    protected NamedPipeResponse(WinNT.HANDLE pipe) {
                        this.pipe = pipe;
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response
                    public int read(byte[] buffer) {
                        IntByReference read = new IntByReference();
                        if (!Kernel32.INSTANCE.ReadFile(this.pipe, buffer, buffer.length, read, (WinBase.OVERLAPPED) null)) {
                            int code = Native.getLastError();
                            if (code == 109) {
                                return -1;
                            }
                            throw new Win32Exception(code);
                        }
                        return read.getValue();
                    }

                    @Override // java.io.Closeable, java.lang.AutoCloseable
                    public void close() {
                        try {
                            if (!Kernel32.INSTANCE.DisconnectNamedPipe(this.pipe)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                            if (!Kernel32.INSTANCE.CloseHandle(this.pipe)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                        } catch (Throwable th) {
                            if (!Kernel32.INSTANCE.CloseHandle(this.pipe)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                            throw th;
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaWindowsNamedPipe$Factory.class */
                public static class Factory implements Factory {
                    public static final String LIBRARY_NAME = "net.bytebuddy.library.name";
                    private final WindowsLibrary library = (WindowsLibrary) Native.loadLibrary("kernel32", WindowsLibrary.class, W32APIOptions.DEFAULT_OPTIONS);
                    private final WindowsAttachLibrary attachLibrary = (WindowsAttachLibrary) Native.loadLibrary(System.getProperty(LIBRARY_NAME, "attach_hotspot_windows"), WindowsAttachLibrary.class);

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Factory
                    public Connection connect(String processId) {
                        WinNT.HANDLE process = Kernel32.INSTANCE.OpenProcess(2039803, false, Integer.parseInt(processId));
                        if (process == null) {
                            throw new Win32Exception(Native.getLastError());
                        }
                        try {
                            WinDef.LPVOID code = this.attachLibrary.allocate_remote_code(process);
                            if (code == null) {
                                throw new Win32Exception(Native.getLastError());
                            }
                            return new ForJnaWindowsNamedPipe(this.library, this.attachLibrary, process, code);
                        } catch (Throwable throwable) {
                            if (!Kernel32.INSTANCE.CloseHandle(process)) {
                                throw new Win32Exception(Native.getLastError());
                            }
                            if (throwable instanceof RuntimeException) {
                                throw ((RuntimeException) throwable);
                            }
                            throw new IllegalStateException(throwable);
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaSolarisDoor.class */
            public static class ForJnaSolarisDoor implements Connection {
                private final SolarisLibrary library;
                private final File socket;

                protected ForJnaSolarisDoor(SolarisLibrary library, File socket) {
                    this.library = library;
                    this.socket = socket;
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection
                @SuppressFBWarnings(value = {"UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD", "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"}, justification = "This pattern is required for use of JNA.")
                public Response execute(String protocol, String... argument) throws IOException {
                    int handle = this.library.open(this.socket.getAbsolutePath(), 2);
                    try {
                        SolarisLibrary.DoorArgument door = new SolarisLibrary.DoorArgument();
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        outputStream.write(protocol.getBytes("UTF-8"));
                        outputStream.write(0);
                        for (String anArgument : argument) {
                            if (anArgument != null) {
                                outputStream.write(anArgument.getBytes("UTF-8"));
                            }
                            outputStream.write(0);
                        }
                        door.dataSize = outputStream.size();
                        Memory dataPointer = new Memory(outputStream.size());
                        try {
                            dataPointer.write(0L, outputStream.toByteArray(), 0, outputStream.size());
                            door.dataPointer = dataPointer;
                            Memory result = new Memory(128L);
                            try {
                                door.resultPointer = result;
                                door.resultSize = (int) result.size();
                                if (this.library.door_call(handle, door) != 0) {
                                    throw new IllegalStateException("Door call to target VM failed");
                                }
                                if (door.resultSize < 4 || door.resultPointer.getInt(0L) != 0) {
                                    throw new IllegalStateException("Target VM could not execute door call");
                                }
                                if (door.descriptorCount != 1 || door.descriptorPointer == null) {
                                    throw new IllegalStateException("Did not receive communication descriptor from target VM");
                                }
                                return new Response(this.library, door.descriptorPointer.getInt(4L));
                            } finally {
                            }
                        } finally {
                        }
                    } finally {
                        this.library.close(handle);
                    }
                }

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaSolarisDoor$SolarisLibrary.class */
                protected interface SolarisLibrary extends Library {
                    int kill(int i, int i2) throws LastErrorException;

                    int open(String str, int i) throws LastErrorException;

                    int read(int i, ByteBuffer byteBuffer, int i2) throws LastErrorException;

                    int close(int i) throws LastErrorException;

                    int door_call(int i, DoorArgument doorArgument) throws LastErrorException;

                    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaSolarisDoor$SolarisLibrary$DoorArgument.class */
                    public static class DoorArgument extends Structure {
                        public Pointer dataPointer;
                        public int dataSize;
                        public Pointer descriptorPointer;
                        public int descriptorCount;
                        public Pointer resultPointer;
                        public int resultSize;

                        protected List<String> getFieldOrder() {
                            return Arrays.asList("dataPointer", "dataSize", "descriptorPointer", "descriptorCount", "resultPointer", "resultSize");
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaSolarisDoor$Response.class */
                protected static class Response implements Response {
                    private final SolarisLibrary library;
                    private final int handle;

                    protected Response(SolarisLibrary library, int handle) {
                        this.library = library;
                        this.handle = handle;
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response
                    public int read(byte[] buffer) {
                        int read = this.library.read(this.handle, ByteBuffer.wrap(buffer), buffer.length);
                        if (read == 0) {
                            return -1;
                        }
                        return read;
                    }

                    @Override // java.io.Closeable, java.lang.AutoCloseable
                    public void close() {
                        this.library.close(this.handle);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForHotSpot$Connection$ForJnaSolarisDoor$Factory.class */
                public static class Factory extends Factory.ForSocketFile {
                    private final SolarisLibrary library;

                    public Factory(int attempts, long pause, TimeUnit timeUnit) {
                        super("/tmp", attempts, pause, timeUnit);
                        this.library = (SolarisLibrary) Native.loadLibrary("c", SolarisLibrary.class);
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Factory.ForSocketFile
                    protected void kill(String processId, int signal) {
                        this.library.kill(Integer.parseInt(processId), signal);
                    }

                    @Override // net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Factory.ForSocketFile
                    protected Connection doConnect(File socket) {
                        return new ForJnaSolarisDoor(this.library, socket);
                    }
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9.class */
    public static class ForOpenJ9 extends AbstractBase {
        private static final String IBM_TEMPORARY_FOLDER = "com.ibm.tools.attach.directory";
        private final Socket socket;

        protected ForOpenJ9(Socket socket) {
            this.socket = socket;
        }

        public static VirtualMachine attach(String processId) throws IOException {
            return attach(processId, 5000, Platform.isWindows() ? new Dispatcher.ForJnaWindowsEnvironment() : new Dispatcher.ForJnaPosixEnvironment(15, 100L, TimeUnit.MILLISECONDS));
        }

        /* JADX WARN: Finally extract failed */
        /* JADX WARN: Incorrect condition in loop: B:159:0x04f2 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static net.bytebuddy.agent.VirtualMachine attach(java.lang.String r7, int r8, net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher r9) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 1387
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: net.bytebuddy.agent.VirtualMachine.ForOpenJ9.attach(java.lang.String, int, net.bytebuddy.agent.VirtualMachine$ForOpenJ9$Dispatcher):net.bytebuddy.agent.VirtualMachine");
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public Properties getSystemProperties() throws IOException {
            write(this.socket, "ATTACH_GETSYSTEMPROPERTIES".getBytes("UTF-8"));
            Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(read(this.socket)));
            return properties;
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public Properties getAgentProperties() throws IOException {
            write(this.socket, "ATTACH_GETAGENTPROPERTIES".getBytes("UTF-8"));
            Properties properties = new Properties();
            properties.load(new ByteArrayInputStream(read(this.socket)));
            return properties;
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgent(String jarFile, String argument) throws IOException {
            write(this.socket, ("ATTACH_LOADAGENT(instrument," + jarFile + '=' + (argument == null ? "" : argument) + ')').getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM failed loading agent: " + answer);
            }
            if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgentPath(String path, String argument) throws IOException {
            write(this.socket, ("ATTACH_LOADAGENTPATH(" + path + (argument == null ? "" : ',' + argument) + ')').getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM failed loading native agent: " + answer);
            }
            if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void loadAgentLibrary(String library, String argument) throws IOException {
            write(this.socket, ("ATTACH_LOADAGENTLIBRARY(" + library + (argument == null ? "" : ',' + argument) + ')').getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM failed loading native library: " + answer);
            }
            if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void startManagementAgent(Properties properties) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            properties.store(outputStream, (String) null);
            write(this.socket, "ATTACH_START_MANAGEMENT_AGENT".getBytes("UTF-8"));
            write(this.socket, outputStream.toByteArray());
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM could not start management agent: " + answer);
            }
            if (!answer.startsWith("ATTACH_ACK") && !answer.startsWith("ATTACH_RESULT=")) {
                throw new IllegalStateException("Unexpected response: " + answer);
            }
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public String startLocalManagementAgent() throws IOException {
            write(this.socket, "ATTACH_START_LOCAL_MANAGEMENT_AGENT".getBytes("UTF-8"));
            String answer = new String(read(this.socket), "UTF-8");
            if (answer.startsWith("ATTACH_ERR")) {
                throw new IllegalStateException("Target VM could not start management agent: " + answer);
            }
            if (answer.startsWith("ATTACH_ACK")) {
                return answer.substring("ATTACH_ACK".length());
            }
            if (answer.startsWith("ATTACH_RESULT=")) {
                return answer.substring("ATTACH_RESULT=".length());
            }
            throw new IllegalStateException("Unexpected response: " + answer);
        }

        @Override // net.bytebuddy.agent.VirtualMachine
        public void detach() throws IOException {
            try {
                write(this.socket, "ATTACH_DETACH".getBytes("UTF-8"));
                read(this.socket);
            } finally {
                this.socket.close();
            }
        }

        private static void write(Socket socket, byte[] value) throws IOException {
            socket.getOutputStream().write(value);
            socket.getOutputStream().write(0);
            socket.getOutputStream().flush();
        }

        private static byte[] read(Socket socket) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int length = socket.getInputStream().read(buffer);
                if (length != -1) {
                    if (length > 0 && buffer[length - 1] == 0) {
                        outputStream.write(buffer, 0, length - 1);
                        break;
                    }
                    outputStream.write(buffer, 0, length);
                } else {
                    break;
                }
            }
            return outputStream.toByteArray();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher.class */
        public interface Dispatcher {
            String getTemporaryFolder();

            int pid();

            int userId();

            boolean isExistingProcess(int i);

            int getOwnerIdOf(File file);

            void setPermissions(File file, int i);

            void incrementSemaphore(File file, String str, boolean z, int i);

            void decrementSemaphore(File file, String str, boolean z, int i);

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher$ForJnaPosixEnvironment.class */
            public static class ForJnaPosixEnvironment implements Dispatcher {
                private final PosixLibrary library = (PosixLibrary) Native.loadLibrary("c", PosixLibrary.class);
                private final int attempts;
                private final long pause;
                private final TimeUnit timeUnit;

                public ForJnaPosixEnvironment(int attempts, long pause, TimeUnit timeUnit) {
                    this.attempts = attempts;
                    this.pause = pause;
                    this.timeUnit = timeUnit;
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public String getTemporaryFolder() {
                    String temporaryFolder = System.getenv("TMPDIR");
                    return temporaryFolder == null ? "/tmp" : temporaryFolder;
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public int pid() {
                    return this.library.getpid();
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public int userId() {
                    return this.library.getuid();
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public boolean isExistingProcess(int processId) {
                    return this.library.kill(processId, 0) != 3;
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                @SuppressFBWarnings(value = {"OS_OPEN_STREAM"}, justification = "The stream life-cycle is bound to its process.")
                public int getOwnerIdOf(File file) {
                    try {
                        String statUserSwitch = Platform.isMac() ? "-f" : "-c";
                        Process process = Runtime.getRuntime().exec("stat " + statUserSwitch + " %u " + file.getAbsolutePath());
                        int attempts = this.attempts;
                        boolean exited = false;
                        String line = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8")).readLine();
                        while (true) {
                            try {
                                if (process.exitValue() != 0) {
                                    throw new IllegalStateException("Error while executing stat");
                                    break;
                                }
                                exited = true;
                                break;
                            } catch (IllegalThreadStateException e) {
                                try {
                                    Thread.sleep(this.timeUnit.toMillis(this.pause));
                                    attempts--;
                                    if (attempts <= 0) {
                                        break;
                                    }
                                } catch (InterruptedException exception) {
                                    Thread.currentThread().interrupt();
                                    throw new IllegalStateException(exception);
                                }
                            }
                        }
                        if (!exited) {
                            process.destroy();
                            throw new IllegalStateException("Command for stat did not exit in time");
                        }
                        return Integer.parseInt(line);
                    } catch (IOException exception2) {
                        throw new IllegalStateException("Unable to execute stat command", exception2);
                    }
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public void setPermissions(File file, int permissions) {
                    this.library.chmod(file.getAbsolutePath(), permissions);
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public void incrementSemaphore(File directory, String name, boolean global, int count) {
                    notifySemaphore(directory, name, count, (short) 1, (short) 0, false);
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public void decrementSemaphore(File directory, String name, boolean global, int count) {
                    notifySemaphore(directory, name, count, (short) -1, (short) 6144, true);
                }

                @SuppressFBWarnings(value = {"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD"}, justification = "Modifier is required by JNA.")
                private void notifySemaphore(File directory, String name, int count, short operation, short flags, boolean acceptUnavailable) {
                    int semaphore = this.library.semget(this.library.ftok(new File(directory, name).getAbsolutePath(), 161), 2, 438);
                    PosixLibrary.SemaphoreOperation target = new PosixLibrary.SemaphoreOperation();
                    target.operation = operation;
                    target.flags = flags;
                    while (true) {
                        try {
                            int i = count;
                            count--;
                            if (i <= 0) {
                                break;
                            }
                            try {
                                this.library.semop(semaphore, target, 1);
                            } catch (LastErrorException exception) {
                                if (!acceptUnavailable || (Native.getLastError() != 11 && Native.getLastError() != 35)) {
                                    throw exception;
                                }
                            }
                        } finally {
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher$ForJnaPosixEnvironment$PosixLibrary.class */
                public interface PosixLibrary extends Library {
                    public static final int NULL_SIGNAL = 0;
                    public static final int ESRCH = 3;
                    public static final int EAGAIN = 11;
                    public static final int EDEADLK = 35;
                    public static final short SEM_UNDO = 4096;
                    public static final short IPC_NOWAIT = 2048;

                    int getpid() throws LastErrorException;

                    int getuid() throws LastErrorException;

                    int kill(int i, int i2) throws LastErrorException;

                    int chmod(String str, int i) throws LastErrorException;

                    int ftok(String str, int i) throws LastErrorException;

                    int semget(int i, int i2, int i3) throws LastErrorException;

                    int semop(int i, SemaphoreOperation semaphoreOperation, int i2) throws LastErrorException;

                    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher$ForJnaPosixEnvironment$PosixLibrary$SemaphoreOperation.class */
                    public static class SemaphoreOperation extends Structure {
                        public short number;
                        public short operation;
                        public short flags;

                        protected List<String> getFieldOrder() {
                            return Arrays.asList("number", "operation", "flags");
                        }
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher$ForJnaWindowsEnvironment.class */
            public static class ForJnaWindowsEnvironment implements Dispatcher {
                private static final int NO_USER_ID = 0;
                private static final String CREATION_MUTEX_NAME = "j9shsemcreationMutex";
                private final WindowsLibrary library = (WindowsLibrary) Native.loadLibrary("kernel32", WindowsLibrary.class, W32APIOptions.DEFAULT_OPTIONS);

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public String getTemporaryFolder() {
                    WinDef.DWORD length = new WinDef.DWORD(260L);
                    char[] path = new char[length.intValue()];
                    if (Kernel32.INSTANCE.GetTempPath(length, path).intValue() == 0) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    }
                    return Native.toString(path);
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public int pid() {
                    return Kernel32.INSTANCE.GetCurrentProcessId();
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public int userId() {
                    return 0;
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public boolean isExistingProcess(int processId) {
                    WinNT.HANDLE handle = Kernel32.INSTANCE.OpenProcess(1024, false, processId);
                    if (handle == null) {
                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                    }
                    IntByReference exists = new IntByReference();
                    if (Kernel32.INSTANCE.GetExitCodeProcess(handle, exists)) {
                        return exists.getValue() == 259;
                    }
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public int getOwnerIdOf(File file) {
                    return 0;
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public void setPermissions(File file, int permissions) {
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public void incrementSemaphore(File directory, String name, boolean global, int count) {
                    AttachmentHandle handle = openSemaphore(directory, name, global);
                    do {
                        try {
                            int i = count;
                            count--;
                            if (i <= 0) {
                                return;
                            }
                        } finally {
                            handle.close();
                        }
                    } while (this.library.ReleaseSemaphore(handle.getHandle(), 1L, null));
                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                }

                @Override // net.bytebuddy.agent.VirtualMachine.ForOpenJ9.Dispatcher
                public void decrementSemaphore(File directory, String name, boolean global, int count) {
                    AttachmentHandle handle = openSemaphore(directory, name, global);
                    while (true) {
                        try {
                            int i = count;
                            count--;
                            if (i > 0) {
                                int result = Kernel32.INSTANCE.WaitForSingleObject(handle.getHandle(), 0);
                                switch (result) {
                                    case 0:
                                    case 128:
                                    case 258:
                                        return;
                                    default:
                                        throw new Win32Exception(result);
                                }
                            } else {
                                handle.close();
                                return;
                            }
                        } finally {
                            handle.close();
                        }
                    }
                }

                private AttachmentHandle openSemaphore(File directory, String name, boolean global) {
                    WinNT.SECURITY_DESCRIPTOR securityDescriptor = new WinNT.SECURITY_DESCRIPTOR(65536);
                    try {
                        if (!Advapi32.INSTANCE.InitializeSecurityDescriptor(securityDescriptor, 1)) {
                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                        }
                        if (!Advapi32.INSTANCE.SetSecurityDescriptorDacl(securityDescriptor, true, (WinNT.ACL) null, true)) {
                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                        }
                        WindowsLibrary.SecurityAttributes securityAttributes = new WindowsLibrary.SecurityAttributes();
                        securityAttributes.length = new WinDef.DWORD(securityAttributes.size());
                        securityAttributes.securityDescriptor = securityDescriptor.getPointer();
                        WinNT.HANDLE mutex = this.library.CreateMutex(securityAttributes, false, CREATION_MUTEX_NAME);
                        if (mutex == null) {
                            int lastError = Kernel32.INSTANCE.GetLastError();
                            if (lastError == 183) {
                                mutex = this.library.OpenMutex(2031617, false, CREATION_MUTEX_NAME);
                                if (mutex == null) {
                                    throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                }
                            } else {
                                throw new Win32Exception(lastError);
                            }
                        }
                        int result = Kernel32.INSTANCE.WaitForSingleObject(mutex, 2000);
                        switch (result) {
                            case -1:
                            case 258:
                                throw new Win32Exception(result);
                            default:
                                try {
                                    String target = (global ? "Global\\" : "") + (directory.getAbsolutePath() + '_' + name).replaceAll("[^a-zA-Z0-9_]", "") + "_semaphore";
                                    WinNT.HANDLE parent = this.library.OpenSemaphoreW(WindowsLibrary.SEMAPHORE_ALL_ACCESS, false, target);
                                    if (parent == null) {
                                        WinNT.HANDLE parent2 = this.library.CreateSemaphoreW(null, 0L, 2147483647L, target);
                                        if (parent2 == null) {
                                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                        }
                                        WinNT.HANDLE child = this.library.CreateSemaphoreW(null, 0L, 2147483647L, target + "_set0");
                                        if (child == null) {
                                            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                        }
                                        AttachmentHandle attachmentHandle = new AttachmentHandle(parent2, child);
                                        if (this.library.ReleaseMutex(mutex)) {
                                            return attachmentHandle;
                                        }
                                        throw new Win32Exception(Native.getLastError());
                                    }
                                    WinNT.HANDLE child2 = this.library.OpenSemaphoreW(WindowsLibrary.SEMAPHORE_ALL_ACCESS, false, target + "_set0");
                                    if (child2 == null) {
                                        throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                                    }
                                    AttachmentHandle attachmentHandle2 = new AttachmentHandle(parent, child2);
                                    if (this.library.ReleaseMutex(mutex)) {
                                        return attachmentHandle2;
                                    }
                                    throw new Win32Exception(Native.getLastError());
                                } catch (Throwable th) {
                                    if (!this.library.ReleaseMutex(mutex)) {
                                        throw new Win32Exception(Native.getLastError());
                                    }
                                    throw th;
                                }
                        }
                    } finally {
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher$ForJnaWindowsEnvironment$WindowsLibrary.class */
                public interface WindowsLibrary extends StdCallLibrary {
                    public static final int SEMAPHORE_ALL_ACCESS = 2031619;

                    WinNT.HANDLE OpenSemaphoreW(int i, boolean z, String str);

                    WinNT.HANDLE CreateSemaphoreW(WinBase.SECURITY_ATTRIBUTES security_attributes, long j, long j2, String str);

                    boolean ReleaseSemaphore(WinNT.HANDLE handle, long j, Long l);

                    WinNT.HANDLE CreateMutex(SecurityAttributes securityAttributes, boolean z, String str);

                    WinNT.HANDLE OpenMutex(int i, boolean z, String str);

                    boolean ReleaseMutex(WinNT.HANDLE handle);

                    @SuppressFBWarnings(value = {"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD"}, justification = "Field required by native implementation.")
                    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher$ForJnaWindowsEnvironment$WindowsLibrary$SecurityAttributes.class */
                    public static class SecurityAttributes extends Structure {
                        public WinDef.DWORD length;
                        public Pointer securityDescriptor;
                        public boolean inherit;

                        protected List<String> getFieldOrder() {
                            return Arrays.asList(XMLConstants.LENGTH_ATTRIBUTE, "securityDescriptor", "inherit");
                        }
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/VirtualMachine$ForOpenJ9$Dispatcher$ForJnaWindowsEnvironment$AttachmentHandle.class */
                public static class AttachmentHandle implements Closeable {
                    private final WinNT.HANDLE parent;
                    private final WinNT.HANDLE child;

                    protected AttachmentHandle(WinNT.HANDLE parent, WinNT.HANDLE child) {
                        this.parent = parent;
                        this.child = child;
                    }

                    protected WinNT.HANDLE getHandle() {
                        return this.child;
                    }

                    @Override // java.io.Closeable, java.lang.AutoCloseable
                    public void close() {
                        try {
                            if (!Kernel32.INSTANCE.CloseHandle(this.child)) {
                                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                            }
                            boolean closed = Kernel32.INSTANCE.CloseHandle(this.parent);
                            if (!closed) {
                                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
                            }
                        } catch (Throwable th) {
                            Kernel32.INSTANCE.CloseHandle(this.parent);
                            throw th;
                        }
                    }
                }
            }
        }
    }
}
