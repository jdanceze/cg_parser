.class public final Lcom/autostart/R;
.super Ljava/lang/Object;
.source "R.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/autostart/R$attr;,
        Lcom/autostart/R$drawable;,
        Lcom/autostart/R$id;,
        Lcom/autostart/R$layout;,
        Lcom/autostart/R$string;
    }
.end annotation


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x4a

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 10
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method
