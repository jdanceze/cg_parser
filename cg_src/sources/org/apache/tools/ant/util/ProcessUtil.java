package org.apache.tools.ant.util;

import java.lang.management.ManagementFactory;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ProcessUtil.class */
public class ProcessUtil {
    private ProcessUtil() {
    }

    public static String getProcessId(String fallback) {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf(64);
        if (index < 1) {
            return fallback;
        }
        try {
            return Long.toString(Long.parseLong(jvmName.substring(0, index)));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static void main(String[] args) {
        System.out.println(getProcessId("<PID>"));
        try {
            Thread.sleep(120000L);
        } catch (Exception e) {
        }
    }
}
