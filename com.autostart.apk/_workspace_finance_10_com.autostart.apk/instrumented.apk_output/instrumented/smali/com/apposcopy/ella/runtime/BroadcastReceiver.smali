.class public Lcom/apposcopy/ella/runtime/BroadcastReceiver;
.super Landroid/content/BroadcastReceiver;
.source "BroadcastReceiver.java"


# direct methods
.method public constructor <init>()V
    .locals 0

    .prologue
    .line 7
    invoke-direct {p0}, Landroid/content/BroadcastReceiver;-><init>()V

    return-void
.end method


# virtual methods
.method public onReceive(Landroid/content/Context;Landroid/content/Intent;)V
    .locals 2
    .param p1, "context"    # Landroid/content/Context;
    .param p2, "intent"    # Landroid/content/Intent;

    .prologue
    .line 11
    const-string v0, "ella"

    const-string v1, "Broadcast received."

    invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 12
    const-string v0, "ella"

    const-string v1, "To stop uploading coverage data."

    invoke-static {v0, v1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 13
    invoke-static {}, Lcom/apposcopy/ella/runtime/Ella;->stopRecording()V

    .line 14
    return-void
.end method
