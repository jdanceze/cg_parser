package org.mockito.internal.configuration.plugins;

import java.io.InputStream;
import org.mockito.internal.util.io.IOUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/plugins/PluginFileReader.class */
class PluginFileReader {
    /* JADX INFO: Access modifiers changed from: package-private */
    public String readPluginClass(InputStream input) {
        for (String line : IOUtil.readLines(input)) {
            String stripped = stripCommentAndWhitespace(line);
            if (stripped.length() > 0) {
                return stripped;
            }
        }
        return null;
    }

    private static String stripCommentAndWhitespace(String line) {
        int hash = line.indexOf(35);
        if (hash != -1) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }
}
