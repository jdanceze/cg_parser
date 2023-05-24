package org.apache.tools.ant.types;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Stack;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/XMLCatalog.class */
public class XMLCatalog extends DataType implements EntityResolver, URIResolver {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private Path classpath;
    private Path catalogPath;
    public static final String APACHE_RESOLVER = "org.apache.tools.ant.types.resolver.ApacheCatalogResolver";
    public static final String CATALOG_RESOLVER = "org.apache.xml.resolver.tools.CatalogResolver";
    private Vector<ResourceLocation> elements = new Vector<>();
    private CatalogResolver catalogResolver = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/XMLCatalog$CatalogResolver.class */
    public interface CatalogResolver extends URIResolver, EntityResolver {
        @Override // org.xml.sax.EntityResolver
        InputSource resolveEntity(String str, String str2);
    }

    public XMLCatalog() {
        setChecked(false);
    }

    private Vector<ResourceLocation> getElements() {
        return getRef().elements;
    }

    private Path getClasspath() {
        return getRef().classpath;
    }

    public Path createClasspath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        setChecked(false);
        return this.classpath.createPath();
    }

    public void setClasspath(Path classpath) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
        setChecked(false);
    }

    public void setClasspathRef(Reference r) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createClasspath().setRefid(r);
        setChecked(false);
    }

    public Path createCatalogPath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        if (this.catalogPath == null) {
            this.catalogPath = new Path(getProject());
        }
        setChecked(false);
        return this.catalogPath.createPath();
    }

    public void setCatalogPathRef(Reference r) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        createCatalogPath().setRefid(r);
        setChecked(false);
    }

    public Path getCatalogPath() {
        return getRef().catalogPath;
    }

    public void addDTD(ResourceLocation dtd) throws BuildException {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        getElements().addElement(dtd);
        setChecked(false);
    }

    public void addEntity(ResourceLocation entity) throws BuildException {
        addDTD(entity);
    }

    public void addConfiguredXMLCatalog(XMLCatalog catalog) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        getElements().addAll(catalog.getElements());
        Path nestedClasspath = catalog.getClasspath();
        createClasspath().append(nestedClasspath);
        Path nestedCatalogPath = catalog.getCatalogPath();
        createCatalogPath().append(nestedCatalogPath);
        setChecked(false);
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (!this.elements.isEmpty()) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (isReference()) {
            return getRef().resolveEntity(publicId, systemId);
        }
        dieOnCircularReference();
        log("resolveEntity: '" + publicId + "': '" + systemId + "'", 4);
        InputSource inputSource = getCatalogResolver().resolveEntity(publicId, systemId);
        if (inputSource == null) {
            log("No matching catalog entry found, parser will use: '" + systemId + "'", 4);
        }
        return inputSource;
    }

    @Override // javax.xml.transform.URIResolver
    public Source resolve(String href, String base) throws TransformerException {
        URL baseURL;
        if (isReference()) {
            return getRef().resolve(href, base);
        }
        dieOnCircularReference();
        String uri = removeFragment(href);
        log("resolve: '" + uri + "' with base: '" + base + "'", 4);
        SAXSource source = (SAXSource) getCatalogResolver().resolve(uri, base);
        if (source == null) {
            log("No matching catalog entry found, parser will use: '" + href + "'", 4);
            source = new SAXSource();
            try {
                if (base == null) {
                    baseURL = FILE_UTILS.getFileURL(getProject().getBaseDir());
                } else {
                    baseURL = new URL(base);
                }
                URL url = uri.isEmpty() ? baseURL : new URL(baseURL, uri);
                source.setInputSource(new InputSource(url.toString()));
            } catch (MalformedURLException e) {
                source.setInputSource(new InputSource(uri));
            }
        }
        setEntityResolver(source);
        return source;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        if (this.classpath != null) {
            pushAndInvokeCircularReferenceCheck(this.classpath, stk, p);
        }
        if (this.catalogPath != null) {
            pushAndInvokeCircularReferenceCheck(this.catalogPath, stk, p);
        }
        setChecked(true);
    }

    private XMLCatalog getRef() {
        if (!isReference()) {
            return this;
        }
        return (XMLCatalog) getCheckedRef(XMLCatalog.class);
    }

    private CatalogResolver getCatalogResolver() {
        if (this.catalogResolver == null) {
            AntClassLoader loader = getProject().createClassLoader(Path.systemClasspath);
            try {
                ClassLoader apacheResolverLoader = Class.forName(APACHE_RESOLVER, true, loader).getClassLoader();
                Class<?> baseResolverClass = Class.forName(CATALOG_RESOLVER, true, apacheResolverLoader);
                ClassLoader baseResolverLoader = baseResolverClass.getClassLoader();
                Class<?> clazz = Class.forName(APACHE_RESOLVER, true, baseResolverLoader);
                Object obj = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                this.catalogResolver = new ExternalResolver(clazz, obj);
            } catch (Throwable ex) {
                this.catalogResolver = new InternalResolver();
                if (getCatalogPath() != null && getCatalogPath().list().length != 0) {
                    log("Warning: XML resolver not found; external catalogs will be ignored", 1);
                }
                log("Failed to load Apache resolver: " + ex, 4);
            }
        }
        return this.catalogResolver;
    }

    private void setEntityResolver(SAXSource source) throws TransformerException {
        XMLReader reader = source.getXMLReader();
        if (reader == null) {
            SAXParserFactory spFactory = SAXParserFactory.newInstance();
            spFactory.setNamespaceAware(true);
            try {
                reader = spFactory.newSAXParser().getXMLReader();
            } catch (ParserConfigurationException | SAXException ex) {
                throw new TransformerException(ex);
            }
        }
        reader.setEntityResolver(this);
        source.setXMLReader(reader);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResourceLocation findMatchingEntry(String publicId) {
        return (ResourceLocation) getElements().stream().filter(e -> {
            return e.getPublicId().equals(publicId);
        }).findFirst().orElse(null);
    }

    private String removeFragment(String uri) {
        String result = uri;
        int hashPos = uri.indexOf("#");
        if (hashPos >= 0) {
            result = uri.substring(0, hashPos);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InputSource filesystemLookup(ResourceLocation matchingEntry) {
        URL baseURL;
        String fileName;
        String uri = matchingEntry.getLocation().replace(File.separatorChar, '/');
        if (matchingEntry.getBase() != null) {
            baseURL = matchingEntry.getBase();
        } else {
            try {
                baseURL = FILE_UTILS.getFileURL(getProject().getBaseDir());
            } catch (MalformedURLException e) {
                throw new BuildException("Project basedir cannot be converted to a URL");
            }
        }
        URL url = null;
        try {
            url = new URL(baseURL, uri);
        } catch (MalformedURLException e2) {
            File testFile = new File(uri);
            if (testFile.exists() && testFile.canRead()) {
                log("uri : '" + uri + "' matches a readable file", 4);
                try {
                    url = FILE_UTILS.getFileURL(testFile);
                } catch (MalformedURLException e3) {
                    throw new BuildException("could not find an URL for :" + testFile.getAbsolutePath());
                }
            } else {
                log("uri : '" + uri + "' does not match a readable file", 4);
            }
        }
        InputSource source = null;
        if (url != null && "file".equals(url.getProtocol()) && (fileName = FILE_UTILS.fromURI(url.toString())) != null) {
            log("fileName " + fileName, 4);
            File resFile = new File(fileName);
            if (resFile.exists() && resFile.canRead()) {
                try {
                    source = new InputSource(Files.newInputStream(resFile.toPath(), new OpenOption[0]));
                    String sysid = JAXPUtils.getSystemId(resFile);
                    source.setSystemId(sysid);
                    log("catalog entry matched a readable file: '" + sysid + "'", 4);
                } catch (IOException e4) {
                }
            }
        }
        return source;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InputSource classpathLookup(ResourceLocation matchingEntry) {
        Path cp;
        InputSource source = null;
        Path cp2 = this.classpath;
        if (cp2 != null) {
            cp = this.classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
        } else {
            cp = new Path(getProject()).concatSystemClasspath("last");
        }
        AntClassLoader loader = getProject().createClassLoader(cp);
        InputStream is = loader.getResourceAsStream(matchingEntry.getLocation());
        if (is != null) {
            source = new InputSource(is);
            URL entryURL = loader.getResource(matchingEntry.getLocation());
            String sysid = entryURL.toExternalForm();
            source.setSystemId(sysid);
            log("catalog entry matched a resource in the classpath: '" + sysid + "'", 4);
        }
        return source;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InputSource urlLookup(ResourceLocation matchingEntry) {
        URL baseURL;
        URL url;
        String uri = matchingEntry.getLocation();
        if (matchingEntry.getBase() != null) {
            baseURL = matchingEntry.getBase();
        } else {
            try {
                baseURL = FILE_UTILS.getFileURL(getProject().getBaseDir());
            } catch (MalformedURLException e) {
                throw new BuildException("Project basedir cannot be converted to a URL");
            }
        }
        try {
            url = new URL(baseURL, uri);
        } catch (MalformedURLException e2) {
            url = null;
        }
        InputSource source = null;
        if (url != null) {
            try {
                InputStream is = null;
                URLConnection conn = url.openConnection();
                if (conn != null) {
                    conn.setUseCaches(false);
                    is = conn.getInputStream();
                }
                if (is != null) {
                    source = new InputSource(is);
                    String sysid = url.toExternalForm();
                    source.setSystemId(sysid);
                    log("catalog entry matched as a URL: '" + sysid + "'", 4);
                }
            } catch (IOException e3) {
            }
        }
        return source;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/XMLCatalog$InternalResolver.class */
    public class InternalResolver implements CatalogResolver {
        public InternalResolver() {
            XMLCatalog.this.log("Apache resolver library not found, internal resolver will be used", 3);
        }

        @Override // org.apache.tools.ant.types.XMLCatalog.CatalogResolver, org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            InputSource result = null;
            ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(publicId);
            if (matchingEntry != null) {
                XMLCatalog.this.log("Matching catalog entry found for publicId: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
                result = XMLCatalog.this.filesystemLookup(matchingEntry);
                if (result == null) {
                    result = XMLCatalog.this.classpathLookup(matchingEntry);
                }
                if (result == null) {
                    result = XMLCatalog.this.urlLookup(matchingEntry);
                }
            }
            return result;
        }

        @Override // javax.xml.transform.URIResolver
        public Source resolve(String href, String base) throws TransformerException {
            SAXSource result = null;
            ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(href);
            if (matchingEntry != null) {
                XMLCatalog.this.log("Matching catalog entry found for uri: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
                ResourceLocation entryCopy = matchingEntry;
                if (base != null) {
                    try {
                        URL baseURL = new URL(base);
                        entryCopy = new ResourceLocation();
                        entryCopy.setBase(baseURL);
                    } catch (MalformedURLException e) {
                    }
                }
                entryCopy.setPublicId(matchingEntry.getPublicId());
                entryCopy.setLocation(matchingEntry.getLocation());
                InputSource source = XMLCatalog.this.filesystemLookup(entryCopy);
                if (source == null) {
                    source = XMLCatalog.this.classpathLookup(entryCopy);
                }
                if (source == null) {
                    source = XMLCatalog.this.urlLookup(entryCopy);
                }
                if (source != null) {
                    result = new SAXSource(source);
                }
            }
            return result;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/XMLCatalog$ExternalResolver.class */
    public class ExternalResolver implements CatalogResolver {
        private Method setXMLCatalog;
        private Method parseCatalog;
        private Method resolveEntity;
        private Method resolve;
        private Object resolverImpl;
        private boolean externalCatalogsProcessed = false;

        public ExternalResolver(Class<?> resolverImplClass, Object resolverImpl) {
            this.setXMLCatalog = null;
            this.parseCatalog = null;
            this.resolveEntity = null;
            this.resolve = null;
            this.resolverImpl = null;
            this.resolverImpl = resolverImpl;
            try {
                this.setXMLCatalog = resolverImplClass.getMethod("setXMLCatalog", XMLCatalog.class);
                this.parseCatalog = resolverImplClass.getMethod("parseCatalog", String.class);
                this.resolveEntity = resolverImplClass.getMethod("resolveEntity", String.class, String.class);
                this.resolve = resolverImplClass.getMethod("resolve", String.class, String.class);
                XMLCatalog.this.log("Apache resolver library found, xml-commons resolver will be used", 3);
            } catch (NoSuchMethodException ex) {
                throw new BuildException(ex);
            }
        }

        @Override // org.apache.tools.ant.types.XMLCatalog.CatalogResolver, org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            InputSource result;
            processExternalCatalogs();
            ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(publicId);
            if (matchingEntry != null) {
                XMLCatalog.this.log("Matching catalog entry found for publicId: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
                result = XMLCatalog.this.filesystemLookup(matchingEntry);
                if (result == null) {
                    result = XMLCatalog.this.classpathLookup(matchingEntry);
                }
                if (result == null) {
                    try {
                        result = (InputSource) this.resolveEntity.invoke(this.resolverImpl, publicId, systemId);
                    } catch (Exception ex) {
                        throw new BuildException(ex);
                    }
                }
            } else {
                try {
                    result = (InputSource) this.resolveEntity.invoke(this.resolverImpl, publicId, systemId);
                } catch (Exception ex2) {
                    throw new BuildException(ex2);
                }
            }
            return result;
        }

        @Override // javax.xml.transform.URIResolver
        public Source resolve(String href, String base) throws TransformerException {
            SAXSource result;
            processExternalCatalogs();
            ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(href);
            if (matchingEntry != null) {
                XMLCatalog.this.log("Matching catalog entry found for uri: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
                ResourceLocation entryCopy = matchingEntry;
                if (base != null) {
                    try {
                        URL baseURL = new URL(base);
                        entryCopy = new ResourceLocation();
                        entryCopy.setBase(baseURL);
                    } catch (MalformedURLException e) {
                    }
                }
                entryCopy.setPublicId(matchingEntry.getPublicId());
                entryCopy.setLocation(matchingEntry.getLocation());
                InputSource source = XMLCatalog.this.filesystemLookup(entryCopy);
                if (source == null) {
                    source = XMLCatalog.this.classpathLookup(entryCopy);
                }
                if (source != null) {
                    result = new SAXSource(source);
                } else {
                    try {
                        result = (SAXSource) this.resolve.invoke(this.resolverImpl, href, base);
                    } catch (Exception ex) {
                        throw new BuildException(ex);
                    }
                }
            } else {
                if (base == null) {
                    try {
                        base = XMLCatalog.FILE_UTILS.getFileURL(XMLCatalog.this.getProject().getBaseDir()).toString();
                    } catch (MalformedURLException x) {
                        throw new TransformerException(x);
                    }
                }
                try {
                    result = (SAXSource) this.resolve.invoke(this.resolverImpl, href, base);
                } catch (Exception ex2) {
                    throw new BuildException(ex2);
                }
            }
            return result;
        }

        private void processExternalCatalogs() {
            String[] list;
            if (!this.externalCatalogsProcessed) {
                try {
                    this.setXMLCatalog.invoke(this.resolverImpl, XMLCatalog.this);
                    Path catPath = XMLCatalog.this.getCatalogPath();
                    if (catPath != null) {
                        XMLCatalog.this.log("Using catalogpath '" + XMLCatalog.this.getCatalogPath() + "'", 4);
                        for (String catFileName : XMLCatalog.this.getCatalogPath().list()) {
                            File catFile = new File(catFileName);
                            XMLCatalog.this.log("Parsing " + catFile, 4);
                            try {
                                this.parseCatalog.invoke(this.resolverImpl, catFile.getPath());
                            } catch (Exception ex) {
                                throw new BuildException(ex);
                            }
                        }
                    }
                } catch (Exception ex2) {
                    throw new BuildException(ex2);
                }
            }
            this.externalCatalogsProcessed = true;
        }
    }
}
