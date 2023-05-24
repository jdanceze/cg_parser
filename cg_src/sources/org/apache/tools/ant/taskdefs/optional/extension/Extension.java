package org.apache.tools.ant.taskdefs.optional.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Stream;
import org.apache.commons.cli.HelpFormatter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/Extension.class */
public final class Extension {
    public static final Attributes.Name EXTENSION_LIST = new Attributes.Name("Extension-List");
    public static final Attributes.Name OPTIONAL_EXTENSION_LIST = new Attributes.Name("Optional-Extension-List");
    public static final Attributes.Name EXTENSION_NAME = new Attributes.Name("Extension-Name");
    public static final Attributes.Name SPECIFICATION_VERSION = Attributes.Name.SPECIFICATION_VERSION;
    public static final Attributes.Name SPECIFICATION_VENDOR = Attributes.Name.SPECIFICATION_VENDOR;
    public static final Attributes.Name IMPLEMENTATION_VERSION = Attributes.Name.IMPLEMENTATION_VERSION;
    public static final Attributes.Name IMPLEMENTATION_VENDOR = Attributes.Name.IMPLEMENTATION_VENDOR;
    public static final Attributes.Name IMPLEMENTATION_URL = new Attributes.Name("Implementation-URL");
    public static final Attributes.Name IMPLEMENTATION_VENDOR_ID = new Attributes.Name("Implementation-Vendor-Id");
    public static final Compatibility COMPATIBLE = new Compatibility("COMPATIBLE");
    public static final Compatibility REQUIRE_SPECIFICATION_UPGRADE = new Compatibility("REQUIRE_SPECIFICATION_UPGRADE");
    public static final Compatibility REQUIRE_VENDOR_SWITCH = new Compatibility("REQUIRE_VENDOR_SWITCH");
    public static final Compatibility REQUIRE_IMPLEMENTATION_UPGRADE = new Compatibility("REQUIRE_IMPLEMENTATION_UPGRADE");
    public static final Compatibility INCOMPATIBLE = new Compatibility("INCOMPATIBLE");
    private String extensionName;
    private org.apache.tools.ant.util.DeweyDecimal specificationVersion;
    private String specificationVendor;
    private String implementationVendorID;
    private String implementationVendor;
    private org.apache.tools.ant.util.DeweyDecimal implementationVersion;
    private String implementationURL;

    public static Extension[] getAvailable(Manifest manifest) {
        if (null == manifest) {
            return new Extension[0];
        }
        return (Extension[]) Stream.concat((Stream) Optional.ofNullable(manifest.getMainAttributes()).map((v0) -> {
            return Stream.of(v0);
        }).orElse(Stream.empty()), manifest.getEntries().values().stream()).map(attrs -> {
            return getExtension("", attrs);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).toArray(x$0 -> {
            return new Extension[x$0];
        });
    }

    public static Extension[] getRequired(Manifest manifest) {
        return getListed(manifest, Attributes.Name.EXTENSION_LIST);
    }

    public static Extension[] getOptions(Manifest manifest) {
        return getListed(manifest, OPTIONAL_EXTENSION_LIST);
    }

    public static void addExtension(Extension extension, Attributes attributes) {
        addExtension(extension, "", attributes);
    }

    public static void addExtension(Extension extension, String prefix, Attributes attributes) {
        attributes.putValue(prefix + EXTENSION_NAME, extension.getExtensionName());
        String specificationVendor = extension.getSpecificationVendor();
        if (null != specificationVendor) {
            attributes.putValue(prefix + SPECIFICATION_VENDOR, specificationVendor);
        }
        org.apache.tools.ant.util.DeweyDecimal specificationVersion = extension.getSpecificationVersion();
        if (null != specificationVersion) {
            attributes.putValue(prefix + SPECIFICATION_VERSION, specificationVersion.toString());
        }
        String implementationVendorID = extension.getImplementationVendorID();
        if (null != implementationVendorID) {
            attributes.putValue(prefix + IMPLEMENTATION_VENDOR_ID, implementationVendorID);
        }
        String implementationVendor = extension.getImplementationVendor();
        if (null != implementationVendor) {
            attributes.putValue(prefix + IMPLEMENTATION_VENDOR, implementationVendor);
        }
        org.apache.tools.ant.util.DeweyDecimal implementationVersion = extension.getImplementationVersion();
        if (null != implementationVersion) {
            attributes.putValue(prefix + IMPLEMENTATION_VERSION, implementationVersion.toString());
        }
        String implementationURL = extension.getImplementationURL();
        if (null != implementationURL) {
            attributes.putValue(prefix + IMPLEMENTATION_URL, implementationURL);
        }
    }

    public Extension(String extensionName, String specificationVersion, String specificationVendor, String implementationVersion, String implementationVendor, String implementationVendorId, String implementationURL) {
        this.extensionName = extensionName;
        this.specificationVendor = specificationVendor;
        if (null != specificationVersion) {
            try {
                this.specificationVersion = new org.apache.tools.ant.util.DeweyDecimal(specificationVersion);
            } catch (NumberFormatException nfe) {
                String error = "Bad specification version format '" + specificationVersion + "' in '" + extensionName + "'. (Reason: " + nfe + ")";
                throw new IllegalArgumentException(error);
            }
        }
        this.implementationURL = implementationURL;
        this.implementationVendor = implementationVendor;
        this.implementationVendorID = implementationVendorId;
        if (null != implementationVersion) {
            try {
                this.implementationVersion = new org.apache.tools.ant.util.DeweyDecimal(implementationVersion);
            } catch (NumberFormatException nfe2) {
                String error2 = "Bad implementation version format '" + implementationVersion + "' in '" + extensionName + "'. (Reason: " + nfe2 + ")";
                throw new IllegalArgumentException(error2);
            }
        }
        if (null == this.extensionName) {
            throw new NullPointerException("extensionName property is null");
        }
    }

    public String getExtensionName() {
        return this.extensionName;
    }

    public String getSpecificationVendor() {
        return this.specificationVendor;
    }

    public org.apache.tools.ant.util.DeweyDecimal getSpecificationVersion() {
        return this.specificationVersion;
    }

    public String getImplementationURL() {
        return this.implementationURL;
    }

    public String getImplementationVendor() {
        return this.implementationVendor;
    }

    public String getImplementationVendorID() {
        return this.implementationVendorID;
    }

    public org.apache.tools.ant.util.DeweyDecimal getImplementationVersion() {
        return this.implementationVersion;
    }

    public Compatibility getCompatibilityWith(Extension required) {
        if (!this.extensionName.equals(required.getExtensionName())) {
            return INCOMPATIBLE;
        }
        org.apache.tools.ant.util.DeweyDecimal requiredSpecificationVersion = required.getSpecificationVersion();
        if (null != requiredSpecificationVersion && (null == this.specificationVersion || !isCompatible(this.specificationVersion, requiredSpecificationVersion))) {
            return REQUIRE_SPECIFICATION_UPGRADE;
        }
        String requiredImplementationVendorID = required.getImplementationVendorID();
        if (null != requiredImplementationVendorID && (null == this.implementationVendorID || !this.implementationVendorID.equals(requiredImplementationVendorID))) {
            return REQUIRE_VENDOR_SWITCH;
        }
        org.apache.tools.ant.util.DeweyDecimal requiredImplementationVersion = required.getImplementationVersion();
        if (null != requiredImplementationVersion && (null == this.implementationVersion || !isCompatible(this.implementationVersion, requiredImplementationVersion))) {
            return REQUIRE_IMPLEMENTATION_UPGRADE;
        }
        return COMPATIBLE;
    }

    public boolean isCompatibleWith(Extension required) {
        return COMPATIBLE == getCompatibilityWith(required);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("%s: %s%n", EXTENSION_NAME, this.extensionName));
        if (null != this.specificationVersion) {
            sb.append(String.format("%s: %s%n", SPECIFICATION_VERSION, this.specificationVersion));
        }
        if (null != this.specificationVendor) {
            sb.append(String.format("%s: %s%n", SPECIFICATION_VENDOR, this.specificationVendor));
        }
        if (null != this.implementationVersion) {
            sb.append(String.format("%s: %s%n", IMPLEMENTATION_VERSION, this.implementationVersion));
        }
        if (null != this.implementationVendorID) {
            sb.append(String.format("%s: %s%n", IMPLEMENTATION_VENDOR_ID, this.implementationVendorID));
        }
        if (null != this.implementationVendor) {
            sb.append(String.format("%s: %s%n", IMPLEMENTATION_VENDOR, this.implementationVendor));
        }
        if (null != this.implementationURL) {
            sb.append(String.format("%s: %s%n", IMPLEMENTATION_URL, this.implementationURL));
        }
        return sb.toString();
    }

