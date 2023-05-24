package polyglot.types;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Source;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/TypeSystem.class */
public interface TypeSystem {
    void initialize(LoadedClassResolver loadedClassResolver, ExtensionInfo extensionInfo) throws SemanticException;

    TopLevelResolver systemResolver();

    TableResolver parsedResolver();

    LoadedClassResolver loadedResolver();

    ImportTable importTable(String str, Package r2);

    ImportTable importTable(Package r1);

    List defaultPackageImports();

    boolean packageExists(String str);

    Named forName(String str) throws SemanticException;

    Type typeForName(String str) throws SemanticException;

    InitializerInstance initializerInstance(Position position, ClassType classType, Flags flags);

    ConstructorInstance constructorInstance(Position position, ClassType classType, Flags flags, List list, List list2);

    MethodInstance methodInstance(Position position, ReferenceType referenceType, Flags flags, Type type, String str, List list, List list2);

    FieldInstance fieldInstance(Position position, ReferenceType referenceType, Flags flags, Type type, String str);

    LocalInstance localInstance(Position position, Flags flags, Type type, String str);

    ConstructorInstance defaultConstructor(Position position, ClassType classType);

    UnknownType unknownType(Position position);

    UnknownPackage unknownPackage(Position position);

    UnknownQualifier unknownQualifier(Position position);

    boolean isSubtype(Type type, Type type2);

    boolean descendsFrom(Type type, Type type2);

    boolean isCastValid(Type type, Type type2);

    boolean isImplicitCastValid(Type type, Type type2);

    boolean equals(TypeObject typeObject, TypeObject typeObject2);

    boolean numericConversionValid(Type type, long j);

    boolean numericConversionValid(Type type, Object obj);

    Type leastCommonAncestor(Type type, Type type2) throws SemanticException;

    boolean isCanonical(Type type);

    boolean isAccessible(MemberInstance memberInstance, Context context);

    boolean classAccessible(ClassType classType, Context context);

    boolean classAccessibleFromPackage(ClassType classType, Package r2);

    boolean isEnclosed(ClassType classType, ClassType classType2);

    boolean hasEnclosingInstance(ClassType classType, ClassType classType2);

    boolean canCoerceToString(Type type, Context context);

    boolean isThrowable(Type type);

    boolean isUncheckedException(Type type);

    Collection uncheckedExceptions();

    PrimitiveType promote(Type type) throws SemanticException;

    PrimitiveType promote(Type type, Type type2) throws SemanticException;

    FieldInstance findField(ReferenceType referenceType, String str, Context context) throws SemanticException;

    FieldInstance findField(ReferenceType referenceType, String str, ClassType classType) throws SemanticException;

    FieldInstance findField(ReferenceType referenceType, String str) throws SemanticException;

    MethodInstance findMethod(ReferenceType referenceType, String str, List list, ClassType classType) throws SemanticException;

    MethodInstance findMethod(ReferenceType referenceType, String str, List list, Context context) throws SemanticException;

    ConstructorInstance findConstructor(ClassType classType, List list, ClassType classType2) throws SemanticException;

    ConstructorInstance findConstructor(ClassType classType, List list, Context context) throws SemanticException;

    ClassType findMemberClass(ClassType classType, String str, ClassType classType2) throws SemanticException;

    ClassType findMemberClass(ClassType classType, String str, Context context) throws SemanticException;

    ClassType findMemberClass(ClassType classType, String str) throws SemanticException;

    Type superType(ReferenceType referenceType);

    List interfaces(ReferenceType referenceType);

    boolean throwsSubset(ProcedureInstance procedureInstance, ProcedureInstance procedureInstance2);

    boolean hasMethod(ReferenceType referenceType, MethodInstance methodInstance);

    boolean hasMethodNamed(ReferenceType referenceType, String str);

    boolean isSameMethod(MethodInstance methodInstance, MethodInstance methodInstance2);

