package org.jf.dexlib2;

import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/AnnotationVisibility.class */
public final class AnnotationVisibility {
    public static final int BUILD = 0;
    public static final int RUNTIME = 1;
    public static final int SYSTEM = 2;
    private static String[] NAMES = {"build", "runtime", "system"};

    public static String getVisibility(int visibility) {
        if (visibility < 0 || visibility >= NAMES.length) {
            throw new ExceptionWithContext("Invalid annotation visibility %d", Integer.valueOf(visibility));
        }
        return NAMES[visibility];
    }

    public static int getVisibility(String visibility) {
        String visibility2 = visibility.toLowerCase();
        if (visibility2.equals("build")) {
            return 0;
        }
        if (visibility2.equals("runtime")) {
            return 1;
        }
        if (visibility2.equals("system")) {
            return 2;
        }
        throw new ExceptionWithContext("Invalid annotation visibility: %s", visibility2);
    }

    private AnnotationVisibility() {
    }
}
