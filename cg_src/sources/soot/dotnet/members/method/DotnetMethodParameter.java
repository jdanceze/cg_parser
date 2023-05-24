package soot.dotnet.members.method;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.types.DotnetTypeFactory;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/method/DotnetMethodParameter.class */
public class DotnetMethodParameter {
    public static List<Type> toSootTypeParamsList(List<ProtoAssemblyAllTypes.ParameterDefinition> parameterList) {
        List<Type> types = new ArrayList<>();
        for (ProtoAssemblyAllTypes.ParameterDefinition parameter : parameterList) {
            Type type = DotnetTypeFactory.toSootType(parameter.getType());
            types.add(type);
        }
        return types;
    }
}
