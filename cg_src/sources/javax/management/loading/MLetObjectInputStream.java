package javax.management.loading;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/loading/MLetObjectInputStream.class */
class MLetObjectInputStream extends ObjectInputStream {
    private MLet loader;

    public MLetObjectInputStream(InputStream inputStream, MLet mLet) throws IOException, StreamCorruptedException {
        super(inputStream);
        if (mLet == null) {
            throw new IllegalArgumentException("Illegal null argument to MLetObjectInputStream");
        }
        this.loader = mLet;
    }

    private Class primitiveType(char c) {
        switch (c) {
            case 'B':
                return Byte.TYPE;
            case 'C':
                return Character.TYPE;
            case 'D':
                return Double.TYPE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                return null;
            case 'F':
                return Float.TYPE;
            case 'I':
                return Integer.TYPE;
            case 'J':
                return Long.TYPE;
            case 'S':
                return Short.TYPE;
            case 'Z':
                return Boolean.TYPE;
        }
    }

    @Override // java.io.ObjectInputStream
    protected Class resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        Class primitiveType;
        String name = objectStreamClass.getName();
        if (name.startsWith("[")) {
            int i = 1;
            while (name.charAt(i) == '[') {
                i++;
            }
            if (name.charAt(i) == 'L') {
                primitiveType = this.loader.loadClass(name.substring(i + 1, name.length() - 1));
            } else if (name.length() != i + 1) {
                throw new ClassNotFoundException(name);
            } else {
                primitiveType = primitiveType(name.charAt(i));
            }
            int[] iArr = new int[i];
            for (int i2 = 0; i2 < i; i2++) {
                iArr[i2] = 0;
            }
            return Array.newInstance(primitiveType, iArr).getClass();
        }
        return this.loader.loadClass(name);
    }

    public ClassLoader getClassLoader() {
        return this.loader;
    }
}
