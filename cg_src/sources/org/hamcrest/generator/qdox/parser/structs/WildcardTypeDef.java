package org.hamcrest.generator.qdox.parser.structs;

import net.bytebuddy.description.type.TypeDescription;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/WildcardTypeDef.class */
public class WildcardTypeDef extends TypeDef {
    private TypeDef typeDef;
    private String wildcardExpressionType;

    public WildcardTypeDef() {
        super(TypeDescription.Generic.OfWildcardType.SYMBOL);
    }

    public WildcardTypeDef(TypeDef typeDef, String wildcardExpressionType) {
        super(typeDef.name, typeDef.dimensions);
        this.typeDef = typeDef;
        this.wildcardExpressionType = wildcardExpressionType;
    }

    public TypeDef getTypeDef() {
        return this.typeDef;
    }

    public String getWildcardExpressionType() {
        return this.wildcardExpressionType;
    }
}
