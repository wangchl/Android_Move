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
	 * 子布局在HorizontalScrollViewEx中的位置,水平依次添加
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
	 * 当设置wrap_content属性时,如果HorizontalScrollViewEx的子布局个数为0,那么宽度和高度都为0.
	 * 当宽度和高度都wrap_content时,高度以第一个子布局的高度为准,宽度以第一个子布局的宽度乘以子布局的个数为准.
	 * 当宽度wrap_content时,高度以设置的高度为准,宽度以第一个子布局的宽度乘以子布局的个数为准.
	 * 当高度wrap_content时,高度以第一个子布局的高度为准,宽度以设置的宽度为准.
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
	public boolean onTouchEvent(MotionEvent event) {// 手指触碰屏幕的时候调用的方法
		mVelocityTracker.addMovement(event);// 将事件添加到运动追踪器中
		int x = (int) event.getX();// 滑动宽度的横坐标
		//int y = (int) event.getY();// 滑动高度的纵坐标
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {// 手指按下屏幕的瞬间
			if (!mScroller.isFinished()) {// 滚动未关闭
				mScroller.abortAnimation();// 关闭动画
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {// 手指滑动的时候
			int deltaX = x - mLastX;// 水平方向的距离
			// int deltaY = y - mLastY;
			scrollBy(-deltaX, 0);// 水平方向移动你的滚动视图
			break;
		}
		case MotionEvent.ACTION_UP: {// 手指离开屏幕的瞬间
			int scrollX = getScrollX();
			// 计算一秒钟x轴的速度
			mVelocityTracker.computeCurrentVelocity(1000);
			float xVelocity = mVelocityTracker.getXVelocity();
			if (Math.abs(xVelocity) >= 50) {// 如果速度大于0.05秒
				// 从右向左滑动(负),索引+1. 反之,索引-1.
				mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
			} else {// 速度小于0.05秒
				mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;// 计算索引值
			}
			// 索引从0到最后一页
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
	// 分别记录上次滑动的坐标
	private int mLastX = 0;
	//private int mLastY = 0;

	private void smoothScrollBy(int dx, int dy) {
		mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
		invalidate();
	}

	@Override
	public void computeScroll() {
		// 判断是否在滚动,如果在滚动,返回true.
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 更新界面
			postInvalidate();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		mVelocityTracker.recycle();
		super.onDetachedFromWindow();
	}

}
