package com.md5team.md5privacysafe2.decode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.AsyncTask;

/**
 * @author Axl
 *
 */
public abstract class Decode extends AsyncTask<String, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		byte[] buf = new byte[8*1024 * 1024]; // 1k缓存

		File re = new File(arg0[0]);
		File de = new File(arg0[1]);

		long fileSize = re.length();
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(re);
			out = new FileOutputStream(de);
			int len = 0;
			long dealYet = 0;
			while ((len = in.read(buf)) != -1) {

		//Function of File encryption arithmetic				
				for(int i=0; i<len; ++i)
					buf[i] = (byte)(buf[i]^i);
				
				out.write(buf, 0, len);
				dealYet += len;
				publishProgress((int) ((100*dealYet) / fileSize));
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
