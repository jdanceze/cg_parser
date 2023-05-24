package org.apache.tools.ant.types.resources;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Union.class */
public class Union extends BaseResourceCollectionContainer {
    public static Union getInstance(ResourceCollection rc) {
        return rc instanceof Union ? (Union) rc : new Union(rc);
    }

    public Union() {
    }

    public Union(Project project) {
        super(project);
    }

    public Union(ResourceCollection rc) {
        this(Project.getProject(rc), rc);
    }

    public Union(Project project, ResourceCollection rc) {
        super(project);
        add(rc);
    }

    public String[] list() {
        if (isReference()) {
            return getRef().list();
        }
        return (String[]) streamResources().map((v0) -> {
            return v0.toString();
        }).toArray(x$0 -> {
            return new String[x$0];
        });
    }

    public Resource[] listResources() {
        if (isReference()) {
            return getRef().listResources();
        }
        return (Resource[]) streamResources().toArray(x$0 -> {
            return new Resource[x$0];
        });
    }

    @Override // org.apache.tools.ant.types.resources.BaseResourceCollectionContainer
    protected Collection<Resource> getCollection() {
        return getAllResources();
    }

    @Deprecated
    protected <T> Collection<T> getCollection(boolean asString) {
        return asString ? (Collection<T>) getAllToStrings() : getAllResources();
    }

    protected Collection<String> getAllToStrings() {
        return (Collection) streamResources((v0) -> {
            return v0.toString();
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    protected Set<Resource> getAllResources() {
        return (Set) streamResources().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Union getRef() {
        return (Union) getCheckedRef(Union.class);
    }

    private Stream<? extends Resource> streamResources() {
        return streamResources(Function.identity());
    }

    private <T> Stream<? extends T> streamResources(Function<? super Resource, ? extends T> mapper) {
        return getResourceCollections().stream().flatMap((v0) -> {
            return v0.stream();
        }).map(mapper).distinct();
    }
}
