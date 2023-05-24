package soot.asm;

import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.ClassReader;
import soot.ClassSource;
import soot.IFoundFile;
import soot.SootClass;
import soot.SootResolver;
import soot.javaToJimple.IInitialResolver;
/* loaded from: gencallgraphv3.jar:soot/asm/AsmClassSource.class */
public class AsmClassSource extends ClassSource {
    protected IFoundFile foundFile;

    /* JADX INFO: Access modifiers changed from: protected */
    public AsmClassSource(String cls, IFoundFile foundFile) {
        super(cls);
        if (foundFile == null) {
            throw new IllegalStateException("Error: The FoundFile must not be null.");
        }
        this.foundFile = foundFile;
    }

    @Override // soot.ClassSource
    public IInitialResolver.Dependencies resolve(SootClass sc) {
        InputStream d = null;
        try {
            try {
                d = this.foundFile.inputStream();
                ClassReader clsr = new ClassReader(d);
                SootClassBuilder scb = new SootClassBuilder(sc);
                clsr.accept(scb, 4);
                IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
                deps.typesToSignature.addAll(scb.deps);
                if (!sc.hasOuterClass() && this.className.contains("$")) {
                    String outerClassName = this.className.contains("$-") ? this.className.substring(0, this.className.indexOf("$-")) : this.className.substring(0, this.className.lastIndexOf(36));
                    sc.setOuterClass(SootResolver.v().makeClassRef(outerClassName));
                }
                try {
                    if (d != null) {
                        try {
                            d.close();
                        } catch (IOException e) {
                            throw new RuntimeException("Error: Failed to close source input stream.", e);
                        }
                    }
                    return deps;
                } finally {
                }
            } catch (IOException e2) {
                throw new RuntimeException("Error: Failed to create class reader from class source.", e2);
            }
        } catch (Throwable th) {
            try {
                if (d != null) {
                    try {
                        d.close();
                    } catch (IOException e3) {
                        throw new RuntimeException("Error: Failed to close source input stream.", e3);
                    }
                }
                throw th;
            } finally {
            }
        }
    }

    @Override // soot.ClassSource
    public void close() {
        if (this.foundFile != null) {
            this.foundFile.close();
            this.foundFile = null;
        }
    }
}
