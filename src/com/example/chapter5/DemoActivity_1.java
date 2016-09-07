package com.example.chapter5;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.developart.R;

public class DemoActivity_1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo1);
		initView();
	}

	private void initView() {
		Toast.makeText(this, getIntent().getStringExtra("SZLD"), Toast.LENGTH_SHORT).show();
	}
}
