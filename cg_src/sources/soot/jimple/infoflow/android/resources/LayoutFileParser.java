package soot.jimple.infoflow.android.resources;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.FastHierarchy;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.Transform;
import soot.jimple.infoflow.android.axml.AXmlAttribute;
import soot.jimple.infoflow.android.axml.AXmlHandler;
import soot.jimple.infoflow.android.axml.AXmlNode;
import soot.jimple.infoflow.android.axml.parsers.AXML20Parser;
import soot.jimple.infoflow.android.resources.ARSCFileParser;
import soot.jimple.infoflow.android.resources.controls.AndroidLayoutControl;
import soot.jimple.infoflow.android.resources.controls.LayoutControlFactory;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/resources/LayoutFileParser.class */
public class LayoutFileParser extends AbstractResourceParser {
    protected final String packageName;
    protected final ARSCFileParser resParser;
    protected final MultiMap<String, AndroidLayoutControl> userControls = new HashMultiMap();
    protected final MultiMap<String, String> callbackMethods = new HashMultiMap();
    protected final MultiMap<String, String> includeDependencies = new HashMultiMap();
    protected final MultiMap<String, SootClass> fragments = new HashMultiMap();
    private boolean loadOnlySensitiveControls = false;
    private SootClass scViewGroup = null;
    private SootClass scView = null;
    private SootClass scWebView = null;
    private LayoutControlFactory controlFactory = new LayoutControlFactory();

    public LayoutFileParser(String packageName, ARSCFileParser resParser) {
        this.packageName = packageName;
        this.resParser = resParser;
    }

    private boolean isRealClass(SootClass sc) {
        if (sc == null) {
            return false;
        }
        return (sc.isPhantom() && sc.getMethodCount() == 0 && sc.getFieldCount() == 0) ? false : true;
    }

    private SootClass getLayoutClass(String className) {
        if (className.startsWith(";")) {
            className = className.substring(1);
        }
        if (className.contains("(") || className.contains("<") || className.contains("/")) {
            this.logger.warn("Invalid class name %s", className);
            return null;
        }
        SootClass sc = Scene.v().forceResolve(className, 3);
        if ((sc == null || sc.isPhantom()) && !this.packageName.isEmpty()) {
            sc = Scene.v().forceResolve(String.valueOf(this.packageName) + "." + className, 3);
        }
        if (!isRealClass(sc)) {
            sc = Scene.v().forceResolve("android.view." + className, 3);
        }
        if (!isRealClass(sc)) {
            sc = Scene.v().forceResolve("android.widget." + className, 3);
        }
        if (!isRealClass(sc)) {
            sc = Scene.v().forceResolve("android.webkit." + className, 3);
        }
        if (!isRealClass(sc)) {
            return null;
        }
        return sc;
    }

    private boolean isLayoutClass(SootClass theClass) {
        return theClass != null && Scene.v().getOrMakeFastHierarchy().canStoreType(theClass.getType(), this.scViewGroup.getType());
    }

    private boolean isViewClass(SootClass theClass) {
        if (theClass == null) {
            return false;
        }
        FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
        if (this.scView != null && fh.canStoreType(theClass.getType(), this.scView.getType())) {
            return true;
        }
        if (this.scWebView != null && fh.canStoreType(theClass.getType(), this.scWebView.getType())) {
            return true;
        }
        this.logger.warn(String.format("Layout class %s is not derived from android.view.View", theClass.getName()));
        return false;
    }

    private void addCallbackMethod(String layoutFile, String callback) {
        String layoutFile2 = layoutFile.replace("/layout-large/", "/layout/");
        this.callbackMethods.put(layoutFile2, callback);
        if (this.includeDependencies.containsKey(layoutFile2)) {
            for (String target : this.includeDependencies.get(layoutFile2)) {
                addCallbackMethod(target, callback);
            }
        }
    }

    private void addFragment(String layoutFile, SootClass fragment) {
        if (fragment == null) {
            return;
        }
        String layoutFile2 = layoutFile.replace("/layout-large/", "/layout/");
        this.fragments.put(layoutFile2, fragment);
        if (this.includeDependencies.containsKey(layoutFile2)) {
            for (String target : this.includeDependencies.get(layoutFile2)) {
                addFragment(target, fragment);
            }
        }
    }

    public void parseLayoutFile(final String fileName) {
        Transform transform = new Transform("wjtp.lfp", new SceneTransformer() { // from class: soot.jimple.infoflow.android.resources.LayoutFileParser.1
            @Override // soot.SceneTransformer
            protected void internalTransform(String phaseName, Map options) {
                LayoutFileParser.this.parseLayoutFileDirect(fileName);
            }
        });
        PackManager.v().getPack("wjtp").add(transform);
    }

