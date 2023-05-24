package net.bytebuddy.description.type;

import java.util.Collections;
import java.util.List;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.matcher.ElementMatcher;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/type/TypeVariableToken.class */
public class TypeVariableToken implements ByteCodeElement.Token<TypeVariableToken> {
    private final String symbol;
    private final List<? extends TypeDescription.Generic> bounds;
    private final List<? extends AnnotationDescription> annotations;
    private transient /* synthetic */ int hashCode_Gc6ioQlU;

    @Override // net.bytebuddy.description.ByteCodeElement.Token
    public /* bridge */ /* synthetic */ TypeVariableToken accept(TypeDescription.Generic.Visitor visitor) {
        return accept((TypeDescription.Generic.Visitor<? extends TypeDescription.Generic>) visitor);
    }

    public TypeVariableToken(String symbol, List<? extends TypeDescription.Generic> bounds) {
        this(symbol, bounds, Collections.emptyList());
    }

    public TypeVariableToken(String symbol, List<? extends TypeDescription.Generic> bounds, List<? extends AnnotationDescription> annotations) {
        this.symbol = symbol;
        this.bounds = bounds;
        this.annotations = annotations;
    }

    public static TypeVariableToken of(TypeDescription.Generic typeVariable, ElementMatcher<? super TypeDescription> matcher) {
        return new TypeVariableToken(typeVariable.getSymbol(), typeVariable.getUpperBounds().accept(new TypeDescription.Generic.Visitor.Substitutor.ForDetachment(matcher)), typeVariable.getDeclaredAnnotations());
    }

    public String getSymbol() {
        return this.symbol;
    }

    public TypeList.Generic getBounds() {
        return new TypeList.Generic.Explicit(this.bounds);
    }

    public AnnotationList getAnnotations() {
        return new AnnotationList.Explicit(this.annotations);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.bytebuddy.description.ByteCodeElement.Token
    public TypeVariableToken accept(TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
        return new TypeVariableToken(this.symbol, getBounds().accept(visitor), this.annotations);
    }

    @CachedReturnPlugin.Enhance
    public int hashCode() {
        int hashCode;
        if (this.hashCode_Gc6ioQlU != 0) {
            hashCode = 0;
        } else {
            int result = this.symbol.hashCode();
            hashCode = (31 * ((31 * result) + this.bounds.hashCode())) + this.annotations.hashCode();
        }
        int i = hashCode;
        if (i == 0) {
            i = this.hashCode_Gc6ioQlU;
        } else {
            this.hashCode_Gc6ioQlU = i;
        }
        return i;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TypeVariableToken)) {
            return false;
        }
        TypeVariableToken typeVariableToken = (TypeVariableToken) other;
        return this.symbol.equals(typeVariableToken.symbol) && this.bounds.equals(typeVariableToken.bounds) && this.annotations.equals(typeVariableToken.annotations);
    }

    public String toString() {
        return this.symbol;
    }
}
