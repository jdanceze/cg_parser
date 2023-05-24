package soot.dotnet.specifications;

import soot.dotnet.proto.ProtoAssemblyAllTypes;
/* loaded from: gencallgraphv3.jar:soot/dotnet/specifications/DotnetModifier.class */
public class DotnetModifier {
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoAssemblyAllTypes$Accessibility;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$dotnet$proto$ProtoAssemblyAllTypes$Accessibility() {
        int[] iArr = $SWITCH_TABLE$soot$dotnet$proto$ProtoAssemblyAllTypes$Accessibility;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[ProtoAssemblyAllTypes.Accessibility.valuesCustom().length];
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.INTERNAL.ordinal()] = 4;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.NONE.ordinal()] = 1;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.PRIVATE.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.PROTECTED.ordinal()] = 5;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.PROTECTED_AND_INTERNAL.ordinal()] = 6;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.PROTECTED_OR_INTERNAL.ordinal()] = 7;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.PUBLIC.ordinal()] = 3;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[ProtoAssemblyAllTypes.Accessibility.UNRECOGNIZED.ordinal()] = 8;
        } catch (NoSuchFieldError unused8) {
        }
        $SWITCH_TABLE$soot$dotnet$proto$ProtoAssemblyAllTypes$Accessibility = iArr2;
        return iArr2;
    }

    public static int toSootModifier(ProtoAssemblyAllTypes.TypeDefinition protoType) {
        int modifier = convertAccessibility(protoType.getAccessibility());
        if (protoType.getIsAbstract()) {
            modifier |= 1024;
        }
        if (protoType.getIsStatic()) {
            modifier |= 8;
        }
        if (protoType.getIsSealed()) {
            modifier |= 16;
        }
        if (protoType.getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.INTERFACE)) {
            modifier |= 512;
        }
        if (protoType.getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.ENUM)) {
            modifier |= 16384;
        }
        return modifier;
    }

    public static int toSootModifier(ProtoAssemblyAllTypes.MethodDefinition methodDefinition) {
        int modifier = convertAccessibility(methodDefinition.getAccessibility());
        if (methodDefinition.getIsAbstract()) {
            modifier |= 1024;
        }
        if (methodDefinition.getIsStatic()) {
            modifier |= 8;
        }
        if (methodDefinition.getIsSealed()) {
            modifier |= 16;
        }
        if (methodDefinition.getIsExtern()) {
            modifier |= 256;
        }
        return modifier;
    }

    public static int toSootModifier(ProtoAssemblyAllTypes.FieldDefinition fieldDefinition) {
        int modifier = convertAccessibility(fieldDefinition.getAccessibility());
        if (fieldDefinition.getIsAbstract() || fieldDefinition.getIsVirtual()) {
            modifier |= 1024;
        }
        if (fieldDefinition.getIsStatic()) {
            modifier |= 8;
        }
        if (fieldDefinition.getIsReadOnly()) {
            modifier |= 16;
        }
        return modifier;
    }

    private static int convertAccessibility(ProtoAssemblyAllTypes.Accessibility accessibility) {
        int modifier = 0;
        switch ($SWITCH_TABLE$soot$dotnet$proto$ProtoAssemblyAllTypes$Accessibility()[accessibility.ordinal()]) {
            case 2:
                modifier = 0 | 2;
                break;
            case 3:
            case 4:
            case 6:
            case 7:
                modifier = 0 | 1;
                break;
            case 5:
                modifier = 0 | 4;
                break;
        }
        return modifier;
    }
}
