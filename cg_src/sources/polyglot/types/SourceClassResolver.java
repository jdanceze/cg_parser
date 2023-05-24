package polyglot.types;

import polyglot.frontend.Compiler;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.FileSource;
import polyglot.main.Report;
import polyglot.types.reflect.ClassFile;
import polyglot.types.reflect.ClassFileLoader;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/SourceClassResolver.class */
public class SourceClassResolver extends LoadedClassResolver {
    Compiler compiler;
    ExtensionInfo ext;

    public SourceClassResolver(Compiler compiler, ExtensionInfo ext, String classpath, ClassFileLoader loader, boolean allowRawClasses) {
        super(ext.typeSystem(), classpath, loader, ext.version(), allowRawClasses);
        this.compiler = compiler;
        this.ext = ext;
    }

    @Override // polyglot.types.LoadedClassResolver, polyglot.types.TopLevelResolver
    public boolean packageExists(String name) {
        if (super.packageExists(name)) {
            return true;
        }
        return false;
    }

    @Override // polyglot.types.LoadedClassResolver, polyglot.types.ClassResolver, polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        if (Report.should_report(report_topics, 3)) {
            Report.report(3, new StringBuffer().append("SourceCR.find(").append(name).append(")").toString());
        }
        ClassFile encodedClazz = null;
        ClassFile clazz = loadFile(name);
        if (clazz != null && clazz.encodedClassType(this.version.name()) != null) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Class ").append(name).append(" has encoded type info").toString());
            }
            encodedClazz = clazz;
        }
        if (clazz != null && !this.allowRawClasses) {
            clazz = null;
        }
        FileSource source = this.ext.sourceLoader().classSource(name);
        if (Report.should_report(report_topics, 4)) {
            if (source == null) {
                Report.report(4, new StringBuffer().append("Class ").append(name).append(" not found in source file").toString());
            } else {
                Report.report(4, new StringBuffer().append("Class ").append(name).append(" found in source ").append(source).toString());
            }
        }
        if (encodedClazz != null) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Not using raw class file for ").append(name).toString());
            }
            clazz = null;
        }
        if (clazz != null && source != null) {
            long classModTime = clazz.rawSourceLastModified();
            long sourceModTime = source.lastModified().getTime();
            if (classModTime < sourceModTime) {
                if (Report.should_report(report_topics, 3)) {
                    Report.report(3, new StringBuffer().append("Source file version is newer than compiled for ").append(name).append(".").toString());
                }
                clazz = null;
            } else {
                source = null;
            }
        }
        if (encodedClazz != null && source != null) {
            long classModTime2 = encodedClazz.sourceLastModified(this.version.name());
            long sourceModTime2 = source.lastModified().getTime();
            int comp = checkCompilerVersion(encodedClazz.compilerVersion(this.version.name()));
            if (classModTime2 < sourceModTime2) {
                if (Report.should_report(report_topics, 3)) {
                    Report.report(3, new StringBuffer().append("Source file version is newer than compiled for ").append(name).append(".").toString());
                }
                encodedClazz = null;
            } else if (comp != 0) {
                if (Report.should_report(report_topics, 3)) {
                    Report.report(3, new StringBuffer().append("Incompatible source file version for ").append(name).append(".").toString());
                }
                encodedClazz = null;
            }
        }
        if (encodedClazz != null) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Using encoded class type for ").append(name).toString());
            }
            try {
                return getEncodedType(encodedClazz, name);
            } catch (BadSerializationException e) {
                throw e;
            } catch (SemanticException e2) {
                if (Report.should_report(report_topics, 4)) {
                    Report.report(4, new StringBuffer().append("Could not load encoded class ").append(name).toString());
                }
            }
        }
        if (clazz != null) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Using raw class file for ").append(name).toString());
            }
            return clazz.type(this.ts);
        } else if (source != null) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Using source file for ").append(name).toString());
            }
            return getTypeFromSource(source, name);
        } else if (clazz != null && !this.allowRawClasses) {
            throw new SemanticException(new StringBuffer().append("Class \"").append(name).append("\" not found.").append(" A class file was found, but it did not contain appropriate").append(" information for the Polyglot-based compiler ").append(this.ext.compilerName()).append(". Try using ").append(this.ext.compilerName()).append(" to recompile the source code.").toString());
        } else {
            throw new NoClassException(name);
        }
    }

    protected Named getTypeFromSource(FileSource source, String name) throws SemanticException {
        this.ext.readSource(source);
        return this.ts.parsedResolver().find(name);
    }
}
