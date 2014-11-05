package com.md5team.md5privacysafe2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.crypto.Cipher; 

import com.md5team.md5privacysafe2.aes.Aes_achieve;

import android.annotation.SuppressLint;  
import android.app.Activity;  
import android.os.AsyncTask;  
import android.os.Bundle;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.TextView; 

@SuppressLint("SdCardPath")  

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	


	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	
	
	private final String SDcardPath = "/mnt/sdcard/encry/";  
    private Button mEncryptButton;  
    private Button mDecryptButton;  
    private TextView mShowMessage;  
    private EditText mFileName;  
    private EditText mNewFileName;  
    private Aes_achieve mAESHelper;  
    private EncryptionOrDecryptionTask mTask = null;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		
		
		
		
		
		
		
		
		
		
		
//		super.onCreate(savedInstanceState);  
//        setContentView(R.layout.main);  
        mAESHelper = new Aes_achieve();  
  
//        mFileName = (EditText) findViewById(R.id.file_name);  
//        mNewFileName = (EditText) findViewById(R.id.new_file_name);  
//        mShowMessage = (TextView) findViewById(R.id.message);  
//        mEncryptButton = (Button) findViewById(R.id.encrypt);  
//        mDecryptButton = (Button) findViewById(R.id.decrypt);  
  
        mEncryptButton.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
  
                mShowMessage.setText("开始加密，请稍等...");  
                if (mTask != null) {  
                    mTask.cancel(true);  
                }  
  
                mTask = new EncryptionOrDecryptionTask(true, SDcardPath  
                        + mFileName.getText(), SDcardPath, mNewFileName  
                        .getText().toString(), "zjc");  
                mTask.execute();  
            }  
        });  
  
        mDecryptButton.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
  
                mShowMessage.setText("开始解密，请稍等...");  
  
                if (mTask != null) {  
                    mTask.cancel(true);  
                }  
  
                mTask = new EncryptionOrDecryptionTask(false, SDcardPath  
                        + mFileName.getText(), SDcardPath, mNewFileName  
                        .getText().toString(), "zjc");  
                mTask.execute();  
  
            }  
        });  
		
		
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment f=null;
		switch(position){
		case 0:
			f=new PhotoFragment();
			break;
		case 1:
			f=new FileFragment();
			break;
		default:
			f=PlaceholderFragment.newInstance(position+1);
			break;
		}
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						f).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		case 6:
			mTitle = getString(R.string.title_section6);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	
	private class EncryptionOrDecryptionTask extends  
    AsyncTask<Void, Void, Boolean> {  

private String mSourceFile = "";  
private String mNewFilePath = "";  
private String mNewFileName = "";  
private String mSeed = "";  
private boolean mIsEncrypt = false;  

public EncryptionOrDecryptionTask(boolean isEncrypt, String sourceFile,  
        String newFilePath, String newFileName, String seed) {  
    this.mSourceFile = sourceFile;  
    this.mNewFilePath = newFilePath;  
    this.mNewFileName = newFileName;  
    this.mSeed = seed;  
    this.mIsEncrypt = isEncrypt;  
}  

@Override  
protected Boolean doInBackground(Void... params) {  

    boolean result = false;  

    if (mIsEncrypt) {  
        result = mAESHelper.AESCipher(Cipher.ENCRYPT_MODE, mSourceFile,  
                mNewFilePath + mNewFileName, mSeed);  
    } else {  
        result = mAESHelper.AESCipher(Cipher.DECRYPT_MODE, mSourceFile,  
                mNewFilePath + mNewFileName, mSeed);  
    }  

    return result;  
}  

@Override  
protected void onPostExecute(Boolean result) {  
    super.onPostExecute(result);  
    String showMessage = "";  

    if (mIsEncrypt) {  
        showMessage = result ? "加密已完成" : "加密失败!";  
    } else {  
        showMessage = result ? "解密完成" : "解密失败!";  
    }  

    mShowMessage.setText(showMessage);  
}  
}  
	

}
