package org.apache.tools.ant.taskdefs.optional;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/EchoProperties.class */
public class EchoProperties extends Task {
    private static final String PROPERTIES = "properties";
    private static final String PROPERTY = "property";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_VALUE = "value";
    private File inFile = null;
    private File destfile = null;
    private boolean failonerror = true;
    private List<PropertySet> propertySets = new Vector();
    private String format = "text";
    private String prefix;
    private String regex;

    public void setSrcfile(File file) {
        this.inFile = file;
    }

    public void setDestfile(File destfile) {
        this.destfile = destfile;
    }

    public void setFailOnError(boolean failonerror) {
        this.failonerror = failonerror;
    }

    public void setPrefix(String prefix) {
        if (prefix != null && !prefix.isEmpty()) {
            this.prefix = prefix;
            PropertySet ps = new PropertySet();
            ps.setProject(getProject());
            ps.appendPrefix(prefix);
            addPropertyset(ps);
        }
    }

    public void setRegex(String regex) {
        if (regex != null && !regex.isEmpty()) {
            this.regex = regex;
            PropertySet ps = new PropertySet();
            ps.setProject(getProject());
            ps.appendRegex(regex);
            addPropertyset(ps);
        }
    }

    public void addPropertyset(PropertySet ps) {
        this.propertySets.add(ps);
    }