    private boolean isCompatible(org.apache.tools.ant.util.DeweyDecimal first, org.apache.tools.ant.util.DeweyDecimal second) {
        return first.isGreaterThanOrEqual(second);
    }

    private static Extension[] getListed(Manifest manifest, Attributes.Name listKey) {
        List<Extension> results = new ArrayList<>();
        Attributes mainAttributes = manifest.getMainAttributes();
        if (null != mainAttributes) {
            getExtension(mainAttributes, results, listKey);
        }
        manifest.getEntries().values().forEach(attributes -> {
            getExtension(attributes, results, listKey);
        });
        return (Extension[]) results.toArray(new Extension[results.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void getExtension(Attributes attributes, List<Extension> required, Attributes.Name listKey) {
        String[] split;
        String names = attributes.getValue(listKey);
        if (null == names) {
            return;
        }
        for (String prefix : split(names, Instruction.argsep)) {
            Extension extension = getExtension(prefix + HelpFormatter.DEFAULT_OPT_PREFIX, attributes);
            if (null != extension) {
                required.add(extension);
            }
        }
    }

    private static String[] split(String string, String onToken) {
        StringTokenizer tokenizer = new StringTokenizer(string, onToken);
        String[] result = new String[tokenizer.countTokens()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tokenizer.nextToken();
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Extension getExtension(String prefix, Attributes attributes) {
        String nameKey = prefix + EXTENSION_NAME;
        String name = getTrimmedString(attributes.getValue(nameKey));
        if (null == name) {
            return null;
        }
        String specVendorKey = prefix + SPECIFICATION_VENDOR;
        String specVendor = getTrimmedString(attributes.getValue(specVendorKey));
        String specVersionKey = prefix + SPECIFICATION_VERSION;
        String specVersion = getTrimmedString(attributes.getValue(specVersionKey));
        String impVersionKey = prefix + IMPLEMENTATION_VERSION;
        String impVersion = getTrimmedString(attributes.getValue(impVersionKey));
        String impVendorKey = prefix + IMPLEMENTATION_VENDOR;
        String impVendor = getTrimmedString(attributes.getValue(impVendorKey));
        String impVendorIDKey = prefix + IMPLEMENTATION_VENDOR_ID;
        String impVendorId = getTrimmedString(attributes.getValue(impVendorIDKey));
        String impURLKey = prefix + IMPLEMENTATION_URL;
        String impURL = getTrimmedString(attributes.getValue(impURLKey));
        return new Extension(name, specVersion, specVendor, impVersion, impVendor, impVendorId, impURL);
    }

    private static String getTrimmedString(String value) {
        if (null == value) {
            return null;
        }
        return value.trim();
    }
}
