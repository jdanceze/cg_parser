.class public Lcom/apposcopy/ella/runtime/Ella;
.super Ljava/lang/Object;
.source "Ella.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/apposcopy/ella/runtime/Ella$UploadThread;,
        Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;
    }
.end annotation


# static fields
.field private static final TRACERECORD_TIME_PERIOD:I = 0x1388

.field private static final UPLOAD_TIME_PERIOD:I = 0x1f4

.field private static id:Ljava/lang/String;

.field private static recorder:Lcom/apposcopy/ella/runtime/Recorder;

.field private static recorderClassName:Ljava/lang/String;

.field private static traceRecordingThread:Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;

.field private static uploadThread:Lcom/apposcopy/ella/runtime/Ella$UploadThread;

.field private static uploadUrl:Ljava/lang/String;

.field private static useAndroidDebug:Z


# direct methods
.method static constructor <clinit>()V
    .locals 1

    const/4 v0, 0x0

    sput-boolean v0, Lcom/apposcopy/ella/runtime/Ella;->useAndroidDebug:Z

    const-string v0, "10.0.2.2:25745"

    sput-object v0, Lcom/apposcopy/ella/runtime/Ella;->uploadUrl:Ljava/lang/String;

    const-string v0, "_workspace_finance_10_com.autostart.apk"

    sput-object v0, Lcom/apposcopy/ella/runtime/Ella;->id:Ljava/lang/String;

    const-string v0, "com.apposcopy.ella.runtime.MethodCoverageRecorder"

    sput-object v0, Lcom/apposcopy/ella/runtime/Ella;->recorderClassName:Ljava/lang/String;

    .prologue
    .line 33
    invoke-static {}, Lcom/apposcopy/ella/runtime/Ella;->startRecording()V

    .line 34
    return-void
.end method

.method public constructor <init>()V
    .locals 0

    .prologue
    .line 17
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method static synthetic access$200()Ljava/lang/String;
    .locals 1

    .prologue
    .line 17
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->uploadUrl:Ljava/lang/String;

    return-object v0
.end method

.method static synthetic access$300()Lcom/apposcopy/ella/runtime/Recorder;
    .locals 1

    .prologue
    .line 17
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->recorder:Lcom/apposcopy/ella/runtime/Recorder;

    return-object v0
.end method

.method static synthetic access$400()Ljava/lang/String;
    .locals 1

    .prologue
    .line 17
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->id:Ljava/lang/String;

    return-object v0
.end method

.method static synthetic access$500()Ljava/lang/String;
    .locals 1

    .prologue
    .line 17
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->recorderClassName:Ljava/lang/String;

    return-object v0
.end method

.method public static m(I)V
    .locals 1
    .param p0, "mId"    # I

    .prologue
    .line 38
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->recorder:Lcom/apposcopy/ella/runtime/Recorder;

    invoke-virtual {v0, p0}, Lcom/apposcopy/ella/runtime/Recorder;->m(I)V

    .line 39
    return-void
.end method

.method static startRecording()V
    .locals 2

    .prologue
    .line 49
    :try_start_0
    sget-object v1, Lcom/apposcopy/ella/runtime/Ella;->recorderClassName:Ljava/lang/String;

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/Class;->newInstance()Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Lcom/apposcopy/ella/runtime/Recorder;

    sput-object v1, Lcom/apposcopy/ella/runtime/Ella;->recorder:Lcom/apposcopy/ella/runtime/Recorder;

    .line 50
    new-instance v1, Lcom/apposcopy/ella/runtime/Ella$UploadThread;

    invoke-direct {v1}, Lcom/apposcopy/ella/runtime/Ella$UploadThread;-><init>()V

    sput-object v1, Lcom/apposcopy/ella/runtime/Ella;->uploadThread:Lcom/apposcopy/ella/runtime/Ella$UploadThread;

    .line 51
    sget-object v1, Lcom/apposcopy/ella/runtime/Ella;->uploadThread:Lcom/apposcopy/ella/runtime/Ella$UploadThread;

    invoke-virtual {v1}, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->start()V
    :try_end_0
    .catch Ljava/lang/ClassNotFoundException; {:try_start_0 .. :try_end_0} :catch_0
    .catch Ljava/lang/InstantiationException; {:try_start_0 .. :try_end_0} :catch_1
    .catch Ljava/lang/IllegalAccessException; {:try_start_0 .. :try_end_0} :catch_2

    .line 60
    sget-boolean v1, Lcom/apposcopy/ella/runtime/Ella;->useAndroidDebug:Z

    if-eqz v1, :cond_0

    .line 61
    new-instance v1, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;

    invoke-direct {v1}, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;-><init>()V

    sput-object v1, Lcom/apposcopy/ella/runtime/Ella;->traceRecordingThread:Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;

    .line 62
    sget-object v1, Lcom/apposcopy/ella/runtime/Ella;->traceRecordingThread:Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;

    invoke-virtual {v1}, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;->start()V

    .line 64
    :cond_0
    return-void

    .line 52
    :catch_0
    move-exception v0

    .line 53
    .local v0, "e":Ljava/lang/ClassNotFoundException;
    new-instance v1, Ljava/lang/Error;

    invoke-direct {v1, v0}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v1

    .line 54
    .end local v0    # "e":Ljava/lang/ClassNotFoundException;
    :catch_1
    move-exception v0

    .line 55
    .local v0, "e":Ljava/lang/InstantiationException;
    new-instance v1, Ljava/lang/Error;

    invoke-direct {v1, v0}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v1

    .line 56
    .end local v0    # "e":Ljava/lang/InstantiationException;
    :catch_2
    move-exception v0

    .line 57
    .local v0, "e":Ljava/lang/IllegalAccessException;
    new-instance v1, Ljava/lang/Error;

    invoke-direct {v1, v0}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v1
.end method

.method static stopRecording()V
    .locals 2

    .prologue
    const/4 v1, 0x1

    .line 68
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->uploadThread:Lcom/apposcopy/ella/runtime/Ella$UploadThread;

    invoke-static {v0, v1}, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->access$002(Lcom/apposcopy/ella/runtime/Ella$UploadThread;Z)Z

    .line 69
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->traceRecordingThread:Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;

    if-eqz v0, :cond_0

    .line 70
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->traceRecordingThread:Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;

    invoke-static {v0, v1}, Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;->access$102(Lcom/apposcopy/ella/runtime/Ella$TraceRecordingThread;Z)Z

    .line 71
    :cond_0
    return-void
.end method

.method public static v(ILjava/lang/Object;)V
    .locals 1
    .param p0, "metadata"    # I
    .param p1, "obj"    # Ljava/lang/Object;

    .prologue
    .line 43
    sget-object v0, Lcom/apposcopy/ella/runtime/Ella;->recorder:Lcom/apposcopy/ella/runtime/Recorder;

    invoke-virtual {v0, p1, p0}, Lcom/apposcopy/ella/runtime/Recorder;->v(Ljava/lang/Object;I)V

    .line 44
    return-void
.end method
