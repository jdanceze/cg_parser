package com.sun.xml.bind.v2.schemagen;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.bind.api.CompositeStructure;
import com.sun.xml.bind.api.ErrorListener;
import com.sun.xml.bind.v2.TODO;
import com.sun.xml.bind.v2.WellKnownNamespace;
import com.sun.xml.bind.v2.model.core.Adapter;
import com.sun.xml.bind.v2.model.core.ArrayInfo;
import com.sun.xml.bind.v2.model.core.AttributePropertyInfo;
import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.model.core.Element;
import com.sun.xml.bind.v2.model.core.ElementInfo;
import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.bind.v2.model.core.EnumConstant;
import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
import com.sun.xml.bind.v2.model.core.MapPropertyInfo;
import com.sun.xml.bind.v2.model.core.MaybeElement;
import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.model.core.NonElementRef;
import com.sun.xml.bind.v2.model.core.PropertyInfo;
import com.sun.xml.bind.v2.model.core.ReferencePropertyInfo;
import com.sun.xml.bind.v2.model.core.TypeInfo;
import com.sun.xml.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.bind.v2.model.core.TypeRef;
import com.sun.xml.bind.v2.model.core.ValuePropertyInfo;
import com.sun.xml.bind.v2.model.core.WildcardMode;
import com.sun.xml.bind.v2.model.impl.ClassInfoImpl;
import com.sun.xml.bind.v2.model.nav.Navigator;
import com.sun.xml.bind.v2.runtime.SwaRefAdapter;
import com.sun.xml.bind.v2.schemagen.Tree;
import com.sun.xml.bind.v2.schemagen.episode.Bindings;
import com.sun.xml.bind.v2.schemagen.xmlschema.Any;
import com.sun.xml.bind.v2.schemagen.xmlschema.AttrDecls;
import com.sun.xml.bind.v2.schemagen.xmlschema.AttributeType;
import com.sun.xml.bind.v2.schemagen.xmlschema.ComplexExtension;
import com.sun.xml.bind.v2.schemagen.xmlschema.ComplexType;
import com.sun.xml.bind.v2.schemagen.xmlschema.ComplexTypeHost;
import com.sun.xml.bind.v2.schemagen.xmlschema.ContentModelContainer;
import com.sun.xml.bind.v2.schemagen.xmlschema.ExplicitGroup;
import com.sun.xml.bind.v2.schemagen.xmlschema.Import;
import com.sun.xml.bind.v2.schemagen.xmlschema.LocalAttribute;
import com.sun.xml.bind.v2.schemagen.xmlschema.LocalElement;
import com.sun.xml.bind.v2.schemagen.xmlschema.Schema;
import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleExtension;
import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleRestrictionModel;
import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleType;
import com.sun.xml.bind.v2.schemagen.xmlschema.SimpleTypeHost;
import com.sun.xml.bind.v2.schemagen.xmlschema.TopLevelAttribute;
import com.sun.xml.bind.v2.schemagen.xmlschema.TopLevelElement;
import com.sun.xml.bind.v2.schemagen.xmlschema.TypeDefParticle;
import com.sun.xml.bind.v2.schemagen.xmlschema.TypeHost;
import com.sun.xml.bind.v2.util.CollisionCheckStack;
import com.sun.xml.bind.v2.util.StackRecorder;
import com.sun.xml.txw2.TXW;
import com.sun.xml.txw2.TxwException;
import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.output.ResultFactory;
import com.sun.xml.txw2.output.XmlSerializer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimeType;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.xml.sax.SAXParseException;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/XmlSchemaGenerator.class */
public final class XmlSchemaGenerator<T, C, F, M> {
    private static final Logger logger;
    private ErrorListener errorListener;
    private Navigator<T, C, F, M> navigator;
    private final TypeInfoSet<T, C, F, M> types;
    private final NonElement<T, C> stringType;
    private final NonElement<T, C> anyType;
    private static final Comparator<String> NAMESPACE_COMPARATOR;
    private static final String newline = "\n";
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Map<String, XmlSchemaGenerator<T, C, F, M>.Namespace> namespaces = new TreeMap(NAMESPACE_COMPARATOR);
    private final CollisionCheckStack<ClassInfo<T, C>> collisionChecker = new CollisionCheckStack<>();

