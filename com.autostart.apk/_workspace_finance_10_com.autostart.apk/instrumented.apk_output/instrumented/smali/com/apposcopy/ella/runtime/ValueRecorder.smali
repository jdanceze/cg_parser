.class public Lcom/apposcopy/ella/runtime/ValueRecorder;
.super Lcom/apposcopy/ella/runtime/SequenceRecorder;
.source "ValueRecorder.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 3
    invoke-direct {p0}, Lcom/apposcopy/ella/runtime/SequenceRecorder;-><init>()V

    return-void
.end method


# virtual methods
.method public v(Ljava/lang/Object;I)V
    .locals 4
    .param p1, "obj"    # Ljava/lang/Object;
    .param p2, "metadata"    # I

    .prologue
    .line 7
    invoke-static {p1}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v0

    .line 8
    .local v0, "hashCode":I
    int-to-long v2, p2

    invoke-virtual {p0, v0, v2, v3}, Lcom/apposcopy/ella/runtime/ValueRecorder;->record(IJ)V

    .line 9
    return-void
.end method
