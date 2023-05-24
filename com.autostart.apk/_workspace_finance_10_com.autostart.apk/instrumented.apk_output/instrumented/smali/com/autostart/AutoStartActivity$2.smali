.class Lcom/autostart/AutoStartActivity$2;
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

    const v2, 0x2

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 1
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$2;->this$0:Lcom/autostart/AutoStartActivity;

    .line 113
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 5
    .param p1, "v"    # Landroid/view/View;

    const v4, 0x3

    invoke-static {v4}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v3, p1

    move-object v2, p0

    .prologue
    .line 116
    new-instance v0, Landroid/content/Intent;

    const-string v1, "android.intent.action.VIEW"

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    .line 117
    .local v0, "intent":Landroid/content/Intent;
    const-string v1, "market://details?id=com.discolight"

    invoke-static {v1}, Landroid/net/Uri;->parse(Ljava/lang/String;)Landroid/net/Uri;

    move-result-object v1

    invoke-virtual {v0, v1}, Landroid/content/Intent;->setData(Landroid/net/Uri;)Landroid/content/Intent;

    .line 118
    iget-object v1, v2, Lcom/autostart/AutoStartActivity$2;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-virtual {v1, v0}, Lcom/autostart/AutoStartActivity;->startActivity(Landroid/content/Intent;)V

    .line 119
    return-void
.end method
