package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.URLProvider;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/CloseResources.class */
public class CloseResources extends Task {
    private Union resources = new Union();

    public void add(ResourceCollection rc) {
        this.resources.add(rc);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        Iterator<Resource> it = this.resources.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            URLProvider up = (URLProvider) r.as(URLProvider.class);
            if (up != null) {
                URL u = up.getURL();
                try {
                    FileUtils.close(u.openConnection());
                } catch (IOException e) {
                }
            }
        }
    }
}
