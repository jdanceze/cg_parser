package org.hamcrest.generator.qdox.model;

import org.hamcrest.generator.qdox.parser.structs.TypeDef;
import org.hamcrest.generator.qdox.parser.structs.TypeVariableDef;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/TypeVariable.class */
public class TypeVariable extends Type {
    public static final TypeVariable[] EMPTY_ARRAY = new TypeVariable[0];
    private Type[] bounds;

    public TypeVariable(String fullName, TypeVariableDef def, JavaClassParent context) {
        super(fullName, def.name, 0, context);
        if (def.bounds != null && !def.bounds.isEmpty()) {
            this.bounds = new Type[def.bounds.size()];
            for (int index = 0; index < def.bounds.size(); index++) {
                this.bounds[index] = createUnresolved((TypeDef) def.bounds.get(index), context);
            }
        }
    }

    public static TypeVariable createUnresolved(TypeVariableDef def, JavaClassParent context) {
        return new TypeVariable(null, def, context);
    }

    @Override // org.hamcrest.generator.qdox.model.Type
    public String getValue() {
        return (this.bounds == null || this.bounds.length == 0) ? "" : this.bounds[0].getValue();
    }

    @Override // org.hamcrest.generator.qdox.model.Type
    public String getGenericValue() {
        StringBuffer result = new StringBuffer("<");
        result.append(super.getValue());
        if (this.bounds != null && this.bounds.length > 0) {
            result.append(" extends ");
            for (int index = 0; index < this.bounds.length; index++) {
                if (index > 0) {
                    result.append(",");
                }
                result.append(this.bounds[index].getGenericValue());
            }
        }
        result.append(">");
        return result.toString();
    }

    public String getName() {
        return super.getValue();
    }
}
