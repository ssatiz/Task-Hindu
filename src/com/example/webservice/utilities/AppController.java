package com.example.webservice.utilities;

 
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.example.task.R;
 
 
 
public class AppController extends Application {

	public static final String TAG = AppController.class
			.getSimpleName();

	private RequestQueue mRequestQueue;

	public static AppController mInstance;

	//    private ImageLoader mImageLoader;

	public static final boolean DEVELOPER_MODE = true;


	private boolean closed;

	// identity name and type, see:
	// http://xmpp.org/registrar/disco-categories.html
	public static final String XMPP_IDENTITY_NAME = "morfit";
	public static final String XMPP_IDENTITY_TYPE = "phone";

	// MTM is needed globally for both the backend (connect)
	// and the frontend (display dialog)
	//public MemorizingTrustManager mMTM;

	//private ChatConfiguration mConfig;
	@Override
	public void onCreate() {
		mInstance = this ;

		closed = false;
//		mMTM = new MemorizingTrustManager(this);
//		mConfig = new ChatConfiguration(PreferenceManager
// 				.getDefaultSharedPreferences(this));
 		super.onCreate();
		
//		initImageLoader(getApplicationContext());

//		ACRA.init(this);
//		
//		 // configure Flurry
//        FlurryAgent.setLogEnabled(false);
// 
        VolleyLog.setTag(getString(R.string.app_name));
        VolleyLog.DEBUG =false;
        
        // init Flurry
     //   FlurryAgent.init(this, Const.FLURRY_ANALYTICS_KEY);
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	//    public ImageLoader getImageLoader() {
	//        getRequestQueue();
	//        if (mImageLoader == null) {
	//            mImageLoader = new ImageLoader(this.mRequestQueue,
	//                    new LruBitmapCache(1024*3));
	//        }
	//        return this.mImageLoader;
	//    }


	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	public static AppController getApp(Context ctx) {
		return (AppController)ctx.getApplicationContext();
	}

	 
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		/*ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.diskCacheSize(100 * 1024 * 1024) // 50 Mb
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.diskCacheExtraOptions(480, 320, null)
		.threadPoolSize(2)
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);*/
	}

}
