package soot.dotnet.members;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodSource;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootResolver;
import soot.SourceLocator;
import soot.Type;
import soot.coffi.Instruction;
import soot.dotnet.AssemblyFile;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.members.method.DotnetMethodParameter;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.specifications.DotnetAttributeArgument;
import soot.dotnet.specifications.DotnetModifier;
import soot.dotnet.types.DotnetBasicTypes;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.DeprecatedTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/DotnetMethod.class */
public class DotnetMethod extends AbstractDotnetMember {
    private static final Logger logger = LoggerFactory.getLogger(DotnetMethod.class);
    private final ProtoAssemblyAllTypes.MethodDefinition protoMethod;
    private final SootClass declaringClass;
    private SootMethod sootMethod;
    private final DotnetMethodType dotnetMethodType;
    public static final String STATIC_CONSTRUCTOR_NAME = ".cctor";
    public static final String CONSTRUCTOR_NAME = ".ctor";
    public static final String JAVA_STATIC_CONSTRUCTOR_NAME = "<clinit>";
    public static final String JAVA_CONSTRUCTOR_NAME = "<init>";
    public static final String DESTRUCTOR_NAME = "Finalize";
    public static final String MAIN_METHOD_SIGNATURE = "void Main(System.String[])";

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/dotnet/members/DotnetMethod$DotnetMethodType.class */
    public enum DotnetMethodType {
        METHOD,
        PROPERTY,
        EVENT;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static DotnetMethodType[] valuesCustom() {
            DotnetMethodType[] valuesCustom = values();
            int length = valuesCustom.length;
            DotnetMethodType[] dotnetMethodTypeArr = new DotnetMethodType[length];
            System.arraycopy(valuesCustom, 0, dotnetMethodTypeArr, 0, length);
            return dotnetMethodTypeArr;
        }
    }

    public DotnetMethod(ProtoAssemblyAllTypes.MethodDefinition protoMethod, SootClass declaringClass, DotnetMethodType declaringMemberType) {
        this.sootMethod = null;
        if (protoMethod == null || declaringClass == null) {
            throw new NullPointerException();
        }
        this.declaringClass = declaringClass;
        if (protoMethod.getIsConstructor()) {
            ProtoAssemblyAllTypes.MethodDefinition.Builder builder = ProtoAssemblyAllTypes.MethodDefinition.newBuilder(protoMethod);
            builder.setName(convertCtorName(protoMethod.getName()));
            builder.setFullName(convertCtorName(protoMethod.getFullName()));
            this.protoMethod = builder.build();
        } else {
            this.protoMethod = protoMethod;
        }
        this.dotnetMethodType = declaringMemberType;
    }

    public DotnetMethod(ProtoAssemblyAllTypes.MethodDefinition protoMethod, SootClass declaringClass) {
        this(protoMethod, declaringClass, DotnetMethodType.METHOD);
    }

    public DotnetMethod(ProtoAssemblyAllTypes.MethodDefinition protoMethod) {
        this.sootMethod = null;
        if (protoMethod.getIsConstructor()) {
            ProtoAssemblyAllTypes.MethodDefinition.Builder builder = ProtoAssemblyAllTypes.MethodDefinition.newBuilder(protoMethod);
            builder.setName(convertCtorName(protoMethod.getName()));
            builder.setFullName(convertCtorName(protoMethod.getFullName()));
            this.protoMethod = builder.build();
        } else {
            this.protoMethod = protoMethod;
        }
        if (protoMethod.hasDeclaringType()) {
            this.declaringClass = SootResolver.v().makeClassRef(protoMethod.getDeclaringType().getFullname());
        } else {
            this.declaringClass = null;
        }
        this.dotnetMethodType = DotnetMethodType.METHOD;
    }

    public boolean isConstructor() {
        return this.protoMethod.getIsConstructor();
    }

    public boolean isStatic() {
        return this.protoMethod.getIsStatic() || this.protoMethod.getName().contains(STATIC_CONSTRUCTOR_NAME) || this.protoMethod.getName().contains("<clinit>");
    }

    public ProtoAssemblyAllTypes.MethodDefinition getProtoMessage() {
        return this.protoMethod;
    }

    public String getName() {
        return this.protoMethod.getName();
    }

    public ProtoAssemblyAllTypes.TypeDefinition getReturnType() {
        return this.protoMethod.getReturnType();
    }

    public SootMethod toSootMethod() {
        return toSootMethod(createMethodSource());
    }

