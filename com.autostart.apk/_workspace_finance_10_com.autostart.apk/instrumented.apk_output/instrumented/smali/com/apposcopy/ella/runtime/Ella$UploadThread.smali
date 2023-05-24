.class Lcom/apposcopy/ella/runtime/Ella$UploadThread;
.super Ljava/lang/Thread;
.source "Ella.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/apposcopy/ella/runtime/Ella;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0xa
    name = "UploadThread"
.end annotation


# instance fields
.field private first:Z

.field private socketOut:Ljava/io/OutputStream;

.field private stop:Z


# direct methods
.method constructor <init>()V
    .locals 1

    .prologue
    .line 112
    invoke-direct {p0}, Ljava/lang/Thread;-><init>()V

    .line 106
    const/4 v0, 0x0

    iput-boolean v0, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->stop:Z

    .line 107
    const/4 v0, 0x1

    iput-boolean v0, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->first:Z

    .line 113
    return-void
.end method

.method static synthetic access$002(Lcom/apposcopy/ella/runtime/Ella$UploadThread;Z)Z
    .locals 0
    .param p0, "x0"    # Lcom/apposcopy/ella/runtime/Ella$UploadThread;
    .param p1, "x1"    # Z

    .prologue
    .line 104
    iput-boolean p1, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->stop:Z

    return p1
.end method