    public void setFormat(FormatAttribute ea) {
        this.format = ea.getValue();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/EchoProperties$FormatAttribute.class */
    public static class FormatAttribute extends EnumeratedAttribute {
        private String[] formats = {EncodingConstants.XML_NAMESPACE_PREFIX, "text"};

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return this.formats;
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.prefix != null && this.regex != null) {
            throw new BuildException("Please specify either prefix or regex, but not both", getLocation());
        }
        Hashtable<Object, Object> allProps = new Hashtable<>();
        if (this.inFile == null && this.propertySets.isEmpty()) {
            allProps.putAll(getProject().getProperties());
        } else if (this.inFile != null) {
            if (this.inFile.isDirectory()) {
                if (this.failonerror) {
                    throw new BuildException("srcfile is a directory!", getLocation());
                }
                log("srcfile is a directory!", 0);
                return;
            } else if (this.inFile.exists() && !this.inFile.canRead()) {
                if (this.failonerror) {
                    throw new BuildException("Can not read from the specified srcfile!", getLocation());
                }
                log("Can not read from the specified srcfile!", 0);
                return;
            } else {
                try {
                    InputStream in = Files.newInputStream(this.inFile.toPath(), new OpenOption[0]);
                    try {
                        Properties props = new Properties();
                        props.load(in);
                        allProps.putAll(props);
                        if (in != null) {
                            in.close();
                        }
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
                } catch (FileNotFoundException fnfe) {
                    String message = "Could not find file " + this.inFile.getAbsolutePath();
                    if (this.failonerror) {
                        throw new BuildException(message, fnfe, getLocation());
                    }
                    log(message, 1);
                    return;
                } catch (IOException ioe) {
                    String message2 = "Could not read file " + this.inFile.getAbsolutePath();
                    if (this.failonerror) {
                        throw new BuildException(message2, ioe, getLocation());
                    }
                    log(message2, 1);
                    return;
                }
            }
        }
        Stream<R> map = this.propertySets.stream().map((v0) -> {
            return v0.getProperties();
        });
        Objects.requireNonNull(allProps);
        map.forEach((v1) -> {
            r1.putAll(v1);
        });
        try {
            OutputStream os = createOutputStream();
            if (os != null) {
                saveProperties(allProps, os);
            }
            if (os != null) {
                os.close();
            }
        } catch (IOException ioe2) {
            if (this.failonerror) {
                throw new BuildException(ioe2, getLocation());
            }
            log(ioe2.getMessage(), 2);
        }
    }

    protected void saveProperties(Hashtable<Object, Object> allProps, OutputStream os) throws IOException, BuildException {
        final List<Object> keyList = new ArrayList<>(allProps.keySet());
        Properties props = new Properties() { // from class: org.apache.tools.ant.taskdefs.optional.EchoProperties.1
            private static final long serialVersionUID = 5090936442309201654L;

            @Override // java.util.Hashtable, java.util.Dictionary
            public Enumeration<Object> keys() {
                return (Enumeration) keyList.stream().sorted(Comparator.comparing((v0) -> {
                    return v0.toString();
                })).collect(Collectors.collectingAndThen(Collectors.toList(), (v0) -> {
                    return Collections.enumeration(v0);
                }));
            }

            @Override // java.util.Hashtable, java.util.Map
            public Set<Map.Entry<Object, Object>> entrySet() {
                Set<Map.Entry<Object, Object>> result = super.entrySet();
                if (JavaEnvUtils.isKaffe()) {
                    Set<Map.Entry<Object, Object>> t = new TreeSet<>(Comparator.comparing((v0) -> {
                        return v0.getKey();
                    }.andThen((v0) -> {
                        return v0.toString();
                    })));
                    t.addAll(result);
                    return t;
                }
                return result;
            }
        };
        allProps.forEach(k, v -> {
            props.put(String.valueOf(k), String.valueOf(v));
        });
        if ("text".equals(this.format)) {
            jdkSaveProperties(props, os, "Ant properties");
        } else if (EncodingConstants.XML_NAMESPACE_PREFIX.equals(this.format)) {
            xmlSaveProperties(props, os);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/EchoProperties$Tuple.class */
    public static final class Tuple implements Comparable<Tuple> {
        private String key;
        private String value;

        private Tuple(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.lang.Comparable
        public int compareTo(Tuple o) {
            return Comparator.naturalOrder().compare(this.key, o.key);
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || o.getClass() != getClass()) {
                return false;
            }
            Tuple that = (Tuple) o;
            return Objects.equals(this.key, that.key) && Objects.equals(this.value, that.value);
        }

        public int hashCode() {
            return Objects.hash(this.key);
        }
    }

    private List<Tuple> sortProperties(Properties props) {
        return (List) props.stringPropertyNames().stream().map(k -> {
            return new Tuple(k, props.getProperty(k));
        }).sorted().collect(Collectors.toList());
    }

    protected void xmlSaveProperties(Properties props, OutputStream os) throws IOException {
        Document doc = getDocumentBuilder().newDocument();
        Element rootElement = doc.createElement(PROPERTIES);
        List<Tuple> sorted = sortProperties(props);
        for (Tuple tuple : sorted) {
            Element propElement = doc.createElement("property");
            propElement.setAttribute("name", tuple.key);
            propElement.setAttribute("value", tuple.value);
            rootElement.appendChild(propElement);
        }
        try {
            Writer wri = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            new DOMElementWriter().write(rootElement, wri, 0, "\t");
            wri.flush();
            wri.close();
        } catch (IOException ioe) {
            throw new BuildException("Unable to write XML file", ioe);
        }
    }

    protected void jdkSaveProperties(Properties props, OutputStream os, String header) throws IOException {
        try {
            try {
                props.store(os, header);
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        log("Failed to close output stream");
                    }
                }
            } catch (IOException ioe) {
                throw new BuildException(ioe, getLocation());
            }
        } catch (Throwable th) {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e2) {
                    log("Failed to close output stream");
                }
            }
            throw th;
        }
    }

    private OutputStream createOutputStream() throws IOException {
        if (this.destfile == null) {
            return new LogOutputStream(this);
        }
        if (this.destfile.exists() && this.destfile.isDirectory()) {
            if (this.failonerror) {
                throw new BuildException("destfile is a directory!", getLocation());
            }
            log("destfile is a directory!", 0);
            return null;
        } else if (this.destfile.exists() && !this.destfile.canWrite()) {
            if (this.failonerror) {
                throw new BuildException("Can not write to the specified destfile!", getLocation());
            }
            log("Can not write to the specified destfile!", 0);
            return null;
        } else {
            return Files.newOutputStream(this.destfile.toPath(), new OpenOption[0]);
        }
    }

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
