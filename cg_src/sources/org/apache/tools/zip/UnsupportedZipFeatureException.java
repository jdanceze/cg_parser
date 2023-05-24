package org.apache.tools.zip;

import java.io.Serializable;
import java.util.zip.ZipException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/UnsupportedZipFeatureException.class */
public class UnsupportedZipFeatureException extends ZipException {
    private final Feature reason;
    private final transient ZipEntry entry;
    private static final long serialVersionUID = 20161221;

    public UnsupportedZipFeatureException(Feature reason, ZipEntry entry) {
        super("unsupported feature " + reason + " used in entry " + entry.getName());
        this.reason = reason;
        this.entry = entry;
    }

    public Feature getFeature() {
        return this.reason;
    }

    public ZipEntry getEntry() {
        return this.entry;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/UnsupportedZipFeatureException$Feature.class */
    public static class Feature implements Serializable {
        public static final Feature ENCRYPTION = new Feature("encryption");
        public static final Feature METHOD = new Feature("compression method");
        public static final Feature DATA_DESCRIPTOR = new Feature("data descriptor");
        private final String name;

        private Feature(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}
