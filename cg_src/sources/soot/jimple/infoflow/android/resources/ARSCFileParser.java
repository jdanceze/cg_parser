package soot.jimple.infoflow.android.resources;

import android.text.Spanned;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.android.axml.ApkHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser.class */
public class ARSCFileParser extends AbstractResourceParser {
    protected static final int RES_STRING_POOL_TYPE = 1;
    protected static final int RES_TABLE_TYPE = 2;
    protected static final int RES_TABLE_PACKAGE_TYPE = 512;
    protected static final int RES_TABLE_TYPE_SPEC_TYPE = 514;
    protected static final int RES_TABLE_TYPE_TYPE = 513;
    protected static final int SORTED_FLAG = 1;
    protected static final int UTF8_FLAG = 256;
    protected static final int SPEC_PUBLIC = 1073741824;
    protected static final int TYPE_NULL = 0;
    protected static final int TYPE_REFERENCE = 1;
    protected static final int TYPE_ATTRIBUTE = 2;
    protected static final int TYPE_STRING = 3;
    protected static final int TYPE_FLOAT = 4;
    protected static final int TYPE_DIMENSION = 5;
    protected static final int TYPE_FRACTION = 6;
    protected static final int TYPE_FIRST_INT = 16;
    protected static final int TYPE_INT_DEC = 16;
    protected static final int TYPE_INT_HEX = 17;
    protected static final int TYPE_INT_BOOLEAN = 18;
    protected static final int TYPE_FIRST_COLOR_INT = 28;
    protected static final int TYPE_INT_COLOR_ARGB8 = 28;
    protected static final int TYPE_INT_COLOR_RGB8 = 29;
    protected static final int TYPE_INT_COLOR_ARGB4 = 30;
    protected static final int TYPE_INT_COLOR_RGB4 = 31;
    protected static final int TYPE_LAST_COLOR_INT = 31;
    protected static final int TYPE_LAST_INT = 31;
    protected static final int ATTR_TYPE = 16777216;
    protected static final int ATTR_MIN = 16777217;
    protected static final int ATTR_MAX = 16777218;
    protected static final int ATTR_L10N = 16777219;
    protected static final int ATTR_OTHER = 16777220;
    protected static final int ATTR_ZERO = 16777221;
    protected static final int ATTR_ONE = 16777222;
    protected static final int ATTR_TWO = 16777223;
    protected static final int ATTR_FEW = 16777224;
    protected static final int ATTR_MANY = 16777225;
    protected static final int NO_ENTRY = -1;
    protected static final int COMPLEX_UNIT_SHIFT = 0;
    protected static final int COMPLEX_UNIT_MASK = 15;
    protected static final int COMPLEX_UNIT_PX = 0;
    protected static final int COMPLEX_UNIT_DIP = 1;
    protected static final int COMPLEX_UNIT_SP = 2;
    protected static final int COMPLEX_UNIT_PT = 3;
    protected static final int COMPLEX_UNIT_IN = 4;
    protected static final int COMPLEX_UNIT_MM = 5;
    protected static final int COMPLEX_UNIT_FRACTION = 0;
    protected static final int COMPLEX_UNIT_FRACTION_PARENT = 1;
    protected static final int COMPLEX_RADIX_SHIFT = 4;
    protected static final int COMPLEX_RADIX_MASK = 3;
    protected static final int COMPLEX_RADIX_23p0 = 0;
    protected static final int COMPLEX_RADIX_16p7 = 1;
    protected static final int COMPLEX_RADIX_8p15 = 2;
    protected static final int COMPLEX_RADIX_0p23 = 3;
    protected static final int COMPLEX_MANTISSA_SHIFT = 8;
    protected static final int COMPLEX_MANTISSA_MASK = 16777215;
    protected static final float MANTISSA_MULT = 0.00390625f;
    protected static final float[] RADIX_MULTS;
    public static final int FLAG_COMPLEX = 1;
    public static final int FLAG_PUBLIC = 2;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Integer, String> stringTable = new HashMap();
    private final List<ResPackage> packages = new ArrayList();

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$Dimension.class */
    public enum Dimension {
        PX,
        DIP,
        SP,
        PT,
        IN,
        MM;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Dimension[] valuesCustom() {
            Dimension[] valuesCustom = values();
            int length = valuesCustom.length;
            Dimension[] dimensionArr = new Dimension[length];
            System.arraycopy(valuesCustom, 0, dimensionArr, 0, length);
            return dimensionArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$FractionType.class */
    public enum FractionType {
        Fraction,
        FractionParent;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FractionType[] valuesCustom() {
            FractionType[] valuesCustom = values();
            int length = valuesCustom.length;
            FractionType[] fractionTypeArr = new FractionType[length];
            System.arraycopy(valuesCustom, 0, fractionTypeArr, 0, length);
            return fractionTypeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$NullResource.class */
    public static class NullResource extends AbstractResource {
    }

    static {
        $assertionsDisabled = !ARSCFileParser.class.desiredAssertionStatus();
        RADIX_MULTS = new float[]{MANTISSA_MULT, 3.0517578E-5f, 1.1920929E-7f, 4.656613E-10f};
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResPackage.class */
    public static class ResPackage {
        private int packageId;
        private String packageName;
        private List<ResType> types = new ArrayList();

        public int getPackageId() {
            return this.packageId;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public List<ResType> getDeclaredTypes() {
            return this.types;
        }

        public ResType getResourceType(String type) {
            for (ResType resType : this.types) {
                if (resType.typeName.equals(type)) {
                    return resType;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAll(ResPackage other) {
            for (ResType tp : other.types) {
                ResType existingType = getType(tp.id, tp.typeName);
                if (existingType == null) {
                    this.types.add(tp);
                } else {
                    existingType.addAll(tp);
                }
            }
        }

        public ResType getType(int id, String typeName) {
            return this.types.stream().filter(t -> {
                return t.id == id && t.typeName.equals(typeName);
            }).findFirst().orElse(null);
        }

        public int hashCode() {
            int result = (31 * 1) + this.packageId;
            return (31 * ((31 * result) + (this.packageName == null ? 0 : this.packageName.hashCode()))) + (this.types == null ? 0 : this.types.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResPackage other = (ResPackage) obj;
            if (this.packageId != other.packageId) {
                return false;
            }
            if (this.packageName == null) {
                if (other.packageName != null) {
                    return false;
                }
            } else if (!this.packageName.equals(other.packageName)) {
                return false;
            }
            if (this.types == null) {
                if (other.types != null) {
                    return false;
                }
                return true;
            } else if (!this.types.equals(other.types)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResType.class */
    public static class ResType {
        private int id;
        private String typeName;
        private List<ResConfig> configurations = new ArrayList();

        public String getTypeName() {
            return this.typeName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAll(ResType tp) {
            for (ResConfig config : tp.configurations) {
                ResConfig existingConfig = getConfiguration(config.getConfig());
                if (existingConfig == null) {
                    this.configurations.add(config);
                } else {
                    existingConfig.addAll(config);
                }
            }
        }

        public ResConfig getConfiguration(ResTable_Config config) {
            return this.configurations.stream().filter(c -> {
                return c.config.equals(config);
            }).findFirst().orElse(null);
        }

        public List<ResConfig> getConfigurations() {
            return this.configurations;
        }

        public Collection<AbstractResource> getAllResources() {
            Map<String, AbstractResource> resources = new HashMap<>();
            for (ResConfig rc : this.configurations) {
                for (AbstractResource res : rc.getResources()) {
                    if (!resources.containsKey(res.resourceName)) {
                        resources.put(res.resourceName, res);
                    }
                }
            }
            return resources.values();
        }

        public Collection<String> getAllResourceNames() {
            return (Collection) this.configurations.stream().flatMap(c -> {
                return c.getResources().stream();
            }).map(r -> {
                return r.getResourceName();
            }).collect(Collectors.toSet());
        }

        public List<AbstractResource> getAllResources(int resourceID) {
            List<AbstractResource> resourceList = new ArrayList<>();
            for (ResConfig rc : this.configurations) {
                for (AbstractResource res : rc.getResources()) {
                    if (res.resourceID == resourceID) {
                        resourceList.add(res);
                    }
                }
            }
            return resourceList;
        }

        public AbstractResource getResourceByName(String resourceName) {
            for (ResConfig rc : this.configurations) {
                for (AbstractResource res : rc.getResources()) {
                    if (res.getResourceName().equals(resourceName)) {
                        return res;
                    }
                }
            }
            return null;
        }

        public AbstractResource getFirstResource(String resourceName) {
            for (ResConfig rc : this.configurations) {
                for (AbstractResource res : rc.getResources()) {
                    if (res.resourceName.equals(resourceName)) {
                        return res;
                    }
                }
            }
            return null;
        }

        public AbstractResource getFirstResource(int resourceID) {
            for (ResConfig rc : this.configurations) {
                for (AbstractResource res : rc.getResources()) {
                    if (res.resourceID == resourceID) {
                        return res;
                    }
                }
            }
            return null;
        }

        public String toString() {
            return this.typeName;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.configurations == null ? 0 : this.configurations.hashCode());
            return (31 * ((31 * result) + this.id)) + (this.typeName == null ? 0 : this.typeName.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResType other = (ResType) obj;
            if (this.configurations == null) {
                if (other.configurations != null) {
                    return false;
                }
            } else if (!this.configurations.equals(other.configurations)) {
                return false;
            }
            if (this.id != other.id) {
                return false;
            }
            if (this.typeName == null) {
                if (other.typeName != null) {
                    return false;
                }
                return true;
            } else if (!this.typeName.equals(other.typeName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResConfig.class */
    public static class ResConfig {
        private ResTable_Config config;
        private List<AbstractResource> resources = new ArrayList();

        public ResTable_Config getConfig() {
            return this.config;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addAll(ResConfig other) {
            this.resources.addAll(other.resources);
        }

        public List<AbstractResource> getResources() {
            return this.resources;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.config == null ? 0 : this.config.hashCode());
            return (31 * result) + (this.resources == null ? 0 : this.resources.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResConfig other = (ResConfig) obj;
            if (this.config == null) {
                if (other.config != null) {
                    return false;
                }
            } else if (!this.config.equals(other.config)) {
                return false;
            }
            if (this.resources == null) {
                if (other.resources != null) {
                    return false;
                }
                return true;
            } else if (!this.resources.equals(other.resources)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$AbstractResource.class */
    public static abstract class AbstractResource {
        private String resourceName;
        private int resourceID;

        public String getResourceName() {
            return this.resourceName;
        }

        public int getResourceID() {
            return this.resourceID;
        }

        public int hashCode() {
            int result = (31 * 1) + this.resourceID;
            return (31 * result) + (this.resourceName == null ? 0 : this.resourceName.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AbstractResource other = (AbstractResource) obj;
            if (this.resourceID != other.resourceID) {
                return false;
            }
            if (this.resourceName == null) {
                if (other.resourceName != null) {
                    return false;
                }
                return true;
            } else if (!this.resourceName.equals(other.resourceName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ReferenceResource.class */
    public static class ReferenceResource extends AbstractResource {
        private int referenceID;

        public ReferenceResource(int id) {
            this.referenceID = id;
        }

        public int getReferenceID() {
            return this.referenceID;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + this.referenceID;
            return result;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ReferenceResource other = (ReferenceResource) obj;
            if (this.referenceID != other.referenceID) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$AttributeResource.class */
    public static class AttributeResource extends AbstractResource {
        private int attributeID;

        public AttributeResource(int id) {
            this.attributeID = id;
        }

        public int getAttributeID() {
            return this.attributeID;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + this.attributeID;
            return result;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            AttributeResource other = (AttributeResource) obj;
            if (this.attributeID != other.attributeID) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$StringResource.class */
    public static class StringResource extends AbstractResource {
        private String value;

        public StringResource(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            return this.value;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + (this.value == null ? 0 : this.value.hashCode());
            return result;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StringResource other = (StringResource) obj;
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
                return true;
            } else if (!this.value.equals(other.value)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$IntegerResource.class */
    public static class IntegerResource extends AbstractResource {
        private int value;

        public IntegerResource(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public String toString() {
            return Integer.toString(this.value);
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + this.value;
            return result;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IntegerResource other = (IntegerResource) obj;
            if (this.value != other.value) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$FloatResource.class */
    public static class FloatResource extends AbstractResource {
        private float value;

        public FloatResource(float value) {
            this.value = value;
        }

        public float getValue() {
            return this.value;
        }

        public String toString() {
            return Float.toString(this.value);
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + Float.floatToIntBits(this.value);
            return result;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            FloatResource other = (FloatResource) obj;
            if (Float.floatToIntBits(this.value) != Float.floatToIntBits(other.value)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$BooleanResource.class */
    public static class BooleanResource extends AbstractResource {
        private boolean value;

        public BooleanResource(int value) {
            this.value = value != 0;
        }

        public boolean getValue() {
            return this.value;
        }

        public String toString() {
            return Boolean.toString(this.value);
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + (this.value ? 1231 : 1237);
            return result;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            BooleanResource other = (BooleanResource) obj;
            if (this.value != other.value) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ColorResource.class */
    public static class ColorResource extends AbstractResource {
        private int a;
        private int r;
        private int g;
        private int b;

        public ColorResource(int a, int r, int g, int b) {
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int getA() {
            return this.a;
        }

        public int getR() {
            return this.r;
        }

        public int getG() {
            return this.g;
        }

        public int getB() {
            return this.b;
        }

        public int getARGB() {
            return (this.a << 24) | (this.r << 16) | (this.g << 8) | this.b;
        }

        public String toString() {
            return String.format("#%02x%02x%02x%02x", Integer.valueOf(this.a), Integer.valueOf(this.r), Integer.valueOf(this.g), Integer.valueOf(this.b));
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + this.a;
            return (31 * ((31 * ((31 * result) + this.b)) + this.g)) + this.r;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ColorResource other = (ColorResource) obj;
            if (this.a != other.a || this.b != other.b || this.g != other.g || this.r != other.r) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ArrayResource.class */
    public static class ArrayResource extends AbstractResource {
        private final List<AbstractResource> arrayElements;

        public ArrayResource() {
            this.arrayElements = new ArrayList();
        }

        public ArrayResource(List<AbstractResource> arrayElements) {
            this.arrayElements = arrayElements;
        }

        public void add(AbstractResource resource) {
            this.arrayElements.add(resource);
        }

        public String toString() {
            return this.arrayElements.toString();
        }

        public List<AbstractResource> getArrayElements() {
            return Collections.unmodifiableList(this.arrayElements);
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = (31 * 1) + (this.arrayElements == null ? 0 : this.arrayElements.hashCode());
            return result;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ArrayResource other = (ArrayResource) obj;
            if (this.arrayElements == null) {
                if (other.arrayElements != null) {
                    return false;
                }
                return true;
            } else if (!this.arrayElements.equals(other.arrayElements)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$FractionResource.class */
    public static class FractionResource extends AbstractResource {
        private FractionType type;
        private float value;

        public FractionResource(FractionType type, float value) {
            this.type = type;
            this.value = value;
        }

        public FractionType getType() {
            return this.type;
        }

        public float getValue() {
            return this.value;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = super.hashCode();
            return (31 * ((31 * result) + (this.type == null ? 0 : this.type.hashCode()))) + Float.floatToIntBits(this.value);
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            FractionResource other = (FractionResource) obj;
            if (this.type != other.type || Float.floatToIntBits(this.value) != Float.floatToIntBits(other.value)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$DimensionResource.class */
    public static class DimensionResource extends AbstractResource {
        private int value;
        private Dimension unit;

        public DimensionResource(int value, Dimension unit) {
            this.value = value;
            this.unit = unit;
        }

        DimensionResource(int dimension, int value) {
            this.value = value;
            switch (dimension) {
                case 0:
                    this.unit = Dimension.PX;
                    return;
                case 1:
                    this.unit = Dimension.DIP;
                    return;
                case 2:
                    this.unit = Dimension.SP;
                    return;
                case 3:
                    this.unit = Dimension.PT;
                    return;
                case 4:
                    this.unit = Dimension.IN;
                    return;
                case 5:
                    this.unit = Dimension.MM;
                    return;
                default:
                    throw new RuntimeException("Invalid dimension: " + dimension);
            }
        }

        public int getValue() {
            return this.value;
        }

        public Dimension getUnit() {
            return this.unit;
        }

        public String toString() {
            return String.valueOf(Integer.toString(this.value)) + this.unit.toString().toLowerCase();
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = super.hashCode();
            return (31 * ((31 * result) + (this.unit == null ? 0 : this.unit.hashCode()))) + this.value;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            DimensionResource other = (DimensionResource) obj;
            if (this.unit != other.unit || this.value != other.value) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ComplexResource.class */
    public static class ComplexResource extends AbstractResource {
        private final String resType;
        private final Map<String, AbstractResource> value;

        public ComplexResource(String resType) {
            this.resType = resType;
            this.value = new HashMap();
        }

        public ComplexResource(String resType, Map<String, AbstractResource> value) {
            this.resType = resType;
            this.value = value;
        }

        public Map<String, AbstractResource> getValue() {
            return this.value;
        }

        public String getResType() {
            return this.resType;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public int hashCode() {
            int result = super.hashCode();
            return (31 * result) + (this.value == null ? 0 : this.value.hashCode());
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.AbstractResource
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            ComplexResource other = (ComplexResource) obj;
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
                return true;
            } else if (!this.value.equals(other.value)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_Header.class */
    public static class ResTable_Header {
        ResChunk_Header header = new ResChunk_Header();
        int packageCount;

        protected ResTable_Header() {
        }

        public int hashCode() {
            int result = (31 * 1) + (this.header == null ? 0 : this.header.hashCode());
            return (31 * result) + this.packageCount;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_Header other = (ResTable_Header) obj;
            if (this.header == null) {
                if (other.header != null) {
                    return false;
                }
            } else if (!this.header.equals(other.header)) {
                return false;
            }
            if (this.packageCount != other.packageCount) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResChunk_Header.class */
    public static class ResChunk_Header {
        int type;
        int headerSize;
        int size;

        protected ResChunk_Header() {
        }

        public int hashCode() {
            int result = (31 * 1) + this.headerSize;
            return (31 * ((31 * result) + this.size)) + this.type;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResChunk_Header other = (ResChunk_Header) obj;
            if (this.headerSize != other.headerSize || this.size != other.size || this.type != other.type) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResStringPool_Header.class */
    public static class ResStringPool_Header {
        ResChunk_Header header;
        int stringCount;
        int styleCount;
        boolean flagsSorted;
        boolean flagsUTF8;
        int stringsStart;
        int stylesStart;

        protected ResStringPool_Header() {
        }

        public int hashCode() {
            int result = (31 * 1) + (this.flagsSorted ? 1231 : 1237);
            return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.flagsUTF8 ? 1231 : 1237))) + (this.header == null ? 0 : this.header.hashCode()))) + this.stringCount)) + this.stringsStart)) + this.styleCount)) + this.stylesStart;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResStringPool_Header other = (ResStringPool_Header) obj;
            if (this.flagsSorted != other.flagsSorted || this.flagsUTF8 != other.flagsUTF8) {
                return false;
            }
            if (this.header == null) {
                if (other.header != null) {
                    return false;
                }
            } else if (!this.header.equals(other.header)) {
                return false;
            }
            if (this.stringCount != other.stringCount || this.stringsStart != other.stringsStart || this.styleCount != other.styleCount || this.stylesStart != other.stylesStart) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_Package.class */
    public static class ResTable_Package {
        ResChunk_Header header;
        int id;
        String name;
        int typeStrings;
        int lastPublicType;
        int keyStrings;
        int lastPublicKey;

        protected ResTable_Package() {
        }

        public int hashCode() {
            int result = (31 * 1) + (this.header == null ? 0 : this.header.hashCode());
            return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.id)) + this.keyStrings)) + this.lastPublicKey)) + this.lastPublicType)) + (this.name == null ? 0 : this.name.hashCode()))) + this.typeStrings;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_Package other = (ResTable_Package) obj;
            if (this.header == null) {
                if (other.header != null) {
                    return false;
                }
            } else if (!this.header.equals(other.header)) {
                return false;
            }
            if (this.id != other.id || this.keyStrings != other.keyStrings || this.lastPublicKey != other.lastPublicKey || this.lastPublicType != other.lastPublicType) {
                return false;
            }
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!this.name.equals(other.name)) {
                return false;
            }
            if (this.typeStrings != other.typeStrings) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_TypeSpec.class */
    public static class ResTable_TypeSpec {
        ResChunk_Header header;
        int id;
        int res0;
        int res1;
        int entryCount;

        protected ResTable_TypeSpec() {
        }

        public int hashCode() {
            int result = (31 * 1) + this.entryCount;
            return (31 * ((31 * ((31 * ((31 * result) + (this.header == null ? 0 : this.header.hashCode()))) + this.id)) + this.res0)) + this.res1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_TypeSpec other = (ResTable_TypeSpec) obj;
            if (this.entryCount != other.entryCount) {
                return false;
            }
            if (this.header == null) {
                if (other.header != null) {
                    return false;
                }
            } else if (!this.header.equals(other.header)) {
                return false;
            }
            if (this.id != other.id || this.res0 != other.res0 || this.res1 != other.res1) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_Type.class */
    public static class ResTable_Type {
        ResChunk_Header header;
        int id;
        int res0;
        int res1;
        int entryCount;
        int entriesStart;
        ResTable_Config config = new ResTable_Config();

        protected ResTable_Type() {
        }

        public int hashCode() {
            int result = (31 * 1) + (this.config == null ? 0 : this.config.hashCode());
            return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.entriesStart)) + this.entryCount)) + (this.header == null ? 0 : this.header.hashCode()))) + this.id)) + this.res0)) + this.res1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_Type other = (ResTable_Type) obj;
            if (this.config == null) {
                if (other.config != null) {
                    return false;
                }
            } else if (!this.config.equals(other.config)) {
                return false;
            }
            if (this.entriesStart != other.entriesStart || this.entryCount != other.entryCount) {
                return false;
            }
            if (this.header == null) {
                if (other.header != null) {
                    return false;
                }
            } else if (!this.header.equals(other.header)) {
                return false;
            }
            if (this.id != other.id || this.res0 != other.res0 || this.res1 != other.res1) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_Config.class */
    public static class ResTable_Config {
        int size;
        int mmc;
        int mnc;
        int orientation;
        int touchscreen;
        int density;
        int keyboard;
        int navigation;
        int inputFlags;
        int inputPad0;
        int screenWidth;
        int screenHeight;
        int sdkVersion;
        int minorVersion;
        int screenLayout;
        int uiMode;
        int smallestScreenWidthDp;
        int screenWidthDp;
        int screenHeightDp;
        char[] language = new char[2];
        char[] country = new char[2];
        char[] localeScript = new char[4];
        char[] localeVariant = new char[8];

        public int getMmc() {
            return this.mmc;
        }

        public int getMnc() {
            return this.mnc;
        }

        public String getLanguage() {
            return new String(this.language);
        }

        public String getCountry() {
            return new String(this.country);
        }

        public int getOrientation() {
            return this.orientation;
        }

        public int getTouchscreen() {
            return this.touchscreen;
        }

        public int getDensity() {
            return this.density;
        }

        public int getKeyboard() {
            return this.keyboard;
        }

        public int getNavigation() {
            return this.navigation;
        }

        public int getInputFlags() {
            return this.inputFlags;
        }

        public int getInputPad0() {
            return this.inputPad0;
        }

        public int getScreenWidth() {
            return this.screenWidth;
        }

        public int getScreenHeight() {
            return this.screenHeight;
        }

        public int getSdkVersion() {
            return this.sdkVersion;
        }

        public int getMinorVersion() {
            return this.minorVersion;
        }

        public int getScreenLayout() {
            return this.screenLayout;
        }

        public int getUiMode() {
            return this.uiMode;
        }

        public int getSmallestScreenWidthDp() {
            return this.smallestScreenWidthDp;
        }

        public int getScreenWidthDp() {
            return this.screenWidthDp;
        }

        public int getScreenHeightDp() {
            return this.screenHeightDp;
        }

        public String getLocaleScript() {
            return new String(this.localeScript);
        }

        public String getLocaleVariant() {
            return new String(this.localeVariant);
        }

        public int hashCode() {
            int result = (31 * 1) + Arrays.hashCode(this.country);
            return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.density)) + this.inputFlags)) + this.inputPad0)) + this.keyboard)) + Arrays.hashCode(this.language))) + Arrays.hashCode(this.localeScript))) + Arrays.hashCode(this.localeVariant))) + this.minorVersion)) + this.mmc)) + this.mnc)) + this.navigation)) + this.orientation)) + this.screenHeight)) + this.screenHeightDp)) + this.screenLayout)) + this.screenWidth)) + this.screenWidthDp)) + this.sdkVersion)) + this.size)) + this.smallestScreenWidthDp)) + this.touchscreen)) + this.uiMode;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_Config other = (ResTable_Config) obj;
            if (!Arrays.equals(this.country, other.country) || this.density != other.density || this.inputFlags != other.inputFlags || this.inputPad0 != other.inputPad0 || this.keyboard != other.keyboard || !Arrays.equals(this.language, other.language) || !Arrays.equals(this.localeScript, other.localeScript) || !Arrays.equals(this.localeVariant, other.localeVariant) || this.minorVersion != other.minorVersion || this.mmc != other.mmc || this.mnc != other.mnc || this.navigation != other.navigation || this.orientation != other.orientation || this.screenHeight != other.screenHeight || this.screenHeightDp != other.screenHeightDp || this.screenLayout != other.screenLayout || this.screenWidth != other.screenWidth || this.screenWidthDp != other.screenWidthDp || this.sdkVersion != other.sdkVersion || this.size != other.size || this.smallestScreenWidthDp != other.smallestScreenWidthDp || this.touchscreen != other.touchscreen || this.uiMode != other.uiMode) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_Entry.class */
    public static class ResTable_Entry {
        int size;
        boolean flagsComplex;
        boolean flagsPublic;
        int key;

        protected ResTable_Entry() {
        }

        public int hashCode() {
            int result = (31 * 1) + (this.flagsComplex ? 1231 : 1237);
            return (31 * ((31 * ((31 * result) + (this.flagsPublic ? 1231 : 1237))) + this.key)) + this.size;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_Entry other = (ResTable_Entry) obj;
            if (this.flagsComplex != other.flagsComplex || this.flagsPublic != other.flagsPublic || this.key != other.key || this.size != other.size) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_Map_Entry.class */
    public static class ResTable_Map_Entry extends ResTable_Entry {
        int parent;
        int count;

        protected ResTable_Map_Entry() {
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.ResTable_Entry
        public int hashCode() {
            int result = super.hashCode();
            return (31 * ((31 * result) + this.count)) + this.parent;
        }

        @Override // soot.jimple.infoflow.android.resources.ARSCFileParser.ResTable_Entry
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_Map_Entry other = (ResTable_Map_Entry) obj;
            if (this.count != other.count || this.parent != other.parent) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$Res_Value.class */
    public static class Res_Value {
        int size;
        int res0;
        int dataType;
        int data;

        protected Res_Value() {
        }

        public int hashCode() {
            int result = (31 * 1) + this.data;
            return (31 * ((31 * ((31 * result) + this.dataType)) + this.res0)) + this.size;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Res_Value other = (Res_Value) obj;
            if (this.data != other.data || this.dataType != other.dataType || this.res0 != other.res0 || this.size != other.size) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResTable_Map.class */
    public static class ResTable_Map {
        int name;
        Res_Value value = new Res_Value();

        protected ResTable_Map() {
        }

        public int hashCode() {
            int result = (31 * 1) + this.name;
            return (31 * result) + (this.value == null ? 0 : this.value.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResTable_Map other = (ResTable_Map) obj;
            if (this.name != other.name) {
                return false;
            }
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
                return true;
            } else if (!this.value.equals(other.value)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/ARSCFileParser$ResourceId.class */
    public static class ResourceId {
        private int packageId;
        private int typeId;
        private int itemIndex;

        public ResourceId(int packageId, int typeId, int itemIndex) {
            this.packageId = packageId;
            this.typeId = typeId;
            this.itemIndex = itemIndex;
        }

        public int getPackageId() {
            return this.packageId;
        }

        public int getTypeId() {
            return this.typeId;
        }

        public int getItemIndex() {
            return this.itemIndex;
        }

        public String toString() {
            return "Package " + this.packageId + ", type " + this.typeId + ", item " + this.itemIndex;
        }

        public int hashCode() {
            int result = (31 * 1) + this.itemIndex;
            return (31 * ((31 * result) + this.packageId)) + this.typeId;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ResourceId other = (ResourceId) obj;
            if (this.itemIndex != other.itemIndex || this.packageId != other.packageId || this.typeId != other.typeId) {
                return false;
            }
            return true;
        }
    }

    public void parse(String apkFile) throws IOException {
        handleAndroidResourceFiles(apkFile, null, new IResourceHandler() { // from class: soot.jimple.infoflow.android.resources.ARSCFileParser.1
            @Override // soot.jimple.infoflow.android.resources.IResourceHandler
            public void handleResourceFile(String fileName, Set<String> fileNameFilter, InputStream stream) {
                try {
                    if (fileName.equals("resources.arsc")) {
                        ARSCFileParser.this.parse(stream);
                    }
                } catch (IOException ex) {
                    ARSCFileParser.this.logger.error("Could not read resource file", (Throwable) ex);
                }
            }
        });
    }

    public void parse(InputStream stream) throws IOException {
        readResourceHeader(stream);
    }

    private void readResourceHeader(InputStream stream) throws IOException {
        AbstractResource res;
        ResTable_Header resourceHeader = new ResTable_Header();
        readChunkHeader(stream, resourceHeader.header);
        resourceHeader.packageCount = readUInt32(stream);
        this.logger.debug(String.format("Package Groups (%d)", Integer.valueOf(resourceHeader.packageCount)));
        int remainingSize = resourceHeader.header.size - resourceHeader.header.headerSize;
        if (remainingSize <= 0) {
            return;
        }
        byte[] remainingData = new byte[remainingSize];
        int i = 0;
        while (true) {
            int totalBytesRead = i;
            if (totalBytesRead < remainingSize) {
                byte[] block = new byte[Math.min(2048, remainingSize - totalBytesRead)];
                int bytesRead = stream.read(block);
                if (bytesRead < 0) {
                    this.logger.error("Could not read block from resource file");
                    return;
                } else {
                    System.arraycopy(block, 0, remainingData, totalBytesRead, bytesRead);
                    i = totalBytesRead + bytesRead;
                }
            } else {
                int offset = 0;
                int packageCtr = 0;
                Map<Integer, String> keyStrings = new HashMap<>();
                Map<Integer, String> typeStrings = new HashMap<>();
                while (offset < remainingData.length - 1) {
                    int beforeBlock = offset;
                    ResChunk_Header nextChunkHeader = new ResChunk_Header();
                    int offset2 = readChunkHeader(nextChunkHeader, remainingData, offset);
                    if (nextChunkHeader.type == 1) {
                        ResStringPool_Header stringPoolHeader = new ResStringPool_Header();
                        stringPoolHeader.header = nextChunkHeader;
                        readStringTable(remainingData, parseStringPoolHeader(stringPoolHeader, remainingData, offset2), beforeBlock, stringPoolHeader, this.stringTable);
                        if (!$assertionsDisabled && this.stringTable.size() != stringPoolHeader.stringCount) {
                            throw new AssertionError();
                        }
                    } else if (nextChunkHeader.type == 512) {
                        ResTable_Package packageTable = new ResTable_Package();
                        packageTable.header = nextChunkHeader;
                        parsePackageTable(packageTable, remainingData, offset2);
                        this.logger.debug(String.format("\tPackage %s id=%d name=%s", Integer.valueOf(packageCtr), Integer.valueOf(packageTable.id), packageTable.name));
                        int endOfRecord = beforeBlock + nextChunkHeader.size;
                        ResPackage resPackage = new ResPackage();
                        this.packages.add(resPackage);
                        resPackage.packageId = packageTable.id;
                        resPackage.packageName = packageTable.name;
                        int typeStringsOffset = beforeBlock + packageTable.typeStrings;
                        ResChunk_Header typePoolHeader = new ResChunk_Header();
                        int typeStringsOffset2 = readChunkHeader(typePoolHeader, remainingData, typeStringsOffset);
                        if (typePoolHeader.type != 1) {
                            throw new RuntimeException("Unexpected block type for package type strings");
                        }
                        ResStringPool_Header typePool = new ResStringPool_Header();
                        typePool.header = typePoolHeader;
                        readStringTable(remainingData, parseStringPoolHeader(typePool, remainingData, typeStringsOffset2), typeStringsOffset, typePool, typeStrings);
                        int keyStringsOffset = beforeBlock + packageTable.keyStrings;
                        ResChunk_Header keyPoolHeader = new ResChunk_Header();
                        int keyStringsOffset2 = readChunkHeader(keyPoolHeader, remainingData, keyStringsOffset);
                        if (keyPoolHeader.type != 1) {
                            throw new RuntimeException("Unexpected block type for package key strings");
                        }
                        ResStringPool_Header keyPool = new ResStringPool_Header();
                        keyPool.header = keyPoolHeader;
                        readStringTable(remainingData, parseStringPoolHeader(keyPool, remainingData, keyStringsOffset2), keyStringsOffset, keyPool, keyStrings);
                        int i2 = keyStringsOffset;
                        int i3 = keyPoolHeader.size;
                        while (true) {
                            int offset3 = i2 + i3;
                            if (offset3 < endOfRecord) {
                                ResChunk_Header innerHeader = new ResChunk_Header();
                                int offset4 = readChunkHeader(innerHeader, remainingData, offset3);
                                if (innerHeader.type == 514) {
                                    ResTable_TypeSpec typeSpecTable = new ResTable_TypeSpec();
                                    typeSpecTable.header = innerHeader;
                                    int offset5 = readTypeSpecTable(typeSpecTable, remainingData, offset4);
                                    if (!$assertionsDisabled && offset5 != offset3 + typeSpecTable.header.headerSize) {
                                        throw new AssertionError();
                                    }
                                    ResType tp = new ResType();
                                    tp.id = typeSpecTable.id;
                                    tp.typeName = typeStrings.get(Integer.valueOf(typeSpecTable.id - 1));
                                    resPackage.types.add(tp);
                                } else if (innerHeader.type == 513) {
                                    ResTable_Type typeTable = new ResTable_Type();
                                    typeTable.header = innerHeader;
                                    int offset6 = readTypeTable(typeTable, remainingData, offset4);
                                    if (!$assertionsDisabled && offset6 != offset3 + typeTable.header.headerSize) {
                                        throw new AssertionError();
                                    }
                                    ResType resType = null;
                                    Iterator it = resPackage.types.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        ResType rt = (ResType) it.next();
                                        if (rt.id == typeTable.id) {
                                            resType = rt;
                                            break;
                                        }
                                    }
                                    if (resType == null) {
                                        throw new RuntimeException("Reference to undeclared type found");
                                    }
                                    ResConfig config = new ResConfig();
                                    config.config = typeTable.config;
                                    resType.configurations.add(config);
                                    int resourceIdx = 0;
                                    for (int i4 = 0; i4 < typeTable.entryCount; i4++) {
                                        int entryOffset = readUInt32(remainingData, offset6);
                                        offset6 += 4;
                                        if (entryOffset == -1) {
                                            resourceIdx++;
                                        } else {
                                            int entryOffset2 = entryOffset + offset3 + typeTable.entriesStart;
                                            ResTable_Entry entry = readEntryTable(remainingData, entryOffset2);
                                            int entryOffset3 = entryOffset2 + entry.size;
                                            if (entry.flagsComplex) {
                                                ComplexResource cmpRes = new ComplexResource(resType.typeName);
                                                res = cmpRes;
                                                for (int j = 0; j < ((ResTable_Map_Entry) entry).count; j++) {
                                                    ResTable_Map map = new ResTable_Map();
                                                    entryOffset3 = readComplexValue(map, remainingData, entryOffset3);
                                                    String mapName = new StringBuilder(String.valueOf(map.name)).toString();
                                                    AbstractResource value = parseValue(map.value);
                                                    if (resType.typeName == null || !resType.typeName.equals("array") || !(value instanceof StringResource)) {
                                                        cmpRes.value.put(mapName, value);
                                                    } else {
                                                        ArrayResource existingResource = (AbstractResource) cmpRes.value.get(mapName);
                                                        if (existingResource == null) {
                                                            existingResource = new ArrayResource();
                                                            cmpRes.value.put(mapName, existingResource);
                                                        }
                                                        if (existingResource instanceof ArrayResource) {
                                                            ((ArrayResource) existingResource).add(value);
                                                        }
                                                    }
                                                }
                                            } else {
                                                Res_Value val = new Res_Value();
                                                readValue(val, remainingData, entryOffset3);
                                                res = parseValue(val);
                                                if (res == null) {
                                                    this.logger.error(String.format("Could not parse resource %s of type 0x%x, skipping entry", keyStrings.get(Integer.valueOf(entry.key)), Integer.valueOf(val.dataType)));
                                                }
                                            }
                                            if (keyStrings.containsKey(Integer.valueOf(entry.key))) {
                                                res.resourceName = keyStrings.get(Integer.valueOf(entry.key));
                                            } else {
                                                res.resourceName = "<INVALID RESOURCE>";
                                            }
                                            if (res.resourceID <= 0) {
                                                res.resourceID = (packageTable.id << 24) + (typeTable.id << 16) + resourceIdx;
                                            }
                                            config.resources.add(res);
                                            resourceIdx++;
                                        }
                                    }
                                } else {
                                    continue;
                                }
                                i2 = offset3;
                                i3 = innerHeader.size;
                            } else {
                                if (this.logger.isTraceEnabled()) {
                                    for (ResType resType2 : resPackage.types) {
                                        Logger logger = this.logger;
                                        Object[] objArr = new Object[4];
                                        objArr[0] = resType2.typeName;
                                        objArr[1] = Integer.valueOf(resType2.id - 1);
                                        objArr[2] = Integer.valueOf(resType2.configurations.size());
                                        objArr[3] = Integer.valueOf(resType2.configurations.size() > 0 ? ((ResConfig) resType2.configurations.get(0)).resources.size() : 0);
                                        logger.trace("\t\tType {} ({}), configCount={}, entryCount={}", objArr);
                                        for (ResConfig resConfig : resType2.configurations) {
                                            this.logger.trace("\t\t\tconfig");
                                            for (AbstractResource res2 : resConfig.resources) {
                                                this.logger.trace("\t\t\t\tresource {}: {}", Integer.toHexString(res2.resourceID), res2.resourceName);
                                            }
                                        }
                                    }
                                }
                                packageCtr++;
                            }
                        }
                    } else {
                        continue;
                    }
                    offset = beforeBlock + nextChunkHeader.size;
                    remainingSize -= nextChunkHeader.size;
                }
                return;
            }
        }
    }

    protected boolean isAttribute(ResTable_Map map) {
        return map.name == 16777216 || map.name == ATTR_MIN || map.name == ATTR_MAX || map.name == ATTR_L10N || map.name == ATTR_OTHER || map.name == ATTR_ZERO || map.name == ATTR_ONE || map.name == ATTR_TWO || map.name == ATTR_FEW || map.name == ATTR_MANY;
    }

    protected static float complexToFloat(int complex) {
        return (complex & (-256)) * RADIX_MULTS[(complex >> 4) & 3];
    }

    private AbstractResource parseValue(Res_Value val) {
        AbstractResource res;
        switch (val.dataType) {
            case 0:
                res = new NullResource();
                break;
            case 1:
                res = new ReferenceResource(val.data);
                break;
            case 2:
                res = new AttributeResource(val.data);
                break;
            case 3:
                res = new StringResource(this.stringTable.get(Integer.valueOf(val.data)));
                break;
            case 4:
                res = new FloatResource(Float.intBitsToFloat(val.data));
                break;
            case 5:
                res = new DimensionResource(val.data & 15, val.data >> 0);
                break;
            case 6:
                int fracType = (val.data >> 0) & 15;
                float data = complexToFloat(val.data);
                if (fracType == 0) {
                    res = new FractionResource(FractionType.Fraction, data);
                    break;
                } else {
                    res = new FractionResource(FractionType.FractionParent, data);
                    break;
                }
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            default:
                this.logger.warn(String.format("Unsupported data type: 0x%x", Integer.valueOf(val.dataType)));
                return null;
            case 16:
            case 17:
                res = new IntegerResource(val.data);
                break;
            case 18:
                res = new BooleanResource(val.data);
                break;
            case 28:
            case 29:
            case 30:
            case 31:
                res = new ColorResource(val.data & (-1), val.data & 255, val.data & 255, val.data & 255);
                break;
        }
        return res;
    }

    private int readComplexValue(ResTable_Map map, byte[] remainingData, int offset) throws IOException {
        map.name = readUInt32(remainingData, offset);
        return readValue(map.value, remainingData, offset + 4);
    }

    private int readValue(Res_Value val, byte[] remainingData, int offset) throws IOException {
        val.size = readUInt16(remainingData, offset);
        int offset2 = offset + 2;
        if (val.size > 8) {
            return 0;
        }
        val.res0 = readUInt8(remainingData, offset2);
        if (val.res0 != 0) {
            throw new RuntimeException("File format error, res0 was not zero");
        }
        int offset3 = offset2 + 1;
        val.dataType = readUInt8(remainingData, offset3);
        int offset4 = offset3 + 1;
        val.data = readUInt32(remainingData, offset4);
        int offset5 = offset4 + 4;
        if ($assertionsDisabled || offset5 == offset + val.size) {
            return offset5;
        }
        throw new AssertionError();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ResTable_Entry readEntryTable(byte[] data, int offset) throws IOException {
        ResTable_Entry entry;
        int size = readUInt16(data, offset);
        int offset2 = offset + 2;
        if (size == 8) {
            entry = new ResTable_Entry();
        } else if (size == 16) {
            entry = new ResTable_Map_Entry();
        } else {
            throw new RuntimeException("Unknown entry type");
        }
        entry.size = size;
        int flags = readUInt16(data, offset2);
        int offset3 = offset2 + 2;
        entry.flagsComplex = (flags & 1) == 1;
        entry.flagsPublic = (flags & 2) == 2;
        entry.key = readUInt32(data, offset3);
        int offset4 = offset3 + 4;
        if (entry instanceof ResTable_Map_Entry) {
            ResTable_Map_Entry mapEntry = (ResTable_Map_Entry) entry;
            mapEntry.parent = readUInt32(data, offset4);
            int offset5 = offset4 + 4;
            mapEntry.count = readUInt32(data, offset5);
            int i = offset5 + 4;
        }
        return entry;
    }

    private int readTypeTable(ResTable_Type typeTable, byte[] data, int offset) throws IOException {
        typeTable.id = readUInt8(data, offset);
        int offset2 = offset + 1;
        typeTable.res0 = readUInt8(data, offset2);
        if (typeTable.res0 != 0) {
            throw new RuntimeException("File format error, res0 was not zero");
        }
        int offset3 = offset2 + 1;
        typeTable.res1 = readUInt16(data, offset3);
        if (typeTable.res1 != 0) {
            throw new RuntimeException("File format error, res1 was not zero");
        }
        int offset4 = offset3 + 2;
        typeTable.entryCount = readUInt32(data, offset4);
        int offset5 = offset4 + 4;
        typeTable.entriesStart = readUInt32(data, offset5);
        return readConfigTable(typeTable.config, data, offset5 + 4);
    }

    private int readConfigTable(ResTable_Config config, byte[] data, int offset) throws IOException {
        config.size = readUInt32(data, offset);
        int offset2 = offset + 4;
        config.mmc = readUInt16(data, offset2);
        int offset3 = offset2 + 2;
        config.mnc = readUInt16(data, offset3);
        int offset4 = offset3 + 2;
        config.language[0] = (char) data[offset4];
        config.language[1] = (char) data[offset4 + 1];
        int offset5 = offset4 + 2;
        config.country[0] = (char) data[offset5];
        config.country[1] = (char) data[offset5 + 1];
        int offset6 = offset5 + 2;
        config.orientation = readUInt8(data, offset6);
        int offset7 = offset6 + 1;
        config.touchscreen = readUInt8(data, offset7);
        int offset8 = offset7 + 1;
        config.density = readUInt16(data, offset8);
        int offset9 = offset8 + 2;
        config.keyboard = readUInt8(data, offset9);
        int offset10 = offset9 + 1;
        config.navigation = readUInt8(data, offset10);
        int offset11 = offset10 + 1;
        config.inputFlags = readUInt8(data, offset11);
        int offset12 = offset11 + 1;
        config.inputPad0 = readUInt8(data, offset12);
        int offset13 = offset12 + 1;
        config.screenWidth = readUInt16(data, offset13);
        int offset14 = offset13 + 2;
        config.screenHeight = readUInt16(data, offset14);
        int offset15 = offset14 + 2;
        config.sdkVersion = readUInt16(data, offset15);
        int offset16 = offset15 + 2;
        config.minorVersion = readUInt16(data, offset16);
        int offset17 = offset16 + 2;
        if (config.size <= 28) {
            return offset17;
        }
        config.screenLayout = readUInt8(data, offset17);
        int offset18 = offset17 + 1;
        config.uiMode = readUInt8(data, offset18);
        int offset19 = offset18 + 1;
        config.smallestScreenWidthDp = readUInt16(data, offset19);
        int offset20 = offset19 + 2;
        if (config.size <= 32) {
            return offset20;
        }
        config.screenWidthDp = readUInt16(data, offset20);
        int offset21 = offset20 + 2;
        config.screenHeightDp = readUInt16(data, offset21);
        int offset22 = offset21 + 2;
        if (config.size <= 36) {
            return offset22;
        }
        for (int i = 0; i < 4; i++) {
            config.localeScript[i] = (char) data[offset22 + i];
        }
        int offset23 = offset22 + 4;
        if (config.size <= 40) {
            return offset23;
        }
        for (int i2 = 0; i2 < 8; i2++) {
            config.localeVariant[i2] = (char) data[offset23 + i2];
        }
        int offset24 = offset23 + 8;
        if (config.size <= 48) {
            return offset24;
        }
        int remainingSize = config.size - 48;
        if (remainingSize > 0) {
            byte[] remainingBytes = new byte[remainingSize];
            System.arraycopy(data, offset24, remainingBytes, 0, remainingSize);
            BigInteger remainingData = new BigInteger(1, remainingBytes);
            if (!remainingData.equals(BigInteger.ZERO)) {
                this.logger.debug("Excessive {} non-null bytes in ResTable_Config ignored", Integer.valueOf(remainingSize));
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("remaining data: 0x" + remainingData.toString(16));
                }
            }
            offset24 += remainingSize;
        }
        return offset24;
    }

    private int readTypeSpecTable(ResTable_TypeSpec typeSpecTable, byte[] data, int offset) throws IOException {
        typeSpecTable.id = readUInt8(data, offset);
        int offset2 = offset + 1;
        typeSpecTable.res0 = readUInt8(data, offset2);
        int offset3 = offset2 + 1;
        if (typeSpecTable.res0 != 0) {
            throw new RuntimeException("File format violation, res0 was not zero");
        }
        typeSpecTable.res1 = readUInt16(data, offset3);
        int offset4 = offset3 + 2;
        if (typeSpecTable.res1 != 0) {
            throw new RuntimeException("File format violation, res1 was not zero");
        }
        typeSpecTable.entryCount = readUInt32(data, offset4);
        return offset4 + 4;
    }

    private int readStringTable(byte[] remainingData, int offset, int blockStart, ResStringPool_Header stringPoolHeader, Map<Integer, String> stringList) throws IOException {
        String trim;
        for (int i = 0; i < stringPoolHeader.stringCount; i++) {
            offset += 4;
            int stringIdx = readUInt32(remainingData, offset) + stringPoolHeader.stringsStart + blockStart;
            if (stringPoolHeader.flagsUTF8) {
                trim = readStringUTF8(remainingData, stringIdx).trim();
            } else {
                trim = readString(remainingData, stringIdx).trim();
            }
            String str = trim;
            stringList.put(Integer.valueOf(i), str);
        }
        return offset;
    }

    private int parsePackageTable(ResTable_Package packageTable, byte[] data, int offset) throws IOException {
        packageTable.id = readUInt32(data, offset);
        int offset2 = offset + 4;
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            int curChar = readUInt16(data, offset2);
            bld.append((char) curChar);
            offset2 += 2;
        }
        packageTable.name = bld.toString().trim();
        packageTable.typeStrings = readUInt32(data, offset2);
        int offset3 = offset2 + 4;
        packageTable.lastPublicType = readUInt32(data, offset3);
        int offset4 = offset3 + 4;
        packageTable.keyStrings = readUInt32(data, offset4);
        int offset5 = offset4 + 4;
        packageTable.lastPublicKey = readUInt32(data, offset5);
        return offset5 + 4;
    }

    private String readString(byte[] remainingData, int stringIdx) throws IOException {
        int strLen = readUInt16(remainingData, stringIdx);
        if (strLen == 0) {
            return "";
        }
        int stringIdx2 = stringIdx + 2;
        byte[] str = new byte[strLen * 2];
        System.arraycopy(remainingData, stringIdx2, str, 0, strLen * 2);
        return new String(remainingData, stringIdx2, strLen * 2, "UTF-16LE");
    }

    private String readStringUTF8(byte[] remainingData, int stringIdx) throws IOException {
        int strLen = readUInt8(remainingData, stringIdx + 1);
        String str = new String(remainingData, stringIdx + 2, strLen, "UTF-8");
        return str;
    }

    private int parseStringPoolHeader(ResStringPool_Header stringPoolHeader, byte[] data, int offset) throws IOException {
        stringPoolHeader.stringCount = readUInt32(data, offset);
        stringPoolHeader.styleCount = readUInt32(data, offset + 4);
        int flags = readUInt32(data, offset + 8);
        stringPoolHeader.flagsSorted = (flags & 1) == 1;
        stringPoolHeader.flagsUTF8 = (flags & 256) == 256;
        stringPoolHeader.stringsStart = readUInt32(data, offset + 12);
        stringPoolHeader.stylesStart = readUInt32(data, offset + 16);
        return offset + 20;
    }

    private void readChunkHeader(InputStream stream, ResChunk_Header nextChunkHeader) throws IOException {
        byte[] header = new byte[8];
        stream.read(header);
        readChunkHeader(nextChunkHeader, header, 0);
    }

    private int readChunkHeader(ResChunk_Header nextChunkHeader, byte[] data, int offset) throws IOException {
        nextChunkHeader.type = readUInt16(data, offset);
        int offset2 = offset + 2;
        nextChunkHeader.headerSize = readUInt16(data, offset2);
        int offset3 = offset2 + 2;
        nextChunkHeader.size = readUInt32(data, offset3);
        return offset3 + 4;
    }

    private int readUInt8(byte[] uint16, int offset) throws IOException {
        int b0 = uint16[0 + offset] & 255;
        return b0;
    }

    private int readUInt16(byte[] uint16, int offset) throws IOException {
        int b0 = uint16[0 + offset] & 255;
        int b1 = uint16[1 + offset] & 255;
        return (b1 << 8) + b0;
    }

    private int readUInt32(InputStream stream) throws IOException {
        byte[] uint32 = new byte[4];
        stream.read(uint32);
        return readUInt32(uint32, 0);
    }

    private int readUInt32(byte[] uint32, int offset) throws IOException {
        int b0 = uint32[0 + offset] & 255;
        int b1 = uint32[1 + offset] & 255;
        int b2 = uint32[2 + offset] & 255;
        int b3 = uint32[3 + offset] & 255;
        return (Math.abs(b3) << 24) + (Math.abs(b2) << 16) + (Math.abs(b1) << 8) + Math.abs(b0);
    }

    public Map<Integer, String> getGlobalStringPool() {
        return this.stringTable;
    }

    public List<ResPackage> getPackages() {
        return this.packages;
    }

    public ResPackage getPackage(int pkgID, String pkgName) {
        return this.packages.stream().filter(p -> {
            return p.packageId == pkgID && p.packageName.equals(pkgName);
        }).findFirst().orElse(null);
    }

    public AbstractResource findResource(int resourceId) {
        ResourceId id = parseResourceId(resourceId);
        for (ResPackage resPackage : this.packages) {
            if (resPackage.packageId == id.packageId) {
                for (ResType resType : resPackage.types) {
                    if (resType.id == id.typeId) {
                        return resType.getFirstResource(resourceId);
                    }
                }
                return null;
            }
        }
        return null;
    }

    public List<AbstractResource> findAllResources(int resourceId) {
        List<AbstractResource> resourceList = new ArrayList<>();
        ResourceId id = parseResourceId(resourceId);
        Iterator<ResPackage> it = this.packages.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ResPackage resPackage = it.next();
            if (resPackage.packageId == id.packageId) {
                for (ResType resType : resPackage.types) {
                    if (resType.id == id.typeId) {
                        resourceList.addAll(resType.getAllResources(resourceId));
                    }
                }
            }
        }
        return resourceList;
    }

    public ResType findResourceType(int resourceId) {
        ResourceId id = parseResourceId(resourceId);
        for (ResPackage resPackage : this.packages) {
            if (resPackage.packageId == id.packageId) {
                for (ResType resType : resPackage.types) {
                    if (resType.id == id.typeId) {
                        return resType;
                    }
                }
                return null;
            }
        }
        return null;
    }

    public ResourceId parseResourceId(int resourceId) {
        return new ResourceId((resourceId & (-16777216)) >> 24, (resourceId & Spanned.SPAN_PRIORITY) >> 16, resourceId & 65535);
    }

    public AbstractResource findResourceByName(String type, String resourceName) {
        for (ResPackage resPackage : this.packages) {
            ResType resType = resPackage.getResourceType(type);
            if (resType != null) {
                for (AbstractResource res : resType.getAllResources()) {
                    if (res.resourceName.equals(resourceName)) {
                        return res;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public String findStringResource(String resourceName) {
        AbstractResource res = findResourceByName("string", resourceName);
        if (res instanceof StringResource) {
            StringResource stringRes = (StringResource) res;
            return stringRes.value;
        }
        return null;
    }

    public List<AbstractResource> findResourcesByType(String type) {
        List<AbstractResource> resourceList = new ArrayList<>();
        for (ResPackage resPackage : this.packages) {
            ResType resType = resPackage.getResourceType(type);
            if (resType != null) {
                resourceList.addAll(resType.getAllResources());
            }
        }
        return resourceList;
    }

    public static ARSCFileParser getInstance(File apkFile) throws IOException {
        ARSCFileParser parser = new ARSCFileParser();
        Throwable th = null;
        try {
            ApkHandler handler = new ApkHandler(apkFile);
            InputStream is = handler.getInputStream("resources.arsc");
            if (is == null) {
                if (handler != null) {
                    handler.close();
                    return null;
                }
                return null;
            }
            try {
                parser.parse(is);
                if (is != null) {
                    is.close();
                }
                if (handler != null) {
                    handler.close();
                }
                return parser;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void addAll(ARSCFileParser otherParser) {
        for (ResPackage pkg : otherParser.packages) {
            ResPackage existingPackage = getPackage(pkg.packageId, pkg.packageName);
            if (existingPackage != null) {
                existingPackage.addAll(pkg);
            } else {
                this.packages.add(pkg);
            }
        }
        this.stringTable.putAll(otherParser.stringTable);
    }
}
