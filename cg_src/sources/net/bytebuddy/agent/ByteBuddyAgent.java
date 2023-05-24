package net.bytebuddy.agent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import net.bytebuddy.agent.VirtualMachine;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent.class */
public class ByteBuddyAgent {
    public static final String LATENT_RESOLVE = "net.bytebuddy.agent.latent";
    private static final String AGENT_CLASS_PROPERTY = "Agent-Class";
    private static final String CAN_REDEFINE_CLASSES_PROPERTY = "Can-Redefine-Classes";
    private static final String CAN_RETRANSFORM_CLASSES_PROPERTY = "Can-Retransform-Classes";
    private static final String CAN_SET_NATIVE_METHOD_PREFIX = "Can-Set-Native-Method-Prefix";
    private static final String MANIFEST_VERSION_VALUE = "1.0";
    private static final int BUFFER_SIZE = 1024;
    private static final int START_INDEX = 0;
    private static final int END_OF_FILE = -1;
    private static final int SUCCESSFUL_ATTACH = 0;
    private static final String ATTACHER_FILE_NAME = "byteBuddyAttacher";
    private static final String CLASS_FILE_EXTENSION = ".class";
    private static final String JAR_FILE_EXTENSION = ".jar";
    private static final String CLASS_PATH_ARGUMENT = "-cp";
    private static final String JAVA_HOME = "java.home";
    private static final String OS_NAME = "os.name";
    private static final String INSTRUMENTATION_METHOD = "getInstrumentation";
    private static final String FILE_PROTOCOL = "file";
    private static final Object STATIC_MEMBER = null;
    private static final ClassLoader BOOTSTRAP_CLASS_LOADER = null;
    private static final String WITHOUT_ARGUMENT = null;
    private static final Instrumentation UNAVAILABLE = null;
    private static final File CANNOT_SELF_RESOLVE = null;
    private static final AttachmentTypeEvaluator ATTACHMENT_TYPE_EVALUATOR = (AttachmentTypeEvaluator) AccessController.doPrivileged(AttachmentTypeEvaluator.InstallationAction.INSTANCE);

    private ByteBuddyAgent() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    public static Instrumentation getInstrumentation() {
        Instrumentation instrumentation = doGetInstrumentation();
        if (instrumentation == null) {
            throw new IllegalStateException("The Byte Buddy agent is not initialized");
        }
        return instrumentation;
    }

    public static void attach(File agentJar, String processId) {
        attach(agentJar, processId, WITHOUT_ARGUMENT);
    }

    public static void attach(File agentJar, String processId, String argument) {
        attach(agentJar, processId, argument, AttachmentProvider.DEFAULT);
    }

