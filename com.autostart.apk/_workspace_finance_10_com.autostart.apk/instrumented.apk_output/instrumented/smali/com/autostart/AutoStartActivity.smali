.class public Lcom/autostart/AutoStartActivity;
.super Landroid/app/Activity;
.source "AutoStartActivity.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;,
        Lcom/autostart/AutoStartActivity$IconifiedTextView;,
        Lcom/autostart/AutoStartActivity$OnResumeTask;,
        Lcom/autostart/AutoStartActivity$PInfo;
    }
.end annotation


# static fields
.field public static final TAG:Ljava/lang/String; = "*******AutoStart*********  "


# instance fields
.field private appList:Ljava/util/ArrayList;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/ArrayList",
            "<",
            "Lcom/autostart/AutoStartActivity$PInfo;",
            ">;"
        }
    .end annotation
.end field

.field private bt:Landroid/widget/Button;

.field private btDiscoLight:Landroid/widget/Button;

.field private cb:Landroid/widget/CheckBox;

.field private cbGoTOHome:Landroid/widget/CheckBox;

.field private cb_showAll:Landroid/widget/CheckBox;

.field private etDelay:Landroid/widget/EditText;

.field private etDelayBetween:Landroid/widget/EditText;

.field private imageCache:Landroid/support/v4/util/LruCache;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroid/support/v4/util/LruCache",
            "<",
            "Ljava/lang/String;",
            "Landroid/graphics/drawable/Drawable;",
            ">;"
        }
    .end annotation
.end field

.field private ita:Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

.field private llAppSelect:Landroid/widget/LinearLayout;

.field private llApps:Landroid/widget/LinearLayout;

.field private lv:Landroid/widget/ListView;

.field private newPackageList:Ljava/util/List;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/List",
            "<",
            "Lcom/autostart/AutoStartActivity$PInfo;",
            ">;"
        }
    .end annotation
.end field

.field private newPackageList_all:Ljava/util/List;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/List",
            "<",
            "Lcom/autostart/AutoStartActivity$PInfo;",
            ">;"
        }
    .end annotation
.end field

.field private packages:Ljava/util/List;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/List",
            "<",
            "Landroid/content/pm/ApplicationInfo;",
            ">;"
        }
    .end annotation
.end field

.field private pm:Landroid/content/pm/PackageManager;

.field private progressbar:Landroid/widget/ProgressBar;

.field private resumeTask:Lcom/autostart/AutoStartActivity$OnResumeTask;

.field private scale:F

.field private svAppSettings:Landroid/widget/ScrollView;

.field private tb:Landroid/widget/ToggleButton;

.field private textView1:Landroid/widget/TextView;

.field private textView2:Landroid/widget/TextView;

.field private tvDelay:Landroid/widget/TextView;

.field private tvDelayBetween:Landroid/widget/TextView;


# direct methods
.method public constructor <init>()V
    .locals 2

    const v1, 0x26

    invoke-static {v1}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v0, p0

    .prologue
    .line 50
    invoke-direct {v0}, Landroid/app/Activity;-><init>()V

    return-void
.end method

.method static synthetic access$0(Lcom/autostart/AutoStartActivity;)Landroid/support/v4/util/LruCache;
    .locals 3

    const v2, 0x27

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 64
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->imageCache:Landroid/support/v4/util/LruCache;

    return-object v0
.end method

.method static synthetic access$1(Lcom/autostart/AutoStartActivity;Landroid/graphics/drawable/Drawable;)Landroid/graphics/drawable/Drawable;
    .locals 4

    const v3, 0x28

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 186
    invoke-direct {v1, v2}, Lcom/autostart/AutoStartActivity;->resize(Landroid/graphics/drawable/Drawable;)Landroid/graphics/drawable/Drawable;

    move-result-object v0

    return-object v0
.end method

.method static synthetic access$10(Lcom/autostart/AutoStartActivity;)Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;
    .locals 3

    const v2, 0x29

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 62
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->ita:Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    return-object v0
.end method

.method static synthetic access$11(Lcom/autostart/AutoStartActivity;)Landroid/widget/Button;
    .locals 3

    const v2, 0x2a

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 55
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->bt:Landroid/widget/Button;

    return-object v0
.end method

.method static synthetic access$12(Lcom/autostart/AutoStartActivity;)Landroid/widget/ProgressBar;
    .locals 3

    const v2, 0x2b

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 56
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->progressbar:Landroid/widget/ProgressBar;

    return-object v0
.end method

.method static synthetic access$13(Lcom/autostart/AutoStartActivity;Z)V
    .locals 3

    const v2, 0x2c

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move v1, p1

    move-object v0, p0

    .prologue
    .line 304
    invoke-direct {v0, v1}, Lcom/autostart/AutoStartActivity;->showMainScreen(Z)V

    return-void
.end method

.method static synthetic access$14(Lcom/autostart/AutoStartActivity;)Ljava/util/ArrayList;
    .locals 3

    const v2, 0x2d

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 66
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    return-object v0
.end method

.method static synthetic access$15(Lcom/autostart/AutoStartActivity;)Landroid/widget/LinearLayout;
    .locals 3

    const v2, 0x2e

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 65
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->llApps:Landroid/widget/LinearLayout;

    return-object v0
.end method

.method static synthetic access$2(Lcom/autostart/AutoStartActivity;)F
    .locals 3

    const v2, 0x2f

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 70
    iget v0, v1, Lcom/autostart/AutoStartActivity;->scale:F

    return v0
