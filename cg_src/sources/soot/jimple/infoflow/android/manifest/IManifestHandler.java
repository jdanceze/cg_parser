package soot.jimple.infoflow.android.manifest;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import soot.jimple.infoflow.android.manifest.IActivity;
import soot.jimple.infoflow.android.manifest.IBroadcastReceiver;
import soot.jimple.infoflow.android.manifest.IContentProvider;
import soot.jimple.infoflow.android.manifest.IService;
import soot.jimple.infoflow.util.SystemClassHandler;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/manifest/IManifestHandler.class */
public interface IManifestHandler<A extends IActivity, S extends IService, C extends IContentProvider, B extends IBroadcastReceiver> extends Closeable {
    String getPackageName();

    IComponentContainer<? extends A> getActivities();

    IComponentContainer<? extends C> getContentProviders();

    IComponentContainer<? extends S> getServices();

    IComponentContainer<? extends B> getBroadcastReceivers();

    IAndroidApplication getApplication();

    default List<? extends IAndroidComponent> getAllComponents() {
        List<IAndroidComponent> components = new ArrayList<>();
        List<? extends IActivity> activities = getActivities().asList();
        if (activities != null && !activities.isEmpty()) {
            components.addAll(activities);
        }
        List<? extends IContentProvider> providers = getContentProviders().asList();
        if (providers != null && !providers.isEmpty()) {
            components.addAll(providers);
        }
        List<? extends IService> services = getServices().asList();
        if (services != null && !services.isEmpty()) {
            components.addAll(services);
        }
        List<? extends IBroadcastReceiver> receivers = getBroadcastReceivers().asList();
        if (receivers != null && !receivers.isEmpty()) {
            components.addAll(receivers);
        }
        return components;
    }

    default Set<String> getEntryPointClasses() {
        String appName;
        IAndroidApplication app = getApplication();
        if (app != null && !app.isEnabled()) {
            return Collections.emptySet();
        }
        Set<String> entryPoints = new HashSet<>();
        for (IAndroidComponent node : getAllComponents()) {
            checkAndAddComponent(entryPoints, node);
        }
        if (entryPoints.isEmpty()) {
            List<IAndroidComponent> allEnabled = (List) getAllComponents().stream().filter(c -> {
                return c.isEnabled();
            }).collect(Collectors.toList());
            allEnabled.forEach(e -> {
                entryPoints.add(e.getNameString());
            });
        }
        if (app != null && (appName = app.getName()) != null && !appName.isEmpty()) {
            entryPoints.add(appName);
        }
        return entryPoints;
    }

    default void checkAndAddComponent(Set<String> entryPoints, IAndroidComponent component) {
        String className;
        String packageName = String.valueOf(getPackageName()) + ".";
        if (component.isEnabled() && (className = component.getNameString()) != null && !className.isEmpty()) {
            if (className.startsWith(packageName) || !SystemClassHandler.v().isClassInSystemPackage(className)) {
                entryPoints.add(className);
            }
        }
    }
}
