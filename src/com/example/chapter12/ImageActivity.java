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
	/** ��Ļ�Ŀ�� */
	private int screenWidth;
	/** ÿ��ͼƬ�Ŀ�� */
	private int mImageWidth;
	/** �Ƿ���wifi */
	private boolean mIsWifi;
	/** �Ƿ��ȡ����ͼƬ */
	private boolean mCanGetBitmapFromNetWork = false;
	/** ���񲼾� */
	private GridView mImageGridView;
	/** ͼƬ������ */
	private ImageAdapter mImageAdapter;
	/** ��ͼ�Ƿ���� */
	private boolean mIsGridViewIdle = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		// ��ʼ������
		initData();
		// ���ز���
		initView();
	}

	/**
	 * ��ʼ������
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
	 * ��ʼ������
	 */
	private void initView() {
		mImageGridView = (GridView) findViewById(R.id.gridView1);
		mImageAdapter = new ImageAdapter(this, mURList, mCanGetBitmapFromNetWork, mIsGridViewIdle, mImageWidth);
		mImageGridView.setAdapter(mImageAdapter);
		mImageGridView.setOnScrollListener(this);

		if (!mIsWifi) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("����ʹ�û���������ش��5MB��ͼƬ��ȷ��Ҫ������");
			builder.setTitle("ע��");
			builder.setPositiveButton("��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mCanGetBitmapFromNetWork = true;
					mImageAdapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton("��", null);
			builder.show();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {// ��ͼ��������ʱ�����ͼƬ
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
