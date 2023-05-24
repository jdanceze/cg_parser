.class public final Lcom/autostart/R$attr;
.super Ljava/lang/Object;
.source "R.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/autostart/R;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x19
    name = "attr"
.end annotation


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x45

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 11
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method
