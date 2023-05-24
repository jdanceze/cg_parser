package org.hamcrest.generator.qdox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.hamcrest.generator.qdox.directorywalker.DirectoryScanner;
import org.hamcrest.generator.qdox.directorywalker.FileVisitor;
import org.hamcrest.generator.qdox.directorywalker.SuffixFilter;
import org.hamcrest.generator.qdox.model.ClassLibrary;
import org.hamcrest.generator.qdox.model.DefaultDocletTagFactory;
import org.hamcrest.generator.qdox.model.DocletTagFactory;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaPackage;
import org.hamcrest.generator.qdox.model.JavaSource;
import org.hamcrest.generator.qdox.model.ModelBuilder;
import org.hamcrest.generator.qdox.parser.Lexer;
import org.hamcrest.generator.qdox.parser.ParseException;
import org.hamcrest.generator.qdox.parser.impl.JFlexLexer;
import org.hamcrest.generator.qdox.parser.impl.Parser;
import org.hamcrest.generator.qdox.parser.structs.ClassDef;
import org.hamcrest.generator.qdox.parser.structs.FieldDef;
import org.hamcrest.generator.qdox.parser.structs.MethodDef;
import org.hamcrest.generator.qdox.parser.structs.PackageDef;
import org.hamcrest.generator.qdox.parser.structs.TypeDef;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/JavaDocBuilder.class */
public class JavaDocBuilder implements Serializable {
    private final JavaClassContext context;
    private Set packages;
    private List sources;
    private DocletTagFactory docletTagFactory;
    private String encoding;
    private boolean debugLexer;
    private boolean debugParser;
    private ErrorHandler errorHandler;

    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/JavaDocBuilder$ErrorHandler.class */
    public interface ErrorHandler {
        void handle(ParseException parseException);
    }

    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/JavaDocBuilder$DefaultErrorHandler.class */
    public static class DefaultErrorHandler implements ErrorHandler, Serializable {
        @Override // org.hamcrest.generator.qdox.JavaDocBuilder.ErrorHandler
        public void handle(ParseException parseException) {
            throw parseException;
        }
    }

    public JavaDocBuilder() {
        this(new DefaultDocletTagFactory());
    }

    public JavaDocBuilder(DocletTagFactory docletTagFactory) {
        this.packages = new HashSet();
        this.sources = new ArrayList();
        this.encoding = System.getProperty("file.encoding");
        this.errorHandler = new DefaultErrorHandler();
        this.docletTagFactory = docletTagFactory;
        ClassLibrary classLibrary = new ClassLibrary();
        classLibrary.addDefaultLoader();
        this.context = new JavaClassContext(this);
        this.context.setClassLibrary(classLibrary);
    }

    public JavaDocBuilder(ClassLibrary classLibrary) {
        this(new DefaultDocletTagFactory(), classLibrary);
    }

    public JavaDocBuilder(DocletTagFactory docletTagFactory, ClassLibrary classLibrary) {
        this.packages = new HashSet();
        this.sources = new ArrayList();
        this.encoding = System.getProperty("file.encoding");
        this.errorHandler = new DefaultErrorHandler();
        this.docletTagFactory = docletTagFactory;
        this.context = new JavaClassContext(this);
        this.context.setClassLibrary(classLibrary);
    }

    private void addClasses(JavaSource source) {
        Set resultSet = new HashSet();
        addClassesRecursive(source, resultSet);
        JavaClass[] javaClasses = (JavaClass[]) resultSet.toArray(new JavaClass[resultSet.size()]);
        for (JavaClass cls : javaClasses) {
            addClass(cls);
        }
    }

    private void addClass(JavaClass cls) {
        this.context.add(cls);
        cls.setJavaClassContext(this.context);
    }

