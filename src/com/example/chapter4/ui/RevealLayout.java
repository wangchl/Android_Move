package com.example.chapter4.ui;

import java.util.ArrayList;

import com.example.developart.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 一个特殊的LinearLayout,任何放入内部的clickable元素都具有波纹效果，当它被点击的时候， 为了性能，尽量不要在内部放入复杂的元素
 * note: long click listener is not supported current for fix compatible bug.
 */
public class RevealLayout extends LinearLayout {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 去除锯齿
	private int[] mLocationInScreen = new int[2];

	private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();

	private View mTouchTarget;
	private float mCenterX;
	private float mCenterY;
	private int mTargetWidth;
	private int mTargetHeight;
	private int mMinBetweenWidthAndHeight;
	// private int mMaxBetweenWidthAndHeight;
	private int mMaxRevealRadius;
	private boolean mIsPressed = false;
	private boolean mShouldDoAnimation = false;
	private int mRevealRadius = 0;
	private int mRevealRadiusGap;

	public RevealLayout(Context context) {
		super(context);
		init();
	}

	public RevealLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RevealLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 不调用系统的绘图工具
		setWillNotDraw(false);
		// 绘制颜色
		mPaint.setColor(getResources().getColor(R.color.reveal_color));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// 获取View在x轴和y轴的绝对坐标
		this.getLocationOnScreen(mLocationInScreen);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {// 绘画
		super.dispatchDraw(canvas);
		if (!mShouldDoAnimation || mTargetWidth <= 0 || mTouchTarget == null) {
			return;
		}
		if (mRevealRadius > mMinBetweenWidthAndHeight / 2) {
			mRevealRadius += mRevealRadiusGap * 4;
		} else {
			mRevealRadius += mRevealRadiusGap;
		}

		this.getLocationOnScreen(mLocationInScreen);
		int[] location = new int[2];
		mTouchTarget.getLocationOnScreen(location);

		int left = location[0] - mLocationInScreen[0];
		int top = location[1] - mLocationInScreen[1];
		int right = left + mTouchTarget.getMeasuredWidth();
		int bottom = top + mTouchTarget.getMeasuredHeight();

		canvas.save();
		canvas.clipRect(left, top, right, bottom);
		canvas.drawCircle(mCenterX, mCenterY, mRevealRadius, mPaint);
		canvas.restore();

		if (mRevealRadius <= mMaxRevealRadius) {
			postInvalidateDelayed(40, left, top, right, bottom);
		} else if (!mIsPressed) {
			mShouldDoAnimation = false;
			postInvalidateDelayed(40, left, top, right, bottom);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {// 事件分发
		// 获取到系统屏幕的宽度和高度
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			View touchTarget = getTouchTarget(this, x, y);
			// 如果子布局可以聚焦,可以点击,可见.
			if (touchTarget != null && touchTarget.isClickable() && touchTarget.isEnabled()) {
				// 子布局定义为全局变量
				mTouchTarget = touchTarget;
				initParametersForChild(ev, touchTarget);
				postInvalidateDelayed(40);
			}
		} else if (action == MotionEvent.ACTION_UP) {
			mIsPressed = false;
			postInvalidateDelayed(40);
			mDispatchUpTouchEventRunnable.event = ev;
			postDelayed(mDispatchUpTouchEventRunnable, 400);
			return true;
		} else if (action == MotionEvent.ACTION_CANCEL) {
			mIsPressed = false;
			postInvalidateDelayed(40);
		}

		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 初始化子布局的参数
	 * 
	 * @param ev
	 *            事件
	 * @param touchTarget
	 *            子布局
	 */
	private void initParametersForChild(MotionEvent ev, View view) {
		// 子布局的横纵坐标
		mCenterX = ev.getX();
		mCenterY = ev.getY();
		// 子布局的宽度和高度
		mTargetWidth = view.getMeasuredWidth();
		mTargetHeight = view.getMeasuredHeight();
		// 子布局的最小值和最大值
		mMinBetweenWidthAndHeight = Math.min(mTargetWidth, mTargetHeight);
		// mMaxBetweenWidthAndHeight = Math.max(mTargetWidth, mTargetHeight);

		mRevealRadius = 0;
		mShouldDoAnimation = true;
		mIsPressed = true;
		mRevealRadiusGap = mMinBetweenWidthAndHeight / 8;

		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int left = location[0] - mLocationInScreen[0];
		int transformedCenterX = (int) (mCenterX) - left;
		mMaxRevealRadius = Math.max(transformedCenterX, mTargetWidth - transformedCenterX);
	}

	/**
	 * 
	 * @param view
	 *            子布局
	 * @param x
	 *            系统的x轴
	 * @param y
	 *            系统的y轴
	 * @return 如果子布局可以聚焦,则返回当前子布局,否则返回null.
	 */
	private View getTouchTarget(RevealLayout view, int x, int y) {
		View target = null;
		ArrayList<View> touchables = view.getTouchables();
		for (View child : touchables) {
			if (isTouchPointInView(child, x, y)) {
				target = child;
				break;
			}
		}
		return target;
	}

	/**
	 * 判断view是否可以聚焦
	 * 
	 * @param view
	 *            子布局
	 * @param x
	 *            系统的x轴
	 * @param y
	 *            系统的y轴
	 * @return 如果子布局可点击,并且在子容器中,则返回true,否则返回 false.
	 */
	private boolean isTouchPointInView(View view, int x, int y) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int left = location[0];
		int top = location[1];
		int right = left + view.getMeasuredWidth();
		int bottom = top + view.getMeasuredHeight();
		if (view.isClickable() && y >= top && y <= bottom && x >= left && x <= right) {
			return true;
		}
		return false;
	}

	private class DispatchUpTouchEventRunnable implements Runnable {
		public MotionEvent event;

		@Override
		public void run() {
			if (mTouchTarget == null || !mTouchTarget.isEnabled()) {
				return;
			}

			if (isTouchPointInView(mTouchTarget, (int) event.getRawX(), (int) event.getRawY())) {
				mTouchTarget.performClick();
			}
		}
	};

}
