package soot.xml;

import android.provider.Browser;
import android.provider.ContactsContract;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.main.Report;
import soot.Body;
import soot.G;
import soot.LabeledUnitPrinter;
import soot.Local;
import soot.Main;
import soot.Modifier;
import soot.NormalUnitPrinter;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
import soot.jimple.toolkits.scalar.DefaultLocalCreation;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.LiveLocals;
import soot.toolkits.scalar.SimpleLiveLocals;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/xml/XMLPrinter.class */
public class XMLPrinter {
    private static final Logger logger = LoggerFactory.getLogger(XMLPrinter.class);
    public static final String xmlHeader = "<?xml version=\"1.0\" ?>\n";
    public static final String dtdHeader = "<!DOCTYPE jil SYSTEM \"http://www.sable.mcgill.ca/~flynn/jil/jil10.dtd\">\n";
    public XMLRoot root;

    public XMLPrinter(Singletons.Global g) {
    }

    public static XMLPrinter v() {
        return G.v().soot_xml_XMLPrinter();
    }

    public String toString() {
        if (this.root != null) {
            return this.root.toString();
        }
        throw new RuntimeException("Error generating XML!");
    }

    public XMLNode addElement(String name) {
        return addElement(name, "", "", "");
    }

    public XMLNode addElement(String name, String value) {
        return addElement(name, value, "", "");
    }

    public XMLNode addElement(String name, String value, String[] attributes) {
        return addElement(name, value, attributes, (String[]) null);
    }

    public XMLNode addElement(String name, String value, String attribute, String attributeValue) {
        return addElement(name, value, new String[]{attribute}, new String[]{attributeValue});
    }

    public XMLNode addElement(String name, String value, String[] attributes, String[] values) {
        return this.root.addElement(name, value, attributes, values);
    }

    public void printJimpleStyleTo(SootClass cl, PrintWriter out) {
        String[] strArr;
        this.root = new XMLRoot();
        Scene sc = Scene.v();
        XMLNode xmlRootNode = this.root.addElement("jil");
        StringBuilder cmdlineStr = new StringBuilder();
        for (String element : Main.v().cmdLineArgs) {
            cmdlineStr.append(element).append(' ');
        }
        String dateStr = new Date().toString();
        XMLNode xmlHistoryNode = xmlRootNode.addChild("history");
        xmlHistoryNode.addAttribute(Browser.BookmarkColumns.CREATED, dateStr);
        xmlHistoryNode.addChild(DefaultLocalCreation.DEFAULT_PREFIX, new String[]{"version", "command", ContactsContract.StreamItemsColumns.TIMESTAMP}, new String[]{Main.versionString, cmdlineStr.toString().trim(), dateStr});
        XMLNode xmlClassNode = xmlRootNode.addChild("class", new String[]{"name"}, new String[]{sc.quotedNameOf(cl.getName())});
        if (!cl.getPackageName().isEmpty()) {
            xmlClassNode.addAttribute("package", cl.getPackageName());
        }
        if (cl.hasSuperclass()) {
            xmlClassNode.addAttribute(Jimple.EXTENDS, sc.quotedNameOf(cl.getSuperclass().getName()));
        }
        XMLNode xmlTempNode = xmlClassNode.addChild("modifiers");
        StringTokenizer st = new StringTokenizer(Modifier.toString(cl.getModifiers()));
        while (st.hasMoreTokens()) {
            xmlTempNode.addChild("modifier", new String[]{"name"}, new String[]{st.nextToken()});
        }
        xmlTempNode.addAttribute("count", String.valueOf(xmlTempNode.getNumberOfChildren()));
        XMLNode xmlTempNode2 = xmlClassNode.addChild("interfaces", "", new String[]{"count"}, new String[]{String.valueOf(cl.getInterfaceCount())});
        for (SootClass next : cl.getInterfaces()) {
            xmlTempNode2.addChild(Jimple.IMPLEMENTS, "", new String[]{"class"}, new String[]{sc.quotedNameOf(next.getName())});
        }
        XMLNode xmlTempNode3 = xmlClassNode.addChild("fields", "", new String[]{"count"}, new String[]{String.valueOf(cl.getFieldCount())});
        int i = 0;
        for (SootField f : cl.getFields()) {
            if (!f.isPhantom()) {
                int i2 = i;
                i++;
                XMLNode xmlFieldNode = xmlTempNode3.addChild("field", "", new String[]{"id", "name", "type"}, new String[]{String.valueOf(i2), f.getName(), f.getType().toString()});
                XMLNode xmlModifiersNode = xmlFieldNode.addChild("modifiers");
                StringTokenizer st2 = new StringTokenizer(Modifier.toString(f.getModifiers()));
                while (st2.hasMoreTokens()) {
                    xmlModifiersNode.addChild("modifier", new String[]{"name"}, new String[]{st2.nextToken()});
                }
                xmlModifiersNode.addAttribute("count", String.valueOf(xmlModifiersNode.getNumberOfChildren()));
            }
        }
        XMLNode methodsNode = xmlClassNode.addChild("methods", new String[]{"count"}, new String[]{String.valueOf(cl.getMethodCount())});
        Iterator<SootMethod> methodIt = cl.methodIterator();
        while (methodIt.hasNext()) {
            SootMethod method = methodIt.next();
            if (!method.isPhantom() && !Modifier.isAbstract(method.getModifiers()) && !Modifier.isNative(method.getModifiers())) {
                if (!method.hasActiveBody()) {
                    throw new RuntimeException("method " + method.getName() + " has no active body!");
                }
                Body body = method.getActiveBody();
                body.validate();
                printStatementsInBody(body, methodsNode);
            }
        }
        out.println(toString());
    }

