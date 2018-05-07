package pragma.embd.webservicecall;

import pragma.embd.constants.Constants;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CallingWebservice {
	
	public Context _context; 
	
	RequestParams params;
	
	public CallingWebservice(Context context){
		
		_context = context;
	}

	
	public void callWebservice(RequestParams params, String queryType,AsyncHttpResponseHandler handler ){
		
		String returnValue;
        
         // Make RESTful webservice call using AsyncHttpClient object
         AsyncHttpClient client = new AsyncHttpClient();
		
		client.get(Constants.ipAddress + queryType, params ,handler); 
		
	}
}
