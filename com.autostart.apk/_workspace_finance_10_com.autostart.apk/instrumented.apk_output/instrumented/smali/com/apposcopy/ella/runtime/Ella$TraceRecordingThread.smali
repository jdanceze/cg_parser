.class Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;
.super Ljava/lang/Thread;
.source "Ella.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/apposcopy/ella/runtime/Ella;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0xa
    name = "TraceRecordingThread"
.end annotation


# instance fields
.field private stop:Z


# direct methods
.method constructor <init>()V
    .locals 1

    .prologue
    .line 79
    invoke-direct {p0}, Ljava/lang/Thread;-><init>()V

    .line 75
    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;->stop:Z

    .line 80
    return-void
.end method

.method static synthetic access$102(Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;Z)Z
    .locals 0
    .param p0, "x0"    # Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;
    .param p1, "x1"    # Z

    .prologue
    .line 73
    iput-boolean p1, p0, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;->stop:Z

    return p1
.end method


# virtual methods
.method public run()V
    .locals 8

    .prologue
    .line 84
    const/4 v0, 0x1

    .line 85
    .local v0, "count":I
    const-string v4, "/sdcard/debug.traces"

    .line 86
    .local v4, "traceDirName":Ljava/lang/String;
    new-instance v3, Ljava/io/File;

    invoke-direct {v3, v4}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 87
    .local v3, "traceDir":Ljava/io/File;
    invoke-virtual {v3}, Ljava/io/File;->exists()Z

    move-result v6

    if-nez v6, :cond_0

    .line 88
    invoke-virtual {v3}, Ljava/io/File;->mkdir()Z

    .line 90
    :cond_0
    :goto_0
    iget-boolean v6, p0, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;->stop:Z

    if-nez v6, :cond_1

    .line 92
    :try_start_0
    new-instance v1, Ljava/text/SimpleDateFormat;

    const-string v6, "yyyy-MM-dd-HH-mm-ss"

    invoke-direct {v1, v6}, Ljava/text/SimpleDateFormat;-><init>(Ljava/lang/String;)V

    .line 93
    .local v1, "dateFormat":Ljava/text/DateFormat;
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v6, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, "/"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    new-instance v7, Ljava/util/Date;

    invoke-direct {v7}, Ljava/util/Date;-><init>()V

    invoke-virtual {v1, v7}, Ljava/text/DateFormat;->format(Ljava/util/Date;)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v5

    .line 94
    .local v5, "traceFileName":Ljava/lang/String;
    invoke-static {v5}, Landroid/os/Debug;->startMethodTracing(Ljava/lang/String;)V

    .line 95
    const-wide/16 v6, 0x1388

    invoke-static {v6, v7}, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;->sleep(J)V

    .line 96
    invoke-static {}, Landroid/os/Debug;->stopMethodTracing()V
    :try_end_0
    .catch Ljava/lang/InterruptedException; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 97
    .end local v1    # "dateFormat":Ljava/text/DateFormat;
    .end local v5    # "traceFileName":Ljava/lang/String;
    :catch_0
    move-exception v2

    .line 101
    :cond_1
    return-void
.end method
