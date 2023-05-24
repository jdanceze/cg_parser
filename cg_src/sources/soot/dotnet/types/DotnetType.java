package soot.dotnet.types;

import com.google.common.base.Strings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootResolver;
import soot.dotnet.AssemblyFile;
import soot.dotnet.members.DotnetEvent;
import soot.dotnet.members.DotnetField;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.members.DotnetProperty;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.specifications.DotnetAttributeArgument;
import soot.dotnet.specifications.DotnetModifier;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.DeprecatedTag;
/* loaded from: gencallgraphv3.jar:soot/dotnet/types/DotnetType.class */
public class DotnetType {
    private static final Logger logger = LoggerFactory.getLogger(DotnetType.class);
    private final ProtoAssemblyAllTypes.TypeDefinition typeDefinition;
    private final AssemblyFile assemblyFile;

    public DotnetType(ProtoAssemblyAllTypes.TypeDefinition typeDefinition, File assemblyFile) {
        if (typeDefinition == null) {
            throw new NullPointerException("Passed Type Definition is null!");
        }
        this.typeDefinition = typeDefinition;
        if (!(assemblyFile instanceof AssemblyFile)) {
            throw new RuntimeException("Given File object is no assembly file!");
        }
        this.assemblyFile = (AssemblyFile) assemblyFile;
    }

    public IInitialResolver.Dependencies resolveSootClass(SootClass sootClass) {
        IInitialResolver.Dependencies dependencies = new IInitialResolver.Dependencies();
        resolveModifier(sootClass);
        resolveSuperclassInterfaces(sootClass, dependencies);
        resolveOuterClass(sootClass, dependencies);
        resolveFields(sootClass);
        resolveMethods(sootClass);
        resolveProperties(sootClass);
        resolveEvents(sootClass);
        resolveAttributes(sootClass);
        return dependencies;
    }

    private void resolveModifier(SootClass sootClass) {
        sootClass.setModifiers(DotnetModifier.toSootModifier(this.typeDefinition));
    }

