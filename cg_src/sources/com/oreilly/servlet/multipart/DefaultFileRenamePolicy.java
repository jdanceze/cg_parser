package com.oreilly.servlet.multipart;

import java.io.File;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/DefaultFileRenamePolicy.class */
public class DefaultFileRenamePolicy implements FileRenamePolicy {
    @Override // com.oreilly.servlet.multipart.FileRenamePolicy
    public File rename(File f) {
        String body;
        String ext;
        if (createNewFile(f)) {
            return f;
        }
        String name = f.getName();
        int dot = name.lastIndexOf(".");
        if (dot != -1) {
            body = name.substring(0, dot);
            ext = name.substring(dot);
        } else {
            body = name;
            ext = "";
        }
        int count = 0;
        while (!createNewFile(f) && count < 9999) {
            count++;
            String newName = new StringBuffer().append(body).append(count).append(ext).toString();
            f = new File(f.getParent(), newName);
        }
        return f;
    }

    private boolean createNewFile(File f) {
        try {
            return f.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }
}
