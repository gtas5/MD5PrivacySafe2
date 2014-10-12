/**
 * 
 */
package com.md5team.md5privacysafe2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.md5team.md5privacysafe2.db.DBHelper;

/**
 * @author LiuYajun
 * 
 */
public class PhotoFragment extends Fragment {
	GridView grid;
	List<HashMap<String, Object>> list=new ArrayList<HashMap<String,Object>>();
	PhotoAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.photo_fragment, container,
				false);
		grid = (GridView) rootView.findViewById(R.id.photo_grid);
		adapter=new PhotoAdapter();
		grid.setAdapter(adapter);
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DBHelper dbHelper;
		try {
			dbHelper = DBHelper.getInstance(getActivity());
			list = dbHelper.queryALLPhotos();
			adapter.notifyDataSetChanged();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class PhotoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
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
				iteam=inflater.inflate(R.layout.photo_iteam, null);
			}else{
				iteam=convertView;
			}
			TextView photoName=(TextView) iteam.findViewById(R.id.ItemText);
			ImageView photoThub=(ImageView) iteam.findViewById(R.id.ItemImage);
			photoName.setText((CharSequence) list.get(position).get("PhotoName"));
			photoThub.setImageBitmap((Bitmap) list.get(position).get("PhotoImage"));
			return iteam;
		}

		
	}
	
}