    public static void attach(File agentJar, String processId, AttachmentProvider attachmentProvider) {
        attach(agentJar, processId, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attach(File agentJar, String processId, String argument, AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processId, argument, new AgentProvider.ForExistingAgent(agentJar), false);
    }

    public static void attach(File agentJar, ProcessProvider processProvider) {
        attach(agentJar, processProvider, WITHOUT_ARGUMENT);
    }

    public static void attach(File agentJar, ProcessProvider processProvider, String argument) {
        attach(agentJar, processProvider, argument, AttachmentProvider.DEFAULT);
    }

    public static void attach(File agentJar, ProcessProvider processProvider, AttachmentProvider attachmentProvider) {
        attach(agentJar, processProvider, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attach(File agentJar, ProcessProvider processProvider, String argument, AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processProvider.resolve(), argument, new AgentProvider.ForExistingAgent(agentJar), false);
    }

    public static void attachNative(File agentLibrary, String processId) {
        attachNative(agentLibrary, processId, WITHOUT_ARGUMENT);
    }

    public static void attachNative(File agentLibrary, String processId, String argument) {
        attachNative(agentLibrary, processId, argument, AttachmentProvider.DEFAULT);
    }

    public static void attachNative(File agentLibrary, String processId, AttachmentProvider attachmentProvider) {
        attachNative(agentLibrary, processId, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attachNative(File agentLibrary, String processId, String argument, AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processId, argument, new AgentProvider.ForExistingAgent(agentLibrary), true);
    }

    public static void attachNative(File agentLibrary, ProcessProvider processProvider) {
        attachNative(agentLibrary, processProvider, WITHOUT_ARGUMENT);
    }

    public static void attachNative(File agentLibrary, ProcessProvider processProvider, String argument) {
        attachNative(agentLibrary, processProvider, argument, AttachmentProvider.DEFAULT);
    }

    public static void attachNative(File agentLibrary, ProcessProvider processProvider, AttachmentProvider attachmentProvider) {
        attachNative(agentLibrary, processProvider, WITHOUT_ARGUMENT, attachmentProvider);
    }

    public static void attachNative(File agentLibrary, ProcessProvider processProvider, String argument, AttachmentProvider attachmentProvider) {
        install(attachmentProvider, processProvider.resolve(), argument, new AgentProvider.ForExistingAgent(agentLibrary), true);
    }

    public static Instrumentation install() {
        return install(AttachmentProvider.DEFAULT);
    }

    public static Instrumentation install(AttachmentProvider attachmentProvider) {
        return install(attachmentProvider, ProcessProvider.ForCurrentVm.INSTANCE);
    }

    public static Instrumentation install(ProcessProvider processProvider) {
        return install(AttachmentProvider.DEFAULT, processProvider);
    }

    public static synchronized Instrumentation install(AttachmentProvider attachmentProvider, ProcessProvider processProvider) {
        Instrumentation instrumentation = doGetInstrumentation();
        if (instrumentation != null) {
            return instrumentation;
        }
        install(attachmentProvider, processProvider.resolve(), WITHOUT_ARGUMENT, AgentProvider.ForByteBuddyAgent.INSTANCE, false);
        return doGetInstrumentation();
    }

    private static void install(AttachmentProvider attachmentProvider, String processId, String argument, AgentProvider agentProvider, boolean isNative) {
        AttachmentProvider.Accessor attachmentAccessor = attachmentProvider.attempt();
        if (!attachmentAccessor.isAvailable()) {
            throw new IllegalStateException("No compatible attachment provider is available");
        }
        try {
            if (attachmentAccessor.isExternalAttachmentRequired() && ATTACHMENT_TYPE_EVALUATOR.requiresExternalAttachment(processId)) {
                installExternal(attachmentAccessor.getExternalAttachment(), processId, agentProvider.resolve(), isNative, argument);
            } else {
                Attacher.install(attachmentAccessor.getVirtualMachineType(), processId, agentProvider.resolve().getAbsolutePath(), isNative, argument);
            }
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception2) {
            throw new IllegalStateException("Error during attachment using: " + attachmentProvider, exception2);
        }
    }

    private static void installExternal(AttachmentProvider.Accessor.ExternalAttachment externalAttachment, String processId, File agent, boolean isNative, String argument) throws Exception {
        File selfResolvedJar = trySelfResolve();
        File attachmentJar = null;
        if (selfResolvedJar == null) {
            try {
                InputStream inputStream = Attacher.class.getResourceAsStream('/' + Attacher.class.getName().replace('.', '/') + ".class");
                if (inputStream == null) {
                    throw new IllegalStateException("Cannot locate class file for Byte Buddy installation process");
                }
                attachmentJar = File.createTempFile(ATTACHER_FILE_NAME, JAR_FILE_EXTENSION);
                JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(attachmentJar));
                try {
                    jarOutputStream.putNextEntry(new JarEntry(Attacher.class.getName().replace('.', '/') + ".class"));
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int index = inputStream.read(buffer);
                        if (index == -1) {
                            break;
                        }
                        jarOutputStream.write(buffer, 0, index);
                    }
                    jarOutputStream.closeEntry();
                    jarOutputStream.close();
                    inputStream.close();
                } catch (Throwable th) {
                    jarOutputStream.close();
                    throw th;
                }
            } catch (Throwable th2) {
                if (0 != 0 && !attachmentJar.delete()) {
                    attachmentJar.deleteOnExit();
                }
                throw th2;
            }
        }
        StringBuilder classPath = new StringBuilder().append(quote((selfResolvedJar == null ? attachmentJar : selfResolvedJar).getCanonicalPath()));
        for (File jar : externalAttachment.getClassPath()) {
            classPath.append(File.pathSeparatorChar).append(quote(jar.getCanonicalPath()));
        }
        String[] strArr = new String[9];
        strArr[0] = System.getProperty(JAVA_HOME) + File.separatorChar + "bin" + File.separatorChar + (System.getProperty(OS_NAME, "").toLowerCase(Locale.US).contains(Os.FAMILY_WINDOWS) ? "java.exe" : "java");
        strArr[1] = CLASS_PATH_ARGUMENT;
        strArr[2] = classPath.toString();
        strArr[3] = Attacher.class.getName();
        strArr[4] = externalAttachment.getVirtualMachineType();
        strArr[5] = processId;
        strArr[6] = quote(agent.getAbsolutePath());
        strArr[7] = Boolean.toString(isNative);
        strArr[8] = argument == null ? "" : "=" + argument;
        if (new ProcessBuilder(strArr).start().waitFor() != 0) {
            throw new IllegalStateException("Could not self-attach to current VM using external process");
        }
        if (attachmentJar != null && !attachmentJar.delete()) {
            attachmentJar.deleteOnExit();
        }
    }

    @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
    private static File trySelfResolve() {
        try {
            if (Boolean.getBoolean(LATENT_RESOLVE)) {
                return CANNOT_SELF_RESOLVE;
            }
            ProtectionDomain protectionDomain = Attacher.class.getProtectionDomain();
            if (protectionDomain == null) {
                return CANNOT_SELF_RESOLVE;
            }
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource == null) {
                return CANNOT_SELF_RESOLVE;
            }
            URL location = codeSource.getLocation();
            if (!location.getProtocol().equals("file")) {
                return CANNOT_SELF_RESOLVE;
            }
            try {
                return new File(location.toURI());
            } catch (URISyntaxException e) {
                return new File(location.getPath());
            }
        } catch (Exception e2) {
            return CANNOT_SELF_RESOLVE;
        }
    }

