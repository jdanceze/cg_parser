package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/NameType.class */
public class NameType {
    public static final NameType NO_NAME = new NameType();
    public static final NameType PACKAGE_NAME = new NameType() { // from class: soot.JastAddJ.NameType.1
        @Override // soot.JastAddJ.NameType
        public Access reclassify(String name, int start, int end) {
            return new PackageAccess(name, start, end);
        }
    };
    public static final NameType TYPE_NAME = new NameType() { // from class: soot.JastAddJ.NameType.2
        @Override // soot.JastAddJ.NameType
        public Access reclassify(String name, int start, int end) {
            return new TypeAccess(name, start, end);
        }
    };
    public static final NameType PACKAGE_OR_TYPE_NAME = new NameType() { // from class: soot.JastAddJ.NameType.3
        @Override // soot.JastAddJ.NameType
        public Access reclassify(String name, int start, int end) {
            return new PackageOrTypeAccess(name, start, end);
        }
    };
    public static final NameType AMBIGUOUS_NAME = new NameType() { // from class: soot.JastAddJ.NameType.4
        @Override // soot.JastAddJ.NameType
        public Access reclassify(String name, int start, int end) {
            return new AmbiguousAccess(name, start, end);
        }
    };
    public static final NameType METHOD_NAME = new NameType();
    public static final NameType ARRAY_TYPE_NAME = new NameType();
    public static final NameType ARRAY_READ_NAME = new NameType();
    public static final NameType EXPRESSION_NAME = new NameType() { // from class: soot.JastAddJ.NameType.5
        @Override // soot.JastAddJ.NameType
        public Access reclassify(String name, int start, int end) {
            return new VarAccess(name, start, end);
        }
    };

    /* synthetic */ NameType(NameType nameType) {
        this();
    }

    private NameType() {
    }

    public Access reclassify(String name, int start, int end) {
        throw new Error("Can not reclassify ParseName node " + name);
    }
}
