package polyglot.types;

import polyglot.frontend.Source;
import polyglot.types.ClassType;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ParsedClassType.class */
public interface ParsedClassType extends ClassType {
    void position(Position position);

    Source fromSource();

    void package_(Package r1);

    void superType(Type type);

    void addInterface(Type type);

    void addField(FieldInstance fieldInstance);

    void addMethod(MethodInstance methodInstance);

    void addConstructor(ConstructorInstance constructorInstance);

    void addMemberClass(ClassType classType);

    void flags(Flags flags);

    void outer(ClassType classType);

    void name(String str);

    void kind(ClassType.Kind kind);

    void inStaticContext(boolean z);
}
