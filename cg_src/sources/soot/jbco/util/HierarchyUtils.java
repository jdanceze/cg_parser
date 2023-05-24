package soot.jbco.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/jbco/util/HierarchyUtils.class */
public final class HierarchyUtils {
    private HierarchyUtils() {
        throw new IllegalAccessError();
    }

    public static List<SootClass> getAllInterfacesOf(SootClass sc) {
        Hierarchy hierarchy = Scene.v().getActiveHierarchy();
        Stream empty = sc.isInterface() ? Stream.empty() : hierarchy.getSuperclassesOf(sc).stream().map(HierarchyUtils::getAllInterfacesOf).flatMap((v0) -> {
            return v0.stream();
        });
        Stream<SootClass> directInterfaces = Stream.concat(sc.getInterfaces().stream(), sc.getInterfaces().stream().map(HierarchyUtils::getAllInterfacesOf).flatMap((v0) -> {
            return v0.stream();
        }));
        return (List) Stream.concat(empty, directInterfaces).collect(Collectors.toList());
    }
}
