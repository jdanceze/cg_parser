package soot;

import android.hardware.Camera;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/PhaseOptions.class */
public class PhaseOptions {
    private static final Logger logger = LoggerFactory.getLogger(PhaseOptions.class);
    private PackManager pm;
    private final Map<HasPhaseOptions, Map<String, String>> phaseToOptionMap = new HashMap();

    public void setPackManager(PackManager m) {
        this.pm = m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackManager getPM() {
        if (this.pm == null) {
            PackManager.v();
        }
        return this.pm;
    }

    public PhaseOptions(Singletons.Global g) {
    }

    public static PhaseOptions v() {
        return G.v().soot_PhaseOptions();
    }

    public Map<String, String> getPhaseOptions(String phaseName) {
        return getPhaseOptions(getPM().getPhase(phaseName));
    }

    public Map<String, String> getPhaseOptions(HasPhaseOptions phase) {
        Map<String, String> ret;
        Map<String, String> ret2 = this.phaseToOptionMap.get(phase);
        if (ret2 == null) {
            ret = new HashMap<>();
        } else {
            ret = new HashMap<>(ret2);
        }
        StringTokenizer st = new StringTokenizer(phase.getDefaultOptions());
        while (st.hasMoreTokens()) {
            String opt = st.nextToken();
            String key = getKey(opt);
            String value = getValue(opt);
            ret.putIfAbsent(key, value);
        }
        return Collections.unmodifiableMap(ret);
    }

    public boolean processPhaseOptions(String phaseName, String option) {
        StringTokenizer st = new StringTokenizer(option, ",");
        while (st.hasMoreTokens()) {
            if (!setPhaseOption(phaseName, st.nextToken())) {
                return false;
            }
        }
        return true;
    }

    public static boolean getBoolean(Map<String, String> options, String name) {
        String val = options.get(name);
        return val != null && "true".equals(val);
    }

    public static boolean getBoolean(Map<String, String> options, String name, boolean defaultValue) {
        String val = options.get(name);
        return val != null ? "true".equals(val) : defaultValue;
    }

    public static String getString(Map<String, String> options, String name) {
        String val = options.get(name);
        return val != null ? val : "";
    }

    public static float getFloat(Map<String, String> options, String name) {
        String val = options.get(name);
        if (val != null) {
            return Float.parseFloat(val);
        }
        return 1.0f;
    }

    public static int getInt(Map<String, String> options, String name) {
        String val = options.get(name);
        if (val != null) {
            return Integer.parseInt(val);
        }
        return 0;
    }

    private Map<String, String> mapForPhase(String phaseName) {
        HasPhaseOptions phase = getPM().getPhase(phaseName);
        if (phase != null) {
            return mapForPhase(phase);
        }
        return null;
    }

    private Map<String, String> mapForPhase(HasPhaseOptions phase) {
        Map<String, String> optionMap = this.phaseToOptionMap.get(phase);
        if (optionMap == null) {
            Map<HasPhaseOptions, Map<String, String>> map = this.phaseToOptionMap;
            Map<String, String> hashMap = new HashMap<>();
            optionMap = hashMap;
            map.put(phase, hashMap);
        }
        return optionMap;
    }

    private String getKey(String option) {
        int delimLoc = option.indexOf(58);
        if (delimLoc < 0) {
            if (Camera.Parameters.FLASH_MODE_ON.equals(option) || "off".equals(option)) {
                return "enabled";
            }
            return option;
        }
        return option.substring(0, delimLoc);
    }

    private String getValue(String option) {
        int delimLoc = option.indexOf(58);
        if (delimLoc < 0) {
            if ("off".equals(option)) {
                return "false";
            }
            return "true";
        }
        return option.substring(delimLoc + 1);
    }

    private void resetRadioPack(String phaseName) {
        for (Pack p : getPM().allPacks()) {
            if ((p instanceof RadioScenePack) && p.get(phaseName) != null) {
                Iterator<Transform> it = p.iterator();
                while (it.hasNext()) {
                    Transform t = it.next();
                    setPhaseOption(t.getPhaseName(), "enabled:false");
                }
            }
        }
    }

    private boolean checkParentEnabled(String phaseName) {
        return true;
    }

    public boolean setPhaseOption(String phaseName, String option) {
        HasPhaseOptions phase = getPM().getPhase(phaseName);
        if (phase == null) {
            logger.debug("Option " + option + " given for nonexistent phase " + phaseName);
            return false;
        }
        return setPhaseOption(phase, option);
    }

    public boolean setPhaseOption(HasPhaseOptions phase, String option) {
        Map<String, String> optionMap = mapForPhase(phase);
        if (!checkParentEnabled(phase.getPhaseName())) {
            return false;
        }
        if (optionMap == null) {
            logger.debug("Option " + option + " given for nonexistent phase " + phase.getPhaseName());
            return false;
        }
        String key = getKey(option);
        if ("enabled".equals(key) && "true".equals(getValue(option))) {
            resetRadioPack(phase.getPhaseName());
        }
        if (declaresOption(phase, key)) {
            optionMap.put(key, getValue(option));
            return true;
        }
        logger.debug("Invalid option " + option + " for phase " + phase.getPhaseName());
        return false;
    }

    private boolean declaresOption(String phaseName, String option) {
        HasPhaseOptions phase = getPM().getPhase(phaseName);
        return declaresOption(phase, option);
    }

    private boolean declaresOption(HasPhaseOptions phase, String option) {
        String declareds = phase.getDeclaredOptions();
        StringTokenizer st = new StringTokenizer(declareds);
        while (st.hasMoreTokens()) {
            if (st.nextToken().equals(option)) {
                return true;
            }
        }
        return false;
    }

    public void setPhaseOptionIfUnset(String phaseName, String option) {
        Map<String, String> optionMap = mapForPhase(phaseName);
        if (optionMap == null) {
            throw new RuntimeException("No such phase " + phaseName);
        }
        if (optionMap.containsKey(getKey(option))) {
            return;
        }
        if (!declaresOption(phaseName, getKey(option))) {
            throw new RuntimeException("No option " + option + " for phase " + phaseName);
        }
        optionMap.put(getKey(option), getValue(option));
    }
}
