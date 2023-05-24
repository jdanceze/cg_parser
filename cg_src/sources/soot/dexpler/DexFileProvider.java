package soot.dexpler;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.CompilationDeathException;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexFileProvider.class */
public class DexFileProvider {
    private static final Logger logger = LoggerFactory.getLogger(DexFileProvider.class);
    private static final Comparator<DexContainer<? extends DexFile>> DEFAULT_PRIORITIZER = new Comparator<DexContainer<? extends DexFile>>() { // from class: soot.dexpler.DexFileProvider.1
        @Override // java.util.Comparator
        public int compare(DexContainer<? extends DexFile> o1, DexContainer<? extends DexFile> o2) {
            String s1 = o1.getDexName();
            String s2 = o2.getDexName();
            if (s1.equals("classes.dex")) {
                return 1;
            }
            if (s2.equals("classes.dex")) {
                return -1;
            }
            boolean s1StartsClasses = s1.startsWith("classes");
            boolean s2StartsClasses = s2.startsWith("classes");
            if (s1StartsClasses && !s2StartsClasses) {
                return 1;
            }
            if (s2StartsClasses && !s1StartsClasses) {
                return -1;
            }
            return s1.compareTo(s2);
        }
    };
    private final Map<String, Map<String, DexContainer<? extends DexFile>>> dexMap = new HashMap();

    public DexFileProvider(Singletons.Global g) {
    }

    public static DexFileProvider v() {
        return G.v().soot_dexpler_DexFileProvider();
    }

    public List<DexContainer<? extends DexFile>> getDexFromSource(File dexSource) throws IOException {
        return getDexFromSource(dexSource, DEFAULT_PRIORITIZER);
    }

    public List<DexContainer<? extends DexFile>> getDexFromSource(File dexSource, Comparator<DexContainer<? extends DexFile>> prioritizer) throws IOException {
        ArrayList<DexContainer<? extends DexFile>> resultList = new ArrayList<>();
        List<File> allSources = allSourcesFromFile(dexSource);
        updateIndex(allSources);
        for (File theSource : allSources) {
            resultList.addAll(this.dexMap.get(theSource.getCanonicalPath()).values());
        }
        if (resultList.size() > 1) {
            Collections.sort(resultList, Collections.reverseOrder(prioritizer));
        }
        return resultList;
    }

    public DexContainer<? extends DexFile> getDexFromSource(File dexSource, String dexName) throws IOException {
        List<File> allSources = allSourcesFromFile(dexSource);
        updateIndex(allSources);
        for (File theSource : allSources) {
            DexContainer<? extends DexFile> dexFile = this.dexMap.get(theSource.getCanonicalPath()).get(dexName);
            if (dexFile != null) {
                return dexFile;
            }
        }
        throw new CompilationDeathException("Dex file with name '" + dexName + "' not found in " + dexSource);
    }

    private List<File> allSourcesFromFile(File dexSource) throws IOException {
        if (dexSource.isDirectory()) {
            List<File> dexFiles = getAllDexFilesInDirectory(dexSource);
            if (dexFiles.size() > 1 && !Options.v().process_multiple_dex()) {
                File file = dexFiles.get(0);
                logger.warn("Multiple dex files detected, only processing '" + file.getCanonicalPath() + "'. Use '-process-multiple-dex' option to process them all.");
                return Collections.singletonList(file);
            }
            return dexFiles;
        }
        String ext = Files.getFileExtension(dexSource.getName()).toLowerCase();
        if ((ext.equals("jar") || ext.equals("zip")) && !Options.v().search_dex_in_archives()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(dexSource);
    }

    private void updateIndex(List<File> dexSources) throws IOException {
        for (File theSource : dexSources) {
            String key = theSource.getCanonicalPath();
            Map<String, DexContainer<? extends DexFile>> dexFiles = this.dexMap.get(key);
            if (dexFiles == null) {
                try {
                    Map<String, DexContainer<? extends DexFile>> dexFiles2 = mappingForFile(theSource);
                    this.dexMap.put(key, dexFiles2);
                } catch (IOException e) {
                    throw new CompilationDeathException("Error parsing dex source", e);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Map<String, DexContainer<? extends DexFile>> mappingForFile(File dexSourceFile) throws IOException {
        int api = Scene.v().getAndroidAPIVersion();
        boolean multiple_dex = Options.v().process_multiple_dex();
        MultiDexContainer<? extends DexBackedDexFile> dexContainer = DexFileFactory.loadDexContainer(dexSourceFile, Opcodes.forApi(api));
        List<String> dexEntryNameList = dexContainer.getDexEntryNames();
        int dexFileCount = dexEntryNameList.size();
        if (dexFileCount < 1) {
            if (Options.v().verbose()) {
                logger.debug(String.format("Warning: No dex file found in '%s'", dexSourceFile));
            }
            return Collections.emptyMap();
        }
        Map<String, DexContainer<? extends DexFile>> dexMap = new HashMap<>(dexFileCount);
        ListIterator<String> entryNameIterator = dexEntryNameList.listIterator(dexFileCount);
        while (entryNameIterator.hasPrevious()) {
            String entryName = entryNameIterator.previous();
            MultiDexContainer.DexEntry<? extends DexFile> entry = dexContainer.getEntry(entryName);
            String entryName2 = deriveDexName(entryName);
            logger.debug(String.format("Found dex file '%s' with %d classes in '%s'", entryName2, Integer.valueOf(entry.getDexFile().getClasses().size()), dexSourceFile.getCanonicalPath()));
            if (multiple_dex) {
                dexMap.put(entryName2, new DexContainer<>(entry, entryName2, dexSourceFile));
            } else if (dexMap.isEmpty() && (entryName2.equals("classes.dex") || !entryNameIterator.hasPrevious())) {
                dexMap = Collections.singletonMap(entryName2, new DexContainer(entry, entryName2, dexSourceFile));
                if (dexFileCount > 1) {
                    logger.warn("Multiple dex files detected, only processing '" + entryName2 + "'. Use '-process-multiple-dex' option to process them all.");
                }
            }
        }
        return Collections.unmodifiableMap(dexMap);
    }

    private String deriveDexName(String entryName) {
        return new File(entryName).getName();
    }

    private List<File> getAllDexFilesInDirectory(File path) {
        Queue<File> toVisit = new ArrayDeque<>();
        Set<File> visited = new HashSet<>();
        List<File> ret = new ArrayList<>();
        toVisit.add(path);
        while (!toVisit.isEmpty()) {
            File cur = toVisit.poll();
            if (!visited.contains(cur)) {
                visited.add(cur);
                if (cur.isDirectory()) {
                    toVisit.addAll(Arrays.asList(cur.listFiles()));
                } else if (cur.isFile() && cur.getName().endsWith(".dex")) {
                    ret.add(cur);
                }
            }
        }
        return ret;
    }

    /* loaded from: gencallgraphv3.jar:soot/dexpler/DexFileProvider$DexContainer.class */
    public static final class DexContainer<T extends DexFile> {
        private final MultiDexContainer.DexEntry<T> base;
        private final String name;
        private final File filePath;

        public DexContainer(MultiDexContainer.DexEntry<T> base, String name, File filePath) {
            this.base = base;
            this.name = name;
            this.filePath = filePath;
        }

        public MultiDexContainer.DexEntry<T> getBase() {
            return this.base;
        }

        public String getDexName() {
            return this.name;
        }

        public File getFilePath() {
            return this.filePath;
        }
    }
}
