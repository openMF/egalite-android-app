package com.bfsi.egalite.servicerequest;

import org.json.JSONException;

import android.os.AsyncTask;

import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.service.ServerCallback;
import com.bfsi.egalite.util.Constants;


public class AsyncOperation extends AsyncTask<String, Void, String>{

	ServerCallback serverCallBack;
	private String requestType,payload;
	
	public AsyncOperation(ServerCallback callback,String reqType,String payload) {
		this.serverCallBack=callback;
		this.requestType=reqType;
		this.payload = payload;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		serverCallBack.doPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String response = null;
		try {
			
			response = RequestManager.initiateRequest(params[0], requestType,
		            payload);
		} catch (ServiceException e) {
			if (e.getMessage().contains(Constants.HOSTNAME)) {
				 try {
					response = RequestManager.initiateRequest(params[0], requestType,
					            payload);
				} catch (ServiceException e1) {
					
					e1.printStackTrace();
				}
				
			}
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			serverCallBack.doPostExecute(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}