package org.hamcrest.generator.qdox.model;

import net.bytebuddy.description.type.TypeDescription;
import org.hamcrest.generator.qdox.parser.structs.WildcardTypeDef;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/WildcardType.class */
public class WildcardType extends Type {
    private String wildcardExpressionType;

    public WildcardType() {
        super(TypeDescription.Generic.OfWildcardType.SYMBOL);
        this.wildcardExpressionType = null;
    }

    public WildcardType(WildcardTypeDef typeDef) {
        this(typeDef, null);
    }

    public WildcardType(WildcardTypeDef typeDef, JavaClassParent context) {
        super((String) null, typeDef, 0, context);
        this.wildcardExpressionType = null;
        this.wildcardExpressionType = typeDef.getWildcardExpressionType();
    }

    @Override // org.hamcrest.generator.qdox.model.Type
    public String getGenericValue() {
        String result = "";
        if (this.wildcardExpressionType != null) {
            result = new StringBuffer().append(result).append("? ").append(this.wildcardExpressionType).append(Instruction.argsep).toString();
        }
        return new StringBuffer().append(result).append(super.getGenericValue()).toString();
    }
}
