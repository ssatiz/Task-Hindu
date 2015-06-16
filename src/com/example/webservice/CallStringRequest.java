package com.example.webservice;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class CallStringRequest extends Request<String> {

	private Listener<String> listener;
	private Map<String, String> params;

	public CallStringRequest(String url, Map<String, String> params,
			Listener<String> reponseListener, ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
	}

	public CallStringRequest(int method, String url, Map<String, String> params,
			Listener<String> reponseListener, ErrorListener errorListener) {
		super(method, url, errorListener);
		this.listener = reponseListener;
		this.params = params;
	}

	 @Override 
	protected Map<String, String> getParams()
			throws AuthFailureError {
		return params;
	};
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		HashMap<String, String> headers = new HashMap<String, String>();
		 headers.put("FITTR-KEY", "a143891c191b65c3f8c2a25ab6e86f7b");
//		 Log.v("", "Headers added");
		return headers;
	}
	
//	@Override
//	public byte[] getBody() throws AuthFailureError {
//		// TODO Auto-generated method stub
//		return super.getBody();
//	}
	
//	@Override
//    public String getBodyContentType() {
//        return "application/json; charset=utf-8";
//    }

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(jsonString,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(String response) {
		// TODO Auto-generated method stub
		
			listener.onResponse(response);
	}
}
