package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PathConvert.class */
public class PathConvert extends Task {
    private static boolean onWindows = Os.isFamily(Os.FAMILY_DOS);
    private Resources path = null;
    private Reference refid = null;
    private String targetOS = null;
    private boolean targetWindows = false;
    private boolean setonempty = true;
    private String property = null;
    private List<MapEntry> prefixMap = new Vector();
    private String pathSep = null;
    private String dirSep = null;
    private Mapper mapper = null;
    private boolean preserveDuplicates;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PathConvert$MapEntry.class */
    public class MapEntry {
        private String from = null;
        private String to = null;

        public MapEntry() {
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String apply(String elem) {
            if (this.from != null && this.to != null) {
                String cmpElem = PathConvert.onWindows ? elem.toLowerCase().replace('\\', '/') : elem;
                String cmpFrom = PathConvert.onWindows ? this.from.toLowerCase().replace('\\', '/') : this.from;
                return cmpElem.startsWith(cmpFrom) ? this.to + elem.substring(this.from.length()) : elem;
            }
            throw new BuildException("Both 'from' and 'to' must be set in a map entry");
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/PathConvert$TargetOs.class */
    public static class TargetOs extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{Os.FAMILY_WINDOWS, Os.FAMILY_UNIX, Os.FAMILY_NETWARE, Os.FAMILY_OS2, Os.FAMILY_TANDEM};
        }
    }

    public Path createPath() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        Path result = new Path(getProject());
        add(result);
        return result;
    }

    public void add(ResourceCollection rc) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        getPath().add(rc);
    }

    private synchronized Resources getPath() {
        if (this.path == null) {
            this.path = new Resources(getProject());
            this.path.setCache(true);
        }
        return this.path;
    }

    public MapEntry createMap() {
        MapEntry entry = new MapEntry();
        this.prefixMap.add(entry);
        return entry;
    }

    @Deprecated
    public void setTargetos(String target) {
        TargetOs to = new TargetOs();
        to.setValue(target);
        setTargetos(to);
    }

    public void setTargetos(TargetOs target) {
        this.targetOS = target.getValue();
        this.targetWindows = (Os.FAMILY_UNIX.equals(this.targetOS) || Os.FAMILY_TANDEM.equals(this.targetOS)) ? false : true;
    }

    public void setSetonempty(boolean setonempty) {
        this.setonempty = setonempty;
    }

    public void setProperty(String p) {
        this.property = p;
    }

    public void setRefid(Reference r) {
        if (this.path != null) {
            throw noChildrenAllowed();
        }
        this.refid = r;
    }

    public void setPathSep(String sep) {
        this.pathSep = sep;
    }

    public void setDirSep(String sep) {
        this.dirSep = sep;
    }

    public void setPreserveDuplicates(boolean preserveDuplicates) {
        this.preserveDuplicates = preserveDuplicates;
    }

    public boolean isPreserveDuplicates() {
        return this.preserveDuplicates;
    }

    public boolean isReference() {
        return this.refid != null;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Resources savedPath = this.path;
        String savedPathSep = this.pathSep;
        String savedDirSep = this.dirSep;
        try {
            if (isReference()) {
                Object o = this.refid.getReferencedObject(getProject());
                if (!(o instanceof ResourceCollection)) {
                    throw new BuildException("refid '%s' does not refer to a resource collection.", this.refid.getRefId());
                }
                getPath().add((ResourceCollection) o);
            }
            validateSetup();
            String fromDirSep = onWindows ? "\\" : "/";
            StringBuilder rslt = new StringBuilder();
            ResourceCollection resources = isPreserveDuplicates() ? this.path : new Union(this.path);
            List<String> ret = new ArrayList<>();
            FileNameMapper mapperImpl = this.mapper == null ? new IdentityMapper() : this.mapper.getImplementation();
            for (Resource r : resources) {
                String[] mapped = mapperImpl.mapFileName(String.valueOf(r));
                for (int m = 0; mapped != null && m < mapped.length; m++) {
                    ret.add(mapped[m]);
                }
            }
            boolean first = true;
            for (String string : ret) {
                String elem = mapElement(string);
                if (!first) {
                    rslt.append(this.pathSep);
                }
                first = false;
                StringTokenizer stDirectory = new StringTokenizer(elem, fromDirSep, true);
                while (stDirectory.hasMoreTokens()) {
                    String token = stDirectory.nextToken();
                    rslt.append(fromDirSep.equals(token) ? this.dirSep : token);
                }
            }
            if (this.setonempty || rslt.length() > 0) {
                String value = rslt.toString();
                if (this.property == null) {
                    log(value);
                } else {
                    log("Set property " + this.property + " = " + value, 3);
                    getProject().setNewProperty(this.property, value);
                }
            }
        } finally {
            this.path = savedPath;
            this.dirSep = savedDirSep;
            this.pathSep = savedPathSep;
        }
    }

    private String mapElement(String elem) {
        for (MapEntry entry : this.prefixMap) {
            String newElem = entry.apply(elem);
            if (newElem != elem) {
                return newElem;
            }
        }
        return elem;
    }

    public void addMapper(Mapper mapper) {
        if (this.mapper != null) {
            throw new BuildException(Expand.ERROR_MULTIPLE_MAPPERS);
        }
        this.mapper = mapper;
    }

    public void add(FileNameMapper fileNameMapper) {
        Mapper m = new Mapper(getProject());
        m.add(fileNameMapper);
        addMapper(m);
    }

    private void validateSetup() throws BuildException {
        if (this.path == null) {
            throw new BuildException("You must specify a path to convert");
        }
        String dsep = File.separator;
        String psep = File.pathSeparator;
        if (this.targetOS != null) {
            psep = this.targetWindows ? ";" : ":";
            dsep = this.targetWindows ? "\\" : "/";
        }
        if (this.pathSep != null) {
            psep = this.pathSep;
        }
        if (this.dirSep != null) {
            dsep = this.dirSep;
        }
        this.pathSep = psep;
        this.dirSep = dsep;
    }

    private BuildException noChildrenAllowed() {
        return new BuildException("You must not specify nested elements when using the refid attribute.");
    }
}
