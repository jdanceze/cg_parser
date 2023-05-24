package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/CVSPass.class */
public class CVSPass extends Task {
    private File passFile;
    private String cvsRoot = null;
    private String password = null;
    private final char[] shifts = {0, 1, 2, 3, 4, 5, 6, 7, '\b', '\t', '\n', 11, '\f', '\r', 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 'r', 'x', '5', 'O', '`', 'm', 'H', 'l', 'F', '@', 'L', 'C', 't', 'J', 'D', 'W', 'o', '4', 'K', 'w', '1', '\"', 'R', 'Q', '_', 'A', 'p', 'V', 'v', 'n', 'z', 'i', ')', '9', 'S', '+', '.', 'f', '(', 'Y', '&', 'g', '-', '2', '*', '{', '[', '#', '}', '7', '6', 'B', '|', '~', ';', '/', '\\', 'G', 's', 'N', 'X', 'k', 'j', '8', '$', 'y', 'u', 'h', 'e', 'd', 'E', 'I', 'c', '?', '^', ']', '\'', '%', '=', '0', ':', 'q', ' ', 'Z', ',', 'b', '<', '3', '!', 'a', '>', 'M', 'T', 'P', 'U', 223, 225, 216, 187, 166, 229, 189, 222, 188, 141, 249, 148, 200, 184, 136, 248, 190, 199, 170, 181, 204, 138, 232, 218, 183, 255, 234, 220, 247, 213, 203, 226, 193, 174, 172, 228, 252, 217, 201, 131, 230, 197, 211, 145, 238, 161, 179, 160, 212, 207, 221, 254, 173, 202, 146, 224, 151, 140, 196, 205, 130, 135, 133, 143, 246, 192, 159, 244, 239, 185, 168, 215, 144, 139, 165, 180, 157, 147, 186, 214, 176, 227, 231, 219, 169, 175, 156, 206, 198, 129, 164, 150, 210, 154, 177, 134, 127, 182, 128, 158, 208, 162, 132, 167, 209, 149, 241, 153, 251, 237, 236, 171, 195, 243, 233, 253, 240, 194, 250, 191, 155, 142, 137, 245, 235, 163, 242, 178, 152};

    public CVSPass() {
        this.passFile = null;
        this.passFile = new File(System.getProperty("cygwin.user.home", System.getProperty(Launcher.USER_HOMEDIR)) + File.separatorChar + ".cvspass");
    }

    @Override // org.apache.tools.ant.Task
    public final void execute() throws BuildException {
        if (this.cvsRoot == null) {
            throw new BuildException("cvsroot is required");
        }
        if (this.password == null) {
            throw new BuildException("password is required");
        }
        log("cvsRoot: " + this.cvsRoot, 4);
        log("password: " + this.password, 4);
        log("passFile: " + this.passFile, 4);
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            try {
                StringBuilder buf = new StringBuilder();
                if (this.passFile.exists()) {
                    reader = new BufferedReader(new FileReader(this.passFile));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        } else if (!line.startsWith(this.cvsRoot)) {
                            buf.append(line).append(System.lineSeparator());
                        }
                    }
                }
                String pwdfile = buf.toString() + this.cvsRoot + " A" + mangle(this.password);
                log("Writing -> " + pwdfile, 4);
                writer = new BufferedWriter(new FileWriter(this.passFile));
                writer.write(pwdfile);
                writer.newLine();
                FileUtils.close((Reader) reader);
                FileUtils.close((Writer) writer);
            } catch (IOException e) {
                throw new BuildException(e);
            }
        } catch (Throwable th) {
            FileUtils.close((Reader) reader);
            FileUtils.close((Writer) writer);
            throw th;
        }
    }

    private final String mangle(String password) {
        char[] charArray;
        StringBuilder buf = new StringBuilder();
        for (char ch : password.toCharArray()) {
            buf.append(this.shifts[ch]);
        }
        return buf.toString();
    }

    public void setCvsroot(String cvsRoot) {
        this.cvsRoot = cvsRoot;
    }

    public void setPassfile(File passFile) {
        this.passFile = passFile;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
