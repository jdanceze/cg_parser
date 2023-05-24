package org.apache.tools.ant.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ScriptFixBSFPath.class */
public class ScriptFixBSFPath {
    private static final String UTIL_OPTIONAL_PACKAGE = "org.apache.tools.ant.util.optional";
    private static final String BSF_PACKAGE = "org.apache.bsf";
    private static final String BSF_MANAGER = "org.apache.bsf.BSFManager";
    private static final String BSF_SCRIPT_RUNNER = "org.apache.tools.ant.util.optional.ScriptRunner";
    private static final String[] BSF_LANGUAGES = {"js", "org.mozilla.javascript.Scriptable", "javascript", "org.mozilla.javascript.Scriptable", "jacl", "tcl.lang.Interp", "netrexx", "netrexx.lang.Rexx", "nrx", "netrexx.lang.Rexx", "jython", "org.python.core.Py", "py", "org.python.core.Py", "xslt", "org.apache.xpath.objects.XObject"};
    private static final Map<String, String> BSF_LANGUAGE_MAP = new HashMap();

    static {
        for (int i = 0; i < BSF_LANGUAGES.length; i += 2) {
            BSF_LANGUAGE_MAP.put(BSF_LANGUAGES[i], BSF_LANGUAGES[i + 1]);
        }
    }

    private File getClassSource(ClassLoader loader, String className) {
        return LoaderUtils.getResourceSource(loader, LoaderUtils.classNameToResource(className));
    }

    private File getClassSource(String className) {
        return getClassSource(getClass().getClassLoader(), className);
    }

    public void fixClassLoader(ClassLoader loader, String language) {
        if (loader == getClass().getClassLoader() || !(loader instanceof AntClassLoader)) {
            return;
        }
        ClassLoader myLoader = getClass().getClassLoader();
        AntClassLoader fixLoader = (AntClassLoader) loader;
        File bsfSource = getClassSource(BSF_MANAGER);
        boolean needMoveRunner = bsfSource == null;
        String languageClassName = BSF_LANGUAGE_MAP.get(language);
        boolean needMoveBsf = (bsfSource == null || languageClassName == null || LoaderUtils.classExists(myLoader, languageClassName) || !LoaderUtils.classExists(loader, languageClassName)) ? false : true;
        boolean needMoveRunner2 = needMoveRunner || needMoveBsf;
        if (bsfSource == null) {
            bsfSource = getClassSource(loader, BSF_MANAGER);
        }
        if (bsfSource == null) {
            throw new BuildException("Unable to find BSF classes for scripting");
        }
        if (needMoveBsf) {
            fixLoader.addPathComponent(bsfSource);
            fixLoader.addLoaderPackageRoot(BSF_PACKAGE);
        }
        if (needMoveRunner2) {
            fixLoader.addPathComponent(LoaderUtils.getResourceSource(fixLoader, LoaderUtils.classNameToResource(BSF_SCRIPT_RUNNER)));
            fixLoader.addLoaderPackageRoot(UTIL_OPTIONAL_PACKAGE);
        }
    }
}
