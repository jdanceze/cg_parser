package soot.jimple.infoflow.sourcesSinks.definitions;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/SourceSinkType.class */
public enum SourceSinkType {
    Undefined,
    Source,
    Sink,
    Neither,
    Both;
    
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$SourceSinkType;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static SourceSinkType[] valuesCustom() {
        SourceSinkType[] valuesCustom = values();
        int length = valuesCustom.length;
        SourceSinkType[] sourceSinkTypeArr = new SourceSinkType[length];
        System.arraycopy(valuesCustom, 0, sourceSinkTypeArr, 0, length);
        return sourceSinkTypeArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$SourceSinkType() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$SourceSinkType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[valuesCustom().length];
        try {
            iArr2[Both.ordinal()] = 5;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[Neither.ordinal()] = 4;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[Sink.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[Source.ordinal()] = 2;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[Undefined.ordinal()] = 1;
        } catch (NoSuchFieldError unused5) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$SourceSinkType = iArr2;
        return iArr2;
    }

    public static SourceSinkType fromFlags(boolean isSink, boolean isSource) {
        if (isSink && isSource) {
            return Both;
        }
        if (!isSink && !isSource) {
            return Neither;
        }
        if (isSource) {
            return Source;
        }
        if (isSink) {
            return Sink;
        }
        return Undefined;
    }

    public boolean isSource() {
        return this == Source || this == Both;
    }

    public boolean isSink() {
        return this == Sink || this == Both;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public SourceSinkType removeType(SourceSinkType toRemove) {
        switch ($SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$SourceSinkType()[ordinal()]) {
            case 1:
            case 4:
                return this;
            case 2:
                return (toRemove == Source || toRemove == Both) ? Neither : this;
            case 3:
                return (toRemove == Sink || toRemove == Both) ? Neither : this;
            case 5:
                switch ($SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$SourceSinkType()[toRemove.ordinal()]) {
                    case 1:
                    case 4:
                        return this;
                    case 2:
                        return Sink;
                    case 3:
                        return Source;
                    case 5:
                        return Neither;
                }
        }
        return this;
    }

    public SourceSinkType addType(SourceSinkType toAdd) {
        switch ($SWITCH_TABLE$soot$jimple$infoflow$sourcesSinks$definitions$SourceSinkType()[ordinal()]) {
            case 1:
            case 4:
                return toAdd;
            case 2:
                return (toAdd == Sink || toAdd == Both) ? Both : this;
            case 3:
                return (toAdd == Source || toAdd == Both) ? Both : this;
            case 5:
                return this;
            default:
                return this;
        }
    }

    public static SourceSinkType fromString(String sourceSinkTypeString) {
        switch (sourceSinkTypeString.hashCode()) {
            case -1843176421:
                if (sourceSinkTypeString.equals("SOURCE")) {
                    return Source;
                }
                break;
            case 2044801:
                if (sourceSinkTypeString.equals("BOTH")) {
                    return Both;
                }
                break;
            case 2402104:
                if (sourceSinkTypeString.equals("NONE")) {
                    return Neither;
                }
                break;
            case 2545299:
                if (sourceSinkTypeString.equals("SINK")) {
                    return Sink;
                }
                break;
        }
        throw new RuntimeException("[SourceSinkType.sourceSinkTypeString]error in target definition: " + sourceSinkTypeString);
    }
}
