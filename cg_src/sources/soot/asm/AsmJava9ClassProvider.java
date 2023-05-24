package soot.asm;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ClassProvider;
import soot.ClassSource;
import soot.IFoundFile;
import soot.ModulePathSourceLocator;
/* loaded from: gencallgraphv3.jar:soot/asm/AsmJava9ClassProvider.class */
public class AsmJava9ClassProvider implements ClassProvider {
    private static final Logger logger = LoggerFactory.getLogger(AsmJava9ClassProvider.class);

    /* JADX WARN: Finally extract failed */
    @Override // soot.ClassProvider
    public ClassSource find(String cls) {
        String clsFile = String.valueOf(cls.replace('.', '/')) + ".class";
        IFoundFile file = null;
        Path p = ModulePathSourceLocator.getRootModulesPathOfJDK();
        Throwable th = null;
        try {
            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream(p);
                try {
                    for (Path entry : stream) {
                        file = ModulePathSourceLocator.v().lookUpInVirtualFileSystem(entry.toUri().toString(), clsFile);
                        if (file != null) {
                            break;
                        }
                    }
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Throwable th2) {
                    if (stream != null) {
                        stream.close();
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
        } catch (FileSystemNotFoundException e2) {
            logger.debug("Could not read my modules (perhaps not Java 9?).");
        }
        if (file == null) {
            return null;
        }
        return new AsmClassSource(cls, file);
    }
}
