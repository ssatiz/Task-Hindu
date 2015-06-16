
package com.example.webservice;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.example.webservice.utilities.AppController;

public class CallWebService {
	
	Context mCtx;
	final String TAG = JsonRequest.class.getSimpleName();
	String tag_json_obj = "jobj_req";
	String tag_json_arr = "jarr_req";
	String url;
	
	String serviceResponse;
	String status;
	String message;
	
	Handler mHandler;
	
	Map<String, String> params;
//	RequestQueue requestQueue ;
	int method;
	
	File f;
		
	public CallWebService(Context mCtx, String url, Handler mHandler, Map<String, String> params, int method) {
		this.mCtx = mCtx;
		this.url = url;
		this.mHandler = mHandler;
		this.params = params;
		this.method = method;
		
//		requestQueue = Volley.newRequestQueue(mCtx);
	}
	
	public CallWebService(Context mCtx, String url, Handler mHandler, Map<String, String> params, int method, File f) {
		this.mCtx = mCtx;
		this.url = url;
		this.mHandler = mHandler;
		this.params = params;
		this.method = method;
		this.f = f;
		
//		requestQueue = Volley.newRequestQueue(mCtx);
	}
	

	public void callStringRequestWebService(){
		
		 CallStringRequest jsObjRequest = new CallStringRequest(method,
	        		url,params,
	        		new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
 							
							serviceResponse = response.toString();
							
							Message message = new Message();
							Bundle extras = new Bundle();
							if(serviceResponse.startsWith("{") || 
									serviceResponse.startsWith("[")){
								extras.putString("response", serviceResponse);
							}else{
								extras.putString("response", "error");
							}
							message.setData(extras);
							mHandler.sendMessage(message);
							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
//							Log.v("","Response code:"+error.networkResponse.statusCode);
							serviceResponse = "error";	
							Message message = new Message();
							Bundle extras = new Bundle();
							if(error.networkResponse!=null){
//								Log.v("","Response code:"+error.networkResponse.statusCode);
								extras.putInt("response_code", error.networkResponse.statusCode);
							}
							
							extras.putString("response", serviceResponse);
							
							message.setData(extras);
							mHandler.sendMessage(message);
							
						}
						
					});
	        
		 
		 jsObjRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(100), 
					0, 0f));
			 
	       
//	        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 
//					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	        AppController.getInstance().addToRequestQueue(jsObjRequest);
//	        requestQueue.add(jsObjRequest);
	}
	
	 public void callStringRequestWebServiceForCampaign(){
			
		 CallStringRequest jsObjRequest = new CallStringRequest(method,
	        		url,params,
	        		new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							
							serviceResponse = response.toString();
 							Message message = new Message();
							Bundle extras = new Bundle();
 								extras.putString("response", serviceResponse);
 							message.setData(extras);
							mHandler.sendMessage(message);
							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							
							serviceResponse = "error";	
							Message message = new Message();
							Bundle extras = new Bundle();
							if(error.networkResponse!=null){
//								Log.v("","Response code:"+error.networkResponse.statusCode);
								extras.putInt("response_code", error.networkResponse.statusCode);
							}
							
							extras.putString("response", serviceResponse);
							
							message.setData(extras);
							mHandler.sendMessage(message);
							
						}
						
					});
		 
		  jsObjRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(100), 
					0, 0f));
	        AppController.getInstance().addToRequestQueue(jsObjRequest);
//	        requestQueue.add(jsObjRequest);
	}
		 
	
	public void callJSONObjectRequestWebService(){
		
		 CallJSONRequest jsObjRequest = new CallJSONRequest(method,
				 url,params,
	        		new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							
//							Log.v("","Reponse: "+response.toString());
							Message message = new Message();
							Bundle extras = new Bundle();
							extras.putString("response", serviceResponse);
							message.setData(extras);
							mHandler.sendMessage(message);
							
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							serviceResponse = "error";
							
							Message message = new Message();
							Bundle extras = new Bundle();
							extras.putString("response", serviceResponse);
							message.setData(extras);
							mHandler.sendMessage(message);
						}
						
						
					});
	        
	       
	        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 
					0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	        AppController.getInstance().addToRequestQueue(jsObjRequest);
//	        requestQueue.add(jsObjRequest);
	}
	
}
