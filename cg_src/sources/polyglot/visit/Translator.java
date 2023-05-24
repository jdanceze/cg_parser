package polyglot.visit;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.SourceCollection;
import polyglot.ast.SourceFile;
import polyglot.ast.TopLevelDecl;
import polyglot.frontend.Job;
import polyglot.frontend.TargetFactory;
import polyglot.main.Options;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.Package;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Copy;
import polyglot.util.InternalCompilerError;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/Translator.class */
public class Translator extends PrettyPrinter implements Copy {
    protected Job job;
    protected NodeFactory nf;
    protected TargetFactory tf;
    protected TypeSystem ts;
    protected Context context;
    protected ClassType outerClass = null;
    private static HashMap createdFiles = new HashMap();

    public static HashMap getFileNames() {
        return createdFiles;
    }

    public Translator(Job job, TypeSystem ts, NodeFactory nf, TargetFactory tf) {
        this.job = job;
        this.nf = nf;
        this.tf = tf;
        this.ts = ts;
        this.context = job.context();
        if (this.context == null) {
            this.context = ts.createContext();
        }
    }

    public Job job() {
        return this.job;
    }

    public Translator context(Context c) {
        if (c == this.context) {
            return this;
        }
        Translator tr = (Translator) copy();
        tr.context = c;
        return tr;
    }

