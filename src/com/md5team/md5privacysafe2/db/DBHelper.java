package com.md5team.md5privacysafe2.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;

/**
 * @author LiuYajun
 * 
 */
public class DBHelper {
	public final static String DB_NAME = "md5db.db";
	public final static String PRIVACTED_DIR = "/MD5PrivacySafe";
	public final static String PRIVACTED_PHOTO_DIR =PRIVACTED_DIR+"/photo";
	public final static String PRIVACTED_FILE_DIR =PRIVACTED_DIR+"/file";
	
	/**
	 * 加密后的图片，文件的后缀名会杯添加该字符串，以防止图库等程序读取
	 */
	public final static String PRIVACTED_NAME_FIX="_";
	private static DBHelper helper = null;
	private static SQLiteDatabase db = null;
	String sdCardPath;

	private DBHelper() {
		
	}

	private DBHelper(Context context) throws FileNotFoundException {
		sdCardPath=getSDcardPath();
		db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXITS photo (orig_path VARCHAR(64),orig_name VARCHAR(32),thumb BLOB，privacted_path VARCHAR(64),privacted_name VARCHAR(32))");
		db.execSQL("CREATE TABLE IF NOT EXITS file (orig_path VARCHAR(64),orig_name VARCHAR(32),privacted_path VARCHAR(64),privacted_name VARCHAR(32),file_type VARCHAR(8))");
		db.execSQL("CREATE TABLE IF NOT EXITS txt (privacted_txt VARCHAR(1024),txt_type VARCHAR(8))");
	}

	private String getSDcardPath() throws FileNotFoundException {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}else{
			throw new FileNotFoundException("SDCARD not found");
		}
		return sdDir.toString();
	}
	
	// Bitmap to byte[]
	private byte[] bmpToByteArray(Bitmap bmp) throws IOException {
		// Default size is 32 bytes
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.close();
		return bos.toByteArray();
	}


	public DBHelper getInstance(Context context) throws FileNotFoundException {
		if (helper == null) {
			helper = new DBHelper(context);
		}
		return helper;
	}

	public boolean storeNewPhoto(String origPath,String origName,Bitmap thumb) throws IOException{
		ContentValues values = new ContentValues();
		values.put("orig_path", origPath);
		values.put("orig_name", origName);
		values.put("thumb", bmpToByteArray(thumb));
		values.put("privacted_path", sdCardPath+PRIVACTED_PHOTO_DIR);
		values.put("privacted_name", origName+PRIVACTED_NAME_FIX);
		db.insert("photo", null, values);
		return true;
	}
	
	
	
}
