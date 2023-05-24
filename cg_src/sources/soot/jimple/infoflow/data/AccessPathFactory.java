package soot.jimple.infoflow.data;

import gnu.trove.set.hash.TCustomHashSet;
import gnu.trove.strategy.HashingStrategy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Local;
import soot.PrimType;
import soot.RefLikeType;
import soot.RefType;
import soot.SootField;
import soot.Type;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.typing.TypeUtils;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/AccessPathFactory.class */
public class AccessPathFactory {
    protected static final Logger logger;
    private final InfoflowConfiguration config;
    private final TypeUtils typeUtils;
    private MyConcurrentHashMap<Type, Set<AccessPathFragment[]>> baseRegister = new MyConcurrentHashMap<>();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AccessPathFactory.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(AccessPathFactory.class);
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/AccessPathFactory$BasePair.class */
    public static class BasePair {
        private final SootField[] fields;
        private final Type[] types;
        private int hashCode = 0;

        private BasePair(SootField[] fields, Type[] types) {
            this.fields = fields;
            this.types = types;
            if (fields == null || fields.length == 0) {
                throw new RuntimeException("A base must contain at least one field");
            }
        }

        public SootField[] getFields() {
            return this.fields;
        }

        public Type[] getTypes() {
            return this.types;
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                int result = (31 * 1) + Arrays.hashCode(this.fields);
                this.hashCode = (31 * result) + Arrays.hashCode(this.types);
            }
            return this.hashCode;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            BasePair other = (BasePair) obj;
            if (!Arrays.equals(this.fields, other.fields) || !Arrays.equals(this.types, other.types)) {
                return false;
            }
            return true;
        }

