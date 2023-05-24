package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.selectors.BaseExtendSelector;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.ResourceUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/ModifiedSelector.class */
public class ModifiedSelector extends BaseExtendSelector implements BuildListener, ResourceSelector {
    private static final String CACHE_PREFIX = "cache.";
    private static final String ALGORITHM_PREFIX = "algorithm.";
    private static final String COMPARATOR_PREFIX = "comparator.";
    private String cacheClass;
    private String algorithmClass;
    private String comparatorClass;
    private CacheName cacheName = null;
    private AlgorithmName algoName = null;
    private ComparatorName compName = null;
    private boolean update = true;
    private boolean selectDirectories = true;
    private boolean selectResourcesWithoutInputStream = true;
    private boolean delayUpdate = true;
    private Comparator<? super String> comparator = null;
    private Algorithm algorithm = null;
    private Cache cache = null;
    private int modified = 0;
    private boolean isConfigured = false;
    private List<Parameter> configParameter = Collections.synchronizedList(new ArrayList());
    private List<Parameter> specialParameter = Collections.synchronizedList(new ArrayList());
    private ClassLoader myClassLoader = null;
    private Path classpath = null;

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        configure();
        if (this.cache == null) {
            setError("Cache must be set.");
        } else if (this.algorithm == null) {
            setError("Algorithm must be set.");
        } else if (!this.cache.isValid()) {
            setError("Cache must be proper configured.");
        } else if (!this.algorithm.isValid()) {
            setError("Algorithm must be proper configured.");
        }
    }

    public void configure() {
        File cachefile;
        if (this.isConfigured) {
            return;
        }
        this.isConfigured = true;
        Project p = getProject();
        if (p != null) {
            cachefile = new File(p.getBaseDir(), "cache.properties");
            getProject().addBuildListener(this);
        } else {
            cachefile = new File("cache.properties");
            setDelayUpdate(false);
        }
        Cache defaultCache = new PropertiesfileCache(cachefile);
        Algorithm defaultAlgorithm = new DigestAlgorithm();
        Comparator<? super String> defaultComparator = new EqualComparator();
        for (Parameter parameter : this.configParameter) {
            if (parameter.getName().indexOf(46) > 0) {
                this.specialParameter.add(parameter);
            } else {
                useParameter(parameter);
            }
        }
        this.configParameter.clear();
        if (this.algoName != null) {
            if ("hashvalue".equals(this.algoName.getValue())) {
                this.algorithm = new HashvalueAlgorithm();
            } else if ("digest".equals(this.algoName.getValue())) {
                this.algorithm = new DigestAlgorithm();
            } else if ("checksum".equals(this.algoName.getValue())) {
                this.algorithm = new ChecksumAlgorithm();
            } else if ("lastmodified".equals(this.algoName.getValue())) {
                this.algorithm = new LastModifiedAlgorithm();
            }
        } else if (this.algorithmClass != null) {
            this.algorithm = (Algorithm) loadClass(this.algorithmClass, "is not an Algorithm.", Algorithm.class);
        } else {
            this.algorithm = defaultAlgorithm;
        }
        if (this.cacheName != null) {
            if ("propertyfile".equals(this.cacheName.getValue())) {
                this.cache = new PropertiesfileCache();
            }
        } else if (this.cacheClass != null) {
            this.cache = (Cache) loadClass(this.cacheClass, "is not a Cache.", Cache.class);
        } else {
            this.cache = defaultCache;
        }
        if (this.compName != null) {
            if ("equal".equals(this.compName.getValue())) {
                this.comparator = new EqualComparator();
            } else if ("rule".equals(this.compName.getValue())) {
                throw new BuildException("RuleBasedCollator not yet supported.");
            }
        } else if (this.comparatorClass != null) {
            Comparator<? super String> localComparator = (Comparator) loadClass(this.comparatorClass, "is not a Comparator.", Comparator.class);
            this.comparator = localComparator;
        } else {
            this.comparator = defaultComparator;
        }
        this.specialParameter.forEach(this::useParameter);
        this.specialParameter.clear();
    }

    protected <T> T loadClass(String classname, String msg, Class<? extends T> type) {
        Class<?> clazz;
        try {
            ClassLoader cl = getClassLoader();
            if (cl != null) {
                clazz = cl.loadClass(classname);
            } else {
                clazz = Class.forName(classname);
            }
            T t = (T) clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            if (!type.isInstance(t)) {
                throw new BuildException("Specified class (%s) %s", classname, msg);
            }
            return t;
        } catch (ClassNotFoundException e) {
            throw new BuildException("Specified class (%s) not found.", classname);
        } catch (Exception e2) {
            throw new BuildException(e2);
        }
    }

    @Override // org.apache.tools.ant.types.selectors.FileSelector, org.apache.tools.ant.types.resources.selectors.ResourceSelector
    public boolean isSelected(Resource resource) {
        if (resource.isFilesystemOnly()) {
            FileResource fileResource = (FileResource) resource;
            File file = fileResource.getFile();
            String filename = fileResource.getName();
            File basedir = fileResource.getBaseDir();
            return isSelected(basedir, filename, file);
        }
        try {
            FileUtils fu = FileUtils.getFileUtils();
            File tmpFile = fu.createTempFile(getProject(), "modified-", ".tmp", null, true, false);
            Resource tmpResource = new FileResource(tmpFile);
            ResourceUtils.copyResource(resource, tmpResource);
            boolean isSelected = isSelected(tmpFile.getParentFile(), tmpFile.getName(), resource.toLongString());
            tmpFile.delete();
            return isSelected;
        } catch (UnsupportedOperationException e) {
            log("The resource '" + resource.getName() + "' does not provide an InputStream, so it is not checked. According to 'selres' attribute value it is " + (this.selectResourcesWithoutInputStream ? "" : " not") + "selected.", 2);
            return this.selectResourcesWithoutInputStream;
        } catch (Exception e2) {
            throw new BuildException(e2);
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        return isSelected(basedir, filename, file.getAbsolutePath());
    }

    private boolean isSelected(File basedir, String filename, String cacheKey) {
        validate();
        File f = new File(basedir, filename);
        if (f.isDirectory()) {
            return this.selectDirectories;
        }
        String cachedValue = String.valueOf(this.cache.get(f.getAbsolutePath()));
        String newValue = this.algorithm.getValue(f);
        boolean rv = this.comparator.compare(cachedValue, newValue) != 0;
        if (this.update && rv) {
            this.cache.put(f.getAbsolutePath(), newValue);
            setModified(getModified() + 1);
            if (!getDelayUpdate()) {
                saveCache();
            }
        }
        return rv;
    }

    protected void saveCache() {
        if (getModified() > 0) {
            this.cache.save();
            setModified(0);
        }
    }

    public void setAlgorithmClass(String classname) {
        this.algorithmClass = classname;
    }

    public void setComparatorClass(String classname) {
        this.comparatorClass = classname;
    }

    public void setCacheClass(String classname) {
        this.cacheClass = classname;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setSeldirs(boolean seldirs) {
        this.selectDirectories = seldirs;
    }

    public void setSelres(boolean newValue) {
        this.selectResourcesWithoutInputStream = newValue;
    }

    public int getModified() {
        return this.modified;
    }

    public void setModified(int modified) {
        this.modified = modified;
    }

    public boolean getDelayUpdate() {
        return this.delayUpdate;
    }

    public void setDelayUpdate(boolean delayUpdate) {
        this.delayUpdate = delayUpdate;
    }

    public void addClasspath(Path path) {
        if (this.classpath != null) {
            throw new BuildException("<classpath> can be set only once.");
        }
        this.classpath = path;
    }

    public ClassLoader getClassLoader() {
        ClassLoader createClassLoader;
        if (this.myClassLoader == null) {
            if (this.classpath == null) {
                createClassLoader = getClass().getClassLoader();
            } else {
                createClassLoader = getProject().createClassLoader(this.classpath);
            }
            this.myClassLoader = createClassLoader;
        }
        return this.myClassLoader;
    }

    public void setClassLoader(ClassLoader loader) {
        this.myClassLoader = loader;
    }

    public void addParam(String key, Object value) {
        Parameter par = new Parameter();
        par.setName(key);
        par.setValue(String.valueOf(value));
        this.configParameter.add(par);
    }

    public void addParam(Parameter parameter) {
        this.configParameter.add(parameter);
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        if (parameters != null) {
            Collections.addAll(this.configParameter, parameters);
        }
    }

    public void useParameter(Parameter parameter) {
        String key = parameter.getName();
        String value = parameter.getValue();
        if ("cache".equals(key)) {
            CacheName cn = new CacheName();
            cn.setValue(value);
            setCache(cn);
        } else if ("algorithm".equals(key)) {
            AlgorithmName an = new AlgorithmName();
            an.setValue(value);
            setAlgorithm(an);
        } else if ("comparator".equals(key)) {
            ComparatorName cn2 = new ComparatorName();
            cn2.setValue(value);
            setComparator(cn2);
        } else if ("update".equals(key)) {
            setUpdate("true".equalsIgnoreCase(value));
        } else if ("delayupdate".equals(key)) {
            setDelayUpdate("true".equalsIgnoreCase(value));
        } else if ("seldirs".equals(key)) {
            setSeldirs("true".equalsIgnoreCase(value));
        } else if (key.startsWith(CACHE_PREFIX)) {
            String name = key.substring(CACHE_PREFIX.length());
            tryToSetAParameter(this.cache, name, value);
        } else if (key.startsWith(ALGORITHM_PREFIX)) {
            String name2 = key.substring(ALGORITHM_PREFIX.length());
            tryToSetAParameter(this.algorithm, name2, value);
        } else if (key.startsWith(COMPARATOR_PREFIX)) {
            String name3 = key.substring(COMPARATOR_PREFIX.length());
            tryToSetAParameter(this.comparator, name3, value);
        } else {
            setError("Invalid parameter " + key);
        }
    }

    protected void tryToSetAParameter(Object obj, String name, String value) {
        Project prj = getProject() != null ? getProject() : new Project();
        IntrospectionHelper iHelper = IntrospectionHelper.getHelper(prj, obj.getClass());
        try {
            iHelper.setAttribute(prj, obj, name, value);
        } catch (BuildException e) {
        }
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return String.format("{modifiedselector update=%s seldirs=%s cache=%s algorithm=%s comparator=%s}", Boolean.valueOf(this.update), Boolean.valueOf(this.selectDirectories), this.cache, this.algorithm, this.comparator);
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        if (getDelayUpdate()) {
            saveCache();
        }
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
        if (getDelayUpdate()) {
            saveCache();
        }
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
        if (getDelayUpdate()) {
            saveCache();
        }
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
    }

    public Cache getCache() {
        return this.cache;
    }

    public void setCache(CacheName name) {
        this.cacheName = name;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/ModifiedSelector$CacheName.class */
    public static class CacheName extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"propertyfile"};
        }
    }

    public Algorithm getAlgorithm() {
        return this.algorithm;
    }

    public void setAlgorithm(AlgorithmName name) {
        this.algoName = name;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/ModifiedSelector$AlgorithmName.class */
    public static class AlgorithmName extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"hashvalue", "digest", "checksum", "lastmodified"};
        }
    }

    public Comparator<? super String> getComparator() {
        return this.comparator;
    }

    public void setComparator(ComparatorName name) {
        this.compName = name;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/ModifiedSelector$ComparatorName.class */
    public static class ComparatorName extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"equal", "rule"};
        }
    }
}
