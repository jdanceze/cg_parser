package heros.utilities;

import com.google.common.collect.Lists;
import java.util.List;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/utilities/JsonArray.class */
public class JsonArray {
    private List<String> items = Lists.newLinkedList();

    public void add(String item) {
        this.items.add(item);
    }

    public void write(StringBuilder builder, int tabs) {
        builder.append("[\n");
        for (String item : this.items) {
            JsonDocument.tabs(tabs + 1, builder);
            builder.append("\"" + item + "\",\n");
        }
        if (!this.items.isEmpty()) {
            builder.delete(builder.length() - 2, builder.length() - 1);
        }
        JsonDocument.tabs(tabs, builder);
        builder.append("]");
    }
}
