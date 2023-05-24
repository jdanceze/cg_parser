package org.apache.tools.ant.taskdefs.optional.extension;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Stream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/Specification.class */
public final class Specification {
    private static final String MISSING = "Missing ";
    public static final Attributes.Name SPECIFICATION_TITLE = Attributes.Name.SPECIFICATION_TITLE;
    public static final Attributes.Name SPECIFICATION_VERSION = Attributes.Name.SPECIFICATION_VERSION;
    public static final Attributes.Name SPECIFICATION_VENDOR = Attributes.Name.SPECIFICATION_VENDOR;
    public static final Attributes.Name IMPLEMENTATION_TITLE = Attributes.Name.IMPLEMENTATION_TITLE;
    public static final Attributes.Name IMPLEMENTATION_VERSION = Attributes.Name.IMPLEMENTATION_VERSION;
    public static final Attributes.Name IMPLEMENTATION_VENDOR = Attributes.Name.IMPLEMENTATION_VENDOR;
    public static final Compatibility COMPATIBLE = new Compatibility("COMPATIBLE");
    public static final Compatibility REQUIRE_SPECIFICATION_UPGRADE = new Compatibility("REQUIRE_SPECIFICATION_UPGRADE");
    public static final Compatibility REQUIRE_VENDOR_SWITCH = new Compatibility("REQUIRE_VENDOR_SWITCH");
    public static final Compatibility REQUIRE_IMPLEMENTATION_CHANGE = new Compatibility("REQUIRE_IMPLEMENTATION_CHANGE");
    public static final Compatibility INCOMPATIBLE = new Compatibility("INCOMPATIBLE");
    private String specificationTitle;
    private org.apache.tools.ant.util.DeweyDecimal specificationVersion;
    private String specificationVendor;
    private String implementationTitle;
    private String implementationVendor;
    private String implementationVersion;
    private String[] sections;

    public static Specification[] getSpecifications(Manifest manifest) throws ParseException {
        if (null == manifest) {
            return new Specification[0];
        }
        List<Specification> results = new ArrayList<>();
        for (Map.Entry<String, Attributes> e : manifest.getEntries().entrySet()) {
            Optional ofNullable = Optional.ofNullable(getSpecification(e.getKey(), e.getValue()));
            Objects.requireNonNull(results);
            ofNullable.ifPresent((v1) -> {
                r1.add(v1);
            });
        }
        return (Specification[]) removeDuplicates(results).toArray(new Specification[removeDuplicates(results).size()]);
    }

    public Specification(String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor) {
        this(specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, null);
    }

    public Specification(String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor, String[] sections) {
        this.specificationTitle = specificationTitle;
        this.specificationVendor = specificationVendor;
        if (null != specificationVersion) {
            try {
                this.specificationVersion = new org.apache.tools.ant.util.DeweyDecimal(specificationVersion);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Bad specification version format '" + specificationVersion + "' in '" + specificationTitle + "'. (Reason: " + nfe + ")");
            }
        }
        this.implementationTitle = implementationTitle;
        this.implementationVendor = implementationVendor;
        this.implementationVersion = implementationVersion;
        if (null == this.specificationTitle) {
            throw new NullPointerException("specificationTitle");
        }
        this.sections = sections == null ? null : (String[]) sections.clone();
    }

    public String getSpecificationTitle() {
        return this.specificationTitle;
    }

    public String getSpecificationVendor() {
        return this.specificationVendor;
    }

    public String getImplementationTitle() {
        return this.implementationTitle;
    }

    public org.apache.tools.ant.util.DeweyDecimal getSpecificationVersion() {
        return this.specificationVersion;
    }

    public String getImplementationVendor() {
        return this.implementationVendor;
    }

    public String getImplementationVersion() {
        return this.implementationVersion;
    }

    public String[] getSections() {
        if (this.sections == null) {
            return null;
        }
        return (String[]) this.sections.clone();
    }

    public Compatibility getCompatibilityWith(Specification other) {
        if (!this.specificationTitle.equals(other.getSpecificationTitle())) {
            return INCOMPATIBLE;
        }
        org.apache.tools.ant.util.DeweyDecimal otherSpecificationVersion = other.getSpecificationVersion();
        if (null != this.specificationVersion && (null == otherSpecificationVersion || !isCompatible(this.specificationVersion, otherSpecificationVersion))) {
            return REQUIRE_SPECIFICATION_UPGRADE;
        }
        if (null != this.implementationVendor && !this.implementationVendor.equals(other.getImplementationVendor())) {
            return REQUIRE_VENDOR_SWITCH;
        }
        if (null != this.implementationVersion && !this.implementationVersion.equals(other.getImplementationVersion())) {
            return REQUIRE_IMPLEMENTATION_CHANGE;
        }
        return COMPATIBLE;
    }

