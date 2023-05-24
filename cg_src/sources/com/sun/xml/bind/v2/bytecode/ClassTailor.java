package com.sun.xml.bind.v2.bytecode;

import com.sun.xml.bind.Util;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/bytecode/ClassTailor.class */
public final class ClassTailor {
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ClassTailor.class.desiredAssertionStatus();
        logger = Util.getClassLogger();
    }

    private ClassTailor() {
    }

    public static String toVMClassName(Class c) {
        if ($assertionsDisabled || !c.isPrimitive()) {
            if (c.isArray()) {
                return toVMTypeName(c);
            }
            return c.getName().replace('.', '/');
        }
        throw new AssertionError();
    }

    public static String toVMTypeName(Class c) {
        if (c.isArray()) {
            return '[' + toVMTypeName(c.getComponentType());
        }
        if (c.isPrimitive()) {
            if (c == Boolean.TYPE) {
                return "Z";
            }
            if (c == Character.TYPE) {
                return "C";
            }
            if (c == Byte.TYPE) {
                return "B";
            }
            if (c == Double.TYPE) {
                return "D";
            }
            if (c == Float.TYPE) {
                return "F";
            }
            if (c == Integer.TYPE) {
                return "I";
            }
            if (c == Long.TYPE) {
                return "J";
            }
            if (c == Short.TYPE) {
                return "S";
            }
            throw new IllegalArgumentException(c.getName());
        }
        return 'L' + c.getName().replace('.', '/') + ';';
    }

    public static byte[] tailor(Class templateClass, String newClassName, String... replacements) {
        String vmname = toVMClassName(templateClass);
        return tailor(SecureLoader.getClassClassLoader(templateClass).getResourceAsStream(vmname + ".class"), vmname, newClassName, replacements);
    }

    public static byte[] tailor(InputStream image, String templateClassName, String newClassName, String... replacements) {
        DataInputStream in = new DataInputStream(image);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            DataOutputStream out = new DataOutputStream(baos);
            long l = in.readLong();
            out.writeLong(l);
            short count = in.readShort();
            out.writeShort(count);
            int i = 0;
            while (i < count) {
                byte tag = in.readByte();
                out.writeByte(tag);
                switch (tag) {
                    case 0:
                        break;
                    case 1:
                        String value = in.readUTF();
                        if (value.equals(templateClassName)) {
                            value = newClassName;
                        } else {
                            int j = 0;
                            while (true) {
                                if (j < replacements.length) {
                                    if (!value.equals(replacements[j])) {
                                        j += 2;
                                    } else {
                                        value = replacements[j + 1];
                                    }
                                }
                            }
                        }
                        out.writeUTF(value);
                        break;
                    case 2:
                    default:
                        throw new IllegalArgumentException("Unknown constant type " + ((int) tag));
                    case 3:
                    case 4:
                        out.writeInt(in.readInt());
                        break;
                    case 5:
                    case 6:
                        i++;
                        out.writeLong(in.readLong());
                        break;
                    case 7:
                    case 8:
                        out.writeShort(in.readShort());
                        break;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        out.writeInt(in.readInt());
                        break;
                }
                i++;
            }
            byte[] buf = new byte[512];
            while (true) {
                int len = in.read(buf);
                if (len > 0) {
                    out.write(buf, 0, len);
                } else {
                    in.close();
                    out.close();
                    return baos.toByteArray();
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "failed to tailor", (Throwable) e);
            return null;
        }
    }
}
