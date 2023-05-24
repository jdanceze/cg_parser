package org.powermock.configuration.support;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/configuration/support/ValueAliases.class */
class ValueAliases {
    private final Map<String, String> alias;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ValueAliases(Map<String, String> alias) {
        this.alias = alias;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String findValue(String value) {
        if (this.alias.containsKey(value)) {
            return this.alias.get(value);
        }
        return value;
    }
}
