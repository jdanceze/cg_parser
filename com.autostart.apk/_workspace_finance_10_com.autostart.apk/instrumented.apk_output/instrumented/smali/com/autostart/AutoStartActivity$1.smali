.class Lcom/autostart/AutoStartActivity$1;
.super Ljava/lang/Object;
.source "AutoStartActivity.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/autostart/AutoStartActivity;->onCreate(Landroid/os/Bundle;)V
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

    const v2, 0x0

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 1
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$1;->this$0:Lcom/autostart/AutoStartActivity;

    .line 106
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 5
    .param p1, "v"    # Landroid/view/View;

    const v4, 0x1

    invoke-static {v4}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v3, p1

    move-object v2, p0

    .prologue
    .line 109
    iget-object v0, v2, Lcom/autostart/AutoStartActivity$1;->this$0:Lcom/autostart/AutoStartActivity;

    const/4 v1, 0x0

    invoke-static {v0, v1}, Lcom/autostart/AutoStartActivity;->access$13(Lcom/autostart/AutoStartActivity;Z)V

    .line 110
    return-void
.end method