    private static String quote(String value) {
        return value.contains(Instruction.argsep) ? '\"' + value + '\"' : value;
    }

    @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Legal outcome where reflection communicates errors by throwing an exception")
    private static Instrumentation doGetInstrumentation() {
        try {
            return (Instrumentation) ClassLoader.getSystemClassLoader().loadClass(Installer.class.getName()).getMethod(INSTRUMENTATION_METHOD, new Class[0]).invoke(STATIC_MEMBER, new Object[0]);
        } catch (Exception e) {
            return UNAVAILABLE;
        }
    }

    @SuppressFBWarnings(value = {"IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION"}, justification = "Safe initialization is implied")
    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider.class */
    public interface AttachmentProvider {
        public static final AttachmentProvider DEFAULT = new Compound(ForModularizedVm.INSTANCE, ForJ9Vm.INSTANCE, ForStandardToolsJarVm.JVM_ROOT, ForStandardToolsJarVm.JDK_ROOT, ForStandardToolsJarVm.MACINTOSH, ForUserDefinedToolsJar.INSTANCE, ForEmulatedAttachment.INSTANCE);

        Accessor attempt();

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$Accessor.class */
        public interface Accessor {
            public static final String VIRTUAL_MACHINE_TYPE_NAME = "com.sun.tools.attach.VirtualMachine";
            public static final String VIRTUAL_MACHINE_TYPE_NAME_J9 = "com.ibm.tools.attach.VirtualMachine";

            boolean isAvailable();

            boolean isExternalAttachmentRequired();

            Class<?> getVirtualMachineType();

            ExternalAttachment getExternalAttachment();

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$Accessor$Unavailable.class */
            public enum Unavailable implements Accessor {
                INSTANCE;