    @Override // polyglot.util.Copy
    public Object copy() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone() weirdness.");
        }
    }

    public HeaderTranslator headerContext(Context c) {
        HeaderTranslator ht = new HeaderTranslator(this);
        ht.context = c;
        return ht;
    }

    public ClassType outerClass() {
        return this.outerClass;
    }

    public void setOuterClass(ClassType ct) {
        this.outerClass = ct;
    }

    public TypeSystem typeSystem() {
        return this.ts;
    }

    public Context context() {
        return this.context;
    }

    public NodeFactory nodeFactory() {
        return this.nf;
    }

    public TargetFactory targetFactory() {
        return this.tf;
    }

    @Override // polyglot.visit.PrettyPrinter
    public void print(Node parent, Node child, CodeWriter w) {
        Translator tr;
        if (parent != null) {
            Context c = parent.del().enterScope(child, this.context);
            tr = context(c);
        } else {
            Context c2 = child.del().enterScope(this.context);
            tr = context(c2);
        }
        child.del().translate(w, tr);
        if (parent != null) {
            parent.addDecls(this.context);
        }
    }

    public boolean translate(Node ast) {
        if (ast instanceof SourceFile) {
            SourceFile sfn = (SourceFile) ast;
            return translateSource(sfn);
        } else if (ast instanceof SourceCollection) {
            SourceCollection sc = (SourceCollection) ast;
            boolean okay = true;
            for (SourceFile sfn2 : sc.sources()) {
                okay &= translateSource(sfn2);
            }
            return okay;
        } else {
            throw new InternalCompilerError(new StringBuffer().append("AST root must be a SourceFile; found a ").append(ast.getClass().getName()).toString());
        }
    }

    protected boolean translateSource(SourceFile sfn) {
        File of;
        String className;
        typeSystem();
        nodeFactory();
        TargetFactory tf = this.tf;
        int outputWidth = this.job.compiler().outputWidth();
        Collection outputFiles = this.job.compiler().outputFiles();
        List exports = exports(sfn);
        try {
            Writer headerWriter = null;
            CodeWriter wH = null;
            String pkg = "";
            if (sfn.package_() != null) {
                Package p = sfn.package_().package_();
                pkg = p.toString();
            }
            Context c = sfn.del().enterScope(this.context);
            TopLevelDecl first = null;
            if (exports.size() == 0) {
                of = tf.outputFile(pkg, sfn.source());
            } else {
                first = (TopLevelDecl) exports.get(0);
                of = tf.outputFile(pkg, first.name(), sfn.source());
            }
            String opfPath = of.getPath();
            if (!opfPath.endsWith("$")) {
                outputFiles.add(of.getPath());
            }
            Writer ofw = tf.outputWriter(of);
            CodeWriter w = new CodeWriter(ofw, outputWidth);
            createdFiles.put(of.getPath(), null);
            if (Options.global.cppBackend()) {
                File headerFile = new File(tf.headerNameForFileName(of.getPath()));
                headerWriter = tf.outputWriter(headerFile);
                wH = new CodeWriter(headerWriter, outputWidth);
                if (!exports.isEmpty()) {
                    first = (TopLevelDecl) exports.get(0);
                    className = first.name();
                } else {
                    String name = sfn.source().name();
                    className = name.substring(0, name.lastIndexOf(46));
                }
                writeHFileHeader(sfn, className, wH);
            }
            writeHeader(sfn, w);
            Iterator i = sfn.decls().iterator();
            while (i.hasNext()) {
                TopLevelDecl decl = (TopLevelDecl) i.next();
                if (decl.flags().isPublic() && decl != first && !Options.global.cppBackend()) {
                    w.flush();
                    ofw.close();
                    File of2 = tf.outputFile(pkg, decl.name(), sfn.source());
                    outputFiles.add(of2.getPath());
                    ofw = tf.outputWriter(of2);
                    w = new CodeWriter(ofw, outputWidth);
                    writeHeader(sfn, w);
                }
                decl.del().translate(w, context(c));
                if (Options.global.cppBackend()) {
                    decl.del().translate(wH, headerContext(c));
                }
                if (i.hasNext()) {
                    w.newline(0);
                }
            }
            writeFooter(sfn, w);
            if (Options.global.cppBackend()) {
                writeHFileFooter(sfn, wH);
                wH.flush();
                headerWriter.close();
            }
            w.flush();
            ofw.close();
            return true;
        } catch (IOException e) {
            this.job.compiler().errorQueue().enqueue(2, new StringBuffer().append("I/O error while translating: ").append(e.getMessage()).toString());
            return false;
        }
    }

    public static String macroEscape(String s) {
        String stringBuffer;
        String out = "_";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.' || c == ':') {
                stringBuffer = new StringBuffer().append(out).append("_").toString();
            } else {
                stringBuffer = new StringBuffer().append(out).append(c).toString();
            }
            out = stringBuffer;
        }
        return out;
    }

    public static String cScope(String s) {
        String stringBuffer;
        String out = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') {
                stringBuffer = new StringBuffer().append(out).append("::").toString();
            } else {
                stringBuffer = new StringBuffer().append(out).append(c).toString();
            }
            out = stringBuffer;
        }
        return out;
    }

    protected void writeHFileHeader(SourceFile sfn, String className, CodeWriter w) {
        String pkg = null;
        if (sfn.package_() != null) {
            Package p = sfn.package_().package_();
            pkg = p.fullName();
        }
        pkg = (pkg == null || pkg.equals("")) ? "jmatch_primary" : "jmatch_primary";
        String macroName = new StringBuffer().append("_").append(macroEscape(pkg)).append("_").append(macroEscape(className)).append("_H").toString();
        w.write(new StringBuffer().append("#ifndef ").append(macroName).toString());
        w.newline(0);
        w.write(new StringBuffer().append("#define ").append(macroName).toString());
        w.newline(0);
        if (sfn.package_() != null) {
            sfn.package_().del().translate(w, this);
        } else {
            w.write(new StringBuffer().append("namespace ").append(cScope(pkg)).append(" {").toString());
        }
        w.newline(0);
        w.write("using namespace jmatch_primary;");
        w.newline(0);
        w.write("using namespace java::lang;");
        w.newline(0);
        for (Import imp : sfn.imports()) {
            imp.del().translate(w, this);
            w.newline(0);
        }
    }

    protected void writeHFileFooter(SourceFile sfn, CodeWriter w) {
        int packageDepth = 0;
        if (null != sfn.package_()) {
            Package p = sfn.package_().package_();
            String pkgName = p.toString();
            if (pkgName.length() > 0) {
                packageDepth = 0 + 1;
            }
            for (int i = 0; i < pkgName.length(); i++) {
                if (pkgName.charAt(i) == '.') {
                    packageDepth++;
                }
            }
            w.write("/* closing namespace */");
            w.newline(0);
            for (int i2 = 0; i2 < packageDepth; i2++) {
                w.write("}");
            }
            w.newline(0);
            w.newline(0);
        }
        if (packageDepth == 0) {
            w.newline(0);
            w.write("} /* namespace */");
            w.newline(0);
            w.newline(0);
        }
        w.write("#endif");
        w.newline(0);
        w.newline(0);
    }

    protected void writeFooter(SourceFile sfn, CodeWriter w) {
        if (Options.global.cppBackend()) {
            int packageDepth = 0;
            if (null != sfn.package_()) {
                Package p = sfn.package_().package_();
                String pkgName = p.toString();
                if (pkgName.length() > 0) {
                    packageDepth = 0 + 1;
                }
                for (int i = 0; i < pkgName.length(); i++) {
                    if (pkgName.charAt(i) == '.') {
                        packageDepth++;
                    }
                }
                w.write("/* closing namespace */");
                w.newline(0);
                for (int i2 = 0; i2 < packageDepth; i2++) {
                    w.write("}");
                }
                w.newline(0);
                w.newline(0);
            }
            if (packageDepth == 0) {
                w.newline(0);
                w.write("} /* namespace */");
                w.newline(0);
                w.newline(0);
            }
        }
    }

    protected void writeHeader(SourceFile sfn, CodeWriter w) {
        if (Options.global.cppBackend()) {
            String pkg = "";
            if (sfn.package_() != null) {
                Package p = sfn.package_().package_();
                pkg = new StringBuffer().append(p.toString()).append(".").toString();
            }
            int dots = 0;
            for (int i = 0; i < pkg.length(); i++) {
                if (pkg.charAt(i) == '.') {
                    dots++;
                }
            }
            w.write("#include\"");
            for (int i2 = 0; i2 < dots; i2++) {
                w.write("../");
            }
            w.write("mainproj.h\"");
            w.newline(0);
            if (null != sfn.package_()) {
                sfn.package_().del().translate(w, this);
                w.newline(0);
                w.newline(0);
            } else {
                w.write("namespace jmatch_primary {");
                w.newline(0);
                w.newline(0);
            }
            w.write("using namespace jmatch_primary;");
            w.newline(0);
            w.write("using namespace java::lang;");
            w.newline(0);
            for (Import imp : sfn.imports()) {
                imp.del().translate(w, this);
                w.newline(0);
            }
            return;
        }
        if (sfn.package_() != null) {
            w.write("package ");
            sfn.package_().del().translate(w, this);
            w.write(";");
            w.newline(0);
            w.newline(0);
        }
        boolean newline = false;
        for (Import imp2 : sfn.imports()) {
            imp2.del().translate(w, this);
            newline = true;
        }
        if (newline) {
            w.newline(0);
        }
    }

    protected List exports(SourceFile sfn) {
        List exports = new LinkedList();
        for (TopLevelDecl decl : sfn.decls()) {
            if (decl.flags().isPublic()) {
                exports.add(decl);
            }
        }
        return exports;
    }

    public String toString() {
        return "Translator";
    }
}