    public boolean isCompatibleWith(Specification other) {
        return COMPATIBLE == getCompatibilityWith(other);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("%s: %s%n", SPECIFICATION_TITLE, this.specificationTitle));
        if (null != this.specificationVersion) {
            sb.append(String.format("%s: %s%n", SPECIFICATION_VERSION, this.specificationVersion));
        }
        if (null != this.specificationVendor) {
            sb.append(String.format("%s: %s%n", SPECIFICATION_VENDOR, this.specificationVendor));
        }
        if (null != this.implementationTitle) {
            sb.append(String.format("%s: %s%n", IMPLEMENTATION_TITLE, this.implementationTitle));
        }
        if (null != this.implementationVersion) {
            sb.append(String.format("%s: %s%n", IMPLEMENTATION_VERSION, this.implementationVersion));
        }
        if (null != this.implementationVendor) {
            sb.append(String.format("%s: %s%n", IMPLEMENTATION_VENDOR, this.implementationVendor));
        }
        return sb.toString();
    }

    private boolean isCompatible(org.apache.tools.ant.util.DeweyDecimal first, org.apache.tools.ant.util.DeweyDecimal second) {
        return first.isGreaterThanOrEqual(second);
    }

    private static List<Specification> removeDuplicates(List<Specification> list) {
        List<Specification> results = new ArrayList<>();
        List<String> sections = new ArrayList<>();
        while (!list.isEmpty()) {
            Specification specification = list.remove(0);
            Iterator<Specification> iterator = list.iterator();
            while (iterator.hasNext()) {
                Specification other = iterator.next();
                if (isEqual(specification, other)) {
                    Optional.ofNullable(other.getSections()).ifPresent(s -> {
                        Collections.addAll(sections, s);
                    });
                    iterator.remove();
                }
            }
            results.add(mergeInSections(specification, sections));
            sections.clear();
        }
        return results;
    }

    private static boolean isEqual(Specification specification, Specification other) {
        return specification.getSpecificationTitle().equals(other.getSpecificationTitle()) && specification.getSpecificationVersion().isEqual(other.getSpecificationVersion()) && specification.getSpecificationVendor().equals(other.getSpecificationVendor()) && specification.getImplementationTitle().equals(other.getImplementationTitle()) && specification.getImplementationVersion().equals(other.getImplementationVersion()) && specification.getImplementationVendor().equals(other.getImplementationVendor());
    }

    private static Specification mergeInSections(Specification specification, List<String> sectionsToAdd) {
        if (sectionsToAdd.isEmpty()) {
            return specification;
        }
        Stream<String> sections = Stream.concat((Stream) Optional.ofNullable(specification.getSections()).map((v0) -> {
            return Stream.of(v0);
        }).orElse(Stream.empty()), sectionsToAdd.stream());
        return new Specification(specification.getSpecificationTitle(), specification.getSpecificationVersion().toString(), specification.getSpecificationVendor(), specification.getImplementationTitle(), specification.getImplementationVersion(), specification.getImplementationVendor(), (String[]) sections.toArray(x$0 -> {
            return new String[x$0];
        }));
    }

    private static String getTrimmedString(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private static Specification getSpecification(String section, Attributes attributes) throws ParseException {
        String name = getTrimmedString(attributes.getValue(SPECIFICATION_TITLE));
        if (null == name) {
            return null;
        }
        String specVendor = getTrimmedString(attributes.getValue(SPECIFICATION_VENDOR));
        if (null == specVendor) {
            throw new ParseException(MISSING + SPECIFICATION_VENDOR, 0);
        }
        String specVersion = getTrimmedString(attributes.getValue(SPECIFICATION_VERSION));
        if (null == specVersion) {
            throw new ParseException(MISSING + SPECIFICATION_VERSION, 0);
        }
        String impTitle = getTrimmedString(attributes.getValue(IMPLEMENTATION_TITLE));
        if (null == impTitle) {
            throw new ParseException(MISSING + IMPLEMENTATION_TITLE, 0);
        }
        String impVersion = getTrimmedString(attributes.getValue(IMPLEMENTATION_VERSION));
        if (null == impVersion) {
            throw new ParseException(MISSING + IMPLEMENTATION_VERSION, 0);
        }
        String impVendor = getTrimmedString(attributes.getValue(IMPLEMENTATION_VENDOR));
        if (null == impVendor) {
            throw new ParseException(MISSING + IMPLEMENTATION_VENDOR, 0);
        }
        return new Specification(name, specVersion, specVendor, impTitle, impVersion, impVendor, new String[]{section});
    }
}
