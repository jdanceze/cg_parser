.class public Lcom/autostart/MyBroadcastreceiver;
.super Landroid/content/BroadcastReceiver;
.source "MyBroadcastreceiver.java"


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x43

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 11
    invoke-direct {v0}, Landroid/content/BroadcastReceiver;-><init>()V

    return-void
.end method


# virtual methods
.method public onReceive(Landroid/content/Context;Landroid/content/Intent;)V
    .locals 14
    .param p1, "context"    # Landroid/content/Context;
    .param p2, "intent"    # Landroid/content/Intent;

    const v13, 0x44

    invoke-static {v13}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object/from16 v12, p2

    move-object/from16 v11, p1

    move-object v10, p0

    .prologue
    const/4 v6, 0x0

    .line 14
    const-string v5, "autostart"

    invoke-virtual {v11, v5, v6}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v3

    .line 15
    .local v3, "prefs":Landroid/content/SharedPreferences;
    const-string v5, "startdelay"

    invoke-interface {v3, v5, v6}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v4

    .line 17
    .local v4, "timeout":I
    const-string v5, "alarm"

    invoke-virtual {v11, v5}, Landroid/content/Context;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/app/AlarmManager;

    .line 18
    .local v1, "mgr":Landroid/app/AlarmManager;
    new-instance v0, Landroid/content/Intent;

    const-class v5, Lcom/autostart/TimeOutReceiver;

    invoke-direct {v0, v11, v5}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 19
    .local v0, "i":Landroid/content/Intent;
    invoke-static {v11, v6, v0, v6}, Landroid/app/PendingIntent;->getBroadcast(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;

    move-result-object v2

    .line 21
    .local v2, "pi":Landroid/app/PendingIntent;
    const/4 v5, 0x2

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v6

    mul-int/lit16 v8, v4, 0x3e8

    add-int/lit16 v8, v8, 0x12c

    int-to-long v8, v8

    add-long/2addr v6, v8

    invoke-virtual {v1, v5, v6, v7, v2}, Landroid/app/AlarmManager;->set(IJLandroid/app/PendingIntent;)V

    .line 23
    return-void
.end method