# virtual methods
.method public run()V
    .locals 8

    .prologue
    const/4 v7, 0x1

    .line 117
    invoke-static {}, Lcom/apposcopy/ella/runtime/Ella;->access$200()Ljava/lang/String;

    move-result-object v5

    const-string v6, ":"

    invoke-virtual {v5, v6}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v4

    .line 118
    .local v4, "tokens":[Ljava/lang/String;
    const/4 v5, 0x0

    aget-object v2, v4, v5

    .line 119
    .local v2, "serverAddress":Ljava/lang/String;
    aget-object v5, v4, v7

    invoke-static {v5}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v1

    .line 121
    .local v1, "port":I
    :try_start_0
    new-instance v3, Ljava/net/Socket;

    invoke-direct {v3, v2, v1}, Ljava/net/Socket;-><init>(Ljava/lang/String;I)V

    .line 122
    .local v3, "socket":Ljava/net/Socket;
    const/4 v5, 0x1

    invoke-virtual {v3, v5}, Ljava/net/Socket;->setKeepAlive(Z)V

    .line 123
    invoke-virtual {v3}, Ljava/net/Socket;->getOutputStream()Ljava/io/OutputStream;

    move-result-object v5

    iput-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;
    :try_end_0
    .catch Ljava/io/IOException; {:try_start_0 .. :try_end_0} :catch_2

    .line 128
    :goto_0
    iget-boolean v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->stop:Z

    if-nez v5, :cond_0

    .line 130
    const-wide/16 v6, 0x1f4

    :try_start_1
    invoke-static {v6, v7}, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->sleep(J)V
    :try_end_1
    .catch Ljava/lang/InterruptedException; {:try_start_1 .. :try_end_1} :catch_1

    .line 132
    :try_start_2
    invoke-virtual {p0}, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->uploadCoverage()V
    :try_end_2
    .catch Ljava/io/IOException; {:try_start_2 .. :try_end_2} :catch_0
    .catch Ljava/lang/InterruptedException; {:try_start_2 .. :try_end_2} :catch_1

    goto :goto_0

    .line 133
    :catch_0
    move-exception v0

    .line 134
    .local v0, "e":Ljava/io/IOException;
    :try_start_3
    new-instance v5, Ljava/lang/Error;

    invoke-direct {v5, v0}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v5
    :try_end_3
    .catch Ljava/lang/InterruptedException; {:try_start_3 .. :try_end_3} :catch_1

    .line 136
    .end local v0    # "e":Ljava/io/IOException;
    :catch_1
    move-exception v0

    .line 142
    :cond_0
    :try_start_4
    iget-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;

    invoke-virtual {v5}, Ljava/io/OutputStream;->close()V
    :try_end_4
    .catch Ljava/io/IOException; {:try_start_4 .. :try_end_4} :catch_3

    .line 146
    return-void

    .line 124
    .end local v3    # "socket":Ljava/net/Socket;
    :catch_2
    move-exception v0

    .line 125
    .restart local v0    # "e":Ljava/io/IOException;
    new-instance v5, Ljava/lang/Error;

    invoke-direct {v5, v0}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v5

    .line 143
    .end local v0    # "e":Ljava/io/IOException;
    .restart local v3    # "socket":Ljava/net/Socket;
    :catch_3
    move-exception v0

    .line 144
    .restart local v0    # "e":Ljava/io/IOException;
    new-instance v5, Ljava/lang/Error;

    invoke-direct {v5, v0}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v5
.end method

.method public uploadCoverage()V
    .locals 8
    .annotation system Ldalvik/annotation/Throws;
        value = {
            Ljava/io/IOException;
        }
    .end annotation

    .prologue
    .line 150
    invoke-static {}, Lcom/apposcopy/ella/runtime/Ella;->access$300()Lcom/apposcopy/ella/runtime/Recorder;

    move-result-object v5

    invoke-virtual {v5}, Lcom/apposcopy/ella/runtime/Recorder;->data()Ljava/lang/String;

    move-result-object v4

    .line 151
    .local v4, "payload":Ljava/lang/String;
    invoke-virtual {v4}, Ljava/lang/String;->length()I

    move-result v5

    if-nez v5, :cond_0

    iget-boolean v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->stop:Z

    if-nez v5, :cond_0

    .line 152
    const-string v5, "ella"

    const-string v6, "no data to upload"

    invoke-static {v5, v6}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 178
    :goto_0
    return-void

    .line 155
    :cond_0
    new-instance v2, Lorg/json/JSONObject;

    invoke-direct {v2}, Lorg/json/JSONObject;-><init>()V

    .line 157
    .local v2, "json":Lorg/json/JSONObject;
    :try_start_0
    const-string v5, "id"

    invoke-static {}, Lcom/apposcopy/ella/runtime/Ella;->access$400()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v5, v6}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

    .line 158
    const-string v5, "cov"

    invoke-virtual {v2, v5, v4}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

    .line 159
    const-string v5, "stop"

    iget-boolean v6, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->stop:Z

    invoke-static {v6}, Ljava/lang/String;->valueOf(Z)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v5, v6}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

    .line 160
    iget-boolean v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->first:Z

    if-eqz v5, :cond_1

    .line 161
    const-string v5, "recorder"

    invoke-static {}, Lcom/apposcopy/ella/runtime/Ella;->access$500()Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v2, v5, v6}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;

    .line 162
    const/4 v5, 0x0

    iput-boolean v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->first:Z
    :try_end_0
    .catch Lorg/json/JSONException; {:try_start_0 .. :try_end_0} :catch_1

    .line 167
    :cond_1
    invoke-virtual {v2}, Lorg/json/JSONObject;->toString()Ljava/lang/String;

    move-result-object v3

    .line 168
    .local v3, "jsonString":Ljava/lang/String;
    const-string v5, "UTF-8"

    invoke-static {v5}, Ljava/nio/charset/Charset;->forName(Ljava/lang/String;)Ljava/nio/charset/Charset;

    move-result-object v5

    invoke-virtual {v3, v5}, Ljava/lang/String;->getBytes(Ljava/nio/charset/Charset;)[B

    move-result-object v0

    .line 169
    .local v0, "content":[B
    const-string v5, "ella"

    new-instance v6, Ljava/lang/StringBuilder;

    invoke-direct {v6}, Ljava/lang/StringBuilder;-><init>()V

    const-string v7, "Uploading coverage. id: "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-static {}, Lcom/apposcopy/ella/runtime/Ella;->access$400()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, " data size: "

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    array-length v7, v0

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v6

    invoke-static {v5, v6}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 172
    :try_start_1
    iget-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;

    invoke-virtual {v5, v0}, Ljava/io/OutputStream;->write([B)V

    .line 173
    iget-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;

    const/16 v6, 0xd

    invoke-virtual {v5, v6}, Ljava/io/OutputStream;->write(I)V

    iget-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;

    const/16 v6, 0xa

    invoke-virtual {v5, v6}, Ljava/io/OutputStream;->write(I)V

    iget-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;

    const/16 v6, 0xd

    invoke-virtual {v5, v6}, Ljava/io/OutputStream;->write(I)V

    iget-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;

    const/16 v6, 0xa

    invoke-virtual {v5, v6}, Ljava/io/OutputStream;->write(I)V

    .line 174
    iget-object v5, p0, Lcom/apposcopy/ella/runtime/Ella$UploadThread;->socketOut:Ljava/io/OutputStream;

    invoke-virtual {v5}, Ljava/io/OutputStream;->flush()V
    :try_end_1
    .catch Ljava/io/IOException; {:try_start_1 .. :try_end_1} :catch_0

    goto/16 :goto_0

    .line 175
    :catch_0
    move-exception v1

    .line 176
    .local v1, "e":Ljava/io/IOException;
    new-instance v5, Ljava/lang/Error;

    invoke-direct {v5, v1}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v5

    .line 164
    .end local v0    # "content":[B
    .end local v1    # "e":Ljava/io/IOException;
    .end local v3    # "jsonString":Ljava/lang/String;
    :catch_1
    move-exception v1

    .line 165
    .local v1, "e":Lorg/json/JSONException;
    new-instance v5, Ljava/lang/Error;

    invoke-direct {v5, v1}, Ljava/lang/Error;-><init>(Ljava/lang/Throwable;)V

    throw v5
.end method
