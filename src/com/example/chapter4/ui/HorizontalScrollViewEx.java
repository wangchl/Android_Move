package com.example.chapter4.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizontalScrollViewEx extends ViewGroup {

	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	public HorizontalScrollViewEx(Context context) {
		super(context);
		init();
	}

	public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		if (mScroller == null) {
			mScroller = new Scroller(getContext());
			mVelocityTracker = VelocityTracker.obtain();
		}
	}

	private int mLastXIntercepted = 0;
	private int mLastYIntercepted = 0;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean intercepted = false;
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			intercepted = false;
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
				intercepted = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = x - mLastXIntercepted;
			int deltaY = y - mLastYIntercepted;
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				intercepted = true;
			} else {
				intercepted = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			intercepted = false;
			break;
		default:
			break;
		}
		mLastXIntercepted = x;
		mLastYIntercepted = y;
		return intercepted;
	}

	/**
	 * �Ӳ�����HorizontalScrollViewEx�е�λ��,ˮƽ�������
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int childCount = getChildCount();
		mChildrenSize = childCount;

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				mChildWidth = childWidth;
				childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	/**
	 * ������wrap_content����ʱ,���HorizontalScrollViewEx���Ӳ��ָ���Ϊ0,��ô��Ⱥ͸߶ȶ�Ϊ0.
	 * ����Ⱥ͸߶ȶ�wrap_contentʱ,�߶��Ե�һ���Ӳ��ֵĸ߶�Ϊ׼,����Ե�һ���Ӳ��ֵĿ�ȳ����Ӳ��ֵĸ���Ϊ׼.
	 * �����wrap_contentʱ,�߶������õĸ߶�Ϊ׼,����Ե�һ���Ӳ��ֵĿ�ȳ����Ӳ��ֵĸ���Ϊ׼.
	 * ���߶�wrap_contentʱ,�߶��Ե�һ���Ӳ��ֵĸ߶�Ϊ׼,��������õĿ��Ϊ׼.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int measuredWidth = 0;
		int measuredHeight = 0;
		final int childCount = getChildCount();
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		if (childCount == 0) {
			setMeasuredDimension(0, 0);
		} else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
			final View childView = getChildAt(0);
			measuredWidth = childView.getMeasuredWidth() * childCount;
			measuredHeight = childView.getMeasuredHeight();
			setMeasuredDimension(measuredWidth, measuredHeight);
		} else if (heightSpecMode == MeasureSpec.AT_MOST) {
			final View childView = getChildAt(0);
			measuredHeight = childView.getMeasuredHeight();
			setMeasuredDimension(widthSpaceSize, childView.getMeasuredHeight());
		} else if (widthSpecMode == MeasureSpec.AT_MOST) {
			final View childView = getChildAt(0);
			measuredWidth = childView.getMeasuredWidth() * childCount;
			setMeasuredDimension(measuredWidth, heightSpaceSize);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {// ��ָ������Ļ��ʱ����õķ���
		mVelocityTracker.addMovement(event);// ���¼���ӵ��˶�׷������
		int x = (int) event.getX();// ������ȵĺ�����
		//int y = (int) event.getY();// �����߶ȵ�������
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {// ��ָ������Ļ��˲��
			if (!mScroller.isFinished()) {// ����δ�ر�
				mScroller.abortAnimation();// �رն���
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {// ��ָ������ʱ��
			int deltaX = x - mLastX;// ˮƽ����ľ���
			// int deltaY = y - mLastY;
			scrollBy(-deltaX, 0);// ˮƽ�����ƶ���Ĺ�����ͼ
			break;
		}
		case MotionEvent.ACTION_UP: {// ��ָ�뿪��Ļ��˲��
			int scrollX = getScrollX();
			// ����һ����x����ٶ�
			mVelocityTracker.computeCurrentVelocity(1000);
			float xVelocity = mVelocityTracker.getXVelocity();
			if (Math.abs(xVelocity) >= 50) {// ����ٶȴ���0.05��
				// �������󻬶�(��),����+1. ��֮,����-1.
				mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
			} else {// �ٶ�С��0.05��
				mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;// ��������ֵ
			}
			// ������0�����һҳ
			mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));
			int dx = mChildIndex * mChildWidth - scrollX;
			smoothScrollBy(dx, 0);
			mVelocityTracker.clear();
			break;
		}
		default:
			break;
		}

		mLastX = x;
		//mLastY = y;
		return true;
	}

	private int mChildrenSize;
	private int mChildWidth;
	private int mChildIndex;
	// �ֱ��¼�ϴλ���������
	private int mLastX = 0;
	//private int mLastY = 0;

	private void smoothScrollBy(int dx, int dy) {
		mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
		invalidate();
	}

	@Override
	public void computeScroll() {
		// �ж��Ƿ��ڹ���,����ڹ���,����true.
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// ���½���
			postInvalidate();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		mVelocityTracker.recycle();
		super.onDetachedFromWindow();
	}

}