    boolean moreSpecific(ProcedureInstance procedureInstance, ProcedureInstance procedureInstance2);

    boolean hasFormals(ProcedureInstance procedureInstance, List list);

    NullType Null();

    PrimitiveType Void();

    PrimitiveType Boolean();

    PrimitiveType Char();

    PrimitiveType Byte();

    PrimitiveType Short();

    PrimitiveType Int();

    PrimitiveType Long();

    PrimitiveType Float();

    PrimitiveType Double();

    ClassType Object();

    ClassType String();

    ClassType Class();

    ClassType Throwable();

    ClassType Error();

    ClassType Exception();

    ClassType RuntimeException();

    ClassType Cloneable();

    ClassType Serializable();

    ClassType NullPointerException();

    ClassType ClassCastException();

    ClassType OutOfBoundsException();

    ClassType ArrayStoreException();

    ClassType ArithmeticException();

    ArrayType arrayOf(Type type);

    ArrayType arrayOf(Position position, Type type);

    ArrayType arrayOf(Type type, int i);

    ArrayType arrayOf(Position position, Type type, int i);

    Package packageForName(String str) throws SemanticException;

    Package packageForName(Package r1, String str) throws SemanticException;

    Package createPackage(String str);

    Package createPackage(Package r1, String str);

    Context createContext();

    Resolver packageContextResolver(Resolver resolver, Package r2);

    Resolver classContextResolver(ClassType classType);

    LazyClassInitializer defaultClassInitializer();

    ParsedClassType createClassType(LazyClassInitializer lazyClassInitializer);

    ParsedClassType createClassType();

    ParsedClassType createClassType(LazyClassInitializer lazyClassInitializer, Source source);

    ParsedClassType createClassType(Source source);

    Set getTypeEncoderRootSet(Type type);

    String getTransformedClassName(ClassType classType);

    Object placeHolder(TypeObject typeObject, Set set);

    Object placeHolder(TypeObject typeObject);

    String translatePackage(Resolver resolver, Package r2);

    String translatePrimitive(Resolver resolver, PrimitiveType primitiveType);

    String translateArray(Resolver resolver, ArrayType arrayType);

    String translateClass(Resolver resolver, ClassType classType);

    String wrapperTypeString(PrimitiveType primitiveType);

    boolean methodCallValid(MethodInstance methodInstance, String str, List list);

    boolean callValid(ProcedureInstance procedureInstance, List list);

    List overrides(MethodInstance methodInstance);

    boolean canOverride(MethodInstance methodInstance, MethodInstance methodInstance2);

    void checkOverride(MethodInstance methodInstance, MethodInstance methodInstance2) throws SemanticException;

    List implemented(MethodInstance methodInstance);

    PrimitiveType primitiveForName(String str) throws SemanticException;

    void checkMethodFlags(Flags flags) throws SemanticException;

    void checkLocalFlags(Flags flags) throws SemanticException;

    void checkFieldFlags(Flags flags) throws SemanticException;

    void checkConstructorFlags(Flags flags) throws SemanticException;

    void checkInitializerFlags(Flags flags) throws SemanticException;

    void checkTopLevelClassFlags(Flags flags) throws SemanticException;

    void checkMemberClassFlags(Flags flags) throws SemanticException;

    void checkLocalClassFlags(Flags flags) throws SemanticException;

    void checkAccessFlags(Flags flags) throws SemanticException;

    void checkCycles(ReferenceType referenceType) throws SemanticException;

    void checkClassConformance(ClassType classType) throws SemanticException;

    Type staticTarget(Type type);

    Flags flagsForBits(int i);

    Flags createNewFlag(String str, Flags flags);

    Flags NoFlags();

    Flags Public();

    Flags Protected();

    Flags Private();

    Flags Static();

    Flags Final();

    Flags Synchronized();

    Flags Transient();

    Flags Native();

    Flags Interface();

    Flags Abstract();

    Flags Volatile();

    Flags StrictFP();
}
