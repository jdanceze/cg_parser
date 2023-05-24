.class Lcom/autostart/AutoStartActivity$OnResumeTask;
.super Landroid/os/AsyncTask;
.source "AutoStartActivity.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/autostart/AutoStartActivity;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x2
    name = "OnResumeTask"
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Landroid/os/AsyncTask",
        "<",
        "Ljava/lang/Void;",
        "Ljava/lang/Void;",
        "Ljava/lang/Void;",
        ">;"
    }
.end annotation


# instance fields
.field private myCancel:Z

.field final synthetic this$0:Lcom/autostart/AutoStartActivity;


# direct methods
.method private constructor <init>(Lcom/autostart/AutoStartActivity;)V
    .locals 4

    const v3, 0x15

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 447
    iput-object v2, v1, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-direct {v1}, Landroid/os/AsyncTask;-><init>()V

    .line 448
    const/4 v0, 0x0

    iput-boolean v0, v1, Lcom/autostart/AutoStartActivity$OnResumeTask;->myCancel:Z

    return-void
.end method

.method synthetic constructor <init>(Lcom/autostart/AutoStartActivity;Lcom/autostart/AutoStartActivity$OnResumeTask;)V
    .locals 4

    const v3, 0x16

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p2

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 447
    invoke-direct {v0, v1}, Lcom/autostart/AutoStartActivity$OnResumeTask;-><init>(Lcom/autostart/AutoStartActivity;)V

    return-void
.end method

.method static synthetic access$3(Lcom/autostart/AutoStartActivity$OnResumeTask;)Z
    .locals 3

    const v2, 0x17

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 448
    iget-boolean v0, v1, Lcom/autostart/AutoStartActivity$OnResumeTask;->myCancel:Z

    return v0
.end method


