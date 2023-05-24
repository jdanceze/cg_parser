package polyglot.types;

import java.util.List;
import polyglot.util.Copy;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/Context.class */
public interface Context extends Resolver, Copy {
    TypeSystem typeSystem();

    void addVariable(VarInstance varInstance);

    void addMethod(MethodInstance methodInstance);

    void addNamed(Named named);

    MethodInstance findMethod(String str, List list) throws SemanticException;

    VarInstance findVariable(String str) throws SemanticException;

    VarInstance findVariableSilent(String str);

    LocalInstance findLocal(String str) throws SemanticException;

    FieldInstance findField(String str) throws SemanticException;

    ClassType findFieldScope(String str) throws SemanticException;

    ClassType findMethodScope(String str) throws SemanticException;

    ImportTable importTable();

    Resolver outerResolver();

    Context pushSource(ImportTable importTable);

    Context pushClass(ParsedClassType parsedClassType, ClassType classType);

    Context pushCode(CodeInstance codeInstance);

    Context pushBlock();

    Context pushStatic();

    Context pop();

    boolean inCode();

    boolean isLocal(String str);

    boolean inStaticContext();

    ClassType currentClass();

    ParsedClassType currentClassScope();

    CodeInstance currentCode();

    Package package_();
}
