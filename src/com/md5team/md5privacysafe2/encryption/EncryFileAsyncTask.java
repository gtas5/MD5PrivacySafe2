package com.md5team.md5privacysafe2.encryption;

import java.io.File;
import android.os.AsyncTask;

public class EncryFileAsyncTask extends AsyncTask<File, Integer, Boolean> {
	@Override
	protected Boolean doInBackground(File... arg0) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 101; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			publishProgress(i);
		}
		return true;
	}
}