package com.example.chapter7;

import com.example.developart.R;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FourthActivity extends Activity {

	private final static String TAG = "FourthActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fourth);

	}

	@Override
	public void finish() {
		super.finish();
		// 开启动画.
		overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			Button btnFourth = (Button) findViewById(R.id.btn_fourth);
			performAnimate(btnFourth, btnFourth.getWidth(), 500);
		}
	}

	private void performAnimate(final View target, final int start, final int end) {
		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			// 持有一个IntEvaluator对象，方便估值的时候使用
			private IntEvaluator mIntEvaluator = new IntEvaluator();

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// 获得当前动画的进度值
				int currentValue = (Integer) animation.getAnimatedValue();
				Log.d(TAG, "currentValue:" + currentValue);

				// 获得当前进度占整个动画过程的比例
				float fraction = animation.getAnimatedFraction();
				// 直接调用整形估值器通过比例计算宽度
				target.getLayoutParams().width = mIntEvaluator.evaluate(fraction, start, end);
				target.requestLayout();
			}
		});
		valueAnimator.setDuration(5000).start();
	}
}
