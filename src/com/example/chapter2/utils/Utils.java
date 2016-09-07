package com.example.chapter2.utils;

import android.os.Environment;

public class Utils {

	/** 文件的保存目录 */
	public static final String DIR = Environment.getExternalStorageDirectory().getPath() + "/WCL/";
	/** 临时文件 */
	public static final String TEMPFILE = DIR + "data.txt";

}
