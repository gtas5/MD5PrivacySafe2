package com.md5team.md5privacysafe2;

import java.io.File;

import com.md5team.md5privacysafe2.encryption.EncryFileAsyncTask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
				Toast.makeText(getApplicationContext(), "加密成功！",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "加密失败！",
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
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String action = i.getAction();
		if (Intent.ACTION_SEND.equals(action)) {
			if (extras.containsKey(Intent.EXTRA_STREAM)) {
				try {
					// Get resource path from intent
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
					// 返回路径
					// String path = getRealPathFromURI(this, uri);
					Toast.makeText(getApplicationContext(), uri.toString(),
							Toast.LENGTH_SHORT).show();
					encryTask.execute(new File(""));
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
				}
			} else if (extras.containsKey(Intent.EXTRA_TEXT)) {
			}
		}
	}
}
