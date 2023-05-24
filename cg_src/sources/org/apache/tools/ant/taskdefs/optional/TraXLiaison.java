package org.apache.tools.ant.taskdefs.optional;

import com.google.common.net.HttpHeaders;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.XSLTLiaison4;
import org.apache.tools.ant.taskdefs.XSLTLogger;
import org.apache.tools.ant.taskdefs.XSLTLoggerAware;
import org.apache.tools.ant.taskdefs.XSLTProcess;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.URLProvider;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.StreamUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/TraXLiaison.class */
public class TraXLiaison implements XSLTLiaison4, ErrorListener, XSLTLoggerAware {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private Project project;
    private Resource stylesheet;
    private XSLTLogger logger;
    private EntityResolver entityResolver;
    private Transformer transformer;
    private Templates templates;
    private long templatesModTime;
    private URIResolver uriResolver;
    private String factoryName = null;
    private TransformerFactory tfactory = null;
    private final Vector<String[]> outputProperties = new Vector<>();
    private final Hashtable<String, Object> params = new Hashtable<>();
    private final List<Object[]> attributes = new ArrayList();
    private final Map<String, Boolean> features = new HashMap();
    private boolean suppressWarnings = false;
    private XSLTProcess.TraceConfiguration traceConfiguration = null;

    @Override // org.apache.tools.ant.taskdefs.XSLTLiaison
    public void setStylesheet(File stylesheet) throws Exception {
        FileResource fr = new FileResource();
        fr.setProject(this.project);
        fr.setFile(stylesheet);
        setStylesheet(fr);
    }

    @Override // org.apache.tools.ant.taskdefs.XSLTLiaison3
    public void setStylesheet(Resource stylesheet) throws Exception {
        if (this.stylesheet != null) {
            this.transformer = null;
            if (!this.stylesheet.equals(stylesheet) || stylesheet.getLastModified() != this.templatesModTime) {
                this.templates = null;
            }
        }
        this.stylesheet = stylesheet;
    }

