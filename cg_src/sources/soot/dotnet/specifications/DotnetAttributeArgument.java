package soot.dotnet.specifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dotnet.types.DotnetType;
/* loaded from: gencallgraphv3.jar:soot/dotnet/specifications/DotnetAttributeArgument.class */
public class DotnetAttributeArgument {
    private static final Logger logger = LoggerFactory.getLogger(DotnetType.class);

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x00ce, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_INT16) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x00da, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_INT32) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00e6, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_INT64) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00f2, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_SBYTE) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00fe, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_DECIMAL) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x010a, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_BYTE) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0122, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_DOUBLE) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0146, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_UINT16) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0152, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_UINT32) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x015e, code lost:
        if (r0.equals(soot.dotnet.types.DotnetBasicTypes.SYSTEM_UINT64) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01a7, code lost:
        r0 = r7.getValueInt32List().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01b5, code lost:
        r0 = r0.next().intValue();
        r0.add(new soot.tagkit.AnnotationIntElem(r0, r7.getName()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01db, code lost:
        if (r0.hasNext() != false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x021b, code lost:
        r0 = r7.getValueDoubleList().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0229, code lost:
        r0 = r0.next().doubleValue();
        r0.add(new soot.tagkit.AnnotationDoubleElem(r0, r7.getName()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x024f, code lost:
        if (r0.hasNext() != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0298, code lost:
        r0 = r7.getValueInt64List().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x02a6, code lost:
        r0 = r0.next().longValue();
        r0.add(new soot.tagkit.AnnotationLongElem(r0, r7.getName()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x02cc, code lost:
        if (r0.hasNext() != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x02d2, code lost:
        r0 = r7.getValueInt32List().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02e0, code lost:
        r0 = r0.next().intValue();
        r0.add(new soot.tagkit.AnnotationIntElem(java.lang.Byte.valueOf(java.lang.Integer.valueOf(r0).byteValue()), r7.getName()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x030f, code lost:
        if (r0.hasNext() != false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0353, code lost:
        r0 = r7.getValueInt32List().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0361, code lost:
        r0 = r0.next().intValue();
        r0.add(new soot.tagkit.AnnotationIntElem(java.lang.Short.valueOf((short) r0), r7.getName()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x038b, code lost:
        if (r0.hasNext() != false) goto L15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static soot.tagkit.AnnotationElem toAnnotationElem(soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinition r7) {
        /*
            Method dump skipped, instructions count: 1398
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dotnet.specifications.DotnetAttributeArgument.toAnnotationElem(soot.dotnet.proto.ProtoAssemblyAllTypes$AttributeArgumentDefinition):soot.tagkit.AnnotationElem");
    }
}
