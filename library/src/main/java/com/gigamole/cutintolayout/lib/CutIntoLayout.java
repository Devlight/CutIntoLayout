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

package com.gigamole.cutintolayout.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by GIGAMOLE on 21.06.2015.
 */
public class CutIntoLayout extends FrameLayout {

    private int width;
    private int height;

    private Canvas canvas;
    private Bitmap bitmap;

    private Bitmap childBitmap;

    private int left;
    private int top;

    private boolean isChildGet;
    private boolean isGet;

    private final Matrix matrix = new Matrix();
    private final PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);

    private Paint cutIntoPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setAntiAlias(true);
            setXfermode(porterDuffXfermode);
        }
    };

    public CutIntoLayout(Context context) {
        this(context, null);
    }

    public CutIntoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutIntoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = MeasureSpec.getSize(widthMeasureSpec);
        this.height = MeasureSpec.getSize(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        if (this.isGet && this.isChildGet) {
            this.canvas.drawBitmap(this.childBitmap, this.left, this.top, this.cutIntoPaint);
            canvas.drawBitmap(this.bitmap, this.matrix, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getChildCount() == 1) {
            getChildBitmap();
            getBitmap();
        } else {
            throw new IllegalArgumentException(getResources().getString(R.string.child_exception));
        }
    }

    private void getBitmap() {
        this.bitmap = drawableToBitmap(getBackground());
        this.canvas = new Canvas(this.bitmap);

        this.isGet = true;
    }

    private void getChildBitmap() {
        final View child = getChildAt(0);

        this.left = child.getLeft();
        this.top = child.getTop();

        child.setDrawingCacheEnabled(true);
        child.buildDrawingCache();
        this.childBitmap = Bitmap.createBitmap(child.getDrawingCache());
        child.setDrawingCacheEnabled(false);

        child.setVisibility(GONE);

        this.isChildGet = true;
    }

    private Bitmap drawableToBitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final Bitmap bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
