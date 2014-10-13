package com.md5team.md5privacysafe2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.md5team.md5privacysafe2.db.DBHelper;
import com.md5team.md5privacysafe2.model.FileIteam;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author 亚军
 * 
 */
public class FileFragment extends Fragment {

	List<FileIteam> fileList=new ArrayList<FileIteam>();
	FileListAdapter adapter;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(2);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.file_fragment, container,
				false);
		ListView list=(ListView) rootView.findViewById(R.id.fileList);
		adapter=new FileListAdapter();
		list.setAdapter(adapter);
		return rootView;
	}



	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();	
		DBHelper dbHelper;
		try {
			dbHelper = DBHelper.getInstance(getActivity());
			fileList=dbHelper.queryALLFiles();
			adapter.notifyDataSetChanged();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	protected class FileListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fileList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View iteam;
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				iteam=inflater.inflate(R.layout.file_iteam, null);
			}else{
				iteam=convertView;
			}
			TextView fileName=(TextView) iteam.findViewById(R.id.fileName);
			fileName.setText(fileList.get(position).origName);
			return iteam;
		}
		
	}
	
}
