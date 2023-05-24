package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/XmlProperty.class */
public class XmlProperty extends Task {
    private static final String ID = "id";
    private static final String LOCATION = "location";
    private static final String VALUE = "value";
    private static final String PATH = "path";
    private Resource src;
    private String prefix = "";
    private boolean keepRoot = true;
    private boolean validate = false;
    private boolean collapseAttributes = false;
    private boolean semanticAttributes = false;
    private boolean includeSemanticAttribute = false;
    private File rootDirectory = null;
    private Map<String, String> addedAttributes = new Hashtable();
    private XMLCatalog xmlCatalog = new XMLCatalog();
    private String delimiter = ",";
    private static final String REF_ID = "refid";
    private static final String PATHID = "pathid";
    private static final String[] ATTRIBUTES = {"id", REF_ID, "location", "value", "path", PATHID};
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    @Override // org.apache.tools.ant.Task
    public void init() {
        super.init();
        this.xmlCatalog.setProject(getProject());
    }

    protected EntityResolver getEntityResolver() {
        return this.xmlCatalog;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Document document;
        Resource r = getResource();
        if (r == null) {
            throw new BuildException("XmlProperty task requires a source resource");
        }
        try {
            log("Loading " + this.src, 3);
            if (r.isExists()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(this.validate);
                factory.setNamespaceAware(false);
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setEntityResolver(getEntityResolver());
                FileProvider fp = (FileProvider) this.src.as(FileProvider.class);
                if (fp != null) {
                    document = builder.parse(fp.getFile());
                } else {
                    document = builder.parse(this.src.getInputStream());
                }
                Element topElement = document.getDocumentElement();
                this.addedAttributes = new Hashtable();
                if (this.keepRoot) {
                    addNodeRecursively(topElement, this.prefix, null);
                } else {
                    NodeList topChildren = topElement.getChildNodes();
                    int numChildren = topChildren.getLength();
                    for (int i = 0; i < numChildren; i++) {
                        addNodeRecursively(topChildren.item(i), this.prefix, null);
                    }
                }
            } else {
                log("Unable to find property resource: " + r, 3);
            }
        } catch (IOException ioe) {
            throw new BuildException("Failed to load " + this.src, ioe);
        } catch (ParserConfigurationException pce) {
            throw new BuildException(pce);
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
            }
            throw new BuildException("Failed to load " + this.src, x);
        }
    }

    private void addNodeRecursively(Node node, String prefix, Object container) {
        String nodePrefix = prefix;
        if (node.getNodeType() != 3) {
            if (!prefix.trim().isEmpty()) {
                nodePrefix = nodePrefix + ".";
            }
            nodePrefix = nodePrefix + node.getNodeName();
        }
        Object nodeObject = processNode(node, nodePrefix, container);
        if (node.hasChildNodes()) {
            NodeList nodeChildren = node.getChildNodes();
            int numChildren = nodeChildren.getLength();
            for (int i = 0; i < numChildren; i++) {
                addNodeRecursively(nodeChildren.item(i), nodePrefix, nodeObject);
            }
        }
    }

    void addNodeRecursively(Node node, String prefix) {
        addNodeRecursively(node, prefix, null);
    }

    public Object processNode(Node node, String prefix, Object container) {
        Object addedPath = null;
        String id = null;
        if (node.hasAttributes()) {
            NamedNodeMap nodeAttributes = node.getAttributes();
            Node idNode = nodeAttributes.getNamedItem("id");
            id = (!this.semanticAttributes || idNode == null) ? null : idNode.getNodeValue();
            for (int i = 0; i < nodeAttributes.getLength(); i++) {
                Node attributeNode = nodeAttributes.item(i);
                if (!this.semanticAttributes) {
                    String attributeName = getAttributeName(attributeNode);
                    addProperty(prefix + attributeName, getAttributeValue(attributeNode), null);
                } else {
                    String nodeName = attributeNode.getNodeName();
                    String attributeValue = getAttributeValue(attributeNode);
                    Path containingPath = container instanceof Path ? (Path) container : null;
                    if ("id".equals(nodeName)) {
                        continue;
                    } else if (containingPath != null && "path".equals(nodeName)) {
                        containingPath.setPath(attributeValue);
                    } else if (containingPath != null && (container instanceof Path) && REF_ID.equals(nodeName)) {
                        containingPath.setPath(attributeValue);
                    } else if (containingPath != null && (container instanceof Path) && "location".equals(nodeName)) {
                        containingPath.setLocation(resolveFile(attributeValue));
                    } else if (PATHID.equals(nodeName)) {
                        if (container != null) {
                            throw new BuildException("XmlProperty does not support nested paths");
                        }
                        addedPath = new Path(getProject());
                        getProject().addReference(attributeValue, addedPath);
                    } else {
                        String attributeName2 = getAttributeName(attributeNode);
                        addProperty(prefix + attributeName2, attributeValue, id);
                    }
                }
            }
        }
        String nodeText = null;
        boolean emptyNode = false;
        boolean semanticEmptyOverride = false;
        if (node.getNodeType() == 1 && this.semanticAttributes && node.hasAttributes() && (node.getAttributes().getNamedItem("value") != null || node.getAttributes().getNamedItem("location") != null || node.getAttributes().getNamedItem(REF_ID) != null || node.getAttributes().getNamedItem("path") != null || node.getAttributes().getNamedItem(PATHID) != null)) {
            semanticEmptyOverride = true;
        }
        if (node.getNodeType() == 3) {
            nodeText = getAttributeValue(node);
        } else if (node.getNodeType() == 1 && node.getChildNodes().getLength() == 1 && node.getFirstChild().getNodeType() == 4) {
            nodeText = node.getFirstChild().getNodeValue();
            if (nodeText.isEmpty() && !semanticEmptyOverride) {
                emptyNode = true;
            }
        } else if (node.getNodeType() == 1 && node.getChildNodes().getLength() == 0 && !semanticEmptyOverride) {
            nodeText = "";
            emptyNode = true;
        } else if (node.getNodeType() == 1 && node.getChildNodes().getLength() == 1 && node.getFirstChild().getNodeType() == 3 && node.getFirstChild().getNodeValue().isEmpty() && !semanticEmptyOverride) {
            nodeText = "";
            emptyNode = true;
        }
        if (nodeText != null) {
            if (this.semanticAttributes && id == null && (container instanceof String)) {
                id = (String) container;
            }
            if (!nodeText.trim().isEmpty() || emptyNode) {
                addProperty(prefix, nodeText, id);
            }
        }
        return addedPath != null ? addedPath : id;
    }

    private void addProperty(String name, String value, String id) {
        String msg = name + ":" + value;
        if (id != null) {
            msg = msg + "(id=" + id + ")";
        }
        log(msg, 4);
        if (this.addedAttributes.containsKey(name)) {
            value = this.addedAttributes.get(name) + getDelimiter() + value;
            getProject().setProperty(name, value);
            this.addedAttributes.put(name, value);
        } else if (getProject().getProperty(name) == null) {
            getProject().setNewProperty(name, value);
            this.addedAttributes.put(name, value);
        } else {
            log("Override ignored for property " + name, 3);
        }
        if (id != null) {
            getProject().addReference(id, value);
        }
    }

    private String getAttributeName(Node attributeNode) {
        String attributeName = attributeNode.getNodeName();
        if (!this.semanticAttributes) {
            return this.collapseAttributes ? "." + attributeName : "(" + attributeName + ")";
        } else if (REF_ID.equals(attributeName)) {
            return "";
        } else {
            if (!isSemanticAttribute(attributeName) || this.includeSemanticAttribute) {
                return "." + attributeName;
            }
            return "";
        }
    }

    private static boolean isSemanticAttribute(String attributeName) {
        return Arrays.asList(ATTRIBUTES).contains(attributeName);
    }

    private String getAttributeValue(Node attributeNode) {
        Object ref;
        String nodeValue = attributeNode.getNodeValue().trim();
        if (this.semanticAttributes) {
            String attributeName = attributeNode.getNodeName();
            nodeValue = getProject().replaceProperties(nodeValue);
            if ("location".equals(attributeName)) {
                File f = resolveFile(nodeValue);
                return f.getPath();
            } else if (REF_ID.equals(attributeName) && (ref = getProject().getReference(nodeValue)) != null) {
                return ref.toString();
            }
        }
        return nodeValue;
    }

    public void setFile(File src) {
        setSrcResource(new FileResource(src));
    }

    public void setSrcResource(Resource src) {
        if (src.isDirectory()) {
            throw new BuildException("the source can't be a directory");
        }
        if (src.as(FileProvider.class) != null || supportsNonFileResources()) {
            this.src = src;
            return;
        }
        throw new BuildException("Only FileSystem resources are supported.");
    }

    public void addConfigured(ResourceCollection a) {
        if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported as archives");
        }
        setSrcResource(a.iterator().next());
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix.trim();
    }

    public void setKeeproot(boolean keepRoot) {
        this.keepRoot = keepRoot;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public void setCollapseAttributes(boolean collapseAttributes) {
        this.collapseAttributes = collapseAttributes;
    }

    public void setSemanticAttributes(boolean semanticAttributes) {
        this.semanticAttributes = semanticAttributes;
    }

    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void setIncludeSemanticAttribute(boolean includeSemanticAttribute) {
        this.includeSemanticAttribute = includeSemanticAttribute;
    }

    public void addConfiguredXMLCatalog(XMLCatalog catalog) {
        this.xmlCatalog.addConfiguredXMLCatalog(catalog);
    }

    protected File getFile() {
        FileProvider fp = (FileProvider) this.src.as(FileProvider.class);
        if (fp != null) {
            return fp.getFile();
        }
        return null;
    }

    protected Resource getResource() {
        File f = getFile();
        FileProvider fp = (FileProvider) this.src.as(FileProvider.class);
        return f == null ? this.src : (fp == null || !fp.getFile().equals(f)) ? new FileResource(f) : this.src;
    }

    protected String getPrefix() {
        return this.prefix;
    }

    protected boolean getKeeproot() {
        return this.keepRoot;
    }

    protected boolean getValidate() {
        return this.validate;
    }

    protected boolean getCollapseAttributes() {
        return this.collapseAttributes;
    }

    protected boolean getSemanticAttributes() {
        return this.semanticAttributes;
    }

    protected File getRootDirectory() {
        return this.rootDirectory;
    }

    @Deprecated
    protected boolean getIncludeSementicAttribute() {
        return getIncludeSemanticAttribute();
    }

    protected boolean getIncludeSemanticAttribute() {
        return this.includeSemanticAttribute;
    }

    private File resolveFile(String fileName) {
        return FILE_UTILS.resolveFile(this.rootDirectory == null ? getProject().getBaseDir() : this.rootDirectory, fileName);
    }

    protected boolean supportsNonFileResources() {
        return getClass().equals(XmlProperty.class);
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
