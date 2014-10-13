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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author 亚军
 *
 */
public class ReceiveFileActivity extends ActionBarActivity {
	ProgressBar progressBar;
	Button cancleButton;
	DBHelper dbHelper;
	EncryFileAsyncTask encryTask;

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

		encryTask = new EncryFileAsyncTask() {
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

		try {
			dbHelper = DBHelper.getInstance(getApplicationContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String action = i.getAction();
		if (Intent.ACTION_SEND.equals(action)) {
			if (extras.containsKey(Intent.EXTRA_STREAM)) {
				try {
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
					String fileType=getFileType(uri);
					
					if (fileType.equals("jpeg")||fileType.equals("png")) {	//处理照片
						String path = getPhotoPath(uri);
						File photo = new File(path);
						// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
						ContentResolver resolver = getContentResolver();
						Bitmap bm = MediaStore.Images.Media.getBitmap(resolver,
								uri); // 显得到bitmap图片
						Bitmap thum = ThumbnailUtils.extractThumbnail(bm, 90,90);
						dbHelper.storeNewPhoto(photo.getParent(),
								photo.getName(), thum);
						encryTask.execute(path,dbHelper.getEncrytedPhotoPathAndName(photo.getName()));
					}else{													//处理其他文件
						
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
				}
			} else if (extras.containsKey(Intent.EXTRA_TEXT)) {				//处理加密文本
				
			}
		}
	}

	private String getFileType(Uri uri){
		ContentResolver cR = getContentResolver();
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String type = mime.getExtensionFromMimeType(cR.getType(uri));
		return type;
	}

	private String getPhotoPath(Uri uri) throws FileNotFoundException, IOException {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

}
