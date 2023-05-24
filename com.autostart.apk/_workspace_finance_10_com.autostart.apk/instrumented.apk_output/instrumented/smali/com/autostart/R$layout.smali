.class public final Lcom/autostart/R$layout;
.super Ljava/lang/Object;
.source "R.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/autostart/R;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x19
    name = "layout"
.end annotation


# static fields
.field public static final main:I = 0x7f030000


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x48

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 43
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method
