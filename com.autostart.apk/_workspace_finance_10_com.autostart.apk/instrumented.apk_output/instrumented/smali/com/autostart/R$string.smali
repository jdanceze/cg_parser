.class public final Lcom/autostart/R$string;
.super Ljava/lang/Object;
.source "R.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/autostart/R;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x19
    name = "string"
.end annotation


# static fields
.field public static final app_name:I = 0x7f040001

.field public static final hello:I = 0x7f040000


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x49

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 46
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method
