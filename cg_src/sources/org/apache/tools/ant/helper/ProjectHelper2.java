package org.apache.tools.ant.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExtensionPoint;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.URLProvider;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.apache.tools.zip.ZipFile;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelper2.class */
public class ProjectHelper2 extends ProjectHelper {
    public static final String REFID_TARGETS = "ant.targets";
    private static final String REFID_CONTEXT = "ant.parsing.context";
    private static AntHandler elementHandler = new ElementHandler();
    private static AntHandler targetHandler = new TargetHandler();
    private static AntHandler mainHandler = new MainHandler();
    private static AntHandler projectHandler = new ProjectHandler();
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    @Override // org.apache.tools.ant.ProjectHelper
    public boolean canParseAntlibDescriptor(Resource resource) {
        return true;
    }

    @Override // org.apache.tools.ant.ProjectHelper
    public UnknownElement parseAntlibDescriptor(Project containingProject, Resource resource) {
        URLProvider up = (URLProvider) resource.as(URLProvider.class);
        if (up == null) {
            throw new BuildException("Unsupported resource type: " + resource);
        }
        return parseUnknownElement(containingProject, up.getURL());
    }

    public UnknownElement parseUnknownElement(Project project, URL source) throws BuildException {
        Target dummyTarget = new Target();
        dummyTarget.setProject(project);
        AntXMLContext context = new AntXMLContext(project);
        context.addTarget(dummyTarget);
        context.setImplicitTarget(dummyTarget);
        parse(context.getProject(), source, new RootHandler(context, elementHandler));
        Task[] tasks = dummyTarget.getTasks();
        if (tasks.length != 1) {
            throw new BuildException("No tasks defined");
        }
        return (UnknownElement) tasks[0];
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.tools.ant.ProjectHelper
    public void parse(Project project, Object source) throws BuildException {
        getImportStack().addElement(source);
        AntXMLContext context = (AntXMLContext) project.getReference(REFID_CONTEXT);
        if (context == null) {
            context = new AntXMLContext(project);
            project.addReference(REFID_CONTEXT, context);
            project.addReference(REFID_TARGETS, context.getTargets());
        }
        if (getImportStack().size() > 1) {
            context.setIgnoreProjectTag(true);
            Target currentTarget = context.getCurrentTarget();
            Target currentImplicit = context.getImplicitTarget();
            Map<String, Target> currentTargets = context.getCurrentTargets();
            try {
                Target newCurrent = new Target();
                newCurrent.setProject(project);
                newCurrent.setName("");
                context.setCurrentTarget(newCurrent);
                context.setCurrentTargets(new HashMap());
                context.setImplicitTarget(newCurrent);
                parse(project, source, new RootHandler(context, mainHandler));
                newCurrent.execute();
                context.setCurrentTarget(currentTarget);
                context.setImplicitTarget(currentImplicit);
                context.setCurrentTargets(currentTargets);
                return;
            } catch (Throwable th) {
                context.setCurrentTarget(currentTarget);
                context.setImplicitTarget(currentImplicit);
                context.setCurrentTargets(currentTargets);
                throw th;
            }
        }
        context.setCurrentTargets(new HashMap());
        parse(project, source, new RootHandler(context, mainHandler));
        context.getImplicitTarget().execute();
        resolveExtensionOfAttributes(project);
    }

    public void parse(Project project, Object source, RootHandler handler) throws BuildException {
        String buildFileName;
        String uri;
        AntXMLContext context = handler.context;
        File buildFile = null;
        URL url = null;
        if (source instanceof File) {
            buildFile = (File) source;
        } else if (source instanceof URL) {
            url = (URL) source;
        } else if (source instanceof Resource) {
            FileProvider fp = (FileProvider) ((Resource) source).as(FileProvider.class);
            if (fp != null) {
                buildFile = fp.getFile();
            } else {
                URLProvider up = (URLProvider) ((Resource) source).as(URLProvider.class);
                if (up != null) {
                    url = up.getURL();
                }
            }
        }
        if (buildFile != null) {
            buildFile = FILE_UTILS.normalize(buildFile.getAbsolutePath());
            context.setBuildFile(buildFile);
            buildFileName = buildFile.toString();
        } else if (url != null) {
            try {
                context.setBuildFile((File) null);
                context.setBuildFile(url);
                buildFileName = url.toString();
            } catch (MalformedURLException ex) {
                throw new BuildException(ex);
            }
        } else {
            throw new BuildException("Source " + source.getClass().getName() + " not supported by this plugin");
        }
        InputStream inputStream = null;
        ZipFile zf = null;
        try {
            try {
                try {
                    try {
                        XMLReader parser = JAXPUtils.getNamespaceXMLReader();
                        if (buildFile != null) {
                            uri = FILE_UTILS.toURI(buildFile.getAbsolutePath());
                            inputStream = Files.newInputStream(buildFile.toPath(), new OpenOption[0]);
                        } else {
                            uri = url.toString();
                            int pling = uri.indexOf("!/");
                            if (uri.startsWith("jar:file") && pling > -1) {
                                zf = new ZipFile(Locator.fromJarURI(uri), "UTF-8");
                                inputStream = zf.getInputStream(zf.getEntry(uri.substring(pling + 2)));
                            } else {
                                URLConnection conn = url.openConnection();
                                conn.setUseCaches(false);
                                inputStream = conn.getInputStream();
                            }
                        }
                        InputSource inputSource = new InputSource(inputStream);
                        if (uri != null) {
                            inputSource.setSystemId(uri);
                        }
                        project.log("parsing buildfile " + buildFileName + " with URI = " + uri + (zf != null ? " from a zip file" : ""), 3);
                        parser.setContentHandler(handler);
                        parser.setEntityResolver(handler);
                        parser.setErrorHandler(handler);
                        parser.setDTDHandler(handler);
                        parser.parse(inputSource);
                        FileUtils.close(inputStream);
                        ZipFile.closeQuietly(zf);
                    } catch (FileNotFoundException exc) {
                        throw new BuildException(exc);
                    }
                } catch (UnsupportedEncodingException exc2) {
                    throw new BuildException("Encoding of project file " + buildFileName + " is invalid.", exc2);
                } catch (SAXParseException exc3) {
                    Location location = new Location(exc3.getSystemId(), exc3.getLineNumber(), exc3.getColumnNumber());
                    Throwable t = exc3.getException();
                    if (t instanceof BuildException) {
                        BuildException be = (BuildException) t;
                        if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                            be.setLocation(location);
                        }
                        throw be;
                    }
                    throw new BuildException(exc3.getMessage(), t == null ? exc3 : t, location);
                }
            } catch (IOException exc4) {
                throw new BuildException("Error reading project file " + buildFileName + ": " + exc4.getMessage(), exc4);
            } catch (SAXException exc5) {
                Throwable t2 = exc5.getException();
                if (t2 instanceof BuildException) {
                    throw ((BuildException) t2);
                }
                throw new BuildException(exc5.getMessage(), t2 == null ? exc5 : t2);
            }
        } catch (Throwable th) {
            FileUtils.close(inputStream);
            ZipFile.closeQuietly(zf);
            throw th;
        }
    }

