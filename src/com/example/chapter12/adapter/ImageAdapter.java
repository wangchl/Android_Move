package com.example.chapter12.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.chapter12.loader.ImageLoader;
import com.example.developart.R;

/**
 * 加载图片适配器
 */
public class ImageAdapter extends BaseAdapter {

	/** 布局解析器 */
	private LayoutInflater mInflater;
	/** 默认图片 */
	private Drawable mDefaultBitmapDrawable;
	/** 图片url */
	private ArrayList<String> mURList;
	/** 是否获取网络图片 */
	private boolean mCanGetBitmapFromNetWork = false;
	/** 视图是否滚动 */
	private boolean mIsGridViewIdle = true;
	/** 每张图片的宽度 */
	private int mImageWidth;
	private ImageLoader mImageLoader;

	/**
	 * 
	 * @param context
	 *            上下文
	 * @param mURList
	 *            装有网络url的集合
	 * @param mCanGetBitmapFromNetWork
	 *            是否可以连接网络
	 * @param mIsGridViewIdle
	 *            是否滚动
	 * @param mImageWidth
	 *            每张图片的宽度
	 */
	public ImageAdapter(Context context, ArrayList<String> mURList, boolean mCanGetBitmapFromNetWork,
			boolean mIsGridViewIdle, int mImageWidth) {
		mInflater = LayoutInflater.from(context);
		this.mURList = mURList;
		this.mCanGetBitmapFromNetWork = mCanGetBitmapFromNetWork;
		this.mIsGridViewIdle = mIsGridViewIdle;
		this.mImageWidth = mImageWidth;
		mImageLoader = ImageLoader.build(context);
		mDefaultBitmapDrawable = context.getResources().getDrawable(R.drawable.image_default);
	}

	@Override
	public int getCount() {
		return mURList.size();
	}

	@Override
	public String getItem(int position) {
		return mURList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.image_list_item, parent, false);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageView imageView = holder.imageView;
		final String tag = (String) imageView.getTag();
		final String uri = getItem(position);
		if (!uri.equals(tag)) {
			imageView.setImageDrawable(mDefaultBitmapDrawable);
		}
		if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
			imageView.setTag(uri);
			mImageLoader.bindBitmap(uri, imageView, mImageWidth, mImageWidth);
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView imageView;
	}
}
