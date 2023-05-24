package soot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.coffi.Util;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
import soot.tagkit.SourceFileTag;
/* loaded from: gencallgraphv3.jar:soot/CoffiClassSource.class */
public class CoffiClassSource extends ClassSource {
    private static final Logger logger = LoggerFactory.getLogger(CoffiClassSource.class);
    private IFoundFile foundFile;
    private InputStream classFile;
    private final String fileName;
    private final String zipFileName;

    public CoffiClassSource(String className, IFoundFile foundFile) {
        super(className);
        if (foundFile == null) {
            throw new IllegalStateException("Error: The FoundFile must not be null.");
        }
        this.foundFile = foundFile;
        this.classFile = foundFile.inputStream();
        this.fileName = foundFile.getFile().getAbsolutePath();
        this.zipFileName = !foundFile.isZipFile() ? null : foundFile.getFilePath();
    }

    public CoffiClassSource(String className, InputStream classFile, String fileName) {
        super(className);
        if (classFile == null || fileName == null) {
            throw new IllegalStateException("Error: The class file input strean and file name must not be null.");
        }
        this.classFile = classFile;
        this.fileName = fileName;
        this.zipFileName = null;
        this.foundFile = null;
    }

    @Override // soot.ClassSource
    public IInitialResolver.Dependencies resolve(SootClass sc) {
        if (Options.v().verbose()) {
            logger.debug("resolving [from .class]: " + this.className);
        }
        List<Type> references = new ArrayList<>();
        try {
            Util.v().resolveFromClassFile(sc, this.classFile, this.fileName, references);
            close();
            addSourceFileTag(sc);
            IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
            deps.typesToSignature.addAll(references);
            return deps;
        } catch (Throwable th) {
            close();
            throw th;
        }
    }

    private void addSourceFileTag(SootClass sc) {
        if (this.fileName == null && this.zipFileName == null) {
            return;
        }
        SourceFileTag tag = (SourceFileTag) sc.getTag(SourceFileTag.NAME);
        if (tag == null) {
            tag = new SourceFileTag();
            sc.addTag(tag);
        }
        if (tag.getSourceFile() == null) {
            String name = this.zipFileName == null ? new File(this.fileName).getName() : new File(this.zipFileName).getName();
            tag.setSourceFile(name);
        }
    }

    @Override // soot.ClassSource
    public void close() {
        try {
            try {
                if (this.classFile != null) {
                    this.classFile.close();
                    this.classFile = null;
                }
            } catch (IOException e) {
                throw new RuntimeException("Error: Failed to close source input stream.", e);
            }
        } finally {
            if (this.foundFile != null) {
                this.foundFile.close();
                this.foundFile = null;
            }
        }
    }
}
