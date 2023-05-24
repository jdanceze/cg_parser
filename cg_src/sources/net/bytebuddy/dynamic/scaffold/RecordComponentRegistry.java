package net.bytebuddy.dynamic.scaffold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.scaffold.TypeWriter;
import net.bytebuddy.implementation.attribute.RecordComponentAttributeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.LatentMatcher;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/RecordComponentRegistry.class */
public interface RecordComponentRegistry {
    RecordComponentRegistry prepend(LatentMatcher<? super RecordComponentDescription> latentMatcher, RecordComponentAttributeAppender.Factory factory, Transformer<RecordComponentDescription> transformer);

    Compiled compile(TypeDescription typeDescription);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/RecordComponentRegistry$Compiled.class */
    public interface Compiled extends TypeWriter.RecordComponentPool {

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/RecordComponentRegistry$Compiled$NoOp.class */
        public enum NoOp implements Compiled {
            INSTANCE;

            @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.RecordComponentPool
            public TypeWriter.RecordComponentPool.Record target(RecordComponentDescription recordComponentDescription) {
                return new TypeWriter.RecordComponentPool.Record.ForImplicitRecordComponent(recordComponentDescription);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/RecordComponentRegistry$Default.class */
    public static class Default implements RecordComponentRegistry {
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

        @Override // net.bytebuddy.dynamic.scaffold.RecordComponentRegistry
        public RecordComponentRegistry prepend(LatentMatcher<? super RecordComponentDescription> matcher, RecordComponentAttributeAppender.Factory recordComponentAttributeAppenderFactory, Transformer<RecordComponentDescription> transformer) {
            List<Entry> entries = new ArrayList<>(this.entries.size() + 1);
            entries.add(new Entry(matcher, recordComponentAttributeAppenderFactory, transformer));
            entries.addAll(this.entries);
            return new Default(entries);
        }

        @Override // net.bytebuddy.dynamic.scaffold.RecordComponentRegistry
        public Compiled compile(TypeDescription instrumentedType) {
            List<Compiled.Entry> entries = new ArrayList<>(this.entries.size());
            Map<RecordComponentAttributeAppender.Factory, RecordComponentAttributeAppender> recordComponentAttributeAppenders = new HashMap<>();
            for (Entry entry : this.entries) {
                RecordComponentAttributeAppender recordComponentAttributeAppender = recordComponentAttributeAppenders.get(entry.getRecordComponentAttributeAppender());
                if (recordComponentAttributeAppender == null) {
                    recordComponentAttributeAppender = entry.getRecordComponentAttributeAppender().make(instrumentedType);
                    recordComponentAttributeAppenders.put(entry.getRecordComponentAttributeAppender(), recordComponentAttributeAppender);
                }
                entries.add(new Compiled.Entry(entry.resolve(instrumentedType), recordComponentAttributeAppender, entry.getTransformer()));
            }
            return new Compiled(instrumentedType, entries);
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/RecordComponentRegistry$Default$Entry.class */
        protected static class Entry implements LatentMatcher<RecordComponentDescription> {
            private final LatentMatcher<? super RecordComponentDescription> matcher;
            private final RecordComponentAttributeAppender.Factory recordComponentAttributeAppender;
            private final Transformer<RecordComponentDescription> transformer;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher) && this.recordComponentAttributeAppender.equals(((Entry) obj).recordComponentAttributeAppender) && this.transformer.equals(((Entry) obj).transformer);
            }

            public int hashCode() {
                return (((((17 * 31) + this.matcher.hashCode()) * 31) + this.recordComponentAttributeAppender.hashCode()) * 31) + this.transformer.hashCode();
            }

            protected Entry(LatentMatcher<? super RecordComponentDescription> matcher, RecordComponentAttributeAppender.Factory recordComponentAttributeAppender, Transformer<RecordComponentDescription> transformer) {
                this.matcher = matcher;
                this.recordComponentAttributeAppender = recordComponentAttributeAppender;
                this.transformer = transformer;
            }

            protected RecordComponentAttributeAppender.Factory getRecordComponentAttributeAppender() {
                return this.recordComponentAttributeAppender;
            }

            protected Transformer<RecordComponentDescription> getTransformer() {
                return this.transformer;
            }

            @Override // net.bytebuddy.matcher.LatentMatcher
            public ElementMatcher<? super RecordComponentDescription> resolve(TypeDescription typeDescription) {
                return this.matcher.resolve(typeDescription);
            }
        }

        @HashCodeAndEqualsPlugin.Enhance
        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/RecordComponentRegistry$Default$Compiled.class */
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

            @Override // net.bytebuddy.dynamic.scaffold.TypeWriter.RecordComponentPool
            public TypeWriter.RecordComponentPool.Record target(RecordComponentDescription recordComponentDescription) {
                for (Entry entry : this.entries) {
                    if (entry.matches(recordComponentDescription)) {
                        return entry.bind(this.instrumentedType, recordComponentDescription);
                    }
                }
                return new TypeWriter.RecordComponentPool.Record.ForImplicitRecordComponent(recordComponentDescription);
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/RecordComponentRegistry$Default$Compiled$Entry.class */
            protected static class Entry implements ElementMatcher<RecordComponentDescription> {
                private final ElementMatcher<? super RecordComponentDescription> matcher;
                private final RecordComponentAttributeAppender recordComponentAttributeAppender;
                private final Transformer<RecordComponentDescription> transformer;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.matcher.equals(((Entry) obj).matcher) && this.recordComponentAttributeAppender.equals(((Entry) obj).recordComponentAttributeAppender) && this.transformer.equals(((Entry) obj).transformer);
                }

                public int hashCode() {
                    return (((((17 * 31) + this.matcher.hashCode()) * 31) + this.recordComponentAttributeAppender.hashCode()) * 31) + this.transformer.hashCode();
                }

                protected Entry(ElementMatcher<? super RecordComponentDescription> matcher, RecordComponentAttributeAppender recordComponentAttributeAppender, Transformer<RecordComponentDescription> transformer) {
                    this.matcher = matcher;
                    this.recordComponentAttributeAppender = recordComponentAttributeAppender;
                    this.transformer = transformer;
                }

                protected TypeWriter.RecordComponentPool.Record bind(TypeDescription instrumentedType, RecordComponentDescription recordComponentDescription) {
                    return new TypeWriter.RecordComponentPool.Record.ForExplicitRecordComponent(this.recordComponentAttributeAppender, this.transformer.transform(instrumentedType, recordComponentDescription));
                }

                @Override // net.bytebuddy.matcher.ElementMatcher
                public boolean matches(RecordComponentDescription target) {
                    return this.matcher.matches(target);
                }
            }
        }
    }
}
