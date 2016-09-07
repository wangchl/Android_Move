package com.example.chapter12;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

import com.example.chapter12.adapter.ImageAdapter;
import com.example.chapter12.utils.ImageUrl;
import com.example.chapter4.utils.MyUtils;
import com.example.developart.R;

public class ImageActivity extends Activity implements OnScrollListener {

	// private static final String TAG = "ImageActivity";
	private ArrayList<String> mURList = new ArrayList<String>();
	/** 屏幕的宽度 */
	private int screenWidth;
	/** 每张图片的宽度 */
	private int mImageWidth;
	/** 是否开启wifi */
	private boolean mIsWifi;
	/** 是否获取网络图片 */
	private boolean mCanGetBitmapFromNetWork = false;
	/** 网格布局 */
	private GridView mImageGridView;
	/** 图片适配器 */
	private ImageAdapter mImageAdapter;
	/** 视图是否滚动 */
	private boolean mIsGridViewIdle = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		// 初始化数据
		initData();
		// 加载布局
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		for (String url : ImageUrl.imageUrls) {
			mURList.add(url);
		}
		screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
		int space = (int) MyUtils.dp2px(this, 20);
		mImageWidth = (screenWidth - space) / 3;
		mIsWifi = MyUtils.isWifi(this);
		if (mIsWifi) {
			mCanGetBitmapFromNetWork = true;
		}
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		mImageGridView = (GridView) findViewById(R.id.gridView1);
		mImageAdapter = new ImageAdapter(this, mURList, mCanGetBitmapFromNetWork, mIsGridViewIdle, mImageWidth);
		mImageGridView.setAdapter(mImageAdapter);
		mImageGridView.setOnScrollListener(this);

		if (!mIsWifi) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("初次使用会从网络下载大概5MB的图片，确认要下载吗？");
			builder.setTitle("注意");
			builder.setPositiveButton("是", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mCanGetBitmapFromNetWork = true;
					mImageAdapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton("否", null);
			builder.show();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {// 视图不滚动的时候加载图片
			mIsGridViewIdle = true;
			mImageAdapter.notifyDataSetChanged();
		} else {
			mIsGridViewIdle = false;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
