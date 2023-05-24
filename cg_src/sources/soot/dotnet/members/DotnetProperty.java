package soot.dotnet.members;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodSource;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.dotnet.AssemblyFile;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.proto.ProtoIlInstructions;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/DotnetProperty.class */
public class DotnetProperty extends AbstractDotnetMember {
    private static final Logger logger = LoggerFactory.getLogger(DotnetProperty.class);
    private final SootClass declaringClass;
    private final ProtoAssemblyAllTypes.PropertyDefinition protoProperty;
    private DotnetMethod setterMethod;
    private DotnetMethod getterMethod;

    public DotnetProperty(ProtoAssemblyAllTypes.PropertyDefinition protoProperty, SootClass declaringClass) {
        this.protoProperty = protoProperty;
        this.declaringClass = declaringClass;
        initDotnetMethods();
    }

    private void initDotnetMethods() {
        if (getCanGet() && this.protoProperty.hasGetter()) {
            this.getterMethod = new DotnetMethod(this.protoProperty.getGetter(), this.declaringClass, DotnetMethod.DotnetMethodType.PROPERTY);
        }
        if (getCanSet() && this.protoProperty.hasSetter()) {
            this.setterMethod = new DotnetMethod(this.protoProperty.getSetter(), this.declaringClass, DotnetMethod.DotnetMethodType.PROPERTY);
        }
    }

    public boolean getCanGet() {
        return this.protoProperty.getCanGet();
    }

    public boolean getCanSet() {
        return this.protoProperty.getCanSet();
    }

    public SootMethod makeSootMethodGetter() {
        if (!this.protoProperty.getCanGet() || !this.protoProperty.hasGetter()) {
            return null;
        }
        return this.getterMethod.toSootMethod(createPropertyMethodSource(false));
    }

    public SootMethod makeSootMethodSetter() {
        if (!this.protoProperty.getCanSet() || !this.protoProperty.hasSetter()) {
            return null;
        }
        return this.setterMethod.toSootMethod(createPropertyMethodSource(true));
    }

    private MethodSource createPropertyMethodSource(boolean isSetter) {
        return m, phaseName -> {
            AssemblyFile assemblyFile = (AssemblyFile) SourceLocator.v().dexClassIndex().get(this.declaringClass.getName());
            ProtoIlInstructions.IlFunctionMsg ilFunctionMsg = assemblyFile.getMethodBodyOfProperty(this.declaringClass.getName(), this.protoProperty.getName(), r6);
            DotnetMethod dotnetMethod = r6 ? this.setterMethod : this.getterMethod;
            Body b = dotnetMethod.jimplifyMethodBody(ilFunctionMsg);
            isSetter.setActiveBody(b);
            return isSetter.getActiveBody();
        };
    }
}
