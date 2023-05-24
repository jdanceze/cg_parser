.class Lcom/autostart/AutoStartActivity$5;
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

.field private final synthetic val$removeView:Landroid/view/View;


# direct methods
.method constructor <init>(Lcom/autostart/AutoStartActivity;Landroid/view/View;)V
    .locals 4

    const v3, 0x8

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p2

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 1
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$5;->this$0:Lcom/autostart/AutoStartActivity;

    iput-object v2, v0, Lcom/autostart/AutoStartActivity$5;->val$removeView:Landroid/view/View;

    .line 504
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/content/DialogInterface;I)V
    .locals 8
    .param p1, "dialog"    # Landroid/content/DialogInterface;
    .param p2, "id"    # I

    const v7, 0x9

    invoke-static {v7}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move v6, p2

    move-object v5, p1

    move-object v4, p0

    .prologue
    .line 508
    :try_start_0
    iget-object v2, v4, Lcom/autostart/AutoStartActivity$5;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity;->access$15(Lcom/autostart/AutoStartActivity;)Landroid/widget/LinearLayout;

    move-result-object v3

    iget-object v2, v4, Lcom/autostart/AutoStartActivity$5;->val$removeView:Landroid/view/View;

    invoke-virtual {v2}, Landroid/view/View;->getParent()Landroid/view/ViewParent;

    move-result-object v2

    check-cast v2, Landroid/view/ViewGroup;

    invoke-virtual {v3, v2}, Landroid/widget/LinearLayout;->removeView(Landroid/view/View;)V

    .line 510
    iget-object v1, v4, Lcom/autostart/AutoStartActivity$5;->val$removeView:Landroid/view/View;

    check-cast v1, Landroid/widget/TextView;

    .line 511
    .local v1, "tv":Landroid/widget/TextView;
    const/4 v0, 0x0

    .line 512
    .local v0, "i":I
    :goto_0
    iget-object v2, v4, Lcom/autostart/AutoStartActivity$5;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity;->access$14(Lcom/autostart/AutoStartActivity;)Ljava/util/ArrayList;

    move-result-object v2

    invoke-virtual {v2}, Ljava/util/ArrayList;->size()I

    move-result v2

    if-lt v0, v2, :cond_2

    .line 517
    :cond_0
    iget-object v2, v4, Lcom/autostart/AutoStartActivity$5;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity;->access$14(Lcom/autostart/AutoStartActivity;)Ljava/util/ArrayList;

    move-result-object v2

    invoke-virtual {v2}, Ljava/util/ArrayList;->size()I

    move-result v2

    if-ge v0, v2, :cond_1

    .line 518
    iget-object v2, v4, Lcom/autostart/AutoStartActivity$5;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity;->access$14(Lcom/autostart/AutoStartActivity;)Ljava/util/ArrayList;

    move-result-object v2

    invoke-virtual {v2, v0}, Ljava/util/ArrayList;->remove(I)Ljava/lang/Object;

    .line 520
    .end local v0    # "i":I
    .end local v1    # "tv":Landroid/widget/TextView;
    :cond_1
    :goto_1
    return-void

    .line 513
    .restart local v0    # "i":I
    .restart local v1    # "tv":Landroid/widget/TextView;
    :cond_2
    iget-object v2, v4, Lcom/autostart/AutoStartActivity$5;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity;->access$14(Lcom/autostart/AutoStartActivity;)Ljava/util/ArrayList;

    move-result-object v2

    invoke-virtual {v2, v0}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v3

    iget-object v2, v4, Lcom/autostart/AutoStartActivity$5;->val$removeView:Landroid/view/View;

    check-cast v2, Landroid/widget/TextView;

    invoke-virtual {v2}, Landroid/widget/TextView;->getTag()Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v3, v2}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    move-result v2

    if-nez v2, :cond_0

    .line 515
    add-int/lit8 v0, v0, 0x1

    goto :goto_0

    .line 519
    .end local v0    # "i":I
    .end local v1    # "tv":Landroid/widget/TextView;
    :catch_0
    move-exception v2

    goto :goto_1
.end method