    public JavaClass getClassByName(String name) {
        if (name == null) {
            return null;
        }
        return this.context.getClassByName(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JavaClass createSourceClass(String name) {
        File sourceFile = this.context.getClassLibrary().getSourceFile(name);
        if (sourceFile != null) {
            try {
                JavaSource source = addSource(sourceFile);
                for (int index = 0; index < source.getClasses().length; index++) {
                    JavaClass clazz = source.getClasses()[index];
                    if (name.equals(clazz.getFullyQualifiedName())) {
                        return clazz;
                    }
                }
                return source.getNestedClassByName(name);
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e2) {
                return null;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JavaClass createUnknownClass(String name) {
        ModelBuilder unknownBuilder = new ModelBuilder(this.context, this.docletTagFactory, new HashMap());
        ClassDef classDef = new ClassDef();
        classDef.name = name;
        unknownBuilder.beginClass(classDef);
        unknownBuilder.endClass();
        JavaSource unknownSource = unknownBuilder.getSource();
        JavaClass result = unknownSource.getClasses()[0];
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JavaClass createBinaryClass(String name) {
        Class clazz = this.context.getClass(name);
        if (clazz == null) {
            return null;
        }
        try {
            ModelBuilder binaryBuilder = new ModelBuilder(this.context, this.docletTagFactory, new HashMap());
            String packageName = getPackageName(name);
            binaryBuilder.addPackage(new PackageDef(packageName));
            ClassDef classDef = new ClassDef();
            classDef.name = getClassName(name);
            Class[] interfaces = clazz.getInterfaces();
            if (clazz.isInterface()) {
                classDef.type = "interface";
                for (Class anInterface : interfaces) {
                    classDef.extendz.add(new TypeDef(anInterface.getName()));
                }
            } else {
                for (Class anInterface2 : interfaces) {
                    classDef.implementz.add(new TypeDef(anInterface2.getName()));
                }
                Class superclass = clazz.getSuperclass();
                if (superclass != null) {
                    classDef.extendz.add(new TypeDef(superclass.getName()));
                }
            }
            addModifiers(classDef.modifiers, clazz.getModifiers());
            binaryBuilder.beginClass(classDef);
            Constructor[] constructors = clazz.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                addMethodOrConstructor(constructor, binaryBuilder);
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                addMethodOrConstructor(method, binaryBuilder);
            }
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                addField(field, binaryBuilder);
            }
            binaryBuilder.endClass();
            JavaSource binarySource = binaryBuilder.getSource();
            JavaClass result = binarySource.getClasses()[0];
            return result;
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    private void addModifiers(Set set, int modifier) {
        String modifierString = Modifier.toString(modifier);
        StringTokenizer stringTokenizer = new StringTokenizer(modifierString);
        while (stringTokenizer.hasMoreTokens()) {
            set.add(stringTokenizer.nextToken());
        }
    }

    private void addField(Field field, ModelBuilder binaryBuilder) {
        FieldDef fieldDef = new FieldDef();
        Class fieldType = field.getType();
        fieldDef.name = field.getName();
        fieldDef.type = getTypeDef(fieldType);
        fieldDef.dimensions = getDimension(fieldType);
        addModifiers(fieldDef.modifiers, field.getModifiers());
        binaryBuilder.addField(fieldDef);
    }

    private void addMethodOrConstructor(Member member, ModelBuilder binaryBuilder) {
        Class[] exceptions;
        Class[] parameterTypes;
        MethodDef methodDef = new MethodDef();
        int lastDot = member.getName().lastIndexOf(46);
        methodDef.name = member.getName().substring(lastDot + 1);
        addModifiers(methodDef.modifiers, member.getModifiers());
        if (member instanceof Method) {
            methodDef.constructor = false;
            exceptions = ((Method) member).getExceptionTypes();
            parameterTypes = ((Method) member).getParameterTypes();
            Class returnType = ((Method) member).getReturnType();
            methodDef.returnType = getTypeDef(returnType);
            methodDef.dimensions = getDimension(returnType);
        } else {
            methodDef.constructor = true;
            exceptions = ((Constructor) member).getExceptionTypes();
            parameterTypes = ((Constructor) member).getParameterTypes();
        }
        for (Class exception : exceptions) {
            methodDef.exceptions.add(exception.getName());
        }
        binaryBuilder.addMethod(methodDef);
        for (int j = 0; j < parameterTypes.length; j++) {
            FieldDef param = new FieldDef();
            Class parameterType = parameterTypes[j];
            param.name = new StringBuffer().append("p").append(j).toString();
            param.type = getTypeDef(parameterType);
            param.dimensions = getDimension(parameterType);
            binaryBuilder.addParameter(param);
        }
    }

    private static final int getDimension(Class c) {
        return c.getName().lastIndexOf(91) + 1;
    }

    private static String getTypeName(Class c) {
        return c.getComponentType() != null ? c.getComponentType().getName() : c.getName();
    }

    private static TypeDef getTypeDef(Class c) {
        return new TypeDef(getTypeName(c));
    }

    private String getPackageName(String fullClassName) {
        int lastDot = fullClassName.lastIndexOf(46);
        return lastDot == -1 ? "" : fullClassName.substring(0, lastDot);
    }

    private String getClassName(String fullClassName) {
        int lastDot = fullClassName.lastIndexOf(46);
        return lastDot == -1 ? fullClassName : fullClassName.substring(lastDot + 1);
    }

    public JavaSource addSource(Reader reader) {
        return addSource(reader, "UNKNOWN SOURCE");
    }

    public JavaSource addSource(Reader reader, String sourceInfo) {
        ModelBuilder builder = new ModelBuilder(this.context, this.docletTagFactory, null);
        Lexer lexer = new JFlexLexer(reader);
        Parser parser = new Parser(lexer, builder);
        parser.setDebugLexer(this.debugLexer);
        parser.setDebugParser(this.debugParser);
        try {
            parser.parse();
        } catch (ParseException e) {
            e.setSourceInfo(sourceInfo);
            this.errorHandler.handle(e);
        }
        JavaSource source = builder.getSource();
        this.sources.add(source);
        addClasses(source);
        JavaPackage pkg = this.context.getPackageByName(source.getPackageName());
        if (!this.packages.contains(pkg)) {
            this.packages.add(pkg);
        }
        return source;
    }

    public JavaSource addSource(File file) throws IOException, FileNotFoundException {
        return addSource(file.toURL());
    }

    public JavaSource addSource(URL url) throws IOException, FileNotFoundException {
        JavaSource source = addSource(new InputStreamReader(url.openStream(), this.encoding), url.toExternalForm());
        source.setURL(url);
        return source;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public JavaSource[] getSources() {
        return (JavaSource[]) this.sources.toArray(new JavaSource[this.sources.size()]);
    }

    public JavaClass[] getClasses() {
        Set resultSet = new HashSet();
        JavaSource[] javaSources = getSources();
        for (JavaSource javaSource : javaSources) {
            addClassesRecursive(javaSource, resultSet);
        }
        JavaClass[] result = (JavaClass[]) resultSet.toArray(new JavaClass[resultSet.size()]);
        return result;
    }

    public JavaPackage[] getPackages() {
        return (JavaPackage[]) this.packages.toArray(new JavaPackage[this.packages.size()]);
    }

    private void addClassesRecursive(JavaSource javaSource, Set resultSet) {
        JavaClass[] classes = javaSource.getClasses();
        for (JavaClass javaClass : classes) {
            addClassesRecursive(javaClass, resultSet);
        }
    }

    private void addClassesRecursive(JavaClass javaClass, Set set) {
        set.add(javaClass);
        JavaClass[] innerClasses = javaClass.getNestedClasses();
        for (JavaClass innerClass : innerClasses) {
            addClassesRecursive(innerClass, set);
        }
    }

    public void addSourceTree(File file) {
        FileVisitor errorHandler = new FileVisitor(this) { // from class: org.hamcrest.generator.qdox.JavaDocBuilder.1
            private final JavaDocBuilder this$0;

            {
                this.this$0 = this;
            }

            @Override // org.hamcrest.generator.qdox.directorywalker.FileVisitor
            public void visitFile(File badFile) {
                throw new RuntimeException(new StringBuffer().append("Cannot read file : ").append(badFile.getName()).toString());
            }
        };
        addSourceTree(file, errorHandler);
    }

    public void addSourceTree(File file, FileVisitor errorHandler) {
        DirectoryScanner scanner = new DirectoryScanner(file);
        scanner.addFilter(new SuffixFilter(".java"));
        scanner.scan(new FileVisitor(this, errorHandler) { // from class: org.hamcrest.generator.qdox.JavaDocBuilder.2
            private final FileVisitor val$errorHandler;
            private final JavaDocBuilder this$0;

            {
                this.this$0 = this;
                this.val$errorHandler = errorHandler;
            }

            @Override // org.hamcrest.generator.qdox.directorywalker.FileVisitor
            public void visitFile(File currentFile) {
                try {
                    this.this$0.addSource(currentFile);
                } catch (IOException e) {
                    this.val$errorHandler.visitFile(currentFile);
                }
            }
        });
    }

    public List search(Searcher searcher) {
        List results = new LinkedList();
        for (String clsName : this.context.getClassLibrary().all()) {
            JavaClass cls = getClassByName(clsName);
            if (searcher.eval(cls)) {
                results.add(cls);
            }
        }
        return results;
    }

    public ClassLibrary getClassLibrary() {
        return this.context.getClassLibrary();
    }

    public void save(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        try {
            out.writeObject(this);
            out.close();
            fos.close();
        } catch (Throwable th) {
            out.close();
            fos.close();
            throw th;
        }
    }

    public static JavaDocBuilder load(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fis);
        try {
            try {
                JavaDocBuilder builder = (JavaDocBuilder) in.readObject();
                in.close();
                fis.close();
                return builder;
            } catch (ClassNotFoundException e) {
                throw new Error(new StringBuffer().append("Couldn't load class : ").append(e.getMessage()).toString());
            }
        } catch (Throwable th) {
            in.close();
            fis.close();
            throw th;
        }
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setDebugLexer(boolean debugLexer) {
        this.debugLexer = debugLexer;
    }

    public void setDebugParser(boolean debugParser) {
        this.debugParser = debugParser;
    }

    public JavaPackage getPackageByName(String name) {
        if (name != null) {
            for (JavaPackage pkg : this.packages) {
                if (name.equals(pkg.getName())) {
                    return pkg;
                }
            }
            return null;
        }
        return null;
    }
}
