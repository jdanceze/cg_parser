.class public Lcom/autostart/TimeOutReceiver;
.super Landroid/content/BroadcastReceiver;
.source "TimeOutReceiver.java"


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x4d

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 14
    invoke-direct {v0}, Landroid/content/BroadcastReceiver;-><init>()V

    return-void
.end method


# virtual methods
.method public onReceive(Landroid/content/Context;Landroid/content/Intent;)V
    .locals 36
    .param p1, "context"    # Landroid/content/Context;
    .param p2, "intent"    # Landroid/content/Intent;

    const v35, 0x4e

    invoke-static/range {v35 .. v35}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object/from16 v34, p2

    move-object/from16 v33, p1

    move-object/from16 v32, p0

    .prologue
    .line 17
    const-string v27, "autostart"

    const/16 v28, 0x0

    move-object/from16 v0, v33

    move-object/from16 v1, v27

    move/from16 v2, v28

    invoke-virtual {v0, v1, v2}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v23

    .line 18
    .local v23, "prefs":Landroid/content/SharedPreferences;
    const-string v27, "package"

    const-string v28, ""

    move-object/from16 v0, v23

    move-object/from16 v1, v27

    move-object/from16 v2, v28

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v19

    .line 19
    .local v19, "packageNames":Ljava/lang/String;
    const-string v27, "class"

    const-string v28, ""

    move-object/from16 v0, v23

    move-object/from16 v1, v27

    move-object/from16 v2, v28

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v5

    .line 20
    .local v5, "classNames":Ljava/lang/String;
    const-string v27, "inbetweendelay"

    const/16 v28, 0x3

    move-object/from16 v0, v23

    move-object/from16 v1, v27

    move/from16 v2, v28

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v26

    .line 21
    .local v26, "timeout":I
    const-string v27, "enabled"

    const/16 v28, 0x0

    move-object/from16 v0, v23

    move-object/from16 v1, v27

    move/from16 v2, v28

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v10

    .line 22
    .local v10, "enabled":Z
    const-string v27, "noti"

    const/16 v28, 0x1

    move-object/from16 v0, v23

    move-object/from16 v1, v27

    move/from16 v2, v28

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v17

    .line 23
    .local v17, "notificaton":Z
    const-string v27, "iteration"

    const/16 v28, 0x0

    move-object/from16 v0, v23

    move-object/from16 v1, v27

    move/from16 v2, v28

    invoke-interface {v0, v1, v2}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v22

    .line 25
    .local v22, "pos":I
    if-eqz v10, :cond_1

    const-string v27, ""

    move-object/from16 v0, v19

    move-object/from16 v1, v27

    invoke-virtual {v0, v1}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v27

    if-nez v27, :cond_1

    const-string v27, ""

    move-object/from16 v0, v27

    invoke-virtual {v5, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v27

    if-nez v27, :cond_1

    .line 27
    :try_start_0
    const-string v27, ";;"

    move-object/from16 v0, v19

    move-object/from16 v1, v27

    invoke-virtual {v0, v1}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v25

    .line 28
    .local v25, "splitPackages":[Ljava/lang/String;
    const-string v27, ";;"

    move-object/from16 v0, v27

    invoke-virtual {v5, v0}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v24

    .line 29
    .local v24, "splitClassNames":[Ljava/lang/String;
    move-object/from16 v0, v24

    array-length v0, v0

    move/from16 v27, v0

    move/from16 v0, v27

    move/from16 v1, v22

    if-le v0, v1, :cond_1

    move-object/from16 v0, v25

    array-length v0, v0

    move/from16 v27, v0

    move/from16 v0, v27

    move/from16 v1, v22

    if-le v0, v1, :cond_1

    .line 30
    aget-object v18, v25, v22

    .line 31
    .local v18, "packageName":Ljava/lang/String;
    aget-object v4, v24, v22

    .line 33
    .local v4, "className":Ljava/lang/String;
    if-eqz v17, :cond_0

    .line 34
    const-string v27, "notification"

    move-object/from16 v0, v33

    move-object/from16 v1, v27

    invoke-virtual {v0, v1}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/app/NotificationManager;

    .line 35
    .local v14, "mNotificationManager":Landroid/app/NotificationManager;
    const-string v8, "Auto Start"

    .line 36
    .local v8, "contentTitle":Ljava/lang/CharSequence;
    new-instance v27, Ljava/lang/StringBuilder;

    const-string v28, "Started application: "

    invoke-direct/range {v27 .. v28}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    move-object/from16 v0, v27

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v27

    invoke-virtual/range {v27 .. v27}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v7

    .line 37
    .local v7, "contentText":Ljava/lang/CharSequence;
    new-instance v16, Landroid/content/Intent;

    const-class v27, Lcom/autostart/AutoStartActivity;

    move-object/from16 v0, v16

    move-object/from16 v1, v33

    move-object/from16 v2, v27

    invoke-direct {v0, v1, v2}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 38
    .local v16, "notificationIntent":Landroid/content/Intent;
    const/high16 v27, 0x24000000

    move-object/from16 v0, v16

    move/from16 v1, v27

    invoke-virtual {v0, v1}, Landroid/content/Intent;->setFlags(I)Landroid/content/Intent;

    .line 40
    const/16 v27, 0x0

    .line 41
    const/16 v28, 0x0

    .line 40
    move-object/from16 v0, v33

    move/from16 v1, v27

    move-object/from16 v2, v16

    move/from16 v3, v28

    invoke-static {v0, v1, v2, v3}, Landroid/app/PendingIntent;->getActivity(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;

    move-result-object v6

    .line 43
    .local v6, "contentIntent":Landroid/app/PendingIntent;
    new-instance v13, Landroid/app/Notification;

    const v27, 0x7f020003

    .line 44
    invoke-static {}, Ljava/lang/System;->currentTimeMillis()J

    move-result-wide v28

    .line 43
    move/from16 v0, v27

    move-wide/from16 v1, v28

    invoke-direct {v13, v0, v7, v1, v2}, Landroid/app/Notification;-><init>(ILjava/lang/CharSequence;J)V

    .line 45
    .local v13, "mNotification":Landroid/app/Notification;
    move-object/from16 v0, v33

    invoke-virtual {v13, v0, v8, v7, v6}, Landroid/app/Notification;->setLatestEventInfo(Landroid/content/Context;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Landroid/app/PendingIntent;)V

    .line 47
    const/16 v27, 0xd85

    move/from16 v0, v27

    invoke-virtual {v14, v0, v13}, Landroid/app/NotificationManager;->notify(ILandroid/app/Notification;)V

    .line 50
    .end local v6    # "contentIntent":Landroid/app/PendingIntent;
    .end local v7    # "contentText":Ljava/lang/CharSequence;
    .end local v8    # "contentTitle":Ljava/lang/CharSequence;
    .end local v13    # "mNotification":Landroid/app/Notification;
    .end local v14    # "mNotificationManager":Landroid/app/NotificationManager;
    .end local v16    # "notificationIntent":Landroid/content/Intent;
    :cond_0
    invoke-virtual/range {v33 .. v33}, Landroid/content/Context;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v21

    .line 51
    .local v21, "pm":Landroid/content/pm/PackageManager;
    move-object/from16 v0, v21

    move-object/from16 v1, v18

    invoke-virtual {v0, v1}, Landroid/content/pm/PackageManager;->getLaunchIntentForPackage(Ljava/lang/String;)Landroid/content/Intent;

    move-result-object v11

    .line 52
    .local v11, "i":Landroid/content/Intent;
    const/high16 v27, 0x10000000

    move/from16 v0, v27

    invoke-virtual {v11, v0}, Landroid/content/Intent;->setFlags(I)Landroid/content/Intent;

    .line 53
    move-object/from16 v0, v33

    invoke-virtual {v0, v11}, Landroid/content/Context;->startActivity(Landroid/content/Intent;)V

    .line 55
    add-int/lit8 v22, v22, 0x1

    .line 56
    invoke-interface/range {v23 .. v23}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v9

    .line 57
    .local v9, "editor":Landroid/content/SharedPreferences$Editor;
    move-object/from16 v0, v25

    array-length v0, v0

    move/from16 v27, v0

    move/from16 v0, v22

    move/from16 v1, v27

    if-ne v0, v1, :cond_2

    .line 58
    const-string v27, "iteration"

    const/16 v28, 0x0

    move-object/from16 v0, v27

    move/from16 v1, v28

    invoke-interface {v9, v0, v1}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 67
    :goto_0
    invoke-interface {v9}, Landroid/content/SharedPreferences$Editor;->commit()Z

    .line 72
    .end local v4    # "className":Ljava/lang/String;
    .end local v9    # "editor":Landroid/content/SharedPreferences$Editor;
    .end local v11    # "i":Landroid/content/Intent;
    .end local v18    # "packageName":Ljava/lang/String;
    .end local v21    # "pm":Landroid/content/pm/PackageManager;
    .end local v24    # "splitClassNames":[Ljava/lang/String;
    .end local v25    # "splitPackages":[Ljava/lang/String;
    :cond_1
    :goto_1
    return-void

    .line 60
    .restart local v4    # "className":Ljava/lang/String;
    .restart local v9    # "editor":Landroid/content/SharedPreferences$Editor;
    .restart local v11    # "i":Landroid/content/Intent;
    .restart local v18    # "packageName":Ljava/lang/String;
    .restart local v21    # "pm":Landroid/content/pm/PackageManager;
    .restart local v24    # "splitClassNames":[Ljava/lang/String;
    .restart local v25    # "splitPackages":[Ljava/lang/String;
    :cond_2
    const-string v27, "iteration"

    move-object/from16 v0, v27

    move/from16 v1, v22

    invoke-interface {v9, v0, v1}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 61
    const-string v27, "alarm"

    move-object/from16 v0, v33

    move-object/from16 v1, v27

    invoke-virtual {v0, v1}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v15

    check-cast v15, Landroid/app/AlarmManager;

    .line 62
    .local v15, "mgr":Landroid/app/AlarmManager;
    new-instance v12, Landroid/content/Intent;

    const-class v27, Lcom/autostart/TimeOutHomeReceiver;

    move-object/from16 v0, v33

    move-object/from16 v1, v27

    invoke-direct {v12, v0, v1}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 63
    .local v12, "ii":Landroid/content/Intent;
    const/16 v27, 0x0

    const/16 v28, 0x0

    move-object/from16 v0, v33

    move/from16 v1, v27

    move/from16 v2, v28

    invoke-static {v0, v1, v12, v2}, Landroid/app/PendingIntent;->getBroadcast(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;

    move-result-object v20

    .line 65
    .local v20, "pi":Landroid/app/PendingIntent;
    const/16 v27, 0x2

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v28

    move/from16 v0, v26

    mul-int/lit16 v0, v0, 0x3e8

    move/from16 v30, v0

    move/from16 v0, v30

    int-to-long v0, v0

    move-wide/from16 v30, v0

    add-long v28, v28, v30

    move/from16 v0, v27

    move-wide/from16 v1, v28

    move-object/from16 v3, v20

    invoke-virtual {v15, v0, v1, v2, v3}, Landroid/app/AlarmManager;->set(IJLandroid/app/PendingIntent;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 70
    .end local v4    # "className":Ljava/lang/String;
    .end local v9    # "editor":Landroid/content/SharedPreferences$Editor;
    .end local v11    # "i":Landroid/content/Intent;
    .end local v12    # "ii":Landroid/content/Intent;
    .end local v15    # "mgr":Landroid/app/AlarmManager;
    .end local v18    # "packageName":Ljava/lang/String;
    .end local v20    # "pi":Landroid/app/PendingIntent;
    .end local v21    # "pm":Landroid/content/pm/PackageManager;
    .end local v24    # "splitClassNames":[Ljava/lang/String;
    .end local v25    # "splitPackages":[Ljava/lang/String;
    :catch_0
    move-exception v27

    goto :goto_1
.end method
