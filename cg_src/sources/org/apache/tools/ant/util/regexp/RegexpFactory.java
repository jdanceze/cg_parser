package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.ClasspathUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/regexp/RegexpFactory.class */
public class RegexpFactory extends RegexpMatcherFactory {
    public Regexp newRegexp() throws BuildException {
        return newRegexp(null);
    }

    public Regexp newRegexp(Project p) throws BuildException {
        String systemDefault;
        if (p == null) {
            systemDefault = System.getProperty(MagicNames.REGEXP_IMPL);
        } else {
            systemDefault = p.getProperty(MagicNames.REGEXP_IMPL);
        }
        if (systemDefault != null) {
            return createRegexpInstance(systemDefault);
        }
        return new Jdk14RegexpRegexp();
    }

    protected Regexp createRegexpInstance(String classname) throws BuildException {
        return (Regexp) ClasspathUtils.newInstance(classname, RegexpFactory.class.getClassLoader(), Regexp.class);
    }
}
