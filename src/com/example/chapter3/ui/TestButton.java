package com.example.chapter3.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class TestButton extends TextView {

	private int mScaledTouchSlop;
	private final static String TAG = "TestButton";
	// 分别记录上一次的滑动的坐标
	private int mLastX = 0;
	private int mLastY = 0;

	public TestButton(Context context) {
		this(context, null);
	}

	public TestButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TestButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取屏幕滑动的最小单元
		mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		Log.d(TAG, "sts:" + mScaledTouchSlop);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 系统屏幕的x轴和y轴
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// 触摸屏幕的瞬间
			break;
		case MotionEvent.ACTION_MOVE:// 在屏幕上移动
			// 计算出位移
			int deltaX = x - mLastX;
			int deltaY = y - mLastY;
			Log.d(TAG, "move,deltaX:" + deltaX + " deltaY:" + deltaY);
			// 根据位移计算出横纵坐标的偏移量
			int translationX = (int) getTranslationX() + deltaX;
			int translationY = (int) getTranslationY() + deltaY;
			// 移动
			setTranslationX(translationX);
			setTranslationY(translationY);
			break;
		case MotionEvent.ACTION_UP:// 离开屏幕的瞬间
			break;
		default:
			break;
		}
		mLastX = x;
		mLastY = y;
		return true;
	}
}
