package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.chapter12.ImageActivity;
import com.example.chapter3.TestActivity;
import com.example.chapter4.DemoActivity;
import com.example.chapter4.DemoSecondActivity;
import com.example.chapter5.DemoActivity_2;
import com.example.chapter5.RemoteViewActivity;
import com.example.chapter6.DemoThirdActivity;
import com.example.chapter7.FourthActivity;
import com.example.developart.R;

public class DemoMainActivity extends Activity {

	private Button button1, button2, button3, button4, button5, button6, button7, button8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_main);
		initView();
		initOnclickListener();
	}

	private void initView() {
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		button5 = (Button) findViewById(R.id.button5);
		button6 = (Button) findViewById(R.id.button6);
		button7 = (Button) findViewById(R.id.button7);
		button8 = (Button) findViewById(R.id.button8);
	}

	private void initOnclickListener() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				switch (v.getId()) {
				case R.id.button1:
					intent = new Intent(DemoMainActivity.this, TestActivity.class);
					startActivity(intent);
					break;
				case R.id.button2:
					intent = new Intent(DemoMainActivity.this, DemoActivity.class);
					startActivity(intent);
					break;
				case R.id.button3:
					intent = new Intent(DemoMainActivity.this, DemoSecondActivity.class);
					startActivity(intent);
					break;
				case R.id.button4:
					intent = new Intent(DemoMainActivity.this, DemoThirdActivity.class);
					startActivity(intent);
					break;
				case R.id.button5:
					intent = new Intent(DemoMainActivity.this, FourthActivity.class);
					startActivity(intent);
					// ¿ªÆô¶¯»­
					overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
					break;
				case R.id.button6:
					intent = new Intent(DemoMainActivity.this, ImageActivity.class);
					startActivity(intent);
					break;
				case R.id.button7:
					intent = new Intent(DemoMainActivity.this, RemoteViewActivity.class);
					startActivity(intent);
					break;
				case R.id.button8:
					intent = new Intent(DemoMainActivity.this, DemoActivity_2.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		};
		button1.setOnClickListener(onClickListener);
		button2.setOnClickListener(onClickListener);
		button3.setOnClickListener(onClickListener);
		button4.setOnClickListener(onClickListener);
		button5.setOnClickListener(onClickListener);
		button6.setOnClickListener(onClickListener);
		button7.setOnClickListener(onClickListener);
		button8.setOnClickListener(onClickListener);
	}
}
