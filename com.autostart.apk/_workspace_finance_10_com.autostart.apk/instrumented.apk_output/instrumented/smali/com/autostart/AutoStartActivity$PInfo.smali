.class Lcom/autostart/AutoStartActivity$PInfo;
.super Ljava/lang/Object;
.source "AutoStartActivity.java"

# interfaces
.implements Ljava/lang/Comparable;


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/autostart/AutoStartActivity;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = "PInfo"
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Ljava/lang/Comparable",
        "<",
        "Lcom/autostart/AutoStartActivity$PInfo;",
        ">;"
    }
.end annotation


# instance fields
.field private appname:Ljava/lang/String;

.field private packageInfo:Landroid/content/pm/ApplicationInfo;

.field private pname:Ljava/lang/String;

.field final synthetic this$0:Lcom/autostart/AutoStartActivity;


# direct methods
.method public constructor <init>(Lcom/autostart/AutoStartActivity;)V
    .locals 4

    const v3, 0x1d

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 435
    iput-object v2, v1, Lcom/autostart/AutoStartActivity$PInfo;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-direct {v1}, Ljava/lang/Object;-><init>()V

    .line 437
    const-string v0, ""

    iput-object v0, v1, Lcom/autostart/AutoStartActivity$PInfo;->appname:Ljava/lang/String;

    .line 438
    const-string v0, ""

    iput-object v0, v1, Lcom/autostart/AutoStartActivity$PInfo;->pname:Ljava/lang/String;

    .line 435
    return-void
.end method

.method static synthetic access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;
    .locals 3

    const v2, 0x1e

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 437
    iget-object v0, v1, Lcom/autostart/AutoStartActivity$PInfo;->appname:Ljava/lang/String;

    return-object v0
.end method

.method static synthetic access$2(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;
    .locals 3

    const v2, 0x1f

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 438
    iget-object v0, v1, Lcom/autostart/AutoStartActivity$PInfo;->pname:Ljava/lang/String;

    return-object v0
.end method

.method static synthetic access$3(Lcom/autostart/AutoStartActivity$PInfo;)Landroid/content/pm/ApplicationInfo;
    .locals 3

    const v2, 0x20

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 439
    iget-object v0, v1, Lcom/autostart/AutoStartActivity$PInfo;->packageInfo:Landroid/content/pm/ApplicationInfo;

    return-object v0
.end method

.method static synthetic access$4(Lcom/autostart/AutoStartActivity$PInfo;Ljava/lang/String;)V
    .locals 3

    const v2, 0x21

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 437
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$PInfo;->appname:Ljava/lang/String;

    return-void
.end method

.method static synthetic access$5(Lcom/autostart/AutoStartActivity$PInfo;Ljava/lang/String;)V
    .locals 3

    const v2, 0x22

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 438
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$PInfo;->pname:Ljava/lang/String;

    return-void
.end method

.method static synthetic access$6(Lcom/autostart/AutoStartActivity$PInfo;Landroid/content/pm/ApplicationInfo;)V
    .locals 3

    const v2, 0x23

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 439
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$PInfo;->packageInfo:Landroid/content/pm/ApplicationInfo;

    return-void
.end method


# virtual methods
.method public compareTo(Lcom/autostart/AutoStartActivity$PInfo;)I
    .locals 5
    .param p1, "p"    # Lcom/autostart/AutoStartActivity$PInfo;

    const v4, 0x24

    invoke-static {v4}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v3, p1

    move-object v2, p0

    .prologue
    .line 443
    iget-object v0, v2, Lcom/autostart/AutoStartActivity$PInfo;->appname:Ljava/lang/String;

    iget-object v1, v3, Lcom/autostart/AutoStartActivity$PInfo;->appname:Ljava/lang/String;

    invoke-virtual {v0, v1}, Ljava/lang/String;->compareToIgnoreCase(Ljava/lang/String;)I

    move-result v0

    return v0
.end method

.method public bridge synthetic compareTo(Ljava/lang/Object;)I
    .locals 4

    const v3, 0x25

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 1
    check-cast v2, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-virtual {v1, v2}, Lcom/autostart/AutoStartActivity$PInfo;->compareTo(Lcom/autostart/AutoStartActivity$PInfo;)I

    move-result v0

    return v0
.end method
