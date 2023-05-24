package org.hamcrest.generator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import org.hamcrest.generator.FactoryMethod;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/HamcrestFactoryWriter.class */
public class HamcrestFactoryWriter implements FactoryWriter {
    private final PrintWriter output;
    private final String javaPackageName;
    private final String javaClassName;
    private String indentationString = "  ";
    private String newLine = "\n";
    private int indentation = 1;

    public HamcrestFactoryWriter(String javaPackageName, String javaClassName, Writer output) {
        this.javaPackageName = javaPackageName;
        this.javaClassName = javaClassName;
        this.output = new PrintWriter(output);
    }

    @Override // org.hamcrest.generator.FactoryWriter
    public void writeHeader() throws IOException {
        this.output.append((CharSequence) "// Generated source.").append((CharSequence) this.newLine).append((CharSequence) "package ").append((CharSequence) this.javaPackageName).append(';').append((CharSequence) this.newLine).append((CharSequence) this.newLine);
        this.output.append((CharSequence) "public class ").append((CharSequence) this.javaClassName).append((CharSequence) " {").append((CharSequence) this.newLine).append((CharSequence) this.newLine);
    }

    @Override // org.hamcrest.generator.FactoryWriter
    public void writeFooter() throws IOException {
        this.output.append('}').append((CharSequence) this.newLine);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.output.close();
    }

    @Override // java.io.Flushable
    public void flush() throws IOException {
        this.output.flush();
    }

    @Override // org.hamcrest.generator.FactoryWriter
    public void writeMethod(String generatedMethodName, FactoryMethod factoryMethodToDelegateTo) throws IOException {
        writeJavaDoc(factoryMethodToDelegateTo);
        indent();
        this.output.append((CharSequence) "public static ");
        writeGenericTypeParameters(factoryMethodToDelegateTo);
        this.output.append((CharSequence) factoryMethodToDelegateTo.getReturnType());
        if (factoryMethodToDelegateTo.getGenerifiedType() != null) {
            this.output.append('<').append((CharSequence) factoryMethodToDelegateTo.getGenerifiedType()).append('>');
        }
        this.output.append(' ').append((CharSequence) generatedMethodName);
        writeParameters(factoryMethodToDelegateTo);
        writeExceptions(factoryMethodToDelegateTo);
        this.output.append((CharSequence) " {").append((CharSequence) this.newLine);
        this.indentation++;
        writeMethodBody(factoryMethodToDelegateTo);
        this.indentation--;
        indent();
        this.output.append('}').append((CharSequence) this.newLine).append((CharSequence) this.newLine);
    }

    private void writeGenericTypeParameters(FactoryMethod factoryMethod) {
        if (!factoryMethod.getGenericTypeParameters().isEmpty()) {
            this.output.append('<');
            boolean seenFirst = false;
            for (String type : factoryMethod.getGenericTypeParameters()) {
                if (seenFirst) {
                    this.output.append((CharSequence) ", ");
                } else {
                    seenFirst = true;
                }
                this.output.append((CharSequence) type);
            }
            this.output.append((CharSequence) "> ");
        }
    }

    private void writeMethodBody(FactoryMethod factoryMethod) {
        indent();
        this.output.append((CharSequence) "return ").append((CharSequence) factoryMethod.getMatcherClass());
        this.output.append('.');
        if (!factoryMethod.getGenericTypeParameters().isEmpty()) {
            this.output.append('<');
            boolean seenFirst = false;
            for (String type : factoryMethod.getGenericTypeParameters()) {
                if (seenFirst) {
                    this.output.append((CharSequence) ",");
                } else {
                    seenFirst = true;
                }
                StringTokenizer iter = new StringTokenizer(type);
                iter.hasMoreElements();
                this.output.append((CharSequence) iter.nextToken());
            }
            this.output.append((CharSequence) ">");
        }
        this.output.append((CharSequence) factoryMethod.getName());
        this.output.append('(');
        boolean seenFirst2 = false;
        for (FactoryMethod.Parameter parameter : factoryMethod.getParameters()) {
            if (seenFirst2) {
                this.output.append((CharSequence) ", ");
            } else {
                seenFirst2 = true;
            }
            this.output.append((CharSequence) parameter.getName());
        }
        this.output.append(')');
        this.output.append(';').append((CharSequence) this.newLine);
    }

    private void writeExceptions(FactoryMethod factoryMethod) {
        boolean seenFirst = false;
        for (String exception : factoryMethod.getExceptions()) {
            if (seenFirst) {
                this.output.append((CharSequence) ", ");
            } else {
                this.output.append((CharSequence) " throws ");
                seenFirst = true;
            }
            this.output.append((CharSequence) exception);
        }
    }

    private void writeParameters(FactoryMethod factoryMethod) {
        this.output.append('(');
        boolean seenFirst = false;
        for (FactoryMethod.Parameter parameter : factoryMethod.getParameters()) {
            if (seenFirst) {
                this.output.append((CharSequence) ", ");
            } else {
                seenFirst = true;
            }
            this.output.append((CharSequence) parameter.getType()).append(' ').append((CharSequence) parameter.getName());
        }
        this.output.append(')');
    }

    private void writeJavaDoc(FactoryMethod factoryMethod) {
        if (factoryMethod.getJavaDoc() != null) {
            String[] lines = factoryMethod.getJavaDoc().split("\n");
            if (lines.length > 0) {
                indent();
                this.output.append((CharSequence) "/**").append((CharSequence) this.newLine);
                for (String line : lines) {
                    indent();
                    this.output.append((CharSequence) " * ").append((CharSequence) line).append((CharSequence) this.newLine);
                }
                indent();
                this.output.append((CharSequence) " */").append((CharSequence) this.newLine);
            }
        }
    }

    private void indent() {
        for (int i = 0; i < this.indentation; i++) {
            this.output.append((CharSequence) this.indentationString);
        }
    }
}
