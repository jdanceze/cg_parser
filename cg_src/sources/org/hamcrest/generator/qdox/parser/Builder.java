package org.hamcrest.generator.qdox.parser;

import org.hamcrest.generator.qdox.model.Annotation;
import org.hamcrest.generator.qdox.model.Type;
import org.hamcrest.generator.qdox.parser.structs.ClassDef;
import org.hamcrest.generator.qdox.parser.structs.FieldDef;
import org.hamcrest.generator.qdox.parser.structs.MethodDef;
import org.hamcrest.generator.qdox.parser.structs.PackageDef;
import org.hamcrest.generator.qdox.parser.structs.TagDef;
import org.hamcrest.generator.qdox.parser.structs.TypeDef;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/Builder.class */
public interface Builder {
    void addPackage(PackageDef packageDef);

    void addImport(String str);

    void addJavaDoc(String str);

    void addJavaDocTag(TagDef tagDef);

    void beginClass(ClassDef classDef);

    void endClass();

    void beginMethod();

    void endMethod(MethodDef methodDef);

    void addParameter(FieldDef fieldDef);

    void addField(FieldDef fieldDef);

    void addAnnotation(Annotation annotation);

    Type createType(String str, int i);

    Type createType(TypeDef typeDef);
}
