package com.met.andruino.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.met.andruino.R;


public class ModesListAdapter extends ArrayAdapter<String>{

	private Context mContext;
	private List<String> elements;
		/*
		 * default: Menu...
		 * */
	
	public ModesListAdapter(Context context, List<String> modeList) {
		// TODO Auto-generated constructor stub
		//super(context, R.layout.my_listadapter);
		super(context, android.R.layout.simple_list_item_1);
		Log.d("DEBUG - ModeListAdapter", "After Super");
		this.mContext = context;
		//this.elements.clear();
		this.elements = modeList;
		Log.d("DEBUG - ModeListAdapter", "Size... = " + this.elements.size());
		populateList();
	}
	
	private void populateList(){
		//this.modeList.clear();
		//String dataStr = "";
		Log.d("DEBUG - ModeListAdapter", "In Populate List");
	}
	
	public int getCount() {
		return this.elements.size();		
	}
	
	public String getItem(int index) {
		
		return this.elements.get(index);		
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		Log.d("DEBUG - MyEventListAdapter", "In getView");
		if (row == null){
			Log.d("DEBUG", "Starting Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(
																Context.LAYOUT_INFLATER_SERVICE); //Add subviews dinamically
			row = inflater.inflate(R.layout.my_modelistadapter, parent, false);
			row.setClickable(true);
			row.setOnClickListener(new OnClickListener() { 
				public void onClick(View v) {
					//String data = (String) v.getTag().toString(); 
					//Toast.makeText(getContext(), data, Toast.LENGTH_SHORT).show();
				} 
			});
			Log.d("DEBUG", "Successfully completed Row Inflation!");	
		}
		
		String event = getItem(position);
		Log.d("DEBUG - MyEventListAdapter", "Events Get item....");
		
		TextView name = (TextView) row.findViewById(R.id.model_list_function_name);
		name.setText((position+1) + ". " + event);
		
		row.setTag(event);
		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Log.d("DEBUG - MyEventListAdapter", "Clicked: " + v.getTag().toString());
				//TODO START ACTIVITY FOR EACH FUNCTIONALITY
//				Intent intent = new Intent(mContext, SingleEsventActivity.class);
//				intent.putExtra(SingleEventActivity.LIST_POSITION, v.getTag().toString());
//				mContext.startActivity(intent);
			}
		});
		
		return row;
		
	}
}
