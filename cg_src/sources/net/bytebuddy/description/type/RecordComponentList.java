package net.bytebuddy.description.type;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.FilterableList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentList.class */
public interface RecordComponentList<T extends RecordComponentDescription> extends FilterableList<T, RecordComponentList<T>> {
    ByteCodeElement.Token.TokenList<RecordComponentDescription.Token> asTokenList(ElementMatcher<? super TypeDescription> elementMatcher);

    TypeList.Generic asTypeList();

    RecordComponentList<RecordComponentDescription.InDefinedShape> asDefined();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentList$AbstractBase.class */
    public static abstract class AbstractBase<S extends RecordComponentDescription> extends FilterableList.AbstractBase<S, RecordComponentList<S>> implements RecordComponentList<S> {
        @Override // net.bytebuddy.description.type.RecordComponentList
        public ByteCodeElement.Token.TokenList<RecordComponentDescription.Token> asTokenList(ElementMatcher<? super TypeDescription> matcher) {
            List<RecordComponentDescription.Token> tokens = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                RecordComponentDescription recordComponentDescription = (RecordComponentDescription) it.next();
                tokens.add(recordComponentDescription.asToken(matcher));
            }
            return new ByteCodeElement.Token.TokenList<>(tokens);
        }

        @Override // net.bytebuddy.description.type.RecordComponentList
        public TypeList.Generic asTypeList() {
            List<TypeDescription.Generic> typeDescriptions = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                RecordComponentDescription recordComponentDescription = (RecordComponentDescription) it.next();
                typeDescriptions.add(recordComponentDescription.getType());
            }
            return new TypeList.Generic.Explicit(typeDescriptions);
        }

        @Override // net.bytebuddy.description.type.RecordComponentList
        public RecordComponentList<RecordComponentDescription.InDefinedShape> asDefined() {
            List<RecordComponentDescription.InDefinedShape> recordComponents = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                RecordComponentDescription recordComponentDescription = (RecordComponentDescription) it.next();
                recordComponents.add(recordComponentDescription.asDefined());
            }
            return new Explicit(recordComponents);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // net.bytebuddy.matcher.FilterableList.AbstractBase
        public RecordComponentList<S> wrap(List<S> values) {
            return new Explicit(values);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentList$ForLoadedRecordComponents.class */
    public static class ForLoadedRecordComponents extends AbstractBase<RecordComponentDescription.InDefinedShape> {
        private final List<?> recordComponents;

        /* JADX INFO: Access modifiers changed from: protected */
        public ForLoadedRecordComponents(Object... recordComponent) {
            this(Arrays.asList(recordComponent));
        }

        protected ForLoadedRecordComponents(List<?> recordComponents) {
            this.recordComponents = recordComponents;
        }

        @Override // java.util.AbstractList, java.util.List
        public RecordComponentDescription.InDefinedShape get(int index) {
            return new RecordComponentDescription.ForLoadedRecordComponent((AnnotatedElement) this.recordComponents.get(index));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.recordComponents.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentList$Explicit.class */
    public static class Explicit<S extends RecordComponentDescription> extends AbstractBase<S> {
        private final List<? extends S> recordComponents;

        public Explicit(S... recordComponent) {
            this(Arrays.asList(recordComponent));
        }

        public Explicit(List<? extends S> recordComponents) {
            this.recordComponents = recordComponents;
        }

        @Override // java.util.AbstractList, java.util.List
        public S get(int index) {
            return this.recordComponents.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.recordComponents.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentList$ForTokens.class */
    public static class ForTokens extends AbstractBase<RecordComponentDescription.InDefinedShape> {
        private final TypeDescription typeDescription;
        private final List<? extends RecordComponentDescription.Token> tokens;

        public ForTokens(TypeDescription typeDescription, RecordComponentDescription.Token... token) {
            this(typeDescription, Arrays.asList(token));
        }

        public ForTokens(TypeDescription typeDescription, List<? extends RecordComponentDescription.Token> tokens) {
            this.typeDescription = typeDescription;
            this.tokens = tokens;
        }

        @Override // java.util.AbstractList, java.util.List
        public RecordComponentDescription.InDefinedShape get(int index) {
            return new RecordComponentDescription.Latent(this.typeDescription, this.tokens.get(index));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.tokens.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentList$TypeSubstituting.class */
    public static class TypeSubstituting extends AbstractBase<RecordComponentDescription.InGenericShape> {
        private final TypeDescription.Generic declaringType;
        private final List<? extends RecordComponentDescription> recordComponentDescriptions;
        private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

        public TypeSubstituting(TypeDescription.Generic declaringType, List<? extends RecordComponentDescription> recordComponentDescriptions, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            this.declaringType = declaringType;
            this.recordComponentDescriptions = recordComponentDescriptions;
            this.visitor = visitor;
        }

        @Override // java.util.AbstractList, java.util.List
        public RecordComponentDescription.InGenericShape get(int index) {
            return new RecordComponentDescription.TypeSubstituting(this.declaringType, this.recordComponentDescriptions.get(index), this.visitor);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.recordComponentDescriptions.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/RecordComponentList$Empty.class */
    public static class Empty<S extends RecordComponentDescription> extends FilterableList.Empty<S, RecordComponentList<S>> implements RecordComponentList<S> {
        @Override // net.bytebuddy.description.type.RecordComponentList
        public RecordComponentList<RecordComponentDescription.InDefinedShape> asDefined() {
            return new Empty();
        }

        @Override // net.bytebuddy.description.type.RecordComponentList
        public ByteCodeElement.Token.TokenList<RecordComponentDescription.Token> asTokenList(ElementMatcher<? super TypeDescription> matcher) {
            return new ByteCodeElement.Token.TokenList<>(new RecordComponentDescription.Token[0]);
        }

        @Override // net.bytebuddy.description.type.RecordComponentList
        public TypeList.Generic asTypeList() {
            return new TypeList.Generic.Empty();
        }
    }
}
