package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.ClasspathUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/regexp/RegexpMatcherFactory.class */
public class RegexpMatcherFactory {
    public RegexpMatcher newRegexpMatcher() throws BuildException {
        return newRegexpMatcher(null);
    }

    public RegexpMatcher newRegexpMatcher(Project p) throws BuildException {
        String systemDefault;
        if (p == null) {
            systemDefault = System.getProperty(MagicNames.REGEXP_IMPL);
        } else {
            systemDefault = p.getProperty(MagicNames.REGEXP_IMPL);
        }
        if (systemDefault != null) {
            return createInstance(systemDefault);
        }
        return new Jdk14RegexpMatcher();
    }

    protected RegexpMatcher createInstance(String className) throws BuildException {
        return (RegexpMatcher) ClasspathUtils.newInstance(className, RegexpMatcherFactory.class.getClassLoader(), RegexpMatcher.class);
    }

    protected void testAvailability(String className) throws BuildException {
        try {
            Class.forName(className);
        } catch (Throwable t) {
            throw new BuildException(t);
        }
    }

    public static boolean regexpMatcherPresent(Project project) {
        try {
            new RegexpMatcherFactory().newRegexpMatcher(project);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }
}
