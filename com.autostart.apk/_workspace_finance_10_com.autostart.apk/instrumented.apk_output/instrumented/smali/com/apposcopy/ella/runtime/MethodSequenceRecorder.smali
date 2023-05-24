.class public Lcom/apposcopy/ella/runtime/MethodSequenceRecorder;
.super Lcom/apposcopy/ella/runtime/SequenceRecorder;
.source "MethodSequenceRecorder.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 3
    invoke-direct {p0}, Lcom/apposcopy/ella/runtime/SequenceRecorder;-><init>()V

    return-void
.end method


# virtual methods
.method public m(I)V
    .locals 2
    .param p1, "methodId"    # I

    .prologue
    .line 7
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v0

    .line 8
    .local v0, "time":J
    invoke-virtual {p0, p1, v0, v1}, Lcom/apposcopy/ella/runtime/MethodSequenceRecorder;->record(IJ)V

    .line 9
    return-void
.end method
