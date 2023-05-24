package org.apache.http.impl.client;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/client/RedirectLocations.class */
public class RedirectLocations {
    private final Set<URI> uris = new HashSet();

    public boolean contains(URI uri) {
        return this.uris.contains(uri);
    }

    public void add(URI uri) {
        this.uris.add(uri);
    }

    public boolean remove(URI uri) {
        return this.uris.remove(uri);
    }
}
