package com.example.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.model.ProjectData;
import com.example.util.BundleKeys;
import com.example.util.Util;
import com.example.webservice.CallWebService;
import com.example.webservice.utilities.Const;
import com.example.webservice.utilities.WebServiceResponseKeys;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ListProject extends FragmentActivity implements OnClickListener{

	ListView lstProject;
	ProgressDialog loading;
	
	List<ProjectData> lstProjectDatas = new ArrayList<ProjectData>();
	Adapter adapter;
	TextView lblListView,lblMapView,lblPrevious,lblCurrent;
	String identifyTab;
	GoogleMap googleMap;
	LinearLayout lnrMap;
	LatLng latLng;
	Marker markerOptions;
	String strLocationFrmMap="";

	Bitmap bm ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);

		
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		loading= new ProgressDialog(ListProject.this);
		loading.setIndeterminate(true);
		loading.setTitle(R.string.loading);
 		loading.setCancelable(false);
		
 		lblListView=(TextView)findViewById(R.id.lblListView);
 		lblMapView=(TextView)findViewById(R.id.lblMapView);
 		lblListView.setOnClickListener(this);
 		lblMapView.setOnClickListener(this);
 		
 		lblCurrent=lblListView;
		lstProject=(ListView)findViewById(R.id.lstProjectName);
		lnrMap=(LinearLayout)findViewById(R.id.lnrMap);
		
		identifyTab=getResources().getString(R.string.lst_view);
		setTabColur(lblListView);
		
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.company)
	            .copy(Bitmap.Config.ARGB_8888, true);
		
		SupportMapFragment supportMapFragment = (SupportMapFragment) 
	 			getSupportFragmentManager().findFragmentById(R.id.map);
	 		googleMap = supportMapFragment.getMap();
		
		
		lstProject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View arg1, int postition,long arg3) {
				// TODO Auto-generated method stub
				 Log.e("Clicked-ID", lstProjectDatas.get(postition).id);
				 Intent detailIntent=new Intent(ListProject.this,ProjectDetail.class);
				 detailIntent.putExtra(BundleKeys.id, lstProjectDatas.get(postition).id);
				 startActivity(detailIntent);
 			}

			
		});
		
		callAPI();
		
		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker mark) {
				// TODO Auto-generated method stub
				String id=fetchID( mark.getTitle());
				Intent detailIntent=new Intent(ListProject.this,ProjectDetail.class);
				detailIntent.putExtra(BundleKeys.id, id);
				startActivity(detailIntent);
  			}
		});

	}

	private String fetchID(String value){
 		
		for(int i=0;i<lstProjectDatas.size();i++){
		
			if(	lstProjectDatas.get(i).projectName.equals(value)){
				value=lstProjectDatas.get(i).id;
			}
		}
	
		return value;
		
	}
	 
	private void callAPI() {
		// TODO Auto-generated method stub
		if(Util.haveInternet(ListProject.this)){
			loading.show();
			new CallWebService(ListProject.this, Const.LIST_PROJECT, mHandler, null, Request.Method.GET).
			callStringRequestWebService();
			
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(ListProject.this);
			builder.setCancelable(false);
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getResources().getString(R.string.INTERNET_PROBLEM));
			builder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					dialog.dismiss();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

		}
	}
	
	Handler mHandler=new Handler(){
		
		public void dispatchMessage(Message msg) {
			Bundle extras = msg.getData();
			String response = "";
			if(extras.containsKey(WebServiceResponseKeys.RESPONSE)){
				response=extras.getString(WebServiceResponseKeys.RESPONSE);

			}
			parseResponse(response);
			Log.e("Result-test", response);
			
		}

		
	};
	
	private void parseResponse(String response) {
		// TODO Auto-generated method stub
		if(loading!=null){
			loading.cancel();
		}
		
		try {
			JSONArray resObj=new JSONArray(response);
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setDateFormat("M/d/yy hh:mm a");
			Gson gson = gsonBuilder.create();
			
			lstProjectDatas = Arrays.asList(gson.fromJson(resObj.toString(), ProjectData[].class));
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(lstProjectDatas.size()>0){
			adapter=new Adapter(ListProject.this,lstProjectDatas);
			lstProject.setAdapter(adapter);
		}
		
	}
	
	public class Adapter extends BaseAdapter{

		List<ProjectData> lstProjectDatas;
		Context context;
		LayoutInflater inflater;
		
		public class Holder{
			TextView lblProjectName;
		}
		public Adapter(ListProject listProject,List<ProjectData> lstProjectDatas) {
			// TODO Auto-generated constructor stub
			this.context=listProject;
			this.lstProjectDatas=lstProjectDatas;
			inflater=(LayoutInflater)getSystemService(context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lstProjectDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return lstProjectDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder;
			if(convertview==null){
				holder=new Holder();
				convertview=inflater.inflate(R.layout.inflate_project, parent, false);
				holder.lblProjectName=(TextView)convertview.findViewById(R.id.lblProjectName);
				convertview.setTag(holder);
			}else{
				holder=(Holder) convertview.getTag();
			}
			holder.lblProjectName.setText(lstProjectDatas.get(position).projectName);
			return convertview;
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.lblListView:
			
			if(!identifyTab.equals(getResources().getString(R.string.lst_view))){
				identifyTab=getResources().getString(R.string.lst_view);
				setTabColur(lblListView);
				lstProject.setVisibility(View.VISIBLE);
				lnrMap.setVisibility(View.GONE);
				
			}
			break;
		case R.id.lblMapView:
			
			if(identifyTab.equals(getResources().getString(R.string.lst_view))){
				identifyTab=getResources().getString(R.string.map_view);
				setTabColur(lblMapView);
				lstProject.setVisibility(View.GONE);
				lnrMap.setVisibility(View.VISIBLE);
				setMarker();
			}
			 
			break;

		default:
			break;
		}
		
	}
	
	private void setMarker() {
		// TODO Auto-generated method stub
		if(lstProjectDatas!=null){
			
				for(int i=0;i<lstProjectDatas.size();i++){
					getLocationMark(Double.parseDouble(lstProjectDatas.get(i).lat) ,Double.parseDouble(lstProjectDatas.get(i).lon),lstProjectDatas.get(i).projectName);
				}
		}
		
	}

	
	
	public void getLocationMark(double lat,double lng,String selectedPlace){
		//googleMap.clear();
 		latLng = new LatLng(lat,lng);
 		markerOptions = googleMap.addMarker(new MarkerOptions()
  		.position(latLng)
		.title(selectedPlace)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))
  				);
 		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
 	CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng,14);
 		googleMap.animateCamera(yourLocation);
		markerOptions.showInfoWindow();
			
			
 	}
	
	private void setTabColur(TextView lblCurrentView) {

		lblPrevious = lblCurrent;
		lblCurrent = lblCurrentView;

		if(lblPrevious.getId() == R.id.lblListView){
			lblListView.setBackgroundResource(R.drawable.blue_tapleft_bg);
			lblListView.setTextColor(getResources().getColor(R.color.white));

		}else if(lblPrevious.getId()==R.id.lblMapView) {
			lblMapView.setBackgroundResource(R.drawable.blue_tapright_bg);
			lblMapView.setTextColor(getResources().getColor(R.color.white));

		}

		if(lblCurrent.getId() == R.id.lblListView){
			lblListView.setBackgroundResource(R.drawable.white_tapleft_bg);
			lblListView.setTextColor(getResources().getColor(R.color.crowdfund_label));

		}else if(lblCurrent.getId()==R.id.lblMapView){

			lblMapView.setBackgroundResource(R.drawable.white_tapright_bg);
			lblMapView.setTextColor(getResources().getColor(R.color.crowdfund_label));

		}


	
	}

	
	
	

	 
}

