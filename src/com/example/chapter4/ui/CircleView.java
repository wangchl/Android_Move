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
 * �Զ���View�ĵ�һ�ַ�ʽ,��дonDraw����.margin���ɸ��ؼ����õ�padding�ɵ�ǰ���Ծ���
 */
public class CircleView extends View {

	private int mColor = Color.RED;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// ����Paintʱֱ�Ӽ���ȥ�������

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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// ���wrap_content��������,����wrap_content��Ĭ��ֵΪ200
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
	protected void onDraw(Canvas canvas) {// ���padding���Բ�������
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
