package heros.utilities;

import com.google.common.collect.Maps;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/utilities/JsonDocument.class */
public class JsonDocument {
    private DefaultValueMap<String, JsonDocument> documents = new DefaultValueMap<String, JsonDocument>() { // from class: heros.utilities.JsonDocument.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // heros.utilities.DefaultValueMap
        public JsonDocument createItem(String key) {
            return new JsonDocument();
        }
    };
    private DefaultValueMap<String, JsonArray> arrays = new DefaultValueMap<String, JsonArray>() { // from class: heros.utilities.JsonDocument.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // heros.utilities.DefaultValueMap
        public JsonArray createItem(String key) {
            return new JsonArray();
        }
    };
    private Map<String, String> keyValuePairs = Maps.newHashMap();

    public JsonDocument doc(String key) {
        return this.documents.getOrCreate(key);
    }

    public JsonDocument doc(String key, JsonDocument doc) {
        if (this.documents.containsKey(key)) {
            throw new IllegalArgumentException("There is already a document registered for key: " + key);
        }
        this.documents.put(key, doc);
        return doc;
    }

    public JsonArray array(String key) {
        return this.arrays.getOrCreate(key);
    }

    public void keyValue(String key, String value) {
        this.keyValuePairs.put(key, value);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        write(builder, 0);
        return builder.toString();
    }

    public void write(StringBuilder builder, int tabs) {
        builder.append("{\n");
        for (Map.Entry<String, String> entry : this.keyValuePairs.entrySet()) {
            tabs(tabs + 1, builder);
            builder.append("\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",\n");
        }
        for (Map.Entry<String, JsonArray> entry2 : this.arrays.entrySet()) {
            tabs(tabs + 1, builder);
            builder.append("\"" + entry2.getKey() + "\": ");
            entry2.getValue().write(builder, tabs + 1);
            builder.append(",\n");
        }
        for (Map.Entry<String, JsonDocument> entry3 : this.documents.entrySet()) {
            tabs(tabs + 1, builder);
            builder.append("\"" + entry3.getKey() + "\": ");
            entry3.getValue().write(builder, tabs + 1);
            builder.append(",\n");
        }
        if (!this.keyValuePairs.isEmpty() || !this.arrays.isEmpty() || !this.documents.isEmpty()) {
            builder.delete(builder.length() - 2, builder.length() - 1);
        }
        tabs(tabs, builder);
        builder.append("}");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void tabs(int tabs, StringBuilder builder) {
        for (int i = 0; i < tabs; i++) {
            builder.append("\t");
        }
    }
}
