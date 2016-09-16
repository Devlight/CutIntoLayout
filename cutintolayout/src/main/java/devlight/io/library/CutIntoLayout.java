/*
 * Copyright (C) 2015 Basil Miller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package devlight.io.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 21.06.2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CutIntoLayout extends FrameLayout {

    // Paint flags
    private final static int FLAGS =
            Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG;

    // View bounds
    private final RectF mBounds = new RectF();
    // Cut into paint
    private final Paint mPaint = new Paint(FLAGS) {
        {
            setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        }
    };

    // Child screenshot
    private Bitmap mChildBitmap;
    // Child coordinate
    private int mChildLeft;
    private int mChildTop;

    // Masks
    private Canvas mMaskCanvas = new Canvas();
    private Bitmap mMaskBitmap;
    private Drawable mMask;

    public CutIntoLayout(Context context) {
        this(context, null);
    }

    public CutIntoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutIntoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CutIntoLayout);
        try {
            setMask(typedArray.getDrawable(R.styleable.CutIntoLayout_cil_mask));
        } finally {
            typedArray.recycle();
        }
    }

    public Drawable getMask() {
        return mMask;
    }

    public void setMask(final Drawable mask) {
        mMask = mask;
        requestLayout();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBounds.set(0.0F, 0.0F, getMeasuredWidth(), getMeasuredHeight());

        mMaskBitmap = Bitmap.createBitmap(
                (int) mBounds.width(), (int) mBounds.height(), Bitmap.Config.ARGB_8888
        );
        mMaskCanvas.setBitmap(mMaskBitmap);

        // Set mask bounds relative to view bounds
        mMask.setBounds(0, 0, (int) mBounds.width(), (int) mBounds.height());
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        // Check for availability
        if (mMask == null || mChildBitmap == null) return;

        // Clear
        mMaskCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // Draw background mask
        mMask.draw(mMaskCanvas);
        // Cut into view
        mMaskCanvas.drawBitmap(mChildBitmap, mChildLeft, mChildTop, mPaint);

        canvas.drawBitmap(mMaskBitmap, 0.0F, 0.0F, null);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getChildCount() > 1)
            throw new IllegalArgumentException(getResources().getString(R.string.child_exception));

        final View child = getChildAt(0);

        child.setVisibility(VISIBLE);
        child.setDrawingCacheEnabled(true);
        child.buildDrawingCache();

        final Bitmap drawingCache = child.getDrawingCache();
        if (drawingCache == null) return;

        // Obtain child screenshot
        mChildBitmap = Bitmap.createBitmap(drawingCache);
        drawingCache.recycle();

        // Obtain child offset
        mChildLeft = child.getLeft();
        mChildTop = child.getTop();

        child.setDrawingCacheEnabled(false);
        child.setVisibility(GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mChildBitmap.recycle();
        mChildBitmap = null;

        mMaskCanvas.setBitmap(null);
        mMaskBitmap.recycle();
        mMaskBitmap = null;
    }
}