.end method

.method static synthetic access$3(Lcom/autostart/AutoStartActivity;)Ljava/util/List;
    .locals 3

    const v2, 0x30

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 59
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->packages:Ljava/util/List;

    return-object v0
.end method

.method static synthetic access$4(Lcom/autostart/AutoStartActivity;)Landroid/content/pm/PackageManager;
    .locals 3

    const v2, 0x31

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 60
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->pm:Landroid/content/pm/PackageManager;

    return-object v0
.end method

.method static synthetic access$5(Lcom/autostart/AutoStartActivity;Landroid/content/pm/ApplicationInfo;)Z
    .locals 4

    const v3, 0x32

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 326
    invoke-direct {v1, v2}, Lcom/autostart/AutoStartActivity;->isSystemPackage(Landroid/content/pm/ApplicationInfo;)Z

    move-result v0

    return v0
.end method

.method static synthetic access$6(Lcom/autostart/AutoStartActivity;)Ljava/util/List;
    .locals 3

    const v2, 0x33

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 61
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->newPackageList:Ljava/util/List;

    return-object v0
.end method

.method static synthetic access$7(Lcom/autostart/AutoStartActivity;)Ljava/util/List;
    .locals 3

    const v2, 0x34

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 61
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->newPackageList_all:Ljava/util/List;

    return-object v0
.end method

.method static synthetic access$8(Lcom/autostart/AutoStartActivity;Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;)V
    .locals 3

    const v2, 0x35

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 62
    iput-object v1, v0, Lcom/autostart/AutoStartActivity;->ita:Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    return-void
.end method

.method static synthetic access$9(Lcom/autostart/AutoStartActivity;)Landroid/widget/ListView;
    .locals 3

    const v2, 0x36

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 53
    iget-object v0, v1, Lcom/autostart/AutoStartActivity;->lv:Landroid/widget/ListView;

    return-object v0
.end method

.method private isCallable(Landroid/content/Intent;)Z
    .locals 6
    .param p1, "intent"    # Landroid/content/Intent;

    const v5, 0x37

    invoke-static {v5}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v4, p1

    move-object v3, p0

    .prologue
    .line 321
    invoke-virtual {v3}, Lcom/autostart/AutoStartActivity;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v1

    .line 322
    const/high16 v2, 0x10000

    .line 321
    invoke-virtual {v1, v4, v2}, Landroid/content/pm/PackageManager;->queryIntentActivities(Landroid/content/Intent;I)Ljava/util/List;

    move-result-object v0

    .line 323
    .local v0, "list":Ljava/util/List;, "Ljava/util/List<Landroid/content/pm/ResolveInfo;>;"
    invoke-interface {v0}, Ljava/util/List;->size()I

    move-result v1

    if-lez v1, :cond_0

    const/4 v1, 0x1

    :goto_0
    return v1

    :cond_0
    const/4 v1, 0x0

    goto :goto_0
.end method

.method private isSystemPackage(Landroid/content/pm/ApplicationInfo;)Z
    .locals 4
    .param p1, "aapplicationInfo"    # Landroid/content/pm/ApplicationInfo;

    const v3, 0x38

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 327
    iget v0, v2, Landroid/content/pm/ApplicationInfo;->flags:I

    and-int/lit8 v0, v0, 0x1

    if-eqz v0, :cond_0

    const/4 v0, 0x1

    :goto_0
    return v0

    .line 328
    :cond_0
    const/4 v0, 0x0

    goto :goto_0
.end method

