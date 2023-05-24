.class Lcom/autostart/AutoStartActivity$3;
.super Ljava/lang/Object;
.source "AutoStartActivity.java"

# interfaces
.implements Landroid/widget/CompoundButton$OnCheckedChangeListener;


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

    const v2, 0x4

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 1
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    .line 122
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onCheckedChanged(Landroid/widget/CompoundButton;Z)V
    .locals 9
    .param p1, "buttonView"    # Landroid/widget/CompoundButton;
    .param p2, "isChecked"    # Z

    const v8, 0x5

    invoke-static {v8}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move v7, p2

    move-object v6, p1

    move-object v5, p0

    .prologue
    .line 125
    if-eqz v7, :cond_0

    .line 126
    iget-object v0, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    new-instance v1, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    iget-object v2, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    iget-object v3, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-virtual {v3}, Lcom/autostart/AutoStartActivity;->getApplicationContext()Landroid/content/Context;

    move-result-object v3

    iget-object v4, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v4}, Lcom/autostart/AutoStartActivity;->access$7(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v4

    invoke-direct {v1, v2, v3, v4}, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;-><init>(Lcom/autostart/AutoStartActivity;Landroid/content/Context;Ljava/util/List;)V

    invoke-static {v0, v1}, Lcom/autostart/AutoStartActivity;->access$8(Lcom/autostart/AutoStartActivity;Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;)V

    .line 130
    :goto_0
    iget-object v0, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v0}, Lcom/autostart/AutoStartActivity;->access$9(Lcom/autostart/AutoStartActivity;)Landroid/widget/ListView;

    move-result-object v0

    iget-object v1, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v1}, Lcom/autostart/AutoStartActivity;->access$10(Lcom/autostart/AutoStartActivity;)Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    move-result-object v1

    invoke-virtual {v0, v1}, Landroid/widget/ListView;->setAdapter(Landroid/widget/ListAdapter;)V

    .line 131
    return-void

    .line 128
    :cond_0
    iget-object v0, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    new-instance v1, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    iget-object v2, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    iget-object v3, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-virtual {v3}, Lcom/autostart/AutoStartActivity;->getApplicationContext()Landroid/content/Context;

    move-result-object v3

    iget-object v4, v5, Lcom/autostart/AutoStartActivity$3;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v4}, Lcom/autostart/AutoStartActivity;->access$6(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v4

    invoke-direct {v1, v2, v3, v4}, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;-><init>(Lcom/autostart/AutoStartActivity;Landroid/content/Context;Ljava/util/List;)V

    invoke-static {v0, v1}, Lcom/autostart/AutoStartActivity;->access$8(Lcom/autostart/AutoStartActivity;Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;)V

    goto :goto_0
.end method
