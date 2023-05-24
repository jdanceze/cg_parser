package org.apache.xmlcommons;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/apache/xmlcommons/Version.class */
public class Version {
    public static String getVersion() {
        return new StringBuffer().append(getProduct()).append(Instruction.argsep).append(getVersionNum()).toString();
    }

    public static String getProduct() {
        return "XmlCommonsExternal";
    }

    public static String getVersionNum() {
        return "1.2.01";
    }

    public static void main(String[] strArr) {
        System.out.println(getVersion());
    }
}
