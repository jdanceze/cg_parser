.class public Lcom/autostart/TimeOutHomeReceiver;
.super Landroid/content/BroadcastReceiver;
.source "TimeOutHomeReceiver.java"


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x4b

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 11
    invoke-direct {v0}, Landroid/content/BroadcastReceiver;-><init>()V

    return-void
.end method


# virtual methods
.method public onReceive(Landroid/content/Context;Landroid/content/Intent;)V
    .locals 17
    .param p1, "context"    # Landroid/content/Context;
    .param p2, "intent"    # Landroid/content/Intent;

    const v16, 0x4c

    invoke-static/range {v16 .. v16}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object/from16 v15, p2

    move-object/from16 v14, p1

    move-object/from16 v13, p0

    .prologue
    .line 15
    :try_start_0
    const-string v8, "autostart"

    const/4 v9, 0x0

    invoke-virtual {v14, v8, v9}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v6

    .line 16
    .local v6, "prefs":Landroid/content/SharedPreferences;
    const-string v8, "gotohome"

    const/4 v9, 0x1

    invoke-interface {v6, v8, v9}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1

    move-result v2

    .line 18
    .local v2, "gotoHome":Z
    if-eqz v2, :cond_0

    .line 20
    :try_start_1
    new-instance v7, Landroid/content/Intent;

    const-string v8, "android.intent.action.MAIN"

    invoke-direct {v7, v8}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    .line 21
    .local v7, "startMain":Landroid/content/Intent;
    const-string v8, "android.intent.category.HOME"

    invoke-virtual {v7, v8}, Landroid/content/Intent;->addCategory(Ljava/lang/String;)Landroid/content/Intent;

    .line 22
    const/high16 v8, 0x10000000

    invoke-virtual {v7, v8}, Landroid/content/Intent;->setFlags(I)Landroid/content/Intent;

    .line 23
    invoke-virtual {v14, v7}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    .line 30
    .end local v7    # "startMain":Landroid/content/Intent;
    :cond_0
    :goto_0
    :try_start_2
    const-string v8, "alarm"

    invoke-virtual {v14, v8}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/app/AlarmManager;

    .line 31
    .local v4, "mgr":Landroid/app/AlarmManager;
    new-instance v3, Landroid/content/Intent;

    const-class v8, Lcom/autostart/TimeOutReceiver;

    invoke-direct {v3, v14, v8}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 32
    .local v3, "i":Landroid/content/Intent;
    const/4 v8, 0x0

    const/4 v9, 0x0

    invoke-static {v14, v8, v3, v9}, Landroid/app/PendingIntent;->getBroadcast(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;

    move-result-object v5

    .line 34
    .local v5, "pi":Landroid/app/PendingIntent;
    const/4 v8, 0x2

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v9

    const-wide/16 v11, 0x3e8

    add-long/2addr v9, v11

    invoke-virtual {v4, v8, v9, v10, v5}, Landroid/app/AlarmManager;->set(IJLandroid/app/PendingIntent;)V

    .line 38
    .end local v2    # "gotoHome":Z
    .end local v3    # "i":Landroid/content/Intent;
    .end local v4    # "mgr":Landroid/app/AlarmManager;
    .end local v5    # "pi":Landroid/app/PendingIntent;
    .end local v6    # "prefs":Landroid/content/SharedPreferences;
    :goto_1
    return-void

    .line 25
    .restart local v2    # "gotoHome":Z
    .restart local v6    # "prefs":Landroid/content/SharedPreferences;
    :catch_0
    move-exception v1

    .line 26
    .local v1, "ee":Ljava/lang/Exception;
    invoke-virtual {v1}, Ljava/lang/Exception;->printStackTrace()V
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_1

    goto :goto_0

    .line 35
    .end local v1    # "ee":Ljava/lang/Exception;
    .end local v2    # "gotoHome":Z
    .end local v6    # "prefs":Landroid/content/SharedPreferences;
    :catch_1
    move-exception v0

    .line 36
    .local v0, "e":Ljava/lang/Exception;
    invoke-virtual {v0}, Ljava/lang/Exception;->printStackTrace()V

    goto :goto_1
.end method
