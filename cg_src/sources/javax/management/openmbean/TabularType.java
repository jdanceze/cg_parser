package javax.management.openmbean;

import java.io.Serializable;
import java.util.List;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/openmbean/TabularType.class */
public class TabularType extends OpenType implements Serializable {
    static final long serialVersionUID = 6554071860220659261L;
    private CompositeType rowType;
    private List indexNames;
    private transient Integer myHashCode;
    private transient String myToString;
    static Class class$javax$management$openmbean$TabularData;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TabularType(java.lang.String r7, java.lang.String r8, javax.management.openmbean.CompositeType r9, java.lang.String[] r10) throws javax.management.openmbean.OpenDataException {
        /*
            r6 = this;
            r0 = r6
            java.lang.Class r1 = javax.management.openmbean.TabularType.class$javax$management$openmbean$TabularData
            if (r1 != 0) goto L13
            java.lang.String r1 = "javax.management.openmbean.TabularData"
            java.lang.Class r1 = class$(r1)
            r2 = r1
            javax.management.openmbean.TabularType.class$javax$management$openmbean$TabularData = r2
            goto L16
        L13:
            java.lang.Class r1 = javax.management.openmbean.TabularType.class$javax$management$openmbean$TabularData
        L16:
            java.lang.String r1 = r1.getName()
            r2 = r7
            r3 = r8
            r0.<init>(r1, r2, r3)
            r0 = r6
            r1 = 0
            r0.myHashCode = r1
            r0 = r6
            r1 = 0
            r0.myToString = r1
            r0 = r9
            if (r0 != 0) goto L36
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.String r2 = "Argument rowType cannot be null."
            r1.<init>(r2)
            throw r0
        L36:
            r0 = r10
            java.lang.String r1 = "indexNames"
            checkForNullElement(r0, r1)
            r0 = r10
            java.lang.String r1 = "indexNames"
            checkForEmptyString(r0, r1)
            r0 = 0
            r11 = r0
            goto L87
        L4a:
            r0 = r9
            r1 = r10
            r2 = r11
            r1 = r1[r2]
            boolean r0 = r0.containsKey(r1)
            if (r0 != 0) goto L84
            javax.management.openmbean.OpenDataException r0 = new javax.management.openmbean.OpenDataException
            r1 = r0
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Argument's element value indexNames["
            java.lang.StringBuffer r2 = r2.append(r3)
            r3 = r11
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r3 = "]=\""
            java.lang.StringBuffer r2 = r2.append(r3)
            r3 = r10
            r4 = r11
            r3 = r3[r4]
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r3 = "\" is not a valid item name for rowType."
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L84:
            int r11 = r11 + 1
        L87:
            r0 = r11
            r1 = r10
            int r1 = r1.length
            if (r0 < r1) goto L4a
            r0 = r6
            r1 = r9
            r0.rowType = r1
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r2 = r10
            int r2 = r2.length
            r3 = 1
            int r2 = r2 + r3
            r1.<init>(r2)
            r12 = r0
            r0 = 0
            r13 = r0
            goto Lb6
        La8:
            r0 = r12
            r1 = r10
            r2 = r13
            r1 = r1[r2]
            boolean r0 = r0.add(r1)
            int r13 = r13 + 1
        Lb6:
            r0 = r13
            r1 = r10
            int r1 = r1.length
            if (r0 < r1) goto La8
            r0 = r6
            r1 = r12
            java.util.List r1 = java.util.Collections.unmodifiableList(r1)
            r0.indexNames = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.openmbean.TabularType.<init>(java.lang.String, java.lang.String, javax.management.openmbean.CompositeType, java.lang.String[]):void");
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static void checkForNullElement(Object[] objArr, String str) {
        if (objArr == null || objArr.length == 0) {
            throw new IllegalArgumentException(new StringBuffer().append("Argument ").append(str).append("[] cannot be null or empty.").toString());
        }
        for (int i = 0; i < objArr.length; i++) {
            if (objArr[i] == null) {
                throw new IllegalArgumentException(new StringBuffer().append("Argument's element ").append(str).append("[").append(i).append("] cannot be null.").toString());
            }
        }
    }

    private static void checkForEmptyString(String[] strArr, String str) {
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].trim().equals("")) {
                throw new IllegalArgumentException(new StringBuffer().append("Argument's element ").append(str).append("[").append(i).append("] cannot be an empty string.").toString());
            }
        }
    }

    public CompositeType getRowType() {
        return this.rowType;
    }

    public List getIndexNames() {
        return this.indexNames;
    }

    @Override // javax.management.openmbean.OpenType
    public boolean isValue(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return equals(((TabularData) obj).getTabularType());
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.OpenType
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            TabularType tabularType = (TabularType) obj;
            if (!getTypeName().equals(tabularType.getTypeName()) || !this.rowType.equals(tabularType.rowType) || !this.indexNames.equals(tabularType.indexNames)) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override // javax.management.openmbean.OpenType
    public int hashCode() {
        if (this.myHashCode == null) {
            int hashCode = 0 + getTypeName().hashCode() + this.rowType.hashCode();
            for (Object obj : this.indexNames) {
                hashCode += obj.hashCode();
            }
            this.myHashCode = new Integer(hashCode);
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.openmbean.OpenType
    public String toString() {
        if (this.myToString == null) {
            StringBuffer append = new StringBuffer().append(getClass().getName()).append("(name=").append(getTypeName()).append(",rowType=").append(this.rowType.toString()).append(",indexNames=(");
            int i = 0;
            for (Object obj : this.indexNames) {
                if (i > 0) {
                    append.append(",");
                }
                append.append(obj.toString());
                i++;
            }
            append.append("))");
            this.myToString = append.toString();
        }
        return this.myToString;
    }
}
