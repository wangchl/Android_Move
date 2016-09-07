package com.example.chapter4;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapter4.ui.HorizontalScrollViewEx;
import com.example.chapter4.utils.MyUtils;
import com.example.developart.R;

public class DemoSecondActivity extends Activity {
	private HorizontalScrollViewEx mListContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_second);
		initView();
	}

	private void initView() {
		LayoutInflater inflater = getLayoutInflater();
		mListContainer = (HorizontalScrollViewEx) findViewById(R.id.container);
		final int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
		// final int screenHeight = MyUtils.getScreenMetrics(this).heightPixels;
		for (int i = 0; i < 3; i++) {
			ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.content_layout, mListContainer, false);
			layout.getLayoutParams().width = screenWidth;
			TextView textView = (TextView) layout.findViewById(R.id.title);
			textView.setText("page " + (i + 1));
			layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
			createList(layout);
			mListContainer.addView(layout);
		}
	}

	private void createList(ViewGroup layout) {
		ListView listView = (ListView) layout.findViewById(R.id.list);
		// ����Ч��
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_item);
		LayoutAnimationController controller = new LayoutAnimationController(animation);
		controller.setDelay(0.5f);
		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		listView.setLayoutAnimation(controller);

		ArrayList<String> datas = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			datas.add("name " + i);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.content_list_item, R.id.name, datas);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(DemoSecondActivity.this, "click item: " + id, Toast.LENGTH_SHORT).show();

			}
		});
	}
}
