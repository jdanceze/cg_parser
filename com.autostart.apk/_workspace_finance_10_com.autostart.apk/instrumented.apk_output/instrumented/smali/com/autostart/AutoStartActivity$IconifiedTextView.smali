.class Lcom/autostart/AutoStartActivity$IconifiedTextView;
.super Landroid/widget/LinearLayout;
.source "AutoStartActivity.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lcom/autostart/AutoStartActivity;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x2
    name = "IconifiedTextView"
.end annotation


# instance fields
.field private mIcon:Landroid/widget/ImageView;

.field private mText:Landroid/widget/TextView;

.field final synthetic this$0:Lcom/autostart/AutoStartActivity;


# direct methods
.method public constructor <init>(Lcom/autostart/AutoStartActivity;Landroid/content/Context;Lcom/autostart/AutoStartActivity$PInfo;)V
    .locals 13
    .param p2, "context"    # Landroid/content/Context;
    .param p3, "appInfo"    # Lcom/autostart/AutoStartActivity$PInfo;

    const v12, 0x11

    invoke-static {v12}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object/from16 v11, p3

    move-object/from16 v10, p2

    move-object v9, p1

    move-object v8, p0

    .prologue
    const/4 v7, 0x0

    const/4 v6, -0x2

    const/16 v5, 0xa

    .line 391
    iput-object v9, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->this$0:Lcom/autostart/AutoStartActivity;

    .line 392
    invoke-direct {v8, v10}, Landroid/widget/LinearLayout;-><init>(Landroid/content/Context;)V

    .line 396
    invoke-virtual {v8, v7}, Lcom/autostart/AutoStartActivity$IconifiedTextView;->setOrientation(I)V

    .line 398
    new-instance v2, Landroid/widget/ImageView;

    invoke-direct {v2, v10}, Landroid/widget/ImageView;-><init>(Landroid/content/Context;)V

    iput-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mIcon:Landroid/widget/ImageView;

    .line 399
    iget-object v3, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mIcon:Landroid/widget/ImageView;

    invoke-static {v9}, Lcom/autostart/AutoStartActivity;->access$0(Lcom/autostart/AutoStartActivity;)Landroid/support/v4/util/LruCache;

    move-result-object v2

    invoke-static {v11}, Lcom/autostart/AutoStartActivity$PInfo;->access$2(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v2, v4}, Landroid/support/v4/util/LruCache;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/graphics/drawable/Drawable;

    invoke-virtual {v3, v2}, Landroid/widget/ImageView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    .line 401
    iget-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mIcon:Landroid/widget/ImageView;

    const/4 v3, 0x3

    const/4 v4, 0x5

    invoke-virtual {v2, v3, v5, v4, v5}, Landroid/widget/ImageView;->setPadding(IIII)V

    .line 405
    const/high16 v2, 0x42900000    # 72.0f

    invoke-static {v9}, Lcom/autostart/AutoStartActivity;->access$2(Lcom/autostart/AutoStartActivity;)F

    move-result v3

    mul-float/2addr v2, v3

    const/high16 v3, 0x3f000000    # 0.5f

    add-float/2addr v2, v3

    float-to-int v0, v2

    .line 406
    .local v0, "IconSize":I
    iget-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mIcon:Landroid/widget/ImageView;

    new-instance v3, Landroid/widget/LinearLayout$LayoutParams;

    .line 407
    invoke-direct {v3, v0, v0}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 406
    invoke-virtual {v8, v2, v3}, Lcom/autostart/AutoStartActivity$IconifiedTextView;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 409
    new-instance v2, Landroid/widget/TextView;

    invoke-direct {v2, v10}, Landroid/widget/TextView;-><init>(Landroid/content/Context;)V

    iput-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    .line 410
    iget-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    const/4 v3, 0x1

    const/16 v4, 0x8

    invoke-virtual {v2, v3, v4, v7, v5}, Landroid/widget/TextView;->setPadding(IIII)V

    .line 411
    iget-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    invoke-static {v11}, Lcom/autostart/AutoStartActivity$PInfo;->access$1(Lcom/autostart/AutoStartActivity$PInfo;)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 413
    new-instance v1, Landroid/widget/LinearLayout$LayoutParams;

    invoke-direct {v1, v6, v6}, Landroid/widget/LinearLayout$LayoutParams;-><init>(II)V

    .line 415
    .local v1, "paramsText":Landroid/widget/LinearLayout$LayoutParams;
    const/16 v2, 0x10

    iput v2, v1, Landroid/widget/LinearLayout$LayoutParams;->gravity:I

    .line 416
    iget-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    invoke-virtual {v8, v2, v1}, Lcom/autostart/AutoStartActivity$IconifiedTextView;->addView(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V

    .line 417
    iget-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    const/high16 v3, 0x41b00000    # 22.0f

    invoke-virtual {v2, v3}, Landroid/widget/TextView;->setTextSize(F)V

    .line 418
    iget-object v2, v8, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    const/4 v3, -0x1

    invoke-virtual {v2, v3}, Landroid/widget/TextView;->setTextColor(I)V

    .line 419
    return-void
.end method


# virtual methods
.method public getText()Ljava/lang/String;
    .locals 3

    const v2, 0x12

    invoke-static {v2}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v1, p0

    .prologue
    .line 426
    iget-object v0, v1, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    invoke-virtual {v0}, Landroid/widget/TextView;->getText()Ljava/lang/CharSequence;

    move-result-object v0

    invoke-interface {v0}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v0

    return-object v0
.end method

.method public setIcon(Landroid/graphics/drawable/Drawable;)V
    .locals 4
    .param p1, "bullet"    # Landroid/graphics/drawable/Drawable;

    const v3, 0x13

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 430
    iget-object v0, v1, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mIcon:Landroid/widget/ImageView;

    invoke-virtual {v0, v2}, Landroid/widget/ImageView;->setImageDrawable(Landroid/graphics/drawable/Drawable;)V

    .line 431
    return-void
.end method

.method public setText(Ljava/lang/String;)V
    .locals 4
    .param p1, "words"    # Ljava/lang/String;

    const v3, 0x14

    invoke-static {v3}, Lcom/apposcopy/ella/runtime/Ella;->m(I)V

    move-object v2, p1

    move-object v1, p0

    .prologue
    .line 422
    iget-object v0, v1, Lcom/autostart/AutoStartActivity$IconifiedTextView;->mText:Landroid/widget/TextView;

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 423
    return-void
.end method
