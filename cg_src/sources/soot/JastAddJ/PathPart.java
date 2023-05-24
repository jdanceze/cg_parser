package soot.JastAddJ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PathPart.class */
public class PathPart {
    protected InputStream is;
    protected String pathName;
    protected String relativeName;
    protected String fullName;
    protected long age;
    protected Program program;
    protected boolean isSource;

    /* JADX INFO: Access modifiers changed from: protected */
    public String fileSuffix() {
        return this.isSource ? ".java" : ".class";
    }

    public static PathPart createSourcePath(String fileName, Program program) {
        PathPart p = createPathPart(fileName);
        if (p != null) {
            p.isSource = true;
            p.program = program;
        }
        return p;
    }

    public static PathPart createClassPath(String fileName, Program program) {
        PathPart p = createPathPart(fileName);
        if (p != null) {
            p.isSource = false;
            p.program = program;
        }
        return p;
    }

    private static PathPart createPathPart(String s) {
        try {
            File f = new File(s);
            if (f.isDirectory()) {
                return new FolderPart(f);
            }
            if (f.isFile()) {
                return new ZipFilePart(f);
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public InputStream getInputStream() {
        return this.is;
    }

    public long getAge() {
        return this.age;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public boolean hasPackage(String name) {
        return false;
    }

    public boolean selectCompilationUnit(String canonicalName) throws IOException {
        return false;
    }

    public CompilationUnit getCompilationUnit() {
        long startTime = System.currentTimeMillis();
        try {
            try {
                if (!this.isSource) {
                    if (this.program.options().verbose()) {
                        System.out.print("Loading .class file: " + this.fullName + Instruction.argsep);
                    }
                    CompilationUnit u = this.program.bytecodeReader.read(this.is, this.fullName, this.program);
                    u.setPathName(this.pathName);
                    u.setRelativeName(this.relativeName);
                    u.setFromSource(false);
                    if (this.program.options().verbose()) {
                        System.out.println("from " + this.pathName + " in " + (System.currentTimeMillis() - startTime) + " ms");
                    }
                    try {
                        if (this.is != null) {
                            this.is.close();
                            this.is = null;
                        }
                        return u;
                    } catch (Exception e) {
                        throw new Error("Error: Failed to close input stream for " + this.fullName + ".", e);
                    }
                }
                if (this.program.options().verbose()) {
                    System.out.print("Loading .java file: " + this.fullName + Instruction.argsep);
                }
                CompilationUnit u2 = this.program.javaParser.parse(this.is, this.fullName);
                u2.setPathName(this.pathName);
                u2.setRelativeName(this.relativeName);
                u2.setFromSource(true);
                if (this.program.options().verbose()) {
                    System.out.println("in " + (System.currentTimeMillis() - startTime) + " ms");
                }
                try {
                    if (this.is != null) {
                        this.is.close();
                        this.is = null;
                    }
                    return u2;
                } catch (Exception e2) {
                    throw new Error("Error: Failed to close input stream for " + this.fullName + ".", e2);
                }
            } catch (Exception e3) {
                throw new Error("Error: Failed to load " + this.fullName + ".", e3);
            }
        } catch (Throwable th) {
            try {
                if (this.is != null) {
                    this.is.close();
                    this.is = null;
                }
                throw th;
            } catch (Exception e4) {
                throw new Error("Error: Failed to close input stream for " + this.fullName + ".", e4);
            }
        }
    }
}
