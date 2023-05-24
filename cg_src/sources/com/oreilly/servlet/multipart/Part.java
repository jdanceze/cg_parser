package com.oreilly.servlet.multipart;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/Part.class */
public abstract class Part {
    private String name;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Part(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isFile() {
        return false;
    }

    public boolean isParam() {
        return false;
    }
}
