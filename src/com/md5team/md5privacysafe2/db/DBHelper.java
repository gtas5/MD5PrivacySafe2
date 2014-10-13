package com.md5team.md5privacysafe2.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.md5team.md5privacysafe2.model.FileIteam;
import com.md5team.md5privacysafe2.model.PhotoIteam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 数据库工具类，同时也提供一些文件功能
 * @author LiuYajun
 * 
 */
public class DBHelper {
	public final static String DB_NAME = "md5db.db";
	public final static String ENCRYTED_DIR = "/MD5PrivacySafe/encyted/";
	public final static String ENCRYTED_PHOTO_DIR =ENCRYTED_DIR+"/photo/";
	public final static String ENCRYTED_FILE_DIR =ENCRYTED_DIR+"/file/";
	
	/**
	 * 加密后的图片，文件的后缀名会杯添加该字符串，以防止图库等程序读取
	 */
	public final static String ENCRYTED_NAME_FIX=".md5safe";
	private static DBHelper helper = null;
	private static SQLiteDatabase db = null;
	String sdCardPath;

	private DBHelper() {
		
	}

	private DBHelper(Context context) throws IOException {
		sdCardPath=getSDcardPath();
		File photoDir=new File(getEncrytedPhotoDir());
		File fileDir=new File(getEncrytedFileDir());
				
		if(!photoDir.exists()){
			photoDir.mkdirs();
		}
		
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		
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


	public static DBHelper getInstance(Context context) throws IOException {
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
		values.put("encryted_name", origName+ENCRYTED_NAME_FIX);
		long result=db.insert("photo", null, values);
		return result!=-1;
	}
	
	/**
	 * 存取新文件信息到数据库，注意文件本身并没有存储到数据库
	 * @param origPath 加密前文件的路径
	 * @param origName 加密前文件的名字
	 * @return
	 */
	public boolean storeNewFile(String origPath,String origName,String fileType){
		ContentValues values = new ContentValues();
		values.put("orig_path", origPath);
		values.put("orig_name", origName);
		values.put("encryted_path", sdCardPath+ENCRYTED_PHOTO_DIR);
		values.put("encryted_name", origName+ENCRYTED_NAME_FIX);
		values.put("file_type", fileType);
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
	
	/**
	 * 查询所有文件记录，注意，这里没有包含短信的记录
	 * @return
	 */
	public List<FileIteam> queryALLFiles(){
		List<FileIteam> list=new ArrayList<FileIteam>();
		
		Cursor c=db.rawQuery("SELECT * FROM file WHERE file_type <> ? ORDER BY file_type", new String[]{"csv"});
		while(c.moveToNext()){
			FileIteam file=new FileIteam();
			file.origName=c.getString(c.getColumnIndex("orig_name"));
			file.origPath=c.getString(c.getColumnIndex("orig_path"));
			file.encrytedName=c.getString(c.getColumnIndex("encryted_name"));
			file.encrytedPath=c.getString(c.getColumnIndex("encryted_path"));
			file.fileType=c.getString(c.getColumnIndex("file_type"));
			list.add(file);
		}
		return list;
	}
	
	/**
	 * 获取加密图的存储目录
	 * @return
	 */
	public String getEncrytedPhotoDir(){
		return sdCardPath+ENCRYTED_PHOTO_DIR;
	}
	
	/**
	 * 获取加密文件的存储目录
	 * @return
	 */
	public String getEncrytedFileDir(){
		return sdCardPath+ENCRYTED_FILE_DIR;
	}
	
	/**
	 * 根据提过的原照片名获取加密后的存储路径和文件名
	 * @param origName
	 * @return
	 */
	public String getEncrytedPhotoPathAndName(String origName){
		return getEncrytedPhotoDir()+origName+ENCRYTED_NAME_FIX;
	}
	
	/**
	 * 根据提过的原文件名获取加密后的存储路径和文件名
	 * @param origName
	 * @return
	 */
	public String getEncrytedFilePathAndName(String origName){
		return getEncrytedFileDir()+origName+ENCRYTED_NAME_FIX;
	}
}
