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
	// �ֱ��¼��һ�εĻ���������
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
		// ��ȡ��Ļ��������С��Ԫ
		mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		Log.d(TAG, "sts:" + mScaledTouchSlop);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ϵͳ��Ļ��x���y��
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// ������Ļ��˲��
			break;
		case MotionEvent.ACTION_MOVE:// ����Ļ���ƶ�
			// �����λ��
			int deltaX = x - mLastX;
			int deltaY = y - mLastY;
			Log.d(TAG, "move,deltaX:" + deltaX + " deltaY:" + deltaY);
			// ����λ�Ƽ�������������ƫ����
			int translationX = (int) getTranslationX() + deltaX;
			int translationY = (int) getTranslationY() + deltaY;
			// �ƶ�
			setTranslationX(translationX);
			setTranslationY(translationY);
			break;
		case MotionEvent.ACTION_UP:// �뿪��Ļ��˲��
			break;
		default:
			break;
		}
		mLastX = x;
		mLastY = y;
		return true;
	}
}
