package net.bytebuddy.description.field;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.FilterableList;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/field/FieldList.class */
public interface FieldList<T extends FieldDescription> extends FilterableList<T, FieldList<T>> {
    ByteCodeElement.Token.TokenList<FieldDescription.Token> asTokenList(ElementMatcher<? super TypeDescription> elementMatcher);

    FieldList<FieldDescription.InDefinedShape> asDefined();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/field/FieldList$AbstractBase.class */
    public static abstract class AbstractBase<S extends FieldDescription> extends FilterableList.AbstractBase<S, FieldList<S>> implements FieldList<S> {
        @Override // net.bytebuddy.description.field.FieldList
        public ByteCodeElement.Token.TokenList<FieldDescription.Token> asTokenList(ElementMatcher<? super TypeDescription> matcher) {
            List<FieldDescription.Token> tokens = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                FieldDescription fieldDescription = (FieldDescription) it.next();
                tokens.add(fieldDescription.asToken(matcher));
            }
            return new ByteCodeElement.Token.TokenList<>(tokens);
        }

        @Override // net.bytebuddy.description.field.FieldList
        public FieldList<FieldDescription.InDefinedShape> asDefined() {
            List<FieldDescription.InDefinedShape> declaredForms = new ArrayList<>(size());
            Iterator it = iterator();
            while (it.hasNext()) {
                FieldDescription fieldDescription = (FieldDescription) it.next();
                declaredForms.add(fieldDescription.asDefined());
            }
            return new Explicit(declaredForms);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // net.bytebuddy.matcher.FilterableList.AbstractBase
        public FieldList<S> wrap(List<S> values) {
            return new Explicit(values);
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/field/FieldList$ForLoadedFields.class */
    public static class ForLoadedFields extends AbstractBase<FieldDescription.InDefinedShape> {
        private final List<? extends Field> fields;

        public ForLoadedFields(Field... field) {
            this(Arrays.asList(field));
        }

        public ForLoadedFields(List<? extends Field> fields) {
            this.fields = fields;
        }

        @Override // java.util.AbstractList, java.util.List
        public FieldDescription.InDefinedShape get(int index) {
            return new FieldDescription.ForLoadedField(this.fields.get(index));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fields.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/field/FieldList$Explicit.class */
    public static class Explicit<S extends FieldDescription> extends AbstractBase<S> {
        private final List<? extends S> fieldDescriptions;

        public Explicit(S... fieldDescription) {
            this(Arrays.asList(fieldDescription));
        }

        public Explicit(List<? extends S> fieldDescriptions) {
            this.fieldDescriptions = fieldDescriptions;
        }

        @Override // java.util.AbstractList, java.util.List
        public S get(int index) {
            return this.fieldDescriptions.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fieldDescriptions.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/field/FieldList$ForTokens.class */
    public static class ForTokens extends AbstractBase<FieldDescription.InDefinedShape> {
        private final TypeDescription declaringType;
        private final List<? extends FieldDescription.Token> tokens;

        public ForTokens(TypeDescription declaringType, FieldDescription.Token... token) {
            this(declaringType, Arrays.asList(token));
        }

        public ForTokens(TypeDescription declaringType, List<? extends FieldDescription.Token> tokens) {
            this.declaringType = declaringType;
            this.tokens = tokens;
        }

        @Override // java.util.AbstractList, java.util.List
        public FieldDescription.InDefinedShape get(int index) {
            return new FieldDescription.Latent(this.declaringType, this.tokens.get(index));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.tokens.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/field/FieldList$TypeSubstituting.class */
    public static class TypeSubstituting extends AbstractBase<FieldDescription.InGenericShape> {
        private final TypeDescription.Generic declaringType;
        private final List<? extends FieldDescription> fieldDescriptions;
        private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

        public TypeSubstituting(TypeDescription.Generic declaringType, List<? extends FieldDescription> fieldDescriptions, TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            this.declaringType = declaringType;
            this.fieldDescriptions = fieldDescriptions;
            this.visitor = visitor;
        }

        @Override // java.util.AbstractList, java.util.List
        public FieldDescription.InGenericShape get(int index) {
            return new FieldDescription.TypeSubstituting(this.declaringType, this.fieldDescriptions.get(index), this.visitor);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fieldDescriptions.size();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/field/FieldList$Empty.class */
    public static class Empty<S extends FieldDescription> extends FilterableList.Empty<S, FieldList<S>> implements FieldList<S> {
        @Override // net.bytebuddy.description.field.FieldList
        public ByteCodeElement.Token.TokenList<FieldDescription.Token> asTokenList(ElementMatcher<? super TypeDescription> matcher) {
            return new ByteCodeElement.Token.TokenList<>(new FieldDescription.Token[0]);
        }

        @Override // net.bytebuddy.description.field.FieldList
        public FieldList<FieldDescription.InDefinedShape> asDefined() {
            return this;
        }
    }
}
