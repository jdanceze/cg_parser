package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/BZip2Resource.class */
public class BZip2Resource extends CompressedResource {
    private static final char[] MAGIC = {'B', 'Z'};

    public BZip2Resource() {
    }

    public BZip2Resource(ResourceCollection other) {
        super(other);
    }

    @Override // org.apache.tools.ant.types.resources.ContentTransformingResource
    protected InputStream wrapStream(InputStream in) throws IOException {
        char[] cArr;
        for (char ch : MAGIC) {
            if (in.read() != ch) {
                throw new IOException("Invalid bz2 stream.");
            }
        }
        return new CBZip2InputStream(in);
    }

    @Override // org.apache.tools.ant.types.resources.ContentTransformingResource
    protected OutputStream wrapStream(OutputStream out) throws IOException {
        char[] cArr;
        for (char ch : MAGIC) {
            out.write(ch);
        }
        return new CBZip2OutputStream(out);
    }

    @Override // org.apache.tools.ant.types.resources.CompressedResource
    protected String getCompressionName() {
        return "Bzip2";
    }
}