.method private resize(Landroid/graphics/drawable/Drawable;)Landroid/graphics/drawable/Drawable;
    .locals 11
    .param p1, "image"    # Landroid/graphics/drawable/Drawable;

    const v10, 0x39

    invoke-static {v10}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v9, p1

    move-object v8, p0

    .prologue
    const/high16 v7, 0x42aa0000    # 85.0f

    const/high16 v6, 0x3f000000    # 0.5f

    .line 189
    invoke-virtual {v8}, Lcom/autostart/AutoStartActivity;->getResources()Landroid/content/res/Resources;

    move-result-object v5

    invoke-virtual {v5}, Landroid/content/res/Resources;->getDisplayMetrics()Landroid/util/DisplayMetrics;

    move-result-object v5

    iget v3, v5, Landroid/util/DisplayMetrics;->density:F

    .line 190
    .local v3, "scale":F
    mul-float v5, v7, v3

    add-float/2addr v5, v6

    float-to-int v4, v5

    .line 191
    .local v4, "width":I
    mul-float v5, v7, v3

    add-float/2addr v5, v6

    float-to-int v2, v5

    .line 193
    .local v2, "height":I
    :try_start_0
    invoke-virtual {v9}, Landroid/graphics/drawable/Drawable;->getCurrent()Landroid/graphics/drawable/Drawable;

    move-result-object v5

    check-cast v5, Landroid/graphics/drawable/BitmapDrawable;

    invoke-virtual {v5}, Landroid/graphics/drawable/BitmapDrawable;->getBitmap()Landroid/graphics/Bitmap;

    move-result-object v1

    .line 194
    .local v1, "d":Landroid/graphics/Bitmap;
    const/4 v5, 0x0

    invoke-static {v1, v4, v2, v5}, Landroid/graphics/Bitmap;->createScaledBitmap(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;

    move-result-object v0

    .line 195
    .local v0, "bitmapOrig":Landroid/graphics/Bitmap;
    new-instance v5, Landroid/graphics/drawable/BitmapDrawable;

    invoke-direct {v5, v0}, Landroid/graphics/drawable/BitmapDrawable;-><init>(Landroid/graphics/Bitmap;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 199
    .end local v0    # "bitmapOrig":Landroid/graphics/Bitmap;
    .end local v1    # "d":Landroid/graphics/Bitmap;
    :goto_0
    return-object v5

    .line 197
    :catch_0
    move-exception v5

    .line 199
    const/4 v5, 0x0

    goto :goto_0
.end method

.method private showMainScreen(Z)V
    .locals 6
    .param p1, "b"    # Z

    const v5, 0x3a

    invoke-static {v5}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move v4, p1

    move-object v3, p0

    .prologue
    const/16 v2, 0x8

    const/4 v1, 0x0

    .line 305
    if-eqz v4, :cond_0

    .line 306
    iget-object v0, v3, Lcom/autostart/AutoStartActivity;->svAppSettings:Landroid/widget/ScrollView;

    invoke-virtual {v0, v1}, Landroid/widget/ScrollView;->setVisibility(I)V

    .line 307
    iget-object v0, v3, Lcom/autostart/AutoStartActivity;->llAppSelect:Landroid/widget/LinearLayout;

    invoke-virtual {v0, v2}, Landroid/widget/LinearLayout;->setVisibility(I)V

    .line 312
    :goto_0
    return-void

    .line 309
    :cond_0
    iget-object v0, v3, Lcom/autostart/AutoStartActivity;->svAppSettings:Landroid/widget/ScrollView;

    invoke-virtual {v0, v2}, Landroid/widget/ScrollView;->setVisibility(I)V

    .line 310
    iget-object v0, v3, Lcom/autostart/AutoStartActivity;->llAppSelect:Landroid/widget/LinearLayout;

    invoke-virtual {v0, v1}, Landroid/widget/LinearLayout;->setVisibility(I)V

    goto :goto_0
.end method


# virtual methods
.method protected addToAppList(Lcom/autostart/AutoStartActivity$PInfo;)V
    .locals 14
    .param p1, "pi"    # Lcom/autostart/AutoStartActivity$PInfo;

    const v13, 0x3b

    invoke-static {v13}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object/from16 v12, p1

    move-object v11, p0

    .prologue
    const/high16 v10, 0x41b00000    # 22.0f

    const/4 v9, 0x6

    const/4 v8, 0x3

    const/4 v7, -0x2

    .line 152
    new-instance v0, Landroid/widget/LinearLayout;

    invoke-direct {v0, v11}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    .line 153
    .local v0, "ll":Landroid/widget/LinearLayout;
    const/4 v5, 0x0

    invoke-virtual {v0, v5}, Landroid/widget/LinearLayout;->setOrientation(I)V

    .line 156
    new-instance v4, Landroid/widget/TextView;

    invoke-direct {v4, v11}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    .line 157
    .local v4, "tv":Landroid/widget/TextView;
    invoke-virtual {v4, v8, v9, v8, v9}, Landroid/widget/TextView;->setPadding(IIII)V

    .line 158
    const v5, -0xaf5f10

    invoke-virtual {v4, v5}, Landroid/widget/TextView;->setTextColor(I)V

    .line 159
    invoke-virtual {v4, v10}, Landroid/widget/TextView;->setTextSize(F)V

    .line 160
    new-instance v5, Landroid/widget/LinearLayout$LayoutParams;

    const/4 v6, -0x1

    invoke-direct {v5, v6, v7}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    invoke-virtual {v4, v5}, Landroid/widget/TextView;->setLayoutParams(Landroid/view/ViewGroup$LayoutParams;)V

    .line 161
    invoke-static {v12}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 162
    invoke-static {v12}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v4, v5}, Landroid/widget/TextView;->setTag(Ljava/lang/Object;)V

    .line 163
    invoke-virtual {v4, v11}, Landroid/widget/TextView;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 164
    new-instance v2, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v2, v7, v7}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 166
    .local v2, "paramsText":Landroid/widget/LinearLayout$LayoutParams;
    const/16 v5, 0x13

    iput v5, v2, Landroid/widget/LinearLayout$LayoutParams;->gravity:I

    .line 167
    const/high16 v5, 0x3f800000    # 1.0f

    iput v5, v2, Landroid/widget/LinearLayout$LayoutParams;->weight:F

    .line 168
    invoke-virtual {v0, v4, v2}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 170
    new-instance v1, Landroid/widget/TextView;

    invoke-direct {v1, v11}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    .line 171
    .local v1, "mTextDelete":Landroid/widget/TextView;
    const-string v5, "X"

    invoke-virtual {v1, v5}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 173
    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v3, v7, v7}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 175
    .local v3, "paramsTextDel":Landroid/widget/LinearLayout$LayoutParams;
    const/16 v5, 0x15

    iput v5, v3, Landroid/widget/LinearLayout$LayoutParams;->gravity:I

    .line 176
    invoke-virtual {v0, v1, v3}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 177
    invoke-virtual {v1, v10}, Landroid/widget/TextView;->setTextSize(F)V

    .line 178
    invoke-virtual {v4, v8, v9, v8, v9}, Landroid/widget/TextView;->setPadding(IIII)V

    .line 179
    const/high16 v5, -0x10000

    invoke-virtual {v1, v5}, Landroid/widget/TextView;->setTextColor(I)V

    .line 180
    invoke-static {v12}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v1, v5}, Landroid/widget/TextView;->setTag(Ljava/lang/Object;)V

    .line 181
    invoke-virtual {v1, v11}, Landroid/widget/TextView;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 183
    iget-object v5, v11, Lcom/autostart/AutoStartActivity;->llApps:Landroid/widget/LinearLayout;

    invoke-virtual {v5, v0}, Landroid/widget/LinearLayout;->addView(Landroid/view/View;)V

    .line 184
    return-void