    public SootMethod toSootMethod(MethodSource methodSource) {
        if (this.sootMethod != null) {
            return this.sootMethod;
        }
        String name = getUniqueName();
        List<Type> parameters = DotnetMethodParameter.toSootTypeParamsList(getParameterDefinitions());
        Type return_type = DotnetTypeFactory.toSootType(getReturnType());
        if (this.dotnetMethodType == DotnetMethodType.METHOD && this.protoMethod.getReturnType().getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.POINTER) && this.protoMethod.getReturnType().getFullname().equals(DotnetBasicTypes.SYSTEM_VOID)) {
            return_type = this.declaringClass.getType();
        }
        int modifier = DotnetModifier.toSootModifier(this.protoMethod);
        SootMethod sm = Scene.v().makeSootMethod(name, parameters, return_type, modifier);
        resolveMethodAttributes(sm);
        resolveMethodParameterRefType(sm);
        if (Modifier.isAbstract(modifier) || Modifier.isNative(modifier) || (Options.v().oaat() && this.declaringClass.resolvingLevel() <= 2)) {
            this.sootMethod = sm;
            return sm;
        }
        sm.setSource(methodSource);
        this.sootMethod = sm;
        return sm;
    }

    private MethodSource createMethodSource() {
        return m, phaseName -> {
            AssemblyFile assemblyFile = (AssemblyFile) SourceLocator.v().dexClassIndex().get(this.declaringClass.getName());
            ProtoIlInstructions.IlFunctionMsg ilFunctionMsg = assemblyFile.getMethodBody(this.declaringClass.getName(), m.getName(), this.protoMethod.getPeToken());
            Body b = jimplifyMethodBody(ilFunctionMsg);
            m.setActiveBody(b);
            return m.getActiveBody();
        };
    }

    public Body jimplifyMethodBody(ProtoIlInstructions.IlFunctionMsg ilFunctionMsg) {
        JimpleBody b = Jimple.v().newBody(this.sootMethod);
        try {
        } catch (Exception e) {
            logger.warn("Error while generating jimple body of " + this.dotnetMethodType.name() + Instruction.argsep + this.sootMethod.getName() + " declared in class " + this.declaringClass.getName() + "!");
            logger.warn(e.getMessage());
            if (Options.v().ignore_methodsource_error()) {
                logger.warn("Ignore errors in generation due to the set parameter. Generate empty Jimple Body.");
                b = Jimple.v().newBody(this.sootMethod);
                DotnetBody.resolveEmptyJimpleBody(b, this.sootMethod);
            } else {
                throw e;
            }
        }
        if (ilFunctionMsg == null) {
            throw new RuntimeException("Could not resolve JimpleBody for " + this.dotnetMethodType.name() + Instruction.argsep + this.sootMethod.getName() + " declared in class " + this.declaringClass.getName());
        }
        DotnetBody methodBody = new DotnetBody(this, ilFunctionMsg);
        methodBody.jimplify(b);
        return b;
    }

    private void resolveMethodAttributes(SootMethod method) {
        if (this.protoMethod.getAttributesCount() == 0) {
            return;
        }
        for (ProtoAssemblyAllTypes.AttributeDefinition attrMsg : this.protoMethod.getAttributesList()) {
            try {
                String annotationType = attrMsg.getAttributeType().getFullname();
                List<AnnotationElem> elements = new ArrayList<>();
                for (ProtoAssemblyAllTypes.AttributeArgumentDefinition fixedArg : attrMsg.getFixedArgumentsList()) {
                    elements.add(DotnetAttributeArgument.toAnnotationElem(fixedArg));
                }
                for (ProtoAssemblyAllTypes.AttributeArgumentDefinition namedArg : attrMsg.getNamedArgumentsList()) {
                    elements.add(DotnetAttributeArgument.toAnnotationElem(namedArg));
                }
                method.addTag(new AnnotationTag(annotationType, elements));
                if (annotationType.equals(DotnetBasicTypes.SYSTEM_OBSOLETEATTRIBUTE)) {
                    method.addTag(new DeprecatedTag());
                }
            } catch (Exception ignore) {
                logger.info("Ignores", (Throwable) ignore);
            }
        }
    }

    private void resolveMethodParameterRefType(SootMethod method) {
        VisibilityParameterAnnotationTag tag = new VisibilityParameterAnnotationTag(this.protoMethod.getParameterCount(), 0);
        for (ProtoAssemblyAllTypes.ParameterDefinition parameter : this.protoMethod.getParameterList()) {
            if (parameter.getIsRef() || parameter.getIsOut() || parameter.getIsIn()) {
                tag.addVisibilityAnnotation(new VisibilityAnnotationTag(1));
            } else {
                tag.addVisibilityAnnotation(new VisibilityAnnotationTag(0));
            }
        }
        method.addTag(tag);
    }

    public List<ProtoAssemblyAllTypes.ParameterDefinition> getParameterDefinitions() {
        return this.protoMethod.getParameterList();
    }

    public SootClass getDeclaringClass() {
        return this.declaringClass;
    }

    public SootMethod getSootMethodSignature() {
        return this.sootMethod;
    }

    public boolean hasCallByRefParameters() {
        return this.protoMethod.getParameterList().stream().anyMatch(x -> {
            return x.getIsIn() || x.getIsOut() || x.getIsRef();
        });
    }

    public boolean hasGenericParameters() {
        return this.protoMethod.getParameterList().stream().anyMatch(x -> {
            return x.getType().getFullname().contains("`");
        });
    }

    public boolean hasCilPrimitiveParameters() {
        return this.protoMethod.getParameterList().stream().anyMatch(x -> {
            return DotnetTypeFactory.listOfCilPrimitives().contains(x.getType().getFullname());
        });
    }

    public String getUniqueName() {
        if ((!hasGenericParameters() && !hasCallByRefParameters() && !hasCilPrimitiveParameters()) || isConstructor()) {
            return getName();
        }
        return String.valueOf(getName()) + "[[" + this.protoMethod.getPeToken() + "]]";
    }

    public static String convertCtorName(String methodName) {
        return methodName.replace(CONSTRUCTOR_NAME, "<init>").replace(STATIC_CONSTRUCTOR_NAME, "<clinit>").replace("+", "$");
    }

    public static String convertCilToJvmNaming(String methodName) {
        return methodName.replace("+", "$").replace("<init>", CONSTRUCTOR_NAME).replace("<clinit>", STATIC_CONSTRUCTOR_NAME);
    }
}
