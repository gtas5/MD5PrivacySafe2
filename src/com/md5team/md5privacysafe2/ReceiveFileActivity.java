package com.md5team.md5privacysafe2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.md5team.md5privacysafe2.db.DBHelper;
import com.md5team.md5privacysafe2.encryption.EncryFileAsyncTask;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ReceiveFileActivity extends ActionBarActivity {
	ProgressBar progressBar;
	Button cancleButton;
	DBHelper dbHelper;
	EncryFileAsyncTask encryTask = new EncryFileAsyncTask() {
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onCancelled(Boolean result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				Toast.makeText(getApplicationContext(), "加密成功",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "加密失败",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			progressBar.setProgress(0);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_file);
		

		
		progressBar = (ProgressBar) findViewById(R.id.encryProgressBar);
		cancleButton = (Button) findViewById(R.id.encryCancleButton);
		cancleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				encryTask.cancel(true);
				finish();
			}
		});
		
		try {
			dbHelper=DBHelper.getInstance(getApplicationContext());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String action = i.getAction();
		if (Intent.ACTION_SEND.equals(action)) {        
			if (extras.containsKey(Intent.EXTRA_STREAM)) {
				try {
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
					String path = getPath(uri);
					File photo=new File(path);
					
					//外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
					ContentResolver resolver = getContentResolver();
					Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, uri); //显得到bitmap图片
					
					encryTask.execute(new File(""));
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
				}
			} else if (extras.containsKey(Intent.EXTRA_TEXT)) {
			}
		}
	}

	private void storeInfoInDB() {

	}
	
	private String getPath(Uri uri) throws FileNotFoundException, IOException{
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor =getContentResolver().query(uri, null, null, null, null);;
		cursor.moveToFirst(); 
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		return cursor.getString(idx);
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	private Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

}
