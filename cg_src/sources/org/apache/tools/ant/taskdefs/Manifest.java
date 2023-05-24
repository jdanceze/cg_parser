package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.StreamUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Manifest.class */
public class Manifest {
    public static final String ATTRIBUTE_SIGNATURE_VERSION = "Signature-Version";
    public static final String ATTRIBUTE_NAME = "Name";
    public static final String ATTRIBUTE_FROM = "From";
    public static final String DEFAULT_MANIFEST_VERSION = "1.0";
    public static final int MAX_LINE_LENGTH = 72;
    public static final int MAX_SECTION_LENGTH = 70;
    public static final String EOL = "\r\n";
    public static final String ERROR_FROM_FORBIDDEN = "Manifest attributes should not start with \"From\" in \"";
    private String manifestVersion;
    private Section mainSection = new Section();
    private Map<String, Section> sections = new LinkedHashMap();
    public static final Charset JAR_CHARSET = StandardCharsets.UTF_8;
    @Deprecated
    public static final String JAR_ENCODING = JAR_CHARSET.name();
    public static final String ATTRIBUTE_MANIFEST_VERSION = "Manifest-Version";
    private static final String ATTRIBUTE_MANIFEST_VERSION_LC = ATTRIBUTE_MANIFEST_VERSION.toLowerCase(Locale.ENGLISH);
    private static final String ATTRIBUTE_NAME_LC = "Name".toLowerCase(Locale.ENGLISH);
    private static final String ATTRIBUTE_FROM_LC = "From".toLowerCase(Locale.ENGLISH);
    public static final String ATTRIBUTE_CLASSPATH = "Class-Path";
    private static final String ATTRIBUTE_CLASSPATH_LC = ATTRIBUTE_CLASSPATH.toLowerCase(Locale.ENGLISH);

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Manifest$Attribute.class */
    public static class Attribute {
        private static final int MAX_NAME_VALUE_LENGTH = 68;
        private static final int MAX_NAME_LENGTH = 70;
        private String name;
        private Vector<String> values;
        private int currentIndex;

        public Attribute() {
            this.name = null;
            this.values = new Vector<>();
            this.currentIndex = 0;
        }

        public Attribute(String line) throws ManifestException {
            this.name = null;
            this.values = new Vector<>();
            this.currentIndex = 0;
            parse(line);
        }

        public Attribute(String name, String value) {
            this.name = null;
            this.values = new Vector<>();
            this.currentIndex = 0;
            this.name = name;
            setValue(value);
        }

        public int hashCode() {
            return Objects.hash(getKey(), this.values);
        }

        public boolean equals(Object rhs) {
            if (rhs == null || rhs.getClass() != getClass()) {
                return false;
            }
            if (rhs == this) {
                return true;
            }
            Attribute rhsAttribute = (Attribute) rhs;
            String lhsKey = getKey();
            String rhsKey = rhsAttribute.getKey();
            return (lhsKey != null || rhsKey == null) && (lhsKey == null || lhsKey.equals(rhsKey)) && this.values.equals(rhsAttribute.values);
        }

