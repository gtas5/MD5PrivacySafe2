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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
		registerForContextMenu(list);
		return rootView;
	}



	@Override
	public void onResume() {
		super.onResume();	
		DBHelper dbHelper;
		try {
			dbHelper = DBHelper.getInstance(getActivity());
			fileList=dbHelper.queryALLFiles();
			adapter.notifyDataSetChanged();
		} catch (IOException e) {
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
			return fileList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}



	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.fileList){
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.file_menu, menu); 
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
}
