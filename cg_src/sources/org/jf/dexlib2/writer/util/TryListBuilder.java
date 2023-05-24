package org.jf.dexlib2.writer.util;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/util/TryListBuilder.class */
public class TryListBuilder<EH extends ExceptionHandler> {
    private final MutableTryBlock<EH> listStart = new MutableTryBlock<>(0, 0);
    private final MutableTryBlock<EH> listEnd = new MutableTryBlock<>(0, 0);

    public TryListBuilder() {
        this.listStart.next = this.listEnd;
        this.listEnd.prev = this.listStart;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <EH extends ExceptionHandler> List<TryBlock<EH>> massageTryBlocks(List<? extends TryBlock<? extends EH>> tryBlocks) {
        TryListBuilder tryListBuilder = new TryListBuilder();
        for (TryBlock<? extends EH> tryBlock : tryBlocks) {
            int startAddress = tryBlock.getStartCodeAddress();
            int endAddress = startAddress + tryBlock.getCodeUnitCount();
            Iterator<? extends Object> it = tryBlock.getExceptionHandlers().iterator();
            while (it.hasNext()) {
                tryListBuilder.addHandler(startAddress, endAddress, (ExceptionHandler) it.next());
            }
        }
        return tryListBuilder.getTryBlocks();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/util/TryListBuilder$TryBounds.class */
    public static class TryBounds<EH extends ExceptionHandler> {
        @Nonnull
        public final MutableTryBlock<EH> start;
        @Nonnull
        public final MutableTryBlock<EH> end;

        public TryBounds(@Nonnull MutableTryBlock<EH> start, @Nonnull MutableTryBlock<EH> end) {
            this.start = start;
            this.end = end;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/util/TryListBuilder$InvalidTryException.class */
    public static class InvalidTryException extends ExceptionWithContext {
        public InvalidTryException(Throwable cause) {
            super(cause);
        }

        public InvalidTryException(Throwable cause, String message, Object... formatArgs) {
            super(cause, message, formatArgs);
        }

        public InvalidTryException(String message, Object... formatArgs) {
            super(message, formatArgs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/util/TryListBuilder$MutableTryBlock.class */
    public static class MutableTryBlock<EH extends ExceptionHandler> extends BaseTryBlock<EH> {
        public MutableTryBlock<EH> prev;
        public MutableTryBlock<EH> next;
        public int startCodeAddress;
        public int endCodeAddress;
        @Nonnull
        public List<EH> exceptionHandlers;

        public MutableTryBlock(int startCodeAddress, int endCodeAddress) {
            this.prev = null;
            this.next = null;
            this.exceptionHandlers = Lists.newArrayList();
            this.startCodeAddress = startCodeAddress;
            this.endCodeAddress = endCodeAddress;
        }

        public MutableTryBlock(int startCodeAddress, int endCodeAddress, @Nonnull List<EH> exceptionHandlers) {
            this.prev = null;
            this.next = null;
            this.exceptionHandlers = Lists.newArrayList();
            this.startCodeAddress = startCodeAddress;
            this.endCodeAddress = endCodeAddress;
            this.exceptionHandlers = Lists.newArrayList(exceptionHandlers);
        }

        @Override // org.jf.dexlib2.iface.TryBlock
        public int getStartCodeAddress() {
            return this.startCodeAddress;
        }

        @Override // org.jf.dexlib2.iface.TryBlock
        public int getCodeUnitCount() {
            return this.endCodeAddress - this.startCodeAddress;
        }

        @Override // org.jf.dexlib2.iface.TryBlock
        @Nonnull
        public List<EH> getExceptionHandlers() {
            return this.exceptionHandlers;
        }

        @Nonnull
        public MutableTryBlock<EH> split(int splitAddress) {
            MutableTryBlock<EH> newTryBlock = new MutableTryBlock<>(splitAddress, this.endCodeAddress, this.exceptionHandlers);
            this.endCodeAddress = splitAddress;
            append(newTryBlock);
            return newTryBlock;
        }

        public void delete() {
            this.next.prev = this.prev;
            this.prev.next = this.next;
        }

        public void mergeNext() {
            this.endCodeAddress = this.next.endCodeAddress;
            this.next.delete();
        }

        public void append(@Nonnull MutableTryBlock<EH> tryBlock) {
            this.next.prev = tryBlock;
            tryBlock.next = this.next;
            tryBlock.prev = this;
            this.next = tryBlock;
        }

        public void prepend(@Nonnull MutableTryBlock<EH> tryBlock) {
            this.prev.next = tryBlock;
            tryBlock.prev = this.prev;
            tryBlock.next = this;
            this.prev = tryBlock;
        }

        public void addHandler(@Nonnull EH handler) {
            for (ExceptionHandler existingHandler : this.exceptionHandlers) {
                String existingType = existingHandler.getExceptionType();
                String newType = handler.getExceptionType();
                if (existingType == null) {
                    if (newType == null) {
                        if (existingHandler.getHandlerCodeAddress() != handler.getHandlerCodeAddress()) {
                            throw new InvalidTryException("Multiple overlapping catch all handlers with different handlers", new Object[0]);
                        }
                        return;
                    }
                } else if (existingType.equals(newType)) {
                    return;
                }
            }
            this.exceptionHandlers.add(handler);
        }
    }

    private TryBounds<EH> getBoundingRanges(int startAddress, int endAddress) {
        MutableTryBlock<EH> startBlock = null;
        MutableTryBlock<EH> mutableTryBlock = this.listStart.next;
        while (true) {
            MutableTryBlock<EH> tryBlock = mutableTryBlock;
            if (tryBlock == this.listEnd) {
                break;
            }
            int currentStartAddress = tryBlock.startCodeAddress;
            int currentEndAddress = tryBlock.endCodeAddress;
            if (startAddress == currentStartAddress) {
                startBlock = tryBlock;
                break;
            } else if (startAddress > currentStartAddress && startAddress < currentEndAddress) {
                startBlock = tryBlock.split(startAddress);
                break;
            } else if (startAddress < currentStartAddress) {
                if (endAddress <= currentStartAddress) {
                    MutableTryBlock<EH> startBlock2 = new MutableTryBlock<>(startAddress, endAddress);
                    tryBlock.prepend(startBlock2);
                    return new TryBounds<>(startBlock2, startBlock2);
                }
                startBlock = new MutableTryBlock<>(startAddress, currentStartAddress);
                tryBlock.prepend(startBlock);
            } else {
                mutableTryBlock = tryBlock.next;
            }
        }
        if (startBlock == null) {
            MutableTryBlock<EH> startBlock3 = new MutableTryBlock<>(startAddress, endAddress);
            this.listEnd.prepend(startBlock3);
            return new TryBounds<>(startBlock3, startBlock3);
        }
        MutableTryBlock<EH> mutableTryBlock2 = startBlock;
        while (true) {
            MutableTryBlock<EH> tryBlock2 = mutableTryBlock2;
            if (tryBlock2 != this.listEnd) {
                int currentStartAddress2 = tryBlock2.startCodeAddress;
                int currentEndAddress2 = tryBlock2.endCodeAddress;
                if (endAddress == currentEndAddress2) {
                    return new TryBounds<>(startBlock, tryBlock2);
                }
                if (endAddress > currentStartAddress2 && endAddress < currentEndAddress2) {
                    tryBlock2.split(endAddress);
                    return new TryBounds<>(startBlock, tryBlock2);
                } else if (endAddress <= currentStartAddress2) {
                    MutableTryBlock<EH> endBlock = new MutableTryBlock<>(tryBlock2.prev.endCodeAddress, endAddress);
                    tryBlock2.prepend(endBlock);
                    return new TryBounds<>(startBlock, endBlock);
                } else {
                    mutableTryBlock2 = tryBlock2.next;
                }
            } else {
                MutableTryBlock<EH> endBlock2 = new MutableTryBlock<>(this.listEnd.prev.endCodeAddress, endAddress);
                this.listEnd.prepend(endBlock2);
                return new TryBounds<>(startBlock, endBlock2);
            }
        }
    }

    public void addHandler(int startAddress, int endAddress, EH handler) {
        TryBounds<EH> bounds = getBoundingRanges(startAddress, endAddress);
        MutableTryBlock<EH> startBlock = bounds.start;
        MutableTryBlock<EH> endBlock = bounds.end;
        int previousEnd = startAddress;
        MutableTryBlock<EH> tryBlock = startBlock;
        do {
            if (tryBlock.startCodeAddress > previousEnd) {
                MutableTryBlock<EH> newBlock = new MutableTryBlock<>(previousEnd, tryBlock.startCodeAddress);
                tryBlock.prepend(newBlock);
                tryBlock = newBlock;
            }
            tryBlock.addHandler(handler);
            previousEnd = tryBlock.endCodeAddress;
            tryBlock = tryBlock.next;
        } while (tryBlock.prev != endBlock);
    }

    public List<TryBlock<EH>> getTryBlocks() {
        return Lists.newArrayList(new Iterator<TryBlock<EH>>() { // from class: org.jf.dexlib2.writer.util.TryListBuilder.1
            @Nullable
            private MutableTryBlock<EH> next;

            {
                this.next = TryListBuilder.this.listStart;
                this.next = readNextItem();
            }

            @Nullable
            protected MutableTryBlock<EH> readNextItem() {
                MutableTryBlock<EH> ret = this.next.next;
                if (ret != TryListBuilder.this.listEnd) {
                    while (ret.next != TryListBuilder.this.listEnd && ret.endCodeAddress == ret.next.startCodeAddress && ret.getExceptionHandlers().equals(ret.next.getExceptionHandlers())) {
                        ret.mergeNext();
                    }
                    return ret;
                }
                return null;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next != null;
            }

            @Override // java.util.Iterator
            @Nonnull
            public TryBlock<EH> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                TryBlock<EH> ret = this.next;
                this.next = readNextItem();
                return ret;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        });
    }
}