    public void parseLayoutFileDirect(String fileName) {
        handleAndroidResourceFiles(fileName, null, new IResourceHandler() { // from class: soot.jimple.infoflow.android.resources.LayoutFileParser.2
            @Override // soot.jimple.infoflow.android.resources.IResourceHandler
            public void handleResourceFile(String fileName2, Set<String> fileNameFilter, InputStream stream) {
                if (!fileName2.startsWith("res/layout") && !fileName2.startsWith("res/navigation")) {
                    return;
                }
                if (fileName2.endsWith(".xml")) {
                    LayoutFileParser.this.scViewGroup = Scene.v().getSootClassUnsafe("android.view.ViewGroup");
                    LayoutFileParser.this.scView = Scene.v().getSootClassUnsafe("android.view.View");
                    LayoutFileParser.this.scWebView = Scene.v().getSootClassUnsafe("android.webkit.WebView");
                    String entryClass = fileName2.substring(0, fileName2.lastIndexOf("."));
                    if (!LayoutFileParser.this.packageName.isEmpty()) {
                        entryClass = String.valueOf(LayoutFileParser.this.packageName) + "." + entryClass;
                    }
                    if (fileNameFilter != null) {
                        boolean found = false;
                        Iterator<String> it = fileNameFilter.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            String s = it.next();
                            if (s.equalsIgnoreCase(entryClass)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            return;
                        }
                    }
                    try {
                        AXmlHandler handler = new AXmlHandler(stream, new AXML20Parser());
                        LayoutFileParser.this.parseLayoutNode(fileName2, handler.getDocument().getRootNode());
                        return;
                    } catch (Exception ex) {
                        LayoutFileParser.this.logger.error("Could not read binary XML file: " + ex.getMessage(), (Throwable) ex);
                        return;
                    }
                }
                LayoutFileParser.this.logger.warn(String.format("Skipping file %s in layout folder...", fileName2));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseLayoutNode(String layoutFile, AXmlNode rootNode) {
        if (rootNode.getTag() == null || rootNode.getTag().isEmpty()) {
            this.logger.warn("Encountered a null or empty node name in file %s, skipping node...", layoutFile);
            return;
        }
        String tname = rootNode.getTag().trim();
        if (!tname.equals("dummy")) {
            if (tname.equals("include")) {
                parseIncludeAttributes(layoutFile, rootNode);
            } else if (!tname.equals("merge")) {
                if (tname.equals("fragment")) {
                    AXmlAttribute<?> attr = rootNode.getAttribute("name");
                    if (attr == null) {
                        this.logger.warn("Fragment without class name or id detected");
                    } else if (rootNode.getAttribute("navGraph") != null) {
                        parseIncludeAttributes(layoutFile, rootNode);
                    } else {
                        addFragment(layoutFile, getLayoutClass(attr.getValue().toString()));
                        if (attr.getType() != 3) {
                            this.logger.warn("Invalid target resource " + attr.getValue() + "for fragment class value");
                        }
                        getLayoutClass(attr.getValue().toString());
                    }
                } else {
                    SootClass childClass = getLayoutClass(tname);
                    if (childClass != null && (isLayoutClass(childClass) || isViewClass(childClass))) {
                        parseLayoutAttributes(layoutFile, childClass, rootNode);
                    }
                }
            }
        }
        for (AXmlNode childNode : rootNode.getChildren()) {
            parseLayoutNode(layoutFile, childNode);
        }
    }

    private void parseIncludeAttributes(String layoutFile, AXmlNode rootNode) {
        for (Map.Entry<String, AXmlAttribute<?>> entry : rootNode.getAttributes().entrySet()) {
            String attrName = entry.getKey();
            if (attrName != null && !attrName.isEmpty()) {
                String attrName2 = attrName.trim();
                AXmlAttribute<?> attr = entry.getValue();
                if (attrName2.equals("layout") || attrName2.equals("navGraph")) {
                    if (attr.getType() == 1 || attr.getType() == 17) {
                        if (attr.getValue() instanceof Integer) {
                            ARSCFileParser.AbstractResource targetRes = this.resParser.findResource(((Integer) attr.getValue()).intValue());
                            if (targetRes == null) {
                                this.logger.warn("Target resource " + attr.getValue() + " for layout include not found");
                                return;
                            } else if (!(targetRes instanceof ARSCFileParser.StringResource)) {
                                this.logger.warn(String.format("Invalid target node for include tag in layout XML, was %s", targetRes.getClass().getName()));
                                return;
                            } else {
                                String targetFile = ((ARSCFileParser.StringResource) targetRes).getValue();
                                if (this.callbackMethods.containsKey(targetFile)) {
                                    for (String callback : this.callbackMethods.get(targetFile)) {
                                        addCallbackMethod(layoutFile, callback);
                                    }
                                } else {
                                    this.includeDependencies.put(targetFile, layoutFile);
                                }
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void parseLayoutAttributes(String layoutFile, SootClass layoutClass, AXmlNode rootNode) {
        AndroidLayoutControl lc = this.controlFactory.createLayoutControl(layoutFile, layoutClass, rootNode);
        if (lc.getClickListener() != null) {
            addCallbackMethod(layoutFile, lc.getClickListener());
        }
        if (!this.loadOnlySensitiveControls || lc.isSensitive()) {
            this.userControls.put(layoutFile, lc);
        }
    }

    public Map<Integer, AndroidLayoutControl> getUserControlsByID() {
        Map<Integer, AndroidLayoutControl> res = new HashMap<>();
        for (AndroidLayoutControl lc : this.userControls.values()) {
            if (lc.getID() != -1) {
                res.put(Integer.valueOf(lc.getID()), lc);
            }
        }
        return res;
    }

    public MultiMap<String, AndroidLayoutControl> getUserControls() {
        return this.userControls;
    }

    public MultiMap<String, String> getCallbackMethods() {
        return this.callbackMethods;
    }

    public MultiMap<String, SootClass> getFragments() {
        return this.fragments;
    }

    public boolean getLoadOnlySensitiveControls() {
        return this.loadOnlySensitiveControls;
    }

    public void setControlFactory(LayoutControlFactory controlFactory) {
        this.controlFactory = controlFactory;
    }
}
