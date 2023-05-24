.class Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;
.super Landroid/widget/BaseAdapter;
.source "AutoStartActivity.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/autostart/AutoStartActivity;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x2
    name = "IconifiedTextListAdapter"
.end annotation


# instance fields
.field private mContext:Landroid/content/Context;

.field private mItems:Ljava/util/List;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Ljava/util/List",
            "<",
            "Lcom/autostart/AutoStartActivity$PInfo;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic this$0:Lcom/autostart/AutoStartActivity;


# direct methods
.method public constructor <init>(Lcom/autostart/AutoStartActivity;Landroid/content/Context;Ljava/util/List;)V
    .locals 5
    .param p2, "context"    # Landroid/content/Context;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/content/Context;",
            "Ljava/util/List",
            "<",
            "Lcom/autostart/AutoStartActivity$PInfo;",
            ">;)V"
        }
    .end annotation

    const v4, 0xc

    invoke-static {v4}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v3, p3

    move-object v2, p2

    move-object v1, p1

    move-object v0, p0

    .prologue
    .line 337
    .local v3, "items":Ljava/util/List;, "Ljava/util/List<Lcom/autostart/AutoStartActivity$PInfo;>;"
    iput-object v1, v0, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-direct {v0}, Landroid/widget/BaseAdapter;-><init>()V

    .line 338
    iput-object v2, v0, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->mContext:Landroid/content/Context;

    .line 339
    iput-object v3, v0, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->mItems:Ljava/util/List;

    .line 340
    return-void
.end method


# virtual methods
.method public getCount()I
    .locals 4

    const v3, 0xd

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p0

    .prologue
    .line 347
    :try_start_0
    iget-object v1, v2, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->mItems:Ljava/util/List;

    invoke-interface {v1}, Ljava/util/List;->size()I
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    move-result v1

    .line 348
    :goto_0
    return v1

    :catch_0
    move-exception v0

    .local v0, "e":Ljava/lang/Exception;
    const/4 v1, 0x0

    goto :goto_0
.end method

.method public getItem(I)Ljava/lang/Object;
    .locals 4
    .param p1, "position"    # I

    const v3, 0xe

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move v2, p1

    move-object v1, p0

    .prologue
    .line 351
    iget-object v0, v1, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->mItems:Ljava/util/List;

    invoke-interface {v0, v2}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v0

    return-object v0
.end method

.method public getItemId(I)J
    .locals 5
    .param p1, "position"    # I

    const v4, 0xf

    invoke-static {v4}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move v3, p1

    move-object v2, p0

    .prologue
    .line 355
    int-to-long v0, v3

    return-wide v0
.end method

.method public getView(ILandroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    .locals 13
    .param p1, "position"    # I
    .param p2, "convertView"    # Landroid/view/View;
    .param p3, "parent"    # Landroid/view/ViewGroup;

    const v12, 0x10

    invoke-static {v12}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object/from16 v11, p3

    move-object/from16 v10, p2

    move v9, p1

    move-object v8, p0

    .prologue
    .line 362
    if-nez v10, :cond_1

    .line 363
    new-instance v0, Lcom/autostart/AutoStartActivity$IconifiedTextView;

    iget-object v4, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->this$0:Lcom/autostart/AutoStartActivity;

    iget-object v5, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->mContext:Landroid/content/Context;

    iget-object v3, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->mItems:Ljava/util/List;

    invoke-interface {v3, v9}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Lcom/autostart/AutoStartActivity$PInfo;

    invoke-direct {v0, v4, v5, v3}, Lcom/autostart/AutoStartActivity$IconifiedTextView;-><init>(Lcom/autostart/AutoStartActivity;Landroid/content/Context;Lcom/autostart/AutoStartActivity$PInfo;)V

    .line 381
    .local v0, "btv":Lcom/autostart/AutoStartActivity$IconifiedTextView;
    :cond_0
    :goto_0
    return-object v0

    .line 365
    .end local v0    # "btv":Lcom/autostart/AutoStartActivity$IconifiedTextView;
    :cond_1
    iget-object v3, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->mItems:Ljava/util/List;

    invoke-interface {v3, v9}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Lcom/autostart/AutoStartActivity$PInfo;

    .local v2, "pi":Lcom/autostart/AutoStartActivity$PInfo;
    move-object v0, v10

    .line 366
    check-cast v0, Lcom/autostart/AutoStartActivity$IconifiedTextView;

    .line 367
    .restart local v0    # "btv":Lcom/autostart/AutoStartActivity$IconifiedTextView;
    invoke-virtual {v0}, Lcom/autostart/AutoStartActivity$IconifiedTextView;->getText()Ljava/lang/String;

    move-result-object v3

    invoke-static {v2}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v3

    if-nez v3, :cond_0

    .line 368
    invoke-static {v2}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v0, v3}, Lcom/autostart/AutoStartActivity$IconifiedTextView;->setText(Ljava/lang/String;)V

    .line 369
    iget-object v3, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$0(Lcom/autostart/AutoStartActivity;)Landroid/support/v4/util/LruCache;

    move-result-object v3

    invoke-static {v2}, Lcom/autostart/AutoStartActivity$PInfo;->access$2(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Landroid/support/v4/util/LruCache;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/graphics/drawable/Drawable;

    .line 370
    .local v1, "d":Landroid/graphics/drawable/Drawable;
    if-eqz v1, :cond_2

    .line 371
    invoke-virtual {v0, v1}, Lcom/autostart/AutoStartActivity$IconifiedTextView;->setIcon(Landroid/graphics/drawable/Drawable;)V

    goto :goto_0

    .line 375
    :cond_2
    :try_start_0
    iget-object v3, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$0(Lcom/autostart/AutoStartActivity;)Landroid/support/v4/util/LruCache;

    move-result-object v3

    invoke-static {v2}, Lcom/autostart/AutoStartActivity$PInfo;->access$2(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v4

    iget-object v5, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v2}, Lcom/autostart/AutoStartActivity$PInfo;->access$3(Lcom/autostart/AutoStartActivity$PInfo;)Landroid/content/pm/ApplicationInfo;

    move-result-object v6

    iget-object v7, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-virtual {v7}, Lcom/autostart/AutoStartActivity;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v7

    invoke-virtual {v6, v7}, Landroid/content/pm/ApplicationInfo;->loadIcon(Landroid/content/pm/PackageManager;)Landroid/graphics/drawable/Drawable;

    move-result-object v6

    invoke-static {v5, v6}, Lcom/autostart/AutoStartActivity;->access$1(Lcom/autostart/AutoStartActivity;Landroid/graphics/drawable/Drawable;)Landroid/graphics/drawable/Drawable;

    move-result-object v5

    invoke-virtual {v3, v4, v5}, Landroid/support/v4/util/LruCache;->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;

    .line 376
    iget-object v3, v8, Lcom/autostart/AutoStartActivity$IconifiedTextListAdapter;->this$0:Lcom/autostart/AutoStartActivity;

    invoke-static {v3}, Lcom/autostart/AutoStartActivity;->access$0(Lcom/autostart/AutoStartActivity;)Landroid/support/v4/util/LruCache;

    move-result-object v3

    invoke-static {v2}, Lcom/autostart/AutoStartActivity$PInfo;->access$2(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v3, v4}, Landroid/support/v4/util/LruCache;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/graphics/drawable/Drawable;

    invoke-virtual {v0, v3}, Lcom/autostart/AutoStartActivity$IconifiedTextView;->setIcon(Landroid/graphics/drawable/Drawable;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    goto :goto_0

    .line 377
    :catch_0
    move-exception v3

    goto :goto_0
.end method
