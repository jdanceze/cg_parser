package com.oreilly.servlet;

import java.io.File;
/* compiled from: MultipartRequest.java */
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/UploadedFile.class */
class UploadedFile {
    private String dir;
    private String filename;
    private String original;
    private String type;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UploadedFile(String dir, String filename, String original, String type) {
        this.dir = dir;
        this.filename = filename;
        this.original = original;
        this.type = type;
    }

    public String getContentType() {
        return this.type;
    }

    public String getFilesystemName() {
        return this.filename;
    }

    public String getOriginalFileName() {
        return this.original;
    }

    public File getFile() {
        if (this.dir == null || this.filename == null) {
            return null;
        }
        return new File(new StringBuffer().append(this.dir).append(File.separator).append(this.filename).toString());
    }
}
