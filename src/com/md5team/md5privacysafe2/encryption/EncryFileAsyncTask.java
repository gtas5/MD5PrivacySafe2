package com.md5team.md5privacysafe2.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.AsyncTask;

/**
 * @author 亚军
 *
 */
public class EncryFileAsyncTask extends AsyncTask<String, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		byte[] buf = new byte[1024*1024 * 1024]; // 1k缓存

		File re = new File(arg0[0]);
		File de = new File(arg0[1]);

		long fileSize = re.length();
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream(re);
			out = new FileOutputStream(de);
			int len = 0;
			long dealYet = 0;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
				dealYet += len;
				publishProgress((int) (dealYet / fileSize));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			de.delete();
			return false;
		} finally {
			try {
				in.close();
				if(de.exists()){
					out.flush();
				}
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}
}
