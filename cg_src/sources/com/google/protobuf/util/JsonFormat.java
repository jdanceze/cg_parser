package com.google.protobuf.util;

import android.widget.ExpandableListView;
import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.protobuf.Any;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Duration;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.FieldMask;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ListValue;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.NullValue;
import com.google.protobuf.StringValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
import com.google.protobuf.UInt64Value;
import com.google.protobuf.Value;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.resource.spi.work.WorkManager;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat.class */
public class JsonFormat {
    private static final Logger logger = Logger.getLogger(JsonFormat.class.getName());

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$TextGenerator.class */
    public interface TextGenerator {
        void indent();

        void outdent();

        void print(CharSequence charSequence) throws IOException;
    }

    private JsonFormat() {
    }

    public static Printer printer() {
        return new Printer(com.google.protobuf.TypeRegistry.getEmptyTypeRegistry(), TypeRegistry.getEmptyTypeRegistry(), false, Collections.emptySet(), false, false, false, false);
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$Printer.class */
    public static class Printer {
        private final com.google.protobuf.TypeRegistry registry;
        private final TypeRegistry oldRegistry;
        private boolean alwaysOutputDefaultValueFields;
        private Set<Descriptors.FieldDescriptor> includingDefaultValueFields;
        private final boolean preservingProtoFieldNames;
        private final boolean omittingInsignificantWhitespace;
        private final boolean printingEnumsAsInts;
        private final boolean sortingMapKeys;

        private Printer(com.google.protobuf.TypeRegistry registry, TypeRegistry oldRegistry, boolean alwaysOutputDefaultValueFields, Set<Descriptors.FieldDescriptor> includingDefaultValueFields, boolean preservingProtoFieldNames, boolean omittingInsignificantWhitespace, boolean printingEnumsAsInts, boolean sortingMapKeys) {
            this.registry = registry;
            this.oldRegistry = oldRegistry;
            this.alwaysOutputDefaultValueFields = alwaysOutputDefaultValueFields;
            this.includingDefaultValueFields = includingDefaultValueFields;
            this.preservingProtoFieldNames = preservingProtoFieldNames;
            this.omittingInsignificantWhitespace = omittingInsignificantWhitespace;
            this.printingEnumsAsInts = printingEnumsAsInts;
            this.sortingMapKeys = sortingMapKeys;
        }

        public Printer usingTypeRegistry(TypeRegistry oldRegistry) {
            if (this.oldRegistry != TypeRegistry.getEmptyTypeRegistry() || this.registry != com.google.protobuf.TypeRegistry.getEmptyTypeRegistry()) {
                throw new IllegalArgumentException("Only one registry is allowed.");
            }
            return new Printer(com.google.protobuf.TypeRegistry.getEmptyTypeRegistry(), oldRegistry, this.alwaysOutputDefaultValueFields, this.includingDefaultValueFields, this.preservingProtoFieldNames, this.omittingInsignificantWhitespace, this.printingEnumsAsInts, this.sortingMapKeys);
        }

        public Printer usingTypeRegistry(com.google.protobuf.TypeRegistry registry) {
            if (this.oldRegistry != TypeRegistry.getEmptyTypeRegistry() || this.registry != com.google.protobuf.TypeRegistry.getEmptyTypeRegistry()) {
                throw new IllegalArgumentException("Only one registry is allowed.");
            }
            return new Printer(registry, this.oldRegistry, this.alwaysOutputDefaultValueFields, this.includingDefaultValueFields, this.preservingProtoFieldNames, this.omittingInsignificantWhitespace, this.printingEnumsAsInts, this.sortingMapKeys);
        }

        public Printer includingDefaultValueFields() {
            checkUnsetIncludingDefaultValueFields();
            return new Printer(this.registry, this.oldRegistry, true, Collections.emptySet(), this.preservingProtoFieldNames, this.omittingInsignificantWhitespace, this.printingEnumsAsInts, this.sortingMapKeys);
        }

        public Printer printingEnumsAsInts() {
            checkUnsetPrintingEnumsAsInts();
            return new Printer(this.registry, this.oldRegistry, this.alwaysOutputDefaultValueFields, this.includingDefaultValueFields, this.preservingProtoFieldNames, this.omittingInsignificantWhitespace, true, this.sortingMapKeys);
        }

        private void checkUnsetPrintingEnumsAsInts() {
            if (this.printingEnumsAsInts) {
                throw new IllegalStateException("JsonFormat printingEnumsAsInts has already been set.");
            }
        }

        public Printer includingDefaultValueFields(Set<Descriptors.FieldDescriptor> fieldsToAlwaysOutput) {
            Preconditions.checkArgument((null == fieldsToAlwaysOutput || fieldsToAlwaysOutput.isEmpty()) ? false : true, "Non-empty Set must be supplied for includingDefaultValueFields.");
            checkUnsetIncludingDefaultValueFields();
            return new Printer(this.registry, this.oldRegistry, false, Collections.unmodifiableSet(new HashSet(fieldsToAlwaysOutput)), this.preservingProtoFieldNames, this.omittingInsignificantWhitespace, this.printingEnumsAsInts, this.sortingMapKeys);
        }

        private void checkUnsetIncludingDefaultValueFields() {
            if (this.alwaysOutputDefaultValueFields || !this.includingDefaultValueFields.isEmpty()) {
                throw new IllegalStateException("JsonFormat includingDefaultValueFields has already been set.");
            }
        }

        public Printer preservingProtoFieldNames() {
            return new Printer(this.registry, this.oldRegistry, this.alwaysOutputDefaultValueFields, this.includingDefaultValueFields, true, this.omittingInsignificantWhitespace, this.printingEnumsAsInts, this.sortingMapKeys);
        }

        public Printer omittingInsignificantWhitespace() {
            return new Printer(this.registry, this.oldRegistry, this.alwaysOutputDefaultValueFields, this.includingDefaultValueFields, this.preservingProtoFieldNames, true, this.printingEnumsAsInts, this.sortingMapKeys);
        }

        public Printer sortingMapKeys() {
            return new Printer(this.registry, this.oldRegistry, this.alwaysOutputDefaultValueFields, this.includingDefaultValueFields, this.preservingProtoFieldNames, this.omittingInsignificantWhitespace, this.printingEnumsAsInts, true);
        }

        public void appendTo(MessageOrBuilder message, Appendable output) throws IOException {
            new PrinterImpl(this.registry, this.oldRegistry, this.alwaysOutputDefaultValueFields, this.includingDefaultValueFields, this.preservingProtoFieldNames, output, this.omittingInsignificantWhitespace, this.printingEnumsAsInts, this.sortingMapKeys).print(message);
        }

        public String print(MessageOrBuilder message) throws InvalidProtocolBufferException {
            try {
                StringBuilder builder = new StringBuilder();
                appendTo(message, builder);
                return builder.toString();
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    public static Parser parser() {
        return new Parser(com.google.protobuf.TypeRegistry.getEmptyTypeRegistry(), TypeRegistry.getEmptyTypeRegistry(), false, 100);
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$Parser.class */
    public static class Parser {
        private final com.google.protobuf.TypeRegistry registry;
        private final TypeRegistry oldRegistry;
        private final boolean ignoringUnknownFields;
        private final int recursionLimit;
        private static final int DEFAULT_RECURSION_LIMIT = 100;

        private Parser(com.google.protobuf.TypeRegistry registry, TypeRegistry oldRegistry, boolean ignoreUnknownFields, int recursionLimit) {
            this.registry = registry;
            this.oldRegistry = oldRegistry;
            this.ignoringUnknownFields = ignoreUnknownFields;
            this.recursionLimit = recursionLimit;
        }

        public Parser usingTypeRegistry(TypeRegistry oldRegistry) {
            if (this.oldRegistry != TypeRegistry.getEmptyTypeRegistry() || this.registry != com.google.protobuf.TypeRegistry.getEmptyTypeRegistry()) {
                throw new IllegalArgumentException("Only one registry is allowed.");
            }
            return new Parser(com.google.protobuf.TypeRegistry.getEmptyTypeRegistry(), oldRegistry, this.ignoringUnknownFields, this.recursionLimit);
        }

        public Parser usingTypeRegistry(com.google.protobuf.TypeRegistry registry) {
            if (this.oldRegistry != TypeRegistry.getEmptyTypeRegistry() || this.registry != com.google.protobuf.TypeRegistry.getEmptyTypeRegistry()) {
                throw new IllegalArgumentException("Only one registry is allowed.");
            }
            return new Parser(registry, this.oldRegistry, this.ignoringUnknownFields, this.recursionLimit);
        }

        public Parser ignoringUnknownFields() {
            return new Parser(this.registry, this.oldRegistry, true, this.recursionLimit);
        }

        public void merge(String json, Message.Builder builder) throws InvalidProtocolBufferException {
            new ParserImpl(this.registry, this.oldRegistry, this.ignoringUnknownFields, this.recursionLimit).merge(json, builder);
        }

        public void merge(Reader json, Message.Builder builder) throws IOException {
            new ParserImpl(this.registry, this.oldRegistry, this.ignoringUnknownFields, this.recursionLimit).merge(json, builder);
        }

        Parser usingRecursionLimit(int recursionLimit) {
            return new Parser(this.registry, this.oldRegistry, this.ignoringUnknownFields, recursionLimit);
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$TypeRegistry.class */
    public static class TypeRegistry {
        private final Map<String, Descriptors.Descriptor> types;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$TypeRegistry$EmptyTypeRegistryHolder.class */
        public static class EmptyTypeRegistryHolder {
            private static final TypeRegistry EMPTY = new TypeRegistry(Collections.emptyMap());

            private EmptyTypeRegistryHolder() {
            }
        }

        public static TypeRegistry getEmptyTypeRegistry() {
            return EmptyTypeRegistryHolder.EMPTY;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        @Nullable
        public Descriptors.Descriptor find(String name) {
            return this.types.get(name);
        }

        @Nullable
        Descriptors.Descriptor getDescriptorForTypeUrl(String typeUrl) throws InvalidProtocolBufferException {
            return find(JsonFormat.getTypeName(typeUrl));
        }

        private TypeRegistry(Map<String, Descriptors.Descriptor> types) {
            this.types = types;
        }

        /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$TypeRegistry$Builder.class */
        public static class Builder {
            private final Set<String> files;
            private final Map<String, Descriptors.Descriptor> types;
            private boolean built;

            private Builder() {
                this.files = new HashSet();
                this.types = new HashMap();
                this.built = false;
            }

            @CanIgnoreReturnValue
            public Builder add(Descriptors.Descriptor messageType) {
                if (this.built) {
                    throw new IllegalStateException("A TypeRegistry.Builder can only be used once.");
                }
                addFile(messageType.getFile());
                return this;
            }

            @CanIgnoreReturnValue
            public Builder add(Iterable<Descriptors.Descriptor> messageTypes) {
                if (this.built) {
                    throw new IllegalStateException("A TypeRegistry.Builder can only be used once.");
                }
                for (Descriptors.Descriptor type : messageTypes) {
                    addFile(type.getFile());
                }
                return this;
            }

            public TypeRegistry build() {
                this.built = true;
                return new TypeRegistry(this.types);
            }

            private void addFile(Descriptors.FileDescriptor file) {
                if (!this.files.add(file.getFullName())) {
                    return;
                }
                for (Descriptors.FileDescriptor dependency : file.getDependencies()) {
                    addFile(dependency);
                }
                for (Descriptors.Descriptor message : file.getMessageTypes()) {
                    addMessage(message);
                }
            }

            private void addMessage(Descriptors.Descriptor message) {
                for (Descriptors.Descriptor nestedType : message.getNestedTypes()) {
                    addMessage(nestedType);
                }
                if (this.types.containsKey(message.getFullName())) {
                    JsonFormat.logger.warning("Type " + message.getFullName() + " is added multiple times.");
                } else {
                    this.types.put(message.getFullName(), message);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$CompactTextGenerator.class */
    private static final class CompactTextGenerator implements TextGenerator {
        private final Appendable output;

        private CompactTextGenerator(Appendable output) {
            this.output = output;
        }

        @Override // com.google.protobuf.util.JsonFormat.TextGenerator
        public void indent() {
        }

        @Override // com.google.protobuf.util.JsonFormat.TextGenerator
        public void outdent() {
        }

        @Override // com.google.protobuf.util.JsonFormat.TextGenerator
        public void print(CharSequence text) throws IOException {
            this.output.append(text);
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$PrettyTextGenerator.class */
    private static final class PrettyTextGenerator implements TextGenerator {
        private final Appendable output;
        private final StringBuilder indent;
        private boolean atStartOfLine;

        private PrettyTextGenerator(Appendable output) {
            this.indent = new StringBuilder();
            this.atStartOfLine = true;
            this.output = output;
        }

        @Override // com.google.protobuf.util.JsonFormat.TextGenerator
        public void indent() {
            this.indent.append("  ");
        }

        @Override // com.google.protobuf.util.JsonFormat.TextGenerator
        public void outdent() {
            int length = this.indent.length();
            if (length < 2) {
                throw new IllegalArgumentException(" Outdent() without matching Indent().");
            }
            this.indent.delete(length - 2, length);
        }

        @Override // com.google.protobuf.util.JsonFormat.TextGenerator
        public void print(CharSequence text) throws IOException {
            int size = text.length();
            int pos = 0;
            for (int i = 0; i < size; i++) {
                if (text.charAt(i) == '\n') {
                    write(text.subSequence(pos, i + 1));
                    pos = i + 1;
                    this.atStartOfLine = true;
                }
            }
            write(text.subSequence(pos, size));
        }

        private void write(CharSequence data) throws IOException {
            if (data.length() == 0) {
                return;
            }
            if (this.atStartOfLine) {
                this.atStartOfLine = false;
                this.output.append(this.indent);
            }
            this.output.append(data);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$PrinterImpl.class */
    public static final class PrinterImpl {
        private final com.google.protobuf.TypeRegistry registry;
        private final TypeRegistry oldRegistry;
        private final boolean alwaysOutputDefaultValueFields;
        private final Set<Descriptors.FieldDescriptor> includingDefaultValueFields;
        private final boolean preservingProtoFieldNames;
        private final boolean printingEnumsAsInts;
        private final boolean sortingMapKeys;
        private final TextGenerator generator;
        private final Gson gson = GsonHolder.DEFAULT_GSON;
        private final CharSequence blankOrSpace;
        private final CharSequence blankOrNewLine;
        private static final Map<String, WellKnownTypePrinter> wellKnownTypePrinters = buildWellKnownTypePrinters();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$PrinterImpl$WellKnownTypePrinter.class */
        public interface WellKnownTypePrinter {
            void print(PrinterImpl printerImpl, MessageOrBuilder messageOrBuilder) throws IOException;
        }

        /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$PrinterImpl$GsonHolder.class */
        private static class GsonHolder {
            private static final Gson DEFAULT_GSON = new GsonBuilder().create();

            private GsonHolder() {
            }
        }

        PrinterImpl(com.google.protobuf.TypeRegistry registry, TypeRegistry oldRegistry, boolean alwaysOutputDefaultValueFields, Set<Descriptors.FieldDescriptor> includingDefaultValueFields, boolean preservingProtoFieldNames, Appendable jsonOutput, boolean omittingInsignificantWhitespace, boolean printingEnumsAsInts, boolean sortingMapKeys) {
            this.registry = registry;
            this.oldRegistry = oldRegistry;
            this.alwaysOutputDefaultValueFields = alwaysOutputDefaultValueFields;
            this.includingDefaultValueFields = includingDefaultValueFields;
            this.preservingProtoFieldNames = preservingProtoFieldNames;
            this.printingEnumsAsInts = printingEnumsAsInts;
            this.sortingMapKeys = sortingMapKeys;
            if (omittingInsignificantWhitespace) {
                this.generator = new CompactTextGenerator(jsonOutput);
                this.blankOrSpace = "";
                this.blankOrNewLine = "";
                return;
            }
            this.generator = new PrettyTextGenerator(jsonOutput);
            this.blankOrSpace = Instruction.argsep;
            this.blankOrNewLine = "\n";
        }

        void print(MessageOrBuilder message) throws IOException {
            WellKnownTypePrinter specialPrinter = wellKnownTypePrinters.get(message.getDescriptorForType().getFullName());
            if (specialPrinter != null) {
                specialPrinter.print(this, message);
            } else {
                print(message, null);
            }
        }

        private static Map<String, WellKnownTypePrinter> buildWellKnownTypePrinters() {
            Map<String, WellKnownTypePrinter> printers = new HashMap<>();
            printers.put(Any.getDescriptor().getFullName(), new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.1
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printAny(message);
                }
            });
            WellKnownTypePrinter wrappersPrinter = new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.2
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printWrapper(message);
                }
            };
            printers.put(BoolValue.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(Int32Value.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(UInt32Value.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(Int64Value.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(UInt64Value.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(StringValue.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(BytesValue.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(FloatValue.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(DoubleValue.getDescriptor().getFullName(), wrappersPrinter);
            printers.put(Timestamp.getDescriptor().getFullName(), new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.3
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printTimestamp(message);
                }
            });
            printers.put(Duration.getDescriptor().getFullName(), new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.4
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printDuration(message);
                }
            });
            printers.put(FieldMask.getDescriptor().getFullName(), new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.5
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printFieldMask(message);
                }
            });
            printers.put(Struct.getDescriptor().getFullName(), new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.6
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printStruct(message);
                }
            });
            printers.put(Value.getDescriptor().getFullName(), new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.7
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printValue(message);
                }
            });
            printers.put(ListValue.getDescriptor().getFullName(), new WellKnownTypePrinter() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.8
                @Override // com.google.protobuf.util.JsonFormat.PrinterImpl.WellKnownTypePrinter
                public void print(PrinterImpl printer, MessageOrBuilder message) throws IOException {
                    printer.printListValue(message);
                }
            });
            return printers;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printAny(MessageOrBuilder message) throws IOException {
            if (Any.getDefaultInstance().equals(message)) {
                this.generator.print("{}");
                return;
            }
            Descriptors.Descriptor descriptor = message.getDescriptorForType();
            Descriptors.FieldDescriptor typeUrlField = descriptor.findFieldByName("type_url");
            Descriptors.FieldDescriptor valueField = descriptor.findFieldByName("value");
            if (typeUrlField == null || valueField == null || typeUrlField.getType() != Descriptors.FieldDescriptor.Type.STRING || valueField.getType() != Descriptors.FieldDescriptor.Type.BYTES) {
                throw new InvalidProtocolBufferException("Invalid Any type.");
            }
            String typeUrl = (String) message.getField(typeUrlField);
            Descriptors.Descriptor type = this.registry.getDescriptorForTypeUrl(typeUrl);
            if (type == null) {
                type = this.oldRegistry.getDescriptorForTypeUrl(typeUrl);
                if (type == null) {
                    throw new InvalidProtocolBufferException("Cannot find type for url: " + typeUrl);
                }
            }
            ByteString content = (ByteString) message.getField(valueField);
            Message contentMessage = DynamicMessage.getDefaultInstance(type).getParserForType().parseFrom(content);
            WellKnownTypePrinter printer = wellKnownTypePrinters.get(JsonFormat.getTypeName(typeUrl));
            if (printer != null) {
                this.generator.print("{" + ((Object) this.blankOrNewLine));
                this.generator.indent();
                this.generator.print("\"@type\":" + ((Object) this.blankOrSpace) + this.gson.toJson(typeUrl) + "," + ((Object) this.blankOrNewLine));
                this.generator.print("\"value\":" + ((Object) this.blankOrSpace));
                printer.print(this, contentMessage);
                this.generator.print(this.blankOrNewLine);
                this.generator.outdent();
                this.generator.print("}");
                return;
            }
            print(contentMessage, typeUrl);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printWrapper(MessageOrBuilder message) throws IOException {
            Descriptors.Descriptor descriptor = message.getDescriptorForType();
            Descriptors.FieldDescriptor valueField = descriptor.findFieldByName("value");
            if (valueField == null) {
                throw new InvalidProtocolBufferException("Invalid Wrapper type.");
            }
            printSingleFieldValue(valueField, message.getField(valueField));
        }

        private ByteString toByteString(MessageOrBuilder message) {
            if (message instanceof Message) {
                return ((Message) message).toByteString();
            }
            return ((Message.Builder) message).build().toByteString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printTimestamp(MessageOrBuilder message) throws IOException {
            Timestamp value = Timestamp.parseFrom(toByteString(message));
            this.generator.print("\"" + Timestamps.toString(value) + "\"");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printDuration(MessageOrBuilder message) throws IOException {
            Duration value = Duration.parseFrom(toByteString(message));
            this.generator.print("\"" + Durations.toString(value) + "\"");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printFieldMask(MessageOrBuilder message) throws IOException {
            FieldMask value = FieldMask.parseFrom(toByteString(message));
            this.generator.print("\"" + FieldMaskUtil.toJsonString(value) + "\"");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printStruct(MessageOrBuilder message) throws IOException {
            Descriptors.Descriptor descriptor = message.getDescriptorForType();
            Descriptors.FieldDescriptor field = descriptor.findFieldByName("fields");
            if (field == null) {
                throw new InvalidProtocolBufferException("Invalid Struct type.");
            }
            printMapFieldValue(field, message.getField(field));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printValue(MessageOrBuilder message) throws IOException {
            Map<Descriptors.FieldDescriptor, Object> fields = message.getAllFields();
            if (fields.isEmpty()) {
                this.generator.print(Jimple.NULL);
            } else if (fields.size() != 1) {
                throw new InvalidProtocolBufferException("Invalid Value type.");
            } else {
                for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : fields.entrySet()) {
                    printSingleFieldValue(entry.getKey(), entry.getValue());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void printListValue(MessageOrBuilder message) throws IOException {
            Descriptors.Descriptor descriptor = message.getDescriptorForType();
            Descriptors.FieldDescriptor field = descriptor.findFieldByName("values");
            if (field == null) {
                throw new InvalidProtocolBufferException("Invalid ListValue type.");
            }
            printRepeatedFieldValue(field, message.getField(field));
        }

        private void print(MessageOrBuilder message, @Nullable String typeUrl) throws IOException {
            Map<Descriptors.FieldDescriptor, Object> fieldsToPrint;
            this.generator.print("{" + ((Object) this.blankOrNewLine));
            this.generator.indent();
            boolean printedField = false;
            if (typeUrl != null) {
                this.generator.print("\"@type\":" + ((Object) this.blankOrSpace) + this.gson.toJson(typeUrl));
                printedField = true;
            }
            if (this.alwaysOutputDefaultValueFields || !this.includingDefaultValueFields.isEmpty()) {
                fieldsToPrint = new TreeMap<>(message.getAllFields());
                for (Descriptors.FieldDescriptor field : message.getDescriptorForType().getFields()) {
                    if (field.isOptional()) {
                        if (field.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE || message.hasField(field)) {
                            Descriptors.OneofDescriptor oneof = field.getContainingOneof();
                            if (oneof != null && !message.hasField(field)) {
                            }
                        }
                    }
                    if (!fieldsToPrint.containsKey(field) && (this.alwaysOutputDefaultValueFields || this.includingDefaultValueFields.contains(field))) {
                        fieldsToPrint.put(field, message.getField(field));
                    }
                }
            } else {
                fieldsToPrint = message.getAllFields();
            }
            for (Map.Entry<Descriptors.FieldDescriptor, Object> field2 : fieldsToPrint.entrySet()) {
                if (printedField) {
                    this.generator.print("," + ((Object) this.blankOrNewLine));
                } else {
                    printedField = true;
                }
                printField(field2.getKey(), field2.getValue());
            }
            if (printedField) {
                this.generator.print(this.blankOrNewLine);
            }
            this.generator.outdent();
            this.generator.print("}");
        }

        private void printField(Descriptors.FieldDescriptor field, Object value) throws IOException {
            if (this.preservingProtoFieldNames) {
                this.generator.print("\"" + field.getName() + "\":" + ((Object) this.blankOrSpace));
            } else {
                this.generator.print("\"" + field.getJsonName() + "\":" + ((Object) this.blankOrSpace));
            }
            if (field.isMapField()) {
                printMapFieldValue(field, value);
            } else if (field.isRepeated()) {
                printRepeatedFieldValue(field, value);
            } else {
                printSingleFieldValue(field, value);
            }
        }

        private void printRepeatedFieldValue(Descriptors.FieldDescriptor field, Object value) throws IOException {
            this.generator.print("[");
            boolean printedElement = false;
            for (Object element : (List) value) {
                if (printedElement) {
                    this.generator.print("," + ((Object) this.blankOrSpace));
                } else {
                    printedElement = true;
                }
                printSingleFieldValue(field, element);
            }
            this.generator.print("]");
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void printMapFieldValue(Descriptors.FieldDescriptor field, Object value) throws IOException {
            Descriptors.Descriptor type = field.getMessageType();
            Descriptors.FieldDescriptor keyField = type.findFieldByName("key");
            Descriptors.FieldDescriptor valueField = type.findFieldByName("value");
            if (keyField == null || valueField == null) {
                throw new InvalidProtocolBufferException("Invalid map field.");
            }
            this.generator.print("{" + ((Object) this.blankOrNewLine));
            this.generator.indent();
            Collection<Object> elements = (List) value;
            if (this.sortingMapKeys && !elements.isEmpty()) {
                Comparator<Object> cmp = null;
                if (keyField.getType() == Descriptors.FieldDescriptor.Type.STRING) {
                    cmp = new Comparator<Object>() { // from class: com.google.protobuf.util.JsonFormat.PrinterImpl.9
                        @Override // java.util.Comparator
                        public int compare(Object o1, Object o2) {
                            ByteString s1 = ByteString.copyFromUtf8((String) o1);
                            ByteString s2 = ByteString.copyFromUtf8((String) o2);
                            return ByteString.unsignedLexicographicalComparator().compare(s1, s2);
                        }
                    };
                }
                TreeMap<Object, Object> tm = new TreeMap<>(cmp);
                for (Object element : elements) {
                    Object entryKey = ((Message) element).getField(keyField);
                    tm.put(entryKey, element);
                }
                elements = tm.values();
            }
            boolean printedElement = false;
            Iterator<Object> it = elements.iterator();
            while (it.hasNext()) {
                Message entry = (Message) it.next();
                Object entryKey2 = entry.getField(keyField);
                Object entryValue = entry.getField(valueField);
                if (printedElement) {
                    this.generator.print("," + ((Object) this.blankOrNewLine));
                } else {
                    printedElement = true;
                }
                printSingleFieldValue(keyField, entryKey2, true);
                this.generator.print(":" + ((Object) this.blankOrSpace));
                printSingleFieldValue(valueField, entryValue);
            }
            if (printedElement) {
                this.generator.print(this.blankOrNewLine);
            }
            this.generator.outdent();
            this.generator.print("}");
        }

        private void printSingleFieldValue(Descriptors.FieldDescriptor field, Object value) throws IOException {
            printSingleFieldValue(field, value, false);
        }

        private void printSingleFieldValue(Descriptors.FieldDescriptor field, Object value, boolean alwaysWithQuotes) throws IOException {
            switch (field.getType()) {
                case INT32:
                case SINT32:
                case SFIXED32:
                    if (alwaysWithQuotes) {
                        this.generator.print("\"");
                    }
                    this.generator.print(((Integer) value).toString());
                    if (alwaysWithQuotes) {
                        this.generator.print("\"");
                        return;
                    }
                    return;
                case INT64:
                case SINT64:
                case SFIXED64:
                    this.generator.print("\"" + ((Long) value).toString() + "\"");
                    return;
                case BOOL:
                    if (alwaysWithQuotes) {
                        this.generator.print("\"");
                    }
                    if (((Boolean) value).booleanValue()) {
                        this.generator.print("true");
                    } else {
                        this.generator.print("false");
                    }
                    if (alwaysWithQuotes) {
                        this.generator.print("\"");
                        return;
                    }
                    return;
                case FLOAT:
                    Float floatValue = (Float) value;
                    if (floatValue.isNaN()) {
                        this.generator.print("\"NaN\"");
                        return;
                    } else if (floatValue.isInfinite()) {
                        if (floatValue.floatValue() < 0.0f) {
                            this.generator.print("\"-Infinity\"");
                            return;
                        } else {
                            this.generator.print("\"Infinity\"");
                            return;
                        }
                    } else {
                        if (alwaysWithQuotes) {
                            this.generator.print("\"");
                        }
                        this.generator.print(floatValue.toString());
                        if (alwaysWithQuotes) {
                            this.generator.print("\"");
                            return;
                        }
                        return;
                    }
                case DOUBLE:
                    Double doubleValue = (Double) value;
                    if (doubleValue.isNaN()) {
                        this.generator.print("\"NaN\"");
                        return;
                    } else if (doubleValue.isInfinite()) {
                        if (doubleValue.doubleValue() < Const.default_value_double) {
                            this.generator.print("\"-Infinity\"");
                            return;
                        } else {
                            this.generator.print("\"Infinity\"");
                            return;
                        }
                    } else {
                        if (alwaysWithQuotes) {
                            this.generator.print("\"");
                        }
                        this.generator.print(doubleValue.toString());
                        if (alwaysWithQuotes) {
                            this.generator.print("\"");
                            return;
                        }
                        return;
                    }
                case UINT32:
                case FIXED32:
                    if (alwaysWithQuotes) {
                        this.generator.print("\"");
                    }
                    this.generator.print(JsonFormat.unsignedToString(((Integer) value).intValue()));
                    if (alwaysWithQuotes) {
                        this.generator.print("\"");
                        return;
                    }
                    return;
                case UINT64:
                case FIXED64:
                    this.generator.print("\"" + JsonFormat.unsignedToString(((Long) value).longValue()) + "\"");
                    return;
                case STRING:
                    this.generator.print(this.gson.toJson(value));
                    return;
                case BYTES:
                    this.generator.print("\"");
                    this.generator.print(BaseEncoding.base64().encode(((ByteString) value).toByteArray()));
                    this.generator.print("\"");
                    return;
                case ENUM:
                    if (field.getEnumType().getFullName().equals("google.protobuf.NullValue")) {
                        if (alwaysWithQuotes) {
                            this.generator.print("\"");
                        }
                        this.generator.print(Jimple.NULL);
                        if (alwaysWithQuotes) {
                            this.generator.print("\"");
                            return;
                        }
                        return;
                    } else if (this.printingEnumsAsInts || ((Descriptors.EnumValueDescriptor) value).getIndex() == -1) {
                        this.generator.print(String.valueOf(((Descriptors.EnumValueDescriptor) value).getNumber()));
                        return;
                    } else {
                        this.generator.print("\"" + ((Descriptors.EnumValueDescriptor) value).getName() + "\"");
                        return;
                    }
                case MESSAGE:
                case GROUP:
                    print((Message) value);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String unsignedToString(int value) {
        if (value >= 0) {
            return Integer.toString(value);
        }
        return Long.toString(value & ExpandableListView.PACKED_POSITION_VALUE_NULL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String unsignedToString(long value) {
        if (value >= 0) {
            return Long.toString(value);
        }
        return BigInteger.valueOf(value & WorkManager.INDEFINITE).setBit(63).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getTypeName(String typeUrl) throws InvalidProtocolBufferException {
        String[] parts = typeUrl.split("/");
        if (parts.length == 1) {
            throw new InvalidProtocolBufferException("Invalid type url found: " + typeUrl);
        }
        return parts[parts.length - 1];
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$ParserImpl.class */
    private static class ParserImpl {
        private final com.google.protobuf.TypeRegistry registry;
        private final TypeRegistry oldRegistry;
        private final boolean ignoringUnknownFields;
        private final int recursionLimit;
        private static final double EPSILON = 1.0E-6d;
        private static final Map<String, WellKnownTypeParser> wellKnownTypeParsers = buildWellKnownTypeParsers();
        private static final BigInteger MAX_UINT64 = new BigInteger("FFFFFFFFFFFFFFFF", 16);
        private static final BigDecimal MORE_THAN_ONE = new BigDecimal(String.valueOf(1.000001d));
        private static final BigDecimal MAX_DOUBLE = new BigDecimal(String.valueOf(Double.MAX_VALUE)).multiply(MORE_THAN_ONE);
        private static final BigDecimal MIN_DOUBLE = new BigDecimal(String.valueOf(-1.7976931348623157E308d)).multiply(MORE_THAN_ONE);
        private final Map<Descriptors.Descriptor, Map<String, Descriptors.FieldDescriptor>> fieldNameMaps = new HashMap();
        private int currentDepth = 0;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/JsonFormat$ParserImpl$WellKnownTypeParser.class */
        public interface WellKnownTypeParser {
            void merge(ParserImpl parserImpl, JsonElement jsonElement, Message.Builder builder) throws InvalidProtocolBufferException;
        }

        ParserImpl(com.google.protobuf.TypeRegistry registry, TypeRegistry oldRegistry, boolean ignoreUnknownFields, int recursionLimit) {
            this.registry = registry;
            this.oldRegistry = oldRegistry;
            this.ignoringUnknownFields = ignoreUnknownFields;
            this.recursionLimit = recursionLimit;
        }

        void merge(Reader json, Message.Builder builder) throws IOException {
            try {
                JsonReader reader = new JsonReader(json);
                reader.setLenient(false);
                merge(JsonParser.parseReader(reader), builder);
            } catch (JsonIOException e) {
                if (e.getCause() instanceof IOException) {
                    throw ((IOException) e.getCause());
                }
                throw new InvalidProtocolBufferException(e.getMessage(), e);
            } catch (RuntimeException e2) {
                throw new InvalidProtocolBufferException(e2.getMessage(), e2);
            }
        }

        void merge(String json, Message.Builder builder) throws InvalidProtocolBufferException {
            try {
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(false);
                merge(JsonParser.parseReader(reader), builder);
            } catch (RuntimeException e) {
                InvalidProtocolBufferException toThrow = new InvalidProtocolBufferException(e.getMessage());
                toThrow.initCause(e);
                throw toThrow;
            }
        }

        private static Map<String, WellKnownTypeParser> buildWellKnownTypeParsers() {
            Map<String, WellKnownTypeParser> parsers = new HashMap<>();
            parsers.put(Any.getDescriptor().getFullName(), new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.1
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeAny(json, builder);
                }
            });
            WellKnownTypeParser wrappersPrinter = new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.2
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeWrapper(json, builder);
                }
            };
            parsers.put(BoolValue.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(Int32Value.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(UInt32Value.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(Int64Value.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(UInt64Value.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(StringValue.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(BytesValue.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(FloatValue.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(DoubleValue.getDescriptor().getFullName(), wrappersPrinter);
            parsers.put(Timestamp.getDescriptor().getFullName(), new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.3
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeTimestamp(json, builder);
                }
            });
            parsers.put(Duration.getDescriptor().getFullName(), new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.4
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeDuration(json, builder);
                }
            });
            parsers.put(FieldMask.getDescriptor().getFullName(), new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.5
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeFieldMask(json, builder);
                }
            });
            parsers.put(Struct.getDescriptor().getFullName(), new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.6
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeStruct(json, builder);
                }
            });
            parsers.put(ListValue.getDescriptor().getFullName(), new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.7
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeListValue(json, builder);
                }
            });
            parsers.put(Value.getDescriptor().getFullName(), new WellKnownTypeParser() { // from class: com.google.protobuf.util.JsonFormat.ParserImpl.8
                @Override // com.google.protobuf.util.JsonFormat.ParserImpl.WellKnownTypeParser
                public void merge(ParserImpl parser, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
                    parser.mergeValue(json, builder);
                }
            });
            return parsers;
        }

        private void merge(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            WellKnownTypeParser specialParser = wellKnownTypeParsers.get(builder.getDescriptorForType().getFullName());
            if (specialParser != null) {
                specialParser.merge(this, json, builder);
            } else {
                mergeMessage(json, builder, false);
            }
        }

        private Map<String, Descriptors.FieldDescriptor> getFieldNameMap(Descriptors.Descriptor descriptor) {
            if (!this.fieldNameMaps.containsKey(descriptor)) {
                Map<String, Descriptors.FieldDescriptor> fieldNameMap = new HashMap<>();
                for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
                    fieldNameMap.put(field.getName(), field);
                    fieldNameMap.put(field.getJsonName(), field);
                }
                this.fieldNameMaps.put(descriptor, fieldNameMap);
                return fieldNameMap;
            }
            return this.fieldNameMaps.get(descriptor);
        }

        private void mergeMessage(JsonElement json, Message.Builder builder, boolean skipTypeUrl) throws InvalidProtocolBufferException {
            if (!(json instanceof JsonObject)) {
                throw new InvalidProtocolBufferException("Expect message object but got: " + json);
            }
            JsonObject object = (JsonObject) json;
            Map<String, Descriptors.FieldDescriptor> fieldNameMap = getFieldNameMap(builder.getDescriptorForType());
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (!skipTypeUrl || !entry.getKey().equals("@type")) {
                    Descriptors.FieldDescriptor field = fieldNameMap.get(entry.getKey());
                    if (field == null) {
                        if (!this.ignoringUnknownFields) {
                            throw new InvalidProtocolBufferException("Cannot find field: " + entry.getKey() + " in message " + builder.getDescriptorForType().getFullName());
                        }
                    } else {
                        mergeField(field, entry.getValue(), builder);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeAny(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            Descriptors.Descriptor descriptor = builder.getDescriptorForType();
            Descriptors.FieldDescriptor typeUrlField = descriptor.findFieldByName("type_url");
            Descriptors.FieldDescriptor valueField = descriptor.findFieldByName("value");
            if (typeUrlField == null || valueField == null || typeUrlField.getType() != Descriptors.FieldDescriptor.Type.STRING || valueField.getType() != Descriptors.FieldDescriptor.Type.BYTES) {
                throw new InvalidProtocolBufferException("Invalid Any type.");
            }
            if (!(json instanceof JsonObject)) {
                throw new InvalidProtocolBufferException("Expect message object but got: " + json);
            }
            JsonObject object = (JsonObject) json;
            if (object.entrySet().isEmpty()) {
                return;
            }
            JsonElement typeUrlElement = object.get("@type");
            if (typeUrlElement == null) {
                throw new InvalidProtocolBufferException("Missing type url when parsing: " + json);
            }
            String typeUrl = typeUrlElement.getAsString();
            Descriptors.Descriptor contentType = this.registry.getDescriptorForTypeUrl(typeUrl);
            if (contentType == null) {
                contentType = this.oldRegistry.getDescriptorForTypeUrl(typeUrl);
                if (contentType == null) {
                    throw new InvalidProtocolBufferException("Cannot resolve type: " + typeUrl);
                }
            }
            builder.setField(typeUrlField, typeUrl);
            Message.Builder contentBuilder = DynamicMessage.getDefaultInstance(contentType).newBuilderForType();
            WellKnownTypeParser specialParser = wellKnownTypeParsers.get(contentType.getFullName());
            if (specialParser != null) {
                JsonElement value = object.get("value");
                if (value != null) {
                    specialParser.merge(this, value, contentBuilder);
                }
            } else {
                mergeMessage(json, contentBuilder, true);
            }
            builder.setField(valueField, contentBuilder.build().toByteString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeFieldMask(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            FieldMask value = FieldMaskUtil.fromJsonString(json.getAsString());
            builder.mergeFrom(value.toByteString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeTimestamp(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            try {
                Timestamp value = Timestamps.parse(json.getAsString());
                builder.mergeFrom(value.toByteString());
            } catch (UnsupportedOperationException | ParseException e) {
                InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Failed to parse timestamp: " + json);
                ex.initCause(e);
                throw ex;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeDuration(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            try {
                Duration value = Durations.parse(json.getAsString());
                builder.mergeFrom(value.toByteString());
            } catch (UnsupportedOperationException | ParseException e) {
                InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Failed to parse duration: " + json);
                ex.initCause(e);
                throw ex;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeStruct(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            Descriptors.Descriptor descriptor = builder.getDescriptorForType();
            Descriptors.FieldDescriptor field = descriptor.findFieldByName("fields");
            if (field == null) {
                throw new InvalidProtocolBufferException("Invalid Struct type.");
            }
            mergeMapField(field, json, builder);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeListValue(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            Descriptors.Descriptor descriptor = builder.getDescriptorForType();
            Descriptors.FieldDescriptor field = descriptor.findFieldByName("values");
            if (field == null) {
                throw new InvalidProtocolBufferException("Invalid ListValue type.");
            }
            mergeRepeatedField(field, json, builder);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeValue(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            Descriptors.Descriptor type = builder.getDescriptorForType();
            if (json instanceof JsonPrimitive) {
                JsonPrimitive primitive = (JsonPrimitive) json;
                if (primitive.isBoolean()) {
                    builder.setField(type.findFieldByName("bool_value"), Boolean.valueOf(primitive.getAsBoolean()));
                } else if (primitive.isNumber()) {
                    builder.setField(type.findFieldByName("number_value"), Double.valueOf(primitive.getAsDouble()));
                } else {
                    builder.setField(type.findFieldByName("string_value"), primitive.getAsString());
                }
            } else if (json instanceof JsonObject) {
                Descriptors.FieldDescriptor field = type.findFieldByName("struct_value");
                Message.Builder structBuilder = builder.newBuilderForField(field);
                merge(json, structBuilder);
                builder.setField(field, structBuilder.build());
            } else if (!(json instanceof JsonArray)) {
                if (json instanceof JsonNull) {
                    builder.setField(type.findFieldByName("null_value"), NullValue.NULL_VALUE.getValueDescriptor());
                    return;
                }
                throw new IllegalStateException("Unexpected json data: " + json);
            } else {
                Descriptors.FieldDescriptor field2 = type.findFieldByName("list_value");
                Message.Builder listBuilder = builder.newBuilderForField(field2);
                merge(json, listBuilder);
                builder.setField(field2, listBuilder.build());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void mergeWrapper(JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            Descriptors.Descriptor type = builder.getDescriptorForType();
            Descriptors.FieldDescriptor field = type.findFieldByName("value");
            if (field == null) {
                throw new InvalidProtocolBufferException("Invalid wrapper type: " + type.getFullName());
            }
            builder.setField(field, parseFieldValue(field, json, builder));
        }

        private void mergeField(Descriptors.FieldDescriptor field, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            if (field.isRepeated()) {
                if (builder.getRepeatedFieldCount(field) > 0) {
                    throw new InvalidProtocolBufferException("Field " + field.getFullName() + " has already been set.");
                }
            } else if (builder.hasField(field)) {
                throw new InvalidProtocolBufferException("Field " + field.getFullName() + " has already been set.");
            }
            if (field.isRepeated() && (json instanceof JsonNull)) {
                return;
            }
            if (field.isMapField()) {
                mergeMapField(field, json, builder);
            } else if (field.isRepeated()) {
                mergeRepeatedField(field, json, builder);
            } else if (field.getContainingOneof() != null) {
                mergeOneofField(field, json, builder);
            } else {
                Object value = parseFieldValue(field, json, builder);
                if (value != null) {
                    builder.setField(field, value);
                }
            }
        }

        private void mergeMapField(Descriptors.FieldDescriptor field, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            if (!(json instanceof JsonObject)) {
                throw new InvalidProtocolBufferException("Expect a map object but found: " + json);
            }
            Descriptors.Descriptor type = field.getMessageType();
            Descriptors.FieldDescriptor keyField = type.findFieldByName("key");
            Descriptors.FieldDescriptor valueField = type.findFieldByName("value");
            if (keyField == null || valueField == null) {
                throw new InvalidProtocolBufferException("Invalid map field: " + field.getFullName());
            }
            JsonObject object = (JsonObject) json;
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                Message.Builder entryBuilder = builder.newBuilderForField(field);
                Object key = parseFieldValue(keyField, new JsonPrimitive(entry.getKey()), entryBuilder);
                Object value = parseFieldValue(valueField, entry.getValue(), entryBuilder);
                if (value == null) {
                    if (!this.ignoringUnknownFields || valueField.getType() != Descriptors.FieldDescriptor.Type.ENUM) {
                        throw new InvalidProtocolBufferException("Map value cannot be null.");
                    }
                } else {
                    entryBuilder.setField(keyField, key);
                    entryBuilder.setField(valueField, value);
                    builder.addRepeatedField(field, entryBuilder.build());
                }
            }
        }

        private void mergeOneofField(Descriptors.FieldDescriptor field, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            Object value = parseFieldValue(field, json, builder);
            if (value == null) {
                return;
            }
            if (builder.getOneofFieldDescriptor(field.getContainingOneof()) != null) {
                throw new InvalidProtocolBufferException("Cannot set field " + field.getFullName() + " because another field " + builder.getOneofFieldDescriptor(field.getContainingOneof()).getFullName() + " belonging to the same oneof has already been set ");
            }
            builder.setField(field, value);
        }

        private void mergeRepeatedField(Descriptors.FieldDescriptor field, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            if (!(json instanceof JsonArray)) {
                throw new InvalidProtocolBufferException("Expected an array for " + field.getName() + " but found " + json);
            }
            JsonArray array = (JsonArray) json;
            for (int i = 0; i < array.size(); i++) {
                Object value = parseFieldValue(field, array.get(i), builder);
                if (value == null) {
                    if (!this.ignoringUnknownFields || field.getType() != Descriptors.FieldDescriptor.Type.ENUM) {
                        throw new InvalidProtocolBufferException("Repeated field elements cannot be null in field: " + field.getFullName());
                    }
                } else {
                    builder.addRepeatedField(field, value);
                }
            }
        }

        private int parseInt32(JsonElement json) throws InvalidProtocolBufferException {
            try {
                return Integer.parseInt(json.getAsString());
            } catch (RuntimeException e) {
                try {
                    BigDecimal value = new BigDecimal(json.getAsString());
                    return value.intValueExact();
                } catch (RuntimeException e2) {
                    InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Not an int32 value: " + json);
                    ex.initCause(e2);
                    throw ex;
                }
            }
        }

        private long parseInt64(JsonElement json) throws InvalidProtocolBufferException {
            try {
                return Long.parseLong(json.getAsString());
            } catch (RuntimeException e) {
                try {
                    BigDecimal value = new BigDecimal(json.getAsString());
                    return value.longValueExact();
                } catch (RuntimeException e2) {
                    InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Not an int64 value: " + json);
                    ex.initCause(e2);
                    throw ex;
                }
            }
        }

        private int parseUint32(JsonElement json) throws InvalidProtocolBufferException {
            try {
                long result = Long.parseLong(json.getAsString());
                if (result < 0 || result > ExpandableListView.PACKED_POSITION_VALUE_NULL) {
                    throw new InvalidProtocolBufferException("Out of range uint32 value: " + json);
                }
                return (int) result;
            } catch (RuntimeException e) {
                try {
                    BigDecimal decimalValue = new BigDecimal(json.getAsString());
                    BigInteger value = decimalValue.toBigIntegerExact();
                    if (value.signum() < 0 || value.compareTo(new BigInteger("FFFFFFFF", 16)) > 0) {
                        throw new InvalidProtocolBufferException("Out of range uint32 value: " + json);
                    }
                    return value.intValue();
                } catch (RuntimeException e2) {
                    InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Not an uint32 value: " + json);
                    ex.initCause(e2);
                    throw ex;
                }
            }
        }

        private long parseUint64(JsonElement json) throws InvalidProtocolBufferException {
            try {
                BigDecimal decimalValue = new BigDecimal(json.getAsString());
                BigInteger value = decimalValue.toBigIntegerExact();
                if (value.compareTo(BigInteger.ZERO) < 0 || value.compareTo(MAX_UINT64) > 0) {
                    throw new InvalidProtocolBufferException("Out of range uint64 value: " + json);
                }
                return value.longValue();
            } catch (RuntimeException e) {
                InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Not an uint64 value: " + json);
                ex.initCause(e);
                throw ex;
            }
        }

        private boolean parseBool(JsonElement json) throws InvalidProtocolBufferException {
            if (json.getAsString().equals("true")) {
                return true;
            }
            if (json.getAsString().equals("false")) {
                return false;
            }
            throw new InvalidProtocolBufferException("Invalid bool value: " + json);
        }

        private float parseFloat(JsonElement json) throws InvalidProtocolBufferException {
            if (json.getAsString().equals("NaN")) {
                return Float.NaN;
            }
            if (json.getAsString().equals("Infinity")) {
                return Float.POSITIVE_INFINITY;
            }
            if (json.getAsString().equals("-Infinity")) {
                return Float.NEGATIVE_INFINITY;
            }
            try {
                double value = Double.parseDouble(json.getAsString());
                if (value > 3.402826869208755E38d || value < -3.402826869208755E38d) {
                    throw new InvalidProtocolBufferException("Out of range float value: " + json);
                }
                return (float) value;
            } catch (RuntimeException e) {
                InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Not a float value: " + json);
                ex.initCause(e);
                throw e;
            }
        }

        private double parseDouble(JsonElement json) throws InvalidProtocolBufferException {
            if (json.getAsString().equals("NaN")) {
                return Double.NaN;
            }
            if (json.getAsString().equals("Infinity")) {
                return Double.POSITIVE_INFINITY;
            }
            if (json.getAsString().equals("-Infinity")) {
                return Double.NEGATIVE_INFINITY;
            }
            try {
                BigDecimal value = new BigDecimal(json.getAsString());
                if (value.compareTo(MAX_DOUBLE) > 0 || value.compareTo(MIN_DOUBLE) < 0) {
                    throw new InvalidProtocolBufferException("Out of range double value: " + json);
                }
                return value.doubleValue();
            } catch (RuntimeException e) {
                InvalidProtocolBufferException ex = new InvalidProtocolBufferException("Not a double value: " + json);
                ex.initCause(e);
                throw ex;
            }
        }

        private String parseString(JsonElement json) {
            return json.getAsString();
        }

        private ByteString parseBytes(JsonElement json) {
            try {
                return ByteString.copyFrom(BaseEncoding.base64().decode(json.getAsString()));
            } catch (IllegalArgumentException e) {
                return ByteString.copyFrom(BaseEncoding.base64Url().decode(json.getAsString()));
            }
        }

        @Nullable
        private Descriptors.EnumValueDescriptor parseEnum(Descriptors.EnumDescriptor enumDescriptor, JsonElement json) throws InvalidProtocolBufferException {
            String value = json.getAsString();
            Descriptors.EnumValueDescriptor result = enumDescriptor.findValueByName(value);
            if (result == null) {
                try {
                    int numericValue = parseInt32(json);
                    if (enumDescriptor.getFile().getSyntax() == Descriptors.FileDescriptor.Syntax.PROTO3) {
                        result = enumDescriptor.findValueByNumberCreatingIfUnknown(numericValue);
                    } else {
                        result = enumDescriptor.findValueByNumber(numericValue);
                    }
                } catch (InvalidProtocolBufferException e) {
                }
                if (result == null && !this.ignoringUnknownFields) {
                    throw new InvalidProtocolBufferException("Invalid enum value: " + value + " for enum type: " + enumDescriptor.getFullName());
                }
            }
            return result;
        }

        @Nullable
        private Object parseFieldValue(Descriptors.FieldDescriptor field, JsonElement json, Message.Builder builder) throws InvalidProtocolBufferException {
            if (json instanceof JsonNull) {
                if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE && field.getMessageType().getFullName().equals(Value.getDescriptor().getFullName())) {
                    Value value = Value.newBuilder().setNullValueValue(0).build();
                    return builder.newBuilderForField(field).mergeFrom(value.toByteString()).build();
                } else if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM && field.getEnumType().getFullName().equals(NullValue.getDescriptor().getFullName())) {
                    return field.getEnumType().findValueByNumber(0);
                } else {
                    return null;
                }
            } else if ((json instanceof JsonObject) && field.getType() != Descriptors.FieldDescriptor.Type.MESSAGE && field.getType() != Descriptors.FieldDescriptor.Type.GROUP) {
                throw new InvalidProtocolBufferException(String.format("Invalid value: %s for expected type: %s", json, field.getType()));
            } else {
                switch (field.getType()) {
                    case INT32:
                    case SINT32:
                    case SFIXED32:
                        return Integer.valueOf(parseInt32(json));
                    case INT64:
                    case SINT64:
                    case SFIXED64:
                        return Long.valueOf(parseInt64(json));
                    case BOOL:
                        return Boolean.valueOf(parseBool(json));
                    case FLOAT:
                        return Float.valueOf(parseFloat(json));
                    case DOUBLE:
                        return Double.valueOf(parseDouble(json));
                    case UINT32:
                    case FIXED32:
                        return Integer.valueOf(parseUint32(json));
                    case UINT64:
                    case FIXED64:
                        return Long.valueOf(parseUint64(json));
                    case STRING:
                        return parseString(json);
                    case BYTES:
                        return parseBytes(json);
                    case ENUM:
                        return parseEnum(field.getEnumType(), json);
                    case MESSAGE:
                    case GROUP:
                        if (this.currentDepth >= this.recursionLimit) {
                            throw new InvalidProtocolBufferException("Hit recursion limit.");
                        }
                        this.currentDepth++;
                        Message.Builder subBuilder = builder.newBuilderForField(field);
                        merge(json, subBuilder);
                        this.currentDepth--;
                        return subBuilder.build();
                    default:
                        throw new InvalidProtocolBufferException("Invalid field type: " + field.getType());
                }
            }
        }
    }
}
