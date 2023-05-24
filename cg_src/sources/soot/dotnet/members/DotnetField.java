package soot.dotnet.members;

import soot.SootField;
import soot.Type;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.specifications.DotnetModifier;
import soot.dotnet.types.DotnetTypeFactory;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/DotnetField.class */
public class DotnetField extends AbstractDotnetMember {
    private final ProtoAssemblyAllTypes.FieldDefinition protoField;

    public DotnetField(ProtoAssemblyAllTypes.FieldDefinition protoField) {
        this.protoField = protoField;
    }

    public SootField makeSootField() {
        int modifier = DotnetModifier.toSootModifier(this.protoField);
        Type type = DotnetTypeFactory.toSootType(this.protoField.getType());
        String name = this.protoField.getName();
        return new SootField(name, type, modifier);
    }
}
