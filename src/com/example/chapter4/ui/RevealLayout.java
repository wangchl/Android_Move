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
 * һ�������LinearLayout,�κη����ڲ���clickableԪ�ض����в���Ч���������������ʱ�� Ϊ�����ܣ�������Ҫ���ڲ����븴�ӵ�Ԫ��
 * note: long click listener is not supported current for fix compatible bug.
 */
public class RevealLayout extends LinearLayout {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// ȥ�����
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
		// ������ϵͳ�Ļ�ͼ����
		setWillNotDraw(false);
		// ������ɫ
		mPaint.setColor(getResources().getColor(R.color.reveal_color));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// ��ȡView��x���y��ľ�������
		this.getLocationOnScreen(mLocationInScreen);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {// �滭
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
	public boolean dispatchTouchEvent(MotionEvent ev) {// �¼��ַ�
		// ��ȡ��ϵͳ��Ļ�Ŀ�Ⱥ͸߶�
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			View touchTarget = getTouchTarget(this, x, y);
			// ����Ӳ��ֿ��Ծ۽�,���Ե��,�ɼ�.
			if (touchTarget != null && touchTarget.isClickable() && touchTarget.isEnabled()) {
				// �Ӳ��ֶ���Ϊȫ�ֱ���
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
	 * ��ʼ���Ӳ��ֵĲ���
	 * 
	 * @param ev
	 *            �¼�
	 * @param touchTarget
	 *            �Ӳ���
	 */
	private void initParametersForChild(MotionEvent ev, View view) {
		// �Ӳ��ֵĺ�������
		mCenterX = ev.getX();
		mCenterY = ev.getY();
		// �Ӳ��ֵĿ�Ⱥ͸߶�
		mTargetWidth = view.getMeasuredWidth();
		mTargetHeight = view.getMeasuredHeight();
		// �Ӳ��ֵ���Сֵ�����ֵ
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
	 *            �Ӳ���
	 * @param x
	 *            ϵͳ��x��
	 * @param y
	 *            ϵͳ��y��
	 * @return ����Ӳ��ֿ��Ծ۽�,�򷵻ص�ǰ�Ӳ���,���򷵻�null.
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
	 * �ж�view�Ƿ���Ծ۽�
	 * 
	 * @param view
	 *            �Ӳ���
	 * @param x
	 *            ϵͳ��x��
	 * @param y
	 *            ϵͳ��y��
	 * @return ����Ӳ��ֿɵ��,��������������,�򷵻�true,���򷵻� false.
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
