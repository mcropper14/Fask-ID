package com.example.facedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.facedetection.GraphicOverlay;

public class RectOverlay extends GraphicOverlay.Graphic {

    private int mRectColor = Color.RED;
    private float strokeWidth = 4.5f;
    private Paint rectPaint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;



    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);

        rectPaint = new Paint();
        rectPaint.setColor(mRectColor);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(strokeWidth);

        this.graphicOverlay = graphicOverlay;
        this.rect = rect;

        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(rect);
        rectF.left = translateX(rectF.left);
        rectF.right = translateX(rectF.right);
        rectF.top = translateX(rectF.top);
        rectF.bottom = translateX(rectF.bottom);

        canvas.drawRect(rectF, rectPaint);

    }
}
