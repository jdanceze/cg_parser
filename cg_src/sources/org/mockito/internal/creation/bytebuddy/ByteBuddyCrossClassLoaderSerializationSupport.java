package org.mockito.internal.creation.bytebuddy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.mockito.Incubating;
import org.mockito.exceptions.base.MockitoSerializationIssue;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.StringUtil;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MemberAccessor;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ByteBuddyCrossClassLoaderSerializationSupport.class */
class ByteBuddyCrossClassLoaderSerializationSupport implements Serializable {
    private static final long serialVersionUID = 7411152578314420778L;
    private static final String MOCKITO_PROXY_MARKER = "ByteBuddyMockitoProxyMarker";
    private boolean instanceLocalCurrentlySerializingFlag = false;
    private final Lock mutex = new ReentrantLock();

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ByteBuddyCrossClassLoaderSerializationSupport$CrossClassLoaderSerializableMock.class */
    public interface CrossClassLoaderSerializableMock {
        Object writeReplace();
    }

    public Object writeReplace(Object mockitoMock) throws ObjectStreamException {
        this.mutex.lock();
        try {
            try {
                if (mockIsCurrentlyBeingReplaced()) {
                    return mockitoMock;
                }
                mockReplacementStarted();
                CrossClassLoaderSerializationProxy crossClassLoaderSerializationProxy = new CrossClassLoaderSerializationProxy(mockitoMock);
                mockReplacementCompleted();
                this.mutex.unlock();
                return crossClassLoaderSerializationProxy;
            } catch (IOException ioe) {
                MockName mockName = MockUtil.getMockName(mockitoMock);
                String mockedType = MockUtil.getMockSettings(mockitoMock).getTypeToMock().getCanonicalName();
                throw new MockitoSerializationIssue(StringUtil.join("The mock '" + mockName + "' of type '" + mockedType + "'", "The Java Standard Serialization reported an '" + ioe.getClass().getSimpleName() + "' saying :", "  " + ioe.getMessage()), ioe);
            }
        } finally {
            mockReplacementCompleted();
            this.mutex.unlock();
        }
    }

    private void mockReplacementCompleted() {
        this.instanceLocalCurrentlySerializingFlag = false;
    }

    private void mockReplacementStarted() {
        this.instanceLocalCurrentlySerializingFlag = true;
    }

    private boolean mockIsCurrentlyBeingReplaced() {
        return this.instanceLocalCurrentlySerializingFlag;
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ByteBuddyCrossClassLoaderSerializationSupport$CrossClassLoaderSerializationProxy.class */
    public static class CrossClassLoaderSerializationProxy implements Serializable {
        private static final long serialVersionUID = -7600267929109286514L;
        private final byte[] serializedMock;
        private final Class<?> typeToMock;
        private final Set<Class<?>> extraInterfaces;

        public CrossClassLoaderSerializationProxy(Object mockitoMock) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new MockitoMockObjectOutputStream(out);
            objectOutputStream.writeObject(mockitoMock);
            objectOutputStream.close();
            out.close();
            MockCreationSettings<?> mockSettings = MockUtil.getMockSettings(mockitoMock);
            this.serializedMock = out.toByteArray();
            this.typeToMock = mockSettings.getTypeToMock();
            this.extraInterfaces = mockSettings.getExtraInterfaces();
        }

        private Object readResolve() throws ObjectStreamException {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(this.serializedMock);
                ObjectInputStream objectInputStream = new MockitoMockObjectInputStream(bis, this.typeToMock, this.extraInterfaces);
                Object deserializedMock = objectInputStream.readObject();
                bis.close();
                objectInputStream.close();
                return deserializedMock;
            } catch (IOException ioe) {
                throw new MockitoSerializationIssue(StringUtil.join("Mockito mock cannot be deserialized to a mock of '" + this.typeToMock.getCanonicalName() + "'. The error was :", "  " + ioe.getMessage(), "If you are unsure what is the reason of this exception, feel free to contact us on the mailing list."), ioe);
            } catch (ClassNotFoundException cce) {
                throw new MockitoSerializationIssue(StringUtil.join("A class couldn't be found while deserializing a Mockito mock, you should check your classpath. The error was :", "  " + cce.getMessage(), "If you are still unsure what is the reason of this exception, feel free to contact us on the mailing list."), cce);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ByteBuddyCrossClassLoaderSerializationSupport$MockitoMockObjectInputStream.class */
    public static class MockitoMockObjectInputStream extends ObjectInputStream {
        private final Class<?> typeToMock;
        private final Set<Class<?>> extraInterfaces;

        public MockitoMockObjectInputStream(InputStream in, Class<?> typeToMock, Set<Class<?>> extraInterfaces) throws IOException {
            super(in);
            this.typeToMock = typeToMock;
            this.extraInterfaces = extraInterfaces;
            enableResolveObject(true);
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            if (notMarkedAsAMockitoMock(readObject())) {
                return super.resolveClass(desc);
            }
            try {
                Class<?> proxyClass = ((ClassCreatingMockMaker) Plugins.getMockMaker()).createMockType(new CreationSettings().setTypeToMock(this.typeToMock).setExtraInterfaces(this.extraInterfaces).setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS));
                hackClassNameToMatchNewlyCreatedClass(desc, proxyClass);
                return proxyClass;
            } catch (ClassCastException cce) {
                throw new MockitoSerializationIssue(StringUtil.join("A Byte Buddy-generated mock cannot be deserialized into a non-Byte Buddy generated mock class", "", "The mock maker in use was: " + Plugins.getMockMaker().getClass()), cce);
            }
        }

        private void hackClassNameToMatchNewlyCreatedClass(ObjectStreamClass descInstance, Class<?> proxyClass) throws ObjectStreamException {
            try {
                MemberAccessor accessor = Plugins.getMemberAccessor();
                Field classNameField = descInstance.getClass().getDeclaredField("name");
                try {
                    accessor.set(classNameField, descInstance, proxyClass.getCanonicalName());
                } catch (IllegalAccessException e) {
                    throw new MockitoSerializationIssue("Access to " + classNameField + " was denied", e);
                }
            } catch (NoSuchFieldException nsfe) {
                throw new MockitoSerializationIssue(StringUtil.join("Wow, the class 'ObjectStreamClass' in the JDK don't have the field 'name',", "this is definitely a bug in our code as it means the JDK team changed a few internal things.", "", "Please report an issue with the JDK used, a code sample and a link to download the JDK would be welcome."), nsfe);
            }
        }

        private boolean notMarkedAsAMockitoMock(Object marker) {
            return !ByteBuddyCrossClassLoaderSerializationSupport.MOCKITO_PROXY_MARKER.equals(marker);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ByteBuddyCrossClassLoaderSerializationSupport$MockitoMockObjectOutputStream.class */
    private static class MockitoMockObjectOutputStream extends ObjectOutputStream {
        private static final String NOTHING = "";

        public MockitoMockObjectOutputStream(ByteArrayOutputStream out) throws IOException {
            super(out);
        }

        @Override // java.io.ObjectOutputStream
        protected void annotateClass(Class<?> cl) throws IOException {
            writeObject(mockitoProxyClassMarker(cl));
        }

        private String mockitoProxyClassMarker(Class<?> cl) {
            if (CrossClassLoaderSerializableMock.class.isAssignableFrom(cl)) {
                return ByteBuddyCrossClassLoaderSerializationSupport.MOCKITO_PROXY_MARKER;
            }
            return "";
        }
    }
}