    private void resolveSuperclassInterfaces(SootClass sootClass, IInitialResolver.Dependencies deps) {
        for (ProtoAssemblyAllTypes.TypeDefinition baseType : this.typeDefinition.getDirectBaseTypesList()) {
            if (baseType.getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.CLASS)) {
                SootClass superClass = SootResolver.v().makeClassRef(baseType.getFullname());
                sootClass.setSuperclass(superClass);
                deps.typesToHierarchy.add(superClass.getType());
            }
            if (baseType.getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.INTERFACE)) {
                SootClass superClass2 = SootResolver.v().makeClassRef(baseType.getFullname());
                if (sootClass.getInterfaces().stream().noneMatch(x -> {
                    return x.getName().equals(baseType.getFullname());
                })) {
                    sootClass.addInterface(superClass2);
                    deps.typesToHierarchy.add(superClass2.getType());
                }
            }
        }
    }

    private void resolveOuterClass(SootClass declaringClass, IInitialResolver.Dependencies deps) {
        if (!Strings.isNullOrEmpty(this.typeDefinition.getDeclaringOuterClass())) {
            SootClass outerClass = SootResolver.v().makeClassRef(this.typeDefinition.getDeclaringOuterClass());
            declaringClass.setOuterClass(outerClass);
            deps.typesToHierarchy.add(outerClass.getType());
        }
    }

    private void resolveFields(SootClass declaringClass) {
        for (ProtoAssemblyAllTypes.FieldDefinition field : this.typeDefinition.getFieldsList()) {
            DotnetField dotnetField = new DotnetField(field);
            SootField sootField = dotnetField.makeSootField();
            if (!declaringClass.declaresField(sootField.getSubSignature())) {
                declaringClass.addField(sootField);
            }
        }
    }

    private void resolveMethods(SootClass declaringClass) {
        for (ProtoAssemblyAllTypes.MethodDefinition method : this.typeDefinition.getMethodsList()) {
            DotnetMethod dotnetMethod = new DotnetMethod(method, declaringClass);
            if (Options.v().resolve_all_dotnet_methods() || (!method.getIsUnsafe() && (!method.getName().equals("InternalCopy") || !declaringClass.getName().equals(DotnetBasicTypes.SYSTEM_STRING)))) {
                SootMethod sootMethod = dotnetMethod.toSootMethod();
                if (declaringClass.declaresMethod(sootMethod.getName(), sootMethod.getParameterTypes(), sootMethod.getReturnType())) {
                    return;
                }
                declaringClass.addMethod(sootMethod);
            }
        }
    }

    private void resolveProperties(SootClass declaringClass) {
        SootMethod setter;
        for (ProtoAssemblyAllTypes.PropertyDefinition property : this.typeDefinition.getPropertiesList()) {
            DotnetProperty dotnetProperty = new DotnetProperty(property, declaringClass);
            if (dotnetProperty.getCanGet()) {
                SootMethod getter = dotnetProperty.makeSootMethodGetter();
                if (getter != null && !declaringClass.declaresMethod(getter.getName(), getter.getParameterTypes(), getter.getReturnType())) {
                    declaringClass.addMethod(getter);
                }
            }
            if (dotnetProperty.getCanSet() && (setter = dotnetProperty.makeSootMethodSetter()) != null && !declaringClass.declaresMethod(setter.getName(), setter.getParameterTypes(), setter.getReturnType())) {
                declaringClass.addMethod(setter);
            }
        }
    }

    private void resolveEvents(SootClass declaringClass) {
        for (ProtoAssemblyAllTypes.EventDefinition eventDefinition : this.typeDefinition.getEventsList()) {
            loadEvent(declaringClass, eventDefinition);
        }
    }

    private void loadEvent(SootClass declaringClass, ProtoAssemblyAllTypes.EventDefinition protoEvent) {
        DotnetEvent dotnetEvent = new DotnetEvent(protoEvent, declaringClass);
        if (dotnetEvent.getCanAdd()) {
            SootMethod getter = dotnetEvent.makeSootMethodAdd();
            if (declaringClass.declaresMethod(getter.getName(), getter.getParameterTypes(), getter.getReturnType())) {
                return;
            }
            declaringClass.addMethod(getter);
        }
        if (dotnetEvent.getCanInvoke()) {
            SootMethod setter = dotnetEvent.makeSootMethodInvoke();
            if (declaringClass.declaresMethod(setter.getName(), setter.getParameterTypes(), setter.getReturnType())) {
                return;
            }
            declaringClass.addMethod(setter);
        }
        if (dotnetEvent.getCanRemove()) {
            SootMethod setter2 = dotnetEvent.makeSootMethodRemove();
            if (declaringClass.declaresMethod(setter2.getName(), setter2.getParameterTypes(), setter2.getReturnType())) {
                return;
            }
            declaringClass.addMethod(setter2);
        }
    }

    private void resolveAttributes(SootClass declaringClass) {
        if (this.typeDefinition.getAttributesCount() == 0) {
            return;
        }
        for (ProtoAssemblyAllTypes.AttributeDefinition attrMsg : this.typeDefinition.getAttributesList()) {
            try {
                String annotationType = attrMsg.getAttributeType().getFullname();
                List<AnnotationElem> elements = new ArrayList<>();
                for (ProtoAssemblyAllTypes.AttributeArgumentDefinition fixedArg : attrMsg.getFixedArgumentsList()) {
                    elements.add(DotnetAttributeArgument.toAnnotationElem(fixedArg));
                }
                for (ProtoAssemblyAllTypes.AttributeArgumentDefinition namedArg : attrMsg.getNamedArgumentsList()) {
                    elements.add(DotnetAttributeArgument.toAnnotationElem(namedArg));
                }
                declaringClass.addTag(new AnnotationTag(annotationType, elements));
                if (annotationType.equals(DotnetBasicTypes.SYSTEM_OBSOLETEATTRIBUTE)) {
                    declaringClass.addTag(new DeprecatedTag());
                }
            } catch (Exception ignore) {
                logger.info("Ignored", (Throwable) ignore);
            }
        }
    }
}