        public String toString() {
            return Arrays.toString(this.fields);
        }
    }

    public AccessPathFactory(InfoflowConfiguration config, TypeUtils typeUtils) {
        this.config = config;
        this.typeUtils = typeUtils;
    }

    public AccessPath createAccessPath(Value val, boolean taintSubFields) {
        return createAccessPath(val, null, null, taintSubFields, false, true, AccessPath.ArrayTaintType.ContentsAndLength);
    }

    public AccessPath createAccessPath(Value val, Type valType, boolean taintSubFields, AccessPath.ArrayTaintType arrayTaintType) {
        return createAccessPath(val, valType, null, taintSubFields, false, true, arrayTaintType);
    }

    public AccessPath createAccessPath(Value val, SootField[] appendingFields, boolean taintSubFields) {
        return createAccessPath(val, null, AccessPathFragment.createFragmentArray(appendingFields, null), taintSubFields, false, true, AccessPath.ArrayTaintType.ContentsAndLength);
    }

    public AccessPath createAccessPath(Value val, AccessPathFragment[] appendingFragments, boolean taintSubFields) {
        return createAccessPath(val, null, appendingFragments, taintSubFields, false, true, AccessPath.ArrayTaintType.ContentsAndLength);
    }

    public AccessPath createAccessPath(Value val, Type valType, AccessPathFragment[] fragments, boolean taintSubFields, boolean cutFirstField, boolean reduceBases, AccessPath.ArrayTaintType arrayTaintType) {
        return createAccessPath(val, valType, fragments, taintSubFields, cutFirstField, reduceBases, arrayTaintType, false);
    }

    public AccessPath createAccessPath(Value val, Type valType, AccessPathFragment[] appendingFragments, boolean taintSubFields, boolean cutFirstField, boolean reduceBases, AccessPath.ArrayTaintType arrayTaintType, boolean canHaveImmutableAliases) {
        Local value;
        Type baseType;
        AccessPathFragment[] fragments;
        boolean cutOffApproximation;
        if (val != null && !AccessPath.canContainValue(val)) {
            logger.error("Access paths cannot be rooted in values of type {}", val.getClass().getName());
            return null;
        } else if (val == null && appendingFragments == null) {
            return null;
        } else {
            InfoflowConfiguration.AccessPathConfiguration accessPathConfig = this.config.getAccessPathConfiguration();
            if (!this.config.getEnableTypeChecking()) {
                valType = null;
            }
            if (val instanceof FieldRef) {
                FieldRef ref = (FieldRef) val;
                if (val instanceof InstanceFieldRef) {
                    InstanceFieldRef iref = (InstanceFieldRef) val;
                    value = (Local) iref.getBase();
                    baseType = value.getType();
                } else {
                    value = null;
                    baseType = null;
                }
                fragments = new AccessPathFragment[(appendingFragments == null ? 0 : appendingFragments.length) + 1];
                fragments[0] = new AccessPathFragment(ref.getField(), null);
                if (appendingFragments != null) {
                    System.arraycopy(appendingFragments, 0, fragments, 1, appendingFragments.length);
                }
            } else if (val instanceof ArrayRef) {
                ArrayRef ref2 = (ArrayRef) val;
                value = (Local) ref2.getBase();
                baseType = valType == null ? value.getType() : valType;
                fragments = appendingFragments == null ? null : (AccessPathFragment[]) Arrays.copyOf(appendingFragments, appendingFragments.length);
            } else {
                value = (Local) val;
                baseType = valType == null ? value == null ? null : value.getType() : valType;
                fragments = appendingFragments == null ? null : (AccessPathFragment[]) Arrays.copyOf(appendingFragments, appendingFragments.length);
            }
            if (accessPathConfig.getAccessPathLength() == 0) {
                fragments = null;
            }
            if (cutFirstField && fragments != null && fragments.length > 0) {
                AccessPathFragment[] newFragments = new AccessPathFragment[fragments.length - 1];
                System.arraycopy(fragments, 1, newFragments, 0, newFragments.length);
                fragments = newFragments.length > 0 ? newFragments : null;
            }
            if (this.config.getAccessPathConfiguration().getUseSameFieldReduction() && fragments != null && fragments.length > 1) {
                int bucketStart = fragments.length - 2;
                while (true) {
                    if (bucketStart < 0) {
                        break;
                    }
                    int repeatPos = -1;
                    int i = bucketStart + 1;
                    while (true) {
                        if (i < fragments.length) {
                            if (fragments[i].getField() != fragments[bucketStart].getField()) {
                                i++;
                            } else {
                                repeatPos = i;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    int repeatLen = repeatPos - bucketStart;
                    if (repeatPos >= 0) {
                        boolean matches = true;
                        for (int i2 = 0; i2 < repeatPos - bucketStart; i2++) {
                            matches &= repeatPos + i2 < fragments.length && fragments[bucketStart + i2].getField() == fragments[repeatPos + i2].getField();
                        }
                        if (matches) {
                            AccessPathFragment[] newFragments2 = new AccessPathFragment[fragments.length - repeatLen];
                            System.arraycopy(fragments, 0, newFragments2, 0, bucketStart + 1);
                            System.arraycopy(fragments, repeatPos + 1, newFragments2, bucketStart + 1, (fragments.length - repeatPos) - 1);
                            fragments = newFragments2;
                            break;
                        }
                    }
                    bucketStart--;
                }
            }
            if (this.config.getEnableTypeChecking()) {
                if (value != null && value.getType() != baseType) {
                    baseType = this.typeUtils.getMorePreciseType(baseType, value.getType());
                    if (baseType == null) {
                        return null;
                    }
                    if (fragments != null && fragments.length > 0 && !(baseType instanceof ArrayType)) {
                        baseType = this.typeUtils.getMorePreciseType(baseType, fragments[0].getField().getDeclaringClass().getType());
                    }
                    if (baseType == null) {
                        return null;
                    }
                }
                if (fragments != null && fragments.length > 0) {
                    for (int i3 = 0; i3 < fragments.length - 1; i3++) {
                        AccessPathFragment curFragment = fragments[i3];
                        Type curType = curFragment.getFieldType();
                        if (!(curType instanceof ArrayType)) {
                            curType = this.typeUtils.getMorePreciseType(curType, fragments[i3 + 1].getField().getDeclaringClass().getType());
                        }
                        if (curType != curType) {
                            fragments[i3] = curFragment.copyWithNewType(curType);
                        }
                    }
                }
            }
            if (fragments != null && Arrays.stream(fragments).anyMatch(f -> {
                return !f.isValid();
            })) {
                return null;
            }
            if (value != null && (value.getType() instanceof ArrayType)) {
                ArrayType at = (ArrayType) value.getType();
                if (!(at.getArrayElementType() instanceof RefLikeType) && fragments != null && fragments.length > 0) {
                    return null;
                }
            }
            if (accessPathConfig.getUseThisChainReduction() && reduceBases && fragments != null) {
                int i4 = 0;
                while (true) {
                    if (i4 >= fragments.length) {
                        break;
                    }
                    SootField curField = fragments[i4].getField();
                    if (curField.getName().startsWith("this$")) {
                        String outerClassName = ((RefType) curField.getType()).getClassName();
                        int startIdx = -1;
                        if (value != null && (value.getType() instanceof RefType) && ((RefType) value.getType()).getClassName().equals(outerClassName)) {
                            startIdx = 0;
                        } else {
                            int j = 0;
                            while (true) {
                                if (j >= i4) {
                                    break;
                                }
                                SootField nextField = fragments[j].getField();
                                if (!(nextField.getType() instanceof RefType) || !((RefType) nextField.getType()).getClassName().equals(outerClassName)) {
                                    j++;
                                } else {
                                    startIdx = j;
                                    break;
                                }
                            }
                        }
                        if (startIdx >= 0) {
                            AccessPathFragment[] newFragments3 = new AccessPathFragment[(fragments.length - (i4 - startIdx)) - 1];
                            System.arraycopy(fragments, 0, newFragments3, 0, startIdx);
                            System.arraycopy(fragments, i4 + 1, newFragments3, startIdx, (fragments.length - i4) - 1);
                            fragments = newFragments3;
                            break;
                        }
                    }
                    i4++;
                }
            }
            boolean recursiveCutOff = false;
            if (accessPathConfig.getUseRecursiveAccessPaths() && reduceBases && fragments != null) {
                int ei = val instanceof StaticFieldRef ? 1 : 0;
                while (ei < fragments.length) {
                    Type eiType = ei == 0 ? baseType : fragments[ei - 1].getFieldType();
                    int ej = ei;
                    while (ej < fragments.length) {
                        if (fragments[ej].getFieldType() == eiType || fragments[ej].getField().getType() == eiType) {
                            AccessPathFragment[] newFragments4 = new AccessPathFragment[(fragments.length - (ej - ei)) - 1];
                            System.arraycopy(fragments, 0, newFragments4, 0, ei);
                            if (fragments.length > ej) {
                                System.arraycopy(fragments, ej + 1, newFragments4, ei, (fragments.length - ej) - 1);
                            }
                            AccessPathFragment[] base = new AccessPathFragment[(ej - ei) + 1];
                            System.arraycopy(fragments, ei, base, 0, base.length);
                            registerBase(eiType, base);
                            fragments = newFragments4;
                            recursiveCutOff = true;
                        } else {
                            ej++;
                        }
                    }
                    ei++;
                }
            }
            if (fragments != null) {
                int maxAccessPathLength = accessPathConfig.getAccessPathLength();
                if (maxAccessPathLength >= 0) {
                    int fieldNum = Math.min(maxAccessPathLength, fragments.length);
                    if (fragments.length > fieldNum) {
                        taintSubFields = true;
                        cutOffApproximation = true;
                    } else {
                        cutOffApproximation = recursiveCutOff;
                    }
                    if (fieldNum == 0) {
                        fragments = null;
                    } else {
                        AccessPathFragment[] newFragments5 = new AccessPathFragment[fieldNum];
                        System.arraycopy(fragments, 0, newFragments5, 0, fieldNum);
                        fragments = newFragments5;
                    }
                } else {
                    cutOffApproximation = recursiveCutOff;
                }
            } else {
                cutOffApproximation = false;
                fragments = null;
            }
            if ($assertionsDisabled || value == null || (baseType instanceof ArrayType) || TypeUtils.isObjectLikeType(baseType) || !(value.getType() instanceof ArrayType)) {
                if ($assertionsDisabled || value == null || !(baseType instanceof ArrayType) || (value.getType() instanceof ArrayType) || TypeUtils.isObjectLikeType(value.getType())) {
                    if ((baseType instanceof PrimType) && fragments != null && fragments.length > 0) {
                        logger.warn("Primitive types cannot have fields: baseType={} fields={}", baseType, Arrays.toString(fragments));
                        return null;
                    } else {
                        if (fragments != null) {
                            for (int i5 = 0; i5 < fragments.length - 2; i5++) {
                                SootField f2 = fragments[i5].getField();
                                Type fieldType = f2.getType();
                                if (fieldType instanceof PrimType) {
                                    logger.warn("Primitive types cannot have fields: field={} type={}", f2, fieldType);
                                    return null;
                                }
                            }
                        }
                        return new AccessPath(value, baseType, fragments, taintSubFields, cutOffApproximation, arrayTaintType, canHaveImmutableAliases);
                    }
                }
                throw new AssertionError("Type mismatch. Type was " + baseType + ", value was: " + (value == null ? null : value.getType()));
            }
            throw new AssertionError();
        }
    }

    private void registerBase(Type eiType, AccessPathFragment[] base) {
        Set<AccessPathFragment[]> bases = this.baseRegister.computeIfAbsent(eiType, t -> {
            return Collections.synchronizedSet(new TCustomHashSet(new HashingStrategy<AccessPathFragment[]>() { // from class: soot.jimple.infoflow.data.AccessPathFactory.1
                @Override // gnu.trove.strategy.HashingStrategy
                public int computeHashCode(AccessPathFragment[] arg0) {
                    return Arrays.hashCode(arg0);
                }

                @Override // gnu.trove.strategy.HashingStrategy
                public boolean equals(AccessPathFragment[] arg0, AccessPathFragment[] arg1) {
                    return Arrays.equals(arg0, arg1);
                }
            }));
        });
        bases.add(base);
    }

    public Collection<AccessPathFragment[]> getBaseForType(Type tp) {
        return this.baseRegister.get(tp);
    }

    public AccessPath copyWithNewValue(AccessPath original, Value val) {
        return copyWithNewValue(original, val, original.getBaseType(), false);
    }

    public AccessPath copyWithNewValue(AccessPath original, Value val, Type newType, boolean cutFirstField) {
        return copyWithNewValue(original, val, newType, cutFirstField, true);
    }

    public AccessPath copyWithNewValue(AccessPath original, Value val, Type newType, boolean cutFirstField, boolean reduceBases) {
        return copyWithNewValue(original, val, newType, cutFirstField, reduceBases, original.getArrayTaintType());
    }

    public AccessPath copyWithNewValue(AccessPath original, Value val, Type newType, boolean cutFirstField, boolean reduceBases, AccessPath.ArrayTaintType arrayTaintType) {
        if (original.getPlainValue() != null && original.getPlainValue().equals(val) && original.getBaseType().equals(newType) && original.getArrayTaintType() == arrayTaintType) {
            return original;
        }
        AccessPath newAP = createAccessPath(val, newType, original.getFragments(), original.getTaintSubFields(), cutFirstField, reduceBases, arrayTaintType, original.getCanHaveImmutableAliases());
        if (newAP != null && newAP.equals(original)) {
            return original;
        }
        return newAP;
    }

    public AccessPath merge(AccessPath ap1, AccessPath ap2) {
        return appendFields(ap1, ap2.getFragments(), ap2.getTaintSubFields());
    }

    public AccessPath appendFields(AccessPath original, AccessPathFragment[] toAppend, boolean taintSubFields) {
        if (toAppend == null || toAppend.length == 0) {
            return original;
        }
        int offset = original.getFragmentCount();
        AccessPathFragment[] fragments = new AccessPathFragment[offset + (toAppend == null ? 0 : toAppend.length)];
        if (offset > 0) {
            System.arraycopy(original.getFragments(), 0, fragments, 0, offset);
        }
        System.arraycopy(toAppend, 0, fragments, offset, toAppend.length);
        return createAccessPath(original.getPlainValue(), original.getBaseType(), fragments, taintSubFields, false, true, original.getArrayTaintType());
    }
}
