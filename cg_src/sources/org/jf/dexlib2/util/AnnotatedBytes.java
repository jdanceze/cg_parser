package org.jf.dexlib2.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.util.ExceptionWithContext;
import org.jf.util.Hex;
import org.jf.util.TwoColumnOutput;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/AnnotatedBytes.class */
public class AnnotatedBytes {
    private int cursor;
    private int indentLevel;
    private int outputWidth;
    @Nonnull
    private TreeMap<Integer, AnnotationEndpoint> annotatations = Maps.newTreeMap();
    private int hexCols = 8;
    private int startLimit = -1;
    private int endLimit = -1;

    public AnnotatedBytes(int width) {
        this.outputWidth = width;
    }

    public void moveTo(int offset) {
        this.cursor = offset;
    }

    public void moveBy(int offset) {
        this.cursor += offset;
    }

    public void annotateTo(int offset, @Nonnull String msg, Object... formatArgs) {
        annotate(offset - this.cursor, msg, formatArgs);
    }

    public void annotate(int length, @Nonnull String msg, Object... formatArgs) {
        String formattedMsg;
        AnnotationItem existingRangeAnnotation;
        Map.Entry<Integer, AnnotationEndpoint> nextEntry;
        if (this.startLimit != -1 && this.endLimit != -1 && (this.cursor < this.startLimit || this.cursor >= this.endLimit)) {
            throw new ExceptionWithContext("Annotating outside the parent bounds", new Object[0]);
        }
        if (formatArgs != null && formatArgs.length > 0) {
            formattedMsg = String.format(msg, formatArgs);
        } else {
            formattedMsg = msg;
        }
        int exclusiveEndOffset = this.cursor + length;
        AnnotationEndpoint endPoint = null;
        AnnotationEndpoint startPoint = this.annotatations.get(Integer.valueOf(this.cursor));
        if (startPoint == null) {
            Map.Entry<Integer, AnnotationEndpoint> previousEntry = this.annotatations.lowerEntry(Integer.valueOf(this.cursor));
            if (previousEntry != null) {
                AnnotationEndpoint previousAnnotations = previousEntry.getValue();
                AnnotationItem previousRangeAnnotation = previousAnnotations.rangeAnnotation;
                if (previousRangeAnnotation != null) {
                    throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(this.cursor, Integer.valueOf(this.cursor + length), formattedMsg), formatAnnotation(previousEntry.getKey().intValue(), previousRangeAnnotation.annotation));
                }
            }
        } else if (length > 0 && (existingRangeAnnotation = startPoint.rangeAnnotation) != null) {
            throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(this.cursor, Integer.valueOf(this.cursor + length), formattedMsg), formatAnnotation(this.cursor, existingRangeAnnotation.annotation));
        }
        if (length > 0 && (nextEntry = this.annotatations.higherEntry(Integer.valueOf(this.cursor))) != null) {
            int nextKey = nextEntry.getKey().intValue();
            if (nextKey < exclusiveEndOffset) {
                AnnotationEndpoint nextEndpoint = nextEntry.getValue();
                AnnotationItem nextRangeAnnotation = nextEndpoint.rangeAnnotation;
                if (nextRangeAnnotation != null) {
                    throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(this.cursor, Integer.valueOf(this.cursor + length), formattedMsg), formatAnnotation(nextKey, nextRangeAnnotation.annotation));
                }
                if (nextEndpoint.pointAnnotations.size() > 0) {
                    throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation %s", formatAnnotation(this.cursor, Integer.valueOf(this.cursor + length), formattedMsg), formatAnnotation(nextKey, Integer.valueOf(nextKey), nextEndpoint.pointAnnotations.get(0).annotation));
                }
                throw new ExceptionWithContext("Cannot add annotation %s, due to existing annotation endpoint at %d", formatAnnotation(this.cursor, Integer.valueOf(this.cursor + length), formattedMsg), Integer.valueOf(nextKey));
            } else if (nextKey == exclusiveEndOffset) {
                endPoint = nextEntry.getValue();
            }
        }
        if (startPoint == null) {
            startPoint = new AnnotationEndpoint();
            this.annotatations.put(Integer.valueOf(this.cursor), startPoint);
        }
        if (length == 0) {
            startPoint.pointAnnotations.add(new AnnotationItem(this.indentLevel, formattedMsg));
        } else {
            startPoint.rangeAnnotation = new AnnotationItem(this.indentLevel, formattedMsg);
            if (endPoint == null) {
                this.annotatations.put(Integer.valueOf(exclusiveEndOffset), new AnnotationEndpoint());
            }
        }
        this.cursor += length;
    }

    private String formatAnnotation(int offset, String annotationMsg) {
        Integer endOffset = this.annotatations.higherKey(Integer.valueOf(offset));
        return formatAnnotation(offset, endOffset, annotationMsg);
    }

    private String formatAnnotation(int offset, Integer endOffset, String annotationMsg) {
        if (endOffset != null) {
            return String.format("[0x%x, 0x%x) \"%s\"", Integer.valueOf(offset), endOffset, annotationMsg);
        }
        return String.format("[0x%x, ) \"%s\"", Integer.valueOf(offset), annotationMsg);
    }

    public void indent() {
        this.indentLevel++;
    }

    public void deindent() {
        this.indentLevel--;
        if (this.indentLevel < 0) {
            this.indentLevel = 0;
        }
    }

    public int getCursor() {
        return this.cursor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/AnnotatedBytes$AnnotationEndpoint.class */
    public static class AnnotationEndpoint {
        @Nonnull
        public final List<AnnotationItem> pointAnnotations;
        @Nullable
        public AnnotationItem rangeAnnotation;

        private AnnotationEndpoint() {
            this.pointAnnotations = Lists.newArrayList();
            this.rangeAnnotation = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/util/AnnotatedBytes$AnnotationItem.class */
    public static class AnnotationItem {
        public final int indentLevel;
        public final String annotation;

        public AnnotationItem(int indentLevel, String annotation) {
            this.indentLevel = indentLevel;
            this.annotation = annotation;
        }
    }

    public int getAnnotationWidth() {
        int leftWidth = 8 + (this.hexCols * 2) + (this.hexCols / 2);
        return this.outputWidth - leftWidth;
    }

    public void writeAnnotations(Writer out, byte[] data, int offset) throws IOException {
        String str;
        int rightWidth = getAnnotationWidth();
        int leftWidth = (this.outputWidth - rightWidth) - 1;
        String padding = Strings.repeat(Instruction.argsep, 1000);
        TwoColumnOutput twoc = new TwoColumnOutput(out, leftWidth, rightWidth, "|");
        Integer[] keys = (Integer[]) this.annotatations.keySet().toArray(new Integer[this.annotatations.size()]);
        AnnotationEndpoint[] values = new AnnotationEndpoint[this.annotatations.size()];
        AnnotationEndpoint[] values2 = (AnnotationEndpoint[]) this.annotatations.values().toArray(values);
        for (int i = 0; i < keys.length - 1; i++) {
            int rangeStart = keys[i].intValue();
            int rangeEnd = keys[i + 1].intValue();
            AnnotationEndpoint annotations = values2[i];
            for (AnnotationItem pointAnnotation : annotations.pointAnnotations) {
                String paddingSub = padding.substring(0, pointAnnotation.indentLevel * 2);
                twoc.write("", paddingSub + pointAnnotation.annotation);
            }
            AnnotationItem rangeAnnotation = annotations.rangeAnnotation;
            if (rangeAnnotation != null) {
                String right = padding.substring(0, rangeAnnotation.indentLevel * 2);
                str = right + rangeAnnotation.annotation;
            } else {
                str = "";
            }
            String right2 = str;
            String left = Hex.dump(data, rangeStart + offset, rangeEnd - rangeStart, rangeStart + offset, this.hexCols, 6);
            twoc.write(left, right2);
        }
        int lastKey = keys[keys.length - 1].intValue();
        if (lastKey < data.length) {
            String left2 = Hex.dump(data, lastKey + offset, (data.length - offset) - lastKey, lastKey + offset, this.hexCols, 6);
            twoc.write(left2, "");
        }
    }

    public void setLimit(int start, int end) {
        this.startLimit = start;
        this.endLimit = end;
    }

    public void clearLimit() {
        this.startLimit = -1;
        this.endLimit = -1;
    }
}