                @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                public boolean isAvailable() {
                    return false;
                }

                @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                public boolean isExternalAttachmentRequired() {
                    throw new IllegalStateException("Cannot read the virtual machine type for an unavailable accessor");
                }

                @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                public Class<?> getVirtualMachineType() {
                    throw new IllegalStateException("Cannot read the virtual machine type for an unavailable accessor");
                }

                @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                public ExternalAttachment getExternalAttachment() {
                    throw new IllegalStateException("Cannot read the virtual machine type for an unavailable accessor");
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$Accessor$ExternalAttachment.class */
            public static class ExternalAttachment {
                private final String virtualMachineType;
                private final List<File> classPath;

                public ExternalAttachment(String virtualMachineType, List<File> classPath) {
                    this.virtualMachineType = virtualMachineType;
                    this.classPath = classPath;
                }

                public String getVirtualMachineType() {
                    return this.virtualMachineType;
                }

                public List<File> getClassPath() {
                    return this.classPath;
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$Accessor$Simple.class */
            public static abstract class Simple implements Accessor {
                protected final Class<?> virtualMachineType;

                protected Simple(Class<?> virtualMachineType) {
                    this.virtualMachineType = virtualMachineType;
                }

                public static Accessor of(ClassLoader classLoader, File... classPath) {
                    try {
                        return new WithExternalAttachment(classLoader.loadClass(Accessor.VIRTUAL_MACHINE_TYPE_NAME), Arrays.asList(classPath));
                    } catch (ClassNotFoundException e) {
                        return Unavailable.INSTANCE;
                    }
                }

                public static Accessor ofJ9() {
                    try {
                        return new WithExternalAttachment(ClassLoader.getSystemClassLoader().loadClass(Accessor.VIRTUAL_MACHINE_TYPE_NAME_J9), Collections.emptyList());
                    } catch (ClassNotFoundException e) {
                        return Unavailable.INSTANCE;
                    }
                }

                @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                public boolean isAvailable() {
                    return true;
                }

                @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                public Class<?> getVirtualMachineType() {
                    return this.virtualMachineType;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$Accessor$Simple$WithExternalAttachment.class */
                public static class WithExternalAttachment extends Simple {
                    private final List<File> classPath;

                    public WithExternalAttachment(Class<?> virtualMachineType, List<File> classPath) {
                        super(virtualMachineType);
                        this.classPath = classPath;
                    }

                    @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                    public boolean isExternalAttachmentRequired() {
                        return true;
                    }

                    @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                    public ExternalAttachment getExternalAttachment() {
                        return new ExternalAttachment(this.virtualMachineType.getName(), this.classPath);
                    }
                }

                /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$Accessor$Simple$WithDirectAttachment.class */
                protected static class WithDirectAttachment extends Simple {
                    public WithDirectAttachment(Class<?> virtualMachineType) {
                        super(virtualMachineType);
                    }

                    @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                    public boolean isExternalAttachmentRequired() {
                        return false;
                    }

                    @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor
                    public ExternalAttachment getExternalAttachment() {
                        throw new IllegalStateException("Cannot apply external attachment");
                    }
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$ForModularizedVm.class */
        public enum ForModularizedVm implements AttachmentProvider {
            INSTANCE;

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider
            public Accessor attempt() {
                return Accessor.Simple.of(ClassLoader.getSystemClassLoader(), new File[0]);
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$ForJ9Vm.class */
        public enum ForJ9Vm implements AttachmentProvider {
            INSTANCE;

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider
            public Accessor attempt() {
                return Accessor.Simple.ofJ9();
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$ForStandardToolsJarVm.class */
        public enum ForStandardToolsJarVm implements AttachmentProvider {
            JVM_ROOT("../lib/tools.jar"),
            JDK_ROOT("lib/tools.jar"),
            MACINTOSH("../Classes/classes.jar");
            
            private static final String JAVA_HOME_PROPERTY = "java.home";
            private final String toolsJarPath;

            ForStandardToolsJarVm(String toolsJarPath) {
                this.toolsJarPath = toolsJarPath;
            }

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider
            @SuppressFBWarnings(value = {"DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit user responsibility")
            public Accessor attempt() {
                File toolsJar = new File(System.getProperty(JAVA_HOME_PROPERTY), this.toolsJarPath);
                try {
                    return (toolsJar.isFile() && toolsJar.canRead()) ? Accessor.Simple.of(new URLClassLoader(new URL[]{toolsJar.toURI().toURL()}, ByteBuddyAgent.BOOTSTRAP_CLASS_LOADER), toolsJar) : Accessor.Unavailable.INSTANCE;
                } catch (MalformedURLException e) {
                    throw new IllegalStateException("Could not represent " + toolsJar + " as URL");
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$ForUserDefinedToolsJar.class */
        public enum ForUserDefinedToolsJar implements AttachmentProvider {
            INSTANCE;
            
            public static final String PROPERTY = "net.bytebuddy.agent.toolsjar";

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider
            @SuppressFBWarnings(value = {"DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED"}, justification = "Privilege is explicit user responsibility")
            public Accessor attempt() {
                String location = System.getProperty(PROPERTY);
                if (location == null) {
                    return Accessor.Unavailable.INSTANCE;
                }
                File toolsJar = new File(location);
                try {
                    return Accessor.Simple.of(new URLClassLoader(new URL[]{toolsJar.toURI().toURL()}, ByteBuddyAgent.BOOTSTRAP_CLASS_LOADER), toolsJar);
                } catch (MalformedURLException e) {
                    throw new IllegalStateException("Could not represent " + toolsJar + " as URL");
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$ForEmulatedAttachment.class */
        public enum ForEmulatedAttachment implements AttachmentProvider {
            INSTANCE;

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider
            public Accessor attempt() {
                try {
                    return new Accessor.Simple.WithDirectAttachment((Class) AccessController.doPrivileged(VirtualMachine.Resolver.INSTANCE));
                } catch (Throwable th) {
                    return Accessor.Unavailable.INSTANCE;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentProvider$Compound.class */
        public static class Compound implements AttachmentProvider {
            private final List<AttachmentProvider> attachmentProviders;

            public Compound(AttachmentProvider... attachmentProvider) {
                this(Arrays.asList(attachmentProvider));
            }

            public Compound(List<? extends AttachmentProvider> attachmentProviders) {
                this.attachmentProviders = new ArrayList();
                for (AttachmentProvider attachmentProvider : attachmentProviders) {
                    if (attachmentProvider instanceof Compound) {
                        this.attachmentProviders.addAll(((Compound) attachmentProvider).attachmentProviders);
                    } else {
                        this.attachmentProviders.add(attachmentProvider);
                    }
                }
            }

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider
            public Accessor attempt() {
                for (AttachmentProvider attachmentProvider : this.attachmentProviders) {
                    Accessor accessor = attachmentProvider.attempt();
                    if (accessor.isAvailable()) {
                        return accessor;
                    }
                }
                return Accessor.Unavailable.INSTANCE;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$ProcessProvider.class */
    public interface ProcessProvider {
        String resolve();

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$ProcessProvider$ForCurrentVm.class */
        public enum ForCurrentVm implements ProcessProvider {
            INSTANCE;
            
            private final ProcessProvider dispatcher = ForJava9CapableVm.make();

            ForCurrentVm() {
            }

            @Override // net.bytebuddy.agent.ByteBuddyAgent.ProcessProvider
            public String resolve() {
                return this.dispatcher.resolve();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$ProcessProvider$ForCurrentVm$ForLegacyVm.class */
            public enum ForLegacyVm implements ProcessProvider {
                INSTANCE;

                @Override // net.bytebuddy.agent.ByteBuddyAgent.ProcessProvider
                public String resolve() {
                    String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
                    int processIdIndex = runtimeName.indexOf(64);
                    if (processIdIndex == -1) {
                        throw new IllegalStateException("Cannot extract process id from runtime management bean");
                    }
                    return runtimeName.substring(0, processIdIndex);
                }
            }

            /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$ProcessProvider$ForCurrentVm$ForJava9CapableVm.class */
            protected static class ForJava9CapableVm implements ProcessProvider {
                private final Method current;
                private final Method pid;

                protected ForJava9CapableVm(Method current, Method pid) {
                    this.current = current;
                    this.pid = pid;
                }

                @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
                public static ProcessProvider make() {
                    try {
                        return new ForJava9CapableVm(Class.forName("java.lang.ProcessHandle").getMethod(MSVSSConstants.TIME_CURRENT, new Class[0]), Class.forName("java.lang.ProcessHandle").getMethod("pid", new Class[0]));
                    } catch (Exception e) {
                        return ForLegacyVm.INSTANCE;
                    }
                }

                @Override // net.bytebuddy.agent.ByteBuddyAgent.ProcessProvider
                public String resolve() {
                    try {
                        return this.pid.invoke(this.current.invoke(ByteBuddyAgent.STATIC_MEMBER, new Object[0]), new Object[0]).toString();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Cannot access Java 9 process API", exception);
                    } catch (InvocationTargetException exception2) {
                        throw new IllegalStateException("Error when accessing Java 9 process API", exception2.getCause());
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AgentProvider.class */
    public interface AgentProvider {
        File resolve() throws IOException;

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AgentProvider$ForByteBuddyAgent.class */
        public enum ForByteBuddyAgent implements AgentProvider {
            INSTANCE;
            
            private static final String AGENT_FILE_NAME = "byteBuddyAgent";

            private static File trySelfResolve() throws IOException {
                CodeSource codeSource;
                File agentJar;
                ProtectionDomain protectionDomain = Installer.class.getProtectionDomain();
                if (!Boolean.getBoolean(ByteBuddyAgent.LATENT_RESOLVE) && protectionDomain != null && (codeSource = protectionDomain.getCodeSource()) != null) {
                    URL location = codeSource.getLocation();
                    if (location.getProtocol().equals("file")) {
                        try {
                            agentJar = new File(location.toURI());
                        } catch (URISyntaxException e) {
                            agentJar = new File(location.getPath());
                        }
                        if (agentJar.isFile() && agentJar.canRead()) {
                            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(agentJar));
                            try {
                                Manifest manifest = jarInputStream.getManifest();
                                if (manifest == null) {
                                    File file = ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                                    jarInputStream.close();
                                    return file;
                                }
                                Attributes attributes = manifest.getMainAttributes();
                                if (attributes == null) {
                                    File file2 = ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                                    jarInputStream.close();
                                    return file2;
                                } else if (Installer.class.getName().equals(attributes.getValue(ByteBuddyAgent.AGENT_CLASS_PROPERTY)) && Boolean.parseBoolean(attributes.getValue(ByteBuddyAgent.CAN_REDEFINE_CLASSES_PROPERTY)) && Boolean.parseBoolean(attributes.getValue(ByteBuddyAgent.CAN_RETRANSFORM_CLASSES_PROPERTY)) && Boolean.parseBoolean(attributes.getValue(ByteBuddyAgent.CAN_SET_NATIVE_METHOD_PREFIX))) {
                                    return agentJar;
                                } else {
                                    File file3 = ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                                    jarInputStream.close();
                                    return file3;
                                }
                            } finally {
                                jarInputStream.close();
                            }
                        }
                        return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                    }
                    return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
                }
                return ByteBuddyAgent.CANNOT_SELF_RESOLVE;
            }

            private static File createJarFile() throws IOException {
                InputStream inputStream = Installer.class.getResourceAsStream('/' + Installer.class.getName().replace('.', '/') + ".class");
                if (inputStream == null) {
                    throw new IllegalStateException("Cannot locate class file for Byte Buddy installer");
                }
                try {
                    File agentJar = File.createTempFile(AGENT_FILE_NAME, ByteBuddyAgent.JAR_FILE_EXTENSION);
                    agentJar.deleteOnExit();
                    Manifest manifest = new Manifest();
                    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
                    manifest.getMainAttributes().put(new Attributes.Name(ByteBuddyAgent.AGENT_CLASS_PROPERTY), Installer.class.getName());
                    manifest.getMainAttributes().put(new Attributes.Name(ByteBuddyAgent.CAN_REDEFINE_CLASSES_PROPERTY), Boolean.TRUE.toString());
                    manifest.getMainAttributes().put(new Attributes.Name(ByteBuddyAgent.CAN_RETRANSFORM_CLASSES_PROPERTY), Boolean.TRUE.toString());
                    manifest.getMainAttributes().put(new Attributes.Name(ByteBuddyAgent.CAN_SET_NATIVE_METHOD_PREFIX), Boolean.TRUE.toString());
                    JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(agentJar), manifest);
                    jarOutputStream.putNextEntry(new JarEntry(Installer.class.getName().replace('.', '/') + ".class"));
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int index = inputStream.read(buffer);
                        if (index != -1) {
                            jarOutputStream.write(buffer, 0, index);
                        } else {
                            jarOutputStream.closeEntry();
                            jarOutputStream.close();
                            return agentJar;
                        }
                    }
                } finally {
                    inputStream.close();
                }
            }

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AgentProvider
            public File resolve() throws IOException {
                try {
                    File agentJar = trySelfResolve();
                    return agentJar == null ? createJarFile() : agentJar;
                } catch (Exception e) {
                    return createJarFile();
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AgentProvider$ForExistingAgent.class */
        public static class ForExistingAgent implements AgentProvider {
            private File agent;

            protected ForExistingAgent(File agent) {
                this.agent = agent;
            }

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AgentProvider
            public File resolve() {
                return this.agent;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentTypeEvaluator.class */
    public interface AttachmentTypeEvaluator {
        boolean requiresExternalAttachment(String str);

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentTypeEvaluator$InstallationAction.class */
        public enum InstallationAction implements PrivilegedAction<AttachmentTypeEvaluator> {
            INSTANCE;
            
            private static final String JDK_ALLOW_SELF_ATTACH = "jdk.attach.allowAttachSelf";

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
            public AttachmentTypeEvaluator run() {
                try {
                    if (Boolean.getBoolean(JDK_ALLOW_SELF_ATTACH)) {
                        return Disabled.INSTANCE;
                    }
                    return new ForJava9CapableVm(Class.forName("java.lang.ProcessHandle").getMethod(MSVSSConstants.TIME_CURRENT, new Class[0]), Class.forName("java.lang.ProcessHandle").getMethod("pid", new Class[0]));
                } catch (Exception e) {
                    return Disabled.INSTANCE;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentTypeEvaluator$Disabled.class */
        public enum Disabled implements AttachmentTypeEvaluator {
            INSTANCE;

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentTypeEvaluator
            public boolean requiresExternalAttachment(String processId) {
                return false;
            }
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/ByteBuddyAgent$AttachmentTypeEvaluator$ForJava9CapableVm.class */
        public static class ForJava9CapableVm implements AttachmentTypeEvaluator {
            private final Method current;
            private final Method pid;

            protected ForJava9CapableVm(Method current, Method pid) {
                this.current = current;
                this.pid = pid;
            }

            @Override // net.bytebuddy.agent.ByteBuddyAgent.AttachmentTypeEvaluator
            public boolean requiresExternalAttachment(String processId) {
                try {
                    return this.pid.invoke(this.current.invoke(ByteBuddyAgent.STATIC_MEMBER, new Object[0]), new Object[0]).toString().equals(processId);
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot access Java 9 process API", exception);
                } catch (InvocationTargetException exception2) {
                    throw new IllegalStateException("Error when accessing Java 9 process API", exception2.getCause());
                }
            }
        }
    }
}
