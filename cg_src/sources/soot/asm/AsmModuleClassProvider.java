package soot.asm;

import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ClassProvider;
import soot.ClassSource;
import soot.FoundFile;
import soot.IFoundFile;
import soot.ModulePathSourceLocator;
/* loaded from: gencallgraphv3.jar:soot/asm/AsmModuleClassProvider.class */
public class AsmModuleClassProvider implements ClassProvider {
    private static final Logger logger = LoggerFactory.getLogger(AsmModuleClassProvider.class);

    @Override // soot.ClassProvider
    public ClassSource find(String cls) {
        int idx = cls.lastIndexOf(58) + 1;
        String clsFile = String.valueOf(cls.substring(0, idx)) + cls.substring(idx).replace('.', '/') + ".class";
        IFoundFile file = ModulePathSourceLocator.v().lookUpInModulePath(clsFile);
        if (file == null) {
            return null;
        }
        return new AsmClassSource(cls, file);
    }

    public String getModuleName(FoundFile file) {
        final String[] moduleName = new String[1];
        ClassVisitor visitor = new ClassVisitor(524288) { // from class: soot.asm.AsmModuleClassProvider.1
            @Override // org.objectweb.asm.ClassVisitor
            public ModuleVisitor visitModule(String name, int access, String version) {
                moduleName[0] = name;
                return null;
            }
        };
        try {
            Throwable th = null;
            try {
                InputStream d = file.inputStream();
                try {
                    new ClassReader(d).accept(visitor, 4);
                    String str = moduleName[0];
                    if (d != null) {
                        d.close();
                    }
                    return str;
                } catch (Throwable th2) {
                    if (d != null) {
                        d.close();
                    }
                    throw th2;
                }
            } catch (Throwable th3) {
                if (0 == 0) {
                    th = th3;
                } else if (null != th3) {
                    th.addSuppressed(th3);
                }
                throw th;
            }
        } catch (IOException e) {
            logger.debug(e.getMessage(), (Throwable) e);
            return null;
        } finally {
            file.close();
        }
    }
}
