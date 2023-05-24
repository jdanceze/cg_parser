package com.oreilly.servlet.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletInputStream;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/FilePart.class */
public class FilePart extends Part {
    private String fileName;
    private String filePath;
    private String contentType;
    private PartInputStream partInput;
    private FileRenamePolicy policy;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.multipart.FilePart.writeTo(java.io.File):long, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/FilePart.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    public long writeTo(java.io.File r1) throws java.io.IOException {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.multipart.FilePart.writeTo(java.io.File):long, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/FilePart.class
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oreilly.servlet.multipart.FilePart.writeTo(java.io.File):long");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FilePart(String name, ServletInputStream in, String boundary, String contentType, String fileName, String filePath) throws IOException {
        super(name);
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.partInput = new PartInputStream(in, boundary);
    }

    public void setRenamePolicy(FileRenamePolicy policy) {
        this.policy = policy;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getContentType() {
        return this.contentType;
    }

    public InputStream getInputStream() {
        return this.partInput;
    }

    public long writeTo(OutputStream out) throws IOException {
        long size = 0;
        if (this.fileName != null) {
            size = write(out);
        }
        return size;
    }

    long write(OutputStream out) throws IOException {
        if (this.contentType.equals("application/x-macbinary")) {
            out = new MacBinaryDecoderOutputStream(out);
        }
        long size = 0;
        byte[] buf = new byte[8192];
        while (true) {
            int read = this.partInput.read(buf);
            if (read != -1) {
                out.write(buf, 0, read);
                size += read;
            } else {
                return size;
            }
        }
    }

    @Override // com.oreilly.servlet.multipart.Part
    public boolean isFile() {
        return true;
    }
}
