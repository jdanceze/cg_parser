package polyglot.frontend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import polyglot.types.reflect.ClassFileLoader;
import polyglot.util.ErrorLimitError;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.util.StdErrorQueue;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/Compiler.class */
public class Compiler {
    private ExtensionInfo extensionInfo;
    private List allExtensions;
    private ErrorQueue eq;
    private ClassFileLoader loader;
    private Collection outputFiles;
    static Class class$polyglot$frontend$Compiler;

    public Compiler(ExtensionInfo extensionInfo) {
        this(extensionInfo, new StdErrorQueue(System.err, extensionInfo.getOptions().error_count, extensionInfo.compilerName()));
    }

    public Compiler(ExtensionInfo extensionInfo, ErrorQueue eq) {
        this.outputFiles = new HashSet();
        this.extensionInfo = extensionInfo;
        this.eq = eq;
        this.allExtensions = new ArrayList(2);
        this.loader = new ClassFileLoader(extensionInfo);
        extensionInfo.initCompiler(this);
    }

    public Collection outputFiles() {
        return this.outputFiles;
    }

    public boolean compile(Collection sources) {
        boolean okay = false;
        try {
            try {
                try {
                    try {
                        SourceLoader source_loader = sourceExtension().sourceLoader();
                        Iterator i = sources.iterator();
                        while (i.hasNext()) {
                            String sourceName = (String) i.next();
                            FileSource source = source_loader.fileSource(sourceName);
                            source.setUserSpecified(true);
                            sourceExtension().addJob(source);
                        }
                        okay = sourceExtension().runToCompletion();
                    } catch (InternalCompilerError e) {
                        try {
                            this.eq.enqueue(1, e.message(), e.position());
                        } catch (ErrorLimitError e2) {
                        }
                        this.eq.flush();
                        throw e;
                    }
                } catch (IOException e3) {
                    this.eq.enqueue(2, e3.getMessage());
                }
            } catch (FileNotFoundException e4) {
                this.eq.enqueue(2, new StringBuffer().append("Cannot find source file \"").append(e4.getMessage()).append("\".").toString());
            } catch (RuntimeException e5) {
                this.eq.flush();
                throw e5;
            }
        } catch (ErrorLimitError e6) {
        }
        this.eq.flush();
        for (ExtensionInfo ext : this.allExtensions) {
            ext.getStats().report();
        }
        return okay;
    }

    public ClassFileLoader loader() {
        return this.loader;
    }

    public boolean useFullyQualifiedNames() {
        return this.extensionInfo.getOptions().fully_qualified_names;
    }

    public void addExtension(ExtensionInfo ext) {
        this.allExtensions.add(ext);
    }

    public List allExtensions() {
        return this.allExtensions;
    }

    public ExtensionInfo sourceExtension() {
        return this.extensionInfo;
    }

    public int outputWidth() {
        return this.extensionInfo.getOptions().output_width;
    }

    public boolean serializeClassInfo() {
        return this.extensionInfo.getOptions().serialize_type_info;
    }

    public ErrorQueue errorQueue() {
        return this.eq;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class cls;
        try {
            if (class$polyglot$frontend$Compiler == null) {
                cls = class$("polyglot.frontend.Compiler");
                class$polyglot$frontend$Compiler = cls;
            } else {
                cls = class$polyglot$frontend$Compiler;
            }
            ClassLoader loader = cls.getClassLoader();
            loader.loadClass("polyglot.util.StdErrorQueue");
        } catch (ClassNotFoundException e) {
            throw new InternalCompilerError(e.getMessage());
        }
    }
}
