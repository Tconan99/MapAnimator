package com.jc.android.mapanimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by tconan on 2017/12/20.
 */

public class MapAnimatorView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap = null;
    Camera camera = new Camera();


    AnimatorSet animator = new AnimatorSet();

    int degree = 0;
    ObjectAnimator degreeAnimator = ObjectAnimator.ofInt(this, "degree", 0, -60);
    ObjectAnimator degreeAnimator2 = ObjectAnimator.ofInt(this, "degree", -60, 0);

    int pos = 0;
    ObjectAnimator posAnimator = ObjectAnimator.ofInt(this, "pos", 0, 270);


    public MapAnimatorView(Context context) {
        super(context);
    }

    public MapAnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.maps);

        degreeAnimator.setDuration(1000);
        degreeAnimator.setStartDelay(500);
        degreeAnimator.setInterpolator(new LinearInterpolator());

        posAnimator.setDuration(1000);
        posAnimator.setStartDelay(500);
        posAnimator.setInterpolator(new LinearInterpolator());

        degreeAnimator2.setDuration(500);
        degreeAnimator2.setInterpolator(new LinearInterpolator());

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float newZ = - displayMetrics.density * 6;
        camera.setLocation(0, 0, newZ);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                degree = 0;
                pos = 0;
                animator.start();
            }
        });

        animator.playSequentially(degreeAnimator, posAnimator, degreeAnimator2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    public void setPos(int pos) {
        this.pos = pos;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;


        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-pos);
        canvas.clipRect(-centerX, -centerY, 0, centerY);
        camera.save();
        camera.applyToCanvas(canvas);
        canvas.rotate(pos);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-pos);
        canvas.clipRect(0, -centerY, centerX, centerY);
        camera.save();
        camera.rotateY(degree);
        camera.applyToCanvas(canvas);
        canvas.rotate(pos);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

    }
}
