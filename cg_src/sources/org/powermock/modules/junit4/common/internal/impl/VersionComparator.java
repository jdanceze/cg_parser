package org.powermock.modules.junit4.common.internal.impl;

import java.util.Comparator;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/impl/VersionComparator.class */
class VersionComparator implements Comparator {
    public boolean equals(Object o1, Object o2) {
        return compare(o1, o2) == 0;
    }

    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        String version1 = (String) o1;
        String version2 = (String) o2;
        VersionTokenizer tokenizer1 = new VersionTokenizer(version1);
        VersionTokenizer tokenizer2 = new VersionTokenizer(version2);
        while (tokenizer1.MoveNext()) {
            if (!tokenizer2.MoveNext()) {
                do {
                    int number1 = tokenizer1.getNumber();
                    String suffix1 = tokenizer1.getSuffix();
                    if (number1 != 0 || suffix1.length() != 0) {
                        return 1;
                    }
                } while (tokenizer1.MoveNext());
                return 0;
            }
            int number12 = tokenizer1.getNumber();
            String suffix12 = tokenizer1.getSuffix();
            int number2 = tokenizer2.getNumber();
            String suffix2 = tokenizer2.getSuffix();
            if (number12 < number2) {
                return -1;
            }
            if (number12 > number2) {
                return 1;
            }
            boolean empty1 = suffix12.length() == 0;
            boolean empty2 = suffix2.length() == 0;
            if (!empty1 || !empty2) {
                if (empty1) {
                    return 1;
                }
                if (empty2) {
                    return -1;
                }
                int result = suffix12.compareTo(suffix2);
                if (result != 0) {
                    return result;
                }
            }
        }
        if (tokenizer2.MoveNext()) {
            do {
                int number22 = tokenizer2.getNumber();
                String suffix22 = tokenizer2.getSuffix();
                if (number22 != 0 || suffix22.length() != 0) {
                    return -1;
                }
            } while (tokenizer2.MoveNext());
            return 0;
        }
        return 0;
    }
}
