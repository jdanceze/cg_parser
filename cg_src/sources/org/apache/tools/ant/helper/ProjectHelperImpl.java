package org.apache.tools.ant.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Locale;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.TypeAdapter;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderAdapter;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl.class */
public class ProjectHelperImpl extends ProjectHelper {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private Parser parser;
    private Project project;
    private File buildFile;
    private File buildFileParent;
    private Locator locator;
    private Target implicitTarget = new Target();

    public ProjectHelperImpl() {
        this.implicitTarget.setName("");
    }

    @Override // org.apache.tools.ant.ProjectHelper
    public void parse(Project project, Object source) throws BuildException {
        if (!(source instanceof File)) {
            throw new BuildException("Only File source supported by default plugin");
        }
        File bFile = (File) source;
        this.project = project;
        this.buildFile = new File(bFile.getAbsolutePath());
        this.buildFileParent = new File(this.buildFile.getParent());
        try {
            this.parser = JAXPUtils.getParser();
        } catch (BuildException e) {
            this.parser = new XMLReaderAdapter(JAXPUtils.getXMLReader());
        }
        try {
            InputStream inputStream = Files.newInputStream(bFile.toPath(), new OpenOption[0]);
            try {
                String uri = FILE_UTILS.toURI(bFile.getAbsolutePath());
                InputSource inputSource = new InputSource(inputStream);
                inputSource.setSystemId(uri);
                project.log("parsing buildfile " + bFile + " with URI = " + uri, 3);
                HandlerBase hb = new RootHandler(this);
                this.parser.setDocumentHandler(hb);
                this.parser.setEntityResolver(hb);
                this.parser.setErrorHandler(hb);
                this.parser.setDTDHandler(hb);
                this.parser.parse(inputSource);
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException exc) {
            throw new BuildException(exc);
        } catch (UnsupportedEncodingException exc2) {
            throw new BuildException("Encoding of project file is invalid.", exc2);
        } catch (IOException exc3) {
            throw new BuildException("Error reading project file: " + exc3.getMessage(), exc3);
        } catch (SAXParseException exc4) {
            Location location = new Location(exc4.getSystemId(), exc4.getLineNumber(), exc4.getColumnNumber());
            Throwable t = exc4.getException();
            if (t instanceof BuildException) {
                BuildException be = (BuildException) t;
                if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                    be.setLocation(location);
                }
                throw be;
            }
            throw new BuildException(exc4.getMessage(), t, location);
        } catch (SAXException exc5) {
            Throwable t2 = exc5.getException();
            if (t2 instanceof BuildException) {
                throw ((BuildException) t2);
            }
            throw new BuildException(exc5.getMessage(), t2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$AbstractHandler.class */
    public static class AbstractHandler extends HandlerBase {
        protected DocumentHandler parentHandler;
        ProjectHelperImpl helperImpl;

        public AbstractHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
            this.parentHandler = parentHandler;
            this.helperImpl = helperImpl;
            helperImpl.parser.setDocumentHandler(this);
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String tag, AttributeList attrs) throws SAXParseException {
            throw new SAXParseException("Unexpected element \"" + tag + "\"", this.helperImpl.locator);
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void characters(char[] buf, int start, int count) throws SAXParseException {
            String s = new String(buf, start, count).trim();
            if (!s.isEmpty()) {
                throw new SAXParseException("Unexpected text \"" + s + "\"", this.helperImpl.locator);
            }
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void endElement(String name) throws SAXException {
            this.helperImpl.parser.setDocumentHandler(this.parentHandler);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$RootHandler.class */
    static class RootHandler extends HandlerBase {
        ProjectHelperImpl helperImpl;

        public RootHandler(ProjectHelperImpl helperImpl) {
            this.helperImpl = helperImpl;
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) {
            this.helperImpl.project.log("resolving systemId: " + systemId, 3);
            if (systemId.startsWith("file:")) {
                String path = ProjectHelperImpl.FILE_UTILS.fromURI(systemId);
                File file = new File(path);
                if (!file.isAbsolute()) {
                    file = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, path);
                    this.helperImpl.project.log("Warning: '" + systemId + "' in " + this.helperImpl.buildFile + " should be expressed simply as '" + path.replace('\\', '/') + "' for compliance with other XML tools", 1);
                }
                try {
                    InputSource inputSource = new InputSource(Files.newInputStream(file.toPath(), new OpenOption[0]));
                    inputSource.setSystemId(ProjectHelperImpl.FILE_UTILS.toURI(file.getAbsolutePath()));
                    return inputSource;
                } catch (IOException e) {
                    this.helperImpl.project.log(file.getAbsolutePath() + " could not be found", 1);
                    return null;
                }
            }
            return null;
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String tag, AttributeList attrs) throws SAXParseException {
            if ("project".equals(tag)) {
                new ProjectHandler(this.helperImpl, this).init(tag, attrs);
                return;
            }
            throw new SAXParseException("Config file is not of expected XML type", this.helperImpl.locator);
        }

        @Override // org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void setDocumentLocator(Locator locator) {
            this.helperImpl.locator = locator;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$ProjectHandler.class */
    static class ProjectHandler extends AbstractHandler {
        public ProjectHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
            super(helperImpl, parentHandler);
        }

        public void init(String tag, AttributeList attrs) throws SAXParseException {
            String def = null;
            String name = null;
            String id = null;
            String baseDir = null;
            for (int i = 0; i < attrs.getLength(); i++) {
                String key = attrs.getName(i);
                String value = attrs.getValue(i);
                boolean z = true;
                switch (key.hashCode()) {
                    case -332611556:
                        if (key.equals(MagicNames.PROJECT_BASEDIR)) {
                            z = true;
                            break;
                        }
                        break;
                    case 3355:
                        if (key.equals("id")) {
                            z = true;
                            break;
                        }
                        break;
                    case 3373707:
                        if (key.equals("name")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1544803905:
                        if (key.equals("default")) {
                            z = false;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        def = value;
                        break;
                    case true:
                        name = value;
                        break;
                    case true:
                        id = value;
                        break;
                    case true:
                        baseDir = value;
                        break;
                    default:
                        throw new SAXParseException("Unexpected attribute \"" + key + "\"", this.helperImpl.locator);
                }
            }
            if (def != null && !def.isEmpty()) {
                this.helperImpl.project.setDefault(def);
                if (name != null) {
                    this.helperImpl.project.setName(name);
                    this.helperImpl.project.addReference(name, this.helperImpl.project);
                }
                if (id != null) {
                    this.helperImpl.project.addReference(id, this.helperImpl.project);
                }
                if (this.helperImpl.project.getProperty(MagicNames.PROJECT_BASEDIR) != null) {
                    this.helperImpl.project.setBasedir(this.helperImpl.project.getProperty(MagicNames.PROJECT_BASEDIR));
                } else if (baseDir == null) {
                    this.helperImpl.project.setBasedir(this.helperImpl.buildFileParent.getAbsolutePath());
                } else if (new File(baseDir).isAbsolute()) {
                    this.helperImpl.project.setBasedir(baseDir);
                } else {
                    File resolvedBaseDir = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, baseDir);
                    this.helperImpl.project.setBaseDir(resolvedBaseDir);
                }
                this.helperImpl.project.addTarget("", this.helperImpl.implicitTarget);
                return;
            }
            throw new BuildException("The default attribute is required");
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String name, AttributeList attrs) throws SAXParseException {
            if (!TypeProxy.INSTANCE_FIELD.equals(name)) {
                ProjectHelperImpl.handleElement(this.helperImpl, this, this.helperImpl.implicitTarget, name, attrs);
            } else {
                handleTarget(name, attrs);
            }
        }

        private void handleTarget(String tag, AttributeList attrs) throws SAXParseException {
            new TargetHandler(this.helperImpl, this).init(tag, attrs);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$TargetHandler.class */
    public static class TargetHandler extends AbstractHandler {
        private Target target;

        public TargetHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
            super(helperImpl, parentHandler);
        }

        public void init(String tag, AttributeList attrs) throws SAXParseException {
            String name = null;
            String depends = "";
            String ifCond = null;
            String unlessCond = null;
            String id = null;
            String description = null;
            for (int i = 0; i < attrs.getLength(); i++) {
                String key = attrs.getName(i);
                String value = attrs.getValue(i);
                boolean z = true;
                switch (key.hashCode()) {
                    case -1724546052:
                        if (key.equals("description")) {
                            z = true;
                            break;
                        }
                        break;
                    case -840451150:
                        if (key.equals("unless")) {
                            z = true;
                            break;
                        }
                        break;
                    case 3355:
                        if (key.equals("id")) {
                            z = true;
                            break;
                        }
                        break;
                    case 3357:
                        if (key.equals(Jimple.IF)) {
                            z = true;
                            break;
                        }
                        break;
                    case 3373707:
                        if (key.equals("name")) {
                            z = false;
                            break;
                        }
                        break;
                    case 1554151303:
                        if (key.equals("depends")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        name = value;
                        if (name.isEmpty()) {
                            throw new BuildException("name attribute must not be empty", new Location(this.helperImpl.locator));
                        }
                        break;
                    case true:
                        depends = value;
                        break;
                    case true:
                        ifCond = value;
                        break;
                    case true:
                        unlessCond = value;
                        break;
                    case true:
                        id = value;
                        break;
                    case true:
                        description = value;
                        break;
                    default:
                        throw new SAXParseException("Unexpected attribute \"" + key + "\"", this.helperImpl.locator);
                }
            }
            if (name == null) {
                throw new SAXParseException("target element appears without a name attribute", this.helperImpl.locator);
            }
            this.target = new Target();
            this.target.addDependency("");
            this.target.setName(name);
            this.target.setIf(ifCond);
            this.target.setUnless(unlessCond);
            this.target.setDescription(description);
            this.helperImpl.project.addTarget(name, this.target);
            if (id != null && !id.isEmpty()) {
                this.helperImpl.project.addReference(id, this.target);
            }
            if (!depends.isEmpty()) {
                this.target.setDepends(depends);
            }
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String name, AttributeList attrs) throws SAXParseException {
            ProjectHelperImpl.handleElement(this.helperImpl, this, this.target, name, attrs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleElement(ProjectHelperImpl helperImpl, DocumentHandler parent, Target target, String elementName, AttributeList attrs) throws SAXParseException {
        if ("description".equals(elementName)) {
            new DescriptionHandler(helperImpl, parent);
        } else if (helperImpl.project.getDataTypeDefinitions().get(elementName) != null) {
            new DataTypeHandler(helperImpl, parent, target).init(elementName, attrs);
        } else {
            new TaskHandler(helperImpl, parent, target, null, target).init(elementName, attrs);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$DescriptionHandler.class */
    public static class DescriptionHandler extends AbstractHandler {
        public DescriptionHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
            super(helperImpl, parentHandler);
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void characters(char[] buf, int start, int count) {
            String text = new String(buf, start, count);
            String currentDescription = this.helperImpl.project.getDescription();
            if (currentDescription == null) {
                this.helperImpl.project.setDescription(text);
            } else {
                this.helperImpl.project.setDescription(currentDescription + text);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$TaskHandler.class */
    public static class TaskHandler extends AbstractHandler {
        private Target target;
        private TaskContainer container;
        private Task task;
        private RuntimeConfigurable parentWrapper;
        private RuntimeConfigurable wrapper;

        public TaskHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, TaskContainer container, RuntimeConfigurable parentWrapper, Target target) {
            super(helperImpl, parentHandler);
            this.wrapper = null;
            this.container = container;
            this.parentWrapper = parentWrapper;
            this.target = target;
        }

        public void init(String tag, AttributeList attrs) throws SAXParseException {
            try {
                this.task = this.helperImpl.project.createTask(tag);
            } catch (BuildException e) {
            }
            if (this.task == null) {
                this.task = new UnknownElement(tag);
                this.task.setProject(this.helperImpl.project);
                this.task.setTaskName(tag);
            }
            this.task.setLocation(new Location(this.helperImpl.locator));
            this.helperImpl.configureId(this.task, attrs);
            this.task.setOwningTarget(this.target);
            this.container.addTask(this.task);
            this.task.init();
            this.wrapper = this.task.getRuntimeConfigurableWrapper();
            this.wrapper.setAttributes(attrs);
            if (this.parentWrapper != null) {
                this.parentWrapper.addChild(this.wrapper);
            }
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void characters(char[] buf, int start, int count) {
            this.wrapper.addText(buf, start, count);
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String name, AttributeList attrs) throws SAXParseException {
            if (this.task instanceof TaskContainer) {
                new TaskHandler(this.helperImpl, this, (TaskContainer) this.task, this.wrapper, this.target).init(name, attrs);
            } else {
                new NestedElementHandler(this.helperImpl, this, this.task, this.wrapper, this.target).init(name, attrs);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$NestedElementHandler.class */
    static class NestedElementHandler extends AbstractHandler {
        private Object parent;
        private Object child;
        private RuntimeConfigurable parentWrapper;
        private RuntimeConfigurable childWrapper;
        private Target target;

        public NestedElementHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, Object parent, RuntimeConfigurable parentWrapper, Target target) {
            super(helperImpl, parentHandler);
            this.childWrapper = null;
            if (parent instanceof TypeAdapter) {
                this.parent = ((TypeAdapter) parent).getProxy();
            } else {
                this.parent = parent;
            }
            this.parentWrapper = parentWrapper;
            this.target = target;
        }

        public void init(String propType, AttributeList attrs) throws SAXParseException {
            Class<?> parentClass = this.parent.getClass();
            IntrospectionHelper ih = IntrospectionHelper.getHelper(this.helperImpl.project, parentClass);
            try {
                String elementName = propType.toLowerCase(Locale.ENGLISH);
                if (this.parent instanceof UnknownElement) {
                    UnknownElement uc = new UnknownElement(elementName);
                    uc.setProject(this.helperImpl.project);
                    ((UnknownElement) this.parent).addChild(uc);
                    this.child = uc;
                } else {
                    this.child = ih.createElement(this.helperImpl.project, this.parent, elementName);
                }
                this.helperImpl.configureId(this.child, attrs);
                this.childWrapper = new RuntimeConfigurable(this.child, propType);
                this.childWrapper.setAttributes(attrs);
                this.parentWrapper.addChild(this.childWrapper);
            } catch (BuildException exc) {
                throw new SAXParseException(exc.getMessage(), this.helperImpl.locator, exc);
            }
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void characters(char[] buf, int start, int count) {
            this.childWrapper.addText(buf, start, count);
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String name, AttributeList attrs) throws SAXParseException {
            if (this.child instanceof TaskContainer) {
                new TaskHandler(this.helperImpl, this, (TaskContainer) this.child, this.childWrapper, this.target).init(name, attrs);
            } else {
                new NestedElementHandler(this.helperImpl, this, this.child, this.childWrapper, this.target).init(name, attrs);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/helper/ProjectHelperImpl$DataTypeHandler.class */
    public static class DataTypeHandler extends AbstractHandler {
        private Target target;
        private Object element;
        private RuntimeConfigurable wrapper;

        public DataTypeHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, Target target) {
            super(helperImpl, parentHandler);
            this.wrapper = null;
            this.target = target;
        }

        public void init(String propType, AttributeList attrs) throws SAXParseException {
            try {
                this.element = this.helperImpl.project.createDataType(propType);
                if (this.element == null) {
                    throw new BuildException("Unknown data type " + propType);
                }
                this.wrapper = new RuntimeConfigurable(this.element, propType);
                this.wrapper.setAttributes(attrs);
                this.target.addDataType(this.wrapper);
            } catch (BuildException exc) {
                throw new SAXParseException(exc.getMessage(), this.helperImpl.locator, exc);
            }
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void characters(char[] buf, int start, int count) {
            this.wrapper.addText(buf, start, count);
        }

        @Override // org.apache.tools.ant.helper.ProjectHelperImpl.AbstractHandler, org.xml.sax.HandlerBase, org.xml.sax.DocumentHandler
        public void startElement(String name, AttributeList attrs) throws SAXParseException {
            new NestedElementHandler(this.helperImpl, this, this.element, this.wrapper, this.target).init(name, attrs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void configureId(Object target, AttributeList attr) {
        String id = attr.getValue("id");
        if (id != null) {
            this.project.addReference(id, target);
        }
    }
}
