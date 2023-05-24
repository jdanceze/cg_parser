package org.mockito.internal.util.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.mockito.exceptions.base.MockitoException;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/io/IOUtil.class */
public class IOUtil {
    public static void writeText(String text, File output) {
        PrintWriter pw = null;
        try {
            try {
                pw = new PrintWriter(new FileWriter(output));
                pw.write(text);
                close(pw);
            } catch (Exception e) {
                throw new MockitoException("Problems writing text to file: " + output, e);
            }
        } catch (Throwable th) {
            close(pw);
            throw th;
        }
    }

    public static Collection<String> readLines(InputStream is) {
        List<String> out = new LinkedList<>();
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                String line = r.readLine();
                if (line != null) {
                    out.add(line);
                } else {
                    return out;
                }
            } catch (IOException e) {
                throw new MockitoException("Problems reading from: " + is, e);
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            close(closeable);
        } catch (MockitoException e) {
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new MockitoException("Problems closing stream: " + closeable, e);
            }
        }
    }
}