    private void printStatementsInBody(Body body, XMLNode methodsNode) {
        LabeledUnitPrinter up = new NormalUnitPrinter(body);
        Map<Unit, String> stmtToName = up.labels();
        Chain<Unit> units = body.getUnits();
        String currentLabel = "default";
        String cleanMethodName = cleanMethod(body.getMethod().getName());
        XMLNode methodNode = methodsNode.addChild("method", new String[]{"name", "returntype", "class"}, new String[]{cleanMethodName, body.getMethod().getReturnType().toString(), body.getMethod().getDeclaringClass().getName()});
        String declarationStr = body.getMethod().getDeclaration().trim();
        methodNode.addChild("declaration", toCDATA(declarationStr), new String[]{XMLConstants.LENGTH_ATTRIBUTE}, new String[]{String.valueOf(declarationStr.length())});
        XMLNode parametersNode = methodNode.addChild("parameters", new String[]{"method"}, new String[]{cleanMethodName});
        XMLNode localsNode = methodNode.addChild("locals");
        XMLNode labelsNode = methodNode.addChild("labels");
        XMLNode stmtsNode = methodNode.addChild("statements");
        XMLLabel xmlLabel = new XMLLabel(0L, cleanMethodName, currentLabel);
        long labelCount = 0 + 1;
        labelsNode.addChild("label", new String[]{"id", "name", "method"}, new String[]{String.valueOf(0L), currentLabel, cleanMethodName});
        LiveLocals sll = new SimpleLiveLocals(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body));
        ArrayList<String> useList = new ArrayList<>();
        ArrayList<ArrayList<Long>> useDataList = new ArrayList<>();
        ArrayList<String> defList = new ArrayList<>();
        ArrayList<ArrayList<Long>> defDataList = new ArrayList<>();
        ArrayList<ArrayList<String>> paramData = new ArrayList<>();
        ArrayList<XMLLabel> xmlLabelsList = new ArrayList<>();
        long statementCount = 0;
        long maxStmtCount = 0;
        long labelID = 0;
        for (Unit currentStmt : units) {
            if (stmtToName.containsKey(currentStmt)) {
                currentLabel = stmtToName.get(currentStmt);
                xmlLabel.stmtCount = labelID;
                xmlLabel.stmtPercentage = (((float) labelID) / units.size()) * 100.0f;
                if (xmlLabel.stmtPercentage > maxStmtCount) {
                    maxStmtCount = xmlLabel.stmtPercentage;
                }
                xmlLabelsList.add(xmlLabel);
                xmlLabel = new XMLLabel(labelCount, cleanMethodName, currentLabel);
                labelsNode.addChild("label", new String[]{"id", "name", "method"}, new String[]{String.valueOf(labelCount), currentLabel, cleanMethodName});
                labelCount++;
                labelID = 0;
            }
            XMLNode stmtNode = stmtsNode.addChild("statement", new String[]{"id", "label", "method", "labelid"}, new String[]{String.valueOf(statementCount), currentLabel, cleanMethodName, String.valueOf(labelID)});
            XMLNode sootstmtNode = stmtNode.addChild("soot_statement", new String[]{"branches", "fallsthrough"}, new String[]{boolToString(currentStmt.branches()), boolToString(currentStmt.fallsThrough())});
            int j = 0;
            for (ValueBox box : currentStmt.getUseBoxes()) {
                if (box.getValue() instanceof Local) {
                    String local = cleanLocal(box.getValue().toString());
                    sootstmtNode.addChild("uses", new String[]{"id", "local", "method"}, new String[]{String.valueOf(j), local, cleanMethodName});
                    j++;
                    int useIndex = useList.indexOf(local);
                    if (useIndex == -1) {
                        useDataList.add(null);
                        useIndex = useList.size();
                        useList.add(local);
                    }
                    if (useDataList.size() > useIndex) {
                        ArrayList<Long> tempArrayList = useDataList.get(useIndex);
                        if (tempArrayList == null) {
                            tempArrayList = new ArrayList<>();
                        }
                        tempArrayList.add(Long.valueOf(statementCount));
                        useDataList.set(useIndex, tempArrayList);
                    }
                }
            }
            int j2 = 0;
            for (ValueBox box2 : currentStmt.getDefBoxes()) {
                if (box2.getValue() instanceof Local) {
                    String local2 = cleanLocal(box2.getValue().toString());
                    sootstmtNode.addChild("defines", new String[]{"id", "local", "method"}, new String[]{String.valueOf(j2), local2, cleanMethodName});
                    j2++;
                    int defIndex = defList.indexOf(local2);
                    if (defIndex == -1) {
                        defDataList.add(null);
                        defIndex = defList.size();
                        defList.add(local2);
                    }
                    if (defDataList.size() > defIndex) {
                        ArrayList<Long> tempArrayList2 = defDataList.get(defIndex);
                        if (tempArrayList2 == null) {
                            tempArrayList2 = new ArrayList<>();
                        }
                        tempArrayList2.add(Long.valueOf(statementCount));
                        defDataList.set(defIndex, tempArrayList2);
                    }
                }
            }
            List<Local> liveLocalsIn = sll.getLiveLocalsBefore(currentStmt);
            List<Local> liveLocalsOut = sll.getLiveLocalsAfter(currentStmt);
            XMLNode livevarsNode = sootstmtNode.addChild("livevariables", new String[]{"incount", "outcount"}, new String[]{String.valueOf(liveLocalsIn.size()), String.valueOf(liveLocalsOut.size())});
            ListIterator<Local> it = liveLocalsIn.listIterator();
            while (it.hasNext()) {
                int i = it.nextIndex();
                Local val = it.next();
                livevarsNode.addChild("in", new String[]{"id", "local", "method"}, new String[]{String.valueOf(i), cleanLocal(val.toString()), cleanMethodName});
            }
            ListIterator<Local> it2 = liveLocalsOut.listIterator();
            while (it2.hasNext()) {
                int i2 = it2.nextIndex();
                Local val2 = it2.next();
                livevarsNode.addChild("out", new String[]{"id", "local", "method"}, new String[]{String.valueOf(i2), cleanLocal(val2.toString()), cleanMethodName});
            }
            int e = body.getMethod().getParameterCount();
            for (int i3 = 0; i3 < e; i3++) {
                paramData.add(new ArrayList<>());
            }
            currentStmt.toString(up);
            String jimpleStr = up.toString().trim();
            if ((currentStmt instanceof IdentityStmt) && jimpleStr.contains("@parameter")) {
                String tempStr = jimpleStr.substring(jimpleStr.indexOf("@parameter") + 10);
                int idx = tempStr.indexOf(58);
                if (idx != -1) {
                    tempStr = tempStr.substring(0, idx).trim();
                }
                int idx2 = tempStr.indexOf(32);
                if (idx2 != -1) {
                    tempStr = tempStr.substring(0, idx2).trim();
                }
                int paramIndex = Integer.valueOf(tempStr).intValue();
                ArrayList<String> tempVec = paramData.get(paramIndex);
                if (tempVec != null) {
                    tempVec.add(Long.toString(statementCount));
                }
                paramData.set(paramIndex, tempVec);
            }
            sootstmtNode.addChild("jimple", toCDATA(jimpleStr), new String[]{XMLConstants.LENGTH_ATTRIBUTE}, new String[]{String.valueOf(jimpleStr.length() + 1)});
            labelID++;
            statementCount++;
        }
        stmtsNode.addAttribute("count", String.valueOf(statementCount));
        List<Type> parameterTypes = body.getMethod().getParameterTypes();
        parametersNode.addAttribute("count", String.valueOf(parameterTypes.size()));
        ListIterator<Type> it3 = parameterTypes.listIterator();
        while (it3.hasNext()) {
            int i4 = it3.nextIndex();
            Type val3 = it3.next();
            XMLNode paramNode = parametersNode.addChild("parameter", new String[]{"id", "type", "method", "name"}, new String[]{String.valueOf(i4), String.valueOf(val3), cleanMethodName, "_parameter" + i4});
            XMLNode sootparamNode = paramNode.addChild("soot_parameter");
            ArrayList<String> tempVec2 = paramData.get(i4);
            ListIterator<String> itk = tempVec2.listIterator();
            while (itk.hasNext()) {
                int k = itk.nextIndex();
                String valk = itk.next();
                sootparamNode.addChild("use", new String[]{"id", "line", "method"}, new String[]{String.valueOf(k), valk, cleanMethodName});
            }
            sootparamNode.addAttribute("uses", String.valueOf(tempVec2.size()));
        }
        xmlLabel.stmtCount = labelID;
        xmlLabel.stmtPercentage = (((float) labelID) / units.size()) * 100.0f;
        if (xmlLabel.stmtPercentage > maxStmtCount) {
            maxStmtCount = xmlLabel.stmtPercentage;
        }
        xmlLabelsList.add(xmlLabel);
        Collection<Local> locals = body.getLocals();
        ArrayList<String> localTypes = new ArrayList<>();
        ArrayList<ArrayList<XMLNode>> typedLocals = new ArrayList<>();
        ArrayList<Integer> typeCounts = new ArrayList<>();
        int j3 = 0;
        for (Local localData : locals) {
            int useCount = 0;
            int defineCount = 0;
            String localType = localData.getType().toString();
            if (!localTypes.contains(localType)) {
                localTypes.add(localType);
                typedLocals.add(new ArrayList<>());
                typeCounts.add(0);
            }
            String local3 = cleanLocal(localData.toString());
            XMLNode localNode = new XMLNode("local", "", new String[]{"id", "method", "name", "type"}, new String[]{String.valueOf(j3), cleanMethodName, local3, localType});
            XMLNode sootlocalNode = localNode.addChild("soot_local");
            int currentType = 0;
            ListIterator<String> it4 = localTypes.listIterator();
            while (true) {
                if (!it4.hasNext()) {
                    break;
                }
                String val4 = it4.next();
                if (localType.equalsIgnoreCase(val4)) {
                    int k2 = it4.previousIndex();
                    currentType = k2;
                    typeCounts.set(k2, Integer.valueOf(typeCounts.get(k2).intValue() + 1));
                    break;
                }
            }
            Iterator<String> it5 = useList.iterator();
            while (true) {
                if (!it5.hasNext()) {
                    break;
                }
                String nextUse = it5.next();
                if (local3.equalsIgnoreCase(nextUse)) {
                    ArrayList<Long> tempArrayList3 = useDataList.get(useList.indexOf(local3));
                    useCount = tempArrayList3.size();
                    ListIterator<Long> it6 = tempArrayList3.listIterator();
                    while (it6.hasNext()) {
                        int i5 = it6.nextIndex();
                        Long val5 = it6.next();
                        sootlocalNode.addChild("use", new String[]{"id", "line", "method"}, new String[]{String.valueOf(i5), String.valueOf(val5), cleanMethodName});
                    }
                }
            }
            Iterator<String> it7 = defList.iterator();
            while (true) {
                if (!it7.hasNext()) {
                    break;
                }
                String nextDef = it7.next();
                if (local3.equalsIgnoreCase(nextDef)) {
                    ArrayList<Long> tempArrayList4 = defDataList.get(defList.indexOf(local3));
                    defineCount = tempArrayList4.size();
                    ListIterator<Long> it8 = tempArrayList4.listIterator();
                    while (it8.hasNext()) {
                        int i6 = it8.nextIndex();
                        Long val6 = it8.next();
                        sootlocalNode.addChild("definition", new String[]{"id", "line", "method"}, new String[]{String.valueOf(i6), String.valueOf(val6), cleanMethodName});
                    }
                }
            }
            sootlocalNode.addAttribute("uses", String.valueOf(useCount));
            sootlocalNode.addAttribute("defines", String.valueOf(defineCount));
            ArrayList<XMLNode> list = typedLocals.get(currentType);
            list.add(localNode);
            typedLocals.set(currentType, list);
            localsNode.addChild((XMLNode) localNode.clone());
            j3++;
        }
        localsNode.addAttribute("count", String.valueOf(locals.size()));
        XMLNode typesNode = localsNode.addChild(Report.types, new String[]{"count"}, new String[]{String.valueOf(localTypes.size())});
        ListIterator<String> it9 = localTypes.listIterator();
        while (it9.hasNext()) {
            int i7 = it9.nextIndex();
            String val7 = it9.next();
            XMLNode typeNode = typesNode.addChild("type", new String[]{"id", "type", "count"}, new String[]{String.valueOf(i7), val7, String.valueOf(typeCounts.get(i7))});
            Iterator<XMLNode> it10 = typedLocals.get(i7).iterator();
            while (it10.hasNext()) {
                XMLNode n = it10.next();
                typeNode.addChild(n);
            }
        }
        labelsNode.addAttribute("count", String.valueOf(labelCount));
        XMLNode current = labelsNode.child;
        Iterator<XMLLabel> it11 = xmlLabelsList.iterator();
        while (it11.hasNext()) {
            XMLLabel tempLabel = it11.next();
            tempLabel.stmtPercentage = (((float) tempLabel.stmtPercentage) / ((float) maxStmtCount)) * 100.0f;
            if (current != null) {
                current.addAttribute("stmtcount", String.valueOf(tempLabel.stmtCount));
                current.addAttribute("stmtpercentage", String.valueOf(tempLabel.stmtPercentage));
                current = current.next;
            }
        }
        int j4 = 0;
        XMLNode exceptionsNode = methodNode.addChild("exceptions");
        for (Trap trap : body.getTraps()) {
            int i8 = j4;
            j4++;
            XMLNode catchNode = exceptionsNode.addChild("exception", new String[]{"id", "method", "type"}, new String[]{String.valueOf(i8), cleanMethodName, Scene.v().quotedNameOf(trap.getException().getName())});
            catchNode.addChild("begin", new String[]{"label"}, new String[]{stmtToName.get(trap.getBeginUnit())});
            catchNode.addChild("end", new String[]{"label"}, new String[]{stmtToName.get(trap.getEndUnit())});
            catchNode.addChild("handler", new String[]{"label"}, new String[]{stmtToName.get(trap.getHandlerUnit())});
        }
        exceptionsNode.addAttribute("count", String.valueOf(exceptionsNode.getNumberOfChildren()));
    }

    private static String cleanMethod(String str) {
        return str.trim().replace('<', '_').replace('>', '_');
    }

    private static String cleanLocal(String str) {
        return str.trim();
    }

    private static String toCDATA(String str) {
        return "<![CDATA[" + str.replaceAll("]]>", "]]&gt;") + "]]>";
    }

    private static String boolToString(boolean bool) {
        return bool ? "true" : "false";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/xml/XMLPrinter$XMLLabel.class */
    public static class XMLLabel {
        public final long id;
        public final String methodName;
        public final String label;
        public long stmtCount;
        public long stmtPercentage;

        public XMLLabel(long in_id, String in_methodName, String in_label) {
            this.id = in_id;
            this.methodName = in_methodName;
            this.label = in_label;
        }
    }
}
