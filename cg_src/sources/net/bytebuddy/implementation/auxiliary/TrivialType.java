package net.bytebuddy.implementation.auxiliary;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.MethodAccessorFactory;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/auxiliary/TrivialType.class */
public enum TrivialType implements AuxiliaryType {
    SIGNATURE_RELEVANT(true),
    PLAIN(false);
    
    private final boolean eager;

    TrivialType(boolean eager) {
        this.eager = eager;
    }

    @Override // net.bytebuddy.implementation.auxiliary.AuxiliaryType
    public DynamicType make(String auxiliaryTypeName, ClassFileVersion classFileVersion, MethodAccessorFactory methodAccessorFactory) {
        List emptyList;
        DynamicType.Builder subclass = new ByteBuddy(classFileVersion).with(TypeValidation.DISABLED).with(MethodGraph.Empty.INSTANCE).subclass(Object.class, (ConstructorStrategy) ConstructorStrategy.Default.NO_CONSTRUCTORS);
        if (this.eager) {
            emptyList = Collections.singletonList(AnnotationDescription.Builder.ofType(AuxiliaryType.SignatureRelevant.class).build(false));
        } else {
            emptyList = Collections.emptyList();
        }
        return subclass.annotateType((Collection<? extends AnnotationDescription>) emptyList).name(auxiliaryTypeName).modifiers(DEFAULT_TYPE_MODIFIER).make();
    }
}
