package com.example.chapter4.ui;

import com.example.developart.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义View的第一种方式,重写onDraw方法.margin是由父控件觉得的padding由当前属性决定
 */
public class CircleView extends View {

	private int mColor = Color.RED;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 构建Paint时直接加上去锯齿属性

	public CircleView(Context context) {
		super(context);
		init();
	}

	public CircleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
		mColor = a.getColor(R.styleable.CircleView_circle_color, Color.RED);
		a.recycle();
		init();
	}

	private void init() {
		mPaint.setColor(mColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// 针对wrap_content不起作用,设置wrap_content的默认值为200
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(200, 200);
		} else if (widthSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(200, heightSpecSize);
		} else if (heightSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(widthSpecSize, 200);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {// 针对padding属性不起作用
		super.onDraw(canvas);
		final int paddingLeft = getPaddingLeft();
		final int paddingTop = getPaddingTop();
		final int paddingRight = getPaddingRight();
		final int paddingBottom = getPaddingBottom();
		int width = getWidth() - paddingLeft - paddingRight;
		int height = getHeight() - paddingTop - paddingBottom;
		int radius = Math.min(width, height) / 2;
		canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
	}
}
