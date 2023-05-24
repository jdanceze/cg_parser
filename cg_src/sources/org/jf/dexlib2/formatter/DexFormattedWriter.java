package org.jf.dexlib2.formatter;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import org.jf.dexlib2.MethodHandleType;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.reference.CallSiteReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.EnumEncodedValue;
import org.jf.dexlib2.iface.value.FieldEncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.iface.value.MethodHandleEncodedValue;
import org.jf.dexlib2.iface.value.MethodTypeEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/formatter/DexFormattedWriter.class */
public class DexFormattedWriter extends Writer {
    protected final Writer writer;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DexFormattedWriter.class.desiredAssertionStatus();
    }

    public DexFormattedWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeMethodDescriptor(MethodReference methodReference) throws IOException {
        writeType(methodReference.getDefiningClass());
        this.writer.write("->");
        writeSimpleName(methodReference.getName());
        this.writer.write(40);
        for (CharSequence paramType : methodReference.getParameterTypes()) {
            writeType(paramType);
        }
        this.writer.write(41);
        writeType(methodReference.getReturnType());
    }

    public void writeShortMethodDescriptor(MethodReference methodReference) throws IOException {
        writeSimpleName(methodReference.getName());
        this.writer.write(40);
        for (CharSequence paramType : methodReference.getParameterTypes()) {
            writeType(paramType);
        }
        this.writer.write(41);
        writeType(methodReference.getReturnType());
    }

    public void writeMethodProtoDescriptor(MethodProtoReference protoReference) throws IOException {
        this.writer.write(40);
        for (CharSequence paramType : protoReference.getParameterTypes()) {
            writeType(paramType);
        }
        this.writer.write(41);
        writeType(protoReference.getReturnType());
    }

    public void writeFieldDescriptor(FieldReference fieldReference) throws IOException {
        writeType(fieldReference.getDefiningClass());
        this.writer.write("->");
        writeSimpleName(fieldReference.getName());
        this.writer.write(58);
        writeType(fieldReference.getType());
    }

    public void writeShortFieldDescriptor(FieldReference fieldReference) throws IOException {
        writeSimpleName(fieldReference.getName());
        this.writer.write(58);
        writeType(fieldReference.getType());
    }

    public void writeMethodHandle(MethodHandleReference methodHandleReference) throws IOException {
        this.writer.write(MethodHandleType.toString(methodHandleReference.getMethodHandleType()));
        this.writer.write(64);
        Reference memberReference = methodHandleReference.getMemberReference();
        if (memberReference instanceof MethodReference) {
            writeMethodDescriptor((MethodReference) memberReference);
        } else {
            writeFieldDescriptor((FieldReference) memberReference);
        }
    }

    public void writeCallSite(CallSiteReference callSiteReference) throws IOException {
        writeSimpleName(callSiteReference.getName());
        this.writer.write(40);
        writeQuotedString(callSiteReference.getMethodName());
        this.writer.write(", ");
        writeMethodProtoDescriptor(callSiteReference.getMethodProto());
        for (EncodedValue encodedValue : callSiteReference.getExtraArguments()) {
            this.writer.write(", ");
            writeEncodedValue(encodedValue);
        }
        this.writer.write(")@");
        MethodHandleReference methodHandle = callSiteReference.getMethodHandle();
        if (methodHandle.getMethodHandleType() != 4) {
            throw new IllegalArgumentException("The linker method handle for a call site must be of type invoke-static");
        }
        writeMethodDescriptor((MethodReference) callSiteReference.getMethodHandle().getMemberReference());
    }

    public void writeType(CharSequence type) throws IOException {
        for (int i = 0; i < type.length(); i++) {
            char c = type.charAt(i);
            if (c == 'L') {
                writeClass(type.subSequence(i, type.length()));
                return;
            } else if (c == '[') {
                this.writer.write(c);
            } else if (c == 'Z' || c == 'B' || c == 'S' || c == 'C' || c == 'I' || c == 'J' || c == 'F' || c == 'D' || c == 'V') {
                this.writer.write(c);
                if (i != type.length() - 1) {
                    throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
                }
                return;
            } else {
                throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
            }
        }
        throw new IllegalArgumentException(String.format("Invalid type string: %s", type));
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00ce, code lost:
        if (r11 != (r9.length() - 1)) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00da, code lost:
        if (r9.charAt(r11) == ';') goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00f1, code lost:
        throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid type string: %s", r9));
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00f2, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void writeClass(java.lang.CharSequence r9) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 243
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jf.dexlib2.formatter.DexFormattedWriter.writeClass(java.lang.CharSequence):void");
    }

    protected void writeSimpleName(CharSequence simpleName) throws IOException {
        this.writer.append(simpleName);
    }

    public void writeQuotedString(CharSequence charSequence) throws IOException {
        this.writer.write(34);
        String string = charSequence.toString();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c >= ' ' && c < 127) {
                if (c == '\'' || c == '\"' || c == '\\') {
                    this.writer.write(92);
                }
                this.writer.write(c);
            } else {
                if (c <= 127) {
                    switch (c) {
                        case '\t':
                            this.writer.write("\\t");
                            break;
                        case '\n':
                            this.writer.write("\\n");
                            break;
                        case '\r':
                            this.writer.write("\\r");
                            break;
                    }
                }
                this.writer.write("\\u");
                this.writer.write(Character.forDigit(c >> '\f', 16));
                this.writer.write(Character.forDigit((c >> '\b') & 15, 16));
                this.writer.write(Character.forDigit((c >> 4) & 15, 16));
                this.writer.write(Character.forDigit(c & 15, 16));
            }
        }
        this.writer.write(34);
    }

    public void writeEncodedValue(EncodedValue encodedValue) throws IOException {
        switch (encodedValue.getValueType()) {
            case 0:
                this.writer.write(String.format("0x%x", Byte.valueOf(((ByteEncodedValue) encodedValue).getValue())));
                return;
            case 1:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 18:
            case 19:
            case 20:
            default:
                throw new IllegalArgumentException("Unknown encoded value type");
            case 2:
                this.writer.write(String.format("0x%x", Short.valueOf(((ShortEncodedValue) encodedValue).getValue())));
                return;
            case 3:
                this.writer.write(String.format("0x%x", Integer.valueOf(((CharEncodedValue) encodedValue).getValue())));
                return;
            case 4:
                this.writer.write(String.format("0x%x", Integer.valueOf(((IntEncodedValue) encodedValue).getValue())));
                return;
            case 6:
                this.writer.write(String.format("0x%x", Long.valueOf(((LongEncodedValue) encodedValue).getValue())));
                return;
            case 16:
                this.writer.write(Float.toString(((FloatEncodedValue) encodedValue).getValue()));
                return;
            case 17:
                this.writer.write(Double.toString(((DoubleEncodedValue) encodedValue).getValue()));
                return;
            case 21:
                writeMethodProtoDescriptor(((MethodTypeEncodedValue) encodedValue).getValue());
                return;
            case 22:
                writeMethodHandle(((MethodHandleEncodedValue) encodedValue).getValue());
                return;
            case 23:
                writeQuotedString(((StringEncodedValue) encodedValue).getValue());
                return;
            case 24:
                writeType(((TypeEncodedValue) encodedValue).getValue());
                return;
            case 25:
                writeFieldDescriptor(((FieldEncodedValue) encodedValue).getValue());
                return;
            case 26:
                writeMethodDescriptor(((MethodEncodedValue) encodedValue).getValue());
                return;
            case 27:
                writeFieldDescriptor(((EnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                writeArray((ArrayEncodedValue) encodedValue);
                return;
            case 29:
                writeAnnotation((AnnotationEncodedValue) encodedValue);
                return;
            case 30:
                this.writer.write(Jimple.NULL);
                return;
            case 31:
                this.writer.write(Boolean.toString(((BooleanEncodedValue) encodedValue).getValue()));
                return;
        }
    }

    protected void writeAnnotation(AnnotationEncodedValue annotation) throws IOException {
        this.writer.write("Annotation[");
        writeType(annotation.getType());
        Set<? extends AnnotationElement> elements = annotation.getElements();
        for (AnnotationElement element : elements) {
            this.writer.write(", ");
            writeSimpleName(element.getName());
            this.writer.write(61);
            writeEncodedValue(element.getValue());
        }
        this.writer.write(93);
    }

    protected void writeArray(ArrayEncodedValue array) throws IOException {
        this.writer.write("Array[");
        boolean first = true;
        for (EncodedValue element : array.getValue()) {
            if (first) {
                first = false;
            } else {
                this.writer.write(", ");
            }
            writeEncodedValue(element);
        }
        this.writer.write(93);
    }

    public void writeReference(Reference reference) throws IOException {
        if (reference instanceof StringReference) {
            writeQuotedString((StringReference) reference);
        } else if (reference instanceof TypeReference) {
            writeType((TypeReference) reference);
        } else if (reference instanceof FieldReference) {
            writeFieldDescriptor((FieldReference) reference);
        } else if (reference instanceof MethodReference) {
            writeMethodDescriptor((MethodReference) reference);
        } else if (reference instanceof MethodProtoReference) {
            writeMethodProtoDescriptor((MethodProtoReference) reference);
        } else if (reference instanceof MethodHandleReference) {
            writeMethodHandle((MethodHandleReference) reference);
        } else if (reference instanceof CallSiteReference) {
            writeCallSite((CallSiteReference) reference);
        } else {
            throw new IllegalArgumentException(String.format("Not a known reference type: %s", reference.getClass()));
        }
    }

    @Override // java.io.Writer
    public void write(int c) throws IOException {
        this.writer.write(c);
    }

    @Override // java.io.Writer
    public void write(char[] cbuf) throws IOException {
        this.writer.write(cbuf);
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.writer.write(cbuf, off, len);
    }

    @Override // java.io.Writer
    public void write(String str) throws IOException {
        this.writer.write(str);
    }

    @Override // java.io.Writer
    public void write(String str, int off, int len) throws IOException {
        this.writer.write(str, off, len);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq) throws IOException {
        return this.writer.append(csq);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return this.writer.append(csq, start, end);
    }

    @Override // java.io.Writer, java.lang.Appendable
    public Writer append(char c) throws IOException {
        return this.writer.append(c);
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.writer.close();
    }
}
