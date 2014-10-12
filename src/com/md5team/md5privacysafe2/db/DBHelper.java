package com.md5team.md5privacysafe2.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.md5team.md5privacysafe2.model.PhotoIteam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 数据库工具类
 * @author LiuYajun
 * 
 */
public class DBHelper {
	public final static String DB_NAME = "md5db.db";
	public final static String ENCRYTED_DIR = "/MD5PrivacySafe";
	public final static String ENCRYTED_PHOTO_DIR =ENCRYTED_DIR+"/photo";
	public final static String ENCRYTED_FILE_DIR =ENCRYTED_DIR+"/file";
	
	/**
	 * 加密后的图片，文件的后缀名会杯添加该字符串，以防止图库等程序读取
	 */
	public final static String encryted_NAME_FIX="_";
	private static DBHelper helper = null;
	private static SQLiteDatabase db = null;
	String sdCardPath;

	private DBHelper() {
		
	}

	private DBHelper(Context context) throws FileNotFoundException {
		sdCardPath=getSDcardPath();
		db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS photo (orig_path VARCHAR(64),orig_name VARCHAR(32),thumb BLOB,encryted_path VARCHAR(64),encryted_name VARCHAR(32))");
		db.execSQL("CREATE TABLE IF NOT EXISTS file (orig_path VARCHAR(64),orig_name VARCHAR(32),encryted_path VARCHAR(64),encryted_name VARCHAR(32),file_type VARCHAR(8))");
		db.execSQL("CREATE TABLE IF NOT EXISTS txt (encryted_txt VARCHAR(1024),txt_type VARCHAR(8))");
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


	public static DBHelper getInstance(Context context) throws FileNotFoundException {
		if (helper == null) {
			helper = new DBHelper(context);
		}
		return helper;
	}

	/**
	 * 存储新照片到数据库，注意，只有缩略图杯存储到数据库，图片本身没有被存储到数据库
	 * @param origPath 加密前文件的路径
	 * @param origName 加密前文件的名字
	 * @param thumb    文件的缩略图
	 * @return
	 * @throws IOException
	 */
	public boolean storeNewPhoto(String origPath,String origName,Bitmap thumb) throws IOException{
		ContentValues values = new ContentValues();
		values.put("orig_path", origPath);
		values.put("orig_name", origName);
		values.put("thumb", bmpToByteArray(thumb));
		values.put("encryted_path", sdCardPath+ENCRYTED_PHOTO_DIR);
		values.put("encryted_name", origName+encryted_NAME_FIX);
		long result=db.insert("photo", null, values);
		return result!=-1;
	}
	
	/**
	 * 存取新文件信息到数据库，注意文件本身并没有存储到数据库
	 * @param origPath 加密前文件的路径
	 * @param origName 加密前文件的名字
	 * @return
	 */
	public boolean storeNewFile(String origPath,String origName){
		ContentValues values = new ContentValues();
		values.put("orig_path", origPath);
		values.put("orig_name", origName);
		values.put("encryted_path", sdCardPath+ENCRYTED_PHOTO_DIR);
		values.put("encryted_name", origName+encryted_NAME_FIX);
		boolean i=db.isOpen();
		db.insert("file", null, values);
		return true;
	}
	
	/**
	 * 存储新字符串到数据库
	 * @param encrytedTxt
	 * @param txtType
	 * @return
	 */
	public boolean storeNewText(String encrytedTxt,String txtType){
		ContentValues values = new ContentValues();
		values.put("encryted_txt", encrytedTxt);
		values.put("txt_type", txtType);
		db.insert("photo", null, values);
		return true;
	}
	
	/**
	 * 查询所有图片记录
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> queryALLPhotos(){
		ArrayList<HashMap<String, Object>> list=new ArrayList<>();
		
		Cursor c=db.rawQuery("SELECT * FROM photo",null);
		while(c.moveToNext()){
		    HashMap<String, Object> map = new HashMap<String, Object>(); 
			PhotoIteam iteam=new PhotoIteam();
			iteam.origPath=c.getString(c.getColumnIndex("orig_path"));
			iteam.origName=c.getString(c.getColumnIndex("orig_name"));
			iteam.encyptedName=c.getString(c.getColumnIndex("encryted_name"));
			iteam.encyptedPath=c.getString(c.getColumnIndex("encryted_path"));
			byte[] in=c.getBlob(c.getColumnIndex("thumb"));
			Bitmap thub=BitmapFactory.decodeByteArray(in,0,in.length);
			map.put("PhotoName", iteam.origName);
			map.put("PhotoIteam", iteam);
			map.put("PhotoImage", thub);
			
			list.add(map);
		}
		
		return list;
	}
	
}
