package soot.jimple.infoflow.android.manifest;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParserException;
import soot.jimple.infoflow.android.axml.AXmlAttribute;
import soot.jimple.infoflow.android.axml.AXmlDocument;
import soot.jimple.infoflow.android.axml.AXmlHandler;
import soot.jimple.infoflow.android.axml.AXmlNode;
import soot.jimple.infoflow.android.axml.ApkHandler;
import soot.jimple.infoflow.android.manifest.IActivity;
import soot.jimple.infoflow.android.manifest.IBroadcastReceiver;
import soot.jimple.infoflow.android.manifest.IContentProvider;
import soot.jimple.infoflow.android.manifest.IService;
import soot.jimple.infoflow.android.manifest.binary.BinaryAndroidApplication;
import soot.jimple.infoflow.android.manifest.containers.EagerComponentContainer;
import soot.jimple.infoflow.android.manifest.containers.EmptyComponentContainer;
import soot.jimple.infoflow.android.resources.ARSCFileParser;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/BaseProcessManifest.class */
public abstract class BaseProcessManifest<A extends IActivity, S extends IService, C extends IContentProvider, B extends IBroadcastReceiver> implements IManifestHandler<A, S, C, B> {
    protected ApkHandler apk;
    protected AXmlHandler axml;
    protected ARSCFileParser arscParser;
    protected AXmlNode manifest;
    protected AXmlNode application;
    protected List<AXmlNode> providers;
    protected List<AXmlNode> services;
    protected List<AXmlNode> activities;
    protected List<AXmlNode> aliasActivities;
    protected List<AXmlNode> receivers;
    protected IComponentFactory<A, S, C, B> factory;
    private String cache_PackageName;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/BaseProcessManifest$IComponentFactory.class */
    protected interface IComponentFactory<A extends IActivity, S extends IService, C extends IContentProvider, B extends IBroadcastReceiver> {
        A createActivity(AXmlNode aXmlNode);

        B createBroadcastReceiver(AXmlNode aXmlNode);

        C createContentProvider(AXmlNode aXmlNode);

        S createService(AXmlNode aXmlNode);
    }

    protected abstract IComponentFactory<A, S, C, B> createComponentFactory();

    public BaseProcessManifest(String apkPath) throws IOException, XmlPullParserException {
        this(new File(apkPath));
    }

    public BaseProcessManifest(File apkFile) throws IOException, XmlPullParserException {
        this(apkFile, ARSCFileParser.getInstance(apkFile));
    }

