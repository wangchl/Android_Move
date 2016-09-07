package com.example.chapter6;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.chapter6.ui.CustomDrawable;
import com.example.developart.R;

public class DemoThirdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_third);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			View testScale = findViewById(R.id.test_scale);
			testScale.getBackground().setLevel(10);

			ImageView testClip = (ImageView) findViewById(R.id.test_clip);
			testClip.getDrawable().setLevel(8000);

			// test custom drawable
			View testCustomDrawable = findViewById(R.id.test_custom_drawable);
			CustomDrawable customDrawable = new CustomDrawable(Color.parseColor("#0ac39e"));
			testCustomDrawable.setBackgroundDrawable(customDrawable);
		}
	}
}