    @Override // org.apache.tools.ant.taskdefs.XSLTLiaison
    public void transform(File infile, File outfile) throws Exception {
        if (this.transformer == null) {
            createTransformer();
        }
        InputStream fis = new BufferedInputStream(Files.newInputStream(infile.toPath(), new OpenOption[0]));
        try {
            OutputStream fos = new BufferedOutputStream(Files.newOutputStream(outfile.toPath(), new OpenOption[0]));
            StreamResult res = new StreamResult(fos);
            res.setSystemId(JAXPUtils.getSystemId(outfile));
            setTransformationParameters();
            this.transformer.transform(getSource(fis, infile), res);
            fos.close();
            fis.close();
        } catch (Throwable th) {
            try {
                fis.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private Source getSource(InputStream is, File infile) throws ParserConfigurationException, SAXException {
        Source src;
        if (this.entityResolver != null) {
            if (getFactory().getFeature(SAXSource.FEATURE)) {
                SAXParserFactory spFactory = SAXParserFactory.newInstance();
                spFactory.setNamespaceAware(true);
                XMLReader reader = spFactory.newSAXParser().getXMLReader();
                reader.setEntityResolver(this.entityResolver);
                src = new SAXSource(reader, new InputSource(is));
            } else {
                throw new IllegalStateException("xcatalog specified, but parser doesn't support SAX");
            }
        } else {
            src = new StreamSource(is);
        }
        src.setSystemId(JAXPUtils.getSystemId(infile));
        return src;
    }

    private Source getSource(InputStream is, Resource resource) throws ParserConfigurationException, SAXException {
        Source src;
        if (this.entityResolver != null) {
            if (getFactory().getFeature(SAXSource.FEATURE)) {
                SAXParserFactory spFactory = SAXParserFactory.newInstance();
                spFactory.setNamespaceAware(true);
                XMLReader reader = spFactory.newSAXParser().getXMLReader();
                reader.setEntityResolver(this.entityResolver);
                src = new SAXSource(reader, new InputSource(is));
            } else {
                throw new IllegalStateException("xcatalog specified, but parser doesn't support SAX");
            }
        } else {
            src = new StreamSource(is);
        }
        src.setSystemId(resourceToURI(resource));
        return src;
    }

    private String resourceToURI(Resource resource) {
        FileProvider fp = (FileProvider) resource.as(FileProvider.class);
        if (fp != null) {
            return FILE_UTILS.toURI(fp.getFile().getAbsolutePath());
        }
        URLProvider up = (URLProvider) resource.as(URLProvider.class);
        if (up != null) {
            URL u = up.getURL();
            return String.valueOf(u);
        }
        return resource.getName();
    }

    private void readTemplates() throws IOException, TransformerConfigurationException, ParserConfigurationException, SAXException {
        InputStream xslStream = new BufferedInputStream(this.stylesheet.getInputStream());
        try {
            this.templatesModTime = this.stylesheet.getLastModified();
            Source src = getSource(xslStream, this.stylesheet);
            this.templates = getFactory().newTemplates(src);
            xslStream.close();
        } catch (Throwable th) {
            try {
                xslStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void createTransformer() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        if (this.templates == null) {
            readTemplates();
        }
        this.transformer = this.templates.newTransformer();
        this.transformer.setErrorListener(this);
        if (this.uriResolver != null) {
            this.transformer.setURIResolver(this.uriResolver);
        }
        Iterator<String[]> it = this.outputProperties.iterator();
        while (it.hasNext()) {
            String[] pair = it.next();
            this.transformer.setOutputProperty(pair[0], pair[1]);
        }
        if (this.traceConfiguration != null) {
            if ("org.apache.xalan.transformer.TransformerImpl".equals(this.transformer.getClass().getName())) {
                try {
                    Class<?> traceSupport = Class.forName("org.apache.tools.ant.taskdefs.optional.Xalan2TraceSupport", true, Thread.currentThread().getContextClassLoader());
                    XSLTTraceSupport ts = (XSLTTraceSupport) traceSupport.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    ts.configureTrace(this.transformer, this.traceConfiguration);
                    return;
                } catch (Exception e) {
                    String msg = "Failed to enable tracing because of " + e;
                    if (this.project != null) {
                        this.project.log(msg, 1);
                        return;
                    } else {
                        System.err.println(msg);
                        return;
                    }
                }
            }
            String msg2 = "Not enabling trace support for transformer implementation" + this.transformer.getClass().getName();
            if (this.project != null) {
                this.project.log(msg2, 1);
            } else {
                System.err.println(msg2);
            }
        }
    }

    private void setTransformationParameters() {
        this.params.forEach(key, value -> {
            this.transformer.setParameter(key, value);
        });
    }

    private TransformerFactory getFactory() throws BuildException {
        if (this.tfactory != null) {
            return this.tfactory;
        }
        if (this.factoryName == null) {
            this.tfactory = TransformerFactory.newInstance();
        } else {
            Class<?> clazz = null;
            try {
                try {
                    clazz = Class.forName(this.factoryName, true, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    String msg = "Failed to load " + this.factoryName + " via the configured classpath, will try Ant's classpath instead.";
                    if (this.logger != null) {
                        this.logger.log(msg);
                    } else if (this.project != null) {
                        this.project.log(msg, 1);
                    } else {
                        System.err.println(msg);
                    }
                }
                if (clazz == null) {
                    clazz = Class.forName(this.factoryName);
                }
                this.tfactory = (TransformerFactory) clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Exception e2) {
                throw new BuildException(e2);
            }
        }
        applyReflectionHackForExtensionMethods();
        this.tfactory.setErrorListener(this);
        for (Object[] pair : this.attributes) {
            this.tfactory.setAttribute((String) pair[0], pair[1]);
        }
        for (Map.Entry<String, Boolean> feature : this.features.entrySet()) {
            try {
                this.tfactory.setFeature(feature.getKey(), feature.getValue().booleanValue());
            } catch (TransformerConfigurationException ex) {
                throw new BuildException(ex);
            }
        }
        if (this.uriResolver != null) {
            this.tfactory.setURIResolver(this.uriResolver);
        }
        return this.tfactory;
    }

    public void setFactory(String name) {
        this.factoryName = name;
    }

    public void setAttribute(String name, Object value) {
        Object[] pair = {name, value};
        this.attributes.add(pair);
    }

    public void setFeature(String name, boolean value) {
        this.features.put(name, Boolean.valueOf(value));
    }

    public void setOutputProperty(String name, String value) {
        String[] pair = {name, value};
        this.outputProperties.addElement(pair);
    }

    public void setEntityResolver(EntityResolver aResolver) {
        this.entityResolver = aResolver;
    }

    public void setURIResolver(URIResolver aResolver) {
        this.uriResolver = aResolver;
    }

    @Override // org.apache.tools.ant.taskdefs.XSLTLiaison
    public void addParam(String name, String value) {
        this.params.put(name, value);
    }

    @Override // org.apache.tools.ant.taskdefs.XSLTLiaison4
    public void addParam(String name, Object value) {
        this.params.put(name, value);
    }

    @Override // org.apache.tools.ant.taskdefs.XSLTLoggerAware
    public void setLogger(XSLTLogger l) {
        this.logger = l;
    }

    @Override // javax.xml.transform.ErrorListener
    public void error(TransformerException e) {
        logError(e, "Error");
    }

    @Override // javax.xml.transform.ErrorListener
    public void fatalError(TransformerException e) {
        logError(e, "Fatal Error");
        throw new BuildException("Fatal error during transformation using " + this.stylesheet + ": " + e.getMessageAndLocation(), e);
    }

    @Override // javax.xml.transform.ErrorListener
    public void warning(TransformerException e) {
        if (!this.suppressWarnings) {
            logError(e, HttpHeaders.WARNING);
        }
    }

    private void logError(TransformerException e, String type) {
        if (this.logger == null) {
            return;
        }
        StringBuffer msg = new StringBuffer();
        SourceLocator locator = e.getLocator();
        if (locator != null) {
            String systemid = locator.getSystemId();
            if (systemid != null) {
                String url = systemid;
                if (url.startsWith("file:")) {
                    url = FileUtils.getFileUtils().fromURI(url);
                }
                msg.append(url);
            } else {
                msg.append("Unknown file");
            }
            int line = locator.getLineNumber();
            if (line != -1) {
                msg.append(":");
                msg.append(line);
                int column = locator.getColumnNumber();
                if (column != -1) {
                    msg.append(":");
                    msg.append(column);
                }
            }
        }
        msg.append(": ");
        msg.append(type);
        msg.append("! ");
        msg.append(e.getMessage());
        if (e.getCause() != null) {
            msg.append(" Cause: ");
            msg.append(e.getCause());
        }
        this.logger.log(msg.toString());
    }

    @Deprecated
    protected String getSystemId(File file) {
        return JAXPUtils.getSystemId(file);
    }

    @Override // org.apache.tools.ant.taskdefs.XSLTLiaison2
    public void configure(XSLTProcess xsltTask) {
        this.project = xsltTask.getProject();
        XSLTProcess.Factory factory = xsltTask.getFactory();
        if (factory != null) {
            setFactory(factory.getName());
            StreamUtils.enumerationAsStream(factory.getAttributes()).forEach(attr -> {
                setAttribute(attr.getName(), attr.getValue());
            });
            factory.getFeatures().forEach(feature -> {
                setFeature(feature.getName(), feature.getValue());
            });
        }
        XMLCatalog xmlCatalog = xsltTask.getXMLCatalog();
        if (xmlCatalog != null) {
            setEntityResolver(xmlCatalog);
            setURIResolver(xmlCatalog);
        }
        StreamUtils.enumerationAsStream(xsltTask.getOutputProperties()).forEach(prop -> {
            setOutputProperty(prop.getName(), prop.getValue());
        });
        this.suppressWarnings = xsltTask.getSuppressWarnings();
        this.traceConfiguration = xsltTask.getTraceConfiguration();
    }

    private void applyReflectionHackForExtensionMethods() {
        if (!JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_9)) {
            try {
                Field _isNotSecureProcessing = this.tfactory.getClass().getDeclaredField("_isNotSecureProcessing");
                _isNotSecureProcessing.setAccessible(true);
                _isNotSecureProcessing.set(this.tfactory, Boolean.TRUE);
            } catch (Exception x) {
                if (this.project != null) {
                    this.project.log(x.toString(), 4);
                }
            }
        }
    }
}