    public BaseProcessManifest(File apkFile, ARSCFileParser arscParser) throws IOException, XmlPullParserException {
        this.apk = null;
        this.providers = null;
        this.services = null;
        this.activities = null;
        this.aliasActivities = null;
        this.receivers = null;
        this.factory = createComponentFactory();
        this.cache_PackageName = null;
        if (!apkFile.exists()) {
            throw new RuntimeException(String.format("The given APK file %s does not exist", apkFile.getCanonicalPath()));
        }
        this.apk = new ApkHandler(apkFile);
        this.arscParser = arscParser;
        Throwable th = null;
        try {
            InputStream is = this.apk.getInputStream("AndroidManifest.xml");
            if (is == null) {
                throw new FileNotFoundException(String.format("The file %s does not contain an Android Manifest", apkFile.getAbsolutePath()));
            }
            handle(is);
            if (is != null) {
                is.close();
            }
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public BaseProcessManifest(InputStream manifestIS, ARSCFileParser arscParser) throws IOException, XmlPullParserException {
        this.apk = null;
        this.providers = null;
        this.services = null;
        this.activities = null;
        this.aliasActivities = null;
        this.receivers = null;
        this.factory = createComponentFactory();
        this.cache_PackageName = null;
        this.arscParser = arscParser;
        handle(manifestIS);
    }

    protected void handle(InputStream manifestIS) throws IOException, XmlPullParserException {
        this.axml = new AXmlHandler(manifestIS);
        AXmlDocument document = this.axml.getDocument();
        this.manifest = document.getRootNode();
        if (!this.manifest.getTag().equals("manifest")) {
            throw new RuntimeException("Root node is not a manifest node");
        }
        List<AXmlNode> applications = this.manifest.getChildrenWithTag("application");
        if (applications.isEmpty()) {
            throw new RuntimeException("Manifest contains no application node");
        }
        if (applications.size() > 1) {
            throw new RuntimeException("Manifest contains more than one application node");
        }
        this.application = applications.get(0);
        this.providers = this.axml.getNodesWithTag("provider");
        this.services = this.axml.getNodesWithTag("service");
        this.activities = this.axml.getNodesWithTag(Context.ACTIVITY_SERVICE);
        this.aliasActivities = this.axml.getNodesWithTag("activity-alias");
        this.receivers = this.axml.getNodesWithTag("receiver");
    }

    public AXmlHandler getAXml() {
        return this.axml;
    }

    public ApkHandler getApk() {
        return this.apk;
    }

    public AXmlNode getManifest() {
        return this.manifest;
    }

    @Override // soot.jimple.infoflow.android.manifest.IManifestHandler
    public IAndroidApplication getApplication() {
        return new BinaryAndroidApplication(this.application, this);
    }

    @Override // soot.jimple.infoflow.android.manifest.IManifestHandler
    public IComponentContainer<C> getContentProviders() {
        if (this.providers == null) {
            return EmptyComponentContainer.get();
        }
        return new EagerComponentContainer((Collection) this.providers.stream().map(p -> {
            return this.factory.createContentProvider(p);
        }).collect(Collectors.toList()));
    }

    @Override // soot.jimple.infoflow.android.manifest.IManifestHandler
    public IComponentContainer<S> getServices() {
        if (this.services == null) {
            return EmptyComponentContainer.get();
        }
        return new EagerComponentContainer((Collection) this.services.stream().map(s -> {
            return this.factory.createService(s);
        }).collect(Collectors.toList()));
    }

    public ComponentType getComponentType(String className) {
        for (AXmlNode node : this.activities) {
            if (node.getAttribute("name").getValue().equals(className)) {
                return ComponentType.Activity;
            }
        }
        for (AXmlNode node2 : this.services) {
            if (node2.getAttribute("name").getValue().equals(className)) {
                return ComponentType.Service;
            }
        }
        for (AXmlNode node3 : this.receivers) {
            if (node3.getAttribute("name").getValue().equals(className)) {
                return ComponentType.BroadcastReceiver;
            }
        }
        for (AXmlNode node4 : this.providers) {
            if (node4.getAttribute("name").getValue().equals(className)) {
                return ComponentType.ContentProvider;
            }
        }
        return null;
    }

    @Override // soot.jimple.infoflow.android.manifest.IManifestHandler
    public IComponentContainer<A> getActivities() {
        if (this.activities == null) {
            return EmptyComponentContainer.get();
        }
        return new EagerComponentContainer((Collection) this.activities.stream().map(a -> {
            return this.factory.createActivity(a);
        }).collect(Collectors.toList()));
    }

    public List<AXmlNode> getAliasActivities() {
        return new ArrayList(this.aliasActivities);
    }

    @Override // soot.jimple.infoflow.android.manifest.IManifestHandler
    public IComponentContainer<B> getBroadcastReceivers() {
        if (this.receivers == null) {
            return EmptyComponentContainer.get();
        }
        return new EagerComponentContainer((Collection) this.receivers.stream().map(r -> {
            return this.factory.createBroadcastReceiver(r);
        }).collect(Collectors.toList()));
    }

    public AXmlNode getProvider(String name) {
        return getNodeWithName(this.providers, name);
    }

    public AXmlNode getService(String name) {
        return getNodeWithName(this.services, name);
    }

    public AXmlNode getActivity(String name) {
        return getNodeWithName(this.activities, name);
    }

    public AXmlNode getAliasActivity(String name) {
        return getNodeWithName(this.aliasActivities, name);
    }

    public AXmlNode getReceiver(String name) {
        return getNodeWithName(this.receivers, name);
    }

    protected AXmlNode getNodeWithName(List<AXmlNode> list, String name) {
        for (AXmlNode node : list) {
            Object attr = node.getAttributes().get("name");
            if (attr != null && ((AXmlAttribute) attr).getValue().equals(name)) {
                return node;
            }
        }
        return null;
    }

    public AXmlNode getAliasActivityTarget(AXmlNode aliasActivity) {
        AXmlAttribute<?> targetActivityAttribute;
        if (isAliasActivity(aliasActivity) && (targetActivityAttribute = aliasActivity.getAttribute("targetActivity")) != null) {
            return getActivity((String) targetActivityAttribute.getValue());
        }
        return null;
    }

    public static boolean isAliasActivity(AXmlNode activity) {
        return activity.getTag().equals("activity-alias");
    }

    public ArrayList<AXmlNode> getAllActivities() {
        ArrayList<AXmlNode> allActivities = new ArrayList<>(this.activities);
        allActivities.addAll(this.aliasActivities);
        return allActivities;
    }

    public byte[] getOutput() {
        return this.axml.toByteArray();
    }

    @Override // soot.jimple.infoflow.android.manifest.IManifestHandler
    public String getPackageName() {
        AXmlAttribute<?> attr;
        if (this.cache_PackageName == null && (attr = this.manifest.getAttribute("package")) != null && attr.getValue() != null) {
            this.cache_PackageName = (String) attr.getValue();
        }
        return this.cache_PackageName;
    }

    public int getVersionCode() {
        AXmlAttribute<?> attr = this.manifest.getAttribute("versionCode");
        if (attr == null || attr.getValue() == null) {
            return -1;
        }
        return Integer.parseInt(attr.getValue().toString());
    }

    public String getVersionName() {
        AXmlAttribute<?> attr = this.manifest.getAttribute("versionName");
        if (attr == null || attr.getValue() == null) {
            return null;
        }
        return attr.getValue().toString();
    }

    public int getMinSdkVersion() {
        AXmlAttribute<?> attr;
        List<AXmlNode> usesSdk = this.manifest.getChildrenWithTag("uses-sdk");
        if (usesSdk == null || usesSdk.isEmpty() || (attr = usesSdk.get(0).getAttribute("minSdkVersion")) == null || attr.getValue() == null) {
            return -1;
        }
        if (attr.getValue() instanceof Integer) {
            return ((Integer) attr.getValue()).intValue();
        }
        return Integer.parseInt(attr.getValue().toString());
    }

    public int getTargetSdkVersion() {
        AXmlAttribute<?> attr;
        List<AXmlNode> usesSdk = this.manifest.getChildrenWithTag("uses-sdk");
        if (usesSdk == null || usesSdk.isEmpty() || (attr = usesSdk.get(0).getAttribute("targetSdkVersion")) == null || attr.getValue() == null) {
            return -1;
        }
        if (attr.getValue() instanceof Integer) {
            return ((Integer) attr.getValue()).intValue();
        }
        return Integer.parseInt(attr.getValue().toString());
    }

    public Set<String> getPermissions() {
        List<AXmlNode> usesPerms = this.manifest.getChildrenWithTag("uses-permission");
        Set<String> permissions = new HashSet<>();
        for (AXmlNode perm : usesPerms) {
            AXmlAttribute<?> attr = perm.getAttribute("name");
            if (attr != null) {
                permissions.add((String) attr.getValue());
            } else {
                for (AXmlAttribute<?> a : perm.getAttributes().values()) {
                    if (a.getType() == 3 && (a.getName() == null || a.getName().isEmpty())) {
                        permissions.add((String) a.getValue());
                    }
                }
            }
        }
        return permissions;
    }

    public Set<String> getIntentFilter() {
        List<AXmlNode> usesActions = this.axml.getNodesWithTag(Camera.Parameters.SCENE_MODE_ACTION);
        Set<String> intentFilters = new HashSet<>();
        for (AXmlNode ittft : usesActions) {
            if (ittft.getParent().getTag().equals("intent-filter")) {
                AXmlAttribute<?> attr = ittft.getAttribute("name");
                if (attr != null) {
                    intentFilters.add(attr.getValue().toString());
                } else {
                    for (AXmlAttribute<?> a : ittft.getAttributes().values()) {
                        if (a.getType() == 3 && (a.getName() == null || a.getName().isEmpty())) {
                            intentFilters.add((String) a.getValue());
                        }
                    }
                }
            }
        }
        return intentFilters;
    }

    public Set<String> getHardware() {
        List<AXmlNode> usesHardware = this.manifest.getChildrenWithTag("uses-feature");
        Set<String> hardware = new HashSet<>();
        for (AXmlNode hard : usesHardware) {
            AXmlAttribute<?> attr = hard.getAttribute("name");
            if (attr != null) {
                hardware.add(attr.getValue().toString());
            } else {
                for (AXmlAttribute<?> a : hard.getAttributes().values()) {
                    if (a.getType() == 3 && (a.getName() == null || a.getName().isEmpty())) {
                        hardware.add(a.getValue().toString());
                    }
                }
            }
        }
        return hardware;
    }

    public void addPermission(String permissionName) {
        AXmlNode permission = new AXmlNode("uses-permission", null, this.manifest, "");
        AXmlAttribute<String> permissionNameAttr = new AXmlAttribute<>("name", permissionName, AXmlHandler.ANDROID_NAMESPACE);
        permission.addAttribute(permissionNameAttr);
    }

    public void addProvider(AXmlNode node) {
        if (this.providers.isEmpty()) {
            this.providers = new ArrayList();
        }
        this.providers.add(node);
    }

    public void addReceiver(AXmlNode node) {
        if (this.receivers.isEmpty()) {
            this.receivers = new ArrayList();
        }
        this.receivers.add(node);
    }

    public void addActivity(AXmlNode node) {
        if (this.activities.isEmpty()) {
            this.activities = new ArrayList();
        }
        this.activities.add(node);
    }

    public void addService(AXmlNode node) {
        if (this.services.isEmpty()) {
            this.services = new ArrayList();
        }
        this.services.add(node);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.apk != null) {
            this.apk.close();
        }
    }

    public Set<AXmlNode> getLaunchableActivityNodes() {
        Set<AXmlNode> allLaunchableActivities = new LinkedHashSet<>();
        Iterator<AXmlNode> it = getAllActivities().iterator();
        while (it.hasNext()) {
            AXmlNode activity = it.next();
            for (AXmlNode activityChildren : activity.getChildren()) {
                if (activityChildren.getTag().equals("intent-filter")) {
                    boolean actionFilter = false;
                    boolean categoryFilter = false;
                    for (AXmlNode intentFilter : activityChildren.getChildren()) {
                        if (intentFilter.getTag().equals(Camera.Parameters.SCENE_MODE_ACTION) && intentFilter.getAttribute("name").getValue().toString().equals(Intent.ACTION_MAIN)) {
                            actionFilter = true;
                        } else if (intentFilter.getTag().equals("category") && intentFilter.getAttribute("name").getValue().toString().equals(Intent.CATEGORY_LAUNCHER)) {
                            categoryFilter = true;
                        }
                    }
                    if (actionFilter && categoryFilter) {
                        allLaunchableActivities.add(activity);
                    }
                }
            }
        }
        return allLaunchableActivities;
    }

    public String expandClassName(String className) {
        String packageName = getPackageName();
        if (className.startsWith(".")) {
            return String.valueOf(packageName) + className;
        }
        if (!className.contains(".")) {
            return String.valueOf(packageName) + "." + className;
        }
        return className;
    }

    public ARSCFileParser getArscParser() {
        return this.arscParser;
    }
}
