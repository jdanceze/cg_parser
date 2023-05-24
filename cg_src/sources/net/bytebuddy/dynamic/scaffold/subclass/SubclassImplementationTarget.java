package net.bytebuddy.dynamic.scaffold.subclass;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatchers;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/subclass/SubclassImplementationTarget.class */
public class SubclassImplementationTarget extends Implementation.Target.AbstractBase {
    protected final OriginTypeResolver originTypeResolver;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/subclass/SubclassImplementationTarget$OriginTypeResolver.class */
    public enum OriginTypeResolver {
        SUPER_CLASS { // from class: net.bytebuddy.dynamic.scaffold.subclass.SubclassImplementationTarget.OriginTypeResolver.1
            @Override // net.bytebuddy.dynamic.scaffold.subclass.SubclassImplementationTarget.OriginTypeResolver
            protected TypeDefinition identify(TypeDescription typeDescription) {
                return typeDescription.getSuperClass();
            }
        },
        LEVEL_TYPE { // from class: net.bytebuddy.dynamic.scaffold.subclass.SubclassImplementationTarget.OriginTypeResolver.2
            @Override // net.bytebuddy.dynamic.scaffold.subclass.SubclassImplementationTarget.OriginTypeResolver
            protected TypeDefinition identify(TypeDescription typeDescription) {
                return typeDescription;
            }
        };

        protected abstract TypeDefinition identify(TypeDescription typeDescription);
    }

    @Override // net.bytebuddy.implementation.Implementation.Target.AbstractBase
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.originTypeResolver.equals(((SubclassImplementationTarget) obj).originTypeResolver);
        }
        return false;
    }

    @Override // net.bytebuddy.implementation.Implementation.Target.AbstractBase
    public int hashCode() {
        return (super.hashCode() * 31) + this.originTypeResolver.hashCode();
    }

    protected SubclassImplementationTarget(TypeDescription instrumentedType, MethodGraph.Linked methodGraph, Implementation.Target.AbstractBase.DefaultMethodInvocation defaultMethodInvocation, OriginTypeResolver originTypeResolver) {
        super(instrumentedType, methodGraph, defaultMethodInvocation);
        this.originTypeResolver = originTypeResolver;
    }

    @Override // net.bytebuddy.implementation.Implementation.Target
    public Implementation.SpecialMethodInvocation invokeSuper(MethodDescription.SignatureToken token) {
        if (token.getName().equals("<init>")) {
            return invokeConstructor(token);
        }
        return invokeMethod(token);
    }

    private Implementation.SpecialMethodInvocation invokeConstructor(MethodDescription.SignatureToken token) {
        TypeDescription.Generic superClass = this.instrumentedType.getSuperClass();
        MethodList<?> candidates = superClass == null ? new MethodList.Empty<>() : superClass.getDeclaredMethods().filter(ElementMatchers.hasSignature(token).and(ElementMatchers.isVisibleTo(this.instrumentedType)));
        return candidates.size() == 1 ? Implementation.SpecialMethodInvocation.Simple.of((MethodDescription) candidates.getOnly(), this.instrumentedType.getSuperClass().asErasure()) : Implementation.SpecialMethodInvocation.Illegal.INSTANCE;
    }

    private Implementation.SpecialMethodInvocation invokeMethod(MethodDescription.SignatureToken token) {
        MethodGraph.Node methodNode = this.methodGraph.getSuperClassGraph().locate(token);
        return methodNode.getSort().isUnique() ? Implementation.SpecialMethodInvocation.Simple.of(methodNode.getRepresentative(), this.instrumentedType.getSuperClass().asErasure()) : Implementation.SpecialMethodInvocation.Illegal.INSTANCE;
    }

    @Override // net.bytebuddy.implementation.Implementation.Target
    public TypeDefinition getOriginType() {
        return this.originTypeResolver.identify(this.instrumentedType);
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/subclass/SubclassImplementationTarget$Factory.class */
    public enum Factory implements Implementation.Target.Factory {
        SUPER_CLASS(OriginTypeResolver.SUPER_CLASS),
        LEVEL_TYPE(OriginTypeResolver.LEVEL_TYPE);
        
        private final OriginTypeResolver originTypeResolver;

        Factory(OriginTypeResolver originTypeResolver) {
            this.originTypeResolver = originTypeResolver;
        }

        @Override // net.bytebuddy.implementation.Implementation.Target.Factory
        public Implementation.Target make(TypeDescription instrumentedType, MethodGraph.Linked methodGraph, ClassFileVersion classFileVersion) {
            return new SubclassImplementationTarget(instrumentedType, methodGraph, Implementation.Target.AbstractBase.DefaultMethodInvocation.of(classFileVersion), this.originTypeResolver);
        }
    }
}
