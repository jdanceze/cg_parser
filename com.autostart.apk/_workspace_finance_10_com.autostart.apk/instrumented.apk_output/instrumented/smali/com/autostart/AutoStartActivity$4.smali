.class Lcom/autostart/AutoStartActivity$4;
.super Ljava/lang/Object;
.source "AutoStartActivity.java"

# interfaces
.implements Landroid/widget/AdapterView$OnItemClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/autostart/AutoStartActivity;->onCreate(Landroid/os/Bundle;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Landroid/widget/AdapterView$OnItemClickListener;"
    }
.end annotation


# instance fields
.field final synthetic this$0:Lcom/autostart/AutoStartActivity;


# direct methods
.method constructor <init>(Lcom/autostart/AutoStartActivity;)V
    .locals 3

    const v2, 0x6

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 1
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$4;->this$0:Lcom/autostart/AutoStartActivity;

    .line 134
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onItemClick(Landroid/widget/AdapterView;Landroid/view/View;IJ)V
    .locals 10
    .param p2, "arg1"    # Landroid/view/View;
    .param p3, "pos"    # I
    .param p4, "arg3"    # J
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/widget/AdapterView",
            "<*>;",
            "Landroid/view/View;",
            "IJ)V"
        }
    .end annotation

    const v9, 0x7

    invoke-static {v9}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-wide v7, p4

    move v6, p3

    move-object v5, p2

    move-object v4, p1

    move-object v3, p0

    .prologue
    .line 138
    .local v4, "arg0":Landroid/widget/AdapterView;, "Landroid/widget/AdapterView<*>;"
    iget-object v1, v3, Lcom/autostart/AutoStartActivity$4;->this$0:Lcom/autostart/AutoStartActivity;

    const/4 v2, 0x1

    invoke-static {v1, v2}, Lcom/autostart/AutoStartActivity;->access$13(Lcom/autostart/AutoStartActivity;Z)V

    .line 140
    iget-object v1, v3, Lcom/autostart/AutoStartActivity$4;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v1}, Lcom/autostart/AutoStartActivity;->access$10(Lcom/autostart/AutoStartActivity;)Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    move-result-object v1

    invoke-virtual {v1, v6}, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->getItem(I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/autostart/AutoStartActivity$PInfo;

    .line 142
    .local v0, "pi":Lcom/autostart/AutoStartActivity$PInfo;
    iget-object v1, v3, Lcom/autostart/AutoStartActivity$4;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v1}, Lcom/autostart/AutoStartActivity;->access$14(Lcom/autostart/AutoStartActivity;)Ljava/util/ArrayList;

    move-result-object v1

    invoke-virtual {v1, v0}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    .line 144
    iget-object v1, v3, Lcom/autostart/AutoStartActivity$4;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-virtual {v1, v0}, Lcom/autostart/AutoStartActivity;->addToAppList(Lcom/autostart/AutoStartActivity$PInfo;)V

    .line 145
    return-void
.end method
