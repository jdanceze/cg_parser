.class Lcom/autostart/AutoStartActivity$6;
.super Ljava/lang/Object;
.source "AutoStartActivity.java"

# interfaces
.implements Landroid/content/DialogInterface$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/autostart/AutoStartActivity;->onClick(Landroid/view/View;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/autostart/AutoStartActivity;


# direct methods
.method constructor <init>(Lcom/autostart/AutoStartActivity;)V
    .locals 3

    const v2, 0xa

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 1
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$6;->this$0:Lcom/autostart/AutoStartActivity;

    .line 523
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/content/DialogInterface;I)V
    .locals 4
    .param p1, "dialog"    # Landroid/content/DialogInterface;
    .param p2, "id"    # I

    const v3, 0xb

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move v2, p2

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 526
    invoke-interface {v1}, Landroid/content/DialogInterface;->cancel()V

    .line 527
    return-void
.end method
