package com.bfsi.egalite.service;

import org.json.JSONException;

public interface ServerCallback {
	  void doPostExecute(String serverResponse) throws JSONException;
	  void doPreExecute();
}
