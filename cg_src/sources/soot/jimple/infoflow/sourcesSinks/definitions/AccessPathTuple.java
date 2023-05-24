package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.Type;
import soot.Value;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.data.AccessPathFragment;
import soot.jimple.infoflow.typing.TypeUtils;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/AccessPathTuple.class */
public class AccessPathTuple {
    private final String baseType;
    private final String[] fields;
    private final String[] fieldTypes;
    private final SourceSinkType sinkSource;
    private String description;
    private int hashCode;
    private static AccessPathTuple SOURCE_TUPLE;
    private static AccessPathTuple SINK_TUPLE;

    AccessPathTuple(String[] fields, String[] fieldTypes, SourceSinkType sinkSource) {
        this(null, fields, fieldTypes, sinkSource);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessPathTuple(String baseType, String[] fields, String[] fieldTypes, SourceSinkType sinkSource) {
        this.hashCode = 0;
        this.baseType = baseType;
        this.fields = fields;
        this.fieldTypes = fieldTypes;
        this.sinkSource = sinkSource;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessPathTuple(AccessPathTuple original) {
        this.hashCode = 0;
        this.baseType = original.baseType;
        this.fields = original.fields;
        this.fieldTypes = original.fields;
        this.sinkSource = original.sinkSource;
        this.description = original.description;
    }

    public static AccessPathTuple create(boolean isSource, boolean isSink) {
        return fromPathElements((String[]) null, (String[]) null, isSource, isSink);
    }

    public static AccessPathTuple fromPathElements(List<String> fields, List<String> fieldTypes, boolean isSource, boolean isSink) {
        String[] fieldArray = (fields == null || fields.isEmpty()) ? null : (String[]) fields.toArray(new String[fields.size()]);
        String[] fieldTypeArray = (fieldTypes == null || fieldTypes.isEmpty()) ? null : (String[]) fieldTypes.toArray(new String[fieldTypes.size()]);
        return fromPathElements(fieldArray, fieldTypeArray, isSource, isSink);
    }

    public static AccessPathTuple fromPathElements(List<String> fields, List<String> fieldTypes, SourceSinkType sourceSinkType) {
        return fromPathElements((String) null, fields, fieldTypes, sourceSinkType);
    }

    public static AccessPathTuple fromPathElements(String field, String fieldType, SourceSinkType sourceSinkType) {
        return fromPathElements((String) null, field, fieldType, sourceSinkType);
    }

    public static AccessPathTuple fromPathElements(String baseType, String field, String fieldType, SourceSinkType sourceSinkType) {
        return fromPathElements(baseType, Collections.singletonList(field), Collections.singletonList(fieldType), sourceSinkType);
    }

    public static AccessPathTuple fromPathElements(String baseType, List<String> fields, List<String> fieldTypes, SourceSinkType sourceSinkType) {
        String[] fieldArray = (fields == null || fields.isEmpty()) ? null : (String[]) fields.toArray(new String[fields.size()]);
        String[] fieldTypeArray = (fieldTypes == null || fieldTypes.isEmpty()) ? null : (String[]) fieldTypes.toArray(new String[fieldTypes.size()]);
        return new AccessPathTuple(baseType, fieldArray, fieldTypeArray, sourceSinkType);
    }

    public static AccessPathTuple fromPathElements(String[] fields, String[] fieldTypes, boolean isSource, boolean isSink) {
        return new AccessPathTuple(fields, fieldTypes, SourceSinkType.fromFlags(isSink, isSource));
    }

    public static AccessPathTuple fromPathElements(String baseType, String[] fields, String[] fieldTypes, boolean isSource, boolean isSink) {
        return new AccessPathTuple(baseType, fields, fieldTypes, SourceSinkType.fromFlags(isSink, isSource));
    }

    public String getBaseType() {
        return this.baseType;
    }

    public String[] getFields() {
        return this.fields;
    }

    public String[] getFieldTypes() {
        return this.fieldTypes;
    }

    public SourceSinkType getSourceSinkType() {
        return this.sinkSource;
    }

    public static AccessPathTuple getBlankSourceTuple() {
        if (SOURCE_TUPLE == null) {
            SOURCE_TUPLE = new ImmutableAccessPathTuple(create(true, false));
        }
        return SOURCE_TUPLE;
    }

    public static AccessPathTuple getBlankSinkTuple() {
        if (SINK_TUPLE == null) {
            SINK_TUPLE = new ImmutableAccessPathTuple(create(false, true));
        }
        return SINK_TUPLE;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        int result = (31 * ((31 * ((31 * ((31 * ((31 * 1) + (this.baseType == null ? 0 : this.baseType.hashCode()))) + (this.description == null ? 0 : this.description.hashCode()))) + Arrays.hashCode(this.fieldTypes))) + Arrays.hashCode(this.fields))) + (this.sinkSource == null ? 0 : this.sinkSource.hashCode());
        this.hashCode = result;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccessPathTuple other = (AccessPathTuple) obj;
        if (this.baseType == null) {
            if (other.baseType != null) {
                return false;
            }
        } else if (!this.baseType.equals(other.baseType)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (!Arrays.equals(this.fieldTypes, other.fieldTypes) || !Arrays.equals(this.fields, other.fields) || this.sinkSource != other.sinkSource) {
            return false;
        }
        return true;
    }

    public AccessPathTuple simplify() {
        AccessPathTuple blankSource = getBlankSourceTuple();
        AccessPathTuple blankSink = getBlankSinkTuple();
        if (equals(blankSource)) {
            return blankSource;
        }
        if (equals(blankSink)) {
            return blankSink;
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v53 */
    /* JADX WARN: Type inference failed for: r0v54, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v57 */
    public AccessPath toAccessPath(Value baseVal, InfoflowManager manager, boolean canHaveImmutableAliases) {
        if ((baseVal.getType() instanceof PrimType) || this.fields == null || this.fields.length == 0) {
            return manager.getAccessPathFactory().createAccessPath(baseVal, null, null, true, false, true, AccessPath.ArrayTaintType.ContentsAndLength, canHaveImmutableAliases);
        }
        RefType baseType = (this.baseType == null || this.baseType.isEmpty()) ? null : RefType.v(this.baseType);
        SootClass baseClass = baseType == null ? ((RefType) baseVal.getType()).getSootClass() : baseType.getSootClass();
        AccessPathFragment[] fragments = new AccessPathFragment[this.fields.length];
        int i = 0;
        while (i < fragments.length) {
            String fieldName = this.fields[i];
            Type lastFieldType = i == 0 ? baseClass.getType() : TypeUtils.getTypeFromString(this.fieldTypes[i - 1]);
            if (!(lastFieldType instanceof RefType)) {
                throw new InvalidAccessPathException(String.format("Type %s cannot have fields (requested: %s)", lastFieldType.toString(), fieldName));
            }
            SootClass lastFieldClass = ((RefType) lastFieldType).getSootClass();
            Type fieldType = TypeUtils.getTypeFromString(this.fieldTypes[i]);
            SootField fld = lastFieldClass.getFieldUnsafe(fieldName, fieldType);
            if (fld == null) {
                ?? r0 = lastFieldClass;
                synchronized (r0) {
                    fld = lastFieldClass.getFieldUnsafe(fieldName, fieldType);
                    r0 = fld;
                    if (r0 == 0) {
                        SootField f = Scene.v().makeSootField(fieldName, fieldType, 0);
                        f.setPhantom(true);
                        fld = lastFieldClass.getOrAddField(f);
                    }
                }
            }
            if (fld == null) {
                return null;
            }
            fragments[i] = new AccessPathFragment(fld, fieldType);
            i++;
        }
        return manager.getAccessPathFactory().createAccessPath(baseVal, baseType, fragments, true, false, true, AccessPath.ArrayTaintType.ContentsAndLength, canHaveImmutableAliases);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.fields != null && this.fields.length > 0) {
            for (int i = 0; i < this.fields.length; i++) {
                if (i > 0) {
                    sb.append(".");
                }
                sb.append(this.fields[i]);
            }
        } else {
            sb.append("<empty>");
        }
        if (this.description != null && !this.description.isEmpty()) {
            sb.append(" (");
            sb.append(this.description);
            sb.append(")");
        }
        return sb.toString();
    }
}
