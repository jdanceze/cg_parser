.class public Lcom/apposcopy/ella/runtime/SequenceRecorder;
.super Lcom/apposcopy/ella/runtime/Recorder;
.source "SequenceRecorder.java"


# static fields
.field static final synthetic $assertionsDisabled:Z


# instance fields
.field protected data:[I

.field protected metadata:[J

.field private readp:Ljava/util/concurrent/atomic/AtomicInteger;

.field private rwl:Ljava/util/concurrent/locks/ReadWriteLock;

.field private writep:Ljava/util/concurrent/atomic/AtomicInteger;


# direct methods
.method static constructor <clinit>()V
    .locals 1

    .prologue
    .line 7
    const-class v0, Lcom/apposcopy/ella/runtime/SequenceRecorder;

    invoke-virtual {v0}, Ljava/lang/Class;->desiredAssertionStatus()Z

    move-result v0

    if-nez v0, :cond_0

    const/4 v0, 0x1

    :goto_0
    sput-boolean v0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->$assertionsDisabled:Z

    return-void

    :cond_0
    const/4 v0, 0x0

    goto :goto_0
.end method

.method public constructor <init>()V
    .locals 3

    .prologue
    const/16 v2, 0x2710

    const/4 v1, 0x0

    .line 7
    invoke-direct {p0}, Lcom/apposcopy/ella/runtime/Recorder;-><init>()V

    .line 14
    new-instance v0, Ljava/util/concurrent/locks/ReentrantReadWriteLock;

    invoke-direct {v0}, Ljava/util/concurrent/locks/ReentrantReadWriteLock;-><init>()V

    iput-object v0, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    .line 15
    new-instance v0, Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-direct {v0, v1}, Ljava/util/concurrent/atomic/AtomicInteger;-><init>(I)V

    iput-object v0, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->readp:Ljava/util/concurrent/atomic/AtomicInteger;

    .line 16
    new-instance v0, Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-direct {v0, v1}, Ljava/util/concurrent/atomic/AtomicInteger;-><init>(I)V

    iput-object v0, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->writep:Ljava/util/concurrent/atomic/AtomicInteger;

    .line 18
    new-array v0, v2, [I

    iput-object v0, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    .line 19
    new-array v0, v2, [J

    iput-object v0, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->metadata:[J

    return-void
.end method


# virtual methods
.method public data()Ljava/lang/String;
    .locals 10

    .prologue
    .line 64
    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    .line 67
    .local v0, "builder":Ljava/lang/StringBuilder;
    iget-object v6, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v6}, Ljava/util/concurrent/locks/ReadWriteLock;->writeLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v6

    invoke-interface {v6}, Ljava/util/concurrent/locks/Lock;->lock()V

    .line 68
    iget-object v3, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    .line 69
    .local v3, "seq":[I
    iget-object v4, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->metadata:[J

    .line 70
    .local v4, "ts":[J
    iget-object v6, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->writep:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v6}, Ljava/util/concurrent/atomic/AtomicInteger;->get()I

    move-result v5

    .line 72
    .local v5, "wi":I
    iget-object v6, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->readp:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v6, v5}, Ljava/util/concurrent/atomic/AtomicInteger;->getAndSet(I)I

    move-result v2

    .local v2, "ri":I
    :goto_0
    if-ge v2, v5, :cond_0

    .line 73
    iget-object v6, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v6, v6

    rem-int v1, v2, v6

    .line 74
    .local v1, "i":I
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    aget-wide v8, v4, v1

    invoke-virtual {v6, v8, v9}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, " "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    aget v7, v3, v1

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, "\n"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v0, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    .line 72
    add-int/lit8 v2, v2, 0x1

    goto :goto_0

    .line 76
    .end local v1    # "i":I
    :cond_0
    iget-object v6, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v6}, Ljava/util/concurrent/locks/ReadWriteLock;->writeLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v6

    invoke-interface {v6}, Ljava/util/concurrent/locks/Lock;->unlock()V

    .line 77
    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    return-object v6
.end method

.method public record(IJ)V
    .locals 10
    .param p1, "d"    # I
    .param p2, "md"    # J

    .prologue
    const/4 v9, 0x0

    .line 23
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v7}, Ljava/util/concurrent/locks/ReadWriteLock;->readLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/concurrent/locks/Lock;->lock()V

    .line 24
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->writep:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v7}, Ljava/util/concurrent/atomic/AtomicInteger;->getAndIncrement()I

    move-result v6

    .line 25
    .local v6, "wi":I
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->readp:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v7}, Ljava/util/concurrent/atomic/AtomicInteger;->get()I

    move-result v5

    .line 26
    .local v5, "ri":I
    sub-int v7, v6, v5

    iget-object v8, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v8, v8

    if-lt v7, v8, :cond_2

    .line 28
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v7}, Ljava/util/concurrent/locks/ReadWriteLock;->readLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/concurrent/locks/Lock;->unlock()V

    .line 29
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v7}, Ljava/util/concurrent/locks/ReadWriteLock;->writeLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/concurrent/locks/Lock;->lock()V

    .line 30
    sub-int v7, v6, v5

    iget-object v8, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v8, v8

    if-lt v7, v8, :cond_1

    .line 34
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v7, v7

    rem-int/2addr v6, v7

    .line 35
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v7, v7

    rem-int/2addr v5, v7

    .line 36
    if-ge v6, v5, :cond_0

    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v7, v7

    add-int/2addr v6, v7

    .line 37
    :cond_0
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->writep:Ljava/util/concurrent/atomic/AtomicInteger;

    add-int/lit8 v8, v6, 0x1

    invoke-virtual {v7, v8}, Ljava/util/concurrent/atomic/AtomicInteger;->set(I)V

    .line 38
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->readp:Ljava/util/concurrent/atomic/AtomicInteger;

    invoke-virtual {v7, v5}, Ljava/util/concurrent/atomic/AtomicInteger;->set(I)V

    .line 40
    iget-object v3, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    .line 41
    .local v3, "oldData":[I
    array-length v7, v3

    mul-int/lit8 v1, v7, 0x2

    .line 42
    .local v1, "newLen":I
    new-array v0, v1, [I

    .line 43
    .local v0, "newData":[I
    array-length v7, v3

    invoke-static {v3, v9, v0, v9, v7}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 44
    iput-object v0, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    .line 46
    iget-object v4, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->metadata:[J

    .line 47
    .local v4, "oldMetadata":[J
    new-array v2, v1, [J

    .line 48
    .local v2, "newMetadata":[J
    array-length v7, v4

    invoke-static {v4, v9, v2, v9, v7}, Ljava/lang/System;->arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V

    .line 49
    iput-object v2, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->metadata:[J

    .line 52
    .end local v0    # "newData":[I
    .end local v1    # "newLen":I
    .end local v2    # "newMetadata":[J
    .end local v3    # "oldData":[I
    .end local v4    # "oldMetadata":[J
    :cond_1
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v7}, Ljava/util/concurrent/locks/ReadWriteLock;->writeLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/concurrent/locks/Lock;->unlock()V

    .line 53
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v7}, Ljava/util/concurrent/locks/ReadWriteLock;->readLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/concurrent/locks/Lock;->lock()V

    .line 55
    :cond_2
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v7, v7

    rem-int/2addr v6, v7

    .line 56
    sget-boolean v7, Lcom/apposcopy/ella/runtime/SequenceRecorder;->$assertionsDisabled:Z

    if-nez v7, :cond_3

    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    array-length v7, v7

    rem-int v7, v5, v7

    if-ne v6, v7, :cond_3

    new-instance v7, Ljava/lang/AssertionError;

    invoke-direct {v7}, Ljava/lang/AssertionError;-><init>()V

    throw v7

    .line 57
    :cond_3
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->data:[I

    aput p1, v7, v6

    .line 58
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->metadata:[J

    aput-wide p2, v7, v6

    .line 59
    iget-object v7, p0, Lcom/apposcopy/ella/runtime/SequenceRecorder;->rwl:Ljava/util/concurrent/locks/ReadWriteLock;

    invoke-interface {v7}, Ljava/util/concurrent/locks/ReadWriteLock;->readLock()Ljava/util/concurrent/locks/Lock;

    move-result-object v7

    invoke-interface {v7}, Ljava/util/concurrent/locks/Lock;->unlock()V

    .line 60
    return-void
.end method
