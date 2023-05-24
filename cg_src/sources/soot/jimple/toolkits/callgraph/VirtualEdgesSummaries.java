package soot.jimple.toolkits.callgraph;

import com.google.common.collect.Iterables;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.bytebuddy.description.type.TypeDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import soot.Kind;
import soot.MethodSubSignature;
import soot.ModuleUtil;
import soot.RefType;
import soot.Scene;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
import soot.jimple.infoflow.rifl.RIFLConstants;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries.class */
public class VirtualEdgesSummaries {
    public static final int BASE_INDEX = -1;
    private static final String SUMMARIESFILE = "virtualedges.xml";
    protected final HashMap<MethodSubSignature, VirtualEdge> instanceinvokeEdges = new LinkedHashMap();
    protected final HashMap<String, VirtualEdge> staticinvokeEdges = new LinkedHashMap();
    private static final Logger logger = LoggerFactory.getLogger(VirtualEdgesSummaries.class);

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries$VirtualEdgeSource.class */
    public static abstract class VirtualEdgeSource {
    }

    public VirtualEdgesSummaries() {
        Path summariesFile = Paths.get(SUMMARIESFILE, new String[0]);
        try {
            InputStream in = Files.exists(summariesFile, new LinkOption[0]) ? Files.newInputStream(summariesFile, new OpenOption[0]) : ModuleUtil.class.getResourceAsStream("/virtualedges.xml");
            try {
                if (in == null) {
                    logger.error("Virtual edge summaries file not found");
                } else {
                    loadSummaries(in);
                }
                if (in != null) {
                    in.close();
                }
            } catch (Throwable th) {
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (IOException | ParserConfigurationException | SAXException e1) {
            logger.error("An error occurred while reading in virtual edge summaries", (Throwable) e1);
        }
    }

    public VirtualEdgesSummaries(File summariesFile) {
        try {
            InputStream in = new FileInputStream(summariesFile);
            try {
                loadSummaries(in);
                if (in != null) {
                    in.close();
                }
            } catch (Throwable th) {
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (IOException | ParserConfigurationException | SAXException e1) {
            logger.error("An error occurred while reading in virtual edge summaries", (Throwable) e1);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected void loadSummaries(InputStream in) throws SAXException, IOException, ParserConfigurationException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        doc.getDocumentElement().normalize();
        NodeList edges = doc.getElementsByTagName("edge");
        int e = edges.getLength();
        for (int i = 0; i < e; i++) {
            if (edges.item(i).getNodeType() == 1) {
                Element edge = (Element) edges.item(i);
                VirtualEdge edg = new VirtualEdge();
                String attribute = edge.getAttribute("type");
                switch (attribute.hashCode()) {
                    case -1821113846:
                        if (attribute.equals("THREAD")) {
                            edg.edgeType = Kind.THREAD;
                            break;
                        }
                        edg.edgeType = Kind.GENERIC_FAKE;
                        break;
                    case -1469323053:
                        if (attribute.equals("EXECUTOR")) {
                            edg.edgeType = Kind.EXECUTOR;
                            break;
                        }
                        edg.edgeType = Kind.GENERIC_FAKE;
                        break;
                    case -1169306669:
                        if (attribute.equals("PRIVILEGED")) {
                            edg.edgeType = Kind.PRIVILEGED;
                            break;
                        }
                        edg.edgeType = Kind.GENERIC_FAKE;
                        break;
                    case 1079880161:
                        if (attribute.equals("ASYNCTASK")) {
                            edg.edgeType = Kind.ASYNCTASK;
                            break;
                        }
                        edg.edgeType = Kind.GENERIC_FAKE;
                        break;
                    case 1410786090:
                        if (attribute.equals("HANDLER")) {
                            edg.edgeType = Kind.HANDLER;
                            break;
                        }
                        edg.edgeType = Kind.GENERIC_FAKE;
                        break;
                    case 2083712221:
                        if (!attribute.equals("GENERIC_FAKE")) {
                        }
                        edg.edgeType = Kind.GENERIC_FAKE;
                        break;
                    default:
                        edg.edgeType = Kind.GENERIC_FAKE;
                        break;
                }
                edg.source = parseEdgeSource((Element) edge.getElementsByTagName(RIFLConstants.SOURCE_TAG).item(0));
                edg.targets = new HashSet();
                Element targetsElement = (Element) edge.getElementsByTagName("targets").item(0);
                edg.targets.addAll(parseEdgeTargets(targetsElement));
                if (edg.source instanceof InstanceinvokeSource) {
                    InstanceinvokeSource inst = (InstanceinvokeSource) edg.source;
                    MethodSubSignature subsig = inst.subSignature;
                    addInstanceInvoke(edg, subsig);
                }
                if (edg.source instanceof StaticinvokeSource) {
                    StaticinvokeSource stat = (StaticinvokeSource) edg.source;
                    this.staticinvokeEdges.put(stat.signature, edg);
                }
            }
        }
        logger.debug("Found {} instanceinvoke, {} staticinvoke edge descriptions", Integer.valueOf(this.instanceinvokeEdges.size()), Integer.valueOf(this.staticinvokeEdges.size()));
    }

    protected void addInstanceInvoke(VirtualEdge edg, MethodSubSignature subsig) {
        VirtualEdge existing = this.instanceinvokeEdges.get(subsig);
        if (existing != null) {
            existing.targets.addAll(edg.targets);
        } else {
            this.instanceinvokeEdges.put(subsig, edg);
        }
    }

    public VirtualEdgesSummaries(Collection<VirtualEdge> edges) {
        for (VirtualEdge vi : edges) {
            if (vi.source instanceof InstanceinvokeSource) {
                InstanceinvokeSource inst = (InstanceinvokeSource) vi.source;
                addInstanceInvoke(vi, inst.subSignature);
            } else if (vi.source instanceof StaticinvokeSource) {
                StaticinvokeSource stat = (StaticinvokeSource) vi.source;
                this.staticinvokeEdges.put(stat.signature, vi);
            }
        }
    }

    public Document toXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("virtualedges");
        document.appendChild(root);
        for (VirtualEdge edge : Iterables.concat(this.instanceinvokeEdges.values(), this.staticinvokeEdges.values())) {
            Node e = edgeToXML(document, edge);
            root.appendChild(e);
        }
        return document;
    }

    private static Element edgeToXML(Document doc, VirtualEdge edge) {
        Element node = doc.createElement("edge");
        node.setAttribute("type", edge.edgeType.name());
        Element source = doc.createElement(RIFLConstants.SOURCE_TAG);
        node.appendChild(source);
        if (edge.source instanceof StaticinvokeSource) {
            source.setAttribute("invoketype", Jimple.STATIC);
            source.setAttribute(XMLConstants.SIGNATURE_ATTRIBUTE, ((StaticinvokeSource) edge.source).signature);
        } else if (edge.source instanceof InstanceinvokeSource) {
            InstanceinvokeSource inv = (InstanceinvokeSource) edge.source;
            source.setAttribute("invoketype", "instance");
            source.setAttribute("subsignature", inv.subSignature.toString());
            if (inv.declaringType != null) {
                source.setAttribute("declaringclass", inv.declaringType.getClassName());
            }
        } else if (edge.source == null) {
            throw new IllegalArgumentException("Unsupported null source type");
        } else {
            throw new IllegalArgumentException("Unsupported source type " + edge.source.getClass());
        }
        Element targets = doc.createElement("targets");
        node.appendChild(targets);
        for (VirtualEdgeTarget e : edge.targets) {
            Element target = edgeTargetToXML(doc, e);
            targets.appendChild(target);
        }
        return node;
    }

    private static Element edgeTargetToXML(Document doc, VirtualEdgeTarget e) {
        Element target;
        if (e instanceof DirectTarget) {
            target = doc.createElement("direct");
        } else if (e instanceof IndirectTarget) {
            target = doc.createElement("indirect");
            IndirectTarget id = (IndirectTarget) e;
            for (VirtualEdgeTarget i : id.targets) {
                target.appendChild(edgeTargetToXML(doc, i));
            }
        } else if (e == null) {
            throw new IllegalArgumentException("Unsupported null edge type");
        } else {
            throw new IllegalArgumentException("Unsupported source type " + e.getClass());
        }
        if (e.targetType != null) {
            target.setAttribute("declaringclass", e.targetType.getClassName());
        }
        target.setAttribute("subsignature", e.targetMethod.toString());
        if (e.isBase()) {
            target.setAttribute("target-position", XMLConstants.BASE_TAG);
        } else {
            target.setAttribute(XMLConstants.INDEX_ATTRIBUTE, String.valueOf(e.argIndex));
            target.setAttribute("target-position", "argument");
        }
        return target;
    }

    public VirtualEdge getVirtualEdgesMatchingSubSig(MethodSubSignature subsig) {
        return this.instanceinvokeEdges.get(subsig);
    }

    public VirtualEdge getVirtualEdgesMatchingFunction(String signature) {
        return this.staticinvokeEdges.get(signature);
    }

    private static VirtualEdgeSource parseEdgeSource(Element source) {
        String attribute = source.getAttribute("invoketype");
        switch (attribute.hashCode()) {
            case -892481938:
                if (attribute.equals(Jimple.STATIC)) {
                    return new StaticinvokeSource(source.getAttribute(XMLConstants.SIGNATURE_ATTRIBUTE));
                }
                return null;
            case 555127957:
                if (attribute.equals("instance")) {
                    RefType dClass = getDeclaringClassType(source);
                    return new InstanceinvokeSource(dClass, source.getAttribute("subsignature"));
                }
                return null;
            default:
                return null;
        }
    }

    private static RefType getDeclaringClassType(Element source) {
        String declClass = source.getAttribute("declaringclass");
        RefType dClass = null;
        if (declClass != null && !declClass.isEmpty()) {
            dClass = RefType.v(declClass);
        }
        return dClass;
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x0216, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.util.List<soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.VirtualEdgeTarget> parseEdgeTargets(org.w3c.dom.Element r7) {
        /*
            Method dump skipped, instructions count: 546
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.parseEdgeTargets(org.w3c.dom.Element):java.util.List");
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries$StaticinvokeSource.class */
    public static class StaticinvokeSource extends VirtualEdgeSource {
        String signature;

        public StaticinvokeSource(String signature) {
            this.signature = signature;
        }

        public String getSignature() {
            return this.signature;
        }

        public String toString() {
            return this.signature;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.signature == null ? 0 : this.signature.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StaticinvokeSource other = (StaticinvokeSource) obj;
            if (this.signature == null) {
                if (other.signature != null) {
                    return false;
                }
                return true;
            } else if (!this.signature.equals(other.signature)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries$InstanceinvokeSource.class */
    public static class InstanceinvokeSource extends VirtualEdgeSource {
        MethodSubSignature subSignature;
        RefType declaringType;

        public InstanceinvokeSource(RefType declaringType, String subSignature) {
            this.subSignature = new MethodSubSignature(Scene.v().getSubSigNumberer().findOrAdd(subSignature));
            this.declaringType = declaringType;
        }

        public InstanceinvokeSource(Stmt invokeStmt) {
            this(invokeStmt.getInvokeExpr().getMethodRef().getDeclaringClass().getType(), invokeStmt.getInvokeExpr().getMethodRef().getSubSignature().getString());
        }

        public String toString() {
            return String.valueOf(this.declaringType != null ? this.declaringType + ": " : "") + this.subSignature.toString();
        }

        public RefType getDeclaringType() {
            return this.declaringType;
        }

        public MethodSubSignature getSubSignature() {
            return this.subSignature;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.declaringType == null ? 0 : this.declaringType.hashCode());
            return (31 * result) + (this.subSignature == null ? 0 : this.subSignature.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            InstanceinvokeSource other = (InstanceinvokeSource) obj;
            if (this.declaringType == null) {
                if (other.declaringType != null) {
                    return false;
                }
            } else if (!this.declaringType.equals(other.declaringType)) {
                return false;
            }
            if (this.subSignature == null) {
                if (other.subSignature != null) {
                    return false;
                }
                return true;
            } else if (!this.subSignature.equals(other.subSignature)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries$VirtualEdgeTarget.class */
    public static abstract class VirtualEdgeTarget {
        protected int argIndex;
        protected MethodSubSignature targetMethod;
        protected RefType targetType;

        VirtualEdgeTarget() {
        }

        public VirtualEdgeTarget(RefType targetType, MethodSubSignature targetMethod) {
            this.argIndex = -1;
            this.targetMethod = targetMethod;
            this.targetType = targetType;
        }

        public VirtualEdgeTarget(RefType targetType, MethodSubSignature targetMethod, int argIndex) {
            this.argIndex = argIndex;
            this.targetMethod = targetMethod;
            this.targetType = targetType;
        }

        public RefType getTargetType() {
            return this.targetType;
        }

        public String toString() {
            return isBase() ? XMLConstants.BASE_TAG : String.format("argument %d", Integer.valueOf(this.argIndex));
        }

        public boolean isBase() {
            return this.argIndex == -1;
        }

        public int getArgIndex() {
            return this.argIndex;
        }

        public MethodSubSignature getTargetMethod() {
            return this.targetMethod;
        }

        public int hashCode() {
            int result = (31 * 1) + this.argIndex;
            return (31 * result) + (this.targetMethod == null ? 0 : this.targetMethod.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            VirtualEdgeTarget other = (VirtualEdgeTarget) obj;
            if (this.argIndex != other.argIndex) {
                return false;
            }
            if (this.targetMethod == null) {
                if (other.targetMethod != null) {
                    return false;
                }
                return true;
            } else if (!this.targetMethod.equals(other.targetMethod)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries$DirectTarget.class */
    public static class DirectTarget extends VirtualEdgeTarget {
        DirectTarget() {
        }

        public DirectTarget(RefType targetType, MethodSubSignature targetMethod, int argIndex) {
            super(targetType, targetMethod, argIndex);
        }

        public DirectTarget(RefType targetType, MethodSubSignature targetMethod) {
            super(targetType, targetMethod);
        }

        @Override // soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.VirtualEdgeTarget
        public String toString() {
            Object[] objArr = new Object[3];
            objArr[0] = this.targetType != null ? String.valueOf(this.targetType.getClassName()) + ": " : "";
            objArr[1] = this.targetMethod.toString();
            objArr[2] = super.toString();
            return String.format("Direct to %s%s on %s", objArr);
        }

        @Override // soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.VirtualEdgeTarget
        public int hashCode() {
            return super.hashCode();
        }

        @Override // soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.VirtualEdgeTarget
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries$IndirectTarget.class */
    public static class IndirectTarget extends VirtualEdgeTarget {
        List<VirtualEdgeTarget> targets;

        IndirectTarget() {
            this.targets = new ArrayList();
        }

        public IndirectTarget(RefType targetType, MethodSubSignature targetMethod, int argIndex) {
            super(targetType, targetMethod, argIndex);
            this.targets = new ArrayList();
        }

        public IndirectTarget(InstanceinvokeSource source) {
            super(source.declaringType, source.subSignature);
            this.targets = new ArrayList();
        }

        public IndirectTarget(RefType targetType, MethodSubSignature targetMethod) {
            super(targetType, targetMethod);
            this.targets = new ArrayList();
        }

        public void addTarget(VirtualEdgeTarget target) {
            if (!this.targets.contains(target)) {
                this.targets.add(target);
            }
        }

        public void addTargets(Collection<? extends VirtualEdgeTarget> targets) {
            for (VirtualEdgeTarget target : targets) {
                addTarget(target);
            }
        }

        public List<VirtualEdgeTarget> getTargets() {
            return this.targets;
        }

        @Override // soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.VirtualEdgeTarget
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (VirtualEdgeTarget t : this.targets) {
                sb.append('(').append(t.toString()).append(") ");
            }
            return String.format("(Instances passed to <" + (this.targetType != null ? this.targetType : TypeDescription.Generic.OfWildcardType.SYMBOL) + ": %s> on %s => %s)", this.targetMethod.toString(), super.toString(), sb.toString());
        }

        @Override // soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.VirtualEdgeTarget
        public int hashCode() {
            int result = super.hashCode();
            return (31 * result) + (this.targets == null ? 0 : this.targets.hashCode());
        }

        @Override // soot.jimple.toolkits.callgraph.VirtualEdgesSummaries.VirtualEdgeTarget
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj) || getClass() != obj.getClass()) {
                return false;
            }
            IndirectTarget other = (IndirectTarget) obj;
            if (this.targets == null) {
                if (other.targets != null) {
                    return false;
                }
                return true;
            } else if (!this.targets.equals(other.targets)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/VirtualEdgesSummaries$VirtualEdge.class */
    public static class VirtualEdge {
        Kind edgeType;
        VirtualEdgeSource source;
        Set<VirtualEdgeTarget> targets;

        VirtualEdge() {
        }

        public VirtualEdge(Kind edgeType, VirtualEdgeSource source, VirtualEdgeTarget target) {
            this(edgeType, source, new ArrayList(Collections.singletonList(target)));
        }

        public VirtualEdge(Kind edgeType, VirtualEdgeSource source, Collection<VirtualEdgeTarget> targets) {
            this.edgeType = edgeType;
            this.source = source;
            this.targets = new HashSet(targets);
        }

        public Kind getEdgeType() {
            return this.edgeType;
        }

        public VirtualEdgeSource getSource() {
            return this.source;
        }

        public Set<VirtualEdgeTarget> getTargets() {
            return this.targets;
        }

        public void addTargets(Collection<VirtualEdgeTarget> newTargets) {
            this.targets.addAll(newTargets);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (VirtualEdgeTarget t : this.targets) {
                sb.append(t.toString()).append(' ');
            }
            return String.format("%s %s => %s\n", this.edgeType, this.source.toString(), sb.toString());
        }

        public int hashCode() {
            int result = (31 * 1) + (this.edgeType == null ? 0 : this.edgeType.hashCode());
            return (31 * ((31 * result) + (this.source == null ? 0 : this.source.hashCode()))) + (this.targets == null ? 0 : this.targets.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            VirtualEdge other = (VirtualEdge) obj;
            if (this.edgeType == null) {
                if (other.edgeType != null) {
                    return false;
                }
            } else if (!this.edgeType.equals(other.edgeType)) {
                return false;
            }
            if (this.source == null) {
                if (other.source != null) {
                    return false;
                }
            } else if (!this.source.equals(other.source)) {
                return false;
            }
            if (this.targets == null) {
                if (other.targets != null) {
                    return false;
                }
                return true;
            } else if (!this.targets.equals(other.targets)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean isEmpty() {
        return this.instanceinvokeEdges.isEmpty() && this.staticinvokeEdges.isEmpty();
    }

    public Set<VirtualEdge> getAllVirtualEdges() {
        Set<VirtualEdge> allEdges = new HashSet<>(this.instanceinvokeEdges.size() + this.staticinvokeEdges.size());
        allEdges.addAll(this.instanceinvokeEdges.values());
        allEdges.addAll(this.staticinvokeEdges.values());
        return allEdges;
    }
}
