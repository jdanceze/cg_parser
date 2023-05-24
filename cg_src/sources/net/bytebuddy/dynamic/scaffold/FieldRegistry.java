package net.bytebuddy.dynamic.scaffold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import net.bytebuddy.implementation.attribute.FieldAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.LatentMatcher;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/FieldRegistry.class */
public interface FieldRegistry {
    FieldRegistry prepend(LatentMatcher<? super FieldDescription> latentMatcher, FieldAttributeAppender.Factory factory, Object obj, Transformer<FieldDescription> transformer);

    Compiled compile(TypeDescription typeDescription);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/FieldRegistry$Compiled.class */
    public interface Compiled extends TypeWriter.FieldPool {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/FieldRegistry$Compiled$NoOp.class */
        public enum NoOp implements Compiled {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.FieldPool
            public TypeWriter.FieldPool.Record target(FieldDescription fieldDescription) {
                return new TypeWriter.FieldPool.Record.ForImplicitField(fieldDescription);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/FieldRegistry$Default.class */
    public static class Default implements FieldRegistry {
        private final List<Entry> entries;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.entries.equals(((Default) obj).entries);
        }

        public int hashCode() {
            return (17 * 31) + this.entries.hashCode();
        }

        public Default() {
            this(Collections.emptyList());
        }

        private Default(List<Entry> entries) {
            this.entries = entries;
        }

        @Override // net.bytebuddy.dynamic.scaffold.FieldRegistry
        public FieldRegistry prepend(LatentMatcher<? super FieldDescription> matcher, FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Object defaultValue, Transformer<FieldDescription> transformer) {
            List<Entry> entries = new ArrayList<>(this.entries.size() + 1);
            entries.add(new Entry(matcher, fieldAttributeAppenderFactory, defaultValue, transformer));
            entries.addAll(this.entries);
            return new Default(entries);
        }

        @Override // net.bytebuddy.dynamic.scaffold.FieldRegistry
        public Compiled compile(TypeDescription instrumentedType) {
            List<Compiled.Entry> entries = new ArrayList<>(this.entries.size());
            Map<FieldAttributeAppender.Factory, FieldAttributeAppender> fieldAttributeAppenders = new HashMap<>();
            for (Entry entry : this.entries) {
                FieldAttributeAppender fieldAttributeAppender = fieldAttributeAppenders.get(entry.getFieldAttributeAppenderFactory());
                if (fieldAttributeAppender == null) {
                    fieldAttributeAppender = entry.getFieldAttributeAppenderFactory().make(instrumentedType);
                    fieldAttributeAppenders.put(entry.getFieldAttributeAppenderFactory(), fieldAttributeAppender);
                }
                entries.add(new Compiled.Entry(entry.resolve(instrumentedType), fieldAttributeAppender, entry.getDefaultValue(), entry.getTransformer()));
            }
            return new Compiled(instrumentedType, entries);
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/FieldRegistry$Default$Entry.class */
        protected static class Entry implements LatentMatcher<FieldDescription> {
            private final LatentMatcher<? super FieldDescription> matcher;
            private final FieldAttributeAppender.Factory fieldAttributeAppenderFactory;
            private final Object defaultValue;
            private final Transformer<FieldDescription> transformer;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher) && this.fieldAttributeAppenderFactory.equals(((Entry) obj).fieldAttributeAppenderFactory) && this.defaultValue.equals(((Entry) obj).defaultValue) && this.transformer.equals(((Entry) obj).transformer);
            }

            public int hashCode() {
                return (((((((17 * 31) + this.matcher.hashCode()) * 31) + this.fieldAttributeAppenderFactory.hashCode()) * 31) + this.defaultValue.hashCode()) * 31) + this.transformer.hashCode();
            }

            protected Entry(LatentMatcher<? super FieldDescription> matcher, FieldAttributeAppender.Factory fieldAttributeAppenderFactory, Object defaultValue, Transformer<FieldDescription> transformer) {
                this.matcher = matcher;
                this.fieldAttributeAppenderFactory = fieldAttributeAppenderFactory;
                this.defaultValue = defaultValue;
                this.transformer = transformer;
            }

            protected FieldAttributeAppender.Factory getFieldAttributeAppenderFactory() {
                return this.fieldAttributeAppenderFactory;
            }

            protected Object getDefaultValue() {
                return this.defaultValue;
            }

            protected Transformer<FieldDescription> getTransformer() {
                return this.transformer;
            }

            @Override // net.bytebuddy.matcher.LatentMatcher
            public ElementMatcher<? super FieldDescription> resolve(TypeDescription typeDescription) {
                return this.matcher.resolve(typeDescription);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/FieldRegistry$Default$Compiled.class */
        protected static class Compiled implements Compiled {
            private final TypeDescription instrumentedType;
            private final List<Entry> entries;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.instrumentedType.equals(((Compiled) obj).instrumentedType) && this.entries.equals(((Compiled) obj).entries);
            }

            public int hashCode() {
                return (((17 * 31) + this.instrumentedType.hashCode()) * 31) + this.entries.hashCode();
            }

            protected Compiled(TypeDescription instrumentedType, List<Entry> entries) {
                this.instrumentedType = instrumentedType;
                this.entries = entries;
            }

            @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.FieldPool
            public TypeWriter.FieldPool.Record target(FieldDescription fieldDescription) {
                for (Entry entry : this.entries) {
                    if (entry.matches(fieldDescription)) {
                        return entry.bind(this.instrumentedType, fieldDescription);
                    }
                }
                return new TypeWriter.FieldPool.Record.ForImplicitField(fieldDescription);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/FieldRegistry$Default$Compiled$Entry.class */
            protected static class Entry implements ElementMatcher<FieldDescription> {
                private final ElementMatcher<? super FieldDescription> matcher;
                private final FieldAttributeAppender fieldAttributeAppender;
                private final Object defaultValue;
                private final Transformer<FieldDescription> transformer;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher) && this.fieldAttributeAppender.equals(((Entry) obj).fieldAttributeAppender) && this.defaultValue.equals(((Entry) obj).defaultValue) && this.transformer.equals(((Entry) obj).transformer);
                }

                public int hashCode() {
                    return (((((((17 * 31) + this.matcher.hashCode()) * 31) + this.fieldAttributeAppender.hashCode()) * 31) + this.defaultValue.hashCode()) * 31) + this.transformer.hashCode();
                }

                protected Entry(ElementMatcher<? super FieldDescription> matcher, FieldAttributeAppender fieldAttributeAppender, Object defaultValue, Transformer<FieldDescription> transformer) {
                    this.matcher = matcher;
                    this.fieldAttributeAppender = fieldAttributeAppender;
                    this.defaultValue = defaultValue;
                    this.transformer = transformer;
                }

                protected TypeWriter.FieldPool.Record bind(TypeDescription instrumentedType, FieldDescription fieldDescription) {
                    return new TypeWriter.FieldPool.Record.ForExplicitField(this.fieldAttributeAppender, this.defaultValue, this.transformer.transform(instrumentedType, fieldDescription));
                }

                @Override // net.bytebuddy.matcher.ElementMatcher
                public boolean matches(FieldDescription target) {
                    return this.matcher.matches(target);
                }
            }
        }
    }
}
