package polyglot.types;

import java.io.InvalidClassException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import polyglot.main.Report;
import polyglot.main.Version;
import polyglot.types.reflect.ClassFile;
import polyglot.types.reflect.ClassFileLoader;
import polyglot.types.reflect.ClassPathLoader;
import polyglot.util.CollectionUtil;
import polyglot.util.TypeEncoder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/LoadedClassResolver.class */
public class LoadedClassResolver extends ClassResolver implements TopLevelResolver {
    protected static final int NOT_COMPATIBLE = -1;
    protected static final int MINOR_NOT_COMPATIBLE = 1;
    protected static final int COMPATIBLE = 0;
    TypeSystem ts;
    TypeEncoder te;
    ClassPathLoader loader;
    Version version;
    Set nocache = new HashSet();
    boolean allowRawClasses;
    static final Collection report_topics = CollectionUtil.list(Report.types, Report.resolver, Report.loader);

    public LoadedClassResolver(TypeSystem ts, String classpath, ClassFileLoader loader, Version version, boolean allowRawClasses) {
        this.ts = ts;
        this.te = new TypeEncoder(ts);
        this.loader = new ClassPathLoader(classpath, loader);
        this.version = version;
        this.allowRawClasses = allowRawClasses;
    }

    @Override // polyglot.types.TopLevelResolver
    public boolean packageExists(String name) {
        return this.loader.packageExists(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassFile loadFile(String name) {
        ClassFile clazz;
        if (this.nocache.contains(name)) {
            return null;
        }
        try {
            clazz = this.loader.loadClass(name);
        } catch (ClassFormatError e) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Class ").append(name).append(" format error").toString());
            }
        }
        if (clazz == null) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Class ").append(name).append(" not found in classpath ").append(this.loader.classpath()).toString());
            }
            this.nocache.add(name);
            return null;
        }
        if (Report.should_report(report_topics, 4)) {
            Report.report(4, new StringBuffer().append("Class ").append(name).append(" found in classpath ").append(this.loader.classpath()).toString());
        }
        return clazz;
    }

    @Override // polyglot.types.ClassResolver, polyglot.types.Resolver
    public Named find(String name) throws SemanticException {
        if (Report.should_report(report_topics, 3)) {
            Report.report(3, new StringBuffer().append("LoadedCR.find(").append(name).append(")").toString());
        }
        ClassFile clazz = loadFile(name);
        if (clazz == null) {
            throw new NoClassException(name);
        }
        if (clazz.encodedClassType(this.version.name()) != null) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Using encoded class type for ").append(name).toString());
            }
            return getEncodedType(clazz, name);
        } else if (this.allowRawClasses) {
            if (Report.should_report(report_topics, 4)) {
                Report.report(4, new StringBuffer().append("Using raw class file for ").append(name).toString());
            }
            return clazz.type(this.ts);
        } else {
            throw new SemanticException(new StringBuffer().append("Unable to find a suitable definition of \"").append(name).append("\". A class file was found,").append(" but it did not contain appropriate information for this").append(" language extension. If the source for this file is written").append(" in the language extension, try recompiling the source code.").toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassType getEncodedType(ClassFile clazz, String name) throws SemanticException {
        int comp = checkCompilerVersion(clazz.compilerVersion(this.version.name()));
        if (comp == -1) {
            throw new SemanticException(new StringBuffer().append("Unable to find a suitable definition of ").append(clazz.name()).append(". Try recompiling or obtaining ").append(" a newer version of the class file.").toString());
        }
        try {
            ClassType dt = (ClassType) this.te.decode(clazz.encodedClassType(this.version.name()));
            ((CachingResolver) this.ts.systemResolver()).addNamed(name, dt);
            if (Report.should_report(report_topics, 2)) {
                Report.report(2, new StringBuffer().append("Returning serialized ClassType for ").append(clazz.name()).append(".").toString());
            }
            return dt;
        } catch (InvalidClassException e) {
            throw new BadSerializationException(clazz.name());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int checkCompilerVersion(String clazzVersion) {
        if (clazzVersion == null) {
            return -1;
        }
        StringTokenizer st = new StringTokenizer(clazzVersion, ".");
        try {
            int v = Integer.parseInt(st.nextToken());
            Version version = this.version;
            if (v != version.major()) {
                return -1;
            }
            int v2 = Integer.parseInt(st.nextToken());
            if (v2 != version.minor()) {
                return 1;
            }
            return 0;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
