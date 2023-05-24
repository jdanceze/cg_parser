package soot.jimple.infoflow.rifl;

import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument.class */
public class RIFLDocument {
    private InterfaceSpec interfaceSpec = new InterfaceSpec();
    private List<DomainSpec> domains = new ArrayList();
    private List<DomainAssignment> domainAssignment = new ArrayList();
    private List<FlowPair> flowPolicy = new ArrayList();

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$SourceSinkType.class */
    public enum SourceSinkType {
        Category,
        Source,
        Sink;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static SourceSinkType[] valuesCustom() {
            SourceSinkType[] valuesCustom = values();
            int length = valuesCustom.length;
            SourceSinkType[] sourceSinkTypeArr = new SourceSinkType[length];
            System.arraycopy(valuesCustom, 0, sourceSinkTypeArr, 0, length);
            return sourceSinkTypeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$Assignable.class */
    public class Assignable {
        private final String handle;
        private final SourceSinkSpec element;

        public Assignable(String handle, SourceSinkSpec element) {
            this.handle = handle;
            this.element = element;
        }

        public String getHandle() {
            return this.handle;
        }

        public SourceSinkSpec getElement() {
            return this.element;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.handle == null ? 0 : this.handle.hashCode());
            return (31 * result) + (this.element == null ? 0 : this.element.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Assignable other = (Assignable) obj;
            if (this.handle == null) {
                if (other.handle != null) {
                    return false;
                }
            } else if (!this.handle.equals(other.handle)) {
                return false;
            }
            if (this.element == null) {
                if (other.element != null) {
                    return false;
                }
                return true;
            } else if (!this.element.equals(other.element)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$InterfaceSpec.class */
    public class InterfaceSpec {
        private final List<Assignable> sourcesSinks = new ArrayList();

        public InterfaceSpec() {
        }

        public List<Assignable> getSourcesSinks() {
            return this.sourcesSinks;
        }

        public Assignable getElementByHandle(String handle) {
            for (Assignable assign : this.sourcesSinks) {
                if (assign.getHandle().equals(handle)) {
                    return assign;
                }
            }
            return null;
        }

        public int hashCode() {
            return 31 * this.sourcesSinks.hashCode();
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || !(other instanceof InterfaceSpec)) {
                return false;
            }
            InterfaceSpec otherIO = (InterfaceSpec) other;
            return this.sourcesSinks.equals(otherIO.sourcesSinks);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$SourceSinkSpec.class */
    public abstract class SourceSinkSpec {
        protected final SourceSinkType type;

        public SourceSinkSpec(SourceSinkType type) {
            this.type = type;
        }

        public SourceSinkType getType() {
            return this.type;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.type == null ? 0 : this.type.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SourceSinkSpec other = (SourceSinkSpec) obj;
            if (this.type != other.type) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$JavaSourceSinkSpec.class */
    public abstract class JavaSourceSinkSpec extends SourceSinkSpec {
        private final String className;

        public JavaSourceSinkSpec(SourceSinkType type, String className) {
            super(type);
            this.className = className;
        }

        public String getClassName() {
            return this.className;
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public int hashCode() {
            int result = super.hashCode();
            return (31 * result) + (this.className == null ? 0 : this.className.hashCode());
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            JavaSourceSinkSpec other = (JavaSourceSinkSpec) obj;
            if (this.className == null) {
                if (other.className != null) {
                    return false;
                }
                return true;
            } else if (!this.className.equals(other.className)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$JavaMethodSourceSinkSpec.class */
    public abstract class JavaMethodSourceSinkSpec extends JavaSourceSinkSpec {
        private final String halfSignature;

        public JavaMethodSourceSinkSpec(SourceSinkType type, String className, String halfSignature) {
            super(type, className);
            this.halfSignature = halfSignature;
        }

        public String getHalfSignature() {
            return this.halfSignature;
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.JavaSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public int hashCode() {
            int result = super.hashCode();
            return (31 * result) + (this.halfSignature == null ? 0 : this.halfSignature.hashCode());
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.JavaSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            JavaMethodSourceSinkSpec other = (JavaMethodSourceSinkSpec) obj;
            if (this.halfSignature == null) {
                if (other.halfSignature != null) {
                    return false;
                }
                return true;
            } else if (!this.halfSignature.equals(other.halfSignature)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$JavaParameterSpec.class */
    public class JavaParameterSpec extends JavaMethodSourceSinkSpec {
        private final int paramIdx;

        public JavaParameterSpec(SourceSinkType type, String className, String halfSignature, int paramIdx) {
            super(type, className, halfSignature);
            this.paramIdx = paramIdx;
        }

        public int getParamIdx() {
            return this.paramIdx;
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.JavaMethodSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.JavaSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public int hashCode() {
            int result = super.hashCode();
            return (31 * result) + this.paramIdx;
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.JavaMethodSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.JavaSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            JavaParameterSpec other = (JavaParameterSpec) obj;
            if (this.paramIdx != other.paramIdx) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$JavaReturnValueSpec.class */
    public class JavaReturnValueSpec extends JavaMethodSourceSinkSpec {
        public JavaReturnValueSpec(SourceSinkType type, String className, String halfSignature) {
            super(type, className, halfSignature);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$JavaFieldSpec.class */
    public class JavaFieldSpec extends JavaSourceSinkSpec {
        private final String fieldName;

        public JavaFieldSpec(SourceSinkType type, String className, String fieldName) {
            super(type, className);
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.JavaSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public int hashCode() {
            return 31 * this.fieldName.hashCode();
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.JavaSourceSinkSpec, soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || !(other instanceof JavaFieldSpec)) {
                return false;
            }
            JavaFieldSpec otherSpec = (JavaFieldSpec) other;
            return this.fieldName.equals(otherSpec.fieldName);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$DomainSpec.class */
    public class DomainSpec {
        private final String name;

        public DomainSpec(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$Category.class */
    public class Category extends SourceSinkSpec {
        private final String name;
        private final List<SourceSinkSpec> elements;

        public Category(String name) {
            super(SourceSinkType.Category);
            this.elements = new ArrayList();
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public List<SourceSinkSpec> getElements() {
            return this.elements;
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public int hashCode() {
            return 31 * this.name.hashCode();
        }

        @Override // soot.jimple.infoflow.rifl.RIFLDocument.SourceSinkSpec
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || !(other instanceof Category)) {
                return false;
            }
            Category otherSpec = (Category) other;
            return this.name.equals(otherSpec.name);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$DomainAssignment.class */
    public class DomainAssignment {
        private final Assignable sourceOrSink;
        private final DomainSpec domain;

        public DomainAssignment(Assignable sourceOrSink, DomainSpec domain) {
            this.sourceOrSink = sourceOrSink;
            this.domain = domain;
        }

        public Assignable getSourceOrSink() {
            return this.sourceOrSink;
        }

        public DomainSpec getDomain() {
            return this.domain;
        }

        public int hashCode() {
            return (31 * this.sourceOrSink.hashCode()) + (31 * this.domain.hashCode());
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || !(other instanceof DomainAssignment)) {
                return false;
            }
            DomainAssignment otherPair = (DomainAssignment) other;
            return this.sourceOrSink.equals(otherPair.sourceOrSink) && this.domain.equals(otherPair.domain);
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/rifl/RIFLDocument$FlowPair.class */
    public class FlowPair {
        private final DomainSpec firstDomain;
        private final DomainSpec secondDomain;

        public FlowPair(DomainSpec firstDomain, DomainSpec secondDomain) {
            this.firstDomain = firstDomain;
            this.secondDomain = secondDomain;
        }

        public DomainSpec getFirstDomain() {
            return this.firstDomain;
        }

        public DomainSpec getSecondDomain() {
            return this.secondDomain;
        }

        public int hashCode() {
            return (31 * this.firstDomain.hashCode()) + (31 * this.secondDomain.hashCode());
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || !(other instanceof FlowPair)) {
                return false;
            }
            FlowPair otherPair = (FlowPair) other;
            return this.firstDomain.equals(otherPair.firstDomain) && this.secondDomain.equals(otherPair.secondDomain);
        }
    }

    public InterfaceSpec getInterfaceSpec() {
        return this.interfaceSpec;
    }

    public List<DomainSpec> getDomains() {
        return this.domains;
    }

    public DomainSpec getDomainByName(String domainName) {
        for (DomainSpec ds : this.domains) {
            if (ds.getName().equals(domainName)) {
                return ds;
            }
        }
        return null;
    }

    public List<DomainAssignment> getDomainAssignment() {
        return this.domainAssignment;
    }

    public List<FlowPair> getFlowPolicy() {
        return this.flowPolicy;
    }

    public static String getRIFLSpecVersion() {
        return "1.0";
    }

    public int hashCode() {
        int result = (31 * 1) + (this.interfaceSpec == null ? 0 : this.interfaceSpec.hashCode());
        return (31 * ((31 * ((31 * result) + (this.domainAssignment == null ? 0 : this.domainAssignment.hashCode()))) + (this.domains == null ? 0 : this.domains.hashCode()))) + (this.flowPolicy == null ? 0 : this.flowPolicy.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof RIFLDocument)) {
            return false;
        }
        RIFLDocument other = (RIFLDocument) obj;
        return this.interfaceSpec.equals(other.interfaceSpec) && this.domainAssignment.equals(other.domainAssignment) && this.domains.equals(other.domains) && this.flowPolicy.equals(other.flowPolicy);
    }
}