.end method

.method public onClick(Landroid/view/View;)V
    .locals 12
    .param p1, "v"    # Landroid/view/View;

    const v11, 0x3c

    invoke-static {v11}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v10, p1

    move-object v9, p0

    .prologue
    .line 497
    move-object v4, v10

    .line 498
    .local v4, "removeView":Landroid/view/View;
    :try_start_0
    move-object v0, v4

    check-cast v0, Landroid/widget/TextView;

    move-object v6, v0

    invoke-virtual {v6}, Landroid/widget/TextView;->getTag()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Ljava/lang/String;

    .line 499
    .local v5, "title":Ljava/lang/String;
    new-instance v6, Ljava/lang/StringBuilder;

    const-string v7, "Remove "

    invoke-direct {v6, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    invoke-virtual {v6, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    const-string v7, " from autostart list?"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v3

    .line 500
    .local v3, "message":Ljava/lang/String;
    new-instance v2, Landroid/app/AlertDialog$Builder;

    invoke-direct {v2, v9}, Landroid/app/AlertDialog$Builder;-><init>(Landroid/content/Context;)V

    .line 501
    .local v2, "builder":Landroid/app/AlertDialog$Builder;
    invoke-virtual {v2, v5}, Landroid/app/AlertDialog$Builder;->setTitle(Ljava/lang/CharSequence;)Landroid/app/AlertDialog$Builder;

    move-result-object v6

    .line 502
    invoke-virtual {v6, v3}, Landroid/app/AlertDialog$Builder;->setMessage(Ljava/lang/CharSequence;)Landroid/app/AlertDialog$Builder;

    move-result-object v6

    .line 503
    const-string v7, "Yes"

    .line 504
    new-instance v8, Lcom/autostart/AutoStartActivity$5;

    invoke-direct {v8, v9, v4}, Lcom/autostart/AutoStartActivity$5;-><init>(Lcom/autostart/AutoStartActivity;Landroid/view/View;)V

    .line 503
    invoke-virtual {v6, v7, v8}, Landroid/app/AlertDialog$Builder;->setNegativeButton(Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;

    move-result-object v6

    .line 522
    const-string v7, "No"

    .line 523
    new-instance v8, Lcom/autostart/AutoStartActivity$6;

    invoke-direct {v8, v9}, Lcom/autostart/AutoStartActivity$6;-><init>(Lcom/autostart/AutoStartActivity;)V

    .line 522
    invoke-virtual {v6, v7, v8}, Landroid/app/AlertDialog$Builder;->setPositiveButton(Ljava/lang/CharSequence;Landroid/content/DialogInterface$OnClickListener;)Landroid/app/AlertDialog$Builder;

    .line 529
    invoke-virtual {v2}, Landroid/app/AlertDialog$Builder;->create()Landroid/app/AlertDialog;

    move-result-object v1

    .line 530
    .local v1, "alert":Landroid/app/AlertDialog;
    invoke-virtual {v1}, Landroid/app/AlertDialog;->show()V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 533
    .end local v1    # "alert":Landroid/app/AlertDialog;
    .end local v2    # "builder":Landroid/app/AlertDialog$Builder;
    .end local v3    # "message":Ljava/lang/String;
    .end local v5    # "title":Ljava/lang/String;
    :goto_0
    return-void

    .line 531
    :catch_0
    move-exception v6

    goto :goto_0
.end method

.method public onConfigurationChanged(Landroid/content/res/Configuration;)V
    .locals 3
    .param p1, "newConfig"    # Landroid/content/res/Configuration;

    const v2, 0x3d

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 317
    invoke-super {v0, v1}, Landroid/app/Activity;->onConfigurationChanged(Landroid/content/res/Configuration;)V

    .line 318
    return-void
.end method

.method public onCreate(Landroid/os/Bundle;)V
    .locals 8
    .param p1, "savedInstanceState"    # Landroid/os/Bundle;

    const v7, 0x3e

    invoke-static {v7}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v6, p1

    move-object v5, p0

    .prologue
    .line 75
    invoke-super {v5, v6}, Landroid/app/Activity;->onCreate(Landroid/os/Bundle;)V

    .line 76
    const/high16 v3, 0x7f030000

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->setContentView(I)V

    .line 78
    const-string v3, "activity"

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/app/ActivityManager;

    .line 79
    .local v0, "am":Landroid/app/ActivityManager;
    invoke-virtual {v0}, Landroid/app/ActivityManager;->getMemoryClass()I

    move-result v3

    mul-int/lit16 v3, v3, 0x400

    mul-int/lit16 v2, v3, 0x400

    .line 80
    .local v2, "memoryClass":I
    new-instance v3, Landroid/support/v4/util/LruCache;

    div-int/lit8 v4, v2, 0x2

    invoke-direct {v3, v4}, Landroid/support/v4/util/LruCache;-><init>(I)V

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->imageCache:Landroid/support/v4/util/LruCache;

    .line 81
    new-instance v3, Ljava/util/ArrayList;

    invoke-direct {v3}, Ljava/util/ArrayList;-><init>()V

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    .line 83
    const v3, 0x7f050002

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/ListView;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->lv:Landroid/widget/ListView;

    .line 84
    const v3, 0x7f050005

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/ToggleButton;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->tb:Landroid/widget/ToggleButton;

    .line 85
    const v3, 0x7f050008

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/Button;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->bt:Landroid/widget/Button;

    .line 86
    const v3, 0x7f050010

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/Button;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->btDiscoLight:Landroid/widget/Button;

    .line 87
    const v3, 0x7f050006

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/LinearLayout;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->llApps:Landroid/widget/LinearLayout;

    .line 88
    const v3, 0x7f050004

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/TextView;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->textView1:Landroid/widget/TextView;

    .line 89
    const v3, 0x7f050007

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/TextView;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->textView2:Landroid/widget/TextView;

    .line 90
    const v3, 0x7f050009

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/ProgressBar;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->progressbar:Landroid/widget/ProgressBar;

    .line 91
    const v3, 0x7f05000d

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/TextView;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->tvDelay:Landroid/widget/TextView;

    .line 92
    const v3, 0x7f05000f

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/TextView;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->tvDelayBetween:Landroid/widget/TextView;

    .line 93
    const v3, 0x7f05000a

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/CheckBox;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->cb:Landroid/widget/CheckBox;

    .line 94
    const v3, 0x7f05000e

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/EditText;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->etDelay:Landroid/widget/EditText;

    .line 95
    const v3, 0x7f05000b

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/EditText;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->etDelayBetween:Landroid/widget/EditText;

    .line 96
    const v3, 0x7f050001

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/CheckBox;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->cb_showAll:Landroid/widget/CheckBox;

    .line 97
    const/high16 v3, 0x7f050000

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/LinearLayout;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->llAppSelect:Landroid/widget/LinearLayout;

    .line 98
    const v3, 0x7f050003

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/ScrollView;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->svAppSettings:Landroid/widget/ScrollView;

    .line 99
    const v3, 0x7f05000c

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->findViewById(I)Landroid/view/View;

    move-result-object v3

    check-cast v3, Landroid/widget/CheckBox;

    iput-object v3, v5, Lcom/autostart/AutoStartActivity;->cbGoTOHome:Landroid/widget/CheckBox;

    .line 101
    invoke-virtual {v5}, Lcom/autostart/AutoStartActivity;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    invoke-virtual {v3}, Landroid/content/res/Resources;->getDisplayMetrics()Landroid/util/DisplayMetrics;

    move-result-object v3

    iget v3, v3, Landroid/util/DisplayMetrics;->density:F

    iput v3, v5, Lcom/autostart/AutoStartActivity;->scale:F

    .line 103
    const-string v3, "notification"

    invoke-virtual {v5, v3}, Lcom/autostart/AutoStartActivity;->getSystemService(Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/app/NotificationManager;

    .line 104
    .local v1, "mNotificationManager":Landroid/app/NotificationManager;
    const/16 v3, 0xd85

    invoke-virtual {v1, v3}, Landroid/app/NotificationManager;->cancel(I)V

    .line 106
    iget-object v3, v5, Lcom/autostart/AutoStartActivity;->bt:Landroid/widget/Button;

    new-instance v4, Lcom/autostart/AutoStartActivity$1;

    invoke-direct {v4, v5}, Lcom/autostart/AutoStartActivity$1;-><init>(Lcom/autostart/AutoStartActivity;)V

    invoke-virtual {v3, v4}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 113
    iget-object v3, v5, Lcom/autostart/AutoStartActivity;->btDiscoLight:Landroid/widget/Button;

    new-instance v4, Lcom/autostart/AutoStartActivity$2;

    invoke-direct {v4, v5}, Lcom/autostart/AutoStartActivity$2;-><init>(Lcom/autostart/AutoStartActivity;)V

    invoke-virtual {v3, v4}, Landroid/widget/Button;->setOnClickListener(Landroid/view/View$OnClickListener;)V

    .line 122
    iget-object v3, v5, Lcom/autostart/AutoStartActivity;->cb_showAll:Landroid/widget/CheckBox;

    new-instance v4, Lcom/autostart/AutoStartActivity$3;

    invoke-direct {v4, v5}, Lcom/autostart/AutoStartActivity$3;-><init>(Lcom/autostart/AutoStartActivity;)V

    invoke-virtual {v3, v4}, Landroid/widget/CheckBox;->setOnCheckedChangeListener(Landroid/widget/CompoundButton$OnCheckedChangeListener;)V

    .line 134
    iget-object v3, v5, Lcom/autostart/AutoStartActivity;->lv:Landroid/widget/ListView;

    new-instance v4, Lcom/autostart/AutoStartActivity$4;

    invoke-direct {v4, v5}, Lcom/autostart/AutoStartActivity$4;-><init>(Lcom/autostart/AutoStartActivity;)V

    invoke-virtual {v3, v4}, Landroid/widget/ListView;->setOnItemClickListener(Landroid/widget/AdapterView$OnItemClickListener;)V

    .line 148
    return-void
.end method

.method public onKeyDown(ILandroid/view/KeyEvent;)Z
    .locals 6
    .param p1, "keyCode"    # I
    .param p2, "event"    # Landroid/view/KeyEvent;

    const v5, 0x3f

    invoke-static {v5}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v4, p2

    move v3, p1

    move-object v2, p0

    .prologue
    const/4 v0, 0x1

    .line 295
    const/4 v1, 0x4

    if-ne v3, v1, :cond_0

    invoke-virtual {v4}, Landroid/view/KeyEvent;->getRepeatCount()I

    move-result v1

    if-nez v1, :cond_0

    .line 296
    iget-object v1, v2, Lcom/autostart/AutoStartActivity;->llAppSelect:Landroid/widget/LinearLayout;

    invoke-virtual {v1}, Landroid/widget/LinearLayout;->getVisibility()I

    move-result v1

    if-nez v1, :cond_0

    .line 297
    invoke-direct {v2, v0}, Lcom/autostart/AutoStartActivity;->showMainScreen(Z)V

    .line 301
    :goto_0
    return v0

    :cond_0
    invoke-super {v2, v3, v4}, Landroid/app/Activity;->onKeyDown(ILandroid/view/KeyEvent;)Z

    move-result v0

    goto :goto_0
.end method

.method public onPause()V
    .locals 12

    const v11, 0x40

    invoke-static {v11}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v10, p0

    .prologue
    const/4 v9, 0x0

    .line 252
    const-string v4, ""

    .line 253
    .local v4, "packageName":Ljava/lang/String;
    const-string v0, ""

    .line 255
    .local v0, "className":Ljava/lang/String;
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->lv:Landroid/widget/ListView;

    new-instance v7, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;

    new-instance v8, Ljava/util/ArrayList;

    invoke-direct {v8}, Ljava/util/ArrayList;-><init>()V

    invoke-direct {v7, v10, v10, v8}, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;-><init>(Lcom/autostart/AutoStartActivity;Landroid/content/Context;Ljava/util/List;)V

    invoke-virtual {v6, v7}, Landroid/widget/ListView;->setAdapter(Landroid/widget/ListAdapter;)V

    .line 257
    const/4 v3, 0x0

    .local v3, "i":I
    :goto_0
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    invoke-virtual {v6}, Ljava/util/ArrayList;->size()I

    move-result v6

    if-lt v3, v6, :cond_1

    .line 266
    const-string v6, "autostart"

    invoke-virtual {v10, v6, v9}, Lcom/autostart/AutoStartActivity;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v5

    .line 267
    .local v5, "prefs":Landroid/content/SharedPreferences;
    invoke-interface {v5}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v2

    .line 268
    .local v2, "edit":Landroid/content/SharedPreferences$Editor;
    const-string v6, "package"

    invoke-interface {v2, v6, v4}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 269
    const-string v6, "class"

    invoke-interface {v2, v6, v0}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    .line 270
    const-string v6, "enabled"

    iget-object v7, v10, Lcom/autostart/AutoStartActivity;->tb:Landroid/widget/ToggleButton;

    invoke-virtual {v7}, Landroid/widget/ToggleButton;->isChecked()Z

    move-result v7

    invoke-interface {v2, v6, v7}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 271
    const-string v6, "noti"

    iget-object v7, v10, Lcom/autostart/AutoStartActivity;->cb:Landroid/widget/CheckBox;

    invoke-virtual {v7}, Landroid/widget/CheckBox;->isChecked()Z

    move-result v7

    invoke-interface {v2, v6, v7}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 272
    const-string v6, "gotohome"

    iget-object v7, v10, Lcom/autostart/AutoStartActivity;->cbGoTOHome:Landroid/widget/CheckBox;

    invoke-virtual {v7}, Landroid/widget/CheckBox;->isChecked()Z

    move-result v7

    invoke-interface {v2, v6, v7}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    .line 273
    const-string v6, "iteration"

    invoke-interface {v2, v6, v9}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 275
    :try_start_0
    const-string v6, "startdelay"

    iget-object v7, v10, Lcom/autostart/AutoStartActivity;->etDelay:Landroid/widget/EditText;

    invoke-virtual {v7}, Landroid/widget/EditText;->getText()Landroid/text/Editable;

    move-result-object v7

    invoke-interface {v7}, Landroid/text/Editable;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-static {v7}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v2, v6, v7}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 276
    const-string v6, "inbetweendelay"

    iget-object v7, v10, Lcom/autostart/AutoStartActivity;->etDelayBetween:Landroid/widget/EditText;

    invoke-virtual {v7}, Landroid/widget/EditText;->getText()Landroid/text/Editable;

    move-result-object v7

    invoke-interface {v7}, Landroid/text/Editable;->toString()Ljava/lang/String;

    move-result-object v7

    invoke-static {v7}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v7

    invoke-interface {v2, v6, v7}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 282
    :goto_1
    invoke-interface {v2}, Landroid/content/SharedPreferences$Editor;->commit()Z

    .line 284
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->resumeTask:Lcom/autostart/AutoStartActivity$OnResumeTask;

    if-eqz v6, :cond_0

    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->resumeTask:Lcom/autostart/AutoStartActivity$OnResumeTask;

    invoke-static {v6}, Lcom/autostart/AutoStartActivity$OnResumeTask;->access$3(Lcom/autostart/AutoStartActivity$OnResumeTask;)Z

    move-result v6

    if-nez v6, :cond_0

    .line 285
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->resumeTask:Lcom/autostart/AutoStartActivity$OnResumeTask;

    invoke-virtual {v6}, Lcom/autostart/AutoStartActivity$OnResumeTask;->myCancel()V

    .line 287
    :cond_0
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    invoke-virtual {v6}, Ljava/util/ArrayList;->clear()V

    .line 288
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->llApps:Landroid/widget/LinearLayout;

    invoke-virtual {v6}, Landroid/widget/LinearLayout;->removeAllViews()V

    .line 290
    invoke-super {v10}, Landroid/app/Activity;->onPause()V

    .line 291
    return-void

    .line 258
    .end local v2    # "edit":Landroid/content/SharedPreferences$Editor;
    .end local v5    # "prefs":Landroid/content/SharedPreferences;
    :cond_1
    if-nez v3, :cond_2

    .line 259
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    invoke-virtual {v6, v3}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-static {v6}, Lcom/autostart/AutoStartActivity$PInfo;->access$2(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v4

    .line 260
    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    invoke-virtual {v6, v3}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-static {v6}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v0

    .line 257
    :goto_2
    add-int/lit8 v3, v3, 0x1

    goto/16 :goto_0

    .line 262
    :cond_2
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-static {v4}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v7

    invoke-direct {v6, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    const-string v7, ";;"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    invoke-virtual {v6, v3}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-static {v6}, Lcom/autostart/AutoStartActivity$PInfo;->access$2(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v4

    .line 263
    new-instance v6, Ljava/lang/StringBuilder;

    invoke-static {v0}, Ljava/lang/String;->valueOf(Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v7

    invoke-direct {v6, v7}, Ljava/lang/StringBuilder;-><init>(Ljava/lang/String;)V

    const-string v7, ";;"

    invoke-virtual {v6, v7}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v7

    iget-object v6, v10, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    invoke-virtual {v6, v3}, Ljava/util/ArrayList;->get(I)Ljava/lang/Object;

    move-result-object v6

    check-cast v6, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-static {v6}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v6

    invoke-virtual {v7, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v6

    invoke-virtual {v6}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    goto :goto_2

    .line 277
    .restart local v2    # "edit":Landroid/content/SharedPreferences$Editor;
    .restart local v5    # "prefs":Landroid/content/SharedPreferences;
    :catch_0
    move-exception v1

    .line 278
    .local v1, "e":Ljava/lang/Exception;
    const-string v6, "startdelay"

    invoke-interface {v2, v6, v9}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    .line 279
    const-string v6, "inbetweendelay"

    const/4 v7, 0x3

    invoke-interface {v2, v6, v7}, Landroid/content/SharedPreferences$Editor;->putInt(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;

    goto/16 :goto_1
.end method

.method public onResume()V
    .locals 15

    const v14, 0x41

    invoke-static {v14}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object/from16 v13, p0

    .prologue
    const/4 v12, 0x1

    const/4 v11, 0x0

    .line 204
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->progressbar:Landroid/widget/ProgressBar;

    invoke-virtual {v8, v11}, Landroid/widget/ProgressBar;->setVisibility(I)V

    .line 205
    new-instance v8, Ljava/util/ArrayList;

    invoke-direct {v8}, Ljava/util/ArrayList;-><init>()V

    iput-object v8, v13, Lcom/autostart/AutoStartActivity;->newPackageList:Ljava/util/List;

    .line 206
    new-instance v8, Ljava/util/ArrayList;

    invoke-direct {v8}, Ljava/util/ArrayList;-><init>()V

    iput-object v8, v13, Lcom/autostart/AutoStartActivity;->newPackageList_all:Ljava/util/List;

    .line 207
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->cb_showAll:Landroid/widget/CheckBox;

    invoke-virtual {v8, v11}, Landroid/widget/CheckBox;->setChecked(Z)V

    .line 209
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->llAppSelect:Landroid/widget/LinearLayout;

    invoke-virtual {v8}, Landroid/widget/LinearLayout;->getVisibility()I

    move-result v8

    if-nez v8, :cond_0

    .line 210
    invoke-direct {v13, v12}, Lcom/autostart/AutoStartActivity;->showMainScreen(Z)V

    .line 211
    :cond_0
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->bt:Landroid/widget/Button;

    invoke-virtual {v8, v11}, Landroid/widget/Button;->setEnabled(Z)V

    .line 213
    invoke-virtual {v13}, Lcom/autostart/AutoStartActivity;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v8

    iput-object v8, v13, Lcom/autostart/AutoStartActivity;->pm:Landroid/content/pm/PackageManager;

    .line 214
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->pm:Landroid/content/pm/PackageManager;

    .line 215
    const/16 v9, 0x80

    invoke-virtual {v8, v9}, Landroid/content/pm/PackageManager;->getInstalledApplications(I)Ljava/util/List;

    move-result-object v8

    .line 214
    iput-object v8, v13, Lcom/autostart/AutoStartActivity;->packages:Ljava/util/List;

    .line 217
    new-instance v8, Lcom/autostart/AutoStartActivity$OnResumeTask;

    const/4 v9, 0x0

    invoke-direct {v8, v13, v9}, Lcom/autostart/AutoStartActivity$OnResumeTask;-><init>(Lcom/autostart/AutoStartActivity;Lcom/autostart/AutoStartActivity$OnResumeTask;)V

    iput-object v8, v13, Lcom/autostart/AutoStartActivity;->resumeTask:Lcom/autostart/AutoStartActivity$OnResumeTask;

    .line 218
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->resumeTask:Lcom/autostart/AutoStartActivity$OnResumeTask;

    new-array v9, v11, [Ljava/lang/Void;

    invoke-virtual {v8, v9}, Lcom/autostart/AutoStartActivity$OnResumeTask;->execute([Ljava/lang/Object;)Landroid/os/AsyncTask;

    .line 220
    invoke-virtual {v13}, Lcom/autostart/AutoStartActivity;->getApplicationContext()Landroid/content/Context;

    move-result-object v8

    const-string v9, "autostart"

    invoke-virtual {v8, v9, v11}, Landroid/content/Context;->getSharedPreferences(Ljava/lang/String;I)Landroid/content/SharedPreferences;

    move-result-object v5

    .line 221
    .local v5, "prefs":Landroid/content/SharedPreferences;
    const-string v8, "package"

    const-string v9, ""

    invoke-interface {v5, v8, v9}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    .line 222
    .local v3, "packageName":Ljava/lang/String;
    const-string v8, "class"

    const-string v9, ""

    invoke-interface {v5, v8, v9}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 223
    .local v0, "className":Ljava/lang/String;
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->cb:Landroid/widget/CheckBox;

    const-string v9, "noti"

    invoke-interface {v5, v9, v12}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v9

    invoke-virtual {v8, v9}, Landroid/widget/CheckBox;->setChecked(Z)V

    .line 224
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->cbGoTOHome:Landroid/widget/CheckBox;

    const-string v9, "gotohome"

    invoke-interface {v5, v9, v12}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v9

    invoke-virtual {v8, v9}, Landroid/widget/CheckBox;->setChecked(Z)V

    .line 225
    const-string v8, "enabled"

    invoke-interface {v5, v8, v11}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v1

    .line 226
    .local v1, "enabled":Z
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->etDelay:Landroid/widget/EditText;

    const-string v9, "startdelay"

    invoke-interface {v5, v9, v11}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v9

    invoke-static {v9}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Landroid/widget/EditText;->setText(Ljava/lang/CharSequence;)V

    .line 227
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->etDelayBetween:Landroid/widget/EditText;

    const-string v9, "inbetweendelay"

    const/4 v10, 0x3

    invoke-interface {v5, v9, v10}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v9

    invoke-static {v9}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v9

    invoke-virtual {v8, v9}, Landroid/widget/EditText;->setText(Ljava/lang/CharSequence;)V

    .line 229
    const-string v8, ""

    invoke-virtual {v3, v8}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v8

    if-nez v8, :cond_1

    const-string v8, ""

    invoke-virtual {v0, v8}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v8

    if-nez v8, :cond_1

    .line 230
    if-eqz v1, :cond_2

    .line 231
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->tb:Landroid/widget/ToggleButton;

    invoke-virtual {v8, v12}, Landroid/widget/ToggleButton;->setChecked(Z)V

    .line 235
    :goto_0
    const-string v8, ";;"

    invoke-virtual {v3, v8}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v7

    .line 236
    .local v7, "splitPackages":[Ljava/lang/String;
    const-string v8, ";;"

    invoke-virtual {v0, v8}, Ljava/lang/String;->split(Ljava/lang/String;)[Ljava/lang/String;

    move-result-object v6

    .line 237
    .local v6, "splitClasses":[Ljava/lang/String;
    const/4 v2, 0x0

    .local v2, "i":I
    :goto_1
    array-length v8, v7

    if-lt v2, v8, :cond_3

    .line 247
    .end local v2    # "i":I
    .end local v6    # "splitClasses":[Ljava/lang/String;
    .end local v7    # "splitPackages":[Ljava/lang/String;
    :cond_1
    invoke-super {v13}, Landroid/app/Activity;->onResume()V

    .line 248
    return-void

    .line 233
    :cond_2
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->tb:Landroid/widget/ToggleButton;

    invoke-virtual {v8, v11}, Landroid/widget/ToggleButton;->setChecked(Z)V

    goto :goto_0

    .line 238
    .restart local v2    # "i":I
    .restart local v6    # "splitClasses":[Ljava/lang/String;
    .restart local v7    # "splitPackages":[Ljava/lang/String;
    :cond_3
    new-instance v4, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-direct {v4, v13}, Lcom/autostart/AutoStartActivity$PInfo;-><init>(Lcom/autostart/AutoStartActivity;)V

    .line 239
    .local v4, "pi":Lcom/autostart/AutoStartActivity$PInfo;
    aget-object v8, v6, v2

    invoke-static {v4, v8}, Lcom/autostart/AutoStartActivity$PInfo;->access$4(Lcom/autostart/AutoStartActivity$PInfo;Ljava/lang/String;)V

    .line 240
    aget-object v8, v7, v2

    invoke-static {v4, v8}, Lcom/autostart/AutoStartActivity$PInfo;->access$5(Lcom/autostart/AutoStartActivity$PInfo;Ljava/lang/String;)V

    .line 241
    iget-object v8, v13, Lcom/autostart/AutoStartActivity;->appList:Ljava/util/ArrayList;

    invoke-virtual {v8, v4}, Ljava/util/ArrayList;->add(Ljava/lang/Object;)Z

    .line 243
    invoke-virtual {v13, v4}, Lcom/autostart/AutoStartActivity;->addToAppList(Lcom/autostart/AutoStartActivity$PInfo;)V

    .line 237
    add-int/lit8 v2, v2, 0x1

    goto :goto_1
.end method