        public void parse(String line) throws ManifestException {
            int index = line.indexOf(": ");
            if (index == -1) {
                throw new ManifestException("Manifest line \"" + line + "\" is not valid as it does not contain a name and a value separated by ': '");
            }
            this.name = line.substring(0, index);
            setValue(line.substring(index + 2));
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public String getKey() {
            if (this.name == null) {
                return null;
            }
            return this.name.toLowerCase(Locale.ENGLISH);
        }

        public void setValue(String value) {
            if (this.currentIndex >= this.values.size()) {
                this.values.addElement(value);
                this.currentIndex = this.values.size() - 1;
                return;
            }
            this.values.setElementAt(value, this.currentIndex);
        }

        public String getValue() {
            if (this.values.isEmpty()) {
                return null;
            }
            return String.join(Instruction.argsep, this.values);
        }

        public void addValue(String value) {
            this.currentIndex++;
            setValue(value);
        }

        public Enumeration<String> getValues() {
            return this.values.elements();
        }

        public void addContinuation(String line) {
            setValue(this.values.elementAt(this.currentIndex) + line.substring(1));
        }

        public void write(PrintWriter writer) throws IOException {
            write(writer, false);
        }

        public void write(PrintWriter writer, boolean flatten) throws IOException {
            if (flatten) {
                writeValue(writer, getValue());
                return;
            }
            Iterator<String> it = this.values.iterator();
            while (it.hasNext()) {
                String value = it.next();
                writeValue(writer, value);
            }
        }

        private void writeValue(PrintWriter writer, String value) throws IOException {
            String str;
            String section;
            int nameLength = this.name.getBytes(Manifest.JAR_CHARSET).length;
            if (nameLength > 68) {
                if (nameLength > 70) {
                    throw new IOException("Unable to write manifest line " + this.name + ": " + value);
                }
                writer.print(this.name + ": \r\n");
                str = Instruction.argsep + value;
            } else {
                str = this.name + ": " + value;
            }
            while (true) {
                String line = str;
                if (line.getBytes(Manifest.JAR_CHARSET).length > 70) {
                    int breakIndex = 70;
                    if (70 >= line.length()) {
                        breakIndex = line.length() - 1;
                    }
                    String substring = line.substring(0, breakIndex);
                    while (true) {
                        section = substring;
                        if (section.getBytes(Manifest.JAR_CHARSET).length <= 70 || breakIndex <= 0) {
                            break;
                        }
                        breakIndex--;
                        substring = line.substring(0, breakIndex);
                    }
                    if (breakIndex == 0) {
                        throw new IOException("Unable to write manifest line " + this.name + ": " + value);
                    }
                    writer.print(section + "\r\n");
                    str = Instruction.argsep + line.substring(breakIndex);
                } else {
                    writer.print(line + "\r\n");
                    return;
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Manifest$Section.class */
    public static class Section {
        private List<String> warnings = new Vector();
        private String name = null;
        private Map<String, Attribute> attributes = new LinkedHashMap();

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public String read(BufferedReader reader) throws ManifestException, IOException {
            Attribute attribute = null;
            while (true) {
                String line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    return null;
                }
                if (line.charAt(0) == ' ') {
                    if (attribute == null) {
                        if (this.name == null) {
                            throw new ManifestException("Can't start an attribute with a continuation line " + line);
                        }
                        this.name += line.substring(1);
                    } else {
                        attribute.addContinuation(line);
                    }
                } else {
                    Attribute attribute2 = new Attribute(line);
                    String nameReadAhead = addAttributeAndCheck(attribute2);
                    attribute = getAttribute(attribute2.getKey());
                    if (nameReadAhead != null) {
                        return nameReadAhead;
                    }
                }
            }
        }

        public void merge(Section section) throws ManifestException {
            merge(section, false);
        }

        public void merge(Section section, boolean mergeClassPaths) throws ManifestException {
            Attribute currentCp;
            if ((this.name == null && section.getName() != null) || (this.name != null && section.getName() != null && !this.name.toLowerCase(Locale.ENGLISH).equals(section.getName().toLowerCase(Locale.ENGLISH)))) {
                throw new ManifestException("Unable to merge sections with different names");
            }
            Attribute classpathAttribute = null;
            Iterator it = Collections.list(section.getAttributeKeys()).iterator();
            while (it.hasNext()) {
                String attributeName = (String) it.next();
                Attribute attribute = section.getAttribute(attributeName);
                if (Manifest.ATTRIBUTE_CLASSPATH.equalsIgnoreCase(attributeName)) {
                    if (classpathAttribute == null) {
                        classpathAttribute = new Attribute();
                        classpathAttribute.setName(Manifest.ATTRIBUTE_CLASSPATH);
                    }
                    ArrayList list = Collections.list(attribute.getValues());
                    Attribute attribute2 = classpathAttribute;
                    Objects.requireNonNull(attribute2);
                    list.forEach(this::addValue);
                } else {
                    storeAttribute(attribute);
                }
            }
            if (classpathAttribute != null) {
                if (mergeClassPaths && (currentCp = getAttribute(Manifest.ATTRIBUTE_CLASSPATH)) != null) {
                    ArrayList list2 = Collections.list(currentCp.getValues());
                    Attribute attribute3 = classpathAttribute;
                    Objects.requireNonNull(attribute3);
                    list2.forEach(this::addValue);
                }
                storeAttribute(classpathAttribute);
            }
            this.warnings.addAll(section.warnings);
        }

        public void write(PrintWriter writer) throws IOException {
            write(writer, false);
        }

        public void write(PrintWriter writer, boolean flatten) throws IOException {
            if (this.name != null) {
                Attribute nameAttr = new Attribute("Name", this.name);
                nameAttr.write(writer);
            }
            Iterator it = Collections.list(getAttributeKeys()).iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                getAttribute(key).write(writer, flatten);
            }
            writer.print("\r\n");
        }

        public Attribute getAttribute(String attributeName) {
            return this.attributes.get(attributeName.toLowerCase(Locale.ENGLISH));
        }

        public Enumeration<String> getAttributeKeys() {
            return Collections.enumeration(this.attributes.keySet());
        }

        public String getAttributeValue(String attributeName) {
            Attribute attribute = getAttribute(attributeName.toLowerCase(Locale.ENGLISH));
            if (attribute == null) {
                return null;
            }
            return attribute.getValue();
        }

        public void removeAttribute(String attributeName) {
            String key = attributeName.toLowerCase(Locale.ENGLISH);
            this.attributes.remove(key);
        }

        public void addConfiguredAttribute(Attribute attribute) throws ManifestException {
            String check = addAttributeAndCheck(attribute);
            if (check != null) {
                throw new BuildException("Specify the section name using the \"name\" attribute of the <section> element rather than using a \"Name\" manifest attribute");
            }
        }

        public String addAttributeAndCheck(Attribute attribute) throws ManifestException {
            if (attribute.getName() == null || attribute.getValue() == null) {
                throw new BuildException("Attributes must have name and value");
            }
            String attributeKey = attribute.getKey();
            if (attributeKey.equals(Manifest.ATTRIBUTE_NAME_LC)) {
                this.warnings.add("\"Name\" attributes should not occur in the main section and must be the first element in all other sections: \"" + attribute.getName() + ": " + attribute.getValue() + "\"");
                return attribute.getValue();
            } else if (attributeKey.startsWith(Manifest.ATTRIBUTE_FROM_LC)) {
                this.warnings.add(Manifest.ERROR_FROM_FORBIDDEN + attribute.getName() + ": " + attribute.getValue() + "\"");
                return null;
            } else if (!attributeKey.equals(Manifest.ATTRIBUTE_CLASSPATH_LC)) {
                if (this.attributes.containsKey(attributeKey)) {
                    throw new ManifestException("The attribute \"" + attribute.getName() + "\" may not occur more than once in the same section");
                }
                storeAttribute(attribute);
                return null;
            } else {
                Attribute classpathAttribute = this.attributes.get(attributeKey);
                if (classpathAttribute == null) {
                    storeAttribute(attribute);
                    return null;
                }
                this.warnings.add("Multiple Class-Path attributes are supported but violate the Jar specification and may not be correctly processed in all environments");
                ArrayList list = Collections.list(attribute.getValues());
                Objects.requireNonNull(classpathAttribute);
                list.forEach(this::addValue);
                return null;
            }
        }

        public Object clone() {
            Section cloned = new Section();
            cloned.setName(this.name);
            Stream map = StreamUtils.enumerationAsStream(getAttributeKeys()).map(key -> {
                return new Attribute(getAttribute(key).getName(), getAttribute(key).getValue());
            });
            Objects.requireNonNull(cloned);
            map.forEach(this::storeAttribute);
            return cloned;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void storeAttribute(Attribute attribute) {
            if (attribute == null) {
                return;
            }
            this.attributes.put(attribute.getKey(), attribute);
        }

        public Enumeration<String> getWarnings() {
            return Collections.enumeration(this.warnings);
        }

        public int hashCode() {
            return this.attributes.hashCode();
        }

        public boolean equals(Object rhs) {
            if (rhs == null || rhs.getClass() != getClass()) {
                return false;
            }
            if (rhs == this) {
                return true;
            }
            Section rhsSection = (Section) rhs;
            return this.attributes.equals(rhsSection.attributes);
        }
    }

    public static Manifest getDefaultManifest() throws BuildException {
        try {
            InputStream in = Manifest.class.getResourceAsStream("/org/apache/tools/ant/defaultManifest.mf");
            try {
                if (in == null) {
                    throw new BuildException("Could not find default manifest: %s", "/org/apache/tools/ant/defaultManifest.mf");
                }
                Manifest defaultManifest = new Manifest(new InputStreamReader(in, JAR_CHARSET));
                String version = System.getProperty("java.runtime.version");
                if (version == null) {
                    version = System.getProperty("java.vm.version");
                }
                Attribute createdBy = new Attribute("Created-By", version + " (" + System.getProperty("java.vm.vendor") + ")");
                defaultManifest.getMainSection().storeAttribute(createdBy);
                if (in != null) {
                    in.close();
                }
                return defaultManifest;
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException e) {
            throw new BuildException("Unable to read default manifest", e);
        } catch (ManifestException e2) {
            throw new BuildException("Default manifest is invalid !!", e2);
        }
    }

    public Manifest() {
        this.manifestVersion = "1.0";
        this.manifestVersion = null;
    }

    public Manifest(Reader r) throws ManifestException, IOException {
        this.manifestVersion = "1.0";
        BufferedReader reader = new BufferedReader(r);
        String nextSectionName = this.mainSection.read(reader);
        String readManifestVersion = this.mainSection.getAttributeValue(ATTRIBUTE_MANIFEST_VERSION);
        if (readManifestVersion != null) {
            this.manifestVersion = readManifestVersion;
            this.mainSection.removeAttribute(ATTRIBUTE_MANIFEST_VERSION);
        }
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                if (!line.isEmpty()) {
                    Section section = new Section();
                    if (nextSectionName == null) {
                        Attribute sectionName = new Attribute(line);
                        if (!"Name".equalsIgnoreCase(sectionName.getName())) {
                            throw new ManifestException("Manifest sections should start with a \"Name\" attribute and not \"" + sectionName.getName() + "\"");
                        }
                        nextSectionName = sectionName.getValue();
                    } else {
                        Attribute firstAttribute = new Attribute(line);
                        section.addAttributeAndCheck(firstAttribute);
                    }
                    section.setName(nextSectionName);
                    nextSectionName = section.read(reader);
                    addConfiguredSection(section);
                }
            } else {
                return;
            }
        }
    }

    public void addConfiguredSection(Section section) throws ManifestException {
        String sectionName = section.getName();
        if (sectionName == null) {
            throw new BuildException("Sections must have a name");
        }
        this.sections.put(sectionName, section);
    }

    public void addConfiguredAttribute(Attribute attribute) throws ManifestException {
        if (attribute.getKey() == null || attribute.getValue() == null) {
            throw new BuildException("Attributes must have name and value");
        }
        if (ATTRIBUTE_MANIFEST_VERSION_LC.equals(attribute.getKey())) {
            this.manifestVersion = attribute.getValue();
        } else {
            this.mainSection.addConfiguredAttribute(attribute);
        }
    }

    public void merge(Manifest other) throws ManifestException {
        merge(other, false);
    }

    public void merge(Manifest other, boolean overwriteMain) throws ManifestException {
        merge(other, overwriteMain, false);
    }

    public void merge(Manifest other, boolean overwriteMain, boolean mergeClassPaths) throws ManifestException {
        if (other != null) {
            if (overwriteMain) {
                this.mainSection = (Section) other.mainSection.clone();
            } else {
                this.mainSection.merge(other.mainSection, mergeClassPaths);
            }
            if (other.manifestVersion != null) {
                this.manifestVersion = other.manifestVersion;
            }
            Iterator it = Collections.list(other.getSectionNames()).iterator();
            while (it.hasNext()) {
                String sectionName = (String) it.next();
                Section ourSection = this.sections.get(sectionName);
                Section otherSection = other.sections.get(sectionName);
                if (ourSection == null) {
                    if (otherSection != null) {
                        addConfiguredSection((Section) otherSection.clone());
                    }
                } else {
                    ourSection.merge(otherSection, mergeClassPaths);
                }
            }
        }
    }

    public void write(PrintWriter writer) throws IOException {
        write(writer, false);
    }

    public void write(PrintWriter writer, boolean flatten) throws IOException {
        writer.print("Manifest-Version: " + this.manifestVersion + "\r\n");
        String signatureVersion = this.mainSection.getAttributeValue(ATTRIBUTE_SIGNATURE_VERSION);
        if (signatureVersion != null) {
            writer.print("Signature-Version: " + signatureVersion + "\r\n");
            this.mainSection.removeAttribute(ATTRIBUTE_SIGNATURE_VERSION);
        }
        this.mainSection.write(writer, flatten);
        if (signatureVersion != null) {
            try {
                Attribute svAttr = new Attribute(ATTRIBUTE_SIGNATURE_VERSION, signatureVersion);
                this.mainSection.addConfiguredAttribute(svAttr);
            } catch (ManifestException e) {
            }
        }
        for (String sectionName : this.sections.keySet()) {
            Section section = getSection(sectionName);
            section.write(writer, flatten);
        }
    }

    public String toString() {
        StringWriter sw = new StringWriter();
        try {
            write(new PrintWriter(sw));
            return sw.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public Enumeration<String> getWarnings() {
        List<String> warnings = Collections.list(this.mainSection.getWarnings());
        Stream<R> map = this.sections.values().stream().map(section -> {
            return Collections.list(section.getWarnings());
        });
        Objects.requireNonNull(warnings);
        map.forEach((v1) -> {
            r1.addAll(v1);
        });
        return Collections.enumeration(warnings);
    }

    public int hashCode() {
        int hashCode = 0;
        if (this.manifestVersion != null) {
            hashCode = 0 + this.manifestVersion.hashCode();
        }
        return hashCode + this.mainSection.hashCode() + this.sections.hashCode();
    }

    public boolean equals(Object rhs) {
        if (rhs == null || rhs.getClass() != getClass()) {
            return false;
        }
        if (rhs == this) {
            return true;
        }
        Manifest rhsManifest = (Manifest) rhs;
        if (this.manifestVersion == null) {
            if (rhsManifest.manifestVersion != null) {
                return false;
            }
        } else if (!this.manifestVersion.equals(rhsManifest.manifestVersion)) {
            return false;
        }
        return this.mainSection.equals(rhsManifest.mainSection) && this.sections.equals(rhsManifest.sections);
    }

    public String getManifestVersion() {
        return this.manifestVersion;
    }

    public Section getMainSection() {
        return this.mainSection;
    }

    public Section getSection(String name) {
        return this.sections.get(name);
    }

    public Enumeration<String> getSectionNames() {
        return Collections.enumeration(this.sections.keySet());
    }
}
