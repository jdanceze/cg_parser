package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Iterator;
import java.util.Properties;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/modifiedselector/PropertiesfileCache.class */
public class PropertiesfileCache implements Cache {
    private File cachefile;
    private Properties cache;
    private boolean cacheLoaded;
    private boolean cacheDirty;

    public PropertiesfileCache() {
        this.cachefile = null;
        this.cache = new Properties();
        this.cacheLoaded = false;
        this.cacheDirty = true;
    }

    public PropertiesfileCache(File cachefile) {
        this.cachefile = null;
        this.cache = new Properties();
        this.cacheLoaded = false;
        this.cacheDirty = true;
        this.cachefile = cachefile;
    }

    public void setCachefile(File file) {
        this.cachefile = file;
    }

    public File getCachefile() {
        return this.cachefile;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Cache
    public boolean isValid() {
        return this.cachefile != null;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Cache
    public void load() {
        if (this.cachefile != null && this.cachefile.isFile() && this.cachefile.canRead()) {
            try {
                BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(this.cachefile.toPath(), new OpenOption[0]));
                this.cache.load(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.cacheLoaded = true;
        this.cacheDirty = false;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Cache
    public void save() {
        if (!this.cacheDirty) {
            return;
        }
        if (this.cachefile != null && this.cache.propertyNames().hasMoreElements()) {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(this.cachefile.toPath(), new OpenOption[0]));
                this.cache.store(bos, (String) null);
                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.cacheDirty = false;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Cache
    public void delete() {
        this.cache = new Properties();
        this.cachefile.delete();
        this.cacheLoaded = true;
        this.cacheDirty = false;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Cache
    public Object get(Object key) {
        if (!this.cacheLoaded) {
            load();
        }
        try {
            return this.cache.getProperty(String.valueOf(key));
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Cache
    public void put(Object key, Object value) {
        this.cache.put(String.valueOf(key), String.valueOf(value));
        this.cacheDirty = true;
    }

    @Override // org.apache.tools.ant.types.selectors.modifiedselector.Cache
    public Iterator<String> iterator() {
        return this.cache.stringPropertyNames().iterator();
    }

    public String toString() {
        return String.format("<PropertiesfileCache:cachefile=%s;noOfEntries=%d>", this.cachefile, Integer.valueOf(this.cache.size()));
    }
}