    protected static AntHandler getMainHandler() {
        return mainHandler;
    }

    protected static void setMainHandler(AntHandler handler) {
        mainHandler = handler;
    }

    protected static AntHandler getProjectHandler() {
        return projectHandler;
    }

    protected static void setProjectHandler(AntHandler handler) {
        projectHandler = handler;
    }

    protected static AntHandler getTargetHandler() {
        return targetHandler;
    }

    protected static void setTargetHandler(AntHandler handler) {
        targetHandler = handler;
    }

    protected static AntHandler getElementHandler() {
        return elementHandler;
    }

    protected static void setElementHandler(AntHandler handler) {
        elementHandler = handler;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelper2$AntHandler.class */
    public static class AntHandler {
        public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
        }

        public AntHandler onStartChild(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            throw new SAXParseException("Unexpected element \"" + qname + " \"", context.getLocator());
        }

        public void onEndChild(String uri, String tag, String qname, AntXMLContext context) throws SAXParseException {
        }

        public void onEndElement(String uri, String tag, AntXMLContext context) {
        }

        public void characters(char[] buf, int start, int count, AntXMLContext context) throws SAXParseException {
            String s = new String(buf, start, count).trim();
            if (!s.isEmpty()) {
                throw new SAXParseException("Unexpected text \"" + s + "\"", context.getLocator());
            }
        }

        protected void checkNamespace(String uri) {
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelper2$RootHandler.class */
    public static class RootHandler extends DefaultHandler {
        private Stack<AntHandler> antHandlers = new Stack<>();
        private AntHandler currentHandler;
        private AntXMLContext context;

        public RootHandler(AntXMLContext context, AntHandler rootHandler) {
            this.currentHandler = null;
            this.currentHandler = rootHandler;
            this.antHandlers.push(this.currentHandler);
            this.context = context;
        }

        public AntHandler getCurrentAntHandler() {
            return this.currentHandler;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            this.context.getProject().log("resolving systemId: " + systemId, 3);
            if (systemId.startsWith("file:")) {
                String path = ProjectHelper2.FILE_UTILS.fromURI(systemId);
                File file = new File(path);
                if (!file.isAbsolute()) {
                    file = ProjectHelper2.FILE_UTILS.resolveFile(this.context.getBuildFileParent(), path);
                    this.context.getProject().log("Warning: '" + systemId + "' in " + this.context.getBuildFile() + " should be expressed simply as '" + path.replace('\\', '/') + "' for compliance with other XML tools", 1);
                }
                this.context.getProject().log("file=" + file, 4);
                try {
                    InputSource inputSource = new InputSource(Files.newInputStream(file.toPath(), new OpenOption[0]));
                    inputSource.setSystemId(ProjectHelper2.FILE_UTILS.toURI(file.getAbsolutePath()));
                    return inputSource;
                } catch (IOException e) {
                    this.context.getProject().log(file.getAbsolutePath() + " could not be found", 1);
                }
            }
            this.context.getProject().log("could not resolve systemId", 4);
            return null;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String tag, String qname, Attributes attrs) throws SAXParseException {
            AntHandler next = this.currentHandler.onStartChild(uri, tag, qname, attrs, this.context);
            this.antHandlers.push(this.currentHandler);
            this.currentHandler = next;
            this.currentHandler.onStartElement(uri, tag, qname, attrs, this.context);
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void setDocumentLocator(org.xml.sax.Locator locator) {
            this.context.setLocator(locator);
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String name, String qName) throws SAXException {
            this.currentHandler.onEndElement(uri, name, this.context);
            this.currentHandler = this.antHandlers.pop();
            if (this.currentHandler != null) {
                this.currentHandler.onEndChild(uri, name, qName, this.context);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] buf, int start, int count) throws SAXParseException {
            this.currentHandler.characters(buf, start, count, this.context);
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startPrefixMapping(String prefix, String uri) {
            this.context.startPrefixMapping(prefix, uri);
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endPrefixMapping(String prefix) {
            this.context.endPrefixMapping(prefix);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelper2$MainHandler.class */
    public static class MainHandler extends AntHandler {
        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            if ("project".equals(name) && (uri.isEmpty() || uri.equals(ProjectHelper.ANT_CORE_URI))) {
                return ProjectHelper2.projectHandler;
            }
            if (name.equals(qname)) {
                throw new SAXParseException("Unexpected element \"{" + uri + "}" + name + "\" {" + ProjectHelper.ANT_CORE_URI + "}" + name, context.getLocator());
            }
            throw new SAXParseException("Unexpected element \"" + qname + "\" " + name, context.getLocator());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelper2$ProjectHandler.class */
    public static class ProjectHandler extends AntHandler {
        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            Object dupFile;
            Object contextFile;
            String baseDir = null;
            boolean nameAttributeSet = false;
            Project project = context.getProject();
            context.getImplicitTarget().setLocation(new Location(context.getLocator()));
            for (int i = 0; i < attrs.getLength(); i++) {
                String attrUri = attrs.getURI(i);
                if (attrUri == null || attrUri.isEmpty() || attrUri.equals(uri)) {
                    String value = attrs.getValue(i);
                    String localName = attrs.getLocalName(i);
                    boolean z = true;
                    switch (localName.hashCode()) {
                        case -332611556:
                            if (localName.equals(MagicNames.PROJECT_BASEDIR)) {
                                z = true;
                                break;
                            }
                            break;
                        case 3355:
                            if (localName.equals("id")) {
                                z = true;
                                break;
                            }
                            break;
                        case 3373707:
                            if (localName.equals("name")) {
                                z = true;
                                break;
                            }
                            break;
                        case 1544803905:
                            if (localName.equals("default")) {
                                z = false;
                                break;
                            }
                            break;
                    }
                    switch (z) {
                        case false:
                            if (value != null) {
                                if (!value.isEmpty() && !context.isIgnoringProjectTag()) {
                                    project.setDefault(value);
                                    break;
                                }
                            } else {
                                continue;
                            }
                            break;
                        case true:
                            if (value != null) {
                                context.setCurrentProjectName(value);
                                nameAttributeSet = true;
                                if (!context.isIgnoringProjectTag()) {
                                    project.setName(value);
                                    project.addReference(value, project);
                                    break;
                                } else if (ProjectHelper.isInIncludeMode() && !value.isEmpty() && ProjectHelper.getCurrentTargetPrefix() != null && ProjectHelper.getCurrentTargetPrefix().endsWith(ProjectHelper.USE_PROJECT_NAME_AS_TARGET_PREFIX)) {
                                    String newTargetPrefix = ProjectHelper.getCurrentTargetPrefix().replace(ProjectHelper.USE_PROJECT_NAME_AS_TARGET_PREFIX, value);
                                    ProjectHelper.setCurrentTargetPrefix(newTargetPrefix);
                                    break;
                                }
                            } else {
                                continue;
                            }
                            break;
                        case true:
                            if (value != null) {
                                if (context.isIgnoringProjectTag()) {
                                    break;
                                } else {
                                    project.addReference(value, project);
                                    break;
                                }
                            } else {
                                continue;
                            }
                        case true:
                            if (!context.isIgnoringProjectTag()) {
                                baseDir = value;
                                break;
                            } else {
                                continue;
                            }
                        default:
                            throw new SAXParseException("Unexpected attribute \"" + attrs.getQName(i) + "\"", context.getLocator());
                    }
                }
            }
            String antFileProp = "ant.file." + context.getCurrentProjectName();
            String dup = project.getProperty(antFileProp);
            String typeProp = "ant.file.type." + context.getCurrentProjectName();
            String dupType = project.getProperty(typeProp);
            if (dup != null && nameAttributeSet) {
                if ("url".equals(dupType)) {
                    try {
                        dupFile = new URL(dup);
                        contextFile = context.getBuildFileURL();
                    } catch (MalformedURLException mue) {
                        throw new BuildException("failed to parse " + dup + " as URL while looking at a duplicate project name.", mue);
                    }
                } else {
                    dupFile = new File(dup);
                    contextFile = context.getBuildFile();
                }
                if (context.isIgnoringProjectTag() && !dupFile.equals(contextFile)) {
                    project.log("Duplicated project name in import. Project " + context.getCurrentProjectName() + " defined first in " + dup + " and again in " + contextFile, 1);
                }
            }
            if (nameAttributeSet) {
                if (context.getBuildFile() != null) {
                    project.setUserProperty(antFileProp, context.getBuildFile().toString());
                    project.setUserProperty(typeProp, "file");
                } else if (context.getBuildFileURL() != null) {
                    project.setUserProperty(antFileProp, context.getBuildFileURL().toString());
                    project.setUserProperty(typeProp, "url");
                }
            }
            if (context.isIgnoringProjectTag()) {
                return;
            }
            if (project.getProperty(MagicNames.PROJECT_BASEDIR) != null) {
                project.setBasedir(project.getProperty(MagicNames.PROJECT_BASEDIR));
            } else if (baseDir == null) {
                project.setBasedir(context.getBuildFileParent().getAbsolutePath());
            } else if (new File(baseDir).isAbsolute()) {
                project.setBasedir(baseDir);
            } else {
                project.setBaseDir(ProjectHelper2.FILE_UTILS.resolveFile(context.getBuildFileParent(), baseDir));
            }
            project.addTarget("", context.getImplicitTarget());
            context.setCurrentTarget(context.getImplicitTarget());
        }

        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            return ((TypeProxy.INSTANCE_FIELD.equals(name) || "extension-point".equals(name)) && (uri.isEmpty() || uri.equals(ProjectHelper.ANT_CORE_URI))) ? ProjectHelper2.targetHandler : ProjectHelper2.elementHandler;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelper2$TargetHandler.class */
    public static class TargetHandler extends AntHandler {
        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            String name = null;
            String depends = "";
            String extensionPoint = null;
            ProjectHelper.OnMissingExtensionPoint extensionPointMissing = null;
            Project project = context.getProject();
            Target target = TypeProxy.INSTANCE_FIELD.equals(tag) ? new Target() : new ExtensionPoint();
            target.setProject(project);
            target.setLocation(new Location(context.getLocator()));
            context.addTarget(target);
            for (int i = 0; i < attrs.getLength(); i++) {
                String attrUri = attrs.getURI(i);
                if (attrUri == null || attrUri.isEmpty() || attrUri.equals(uri)) {
                    String value = attrs.getValue(i);
                    String localName = attrs.getLocalName(i);
                    boolean z = true;
                    switch (localName.hashCode()) {
                        case -1724546052:
                            if (localName.equals("description")) {
                                z = true;
                                break;
                            }
                            break;
                        case -840451150:
                            if (localName.equals("unless")) {
                                z = true;
                                break;
                            }
                            break;
                        case -257486218:
                            if (localName.equals("extensionOf")) {
                                z = true;
                                break;
                            }
                            break;
                        case 3355:
                            if (localName.equals("id")) {
                                z = true;
                                break;
                            }
                            break;
                        case 3357:
                            if (localName.equals(Jimple.IF)) {
                                z = true;
                                break;
                            }
                            break;
                        case 3373707:
                            if (localName.equals("name")) {
                                z = false;
                                break;
                            }
                            break;
                        case 1554151303:
                            if (localName.equals("depends")) {
                                z = true;
                                break;
                            }
                            break;
                        case 1707470584:
                            if (localName.equals("onMissingExtensionPoint")) {
                                z = true;
                                break;
                            }
                            break;
                    }
                    switch (z) {
                        case false:
                            name = value;
                            if (name.isEmpty()) {
                                throw new BuildException("name attribute must not be empty");
                            }
                            continue;
                        case true:
                            depends = value;
                            continue;
                        case true:
                            target.setIf(value);
                            continue;
                        case true:
                            target.setUnless(value);
                            continue;
                        case true:
                            if (value != null) {
                                if (value.isEmpty()) {
                                    break;
                                } else {
                                    context.getProject().addReference(value, target);
                                    break;
                                }
                            } else {
                                continue;
                            }
                        case true:
                            target.setDescription(value);
                            continue;
                        case true:
                            extensionPoint = value;
                            continue;
                        case true:
                            try {
                                extensionPointMissing = ProjectHelper.OnMissingExtensionPoint.valueOf(value);
                                continue;
                            } catch (IllegalArgumentException e) {
                                throw new BuildException("Invalid onMissingExtensionPoint " + value);
                            }
                        default:
                            throw new SAXParseException("Unexpected attribute \"" + attrs.getQName(i) + "\"", context.getLocator());
                    }
                }
            }
            if (name == null) {
                throw new SAXParseException("target element appears without a name attribute", context.getLocator());
            }
            String prefix = null;
            boolean isInIncludeMode = context.isIgnoringProjectTag() && ProjectHelper.isInIncludeMode();
            String sep = ProjectHelper.getCurrentPrefixSeparator();
            if (isInIncludeMode) {
                prefix = getTargetPrefix(context);
                if (prefix == null) {
                    throw new BuildException("can't include build file " + context.getBuildFileURL() + ", no as attribute has been given and the project tag doesn't specify a name attribute");
                }
                name = prefix + sep + name;
            }
            if (context.getCurrentTargets().get(name) != null) {
                throw new BuildException("Duplicate target '" + name + "'", target.getLocation());
            }
            Hashtable<String, Target> projectTargets = project.getTargets();
            boolean usedTarget = false;
            if (projectTargets.containsKey(name)) {
                project.log("Already defined in main or a previous import, ignore " + name, 3);
            } else {
                target.setName(name);
                context.getCurrentTargets().put(name, target);
                project.addOrReplaceTarget(name, target);
                usedTarget = true;
            }
            if (!depends.isEmpty()) {
                if (!isInIncludeMode) {
                    target.setDepends(depends);
                } else {
                    for (String string : Target.parseDepends(depends, name, "depends")) {
                        target.addDependency(prefix + sep + string);
                    }
                }
            }
            if (!isInIncludeMode && context.isIgnoringProjectTag()) {
                String targetPrefix = getTargetPrefix(context);
                prefix = targetPrefix;
                if (targetPrefix != null) {
                    String newName = prefix + sep + name;
                    Target newTarget = target;
                    if (usedTarget) {
                        newTarget = TypeProxy.INSTANCE_FIELD.equals(tag) ? new Target(target) : new ExtensionPoint(target);
                    }
                    newTarget.setName(newName);
                    context.getCurrentTargets().put(newName, newTarget);
                    project.addOrReplaceTarget(newName, newTarget);
                }
            }
            if (extensionPointMissing != null && extensionPoint == null) {
                throw new BuildException("onMissingExtensionPoint attribute cannot be specified unless extensionOf is specified", target.getLocation());
            }
            if (extensionPoint != null) {
                ProjectHelper helper = (ProjectHelper) context.getProject().getReference("ant.projectHelper");
                for (String extPointName : Target.parseDepends(extensionPoint, name, "extensionOf")) {
                    if (extensionPointMissing == null) {
                        extensionPointMissing = ProjectHelper.OnMissingExtensionPoint.FAIL;
                    }
                    if (ProjectHelper.isInIncludeMode()) {
                        helper.getExtensionStack().add(new String[]{extPointName, target.getName(), extensionPointMissing.name(), prefix + sep});
                    } else {
                        helper.getExtensionStack().add(new String[]{extPointName, target.getName(), extensionPointMissing.name()});
                    }
                }
            }
        }

        private String getTargetPrefix(AntXMLContext context) {
            String configuredValue = ProjectHelper.getCurrentTargetPrefix();
            if (configuredValue != null && configuredValue.isEmpty()) {
                configuredValue = null;
            }
            if (configuredValue != null) {
                return configuredValue;
            }
            String projectName = context.getCurrentProjectName();
            if (projectName != null && projectName.isEmpty()) {
                projectName = null;
            }
            return projectName;
        }

        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            return ProjectHelper2.elementHandler;
        }

        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public void onEndElement(String uri, String tag, AntXMLContext context) {
            context.setCurrentTarget(context.getImplicitTarget());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelper2$ElementHandler.class */
    public static class ElementHandler extends AntHandler {
        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            RuntimeConfigurable parentWrapper = context.currentWrapper();
            Object parent = null;
            if (parentWrapper != null) {
                parent = parentWrapper.getProxy();
            }
            UnknownElement task = new UnknownElement(tag);
            task.setProject(context.getProject());
            task.setNamespace(uri);
            task.setQName(qname);
            task.setTaskType(ProjectHelper.genComponentName(task.getNamespace(), tag));
            task.setTaskName(qname);
            Location location = new Location(context.getLocator().getSystemId(), context.getLocator().getLineNumber(), context.getLocator().getColumnNumber());
            task.setLocation(location);
            task.setOwningTarget(context.getCurrentTarget());
            if (parent != null) {
                ((UnknownElement) parent).addChild(task);
            } else {
                context.getCurrentTarget().addTask(task);
            }
            context.configureId(task, attrs);
            RuntimeConfigurable wrapper = new RuntimeConfigurable(task, task.getTaskName());
            for (int i = 0; i < attrs.getLength(); i++) {
                String name = attrs.getLocalName(i);
                String attrUri = attrs.getURI(i);
                if (attrUri != null && !attrUri.isEmpty() && !attrUri.equals(uri)) {
                    name = attrUri + ":" + attrs.getQName(i);
                }
                String value = attrs.getValue(i);
                if (ProjectHelper.ANT_TYPE.equals(name) || (ProjectHelper.ANT_CORE_URI.equals(attrUri) && ProjectHelper.ANT_TYPE.equals(attrs.getLocalName(i)))) {
                    name = ProjectHelper.ANT_TYPE;
                    int index = value.indexOf(":");
                    if (index < 0) {
                        continue;
                    } else {
                        String prefix = value.substring(0, index);
                        String mappedUri = context.getPrefixMapping(prefix);
                        if (mappedUri == null) {
                            throw new BuildException("Unable to find XML NS prefix \"" + prefix + "\"");
                        }
                        value = ProjectHelper.genComponentName(mappedUri, value.substring(index + 1));
                    }
                }
                wrapper.setAttribute(name, value);
            }
            if (parentWrapper != null) {
                parentWrapper.addChild(wrapper);
            }
            context.pushWrapper(wrapper);
        }

        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public void characters(char[] buf, int start, int count, AntXMLContext context) throws SAXParseException {
            RuntimeConfigurable wrapper = context.currentWrapper();
            wrapper.addText(buf, start, count);
        }

        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public AntHandler onStartChild(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
            return ProjectHelper2.elementHandler;
        }

        @Override // org.apache.tools.ant.helper.ProjectHelper2.AntHandler
        public void onEndElement(String uri, String tag, AntXMLContext context) {
            context.popWrapper();
        }
    }
}
