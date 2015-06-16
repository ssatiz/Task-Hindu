package com.example.task;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.customview.CirclePageIndicator;
import com.example.model.BedRoom;
import com.example.model.Document;
import com.example.model.ProjectDataDetail;
import com.example.util.BundleKeys;
import com.example.util.Util;
import com.example.webservice.CallWebService;
import com.example.webservice.utilities.Const;
import com.example.webservice.utilities.WebServiceResponseKeys;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ProjectDetail extends FragmentActivity implements OnClickListener{

	GoogleMap googleMap;
	String PID="";
	ProgressDialog loading;
	
	//List<ProjectDataDetail> lstProjectDetail = new ArrayList<ProjectDataDetail>();
	ProjectDataDetail projectDataDetail;
	TextView lblHeader;
	TextView lblDescriptiontxt,lblAddresstxt;
	TextView lblMaxAreaValue,lblMaxPriceValue,lblMaxSqftValue;
	TextView lblMinAreaValue,lblMinPriceValue,lblMinSqftValue;
	
	TextView lblAmenities,lblOtherAmenities;
	TextView lblSpecification;
	
	ViewPager mPager;
	CirclePageIndicator mIndicator;
	CustomPagerAdapter adapter;
	int width,height;
	
	Animation slide_down,slide_up;
	Button btnBuilder,btnProperty;
	LinearLayout lnrBuilderInfo,lnrPropertyInfo;
	
	TextView lblBuilderName,lblBuilderDescription,lblBuilderUrl;
	ScrollView scroll,scrollProperty,ScrollBuilder;
	
	ImageView imgLogo;
	TextView lblBMaxSqftValue,lblBMaxPriceValue,lblBMaxAreaValue;
	TextView lblAMaxAreaValue,lblAMaxPriceValue,lblAMaxSqftValue;

	LinearLayout lnrInflateBed;
	TextView lblBuildingStatus,lblElectricity,lblWater,lblWatertxt;
	
	TextView lblBank,lblApproved,lblBanktxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.project_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);

		
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			if(extras.containsKey(BundleKeys.id)){
				PID=extras.getString(BundleKeys.id);
				Log.e("UTL", PID);
			}
		}
		initialize();


	}

	private void initialize() {
		// TODO Auto-generated method stub
		loading= new ProgressDialog(ProjectDetail.this);
		loading.setIndeterminate(true);
		loading.setTitle(R.string.loading);
		loading.setCancelable(false);

		Animation slide_down     = AnimationUtils.loadAnimation(getApplicationContext(),
	            R.anim.slide_down);

	    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
	            R.anim.slide_up);
	// Start animation
	
	    lblHeader=(TextView)findViewById(R.id.lblHeader);

		
	    scroll=(ScrollView)findViewById(R.id.scrollView1);
	    ScrollBuilder=(ScrollView)findViewById(R.id.scrollBuilder);
	    scrollProperty=(ScrollView)findViewById(R.id.scrollProperty);
		mPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		
		
		lblDescriptiontxt=(TextView)findViewById(R.id.lblDescriptiontxt);
		lblAddresstxt=(TextView)findViewById(R.id.lblAddresstxt);

		lblMaxAreaValue=(TextView)findViewById(R.id.lblMaxAreaValue);
		lblMaxPriceValue=(TextView)findViewById(R.id.lblMaxPriceValue);
		lblMaxSqftValue=(TextView)findViewById(R.id.lblMaxSqftValue);
		lblMinAreaValue=(TextView)findViewById(R.id.lblMinAreaValue);
		lblMinPriceValue=(TextView)findViewById(R.id.lblMinPriceValue);
		lblMinSqftValue=(TextView)findViewById(R.id.lblMinSqftValue);
		
		lblAmenities=(TextView)findViewById(R.id.lblAmenities);
		lblOtherAmenities=(TextView)findViewById(R.id.lblOtherAmenities);
		
		lblSpecification=(TextView)findViewById(R.id.lblSpecification);
		
		
		btnBuilder=(Button)findViewById(R.id.btnBuilder);
		btnProperty=(Button)findViewById(R.id.btnProperty);
		btnBuilder.setOnClickListener(this);
		btnProperty.setOnClickListener(this);
		
		lnrBuilderInfo=(LinearLayout)findViewById(R.id.lnrBuilderInfo);
		lnrPropertyInfo=(LinearLayout)findViewById(R.id.lnrPropertyInfo);
		
		lblBuilderName=(TextView)findViewById(R.id.lblBuilderName);
		lblBuilderDescription=(TextView)findViewById(R.id.lblBuilderDescription);
		lblBuilderUrl=(TextView)findViewById(R.id.lblBuilderUrl);
		imgLogo=(ImageView)findViewById(R.id.imgLogo);
		
		lblBMaxSqftValue=(TextView)findViewById(R.id.lblBMaxSqftValue);
		lblBMaxPriceValue=(TextView)findViewById(R.id.lblBMaxPriceValue);
		lblBMaxAreaValue=(TextView)findViewById(R.id.lblBMaxAreaValue);

		lblAMaxAreaValue=(TextView)findViewById(R.id.lblAMaxAreaValue);
		lblAMaxPriceValue=(TextView)findViewById(R.id.lblAMaxPriceValue);
		lblAMaxSqftValue=(TextView)findViewById(R.id.lblAMaxSqftValue);
		
		lblBuildingStatus=(TextView)findViewById(R.id.lblBuildingStatus);
		lblElectricity=(TextView)findViewById(R.id.lblElectricity);
		lblWater=(TextView)findViewById(R.id.lblWater);
		lblWatertxt=(TextView)findViewById(R.id.lblWatertxt);
		
		lblBank=(TextView)findViewById(R.id.lblBank);
		lblApproved=(TextView)findViewById(R.id.lblApproved);
		lblBanktxt=(TextView)findViewById(R.id.lblBanktxt);
		
		lnrInflateBed=(LinearLayout)findViewById(R.id.lnrInflateBed);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		  width = screenWidth;
		  height = (int)(450* width/1350) ; 
		CallDetailAPI();


	}

	private void CallDetailAPI() {
		// TODO Auto-generated method stub

		if(Util.haveInternet(ProjectDetail.this)){
			String url=Const.PROJECT_DETAIL+PID;
			Log.e("UTL", url);
			loading.show();
			new CallWebService(ProjectDetail.this, url, mHandler, null, Request.Method.GET).
			callStringRequestWebService();

		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetail.this);
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
		Gson gson1;

		try {
			JSONObject resObj=new JSONObject(response);
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setDateFormat("M/d/yy hh:mm a");
			gson1= gsonBuilder.create();
			Gson gson = gsonBuilder.create();
			projectDataDetail = gson.fromJson(resObj.toString(), ProjectDataDetail.class);

			if(resObj.has("propertyTypes")){
				JSONObject propertyObj = resObj.getJSONObject("propertyTypes");
				if(propertyObj.has("Apartments")){
					JSONObject apartmentObj = propertyObj.getJSONObject("Apartments");
					if(apartmentObj.has("bedrooms")){
						JSONObject bedroomsObj = apartmentObj.getJSONObject("bedrooms");
						projectDataDetail.bedrooms = new ArrayList<BedRoom>();
						if(bedroomsObj.has("1")){
							JSONObject bedroom1Obj = bedroomsObj.getJSONObject("1");
							BedRoom bedroom = gson.fromJson(bedroom1Obj.toString(), BedRoom.class);
							projectDataDetail.bedrooms.add(bedroom);
						}
						if(bedroomsObj.has("2")){
							JSONObject bedroom1Obj = bedroomsObj.getJSONObject("2");
							BedRoom bedroom = gson.fromJson(bedroom1Obj.toString(), BedRoom.class);
							projectDataDetail.bedrooms.add(bedroom);
						}
						if(bedroomsObj.has("3")){
							JSONObject bedroom1Obj = bedroomsObj.getJSONObject("3");
							BedRoom bedroom = gson.fromJson(bedroom1Obj.toString(), BedRoom.class);
							projectDataDetail.bedrooms.add(bedroom);
						}
						
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if(projectDataDetail!=null){
		
			adapter=new CustomPagerAdapter(projectDataDetail.documents,ProjectDetail.this);
			mPager.setAdapter(adapter);
			mIndicator.setViewPager(mPager);
	
			lblHeader.setText(projectDataDetail.listingName+"("+projectDataDetail.projectType+")");
			lblDescriptiontxt.setText(projectDataDetail.description);
			lblAddresstxt.setText(projectDataDetail.addressLine1+"\\n"+
					projectDataDetail.addressLine1+"//n"+projectDataDetail.city+
					projectDataDetail.locality+"/n"+projectDataDetail.landmark);
			lblMaxAreaValue.setText(projectDataDetail.maxArea);
			lblMaxPriceValue.setText(projectDataDetail.maxPrice);
			lblMaxSqftValue.setText(projectDataDetail.maxPricePerSqft);

			lblMinAreaValue.setText(projectDataDetail.minArea);
			lblMinPriceValue.setText(projectDataDetail.minPrice);
			lblMinSqftValue.setText(projectDataDetail.minPricePerSqft);
			StringBuilder builder=new StringBuilder();
			for(String s:projectDataDetail.amenities){
				builder.append(s+",");
			}
 		    lblAmenities.setText(builder.toString());
			
 			lblOtherAmenities.setText(projectDataDetail.otherAmenities);
			Log.e("Document size", projectDataDetail.documents.size()+"");
					
			lblSpecification.setText(Html.fromHtml(projectDataDetail.specification));
			
			lblBuilderName.setText(projectDataDetail.builderName);
			
			lblBuilderDescription.setText(Html.fromHtml(projectDataDetail.builderDescription));
			
			try{
				Picasso.with(ProjectDetail.this)
				.load(projectDataDetail.builderLogo)
				.resize(width, height)
				.into(imgLogo);
			}catch(Exception ex){

				}

			lblBuilderUrl.setText(projectDataDetail.builderUrl);
 			lblBMaxSqftValue.setText(projectDataDetail.propertyTypes.Apartments.maxPricePerSqft);
			lblBMaxPriceValue.setText(projectDataDetail.propertyTypes.Apartments.maxPrice);
			lblBMaxAreaValue.setText(projectDataDetail.propertyTypes.Apartments.maxArea);
			for(int i=0;i<projectDataDetail.bedrooms.size();i++){
				//Log.v("", "Bedroom Detail: "+projectDataDetail.bedrooms.get(i).maxArea);
				LinearLayout v=(LinearLayout)getLayoutInflater().inflate(R.layout.inflate_bed,null);
				TextView lblAMaxAreaValue=(TextView)v.findViewById(R.id.lblAMaxAreaValue);
				TextView lblAMaxPriceValue=(TextView)v.findViewById(R.id.lblAMaxPriceValue);
				TextView lblAMaxSqftValue=(TextView)v.findViewById(R.id.lblAMaxSqftValue);
				lblAMaxAreaValue.setText(projectDataDetail.bedrooms.get(i).maxArea);
				lblAMaxPriceValue.setText(projectDataDetail.bedrooms.get(i).maxPrice);
				lblAMaxSqftValue.setText(projectDataDetail.bedrooms.get(i).maxPricePerSqft);
				lnrInflateBed.addView(v);
			} 
			if(projectDataDetail.bankApprovals!=null){
				if(projectDataDetail.bankApprovals.length>1){
	  				StringBuilder builders=new StringBuilder();
					for(String s:projectDataDetail.bankApprovals){
						builder.append(s+",");
					}
	  					lblBank.setText(builders);
	  			}
			}else{
				lblBanktxt.setVisibility(View.GONE);
			}
			
			if(!TextUtils.isEmpty(projectDataDetail.approvedBy)){
				lblApproved.setText(projectDataDetail.approvedBy+("ApprovedNo:"+projectDataDetail.approvalNumber));

			}
			lblBuildingStatus.setText(projectDataDetail.status);
			lblElectricity.setText(projectDataDetail.electricityConnection);
			if(TextUtils.isEmpty(projectDataDetail.waterTypes)){
				lblWatertxt.setVisibility(View.GONE);
			}else{
				lblWater.setText(projectDataDetail.waterTypes);

			}
		}
		
		loading.cancel();
	}
	
	class CustomPagerAdapter extends PagerAdapter {
		 
	    Context mContext;
	    LayoutInflater mLayoutInflater;
	    ArrayList<Document> documents;
	    public CustomPagerAdapter(ArrayList<Document> documents,Context mContext) {
	        
	        this.documents=documents;
	        Log.e("Const", documents.size()+"");
	        this.mContext=mContext;
	        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	 
	    @Override
	    public int getCount() {
	        return documents.size();
	    }
	 
	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == object;
	    }
	 
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {

	    	 View itemView =null;
			 try{
				 itemView = mLayoutInflater.inflate(R.layout.inflate_document, container, false);

				 ImageView imgPhoto=(ImageView)itemView.findViewById(R.id.imgPhotos);
				 TextView lblPropertyType=(TextView)itemView.findViewById(R.id.lblPropertyType);
				 
				 lblPropertyType.setText(documents.get(position).text);
				 
				 try{
						Picasso.with(mContext)
						.load(documents.get(position).reference)
						.resize(width, height)
						.into(imgPhoto);
					}catch(Exception ex){
 
 					}
				
				 Log.e("Image-url", documents.get(position).reference);
				 
				 
			 }catch(Exception e){
//				 Log.e("Gopi", e.toString());
			 }
	      
	        /*ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
	        imageView.setImageResource(mResources[position]);
	 */
	        
	        container.addView(itemView);
	 
	        return itemView;
	    }
	 
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView((LinearLayout) object);
	    }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBuilder:
			if(ScrollBuilder.getVisibility()==View.GONE){
				ScrollBuilder.setVisibility(View.VISIBLE);
				//lnrBuilderInfo.startAnimation(slide_up);
				scroll.setVisibility(View.GONE);
				btnProperty.setVisibility(View.GONE);
			}else{
			
				ScrollBuilder.setVisibility(View.GONE);
				//lnrBuilderInfo.startAnimation(slide_down);
				scroll.setVisibility(View.VISIBLE);
				btnProperty.setVisibility(View.VISIBLE);
			}
			
			
			break;
		case R.id.btnProperty:
			if(scrollProperty.getVisibility()==View.GONE){
				scrollProperty.setVisibility(View.VISIBLE);
				//lnrPropertyInfo.startAnimation(slide_up);
				scroll.setVisibility(View.GONE);
				btnBuilder.setVisibility(View.GONE);
				
			}else{
			
				scrollProperty.setVisibility(View.GONE);
				//lnrBuilderInfo.startAnimation(slide_down);
				scroll.setVisibility(View.VISIBLE);
				btnBuilder.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
	}
}
