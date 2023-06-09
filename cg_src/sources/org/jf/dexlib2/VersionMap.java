package org.jf.dexlib2;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/VersionMap.class */
public class VersionMap {
    public static final int NO_VERSION = -1;

    public static int mapDexVersionToApi(int dexVersion) {
        switch (dexVersion) {
            case 35:
                return 23;
            case 36:
            default:
                return -1;
            case 37:
                return 25;
            case 38:
                return 27;
            case 39:
                return 28;
        }
    }

    public static int mapApiToDexVersion(int api) {
        if (api <= 23) {
            return 35;
        }
        if (api <= 25) {
            return 37;
        }
        if (api <= 27) {
            return 38;
        }
        return 39;
    }

    public static int mapArtVersionToApi(int artVersion) {
        if (artVersion >= 170) {
            return 29;
        }
        if (artVersion >= 138) {
            return 28;
        }
        if (artVersion >= 131) {
            return 27;
        }
        if (artVersion >= 124) {
            return 26;
        }
        if (artVersion >= 79) {
            return 24;
        }
        if (artVersion >= 64) {
            return 23;
        }
        if (artVersion >= 45) {
            return 22;
        }
        if (artVersion >= 39) {
            return 21;
        }
        return 19;
    }

    public static int mapApiToArtVersion(int api) {
        if (api < 19) {
            return -1;
        }
        switch (api) {
            case 19:
            case 20:
                return 7;
            case 21:
                return 39;
            case 22:
                return 45;
            case 23:
                return 64;
            case 24:
            case 25:
                return 79;
            case 26:
                return 124;
            case 27:
                return 131;
            case 28:
                return 138;
            case 29:
                return 170;
            default:
                return 178;
        }
    }
}
