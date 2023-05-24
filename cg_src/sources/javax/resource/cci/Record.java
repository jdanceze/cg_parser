package javax.resource.cci;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/Record.class */
public interface Record extends Cloneable, Serializable {
    String getRecordName();

    void setRecordName(String str);

    void setRecordShortDescription(String str);

    String getRecordShortDescription();

    boolean equals(Object obj);

    int hashCode();

    Object clone() throws CloneNotSupportedException;
}
