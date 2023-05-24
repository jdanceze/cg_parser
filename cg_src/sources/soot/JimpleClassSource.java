package soot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.javaToJimple.IInitialResolver;
import soot.jimple.JimpleMethodSource;
import soot.jimple.parser.JimpleAST;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.parser.ParserException;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/JimpleClassSource.class */
public class JimpleClassSource extends ClassSource {
    private static final Logger logger = LoggerFactory.getLogger(JimpleClassSource.class);
    private IFoundFile foundFile;

    public JimpleClassSource(String className, IFoundFile foundFile) {
        super(className);
        if (foundFile == null) {
            throw new IllegalStateException("Error: The FoundFile must not be null.");
        }
        this.foundFile = foundFile;
    }

    @Override // soot.ClassSource
    public IInitialResolver.Dependencies resolve(SootClass sc) {
        if (Options.v().verbose()) {
            logger.debug("resolving [from .jimple]: " + this.className);
        }
        InputStream classFile = null;
        try {
            try {
                try {
                    try {
                        classFile = this.foundFile.inputStream();
                        JimpleAST jimpAST = new JimpleAST(classFile);
                        jimpAST.getSkeleton(sc);
                        JimpleMethodSource mtdSrc = new JimpleMethodSource(jimpAST);
                        Iterator<SootMethod> mtdIt = sc.methodIterator();
                        while (mtdIt.hasNext()) {
                            SootMethod sm = mtdIt.next();
                            sm.setSource(mtdSrc);
                        }
                        String outerClassName = null;
                        if (!sc.hasOuterClass()) {
                            String className = sc.getName();
                            if (className.contains("$")) {
                                outerClassName = className.contains("$-") ? className.substring(0, className.indexOf("$-")) : className.substring(0, className.lastIndexOf(36));
                                sc.setOuterClass(SootResolver.v().makeClassRef(outerClassName));
                            }
                        }
                        IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
                        for (String t : jimpAST.getCstPool()) {
                            deps.typesToSignature.add(RefType.v(t));
                        }
                        if (outerClassName != null) {
                            deps.typesToSignature.add(RefType.v(outerClassName));
                        }
                        try {
                            if (classFile != null) {
                                try {
                                    classFile.close();
                                } catch (IOException e) {
                                    throw new RuntimeException("Error: Failed to close source input stream.", e);
                                }
                            }
                            return deps;
                        } finally {
                        }
                    } catch (IOException e2) {
                        throw new RuntimeException("Error: Failed to create JimpleAST from source input stream for class " + this.className + ".", e2);
                    }
                } catch (LexerException e3) {
                    throw new RuntimeException("Error: Failed when lexing class " + this.className + ".", e3);
                }
            } catch (ParserException e4) {
                throw new RuntimeException("Error: Failed when parsing class " + this.className + ".", e4);
            }
        } catch (Throwable th) {
            try {
                if (classFile != null) {
                    try {
                        classFile.close();
                    } catch (IOException e5) {
                        throw new RuntimeException("Error: Failed to close source input stream.", e5);
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
