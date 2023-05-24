package org.apache.tools.ant.taskdefs.optional.extension.resolvers;

import java.io.File;
import java.net.URL;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Get;
import org.apache.tools.ant.taskdefs.optional.extension.Extension;
import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/resolvers/URLResolver.class */
public class URLResolver implements ExtensionResolver {
    private File destfile;
    private File destdir;
    private URL url;

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setDestfile(File destfile) {
        this.destfile = destfile;
    }

    public void setDestdir(File destdir) {
        this.destdir = destdir;
    }

    @Override // org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver
    public File resolve(Extension extension, Project project) throws BuildException {
        validate();
        File file = getDest();
        Get get = new Get();
        get.setProject(project);
        get.setDest(file);
        get.setSrc(this.url);
        get.execute();
        return file;
    }

    private File getDest() {
        String filename;
        File result;
        if (null != this.destfile) {
            result = this.destfile;
        } else {
            String file = this.url.getFile();
            if (null == file || file.length() <= 1) {
                filename = "default.file";
            } else {
                int index = file.lastIndexOf(47);
                if (-1 == index) {
                    index = 0;
                }
                filename = file.substring(index);
            }
            result = new File(this.destdir, filename);
        }
        return result;
    }

    private void validate() {
        if (null == this.url) {
            throw new BuildException("Must specify URL");
        }
        if (null == this.destdir && null == this.destfile) {
            throw new BuildException("Must specify destination file or directory");
        }
        if (null != this.destdir && null != this.destfile) {
            throw new BuildException("Must not specify both destination file or directory");
        }
    }

    public String toString() {
        return "URL[" + this.url + "]";
    }
}
