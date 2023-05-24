package org.hamcrest.generator;

import java.io.IOException;
import java.io.PrintStream;
import org.hamcrest.generator.FactoryMethod;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/QuickReferenceWriter.class */
public class QuickReferenceWriter implements FactoryWriter {
    private final PrintStream out;
    private int columnPosition;

    public QuickReferenceWriter(PrintStream out) {
        this.columnPosition = 14;
        this.out = out;
    }

    public QuickReferenceWriter() {
        this(System.out);
    }

    public void setColumnPosition(int columnPosition) {
        this.columnPosition = columnPosition;
    }

    @Override // org.hamcrest.generator.FactoryWriter
    public void writeHeader() throws IOException {
    }

    @Override // org.hamcrest.generator.FactoryWriter
    public void writeMethod(String generatedMethodName, FactoryMethod factoryMethod) throws IOException {
        String actsOn = removePackageNames(factoryMethod.getGenerifiedType());
        for (int i = actsOn.length(); i < this.columnPosition; i++) {
            this.out.append(' ');
        }
        this.out.append('[').append((CharSequence) actsOn).append((CharSequence) "] ");
        this.out.append((CharSequence) generatedMethodName);
        this.out.append('(');
        boolean seenFirst = false;
        for (FactoryMethod.Parameter parameter : factoryMethod.getParameters()) {
            if (seenFirst) {
                this.out.append((CharSequence) ", ");
            } else {
                seenFirst = true;
            }
            this.out.append((CharSequence) removePackageNames(parameter.getType()));
            this.out.append(' ');
            this.out.append((CharSequence) parameter.getName());
        }
        this.out.append(')');
        this.out.println();
    }

    private static String removePackageNames(String in) {
        return in == null ? "" : in.replaceAll("[^<>]*\\.([^\\.])", "$1");
    }

    @Override // org.hamcrest.generator.FactoryWriter
    public void writeFooter() throws IOException {
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    @Override // java.io.Flushable
    public void flush() throws IOException {
    }
}