    static {
        $assertionsDisabled = !XmlSchemaGenerator.class.desiredAssertionStatus();
        logger = com.sun.xml.bind.Util.getClassLogger();
        NAMESPACE_COMPARATOR = new Comparator<String>() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.1
            @Override // java.util.Comparator
            public int compare(String lhs, String rhs) {
                return -lhs.compareTo(rhs);
            }
        };
    }

    public XmlSchemaGenerator(Navigator<T, C, F, M> navigator, TypeInfoSet<T, C, F, M> types) {
        this.navigator = navigator;
        this.types = types;
        this.stringType = types.getTypeInfo((TypeInfoSet<T, C, F, M>) navigator.ref(String.class));
        this.anyType = types.getAnyTypeInfo();
        for (ClassInfo<T, C> ci : types.beans().values()) {
            add(ci);
        }
        for (ElementInfo<T, C> ei1 : types.getElementMappings(null).values()) {
            add(ei1);
        }
        for (EnumLeafInfo<T, C> ei : types.enums().values()) {
            add(ei);
        }
        for (ArrayInfo<T, C> a : types.arrays().values()) {
            add(a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public XmlSchemaGenerator<T, C, F, M>.Namespace getNamespace(String uri) {
        XmlSchemaGenerator<T, C, F, M>.Namespace n = this.namespaces.get(uri);
        if (n == null) {
            Map<String, XmlSchemaGenerator<T, C, F, M>.Namespace> map = this.namespaces;
            XmlSchemaGenerator<T, C, F, M>.Namespace namespace = new Namespace(uri);
            n = namespace;
            map.put(uri, namespace);
        }
        return n;
    }

    public void add(ClassInfo<T, C> clazz) {
        if (!$assertionsDisabled && clazz == null) {
            throw new AssertionError();
        }
        String nsUri = null;
        if (clazz.getClazz() == this.navigator.asDecl(CompositeStructure.class)) {
            return;
        }
        if (clazz.isElement()) {
            nsUri = clazz.getElementName().getNamespaceURI();
            XmlSchemaGenerator<T, C, F, M>.Namespace ns = getNamespace(nsUri);
            ((Namespace) ns).classes.add(clazz);
            ns.addDependencyTo(clazz.getTypeName());
            add(clazz.getElementName(), false, clazz);
        }
        QName tn = clazz.getTypeName();
        if (tn != null) {
            nsUri = tn.getNamespaceURI();
        } else if (nsUri == null) {
            return;
        }
        XmlSchemaGenerator<T, C, F, M>.Namespace n = getNamespace(nsUri);
        ((Namespace) n).classes.add(clazz);
        for (PropertyInfo<T, C> p : clazz.getProperties()) {
            n.processForeignNamespaces(p, 1);
            if (p instanceof AttributePropertyInfo) {
                AttributePropertyInfo<T, C> ap = (AttributePropertyInfo) p;
                String aUri = ap.getXmlName().getNamespaceURI();
                if (aUri.length() > 0) {
                    getNamespace(aUri).addGlobalAttribute(ap);
                    n.addDependencyTo(ap.getXmlName());
                }
            }
            if (p instanceof ElementPropertyInfo) {
                ElementPropertyInfo<T, C> ep = (ElementPropertyInfo) p;
                for (TypeRef<T, C> tref : ep.getTypes()) {
                    String eUri = tref.getTagName().getNamespaceURI();
                    if (eUri.length() > 0 && !eUri.equals(n.uri)) {
                        getNamespace(eUri).addGlobalElement(tref);
                        n.addDependencyTo(tref.getTagName());
                    }
                }
            }
            if (generateSwaRefAdapter(p)) {
                ((Namespace) n).useSwaRef = true;
            }
            MimeType mimeType = p.getExpectedMimeType();
            if (mimeType != null) {
                ((Namespace) n).useMimeNs = true;
            }
        }
        ClassInfo<T, C> bc = clazz.getBaseClass();
        if (bc != null) {
            add(bc);
            n.addDependencyTo(bc.getTypeName());
        }
    }

    public void add(ElementInfo<T, C> elem) {
        ElementInfo ei;
        boolean nillable;
        if (!$assertionsDisabled && elem == null) {
            throw new AssertionError();
        }
        QName name = elem.getElementName();
        XmlSchemaGenerator<T, C, F, M>.Namespace n = getNamespace(name.getNamespaceURI());
        if (elem.getScope() != null) {
            ei = this.types.getElementInfo(elem.getScope().getClazz(), name);
        } else {
            ei = this.types.getElementInfo(null, name);
        }
        XmlElement xmlElem = (XmlElement) ei.getProperty().readAnnotation(XmlElement.class);
        if (xmlElem == null) {
            nillable = false;
        } else {
            nillable = xmlElem.nillable();
        }
        MultiMap multiMap = ((Namespace) n).elementDecls;
        String localPart = name.getLocalPart();
        Objects.requireNonNull(n);
        multiMap.put((MultiMap) localPart, (String) new Namespace.ElementWithType(nillable, elem.getContentType()));
        n.processForeignNamespaces(elem.getProperty(), 1);
    }

    public void add(EnumLeafInfo<T, C> envm) {
        if (!$assertionsDisabled && envm == null) {
            throw new AssertionError();
        }
        String nsUri = null;
        if (envm.isElement()) {
            nsUri = envm.getElementName().getNamespaceURI();
            XmlSchemaGenerator<T, C, F, M>.Namespace ns = getNamespace(nsUri);
            ((Namespace) ns).enums.add(envm);
            ns.addDependencyTo(envm.getTypeName());
            add(envm.getElementName(), false, envm);
        }
        QName typeName = envm.getTypeName();
        if (typeName != null) {
            nsUri = typeName.getNamespaceURI();
        } else if (nsUri == null) {
            return;
        }
        XmlSchemaGenerator<T, C, F, M>.Namespace n = getNamespace(nsUri);
        ((Namespace) n).enums.add(envm);
        n.addDependencyTo(envm.getBaseType().getTypeName());
    }

    public void add(ArrayInfo<T, C> a) {
        if (!$assertionsDisabled && a == null) {
            throw new AssertionError();
        }
        String namespaceURI = a.getTypeName().getNamespaceURI();
        XmlSchemaGenerator<T, C, F, M>.Namespace n = getNamespace(namespaceURI);
        ((Namespace) n).arrays.add(a);
        n.addDependencyTo(a.getItemType().getTypeName());
    }

    public void add(QName tagName, boolean isNillable, NonElement<T, C> type) {
        if (type != null && type.getType() == this.navigator.ref(CompositeStructure.class)) {
            return;
        }
        XmlSchemaGenerator<T, C, F, M>.Namespace n = getNamespace(tagName.getNamespaceURI());
        MultiMap multiMap = ((Namespace) n).elementDecls;
        String localPart = tagName.getLocalPart();
        Objects.requireNonNull(n);
        multiMap.put((MultiMap) localPart, (String) new Namespace.ElementWithType(isNillable, type));
        if (type == null) {
            return;
        }
        n.addDependencyTo(type.getTypeName());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void writeEpisodeFile(XmlSerializer out) {
        String prefix;
        Bindings root = (Bindings) TXW.create(Bindings.class, out);
        if (this.namespaces.containsKey("")) {
            root._namespace(WellKnownNamespace.JAXB, "jaxb");
        }
        root.version("2.1");
        for (Map.Entry<String, XmlSchemaGenerator<T, C, F, M>.Namespace> e : this.namespaces.entrySet()) {
            Bindings group = root.bindings();
            String tns = e.getKey();
            if (!tns.equals("")) {
                group._namespace(tns, "tns");
                prefix = "tns:";
            } else {
                prefix = "";
            }
            group.scd("x-schema::" + (tns.equals("") ? "" : "tns"));
            group.schemaBindings().map(false);
            for (ClassInfo<T, C> ci : ((Namespace) e.getValue()).classes) {
                if (ci.getTypeName() != null) {
                    if (ci.getTypeName().getNamespaceURI().equals(tns)) {
                        Bindings child = group.bindings();
                        child.scd('~' + prefix + ci.getTypeName().getLocalPart());
                        child.klass().ref(ci.getName());
                    }
                    if (ci.isElement() && ci.getElementName().getNamespaceURI().equals(tns)) {
                        Bindings child2 = group.bindings();
                        child2.scd(prefix + ci.getElementName().getLocalPart());
                        child2.klass().ref(ci.getName());
                    }
                }
            }
            for (EnumLeafInfo<T, C> en : ((Namespace) e.getValue()).enums) {
                if (en.getTypeName() != null) {
                    Bindings child3 = group.bindings();
                    child3.scd('~' + prefix + en.getTypeName().getLocalPart());
                    child3.klass().ref(this.navigator.getClassName(en.getClazz()));
                }
            }
            group.commit(true);
        }
        root.commit();
    }

    public void write(SchemaOutputResolver resolver, ErrorListener errorListener) throws IOException {
        if (resolver == null) {
            throw new IllegalArgumentException();
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Writing XML Schema for " + toString(), (Throwable) new StackRecorder());
        }
        SchemaOutputResolver resolver2 = new FoolProofResolver(resolver);
        this.errorListener = errorListener;
        Map<String, String> schemaLocations = this.types.getSchemaLocations();
        Map<XmlSchemaGenerator<T, C, F, M>.Namespace, Result> out = new HashMap<>();
        Map<XmlSchemaGenerator<T, C, F, M>.Namespace, String> systemIds = new HashMap<>();
        this.namespaces.remove("http://www.w3.org/2001/XMLSchema");
        for (XmlSchemaGenerator<T, C, F, M>.Namespace n : this.namespaces.values()) {
            String schemaLocation = schemaLocations.get(n.uri);
            if (schemaLocation != null) {
                systemIds.put(n, schemaLocation);
            } else {
                Result output = resolver2.createOutput(n.uri, "schema" + (out.size() + 1) + ".xsd");
                if (output != null) {
                    out.put(n, output);
                    systemIds.put(n, output.getSystemId());
                }
            }
            n.resetWritten();
        }
        for (Map.Entry<XmlSchemaGenerator<T, C, F, M>.Namespace, Result> e : out.entrySet()) {
            Result result = e.getValue();
            e.getKey().writeTo(result, systemIds);
            if (result instanceof StreamResult) {
                OutputStream outputStream = ((StreamResult) result).getOutputStream();
                if (outputStream != null) {
                    outputStream.close();
                } else {
                    Writer writer = ((StreamResult) result).getWriter();
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/XmlSchemaGenerator$Namespace.class */
    public class Namespace {
        @NotNull
        final String uri;
        private boolean selfReference;
        private final MultiMap<String, XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration> elementDecls;
        private Form attributeFormDefault;
        private Form elementFormDefault;
        private boolean useSwaRef;
        private boolean useMimeNs;
        static final /* synthetic */ boolean $assertionsDisabled;
        private final Set<XmlSchemaGenerator<T, C, F, M>.Namespace> depends = new LinkedHashSet();
        private final Set<ClassInfo<T, C>> classes = new LinkedHashSet();
        private final Set<EnumLeafInfo<T, C>> enums = new LinkedHashSet();
        private final Set<ArrayInfo<T, C>> arrays = new LinkedHashSet();
        private final MultiMap<String, AttributePropertyInfo<T, C>> attributeDecls = new MultiMap<>(null);
        private final Set<ClassInfo> written = new HashSet();

        static {
            $assertionsDisabled = !XmlSchemaGenerator.class.desiredAssertionStatus();
        }

        public Namespace(String uri) {
            this.elementDecls = new MultiMap<>(new ElementWithType(true, XmlSchemaGenerator.this.anyType));
            this.uri = uri;
            if (!$assertionsDisabled && XmlSchemaGenerator.this.namespaces.containsKey(uri)) {
                throw new AssertionError();
            }
            XmlSchemaGenerator.this.namespaces.put(uri, this);
        }

        void resetWritten() {
            this.written.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processForeignNamespaces(PropertyInfo<T, C> p, int processingDepth) {
            for (TypeInfo<T, C> t : p.ref()) {
                if ((t instanceof ClassInfo) && processingDepth > 0) {
                    List<PropertyInfo> l = ((ClassInfo) t).getProperties();
                    for (PropertyInfo subp : l) {
                        processingDepth--;
                        processForeignNamespaces(subp, processingDepth);
                    }
                }
                if (t instanceof Element) {
                    addDependencyTo(((Element) t).getElementName());
                }
                if (t instanceof NonElement) {
                    addDependencyTo(((NonElement) t).getTypeName());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addDependencyTo(@Nullable QName qname) {
            if (qname == null) {
                return;
            }
            String nsUri = qname.getNamespaceURI();
            if (nsUri.equals("http://www.w3.org/2001/XMLSchema")) {
                return;
            }
            if (!nsUri.equals(this.uri)) {
                this.depends.add(XmlSchemaGenerator.this.getNamespace(nsUri));
            } else {
                this.selfReference = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTo(Result result, Map<XmlSchemaGenerator<T, C, F, M>.Namespace, String> systemIds) throws IOException {
            try {
                Schema schema = (Schema) TXW.create(Schema.class, ResultFactory.createSerializer(result));
                Map<String, String> xmlNs = XmlSchemaGenerator.this.types.getXmlNs(this.uri);
                for (Map.Entry<String, String> e : xmlNs.entrySet()) {
                    schema._namespace(e.getValue(), e.getKey());
                }
                if (this.useSwaRef) {
                    schema._namespace(WellKnownNamespace.SWA_URI, "swaRef");
                }
                if (this.useMimeNs) {
                    schema._namespace(WellKnownNamespace.XML_MIME_URI, "xmime");
                }
                this.attributeFormDefault = Form.get(XmlSchemaGenerator.this.types.getAttributeFormDefault(this.uri));
                this.attributeFormDefault.declare("attributeFormDefault", schema);
                this.elementFormDefault = Form.get(XmlSchemaGenerator.this.types.getElementFormDefault(this.uri));
                this.elementFormDefault.declare("elementFormDefault", schema);
                if (!xmlNs.containsValue("http://www.w3.org/2001/XMLSchema") && !xmlNs.containsKey("xs")) {
                    schema._namespace("http://www.w3.org/2001/XMLSchema", "xs");
                }
                schema.version("1.0");
                if (this.uri.length() != 0) {
                    schema.targetNamespace(this.uri);
                }
                for (XmlSchemaGenerator<T, C, F, M>.Namespace ns : this.depends) {
                    schema._namespace(ns.uri);
                }
                if (this.selfReference && this.uri.length() != 0) {
                    schema._namespace(this.uri, "tns");
                }
                schema._pcdata("\n");
                for (XmlSchemaGenerator<T, C, F, M>.Namespace n : this.depends) {
                    Import imp = schema._import();
                    if (n.uri.length() != 0) {
                        imp.namespace(n.uri);
                    }
                    String refSystemId = systemIds.get(n);
                    if (refSystemId != null && !refSystemId.equals("")) {
                        imp.schemaLocation(XmlSchemaGenerator.relativize(refSystemId, result.getSystemId()));
                    }
                    schema._pcdata("\n");
                }
                if (this.useSwaRef) {
                    schema._import().namespace(WellKnownNamespace.SWA_URI).schemaLocation("http://ws-i.org/profiles/basic/1.1/swaref.xsd");
                }
                if (this.useMimeNs) {
                    schema._import().namespace(WellKnownNamespace.XML_MIME_URI).schemaLocation(WellKnownNamespace.XML_MIME_URI);
                }
                Iterator it = this.elementDecls.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration> e2 = (Map.Entry) it.next();
                    e2.getValue().writeTo(e2.getKey(), schema);
                    schema._pcdata("\n");
                }
                for (ClassInfo<T, C> c : this.classes) {
                    if (c.getTypeName() != null) {
                        if (this.uri.equals(c.getTypeName().getNamespaceURI())) {
                            writeClass(c, schema);
                        }
                        schema._pcdata("\n");
                    }
                }
                for (EnumLeafInfo<T, C> e3 : this.enums) {
                    if (e3.getTypeName() != null) {
                        if (this.uri.equals(e3.getTypeName().getNamespaceURI())) {
                            writeEnum(e3, schema);
                        }
                        schema._pcdata("\n");
                    }
                }
                for (ArrayInfo<T, C> a : this.arrays) {
                    writeArray(a, schema);
                    schema._pcdata("\n");
                }
                Iterator it2 = this.attributeDecls.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry<String, AttributePropertyInfo<T, C>> e4 = (Map.Entry) it2.next();
                    TopLevelAttribute a2 = schema.attribute();
                    a2.name(e4.getKey());
                    if (e4.getValue() == null) {
                        writeTypeRef(a2, XmlSchemaGenerator.this.stringType, "type");
                    } else {
                        writeAttributeTypeRef(e4.getValue(), a2);
                    }
                    schema._pcdata("\n");
                }
                schema.commit();
            } catch (TxwException e5) {
                XmlSchemaGenerator.logger.log(Level.INFO, e5.getMessage(), (Throwable) e5);
                throw new IOException(e5.getMessage());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTypeRef(TypeHost th, NonElementRef<T, C> typeRef, String refAttName) {
            switch (typeRef.getSource().id()) {
                case ID:
                    th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", "ID"));
                    return;
                case IDREF:
                    th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
                    return;
                case NONE:
                    MimeType mimeType = typeRef.getSource().getExpectedMimeType();
                    if (mimeType != null) {
                        th._attribute(new QName(WellKnownNamespace.XML_MIME_URI, "expectedContentTypes", "xmime"), mimeType.toString());
                    }
                    if (XmlSchemaGenerator.this.generateSwaRefAdapter(typeRef)) {
                        th._attribute(refAttName, new QName(WellKnownNamespace.SWA_URI, "swaRef", "ref"));
                        return;
                    } else if (typeRef.getSource().getSchemaType() != null) {
                        th._attribute(refAttName, typeRef.getSource().getSchemaType());
                        return;
                    } else {
                        writeTypeRef(th, typeRef.getTarget(), refAttName);
                        return;
                    }
                default:
                    throw new IllegalStateException();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeTypeRef(TypeHost th, NonElement<T, C> type, String refAttName) {
            Element e = null;
            if (type instanceof MaybeElement) {
                MaybeElement me = (MaybeElement) type;
                boolean isElement = me.isElement();
                if (isElement) {
                    e = me.asElement();
                }
            }
            if (type instanceof Element) {
                e = (Element) type;
            }
            if (type.getTypeName() == null) {
                if (e != null && e.getElementName() != null) {
                    th.block();
                    if (type instanceof ClassInfo) {
                        writeClass((ClassInfo) type, th);
                        return;
                    } else {
                        writeEnum((EnumLeafInfo) type, (SimpleTypeHost) th);
                        return;
                    }
                }
                th.block();
                if (type instanceof ClassInfo) {
                    if (XmlSchemaGenerator.this.collisionChecker.push((ClassInfo) type)) {
                        XmlSchemaGenerator.this.errorListener.warning(new SAXParseException(Messages.ANONYMOUS_TYPE_CYCLE.format(XmlSchemaGenerator.this.collisionChecker.getCycleString()), null));
                    } else {
                        writeClass((ClassInfo) type, th);
                    }
                    XmlSchemaGenerator.this.collisionChecker.pop();
                    return;
                }
                writeEnum((EnumLeafInfo) type, (SimpleTypeHost) th);
                return;
            }
            th._attribute(refAttName, type.getTypeName());
        }

        private void writeArray(ArrayInfo<T, C> a, Schema schema) {
            ComplexType ct = schema.complexType().name(a.getTypeName().getLocalPart());
            ct._final("#all");
            LocalElement le = ct.sequence().element().name("item");
            le.type(a.getItemType().getTypeName());
            le.minOccurs(0).maxOccurs("unbounded");
            le.nillable(true);
            ct.commit();
        }

        private void writeEnum(EnumLeafInfo<T, C> e, SimpleTypeHost th) {
            SimpleType st = th.simpleType();
            writeName(e, st);
            SimpleRestrictionModel base = st.restriction();
            writeTypeRef(base, e.getBaseType(), XMLConstants.BASE_TAG);
            for (EnumConstant c : e.getConstants()) {
                base.enumeration().value(c.getLexicalValue());
            }
            st.commit();
        }

        private void writeClass(ClassInfo<T, C> c, TypeHost parent) {
            if (this.written.contains(c)) {
                return;
            }
            this.written.add(c);
            if (containsValueProp(c)) {
                if (c.getProperties().size() == 1) {
                    ValuePropertyInfo<T, C> vp = (ValuePropertyInfo) c.getProperties().get(0);
                    SimpleType st = ((SimpleTypeHost) parent).simpleType();
                    writeName(c, st);
                    if (vp.isCollection()) {
                        writeTypeRef(st.list(), vp.getTarget(), "itemType");
                        return;
                    } else {
                        writeTypeRef(st.restriction(), vp.getTarget(), XMLConstants.BASE_TAG);
                        return;
                    }
                }
                ComplexType ct = ((ComplexTypeHost) parent).complexType();
                writeName(c, ct);
                if (c.isFinal()) {
                    ct._final("extension restriction");
                }
                SimpleExtension se = ct.simpleContent().extension();
                se.block();
                for (PropertyInfo<T, C> p : c.getProperties()) {
                    switch (p.kind()) {
                        case ATTRIBUTE:
                            handleAttributeProp((AttributePropertyInfo) p, se);
                            break;
                        case VALUE:
                            TODO.checkSpec("what if vp.isCollection() == true?");
                            se.base(((ValuePropertyInfo) p).getTarget().getTypeName());
                            break;
                        case ELEMENT:
                        case REFERENCE:
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError();
                            }
                            throw new IllegalStateException();
                    }
                }
                se.commit();
                TODO.schemaGenerator("figure out what to do if bc != null");
                TODO.checkSpec("handle sec 8.9.5.2, bullet #4");
                return;
            }
            ComplexType ct2 = ((ComplexTypeHost) parent).complexType();
            writeName(c, ct2);
            if (c.isFinal()) {
                ct2._final("extension restriction");
            }
            if (c.isAbstract()) {
                ct2._abstract(true);
            }
            AttrDecls contentModel = ct2;
            TypeDefParticle contentModelOwner = ct2;
            ClassInfo<T, C> bc = c.getBaseClass();
            if (bc != null) {
                if (bc.hasValueProperty()) {
                    SimpleExtension se2 = ct2.simpleContent().extension();
                    contentModel = se2;
                    contentModelOwner = null;
                    se2.base(bc.getTypeName());
                } else {
                    ComplexExtension ce = ct2.complexContent().extension();
                    contentModel = ce;
                    contentModelOwner = ce;
                    ce.base(bc.getTypeName());
                }
            }
            if (contentModelOwner != null) {
                ArrayList<Tree> children = new ArrayList<>();
                for (PropertyInfo<T, C> p2 : c.getProperties()) {
                    if ((p2 instanceof ReferencePropertyInfo) && ((ReferencePropertyInfo) p2).isMixed()) {
                        ct2.mixed(true);
                    }
                    Tree t = buildPropertyContentModel(p2);
                    if (t != null) {
                        children.add(t);
                    }
                }
                Tree top = Tree.makeGroup(c.isOrdered() ? GroupKind.SEQUENCE : GroupKind.ALL, children);
                top.write(contentModelOwner);
            }
            for (PropertyInfo<T, C> p3 : c.getProperties()) {
                if (p3 instanceof AttributePropertyInfo) {
                    handleAttributeProp((AttributePropertyInfo) p3, contentModel);
                }
            }
            if (c.hasAttributeWildcard()) {
                contentModel.anyAttribute().namespace("##other").processContents(MSVSSConstants.WRITABLE_SKIP);
            }
            ct2.commit();
        }

        private void writeName(NonElement<T, C> c, TypedXmlWriter xw) {
            QName tn = c.getTypeName();
            if (tn != null) {
                xw._attribute("name", tn.getLocalPart());
            }
        }

        private boolean containsValueProp(ClassInfo<T, C> c) {
            for (PropertyInfo p : c.getProperties()) {
                if (p instanceof ValuePropertyInfo) {
                    return true;
                }
            }
            return false;
        }

        private Tree buildPropertyContentModel(PropertyInfo<T, C> p) {
            switch (p.kind()) {
                case ATTRIBUTE:
                    return null;
                case VALUE:
                    if ($assertionsDisabled) {
                        throw new IllegalStateException();
                    }
                    throw new AssertionError();
                case ELEMENT:
                    return handleElementProp((ElementPropertyInfo) p);
                case REFERENCE:
                    return handleReferenceProp((ReferencePropertyInfo) p);
                case MAP:
                    return handleMapProp((MapPropertyInfo) p);
                default:
                    if ($assertionsDisabled) {
                        throw new IllegalStateException();
                    }
                    throw new AssertionError();
            }
        }

        private Tree handleElementProp(final ElementPropertyInfo<T, C> ep) {
            if (ep.isValueList()) {
                return new Tree.Term() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.1
                    @Override // com.sun.xml.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        TypeRef<T, C> t = ep.getTypes().get(0);
                        LocalElement e = parent.element();
                        e.block();
                        QName tn = t.getTagName();
                        e.name(tn.getLocalPart());
                        com.sun.xml.bind.v2.schemagen.xmlschema.List lst = e.simpleType().list();
                        Namespace.this.writeTypeRef(lst, t, "itemType");
                        Namespace.this.elementFormDefault.writeForm(e, tn);
                        writeOccurs(e, isOptional || !ep.isRequired(), repeated);
                    }
                };
            }
            ArrayList<Tree> children = new ArrayList<>();
            for (final TypeRef<T, C> t : ep.getTypes()) {
                children.add(new Tree.Term() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.2
                    @Override // com.sun.xml.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        LocalElement e = parent.element();
                        QName tn = t.getTagName();
                        PropertyInfo propInfo = t.getSource();
                        TypeInfo parentInfo = propInfo == null ? null : propInfo.parent();
                        if (Namespace.this.canBeDirectElementRef(t, tn, parentInfo)) {
                            if (!t.getTarget().isSimpleType() && (t.getTarget() instanceof ClassInfo) && XmlSchemaGenerator.this.collisionChecker.findDuplicate((ClassInfo) t.getTarget())) {
                                e.ref(new QName(Namespace.this.uri, tn.getLocalPart()));
                            } else {
                                QName elemName = null;
                                if (t.getTarget() instanceof Element) {
                                    Element te = (Element) t.getTarget();
                                    elemName = te.getElementName();
                                }
                                Collection<TypeInfo> refs = propInfo.ref();
                                if (refs != null && !refs.isEmpty() && elemName != null) {
                                    ClassInfoImpl cImpl = null;
                                    Iterator<TypeInfo> it = refs.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        TypeInfo ref = it.next();
                                        if (ref == null || (ref instanceof ClassInfoImpl)) {
                                            if (elemName.equals(((ClassInfoImpl) ref).getElementName())) {
                                                cImpl = (ClassInfoImpl) ref;
                                                break;
                                            }
                                        }
                                    }
                                    if (cImpl != null) {
                                        if (tn.getNamespaceURI() != null && tn.getNamespaceURI().trim().length() != 0) {
                                            e.ref(new QName(tn.getNamespaceURI(), tn.getLocalPart()));
                                        } else {
                                            e.ref(new QName(cImpl.getElementName().getNamespaceURI(), tn.getLocalPart()));
                                        }
                                    } else {
                                        e.ref(new QName("", tn.getLocalPart()));
                                    }
                                } else {
                                    e.ref(tn);
                                }
                            }
                        } else {
                            e.name(tn.getLocalPart());
                            Namespace.this.writeTypeRef(e, t, "type");
                            Namespace.this.elementFormDefault.writeForm(e, tn);
                        }
                        if (t.isNillable()) {
                            e.nillable(true);
                        }
                        if (t.getDefaultValue() != null) {
                            e._default(t.getDefaultValue());
                        }
                        writeOccurs(e, isOptional, repeated);
                    }
                });
            }
            final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeOptional(!ep.isRequired()).makeRepeated(ep.isCollection());
            final QName ename = ep.getXmlName();
            if (ename != null) {
                return new Tree.Term() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.3
                    @Override // com.sun.xml.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        LocalElement e = parent.element();
                        if (ename.getNamespaceURI().length() > 0 && !ename.getNamespaceURI().equals(Namespace.this.uri)) {
                            e.ref(new QName(ename.getNamespaceURI(), ename.getLocalPart()));
                            return;
                        }
                        e.name(ename.getLocalPart());
                        Namespace.this.elementFormDefault.writeForm(e, ename);
                        if (ep.isCollectionNillable()) {
                            e.nillable(true);
                        }
                        writeOccurs(e, !ep.isCollectionRequired(), repeated);
                        ComplexType p = e.complexType();
                        choice.write(p);
                    }
                };
            }
            return choice;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean canBeDirectElementRef(TypeRef<T, C> t, QName tn, TypeInfo parentInfo) {
            Element te = null;
            ClassInfo ci = null;
            QName targetTagName = null;
            if (t.isNillable() || t.getDefaultValue() != null) {
                return false;
            }
            if (t.getTarget() instanceof Element) {
                te = (Element) t.getTarget();
                targetTagName = te.getElementName();
                if (te instanceof ClassInfo) {
                    ci = (ClassInfo) te;
                }
            }
            String nsUri = tn.getNamespaceURI();
            if (!nsUri.equals(this.uri) && nsUri.length() > 0 && (!(parentInfo instanceof ClassInfo) || ((ClassInfo) parentInfo).getTypeName() != null)) {
                return true;
            }
            if (ci != null && targetTagName != null && te.getScope() == null && targetTagName.getNamespaceURI() == null && targetTagName.equals(tn)) {
                return true;
            }
            return (te == null || targetTagName == null || !targetTagName.equals(tn)) ? false : true;
        }

        private void handleAttributeProp(AttributePropertyInfo<T, C> ap, AttrDecls attr) {
            LocalAttribute localAttribute = attr.attribute();
            String attrURI = ap.getXmlName().getNamespaceURI();
            if (attrURI.equals("")) {
                localAttribute.name(ap.getXmlName().getLocalPart());
                writeAttributeTypeRef(ap, localAttribute);
                this.attributeFormDefault.writeForm(localAttribute, ap.getXmlName());
            } else {
                localAttribute.ref(ap.getXmlName());
            }
            if (ap.isRequired()) {
                localAttribute.use("required");
            }
        }

        private void writeAttributeTypeRef(AttributePropertyInfo<T, C> ap, AttributeType a) {
            if (ap.isCollection()) {
                writeTypeRef(a.simpleType().list(), ap, "itemType");
            } else {
                writeTypeRef(a, ap, "type");
            }
        }

        private Tree handleReferenceProp(final ReferencePropertyInfo<T, C> rp) {
            ArrayList<Tree> children = new ArrayList<>();
            for (final Element<T, C> e : rp.getElements()) {
                children.add(new Tree.Term() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.4
                    @Override // com.sun.xml.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        LocalElement eref = parent.element();
                        boolean local = false;
                        QName en = e.getElementName();
                        if (e.getScope() != null) {
                            boolean qualified = en.getNamespaceURI().equals(Namespace.this.uri);
                            boolean unqualified = en.getNamespaceURI().equals("");
                            if (qualified || unqualified) {
                                if (unqualified) {
                                    if (Namespace.this.elementFormDefault.isEffectivelyQualified) {
                                        eref.form("unqualified");
                                    }
                                } else if (!Namespace.this.elementFormDefault.isEffectivelyQualified) {
                                    eref.form("qualified");
                                }
                                local = true;
                                eref.name(en.getLocalPart());
                                if (e instanceof ClassInfo) {
                                    Namespace.this.writeTypeRef(eref, (ClassInfo) e, "type");
                                } else {
                                    Namespace.this.writeTypeRef(eref, ((ElementInfo) e).getContentType(), "type");
                                }
                            }
                        }
                        if (!local) {
                            eref.ref(en);
                        }
                        writeOccurs(eref, isOptional, repeated);
                    }
                });
            }
            final WildcardMode wc = rp.getWildcard();
            if (wc != null) {
                children.add(new Tree.Term() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.5
                    @Override // com.sun.xml.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        Any any = parent.any();
                        String pcmode = XmlSchemaGenerator.getProcessContentsModeName(wc);
                        if (pcmode != null) {
                            any.processContents(pcmode);
                        }
                        any.namespace("##other");
                        writeOccurs(any, isOptional, repeated);
                    }
                });
            }
            final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeRepeated(rp.isCollection()).makeOptional(!rp.isRequired());
            final QName ename = rp.getXmlName();
            if (ename != null) {
                return new Tree.Term() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.6
                    @Override // com.sun.xml.bind.v2.schemagen.Tree
                    protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                        LocalElement e2 = parent.element().name(ename.getLocalPart());
                        Namespace.this.elementFormDefault.writeForm(e2, ename);
                        if (rp.isCollectionNillable()) {
                            e2.nillable(true);
                        }
                        writeOccurs(e2, true, repeated);
                        ComplexType p = e2.complexType();
                        choice.write(p);
                    }
                };
            }
            return choice;
        }

        private Tree handleMapProp(final MapPropertyInfo<T, C> mp) {
            return new Tree.Term() { // from class: com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.7
                @Override // com.sun.xml.bind.v2.schemagen.Tree
                protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
                    QName ename = mp.getXmlName();
                    LocalElement e = parent.element();
                    Namespace.this.elementFormDefault.writeForm(e, ename);
                    if (mp.isCollectionNillable()) {
                        e.nillable(true);
                    }
                    LocalElement e2 = e.name(ename.getLocalPart());
                    writeOccurs(e2, isOptional, repeated);
                    ComplexType p = e2.complexType();
                    LocalElement e3 = p.sequence().element();
                    e3.name("entry").minOccurs(0).maxOccurs("unbounded");
                    ExplicitGroup seq = e3.complexType().sequence();
                    Namespace.this.writeKeyOrValue(seq, "key", mp.getKeyType());
                    Namespace.this.writeKeyOrValue(seq, "value", mp.getValueType());
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeKeyOrValue(ExplicitGroup seq, String tagName, NonElement<T, C> typeRef) {
            LocalElement key = seq.element().name(tagName);
            key.minOccurs(0);
            writeTypeRef(key, typeRef, "type");
        }

        public void addGlobalAttribute(AttributePropertyInfo<T, C> ap) {
            this.attributeDecls.put((MultiMap<String, AttributePropertyInfo<T, C>>) ap.getXmlName().getLocalPart(), (String) ap);
            addDependencyTo(ap.getTarget().getTypeName());
        }

        public void addGlobalElement(TypeRef<T, C> tref) {
            this.elementDecls.put((MultiMap<String, XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration>) tref.getTagName().getLocalPart(), (String) new ElementWithType(false, tref.getTarget()));
            addDependencyTo(tref.getTarget().getTypeName());
        }

        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("[classes=").append(this.classes);
            buf.append(",elementDecls=").append(this.elementDecls);
            buf.append(",enums=").append(this.enums);
            buf.append("]");
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/XmlSchemaGenerator$Namespace$ElementDeclaration.class */
        public abstract class ElementDeclaration {
            public abstract boolean equals(Object obj);

            public abstract int hashCode();

            public abstract void writeTo(String str, Schema schema);

            ElementDeclaration() {
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/XmlSchemaGenerator$Namespace$ElementWithType.class */
        public class ElementWithType extends XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration {
            private final boolean nillable;
            private final NonElement<T, C> type;

            public ElementWithType(boolean nillable, NonElement<T, C> type) {
                super();
                this.type = type;
                this.nillable = nillable;
            }

            @Override // com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.ElementDeclaration
            public void writeTo(String localName, Schema schema) {
                TopLevelElement e = schema.element().name(localName);
                if (this.nillable) {
                    e.nillable(true);
                }
                if (this.type != null) {
                    Namespace.this.writeTypeRef(e, this.type, "type");
                } else {
                    e.complexType();
                }
                e.commit();
            }

            @Override // com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.ElementDeclaration
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                XmlSchemaGenerator<T, C, F, M>.Namespace.ElementWithType that = (ElementWithType) o;
                return this.type.equals(that.type);
            }

            @Override // com.sun.xml.bind.v2.schemagen.XmlSchemaGenerator.Namespace.ElementDeclaration
            public int hashCode() {
                return this.type.hashCode();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean generateSwaRefAdapter(NonElementRef<T, C> typeRef) {
        return generateSwaRefAdapter(typeRef.getSource());
    }

    private boolean generateSwaRefAdapter(PropertyInfo<T, C> prop) {
        Object o;
        Adapter<T, C> adapter = prop.getAdapter();
        if (adapter == null || (o = this.navigator.asDecl(SwaRefAdapter.class)) == null) {
            return false;
        }
        return o.equals(adapter.adapterType);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (XmlSchemaGenerator<T, C, F, M>.Namespace ns : this.namespaces.values()) {
            if (buf.length() > 0) {
                buf.append(',');
            }
            buf.append(ns.uri).append('=').append(ns);
        }
        return super.toString() + '[' + ((Object) buf) + ']';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getProcessContentsModeName(WildcardMode wc) {
        switch (wc) {
            case LAX:
            case SKIP:
                return wc.name().toLowerCase();
            case STRICT:
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    protected static String relativize(String uri, String baseUri) {
        try {
            if ($assertionsDisabled || uri != null) {
                if (baseUri == null) {
                    return uri;
                }
                URI theUri = new URI(Util.escapeURI(uri));
                URI theBaseUri = new URI(Util.escapeURI(baseUri));
                if (theUri.isOpaque() || theBaseUri.isOpaque()) {
                    return uri;
                }
                if (!Util.equalsIgnoreCase(theUri.getScheme(), theBaseUri.getScheme()) || !Util.equal(theUri.getAuthority(), theBaseUri.getAuthority())) {
                    return uri;
                }
                String uriPath = theUri.getPath();
                String basePath = theBaseUri.getPath();
                if (!basePath.endsWith("/")) {
                    basePath = Util.normalizeUriPath(basePath);
                }
                if (uriPath.equals(basePath)) {
                    return ".";
                }
                String relPath = calculateRelativePath(uriPath, basePath, fixNull(theUri.getScheme()).equals("file"));
                if (relPath == null) {
                    return uri;
                }
                StringBuilder relUri = new StringBuilder();
                relUri.append(relPath);
                if (theUri.getQuery() != null) {
                    relUri.append('?').append(theUri.getQuery());
                }
                if (theUri.getFragment() != null) {
                    relUri.append('#').append(theUri.getFragment());
                }
                return relUri.toString();
            }
            throw new AssertionError();
        } catch (URISyntaxException e) {
            throw new InternalError("Error escaping one of these uris:\n\t" + uri + "\n\t" + baseUri);
        }
    }

    private static String fixNull(String s) {
        return s == null ? "" : s;
    }

    private static String calculateRelativePath(String uri, String base, boolean fileUrl) {
        boolean onWindows = File.pathSeparatorChar == ';';
        if (base == null) {
            return null;
        }
        if ((fileUrl && onWindows && startsWithIgnoreCase(uri, base)) || uri.startsWith(base)) {
            return uri.substring(base.length());
        }
        return "../" + calculateRelativePath(uri, Util.getParentUriPath(base), fileUrl);
    }

    private static boolean startsWithIgnoreCase(String s, String t) {
        return s.toUpperCase().startsWith(t.toUpperCase());
    }
}
