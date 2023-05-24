package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class PrintHelperKitkat {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    final Context mContext;
    int mScaleMode = 2;
    int mColorMode = 2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PrintHelperKitkat(Context context) {
        this.mContext = context;
    }

    public void setScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setColorMode(int colorMode) {
        this.mColorMode = colorMode;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public void printBitmap(final String jobName, final Bitmap bitmap) {
        if (bitmap != null) {
            final int fittingMode = this.mScaleMode;
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            PrintAttributes.MediaSize mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
            }
            PrintAttributes attr = new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
            printManager.print(jobName, new PrintDocumentAdapter() { // from class: android.support.v4.print.PrintHelperKitkat.1
                private PrintAttributes mAttributes;

                @Override // android.print.PrintDocumentAdapter
                public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                    this.mAttributes = newPrintAttributes;
                    PrintDocumentInfo info = new PrintDocumentInfo.Builder(jobName).setContentType(1).setPageCount(1).build();
                    boolean changed = newPrintAttributes.equals(oldPrintAttributes) ? false : true;
                    layoutResultCallback.onLayoutFinished(info, changed);
                }

                @Override // android.print.PrintDocumentAdapter
                public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                    float scale;
                    PrintedPdfDocument pdfDocument = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
                    try {
                        PdfDocument.Page page = pdfDocument.startPage(1);
                        RectF content = new RectF(page.getInfo().getContentRect());
                        Matrix matrix = new Matrix();
                        float scale2 = content.width() / bitmap.getWidth();
                        if (fittingMode == 2) {
                            scale = Math.max(scale2, content.height() / bitmap.getHeight());
                        } else {
                            scale = Math.min(scale2, content.height() / bitmap.getHeight());
                        }
                        matrix.postScale(scale, scale);
                        float translateX = (content.width() - (bitmap.getWidth() * scale)) / 2.0f;
                        float translateY = (content.height() - (bitmap.getHeight() * scale)) / 2.0f;
                        matrix.postTranslate(translateX, translateY);
                        page.getCanvas().drawBitmap(bitmap, matrix, null);
                        pdfDocument.finishPage(page);
                        try {
                            pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
                            writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                        } catch (IOException ioe) {
                            Log.e(PrintHelperKitkat.LOG_TAG, "Error writing printed content", ioe);
                            writeResultCallback.onWriteFailed(null);
                        }
                        if (pdfDocument != null) {
                            pdfDocument.close();
                        }
                        if (fileDescriptor != null) {
                            try {
                                fileDescriptor.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (Throwable th) {
                        if (pdfDocument != null) {
                            pdfDocument.close();
                        }
                        if (fileDescriptor != null) {
                            try {
                                fileDescriptor.close();
                            } catch (IOException e2) {
                            }
                        }
                        throw th;
                    }
                }
            }, attr);
        }
    }

    public void printBitmap(String jobName, Uri imageFile) throws FileNotFoundException {
        Bitmap bitmap = loadConstrainedBitmap(imageFile, MAX_PRINT_SIZE);
        printBitmap(jobName, bitmap);
    }

    private Bitmap loadConstrainedBitmap(Uri uri, int maxSideLength) throws FileNotFoundException {
        if (maxSideLength <= 0 || uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        loadBitmap(uri, opt);
        int w = opt.outWidth;
        int h = opt.outHeight;
        if (w <= 0 || h <= 0) {
            return null;
        }
        int imageSide = Math.max(w, h);
        int sampleSize = 1;
        while (imageSide > maxSideLength) {
            imageSide >>>= 1;
            sampleSize <<= 1;
        }
        if (sampleSize <= 0 || Math.min(w, h) / sampleSize <= 0) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = sampleSize;
        return loadBitmap(uri, options);
    }

    private Bitmap loadBitmap(Uri uri, BitmapFactory.Options o) throws FileNotFoundException {
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        InputStream is = null;
        try {
            is = this.mContext.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(is, null, o);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException t) {
                    Log.w(LOG_TAG, "close fail ", t);
                }
            }
        }
    }
}