# virtual methods
.method protected bridge varargs synthetic doInBackground([Ljava/lang/Object;)Ljava/lang/Object;
    .locals 4

    const v3, 0x18

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 1
    check-cast v2, [Ljava/lang/Void;

    invoke-virtual {v1, v2}, Lcom/autostart/AutoStartActivity$OnResumeTask;->doInBackground([Ljava/lang/Void;)Ljava/lang/Void;

    move-result-object v0

    return-object v0
.end method

.method protected varargs doInBackground([Ljava/lang/Void;)Ljava/lang/Void;
    .locals 8
    .param p1, "arg0"    # [Ljava/lang/Void;

    const v7, 0x19

    invoke-static {v7}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v6, p1

    move-object v5, p0

    .prologue
    .line 451
    iget-object v2, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity;->access$3(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v2

    invoke-interface {v2}, Ljava/util/List;->iterator()Ljava/util/Iterator;

    move-result-object v2

    :goto_0
    invoke-interface {v2}, Ljava/util/Iterator;->hasNext()Z

    move-result v3

    if-nez v3, :cond_1

    .line 475
    :cond_0
    const/4 v2, 0x0

    return-object v2

    .line 451
    :cond_1
    invoke-interface {v2}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/pm/ApplicationInfo;

    .line 452
    .local v0, "packageInfo":Landroid/content/pm/ApplicationInfo;
    iget-boolean v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->myCancel:Z

    if-nez v3, :cond_0

    .line 455
    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$4(Lcom/autostart/AutoStartActivity;)Landroid/content/pm/PackageManager;

    move-result-object v3

    iget-object v4, v0, Landroid/content/pm/ApplicationInfo;->packageName:Ljava/lang/String;

    invoke-virtual {v3, v4}, Landroid/content/pm/PackageManager;->getLaunchIntentForPackage(Ljava/lang/String;)Landroid/content/Intent;

    move-result-object v3

    if-eqz v3, :cond_3

    .line 456
    new-instance v1, Lcom/autostart/AutoStartActivity$PInfo;

    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-direct {v1, v3}, Lcom/autostart/AutoStartActivity$PInfo;-><init>(Lcom/autostart/AutoStartActivity;)V

    .line 457
    .local v1, "pi":Lcom/autostart/AutoStartActivity$PInfo;
    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-virtual {v3}, Lcom/autostart/AutoStartActivity;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v3

    invoke-virtual {v0, v3}, Landroid/content/pm/ApplicationInfo;->loadLabel(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;

    move-result-object v3

    invoke-interface {v3}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v3

    invoke-static {v1, v3}, Lcom/autostart/AutoStartActivity$PInfo;->access$4(Lcom/autostart/AutoStartActivity$PInfo;Ljava/lang/String;)V

    .line 458
    iget-object v3, v0, Landroid/content/pm/ApplicationInfo;->packageName:Ljava/lang/String;

    invoke-static {v1, v3}, Lcom/autostart/AutoStartActivity$PInfo;->access$5(Lcom/autostart/AutoStartActivity$PInfo;Ljava/lang/String;)V

    .line 459
    invoke-static {v1, v0}, Lcom/autostart/AutoStartActivity$PInfo;->access$6(Lcom/autostart/AutoStartActivity$PInfo;Landroid/content/pm/ApplicationInfo;)V

    .line 461
    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3, v0}, Lcom/autostart/AutoStartActivity;->access$5(Lcom/autostart/AutoStartActivity;Landroid/content/pm/ApplicationInfo;)Z

    move-result v3

    if-nez v3, :cond_2

    .line 466
    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$6(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    .line 468
    :cond_2
    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$7(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v3

    invoke-interface {v3, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    .line 470
    .end local v1    # "pi":Lcom/autostart/AutoStartActivity$PInfo;
    :cond_3
    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$6(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v3

    invoke-static {v3}, Ljava/util/Collections;->sort(Ljava/util/List;)V

    .line 471
    iget-object v3, v5, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$7(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v3

    invoke-static {v3}, Ljava/util/Collections;->sort(Ljava/util/List;)V

    goto :goto_0
.end method

.method protected myCancel()V
    .locals 3

    const v2, 0x1a

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 490
    const/4 v0, 0x1

    iput-boolean v0, v1, Lcom/autostart/AutoStartActivity$OnResumeTask;->myCancel:Z

    .line 491
    return-void
.end method

.method protected bridge synthetic onPostExecute(Ljava/lang/Object;)V
    .locals 3

    const v2, 0x1b

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 1
    check-cast v1, Ljava/lang/Void;

    invoke-virtual {v0, v1}, Lcom/autostart/AutoStartActivity$OnResumeTask;->onPostExecute(Ljava/lang/Void;)V

    return-void
.end method

.method protected onPostExecute(Ljava/lang/Void;)V
    .locals 9
    .param p1, "result"    # Ljava/lang/Void;

    const v8, 0x1c

    invoke-static {v8}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v7, p1

    move-object v6, p0

    .prologue
    const/4 v5, 0x1

    .line 480
    iget-boolean v0, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->myCancel:Z

    if-nez v0, :cond_0

    .line 481
    iget-object v0, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    new-instance v1, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    iget-object v2, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    iget-object v3, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    iget-object v4, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v4}, Lcom/autostart/AutoStartActivity;->access$6(Lcom/autostart/AutoStartActivity;)Ljava/util/List;

    move-result-object v4

    invoke-direct {v1, v2, v3, v4}, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;-><init>(Lcom/autostart/AutoStartActivity;Landroid/content/Context;Ljava/util/List;)V

    invoke-static {v0, v1}, Lcom/autostart/AutoStartActivity;->access$8(Lcom/autostart/AutoStartActivity;Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;)V

    .line 482
    iget-object v0, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v0}, Lcom/autostart/AutoStartActivity;->access$9(Lcom/autostart/AutoStartActivity;)Landroid/widget/ListView;

    move-result-object v0

    iget-object v1, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v1}, Lcom/autostart/AutoStartActivity;->access$10(Lcom/autostart/AutoStartActivity;)Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    move-result-object v1

    invoke-virtual {v0, v1}, Landroid/widget/ListView;->setAdapter(Landroid/widget/ListAdapter;)V

    .line 484
    :cond_0
    iput-boolean v5, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->myCancel:Z

    .line 485
    iget-object v0, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v0}, Lcom/autostart/AutoStartActivity;->access$11(Lcom/autostart/AutoStartActivity;)Landroid/widget/Button;

    move-result-object v0

    invoke-virtual {v0, v5}, Landroid/widget/Button;->setEnabled(Z)V

    .line 486
    iget-object v0, v6, Lcom/autostart/AutoStartActivity$OnResumeTask;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v0}, Lcom/autostart/AutoStartActivity;->access$12(Lcom/autostart/AutoStartActivity;)Landroid/widget/ProgressBar;

    move-result-object v0

    const/4 v1, 0x4

    invoke-virtual {v0, v1}, Landroid/widget/ProgressBar;->setVisibility(I)V

    .line 487
    return-void
.end method
